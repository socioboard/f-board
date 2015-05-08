package com.socioboard.f_board_pro.database.util;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.facebook.AccessToken;
import com.socioboard.f_board_pro.models.DetermineUserLike;
import com.socioboard.f_board_pro.models.GroupFeedModel;
import com.socioboard.f_board_pro.models.GroupModel;
import com.socioboard.f_board_pro.models.HomeFeedModel;
import com.socioboard.f_board_pro.models.PagesModel;

public class MainSingleTon {

	public static boolean signedInStatus = false; // login status
	
	public static String userid; //current user id
	
	public static String username; //username
	
	public static String accesstoken; //current user access token
	
	public static String userimage;  //current user imege
	
	public static String userEmail; //current user email

	public static AccessToken dummyAccesstoken;  //copy of current user access token

	public static boolean isfrom_schedulefrag=false; //flag for scheduling

	public static HashMap<String, ModelUserDatas> userdetails = new HashMap<String, ModelUserDatas>();//current user details
	
	public static ArrayList<ModelUserDatas> modelUserDatasList; //array list for holding list of users
	
	public static String Accountposition; //position of the user in drawer and list
	
	public static String FeedsNextUrl = null; // url for paging the feeds
	
	public static ArrayList<String> useridlist=new ArrayList<String>(); //list of user id's
	
	//a static method to extract the images as bitmap
	public static Bitmap getBitmapFromURL(String image_link) 
	{
		try
		{
			URL url = new URL(image_link);
			
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			
			connection.setDoInput(true);
			
			connection.connect();
			
			InputStream input = connection.getInputStream();
			
			Bitmap myBitmap = BitmapFactory.decodeStream(input);
			
			return myBitmap;
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
			
			return null;
		}
	}

	public static ArrayList<PagesModel> pagesArrayList = new ArrayList<PagesModel>();//array list for pages and there details
	
	public static boolean isPAgesLoaded = false; //status of page losding
	
	public static String pageFBiD =null; // id of the page

	public static String feedsURL = "https://graph.facebook.com/1049905175035141/feed?access_token=CAACZB5L4uuV8BACXwWhgpnE6lrSuIz0vdr6HtMQM8rUEKFPBVfhuYr56OCvPmRqsWPoYaMtYmaRGPZCqRqa562eaoSXaa1xScB5zKtE5jHFw07wI0GENjFOnluGrduNhHRqJT1iNUCFnTh5GXmZAtc4AiZAPMvVXS9EidsDo9PNVQwd262eSFapVZCFvxJpIZD";
	
	public static GroupModel selectedGroupToFetchFeeds=new GroupModel(); // holds the selected group details to display feeds
	
	public static GroupFeedModel selectedGroupFeed=new GroupFeedModel(); // holds the selected group feed details to display feeds
	
	public static ArrayList<GroupModel> groupArrayList = new ArrayList<GroupModel>(); // holds the list of groups details to display feeds
	
	public static	String  NextUrl =null; // holds the next url for paging

	public static HomeFeedModel selectedHomeFeed = new HomeFeedModel(); // holds the selected home feed details to display feeds
	
	public static ArrayList<HomeFeedModel> feedArrayList = new ArrayList<HomeFeedModel>();// holds the list of home feed details to display feeds

	public static ArrayList<DetermineUserLike> userLikedFeedList = new ArrayList<DetermineUserLike>(); // holds the list feed details which user has liked
	
	public static String selectedFeedForLikes =null; //holds the id of a feed for fetching likes
}
