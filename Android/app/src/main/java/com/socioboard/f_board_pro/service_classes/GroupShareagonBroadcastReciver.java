package com.socioboard.f_board_pro.service_classes;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;

import com.socioboard.f_board_pro.R;
import com.socioboard.f_board_pro.SplashActivity;
import com.socioboard.f_board_pro.database.util.F_Board_LocalData;
import com.socioboard.f_board_pro.models.HomeFeedModel;
import com.socioboard.f_board_pro.models.SchPostModel;

import org.json.JSONArray;

import java.util.ArrayList;

public class GroupShareagonBroadcastReciver extends BroadcastReceiver {

	String userFBiD = null;
	String userFBaccesToken = null;
	String type = null;
	F_Board_LocalData database;
	JSONArray postlinksArray = null, userlist_array = null;
	int getResponseCode;
	ArrayList<HomeFeedModel> arrlist = new ArrayList<HomeFeedModel>();
	SharedPreferences sharedPreferences;
	Context context;

	@Override
	public void onReceive(Context context, Intent intent) {


		database = new F_Board_LocalData(context);

		getResponseCode = intent.getIntExtra("respcode", 404);
		sharedPreferences = context.getSharedPreferences(
				"FacebookBoardShareagongrp", Context.MODE_PRIVATE);
		this.context = context;

		System.out
				.println("++++++++++++++++++++++++++++++++++  Share link Broadcast Reciever  +++++++++++++++++++ getResponseCode"
						+ getResponseCode);

		SchPostModel schTweetModel = database.getGroupShareagon(""
				+ getResponseCode);

		if (schTweetModel != null) {

			myprint(schTweetModel);

			// post this post
			// postSharePostWall(schTweetModel);
			// postSharePostWall(schTweetModel,
			// database.getUserData(schTweetModel.getUserID()).getUserAcessToken());

			Intent intentService = new Intent(context,
					GroupsharegonService.class);

			intentService.putExtra("shareagonService", getResponseCode);

			context.startService(intentService);

			sharedPreferences.edit().putBoolean("isShareagonGroupStarted", true)
					.commit();

			Intent intent1 = new Intent(context, SplashActivity.class);

			intent1.setAction(Intent.ACTION_MAIN);

			intent1.addCategory(Intent.CATEGORY_LAUNCHER);

			PendingIntent pIntent = PendingIntent.getActivity(context, 0,
					intent1, 0);

			NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
					context);

			mBuilder.setSmallIcon(R.drawable.ic_launcher);

			mBuilder.setAutoCancel(true);

			mBuilder.setTicker("Shareagon Group started!!");

			mBuilder.setContentIntent(pIntent);

			mBuilder.setContentTitle("Shareagon Group started");

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

}
