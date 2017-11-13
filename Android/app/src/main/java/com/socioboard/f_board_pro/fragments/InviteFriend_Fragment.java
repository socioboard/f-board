package com.socioboard.f_board_pro.fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.socioboard.f_board_pro.R;
import com.socioboard.f_board_pro.adapter.InvitFriend_Adapter;
import com.socioboard.f_board_pro.database.util.JSONParseraa;
import com.socioboard.f_board_pro.database.util.MainSingleTon;
import com.socioboard.f_board_pro.models.FriendModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class InviteFriend_Fragment extends Fragment implements OnScrollListener{


	InvitFriend_Adapter invitFriend_Adapter;
	ViewGroup viewGroup;

	String cursor =  null;
	boolean isAlredyScrolloing = true;

	View rootview;
	TextView nofriends;
	ListView userfriendListView;
	ProgressBar progressBar;
	ArrayList<FriendModel> friendArraylist;
	
	public SparseBooleanArray sparseBooleanArray;

	String nextPageToken = null;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		rootview = inflater.inflate(R.layout.invite_friend_fragment, container,	false);

		nofriends          = (TextView) rootview.findViewById(R.id.nofriends);
		userfriendListView = (ListView) rootview.findViewById(R.id.listview);
		progressBar = (ProgressBar)rootview.findViewById(R.id.progressBar1);
		progressBar.setVisibility(View.VISIBLE);

		friendArraylist    = new ArrayList<FriendModel>();

		nofriends.setVisibility(View.INVISIBLE);

		userfriendListView.setOnScrollListener(InviteFriend_Fragment.this);
		
		addFooterView();

		getFriendList();

		userfriendListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				
				System.out.println("ssssssssssssssssss");
				
				 switch(parent.getId()) {
				 
			        case R.id.fpic: 
			        	System.out.println("dddpicccccccc");
			            break;
			        case R.id.fname:
			        	System.out.println("namew");
			            break;
			        }
			}
		});


		return rootview;
	}
	private void addFooterView() {

		LayoutInflater inflater = getActivity().getLayoutInflater();

		viewGroup = (ViewGroup) inflater.inflate(R.layout.progress_layout, userfriendListView, false);

		userfriendListView.addFooterView(viewGroup);

		viewGroup.setVisibility(View.INVISIBLE);

	}
	private void loadFromTHisCursor(String cursor) {

		 new LoadMoreSearchPagesAys().execute(cursor);

	}
	
	public void getFriendList()
	{
		Bundle params = new Bundle();

		params.putString(AccessToken.ACCESS_TOKEN_KEY, 	MainSingleTon.accesstoken);

		params.putString("fields", "id,name,picture.width(400).height(400)");

		GraphRequest graphRequest = GraphRequest.newGraphPathRequest(
				MainSingleTon.dummyAccesstoken, "me/taggable_friends",
				new GraphRequest.Callback() {

					@Override
					public void onCompleted(GraphResponse response) {

						System.out.println("response ="
								+ response);
						// writeToFile("SURESHAAA", response.toString());

						if(response.getJSONObject()!=null)
						{
							JSONObject jsonObject = response.getJSONObject();

							try {

								JSONArray jsonArray = jsonObject.getJSONArray("data");

								for (int i = 0; i < jsonArray.length(); i++) {
									FriendModel friendModel = new FriendModel();
									JSONObject jsonObject2 = jsonArray.getJSONObject(i);

									friendModel.setFriendId(jsonObject2.getString("id"));
									friendModel.setFriendName(jsonObject2.getString("name"));
									friendModel.setFriendPic(jsonObject2.getJSONObject("picture").getJSONObject("data").getString("url"));

									System.out.println("-------Name-----"+ jsonObject2.getString("name"));
									System.out.println("--------URL-----= "+ jsonObject2.getJSONObject("picture").getJSONObject("data").getString("url"));

									friendArraylist.add(friendModel);
								}

								if(jsonObject.has("paging"))
								{
									JSONObject jsonObject2=	jsonObject.getJSONObject("paging");

									System.out.println("jsonObject2 ="+jsonObject2);
									
									if(jsonObject2.has("next"))
									{
										cursor = jsonObject2.getString("next");
									}else
									{
										cursor= null;
									}

								}else
								{
									System.out.println("No pagees");
									
									cursor= null;
								}
								sparseBooleanArray = new SparseBooleanArray(friendArraylist.size());

								for (int i = 0; i < friendArraylist.size(); ++i) {

									sparseBooleanArray.put(i, false);

								}
								
								System.out.println("friendArraylist========= "+friendArraylist.size());		
								
								if(friendArraylist.isEmpty())
								{
									isAlredyScrolloing =true;
									userfriendListView.setVisibility(View.INVISIBLE);
									viewGroup.setVisibility(View.GONE);
									nofriends.setVisibility(View.VISIBLE);
									progressBar.setVisibility(View.INVISIBLE);
								}
								else
								{
								  invitFriend_Adapter = new InvitFriend_Adapter(getActivity(), friendArraylist, 0, null,sparseBooleanArray);
								  userfriendListView.setAdapter(invitFriend_Adapter);
								  isAlredyScrolloing =false;
									progressBar.setVisibility(View.INVISIBLE);
								}
 
							} catch (JSONException e) {
								e.printStackTrace();
							}
						}else
						{
							System.out.println("No friends Available");
							nofriends.setVisibility(View.VISIBLE);
						}
					}
				});

		graphRequest.setParameters(params);
		graphRequest.executeAsync();
	}
	public class LoadMoreSearchPagesAys extends AsyncTask<String, Void, ArrayList<FriendModel>> {

		@Override
		protected ArrayList<FriendModel> doInBackground(String... params) {

			String hitURL = params[0];

			JSONParseraa jsonParser = new JSONParseraa();

			JSONObject jsonObject = jsonParser.getJSONFromUrl(hitURL);

			try {
				JSONArray jsonArray = jsonObject.getJSONArray("data");

				if (jsonArray.length() != 0) {

					for (int i = 0; i < jsonArray.length(); i++) {

						FriendModel friendModel = new FriendModel();
						JSONObject jsonObject2 = jsonArray.getJSONObject(i);

						friendModel.setFriendId(jsonObject2.getString("id"));
						friendModel.setFriendName(jsonObject2.getString("name"));
						friendModel.setFriendPic(jsonObject2.getJSONObject("picture").getJSONObject("data").getString("url"));

						System.out.println("-------Name-----"+ jsonObject2.getString("name"));
						System.out.println("--------URL-----= "+ jsonObject2.getJSONObject("picture").getJSONObject("data").getString("url"));

						friendArraylist.add(friendModel);
					
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

			return friendArraylist;
		}

		@Override
		protected void onPostExecute(ArrayList<FriendModel> result) {
			super.onPostExecute(result);

			invitFriend_Adapter.notifyDataSetChanged();

			isAlredyScrolloing = false;

			viewGroup.setVisibility(View.INVISIBLE);

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
}
