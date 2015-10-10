 package com.socioboard.f_board_pro.fragments;


import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.socioboard.f_board_pro.R;
import com.socioboard.f_board_pro.adapter.HomeFeedAdapter;
import com.socioboard.f_board_pro.database.util.JSONParseraa;
import com.socioboard.f_board_pro.database.util.MainSingleTon;
import com.socioboard.f_board_pro.database.util.Utilsss;
import com.socioboard.f_board_pro.models.DetermineUserLike;
import com.socioboard.f_board_pro.models.HomeFeedModel;


public class MyFeeds_Fragment extends Fragment   implements OnScrollListener
{
	View rootView;
	ListView myFeedListView;
	// list with the data to show in the listview
	private ArrayList<HomeFeedModel> mListItems;

	TextView nohomefeeds;
	ProgressBar progressbar;

	HomeFeedAdapter feedAdapter;
	ViewGroup viewGroup;
	String cursor = null;
	boolean isAlredyScrolloing = true;

	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onCreateView(android.view.LayoutInflater, android.view.ViewGroup, android.os.Bundle)
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState)
	{

		rootView    = inflater.inflate(R.layout.myfeed_layout, container, false);

		progressbar = (ProgressBar) rootView.findViewById(R.id.progressBar1);

		myFeedListView = (ListView) rootView.findViewById(R.id.myFeedListView);

		progressbar.setVisibility(View.VISIBLE);

		nohomefeeds =(TextView) rootView.findViewById(R.id.no_feeds);

		nohomefeeds.setVisibility(View.INVISIBLE);

		myFeedListView.setVisibility(View.INVISIBLE);

		mListItems = new ArrayList<HomeFeedModel>();

		myFeedListView.setOnScrollListener(MyFeeds_Fragment.this);

		addFooterView();

		new GetFeeds().execute();

		/*myFeedListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				MainSingleTon.selectedHomeFeed     = mListItems.get(position);
				
				MainSingleTon.selectedFeedForLikes = mListItems.get(position).getFeedId();
				
				MainSingleTon.selectedShareLink    = mListItems.get(position).getSharelink();

				Intent intent = new Intent(getActivity(), ShowPostDetails.class);
				
				startActivity(intent);		
			}
		});*/


		return rootView;

	}

	private void addFooterView() {

		LayoutInflater inflater = getActivity().getLayoutInflater();

		viewGroup = (ViewGroup) inflater.inflate(R.layout.progress_layout, myFeedListView, false);

		myFeedListView.addFooterView(viewGroup);

		viewGroup.setVisibility(View.INVISIBLE);

	}

	private void loadFromTHisCursor(String cursor) {

		new LoadMoreSearchPeopleAys().execute(cursor);

	}

	private class LoadMoreSearchPeopleAys extends AsyncTask<String, Void, Void>
	{
		String type =null;

		@Override
		protected Void doInBackground(String... params)
		{

			String hitNextUrl = params[0];

			System.out.println("HITTTTTTuyrlk ="+hitNextUrl);

			JSONParseraa jsonParser = new JSONParseraa();

			JSONObject jsonObject = jsonParser.getJSONFromUrl(hitNextUrl);

			try {

				JSONArray jsonArray =  jsonObject.getJSONArray("data");

				if(jsonArray.length()!=0)
				{
					for(int i = 0; i<jsonArray.length();i++)
					{
						HomeFeedModel feedModel = new HomeFeedModel();
						DetermineUserLike userLikes= new DetermineUserLike();
						JSONObject jsonObject2 = jsonArray.getJSONObject(i);
						if(jsonObject2.has("id"))
						{
							feedModel.setFeedId(jsonObject2.getString("id"));
							userLikes.setFeedId(jsonObject2.getString("id"));
						}
						if(jsonObject2.has("likes"))
						{
							JSONObject jsonObjectlikes = jsonObject2.getJSONObject("likes");
							JSONArray jsonArraylikes =  jsonObjectlikes.getJSONArray("data");
							feedModel.setLikes(jsonArraylikes.length());
							for (int j = 0; j < jsonArraylikes.length(); j++)
							{
								JSONObject jsonObjectlike = jsonArraylikes.getJSONObject(j);
								if(jsonObjectlike.has("id"))
								{
									String id=jsonObjectlike.getString("id");
									if(id.equalsIgnoreCase(MainSingleTon.userid))
									{
										userLikes.setLike(true);
									}
									else
									{
										userLikes.setLike(false);
									}
								}
								else
								{
								}
							}
						}
						else
						{
							feedModel.setLikes(0);
							userLikes.setLike(false);
						}
						if(jsonObject2.has("comments"))
						{
							JSONObject jsonObjectcomments = jsonObject2.getJSONObject("comments");
							JSONArray jsonArraycomments =  jsonObjectcomments.getJSONArray("data");
							feedModel.setComments(jsonArraycomments.length());
						}else
						{
							feedModel.setComments(0);
						}
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
								if(jsonObject2.has("id"))
								{
									feedModel.setFeedId(jsonObject2.getString("id"));

								}
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
								else if(jsonObject2.has("name"))
								{
									feedModel.setMessage(jsonObject2.getString("name"));
								}

							}
							if(type.equalsIgnoreCase("status"))
							{

								if(jsonObject2.has("id"))
								{
									feedModel.setFeedId(jsonObject2.getString("id"));

								}
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
								else if(jsonObject2.has("name"))
								{
									feedModel.setMessage(jsonObject2.getString("name"));
								}


							}
							if(type.equalsIgnoreCase("photo"))
							{

								if(jsonObject2.has("id"))
								{
									feedModel.setFeedId(jsonObject2.getString("id"));

								}
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
								else if(jsonObject2.has("name"))
								{
									feedModel.setMessage(jsonObject2.getString("name"));
								}
							}
							if(type.equalsIgnoreCase("video"))
							{

								if(jsonObject2.has("id"))
								{
									feedModel.setFeedId(jsonObject2.getString("id"));

								}
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
								else if(jsonObject2.has("name"))
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
							mListItems.add(feedModel);
							MainSingleTon.userLikedFeedList.add(userLikes);

						}else
						{

						}
					}


					if(jsonObject.has("paging"))
					{
						JSONObject js56 =  jsonObject.getJSONObject("paging");

						cursor = js56.getString("next");
					}else
					{
						cursor= null;
					}
				}else
				{
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
		protected void onPostExecute(Void result)
		{

			feedAdapter.notifyDataSetChanged();

			isAlredyScrolloing = false;

			viewGroup.setVisibility(View.INVISIBLE);
		}

	}

	/*get home feeds*/
	public class GetFeeds extends AsyncTask<Void, Void, String>
	{
		String userFBiD =null;
		String userFBaccesToken = null;
		String type = null;

		@Override
		protected String doInBackground(Void... params) 
		{
			userFBaccesToken = MainSingleTon.accesstoken;
			userFBiD = MainSingleTon.userid;
			MainSingleTon.userLikedFeedList.clear();

			String tokenURL = "https://graph.facebook.com/"+userFBiD+"/feed?access_token="+userFBaccesToken;

			System.out.println("URL my feeds : "+tokenURL);

			JSONParseraa jsonParser = new JSONParseraa();

			JSONObject jsonObject = jsonParser.getJSONFromUrl(tokenURL);

			try {

				JSONArray jsonArray =  jsonObject.getJSONArray("data");

				if(jsonArray.length()!=0)
				{
					for(int i = 0; i<jsonArray.length();i++)
					{
						HomeFeedModel feedModel = new HomeFeedModel();
						DetermineUserLike userLikes= new DetermineUserLike();
						JSONObject jsonObject2 = jsonArray.getJSONObject(i);
						if(jsonObject2.has("id"))
						{
							feedModel.setFeedId(jsonObject2.getString("id"));
							userLikes.setFeedId(jsonObject2.getString("id"));
						}
						if(jsonObject2.has("likes"))
						{
							JSONObject jsonObjectlikes = jsonObject2.getJSONObject("likes");
							JSONArray jsonArraylikes =  jsonObjectlikes.getJSONArray("data");
							feedModel.setLikes(jsonArraylikes.length());
							for (int j = 0; j < jsonArraylikes.length(); j++)
							{
								JSONObject jsonObjectlike = jsonArraylikes.getJSONObject(j);
								if(jsonObjectlike.has("id"))
								{
									String id=jsonObjectlike.getString("id");
									if(id.equalsIgnoreCase(MainSingleTon.userid))
									{
										userLikes.setLike(true);
									}
									else
									{
										userLikes.setLike(false);
									}
								}
								else
								{
								}
							}
						}
						else
						{
							feedModel.setLikes(0);
							userLikes.setLike(false);
						}
						if(jsonObject2.has("comments"))
						{
							JSONObject jsonObjectcomments = jsonObject2.getJSONObject("comments");
							JSONArray jsonArraycomments =  jsonObjectcomments.getJSONArray("data");
							feedModel.setComments(jsonArraycomments.length());
						}else
						{
							feedModel.setComments(0);
						}
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
								if(jsonObject2.has("id"))
								{
									feedModel.setFeedId(jsonObject2.getString("id"));

								}
								if(jsonObject2.has("description"))
								{
									feedModel.setDescription(jsonObject2.getString("description"));
								}

								if(jsonObject2.has("created_time"))
								{
								feedModel.setDateTime(Utilsss.GetLocalDateStringFromUTCString(jsonObject2.getString("created_time")));
								}else
								{
									
								}
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
								else if(jsonObject2.has("name"))
								{
									feedModel.setMessage(jsonObject2.getString("name"));
								}



							}
							if(type.equalsIgnoreCase("status"))
							{

								if(jsonObject2.has("id"))
								{
									feedModel.setFeedId(jsonObject2.getString("id"));

								}
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
								else if(jsonObject2.has("name"))
								{
									feedModel.setMessage(jsonObject2.getString("name"));
								}


							}
							if(type.equalsIgnoreCase("photo"))
							{

								if(jsonObject2.has("id"))
								{
									feedModel.setFeedId(jsonObject2.getString("id"));

								}
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
								else if(jsonObject2.has("name"))
								{
									feedModel.setMessage(jsonObject2.getString("name"));
								}
							}
							if(type.equalsIgnoreCase("video"))
							{

								if(jsonObject2.has("id"))
								{
									feedModel.setFeedId(jsonObject2.getString("id"));

								}
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
								else if(jsonObject2.has("name"))
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
							
							mListItems.add(feedModel);
							MainSingleTon.userLikedFeedList.add(userLikes);

						}else
						{

						}
					}


					if(jsonObject.has("paging"))
					{
						JSONObject js56 =  jsonObject.getJSONObject("paging");

						cursor = js56.getString("next");
					}else
					{
						cursor= null;
					}
				}else
				{
					cursor= null;
				}


			} catch (JSONException e)
			{

				e.printStackTrace();
			}

			return null;
		}

		@Override
		protected void onPostExecute(String result)
		{
			if(mListItems.size()>0)
			{
				feedAdapter = new HomeFeedAdapter(getActivity(), mListItems);
				myFeedListView.setAdapter(feedAdapter);
				progressbar.setVisibility(View.INVISIBLE);
				myFeedListView.setVisibility(View.VISIBLE);
				isAlredyScrolloing = false;
			}
			else
			{
				progressbar.setVisibility(View.INVISIBLE);
				myFeedListView.setVisibility(View.INVISIBLE);
				nohomefeeds.setVisibility(View.VISIBLE);
				cursor =null;
				isAlredyScrolloing = true;

			}
			super.onPostExecute(result);
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

				viewGroup.setVisibility(View.VISIBLE);

				isAlredyScrolloing = true;

				if (cursor == null) {
					
					viewGroup.setVisibility(View.GONE);

				} else {
					
					loadFromTHisCursor(cursor);
				}

			}

		} 
	}
}
