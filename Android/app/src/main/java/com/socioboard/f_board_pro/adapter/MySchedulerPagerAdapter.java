package com.socioboard.f_board_pro.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.socioboard.f_board_pro.search_fragments.EventsSearch_Fragment;
import com.socioboard.f_board_pro.search_fragments.GroupsSearch_Fragment;
import com.socioboard.f_board_pro.search_fragments.PagesSearch_Fragment;
import com.socioboard.f_board_pro.search_fragments.PeopleSearch_Fragment;
import com.socioboard.f_board_pro.search_fragments.PlacesSearch_Fragment;

public class MySchedulerPagerAdapter extends FragmentStatePagerAdapter {


	public MySchedulerPagerAdapter(FragmentManager fragmentManager) {
		super(fragmentManager);
	}

	private final String[] TITLES = { "Pages","Groups","People","Events","Places"};

	@Override
	public CharSequence getPageTitle(int position) {
		System.out.println("inside MySchedulerPagerAdapter");
		return TITLES[position];
	}

	@Override
	public Fragment getItem(int i) {
		System.out.println("inside getItem=====================");
		Fragment fragment = null ; 

		switch(i)
		{
		case 0:
			System.out.println("Pagesearch Fragment=======");
			fragment = new PagesSearch_Fragment();
			break;
		case 1:
			System.out.println("GroupSearch_Fragment==========");
			fragment = new GroupsSearch_Fragment();
			break;
		case 2:
			System.out.println("PeopleSearch Fragment============");
			fragment = new PeopleSearch_Fragment();
			break;
		case 3:
			System.out.println("EventSearch Fragment==========");
			fragment = new EventsSearch_Fragment();
			break;
		case 4:
			System.out.println("PlaceSearch Fragment===========");
			fragment = new PlacesSearch_Fragment();
			break;
		}
		return fragment;
	}

	@Override
	public int getCount() {

		return TITLES.length;
	}

	@Override
	public int getItemPosition(Object object) {
		return POSITION_NONE;
	}
}