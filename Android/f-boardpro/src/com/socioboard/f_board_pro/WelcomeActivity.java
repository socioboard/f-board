package com.socioboard.f_board_pro;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
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
import android.util.Base64;
import android.util.Log;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphRequest.GraphJSONArrayCallback;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.socioboard.f_board_pro.database.util.F_Board_LocalData;
import com.socioboard.f_board_pro.database.util.MainSingleTon;
import com.socioboard.f_board_pro.database.util.ModelUserDatas;
import com.socioboard.f_board_pro.database.util.Utilsss;

public class WelcomeActivity extends Activity {
	
	LoginButton connectFacebook;
	CallbackManager callbackManager;
	F_Board_LocalData fBoardLocalData;
	AccessToken myAccessToken = null;
	ProfileTracker profileTracker;
	String currentUserEmailId = null;
	Handler handler; //

	Profile profile;
	String extendedAccessToken =null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);

		FacebookSdk.sdkInitialize(WelcomeActivity.this.getApplicationContext()); //Initialize Facebook SDK  

		setContentView(R.layout.activity_welcome);

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
				System.out.println("keyy hashhhh "+something);
			}

		}
		catch (NameNotFoundException e1) 
		{
			Log.e("name not found", e1.toString());
		} 
		catch (NoSuchAlgorithmException e) 
		{
			Log.e("no such an algorithm", e.toString());
		}
		catch (Exception e) 
		{
			Log.e("exception", e.toString());
		}

		callbackManager = CallbackManager.Factory.create(); //The CallbackManager manages the callbacks into the FacebookSdk from an Activity's  onActivityResult() method.

		connectFacebook = (LoginButton) findViewById(R.id.connectfacebook); //Facebook login button 

		connectFacebook.setReadPermissions(Arrays.asList("email","user_photos",
				"public_profile", "user_posts", 
				"user_likes",
				"user_friends","read_stream",
				"user_hometown","user_work_history",
				"user_location",
				"user_birthday","user_about_me"
				)); ///set facebook permission to get the user details
		/*connectFacebook.setReadPermissions(Arrays.asList("email","user_photos", "public_profile"));*/
