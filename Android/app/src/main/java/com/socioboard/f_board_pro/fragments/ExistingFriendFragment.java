package com.socioboard.f_board_pro.fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.socioboard.f_board_pro.R;
import com.socioboard.f_board_pro.adapter.FriendsAdapter;
import com.socioboard.f_board_pro.database.util.JSONParseraa;
import com.socioboard.f_board_pro.database.util.MainSingleTon;
import com.socioboard.f_board_pro.models.FriendModel;
import com.socioboard.f_board_pro.viewlibary.GridViewHeaderFooterLib;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class ExistingFriendFragment  extends  Fragment implements OnScrollListener{
	View rootView;
	 GridViewHeaderFooterLib mGridView;
	ProgressBar  mProgressBar;
	TextView mNoFriends, invite_friend;
	ArrayList<FriendModel> friendList;
//	View headerview;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		rootView   = inflater.inflate(R.layout.user_friends_fragment, container, false);
		friendList = new ArrayList<FriendModel>();	
		mGridView  = (GridViewHeaderFooterLib)rootView.findViewById(R.id.userfriends);
		mGridView.setVisibility(View.INVISIBLE);
		mGridView.setOnItemClickListener(new UnFriend());

		mNoFriends = (TextView) rootView.findViewById(R.id.nofriends);
		mNoFriends.setText("No friends");
		mNoFriends.setVisibility(View.INVISIBLE);

		mProgressBar  =(ProgressBar)rootView.findViewById(R.id.progressBar1);
		mProgressBar.setVisibility(View.VISIBLE);
		
//		headerview     = 	  inflater.inflate(R.layout.header_existingfriend, null,false);
//		invite_friend  =  (TextView) headerview.findViewById(R.id.invite_friend);
		
//		invite_friend.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//
//				sendInvitation();
//			}
//		});
		//mGridView.addHeaderView(headerview,  null,false);

		new GetUserFriends().execute();

		return rootView;
	}


	private class UnFriend implements GridView.OnItemClickListener
	{
		@Override
		public void onItemClick(AdapterView parent, View view, int position, long id) 
		{
			displayFeed(position);
		}

		private void displayFeed(final int position)
		{/*
			Fragment fragment = new DisplayGroupFeedFragment();
			MainActivity.swipeFragment(fragment);*/
			getActivity().runOnUiThread(new Runnable() 
			{
				@Override
				public void run() 
				{
					//Toast.makeText(getActivity(), friendList.get(position).getFriendName()+" Unfriended", Toast.LENGTH_SHORT).show();
					
				}
			});
		}
	}
	/*class to GetUserFriends a post*/
	public class GetUserFriends extends AsyncTask<String, Void, String>
	{

		String userFBaccesToken = null;
		
		@Override
		protected String doInBackground(String... params)
		{
			userFBaccesToken = MainSingleTon.accesstoken;
			//https://graph.facebook.com/me/friends?access_token=CAANGZCSfBfk0BALxujJTJSywZA4SywcvZCSvSdRzMrW0AlVUkxGgQo04VxEqbsN3ZAJwmbym4qZCZAWQgZAwRLsxBDcSyitTmTT5wT7fgteC5M8ntZBP67oD7S0unenZBkyKJ1kAhOjCRVKLD0qTWL8itxgNNagdEZC8ZADAa7Sc3zZBzJKLNh4F7CnmL1j4OjeTrMkZD
			friendList.clear();

			String tokenURL = "https://graph.facebook.com/me/friends?access_token="+userFBaccesToken;

			JSONParseraa jsonParser = new JSONParseraa();

			JSONObject jsonObject = jsonParser.getJSONFromUrl(tokenURL);
			
			try 
			{
				JSONArray jsonArray =  jsonObject.getJSONArray("data");
				
				for(int i = 0; i<jsonArray.length();i++)
				{
					System.out.println(i+" friend "+jsonArray.getJSONObject(i) );
					FriendModel model=new FriendModel();
					model.setFriendId(jsonArray.getJSONObject(i).getString("id"));
					model.setFriendName(jsonArray.getJSONObject(i).getString("name"));
					model.setFriendPic("https://graph.facebook.com/"+jsonArray.getJSONObject(i).getString("id")+"/picture?type=small");
					friendList.add(model);
					
				}
			}
			catch (Exception e) 
			{
				e.printStackTrace();
			}
			
			return null;
		}
		@Override
		protected void onPostExecute(String result)
		{
			super.onPostExecute(result);
			if(friendList.size()>0)
			{
				FriendsAdapter userFriendsAdapter = new FriendsAdapter(getActivity(), friendList);
				mGridView.setAdapter(userFriendsAdapter);
				mGridView.setVisibility(View.VISIBLE);
				mProgressBar.setVisibility(View.INVISIBLE);
				mNoFriends.setVisibility(View.INVISIBLE);
				
			}
			else
			{
				FriendsAdapter userFriendsAdapter = new FriendsAdapter(getActivity(), friendList);
				mGridView.setAdapter(userFriendsAdapter);
				mProgressBar.setVisibility(View.INVISIBLE);
				mGridView.setVisibility(View.VISIBLE);
				mNoFriends.setVisibility(View.VISIBLE);
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
		// TODO Auto-generated method stub
		
	}
}
