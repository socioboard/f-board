package com.socioboard.f_board_pro;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.socioboard.f_board_pro.adapter.CustomPagerAdapter;
import com.socioboard.f_board_pro.database.util.JSONParseraa;
import com.socioboard.f_board_pro.database.util.MainSingleTon;
import com.socioboard.f_board_pro.database.util.Utilsss;
import com.socioboard.f_board_pro.models.ImageModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

public class ShowAlbum extends Activity
{
	String albumID=null;
	CustomPagerAdapter mCustomPagerAdapter;
	ViewPager mViewPager;
	ArrayList<ImageModel> imageList;
	TextView mNoPhotos, album_name;
	
	RelativeLayout rlt;
	 

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.show_album_fragment);
		
		imageList = new ArrayList<ImageModel>();
		
		albumID   = (String) getIntent().getSerializableExtra("albumID");
		album_name = (TextView) findViewById(R.id.album_name);
		
		album_name.setText(MainSingleTon.selectedAlbum);
		
		rlt = (RelativeLayout) findViewById(R.id.rlt);
		new GetUserPhotos().execute();
		
		rlt.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
				
			}
		});

		mNoPhotos  = (TextView)findViewById(R.id.nophotos);
		mNoPhotos.setVisibility(View.INVISIBLE);
		mViewPager = (ViewPager) findViewById(R.id.pager);
	}
	 
	public class GetUserPhotos extends AsyncTask<String, Void, String>
	{
		String userFBaccesToken = null;
		@Override
		protected String doInBackground(String... params)
		{
			userFBaccesToken = MainSingleTon.accesstoken;

			//photos :https://graph.facebook.com/371696929651665/photos?access_token=CAANGZCSfBfk0BAFCVmwYYmhS2ZCyoxCIkimPyUDyuEiFcwnFb5ZCZCkv2EptfJAmhnTxQDhLq9LDyKV8GzJzNgDT6NuYugXHbiZBRyrMKt1AcrZCDk91wCCPKryAf6MNaDAXZC1GDhfiTQxNijUZAxCJx1v5krRrqUE27XM2fpVth2N7It1px8PovKQtHUCfSgBjnqFyfs6YURDbE9UZBalgzOhqdqbViZCTVeqc2E7wx1DZBTBEQvCc2M4

			imageList.clear();
			System.out.println("album id="+albumID);
			//String tokenURL = "https://graph.facebook.com/"+albumID+"/photos?access_token="+ userFBaccesToken;
			String q="photos{images,name}";
			String tokenURL=null;
			try {
				tokenURL = "https://graph.facebook.com/"+albumID+"?fields="+ URLEncoder.encode(q,"UTF-8")+"&"+"access_token="+ userFBaccesToken;
			}catch (UnsupportedEncodingException e)
			{
				e.printStackTrace();
			}

			System.out.println("user photos tokenURL "+tokenURL); 
			JSONParseraa jsonParser = new JSONParseraa();

			JSONObject jsonObject = jsonParser.getJSONFromUrl(tokenURL);

			System.out.println("user photos "+jsonObject); 
			try 
			{
				JSONObject jsonObject1 = jsonObject.getJSONObject("photos");
				JSONArray jsonArray = jsonObject1.getJSONArray("data");
				for (int i = 0; i < jsonArray.length(); i++)
				{
					ImageModel model=new ImageModel();
					JSONObject jsonObject2 = jsonArray.getJSONObject(i);
					System.out.println("photo "+i+" "+jsonObject2);
					if (jsonObject2.has("id"))
					{
						model.setImageId(jsonObject2.getString("id"));						
					}
					if (jsonObject2.has("created_time"))
					{
						model.setImageDate(Utilsss.GetLocalDateStringFromUTCString(jsonObject2.getString("created_time")));
					}
					if (jsonObject2.has("name"))
					{
						model.setImageName(jsonObject2.getString("name"));
					}
					if(jsonObject2.has("images"))
					{
						JSONArray jsonArray1 =  jsonObject2.getJSONArray("images");
						model.setImageUrl(jsonArray1.getJSONObject(0).getString("source"));
						System.out.println("imagesssss"+jsonArray1.getJSONObject(0).getString("source"));
					}
					if (jsonObject2.has("likes"))
					{
						JSONObject jsonObjectlikes = jsonObject2.getJSONObject("likes");
						JSONArray jsonArraylikes =  jsonObjectlikes.getJSONArray("data");
						model.setImageLikes(jsonArraylikes.length());

					}
					else
					{
						model.setImageLikes(0);
					}
					if (jsonObject2.has("comments"))
					{
						JSONObject jsonObjectcomments = jsonObject2.getJSONObject("comments");
						JSONArray jsonArraycomments =  jsonObjectcomments.getJSONArray("data");
						model.setImageComments(jsonArraycomments.length());
					}
					else
					{
						model.setImageComments(0);
					}

					imageList.add(model);
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
			if (imageList.size()>0) 
			{
				mCustomPagerAdapter = new CustomPagerAdapter(getApplicationContext(),imageList);
				mViewPager.setAdapter(mCustomPagerAdapter);
				mNoPhotos.setVisibility(View.INVISIBLE);
			}
			else
			{
				mNoPhotos.setVisibility(View.VISIBLE);
				mViewPager.setVisibility(View.INVISIBLE);
			}
		}

	}



}
