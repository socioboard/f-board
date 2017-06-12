package com.socioboard.f_board_pro;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.Signature;
import android.os.Bundle;
import android.os.Handler;
import android.util.Base64;
import android.util.Log;

import com.flurry.android.FlurryAgent;
import com.socioboard.f_board_pro.database.util.F_Board_LocalData;
import com.socioboard.f_board_pro.database.util.MainSingleTon;
import com.socioboard.f_board_pro.database.util.ModelUserDatas;
import com.socioboard.f_board_pro.models.UserProfileDetailsModel;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;

/*Activity to launch application */

public class SplashActivity extends Activity
{
	F_Board_LocalData fBoardLocalData;//Local database to store FB user accesstoken

	SharedPreferences preferences; 

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		FlurryAgent.setLogEnabled(false);
		FlurryAgent.init(this, "D8T57RYRKJPV7K5FRQXM");
		setContentView(R.layout.activity_splash); //initialize the splash screen layout

	 /*	Parse.initialize(this,  getResources().getString(R.string.parse_app_id),  getResources().getString(R.string.parse_client_key));

		ParseInstallation.getCurrentInstallation().saveInBackground();

		ParsePush.subscribeInBackground("", new SaveCallback() {
			  @Override
			  public void done(ParseException e) {
			    if (e == null) {
			      Log.d("com.parse.push", "successfully subscribed to the broadcast channel.");
			    } else {
			      Log.e("com.parse.push", "failed to subscribe for push", e);
			    }

			    System.out.println("REEGISTERED = "+e);
			  }



			});*/

		
		// generating keyhash
				try {
					PackageInfo info = getPackageManager().getPackageInfo("com.socioboard.f_board_pro",PackageManager.GET_SIGNATURES);
					for (Signature signature : info.signatures) {
						MessageDigest md = MessageDigest.getInstance("SHA");
						md.update(signature.toByteArray());
						Log.d("KeyHash:",Base64.encodeToString(md.digest(), Base64.DEFAULT));
						System.out.println("@@@@@@@@"+ Base64.encodeToString(md.digest(), Base64.DEFAULT));
					}
				} catch (NameNotFoundException |NoSuchAlgorithmException e) {
                  e.printStackTrace();
				}
		
		MainSingleTon.checkFirstTimeLogin=true;
	
		System.out.println(MainSingleTon.userdetails.size()+"setContentView time------------");
		fBoardLocalData = new F_Board_LocalData(getApplicationContext()); //Initialize the database
		fBoardLocalData.CreateTable(); //Create table for users account

		//user FB_id list

		ArrayList<HashMap<String ,String>> modelUserDatases=fBoardLocalData.getAllUsersData(); // Take already signed users data from database

		System.out.println("ArrayList<ModelUserDatas>"+modelUserDatases);
		System.out.println("MainSingleTon.userdetails.size() ="+MainSingleTon.userdetails.size()+"DATABASE_CONTENT= "+MainSingleTon.userdetails);


