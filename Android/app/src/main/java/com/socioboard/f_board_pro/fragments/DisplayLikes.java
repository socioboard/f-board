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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.socioboard.f_board_pro.R;
import com.socioboard.f_board_pro.adapter.LikesAdapter;
import com.socioboard.f_board_pro.database.util.JSONParseraa;
import com.socioboard.f_board_pro.database.util.MainSingleTon;
import com.socioboard.f_board_pro.models.LikeModel;

public class DisplayLikes extends ListFragment
{
	View rootView;
	ListView mLikeList;
	ArrayList<LikeModel> likeList;
	ProgressBar progressbar;
	TextView nolikes;
	ImageView like;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState)
	{
		rootView    = inflater.inflate(R.layout.display_likes_fragment, container,false);
		likeList    = new ArrayList<LikeModel>();
		progressbar = (ProgressBar) rootView.findViewById(R.id.progressBar1);
		
		progressbar.setVisibility(View.VISIBLE);
		
		nolikes    = (TextView) rootView.findViewById(R.id.no_likes);
		nolikes.setText("Be the first to like this");
		nolikes.setVisibility(View.INVISIBLE);
		
		like=(ImageView) rootView.findViewById(R.id.like);
		
		new GetLikeDetails().execute();
		
		return rootView; 
	}
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState)
	{
		mLikeList = getListView();
	}
	
	/*class to get the details of who liked the feed*/
	public class GetLikeDetails extends AsyncTask<Void, Void, String>
	{
		String FeedId =null;
		String userFBaccesToken = null;
		String type = null;

		@Override
		protected String doInBackground(Void... params) 
		{

			userFBaccesToken = MainSingleTon.accesstoken;
			FeedId = MainSingleTon.selectedFeedForLikes;
			likeList.clear();
			String tokenURL = "https://graph.facebook.com/"+FeedId+"/likes?access_token="+userFBaccesToken;
			JSONParseraa jsonParser = new JSONParseraa();

			JSONObject jsonObject = jsonParser.getJSONFromUrl(tokenURL);
			try
			{
					JSONArray jsonArray =  jsonObject.getJSONArray("data");
					
					
					for(int i = 0; i<jsonArray.length();i++)
					{
						LikeModel likeModel=new LikeModel();
						JSONObject jsonObject2 = jsonArray.getJSONObject(i);
						likeModel.setFeedId(FeedId);
	
						if(jsonObject2.has("id"))
						{
							likeModel.setUserId(jsonObject2.getString("id"));
						}
						if(jsonObject2.has("name"))
						{
							likeModel.setUserName(jsonObject2.getString("name"));	
						}
						
						likeList.add(likeModel);
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
			
			if(likeList.size()>0)
			{
				mLikeList.setAdapter(new LikesAdapter(getActivity(), likeList));
				mLikeList.setVisibility(View.VISIBLE);
				progressbar.setVisibility(View.INVISIBLE);
				nolikes.setVisibility(View.INVISIBLE);
			}
			else
			{
				mLikeList.setVisibility(View.INVISIBLE);
				progressbar.setVisibility(View.INVISIBLE);
				nolikes.setVisibility(View.VISIBLE);
			}
		}
	}
}
