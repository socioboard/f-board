package com.socioboard.f_board_pro.fragments;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.socioboard.f_board_pro.MainActivity;
import com.socioboard.f_board_pro.R;
import com.socioboard.f_board_pro.adapter.GroupFeedAdapter;
import com.socioboard.f_board_pro.database.util.JSONParseraa;
import com.socioboard.f_board_pro.database.util.LoadMoreListView.OnLoadMoreListener;
import com.socioboard.f_board_pro.database.util.MainSingleTon;
import com.socioboard.f_board_pro.database.util.Utilsss;
import com.socioboard.f_board_pro.models.DetermineUserLike;
import com.socioboard.f_board_pro.models.GroupFeedModel;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.text.format.DateFormat;
import android.text.format.Time;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.socioboard.f_board_pro.database.util.LoadMoreListView ;

public class GetGroup_Feeds extends ListFragment   
{
	View rootView;
	ListView mGroupFeedList;
	TextView nogroupfeeds;
	ProgressBar progressbar;
	// list with the data to show in the listview
	private ArrayList<GroupFeedModel> groupFeedList;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState)
	{
		rootView = inflater.inflate(R.layout.feed_list_fragment, container,false);
	
		((MainActivity) getActivity()).setTitle(MainSingleTon.selectedGroupToFetchFeeds.getGroupName());
	

		progressbar=(ProgressBar) rootView.findViewById(R.id.progressBar1);
		
		progressbar.setVisibility(View.VISIBLE);

		nogroupfeeds=(TextView) rootView.findViewById(R.id.no_feeds);
		nogroupfeeds.setText("No Groups found");
		nogroupfeeds.setVisibility(View.INVISIBLE);
		
		groupFeedList=new ArrayList<GroupFeedModel>();
		
		new GetGroupFeeds().execute();
		return rootView;
	}
	private class GroupFeedClickListener implements ListView.OnItemClickListener
	{
		@Override
		public void onItemClick(AdapterView parent, View view, int position, long id) 
		{
			displayFeed(position);
		}

		private void displayFeed(int position)
		{
			MainSingleTon.selectedGroupFeed=groupFeedList.get(position);
			MainSingleTon.selectedFeedForLikes=groupFeedList.get(position).getFeedID();
			Fragment fragment = new DisplayGroupFeedFragment();
			MainActivity.swipeFragment(fragment);
		}
	}
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState)
	{

		mGroupFeedList = getListView();

		groupFeedList = new ArrayList<GroupFeedModel>();
		

		mGroupFeedList.setOnItemClickListener(new GroupFeedClickListener());
		mGroupFeedList.setVisibility(View.INVISIBLE);

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
	
	public class GetGroupFeeds extends AsyncTask<Void, Void, String>
	{
		String userGroupiD =null;
		String userFBaccesToken = null;
		String type = null;
		
		
		@Override
		protected String doInBackground(Void... params) 
		{

			userFBaccesToken = MainSingleTon.accesstoken;
			userGroupiD = MainSingleTon.selectedGroupToFetchFeeds.getGroupId();
			groupFeedList.clear();
			MainSingleTon.userLikedFeedList.clear();

			String tokenURL = "https://graph.facebook.com/"+userGroupiD+"/feed?access_token="+userFBaccesToken;

			JSONParseraa jsonParser = new JSONParseraa();

			JSONObject jsonObject = jsonParser.getJSONFromUrl(tokenURL);

			try {


					JSONArray jsonArray =  jsonObject.getJSONArray("data");
	
					for(int i = 0; i<jsonArray.length();i++)
					{
							GroupFeedModel groupFeedModel = new GroupFeedModel();
							DetermineUserLike userLikes= new DetermineUserLike();
							JSONObject jsonObject2 = jsonArray.getJSONObject(i);
							if(jsonObject2.has("id"))
							{
								groupFeedModel.setFeedID(jsonObject2.getString("id"));
								userLikes.setFeedId(jsonObject2.getString("id"));
							}
							if(jsonObject2.has("likes"))
							{
								JSONObject jsonObjectlikes = jsonObject2.getJSONObject("likes");
								JSONArray jsonArraylikes =  jsonObjectlikes.getJSONArray("data");
								groupFeedModel.setLikes(jsonArraylikes.length());
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
							}else
							{
								groupFeedModel.setLikes(0);
								userLikes.setLike(false);
							}
							if(jsonObject2.has("comments"))
							{
								JSONObject jsonObjectcomments = jsonObject2.getJSONObject("comments");
								JSONArray jsonArraycomments =  jsonObjectcomments.getJSONArray("data");
								groupFeedModel.setComments(jsonArraycomments.length());
								System.out.println(" no of comments "+jsonArraycomments.length());
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
											if(jsonObject3.has("id"))
											{
												groupFeedModel.setFromID(jsonObject3.getString("id"));
											}
											groupFeedModel.setProfilePic("https://graph.facebook.com/"+jsonObject3.getString("id")+"/picture?type=small");
											
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
									}
									if(jsonObject2.has("from"))
									{
										JSONObject jsonObject3 = jsonObject2.getJSONObject("from");
										if(jsonObject3.has("name"))
										{
											groupFeedModel.setFrom(jsonObject3.getString("name"));
										}
										if(jsonObject3.has("id"))
										{
											groupFeedModel.setFromID(jsonObject3.getString("id"));
										}
										groupFeedModel.setProfilePic("https://graph.facebook.com/"+jsonObject3.getString("id")+"/picture?type=small");
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
									}
									if(jsonObject2.has("from"))
									{
										JSONObject jsonObject3 = jsonObject2.getJSONObject("from");
										if(jsonObject3.has("name"))
										{
											groupFeedModel.setFrom(jsonObject3.getString("name"));
										}
										if(jsonObject3.has("id"))
										{
											groupFeedModel.setFromID(jsonObject3.getString("id"));
										}
										groupFeedModel.setProfilePic("https://graph.facebook.com/"+jsonObject3.getString("id")+"/picture?type=small");
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
									}
									if(jsonObject2.has("from"))
									{
										JSONObject jsonObject3 = jsonObject2.getJSONObject("from");
										if(jsonObject3.has("name"))
										{
											groupFeedModel.setFrom(jsonObject3.getString("name"));
										}
										if(jsonObject3.has("id"))
										{
											groupFeedModel.setFromID(jsonObject3.getString("id"));
										}
										groupFeedModel.setProfilePic("https://graph.facebook.com/"+jsonObject3.getString("id")+"/picture?type=small");
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
								MainSingleTon.userLikedFeedList.add(userLikes);
		
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
							
								GroupFeedAdapter feedAdapter = new GroupFeedAdapter(getActivity(), groupFeedList);
								setListAdapter(feedAdapter);
								progressbar.setVisibility(View.INVISIBLE);
								mGroupFeedList.setVisibility(View.VISIBLE);
							}
							else
							{
								System.out.println("group list empty "+MainSingleTon.groupArrayList.size());
								progressbar.setVisibility(View.INVISIBLE);
								mGroupFeedList.setVisibility(View.INVISIBLE);
								nogroupfeeds.setVisibility(View.VISIBLE);
							}
				}
			});
			
			return null;
		}
		

	}
	
	
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
	
							GroupFeedModel groupFeedModel = new GroupFeedModel();
							JSONObject jsonObject2 = jsonArray.getJSONObject(i);
	
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
									
								}
								groupFeedList.add(groupFeedModel);
							}
							else
							{
	
							}
						}
					}
					else
					{ 
						getActivity().runOnUiThread(new Runnable()
						{
							
							@Override
							public void run()
							{
								
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
			return null;
		}
	}
	public String GetLocalDateStringFromUTCString(String utcLongDateTime) 
	{
	    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
	    String localDateString = null;

	    long when = 0;
	    try 
	    {
	        when = dateFormat.parse(utcLongDateTime).getTime();
	    } catch (ParseException e) {
	        e.printStackTrace();
	    }
	    localDateString = dateFormat.format(new Date(when + TimeZone.getDefault().getRawOffset() + (TimeZone.getDefault().inDaylightTime(new Date()) ? TimeZone.getDefault().getDSTSavings() : 0)));
	    System.out.println("TIME     : "+localDateString);
	   
	    System.out.println("In MILLIES     : "+when);
	    ///
	    Calendar date=Calendar.getInstance();
        Date d=date.getTime();
   
        long different = d.getTime() - when;
   
         long secondsInMilli = 1000;
         long minutesInMilli = secondsInMilli * 60;
         long hoursInMilli = minutesInMilli * 60;
         long daysInMilli = hoursInMilli * 24;
         long weeksInMilli=daysInMilli*7;
         long monthsInMilli=weeksInMilli*4;
         long year=monthsInMilli*12;
         

         long elapsedYears=different/year;
         long elapsedMonths=different/monthsInMilli;
         long elspsedWeeks=different/weeksInMilli;
         long elapsedDays = different / daysInMilli;
         different = different % daysInMilli;
         long elapsedHours = different / hoursInMilli;
         long elapsedMinute = different / minutesInMilli;
         String timeago;
         if(elapsedYears>0)
         {
        	 timeago=""+elapsedYears+" Years Ago";
         }
         else
        if(elapsedMonths>0)
         {
        	timeago=""+elapsedMonths+" months Ago";
         }
         else
         if(elspsedWeeks>0)
         {
         	 timeago=""+elspsedWeeks+" weeks Ago";
          }
          else
         if(elapsedDays>0)
         {
          timeago=""+elapsedDays+" Days Ago";
         }
         else
         if(elapsedHours>0)
         { 
          timeago=""+elapsedHours+" Hour Ago";
          
         }else
         {
          timeago=""+elapsedMinute+" Minues Ago";
         }
	    
         System.out.println("created     : "+timeago);
	    return timeago;
	}
}