		if (MainSingleTon.userdetails.size() == 0) //check is there any users available in database
		{ 
			Intent intent = new Intent(SplashActivity.this, WelcomeActivity.class);
			startActivity(intent); // start welcome activity to sign_In  Facebook;
			finish();

		} 
		else
		{
			SharedPreferences lifesharedpref = getSharedPreferences(SharedPrefrence.FacebookSharedPrefrence, Context.MODE_PRIVATE);
			//MainSingleTon.userid = lifesharedpref.getString("userid", "");
			SharedPreferenceModel sharedPreferenceModel=new SharedPreferenceModel();
			UserProfileDetailsModel userProfileDetailsModel=new UserProfileDetailsModel();
			ModelUserDatas  modelUserDatas=new ModelUserDatas();
			//local variable for fetch value

			String a="",b="",c="",d="",e="",f="",g="",h="",i="",j="",k="",l="",m="";

			a=lifesharedpref.getString(SharedPrefrence.FacebookUserId, "");
			MainSingleTon.userid=a;
			sharedPreferenceModel.setFacebookUserId(a);
			userProfileDetailsModel.setUserId(a);

			b=lifesharedpref.getString(SharedPrefrence.FacebookUserName,"");
			MainSingleTon.username=b;
			sharedPreferenceModel.setFacebookUserName(b);
			userProfileDetailsModel.setUserName(b);
			//modelUserDatas.setUsername(b);

			c=lifesharedpref.getString(SharedPrefrence.FacebookFirstName,"");
			MainSingleTon.userFirstName=c;
			sharedPreferenceModel.setFacebookFirstName(c);
			userProfileDetailsModel.setFirst_name(c);

			d=lifesharedpref.getString(SharedPrefrence.FacebookShortImagePath,"");
			MainSingleTon.userimage=d;
			sharedPreferenceModel.setFacebookShortImagePath(d);
			userProfileDetailsModel.setUserProfilePic(d);

			e=lifesharedpref.getString(SharedPrefrence.FacebookLargeImagePath,"");
			MainSingleTon.userLargeImagePath=e;
			sharedPreferenceModel.setFacebookLargeImagePath(e);
			userProfileDetailsModel.setUserLorgeProfilePic(e);

			f=lifesharedpref.getString(SharedPrefrence.FacebookEmailId,"");
			MainSingleTon.userEmail=f;
			sharedPreferenceModel.setFacebookEmailId(f);
			userProfileDetailsModel.setUserMail(f);

			g=lifesharedpref.getString(SharedPrefrence.FacebookGender,"");
			MainSingleTon.userGender=g;
			sharedPreferenceModel.setFacebookGender(g);
			userProfileDetailsModel.setUserGender(g);

			h=lifesharedpref.getString(SharedPrefrence.FacebookDOB,"");
			MainSingleTon.userDOB=h;
			sharedPreferenceModel.setFacebookDOB(h);
			userProfileDetailsModel.setUserBirthDate(h);


			i=lifesharedpref.getString(SharedPrefrence.FacebookAccessToken,"");
			MainSingleTon.accesstoken=i;
			sharedPreferenceModel.setFacebookAccessToken(i);
			userProfileDetailsModel.setUser_token(i);

			j=lifesharedpref.getString(SharedPrefrence.FacebookCoverImagePath,"");
			MainSingleTon.userCoverPicUrl=j;
			sharedPreferenceModel.setFacebookCoverImagePath(j);
			userProfileDetailsModel.setUserCoverPic(j);

			k=lifesharedpref.getString(SharedPrefrence.FacebookWorkPlace,"");
			MainSingleTon.userWorkPlace=k;
			sharedPreferenceModel.setFacebookUserWorkLocation(k);
			userProfileDetailsModel.setUserWork(k);

			l=lifesharedpref.getString(SharedPrefrence.FacebookUserLocation,"");
			MainSingleTon.userCurrentLocation=l;
			sharedPreferenceModel.setFacebookUserCurrentLocation(l);
			userProfileDetailsModel.setUserLocation(l);

			m=lifesharedpref.getString(SharedPrefrence.FacebookUserHomeTown,"");
			MainSingleTon.userHomeTown=m;
			sharedPreferenceModel.setFacebookUserHomeTown(m);
			userProfileDetailsModel.setUserHomeTown(m);

			SharedPreferenceModel.sharedPreferenceModelsArrayList.add(sharedPreferenceModel);
			UserProfileDetailsModel.userProfileDetailsModelsArrayList.add(userProfileDetailsModel);

			MainSingleTon.checkFirstTimeLogin=false;

			/*if(MainSingleTon.userid!=null)
			{
				ModelUserDatas model     =MainSingleTon.userdetails.get(MainSingleTon.userid);
				//MainSingleTon.username   =model.getUsername();
			//	MainSingleTon.userimage  =model.getUserimage();
				MainSingleTon.accesstoken=model.getUserAcessToken();
				System.out.println("Facebook Details: "+MainSingleTon.username+" "+MainSingleTon.userimage+" "+MainSingleTon.accesstoken);
				Intent in=new Intent(SplashActivity.this, MainActivity.class);
				startActivity(in);
				SplashActivity.this.finish();

			}else
			{*/
				/*Map.Entry<String, ModelUserDatas> entry = MainSingleTon.userdetails.entrySet().iterator().next();
				MainSingleTon.userid     =entry.getKey();
				ModelUserDatas value     =entry.getValue();
				MainSingleTon.username   =value.getUsername();
				MainSingleTon.userimage  =value.getUserimage();
				MainSingleTon.accesstoken=value.getUserAcessToken();
*/
				new Handler().postDelayed(new Runnable() {
					@Override
					public void run() {
						Intent in=new Intent(SplashActivity.this,MainActivity.class);
						startActivity(in);
						SplashActivity.this.finish();
					}
				},1500);

			}


	//	}

	}
	@Override
	protected void onStart() {
		super.onStart();
		FlurryAgent.onStartSession(getApplicationContext(), "D8T57RYRKJPV7K5FRQXM");
	}
	@Override
	protected void onStop() {
		super.onStop();
		FlurryAgent.onEndSession(this);
	}

}
