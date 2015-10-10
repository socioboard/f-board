package com.socioboard.f_board_pro;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.socioboard.f_board_pro.database.util.F_Board_LocalData;
import com.socioboard.f_board_pro.database.util.MainSingleTon;
import com.socioboard.f_board_pro.models.SchPostModel;

public class ShareLinkServiceClass  extends Service{

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onStart(Intent intent, int startId) {
		//super.onStart(intent, startId);


		int getResponseCode;

		getResponseCode =intent.getIntExtra("sureshmmm", 404);

		System.out.println("++++++++++++++++++++++++++++++++++ Service class started  +++++++++++++++++++ getResponseCode"
				+ getResponseCode);

		F_Board_LocalData database = new F_Board_LocalData(getApplicationContext());

		SchPostModel schTweetModel = database.getShareScheduler(""+getResponseCode);

		if (schTweetModel != null) {

			postSharePostWall(schTweetModel, database.getUserData(schTweetModel.getUserID()).getUserAcessToken());
			
		}else
		{
			
			
		}
	}
	
	private void postSharePostWall(SchPostModel schPostModel, final String MYAccessToken) {

		try {

			JSONObject json = new JSONObject(schPostModel.getFeedText());

			JSONArray links_array = json.optJSONArray("sharelinks");
			

			for (int i = 0; i < links_array.length(); i++) 
			{
				final int sleepInMiliseconds = (60/schPostModel.getInterval())*1000;
				
				final String dumm =links_array.getString(i);
				
				Thread thread = new Thread(new Runnable() {
					
					@Override
					public void run() {
						 
						try {

							Thread.sleep(sleepInMiliseconds);
							
							shareAsys(dumm, MYAccessToken);

						} catch (InterruptedException e) {

							e.printStackTrace();

						}
					}
				});
				thread.start();

			}

		} catch (Exception e) {

			e.printStackTrace();
		}

	}
	
	
	public void shareAsys(String postLink, String MYAccessToken)
	{

		Bundle params = new Bundle();

		if(postLink.isEmpty())
		{

			/**Do nothing becz user just want to post text not an image*/

		}else
		{

			params.putString("link", postLink); // image to post	
		}

		params.putString(AccessToken.ACCESS_TOKEN_KEY, MYAccessToken);

		new GraphRequest(MainSingleTon.dummyAccesstoken, "me/feed", params,

				HttpMethod.POST, new GraphRequest.Callback() {

			@Override
			public void onCompleted(GraphResponse response) {

				System.out.println("Scheduled response="+response.getJSONObject());

				if(response.getJSONObject()!=null)
				{
					if(response.getJSONObject().has("id"))
					{
						//Success shareed
						System.out.println("Successs");
					}else
					{
						System.out.println("FAILED");
					}
				}else
				{
					//Not share not valid share linke
					System.out.println("NOT VALIDE LINKE");
				}

			}
		}).executeAsync();
	}

}
