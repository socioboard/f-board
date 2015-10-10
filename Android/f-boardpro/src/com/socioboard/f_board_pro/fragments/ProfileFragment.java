package com.socioboard.f_board_pro.fragments;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.socioboard.f_board_pro.MainActivity;
import com.socioboard.f_board_pro.R;
import com.socioboard.f_board_pro.ShowAlbum;
import com.socioboard.f_board_pro.adapter.AlbumAdapter;
import com.socioboard.f_board_pro.database.util.JSONParseraa;
import com.socioboard.f_board_pro.database.util.MainSingleTon;
import com.socioboard.f_board_pro.models.AlbumModel;
import com.socioboard.f_board_pro.models.FriendModel;
import com.socioboard.f_board_pro.models.UserProfileDetailsModel;
import com.socioboard.f_board_pro.viewlibary.GridViewHeaderFooterLib;

public class ProfileFragment extends Fragment
{
	View rootView;
	TextView mLocation, mWork, mBirthDay, mHomeTown, mEmail, mUserName,
	mGender;
	ImageView mProfilePic, mCoverPic, mGenderPic;
	LinearLayout locatinLnr, workLnr, birthdayLnr, hometownLnr, genderLnr;
	ProgressBar mProgressBar;
	String UserId = null;
	GridViewHeaderFooterLib gridViewH;
	Handler handler = new Handler();
	ArrayList<AlbumModel> mUserImageList;

	View gridHeaderView;
	SharedPreferences lifesharedpref ;

