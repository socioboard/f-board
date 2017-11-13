package com.socioboard.f_board_pro.service_classes;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;

import com.facebook.AccessToken;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.socioboard.f_board_pro.database.util.F_Board_LocalData;
import com.socioboard.f_board_pro.database.util.JSONParseraa;
import com.socioboard.f_board_pro.database.util.MainSingleTon;
import com.socioboard.f_board_pro.models.HomeFeedModel;
import com.socioboard.f_board_pro.models.SchPostModel;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class GroupsharegonService extends Service {
	int getResponseCode;
	int complted_count;
	F_Board_LocalData database;
	JSONArray postlinksArray = null, userlist_array = null,
			group_id_array = null;

	SharedPreferences sharedPreferences;

	ArrayList<HomeFeedModel> arrlist = new ArrayList<HomeFeedModel>();

	ArrayList<SchPostModel> schFeedArrlist = new ArrayList<SchPostModel>();

	@Override
	public IBinder onBind(Intent intent) {

		return null;

	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {

		FacebookSdk.sdkInitialize(getApplicationContext());

		System.out.println("start command ");

		sharedPreferences = getSharedPreferences("FacebookBoardShareagongrp",
				Context.MODE_PRIVATE);

		complted_count = sharedPreferences.getInt("completed_links", 0);

		getResponseCode = intent.getIntExtra("shareagonService", 404);

		database = new F_Board_LocalData(getApplicationContext());

		SchPostModel schTweetModel = database.getGroupShareagon(""
				+ getResponseCode);

		if (schTweetModel != null) {

			postSharePostWall(schTweetModel);

		} else {

		}
		return Service.START_REDELIVER_INTENT;

	}

	public static String getFormattedDateFromTimestamp(long timestampInMilliSeconds)
	{
		Date date = new Date();
		date.setTime(timestampInMilliSeconds);
		String formattedDate=new SimpleDateFormat("MMM d, yyyy HH:mm").format(date);
		return formattedDate;

	}

	private void postSharePostWall(final SchPostModel schTweetModel) {

		new Thread(new Runnable() {

			@Override
			public void run() {

				try {

					JSONObject json = new JSONObject(schTweetModel
							.getFeedText());

					postlinksArray = json.optJSONArray("sharelinks");
					userlist_array = json.optJSONArray("userlist");
					group_id_array = json.optJSONArray("groupid");
					int share_interval_baby = schTweetModel.getInterval();

					System.out.println("Given interval="
							+ schTweetModel.getInterval());

					share_interval_baby = share_interval_baby * 60;

					System.out.println("share_interval_baby="
							+ share_interval_baby);

					for (int i = 0; i < postlinksArray.length(); i++) {

						long sleepInMiliseconds = (share_interval_baby / 1) * 1000;


						if (sharedPreferences.getBoolean(
								"isShareagonGroupRunning", false)) {
							System.out.println("running");
						} else {
							System.out.println("Breakedddd");

							stopSelf();

							GroupsharegonService.this.stopSelf();
							GroupsharegonService.this.onDestroy();

							sharedPreferences
									.edit()
									.putBoolean("isShareagonGroupRunning", false)
									.commit();
							sharedPreferences.edit().clear().commit();
							database.deleteThisShareGroup(schTweetModel
									.getFeedId());
							break;
						}

						if (complted_count < postlinksArray.length()) {
							complted_count++;
							System.out.println("increaseddd=" + complted_count);
							sharedPreferences.edit()
									.putInt("completed_links", complted_count)
									.commit();
						}



						for (int j = 0; j < userlist_array.length(); j++) {

							for (int k = 0; k < group_id_array.length(); k++) {
								String accesstokn = database.getUserData(userlist_array.getString(j)).getUserAcessToken();
								String group_id = group_id_array.getString(k);

							/*	new RunGraphRequest().execute(postlinksArray
										.get(i).toString(), accesstokn,
										schTweetModel.getInterval() + "",
										group_id);*/


								new PostRequest().execute(postlinksArray
												.get(i).toString(), accesstokn,
										schTweetModel.getInterval() + "",
										group_id);

								// wait for 15 second and post to each group
								Thread.sleep(15000);
							}

						}




						if (complted_count == postlinksArray.length()) {
							GroupsharegonService.this.stopSelf();
							GroupsharegonService.this.onDestroy();

							database.deleteThisShareGroup(schTweetModel
									.getFeedId());

							schFeedArrlist = database.getAllSchedulledGroupShareagon();

							System.out.println("inside testtttttttttttttttttttttttttt");

							if(schFeedArrlist.size()>0)
							{
								sharedPreferences.edit()
										.putBoolean("isShareagonGroupRunning", true)
										.commit();

								sharedPreferences.edit()
										.putInt("completed_links", 0)
										.commit();

								sharedPreferences.edit()
										.putInt("total_share_links", schFeedArrlist.get(schFeedArrlist.size()-1).getTotal_count())
										.commit();

								sharedPreferences.edit().putInt("total_pages_select",MainSingleTon.pageShareagonList.size()).commit();

								sharedPreferences.edit().putLong("startTime", Long.parseLong(getFormattedDateFromTimestamp(schFeedArrlist.get(0).getFeedtime()))).commit();


							}else
							{
								sharedPreferences.edit()
										.putBoolean("isShareagonGroupRunning", false)
										.commit();
								System.out.println("inside falseeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeee");
							}
							break;
						}


						try {

							Thread.sleep(sleepInMiliseconds);

							System.out.println("WAITING----------------------");

						} catch (InterruptedException e) {

							e.printStackTrace();

						}
					}

				} catch (JSONException e) {
					e.printStackTrace();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		}).start();

	}

	public class RunGraphRequest extends AsyncTask<String, Void, String> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

		}

		@Override
		protected String doInBackground(String... params1) {

			String postLink = params1[0];
			String MYAccessToken = params1[1];
			int postInterval = Integer.parseInt(params1[2]);

			String groupid = params1[3];

			System.out.println("groupid " + groupid);
			System.out.println("myacess tokekn " + MYAccessToken);
			System.out.println("postLink " + postLink);

			Bundle params = new Bundle();

			if (postLink.isEmpty()) {

				/** Do nothing becz user just want to post text not an image */

			} else {

				params.putString("link", postLink); // image to post
			}
			params.putString(AccessToken.ACCESS_TOKEN_KEY, MYAccessToken);

			new GraphRequest(MainSingleTon.dummyAccesstoken, "/" + groupid
					+ "/feed", params,

			HttpMethod.POST, new GraphRequest.Callback() {

				@Override
				public void onCompleted(GraphResponse response) {

					System.out.println("response>>>>>>>>" + response);
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

	public class PostRequest extends AsyncTask<String, Void, JSONObject> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

		}

		@Override
		protected JSONObject doInBackground(String... params) {

			String postLink = params[0];
			String MYAccessToken = params[1];
			int postInterval = Integer.parseInt(params[2]);

			String groupid = params[3];

			
			
			JSONParseraa jsonParser = new JSONParseraa();
			JSONObject jsonObject = null;
			
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			nameValuePairs.add(new BasicNameValuePair("link", postLink));
		
			

			jsonObject = jsonParser.getJSONFromUrlByPost("https://graph.facebook.com/"+groupid+"/feed?access_token="+MYAccessToken,nameValuePairs);

			return jsonObject;
		}

		@Override
		protected void onPostExecute(JSONObject result) {

		}

	}

}
