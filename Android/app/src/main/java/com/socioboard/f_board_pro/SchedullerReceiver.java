package com.socioboard.f_board_pro;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.socioboard.f_board_pro.database.util.F_Board_LocalData;
import com.socioboard.f_board_pro.database.util.MainSingleTon;
import com.socioboard.f_board_pro.models.SchPostModel;

import java.io.ByteArrayOutputStream;

public class SchedullerReceiver extends BroadcastReceiver {

	String userFBiD =null;
	String userFBaccesToken = null;
	String type = null; 
	int likesperminute=0, totalhours=0, perdaylikescount=0, presentcounter=0;

	@Override
	public void onReceive(Context context, Intent intent) {

		int getResponseCode;

		getResponseCode = intent.getIntExtra("respcode", 404);

		System.out.println("++++++++++++++++++++++++++++++++++  FboardScheduller  +++++++++++++++++++ getResponseCode"
				+ getResponseCode);

		F_Board_LocalData database = new F_Board_LocalData(context);

		SchPostModel schTweetModel = database.getSchedulledTweet(""+getResponseCode);

		if (schTweetModel != null) {

			myprint(schTweetModel);

			postImageonWall(schTweetModel, database.getUserData(schTweetModel.getUserID()).getUserAcessToken());

			Intent intent1 = new Intent(context, SplashActivity.class);

			intent1.setAction(Intent.ACTION_MAIN);

			intent1.addCategory(Intent.CATEGORY_LAUNCHER);

			PendingIntent pIntent = PendingIntent.getActivity(context, 0,
					intent1, 0);

			NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
					context);

			mBuilder.setLargeIcon(BitmapFactory.decodeResource(
					context.getResources(), R.drawable.ic_launcher));

			mBuilder.setSmallIcon(R.drawable.ic_launcher);

			mBuilder.setAutoCancel(true);

			mBuilder.setTicker("Post composed");

			mBuilder.setContentIntent(pIntent);

			mBuilder.setContentTitle("Scheduled Feed composed");

			mBuilder.setContentText("Status:"+schTweetModel.getFeedText());

			mBuilder.setStyle(new NotificationCompat.BigPictureStyle().bigPicture(BitmapFactory.decodeFile(schTweetModel.getFeedImagePath())));

			Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

			mBuilder.setSound(alarmSound);

			NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);

			mNotificationManager.notify(schTweetModel.getFeedId(), mBuilder.build());

			database.deleteThisPost(getResponseCode);

		} else {

			myprint("NO DATA FOUND IN TABLE FROM THAT RESPONSE CODE");


			//getSharedPrefData(context);
		}

	}

	public void myprint(Object msg) {

		System.out.println(msg.toString());

	}

	private void postImageonWall(SchPostModel schTweetModel, String MYAccessToken) {

		byte[] data = null;
		Bundle params = new Bundle();
		boolean isPhotoAvailable=false;

		if(schTweetModel.getFeedImagePath().isEmpty())
		{

			/**Do nothing becz user just want to post text not an image*/
			isPhotoAvailable=false;
		}else
		{
			Bitmap bi = BitmapFactory.decodeFile(schTweetModel.getFeedImagePath());
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			bi.compress(Bitmap.CompressFormat.JPEG, 100, baos);
			data = baos.toByteArray();
			params.putByteArray("picture", data); // image to post	
			isPhotoAvailable=true;
		}

		if(schTweetModel.getFeedText().isEmpty())
		{
			/**Do nothing becz user just want to post an image not a text*/
		}else
		{
			if(isPhotoAvailable)
			{
				params.putString("caption", schTweetModel.getFeedText());
			}else
			{
				params.putString("message", schTweetModel.getFeedText());
			}
		}

		params.putString(AccessToken.ACCESS_TOKEN_KEY, MYAccessToken);


		if(isPhotoAvailable)
		{
			new GraphRequest(MainSingleTon.dummyAccesstoken, "me/photos", params,

					HttpMethod.POST, new GraphRequest.Callback() {

				@Override
				public void onCompleted(GraphResponse response) {


					System.out.println("Scheduled response="+response.getJSONObject());

				}
			}).executeAsync();
			
		}else
		{

			new GraphRequest(MainSingleTon.dummyAccesstoken, "me/feed", params,

					HttpMethod.POST, new GraphRequest.Callback() {

				@Override
				public void onCompleted(GraphResponse response) {

					System.out.println("Scheduled response="+response.getJSONObject());

				}
			}).executeAsync();

		}


	}



}
