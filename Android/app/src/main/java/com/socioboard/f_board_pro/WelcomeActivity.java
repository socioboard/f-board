package com.socioboard.f_board_pro;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.facebook.ads.*;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.Signature;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

//import com.appnext.appnextsdk.AppnextTrack;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphRequest.GraphJSONArrayCallback;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.login.LoginBehavior;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.socioboard.f_board_pro.adapter.Viewpageradapter;
import com.socioboard.f_board_pro.database.util.F_Board_LocalData;
import com.socioboard.f_board_pro.database.util.MainSingleTon;
import com.socioboard.f_board_pro.database.util.ModelUserDatas;
import com.socioboard.f_board_pro.database.util.Utilsss;
import com.socioboard.f_board_pro.models.IntroViewPagerModel;
import com.viewpagerindicator.PageIndicator;

public class WelcomeActivity extends Activity {

	CallbackManager callbackManager;
	F_Board_LocalData fBoardLocalData;
	AccessToken myAccessToken = null;
	String currentUserEmailId = null;
	ImageView btn;
	Handler handler; //
	private InterstitialAd interstitialAd;

	Profile profile;
	JSONObject jobj;
	String extendedAccessToken = null;
    static String Token="";
	PageIndicator indicator;
	Viewpageradapter viewpageradapter;
	ArrayList<IntroViewPagerModel> arrayList = new ArrayList<IntroViewPagerModel>();
	TextView textView_privacy_prompt;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		FacebookSdk.sdkInitialize(WelcomeActivity.this.getApplicationContext());

		//AppnextTrack.track(this);

		setContentView(R.layout.activity_welcome);
		textView_privacy_prompt = (TextView) findViewById(R.id.textView_privacy_prompt);
		ViewPager pager = (ViewPager) findViewById(R.id.pager);
		indicator = (PageIndicator) findViewById(R.id.indicator);

		IntroViewPagerModel model = new IntroViewPagerModel();
		model.setDrawable(R.drawable.intro_screen1);
		model.setIntro_text(MainSingleTon.introtext);
		model.setColor("#2196F3");
		arrayList.add(model);

		IntroViewPagerModel model2 = new IntroViewPagerModel();
		model2.setDrawable(R.drawable.intro_screen3);
		model2.setIntro_text("Check all your profile feeds");
		model2.setColor("#2196F3");
		arrayList.add(model2);

		IntroViewPagerModel model3 = new IntroViewPagerModel();
		model3.setDrawable(R.drawable.intro_screen5);
		model3.setIntro_text("List of pages, which you liked or following");
		model3.setColor("#2196F3");
		arrayList.add(model3);

		IntroViewPagerModel model4 = new IntroViewPagerModel();
		model4.setDrawable(R.drawable.intro_screen6);
		model4.setIntro_text("Type and search people, pages, groups, events and places");
		model4.setColor("#2196F3");
		arrayList.add(model4);

		viewpageradapter = new Viewpageradapter(getApplicationContext(),arrayList);
		pager.setAdapter(viewpageradapter);
		indicator.setViewPager(pager);

		fBoardLocalData = new F_Board_LocalData(getApplicationContext());

		handler = new Handler();
		PackageInfo info;
		try {

			info = getPackageManager().getPackageInfo("com.socioboard.f_board_pro",PackageManager.GET_SIGNATURES);

			for (Signature signature : info.signatures) {

				MessageDigest md;
				md = MessageDigest.getInstance("SHA");
				md.update(signature.toByteArray());
				String something = new String(Base64.encode(md.digest(), 0));
				Log.e("hash key", something);
				System.out.println("keyy hashhhh " + something);
			}

		} catch (NameNotFoundException e1) {
			Log.e("name not found", e1.toString());
		} catch (NoSuchAlgorithmException e) {
			Log.e("no such an algorithm", e.toString());
		} catch (Exception e) {
			Log.e("exception", e.toString());
		}

