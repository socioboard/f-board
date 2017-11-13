package com.socioboard.f_board_pro.database.util;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.socioboard.f_board_pro.models.DetermineUserLike;
import com.socioboard.f_board_pro.models.GroupFeedModel;
import com.socioboard.f_board_pro.models.GroupModel;
import com.socioboard.f_board_pro.models.HomeFeedModel;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

public class MainSingleTon {

	public static boolean signedInStatus = false; // login status

	public static String userid; //current user id  -----1
	private static Bitmap bitmapSmall;
	public static String username; //username           -----2
	public static String userFirstName;//...-----7
	public static String userLargeImagePath;//-----8
	public static String userGender;//-----9
	public static String userDOB;//-----10
	public static String userWorkPlace;//-----11
	public static String userCurrentLocation;//-----12
	public static String userHomeTown;//-----13
	public static String accesstoken; //current user access token   -----3

	public static String userimage;  //current user imege     -----4

	public static String userEmail; //current user email   -----5
	public static String userCoverPicUrl = null; // url for cover pic  -----6
	public static AccessToken dummyAccesstoken;  //copy of current user access token

	public static boolean isfrom_schedulefrag=false; //flag for scheduling

	public static HashMap<String, ModelUserDatas> userdetails = new HashMap<String, ModelUserDatas>();//current user details

	public static ArrayList<ModelUserDatas> modelUserDatasList; //array list for holding list of users

	public static String Accountposition; //position of the user in drawer and list

	public static String FeedsNextUrl = null; // url for paging the feeds
	public static boolean checkFirstTimeLogin;


	public static ArrayList<String> useridlist=new ArrayList<String>(); //list of user id's
	public static ArrayList<String> useraccesstokenlist=new ArrayList<String>(); //list of user id's
	public static ArrayList<String> shareLinks = new ArrayList<String>();
	public static ArrayList<String> pageShareagonList = new ArrayList<String>();
	public static ArrayList<String> groupsharegonList=new ArrayList<String>();
	public static ArrayList<String> groupsharegonNameList = new ArrayList<>();
	public static ArrayList<String> autoLikerPageList = new ArrayList<>();


	
	//a static method to extract the images as bitmap
	public static Bitmap getBitmapFromURL(String image_link) {
		if (image_link==null) {
          return null;
		}

			bitmapSmall = null;
			//bitmapLarge=null;

			try {
				System.out.println("image_link22223---" + image_link);
				URL url = new URL(image_link);

				HttpURLConnection connection = (HttpURLConnection) url.openConnection();

				connection.setDoInput(true);

				connection.connect();

				InputStream input = connection.getInputStream();

				bitmapSmall = BitmapFactory.decodeStream(input);

				return bitmapSmall;
			} catch (IOException e) {
				e.printStackTrace();

				return bitmapSmall;
			}

	}



	//public static ArrayList<PagesModel> pagesArrayList = new ArrayList<PagesModel>();//array list for pages and there details

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

	public static String selectedShareLink  =null; //holds the share link of a feed 
	
	public static String selectedAlbum  =null;

	public static int share=0;
    public static String smallLink=null;
	public static void showMyToast(final Activity context,final String text)
	{
		context.runOnUiThread(new Runnable() 
		{
			public void run()
			{
				Toast.makeText(context.getApplicationContext(), text, Toast.LENGTH_SHORT).show();	
			}
		});

	}

	public static String introtext = "You can manage your multiple account ";

	public static String searchKey;

	public static String pgID = null;

	public static ArrayList<String>likePgID = new ArrayList<>();

 	public static String pgNAME =null;

 	public static String pgCategory =null;

	public static String searchKey1 =null;

	public static boolean selectAdmingPage = false;

	public static boolean firebaseNotification = true;


}
