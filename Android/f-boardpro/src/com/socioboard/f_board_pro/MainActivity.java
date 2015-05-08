package com.socioboard.f_board_pro;

import java.util.ArrayList;
import java.util.Arrays;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.socioboard.f_board_pro.adapter.AccountAdapter;
import com.socioboard.f_board_pro.adapter.DrawerAdapter;
import com.socioboard.f_board_pro.database.util.F_Board_LocalData;
import com.socioboard.f_board_pro.database.util.MainSingleTon;
import com.socioboard.f_board_pro.database.util.ModelUserDatas;
import com.socioboard.f_board_pro.database.util.Utilsss;
import com.socioboard.f_board_pro.fragments.DisplayUserFriendsFragment;
import com.socioboard.f_board_pro.fragments.Display_User_Details;
import com.socioboard.f_board_pro.fragments.GetGroups_Fragment;
import com.socioboard.f_board_pro.fragments.GetHomeFeedFragment;
import com.socioboard.f_board_pro.fragments.GetUserFeeds;
import com.socioboard.f_board_pro.fragments.Pages_Fragment;
import com.socioboard.f_board_pro.fragments.Scheduler_Fragment;
import com.socioboard.f_board_pro.viewlibary.Items;
import com.socioboard.f_board_pro.viewlibary.MultiSwipeRefreshLayout;


public class MainActivity extends ActionBarActivity implements MultiSwipeRefreshLayout.CanChildScrollUpCallback 
{
	/*Initialize all the variable */

	Handler handler ;
	LoginButton connectFacebook;
	public static Menu mMenu;
	private String[] mDrawerTitles;
	private TypedArray mDrawerIcons;
	private ArrayList<Items> drawerItems;
	private DrawerLayout mDrawerLayout;
	RelativeLayout addAccountRlt, settingRlt, feedbackRlt;

	private ListView mDrawerList_Left,mDrawerList_Right;

	private ActionBarDrawerToggle mDrawerToggle;
	public static  FragmentManager fragmentManager;//
	private CharSequence mDrawerTitle;
	private CharSequence mTitle;


	// SwipeRefreshLayout allows the user to swipe the screen down to trigger a manual refresh
	private MultiSwipeRefreshLayout mSwipeRefreshLayout;
	private ViewGroup headerRight;
	private ViewGroup footerRight;
	private ArrayList<ModelUserDatas> accountList;

	CallbackManager callbackManager;
	AccessToken myAccessToken = null;
	String currentUserEmailId = null;
	ProfileTracker profileTracker;
	Profile fBprofile;

	public static PendingIntent pendingIntent;
	public static AlarmManager alarmManager;


