package com.socioboard.f_board_pro.fragments;


import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
 






import com.socioboard.f_board_pro.MainActivity;
import com.socioboard.f_board_pro.R;
import com.socioboard.f_board_pro.adapter.GroupListAdapter;
import com.socioboard.f_board_pro.database.util.JSONParseraa;
import com.socioboard.f_board_pro.database.util.MainSingleTon;
import com.socioboard.f_board_pro.models.GroupModel;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class GetGroups_Fragment extends ListFragment
{
	View rootView;
	ListView mGroupList;
	ArrayList<GroupModel> groupList;
	TextView nofeeds;
	ProgressBar progressbar;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState)
	{
		rootView = inflater.inflate(R.layout.get_groups_fragment, container,false);
		groupList=new ArrayList<GroupModel>();
		
		progressbar=(ProgressBar) rootView.findViewById(R.id.progressBar1);
		
		progressbar.setVisibility(View.VISIBLE);
		

		nofeeds=(TextView) rootView.findViewById(R.id.no_groups);
		nofeeds.setText("No feeds");
		nofeeds.setVisibility(View.INVISIBLE);
		
		//fetching user_groups_list
		new GetGroups().execute();
	
		return rootView;
	}
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) 
	{
		mGroupList=getListView();
		mGroupList.setOnItemClickListener(new GroupItemClickListener());
		mGroupList.setVisibility(View.INVISIBLE);
	}
	
	private class GroupItemClickListener implements ListView.OnItemClickListener
	{
		@Override
		public void onItemClick(AdapterView parent, View view, int position, long id) 
		{
			selectGroup(position);
		}

		private void selectGroup(int position)
		{
			
			MainSingleTon.selectedGroupToFetchFeeds=MainSingleTon.groupArrayList.get(position);
			Fragment fragment = new GetGroup_Feeds();
			MainActivity.swipeFragment(fragment);
		}
	}
	
	
	public class GetGroups extends AsyncTask<Void, Void, String>
	{
		String userFBiD =null;
		String userFBaccesToken = null;
		String type = null;

		@Override
		protected String doInBackground(Void... params) 
		{

			userFBaccesToken = MainSingleTon.accesstoken;
			userFBiD = MainSingleTon.userid;
			MainSingleTon.groupArrayList.clear();

			String tokenURL = "https://graph.facebook.com/"+userFBiD+"/groups?access_token="+userFBaccesToken;

			JSONParseraa jsonParser = new JSONParseraa();

			JSONObject jsonObject = jsonParser.getJSONFromUrl(tokenURL);

			try {

					JSONArray jsonArray =  jsonObject.getJSONArray("data");
					
					for(int i = 0; i<jsonArray.length();i++)
					{
						GroupModel groupModel = new GroupModel();
						JSONObject jsonObject2 = jsonArray.getJSONObject(i);
	
						if(jsonObject2.has("id"))
						{
							groupModel.setGroupId(jsonObject2.getString("id"));
						}
						if(jsonObject2.has("name"))
						{
							groupModel.setGroupName(jsonObject2.getString("name"));
						}
						
						if (jsonObject2.has("unread")) 
						{
							groupModel.setGroupUnread(jsonObject2.getInt("unread"));
						}
						MainSingleTon.groupArrayList.add(groupModel);
						
						
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
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			 	
				 			if(MainSingleTon.groupArrayList.size()>0)
							{
								GroupListAdapter gAdapter=new GroupListAdapter(getActivity(), MainSingleTon.groupArrayList);
								mGroupList.setAdapter(gAdapter);
								progressbar.setVisibility(View.INVISIBLE);
								mGroupList.setVisibility(View.VISIBLE);
							}
							else
							{
								progressbar.setVisibility(View.INVISIBLE);
								mGroupList.setVisibility(View.INVISIBLE);
								nofeeds.setVisibility(View.VISIBLE);
							}
				 
			 
		}

	}
	



}
