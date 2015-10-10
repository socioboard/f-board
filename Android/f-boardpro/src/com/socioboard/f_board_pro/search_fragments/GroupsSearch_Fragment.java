package com.socioboard.f_board_pro.search_fragments;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.socioboard.f_board_pro.AllInOneSearchFeeds;
import com.socioboard.f_board_pro.R;
import com.socioboard.f_board_pro.adapter.GorupSearch_Adapter;
import com.socioboard.f_board_pro.database.util.JSONParseraa;
import com.socioboard.f_board_pro.database.util.MainSingleTon;
import com.socioboard.f_board_pro.models.PagesSearch_Model;

public class GroupsSearch_Fragment extends Fragment implements  OnScrollListener{

	View rootview;
	public	ArrayList<PagesSearch_Model> groupArrayList;
	ListView groupSearclistView;
	ProgressBar progressbar;
	ImageView nogroup ;

	String cursor =  null;
	boolean isAlredyScrolloing = true;
	public GorupSearch_Adapter groupSearch_Adapter;
	ViewGroup viewGroup;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		rootview           = inflater.inflate(R.layout.group_search_fragment, container, false);
		groupArrayList     = new ArrayList<PagesSearch_Model>();

		groupSearclistView = (ListView)    rootview.findViewById(R.id.groupSearclistView);
		nogroup            = (ImageView)    rootview.findViewById(R.id.nogroup);
		progressbar        = (ProgressBar) rootview.findViewById(R.id.progressbar);
		progressbar.setVisibility(View.VISIBLE);

		groupSearclistView.setOnScrollListener(GroupsSearch_Fragment.this);

		addFooterView();

		new SearchPagesAys().execute();
		
		groupSearclistView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				
				MainSingleTon.pgID       = groupArrayList.get(position).getPgID();
				MainSingleTon.pgNAME     = groupArrayList.get(position).getPgName();
				MainSingleTon.pgCategory = groupArrayList.get(position).getPgCategory();
				
				Intent intent = new Intent(getActivity(), AllInOneSearchFeeds.class);
				
				startActivity(intent);
				
			}
		});

		return rootview;
	}
	
	private void addFooterView() {

		LayoutInflater inflater = getActivity().getLayoutInflater();

		viewGroup               = (ViewGroup) inflater.inflate(R.layout.progress_layout, groupSearclistView, false);

		groupSearclistView.addFooterView(viewGroup);

		viewGroup.setVisibility(View.INVISIBLE);

	}
	public class SearchPagesAys extends AsyncTask<Void, Void, ArrayList<PagesSearch_Model>>
	{
		@Override
		protected ArrayList<PagesSearch_Model> doInBackground(Void... params) {

			String hitURL = "https://graph.facebook.com/search?q="+MainSingleTon.searchKey+"+&type=group&access_token="+MainSingleTon.accesstoken;

			JSONParseraa jsonParser = new JSONParseraa();

			JSONObject jsonObject = jsonParser.getJSONFromUrl(hitURL);

			try {
				JSONArray jsonArray =  jsonObject.getJSONArray("data");

				if(jsonArray.length()!=0)
				{

					for (int i = 0; i < jsonArray.length(); i++) 
					{
						PagesSearch_Model pagessearch_model = new PagesSearch_Model();
						JSONObject jsonObject2 = jsonArray.getJSONObject(i);
						pagessearch_model.setPgID(jsonObject2.getString("id"));
						pagessearch_model.setPgName(jsonObject2.getString("name"));
						pagessearch_model.setLikesCount(null);
						groupArrayList.add(pagessearch_model);
					}

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
					//No pages found 
					System.out.println("Search groups are not available ");
				}


			} catch (JSONException e) {

				e.printStackTrace();
			}

			return groupArrayList;
		}
		@Override
		protected void onPostExecute(ArrayList<PagesSearch_Model> result) {

			super.onPostExecute(result);

			groupSearch_Adapter = new GorupSearch_Adapter(getActivity(), groupArrayList);

			groupSearclistView.setAdapter(groupSearch_Adapter);

			progressbar.setVisibility(View.INVISIBLE);

			isAlredyScrolloing = false;

			if(groupArrayList.isEmpty())
			{
				nogroup.setVisibility(View.VISIBLE);
				viewGroup.setVisibility(View.GONE);
				groupSearclistView.setVisibility(View.INVISIBLE);
				isAlredyScrolloing =true;
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

		new LoadMoreSearchPagesAys().execute(cursor);

	}
	public class LoadMoreSearchPagesAys extends AsyncTask<String, Void, ArrayList<PagesSearch_Model>>
	{
		@Override
		protected ArrayList<PagesSearch_Model> doInBackground(String... params) {

			String hitURL = params[0];

			JSONParseraa jsonParser = new JSONParseraa();

			JSONObject jsonObject = jsonParser.getJSONFromUrl(hitURL);

			try {
				JSONArray jsonArray =  jsonObject.getJSONArray("data");

				if(jsonArray.length()!=0)
				{

					for (int i = 0; i < jsonArray.length(); i++) 
					{
						PagesSearch_Model pagessearch_model = new PagesSearch_Model();
						JSONObject jsonObject2 = jsonArray.getJSONObject(i);
						pagessearch_model.setPgID(jsonObject2.getString("id"));
						pagessearch_model.setPgName(jsonObject2.getString("name"));
						pagessearch_model.setLikesCount(null);
						groupArrayList.add(pagessearch_model);
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
				}else
				{
					//No pages found 

					cursor= null;

					System.out.println("Search Gourpd are not available ");
				}


			} catch (JSONException e) {

				e.printStackTrace();
			}

			return groupArrayList;
		}
		@Override
		protected void onPostExecute(ArrayList<PagesSearch_Model> result) {

			super.onPostExecute(result);

			groupSearch_Adapter.notifyDataSetChanged();

			isAlredyScrolloing =false;

		}

	}
}
