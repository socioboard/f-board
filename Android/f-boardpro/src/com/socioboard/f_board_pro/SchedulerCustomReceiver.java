package com.socioboard.f_board_pro;


import java.io.ByteArrayOutputStream;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import com.socioboard.f_board_pro.database.util.MainSingleTon;

public class SchedulerCustomReceiver extends BroadcastReceiver 
{
	String imagepath =null, caption=null;
	@Override
	public void onReceive(Context context, Intent intent) 
	{
		SharedPreferences sharedpref=context.getSharedPreferences("SavePhoto", Context.MODE_PRIVATE);
		imagepath =sharedpref.getString("photostring", "");
		caption= sharedpref.getString("caption", "");
		MainSingleTon.accesstoken = sharedpref.getString("accestokednPref", "");


		postImageonWall();

		NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context);
		mBuilder.setSmallIcon(R.drawable.ic_launcher);

		mBuilder.setContentTitle("faceboardpro");

		mBuilder.setContentText("Scheduled successfully!!!");

		Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
		mBuilder.setSound(alarmSound);
		NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);

		// notificationID allows you to update the
		// notification later on.

		mNotificationManager.notify(0, mBuilder.build());	

	}
	private void postImageonWall() {

		byte[] data = null;
		Bundle params = new Bundle();

		if(imagepath.isEmpty())
		{

			/**Do nothing becz user just want to post text not an image*/
		}else
		{
			Bitmap bi = BitmapFactory.decodeFile(imagepath);
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			bi.compress(Bitmap.CompressFormat.JPEG, 100, baos);
			data = baos.toByteArray();
			params.putByteArray("picture", data); // image to post	
		}

		if(caption.isEmpty())
		{
			/**Do nothing becz user just want to post an image not a text*/
		}else
		{
			params.putString("caption", caption); 
		}
		params.putString(AccessToken.ACCESS_TOKEN_KEY, MainSingleTon.accesstoken);

		
			new GraphRequest(null, "me/photos", params,
					HttpMethod.POST, new GraphRequest.Callback() {

				@Override
				public void onCompleted(GraphResponse response) {



				}
			}).executeAsync();
		

	}

}
