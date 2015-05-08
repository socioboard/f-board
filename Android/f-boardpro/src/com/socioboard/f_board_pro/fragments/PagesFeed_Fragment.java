package com.socioboard.f_board_pro.fragments;


import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.socioboard.f_board_pro.R;
import com.socioboard.f_board_pro.adapter.PagesFeed_Adpater;
import com.socioboard.f_board_pro.database.util.JSONParseraa;
import com.socioboard.f_board_pro.database.util.LoadMoreListView;
import com.socioboard.f_board_pro.database.util.Utilsss;
import com.socioboard.f_board_pro.database.util.LoadMoreListView.OnLoadMoreListener;
import com.socioboard.f_board_pro.database.util.MainSingleTon;
import com.socioboard.f_board_pro.models.GroupFeedModel;

public class PagesFeed_Fragment extends ListFragment
{
	View rootview;
	private ArrayList<GroupFeedModel> groupFeedList;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {

		rootview =inflater.inflate(R.layout.pages_feed_fragment, container, false);

		return rootview;
	}
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {

		groupFeedList = new ArrayList<GroupFeedModel>();
		new GetGroupFeeds().execute();
		((LoadMoreListView) getListView()).setOnLoadMoreListener(new OnLoadMoreListener() 
		{
			public void onLoadMore()
			{
				// Do the work to load more items at the end of list
				// here
				//new LoadDataTask().execute();
			}
		});

	}
	public class GetGroupFeeds extends AsyncTask<Void, Void, String>
	{
		String pageFBiD =null;
		String userFBaccesToken = null;
		String type = null;


		@Override
		protected String doInBackground(Void... params) 
		{

			userFBaccesToken = MainSingleTon.accesstoken;
			pageFBiD = MainSingleTon.pageFBiD;
			groupFeedList.clear();

			String tokenURL = "https://graph.facebook.com/"+pageFBiD+"/feed?access_token="+userFBaccesToken;
			System.out.println("hit it.... "+tokenURL);
			JSONParseraa jsonParser = new JSONParseraa();

			JSONObject jsonObject = jsonParser.getJSONFromUrl(tokenURL);

			try {
				JSONArray jsonArray =  jsonObject.getJSONArray("data");

				for(int i = 0; i<jsonArray.length();i++)
				{
					GroupFeedModel groupFeedModel = new GroupFeedModel();
					JSONObject jsonObject2 = jsonArray.getJSONObject(i);
					if(jsonObject2.has("id"))
					{
						groupFeedModel.setFeedID(jsonObject2.getString("id"));
					}
					if(jsonObject2.has("likes"))
					{
						JSONObject jsonObjectlikes = jsonObject2.getJSONObject("likes");
						JSONArray jsonArraylikes =  jsonObjectlikes.getJSONArray("data");
						groupFeedModel.setLikes(jsonArraylikes.length());
					}else
					{
						groupFeedModel.setLikes(0);
					}
					if(jsonObject2.has("comments"))
					{
						JSONObject jsonObjectcomments = jsonObject2.getJSONObject("comments");
						JSONArray jsonArraycomments =  jsonObjectcomments.getJSONArray("data");
						groupFeedModel.setComments(jsonArraycomments.length());
					}else
					{
						groupFeedModel.setComments(0);
					}
					if(jsonObject2.has("type"))
					{
						type = jsonObject2.getString("type");

						if(type.equalsIgnoreCase("link"))
						{

							if(jsonObject2.has("message"))
							{
								groupFeedModel.setMessage(jsonObject2.getString("message"));
							}
							groupFeedModel.setDateTime(Utilsss.GetLocalDateStringFromUTCString(jsonObject2.getString("created_time")));

							if(jsonObject2.has("picture"))
							{
								groupFeedModel.setPicture(jsonObject2.getString("picture"));
							}
							if(jsonObject2.has("from"))
							{
								JSONObject jsonObject3 = jsonObject2.getJSONObject("from");
								if(jsonObject3.has("name"))
								{
									groupFeedModel.setFrom(jsonObject3.getString("name"));
								}
							}
							else
							{
								if(jsonObject2.has("name"))
								{
									groupFeedModel.setFrom(jsonObject2.getString("name"));
								}
							}

						}
						if(type.equalsIgnoreCase("status"))
						{

							if(jsonObject2.has("message"))
							{
								groupFeedModel.setMessage(jsonObject2.getString("message"));
							}
							groupFeedModel.setDateTime(Utilsss.GetLocalDateStringFromUTCString(jsonObject2.getString("created_time")));

							if(jsonObject2.has("picture"))
							{
								groupFeedModel.setPicture(jsonObject2.getString("picture"));
							}
							else
							{
								groupFeedModel.setPicture("https://www.facebook.com/images/icons/post.gif");
							}
							if(jsonObject2.has("from"))
							{
								JSONObject jsonObject3 = jsonObject2.getJSONObject("from");
								if(jsonObject3.has("name"))
								{
									groupFeedModel.setFrom(jsonObject3.getString("name"));
								}
							}
							else
							{
								if(jsonObject2.has("name"))
								{
									groupFeedModel.setFrom(jsonObject2.getString("name"));
								}
							}

						}
						if(type.equalsIgnoreCase("photos"))
						{

							if(jsonObject2.has("message"))
							{
								groupFeedModel.setMessage(jsonObject2.getString("message"));
							}
							groupFeedModel.setDateTime(Utilsss.GetLocalDateStringFromUTCString(jsonObject2.getString("created_time")));

							if(jsonObject2.has("picture"))
							{
								groupFeedModel.setPicture(jsonObject2.getString("picture"));
							}
							else
							{
								groupFeedModel.setPicture("https://www.facebook.com/images/icons/post.gif");
							}
							if(jsonObject2.has("from"))
							{
								JSONObject jsonObject3 = jsonObject2.getJSONObject("from");
								if(jsonObject3.has("name"))
								{
									groupFeedModel.setFrom(jsonObject3.getString("name"));
								}
							}
							else
							{
								if(jsonObject2.has("name"))
								{
									groupFeedModel.setFrom(jsonObject2.getString("name"));
								}
							}

						}
						if(type.equalsIgnoreCase("video"))
						{

							if(jsonObject2.has("message"))
							{
								groupFeedModel.setMessage(jsonObject2.getString("message"));
							}
							groupFeedModel.setDateTime(Utilsss.GetLocalDateStringFromUTCString(jsonObject2.getString("created_time")));

							if(jsonObject2.has("picture"))
							{
								groupFeedModel.setPicture(jsonObject2.getString("picture"));
							}
							else
							{
								groupFeedModel.setPicture("https://www.facebook.com/images/icons/post.gif");
							}
							if(jsonObject2.has("from"))
							{
								JSONObject jsonObject3 = jsonObject2.getJSONObject("from");
								if(jsonObject3.has("name"))
								{
									groupFeedModel.setFrom(jsonObject3.getString("name"));
								}
							}
							else
							{
								if(jsonObject2.has("name"))
								{
									groupFeedModel.setFrom(jsonObject2.getString("name"));
								}
							}

						}
						groupFeedList.add(groupFeedModel);

					}
					else
					{

					}
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
			getActivity().runOnUiThread(new Runnable()
			{

				@Override
				public void run()
				{
					if(groupFeedList.size()>0)
					{

						PagesFeed_Adpater feedAdapter = new PagesFeed_Adpater(getActivity(), groupFeedList);

						setListAdapter(feedAdapter);
					}
					else
					{
					}
				}
			});

			return null;
		}
		


	}



}
