package com.socioboard.f_board_pro.service_classes;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.socioboard.f_board_pro.R;
import com.socioboard.f_board_pro.SplashActivity;
import com.socioboard.f_board_pro.database.util.F_Board_LocalData;
import com.socioboard.f_board_pro.database.util.MainSingleTon;
import com.socioboard.f_board_pro.models.HomeFeedModel;
import com.socioboard.f_board_pro.models.SchPostModel;

public class PageShareagonBroadcastReceiver extends BroadcastReceiver {

	String userFBiD = null;
	String userFBaccesToken = null;
	String type = null;
	F_Board_LocalData database;
	JSONArray postlinksArray = null, userlist_array = null;
	int getResponseCode;
	ArrayList<HomeFeedModel> arrlist = new ArrayList<HomeFeedModel>();
	SharedPreferences  sharedPreferences;
	Context context;

	@Override
	public void onReceive(Context context, Intent intent) {

		database = new F_Board_LocalData(context);

		getResponseCode = intent.getIntExtra("respcode", 404);
		sharedPreferences	 = context.getSharedPreferences("FacebookBoardShareagon",
					Context.MODE_PRIVATE);
		this.context = context;

		System.out.println("++++++++++++++++++++++++++++++++++  Share link Broadcast Reciever  +++++++++++++++++++ getResponseCode"
				+ getResponseCode);

		SchPostModel schTweetModel = database.getPageShareagon(""
				+ getResponseCode);

		if (schTweetModel != null) {

			myprint(schTweetModel);

			// post this post
		    // postSharePostWall(schTweetModel);
			// postSharePostWall(schTweetModel,
			// database.getUserData(schTweetModel.getUserID()).getUserAcessToken());

			Intent intentService = new Intent(context, ShareagonpageServiceClass.class);  
			
			intentService.putExtra("shareagonService", getResponseCode);
			
			context.startService(intentService);
			
			sharedPreferences.edit().putBoolean("isShareagonPageStarted", true).commit();

			Intent intent1 = new Intent(context, SplashActivity.class);

			intent1.setAction(Intent.ACTION_MAIN);

			intent1.addCategory(Intent.CATEGORY_LAUNCHER);

			PendingIntent pIntent = PendingIntent.getActivity(context, 0,
					intent1, 0);

			NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
					context);

			mBuilder.setSmallIcon(R.drawable.ic_launcher);

			mBuilder.setAutoCancel(true);

			mBuilder.setTicker("Page Shareagon started!!");

			mBuilder.setContentIntent(pIntent);

			mBuilder.setContentTitle("Page Shareagon started");

			mBuilder.setStyle(new NotificationCompat.BigPictureStyle()
			.bigPicture(BitmapFactory.decodeFile(schTweetModel
					.getFeedImagePath())));

			Uri alarmSound = RingtoneManager
					.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

			mBuilder.setSound(alarmSound);

			NotificationManager mNotificationManager = (NotificationManager) context
					.getSystemService(context.NOTIFICATION_SERVICE);

			mNotificationManager.notify(schTweetModel.getFeedId(),
					mBuilder.build());

		} else {

			myprint("NO DATA FOUND IN TABLE FROM THAT RESPONSE CODE");

		}

	}

	public void myprint(Object msg) {

		System.out.println(msg.toString());

	}


	private void postSharePostWall(final SchPostModel schTweetModel) {

		new Thread(new Runnable() {

			@Override
			public void run() {

				try {

					JSONObject json = new JSONObject(schTweetModel.getFeedText());

					postlinksArray = json.optJSONArray("sharelinks");
					userlist_array = json.optJSONArray("userlist");
					for (int i = 0; i < postlinksArray.length(); i++) {

						long sleepInMiliseconds = (60 / schTweetModel.getFeedtime()) * 1000;

						try {

							Thread.sleep(sleepInMiliseconds);

						} catch (InterruptedException e) {

							e.printStackTrace();

						}
						
						System.out.println("userlist_array===="+userlist_array.length());
						
						for (int j = 0; j < userlist_array.length(); j++) {
							
							String accesstokn = database.getUserData(userlist_array.getString(j)).getUserAcessToken();
							
							new RunGraphRequest().execute(postlinksArray.get(i).toString(), accesstokn,
									schTweetModel.getInterval() + "");
						}

					}

				} catch (JSONException e) {
					e.printStackTrace();
				}

			}
		}).start();

	}

	public class RunGraphRequest extends AsyncTask<String, Void, String> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

			System.out.println("started running888888888888888");
		}

		@Override
		protected String doInBackground(String... params1) {

			String postLink = params1[0];
			String MYAccessToken = params1[1];
			int postInterval = Integer.parseInt(params1[2]);

			Bundle params = new Bundle();

			if (postLink.isEmpty()) {

				/** Do nothing becz user just want to post text not an image */

			} else {

				params.putString("link", postLink); // image to post
			}
			params.putString(AccessToken.ACCESS_TOKEN_KEY, MYAccessToken);

			new GraphRequest(MainSingleTon.dummyAccesstoken, "me/feed", params,

					HttpMethod.POST, new GraphRequest.Callback() {

				@Override
				public void onCompleted(GraphResponse response) {

					System.out.println("Scheduled response="
							+ response.getJSONObject());

					if (response.getJSONObject() != null) {
						if (response.getJSONObject().has("id")) {
							System.out.println("sucess");
						}
					}

				}

			}).executeAndWait();

			 
			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);

		}

	}

}
