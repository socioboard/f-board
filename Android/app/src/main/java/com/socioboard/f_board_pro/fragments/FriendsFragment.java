package com.socioboard.f_board_pro.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TabHost;
import android.widget.TextView;

import com.facebook.share.model.AppInviteContent;
import com.facebook.share.widget.AppInviteDialog;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.socioboard.f_board_pro.R;
import com.socioboard.f_board_pro.imagelib.DummyTabContent;

public class FriendsFragment extends Fragment  
{
	View rootView;

	TextView mNoFriends, invite_friend;

	TabHost tHost;	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState)
	{
		rootView = inflater.inflate(R.layout.user_friends_fragment_tabs, container, false);

		invite_friend = (TextView)rootView.findViewById(R.id.invite_friend);

		invite_friend.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				sendInvitation();
			}
		});
		//+++++++++++++++++++++++++++++++
		tHost = (TabHost) rootView.findViewById(android.R.id.tabhost);
		tHost.setup();

		LoadAd();

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
		tSpecApple.setIndicator("All Friends",getResources().getDrawable(R.drawable.invite_friend));
		tSpecApple.setContent(new DummyTabContent(getActivity().getBaseContext()));
		tHost.addTab(tSpecApple);

//////////////change color of text in TabWidget//////////////////////////////////////////
		for (int i = 0; i < tHost.getTabWidget().getChildCount(); i++) {
			/*View v = tHost.getTabWidget().getChildAt(i);
			v.setBackgroundColor(getActivity().getResources().getColor(R.color.white));
*/
			TextView tv = (TextView) tHost.getTabWidget().getChildAt(i).findViewById(android.R.id.title);
			tv.setTextColor(getActivity().getResources().getColor(R.color.white));
		}

		return rootView;

	}

	void LoadAd()
	{
		MobileAds.initialize(getActivity(), getString(R.string.adMob_app_id));
		AdView mAdView = (AdView) rootView.findViewById(R.id.adView);
		AdRequest adRequest = new AdRequest.Builder().build();
		mAdView.loadAd(adRequest);

	}

	public void sendInvitation()
	{
		/*

		SharePhoto photo = new SharePhoto.Builder()
		.setImageUrl(Uri.parse("http://i.imgur.com/GQOn2oe.png"))
		.setUserGenerated(false)
		.build();

		ShareOpenGraphObject object = new ShareOpenGraphObject.Builder()
		.putString("og:type", "f-boardproapp:request")
		.putString("og:title", "Installation Request")
		.putPhoto("og:image", photo)
		.putString("og:description", "f-boardpro is a multiple facebook account management app, it helps you to login to multiple facebook accounts from your Android or iOS device and do various facebook activities like post, like, comment,managing pages, groups and much more.")
		.putString("og:url", "https://play.google.com/store/apps/details?id=com.socioboard.f_board_pro")
		.build();


		ShareOpenGraphAction action = new ShareOpenGraphAction.Builder()
		.setActionType("f-boardproapp:invite")
		.putObject("request", object)

		.build();

		ShareOpenGraphContent content = new ShareOpenGraphContent.Builder()
		.setPreviewPropertyName("request")
		.setAction(action)
		.build();

		ShareApi shareApi = new ShareApi(content);

		ShareDialog.show(getActivity(), content);
	*/
		String appLinkUrl, previewImageUrl;

		appLinkUrl = "https://fb.me/965921146783403";
		previewImageUrl = "http://i.imgur.com/THlqpFs.png";

		if ( AppInviteDialog.canShow()) {

			//	LoginManager.getInstance().setLoginBehavior(LoginBehavior.SUPPRESS_SSO);
			AppInviteContent content = new AppInviteContent.Builder()
					.setApplinkUrl(appLinkUrl)
					.setPreviewImageUrl(previewImageUrl)
					.build();
			AppInviteDialog.show(getActivity(), content);

		}
	}

}
