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
import com.socioboard.f_board_pro.fragments.ShareagonPage;
import com.socioboard.f_board_pro.models.HomeFeedModel;
import com.socioboard.f_board_pro.models.SchPostModel;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ShareagonpageServiceClass extends Service {
	int userIDFromShareagonPageAdapter;
	int getResponseCode;
	int complted_count;
	F_Board_LocalData database;
	JSONArray postlinksArray = null, userlist_array = null;

	ShareagonPage shareagonPage;
	ArrayList feeidt = new ArrayList<>();

	SharedPreferences sharedPreferences;

	ArrayList<HomeFeedModel> arrlist = new ArrayList<HomeFeedModel>();

	ArrayList feedid = new ArrayList();

	ArrayList<SchPostModel> schFeedArrlist = new ArrayList<SchPostModel>();

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

		feedid = intent.getStringArrayListExtra("feedida");//get feedId from PageShereagonBroadcastReciever

		database = new F_Board_LocalData(getApplicationContext());

		SchPostModel schTweetModel = database.getPageShareagon("" + getResponseCode);



		if (schTweetModel != null) {

			postSharePostWall(schTweetModel);

		} else {

		}


		return Service.START_REDELIVER_INTENT;
	}


	private void postSharePostWall(final SchPostModel schTweetModel)
	{

		new Thread(new Runnable() {

			@Override
			public void run() {

				try {

					JSONObject json = new JSONObject(schTweetModel
							.getFeedText());

					postlinksArray = json.optJSONArray("sharelinks");

					userlist_array = json.optJSONArray("userlist");

					int share_interval_baby = schTweetModel.getInterval();

					System.out.println("Given interval="
							+ schTweetModel.getInterval());

					share_interval_baby = share_interval_baby * 60;

					System.out.println("share_interval_baby="
							+ share_interval_baby);

					int i;
					for ( i = 0; i < postlinksArray.length(); i++) {

						long sleepInMiliseconds = (share_interval_baby / 1) * 1000;

						if (sharedPreferences.getBoolean(
								"isShareagonPageRunning", false))
						{
							System.out.println("running");
						} else
						{
							System.out.println("Breakedddd");

							stopSelf();

							ShareagonpageServiceClass.this.stopSelf();
							ShareagonpageServiceClass.this.onDestroy();

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




						for (int j = 0; j < userlist_array.length(); j++) {
							String accesstokn = database.getUserData(userlist_array.getString(j)).getUserAcessToken();

//							  new
//							  RunGraphRequest().execute(postlinksArray.get(i)
//							  .toString(), accesstokn,
//							  schTweetModel.getInterval() + "");


							new PagePostRequest().execute(postlinksArray.get(i)
											.toString(), accesstokn,
									schTweetModel.getInterval() + "");
						}




						if (complted_count == postlinksArray.length()) {
							ShareagonpageServiceClass.this.stopSelf();
							ShareagonpageServiceClass.this.onDestroy();
//							database.deleteThisSharePages(schTweetModel
//									.getFeedId());

							for(int k=0;k<feedid.size();k++)//this is used to delete feedId when post is completed
							{
								database.deleteThisSharePages((Integer) feedid.get(k));

								System.out.println("delete feedId = "+feedid.get(k).toString()+" is successssssssssssssssssssssssssssssssss");
							}

							schFeedArrlist = database.getAllSchedulledPageShareagon();//this is used to cheack weather more data is present in database or not

							if(schFeedArrlist.size()>0)//if data presend in database then isShareagonPageRunning is true else false
							{
								sharedPreferences.edit()
										.putBoolean("isShareagonPageRunning", true)
										.commit();

								sharedPreferences.edit()
										.putInt("completed_links", 0)
										.commit();

								sharedPreferences.edit()
										.putInt("total_share_links", schFeedArrlist.get(schFeedArrlist.size()-1).getTotal_count())
										.commit();

							}else
							{
								sharedPreferences.edit()
										.putBoolean("isShareagonPageRunning", false)
										.commit();
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

					if(i==postlinksArray.length())//if postlinkArray is equal to given post count then stop and destroy service
					{
						ShareagonpageServiceClass.this.stopSelf();
							ShareagonpageServiceClass.this.onDestroy();

							database.deleteThisSharePages(schTweetModel
									.getFeedId());
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

			System.out.println("myacess tokekn " + MYAccessToken);
			System.out.println("postLink " + postLink);

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

	public class PagePostRequest extends AsyncTask<String, Void, JSONObject> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

		}

		@Override
		protected JSONObject doInBackground(String... params) {

			String postLink = params[0];
			String MYAccessToken = params[1];
			int postInterval = Integer.parseInt(params[2]);

			JSONParseraa jsonParser = new JSONParseraa();
			JSONObject jsonObject = null;

			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			nameValuePairs.add(new BasicNameValuePair("link", postLink));

			jsonObject = jsonParser.getJSONFromUrlByPost(
					"https://graph.facebook.com/me/feed?access_token="
							+ MYAccessToken, nameValuePairs);

			return jsonObject;
		}

		@Override
		protected void onPostExecute(JSONObject result) {

			System.out.println(" page post result = " + result);
		}

	}

}
