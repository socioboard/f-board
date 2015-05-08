package com.socioboard.f_board_pro.fragments;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import com.socioboard.f_board_pro.MainActivity;
import com.socioboard.f_board_pro.R;
import com.socioboard.f_board_pro.adapter.UserFriendsAdapter;
import com.socioboard.f_board_pro.adapter.UserPhotoAdapter;
import com.socioboard.f_board_pro.database.util.JSONParseraa;
import com.socioboard.f_board_pro.database.util.MainSingleTon;
import com.socioboard.f_board_pro.database.util.ModelUserDatas;
import com.socioboard.f_board_pro.models.FriendModel;
import com.socioboard.f_board_pro.models.ImageModel;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class DisplayUserFriendsFragment extends Fragment  
{
	View rootView;
	GridView mGridView;
	ProgressBar  mProgressBar;
	TextView mNoFriends;
	ArrayList<FriendModel> friendList;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState)
	{
		rootView = inflater.inflate(R.layout.user_friends_fragment, container, false);
		friendList=new ArrayList<FriendModel>();
		
		
		mGridView=(GridView)rootView.findViewById(R.id.userfriends);
		mGridView.setVisibility(View.INVISIBLE);
		mGridView.setOnItemClickListener(new UnFriend());
		
		mNoFriends=(TextView) rootView.findViewById(R.id.nofriends);
		mNoFriends.setVisibility(View.INVISIBLE);
		
		mProgressBar=(ProgressBar)rootView.findViewById(R.id.progressBar1);
		mProgressBar.setVisibility(View.VISIBLE);
		
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
					Toast.makeText(getActivity(), friendList.get(position).getFriendName()+" Unfriended", Toast.LENGTH_SHORT).show();
					
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
				UserFriendsAdapter userFriendsAdapter = new UserFriendsAdapter(getActivity(), friendList);
				mGridView.setAdapter(userFriendsAdapter);
				mGridView.setVisibility(View.VISIBLE);
				mProgressBar.setVisibility(View.INVISIBLE);
				mNoFriends.setVisibility(View.INVISIBLE);
				
			}
			else
			{
				mGridView.setVisibility(View.INVISIBLE);
				mProgressBar.setVisibility(View.INVISIBLE);
				mNoFriends.setVisibility(View.VISIBLE);
			}
		}
		

	}
}
