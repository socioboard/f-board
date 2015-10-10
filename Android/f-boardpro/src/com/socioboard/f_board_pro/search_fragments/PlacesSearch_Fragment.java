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
import android.widget.TextView;

import com.socioboard.f_board_pro.AllInOneSearchFeeds;
import com.socioboard.f_board_pro.R;
import com.socioboard.f_board_pro.adapter.PlacesSearch_Adapter;
import com.socioboard.f_board_pro.database.util.JSONParseraa;
import com.socioboard.f_board_pro.database.util.MainSingleTon;
import com.socioboard.f_board_pro.models.PagesSearch_Model;

public class PlacesSearch_Fragment  extends Fragment implements OnScrollListener{
	View rootview;
	public	ArrayList<PagesSearch_Model> placesArrayList;
	ListView placesSearclistView;
	ProgressBar progressbar;
	ImageView noplaces ;

	String cursor =  null;
	boolean isAlredyScrolloing = true;
	public PlacesSearch_Adapter placesSearch_Adapter;
	ViewGroup viewGroup;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		rootview = inflater.inflate(R.layout.places_search_fragment, container, false);
		placesArrayList = new ArrayList<PagesSearch_Model>();

		placesSearclistView = (ListView)    rootview.findViewById(R.id.placesSearclistView);
		noplaces            = (ImageView)    rootview.findViewById(R.id.noplaces);
		progressbar         = (ProgressBar) rootview.findViewById(R.id.progressbar);

		placesSearclistView.setOnScrollListener(PlacesSearch_Fragment.this);

		progressbar.setVisibility(View.VISIBLE);

		addFooterView();

		new SearchPlacesAys().execute();
		
		placesSearclistView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				
				MainSingleTon.pgID       = placesArrayList.get(position).getPgID();
				MainSingleTon.pgNAME     = placesArrayList.get(position).getPgName();
				MainSingleTon.pgCategory = placesArrayList.get(position).getPgCategory();
				
				Intent intent = new Intent(getActivity(), AllInOneSearchFeeds.class);
				
				startActivity(intent);
				
			}
		});

		return rootview;
	}

	private void addFooterView() {

		LayoutInflater inflater = getActivity().getLayoutInflater();

		viewGroup = (ViewGroup) inflater.inflate(R.layout.progress_layout, placesSearclistView, false);

		placesSearclistView.addFooterView(viewGroup);

		viewGroup.setVisibility(View.INVISIBLE);

	}

	private void loadFromTHisCursor(String cursor) {

		new LoadMoreSearchPeopleAys().execute(cursor);

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

				viewGroup.setVisibility(View.VISIBLE);

				isAlredyScrolloing = true;

				if (cursor == null) {
					
					viewGroup.setVisibility(View.GONE);

				} else {
					
					loadFromTHisCursor(cursor);
				}

			}

		} 
	

	}

	public class SearchPlacesAys extends AsyncTask<Void, Void, ArrayList<PagesSearch_Model>> {

		@Override
		protected ArrayList<PagesSearch_Model> doInBackground(Void... params) {

			String hitURL = "https://graph.facebook.com/search?q="
					+ MainSingleTon.searchKey + "+&type=place&access_token="
					+ MainSingleTon.accesstoken;

			JSONParseraa jsonParser = new JSONParseraa();

			JSONObject jsonObject = jsonParser.getJSONFromUrl(hitURL);

			System.out.println("--------------PLACES-------------PLACES= "
					+ jsonObject);

			try {
				JSONArray jsonArray = jsonObject.getJSONArray("data");

				if (jsonArray.length() != 0) {

					for (int i = 0; i < jsonArray.length(); i++) 
					{

						PagesSearch_Model pagessearch_model = new PagesSearch_Model();
						JSONObject jsonObject2 = jsonArray.getJSONObject(i);
						pagessearch_model.setPgID(jsonObject2.getString("id"));
						pagessearch_model.setPgName(jsonObject2.getString("name"));
						pagessearch_model.setPgCategory(jsonObject2.getString("category"));

						if(jsonObject2.has("location"))
						{
							JSONObject jsonObject3 = jsonObject2.getJSONObject("location");

							if(jsonObject3.has("city"))
							{
								String myLocation = jsonObject3.getString("city")+ jsonObject3.getString("country");
								pagessearch_model.setLocation(myLocation);
							}

						}
						placesArrayList.add(pagessearch_model);

					}
					JSONObject jsonObject2 = jsonObject.getJSONObject("paging");

					if (jsonObject2.has("next")) {

						cursor = jsonObject2.getString("next");

					} else {
						cursor = null;
					}

				} else {

					System.out.println("SEarch PLACES are not available ");

					cursor = null;
				}

			} catch (JSONException e) {

				e.printStackTrace();
			}

			return placesArrayList;
		}

		@Override
		protected void onPostExecute(ArrayList<PagesSearch_Model> result) {
			super.onPostExecute(result);

			placesSearch_Adapter = new PlacesSearch_Adapter(getActivity(), placesArrayList);

			placesSearclistView.setAdapter(placesSearch_Adapter);

			progressbar.setVisibility(View.INVISIBLE);

			isAlredyScrolloing = false;

			if (placesArrayList.isEmpty()) {

				isAlredyScrolloing = true;

				placesSearclistView.setVisibility(View.INVISIBLE);

				noplaces.setVisibility(View.VISIBLE);
			}
		}

	}

	public class LoadMoreSearchPeopleAys extends AsyncTask<String, Void, ArrayList<PagesSearch_Model>> {

		@Override
		protected ArrayList<PagesSearch_Model> doInBackground(String... params) {

			String hitURL = params[0];

			JSONParseraa jsonParser = new JSONParseraa();

			JSONObject jsonObject = jsonParser.getJSONFromUrl(hitURL);

			try {
				JSONArray jsonArray = jsonObject.getJSONArray("data");

				if (jsonArray.length() != 0) {

					for (int i = 0; i < jsonArray.length(); i++) 
					{
						PagesSearch_Model pagessearch_model = new PagesSearch_Model();
						JSONObject jsonObject2 = jsonArray.getJSONObject(i);
						pagessearch_model.setPgID(jsonObject2.getString("id"));
						pagessearch_model.setPgName(jsonObject2.getString("name"));
						pagessearch_model.setPgCategory(jsonObject2.getString("category"));

						if(jsonObject2.has("location"))
						{
							JSONObject jsonObject3 = jsonObject2.getJSONObject("location");

							if(jsonObject3.has("city"))
							{
								String myLocation = jsonObject3.getString("city")+ jsonObject3.getString("country");
								pagessearch_model.setLocation(myLocation);
							}

						}
						placesArrayList.add(pagessearch_model);

					}
					JSONObject jsonObject2 = jsonObject.getJSONObject("paging");

					if (jsonObject2.has("next")) {

						cursor = jsonObject2.getString("next");

					} else {
						cursor = null;
					}

				} else {

					cursor = null;
					
					System.out.println("SEarch PLACES in LOAD MORE are not available ");
				}

			} catch (JSONException e) {

				e.printStackTrace();
			}

			return placesArrayList;
		}

		@Override
		protected void onPostExecute(ArrayList<PagesSearch_Model> result) {
			
			super.onPostExecute(result);

			placesSearch_Adapter.notifyDataSetChanged();

			isAlredyScrolloing = false;

			viewGroup.setVisibility(View.INVISIBLE);

		}

	}
}
