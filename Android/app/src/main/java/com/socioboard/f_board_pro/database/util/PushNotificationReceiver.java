package com.socioboard.f_board_pro.database.util;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;

import com.socioboard.f_board_pro.R;
import com.socioboard.f_board_pro.SplashActivity;

public class PushNotificationReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {

		try {

			JSONObject json = new JSONObject(intent.getExtras().getString("com.parse.Data"));

			System.out.println("push Response " + json.toString());

			NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context);

			String msgType = json.getString("type");

			System.out.println(json.toString());

			Intent intent1 = new Intent(context, SplashActivity.class);
			
			mBuilder.setTicker(json.getString("friendName") + " Beat You");

			mBuilder.setContentTitle(json.getString("friendName"));

			mBuilder.setContentText(" Beat you on Level "
					+ json.getString("level"));

			mBuilder.setSubText("With score : " + json.getString("score"));

		 
			SharedPreferences preferences = context.getSharedPreferences(
					"notificationIds", 0);

			int notificationId = preferences.getInt("id", 1);

			PendingIntent pIntent = PendingIntent.getActivity(context, 0,
					intent1, 0);

			mBuilder.setSmallIcon(R.drawable.ic_launcher);

			mBuilder.setContentIntent(pIntent);

			Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
			mBuilder.setSound(alarmSound);
			mBuilder.setAutoCancel(true);
			NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

			mNotificationManager.notify(notificationId, mBuilder.build());

			Editor editor;
			editor = preferences.edit();
			editor.putInt("id", ++notificationId);
			editor.commit();

		} catch (JSONException jsonException) {

			System.out.println("Recieved jsonException = " + jsonException);
		}

	}
}
