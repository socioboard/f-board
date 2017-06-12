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
import com.socioboard.f_board_pro.adapter.EventSearch_Adapter;
import com.socioboard.f_board_pro.database.util.JSONParseraa;
import com.socioboard.f_board_pro.database.util.MainSingleTon;
import com.socioboard.f_board_pro.models.PagesSearch_Model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class EventsSearch_Fragment  extends Fragment implements OnScrollListener {
	
	boolean isAlredyScrolloing = true;
	View rootview;
	ListView eventSearclistView;
	ImageView noeventTxt;
	public ArrayList<PagesSearch_Model> eventArraylist;
	ProgressBar progressbar;
	String cursor = null;
	ViewGroup viewGroup;

	public EventSearch_Adapter eventSearch_Adapter;
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		rootview           = inflater.inflate(R.layout.event_search_fragment, container, false);

		LoadAd();
		
		eventSearclistView = (ListView) rootview.findViewById(R.id.eventSearclistView);
		
		noeventTxt         = (ImageView) rootview.findViewById(R.id.noevent);
		
		progressbar = (ProgressBar) rootview.findViewById(R.id.progressbar);

		eventArraylist = new ArrayList<PagesSearch_Model>();

		progressbar.setVisibility(View.VISIBLE);

		addFooterView();

     	new SearchEventAys().execute();


     	eventSearclistView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				
				MainSingleTon.pgID       = eventArraylist.get(position).getPgID();
				MainSingleTon.pgNAME     = eventArraylist.get(position).getPgName();
				MainSingleTon.pgCategory = eventArraylist.get(position).getPgCategory();
				
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

	private void addFooterView() {

		LayoutInflater inflater = getActivity().getLayoutInflater();

		viewGroup = (ViewGroup) inflater.inflate(R.layout.progress_layout, eventSearclistView, false);

		eventSearclistView.addFooterView(viewGroup);

		viewGroup.setVisibility(View.INVISIBLE);

	}
	public class SearchEventAys extends  AsyncTask<Void, Void, ArrayList<PagesSearch_Model>> {

		@Override
		protected ArrayList<PagesSearch_Model> doInBackground(Void... params) {

			String hitURL = "https://graph.facebook.com/search?q="
					+ MainSingleTon.searchKey + "+&type=event&access_token="
					+ MainSingleTon.accesstoken;

			JSONParseraa jsonParser = new JSONParseraa();

			JSONObject jsonObject = jsonParser.getJSONFromUrl(hitURL);


			try {
				JSONArray jsonArray = jsonObject.getJSONArray("data");

				if (jsonArray.length() != 0) {
					 
					for (int i = 0; i < jsonArray.length(); i++)
					{
						PagesSearch_Model pagessearch_model = new PagesSearch_Model();
						
						JSONObject jsonObject2 = jsonArray.getJSONObject(i);
						
						if(jsonObject2.has("start_time"))
						{
							pagessearch_model.setEventStartTim(jsonObject2.getString("start_time"));
						}
						else
						{
							pagessearch_model.setEventStartTim(null);
						}
						
						if(jsonObject2.has("end_time"))
						{
							pagessearch_model.setEventEndTim(jsonObject2.getString("end_time"));
						}
						else
						{
							pagessearch_model.setEventEndTim(null);
						}
						if(jsonObject2.has("location"))
						{
							pagessearch_model.setLocation(jsonObject2.getString("location"));
						}
						else
						{
							pagessearch_model.setLocation(null);
						}
						
						if(jsonObject2.has("id"))
						{
							pagessearch_model.setPgID(jsonObject2.getString("id"));
						}else
						{
							pagessearch_model.setPgID(null);

						}
						if(jsonObject2.has("name"))
						{
							pagessearch_model.setPgName(jsonObject2.getString("name"));
						}else
						{
							pagessearch_model.setPgName(null);

						}
					 
						eventArraylist.add(pagessearch_model);
					}
					if(jsonObject.has("paging"))
					{
						System.out.println("------------------runnnnnnnnnn");
						
						JSONObject jsonObject2 = jsonObject.getJSONObject("paging");
						
						if(jsonObject2.has("next"))
						{
							cursor	=	jsonObject2.getString("next");
							
							System.out.println(cursor+"------------------runnnnnnnnnn");
						}else
						{
							cursor= null;
						}
					}else
					{
						cursor= null;
					}


				} else {
					 
					cursor= null;
					
					System.out.println("Search EVENT are not available ");
				}


			} catch (JSONException e) {

				e.printStackTrace();
			}

			return eventArraylist;
		}

		@Override
		protected void onPostExecute(ArrayList<PagesSearch_Model> result) {
			super.onPostExecute(result);

			eventSearch_Adapter = new EventSearch_Adapter(getActivity(), eventArraylist);

			eventSearclistView.setAdapter(eventSearch_Adapter);

			progressbar.setVisibility(View.INVISIBLE);

			isAlredyScrolloing = false;

			if (eventArraylist.isEmpty()) {
				
				viewGroup.setVisibility(View.GONE);
				isAlredyScrolloing = true;
				eventSearclistView.setVisibility(View.INVISIBLE);
				noeventTxt.setVisibility(View.VISIBLE);
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
			
			System.out.println("------------------LoadMoreSearchPagesAys");
			
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
						
						if(jsonObject2.has("start_time"))
						{
							pagessearch_model.setEventStartTim(jsonObject2.getString("start_time"));
						}
						else
						{
							pagessearch_model.setEventStartTim(null);
						}
						
						if(jsonObject2.has("end_time"))
						{
							pagessearch_model.setEventEndTim(jsonObject2.getString("end_time"));
						}
						else
						{
							pagessearch_model.setEventEndTim(null);
						}
						if(jsonObject2.has("location"))
						{
							pagessearch_model.setLocation(jsonObject2.getString("location"));
						}
						else
						{
							pagessearch_model.setLocation(null);
						}
						
						if(jsonObject2.has("id"))
						{
							pagessearch_model.setPgID(jsonObject2.getString("id"));
						}else
						{
							pagessearch_model.setPgID(null);

						}
						if(jsonObject2.has("name"))
						{
							pagessearch_model.setPgName(jsonObject2.getString("name"));
						}else
						{
							pagessearch_model.setPgName(null);

						}
					 
						eventArraylist.add(pagessearch_model);
					}
					if(jsonObject.has("paging"))
					{
						System.out.println("------------------Inside the PAGING");
						
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

					System.out.println("------------------NOT the PAGING");
				}else
				{
					cursor= null;
					
					System.out.println("Search Event are not available ");
				}


			} catch (JSONException e) {

				e.printStackTrace();
			}

			return eventArraylist;
		}
		@Override
		protected void onPostExecute(ArrayList<PagesSearch_Model> result) {

			super.onPostExecute(result);

			eventSearch_Adapter.notifyDataSetChanged();

			isAlredyScrolloing =false;

		}

	}

}
