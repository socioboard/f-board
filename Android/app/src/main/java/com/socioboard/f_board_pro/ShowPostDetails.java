/**
 * 
 */
package com.socioboard.f_board_pro;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.socioboard.f_board_pro.adapter.CommentAdapter;
import com.socioboard.f_board_pro.database.util.JSONParseraa;
import com.socioboard.f_board_pro.database.util.MainSingleTon;
import com.socioboard.f_board_pro.database.util.Utilsss;
import com.socioboard.f_board_pro.models.CommentModel;
import com.socioboard.f_board_pro.su.ArcMenu;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * @author Suresh-Maidaragi
 *
 */
public class ShowPostDetails extends Activity implements OnScrollListener{


	TextView peopleLikedYou,noComments;
	ArrayList<CommentModel> commentList;
	ProgressBar progressbar;
	EditText userComment;
	ImageButton enter;
	ImageView like;
	boolean likestatus=false;
	ListView listview;
	EditText	writcomment;RelativeLayout postet;
	RelativeLayout rlt;
	boolean isAlredyScrolloing = true;
	String cursor = null;
	ViewGroup viewGroup;
	CommentAdapter commentAdapter;

	private static final int[] ITEM_DRAWABLES = { R.drawable.write_cmt, R.drawable.like_btn, R.drawable.share_btn };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy);

		setContentView(R.layout.show_postdetails);

		userComment = (EditText) findViewById(R.id.usercomment);
		enter       = (ImageButton) findViewById(R.id.enter);
		listview    = (ListView) findViewById(R.id.list);
		rlt         = (RelativeLayout) findViewById(R.id.rlt);

		ArcMenu arcMenu2 = (ArcMenu) findViewById(R.id.arc_menu_2);
		initArcMenu(arcMenu2, ITEM_DRAWABLES);

		addFooterView();

		progressbar =(ProgressBar) findViewById(R.id.progressBar1);

		progressbar.setVisibility(View.VISIBLE);
		noComments  = (TextView) findViewById(R.id.no_comments);
		noComments.setText("No Comments");
		noComments.setVisibility(View.INVISIBLE);

		listview.setVisibility(View.INVISIBLE);

		like = (ImageView) findViewById(R.id.like);

		like.setOnClickListener(new OnClickListener() 
		{

			@Override
			public void onClick(View v)
			{

				finish();
			}
		});
		userComment.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v) 
			{
				userComment.requestFocus();

			}
		});
		enter.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v) 
			{

				final String comment  = userComment.getText().toString();

				if(comment.length()>0)
				{
					new CallToFbComment().execute();

				}
				else
				{

					runOnUiThread(new Runnable() 
					{

						@Override
						public void run() 
						{
							Toast.makeText(getApplicationContext(), "Please enter your comment...!", Toast.LENGTH_SHORT).show();
						}
					});

				}

			}
		});

		peopleLikedYou=(TextView) findViewById(R.id.people_liked_you);

		peopleLikedYou.setOnClickListener(new OnClickListener() 
		{

			@Override
			public void onClick(View v) 
			{

				Intent intent = new Intent(ShowPostDetails.this, ShowPostLikes.class);

				startActivity(intent);

			}
		});
		
		rlt.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				
				peopleLikedYou.callOnClick();
			}
		});

		commentList  = new ArrayList<CommentModel>();

		checkLike();

		new GetHomeFeed().execute();

		listview.setOnScrollListener(ShowPostDetails.this);
	}

	private void addFooterView() {

		LayoutInflater inflater =  getLayoutInflater();

		viewGroup = (ViewGroup) inflater.inflate(R.layout.progress_layout, listview, false);

		listview.addFooterView(viewGroup);

		viewGroup.setVisibility(View.INVISIBLE);

	}

	public void showEditBox()
	{

		final Dialog dialog;

		dialog = new Dialog(ShowPostDetails.this);

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
					runOnUiThread(new Runnable() 
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

	private void initArcMenu(ArcMenu menu, int[] itemDrawables) {

		final int itemCount = itemDrawables.length;
		for (int i = 0; i < itemCount; i++) {

			ImageView item = new ImageView(this);

			item.setImageResource(itemDrawables[i]);

			final int position = i;

			menu.addItem(item, new OnClickListener() {

				@Override
				public void onClick(View v) {

					if(position==0)
					{
						//Write comment
						showEditBox();
					}
					if(position==1)
					{
						//Like this
						if(likestatus)//Undo()    new CallToFbUnLike().execute(); 
							new CallToFbUnLike().execute(); 
						else
							new CallToFbLike().execute();
					}
					if(position==2)
					{
						//share this

						sharePost();
					}
				}
			});
		}
	}

	public void sharePost()
	{
		System.out.println("INSIDE THERE SHARE");

		System.out.println("MainSingleTon.selectedShareLink="+MainSingleTon.selectedShareLink);
		System.out.println("MainSingleTon.accesstoken"+MainSingleTon.accesstoken);

		if(MainSingleTon.selectedShareLink!=null)
		{
			postImageonWall(MainSingleTon.selectedShareLink, MainSingleTon.accesstoken);
		}
		else
		{
			MainActivity.makeToast("Sorry didn't recognize post, try again");
		}

	}

	public void Undo() 
	{
		Bundle params = new Bundle();

		params.putString(AccessToken.ACCESS_TOKEN_KEY, MainSingleTon.accesstoken);

		new GraphRequest(MainSingleTon.dummyAccesstoken, MainSingleTon.selectedGroupFeed+"/likes", params, HttpMethod.GET, new GraphRequest.Callback()
		{

			@Override
			public void onCompleted(GraphResponse response) 
			{
				System.out.println("Responce in new delete"+response);


			}
		}).executeAsync();


	}

	public void  checkLike() 
	{
		if(MainSingleTon.selectedHomeFeed.getLikes()>0)
		{
			peopleLikedYou.setText(MainSingleTon.selectedHomeFeed.getLikes()+" people like this");
			
			//like.setBackgroundResource(R.drawable.feedlike_icon);
			
			for (int i = 0; i < MainSingleTon.userLikedFeedList.size(); i++) 
			{
				if(MainSingleTon.userLikedFeedList.get(i).getFeedId().equalsIgnoreCase(MainSingleTon.selectedHomeFeed.getFeedId()))
				{
					if(MainSingleTon.userLikedFeedList.get(i).isLike())
					{
						//like.setBackgroundResource(R.drawable.feedliked_icon);

						likestatus = true;

						if(MainSingleTon.selectedHomeFeed.getLikes()==1)
						{
							peopleLikedYou.setText("You like this ");
						}
						else if(MainSingleTon.selectedHomeFeed.getLikes()==2)
						{
							peopleLikedYou.setText("You and other "+(MainSingleTon.selectedHomeFeed.getLikes()-1)+" person like this");
						}
						else
						{
							peopleLikedYou.setText("You and other "+(MainSingleTon.selectedHomeFeed.getLikes()-1)+" people like this");
						}
					}
					else
					{
						likestatus=false;
						peopleLikedYou.setText(""+MainSingleTon.selectedHomeFeed.getLikes()+" people like this");
					}
				}
			}
		}
		else
		{
			//like.setBackgroundResource(R.drawable.feedlike_icon);
			peopleLikedYou.setText("Be the first to like this");

		}
	}

	public void setUnlike() 
	{
		if(MainSingleTon.selectedHomeFeed.getLikes()>0)
		{
			//like.setBackgroundResource(R.drawable.feedlike_icon);
			for (int i = 0; i < MainSingleTon.userLikedFeedList.size(); i++) 
			{
				if(MainSingleTon.userLikedFeedList.get(i).getFeedId().equalsIgnoreCase(MainSingleTon.selectedHomeFeed.getFeedId()))
				{
					MainSingleTon.selectedHomeFeed.setLikes(MainSingleTon.selectedHomeFeed.getLikes()-1);
					MainSingleTon.userLikedFeedList.get(i).setLike(false);
					likestatus=false;
				}
			}
		}		
	}

	public void setLike()
	{
		//like.setBackgroundResource(R.drawable.feedliked_icon);
		for (int i = 0; i < MainSingleTon.userLikedFeedList.size(); i++) 
		{
			if(MainSingleTon.userLikedFeedList.get(i).getFeedId().equalsIgnoreCase(MainSingleTon.selectedHomeFeed.getFeedId()))
			{

				MainSingleTon.selectedHomeFeed.setLikes(MainSingleTon.selectedHomeFeed.getLikes()+1);
				MainSingleTon.userLikedFeedList.get(i).setLike(true);
				likestatus=true;
			}
		}

	}

	public class CallToFbUnLike extends AsyncTask<String, Void, String>
	{

		HttpResponse response;

		@Override
		protected String doInBackground(String... params)
		{

			HttpClient httpclient = new DefaultHttpClient();

			String URL = "https://graph.facebook.com/"+ MainSingleTon.selectedHomeFeed.getFeedId()+ "/likes";

			HttpPost httppost = new HttpPost(URL);

			try 
			{
				// Add your data
				List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
				nameValuePairs.add(new BasicNameValuePair("access_token",MainSingleTon.accesstoken));
				nameValuePairs.add(new BasicNameValuePair("method","DELETE"));
				httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

				// Execute HTTP Post Request
				response = httpclient.execute(httppost);

				System.out.println("response unlke......"+response.getStatusLine());

			} 
			catch (ClientProtocolException e)
			{

			} catch (IOException e)
			{
				// TODO Auto-generated catch block
			}
			return null;
		}

		@Override
		protected void onPostExecute(String result)
		{
			// TODO Auto-generated method stub
			super.onPostExecute(result);

			if (response.getStatusLine().toString().equals("HTTP/1.1 200 OK"))
			{
				Toast.makeText(getApplicationContext(), "you unlike this", Toast.LENGTH_SHORT).show();
				setUnlike();
				checkLike();

			}
			else
			{
				Toast.makeText(getApplicationContext(), "please try after some time", Toast.LENGTH_SHORT).show();
			}
		}
	}

	public class GetHomeFeed extends AsyncTask<Void, Void, String>
	{
		String homeFeedId =null;
		String userFBaccesToken = null;
		String type = null;

		@Override
		protected String doInBackground(Void... params) 
		{

			userFBaccesToken = MainSingleTon.accesstoken;
			homeFeedId       = MainSingleTon.selectedHomeFeed.getFeedId();
			commentList.clear();

			String tokenURL = "https://graph.facebook.com/"+homeFeedId+"/comments?access_token="+userFBaccesToken;

			System.out.println("tokenURL="+tokenURL);

			JSONParseraa jsonParser = new JSONParseraa();

			JSONObject jsonObject = jsonParser.getJSONFromUrl(tokenURL);

			try {

				JSONArray jsonArray =  jsonObject.getJSONArray("data");

				if(jsonArray.length()!=0)
				{
					for(int i = 0; i<jsonArray.length();i++)
					{
						CommentModel commentModel=new CommentModel();
						JSONObject jsonObject2 = jsonArray.getJSONObject(i);

						if(jsonObject2.has("from"))
						{

							JSONObject jsonObject3 = jsonObject2.getJSONObject("from");
							if(jsonObject3.has("name"))
							{
								System.out.println("name "+jsonObject3.getString("name"));
								commentModel.setName(jsonObject3.getString("name"));								
							}

							if(jsonObject3.has("id"))
							{
								String id = null;
								commentModel.setFromID(id=jsonObject3.getString("id"));	

								commentModel.setProfilePic("https://graph.facebook.com/"+id+"/picture?type=small");

							}
						}
						if(jsonObject2.has("message"))
						{
							commentModel.setComment(jsonObject2.getString("message"));	
						}
						if(jsonObject2.has("created_time"))
						{
							commentModel.setDateTime(Utilsss.GetLocalDateStringFromUTCString(jsonObject2.getString("created_time")));	
						}
						commentList.add(commentModel);

					}
					if(jsonObject.has("paging"))
					{
						JSONObject jsonObject2 = jsonObject.getJSONObject("paging");
						if(jsonObject2.has("next"))
						{
							cursor	=	jsonObject2.getString("next");
						}else
						{
							cursor= null;
						}
					}else
					{
						cursor= null;
					}
				}else
				{
					//No data
					cursor= null;
				}
			}
			catch (JSONException e) 
			{

				e.printStackTrace();
			}

			System.out.println("----------------------------------------------");
			return null;
		}
		@Override
		protected void onPostExecute(String result)
		{

			super.onPostExecute(result);
			isAlredyScrolloing =false;
			if(commentList.size()>0)
			{
				commentAdapter = new CommentAdapter(getApplicationContext(), commentList);
				listview.setAdapter(commentAdapter);
				commentAdapter.notifyDataSetChanged();
				progressbar.setVisibility(View.INVISIBLE);
				listview.setVisibility(View.VISIBLE);
				noComments.setVisibility(View.INVISIBLE);

			}
			else
			{
				progressbar.setVisibility(View.INVISIBLE);
				listview.setVisibility(View.INVISIBLE);
				noComments.setVisibility(View.VISIBLE);
				isAlredyScrolloing =true;
				viewGroup.setVisibility(View.GONE);
			}

		}

	}
	/*class to like a post*/
	public class CallToFbLike extends AsyncTask<String, Void, String>
	{

		HttpResponse response;

		@Override
		protected String doInBackground(String... params)
		{


			HttpClient httpclient = new DefaultHttpClient();

			String URL = "https://graph.facebook.com/"+ MainSingleTon.selectedHomeFeed.getFeedId()+ "/likes";

			HttpPost httppost = new HttpPost(URL);

			try 
			{
				// Add your data
				List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
				nameValuePairs.add(new BasicNameValuePair("access_token",MainSingleTon.accesstoken));

				httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

				// Execute HTTP Post Request
				response = httpclient.execute(httppost);
				System.out.println("response unlke......"+response.getStatusLine());	

			} 
			catch (ClientProtocolException e)
			{
				// TODO Auto-generated catch block
			} catch (IOException e)
			{
				// TODO Auto-generated catch block
			}
			return null;
		}

		@Override
		protected void onPostExecute(String result)
		{
			// TODO Auto-generated method stub
			super.onPostExecute(result);

			if (response.getStatusLine().toString().equals("HTTP/1.1 200 OK"))
			{
				Toast.makeText(getApplicationContext(), "you like this", Toast.LENGTH_SHORT).show();
				setLike();
				checkLike();
			}
			else
			{
				Toast.makeText(getApplicationContext(), "try later", Toast.LENGTH_SHORT).show();
			}
		}
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
				userComment.clearFocus();
				userComment.setText("");

				new GetHomeFeed().execute();
				progressbar.setVisibility(View.VISIBLE);
				listview.setVisibility(View.INVISIBLE);
			}
			else
			{
				Toast.makeText(getApplicationContext(), "Comment failed", Toast.LENGTH_SHORT).show();
			}

		}
	}


	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		// TODO Auto-generated method stub

	}


	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {


		boolean loadMore = firstVisibleItem + visibleItemCount >= totalItemCount;

		if (loadMore) {

			if (isAlredyScrolloing) {

				// do nothing

			} else {

				// bottom progress visible

				viewGroup.setVisibility(View.VISIBLE);

				isAlredyScrolloing = true;

				if(cursor==null)
				{
					viewGroup.setVisibility(View.GONE);
				}else
				{
					loadFromTHisCursor(cursor);
				}

			}

		} else {

		}




	}

	private void loadFromTHisCursor(String cursor) {

		new LoadMoreFeedssAys().execute(cursor);

	}

	public class LoadMoreFeedssAys extends AsyncTask<String, Void, String>
	{

		String type = null;

		@Override
		protected String doInBackground(String... params) 
		{

			String tokenURL = params[0];

			System.out.println("CURSOR URL OR LOADMORE ITEM URL="+tokenURL);

			JSONParseraa jsonParser = new JSONParseraa();

			JSONObject jsonObject = jsonParser.getJSONFromUrl(tokenURL);

			try {

				JSONArray jsonArray =  jsonObject.getJSONArray("data");

				if(jsonArray.length()!=0)
				{
					for(int i = 0; i<jsonArray.length();i++)
					{
						CommentModel commentModel=new CommentModel();
						JSONObject jsonObject2 = jsonArray.getJSONObject(i);

						if(jsonObject2.has("from"))
						{

							JSONObject jsonObject3 = jsonObject2.getJSONObject("from");
							if(jsonObject3.has("name"))
							{
								System.out.println("name "+jsonObject3.getString("name"));
								commentModel.setName(jsonObject3.getString("name"));								
							}

							if(jsonObject3.has("id"))
							{
								String id = null;
								commentModel.setFromID(id=jsonObject3.getString("id"));	

								commentModel.setProfilePic("https://graph.facebook.com/"+id+"/picture?type=small");


							}
						}
						if(jsonObject2.has("message"))
						{
							commentModel.setComment(jsonObject2.getString("message"));	
						}
						if(jsonObject2.has("created_time"))
						{
							commentModel.setDateTime(Utilsss.GetLocalDateStringFromUTCString(jsonObject2.getString("created_time")));	
						}
						commentList.add(commentModel);

					}
					if(jsonObject.has("paging"))
					{
						JSONObject jsonObject2 = jsonObject.getJSONObject("paging");
						if(jsonObject2.has("next"))
						{
							cursor	=	jsonObject2.getString("next");
						}else
						{
							cursor= null;
						}
					}else
					{
						cursor= null;
					}
				}else
				{
					//No data
					cursor= null;
				}
			}
			catch (JSONException e) 
			{

				e.printStackTrace();
			}

			System.out.println("----------------------------------------------");
			return null;
		}
		@Override
		protected void onPostExecute(String result)
		{
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			commentAdapter.notifyDataSetChanged();

			isAlredyScrolloing = false;

			viewGroup.setVisibility(View.INVISIBLE);

		}

	}

	private void postImageonWall(String postLink, String MYAccessToken) {

		Bundle params = new Bundle();

		if(postLink.isEmpty())
		{
         
	  		/**Do nothing becz user just want to post text not an image*/
		 	
		}else
		{
         
			params.putString("link", postLink); // image to post	
		  	
		}

		params.putString(AccessToken.ACCESS_TOKEN_KEY, MYAccessToken);
		
		System.out.println(" MainSingleTon.dummyAccesstoken= "+MainSingleTon.dummyAccesstoken);

		new GraphRequest(MainSingleTon.dummyAccesstoken, "me/feed", params,

				HttpMethod.POST, new GraphRequest.Callback() {

			@Override
			public void onCompleted(GraphResponse response) {

				System.out.println("Scheduled response="+response.getJSONObject());

				if(response.getJSONObject()!=null)
				{
					if(response.getJSONObject().has("id"))
					{
						runOnUiThread(new Runnable() {

							@Override
							public void run() {

								MainActivity.makeToast("Shared successfully!!");
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

		params1.putString(AccessToken.ACCESS_TOKEN_KEY, MYAccessToken);

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

}