		btn = (ImageView) findViewById(R.id.btn);
		btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				LoginWithFB();
			}
		});

	}

	public void LoginWithFB() {

		LoginManager.getInstance().setLoginBehavior(LoginBehavior.WEB_VIEW_ONLY);
		callbackManager = CallbackManager.Factory.create();
		LoginManager.getInstance().logInWithReadPermissions(this,
				Arrays.asList("email", "user_photos", "public_profile",
						"user_posts", "user_likes", "user_friends",
						"user_hometown", "user_work_history", "user_location",
						"user_birthday", "user_about_me",
						"user_managed_groups"));

		LoginManager.getInstance().registerCallback(callbackManager,new FacebookCallback<LoginResult>() {

					@Override
					public void onSuccess(LoginResult loginResult) {
						// connectFacebook.setPublishPermissions(Arrays.asList("publish_actions"));
						myAccessToken = loginResult.getAccessToken();
						System.out.println("AssssssssssssTokem  "+myAccessToken);
						Profile.fetchProfileForCurrentAccessToken();
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
												new GetExtendedAccessToken_new().execute();
												System.out.println("WelcomeActivity......");

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
							GraphRequest.newMyFriendsRequest(myAccessToken,new GraphJSONArrayCallback() {

										@Override
										public void onCompleted(JSONArray objects,GraphResponse response) {

											System.out.println("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$="+ response);
										}
									}).executeAsync();
						} else {

							System.out.println("else..........");
							LoginManager.getInstance().logInWithPublishPermissions(WelcomeActivity.this,Arrays.asList("publish_actions","manage_pages"));
						}

					}

					@Override
					public void onError(FacebookException error) {
						AccessToken.setCurrentAccessToken(null);
						MainSingleTon.showMyToast(WelcomeActivity.this,error.toString());
					}

					@Override
					public void onCancel() {
						AccessToken.setCurrentAccessToken(null);
						MainSingleTon.showMyToast(WelcomeActivity.this,"Cancel Permission ,Please! Allow Permissions");
					}
				});

	}

	@Override
	protected void onResume() {

		super.onResume();

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		callbackManager.onActivityResult(requestCode, resultCode, data);

	}

	@Override
	public void onDestroy() {
		super.onDestroy();

	}

	// AsyncTask class to get the extended access token from current access
	// token
	/*public class GetExtendedAccessToken extends AsyncTask<Void, Void, String> {

		@Override
		protected String doInBackground(Void... params) {

			String tokenURL = "https://graph.facebook.com/oauth/access_token?client_id="
					+ getResources().getString(R.string.app_id)
					+ "&client_secret="
					+ getResources().getString(R.string.app_secret)
					+ "&grant_type=fb_exchange_token&fb_exchange_token="
					+ myAccessToken.getToken();// CURRENT_ACCESS_TOKEN"

			String dummtoken = Utilsss.getJSONString(tokenURL);

			System.out.println("EXXXXXXXXXXTEDED ACCESSTOKEN= " + dummtoken);

			*//*
			 * dummtoken = dummtoken.substring(dummtoken.indexOf("="),
			 * dummtoken.indexOf("&"));
			 * 
			 * extendedAccessToken = dummtoken.replace("=", "");
			 *//*

			extendedAccessToken = dummtoken.replace("access_token=", "");
			extendedAccessToken = extendedAccessToken.substring(0,
					extendedAccessToken.indexOf('&'));

			System.out.println("DDSSDDSASDDSF= " + extendedAccessToken);

			return extendedAccessToken;
		}

		@Override
		protected void onPostExecute(String result) {

			super.onPostExecute(result);

			fBoardLocalData.getAllUsersData();

			if (!MainSingleTon.userdetails.isEmpty()) {
				if (!MainSingleTon.userdetails.containsKey(myAccessToken.getUserId())) {

					System.out.println("Account Exostssssssssssssss");

				}
			} else {
				ModelUserDatas modelUserDatas = new ModelUserDatas();

				if (extendedAccessToken != null) {
					modelUserDatas.setUserAcessToken(extendedAccessToken);

					System.out.println("ExTEDDD TOKEN STORED = "+ extendedAccessToken);
				} else {
					System.out.println("ExTEDDD TOKEN NOT STORED = "+ extendedAccessToken);
					modelUserDatas.setUserAcessToken(myAccessToken.getToken());
				}

				try {
					modelUserDatas.setUserid(myAccessToken.getUserId());
					modelUserDatas.setUsername(jobj.getString("first_name"));
					modelUserDatas.setUserimage("https://graph.facebook.com/"+ myAccessToken.getUserId()+ "/picture?type=small");
					modelUserDatas.setUserEmail(currentUserEmailId);
					MainSingleTon.username = jobj.getString("first_name");
					MainSingleTon.userimage = "https://graph.facebook.com/"+ myAccessToken.getUserId() + "/picture?type=small";
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				MainSingleTon.accesstoken = myAccessToken.getToken();
				MainSingleTon.userid = myAccessToken.getUserId();

				System.out.println("///////////"+myAccessToken.getToken()+" "+myAccessToken.getUserId());
				fBoardLocalData.addNewUserAccount(modelUserDatas);
				MainSingleTon.userdetails.put(myAccessToken.getUserId(),modelUserDatas);
				MainSingleTon.useridlist.add(myAccessToken.getUserId());

				SharedPreferences lifesharedpref = getSharedPreferences(SharedPrefrence.FacebookSharedPrefrence, Context.MODE_PRIVATE);
				SharedPreferences.Editor editor = lifesharedpref.edit();
				editor.putString(SharedPrefrence.FacebookUserId, myAccessToken.getUserId());
				editor.commit();
			}

			// once done with first user just logged out

			LoginManager.getInstance().logOut();

			Intent intent = new Intent(getApplicationContext(),
					MainActivity.class);
			startActivity(intent);// start intent to move for home screen
			finish();

		}
	}*/

	private class GetExtendedAccessToken_new extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... params) {
			// Creating service handler class instance
			String tokenURL = "https://graph.facebook.com/oauth/access_token?client_id="
					+ getResources().getString(R.string.app_id)
					+ "&client_secret="
					+ getResources().getString(R.string.app_secret)
					+ "&grant_type=fb_exchange_token&fb_exchange_token="
					+ myAccessToken.getToken();
			ServiceHandler sh = new ServiceHandler();

			String dummtoken = sh.makeServiceCall(tokenURL, ServiceHandler.GET);
			System.out.println("Hello........"+dummtoken+" "+tokenURL);
			if (dummtoken != null) {
				System.out.println("EXXXXXXXXXXTEDED ACCESSTOKEN= " + dummtoken);

				extendedAccessToken = dummtoken.replace("access_token=", "");
				if (extendedAccessToken.contains(":")) {
					String token1[]=dummtoken.split(":");
					System.out.println("Started !51"+"addNewAccount1LoginManager"+tokenURL+" "+dummtoken);

					System.out.println("Token--------"+token1.toString()+" "+token1.length+" "+token1[1]);
					String sss[]=token1[1].split("[,]+");
					System.out.println("Token--------"+sss[0]+" "+sss[1]);
					String cs=sss[0].substring(1,sss[0].length()-1);
					extendedAccessToken=cs;
				}

				System.out.println("DDSSDDSASDDSF= " + extendedAccessToken);
			} else {
				Log.e("ServiceHandler", "Couldn't get any data from the url");
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {

			super.onPostExecute(result);

			fBoardLocalData.getAllUsersData();

			if (!MainSingleTon.userdetails.isEmpty()) {
				if (!MainSingleTon.userdetails.containsKey(myAccessToken.getUserId())) {
					System.out.println("Account Exostssssssssssssss");
				}
			} else {
				final ModelUserDatas modelUserDatas = new ModelUserDatas();

				if (extendedAccessToken != null) {
					modelUserDatas.setUserAcessToken(extendedAccessToken);
					System.out.println("ExTEDDD TOKEN STORED = "+ extendedAccessToken);
				} else {
					System.out.println("ExTEDDD TOKEN NOT STORED = "+ extendedAccessToken);
                    MainSingleTon.accesstoken=myAccessToken.getToken();

					System.out.println("Hello.............11"+MainSingleTon.accesstoken);
					String aaccesssToken[]=MainSingleTon.accesstoken.split("[\"]+");
					System.out.println("Hello........ "+aaccesssToken[0]+" "+aaccesssToken[1]+" " +aaccesssToken[3]+" "+aaccesssToken[2]+" "+aaccesssToken[4]);
					modelUserDatas.setUserAcessToken(MainSingleTon.accesstoken);

				}

				System.out.println("Hello................");
				try {
					modelUserDatas.setUserid(myAccessToken.getUserId());
					modelUserDatas.setUsername(jobj.getString("first_name"));
					modelUserDatas.setUserimage("https://graph.facebook.com/"+ myAccessToken.getUserId()+ "/picture?type=small");
					modelUserDatas.setUserEmail(currentUserEmailId);
					MainSingleTon.userid=myAccessToken.getUserId();
					MainSingleTon.userFirstName = jobj.getString("first_name");
					MainSingleTon.username = jobj.getString("name");
					MainSingleTon.userEmail=currentUserEmailId;
					MainSingleTon.userimage = "https://graph.facebook.com/"+ myAccessToken.getUserId() + "/picture?type=small";
					System.out.println("Hello........1"+MainSingleTon.userFirstName+" "+MainSingleTon.userimage+" "+currentUserEmailId+" "+myAccessToken);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				new Handler().postDelayed(new Runnable() {
					@Override
					public void run() {
						//MainSingleTon.accesstoken = myAccessToken.getToken();
						MainSingleTon.userid = myAccessToken.getUserId();

						fBoardLocalData.addNewUserAccount(modelUserDatas);
						MainSingleTon.userdetails.put(myAccessToken.getUserId(),modelUserDatas);
						MainSingleTon.useridlist.add(myAccessToken.getUserId());
					}
				},800);


			/*	SharedPreferences lifesharedpref = getSharedPreferences(SharedPrefrence.FacebookSharedPrefrence, Context.MODE_PRIVATE);
				SharedPreferences.Editor editor = lifesharedpref.edit();
				editor.putString(SharedPrefrence.FacebookUserId, myAccessToken.getUserId());
				editor.putString("",MainSingleTon.username);
				editor.commit();*/
			}

			// once done with first user just logged out
			LoginManager.getInstance().logOut();
			Intent intent = new Intent(getApplicationContext(),	MainActivity.class);
			startActivity(intent);// start intent to move for home screen
			finish();

		}

	}
	private void loadInterstitialAd() {
		interstitialAd = new InterstitialAd(this, "657968354387149_657983274385657");
		//interstitialAd.setAdListener(com.facebook.ads.InterstitialAdListener);
		interstitialAd.loadAd();
	}


	public void onError(Ad ad, AdError error) {
		// Ad failed to load
	}


	public void onAdLoaded(Ad ad) {
		// Ad is loaded and ready to be displayed
		// You can now display the full screen add using this code:
		interstitialAd.show();
	}
}