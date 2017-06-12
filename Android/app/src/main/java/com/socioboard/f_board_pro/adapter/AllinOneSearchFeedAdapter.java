package com.socioboard.f_board_pro.adapter;
//adapter for setting home feed list

import java.text.DecimalFormat;
import java.util.ArrayList;

import javax.crypto.spec.PSource;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.socioboard.f_board_pro.AllInOneSearchFeeds;
import com.socioboard.f_board_pro.MainActivity;
import com.socioboard.f_board_pro.R;
import com.socioboard.f_board_pro.ShowPostDetails;
import com.socioboard.f_board_pro.database.util.JSONParseraa;
import com.socioboard.f_board_pro.database.util.MainSingleTon;
import com.socioboard.f_board_pro.imagelib.ImageLoader;
import com.socioboard.f_board_pro.models.HomeFeedModel;
import com.squareup.picasso.Picasso;


public class AllinOneSearchFeedAdapter extends BaseAdapter
{
	private Handler handler = new Handler();
	private Context context;
	private ArrayList<HomeFeedModel> feedList;
	ImageLoader imageLoader ;

	public AllinOneSearchFeedAdapter(Context context, ArrayList<HomeFeedModel> feedList) 
	{
		this.context = context;
		this.feedList = feedList;

		imageLoader = new ImageLoader(context);
	}
	@Override
	public int getCount()
	{
		return feedList.size();
	}

	@Override
	public HomeFeedModel getItem(int position)
	{
		return feedList.get(position);
	}

	@Override
	public long getItemId(int position)
	{
		return feedList.indexOf(getItem(position));
	}
	@Override
	public View getView(final int position, View convertView, ViewGroup parent)
	{
		if (convertView == null)
		{
			LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = mInflater.inflate(R.layout.allinone_feed_item, parent, false);
		}

		final HomeFeedModel rowItem = getItem(position);
		
		RelativeLayout convertview_Rlt = (RelativeLayout) convertView.findViewById(R.id.convertview_Rlt);
		
		ImageView droptoshare = (ImageView) convertView.findViewById(R.id.droptoshare);
		
		droptoshare.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {

				if(MainSingleTon.shareLinks.contains(rowItem.getSharelink()))
				{
					MainActivity.makeToast("Already exist!!");
					
				}else
				{
					MainSingleTon.shareLinks.add(rowItem.getSharelink());
					MainActivity.makeToast("Added total share links "+	MainSingleTon.shareLinks.size());
				}
			}
		});
		
		convertview_Rlt.setOnClickListener(new OnClickListener() {
			

			@Override
			public void onClick(View v) {


				MainSingleTon.selectedHomeFeed = getItem(position);
				MainSingleTon.selectedFeedForLikes=getItem(position).getFeedId();
				MainSingleTon.selectedShareLink=getItem(position).getSharelink();

				System.out.println("MainSingleTon.selectedShareLink ="+MainSingleTon.selectedShareLink);
				Intent intent = new Intent(context, ShowPostDetails.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				context.startActivity(intent);
			}
		});


		TextView mTitle=(TextView) convertView.findViewById(R.id.from);
		mTitle.setText(rowItem.getFrom());

		TextView mMessage=(TextView) convertView.findViewById(R.id.message);
		mMessage.setText(rowItem.getMessage());

		TextView mTime=(TextView) convertView.findViewById(R.id.datetime);
		mTime.setText(rowItem.getDateTime());

		TextView mDescription=(TextView) convertView.findViewById(R.id.description);
		mDescription.setText(rowItem.getDescription());

		ImageView mImage=(ImageView) convertView.findViewById(R.id.image);
		mImage.setBackgroundResource(R.drawable.photo);


		if(rowItem.getPicture()!=null)
			//imageLoader.DisplayImage(rowItem.getPicture(), mImage);
		Picasso.with(context).load(rowItem.getPicture()).into(mImage);


		else
			mImage.setVisibility(View.GONE);
		ImageView profilepic=(ImageView) convertView.findViewById(R.id.profilepic);

		if(rowItem.getProfilePic()!=null)
			//setImageFromUrl(profilepic, position, rowItem.getProfilePic());
			//imageLoader.DisplayImage(rowItem.getProfilePic(), profilepic);
		Picasso.with(context).load(rowItem.getProfilePic()).into(profilepic);
		else
			profilepic.setBackgroundResource(R.drawable.profile);

		TextView likes=(TextView) convertView.findViewById(R.id.likes);
		TextView comments=(TextView) convertView.findViewById(R.id.comments);
		if(rowItem.getLikescount()!=null)
		{
			likes.setText(rowItem.getLikescount());
		}
		else
		{
			//likes.setText("Like");
			setDetails(rowItem.getFeedId(), likes, comments, position,rowItem);
		}


		if(rowItem.getCommentscount()!=null)
		{
			comments.setText(rowItem.getCommentscount());
		}
		else
		{
			//comments.setText("Comment");
			setDetails(rowItem.getFeedId(), likes, comments, position,rowItem);
		}


		TextView share=(TextView) convertView.findViewById(R.id.share);
		if(rowItem.getShares()>0)
		{
			share.setText("Shares "+rowItem.getShares());
		}
		else
		{
			share.setText("Share");

		}

		return convertView;
	}

	public void setDetails(final String pageID, final TextView likecounts, final TextView commentscount,final int postion, final HomeFeedModel rowItem)
	{

		new Thread(new Runnable() {

			@Override
			public void run() {

				String hitURL = "https://graph.facebook.com/"+pageID+"?fields=id,likes.summary(true),comments.summary(true)&access_token="+MainSingleTon.accesstoken;

				JSONParseraa jsonParser = new JSONParseraa();

				JSONObject jsonObject = jsonParser.getJSONFromUrl(hitURL);

				//System.out.println("---------setDetails------------------inside SsSSSessearch= "+jsonObject);

				JSONObject jsonLikeObject2;
				try {

					jsonLikeObject2 = jsonObject.getJSONObject("likes");

					if(jsonLikeObject2.has("summary"))
					{
						int total_count = jsonLikeObject2.getJSONObject("summary").getInt("total_count");

						if(total_count>0)
						{
							String num = "";
							DecimalFormat formatter = new DecimalFormat("#,###,###");

							num = 	formatter.format(total_count);

							likecounts.setText(num+" Likes");
							rowItem.setLikescount(num+" Likes");
							notifyDataSetChanged();

						}else
						{
							//likecounts.setText(" Likes");
						}

					}else
					{

					}

				} catch (Exception e) {
					
					e.printStackTrace();
				}


				try {

					JSONObject jsonObject3 =  jsonObject.getJSONObject("comments");

					if(jsonObject3.has("summary"))
					{
						int total_count = jsonObject3.getJSONObject("summary").getInt("total_count");

						if(total_count>0)
						{
							String num = "";

							DecimalFormat formatter = new DecimalFormat("#,###,###");

							num = 	formatter.format(total_count);

							commentscount.setText(num+" comments");
							rowItem.setCommentscount(num+" comments");
							notifyDataSetChanged();
							
						}else
						{
							//commentscount.setText(" comments");
						}

					}else
					{
						//no 
					}

				} catch (Exception e) {
					
					e.printStackTrace();
					
				}



			}
		}).start();


	}
}
