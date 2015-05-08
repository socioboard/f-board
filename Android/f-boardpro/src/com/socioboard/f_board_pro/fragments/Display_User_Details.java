package com.socioboard.f_board_pro.fragments;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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

import com.socioboard.f_board_pro.MainActivity;
import com.socioboard.f_board_pro.R;
import com.socioboard.f_board_pro.adapter.CommentAdapter;
import com.socioboard.f_board_pro.adapter.UserPhotoAdapter;
import com.socioboard.f_board_pro.database.util.JSONParseraa;
import com.socioboard.f_board_pro.database.util.MainSingleTon;
import com.socioboard.f_board_pro.models.CommentModel;
import com.socioboard.f_board_pro.models.ImageModel;
import com.socioboard.f_board_pro.models.UserProfileDetailsModel;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class Display_User_Details extends Fragment  
{
	View rootView;
	TextView mLocation,mWork,mBirthDay,mHomeTown,mEmail,mUserName,mGender;
	ImageView mProfilePic,mCoverPic,mGenderPic;
	LinearLayout locatinLnr,workLnr,birthdayLnr,hometownLnr,genderLnr;
	ProgressBar mProgressBar;
	String UserId=null;
	GridView userphoto;
	 Handler handler= new Handler();
	 ArrayList<ImageModel> mUserImageList;
	 
	 public Display_User_Details(String UserId)
	 {
		 this.UserId=UserId;
		 mUserImageList = new ArrayList<ImageModel>();
	 }
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState)
	{
		rootView = inflater.inflate(R.layout.user_profile_layout, container, false);
		
		mProgressBar=(ProgressBar) rootView.findViewById(R.id.progressBar1);
		
		mLocation=(TextView) rootView.findViewById(R.id.location);
		mWork=(TextView) rootView.findViewById(R.id.work);
		mBirthDay=(TextView) rootView.findViewById(R.id.birthday);
		mHomeTown=(TextView) rootView.findViewById(R.id.town);
		mEmail=(TextView) rootView.findViewById(R.id.useremail);
		mUserName=(TextView) rootView.findViewById(R.id.username);
		mGender=(TextView) rootView.findViewById(R.id.gender);
		
		locatinLnr=(LinearLayout) rootView.findViewById(R.id.userlocation);
		workLnr=(LinearLayout) rootView.findViewById(R.id.userwork);
		birthdayLnr=(LinearLayout) rootView.findViewById(R.id.userbirthday);
		hometownLnr=(LinearLayout) rootView.findViewById(R.id.usertown);
		genderLnr=(LinearLayout) rootView.findViewById(R.id.usergender);
		
		
		((MainActivity) getActivity()).setTitle(MainSingleTon.username);
		
		userphoto=(GridView)rootView.findViewById(R.id.userphoto);
		userphoto.setVisibility(View.INVISIBLE);
		
		mProfilePic=(ImageView) rootView.findViewById(R.id.userprofilepic);
		mCoverPic=(ImageView) rootView.findViewById(R.id.usercoverpic);
		mGenderPic=(ImageView) rootView.findViewById(R.id.gender_icon);
		setvisible(false);
		
		new GetUserDetails().execute();
		new GetUserPhotos().execute();
		
		

		return rootView;

	}
	public void  setvisible(boolean visible) 
	{
		if(!visible)
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
		if(model.getUserBirthDate()==null)
		{
			birthdayLnr.setVisibility(View.GONE);
		}
		if(model.getUserGender()==null)
		{
			genderLnr.setVisibility(View.GONE);	
		}
		if(model.getUserHomeTown()==null)
		{
			hometownLnr.setVisibility(View.GONE);
		}
		if(model.getUserLocation()==null)
		{
			locatinLnr.setVisibility(View.GONE);
		}
		if(model.getUserWork()==null)
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
		if(model.getUserGender()!=null)
		{
			if(model.getUserGender().equalsIgnoreCase("male"))
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
		getBitmap(mProfilePic,model.getUserProfilePic());
	}
	
	public void getBitmap(final ImageView profilePic,final String iconUrl)
    {
		
		
    	new Thread(new Runnable()
    	{

			@Override
			public void run()
			{
				
				handler.post(new Runnable()
				{
					
						Bitmap pfofile  = MainSingleTon.getBitmapFromURL(iconUrl);
					
					
					@Override
					public void run()
					{
						System.out.println("URL : "+iconUrl);
						profilePic.setImageBitmap((pfofile));
					}
				});
			
			
			}
		}).start();
    }
	/*class to GetUserDetails a post*/
	public class GetUserDetails extends AsyncTask<String, Void, String>
	{

		
		String userFBaccesToken = null;
		UserProfileDetailsModel userModel=new UserProfileDetailsModel();
		@Override
		protected String doInBackground(String... params)
		{
			userFBaccesToken = MainSingleTon.accesstoken;
			
			

			String tokenURL = "https://graph.facebook.com/"+UserId+"/?access_token="+userFBaccesToken;

			JSONParseraa jsonParser = new JSONParseraa();

			JSONObject jsonObject = jsonParser.getJSONFromUrl(tokenURL);
			
			System.out.println("user details "+jsonObject);
			
			
			try
			{
				userModel.setUserProfilePic("https://graph.facebook.com/"+UserId+"/picture?type=small");
				if(jsonObject.has("work"))
				{
					String work=null,position=null,employer=null,location=null;
					JSONArray jsonArray =  jsonObject.getJSONArray("work");
					for (int i = 0; i < jsonArray.length(); i++)
					{
						System.out.println("obj "+i+" "+jsonArray.get(i));
						JSONObject jsonObjectwork = jsonArray.getJSONObject(i);
						if(jsonObjectwork.has("position"))
						{
							JSONObject jsonObject2 = jsonObjectwork.getJSONObject("position");
							System.out.println("position "+jsonObject2.getString("name"));
							position=jsonObject2.getString("name");
						}
						if(jsonObjectwork.has("employer"))
						{
							JSONObject jsonObject2 = jsonObjectwork.getJSONObject("employer");
							System.out.println("employer "+jsonObject2.getString("name"));
							employer=jsonObject2.getString("name");
						}
						if(jsonObjectwork.has("location"))
						{
							JSONObject jsonObject2 = jsonObjectwork.getJSONObject("location");
							System.out.println("work location "+jsonObject2.getString("name"));
							location=jsonObject2.getString("name");
						}
						work=position;
						if(employer!=null)
						{
							work=position+" at"+employer;
						}
						if(location!=null)
						{
							work=position+" at "+employer+", "+location;
						}
						userModel.setUserWork(work);
					}
				}
				if(jsonObject.has("birthday"))
				{
					System.out.println("DOB "+jsonObject.getString("birthday"));
					userModel.setUserBirthDate(jsonObject.getString("birthday"));
				}
				if(jsonObject.has("hometown"))
				{
					JSONObject jsonObject2 = jsonObject.getJSONObject("hometown");
					System.out.println("hometown "+jsonObject2.getString("name"));
					userModel.setUserHomeTown("From "+jsonObject2.getString("name"));
					
				}
				if(jsonObject.has("location"))
				{
					JSONObject jsonObject3 = jsonObject.getJSONObject("location");
					System.out.println("location "+jsonObject3.getString("name"));
					userModel.setUserLocation("Lives in "+jsonObject3.getString("name"));
				}
				if(jsonObject.has("email"))
				{
					System.out.println("location "+jsonObject.getString("email"));
					userModel.setUserMail(jsonObject.getString("email"));
					
				}
				if(jsonObject.has("name"))
				{					
					System.out.println("user name "+jsonObject.getString("name"));
					userModel.setUserName(jsonObject.getString("name"));					
				}
				if(jsonObject.has("gender"))
				{					
					System.out.println("user gender "+jsonObject.getString("gender"));
					userModel.setUserGender(jsonObject.getString("gender"));					
				}
					
			}
			catch (JSONException e) 
			{
				e.printStackTrace();
				System.out.println("error "+e);
			}
			 
			System.out.println("----------------------------------------------");
			return null;
		}
		@Override
		protected void onPostExecute(String result)
		{
			super.onPostExecute(result);
			
			System.out.println("user model "+userModel.toString());
			
							if(userModel!=null)
							{
								setValues(userModel);
								setvisible(true);
								checkValues(userModel);
							}
							else
							{
								
							}
			
		}
		

	}
	/*class to GetUserPhotos a post*/
	public class GetUserPhotos extends AsyncTask<String, Void, String>
	{

		
		String userFBaccesToken = null;
		
		@Override
		protected String doInBackground(String... params)
		{
			userFBaccesToken = MainSingleTon.accesstoken;
			//https://graph.facebook.com/me/photos/uploaded?access_token=CAANGZCSfBfk0BALxujJTJSywZA4SywcvZCSvSdRzMrW0AlVUkxGgQo04VxEqbsN3ZAJwmbym4qZCZAWQgZAwRLsxBDcSyitTmTT5wT7fgteC5M8ntZBP67oD7S0unenZBkyKJ1kAhOjCRVKLD0qTWL8itxgNNagdEZC8ZADAa7Sc3zZBzJKLNh4F7CnmL1j4OjeTrMkZD
			mUserImageList.clear();

			String tokenURL = "https://graph.facebook.com/me/photos/uploaded?access_token="+userFBaccesToken;

			JSONParseraa jsonParser = new JSONParseraa();

			JSONObject jsonObject = jsonParser.getJSONFromUrl(tokenURL);
			
		/*	System.out.println("user photos "+jsonObject);*/
			try 
			{
				JSONArray jsonArray =  jsonObject.getJSONArray("data");
				for(int i = 0; i<jsonArray.length();i++)
				{
					ImageModel model=new ImageModel();
					JSONObject jsonObject2 = jsonArray.getJSONObject(i);
//					System.out.println(i+" photo "+jsonObject2);
					if(jsonObject2.has("picture"))
					{
						model.setIcon(jsonObject2.getString("picture"));
						System.out.println(i+" picture "+jsonObject2.getString("picture"));
					}
					if(jsonObject2.has("images"))
					{
						JSONArray jsonArray1 =  jsonObject2.getJSONArray("images");
						model.setImage(jsonArray1.getJSONObject(0).getString("source"));
						System.out.println(i+" source "+jsonArray1.getJSONObject(0).getString("source"));
					}
					mUserImageList.add(model);
					System.out.println("mUserImageList size "+mUserImageList.size());
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
			if(mUserImageList.size()>0)
			{
				UserPhotoAdapter userPhotoAdapter = new UserPhotoAdapter(getActivity(), mUserImageList);
				userphoto.setAdapter(userPhotoAdapter);
				userphoto.setVisibility(View.VISIBLE);
			}
			else
			{
				userphoto.setVisibility(View.INVISIBLE);
			}
		}
		

	}
}
