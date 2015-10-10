package com.socioboard.f_board_pro.fragments;

import java.util.ArrayList;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.socioboard.f_board_pro.R;
import com.socioboard.f_board_pro.SchedulleComposeActivity;
import com.socioboard.f_board_pro.adapter.SchTweetsAdapter;
import com.socioboard.f_board_pro.database.util.F_Board_LocalData;
import com.socioboard.f_board_pro.imagelib.ImageLoader;
import com.socioboard.f_board_pro.models.SchPostModel;

public class SchedulerFragment extends Fragment {

	View rootView;

	SchTweetsAdapter schTweetsAdapter;

	ArrayList<SchPostModel> schFeedArrlist = new ArrayList<SchPostModel>();

	F_Board_LocalData database;

	ListView listview;

	ImageView imdNewSchdulle, imageViewAddUsers;

	RelativeLayout reltop; 
	
	TextView txtCount;

	Dialog builder;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		database = new F_Board_LocalData(getActivity());

		rootView = inflater.inflate(R.layout.scheduler_fragment, container, false);

		listview = (ListView) rootView.findViewById(R.id.listView1);

		txtCount = (TextView) rootView.findViewById(R.id.textView2);

		imdNewSchdulle = (ImageView) rootView.findViewById(R.id.imageViewNewTWeet);

		reltop = (RelativeLayout) rootView.findViewById(R.id.reltop); 
		
		schFeedArrlist = database.getAllSchedulledFeeds();

		schTweetsAdapter = new SchTweetsAdapter(getActivity(), schFeedArrlist);

		txtCount.setText("Scheduled posts : "+schFeedArrlist.size());

		listview.setAdapter(schTweetsAdapter);


		ImageLoader imageLoader = new ImageLoader(getActivity());

		imageLoader.clearCache();

		imdNewSchdulle.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				Intent intent = new Intent(getActivity(), SchedulleComposeActivity.class);

				getActivity().startActivity(intent);

			}
		});
		
		
		reltop.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {

				imdNewSchdulle.callOnClick();
			}
		});


		listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {


			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				//SchPostModel model = schFeedArrlist.get(position);
				//No more working
			}
		});

		return rootView;
	}

	@Override
	public void onResume() {

		super.onResume();

		schFeedArrlist = database.getAllSchedulledFeeds();

		schTweetsAdapter = new SchTweetsAdapter(getActivity(), schFeedArrlist);

		listview.setAdapter(schTweetsAdapter);

		txtCount.setText("Scheduled posts : "+schFeedArrlist.size());

	}


}
