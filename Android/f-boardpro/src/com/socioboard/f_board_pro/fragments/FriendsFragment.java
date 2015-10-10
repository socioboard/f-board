package com.socioboard.f_board_pro.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TabHost;
import android.widget.TextView;

import com.socioboard.f_board_pro.R;
import com.socioboard.f_board_pro.imagelib.DummyTabContent;

public class FriendsFragment extends Fragment  
{
	View rootView;

	TabHost tHost;	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState)
	{
		rootView = inflater.inflate(R.layout.user_friends_fragment_tabs, container, false);
		//+++++++++++++++++++++++++++++++
		tHost = (TabHost) rootView.findViewById(android.R.id.tabhost);
		tHost.setup();

		/** Defining Tab Change Listener event. This is invoked when tab is changed */
		TabHost.OnTabChangeListener tabChangeListener = new TabHost.OnTabChangeListener() {

			@Override
			public void onTabChanged(String tabId) {
				android.support.v4.app.FragmentManager fm =   getActivity().getSupportFragmentManager();
				ExistingFriendFragment existingFriendsFragment = (ExistingFriendFragment) fm.findFragmentByTag("android");
				InviteFriend_Fragment allFreindsFragment = (InviteFriend_Fragment) fm.findFragmentByTag("apple");
				android.support.v4.app.FragmentTransaction ft = fm.beginTransaction();

				/** Detaches the existingFriendsFragment if exists */
				if(existingFriendsFragment!=null)
					ft.detach(existingFriendsFragment);

				/** Detaches the allFreindsFragment if exists */
				if(allFreindsFragment!=null)
					ft.detach(allFreindsFragment);

				/** If current tab is android */
				if(tabId.equalsIgnoreCase("android")){

					if(existingFriendsFragment==null){		
						/** Create AndroidFragment and adding to fragmenttransaction */
						ft.add(R.id.realtabcontent, new ExistingFriendFragment(), "android");						
					}else{
						/** Bring to the front, if already exists in the fragmenttransaction */
						ft.attach(existingFriendsFragment);						
					}

				}else{	/** If current tab is apple */
					if(allFreindsFragment==null){
						/** Create ALLFreind and adding to fragmenttransaction */
						ft.add(R.id.realtabcontent,new InviteFriend_Fragment(), "apple");						
					}else{
						/** Bring to the front, if already exists in the fragmenttransaction */
						ft.attach(allFreindsFragment);						
					}
				}
				ft.commit();				
			}
		};

		/** Setting tabchangelistener for the tab */
		tHost.setOnTabChangedListener(tabChangeListener);

		/** Defining tab builder for FBoard Friends tab */
		TabHost.TabSpec tSpecAndroid = tHost.newTabSpec("android");
		tSpecAndroid.setIndicator("FBoard Friends", getResources().getDrawable(R.drawable.friends_icon));        
		tSpecAndroid.setContent(new DummyTabContent(getActivity().getBaseContext()));      
		tHost.addTab(tSpecAndroid);


		/** Defining tab builder for All Freinds tab */
		TabHost.TabSpec tSpecApple = tHost.newTabSpec("apple");
		tSpecApple.setIndicator("All Freinds",getResources().getDrawable(R.drawable.invite_friend));        
		tSpecApple.setContent(new DummyTabContent(getActivity().getBaseContext()));
		tHost.addTab(tSpecApple);


		for (int i = 0; i < tHost.getTabWidget().getChildCount(); i++) {
			/*View v = tHost.getTabWidget().getChildAt(i);
			v.setBackgroundColor(getActivity().getResources().getColor(R.color.white));
*/
			TextView tv = (TextView) tHost.getTabWidget().getChildAt(i).findViewById(android.R.id.title);
			tv.setTextColor(getActivity().getResources().getColor(R.color.black));
		}

		return rootView;

	}



}
