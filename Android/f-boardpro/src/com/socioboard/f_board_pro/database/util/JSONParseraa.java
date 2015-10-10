package com.socioboard.f_board_pro.database.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;
public class JSONParseraa {
	
	
	static InputStream iStream = null;
	static JSONObject jsonObject = null;
	static String json = "";
	
	public JSONParseraa() { 
		} 
	
	public JSONObject getJSONFromUrl(String url) {
		
		System.out.println("inside jsonparser###########");
		
		StringBuilder builder = new StringBuilder();
		HttpClient client = new DefaultHttpClient();
		HttpGet httpGet = new HttpGet(url);
		
		try {
			
			HttpResponse response = client.execute(httpGet);
			StatusLine statusLine = response.getStatusLine();
			
			int statusCode = statusLine.getStatusCode();
			
			if (statusCode == 200) {
				
				HttpEntity entity = response.getEntity();
				InputStream content = entity.getContent();
				BufferedReader reader = new BufferedReader(new InputStreamReader(content));
				String line;
				
				while ((line = reader.readLine()) != null) 
				{
					builder.append(line);
					
				 
					}
				} 
			else 
				{ 
					Log.e("==>", "Failed to download file");
				} 
			
			}
			catch (ClientProtocolException e)
			{ 
				e.printStackTrace();
			} 
			catch (IOException e) {
				e.printStackTrace();
				}
	
		// Parse String to JSON object 
		try { 
				jsonObject = new JSONObject( builder.toString());
				 
			} 
		catch (JSONException e) {
			Log.e("JSON Parser", "Error parsing data " + e.toString());
			} // return JSON Object return jarray;
		
			// return JSON Object
			return jsonObject;
	}
}

