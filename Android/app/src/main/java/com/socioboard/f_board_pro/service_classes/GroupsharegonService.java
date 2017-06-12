package com.socioboard.f_board_pro.service_classes;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;

public class GroupsharegonService extends Service {
	int getResponseCode;
	int complted_count;
	F_Board_LocalData database;
	JSONArray postlinksArray = null, userlist_array = null,
			group_id_array = null;

	SharedPreferences sharedPreferences;

	ArrayList<HomeFeedModel> arrlist = new ArrayList<HomeFeedModel>();

	@Override
	public IBinder onBind(Intent intent) {

		return null;

	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {

		FacebookSdk.sdkInitialize(getApplicationContext());

		System.out.println("start command ");

		sharedPreferences = getSharedPreferences("FacebookBoardShareagon",
				Context.MODE_PRIVATE);

		complted_count = sharedPreferences.getInt("completed_links", 0);

		getResponseCode = intent.getIntExtra("shareagonService", 404);

		database = new F_Board_LocalData(getApplicationContext());

		SchPostModel schTweetModel = database.getPageShareagon(""
				+ getResponseCode);

		if (schTweetModel != null) {

			postSharePostWall(schTweetModel);

		} else {

		}
		return Service.START_REDELIVER_INTENT;
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
								"isShareagonPageRunning", false)) {
							System.out.println("running");
						} else {
							System.out.println("Breakedddd");

							stopSelf();

							GroupsharegonService.this.stopSelf();
							GroupsharegonService.this.onDestroy();

							sharedPreferences
									.edit()
									.putBoolean("isShareagonPageRunning", false)
									.commit();
							sharedPreferences.edit().clear().commit();
							database.deleteThisSharePages(schTweetModel
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
						if (complted_count == postlinksArray.length()) {
							GroupsharegonService.this.stopSelf();
							GroupsharegonService.this.onDestroy();
							sharedPreferences.edit()
									.putBoolean("isShareagonPageRunning", false)
									.commit();
							database.deleteThisSharePages(schTweetModel
									.getFeedId());
							break;
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
