package com.socioboard.f_board_pro;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Point;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CompoundButton;
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
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.ads.InterstitialAd;
import com.facebook.applinks.AppLinkData;
import com.facebook.login.LoginBehavior;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.flurry.android.FlurryAgent;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.socioboard.f_board_pro.adapter.AccountAdapter;
import com.socioboard.f_board_pro.adapter.DrawerAdapter;
import com.socioboard.f_board_pro.database.util.F_Board_LocalData;
import com.socioboard.f_board_pro.database.util.JSONParseraa;
import com.socioboard.f_board_pro.database.util.MainSingleTon;
import com.socioboard.f_board_pro.database.util.ModelUserDatas;
import com.socioboard.f_board_pro.database.util.Utilsss;
import com.socioboard.f_board_pro.fragments.AutoLiker;
import com.socioboard.f_board_pro.fragments.FriendsFragment;
import com.socioboard.f_board_pro.fragments.MyFeeds_Fragment;
import com.socioboard.f_board_pro.fragments.Mygroup;
import com.socioboard.f_board_pro.fragments.Pages_Fragment;
import com.socioboard.f_board_pro.fragments.ProfileFragment;
import com.socioboard.f_board_pro.fragments.SchedulerFragment;
import com.socioboard.f_board_pro.fragments.SearchFragment;
import com.socioboard.f_board_pro.fragments.ShareagonGroup;
import com.socioboard.f_board_pro.fragments.ShareagonLinks;
import com.socioboard.f_board_pro.fragments.ShareagonPage;
import com.socioboard.f_board_pro.imagelib.ImageLoader;
import com.socioboard.f_board_pro.models.FirebaseModel;
import com.socioboard.f_board_pro.viewlibary.Items;
import com.socioboard.f_board_pro.viewlibary.MultiSwipeRefreshLayout;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

import bolts.AppLinks;

//import com.appnext.appnextsdk.AppnextTrack;

public class MainActivity extends AppCompatActivity implements  MultiSwipeRefreshLayout.CanChildScrollUpCallback {
	/* Initialize all the variable */

    FirebaseDatabase mFirebaseInstance;

    DatabaseReference mFirebaseDatabase;

    SwitchCompat switchCompat1;
    Handler handler;
    LoginButton connectFacebook;
    public static Menu mMenu;
    private String[] mDrawerTitles;
    private TypedArray mDrawerIcons;
    private ArrayList<Items> drawerItems;
    private DrawerLayout mDrawerLayout;
    RelativeLayout addAccountRlt, settingRlt, feedbackRlt;
    private InterstitialAd interstitialAd;
    JSONObject jobj;
    private ListView mDrawerList_Left, mDrawerList_Right;

    private ActionBarDrawerToggle mDrawerToggle;
    ProfileFragment profileFragment;
    public static FragmentManager fragmentManager;//
    private CharSequence mDrawerTitle;
    private CharSequence mTitle;
    public static Context context;

    boolean doubleBackToExitPressedOnce;
    private int progress = 10;

    // SwipeRefreshLayout allows the user to swipe the screen down to trigger a
    // manual refresh
    private MultiSwipeRefreshLayout mSwipeRefreshLayout;
    private ViewGroup headerRight;
    private ViewGroup footerRight;
    private ArrayList<ModelUserDatas> accountList;

    CallbackManager callbackManager;
    AccessToken myAccessToken = null;
    String currentUserEmailId = null;
    ProfileTracker profileTracker;
    Profile fBprofile;

    ImageView pCoverPic;
    TextView userName,toolbar_title;
    TextView userEmailId;
    ImageView profilePic;

    String userid; //current user id  -----1
    String username; //username           -----2
    String userFirstName;
    String useraccesstoken; //current user access token   -----3
    String android_id;
    String getFirebaseToken;
    boolean notificationstatus;
    public String Email;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        mFirebaseInstance = FirebaseDatabase.getInstance();
        mFirebaseDatabase = mFirebaseInstance.getReferenceFromUrl("https://pushnotificationfirebase-12934.firebaseio.com/");

        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefrence",Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedPreferences.edit();

        super.onCreate(savedInstanceState);

        FacebookSdk.sdkInitialize(MainActivity.this);
        context = getApplicationContext();
        System.out.println("Started !3");
        Uri targetUrl = AppLinks.getTargetUrlFromInboundIntent(MainActivity.this, getIntent());
        System.out.println("Started !4");
        if (targetUrl != null) {
            System.out.println("Started !5");
            Log.i("Activity", "App Link Target URL: " + targetUrl.toString());

        } else {

            System.out.println("Started !6");
            AppLinkData.fetchDeferredAppLinkData(MainActivity.this,
                    new AppLinkData.CompletionHandler() {


                        public void onDeferredAppLinkDataFetched(AppLinkData appLinkData) {
                            // process applink data
                           // System.out.println("Started !8"+appLinkData.toString());
                        }
                    });
        }
        System.out.println("Started !7");
        FlurryAgent.setLogEnabled(false);

        System.out.println("Started !9");
        FlurryAgent.init(MainActivity.this, "D8T57RYRKJPV7K5FRQXM");

        setContentView(R.layout.activity_main);

        System.out.println("Started !10");
        callbackManager = CallbackManager.Factory.create();
        System.out.println("Started !11");
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar_title = (TextView)findViewById(R.id.toolbar_title);


