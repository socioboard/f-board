package com.socioboard.f_board_pro;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
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
import com.socioboard.f_board_pro.database.util.F_Board_LocalData;
import com.socioboard.f_board_pro.database.util.MainSingleTon;
import com.socioboard.f_board_pro.models.SchPostModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class SharlinksBroadcastReceiver extends BroadcastReceiver {

	String userFBiD =null;
	String userFBaccesToken = null;
	String type = null; 
	F_Board_LocalData database;

	int getResponseCode;
	
	@Override
	public void onReceive(Context context, Intent intent) {

		database = new F_Board_LocalData(context);
		getResponseCode = intent.getIntExtra("respcode", 404);

		System.out.println("++++++++++++++++++++++++++++++++++  Share link Broadcast Reciever  +++++++++++++++++++ getResponseCode"
				+ getResponseCode);

		SchPostModel schTweetModel = database.getShareScheduler(""+getResponseCode);

		if (schTweetModel != null) {

			myprint(schTweetModel);

			// post this post

			postSharePostWall(schTweetModel, database.getUserData(schTweetModel.getUserID()).getUserAcessToken());

			Intent intent1 = new Intent(context, SplashActivity.class);

			intent1.setAction(Intent.ACTION_MAIN);

			intent1.addCategory(Intent.CATEGORY_LAUNCHER);

			PendingIntent pIntent = PendingIntent.getActivity(context, 0,
					intent1, 0);

			NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
					context);

		/*	mBuilder.setLargeIcon(BitmapFactory.decodeResource(
					context.getResources(), R.drawable.ic_launcher));
*/
			mBuilder.setSmallIcon(R.drawable.ic_launcher);

			mBuilder.setAutoCancel(true);

			mBuilder.setTicker("Shareagon Link started!!");

			mBuilder.setContentIntent(pIntent);

			mBuilder.setContentTitle("Shareagon link started");

			mBuilder.setStyle(new NotificationCompat.BigPictureStyle().bigPicture(BitmapFactory.decodeFile(schTweetModel.getFeedImagePath())));

			Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

			mBuilder.setSound(alarmSound);

			NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);

			mNotificationManager.notify(schTweetModel.getFeedId(), mBuilder.build());

		} else {

			myprint("NO DATA FOUND IN TABLE FROM THAT RESPONSE CODE");

			//getSharedPrefData(context);
		}

	}

	public void myprint(Object msg) {

		System.out.println(msg.toString());

	}

	private void postSharePostWall(final SchPostModel schPostModel, final String MYAccessToken) {

		try {

			Thread thread = new Thread(new Runnable() {

				@Override
				public void run() {

					try {
						JSONObject json = new JSONObject(schPostModel.getFeedText());

						JSONArray links_array = json.optJSONArray("sharelinks");

						for (int i = 0; i < links_array.length(); i++) 
						{
							int sleepInMiliseconds = (60/schPostModel.getInterval())*1000;

							String dumm = links_array.getString(i);

							System.out.println("dummmmmmmmm"+dumm);

							//shareAsys(dumm, MYAccessToken);
							
							new RunGraphRequest().execute(dumm, MYAccessToken);

							try {

								Thread.sleep(sleepInMiliseconds);
								System.out.println("DELETED="+dumm);


							} catch (InterruptedException e) {

								e.printStackTrace();

							}

							System.out.println("&&&&&&&& = get next link= "+links_array.getString(i));

						}

						database.deleteThisSharePost(getResponseCode);
					} catch (JSONException e) {

						e.printStackTrace();
					}

				}
			});

			thread.start();

		} catch (Exception e) {

			e.printStackTrace();
		}

	}
	
	public class RunGraphRequest extends AsyncTask<String, Void, String>
	{

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			
			System.out.println("started running888888888888888");
		}
		
		@Override
		protected String doInBackground(String... params1) {
			
			String postLink= params1[0];
			String MYAccessToken =params1[1];


			System.out.println(postLink+":***********************");

			System.out.println("myacess tokekn "+MYAccessToken);

			Bundle params = new Bundle();

			if(postLink.isEmpty())
			{

				/**Do nothing becz user just want to post text not an image*/

			}else
			{

				params.putString("link", postLink); // image to post	
			}
			params.putString(AccessToken.ACCESS_TOKEN_KEY, MYAccessToken);

			System.out.println(" MainSingleTon.dummyAccesstoken= "+MainSingleTon.dummyAccesstoken);

			new GraphRequest(MainSingleTon.dummyAccesstoken, "me/feed", params,

					HttpMethod.POST, new GraphRequest.Callback() {

				@Override
				public void onCompleted(GraphResponse response) {

					System.out.println("Scheduled response="+response.getJSONObject());

					if(response.getJSONObject()!=null)
					{
						if(response.getJSONObject().has("id"))
						{
							System.out.println("sucess");
						}
					}

				}

			}).executeAndWait();

			System.out.println(postLink+":*******AFter Asyssss***************");
		
			return null;
		}
		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			
			System.out.println("HEYYYYYYYYYYYYYYYYY=");
		}
		
	}

	public void shareAsys(String postLink, String MYAccessToken)
	{}


}
