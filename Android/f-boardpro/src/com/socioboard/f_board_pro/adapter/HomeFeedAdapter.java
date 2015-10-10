package com.socioboard.f_board_pro.adapter;
//adapter for setting home feed list

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.socioboard.f_board_pro.MainActivity;
import com.socioboard.f_board_pro.R;
import com.socioboard.f_board_pro.ShowPostDetails;
import com.socioboard.f_board_pro.database.util.MainSingleTon;
import com.socioboard.f_board_pro.imagelib.ImageLoader;
import com.socioboard.f_board_pro.models.HomeFeedModel;


public class HomeFeedAdapter extends BaseAdapter
{

	private Context context;
	private ArrayList<HomeFeedModel> feedList;
	ImageLoader imageLoader ;

	private int lastPosition = -1;

	public HomeFeedAdapter(Context context, ArrayList<HomeFeedModel> feedList) 
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
	public class ViewHolder
	{
		RelativeLayout entirView;

		TextView mTitle, mMessage,likes, mTime, mDescription, comments,share;
		ImageView mImage, profilepic;

	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent)
	{
		if (convertView == null)
		{
			LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = mInflater.inflate(R.layout.home_feed_item, parent, false);
		}

		final ViewHolder  holder=new ViewHolder();

		final HomeFeedModel rowItem = getItem(position);

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



		holder.entirView = (RelativeLayout) convertView.findViewById(R.id.entirView);

		holder.entirView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				MainSingleTon.selectedHomeFeed     = rowItem;

				MainSingleTon.selectedFeedForLikes = rowItem.getFeedId();

				MainSingleTon.selectedShareLink    = rowItem.getSharelink();

				Intent intent = new Intent(context, ShowPostDetails.class);

				context.startActivity(intent);	
			}
		});



		holder.mTitle=(TextView) convertView.findViewById(R.id.from);
		holder.mTitle.setText(rowItem.getFrom());

		holder.mMessage =(TextView) convertView.findViewById(R.id.message);
		holder.mMessage.setText(rowItem.getMessage());

		holder.mTime =(TextView) convertView.findViewById(R.id.datetime);
		holder.mTime.setText(rowItem.getDateTime());

		holder.mDescription =(TextView) convertView.findViewById(R.id.description);
		holder.mDescription.setText(rowItem.getDescription());

		holder.mImage=(ImageView) convertView.findViewById(R.id.image);
		holder.mImage.setBackgroundResource(R.drawable.photo);

		if(rowItem.getPicture()!=null)

			imageLoader.DisplayImage(rowItem.getPicture(), holder.mImage);
		else
			holder.mImage.setVisibility(View.VISIBLE);


		holder.profilepic=(ImageView) convertView.findViewById(R.id.profilepic);

		if(rowItem.getProfilePic()!=null)

			imageLoader.DisplayImage(rowItem.getProfilePic(), holder.profilepic);
		else
			holder.profilepic.setBackgroundResource(R.drawable.profile);

		holder.likes=(TextView) convertView.findViewById(R.id.likes);

		Animation animation = AnimationUtils.loadAnimation(context,
				(position > lastPosition) ? R.anim.up_from_bottom
						: R.anim.down_from_top);
		convertView.startAnimation(animation);
		lastPosition = position;


		if(rowItem.getLikes()>0)
		{
			holder.likes.setText("Likes "+rowItem.getLikes());
		}
		else
		{
			holder.likes.setText("Like");
		}

		holder.likes.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {


			}
		});


		holder.comments=(TextView) convertView.findViewById(R.id.comments);
		if(rowItem.getComments()>0)
		{
			holder.comments.setText("Comments "+rowItem.getComments());
		}
		else
		{
			holder.comments.setText("Comment");
		}

		holder.comments.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {


			}
		});

		holder.share=(TextView) convertView.findViewById(R.id.share);
		if(rowItem.getShares()>0)
		{
			holder.share.setText("Shares "+rowItem.getShares());
		}
		else
		{
			holder.share.setText("Share");
		}

		holder.share.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				MainSingleTon.selectedShareLink= rowItem.getSharelink();
				sharePost(holder.share);
			}
		});
		return convertView;
	}

	public void sharePost(final TextView shareTxtView)
	{
		System.out.println("INSIDE THERE SHARE");


		System.out.println("MainSingleTon.selectedShareLink="+MainSingleTon.selectedShareLink);
		System.out.println("MainSingleTon.accesstoken"+MainSingleTon.accesstoken);

		if(MainSingleTon.selectedShareLink!=null)
		{

			System.out.println("psotmsmasdlgkl response=");

			Bundle params = new Bundle();

			if( MainSingleTon.selectedShareLink.isEmpty())
			{

				/**Do nothing becz user just want to post text not an image*/
			}else
			{

				params.putString("link",  MainSingleTon.selectedShareLink); // image to post	
			}

			params.putString(AccessToken.ACCESS_TOKEN_KEY, MainSingleTon.accesstoken);

			new GraphRequest(MainSingleTon.dummyAccesstoken, "me/feed", params,

					HttpMethod.POST, new GraphRequest.Callback() {

				@Override
				public void onCompleted(GraphResponse response) {

					System.out.println("Scheduled response="+response.getJSONObject());

					if(response.getJSONObject()!=null)
					{
						if(response.getJSONObject().has("id"))
						{
							((Activity) context).runOnUiThread(new Runnable() {

								@Override
								public void run() {

									MainActivity.makeToast("Shared successfully!!");
									//shareTxtView.setTextColor(context.getResources().getColor(R.color.color_light_blue_700));
								}
							});
						}
					}else
					{
						MainActivity.makeToast("Sorry you cannot share this post!!");
					}

				}
			}).executeAsync();


			Bundle params1 = new Bundle();

			params1.putString(AccessToken.ACCESS_TOKEN_KEY, MainSingleTon.accesstoken);

			new GraphRequest(MainSingleTon.dummyAccesstoken, "me/interests",
					params1,
					HttpMethod.GET,
					new GraphRequest.Callback() {
				public void onCompleted(GraphResponse response) {

					System.out.println("Hey i am here="+response.getJSONObject());


				}
			}
					).executeAsync();

		}
		else
		{
			MainActivity.makeToast("Sorry didn't recognize post, try again");
		}


	}



}
