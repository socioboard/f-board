package com.socioboard.f_board_pro;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Timer;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.IBinder;
import android.util.Log;

import com.socioboard.f_board_pro.database.util.JSONParseraa;
import com.socioboard.f_board_pro.database.util.MainSingleTon;
import com.socioboard.f_board_pro.database.util.Utilsss;
import com.socioboard.f_board_pro.models.DetermineUserLike;
import com.socioboard.f_board_pro.models.HomeFeedModel;

public class LikeSchedulerReciever extends Service {

	int complted_count;
	String userFBiD = null;
	String userFBaccesToken = null;
	String type = null;
	int likesperminute = 0, totalhours = 0, perdaylikescount = 0;
			
	boolean isServicRunningFFF = false;
	private ArrayList<HomeFeedModel> mListItems;

	private ArrayList<String> AllfeedIds;

	private ArrayList<String> likedFeedIds;

	private ArrayList<String> notlikedIds;

	private ArrayList<HomeFeedModel> oldLikedlistItem;

	boolean isPagesAvailable = false, isFivePagesLoaded = false,
			isFivePagesLoaded1 = false;
	
	String cursor = null, cursor1 = null;
	
	int cursorcount = 0, cursorcount1 = 0;
	
	int randomtime = -3000;

	private ArrayList<HomeFeedModel> oldFeedlist;
	Timer timersa;

	SharedPreferences sharedPreferences;

	@Override
	public IBinder onBind(Intent intent) {

		return null;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		sharedPreferences = getSharedPreferences("FacebookBoardAutoliker",
				Context.MODE_PRIVATE);
		
		complted_count  = sharedPreferences.getInt("completed_likes", 0);
	
		perdaylikescount = sharedPreferences.getInt("totallikerperday", 0);
		
		mListItems = new ArrayList<HomeFeedModel>();

		oldLikedlistItem = new ArrayList<HomeFeedModel>();

		oldFeedlist = new ArrayList<HomeFeedModel>();

		AllfeedIds = new ArrayList<String>();

		likedFeedIds = new ArrayList<String>();

		notlikedIds = new ArrayList<String>();

		System.out
				.println("++++++++++++++++++++++++++++++++++  FboardScheduller  +++++++++++++++++++ getResponseCode");

		getSharedPrefData(getApplicationContext());

		/*
		 * Intent intent123 = new Intent(this, MainActivity.class); final
		 * PendingIntent pendingIntent =
		 * PendingIntent.getActivity(getApplicationContext(), 0, intent123, 0);
		 * 
		 * final Notification notification = new
		 * Notification(R.drawable.ic_launcher, "simulating a download", System
		 * .currentTimeMillis()); notification.flags = notification.flags |
		 * Notification.FLAG_ONGOING_EVENT; notification.contentView = new
		 * RemoteViews(getApplicationContext().getPackageName(),
		 * R.layout.progress_on_notifications_bar); notification.contentIntent =
		 * pendingIntent;
		 * notification.contentView.setImageViewResource(R.id.status_icon,
		 * R.drawable.ic_launcher);
		 * notification.contentView.setTextViewText(R.id.status_text,
		 * "simulation in progress"); //
		 * notification.contentView.setProgressBar(R.id.status_progress, 100, 1,
		 * false);
		 * 
		 * final NotificationManager notificationManager = (NotificationManager)
		 * getApplicationContext().getSystemService(
		 * getApplicationContext().NOTIFICATION_SERVICE);
		 * 
		 * notificationManager.notify(42, notification);
		 * 
		 * timersa= new Timer(); timersa.schedule(new TimerTask() {
		 * 
		 * @Override public void run() {
		 * 
		 * int totalLikesAss=0, penginlikes=0;;
		 * System.out.println("ddrrrrrrddd"); SharedPreferences lisharedpref =
		 * getSharedPreferences("FacebookBoardAutoliker", Context.MODE_PRIVATE);
		 * 
		 * isServicRunningFFF = lisharedpref.getBoolean("isServicRunning",
		 * false);
		 * 
		 * if(isServicRunningFFF) { System.out.println("sssstrue");
		 * 
		 * totalLikesAss =
		 * Integer.parseInt(lisharedpref.getString("totallikerperday", "0"));
		 * 
		 * penginlikes =
		 * Integer.parseInt(lisharedpref.getString("completed_likes", "0"));
		 * 
		 * //notification.contentView.setProgressBar(R.id.status_progress,
		 * totalLikesAss, penginlikes, false);
		 * 
		 * notification.contentView.setTextViewText(R.id.status_text,
		 * penginlikes+" / "+totalLikesAss);
		 * 
		 * notificationManager.notify(42, notification);
		 * 
		 * }else { //service not running notificationManager.cancel(42); }
		 * 
		 * 
		 * } }, 0, 1000);
		 */

		return START_STICKY;
	}

