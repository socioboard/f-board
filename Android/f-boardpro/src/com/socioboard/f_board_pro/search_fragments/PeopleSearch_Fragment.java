package com.socioboard.f_board_pro.search_fragments;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.net.Uri;
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

import com.socioboard.f_board_pro.R;
import com.socioboard.f_board_pro.adapter.PeopleSearch_Adapter;
import com.socioboard.f_board_pro.database.util.JSONParseraa;
import com.socioboard.f_board_pro.database.util.MainSingleTon;
import com.socioboard.f_board_pro.models.PagesSearch_Model;

public class PeopleSearch_Fragment extends Fragment implements OnScrollListener {

	View rootview;
	public ArrayList<PagesSearch_Model> peopleArrayList;
	ListView peopleSearclistView;
	ProgressBar progressbar;
	ImageView nopeople;

	ViewGroup viewGroup;
	String cursor = null;
	boolean isAlredyScrolloing = true;
	PeopleSearch_Adapter peopleSearch_Adapter;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		rootview = inflater.inflate(R.layout.people_search_fragment, container, false);
		peopleArrayList = new ArrayList<PagesSearch_Model>();

		peopleSearclistView = (ListView)    rootview.findViewById(R.id.peopleSearclistView);
		nopeople             = (ImageView)    rootview.findViewById(R.id.nopeople);
		progressbar         = (ProgressBar) rootview.findViewById(R.id.progressbar);

		peopleSearclistView.setOnScrollListener(PeopleSearch_Fragment.this);

		progressbar.setVisibility(View.VISIBLE);

		addFooterView();

		new SearchPagesAys().execute();
		

		peopleSearclistView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				
				MainSingleTon.pgID       = peopleArrayList.get(position).getPgID();
				MainSingleTon.pgNAME     = peopleArrayList.get(position).getPgName();
				MainSingleTon.pgCategory = peopleArrayList.get(position).getPgCategory();
				
				String url = "http://www.facebook.com/"+MainSingleTon.pgID ;
				Intent i = new Intent(Intent.ACTION_VIEW);
				i.setData(Uri.parse(url));
				startActivity(i);
				
			}
		});

		return rootview;
	}

	public class SearchPagesAys extends AsyncTask<Void, Void, ArrayList<PagesSearch_Model>> {

		@Override
		protected ArrayList<PagesSearch_Model> doInBackground(Void... params) {

			String hitURL = "https://graph.facebook.com/search?q="
					+ MainSingleTon.searchKey + "+&type=user&access_token="
					+ MainSingleTon.accesstoken;

			JSONParseraa jsonParser = new JSONParseraa();

			JSONObject jsonObject = jsonParser.getJSONFromUrl(hitURL);

			System.out.println("--------------People-------------People= "
					+ jsonObject);

			try {
				JSONArray jsonArray = jsonObject.getJSONArray("data");

				if (jsonArray.length() != 0) {
					for (int i = 0; i < jsonArray.length(); i++) {
						PagesSearch_Model pagessearch_model = new PagesSearch_Model();
						JSONObject jsonObject2 = jsonArray.getJSONObject(i);
						pagessearch_model.setPgID(jsonObject2.getString("id"));
						pagessearch_model.setPgName(jsonObject2.getString("name"));
						pagessearch_model.setLikesCount(null);
						peopleArrayList.add(pagessearch_model);
					}
					JSONObject jsonObject2 = jsonObject.getJSONObject("paging");
					
					if (jsonObject2.has("next")) {
						
						cursor = jsonObject2.getString("next");
						
					} else {
						cursor = null;
					}

				} else {
					 
					System.out.println("SEarch People are not available ");
					
					cursor = null;
				}

			} catch (JSONException e) {

				e.printStackTrace();
			}

			return peopleArrayList;
		}

		@Override
		protected void onPostExecute(ArrayList<PagesSearch_Model> result) {
			super.onPostExecute(result);

			peopleSearch_Adapter = new PeopleSearch_Adapter(getActivity(), peopleArrayList);

			peopleSearclistView.setAdapter(peopleSearch_Adapter);

			progressbar.setVisibility(View.INVISIBLE);

			isAlredyScrolloing = false;

			if (peopleArrayList.isEmpty()) {
				
				isAlredyScrolloing = true;
				
				peopleSearclistView.setVisibility(View.INVISIBLE);
				
				nopeople.setVisibility(View.VISIBLE);
			}
		}

	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {

	}

	private void addFooterView() {

		LayoutInflater inflater = getActivity().getLayoutInflater();

		viewGroup = (ViewGroup) inflater.inflate(R.layout.progress_layout, peopleSearclistView, false);

		peopleSearclistView.addFooterView(viewGroup);

		viewGroup.setVisibility(View.INVISIBLE);

	}

	private void loadFromTHisCursor(String cursor) {

		new LoadMoreSearchPeopleAys().execute(cursor);

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
					
					for (int i = 0; i < jsonArray.length(); i++) {
						
						PagesSearch_Model pagessearch_model = new PagesSearch_Model();
						JSONObject jsonObject2 = jsonArray.getJSONObject(i);
						pagessearch_model.setPgID(jsonObject2.getString("id"));
						pagessearch_model.setPgName(jsonObject2.getString("name"));
						pagessearch_model.setLikesCount(null);
						peopleArrayList.add(pagessearch_model);
						
					}

					JSONObject jsonObject2 = jsonObject.getJSONObject("paging");

					if (jsonObject2.has("next")) {
						
						cursor = jsonObject2.getString("next");
						
					} else {
						
						cursor = null;
					}

				} else {

					cursor = null;
					System.out.println("SEarch Peoplle are not available ");
				}

			} catch (JSONException e) {

				e.printStackTrace();
			}

			return peopleArrayList;
		}

		@Override
		protected void onPostExecute(ArrayList<PagesSearch_Model> result) {
			
			super.onPostExecute(result);

			peopleSearch_Adapter.notifyDataSetChanged();

			isAlredyScrolloing = false;

			viewGroup.setVisibility(View.INVISIBLE);

		}

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

}