connectFacebook.clearPermissions();
		
		//connectFacebook.setPublishPermissions(Arrays.asList("publish_actions","publish_pages"));
		
		connectFacebook.registerCallback(callbackManager,
				new FacebookCallback<LoginResult>() {

			@Override
			public void onSuccess(LoginResult loginResult)
			{
					connectFacebook.setPublishPermissions(Arrays.asList("publish_actions"));
				
				myAccessToken = loginResult.getAccessToken();
				
				MainSingleTon.dummyAccesstoken = myAccessToken;
				
				System.out.println("Inseid the success="+ loginResult.getAccessToken().getToken());
				System.out.println("Inseid the success="+ loginResult.getAccessToken().getUserId());
				System.out.println("Inseid the success="+ loginResult.getRecentlyGrantedPermissions()); 
				
				GraphRequest request = GraphRequest.newMeRequest(myAccessToken,
						new GraphRequest.GraphJSONObjectCallback() {
					@Override
					public void onCompleted(JSONObject object,	GraphResponse response) {
						System.out.println(" GraphRequesto bject= " + object);
						System.out.println(" GraphRequest response =" + response);

						try {
							if( object.has("email"))
							{
								currentUserEmailId = object.getString("email");
								System.out.println("@@@@@@@@@@@@email111 " + currentUserEmailId);
							}
							new GetExtendedAccessToken().execute();
						 
						} catch (JSONException e) {

							e.printStackTrace();
						}

					}

				});
				
				request.executeAsync();
				 
				//Graph request to get the user profile data like user email, name etc
				GraphRequest.newMyFriendsRequest(myAccessToken, new GraphJSONArrayCallback() {

					@Override
					public void onCompleted(JSONArray objects, GraphResponse response) {

						System.out.println("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$="+response);
					}
				}).executeAsync();
				 
			}

			@Override
			public void onError(FacebookException error) {
				
				error.printStackTrace();
			}

			@Override
			public void onCancel() {
			 
				System.out.println("----- onCancel ");
			}
		});
		 
	}

	@Override
	protected void onResume() {

		super.onResume();

		System.out.println("Insdie tht ONRRRRESSSUME");

		//FB profile tracker to get the user current and old profile data
		profileTracker = new ProfileTracker() {
			@Override
			protected void onCurrentProfileChanged(Profile oldProfile, Profile currentProfile) {

				if(profileTracker.isTracking())

				{
					System.out.println("currentProfilgetName =-------------------------------------"
							+ currentProfile.getName());
					System.out.println("getProfilePictureUri ="
							+ currentProfile.getProfilePictureUri(
									240, 260));
					System.out.println("oldProfile =" + oldProfile);

					profile = currentProfile;

				 
				}else
				{
					System.out.println("TRACKER STAOPED");
				}
			}
		};



	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		callbackManager.onActivityResult(requestCode, resultCode, data);

	}

	@Override
	public void onDestroy() {
		super.onDestroy();

		//Once the profile tracking is done just finish the tracker 
		if (profileTracker.isTracking()) {
			profileTracker.stopTracking();
		}
	}

	//AsyncTask class to get the extended access token from current access token
	public class GetExtendedAccessToken extends AsyncTask<Void, Void, String> {

		@Override
		protected String doInBackground(Void... params) {

			String tokenURL = " https://graph.facebook.com/oauth/access_token?client_id="
					+ getResources().getString(R.string.app_id)
					+ "&client_secret="
					+getResources().getString(R.string.app_secret)
					+ "&grant_type=fb_exchange_token&fb_exchange_token="
					+ myAccessToken.getToken();// CURRENT_ACCESS_TOKEN"

			String dummtoken = Utilsss.getJSONString(tokenURL);

			System.out.println("EXXXXXXXXXXTEDED ACCESSTOKEN= "+dummtoken);

			dummtoken = dummtoken.substring(dummtoken.indexOf("="),dummtoken.indexOf("&"));

			extendedAccessToken = dummtoken.replace("=", "");

			System.out.println("DDSSDDSASDDSF= "+extendedAccessToken);


			return extendedAccessToken;
		}

		@Override
		protected void onPostExecute(String result) {
			
			super.onPostExecute(result);

			System.out.println("INSIDE THE PPOST EXECITE");
			//+++++++++++++++++++++++++++++++++++++++

			ModelUserDatas modelUserDatas = new ModelUserDatas();

			if(extendedAccessToken!=null)
			{
				modelUserDatas.setUserAcessToken(extendedAccessToken);

				System.out.println("ExTEDDD TOKEN STORED = "+extendedAccessToken);
			}else
			{
				System.out.println("ExTEDDD TOKEN NOT STORED = "+extendedAccessToken);

				modelUserDatas.setUserAcessToken(myAccessToken.getToken());
			}
			modelUserDatas.setUserid(myAccessToken.getUserId());
			modelUserDatas.setUsername(profile.getName());
			modelUserDatas.setUserimage(""+ profile.getProfilePictureUri(240, 260));
			modelUserDatas.setUserEmail(currentUserEmailId);

			MainSingleTon.username    = profile.getName();
			MainSingleTon.userimage   = ""+profile.getProfilePictureUri(240, 260);
			MainSingleTon.accesstoken = myAccessToken.getToken();
			MainSingleTon.userid      = myAccessToken.getUserId();
			

			fBoardLocalData.addNewUserAccount(modelUserDatas);
			MainSingleTon.userdetails.put(myAccessToken.getUserId(), modelUserDatas);
			MainSingleTon.useridlist.add(myAccessToken.getUserId());

			SharedPreferences lifesharedpref=getSharedPreferences("FacebookBoard", Context.MODE_PRIVATE);
			SharedPreferences.Editor editor=lifesharedpref.edit();
			editor.putString("userid", myAccessToken.getUserId());
			editor.commit();
			
			if (profileTracker.isTracking()) {
				profileTracker.stopTracking();
				System.out.println("I profileTrackerFFFFFFFFFFFFFFFFFFU");
			}
			 
			//once done with first user just logged out 
			 LoginManager loginManager = LoginManager.getInstance();
			 loginManager.logOut();
			 System.out.println("I FFFFFFFFFFFFFFFFFFULoggedout");
			 
			Intent intent = new Intent(getApplicationContext(),	MainActivity.class);
			startActivity(intent);// start intent to move for home screen
			finish();
			System.out.println("COMIITEEDD STORED  =-----------------------------------");
		}
	}



}