	public ProfileFragment(String UserId) 
	{
		this.UserId = UserId;
		mUserImageList = new ArrayList<AlbumModel>();
		//lifesharedpref = getActivity().getSharedPreferences("FacebookBoard", Context.MODE_PRIVATE);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState)
	{
		rootView = inflater.inflate(R.layout.user_profile_layout, container,false);

		gridHeaderView = inflater.inflate(R.layout.profilefragment_header, null,false);

		mProgressBar = (ProgressBar) rootView.findViewById(R.id.progressBar1);
		mLocation    = (TextView) gridHeaderView.findViewById(R.id.location);
		mWork        = (TextView) gridHeaderView.findViewById(R.id.work);
		mBirthDay    = (TextView) gridHeaderView.findViewById(R.id.birthday);
		mHomeTown    = (TextView) gridHeaderView.findViewById(R.id.town);
		mEmail       = (TextView) gridHeaderView.findViewById(R.id.useremail);
		mUserName    = (TextView) gridHeaderView.findViewById(R.id.username);
		mGender      = (TextView) gridHeaderView.findViewById(R.id.gender);

		locatinLnr   = (LinearLayout) gridHeaderView.findViewById(R.id.userlocation);
		workLnr      = (LinearLayout) gridHeaderView.findViewById(R.id.userwork);
		birthdayLnr  = (LinearLayout) gridHeaderView.findViewById(R.id.userbirthday);
		hometownLnr  = (LinearLayout) gridHeaderView.findViewById(R.id.usertown);
		genderLnr    = (LinearLayout) gridHeaderView.findViewById(R.id.usergender);

		lifesharedpref = getActivity().getSharedPreferences("FacebookBoard", Context.MODE_PRIVATE);
	 
		
		((MainActivity) getActivity()).setTitle(MainSingleTon.username);

		gridViewH = (GridViewHeaderFooterLib) rootView.findViewById(R.id.userphoto);

		gridViewH.addHeaderView(gridHeaderView, null, false);

		gridViewH.setVisibility(View.INVISIBLE);

		gridViewH.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				
				MainSingleTon.selectedAlbum=mUserImageList.get(position-1).getAlbumName();

				Intent intent = new Intent(getActivity(), ShowAlbum.class);
				intent.putExtra("albumID", mUserImageList.get(position-1).getAlbumId());
				startActivity(intent);				
			}
		});

		mProfilePic  = (ImageView) gridHeaderView.findViewById(R.id.userprofilepic);
		mCoverPic    = (ImageView) gridHeaderView.findViewById(R.id.usercoverpic);
		mGenderPic   = (ImageView) gridHeaderView.findViewById(R.id.gender_icon);

		setvisible(false);

		new GetUserDetails().execute();

		new GetUserPhotos().execute();
		
		//new GetUserFriends().execute();

		return rootView;

	}

	public void setvisible(boolean visible)
	{
		if (!visible) 
		{
			locatinLnr.setVisibility(View.GONE);
			workLnr.setVisibility(View.GONE);
			birthdayLnr.setVisibility(View.GONE);
			hometownLnr.setVisibility(View.GONE);
			genderLnr.setVisibility(View.GONE);

			mLocation.setVisibility(View.GONE);
			mWork.setVisibility(View.GONE);
			mBirthDay.setVisibility(View.GONE);
			mHomeTown.setVisibility(View.GONE);
			mEmail.setVisibility(View.GONE);
			mUserName.setVisibility(View.GONE);
			mProfilePic.setVisibility(View.GONE);
			mCoverPic.setVisibility(View.GONE);
			mGender.setVisibility(View.GONE);
			mProgressBar.setVisibility(View.VISIBLE);
		} 
		else
		{
			mProgressBar.setVisibility(View.INVISIBLE);
			locatinLnr.setVisibility(View.VISIBLE);
			workLnr.setVisibility(View.VISIBLE);
			birthdayLnr.setVisibility(View.VISIBLE);
			hometownLnr.setVisibility(View.VISIBLE);
			genderLnr.setVisibility(View.VISIBLE);

			mLocation.setVisibility(View.VISIBLE);
			mWork.setVisibility(View.VISIBLE);
			mBirthDay.setVisibility(View.VISIBLE);
			mHomeTown.setVisibility(View.VISIBLE);
			mEmail.setVisibility(View.VISIBLE);
			mUserName.setVisibility(View.VISIBLE);
			mProfilePic.setVisibility(View.VISIBLE);
			mCoverPic.setVisibility(View.VISIBLE);
			mGender.setVisibility(View.VISIBLE);
		}
	}

	public void checkValues(UserProfileDetailsModel model)
	{
		if (model.getUserBirthDate() == null)
		{
			birthdayLnr.setVisibility(View.GONE);
		}
		if (model.getUserGender() == null)
		{
			genderLnr.setVisibility(View.GONE);
		}
		if (model.getUserHomeTown() == null)
		{
			hometownLnr.setVisibility(View.GONE);
		}
		if (model.getUserLocation() == null)
		{
			locatinLnr.setVisibility(View.GONE);
		}
		if (model.getUserWork() == null) 
		{
			workLnr.setVisibility(View.GONE);
		}

	}

	public void setValues(UserProfileDetailsModel model)
	{
		mLocation.setText(model.getUserLocation());
		mWork.setText(model.getUserWork());
		mBirthDay.setText(model.getUserBirthDate());
		mEmail.setText(model.getUserMail());
		mHomeTown.setText(model.getUserHomeTown());
		mUserName.setText(model.getUserName());
		if (model.getUserGender() != null)
		{
			if (model.getUserGender().equalsIgnoreCase("male"))
			{
				mGenderPic.setBackgroundResource(R.drawable.male);
				mGender.setText("Male");
			} 
			else
			{
				mGenderPic.setBackgroundResource(R.drawable.female);
				mGender.setText("Female");
			}
		}
		getBitmap(mProfilePic, model.getUserProfilePic());
		getBitmap(mCoverPic, model.getUserCoverPic());
	}

	public void getBitmap(final ImageView profilePic, final String iconUrl) {

		new Thread(new Runnable() {

			@Override
			public void run() {

				handler.post(new Runnable() {

					Bitmap pfofile = MainSingleTon.getBitmapFromURL(iconUrl);

					@Override
					public void run() {
						if(pfofile!=null)
						{
							System.out.println("URL : " + iconUrl);
							profilePic.setImageBitmap((pfofile));
						}else
						{

						}
					}
				});

			}
		}).start();
	}

	/* class to GetUserDetails a post */
	public class GetUserDetails extends AsyncTask<String, Void, String> {

		String userFBaccesToken = null;
		UserProfileDetailsModel userModel = new UserProfileDetailsModel();

		@Override
		protected String doInBackground(String... params) {
			userFBaccesToken = MainSingleTon.accesstoken;

			// cover url
			// https://graph.facebook.com/469111716576852?fields=cover&access_token=CAANGZCSfBfk0BAP6Vnuu2X3PvGRwSCD970VxM4OaTmwO49ysexlKNVNxGToCYg0XVtBYeYlnVHv5rNVxCvOjgFwoQjLC5UDape3LZAcZB2Gg3Mnr7vVZAZBVoRZBkUnoa2otSzbeR1ngjmKymcFCHxCYMUXZAOq6IeGZA7JlgzfUSZBAv39Onw8SYWaO5nHaKJdkZD

			String tokenURL = "https://graph.facebook.com/" + UserId+ "/?access_token=" + userFBaccesToken;
			String coverUrl = "https://graph.facebook.com/" + UserId+ "?fields=cover&access_token=" + userFBaccesToken;

			JSONParseraa jsonParser = new JSONParseraa();

			JSONObject jsonObject = jsonParser.getJSONFromUrl(tokenURL);
			JSONObject jsonCover  = jsonParser.getJSONFromUrl(coverUrl);
			System.out.println("coverrrrrrrrr " + jsonCover);
			System.out.println("user details " + jsonObject);

			try
			{
				if (jsonCover!=null) {
					
				
				if (jsonCover.has("cover"))
				{
					System.out.println("cover url "+ jsonCover.getString("cover"));
					JSONObject jsonObject2 = jsonCover.getJSONObject("cover");
					if (jsonObject2.has("source"))
					{
						System.out.println("sourse  "+ jsonObject2.getString("source"));
						userModel.setUserCoverPic(jsonObject2.getString("source"));
						MainSingleTon.userCoverPicUrl=jsonObject2.getString("source");
					}

				}
				}
				
				userModel.setUserProfilePic("https://graph.facebook.com/"+ UserId + "/picture?type=small");
				
				if (jsonObject.has("work")) {
					
					String work = "", position = null, employer = null, location = null;
					JSONArray jsonArray = jsonObject.getJSONArray("work");
					/*
					 * for (int i = 0; i < jsonArray.length(); i++) {
					 */
					System.out.println("obj " + 0 + " " + jsonArray.get(0));
					JSONObject jsonObjectwork = jsonArray.getJSONObject(0);
					if (jsonObjectwork.has("position")) {
						JSONObject jsonObject2 = jsonObjectwork
								.getJSONObject("position");
						System.out.println("position "
								+ jsonObject2.getString("name"));
						position = jsonObject2.getString("name");
					}
					if (jsonObjectwork.has("employer")) {
						JSONObject jsonObject2 = jsonObjectwork
								.getJSONObject("employer");
						System.out.println("employer "
								+ jsonObject2.getString("name"));
						employer = jsonObject2.getString("name");
					}
					if (jsonObjectwork.has("location")) {
						JSONObject jsonObject2 = jsonObjectwork
								.getJSONObject("location");
						System.out.println("work location "
								+ jsonObject2.getString("name"));
						location = jsonObject2.getString("name");
					}
					if (position != null)
						work = position;
					if (employer != null) {
						work = work + " at " + employer;
					}
					if (location != null) {
						work = work + " at " + employer + ", " + location;
					}
					userModel.setUserWork(work);
					/* } */
				}
				if (jsonObject.has("birthday")) {
					System.out.println("DOB "
							+ jsonObject.getString("birthday"));
					userModel
					.setUserBirthDate(jsonObject.getString("birthday"));
				}
				if (jsonObject.has("hometown")) {
					JSONObject jsonObject2 = jsonObject
							.getJSONObject("hometown");
					System.out.println("hometown "
							+ jsonObject2.getString("name"));
					userModel.setUserHomeTown("From "
							+ jsonObject2.getString("name"));

				}
				if (jsonObject.has("location")) {
					JSONObject jsonObject3 = jsonObject
							.getJSONObject("location");
					System.out.println("location "
							+ jsonObject3.getString("name"));
					userModel.setUserLocation("Lives in "
							+ jsonObject3.getString("name"));
				}
				if (jsonObject.has("email")) {
					System.out.println("location "
							+ jsonObject.getString("email"));
					userModel.setUserMail(jsonObject.getString("email"));

				}
				if (jsonObject.has("name")) {
					System.out.println("user name "
							+ jsonObject.getString("name"));
					userModel.setUserName(jsonObject.getString("name"));
				}
				if (jsonObject.has("gender")) {
					System.out.println("user gender "
							+ jsonObject.getString("gender"));
					userModel.setUserGender(jsonObject.getString("gender"));
				}

			} catch (JSONException e) {
				e.printStackTrace();
				System.out.println("error " + e);
			}

			System.out.println("----------------------------------------------");
			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);

			System.out.println("user model " + userModel.toString());

			if (userModel != null) {
				setValues(userModel);
				setvisible(true);
				checkValues(userModel);
			} else {

			}

		}

	}

	/* class to GetUserPhotos a post */
	public class GetUserPhotos extends AsyncTask<String, Void, String>
	{

		String userFBaccesToken = null;

		@Override
		protected String doInBackground(String... params)
		{
			userFBaccesToken = MainSingleTon.accesstoken;
			// https://graph.facebook.com/me/photos/uploaded?access_token=CAANGZCSfBfk0BALxujJTJSywZA4SywcvZCSvSdRzMrW0AlVUkxGgQo04VxEqbsN3ZAJwmbym4qZCZAWQgZAwRLsxBDcSyitTmTT5wT7fgteC5M8ntZBP67oD7S0unenZBkyKJ1kAhOjCRVKLD0qTWL8itxgNNagdEZC8ZADAa7Sc3zZBzJKLNh4F7CnmL1j4OjeTrMkZD
			//photos :https://graph.facebook.com/371696929651665/photos?access_token=CAANGZCSfBfk0BAFCVmwYYmhS2ZCyoxCIkimPyUDyuEiFcwnFb5ZCZCkv2EptfJAmhnTxQDhLq9LDyKV8GzJzNgDT6NuYugXHbiZBRyrMKt1AcrZCDk91wCCPKryAf6MNaDAXZC1GDhfiTQxNijUZAxCJx1v5krRrqUE27XM2fpVth2N7It1px8PovKQtHUCfSgBjnqFyfs6YURDbE9UZBalgzOhqdqbViZCTVeqc2E7wx1DZBTBEQvCc2M4
			//cover :https://graph.facebook.com/371696929651665/picture?type=small&access_token=CAANGZCSfBfk0BAFCVmwYYmhS2ZCyoxCIkimPyUDyuEiFcwnFb5ZCZCkv2EptfJAmhnTxQDhLq9LDyKV8GzJzNgDT6NuYugXHbiZBRyrMKt1AcrZCDk91wCCPKryAf6MNaDAXZC1GDhfiTQxNijUZAxCJx1v5krRrqUE27XM2fpVth2N7It1px8PovKQtHUCfSgBjnqFyfs6YURDbE9UZBalgzOhqdqbViZCTVeqc2E7wx1DZBTBEQvCc2M4
			//albums :https://graph.facebook.com/371696929651665/albums?access_token=CAANGZCSfBfk0BAFCVmwYYmhS2ZCyoxCIkimPyUDyuEiFcwnFb5ZCZCkv2EptfJAmhnTxQDhLq9LDyKV8GzJzNgDT6NuYugXHbiZBRyrMKt1AcrZCDk91wCCPKryAf6MNaDAXZC1GDhfiTQxNijUZAxCJx1v5krRrqUE27XM2fpVth2N7It1px8PovKQtHUCfSgBjnqFyfs6YURDbE9UZBalgzOhqdqbViZCTVeqc2E7wx1DZBTBEQvCc2M4
			mUserImageList.clear();

			String tokenURL = "https://graph.facebook.com/me/albums?access_token="+ userFBaccesToken;

			JSONParseraa jsonParser = new JSONParseraa();

			JSONObject jsonObject = jsonParser.getJSONFromUrl(tokenURL);

			System.out.println("user albums "+jsonObject); 
			try 
			{
				JSONArray jsonArray = jsonObject.getJSONArray("data");
				for (int i = 0; i < jsonArray.length(); i++)
				{
					AlbumModel model = new AlbumModel();
					JSONObject jsonObject2 = jsonArray.getJSONObject(i);
					System.out.println("album "+i+" "+jsonObject2);
					if (jsonObject2.has("id"))
					{
						model.setAlbumId(jsonObject2.getString("id"));
						System.out.println(i + " album "+ jsonObject2.getString("id"));
						model.setAlbumCover("https://graph.facebook.com/"+jsonObject2.getString("id")+"/picture?type=album&access_token="+userFBaccesToken);
					}
					if (jsonObject2.has("name"))
					{
						model.setAlbumName(jsonObject2.getString("name"));
						System.out.println(i + "album name "+ jsonObject2.getString("name"));
					}
					if (jsonObject2.has("count"))
					{
						model.setAlbumSize(jsonObject2.getInt("count"));
						System.out.println(i + "album size "+ jsonObject2.getString("count"));
					}

					mUserImageList.add(model);
					System.out.println("mUserImageList size "+ mUserImageList.size());
				}
			} catch (Exception e)
			{
				e.printStackTrace();
			}

			return null;
		}

		@Override
		protected void onPostExecute(String result)
		{
			super.onPostExecute(result);
			if (mUserImageList.size() > 0) 
			{
				AlbumAdapter userPhotoAdapter = new AlbumAdapter(getActivity(), mUserImageList);
				gridViewH.setAdapter(userPhotoAdapter);
				gridViewH.setVisibility(View.VISIBLE);
			}
			else
			{
				gridViewH.setVisibility(View.INVISIBLE);
			}
		}

	}


	public class GetUserFriends extends AsyncTask<String, Void, String>
	{

		int present_friendCount=0;
		String userFBaccesToken = null;

		@Override
		protected String doInBackground(String... params)
		{
			userFBaccesToken = MainSingleTon.accesstoken;

			String tokenURL = "https://graph.facebook.com/me/friends?access_token="+userFBaccesToken;

			JSONParseraa jsonParser = new JSONParseraa();

			JSONObject jsonObject = jsonParser.getJSONFromUrl(tokenURL);

			try 
			{
				JSONArray jsonArray =  jsonObject.getJSONArray("data");

				for(int i = 0; i<jsonArray.length();i++)
				{
					System.out.println(i+" friend "+jsonArray.getJSONObject(i) );
					FriendModel model=new FriendModel();
					model.setFriendId(jsonArray.getJSONObject(i).getString("id"));
					model.setFriendName(jsonArray.getJSONObject(i).getString("name"));
					model.setFriendPic("https://graph.facebook.com/"+jsonArray.getJSONObject(i).getString("id")+"/picture?type=small");

				}

				if(jsonObject.has("summary"))
				{
					JSONObject jsonObject2 =jsonObject.getJSONObject("summary");

					present_friendCount =	jsonObject2.getInt("total_count");


				}else
				{
					//ZERO FRIENDS
				}
			}
			catch (Exception e) 
			{
				e.printStackTrace();
			}

			return null;
		}
		@Override
		protected void onPostExecute(String result)
		{
			super.onPostExecute(result);
		

			int oldFriendCount =lifesharedpref.getInt(MainSingleTon.userid, 0);

			if(oldFriendCount==0)
			{
				//Generate notification your the new user
				generateNotification("Welcome to FBoardPro your current  statistic has been recorded start using FboarPro regularly and increase your statistic");
			}
			if(oldFriendCount>present_friendCount)
			{
				//Generate notification One friend removed decrease statistics
				generateNotification("Your current FBoardPro statistics is decrease as you loosen a friend");

			}
			if(oldFriendCount<present_friendCount)
			{
				//Generate notification One friend added increase statistics
				generateNotification("Congratulations!! "+MainSingleTon.username+" you got new friend on FBoardPro");
			}
			SharedPreferences.Editor editor = lifesharedpref.edit();
			editor.putInt( MainSingleTon.userid, present_friendCount);
			editor.commit();

		}

	}

	public void generateNotification(String friendliststatus)
	{
		Intent intent1 = new Intent(getActivity(), MainActivity.class);

		intent1.setAction(Intent.ACTION_MAIN);

		intent1.addCategory(Intent.CATEGORY_LAUNCHER);

		PendingIntent pIntent = PendingIntent.getActivity(getActivity(), 0,
				intent1, 0);

		NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
				getActivity());

		mBuilder.setLargeIcon(BitmapFactory.decodeResource(
				getActivity().getResources(), R.drawable.ic_launcher));

		mBuilder.setSmallIcon(R.drawable.ic_launcher);

		mBuilder.setAutoCancel(true);

		mBuilder.setTicker("New FboardPro statistics");

		mBuilder.setContentIntent(pIntent);

		mBuilder.setContentTitle("Welcome!! "+MainSingleTon.username);

		mBuilder.setContentText(friendliststatus);

		Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

		mBuilder.setSound(alarmSound);

		NotificationManager mNotificationManager = (NotificationManager) getActivity().getSystemService(getActivity().NOTIFICATION_SERVICE);

		mNotificationManager.notify(77878, mBuilder.build());

	}
}
