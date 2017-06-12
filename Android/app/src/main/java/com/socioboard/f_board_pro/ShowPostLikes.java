package com.socioboard.f_board_pro;

import java.util.ArrayList;

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

import com.socioboard.f_board_pro.adapter.LikesAdapter;
import com.socioboard.f_board_pro.database.util.JSONParseraa;
import com.socioboard.f_board_pro.database.util.MainSingleTon;
import com.socioboard.f_board_pro.models.LikeModel;

public class ShowPostLikes extends Activity implements OnScrollListener 
{
	ArrayList<LikeModel> arrayListlike;
	ProgressBar progressbar;
	TextView nolikes;
	ImageView like;
	ListView listView;
	RelativeLayout rlt;

	boolean isAlredyScrolloing = true;
	String cursor = null;
	ViewGroup viewGroup;
	LikesAdapter likesAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.showpost_likes);

		arrayListlike    = new ArrayList<LikeModel>();
		progressbar       = (ProgressBar) findViewById(R.id.progressBar1);
		rlt = (RelativeLayout) findViewById(R.id.rlt);
		progressbar.setVisibility(View.VISIBLE);
		listView = (ListView) findViewById(R.id.list);
		nolikes=(TextView) findViewById(R.id.no_likes);
		nolikes.setText("Be the first to like this");
		nolikes.setVisibility(View.INVISIBLE);

		like =(ImageView) findViewById(R.id.like);
		
		rlt.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {

				ShowPostLikes.this.finish();
			}
		});

		addFooterView();
		
		new GetLikeDetails().execute();

		listView.setOnScrollListener(ShowPostLikes.this);

	}

	private void addFooterView() {

		LayoutInflater inflater =  getLayoutInflater();

		viewGroup = (ViewGroup) inflater.inflate(R.layout.progress_layout, listView, false);

		listView.addFooterView(viewGroup);

		viewGroup.setVisibility(View.INVISIBLE);

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

			FeedId           = MainSingleTon.selectedFeedForLikes;

			arrayListlike.clear();

			String tokenURL = "https://graph.facebook.com/"+FeedId+"/likes?access_token="+userFBaccesToken;

			JSONParseraa jsonParser = new JSONParseraa();

			JSONObject jsonObject = jsonParser.getJSONFromUrl(tokenURL);
			try
			{
				JSONArray jsonArray =  jsonObject.getJSONArray("data");

				if(jsonArray.length()!=0)
				{
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

						arrayListlike.add(likeModel);
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
			if(arrayListlike.size()>0)
			{
				
				likesAdapter = new LikesAdapter(getApplicationContext(), arrayListlike);
				listView.setAdapter(likesAdapter);
				listView.setVisibility(View.VISIBLE);
				progressbar.setVisibility(View.INVISIBLE);
				nolikes.setVisibility(View.INVISIBLE);
			}
			else
			{
				listView.setVisibility(View.INVISIBLE);
				progressbar.setVisibility(View.INVISIBLE);
				nolikes.setVisibility(View.VISIBLE);
				isAlredyScrolloing =true;
				viewGroup.setVisibility(View.GONE);
			}
		}
	}


	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {

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
		String FeedId =null;
		String userFBaccesToken = null;
		String type = null;

		@Override
		protected String doInBackground(String... params) 
		{
		 

			String tokenURL = params[0];

			JSONParseraa jsonParser = new JSONParseraa();

			JSONObject jsonObject = jsonParser.getJSONFromUrl(tokenURL);
			try
			{
				JSONArray jsonArray =  jsonObject.getJSONArray("data");

				if(jsonArray.length()!=0)
				{
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

						arrayListlike.add(likeModel);
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

			likesAdapter.notifyDataSetChanged();

			isAlredyScrolloing = false;

			viewGroup.setVisibility(View.INVISIBLE);
		}
	}

}
