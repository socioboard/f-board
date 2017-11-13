package com.socioboard.f_board_pro.fragments;



import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.socioboard.f_board_pro.AllInOneSearchFeeds;
import com.socioboard.f_board_pro.R;
import com.socioboard.f_board_pro.adapter.GroupListAdapter;
import com.socioboard.f_board_pro.database.util.JSONParseraa;
import com.socioboard.f_board_pro.database.util.MainSingleTon;
import com.socioboard.f_board_pro.models.GroupModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Mygroup extends Fragment implements OnScrollListener
{
	View rootView;
	ListView mGroupList;
	ArrayList<GroupModel> groupList;
	TextView nofeeds, text4, txt2;
	ProgressBar progressbar;
	ListView listivew;
	RelativeLayout unkonw_user,footer;

	String cursor =  null;
	boolean isAlredyScrolloing = true;
	ViewGroup viewGroup;
	GroupListAdapter gAdapter;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState)
	{
		rootView = inflater.inflate(R.layout.get_groups_fragment, container,false);

		LoadAd();

		groupList = new ArrayList<GroupModel>();

		progressbar = (ProgressBar) rootView.findViewById(R.id.progressBar1);

		progressbar.setVisibility(View.VISIBLE);

		nofeeds = (TextView) rootView.findViewById(R.id.text12);

		nofeeds.setVisibility(View.INVISIBLE);

		mGroupList = (ListView) rootView.findViewById(R.id.listview);

		footer=(RelativeLayout) rootView.findViewById(R.id.footer);
		
		//text4 = (TextView) rootView.findViewById(R.id.text4);
//		txt2  =(TextView) rootView.findViewById(R.id.txt2);
//
//		txt2.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//
//				String url = "https://www.facebook.com/groups/621124567991195/" ;
//				Intent i = new Intent(Intent.ACTION_VIEW);
//				i.setData(Uri.parse(url));
//				startActivity(i);
//
//
//			}
//		});
		unkonw_user = (RelativeLayout) rootView.findViewById(R.id.unkonw_user);

		//text4.setVisibility(View.INVISIBLE);
		unkonw_user.setVisibility(View.INVISIBLE);
		footer.setVisibility(View.INVISIBLE);
		
		mGroupList.setOnScrollListener(Mygroup.this);

//		text4.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//
//				String url = "http://www.facebook.com/"+MainSingleTon.userid ;
//				Intent i = new Intent(Intent.ACTION_VIEW);
//				i.setData(Uri.parse(url));
//				startActivity(i);
//
//			}
//		});
		mGroupList.setOnScrollListener(Mygroup.this);

		addFooterView();
		//fetching user_groups_list
		new GetGroups().execute();

		mGroupList.setVisibility(View.INVISIBLE);


		mGroupList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				MainSingleTon.pgID      = groupList.get(position).getGroupId();
				MainSingleTon.pgCategory = "";
				MainSingleTon.pgNAME= groupList.get(position).getGroupName();

				Intent intent = new Intent(getActivity(), AllInOneSearchFeeds.class);

				startActivity(intent);

			}
		});
		return rootView;
	}
	private void addFooterView() {

		LayoutInflater inflater = getActivity().getLayoutInflater();

		viewGroup         = (ViewGroup) inflater.inflate(R.layout.progress_layout, mGroupList, false);

		mGroupList.addFooterView(viewGroup);

		viewGroup.setVisibility(View.INVISIBLE);

	}

	void LoadAd()
	{
		MobileAds.initialize(getActivity(), getString(R.string.adMob_app_id));
		AdView mAdView = (AdView) rootView.findViewById(R.id.adView);
		AdRequest adRequest = new AdRequest.Builder().build();
		mAdView.loadAd(adRequest);

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

			groupList.clear();

			String tokenURL = "https://graph.facebook.com/"+userFBiD+"/groups?access_token="+userFBaccesToken;

			JSONParseraa jsonParser = new JSONParseraa();

			System.out.println("SUUUUUUUUUUUUU="+tokenURL);

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
					groupList.add(groupModel);

				} 

				
				JSONObject jsonObject2 = jsonObject.getJSONObject("paging");

				if(jsonObject2.has("next"))
				{
					cursor	=	jsonObject2.getString("next");
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
		protected void onPostExecute(String result) {
			super.onPostExecute(result);

			if(groupList.size()>0)
			{
				gAdapter =  new GroupListAdapter(getActivity(), groupList);
				mGroupList.setAdapter(gAdapter);
				progressbar.setVisibility(View.INVISIBLE);
				mGroupList.setVisibility(View.VISIBLE);
				footer.setVisibility(View.GONE);
				isAlredyScrolloing = false;
			}
			else
			{
				progressbar.setVisibility(View.INVISIBLE);
				mGroupList.setVisibility(View.INVISIBLE);
				nofeeds.setVisibility(View.VISIBLE);
				//text4.setVisibility(View.VISIBLE);
				footer.setVisibility(View.VISIBLE);
				unkonw_user.setVisibility(View.VISIBLE);
				isAlredyScrolloing =true;
				viewGroup.setVisibility(View.GONE);
			}

		}

	}
	private void loadFromTHisCursor(String cursor) {

		new LoadMoreSearchPagesAys().execute(cursor);

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

	public class LoadMoreSearchPagesAys extends AsyncTask<String, Void, String>
	{
		String userFBiD =null;
		String userFBaccesToken = null;
		String type = null;

		@Override
		protected String doInBackground(String... params) 
		{

			String hitURL = params[0];

			JSONParseraa jsonParser = new JSONParseraa();

			JSONObject jsonObject = jsonParser.getJSONFromUrl(hitURL);

			try {

				JSONArray jsonArray =  jsonObject.getJSONArray("data");

				if(jsonArray.length()!=0)
				{
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
						groupList.add(groupModel);

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
				}
				else
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
		protected void onPostExecute(String result) {
			super.onPostExecute(result);

			gAdapter.notifyDataSetChanged();

			isAlredyScrolloing =false;

		}

	}

}
