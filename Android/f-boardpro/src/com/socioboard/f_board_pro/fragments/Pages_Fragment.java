package com.socioboard.f_board_pro.fragments;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.socioboard.f_board_pro.MainActivity;
import com.socioboard.f_board_pro.R;
import com.socioboard.f_board_pro.adapter.Pages_Adapter;
import com.socioboard.f_board_pro.database.util.JSONParseraa;
import com.socioboard.f_board_pro.database.util.LoadMoreListView;
import com.socioboard.f_board_pro.database.util.LoadMoreListView.OnLoadMoreListener;
import com.socioboard.f_board_pro.database.util.MainSingleTon;
import com.socioboard.f_board_pro.models.PagesModel;

public class Pages_Fragment extends ListFragment 
{

	ListView lv;
	String pagesNextUrl =null;
	public  ArrayList<PagesModel> mListItems;

	boolean isPapesaAvailable =false;
	FragmentManager fragmentManager;

	ProgressBar progressbar;
	String mainUrl = null ;
	public View rootview =  null;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{

		rootview	= inflater.inflate(R.layout.pages_fragment, container, false);


		return rootview;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState)
	{
		
		fragmentManager = getFragmentManager();
		

		if(!MainSingleTon.isPAgesLoaded)
		{
			progressbar = (ProgressBar) view.findViewById(R.id.progressbar);

			mainUrl ="https://graph.facebook.com/"+MainSingleTon.userid+"/likes?access_token="+MainSingleTon.accesstoken;

			progressbar.setVisibility(View.VISIBLE);

			new LoadDataTask().execute();
		}

		lv = getListView();
		mListItems = new ArrayList<PagesModel>();

		mListItems.addAll(MainSingleTon.pagesArrayList);


		((LoadMoreListView) getListView()).setOnLoadMoreListener(new OnLoadMoreListener() {
			public void onLoadMore() {
				// Do the work to load more items at the end of list
				// here

				new LoadDataTask().execute();
			}
		});

		getListView().setOnItemClickListener(new OnItemClickListener()
		{

			@Override
			public void onItemClick(AdapterView<?> parent, View view,int position, long id) 
			{
				MainSingleTon.pageFBiD =MainSingleTon.pagesArrayList.get(position).getPgID();
				System.out.println("get page..............");
				/*Fragment fragment = new PagesFeed_Fragment();
				
				fragmentManager.beginTransaction().add(R.id.main_content, fragment).commit();*/
				MainActivity.swipeFragment(new PagesFeed_Fragment());
				 
			}
		});
	}

	private class LoadDataTask extends AsyncTask<Void, Void, Void> {

		
		@Override
		protected Void doInBackground(Void... params)
		{
			MainSingleTon.pagesArrayList.clear();

			if(isPapesaAvailable)
			{
				mainUrl = pagesNextUrl;
			} 
			if(mainUrl!=null)
			{
				JSONParseraa jsonParser = new JSONParseraa();
				
				JSONObject jsonObject = jsonParser.getJSONFromUrl(mainUrl);

				try {
					
					JSONArray jsonArray =  jsonObject.getJSONArray("data");

					if(jsonArray.length()!=0)
					{
						for(int i = 0; i<jsonArray.length();i++)
						{

							PagesModel pagesModel = new PagesModel();

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

							MainSingleTon.pagesArrayList.add(pagesModel);
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
							isPapesaAvailable =true;
							pagesNextUrl = js56.getString("next");
							mListItems.addAll(MainSingleTon.pagesArrayList);
						}
						else
						{
							isPapesaAvailable =false;
							mainUrl =null;

						}

					}

				} catch (JSONException e) {

					e.printStackTrace();
				}
			}
			else
			{
				getActivity().runOnUiThread(new Runnable() {

					@Override
					public void run() {

						Toast.makeText(getActivity(), "----- End ;-) ----", Toast.LENGTH_SHORT).show();
						MainSingleTon.isPAgesLoaded = true;
					}
				});
			}
			if (isCancelled()) {
				return null;
			}

			// Simulates a background task
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) 
		{
			if(progressbar!=null)
			{
				progressbar.setVisibility(View.INVISIBLE);
			}
			super.onPostExecute(result);

			Pages_Adapter pages_Adapter = new Pages_Adapter(getActivity(), MainSingleTon.pagesArrayList);

			setListAdapter(pages_Adapter);

			pages_Adapter.notifyDataSetChanged();
		}

		@Override
		protected void onCancelled() 
		{
			// Notify the loading more operation has finished
			((LoadMoreListView) getListView()).onLoadMoreComplete();
			if(progressbar!=null)
			{
				progressbar.setVisibility(View.INVISIBLE);
			}		
		}
	}


}
