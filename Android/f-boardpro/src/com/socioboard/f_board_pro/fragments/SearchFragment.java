package com.socioboard.f_board_pro.fragments;

import java.net.URLEncoder;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.socioboard.f_board_pro.R;
import com.socioboard.f_board_pro.adapter.MySchedulerPagerAdapter;
import com.socioboard.f_board_pro.database.util.MainSingleTon;

public class SearchFragment  extends Fragment {

	MySchedulerPagerAdapter pageAdapter;
	View  rootView;
	EditText edsearchView1;
	ImageView button1Search;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		rootView = inflater.inflate(R.layout.fragment_search, container, false);

		final MySchedulerPagerAdapter pageAdapter = new MySchedulerPagerAdapter(getChildFragmentManager());

		final ViewPager pager = (ViewPager) rootView.findViewById(R.id.horizontalScro);

		button1Search         = (ImageView) rootView.findViewById(R.id.button1Search);

		edsearchView1         = (EditText) rootView.findViewById(R.id.edsearchView1);
		
		if(MainSingleTon.searchKey1==null)
		{
			  
		}else
		{
			edsearchView1.setText(MainSingleTon.searchKey1); 
			
			pager.setAdapter(pageAdapter);
		}

		button1Search.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				if( !edsearchView1.getText().toString().isEmpty())
				{
					String edtString = edsearchView1.getText().toString();
					MainSingleTon.searchKey1= edtString;
					MainSingleTon.searchKey = URLEncoder.encode(edtString).replace("+", "%20");

					pager.setAdapter(pageAdapter);
				}else
				{
					Toast.makeText(getActivity(), "Please give text to search", Toast.LENGTH_SHORT).show();
				}
				
				View view = getActivity().getCurrentFocus();
				if (view != null) {  
				    InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(getActivity().INPUT_METHOD_SERVICE);
				    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
				}
			}
		});

		

		return rootView;
	}


}