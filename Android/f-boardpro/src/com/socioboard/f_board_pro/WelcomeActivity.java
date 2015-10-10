package com.socioboard.f_board_pro;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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

import com.appnext.appnextsdk.AppnextTrack;
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

	Profile profile;
	JSONObject jobj;
	String extendedAccessToken = null;

	PageIndicator indicator;
	Viewpageradapter viewpageradapter;
	ArrayList<IntroViewPagerModel> arrayList = new ArrayList<IntroViewPagerModel>();
	TextView textView_privacy_prompt;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		FacebookSdk.sdkInitialize(WelcomeActivity.this.getApplicationContext());   

		AppnextTrack.track(this);

		setContentView(R.layout.activity_welcome);

		textView_privacy_prompt = (TextView) findViewById(R.id.textView_privacy_prompt);

		ViewPager pager = (ViewPager) findViewById(R.id.pager);

		indicator      = (PageIndicator) findViewById(R.id.indicator);

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

		viewpageradapter = new Viewpageradapter(getApplicationContext(),
				arrayList);

		pager.setAdapter(viewpageradapter);

		indicator.setViewPager(pager);

		fBoardLocalData = new F_Board_LocalData(getApplicationContext());

		handler = new Handler();

		PackageInfo info;

		try {

			info = getPackageManager()
					.getPackageInfo("com.socioboard.f_board_pro",
							PackageManager.GET_SIGNATURES);

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

		callbackManager = CallbackManager.Factory.create();

		//LoginManager.getInstance().setLoginBehavior(LoginBehavior.SUPPRESS_SSO);

		LoginManager.getInstance().logInWithReadPermissions(
				this,
				Arrays.asList("email", "user_photos", "public_profile",
						"user_posts", "user_likes", "user_friends",
						"user_hometown", "user_work_history", "user_location",
						"user_birthday", "user_about_me", "user_groups","read_stream"));

		LoginManager.getInstance().registerCallback(callbackManager,
				new FacebookCallback<LoginResult>() {

			@Override
			public void onSuccess(LoginResult loginResult) {
				// connectFacebook.setPublishPermissions(Arrays.asList("publish_actions"));

				myAccessToken = loginResult.getAccessToken();

				Profile.fetchProfileForCurrentAccessToken();

				MainSingleTon.dummyAccesstoken = myAccessToken;

				System.out.println("Inseid the success="
						+ loginResult.getAccessToken().getToken());
				System.out.println("Inseid the success="
						+ loginResult.getAccessToken().getUserId());
				System.out.println("Inseid the success="
						+ loginResult.getRecentlyGrantedPermissions());

				if (loginResult.getRecentlyGrantedPermissions()
						.contains("publish_actions")) {
					System.out.println("Insdie tht profileTracker1D");

					GraphRequest request = GraphRequest.newMeRequest(
							myAccessToken,
							new GraphRequest.GraphJSONObjectCallback() {
								@Override
								public void onCompleted(
										JSONObject object,
										GraphResponse response) {


									jobj = object;

									try {

										if (object.has("email")) {
											currentUserEmailId = object
													.getString("email");

										}

										new GetExtendedAccessToken()
										.execute();

									} catch (JSONException e) {

										e.printStackTrace();
									}
								}

							});

					request.executeAsync();

					// Graph request to get the user profile data like
					// user email, name etc
					GraphRequest.newMyFriendsRequest(myAccessToken,
							new GraphJSONArrayCallback() {

						@Override
						public void onCompleted(
								JSONArray objects,
								GraphResponse response) {

							System.out
							.println("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$="
									+ response);
						}
					}).executeAsync();
				} else {

					System.out.println("else..........");
					LoginManager.getInstance()
					.logInWithPublishPermissions(
							WelcomeActivity.this,
							Arrays.asList("publish_actions",
									"manage_pages"));
				}

			}

			@Override
			public void onError(FacebookException error) {
				AccessToken.setCurrentAccessToken(null);

			}

			@Override
			public void onCancel() {
				AccessToken.setCurrentAccessToken(null);

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
	public class GetExtendedAccessToken extends AsyncTask<Void, Void, String> {

		@Override
		protected String doInBackground(Void... params) {

			String tokenURL = " https://graph.facebook.com/oauth/access_token?client_id="
					+ getResources().getString(R.string.app_id)
					+ "&client_secret="
					+ getResources().getString(R.string.app_secret)
					+ "&grant_type=fb_exchange_token&fb_exchange_token="
					+ myAccessToken.getToken();// CURRENT_ACCESS_TOKEN"

			String dummtoken = Utilsss.getJSONString(tokenURL);

			System.out.println("EXXXXXXXXXXTEDED ACCESSTOKEN= " + dummtoken);

			dummtoken = dummtoken.substring(dummtoken.indexOf("="),
					dummtoken.indexOf("&"));

			extendedAccessToken = dummtoken.replace("=", "");

			System.out.println("DDSSDDSASDDSF= " + extendedAccessToken);

			return extendedAccessToken;
		}

		@Override
		protected void onPostExecute(String result) {

			super.onPostExecute(result);
 

			fBoardLocalData.getAllUsersData();

			if (!MainSingleTon.userdetails.isEmpty()) {
				if (!MainSingleTon.userdetails.containsKey(myAccessToken
						.getUserId())) {

					System.out.println("Account Exostssssssssssssss");

				}
			} else {
				ModelUserDatas modelUserDatas = new ModelUserDatas();

				if (extendedAccessToken != null) {
					modelUserDatas.setUserAcessToken(extendedAccessToken);

					System.out.println("ExTEDDD TOKEN STORED = "
							+ extendedAccessToken);
				} else {
					System.out.println("ExTEDDD TOKEN NOT STORED = "
							+ extendedAccessToken);

					modelUserDatas.setUserAcessToken(myAccessToken.getToken());
				}

				try {
					modelUserDatas.setUserid(myAccessToken.getUserId());
					modelUserDatas.setUsername(jobj.getString("first_name"));
					modelUserDatas.setUserimage("https://graph.facebook.com/"
							+ myAccessToken.getUserId()
							+ "/picture?type=large");
					modelUserDatas.setUserEmail(currentUserEmailId);
					MainSingleTon.username = jobj.getString("first_name");
					MainSingleTon.userimage = "https://graph.facebook.com/"
							+ myAccessToken.getUserId()
							+ "/picture?type=large";
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				MainSingleTon.accesstoken = myAccessToken.getToken();
				MainSingleTon.userid = myAccessToken.getUserId();

				fBoardLocalData.addNewUserAccount(modelUserDatas);
				MainSingleTon.userdetails.put(myAccessToken.getUserId(),
						modelUserDatas);
				MainSingleTon.useridlist.add(myAccessToken.getUserId());

				SharedPreferences lifesharedpref = getSharedPreferences(
						"FacebookBoard", Context.MODE_PRIVATE);
				SharedPreferences.Editor editor = lifesharedpref.edit();
				editor.putString("userid", myAccessToken.getUserId());
				editor.commit();
			}

			// once done with first user just logged out

			LoginManager.getInstance().logOut();

			Intent intent = new Intent(getApplicationContext(),
					MainActivity.class);
			startActivity(intent);// start intent to move for home screen
			finish();

			
		}
	}

}