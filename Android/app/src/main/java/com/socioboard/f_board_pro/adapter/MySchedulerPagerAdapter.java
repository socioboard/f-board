package com.socioboard.f_board_pro.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.socioboard.f_board_pro.search_fragments.EventsSearch_Fragment;
import com.socioboard.f_board_pro.search_fragments.GroupsSearch_Fragment;
import com.socioboard.f_board_pro.search_fragments.PagesSearch_Fragment;
import com.socioboard.f_board_pro.search_fragments.PeopleSearch_Fragment;
import com.socioboard.f_board_pro.search_fragments.PlacesSearch_Fragment;

public class MySchedulerPagerAdapter extends FragmentPagerAdapter {

	public MySchedulerPagerAdapter(FragmentManager fragmentManager) {
		super(fragmentManager);
	}

	private final String[] TITLES = { "Pages","Groups","People","Events","Places"};

	@Override
	public CharSequence getPageTitle(int position) {
		return TITLES[position];
	}

	@Override
	public Fragment getItem(int i) {
		Fragment fragment = null ; 

		switch(i)
		{
		case 0:
			fragment = new PagesSearch_Fragment();
			break;
		case 1:
			fragment = new GroupsSearch_Fragment();
			break;
		case 2:
			fragment = new PeopleSearch_Fragment();
			break;
		case 3:
			fragment = new EventsSearch_Fragment();
			break;
		case 4:
			fragment = new PlacesSearch_Fragment();
			break;
	 
		}
		return fragment;
	}

	@Override
	public int getCount() {

		return TITLES.length;
	}
}