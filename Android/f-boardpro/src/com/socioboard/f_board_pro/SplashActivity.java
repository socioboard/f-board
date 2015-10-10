package com.socioboard.f_board_pro;

import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.flurry.android.FlurryAgent;
import com.socioboard.f_board_pro.database.util.F_Board_LocalData;
import com.socioboard.f_board_pro.database.util.MainSingleTon;
import com.socioboard.f_board_pro.database.util.ModelUserDatas;

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

		FlurryAgent.init(this, "XXXXXXXXXXXXXXXX");

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
		
	
		System.out.println(MainSingleTon.userdetails.size()+"setContentView time------------");

		fBoardLocalData = new F_Board_LocalData(getApplicationContext()); //Initialize the database

		fBoardLocalData.CreateTable(); //Create table for users account

		//user FB_id list

		fBoardLocalData.getAllUsersData(); // Take already signed users data from database

		System.out.println("MainSingleTon.userdetails.size() ="+MainSingleTon.userdetails.size()+"DATABASE_CONTENT= "+MainSingleTon.userdetails);


		if (MainSingleTon.userdetails.size() == 0) //check is there any users available in database
		{ 
			Intent intent = new Intent(SplashActivity.this, WelcomeActivity.class);

			startActivity(intent); // start welcome activity to sign_In  Facebook;

			finish();

		} 
		else
		{
			SharedPreferences lifesharedpref = getSharedPreferences("FacebookBoard", Context.MODE_PRIVATE);

			MainSingleTon.userid = lifesharedpref.getString("userid", null);

			if(MainSingleTon.userid!=null) 
			{
				ModelUserDatas model     =MainSingleTon.userdetails.get(MainSingleTon.userid);
				MainSingleTon.username   =model.getUsername();
				MainSingleTon.userimage  =model.getUserimage();
				MainSingleTon.accesstoken=model.getUserAcessToken();

				Intent in=new Intent(SplashActivity.this, MainActivity.class);
				startActivity(in);
				SplashActivity.this.finish();

			}else
			{
				Map.Entry<String, ModelUserDatas> entry = MainSingleTon.userdetails.entrySet().iterator().next();
				MainSingleTon.userid     =entry.getKey();
				ModelUserDatas value     =entry.getValue();
				MainSingleTon.username   =value.getUsername();
				MainSingleTon.userimage  =value.getUserimage();
				MainSingleTon.accesstoken=value.getUserAcessToken();

				Intent in=new Intent(SplashActivity.this,MainActivity.class);
				startActivity(in);
				SplashActivity.this.finish();
			}


		}

	}
	@Override
	protected void onStart() {
		super.onStart();
		FlurryAgent.onStartSession(getApplicationContext(), "XXXXXXXXXXXXXXXXXXXXXX");
	}
	@Override
	protected void onStop() {
		super.onStop();
		FlurryAgent.onEndSession(this);
	}

}