	public void myprint(Object msg) {

		System.out.println(msg.toString());

	}

	public void getSharedPrefData(Context context) {
		SharedPreferences lifesharedpref = context.getSharedPreferences(
				"FacebookBoardAutoliker", Context.MODE_PRIVATE);

		userFBiD = lifesharedpref.getString("likescheduler_id", null);
		userFBaccesToken = lifesharedpref.getString("likeScheluerAccesstoken",
				null);
		likesperminute = lifesharedpref.getInt("likesperminuteInt", 0);
		totalhours     = lifesharedpref.getInt("totalhoursInt", 0);

		perdaylikescount = totalhours * 60 * likesperminute;

		if (userFBiD != null) {
			new GetFeeds().execute(userFBiD);
		}

	}

	public class GetFeeds extends AsyncTask<String, Void, String> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

		@Override
		protected String doInBackground(String... params) {
			String tokenURL = "https://graph.facebook.com/" + userFBiD
					+ "/home?access_token=" + userFBaccesToken;

			System.out.println("HOME FEED url= " + tokenURL);

			JSONParseraa jsonParser = new JSONParseraa();

			JSONObject jsonObject = jsonParser.getJSONFromUrl(tokenURL);

			mListItems.clear();
			try {

				JSONArray jsonArray = jsonObject.getJSONArray("data");

				if (jsonArray.length() != 0) {
					for (int i = 0; i < jsonArray.length(); i++) {
						HomeFeedModel feedModel = new HomeFeedModel();
						DetermineUserLike userLikes = new DetermineUserLike();
						JSONObject jsonObject2 = jsonArray.getJSONObject(i);
						if (jsonObject2.has("id")) {
							feedModel.setFeedId(jsonObject2.getString("id"));
							userLikes.setFeedId(jsonObject2.getString("id"));
						}
						if (jsonObject2.has("likes")) {
							JSONObject jsonObjectlikes = jsonObject2
									.getJSONObject("likes");
							JSONArray jsonArraylikes = jsonObjectlikes
									.getJSONArray("data");
							feedModel.setLikes(jsonArraylikes.length());
							for (int j = 0; j < jsonArraylikes.length(); j++) {
								JSONObject jsonObjectlike = jsonArraylikes
										.getJSONObject(j);
								if (jsonObjectlike.has("id")) {
									String id = jsonObjectlike.getString("id");

									if (id.equalsIgnoreCase(MainSingleTon.userid)) {
										userLikes.setLike(true);
									} else {
										userLikes.setLike(false);
									}
								} else {
								}
							}
						} else {
							feedModel.setLikes(0);
							userLikes.setLike(false);
						}
						if (jsonObject2.has("comments")) {
							JSONObject jsonObjectcomments = jsonObject2
									.getJSONObject("comments");
							JSONArray jsonArraycomments = jsonObjectcomments
									.getJSONArray("data");
							feedModel.setComments(jsonArraycomments.length());
						} else {
							feedModel.setComments(0);
						}
						if (jsonObject2.has("shares")) {
							JSONObject jsonObjectshare = jsonObject2
									.getJSONObject("shares");

							if (jsonObjectshare.has("count")) {
								feedModel.setShares(jsonObjectshare
										.getInt("count"));
							} else {
								feedModel.setShares(0);
							}
						} else {
							feedModel.setShares(0);
						}
						if (jsonObject2.has("type")) {
							type = jsonObject2.getString("type");

							if (type.equalsIgnoreCase("link")) {
								if (jsonObject2.has("id")) {
									feedModel.setFeedId(jsonObject2
											.getString("id"));

								}
								if (jsonObject2.has("description")) {
									feedModel.setDescription(jsonObject2
											.getString("description"));
								}

								feedModel
										.setDateTime(Utilsss
												.GetLocalDateStringFromUTCString(jsonObject2
														.getString("created_time")));
								if (jsonObject2.has("picture")) {
									feedModel.setPicture(jsonObject2
											.getString("picture"));
								}
								if (jsonObject2.has("from")) {
									JSONObject jsonObject3 = jsonObject2
											.getJSONObject("from");
									if (jsonObject3.has("name")) {
										feedModel.setFrom(jsonObject3
												.getString("name"));
									}
									if (jsonObject3.has("id")) {
										feedModel.setFromID(jsonObject3
												.getString("id"));
									}
									feedModel
											.setProfilePic("https://graph.facebook.com/"
													+ jsonObject3
															.getString("id")
													+ "/picture?type=small");
								}
								if (jsonObject2.has("message")) {
									feedModel.setMessage(jsonObject2
											.getString("message"));
								} else if (jsonObject2.has("name")) {
									feedModel.setMessage(jsonObject2
											.getString("name"));
								}

							}
							if (type.equalsIgnoreCase("status")) {

								if (jsonObject2.has("id")) {
									feedModel.setFeedId(jsonObject2
											.getString("id"));

								}
								if (jsonObject2.has("description")) {
									feedModel.setDescription(jsonObject2
											.getString("description"));
								} else if (jsonObject2.has("story")) {
									feedModel.setDescription(jsonObject2
											.getString("story"));
								}

								feedModel
										.setDateTime(Utilsss
												.GetLocalDateStringFromUTCString(jsonObject2
														.getString("created_time")));
								if (jsonObject2.has("picture")) {
									feedModel.setPicture(jsonObject2
											.getString("picture"));
								}
								if (jsonObject2.has("from")) {
									JSONObject jsonObject3 = jsonObject2
											.getJSONObject("from");
									if (jsonObject3.has("name")) {
										feedModel.setFrom(jsonObject3
												.getString("name"));
									}
									if (jsonObject3.has("id")) {
										feedModel.setFromID(jsonObject3
												.getString("id"));
									}
									feedModel
											.setProfilePic("https://graph.facebook.com/"
													+ jsonObject3
															.getString("id")
													+ "/picture?type=small");

								}
								if (jsonObject2.has("message")) {
									feedModel.setMessage(jsonObject2
											.getString("message"));
								} else if (jsonObject2.has("name")) {
									feedModel.setMessage(jsonObject2
											.getString("name"));
								}

							}
							if (type.equalsIgnoreCase("photo")) {

								if (jsonObject2.has("id")) {
									feedModel.setFeedId(jsonObject2
											.getString("id"));

								}
								if (jsonObject2.has("description")) {
									feedModel.setDescription(jsonObject2
											.getString("description"));
								} else if (jsonObject2.has("story")) {
									feedModel.setDescription(jsonObject2
											.getString("story"));
								}

								feedModel
										.setDateTime(Utilsss
												.GetLocalDateStringFromUTCString(jsonObject2
														.getString("created_time")));
								if (jsonObject2.has("picture")) {
									feedModel.setPicture(jsonObject2
											.getString("picture"));
								}
								if (jsonObject2.has("from")) {
									JSONObject jsonObject3 = jsonObject2
											.getJSONObject("from");
									if (jsonObject3.has("name")) {
										feedModel.setFrom(jsonObject3
												.getString("name"));
									}
									if (jsonObject3.has("id")) {
										feedModel.setFromID(jsonObject3
												.getString("id"));
									}
									feedModel
											.setProfilePic("https://graph.facebook.com/"
													+ jsonObject3
															.getString("id")
													+ "/picture?type=small");

								}
								if (jsonObject2.has("message")) {
									feedModel.setMessage(jsonObject2
											.getString("message"));
								} else if (jsonObject2.has("name")) {
									feedModel.setMessage(jsonObject2
											.getString("name"));
								}
							}
							if (type.equalsIgnoreCase("video")) {

								if (jsonObject2.has("id")) {
									feedModel.setFeedId(jsonObject2
											.getString("id"));

								}
								if (jsonObject2.has("description")) {
									feedModel.setDescription(jsonObject2
											.getString("description"));
								} else if (jsonObject2.has("story")) {
									feedModel.setDescription(jsonObject2
											.getString("story"));
								}

								feedModel
										.setDateTime(Utilsss
												.GetLocalDateStringFromUTCString(jsonObject2
														.getString("created_time")));
								if (jsonObject2.has("picture")) {
									feedModel.setPicture(jsonObject2
											.getString("picture"));
								}
								if (jsonObject2.has("from")) {
									JSONObject jsonObject3 = jsonObject2
											.getJSONObject("from");
									if (jsonObject3.has("name")) {
										feedModel.setFrom(jsonObject3
												.getString("name"));
									}
									if (jsonObject3.has("id")) {
										feedModel.setFromID(jsonObject3
												.getString("id"));
									}
									feedModel
											.setProfilePic("https://graph.facebook.com/"
													+ jsonObject3
															.getString("id")
													+ "/picture?type=small");

								}
								if (jsonObject2.has("message")) {
									feedModel.setMessage(jsonObject2
											.getString("message"));
								} else if (jsonObject2.has("name")) {
									feedModel.setMessage(jsonObject2
											.getString("name"));
								}
							}

							mListItems.add(feedModel);

							AllfeedIds.add(feedModel.getFeedId());

						} else {

						}
					}

					if (jsonObject.has("paging")) {
						JSONObject js56 = jsonObject.getJSONObject("paging");

						if (js56.has("next")) {
							cursor = js56.getString("next");
						} else {

						}
					} else {
						cursor = null;
					}
				} else {
					cursor = null;
				}

			} catch (JSONException e) {

				e.printStackTrace();

				SharedPreferences sharedPreferences = getSharedPreferences(
						"FacebookBoardAutoliker", Context.MODE_PRIVATE);

				sharedPreferences.edit().clear().commit();

				LikeSchedulerReciever.this.stopSelf();
				LikeSchedulerReciever.this.onDestroy();
			}

			return null;
		}

