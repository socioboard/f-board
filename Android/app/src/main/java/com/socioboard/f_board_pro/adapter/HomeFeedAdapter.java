package com.socioboard.f_board_pro.adapter;
//adapter for setting home feed list

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

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
import com.squareup.picasso.Picasso;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.facebook.FacebookSdk.getApplicationContext;


public class HomeFeedAdapter extends BaseAdapter
{

	private Context context;
	private ArrayList<HomeFeedModel> feedList;
	ImageLoader imageLoader ;
	String URL;
	public static HttpResponse response=null;
	String cheackResponseStatus = "null";
	EditText writcomment;
	RelativeLayout postet;

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
		//holder.mImage.setBackgroundResource(R.drawable.photo);

		if(rowItem.getPicture()!=null)
		{
			//imageLoader.DisplayImage(rowItem.getPicture(), holder.mImage);
			Picasso.with(context).load(rowItem.getPicture()).into(holder.mImage);
		}
		else
		{
			Picasso.with(context).load(R.drawable.default_image).resize(200,100).into(holder.mImage);
			holder.mImage.setVisibility(View.VISIBLE);
		}

		holder.profilepic=(ImageView) convertView.findViewById(R.id.profilepic);

		if(rowItem.getProfilePic()!=null)

			//imageLoader.DisplayImage(rowItem.getProfilePic(), holder.profilepic);
		Picasso.with(context).load(rowItem.getProfilePic()).into(holder.profilepic);
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

				Toast.makeText(getApplicationContext(),"try latter",Toast.LENGTH_SHORT).show();

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
				//Write comment
				showEditBox();

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

	public void showEditBox()
	{

		final Dialog dialog;

		dialog = new Dialog(HomeFeedAdapter.this.context);

		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

		dialog.setContentView(R.layout.show_editbox);

		//Make transpernt background dialog

		writcomment = (EditText) dialog.findViewById(R.id.writemycommnet);
		postet       = (RelativeLayout) dialog.findViewById(R.id.post);


		dialog.setCancelable(true);

		writcomment.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				writcomment.requestFocus();

			}
		});
		postet.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{

				final String comment  = writcomment.getText().toString();

				if(comment.length()>0)
				{
					new CallToFbComment().execute();

				}
				else
				{
					((Activity)context).runOnUiThread(new Runnable()
					{

						@Override
						public void run()
						{

							Toast.makeText(getApplicationContext(), "Please enter your comment...!", Toast.LENGTH_SHORT).show();
						}
					});

				}

				dialog.dismiss();
			}
		});

		dialog.show();
	}

	public class CallToFbComment extends AsyncTask<String, Void, String>
	{
		String comment=null;
		CallToFbComment()
		{
			comment=writcomment.getText().toString();
		}
		HttpResponse response;

		@Override
		protected String doInBackground(String... params)
		{


			HttpClient httpclient = new DefaultHttpClient();


			String URL = "https://graph.facebook.com/"+MainSingleTon.selectedHomeFeed.getFeedId() +"/comments";


			HttpPost httppost = new HttpPost(URL);


			try
			{
				// Add your data
				List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
				nameValuePairs.add(new BasicNameValuePair("access_token",MainSingleTon.accesstoken));
				nameValuePairs.add(new BasicNameValuePair("message",comment));
				httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

				// Execute HTTP Post Request
				response = httpclient.execute(httppost);



			}
			catch (ClientProtocolException e)
			{
				// TODO Auto-generated catch block
			}
			catch (IOException e)
			{
				// TODO Auto-generated catch block
			}
			return null;
		}

		@Override
		protected void onPostExecute(String result)
		{
			super.onPostExecute(result);

			if (response.getStatusLine().toString().equals("HTTP/1.1 200 OK"))
			{
				Toast.makeText(getApplicationContext(), "Comment success", Toast.LENGTH_SHORT).show();
			}
			else
			{
				Toast.makeText(getApplicationContext(), "Comment failed", Toast.LENGTH_SHORT).show();
			}

		}
	}


}
