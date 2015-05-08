package com.socioboard.f_board_pro.fragments;


import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.socioboard.f_board_pro.MainActivity;
import com.socioboard.f_board_pro.R;
import com.socioboard.f_board_pro.adapter.HomeFeedAdapter;
import com.socioboard.f_board_pro.database.util.JSONParseraa;
import com.socioboard.f_board_pro.database.util.Utilsss;
import com.socioboard.f_board_pro.database.util.LoadMoreListView.OnLoadMoreListener;
import com.socioboard.f_board_pro.database.util.MainSingleTon;
import com.socioboard.f_board_pro.models.DetermineUserLike;
import com.socioboard.f_board_pro.models.HomeFeedModel;
import com.socioboard.f_board_pro.database.util.LoadMoreListView;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


public class GetUserFeeds extends ListFragment  
{
	View rootView;
	ListView lv;
	// list with the data to show in the listview
	private ArrayList<HomeFeedModel> mListItems;
	
	TextView nohomefeeds;
	ProgressBar progressbar;
	 
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState)
	{

		rootView = inflater.inflate(R.layout.feed_list_fragment, container, false);

		return rootView;

	}
	private class HomeFeedClickListener implements ListView.OnItemClickListener
	{
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) 
		{
			displayFeed(position);
		}

		private void displayFeed(int position)
		{

			MainSingleTon.selectedHomeFeed = MainSingleTon.feedArrayList.get(position);
			MainSingleTon.selectedFeedForLikes=MainSingleTon.feedArrayList.get(position).getFeedId();
			Fragment fragment = new DisplayHomeFeedFragment();
			MainActivity.swipeFragment(fragment);
		}
	}
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState)
	{

		progressbar=(ProgressBar) rootView.findViewById(R.id.progressBar1);

		progressbar.setVisibility(View.VISIBLE);


		nohomefeeds=(TextView) rootView.findViewById(R.id.no_feeds);
		nohomefeeds.setText("No feeds");
		nohomefeeds.setVisibility(View.INVISIBLE);
		new GetFeeds().execute();


		lv = getListView();
		lv.setOnItemClickListener(new HomeFeedClickListener()); 
		lv.setVisibility(View.INVISIBLE);
		mListItems = new ArrayList<HomeFeedModel>();
		mListItems.clear();




		((LoadMoreListView) getListView()).setOnLoadMoreListener(new OnLoadMoreListener() 
		{
			public void onLoadMore()
			{
				// Do the work to load more items at the end of list
				// here
				new LoadDataTask().execute();
			}
		});
	}
	/*paging for home feeds*/
	private class LoadDataTask extends AsyncTask<Void, Void, Void>
	{

		String type =null;
		@Override
		protected Void doInBackground(Void... params)
		{



			JSONParseraa jsonParser = new JSONParseraa();

			JSONObject jsonObject = jsonParser.getJSONFromUrl(MainSingleTon.NextUrl);


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
						if(jsonObject2.has("likes"))
						{
							JSONObject jsonObjectlikes = jsonObject2.getJSONObject("likes");
							JSONArray jsonArraylikes =  jsonObjectlikes.getJSONArray("data");
							feedModel.setLikes(jsonArraylikes.length());
							for (int j = 0; j < jsonArraylikes.length(); j++) 
							{
								
							}
						}else
						{
							System.out.println("no likes");
							feedModel.setLikes(0);
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
							MainSingleTon.feedArrayList.add(feedModel);

						}else
						{
						}
					}
				}
				else
				{ 
					getActivity().runOnUiThread(new Runnable() {

						@Override
						public void run() {

							Toast.makeText(getActivity(), "-----!!End!!-----", Toast.LENGTH_SHORT).show();
						}
					});


				}


				if(jsonObject.has("paging"))
				{
					JSONObject js56 =  jsonObject.getJSONObject("paging");

					MainSingleTon.NextUrl = js56.getString("next");
				}
			} 
			catch (JSONException e) 
			{

				e.printStackTrace();
			}


			if (isCancelled()) 
			{
				return null;
			}

			// Simulates a background task
			try
			{

				Thread.sleep(1000);
			} 
			catch (InterruptedException e) 
			{

			}

			mListItems.addAll(MainSingleTon.feedArrayList);

			///++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
			return null;
		}

		@Override
		protected void onPostExecute(Void result)
		{

			// We need notify the adapter that the data have been changed
			((BaseAdapter) getListAdapter()).notifyDataSetChanged();

			// Call onLoadMoreComplete when the LoadMore task, has finished
			((LoadMoreListView) getListView()).onLoadMoreComplete();
			super.onPostExecute(result);
		}

		@Override
		protected void onCancelled()
		{
			((LoadMoreListView) getListView()).onLoadMoreComplete();
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
			MainSingleTon.feedArrayList.clear();
			MainSingleTon.userLikedFeedList.clear();

			String tokenURL = "https://graph.facebook.com/"+userFBiD+"/feed?access_token="+userFBaccesToken;
			
			
			
			System.out.println("url my feeds : "+tokenURL);
			
			
			JSONParseraa jsonParser = new JSONParseraa();
			

			JSONObject jsonObject = jsonParser.getJSONFromUrl(tokenURL);
		

			try {


				JSONArray jsonArray =  jsonObject.getJSONArray("data");
			
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
						MainSingleTon.feedArrayList.add(feedModel);
						MainSingleTon.userLikedFeedList.add(userLikes);

					}else
					{

					}
				}


				if(jsonObject.has("paging"))
				{
					JSONObject js56 =  jsonObject.getJSONObject("paging");


					MainSingleTon.NextUrl = js56.getString("next");
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
			mListItems.addAll(MainSingleTon.feedArrayList);
			if(mListItems.size()>0)
			{
				HomeFeedAdapter feedAdapter = new HomeFeedAdapter(getActivity(), mListItems);
				setListAdapter(feedAdapter);
				progressbar.setVisibility(View.INVISIBLE);
				lv.setVisibility(View.VISIBLE);
			}
			else
			{
				progressbar.setVisibility(View.INVISIBLE);
				lv.setVisibility(View.INVISIBLE);
				nohomefeeds.setVisibility(View.VISIBLE);
			}
			super.onPostExecute(result);
		}


	}
}