        if (toolbar != null) {
            //toolbar.getTitle();
            toolbar_title.setText(MainSingleTon.username);
             //toolbar.setTitle(MainSingleTon.userFirstName);
            //toolbar.setTitle(mDrawerTitle);
            setSupportActionBar(toolbar);
        }else {
            toolbar_title.setText(MainSingleTon.username);
        }

        System.out.println("Started !12");
        mDrawerTitles = getResources().getStringArray(R.array.drawer_titles);
        mDrawerIcons = getResources().obtainTypedArray(R.array.drawer_icons);

        drawerItems = new ArrayList<Items>();
        System.out.println("Started !12--"+mDrawerTitles+" 1-------"+mDrawerIcons+" 2----------"+drawerItems);
        profileTracker = new ProfileTracker() {

            protected void onCurrentProfileChanged(Profile oldProfile,Profile currentProfile) {

                if (profileTracker.isTracking()) {

                    System.out.println("Started !13");
					/*
					 * fBprofile = currentProfile;
					 * LoginManager.getInstance().logInWithPublishPermissions
					 * (MainActivity.this,
					 * Arrays.asList("publish_actions","manage_pages")); new
					 * GetExtendedAccessToken().execute();
					 */

                } else {

                    System.out.println("Started !14");
                }

            }
        };
        // ++++++++++++++++++++++++++++++++++++++++++++

        // ++++++++++++++++++++++++++++++++++++++++++++

        mDrawerList_Left = (ListView) findViewById(R.id.left_drawer);

        System.out.println("Started !15");
        mDrawerList_Right = (ListView) findViewById(R.id.right_drawer);

        for (int i = 0; i < mDrawerTitles.length; i++)
        {
            drawerItems.add(new Items(mDrawerTitles[i], mDrawerIcons.getResourceId(i, -(i + 1))));
            System.out.println("Started !16 : 1-------"+mDrawerTitles[i]+" 2-------"+drawerItems+" 3------"+mDrawerIcons);
        }

