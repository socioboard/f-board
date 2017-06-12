package com.socioboard.f_board_pro.search_fragments;

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

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.socioboard.f_board_pro.AllInOneSearchFeeds;
import com.socioboard.f_board_pro.R;
import com.socioboard.f_board_pro.adapter.PagesSearch_Adapter;
import com.socioboard.f_board_pro.database.util.JSONParseraa;
import com.socioboard.f_board_pro.database.util.MainSingleTon;
import com.socioboard.f_board_pro.models.PagesSearch_Model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class PagesSearch_Fragment extends Fragment implements OnScrollListener {

	PagesSearch_Adapter pagesSearch_Adapter;
	ViewGroup viewGroup;

	String cursor =  null;
	boolean isAlredyScrolloing = true;
	View rootview;
	public ArrayList<PagesSearch_Model> pagesSearchlist;
	ListView pagesSearclistView;
	ProgressBar progressbar;
	ImageView nopages;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		rootview = inflater.inflate(R.layout.pages_search_fragment, container,
				false);

		LoadAd();

		pagesSearchlist = new ArrayList<PagesSearch_Model>();

		pagesSearclistView = (ListView) rootview.findViewById(R.id.pagesSearclistView);
		pagesSearclistView.setOnScrollListener(PagesSearch_Fragment.this);
		nopages            = (ImageView) rootview.findViewById(R.id.nopages);

		progressbar = (ProgressBar) rootview.findViewById(R.id.progressbar);

		progressbar.setVisibility(View.VISIBLE);

		addFooterView();

		new SearchPagesAys().execute();
		
		pagesSearclistView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				
				MainSingleTon.pgID = pagesSearchlist.get(position).getPgID();
				MainSingleTon.pgNAME =pagesSearchlist.get(position).getPgName();
				MainSingleTon.pgCategory = pagesSearchlist.get(position).getPgCategory();
				
				Intent intent = new Intent(getActivity(), AllInOneSearchFeeds.class);
				
				startActivity(intent);
				
			}
		});
		
		

		return rootview;
	}

	void LoadAd()
	{
		MobileAds.initialize(getActivity(), getString(R.string.adMob_app_id));
		AdView mAdView = (AdView) rootview.findViewById(R.id.adView);
		AdRequest adRequest = new AdRequest.Builder().build();
		mAdView.loadAd(adRequest);

	}

	public class SearchPagesAys extends  AsyncTask<Void, Void, ArrayList<PagesSearch_Model>> {

		@Override
		protected ArrayList<PagesSearch_Model> doInBackground(Void... params) {

			String hitURL = "https://graph.facebook.com/search?q="
					+ MainSingleTon.searchKey + "+&type=page&access_token="
					+ MainSingleTon.accesstoken;

			
			System.out.println("hitURL="+hitURL);
			JSONParseraa jsonParser = new JSONParseraa();

			JSONObject jsonObject = jsonParser.getJSONFromUrl(hitURL);


			try {
				JSONArray jsonArray = jsonObject.getJSONArray("data");

				if (jsonArray.length() != 0) {

					for (int i = 0; i < jsonArray.length(); i++) {
						PagesSearch_Model pagessearch_model = new PagesSearch_Model();
						JSONObject jsonObject2 = jsonArray.getJSONObject(i);
						pagessearch_model.setPgCategory(jsonObject2.getString("category"));
						pagessearch_model.setPgID(jsonObject2.getString("id"));
						pagessearch_model.setPgName(jsonObject2.getString("name"));
						pagessearch_model.setLikesCount(null);
						pagesSearchlist.add(pagessearch_model);
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


				} else {
					// No pages found..
			
					System.out.println("SEarch page are not available ");
				}


			} catch (JSONException e) {

				e.printStackTrace();
			}


			return pagesSearchlist;
		}

		@Override
		protected void onPostExecute(ArrayList<PagesSearch_Model> result) {
			super.onPostExecute(result);

			pagesSearch_Adapter = new PagesSearch_Adapter(getActivity(), pagesSearchlist);

			pagesSearclistView.setAdapter(pagesSearch_Adapter);

			progressbar.setVisibility(View.INVISIBLE);

			isAlredyScrolloing =false;

			if (pagesSearchlist.isEmpty()) {
				isAlredyScrolloing =true;
				pagesSearclistView.setVisibility(View.INVISIBLE);
				viewGroup.setVisibility(View.GONE);
				nopages.setVisibility(View.VISIBLE);;
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

	private void addFooterView() {

		LayoutInflater inflater = getActivity().getLayoutInflater();

		viewGroup = (ViewGroup) inflater.inflate(R.layout.progress_layout, pagesSearclistView, false);

		pagesSearclistView.addFooterView(viewGroup);

		viewGroup.setVisibility(View.INVISIBLE);

	}

	private void loadFromTHisCursor(String cursor) {

		new LoadMoreSearchPagesAys().execute(cursor);

	}

	public class LoadMoreSearchPagesAys extends AsyncTask<String, Void, ArrayList<PagesSearch_Model>> {

		@Override
		protected ArrayList<PagesSearch_Model> doInBackground(String... params) {

			String hitURL = params[0];

			JSONParseraa jsonParser = new JSONParseraa();

			JSONObject jsonObject = jsonParser.getJSONFromUrl(hitURL);

			try {
				JSONArray jsonArray = jsonObject.getJSONArray("data");

				if (jsonArray.length() != 0) {

					for (int i = 0; i < jsonArray.length(); i++) {
						PagesSearch_Model pagessearch_model = new PagesSearch_Model();
						JSONObject jsonObject2 = jsonArray.getJSONObject(i);
						pagessearch_model.setPgCategory(jsonObject2.getString("category"));
						pagessearch_model.setPgID(jsonObject2.getString("id"));
						pagessearch_model.setPgName(jsonObject2.getString("name"));
						pagessearch_model.setLikesCount(null);

						pagesSearch_Adapter.pageslist.add(pagessearch_model);
					}
					JSONObject jsonObject2 = jsonObject.getJSONObject("paging");
					if(jsonObject2.has("next"))
					{
						cursor	=	jsonObject2.getString("next");
					}else
					{
						cursor= null;
					}
				} else {
					// No pages found
					cursor = null;
					
					System.out.println("Search page are not available ");
				}

			} catch (JSONException e) {

				e.printStackTrace();
			}

			return pagesSearchlist;
		}

		@Override
		protected void onPostExecute(ArrayList<PagesSearch_Model> result) {
			super.onPostExecute(result);

			pagesSearch_Adapter.notifyDataSetChanged();

			isAlredyScrolloing = false;

			viewGroup.setVisibility(View.INVISIBLE);

		}

	}

}