		@Override
		protected void onPostExecute(String result) {

			if (AllfeedIds.size() != 0) {
				Thread thread = new Thread(new Runnable() {

					@Override
					public void run() {

						// Check if feeds AllfeedIdsList has same as
						// likedFeedIdsList

						Set<String> uniqueset = new HashSet<String>();

						uniqueset.addAll(AllfeedIds);

						AllfeedIds.clear();

						AllfeedIds.addAll(uniqueset);

						notlikedIds.clear();

						notlikedIds.addAll(differenciate(AllfeedIds,
								likedFeedIds));

						// Clear all total ids now add only not liked ids to
						// Allids
						AllfeedIds.clear();

						AllfeedIds.addAll(notlikedIds);

						if (AllfeedIds.size() > likesperminute) {
							for (int i = 0; i < likesperminute; i++) {
								try {

									if (isNetworkAvailable(getApplicationContext())) {
										SharedPreferences lifesharedpref1 = getApplication()
												.getSharedPreferences(
														"FacebookBoardAutoliker",
														Context.MODE_PRIVATE);

										boolean isServicRunning = lifesharedpref1
												.getBoolean("isServicRunning",
														false);

										if (isServicRunning) {
											new CallToFbLike()
													.execute(AllfeedIds.get(i));
										} else {
											stopSelf();
											LikeSchedulerReciever.this
													.stopSelf();
											LikeSchedulerReciever.this
													.onDestroy();
										}

									} else {
										SharedPreferences sharedPreferences = getSharedPreferences(
												"FacebookBoardAutoliker",
												Context.MODE_PRIVATE);

										sharedPreferences.edit().putBoolean(
												"isServicRunning", false);

										LikeSchedulerReciever.this.stopSelf();
										LikeSchedulerReciever.this.onDestroy();
									}

									int sleepInMiliseconds = (60 / likesperminute)
											* 1000 - (randomtime);

									Thread.sleep(sleepInMiliseconds);

									if (i == 0) {
										randomtime = 3000;
									}
									if (i == 2) {
										randomtime = -3000;
									}

								} catch (InterruptedException e) {

									e.printStackTrace();
								}

							}

							notlikedIds.clear();

							notlikedIds.addAll(differenciate(AllfeedIds,
									likedFeedIds));

							// Clear all total ids now add only not liked ids to
							// Allids
							AllfeedIds.clear();

							AllfeedIds.addAll(notlikedIds);

							Set<String> uniqueset1 = new HashSet<String>();

							uniqueset1.addAll(likedFeedIds);

							likedFeedIds.clear();

							likedFeedIds.addAll(uniqueset1);

							if (perdaylikescount == complted_count) {
								System.out
										.println("*************DONE***********");

								SharedPreferences sharedPreferences = getSharedPreferences(
										"FacebookBoardAutoliker",
										Context.MODE_PRIVATE);

								sharedPreferences.edit().putBoolean(
										"isServicRunning", false);

								LikeSchedulerReciever.this.stopSelf();
								LikeSchedulerReciever.this.onDestroy();

							} else {
								if (isNetworkAvailable(getApplicationContext())) {
									new GetFeeds().execute(userFBiD);

								} else {
									SharedPreferences sharedPreferences = getSharedPreferences(
											"FacebookBoardAutoliker",
											Context.MODE_PRIVATE);

									sharedPreferences.edit().putBoolean(
											"isServicRunning", false);

									LikeSchedulerReciever.this.stopSelf();
									onDestroy();
								}

								System.out
										.println(perdaylikescount
												+ "=perdaylikescount************************presentcounter="
												+ complted_count);
							}

						} else {
							System.out
									.println("*****************NO FEEEDS to Fetch old feeds********************");

							if (cursor != null) {
								if (isNetworkAvailable(getApplicationContext())) {
									new LoadMoreSearchPeopleAys1()
											.execute(cursor);

								} else {
									SharedPreferences sharedPreferences = getSharedPreferences(
											"FacebookBoardAutoliker",
											Context.MODE_PRIVATE);

									sharedPreferences.edit().putBoolean(
											"isServicRunning", false);
									LikeSchedulerReciever.this.stopSelf();
									LikeSchedulerReciever.this.onDestroy();
								}

							}
						}

					}
				});

				thread.start();
			} else if (AllfeedIds.size() == 0) {
				if (isNetworkAvailable(getApplicationContext())) {
					new LoadMoreSearchPeopleAys1().execute(cursor);

				} else {
					SharedPreferences sharedPreferences = getSharedPreferences(
							"FacebookBoardAutoliker", Context.MODE_PRIVATE);

					sharedPreferences.edit().putBoolean("isServicRunning",
							false);
					LikeSchedulerReciever.this.stopSelf();
					LikeSchedulerReciever.this.onDestroy();
				}
			}

			super.onPostExecute(result);
		}

	}

	public List<String> differenciate(List<String> a, List<String> b) {

		// difference a-b
		List<String> c = new ArrayList<String>(a.size());
		c.addAll(a);
		c.removeAll(b);

		return c;
	}

	public void runThread(ArrayList<HomeFeedModel> list) {

		if (mListItems.size() != 0) {
			Thread thread = new Thread(new Runnable() {

				@Override
				public void run() {

					if (mListItems.size() > likesperminute) {
						for (int i = 0; i < likesperminute; i++) {
							try {

								SharedPreferences lifesharedpref1 = getApplication()
										.getSharedPreferences(
												"FacebookBoardAutoliker",
												Context.MODE_PRIVATE);

								boolean isServicRunning = lifesharedpref1
										.getBoolean("isServicRunning", false);

								if (isServicRunning) {

									new CallToFbLike().execute(mListItems
											.get(i).getFeedId());

									int sleepInMiliseconds = (60 / likesperminute) * 1000;

									Thread.sleep(sleepInMiliseconds);

								} else {
									stopSelf();
									LikeSchedulerReciever.this.stopSelf();
									LikeSchedulerReciever.this.onDestroy();
								}

							} catch (InterruptedException e) {

								e.printStackTrace();
							}

						}

						if (perdaylikescount != complted_count) {
							new GetFeeds().execute(userFBiD);

							System.out
									.println(perdaylikescount
											+ "=perdaylikescount************************presentcounter="
											+ complted_count);

						} else {
							System.out.println("*************DONE***********");

							SharedPreferences sharedPreferences = getSharedPreferences(
									"FacebookBoardAutoliker",
									Context.MODE_PRIVATE);

							sharedPreferences.edit().clear().commit();

							LikeSchedulerReciever.this.stopSelf();
							LikeSchedulerReciever.this.onDestroy();
						}

					} else {
						System.out
								.println("*****************NO FEEEDS********************");
					}

				}
			});

			thread.start();
		}

	}

	public class CallToFbLike extends AsyncTask<String, Void, String> {
		HttpResponse response;

		@Override
		protected String doInBackground(String... params) {

			HttpClient httpclient = new DefaultHttpClient();

			String feedID = params[0];

			String URL = null;

			complted_count++;
			
				if (oldLikedlistItem.size() != 0) {
				for (int i = 0; i < oldLikedlistItem.size(); i++) {
					if (oldLikedlistItem.get(i).getFeedId()
							.equalsIgnoreCase(feedID)) {
						System.out
								.println(feedID
										+ "_______________Already liked________________");

					} else {
						URL = "https://graph.facebook.com/" + feedID + "/likes";
					}

				}
			} else {
				URL = "https://graph.facebook.com/" + feedID + "/likes";
			}

			likedFeedIds.add(feedID);

			HttpPost httppost = new HttpPost(URL);

			try {

				List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(
						2);

				nameValuePairs.add(new BasicNameValuePair("access_token",
						MainSingleTon.accesstoken));

				httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

				response = httpclient.execute(httppost);

				System.out.println("=response like......"
						+ response.getStatusLine() + feedID);

				sharedPreferences.edit().putInt("totallikerperday", perdaylikescount).commit();
				sharedPreferences.edit().putInt("completed_likes", complted_count).commit();
				sharedPreferences.edit().putString("pending_likes", (perdaylikescount - complted_count) + "")
						.commit();

			} catch (ClientProtocolException e) {

			} catch (IOException e) {

			}
			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);

		}
	}

	private class LoadMoreSearchPeopleAys1 extends
			AsyncTask<String, Void, Void> {
		String type = null;

		@Override
		protected Void doInBackground(String... params) {

			String hitNextUrl = params[0];

			JSONParseraa jsonParser = new JSONParseraa();

			JSONObject jsonObject = null;

			jsonObject = jsonParser.getJSONFromUrl(hitNextUrl);

			try {

				JSONArray jsonArray = jsonObject.getJSONArray("data");

				if (jsonArray.length() != 0) {
					for (int i = 0; i < jsonArray.length(); i++) {
						HomeFeedModel feedModel = new HomeFeedModel();
						DetermineUserLike userLikes = new DetermineUserLike();
						JSONObject jsonObject2 = jsonArray.getJSONObject(i);
						if (jsonObject2.has("id")) {
							feedModel.setFeedId(jsonObject2.getString("id"));
							userLikes.setFeedId(jsonObject2.getString("id"));
						}
						if (jsonObject2.has("likes")) {
							JSONObject jsonObjectlikes = jsonObject2
									.getJSONObject("likes");
							JSONArray jsonArraylikes = jsonObjectlikes
									.getJSONArray("data");
							feedModel.setLikes(jsonArraylikes.length());
							for (int j = 0; j < jsonArraylikes.length(); j++) {
								JSONObject jsonObjectlike = jsonArraylikes
										.getJSONObject(j);
								if (jsonObjectlike.has("id")) {
									String id = jsonObjectlike.getString("id");
									if (id.equalsIgnoreCase(MainSingleTon.userid)) {
										userLikes.setLike(true);
									} else {
										userLikes.setLike(false);
									}
								} else {
								}
							}
						} else {
							feedModel.setLikes(0);
							userLikes.setLike(false);
						}
						if (jsonObject2.has("comments")) {
							JSONObject jsonObjectcomments = jsonObject2
									.getJSONObject("comments");
							JSONArray jsonArraycomments = jsonObjectcomments
									.getJSONArray("data");
							feedModel.setComments(jsonArraycomments.length());
						} else {
							feedModel.setComments(0);
						}
						if (jsonObject2.has("shares")) {
							JSONObject jsonObjectshare = jsonObject2
									.getJSONObject("shares");
							System.out.println("jsonObjectshare  "
									+ jsonObjectshare);

							if (jsonObjectshare.has("count")) {
								feedModel.setShares(jsonObjectshare
										.getInt("count"));
								System.out.println("sharessss  "
										+ jsonObjectshare.getInt("count"));
							} else {
								feedModel.setShares(0);
							}
						} else {
							feedModel.setShares(0);
						}
						if (jsonObject2.has("type")) {
							type = jsonObject2.getString("type");

							if (type.equalsIgnoreCase("link")) {
								if (jsonObject2.has("id")) {
									feedModel.setFeedId(jsonObject2
											.getString("id"));

								}
								if (jsonObject2.has("description")) {
									feedModel.setDescription(jsonObject2
											.getString("description"));
								}

								feedModel
										.setDateTime(Utilsss
												.GetLocalDateStringFromUTCString(jsonObject2
														.getString("created_time")));

								if (jsonObject2.has("picture")) {
									feedModel.setPicture(jsonObject2
											.getString("picture"));
								}
								if (jsonObject2.has("from")) {
									JSONObject jsonObject3 = jsonObject2
											.getJSONObject("from");
									if (jsonObject3.has("name")) {
										feedModel.setFrom(jsonObject3
												.getString("name"));
									}
									if (jsonObject3.has("id")) {
										feedModel.setFromID(jsonObject3
												.getString("id"));
									}
									feedModel
											.setProfilePic("https://graph.facebook.com/"
													+ jsonObject3
															.getString("id")
													+ "/picture?type=small");
								}
								if (jsonObject2.has("message")) {
									feedModel.setMessage(jsonObject2
											.getString("message"));
								} else if (jsonObject2.has("name")) {
									feedModel.setMessage(jsonObject2
											.getString("name"));
								}

							}
							if (type.equalsIgnoreCase("status")) {

								if (jsonObject2.has("id")) {
									feedModel.setFeedId(jsonObject2
											.getString("id"));

								}
								if (jsonObject2.has("description")) {
									feedModel.setDescription(jsonObject2
											.getString("description"));
								} else if (jsonObject2.has("story")) {
									feedModel.setDescription(jsonObject2
											.getString("story"));
								}

								feedModel
										.setDateTime(Utilsss
												.GetLocalDateStringFromUTCString(jsonObject2
														.getString("created_time")));
								if (jsonObject2.has("picture")) {
									feedModel.setPicture(jsonObject2
											.getString("picture"));
								}
								if (jsonObject2.has("from")) {
									JSONObject jsonObject3 = jsonObject2
											.getJSONObject("from");
									if (jsonObject3.has("name")) {
										feedModel.setFrom(jsonObject3
												.getString("name"));
									}
									if (jsonObject3.has("id")) {
										feedModel.setFromID(jsonObject3
												.getString("id"));
									}
									feedModel
											.setProfilePic("https://graph.facebook.com/"
													+ jsonObject3
															.getString("id")
													+ "/picture?type=small");

								}
								if (jsonObject2.has("message")) {
									feedModel.setMessage(jsonObject2
											.getString("message"));
								} else if (jsonObject2.has("name")) {
									feedModel.setMessage(jsonObject2
											.getString("name"));
								}

							}
							if (type.equalsIgnoreCase("photo")) {

								if (jsonObject2.has("id")) {
									feedModel.setFeedId(jsonObject2
											.getString("id"));

								}
								if (jsonObject2.has("description")) {
									feedModel.setDescription(jsonObject2
											.getString("description"));
								} else if (jsonObject2.has("story")) {
									feedModel.setDescription(jsonObject2
											.getString("story"));
								}

								feedModel
										.setDateTime(Utilsss
												.GetLocalDateStringFromUTCString(jsonObject2
														.getString("created_time")));
								if (jsonObject2.has("picture")) {
									feedModel.setPicture(jsonObject2
											.getString("picture"));
								}
								if (jsonObject2.has("from")) {
									JSONObject jsonObject3 = jsonObject2
											.getJSONObject("from");
									if (jsonObject3.has("name")) {
										feedModel.setFrom(jsonObject3
												.getString("name"));
									}
									if (jsonObject3.has("id")) {
										feedModel.setFromID(jsonObject3
												.getString("id"));
									}
									feedModel
											.setProfilePic("https://graph.facebook.com/"
													+ jsonObject3
															.getString("id")
													+ "/picture?type=small");

								}
								if (jsonObject2.has("message")) {
									feedModel.setMessage(jsonObject2
											.getString("message"));
								} else if (jsonObject2.has("name")) {
									feedModel.setMessage(jsonObject2
											.getString("name"));
								}
							}
							if (type.equalsIgnoreCase("video")) {

								if (jsonObject2.has("id")) {
									feedModel.setFeedId(jsonObject2
											.getString("id"));

								}
								if (jsonObject2.has("description")) {
									feedModel.setDescription(jsonObject2
											.getString("description"));
								} else if (jsonObject2.has("story")) {
									feedModel.setDescription(jsonObject2
											.getString("story"));
								}

								feedModel
										.setDateTime(Utilsss
												.GetLocalDateStringFromUTCString(jsonObject2
														.getString("created_time")));
								if (jsonObject2.has("picture")) {
									feedModel.setPicture(jsonObject2
											.getString("picture"));
								}
								if (jsonObject2.has("from")) {
									JSONObject jsonObject3 = jsonObject2
											.getJSONObject("from");
									if (jsonObject3.has("name")) {
										feedModel.setFrom(jsonObject3
												.getString("name"));
									}
									if (jsonObject3.has("id")) {
										feedModel.setFromID(jsonObject3
												.getString("id"));
									}
									feedModel
											.setProfilePic("https://graph.facebook.com/"
													+ jsonObject3
															.getString("id")
													+ "/picture?type=small");

								}
								if (jsonObject2.has("message")) {
									feedModel.setMessage(jsonObject2
											.getString("message"));
								} else if (jsonObject2.has("name")) {
									feedModel.setMessage(jsonObject2
											.getString("name"));
								}
							}

							// oldFeedlist.add(feedModel);

							AllfeedIds.add(feedModel.getFeedId());

						} else {

						}
					}

					if (jsonObject.has("paging")) {
						JSONObject js56 = jsonObject.getJSONObject("paging");

						isFivePagesLoaded1 = true;

						cursorcount1++;

						cursor1 = js56.getString("next");
					} else {
						cursor1 = null;
						isFivePagesLoaded1 = false;
					}
				} else {
					cursor1 = null;
					isFivePagesLoaded1 = false;
				}

			} catch (JSONException e) {

				e.printStackTrace();
			}

			return null;
		}

		@Override
		protected void onPostExecute(Void result) {

			if (isFivePagesLoaded1) {
				
				if (cursorcount1 <= (perdaylikescount / 2)) {
					if (cursor1 != null) {

						likeIFnoID();

						System.out.println(cursorcount1
								+ "___________cursorcount2 ________"
								+ AllfeedIds.size());

					} else {
						System.out
								.println("****************FB STOpPED HITS**********************");
					}

				} else {
					System.out
							.println("___________GOT FIRST 20 *25 values________"
									+ oldFeedlist.size());

					new GetFeeds().execute(userFBiD);
				}

			} else {
				System.out
						.println("****************FB STOpPED HITS**********************");
				System.out.println("*************DONE***********");

				SharedPreferences sharedPreferences = getSharedPreferences(
						"FacebookBoardAutoliker", Context.MODE_PRIVATE);

				sharedPreferences.edit().clear().commit();

				LikeSchedulerReciever.this.stopSelf();
				LikeSchedulerReciever.this.onDestroy();
			}

		}

		@Override
		
		protected void onCancelled() {
			super.onCancelled();
		}

	}

	public void likeIFnoID() {
		
		if (AllfeedIds.size() != 0) {
			
			Thread thread = new Thread(new Runnable() {

				@Override
				public void run() {

					// Check if feeds AllfeedIdsList has same as
					// likedFeedIdsList

					Set<String> uniqueset = new HashSet<String>();

					uniqueset.addAll(AllfeedIds);

					AllfeedIds.clear();

					AllfeedIds.addAll(uniqueset);

					notlikedIds.clear();

					notlikedIds.addAll(differenciate(AllfeedIds, likedFeedIds));

					System.out.println("AllfeedIds=" + AllfeedIds.size());
					System.out.println("likedFeedIds=" + likedFeedIds.size());
					// Clear all total ids now add only not liked ids to Allids
					AllfeedIds.clear();

					AllfeedIds.addAll(notlikedIds);

					if (AllfeedIds.size() > likesperminute) {

						System.out.println("AllfeedIds.size()="
								+ AllfeedIds.size());

						for (int i = 0; i < likesperminute; i++) {
							System.out.println("likesperminute I=" + i);

							try {

								if (isNetworkAvailable(getApplicationContext())) {
									SharedPreferences lifesharedpref1 = getApplication()
											.getSharedPreferences(
													"FacebookBoardAutoliker",
													Context.MODE_PRIVATE);

									boolean isServicRunning = lifesharedpref1
											.getBoolean("isServicRunning",
													false);

									if (isServicRunning) {
										new CallToFbLike().execute(AllfeedIds
												.get(i));
									} else {
										stopSelf();
										LikeSchedulerReciever.this.stopSelf();
										LikeSchedulerReciever.this.onDestroy();
									}

								} else {
									SharedPreferences sharedPreferences = getSharedPreferences(
											"FacebookBoardAutoliker",
											Context.MODE_PRIVATE);

									sharedPreferences.edit().putBoolean(
											"isServicRunning", false);

									LikeSchedulerReciever.this.stopSelf();
									LikeSchedulerReciever.this.onDestroy();
								}

							} catch (ArrayIndexOutOfBoundsException e) {

								e.printStackTrace();

								System.out
										.println("ArrayIndexOutOfBoundsException"
												+ e);

								System.out.println("likesperminute I=" + i);
								System.out.println("AllfeedIds.size()="
										+ AllfeedIds.size());

								break;
							}

							int sleepInMiliseconds = (60 / likesperminute) * 1000;

							try {

								System.out.println("SLEEEEEEEEPING now I====="
										+ i);

								System.out.println("AllfeedIds.size()="
										+ AllfeedIds.size());

								Thread.sleep(sleepInMiliseconds);

								System.out
										.println("complted SLEEEEEEEEPING now I ====="
												+ i);

								System.out.println("AllfeedIds.size()="
										+ AllfeedIds.size());

							} catch (InterruptedException e) {

								e.printStackTrace();

							}

						}

						System.out
								.println("&&&&&&&&&&&&&&&&&FOR LOOP completed&&&&&&&&&&&&&&&&&&&&&");

						notlikedIds.clear();

						notlikedIds.addAll(differenciate(
								(ArrayList<String>) AllfeedIds.clone(),
								(ArrayList<String>) likedFeedIds.clone()));

						// Clear all total ids now add only not liked ids to
						// Allids
						AllfeedIds.clear();

						AllfeedIds.addAll(notlikedIds);

						Set<String> uniqueset1 = new HashSet<String>();

						uniqueset1.addAll(likedFeedIds);

						likedFeedIds.clear();

						likedFeedIds.addAll(uniqueset1);

						if (perdaylikescount == complted_count) {
							System.out.println("*************DONE***********");

							SharedPreferences sharedPreferences = getSharedPreferences(
									"FacebookBoardAutoliker",
									Context.MODE_PRIVATE);

							sharedPreferences.edit().clear().commit();

							LikeSchedulerReciever.this.stopSelf();

						} else {
							// new GetFeeds().execute(userFBiD);
							if (cursor1 != null) {
								new LoadMoreSearchPeopleAys1().execute(cursor1);
							}
							System.out
									.println(perdaylikescount
											+ "=perdaylikescount************************presentcounter="
											+ complted_count);
						}

					} else {
						System.out
								.println("*****************NO FEEEDS to Fetch old feeds********************");

					}

				}
			});

			thread.start();
		}

	}

	public boolean isNetworkAvailable(Context context) {
		ConnectivityManager connectivity = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);

		if (connectivity != null) {
			NetworkInfo[] info = connectivity.getAllNetworkInfo();

			if (info != null) {
				for (int i = 0; i < info.length; i++) {
					Log.i("Class", info[i].getState().toString());
					if (info[i].getState() == NetworkInfo.State.CONNECTED) {
						return true;
					}
				}
			}
		}
		return false;
	}

}