        accountList = new ArrayList<ModelUserDatas>();
        mTitle = mDrawerTitle = MainSingleTon.userFirstName;
         setTitle(mTitle);
        System.out.println("Started !18-- "+mTitle+" "+mDrawerTitle+" "+getTitle());
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerToggle = new ActionBarDrawerToggle(this, /** host Activity */
                mDrawerLayout, /** DrawerLayout object */
                toolbar,
                 /** nav drawer icon to replace 'Up' caret */
                R.string.drawer_open, /** "open drawer" description */
                R.string.drawer_close /** "close drawer" description */

        ) {

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                System.out.println("Started !19");
                //getSupportActionBar().setTitle(mTitle);
                setTitle(mTitle);
                mMenu.findItem(R.id.action_settings).setVisible(true);
                System.out.println("Started !20");
            }

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                setTitle(mDrawerTitle);
                System.out.println("Started !21");
            }
        };

        // Set the drawer toggle as the DrawerListener
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        System.out.println("Started !22");
        LayoutInflater inflater = getLayoutInflater();
        System.out.println("Started !23");
        final ViewGroup footer = (ViewGroup) inflater.inflate(R.layout.footer_left, mDrawerList_Left, false);
        System.out.println("Started !24");
        headerRight = (ViewGroup) inflater.inflate(R.layout.header,mDrawerList_Right, false);
        System.out.println("Started !24");
        footerRight = (ViewGroup) inflater.inflate(R.layout.footer, mDrawerList_Right, false);
        System.out.println("Started !25");
        // **Getting view of footer and herder of drawer listview////
        ImageView user = (ImageView) headerRight.findViewById(R.id.profile_pic);


        System.out.println("ProfileFragment.s_path: "+profileFragment.s_path);
       // final Bitmap bitmap= MainSingleTon.getBitmapFromURL("https://graph.facebook.com/190595751451222/picture?type=small");
        Picasso.with(getApplicationContext()).load(MainSingleTon.userimage).into(user);
      /*  new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                user.setImageBitmap(bitmap);
            }
        },3000);*/


        System.out.println("Started !26");
        addAccountRlt = (RelativeLayout) footerRight.findViewById(R.id.addAccountRlt);
        System.out.println("Started !27");
        settingRlt = (RelativeLayout) footerRight.findViewById(R.id.settingRlt);
        switchCompat1 = (SwitchCompat) footerRight.findViewById(R.id.switchButton1);
        System.out.println("Started !28");
        feedbackRlt = (RelativeLayout) footerRight.findViewById(R.id.feedbackRlt);
        System.out.println("Started !29");
        // Setting onclick listener
        settingRlt.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                MainSingleTon.showMyToast(MainActivity.this,"Available in future version!");
                System.out.println("Started !30");
            }
        });

        boolean cheackvalue = sharedPreferences.getBoolean("cheackv",true);
        if(cheackvalue)
        {
            System.out.println("cheackvalue=="+cheackvalue);
            switchCompat1.setChecked(true);
        }
        else
        {
                System.out.println("cheackvalueinelse=="+cheackvalue);
                switchCompat1.setChecked(false);
        }
        switchCompat1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if(isChecked)
                {
                    editor.putBoolean("cheackv",true);
                    editor.commit();
                    notificationstatus = true;
                    MainSingleTon.firebaseNotification = notificationstatus;
                    registration(notificationstatus,userid,username,useraccesstoken,android_id,getFirebaseToken);
                }
                else {

                    notificationstatus = false;
                    editor.remove("cheackv");
                    editor.putBoolean("cheackv",false);
                    editor.commit();
                    MainSingleTon.firebaseNotification = notificationstatus;
                    registration(notificationstatus,userid,username,useraccesstoken,android_id,getFirebaseToken);
                }

            }
        });

        // Setting onclick listener
        user.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                fragmentManager = getSupportFragmentManager();
                System.out.println("Started !31"+MainSingleTon.userid);
                swipeFragment(new ProfileFragment(MainSingleTon.userid));
                System.out.println("Started !32"+MainSingleTon.userid);
               // toolbar.setTitle(MainSingleTon.userFirstName);
                System.out.println("Started !33");
                mDrawerLayout.closeDrawer(mDrawerList_Right);
                System.out.println("Started !34");
            }
        });

        // Setting onclick listener
        feedbackRlt.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,UserGuide.class);
                startActivity(intent);
             //   finish();
            }
        });

        // Setting onclick listener
        addAccountRlt.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                if (profileTracker.isTracking()) {
                    profileTracker.stopTracking();
                    LoginManager.getInstance().logOut();
                    System.out.println("IS tracking true");
                    Toast.makeText(getApplicationContext(),"IS tracking true",Toast.LENGTH_LONG).show();
                    addNewAccount();


                } else {
                    System.out.println("faslse");
                    addNewAccount();
                }

            }
        });

        mDrawerList_Left.addFooterView(footer, null, false); // true = clickable
        System.out.println("Started !35");
        mDrawerList_Right.addHeaderView(headerRight, null, false); // true =
        // clickable
        System.out.println("Started !36");
        mDrawerList_Right.addFooterView(footerRight, null, false); // true =
        System.out.println("Started !37");
        // clickable

        // Set width of drawer
        DrawerLayout.LayoutParams lp = (DrawerLayout.LayoutParams) mDrawerList_Left.getLayoutParams();
        System.out.println("Started !38");
        lp.width = calculateDrawerWidth();
        mDrawerList_Left.setLayoutParams(lp);
        System.out.println("Started !39");
        // Set width of drawer

        DrawerLayout.LayoutParams lpR = (DrawerLayout.LayoutParams) mDrawerList_Right.getLayoutParams();
        System.out.println("Started !40");
        lpR.width = calculateDrawerWidth();
        mDrawerList_Right.setLayoutParams(lpR);
        System.out.println("Started !41");
        // Set the adapter for the list view
        mDrawerList_Left.setAdapter(new DrawerAdapter(getApplicationContext(),drawerItems));
        // Set the list's click listener
        mDrawerList_Left.setOnItemClickListener(new LeftDrawerItemClickListener());
        //accountList.clear();
        System.out.println("Started !42");
        System.out.println("ACCCCCCCCCCCCCCOUNTLIST =" + accountList.size());
        accountList.clear();
        System.out.println("Started !43");
        pCoverPic = (ImageView) headerRight.findViewById(R.id.cover_pic);
        userName = (TextView) headerRight.findViewById(R.id.username);
        userEmailId = (TextView) headerRight.findViewById(R.id.user_email_id);
        profilePic = (ImageView) headerRight.findViewById(R.id.profile_pic);
        Picasso.with(getApplicationContext()).load(MainSingleTon.userimage).into(user);
        if(MainSingleTon.userCoverPicUrl==null||MainSingleTon.userCoverPicUrl.equalsIgnoreCase(null)||MainSingleTon.userCoverPicUrl.equalsIgnoreCase("null")||MainSingleTon.userCoverPicUrl.length()<=3) {
           // Picasso.with(getApplicationContext()).load(MainSingleTon.userCoverPicUrl).into(pCoverPic);
        }
        else {
            Picasso.with(getApplicationContext()).load(MainSingleTon.userCoverPicUrl).into(pCoverPic);
        }
        System.out.println("Started !44");
        for (int i = 0; i < MainSingleTon.useridlist.size(); i++) {

            ModelUserDatas model = MainSingleTon.userdetails.get(MainSingleTon.useridlist.get(i));
            System.out.println("Started !45"+model.toString()+" "+MainSingleTon.useridlist.size());
            model.setUserid(model.getUserid());
            model.setUserAcessToken(model.getUserAcessToken());
            model.setUserimage(model.getUserimage());
            model.setUsername(model.getUsername());
            model.setUserEmail(model.getUserEmail());
            accountList.add(model);
            System.out.println("Started !46");
            System.out.println("Started !46"+model.getUserAcessToken()+" "+model.getUserid()+" " +model.getUserEmail()+" "+model.getUserimage()+" "+model.getUsername());

        }
        mDrawerList_Right.setAdapter(new AccountAdapter(MainActivity.this,accountList));
        System.out.println("Started !47");
        mDrawerList_Right.setOnItemClickListener(new RightDrawerItemClickListener());
        System.out.println("Started !48"+MainSingleTon.username+" "+MainSingleTon.userimage+" "+MainSingleTon.userEmail);


        setUser(MainSingleTon.username, MainSingleTon.userimage,MainSingleTon.userEmail);

        System.out.println("Started !49");
        ProfileFragment fragment = new ProfileFragment(MainSingleTon.userid);
        System.out.println("Started !50");
        fragmentManager = getSupportFragmentManager();
        System.out.println("Started !51");
        swipeFragment(fragment);

    }

    public void registration(boolean notificationstatus, String userid,String username,String useraccesstoken,String android_id,String getFirebaseToken)
    {
        FirebaseModel firebaseModel = new FirebaseModel(notificationstatus,userid,username,useraccesstoken,android_id,getFirebaseToken);
        mFirebaseDatabase.child("Fboardpro").child(userid).setValue(firebaseModel);
    }

    protected void addNewAccount()
    {

        MainSingleTon.isfrom_schedulefrag = false;
        System.out.println("Started !51"+"addNewAccount1");
        callbackManager = CallbackManager.Factory.create();
        System.out.println("Started !51"+"addNewAccount2");
        Uri targetUrl = AppLinks.getTargetUrlFromInboundIntent(MainActivity.this,getIntent());
        //System.out.println("Started !51"+"addNewAccount1"+targetUrl.toString());
        if (targetUrl != null)
        {
            Log.i("Activity", "App Link Target URL: " + targetUrl.toString());
        } else
        {
            AppLinkData.fetchDeferredAppLinkData(getBaseContext(),new AppLinkData.CompletionHandler()
            {
                        @Override
                        public void onDeferredAppLinkDataFetched(AppLinkData appLinkData) {
                            // process applink data
                            System.out.println("Started !51"+"addNewAccount12");
                            System.out.println("NOT ABLE TO FETCH THE P");
                        }
                    });
        }

        LoginManager.getInstance().setLoginBehavior(LoginBehavior.WEB_VIEW_ONLY);

        LoginManager.getInstance().logInWithReadPermissions(this,Arrays.asList("email", "user_photos", "public_profile",
                        "user_posts", "user_likes", "user_friends",
                        "user_hometown", "user_work_history", "user_location",
                        "user_birthday", "user_about_me"," publish_pages"));
        System.out.println("Started !51"+"addNewAccount1LoginManager");
        LoginManager.getInstance().registerCallback(callbackManager,new FacebookCallback<LoginResult>() {

                    public void onSuccess(LoginResult loginResult) {
                        System.out.println("Started !51"+"addNewAccount1LoginManager22");
                        Profile.fetchProfileForCurrentAccessToken();
                        System.out.println("Started !51"+"addNewAccount1LoginManager23");
                        myAccessToken = loginResult.getAccessToken();
                        System.out.println("Started !51"+"addNewAccount1LoginManager24"+myAccessToken);



                       /* myAccessToken = loginResult.getAccessToken();
                        System.out.println("AssssssssssssTokem  "+myAccessToken);
                        Profile.fetchProfileForCurrentAccessToken();*/
                        MainSingleTon.dummyAccesstoken = myAccessToken;
                        MainSingleTon.accesstoken=myAccessToken.getToken();
                        System.out.println("Inseid the success="+ loginResult.getAccessToken().getToken()+" "+MainSingleTon.accesstoken);
                        System.out.println("Inseid the success="+ loginResult.getAccessToken().getUserId());
                        System.out.println("Inseid the success="+ loginResult.getRecentlyGrantedPermissions());
                        if (loginResult.getRecentlyGrantedPermissions().contains("publish_actions")) {
                            System.out.println("Insdie tht profileTracker1D");

                            GraphRequest request = GraphRequest.newMeRequest(myAccessToken,	new GraphRequest.GraphJSONObjectCallback() {
                                @Override
                                public void onCompleted(JSONObject object,GraphResponse response) {
                                    jobj = object;
                                    try {

                                        if (object.has("email")) {
                                            currentUserEmailId = object.getString("email");
                                            System.out.println("currentUserEmailId: "+currentUserEmailId);
                                        }
                                       // new GetExtendedAccessToken_new().execute();
                                        //new GetExtendedAccessToken().execute();
                                        System.out.println("MainActivity......");

                                    } catch (JSONException e) {

                                        e.printStackTrace();
                                    }
                                }

                            });

                            Bundle parameters = new Bundle();
                            parameters.putString("fields", "id,first_name,name,email,location,birthday");
                            request.setParameters(parameters);
                            request.executeAsync();
                            Log.e("Information",request.toString());

                            // Graph request to get the user profile data like
                            // user email, name etc
                            GraphRequest.newMyFriendsRequest(myAccessToken,new GraphRequest.GraphJSONArrayCallback() {

                                @Override
                                public void onCompleted(JSONArray objects, GraphResponse response) {

                                    System.out.println("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$="+ response);
                                }
                            }).executeAsync();
                        } else {

                            System.out.println("else..........");
                            LoginManager.getInstance().logInWithPublishPermissions(MainActivity.this,Arrays.asList("publish_actions","manage_pages","publish_pages"));
                        }

                    }

                    @Override
                    public void onError(FacebookException error) {
                        AccessToken.setCurrentAccessToken(null);
                        System.out.println("Started !51"+"addNewAccount1LoginManager"+error.toString());
                    }

                    @Override
                    public void onCancel() {
                        AccessToken.setCurrentAccessToken(null);
                        System.out.println("Started !51"+"addNewAccount1LoginManager ---Cancel");
                    }
                });

        profileTracker = new ProfileTracker() {
            @Override
            protected void onCurrentProfileChanged(Profile oldProfile, Profile currentProfile)
            {

                if (profileTracker.isTracking()) {

                    fBprofile = currentProfile;
                    LoginManager.getInstance().logInWithPublishPermissions(MainActivity.this,Arrays.asList("publish_actions", "manage_pages"));
                    new GetExtendedAccessToken().execute();
                }
                else {

                }

            }
        };

    }


    //////////////////////////////////////////////////////////////////////
    public class GetExtendedAccessToken extends AsyncTask<Void, Void, String> {
        String extendedAccessToken;

        @Override
        protected String doInBackground(Void... params) {

            String tokenURL = " https://graph.facebook.com/oauth/access_token?client_id="
                    + getResources().getString(R.string.app_id)
                    + "&client_secret="
                    + getResources().getString(R.string.app_secret)
                    + "&grant_type=fb_exchange_token&fb_exchange_token="
                    + myAccessToken.getToken();
            System.out.println("Started !51"+"addNewAccount1LoginManager"+tokenURL);
            String dummtoken = Utilsss.getJSONString(tokenURL);
            dummtoken=dummtoken.replace("{access_token:","");
            String token1[]=dummtoken.split(":");
            System.out.println("Started !51"+"addNewAccount1LoginManager"+tokenURL+" "+dummtoken);

            String sss[]=token1[1].split("[,]+");
            System.out.println("Token--------"+sss[0]+" "+sss[1]);
            String cs=sss[0].substring(1,sss[0].length()-1);
            extendedAccessToken=cs;
            System.out.println("Token--------"+cs);
            System.out.println("Started !51"+"addNewAccount1LoginManager"+tokenURL+" "+dummtoken+" "+(extendedAccessToken=cs));
            System.out.println("Started !521"+"addNewAccount1LoginManager"+tokenURL+" "+dummtoken+" "+extendedAccessToken);
            return extendedAccessToken;
        }

        @Override
        protected void onPostExecute(String extendedAccessToken)
        {

            super.onPostExecute(extendedAccessToken);

            if (!MainSingleTon.userdetails.containsKey(myAccessToken.getUserId()))
            {

                ModelUserDatas modelUserDatas = new ModelUserDatas();

                if (extendedAccessToken != null) {
                    modelUserDatas.setUserAcessToken(extendedAccessToken);
                    System.out.println("--------------------Add "+extendedAccessToken);
                    MainSingleTon.accesstoken=extendedAccessToken;
                } else {
                    MainSingleTon.accesstoken = myAccessToken.getToken();

                    modelUserDatas.setUserAcessToken(MainSingleTon.accesstoken);
                    System.out.println("Started !----onPostExecute"+myAccessToken.toString()+myAccessToken.getToken()+" "+MainSingleTon.accesstoken);
                }
                modelUserDatas.setUserid(myAccessToken.getUserId());
                modelUserDatas.setUsername(fBprofile.getName());
                modelUserDatas.setUserimage(""+ fBprofile.getProfilePictureUri(240, 260));
                modelUserDatas.setUserEmail(currentUserEmailId);

                MainSingleTon.username = fBprofile.getName();
                MainSingleTon.userimage = ""+ fBprofile.getProfilePictureUri(240, 260);
                MainSingleTon.accesstoken = myAccessToken.getToken();
                MainSingleTon.userid = myAccessToken.getUserId();

                F_Board_LocalData fBoardData = new F_Board_LocalData(getApplicationContext());

                fBoardData.addNewUserAccount(modelUserDatas);

                MainSingleTon.userdetails.put(myAccessToken.getUserId(),modelUserDatas);

                MainSingleTon.useridlist.add(myAccessToken.getUserId());

                if (profileTracker.isTracking()) {
                    profileTracker.stopTracking();
                } else {
                }

                System.out.println("--------------------MainSingleTon.useridlist.size()="+ MainSingleTon.useridlist.size());

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

                setUser(MainSingleTon.username, MainSingleTon.userimage,MainSingleTon.userEmail);
                toolbar_title.setText(MainSingleTon.username);

                System.out.println("Started !49");
                ProfileFragment fragment = new ProfileFragment(MainSingleTon.userid);
                System.out.println("Started !50");
                fragmentManager = getSupportFragmentManager();
                System.out.println("Started !51");
                swipeFragment(fragment);

                LoginManager.getInstance().logOut();


            } else {
                Toast.makeText(getApplicationContext(),"Users Account already exist!!", Toast.LENGTH_SHORT).show();

                LoginManager.getInstance().logOut();

//                Intent intent = new Intent(getApplicationContext(),	MainActivity.class);
//                startActivity(intent);// start intent to move for home screen
//                finish();
            }
        }
    }

    public void setUser(final String namefinal, final String pPic,final String email)
    {
		/* ImageLoader imageLoader=new ImageLoader(this); */
        if(pPic==null)
        {
           // profilePic.setImageResource(R.drawable.account_image);
        }
        else {
            System.out.println("MainActivity: " + pPic);
/*            new Thread(new Runnable() {

                @Override
                public void run() {
                    new Handler().post(new Runnable() {

                        //Bitmap pfofile = MainSingleTon.getBitmapFromURL(pPic);

                        @Override
                        public void run() {
                            System.out.println("MainActivity---UrlPic: " + pPic + "  " + MainSingleTon.getBitmapFromURL(pPic));
                            //profilePic.setImageBitmap((pfofile));
                            Picasso.with(getApplicationContext()).load(pPic).into(profilePic);
                        }
                    });

                }
            }).start();*/
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                  //  System.out.println("MainActivity---UrlPic: " + pPic + "  " + MainSingleTon.getBitmapFromURL(pPic));
                    //profilePic.setImageBitmap((pfofile));
                    Picasso.with(getApplicationContext()).load(pPic).into(profilePic);
                }
            },600);
        }
		/* imageLoader.DisplayImage(pPic, profilePic); */
        profilePic.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) { fragmentManager = getSupportFragmentManager();
                MainActivity.swipeFragment(new ProfileFragment(MainSingleTon.userid));
                mDrawerLayout.closeDrawer(mDrawerList_Right);
                setTitle(MainSingleTon.username);

            }
        });
        // pCoverPic.setImageResource(R.drawable.header_image);

		/*
		 * imageLoader.DisplayImage("https://graph.facebook.com/" +
		 * MainSingleTon.userid+ "?fields=cover&access_token=" +
		 * MainSingleTon.accesstoken, pCoverPic);
		 */

        new GetUserCover().execute();

        pCoverPic.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                fragmentManager = getSupportFragmentManager();
                System.out.println("MainSingleTon.userid+:"+MainSingleTon.userid);
                MainActivity.swipeFragment(new ProfileFragment(MainSingleTon.userid));
                mDrawerLayout.closeDrawer(mDrawerList_Right);
                setTitle(MainSingleTon.username);
            }
        });
        userName.setText(MainSingleTon.username);
        userName.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                MainActivity.swipeFragment(new ProfileFragment(MainSingleTon.userid));
                mDrawerLayout.closeDrawer(mDrawerList_Right);
                setTitle(MainSingleTon.username);
            }
        });
        userEmailId.setText(MainSingleTon.userEmail);

        userEmailId.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                fragmentManager = getSupportFragmentManager();
                MainActivity.swipeFragment(new ProfileFragment(MainSingleTon.userid));
                mDrawerLayout.closeDrawer(mDrawerList_Right);
                setTitle(MainSingleTon.username);

            }
        });
        System.out.println("email " + MainSingleTon.userEmail);


        ////////////////////////////////////////////////////////////////////////////////////////////

        userid = MainSingleTon.userid;
        username = MainSingleTon.username;
        useraccesstoken=MainSingleTon.accesstoken;
        android_id = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
        getFirebaseToken = FirebaseInstanceId.getInstance().getToken();
        userFirstName = MainSingleTon.userFirstName;
        Email = email;
        notificationstatus = MainSingleTon.firebaseNotification;
        System.out.println("Data"+userid);
        System.out.println("Data"+username);
        System.out.println("Data"+useraccesstoken);
        System.out.println("Data"+android_id);
        System.out.println("Data"+getFirebaseToken);
        registration(notificationstatus,userid,username,useraccesstoken,android_id,getFirebaseToken);




