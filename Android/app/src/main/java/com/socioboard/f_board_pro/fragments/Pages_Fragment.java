package com.socioboard.f_board_pro.fragments;

/**
 *Use this class to retrieve the multiple Mainsingeton Ids
 *Using this pgID it will retreive the entire data 
 */

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AutoCompleteTextView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.socioboard.f_board_pro.MainActivity;
import com.socioboard.f_board_pro.R;
import com.socioboard.f_board_pro.adapter.CustomerAdapter;
import com.socioboard.f_board_pro.adapter.Pages_Adapter;
import com.socioboard.f_board_pro.database.util.JSONParseraa;
import com.socioboard.f_board_pro.database.util.MainSingleTon;
import com.socioboard.f_board_pro.models.PagesSearch_Model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Pages_Fragment extends Fragment implements OnScrollListener 
{

	ListView listView;

	public  ArrayList<PagesSearch_Model> mListItems;

	boolean isAlredyScrolloing = true;
	TextView button1, clearAll;
	ProgressBar progressbar;
	TextView nopages;
	ViewGroup viewGroup;
	Pages_Adapter pages_Adapter;
	String cursor =  null;
	RelativeLayout headerLlt;
	AutoCompleteTextView autoCompleteTextView1;
	CustomerAdapter adapter;
	public View rootview =  null;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		rootview = inflater.inflate(R.layout.pages_fragment, container, false);

		LoadAd();

		listView = (ListView) rootview.findViewById(R.id.listView);

		progressbar = (ProgressBar) rootview.findViewById(R.id.progressbar);

		progressbar.setVisibility(View.VISIBLE);

		nopages  = (TextView) rootview.findViewById(R.id.nopages);

		button1  = (TextView) rootview.findViewById(R.id.button1); 

		clearAll = (TextView) rootview.findViewById(R.id.clearAll);

		autoCompleteTextView1 = (AutoCompleteTextView) rootview.findViewById(R.id.autoCompleteTextView1);

		nopages.setVisibility(View.INVISIBLE);

		headerLlt = (RelativeLayout) rootview.findViewById(R.id.headerLlt);

		addFooterView();

		progressbar.setVisibility(View.VISIBLE);

		listView.setOnScrollListener(Pages_Fragment.this);

		mListItems = new ArrayList<PagesSearch_Model>();

		new LoadDataTask().execute();

		button1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				for (int i = 0; i < mListItems.size(); i++) {

					if(MainSingleTon.pageShareagonList.contains(mListItems.get(i).getPgID()))
					{

					}else
					{

						MainSingleTon.pageShareagonList.add(mListItems.get(i).getPgID());
					}

				}
				clearAll.setText("Clear all selected pages from Shareagon ="+MainSingleTon.pageShareagonList.size());

				button1.setVisibility(View.INVISIBLE);

				clearAll.setVisibility(View.VISIBLE);

				MainActivity.makeToast("Total pages added "+MainSingleTon.pageShareagonList.size());

			}
		});

		if(MainSingleTon.pageShareagonList.size()>0)
		{
			button1.setVisibility(View.INVISIBLE);

			clearAll.setVisibility(View.VISIBLE);

			MainActivity.makeToast("Total pages added "+MainSingleTon.pageShareagonList.size());

		}else
		{


		}

		clearAll.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				button1.setVisibility(View.VISIBLE);

				MainSingleTon.pageShareagonList.clear();

				button1.setText("Add all pages to Shareagon = "+MainSingleTon.pageShareagonList.size());

				clearAll.setVisibility(View.INVISIBLE);


			}
		});

		/*listView.setOnItemClickListener(new OnItemClickListener()
		{
			@Override
			public void onItemClick(AdapterView<?> parent, View view,int position, long id) 
			{
				MainSingleTon.pgID      = mListItems.get(position).getPgID();
				MainSingleTon.pgCategory = mListItems.get(position).getPgCategory();
				MainSingleTon.pgNAME= mListItems.get(position).getPgName();
				Intent intent = new Intent(getActivity(), AllInOneSearchFeeds.class);

				startActivity(intent);

			}
		});
		 */
	
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

		viewGroup = (ViewGroup) inflater.inflate(R.layout.progress_layout, listView, false);

		listView.addFooterView(viewGroup);

		viewGroup.setVisibility(View.INVISIBLE);

	}

	private class LoadDataTask extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... params)
		{
			String hirURL ="https://graph.facebook.com/"+MainSingleTon.userid+"/likes?access_token="+MainSingleTon.accesstoken;

			JSONParseraa jsonParser = new JSONParseraa();

			JSONObject jsonObject = jsonParser.getJSONFromUrl(hirURL);

			try {

				JSONArray jsonArray =  jsonObject.getJSONArray("data");

				if(jsonArray.length()!=0)
				{
					for(int i = 0; i<jsonArray.length();i++)
					{
						PagesSearch_Model pagesModel = new PagesSearch_Model();

						JSONObject jsonObject2 = jsonArray.getJSONObject(i);

						if(jsonObject2.has("category"))
						{
							pagesModel.setPgCategory(jsonObject2.getString("category"));

						}else
						{
						}
						if(jsonObject2.has("name"))
						{
							pagesModel.setPgName(jsonObject2.getString("name"));
						}else
						{
						}
						if(jsonObject2.has("id"))
						{

							pagesModel.setPgID(jsonObject2.getString("id"));
						}else
						{
						}
						if(jsonObject2.has("created_time"))
						{

							pagesModel.setPgCreatedTime(jsonObject2.getString("created_time"));
						}else
						{

						}
						pagesModel.setLikesCount(null);

						mListItems.add(pagesModel);
					}
				}
				else
				{ 
					getActivity().runOnUiThread(new Runnable() {

						@Override
						public void run() {

							Toast.makeText(getActivity(), "-----!!No Pages u liked!!-----", Toast.LENGTH_SHORT).show();

						}
					});

				}

				if(jsonObject.has("paging"))
				{
					JSONObject js56 =  jsonObject.getJSONObject("paging");

					if(js56.has("next"))
					{

						System.out.println(mListItems.size()+"---------------js56----------"+js56.getString("next"));
						cursor	=	js56.getString("next");

					}
					else
					{
						System.out.println("---------------NOOOO----------");
						cursor= null;
					}

				}

			} catch (JSONException e) {

				e.printStackTrace();
			}


			if (isCancelled()) {
				return null;
			}

			return null;
		}

		@Override
		protected void onPostExecute(Void result) 
		{
			super.onPostExecute(result);

			if(mListItems.size()>0)
			{
				//pages_Adapter = new Pages_Adapter(getActivity(), mListItems);
				//
				progressbar.setVisibility(View.INVISIBLE);
				nopages.setVisibility(View.INVISIBLE);
				listView.setVisibility(View.VISIBLE);
				isAlredyScrolloing = false;
				
				adapter  = new CustomerAdapter(getActivity(), R.layout.pages_rowitem, mListItems);
				listView.setAdapter(adapter);
				autoCompleteTextView1.setThreshold(1);
				autoCompleteTextView1.setAdapter(adapter);//setting the adapter data into the AutoCompleteTextView  
				autoCompleteTextView1.setTextColor(Color.BLUE);
			}
			else
			{
				isAlredyScrolloing = true;
				progressbar.setVisibility(View.INVISIBLE);
				nopages.setVisibility(View.VISIBLE);
				viewGroup.setVisibility(View.GONE);
			}


		}

		@Override
		protected void onCancelled() 
		{

			if(progressbar!=null)
			{
				progressbar.setVisibility(View.INVISIBLE);
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
	public class LoadMoreSearchPagesAys extends AsyncTask<String, Void, String> {

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
						PagesSearch_Model pagesModel = new PagesSearch_Model();

						JSONObject jsonObject2 = jsonArray.getJSONObject(i);

						if(jsonObject2.has("category"))
						{
							pagesModel.setPgCategory(jsonObject2.getString("category"));

						}else
						{
						}
						if(jsonObject2.has("name"))
						{
							pagesModel.setPgName(jsonObject2.getString("name"));
						}else
						{
						}
						if(jsonObject2.has("id"))
						{

							pagesModel.setPgID(jsonObject2.getString("id"));
						}else
						{
						}
						if(jsonObject2.has("created_time"))
						{

							pagesModel.setPgCreatedTime(jsonObject2.getString("created_time"));
						}else
						{

						}
						pagesModel.setLikesCount(null);

						mListItems.add(pagesModel);
					}
				}

				if(jsonObject.has("paging"))
				{
					JSONObject js56 =  jsonObject.getJSONObject("paging");

					if(js56.has("next"))
					{

						cursor	=	js56.getString("next");
					}
					else
					{
						cursor= null;
					}

				}else
				{
					cursor= null;
				}

			} catch (JSONException e) {

				e.printStackTrace();
			}


			if (isCancelled()) {
				return null;
			}


			return null;
		}

		@Override
		protected void onPostExecute(String result) 
		{
			super.onPostExecute(result);

			//pages_Adapter.notifyDataSetChanged();
			adapter.notifyDataSetChanged();

			isAlredyScrolloing = false;

		}


	}


}
