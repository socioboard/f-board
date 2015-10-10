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

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.socioboard.f_board_pro.adapter.AllinOneSearchFeedAdapter;
import com.socioboard.f_board_pro.database.util.JSONParseraa;
import com.socioboard.f_board_pro.database.util.MainSingleTon;
import com.socioboard.f_board_pro.database.util.Utilsss;
import com.socioboard.f_board_pro.imagelib.ImageLoader;
import com.socioboard.f_board_pro.models.HomeFeedModel;
import com.socioboard.f_board_pro.viewlibary.AccountImageView;

public class AllInOneSearchFeeds extends Activity  implements OnScrollListener{

	View feedheaderLayout;
	ImageView cover_pic, nofeeds;
	TextView username,user_email_id;
	AccountImageView profile_pic;
	ListView feedlistview;
	ImageLoader imageLoader ;
	ArrayList<HomeFeedModel> arrayList;
	ProgressBar progressBar;
	boolean isAlredyScrolloing = true;
	String cursor = null;
	ImageView backImage;
	ViewGroup viewGroup;
	
	RelativeLayout headerRlt;

	AllinOneSearchFeedAdapter allinOneSearchFeedAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.allinone_feedlayout);

		feedheaderLayout     = getLayoutInflater().inflate(R.layout.allinonefeed_header, null, false);
		cover_pic            = (ImageView) feedheaderLayout.findViewById(R.id.cover_pic);
		profile_pic          = (AccountImageView) feedheaderLayout.findViewById(R.id.profile_pic);
		user_email_id        = (TextView) feedheaderLayout.findViewById(R.id.user_email_id);
		username             = (TextView) feedheaderLayout.findViewById(R.id.username);
		imageLoader          = new ImageLoader(getApplicationContext()); 
		arrayList            = new ArrayList<HomeFeedModel>();
		
		headerRlt = (RelativeLayout) findViewById(R.id.headerRlt);

		user_email_id.setText(MainSingleTon.pgCategory);
		username.setText(MainSingleTon.pgNAME);

		progressBar          = (ProgressBar)  findViewById(R.id.progressBar1);
		backImage            = (ImageView) findViewById(R.id.backImage);
		nofeeds              = (ImageView) findViewById(R.id.nofeeds);
		feedlistview         = (ListView) findViewById(R.id.feedlistview);
		feedlistview.setOnScrollListener(AllInOneSearchFeeds.this);

		imageLoader.DisplayImage("https://graph.facebook.com/"+MainSingleTon.pgID+"/picture?type=large", profile_pic);

		feedlistview.addHeaderView(feedheaderLayout);

		addFooterView();

		new GeCoverpic().execute();

		progressBar.setVisibility(View.VISIBLE);

		new GetFeeds().execute();

		backImage.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				onBackPressed();

			}
		});
		
		headerRlt.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				onBackPressed();

				
			}
		});
		
		/*feedlistview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				if(position!=0)
				{
					MainSingleTon.selectedHomeFeed = arrayList.get(position-1);
					MainSingleTon.selectedFeedForLikes=arrayList.get(position-1).getFeedId();
					MainSingleTon.selectedShareLink=arrayList.get(position-1).getSharelink();

					System.out.println("MainSingleTon.selectedShareLink ="+MainSingleTon.selectedShareLink);
					Intent intent = new Intent(AllInOneSearchFeeds.this, ShowPostDetails.class);

					startActivity(intent);
				}				

			}
		});
*/
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();

		AllInOneSearchFeeds.this.finish();
	}

	private void addFooterView() {

		LayoutInflater inflater =  getLayoutInflater();

		viewGroup = (ViewGroup) inflater.inflate(R.layout.progress_layout, feedlistview, false);

		feedlistview.addFooterView(viewGroup);

		viewGroup.setVisibility(View.INVISIBLE);

	}

	public class GeCoverpic extends AsyncTask<Void, Void, String>
	{

		String ConverULR =null;
		@Override
		protected String doInBackground(Void... params) {

			String coverUrl = "https://graph.facebook.com/" + MainSingleTon.pgID+ "?fields=cover&access_token=" + MainSingleTon.accesstoken;

			JSONParseraa jsonParser = new JSONParseraa();

			JSONObject jsonCover = jsonParser.getJSONFromUrl(coverUrl);

			System.out.println("coverrrrrrrrr " + jsonCover);

			try
			{
				if (jsonCover.has("cover"))
				{
					System.out.println("cover url "+ jsonCover.getString("cover"));

					JSONObject jsonObject2 = jsonCover.getJSONObject("cover");

					if (jsonObject2.has("source"))
					{
						System.out.println("sourse  "+ jsonObject2.getString("source"));

						ConverULR=jsonObject2.getString("source");
					}

				}

			}catch(Exception e)
			{
				e.printStackTrace();
			}
			return ConverULR;
		}
		@Override
		protected void onPostExecute(String result) {

			super.onPostExecute(result);

			if(ConverULR!=null)
			{
				imageLoader.DisplayImage(ConverULR, cover_pic);
				allinOneSearchFeedAdapter = new AllinOneSearchFeedAdapter(AllInOneSearchFeeds.this, arrayList);
				feedlistview.setAdapter(allinOneSearchFeedAdapter);
			}
		}

	}

	public class GetFeeds extends AsyncTask<Void, Void, String>
	{
		String type =null;
		@Override
		protected String doInBackground(Void... params) {

			String hitURL = "https://graph.facebook.com/"+MainSingleTon.pgID+"/feed?limit=50&access_token="+MainSingleTon.accesstoken;

			System.out.println("HITTTTTTMEEEEEFEED"+hitURL);
			
			JSONParseraa jsonParser = new JSONParseraa();

	    	JSONObject jsonObject = jsonParser.getJSONFromUrl(hitURL);

			try {
				JSONArray jsonArray =  jsonObject.getJSONArray("data");

				if(jsonArray.length()!=0)
				{
					for(int i = 0; i<jsonArray.length();i++)
					{
						HomeFeedModel feedModel = new HomeFeedModel();
						final JSONObject jsonObject2 = jsonArray.getJSONObject(i);
						if(jsonObject2.has("id"))
						{
							feedModel.setFeedId(jsonObject2.getString("id"));
						}
	
					feedModel.setLikescount(null);
					feedModel.setCommentscount(null);
 						
						if(jsonObject2.has("shares"))
						{
							JSONObject jsonObjectshare = jsonObject2.getJSONObject("shares");
							System.out.println("jsonObjectshare  "+jsonObjectshare);
							if(jsonObjectshare.has("count"))
							{
								feedModel.setShares(jsonObjectshare.getInt("count"));
								System.out.println("sharessss  "+jsonObjectshare.getInt("count"));
							}
							else
							{
								feedModel.setShares(0);
							}
						}else
						{
							feedModel.setShares(0);
						}
						if(jsonObject2.has("type"))
						{
							type = jsonObject2.getString("type");

							if(type.equalsIgnoreCase("link"))
							{

								if(jsonObject2.has("description"))
								{
									feedModel.setDescription(jsonObject2.getString("description"));
								}

								feedModel.setDateTime(Utilsss.GetLocalDateStringFromUTCString(jsonObject2.getString("created_time")));
								if(jsonObject2.has("picture"))
								{
									feedModel.setPicture(jsonObject2.getString("picture"));
								}
								if(jsonObject2.has("from"))
								{
									JSONObject jsonObject3 = jsonObject2.getJSONObject("from");
									if(jsonObject3.has("name"))
									{
										feedModel.setFrom(jsonObject3.getString("name"));
									}
									if(jsonObject3.has("id"))
									{
										feedModel.setFromID(jsonObject3.getString("id"));
									}
									feedModel.setProfilePic("https://graph.facebook.com/"+jsonObject3.getString("id")+"/picture?type=small");

								}
								if(jsonObject2.has("message"))
								{
									feedModel.setMessage(jsonObject2.getString("message"));
								}
								else
								{
									feedModel.setMessage(jsonObject2.getString("name"));
								}


							}
							if(type.equalsIgnoreCase("status"))
							{

								if(jsonObject2.has("description"))
								{
									feedModel.setDescription(jsonObject2.getString("description"));
								}
								else if(jsonObject2.has("story"))
								{
									feedModel.setDescription(jsonObject2.getString("story"));
								}

								feedModel.setDateTime(Utilsss.GetLocalDateStringFromUTCString(jsonObject2.getString("created_time")));
								if(jsonObject2.has("picture"))
								{
									feedModel.setPicture(jsonObject2.getString("picture"));
								}
								if(jsonObject2.has("from"))
								{
									JSONObject jsonObject3 = jsonObject2.getJSONObject("from");
									if(jsonObject3.has("name"))
									{
										feedModel.setFrom(jsonObject3.getString("name"));
									}
									if(jsonObject3.has("id"))
									{
										feedModel.setFromID(jsonObject3.getString("id"));
									}
									feedModel.setProfilePic("https://graph.facebook.com/"+jsonObject3.getString("id")+"/picture?type=small");

								}
								if(jsonObject2.has("message"))
								{
									feedModel.setMessage(jsonObject2.getString("message"));
								}
								else
								{
									feedModel.setMessage(jsonObject2.getString("name"));
								}


							}
							if(type.equalsIgnoreCase("photo"))
							{

								if(jsonObject2.has("description"))
								{
									feedModel.setDescription(jsonObject2.getString("description"));
								}
								else if(jsonObject2.has("story"))
								{
									feedModel.setDescription(jsonObject2.getString("story"));
								}

								feedModel.setDateTime(Utilsss.GetLocalDateStringFromUTCString(jsonObject2.getString("created_time")));
								if(jsonObject2.has("picture"))
								{
									feedModel.setPicture(jsonObject2.getString("picture"));
								}
								if(jsonObject2.has("from"))
								{
									JSONObject jsonObject3 = jsonObject2.getJSONObject("from");
									if(jsonObject3.has("name"))
									{
										feedModel.setFrom(jsonObject3.getString("name"));
									}
									if(jsonObject3.has("id"))
									{
										feedModel.setFromID(jsonObject3.getString("id"));
									}
									feedModel.setProfilePic("https://graph.facebook.com/"+jsonObject3.getString("id")+"/picture?type=small");

								}
								if(jsonObject2.has("message"))
								{
									feedModel.setMessage(jsonObject2.getString("message"));
								}
								else
								{
									if(jsonObject2.has("name"))
										feedModel.setMessage(jsonObject2.getString("name"));
								}
							}
							if(type.equalsIgnoreCase("video"))
							{
								System.out.println("ItsSATTUS in homefeeds");


								if(jsonObject2.has("description"))
								{
									feedModel.setDescription(jsonObject2.getString("description"));
								}
								else if(jsonObject2.has("story"))
								{
									feedModel.setDescription(jsonObject2.getString("story"));
								}

								feedModel.setDateTime(Utilsss.GetLocalDateStringFromUTCString(jsonObject2.getString("created_time")));
								if(jsonObject2.has("picture"))
								{
									feedModel.setPicture(jsonObject2.getString("picture"));
								}
								if(jsonObject2.has("from"))
								{
									JSONObject jsonObject3 = jsonObject2.getJSONObject("from");
									if(jsonObject3.has("name"))
									{
										feedModel.setFrom(jsonObject3.getString("name"));
									}
									if(jsonObject3.has("id"))
									{
										feedModel.setFromID(jsonObject3.getString("id"));
									}
									feedModel.setProfilePic("https://graph.facebook.com/"+jsonObject3.getString("id")+"/picture?type=small");

								}
								if(jsonObject2.has("message"))
								{
									feedModel.setMessage(jsonObject2.getString("message"));
								}
								else
								{
									feedModel.setMessage(jsonObject2.getString("name"));
								}

								
							}
							
							if(jsonObject2.has("actions"))
							{
								try {
									JSONArray jsonArray2 = jsonObject2.getJSONArray("actions");
									
									feedModel.setSharelink(jsonArray2.getJSONObject(2).getString("link"));
									
								} catch (Exception e) {
									 
									e.printStackTrace();
								}
							}else
							{

							}
							arrayList.add(feedModel);

						}else
						{
						}
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

				}
				else
				{ 
					/**NO DATA**/
					System.out.println("EEEEEEEEEENNNNNNNNNNNNNNDDDDDDDDDDDD");

				}

			} 
			catch (JSONException e) 
			{
				e.printStackTrace();
			}
			return null;
		}
		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);

			allinOneSearchFeedAdapter = new AllinOneSearchFeedAdapter(AllInOneSearchFeeds.this, arrayList);
			feedlistview.setAdapter(allinOneSearchFeedAdapter);
			progressBar.setVisibility(View.GONE);
			isAlredyScrolloing =false;

			if (arrayList.isEmpty()) {
				isAlredyScrolloing =true;
				feedlistview.setVisibility(View.INVISIBLE);
				viewGroup.setVisibility(View.GONE);
				nofeeds.setVisibility(View.VISIBLE);
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
		protected String doInBackground(String... params) {


			JSONParseraa jsonParser = new JSONParseraa();

			JSONObject jsonObject = jsonParser.getJSONFromUrl(cursor);

			try {

				JSONArray jsonArray =  jsonObject.getJSONArray("data");

				if(jsonArray.length()!=0)
				{
					for(int i = 0; i<jsonArray.length();i++)
					{
						HomeFeedModel feedModel = new HomeFeedModel();
						JSONObject jsonObject2 = jsonArray.getJSONObject(i);
						if(jsonObject2.has("id"))
						{
							feedModel.setFeedId(jsonObject2.getString("id"));
						}
						//++++++++++++++++++++++++++++++++++++++++++++
						
					/*	String hitURL11 = "https://graph.facebook.com/"+jsonObject2.getString("id")+"?fields=id,likes.summary(true),comments.summary(true)&access_token="+MainSingleTon.accesstoken;

						JSONObject jsonObject11 = jsonParser.getJSONFromUrl(hitURL11);

						//System.out.println("---------setDetails------------------inside SsSSSessearch= "+jsonObject);

						JSONObject jsonLikeObject2;
						try {

							jsonLikeObject2 = jsonObject11.getJSONObject("likes");

							if(jsonLikeObject2.has("summary"))
							{
								int total_count = jsonLikeObject2.getJSONObject("summary").getInt("total_count");

								if(total_count>0)
								{
									String num = "";
									DecimalFormat formatter = new DecimalFormat("#,###,###");

									num = 	formatter.format(total_count);

									feedModel.setLikescount(num+" Likes");
								}else
								{
									feedModel.setLikescount(" Likes");
								}

							}else
							{
								//no 
							}

						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

						
						try {

							JSONObject jsonObject3 =  jsonObject11.getJSONObject("comments");
							
							if(jsonObject3.has("summary"))
							{
								int total_count = jsonObject3.getJSONObject("summary").getInt("total_count");

								if(total_count>0)
								{
									String num = "";
									
									DecimalFormat formatter = new DecimalFormat("#,###,###");

									num = 	formatter.format(total_count);

									feedModel.setCommentscount(num+" comments");
								}else
								{
									feedModel.setCommentscount(" comment");
								}

							}else
							{
								//no 
							}

						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}*/
						
						feedModel.setLikescount(null);
						feedModel.setCommentscount(null);
						
						if(jsonObject2.has("shares"))
						{
							JSONObject jsonObjectshare = jsonObject2.getJSONObject("shares");
							System.out.println("jsonObjectshare  "+jsonObjectshare);
							if(jsonObjectshare.has("count"))
							{
								feedModel.setShares(jsonObjectshare.getInt("count"));
								System.out.println("sharessss  "+jsonObjectshare.getInt("count"));
							}
							else
							{
								feedModel.setShares(0);
							}
						}else
						{
							feedModel.setShares(0);
						}
						if(jsonObject2.has("type"))
						{
							type = jsonObject2.getString("type");

							if(type.equalsIgnoreCase("link"))
							{

								if(jsonObject2.has("description"))
								{
									feedModel.setDescription(jsonObject2.getString("description"));
								}

								feedModel.setDateTime(Utilsss.GetLocalDateStringFromUTCString(jsonObject2.getString("created_time")));
								if(jsonObject2.has("picture"))
								{
									feedModel.setPicture(jsonObject2.getString("picture"));
								}
								if(jsonObject2.has("from"))
								{
									JSONObject jsonObject3 = jsonObject2.getJSONObject("from");
									if(jsonObject3.has("name"))
									{
										feedModel.setFrom(jsonObject3.getString("name"));
									}
									if(jsonObject3.has("id"))
									{
										feedModel.setFromID(jsonObject3.getString("id"));
									}
									feedModel.setProfilePic("https://graph.facebook.com/"+jsonObject3.getString("id")+"/picture?type=small");

								}
								if(jsonObject2.has("message"))
								{
									feedModel.setMessage(jsonObject2.getString("message"));
								}
								else
								{
									feedModel.setMessage(jsonObject2.getString("name"));
								}



							}
							if(type.equalsIgnoreCase("status"))
							{

								if(jsonObject2.has("description"))
								{
									feedModel.setDescription(jsonObject2.getString("description"));
								}
								else if(jsonObject2.has("story"))
								{
									feedModel.setDescription(jsonObject2.getString("story"));
								}

								feedModel.setDateTime(Utilsss.GetLocalDateStringFromUTCString(jsonObject2.getString("created_time")));
								if(jsonObject2.has("picture"))
								{
									feedModel.setPicture(jsonObject2.getString("picture"));
								}
								if(jsonObject2.has("from"))
								{
									JSONObject jsonObject3 = jsonObject2.getJSONObject("from");
									if(jsonObject3.has("name"))
									{
										feedModel.setFrom(jsonObject3.getString("name"));
									}
									if(jsonObject3.has("id"))
									{
										feedModel.setFromID(jsonObject3.getString("id"));
									}
									feedModel.setProfilePic("https://graph.facebook.com/"+jsonObject3.getString("id")+"/picture?type=small");

								}
								if(jsonObject2.has("message"))
								{
									feedModel.setMessage(jsonObject2.getString("message"));
								}
								else
								{
									feedModel.setMessage(jsonObject2.getString("name"));
								}


							}
							if(type.equalsIgnoreCase("photo"))
							{

								if(jsonObject2.has("description"))
								{
									feedModel.setDescription(jsonObject2.getString("description"));
								}
								else if(jsonObject2.has("story"))
								{
									feedModel.setDescription(jsonObject2.getString("story"));
								}

								feedModel.setDateTime(Utilsss.GetLocalDateStringFromUTCString(jsonObject2.getString("created_time")));
								if(jsonObject2.has("picture"))
								{
									feedModel.setPicture(jsonObject2.getString("picture"));
								}
								if(jsonObject2.has("from"))
								{
									JSONObject jsonObject3 = jsonObject2.getJSONObject("from");
									if(jsonObject3.has("name"))
									{
										feedModel.setFrom(jsonObject3.getString("name"));
									}
									if(jsonObject3.has("id"))
									{
										feedModel.setFromID(jsonObject3.getString("id"));
									}
									feedModel.setProfilePic("https://graph.facebook.com/"+jsonObject3.getString("id")+"/picture?type=small");

								}
								if(jsonObject2.has("message"))
								{
									feedModel.setMessage(jsonObject2.getString("message"));
								}
								else
								{
									if(jsonObject2.has("name"))
										feedModel.setMessage(jsonObject2.getString("name"));
								}
							}
							if(type.equalsIgnoreCase("video"))
							{
								System.out.println("ItsSATTUS in homefeeds");


								if(jsonObject2.has("description"))
								{
									feedModel.setDescription(jsonObject2.getString("description"));
								}
								else if(jsonObject2.has("story"))
								{
									feedModel.setDescription(jsonObject2.getString("story"));
								}

								feedModel.setDateTime(Utilsss.GetLocalDateStringFromUTCString(jsonObject2.getString("created_time")));
								if(jsonObject2.has("picture"))
								{
									feedModel.setPicture(jsonObject2.getString("picture"));
								}
								if(jsonObject2.has("from"))
								{
									JSONObject jsonObject3 = jsonObject2.getJSONObject("from");
									if(jsonObject3.has("name"))
									{
										feedModel.setFrom(jsonObject3.getString("name"));
									}
									if(jsonObject3.has("id"))
									{
										feedModel.setFromID(jsonObject3.getString("id"));
									}
									feedModel.setProfilePic("https://graph.facebook.com/"+jsonObject3.getString("id")+"/picture?type=small");

								}
								if(jsonObject2.has("message"))
								{
									feedModel.setMessage(jsonObject2.getString("message"));
								}
								else
								{
									feedModel.setMessage(jsonObject2.getString("name"));
								}
								
							}

							if(jsonObject2.has("actions"))
							{
								try {
									JSONArray jsonArray2 = jsonObject2.getJSONArray("actions");
									
									feedModel.setSharelink(jsonArray2.getJSONObject(2).getString("link"));
									
								} catch (Exception e) {
									 
									e.printStackTrace();
								}
							}else
							{

							}
							
							arrayList.add(feedModel);

						}else
						{
							
						}
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

				}
				else
				{ 
					/**NO DATA**/
					System.out.println("EEEEEEEEEENNNNNNNNNNNNNNDDDDDDDDDDDD");
					cursor= null;
				}

			} 
			catch (JSONException e) 
			{
				e.printStackTrace();
			}
			return null;
		}
		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			allinOneSearchFeedAdapter.notifyDataSetChanged();

			isAlredyScrolloing = false;

			viewGroup.setVisibility(View.INVISIBLE);
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

			String URL = "https://graph.facebook.com/"+ MainSingleTon.pgID+ "/likes";

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
				e.printStackTrace();

			} catch (IOException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
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
				/* setLike();
				   checkLike();*/
			}
			else
			{
				Toast.makeText(getApplicationContext(), "try later", Toast.LENGTH_SHORT).show();
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

			String URL = "https://graph.facebook.com/"+ MainSingleTon.pgID+ "/likes";

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
				Toast.makeText(getApplicationContext(), "you unlike this", Toast.LENGTH_SHORT).show();
				/* setUnlike();
				   checkLike();*/

			}
			else
			{
				Toast.makeText(getApplicationContext(), "please try after some time", Toast.LENGTH_SHORT).show();
			}
		}
	}

}