//////////////////////////////////////////////////////////////////////////////////////////////////




    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
        // trySetupSwipeRefresh();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public void onBackPressed() {

        if (mDrawerLayout.isDrawerOpen(Gravity.RIGHT)) {
            mDrawerLayout.closeDrawer(Gravity.RIGHT);
        } else if (mDrawerLayout.isDrawerOpen(Gravity.LEFT)) {
            mDrawerLayout.closeDrawer(Gravity.LEFT);
        } else {
            if (fragmentManager.getBackStackEntryCount() < 1) {
                if (doubleBackToExitPressedOnce) {
                    super.onBackPressed();
                    return;
                }
                this.doubleBackToExitPressedOnce = true;

                Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

                new Handler().postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        doubleBackToExitPressedOnce = false;
                    }
                }, 2000);
            } else {
                fragmentManager.popBackStack();
            }
        }

    }

    /**
     * Swaps fragments in the main content view
     */
    private void selectItem(int position) {
        Fragment fragment = null;

        System.out.println("selected position " + position);

        switch (position) {
            case 0:
                fragment = new ProfileFragment(MainSingleTon.userid);
                //toolbar_title.setText(MainSingleTon.username);
                toolbar_title.setText(mDrawerTitles[position]);
                break;
            
//            case 1:
//                fragment = new HomeFeed_Fragment();
//                System.out.println("DrawerItem=="+ mDrawerTitles[position]);
//                toolbar_title.setText(mDrawerTitles[position]);
//                break;

            case 1:
                fragment = new MyFeeds_Fragment();
                toolbar_title.setText(mDrawerTitles[position]);
                break;

            case 2:
                fragment = new FriendsFragment();
                toolbar_title.setText(mDrawerTitles[position]);
                break;

            case 3:
                fragment = new Pages_Fragment();
                toolbar_title.setText(mDrawerTitles[position]);
                break;

            case 4:
                fragment = new Mygroup();
                toolbar_title.setText(mDrawerTitles[position]);
                break;

            case 5:
                fragment = new SearchFragment();
                toolbar_title.setText(mDrawerTitles[position]);
                break;

            case 6:
                //fragment = new ChoosePage();
                fragment = new AutoLiker();
                toolbar_title.setText(mDrawerTitles[position]);
                break;

            case 7:
                fragment = new SchedulerFragment();
                toolbar_title.setText(mDrawerTitles[position]);
                break;
            case 8:
                fragment = new ShareagonLinks();
                toolbar_title.setText(mDrawerTitles[position]);
                break;

            case 9:
                fragment = new ShareagonPage();
                toolbar_title.setText(mDrawerTitles[position]);
                break;

            case 10:
                fragment = new ShareagonGroup();
                toolbar_title.setText(mDrawerTitles[position]);
                break;

        }

        if (fragment != null) {
			/*
			 * // Insert the fragment by replacing any existing fragment
			 * FragmentManager fragmentManager = getSupportFragmentManager();
			 * fragmentManager.beginTransaction().replace(R.id.main_content,
			 * fragment).addToBackStack(null).commit();
			 */
            fragmentManager = getSupportFragmentManager();
            swipeFragment(fragment);

        }

        // Highlight the selected item, update the title, and close the drawer
        if (mDrawerList_Left.isEnabled()) {
            mDrawerList_Left.setItemChecked(position, true);

            setTitle(mDrawerTitles[position]);
            updateView(position, position, true,mDrawerList_Left);

            mDrawerLayout.closeDrawer(mDrawerList_Left);
        } else {
            mDrawerList_Right.setItemChecked(position, true);
            if (position != 0) {
                setTitle(mDrawerTitles[position - 1]);
                 updateView(position, position, true,mDrawerList_Right);
            }
            mDrawerLayout.closeDrawer(mDrawerList_Right);
        }

    }

    @Override
    public void setTitle(CharSequence title) {
        mTitle = MainSingleTon.userFirstName;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        return true;
    }

    /* Called whenever we call invalidateOptionsMenu() */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        mMenu = menu;

        if (mDrawerLayout.isDrawerOpen(mDrawerList_Right)) {
            mDrawerLayout.closeDrawer(mDrawerList_Right);
            mMenu.findItem(R.id.action_settings).setVisible(true);
            mMenu.findItem(R.id.action_settings).setVisible(false);
        } else {
            mDrawerLayout.openDrawer(mDrawerList_Right);
            mMenu.findItem(R.id.action_settings).setVisible(true);
            mMenu.findItem(R.id.action_settings).setVisible(false);
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;

        }
        if (mDrawerLayout.isDrawerOpen(mDrawerList_Right)) {
            mDrawerLayout.closeDrawer(mDrawerList_Right);
        } else {
            mDrawerLayout.openDrawer(mDrawerList_Right);
        }
        return super.onOptionsItemSelected(item);
    }

    public int calculateDrawerWidth() {
        // Calculate ActionBar height
        TypedValue tv = new TypedValue();
        int actionBarHeight = 0;
        if (getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true)) {
            actionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data,getResources().getDisplayMetrics());
        }

        Display display = getWindowManager().getDefaultDisplay();
        int width;
        int height;
        if (android.os.Build.VERSION.SDK_INT >= 13) {
            Point size = new Point();
            display.getSize(size);
            width = size.x;
            height = size.y;
        } else {
            width = display.getWidth(); // deprecated
            height = display.getHeight(); // deprecated
        }
        return width - actionBarHeight;
    }

    private void updateView(int position, int counter, boolean visible,ListView mDrawerList) {

        View v = mDrawerList.getChildAt(position
                - mDrawerList.getFirstVisiblePosition());
//        TextView someText = (TextView) v.findViewById(R.id.item_new);
        Resources res = getResources();
        String articlesFound = "";

        switch (position) {
            case 1:
                articlesFound = res.getQuantityString(
                        R.plurals.numberOfNewArticles, counter, counter);
                //someText.setBackgroundResource(R.drawable.new_apps);
                break;
            case 2:
                articlesFound = res.getQuantityString(
                        R.plurals.numberOfNewArticles, counter, counter);
                //someText.setBackgroundResource(R.drawable.new_sales);
                break;
            case 3:
                articlesFound = res.getQuantityString(
                        R.plurals.numberOfNewArticles, counter, counter);
               // someText.setBackgroundResource(R.drawable.new_blog);
                break;
            case 4:
                articlesFound = res.getQuantityString(
                        R.plurals.numberOfNewArticles, counter, counter);
               // someText.setBackgroundResource(R.drawable.new_bookmark);
                break;
            case 5:
                articlesFound = res.getQuantityString(
                        R.plurals.numberOfNewArticles, counter, counter);
               // someText.setBackgroundResource(R.drawable.new_community);
                break;
        }

       // someText.setText(articlesFound);
       // if (visible)
            //someText.setVisibility(View.VISIBLE);
    }

    @Override
    public boolean canSwipeRefreshChildScrollUp() {
        return false;
    }

    private class LeftDrawerItemClickListener implements
            ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectItem(position);
        }
    }

    private class RightDrawerItemClickListener implements  ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view,int positionRightlist, long id) {
            if (positionRightlist - 1 != -1) {
                selectItemRight(positionRightlist);
            } else {

            }
        }
    }

    private void selectItemRight(final int positionRightlist) {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ModelUserDatas model = MainSingleTon.userdetails.get(accountList.get(positionRightlist - 1).getUserid());
                MainSingleTon.userid = model.getUserid();
                MainSingleTon.username = model.getUsername();
                MainSingleTon.userimage = model.getUserimage();
                F_Board_LocalData f_board_localData=new F_Board_LocalData(getApplicationContext());
               MainSingleTon.accesstoken =f_board_localData.returnAccessTokenByUserId(MainSingleTon.userid);

                MainSingleTon.userEmail = model.getUserEmail();
                MainSingleTon.feedArrayList.clear();

                MainSingleTon.isPAgesLoaded = false;

              /*  System.out.println("Checking:--------"+MainSingleTon.userid+" "+MainSingleTon.username+" "+MainSingleTon.userimage+"" +
                        " "+MainSingleTon.userEmail+""+MainSingleTon.accesstoken);
*/              //  Toast.makeText(getApplicationContext(),"Checking:--------MainActivity--1037",Toast.LENGTH_LONG).show();
                setUser(MainSingleTon.username, MainSingleTon.userimage,MainSingleTon.userEmail);
                toolbar_title.setText(MainSingleTon.username);

            }
        });
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                fragmentManager = getSupportFragmentManager();
                System.out.println("------------"+MainSingleTon.userid);
                swipeFragment(new ProfileFragment(MainSingleTon.userid));
                setTitle(MainSingleTon.username);
                mDrawerLayout.closeDrawer(mDrawerList_Right);
            }
        }, 1000);
       /* fragmentManager = getSupportFragmentManager();
        swipeFragment(new ProfileFragment(MainSingleTon.userid));
        setTitle(MainSingleTon.username);
        mDrawerLayout.closeDrawer(mDrawerList_Right);*/
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Bundle bundle = data.getExtras();

        System.out.println("BUNDLDLDLDLLDLD = " + bundle);
        if (!MainSingleTon.isfrom_schedulefrag) {
            System.out.println("FALSE");
            callbackManager.onActivityResult(requestCode, resultCode, data);
        }

    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public void circleIn(View view) {

        // get the center for the clipping circle
        int cx = (view.getLeft() + view.getRight()) / 2;
        int cy = (view.getTop() + view.getBottom()) / 2;

        // get the final radius for the clipping circle
        int finalRadius = Math.max(view.getWidth(), view.getHeight());

        // create the animator for this view (the start radius is zero)
        Animator anim = ViewAnimationUtils.createCircularReveal(view, cx, cy,0, finalRadius);

        // make the view visible and start the animation
        view.setVisibility(View.VISIBLE);
        anim.start();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public void circleOut(final View view) {

        // get the center for the clipping circle
        int cx = (view.getLeft() + view.getRight()) / 2;
        int cy = (view.getTop() + view.getBottom()) / 2;

        // get the initial radius for the clipping circle
        int initialRadius = view.getWidth();

        // create the animation (the final radius is zero)
        Animator anim = ViewAnimationUtils.createCircularReveal(view, cx, cy,
                initialRadius, 0);

        // make the view invisible when the animation is done
        anim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                view.setVisibility(View.INVISIBLE);
            }
        });

        // start the animation
        anim.start();
    }

    public static void swipeFragment(Fragment fragment) {

        fragmentManager.beginTransaction().replace(R.id.main_content, fragment).commit();
    }

    /* class to GetUserDetails a post */
    public class GetUserCover extends AsyncTask<String, Void, String> {
        String userFBaccesToken = null;
        String coverUrl1 = null;

        @Override
        protected String doInBackground(String... params) {
            userFBaccesToken = MainSingleTon.accesstoken;

            String coverUrl = "https://graph.facebook.com/"+ MainSingleTon.userid + "?fields=cover&access_token="+ userFBaccesToken;
            System.out.println("inside set user  coverUrl" + coverUrl);
            JSONParseraa jsonParser = new JSONParseraa();

            JSONObject jsonCover = jsonParser.getJSONFromUrl(coverUrl);
            try {
                if (jsonCover != null) {
                    if (jsonCover.has("cover")) {
                        System.out.println("inside set use try "+ jsonCover.getString("cover"));
                        JSONObject jsonObject2 = jsonCover.getJSONObject("cover");
                        if (jsonObject2.has("source")) {
                            System.out.println(" inside set use source  "+ jsonObject2.getString("source"));
                            coverUrl1 = jsonObject2.getString("source");
                        }

                    }
                }

            } catch (JSONException e) {
                e.printStackTrace();
                System.out.println("error " + e);
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            if (coverUrl1 != null) {
                ImageLoader imageLoader = new ImageLoader(MainActivity.this);
                imageLoader.DisplayImage(coverUrl1, pCoverPic);
                System.out.println("Cover----------"+coverUrl1);
            } else {
                pCoverPic.setImageResource(R.drawable.header_image);
            }

        }

    }

    public static void makeToast(String toast_msg) {
        Toast.makeText(context, toast_msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onStart() {
        super.onStart();
        FlurryAgent.onStartSession(getApplicationContext(),"D8T57RYRKJPV7K5FRQXM");
    }

    @Override
    protected void onStop() {
        super.onStop();
        FlurryAgent.onEndSession(this);
    }

}