	ImageView pCoverPic;
	TextView userName;
	TextView userEmailId ;
	ImageView profilePic;
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		FacebookSdk.sdkInitialize(getApplicationContext());

		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_main);
		handler = new Handler();
		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		if (toolbar != null) setSupportActionBar(toolbar);
		

		fragmentManager = getSupportFragmentManager();
		mDrawerTitles = getResources().getStringArray(R.array.drawer_titles);
		mDrawerIcons = getResources().obtainTypedArray(R.array.drawer_icons);

		drawerItems = new ArrayList<Items>();

		mDrawerList_Left = (ListView) findViewById(R.id.left_drawer);

		mDrawerList_Right= (ListView) findViewById(R.id.right_drawer);

		for (int i = 0; i < mDrawerTitles.length; i++) 
		{
			drawerItems.add(new Items(mDrawerTitles[i], mDrawerIcons.getResourceId(i, -(i + 1))));
		}

		accountList = new ArrayList<ModelUserDatas>();
		mTitle = mDrawerTitle = getTitle();


		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerToggle = new ActionBarDrawerToggle(
				this,                  /** host Activity */
				mDrawerLayout,         /** DrawerLayout object */
				toolbar,                 /** nav drawer icon to replace 'Up' caret */
				R.string.drawer_open,  /** "open drawer" description */
				R.string.drawer_close  /** "close drawer" description */
				) {

			/** Called when a drawer has settled in a completely closed state. */
			public void onDrawerClosed(View view) 
			{
				super.onDrawerClosed(view);
				getSupportActionBar().setTitle(mTitle);
				mMenu.findItem(R.id.action_settings).setVisible(true);


			}

			/** Called when a drawer has settled in a completely open state. */
			public void onDrawerOpened(View drawerView)
			{
				super.onDrawerOpened(drawerView);
				getSupportActionBar().setTitle(mDrawerTitle);


			}
		};

		// Set the drawer toggle as the DrawerListener
		mDrawerLayout.setDrawerListener(mDrawerToggle);

		LayoutInflater inflater = getLayoutInflater();

		final ViewGroup footer = (ViewGroup) inflater.inflate(R.layout.footer_left,mDrawerList_Left, false);

		headerRight = (ViewGroup) inflater.inflate(R.layout.header,mDrawerList_Right, false);

		footerRight = (ViewGroup) inflater.inflate(R.layout.footer, mDrawerList_Right, false);

		//**Getting view of footer and herder of drawer listview//// 
		ImageView user=(ImageView) headerRight.findViewById(R.id.profile_pic);
		addAccountRlt = (RelativeLayout) footerRight.findViewById(R.id.addAccountRlt);
		settingRlt    = (RelativeLayout) footerRight.findViewById(R.id.settingRlt);
		feedbackRlt   = (RelativeLayout) footerRight.findViewById(R.id.feedbackRlt);
		//Setting onclick listener 
		settingRlt.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{


			}
		});
		
		
		//Setting onclick listener 
		user.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				swipeFragment(new Display_User_Details(MainSingleTon.userid));
				setTitle(MainSingleTon.username);
				mDrawerLayout.closeDrawer(mDrawerList_Right);

			}
		});
		
		
		
		//Setting onclick listener 
		feedbackRlt.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v) 
			{

				
			}
		});

		//Setting onclick listener 
		addAccountRlt.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{

				addNewAccount();
			}
		});

		mDrawerList_Left.addFooterView(footer, null, true); // true = clickable

		mDrawerList_Right.addHeaderView(headerRight, null, true); // true = clickable
		mDrawerList_Right.addFooterView(footerRight, null, true); // true = clickable

		//Set width of drawer
		DrawerLayout.LayoutParams lp = (DrawerLayout.LayoutParams) mDrawerList_Left.getLayoutParams();
		lp.width = calculateDrawerWidth();
		mDrawerList_Left.setLayoutParams(lp);

		//Set width of drawer

		DrawerLayout.LayoutParams lpR = (DrawerLayout.LayoutParams) mDrawerList_Right.getLayoutParams();
		lpR.width = calculateDrawerWidth();
		mDrawerList_Right.setLayoutParams(lpR);


		// Set the adapter for the list view
		mDrawerList_Left.setAdapter(new DrawerAdapter(getApplicationContext(), drawerItems));
		// Set the list's click listener
		mDrawerList_Left.setOnItemClickListener(new LeftDrawerItemClickListener());

		accountList.clear();


		System.out.println("ACCCCCCCCCCCCCCOUNTLIST ="+accountList.size());

		pCoverPic =(ImageView)headerRight.findViewById(R.id.cover_pic);
		userName  =(TextView) headerRight.findViewById(R.id.username);
		userEmailId	=(TextView) headerRight.findViewById(R.id.user_email_id);
		profilePic=(ImageView) headerRight.findViewById(R.id.profile_pic);

		for (int i = 0; i < MainSingleTon.useridlist.size(); i++)
		{

			ModelUserDatas model = MainSingleTon.userdetails.get(MainSingleTon.useridlist.get(i));
			model.setUserid(model.getUserid());
			model.setUserAcessToken(model.getUserAcessToken());
			model.setUserimage(model.getUserimage());
			model.setUsername(model.getUsername());
			model.setUserEmail(model.getUserEmail());
			accountList.add(model);

		}

		mDrawerList_Right.setAdapter(new AccountAdapter(MainActivity.this, accountList));

		mDrawerList_Right.setOnItemClickListener(new RightDrawerItemClickListener());


		setUser(MainSingleTon.username, MainSingleTon.userimage, MainSingleTon.userEmail);

		Display_User_Details fragment = new Display_User_Details(MainSingleTon.userid);

		fragmentManager.beginTransaction().replace(R.id.main_content, fragment).commitAllowingStateLoss();

		/**alarm manager to receive the pending intent**/
		
		alarmManager    = (AlarmManager) getSystemService(ALARM_SERVICE);
		
		Intent myIntent = new Intent(MainActivity.this,	SchedulerCustomReceiver.class);
		pendingIntent   = PendingIntent.getBroadcast(MainActivity.this, 0, myIntent, PendingIntent.FLAG_UPDATE_CURRENT);
		

		NotificationManager mNotificationManager = (NotificationManager) this.getApplicationContext().getSystemService(
						Context.NOTIFICATION_SERVICE);
		mNotificationManager.cancelAll();
		alarmManager.cancel(pendingIntent);

	}

	protected void addNewAccount()
	{ 

		
		
		
		MainSingleTon.isfrom_schedulefrag=false;
		
		callbackManager = CallbackManager.Factory.create();

		LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("email","user_photos",
																				"public_profile", "user_posts", 
																				"user_likes","read_stream",
																				"user_friends",
																				"user_hometown","user_work_history",
																				"user_location",
																				"user_birthday","user_about_me"
																				));
		//LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("email","user_photos","public_profile"));	
		//
		
		LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>()
		{

			@Override
			public void onSuccess(LoginResult loginResult)
			{

				Profile.fetchProfileForCurrentAccessToken();
				myAccessToken = loginResult.getAccessToken();
				
				
				
			}

			@Override
			public void onError(FacebookException error) {
				AccessToken.setCurrentAccessToken(null);				
			}

			@Override
			public void onCancel()
			{
				AccessToken.setCurrentAccessToken(null);

			}
		});

		profileTracker = new ProfileTracker()
		{
			@Override
			protected void onCurrentProfileChanged(Profile oldProfile, Profile currentProfile) {

				if(profileTracker.isTracking())

				{

					fBprofile = currentProfile;
					LoginManager.getInstance().logInWithPublishPermissions(MainActivity.this, Arrays.asList("publish_actions"));
					

					new GetExtendedAccessToken().execute();

				}else
				{

				}

			}
		};
		
	

	}

	public class GetExtendedAccessToken extends AsyncTask<Void, Void, String> 
	{
		String extendedAccessToken;
		@Override
		protected String doInBackground(Void... params)
		{

			String tokenURL = " https://graph.facebook.com/oauth/access_token?client_id="
					+ getResources().getString(R.string.app_id)
					+ "&client_secret="
					+getResources().getString(R.string.app_secret)
					+ "&grant_type=fb_exchange_token&fb_exchange_token="
					+ myAccessToken.getToken(); 

			String dummtoken = Utilsss.getJSONString(tokenURL);

			dummtoken = dummtoken.substring(dummtoken.indexOf("="),dummtoken.indexOf("&"));

			extendedAccessToken = dummtoken.replace("=", "");

			return extendedAccessToken;
		}

		@Override
		protected void onPostExecute(String extendedAccessToken)
		{

			super.onPostExecute(extendedAccessToken);

			if(!MainSingleTon.userdetails.containsKey(myAccessToken.getUserId()))
			{

				ModelUserDatas modelUserDatas = new ModelUserDatas();

				if(extendedAccessToken!=null)
				{
					modelUserDatas.setUserAcessToken(extendedAccessToken);
				}else
				{
					modelUserDatas.setUserAcessToken(myAccessToken.getToken());
				}
				modelUserDatas.setUserid(myAccessToken.getUserId());
				modelUserDatas.setUsername(fBprofile.getName());
				modelUserDatas.setUserimage(""+ fBprofile.getProfilePictureUri(240, 260));
				modelUserDatas.setUserEmail(currentUserEmailId);

				MainSingleTon.username    = fBprofile.getName();
				MainSingleTon.userimage   = ""+fBprofile.getProfilePictureUri(240, 260);
				MainSingleTon.accesstoken = myAccessToken.getToken();
				MainSingleTon.userid      = myAccessToken.getUserId();

				F_Board_LocalData	fBoardData = new F_Board_LocalData(getApplicationContext());

				fBoardData.addNewUserAccount(modelUserDatas);

				MainSingleTon.userdetails.put(myAccessToken.getUserId(), modelUserDatas);


				MainSingleTon.useridlist.add(myAccessToken.getUserId());

				if(profileTracker.isTracking())
				{
					profileTracker.stopTracking();
				}else
				{
				}


				System.out.println("--------------------MainSingleTon.useridlist.size()="+MainSingleTon.useridlist.size());

				accountList.clear();

				for (int i = 0; i < MainSingleTon.useridlist.size(); i++) {

					ModelUserDatas model = MainSingleTon.userdetails.get(MainSingleTon.useridlist.get(i));
					model.setUserid(model.getUserid());
					model.setUserAcessToken(model.getUserAcessToken());
					model.setUserimage(model.getUserimage());
					model.setUsername(model.getUsername());

					model.setUserEmail(model.getUserEmail());
					accountList.add(model);

				}

				AccountAdapter accountAdapter = new AccountAdapter(MainActivity.this, accountList);

				mDrawerList_Right.setAdapter(accountAdapter);
				
				LoginManager.getInstance().logOut();
			}else 
			{
				Toast.makeText(getApplicationContext(), "Users Account already exist!!", Toast.LENGTH_SHORT).show();
				
				LoginManager.getInstance().logOut();
			}
		}
	}


	public void setUser(final String namefinal,  final String pPic, final String email)
	{ 

		new Thread(new Runnable() {

			@Override
			public void run() {

				handler.post(new Runnable() {

					Bitmap pfofile  = MainSingleTon.getBitmapFromURL(pPic);

					@Override
					public void run() {

						profilePic.setImageBitmap((pfofile));
					}
				});


			}
		}).start();
		profilePic.setOnClickListener(new OnClickListener() 
		{
			
			@Override
			public void onClick(View v) 
			{
			MainActivity.swipeFragment(new Display_User_Details(MainSingleTon.userid));
			mDrawerLayout.closeDrawer(mDrawerList_Right);
			setTitle(MainSingleTon.username);
				
			}
		});
		pCoverPic.setImageResource(R.drawable.header_image);
		pCoverPic.setOnClickListener(new OnClickListener() 
		{
			
			@Override
			public void onClick(View v) 
			{
				MainActivity.swipeFragment(new Display_User_Details(MainSingleTon.userid));
				mDrawerLayout.closeDrawer(mDrawerList_Right);
				setTitle(MainSingleTon.username);
			}
		});
		userName.setText(namefinal);
		userName.setOnClickListener(new OnClickListener() 
		{
			
			@Override
			public void onClick(View v) 
			{
			
				MainActivity.swipeFragment(new Display_User_Details(MainSingleTon.userid));
				mDrawerLayout.closeDrawer(mDrawerList_Right);
				setTitle(MainSingleTon.username);
			}
		});
		userEmailId.setText(email);
		userEmailId.setOnClickListener(new OnClickListener() 
		{
			
			@Override
			public void onClick(View v) 
			{
				MainActivity.swipeFragment(new Display_User_Details(MainSingleTon.userid));
				mDrawerLayout.closeDrawer(mDrawerList_Right);
				setTitle(MainSingleTon.username);
				
			}
		});
		System.out.println("email "+email);
	}
	@Override
	protected void onPostCreate(Bundle savedInstanceState)
	{
		super.onPostCreate(savedInstanceState);
		// Sync the toggle state after onRestoreInstanceState has occurred.
		mDrawerToggle.syncState();
		//trySetupSwipeRefresh();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig)
	{
		super.onConfigurationChanged(newConfig);
		mDrawerToggle.onConfigurationChanged(newConfig);
	}

	@Override
	public void onBackPressed() {
		 
		
		if (fragmentManager.getBackStackEntryCount() > 1) 
		{
			fragmentManager.popBackStack();
		}else
		{
			super.onBackPressed();
		}
	}
	

	/**
	 * Swaps fragments in the main content view
	 */
	private void selectItem(int position)
	{
		Fragment fragment = null;
System.out.println("selected position "+position);
		switch (position)
		{
			case 0:
				fragment = new Display_User_Details(MainSingleTon.userid);
				break;
			case 1:
				fragment = new GetHomeFeedFragment();
				break;
				
			case 2:
				fragment = new GetUserFeeds();
				break;
	
			case 3:
				fragment = new DisplayUserFriendsFragment();
				break;
				
			case 4:
				fragment = new Pages_Fragment();
				break;
				
			case 5:
				fragment = new Scheduler_Fragment();
				break;

		}

		if (fragment != null)
		{
			// Insert the fragment by replacing any existing fragment
			FragmentManager fragmentManager = getSupportFragmentManager();
			fragmentManager.beginTransaction().replace(R.id.main_content, fragment).addToBackStack(null).commit();

		}

		// Highlight the selected item, update the title, and close the drawer
		if(mDrawerList_Left.isEnabled())
		{
			mDrawerList_Left.setItemChecked(position, true);

			setTitle(mDrawerTitles[position]);
//			updateView(position, position, true,mDrawerList_Left);

			mDrawerLayout.closeDrawer(mDrawerList_Left);
		}
		else
		{
			mDrawerList_Right.setItemChecked(position, true);
			if (position != 0)
			{
				setTitle(mDrawerTitles[position - 1]);
//				updateView(position, position, true,mDrawerList_Right);
			}
			mDrawerLayout.closeDrawer(mDrawerList_Right);
		}

	}

	@Override
	public void setTitle(CharSequence title)
	{
		mTitle = title;
		getSupportActionBar().setTitle(mTitle);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		System.out.println("onCreateOptionsMenu");
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu_main, menu);

		return true;
	}

	/* Called whenever we call invalidateOptionsMenu() */
	@Override
	public boolean onPrepareOptionsMenu(Menu menu)
	{
		mMenu=menu;
		System.out.println("onPrepareOptionsMenu");
		// If the nav drawer is open, hide action items related to the content view
		boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList_Left);

		boolean drawerOpenR = mDrawerLayout.isDrawerOpen(mDrawerList_Right);


		if (mDrawerLayout.isDrawerOpen(mDrawerList_Right))
		{
			mDrawerLayout.closeDrawer(mDrawerList_Right);
			mMenu.findItem(R.id.action_settings).setVisible(true);
		} 
		else
		{
			mMenu.findItem(R.id.action_settings).setVisible(false);
			mDrawerLayout.openDrawer(mDrawerList_Right);


		}
		return super.onPrepareOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) 
	{
		if (mDrawerToggle.onOptionsItemSelected(item)) 
		{
			return true;

		}
		if(mDrawerLayout.isDrawerOpen(mDrawerList_Right))
		{
			mDrawerLayout.closeDrawer(mDrawerList_Right);
		}
		else
		{
			mDrawerLayout.openDrawer(mDrawerList_Right);
		}
		return super.onOptionsItemSelected(item);
	}

	public int calculateDrawerWidth()
	{
		// Calculate ActionBar height
		TypedValue tv = new TypedValue();
		int actionBarHeight = 0;
		if (getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true))
		{
			actionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data, getResources().getDisplayMetrics());
		}

		Display display = getWindowManager().getDefaultDisplay();
		int width;
		int height;
		if (android.os.Build.VERSION.SDK_INT >= 13) 
		{
			Point size = new Point();
			display.getSize(size);
			width = size.x;
			height = size.y;
		} else
		{
			width = display.getWidth();  // deprecated
			height = display.getHeight();  // deprecated
		}
		return width - actionBarHeight;
	}

	private void updateView(int position, int counter, boolean visible,ListView mDrawerList) 
	{

		View v = mDrawerList.getChildAt(position -mDrawerList.getFirstVisiblePosition());
		TextView someText = (TextView) v.findViewById(R.id.item_new);
		Resources res = getResources();
		String articlesFound = "";

		switch (position) 
		{
		case 1:
			articlesFound = res.getQuantityString(R.plurals.numberOfNewArticles, counter, counter);
			someText.setBackgroundResource(R.drawable.new_apps);
			break;
		case 2:
			articlesFound = res.getQuantityString(R.plurals.numberOfNewArticles, counter, counter);
			someText.setBackgroundResource(R.drawable.new_sales);
			break;
		case 3:
			articlesFound = res.getQuantityString(R.plurals.numberOfNewArticles, counter, counter);
			someText.setBackgroundResource(R.drawable.new_blog);
			break;
		case 4:
			articlesFound = res.getQuantityString(R.plurals.numberOfNewArticles, counter, counter);
			someText.setBackgroundResource(R.drawable.new_bookmark);
			break;
		case 5:
			articlesFound = res.getQuantityString(R.plurals.numberOfNewArticles, counter, counter);
			someText.setBackgroundResource(R.drawable.new_community);
			break;
		}

		someText.setText(articlesFound);
		if (visible) someText.setVisibility(View.VISIBLE);
	}

	@Override
	public boolean canSwipeRefreshChildScrollUp()
	{
		return false;
	}

	private void trySetupSwipeRefresh()
	{
		mSwipeRefreshLayout = (MultiSwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
		if (mSwipeRefreshLayout != null)
		{
			mSwipeRefreshLayout.setColorSchemeResources(
					R.color.refresh_progress_1,
					R.color.refresh_progress_2,
					R.color.refresh_progress_3);
			mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() 
			{
				@Override
				public void onRefresh()
				{
					Toast.makeText(getApplication(),"Refresh!", Toast.LENGTH_LONG);
				}
			});

			if (mSwipeRefreshLayout instanceof MultiSwipeRefreshLayout)
			{
				MultiSwipeRefreshLayout mswrl = (MultiSwipeRefreshLayout) mSwipeRefreshLayout;
				mswrl.setCanChildScrollUpCallback(this);
			}
		}
	}

	private class LeftDrawerItemClickListener implements ListView.OnItemClickListener
	{
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) 
		{
			selectItem(position);
		}
	}

	private class RightDrawerItemClickListener implements ListView.OnItemClickListener
	{
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int positionRightlist, long id) 
		{
			if(positionRightlist-1!=-1)
			{
				selectItemRight(positionRightlist);
			}else  
			{

			}
		}
	}

	private void selectItemRight(final int positionRightlist)
	{

		runOnUiThread(new Runnable() {

			@Override
			public void run() {

				ModelUserDatas model       = MainSingleTon.userdetails.get(accountList.get(positionRightlist-1).getUserid());
				MainSingleTon.userid       = model.getUserid();
				MainSingleTon.username     = model.getUsername();
				MainSingleTon.userimage    = model.getUserimage();
				MainSingleTon.accesstoken  = model.getUserAcessToken();
				MainSingleTon.userEmail   = model.getUserEmail();

				MainSingleTon.feedArrayList.clear();
				MainSingleTon.pagesArrayList.clear();
				MainSingleTon.isPAgesLoaded =false;

				setUser(MainSingleTon.username, MainSingleTon.userimage, MainSingleTon.userEmail);

			}
		});
		swipeFragment(new Display_User_Details(MainSingleTon.userid));
		setTitle(MainSingleTon.username);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		super.onActivityResult(requestCode, resultCode, data);
		if (!MainSingleTon.isfrom_schedulefrag)
		{
			callbackManager.onActivityResult(requestCode, resultCode, data);
		}
		
	}

	
	@TargetApi(Build.VERSION_CODES.LOLLIPOP)
	public void circleIn(View view) 
	{

		// get the center for the clipping circle
		int cx = (view.getLeft() + view.getRight()) / 2;
		int cy = (view.getTop() + view.getBottom()) / 2;

		// get the final radius for the clipping circle
		int finalRadius = Math.max(view.getWidth(), view.getHeight());

		// create the animator for this view (the start radius is zero)
		Animator anim =  ViewAnimationUtils.createCircularReveal(view, cx, cy, 0, finalRadius);

		// make the view visible and start the animation
		view.setVisibility(View.VISIBLE);
		anim.start();
	}

	@TargetApi(Build.VERSION_CODES.LOLLIPOP)
	public void circleOut(final View view)
	{

		// get the center for the clipping circle
		int cx = (view.getLeft() + view.getRight()) / 2;
		int cy = (view.getTop() + view.getBottom()) / 2;

		// get the initial radius for the clipping circle
		int initialRadius = view.getWidth();

		// create the animation (the final radius is zero)
		Animator anim = ViewAnimationUtils.createCircularReveal(view, cx, cy, initialRadius, 0);

		// make the view invisible when the animation is done
		anim.addListener(new AnimatorListenerAdapter()
		{
			@Override
			public void onAnimationEnd(Animator animation) 
			{
				super.onAnimationEnd(animation);
				view.setVisibility(View.INVISIBLE);
			}
		});

		// start the animation
		anim.start();
	}
	public static void swipeFragment(Fragment fragment)
	{ 
		fragmentManager.beginTransaction().replace(R.id.main_content, fragment).commitAllowingStateLoss();
	}

}
