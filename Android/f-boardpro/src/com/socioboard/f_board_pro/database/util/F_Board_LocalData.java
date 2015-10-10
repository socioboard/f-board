package com.socioboard.f_board_pro.database.util;


import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.socioboard.f_board_pro.models.SchPostModel;

@SuppressLint("UseValueOf")
public class F_Board_LocalData extends SQLiteOpenHelper {

	public static final String db_name = "fboards.db";

	public static final String table_name = "fboardtable";

	public static final String KEY_UserID = "userid";

	public static final String KEY_Username = "username";

	public static final String KEY_UserAcessToken = "useracesstoken";

	public static final String KEY_Userimage="userimage";

	public static final String sch_table_name = "schedullertable";

	public static final String share_table_name = "sharelink";
	
	public static final String page_shareagon_table_name = "pageshareagon";

	public static final String KEY_SchTID = "schtid";

	public static final String KEY_FeedText = "feedtext";

	public static final String KEY_ShareLink = "shareLink";

	public static final String KEY_FeedImagePath ="feedImagePath";

	public static final String KEY_Interval ="shareinterval";

	public static final String KEY_FeedTime = "feedtimestamp";


	public F_Board_LocalData(Context context) {

		super(context, db_name, null, 1);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		onCreate(db);
	}

	public void CreateTable() {

		String querry = "CREATE TABLE IF NOT EXISTS " + table_name + "("
				+ KEY_UserID + " TEXT," + KEY_Userimage + " TEXT," + KEY_Username + " TEXT,"
				+ KEY_UserAcessToken + " TEXT)";


		String querry2 = "CREATE TABLE IF NOT EXISTS " + sch_table_name + "("
				+ KEY_SchTID + " INTEGER,"+ KEY_UserID + " TEXT,"  + KEY_FeedText + " TEXT,"
				+ KEY_FeedImagePath + " TEXT," + KEY_FeedTime + " INTEGER)";
		

		String querry3 = "CREATE TABLE IF NOT EXISTS " + share_table_name + "("
				+ KEY_SchTID + " INTEGER,"+ KEY_UserID + " TEXT,"+KEY_ShareLink+
				" TEXT,"  + KEY_FeedTime + " INTEGER," + KEY_Interval + " INTEGER)";
		
		String querry4 = "CREATE TABLE IF NOT EXISTS " + page_shareagon_table_name + "("
				+ KEY_SchTID + " INTEGER,"+ KEY_UserID + " TEXT,"+KEY_ShareLink+
				" TEXT,"  + KEY_FeedTime + " INTEGER," + KEY_Interval + " INTEGER)";


		SQLiteDatabase database = this.getWritableDatabase();

		database.execSQL(querry);
		database.execSQL(querry2);
		database.execSQL(querry3);
		database.execSQL(querry4);

		System.out.println("CreateTable " + querry);
		System.out.println("CreateTable2 " + querry2);
		System.out.println("CreateTable3 " + querry3);
		System.out.println("CreateTable3 " + querry4);
		

	}

	public void addNewUserAccount(ModelUserDatas modelUserDatas) {

		// String query = "INSERT INTO " + table_name + "";
		SQLiteDatabase database = this.getWritableDatabase();

		ContentValues contentValues = new ContentValues();
		contentValues.put(KEY_UserID, modelUserDatas.getUserid());
		contentValues.put(KEY_Userimage, modelUserDatas.getUserimage());
		contentValues.put(KEY_Username, modelUserDatas.getUsername());
		contentValues.put(KEY_UserAcessToken,modelUserDatas.getUserAcessToken());

		database.insert(table_name, null, contentValues);

		System.out.println("addNewUserAccount " + contentValues);

	}

	public ModelUserDatas getUserData(String userId) {

		ModelUserDatas modelUserDatas = null;

		String query = "SELECT * FROM " + table_name + " WHERE " + KEY_UserID
				+ " = '" + userId + "'";

		SQLiteDatabase database = this.getReadableDatabase();

		Cursor cursor = database.rawQuery(query, null);

		if (cursor.moveToFirst()) {

			modelUserDatas = new ModelUserDatas();
			modelUserDatas.setUserid(cursor.getString(0));
			modelUserDatas.setUserimage(cursor.getString(1));
			modelUserDatas.setUsername(cursor.getString(2));
			modelUserDatas.setUserAcessToken(cursor.getString(3));
		}

		return modelUserDatas;
	}

	public void getAllUsersData() {

		String query = "SELECT * FROM " + table_name;

		System.out.println(query);

		SQLiteDatabase database = this.getReadableDatabase();

		Cursor cursor = database.rawQuery(query, null);

		ModelUserDatas modelUserDatas;
		MainSingleTon.userdetails.clear();
		MainSingleTon.useridlist.clear(); 
		if (cursor.moveToFirst()) {

			do {

				modelUserDatas = new ModelUserDatas();
				modelUserDatas.setUserid(cursor.getString(0));
				modelUserDatas.setUserimage(cursor.getString(1));
				modelUserDatas.setUsername(cursor.getString(2));
				modelUserDatas.setUserAcessToken(cursor.getString(3));
				MainSingleTon.userdetails.put(cursor.getString(0), modelUserDatas);
				MainSingleTon.useridlist.add(cursor.getString(0));
			} while (cursor.moveToNext());
		}

	}

	public void updateUserData(ModelUserDatas modelUserDatas) {

		SQLiteDatabase database = this.getWritableDatabase();

		String updateQuery = "UPDATE " + table_name + " SET "
				+ KEY_Username+ " = '" + modelUserDatas.getUsername()+"' , "
				+ KEY_Userimage+ " = '" + modelUserDatas.getUserimage() + "' , "
				+ KEY_UserAcessToken + " = '" + modelUserDatas.getUserAcessToken() + "' " + " WHERE "
				+ KEY_UserID + " = '" + modelUserDatas.getUserid() + "'";

		System.out.println(updateQuery);

		database.execSQL(updateQuery);
	}

	public ArrayList<ModelUserDatas> getAllUsersDataArlist() {

		String query = "SELECT * FROM " + table_name;

		ArrayList<ModelUserDatas> allUserDetails = new ArrayList<ModelUserDatas>();

		System.out.println(query);

		SQLiteDatabase database = this.getReadableDatabase();

		Cursor cursor = database.rawQuery(query, null);

		ModelUserDatas modelUserDatas;

		if (cursor.moveToFirst()) {

			do {

				modelUserDatas = new ModelUserDatas();
				modelUserDatas.setUserid(cursor.getString(0));
				modelUserDatas.setUserimage(cursor.getString(1));
				modelUserDatas.setUsername(cursor.getString(2));
				modelUserDatas.setUserAcessToken(cursor.getString(3));
				allUserDetails.add(modelUserDatas);

			} while (cursor.moveToNext());
		}

		return allUserDetails;
	}



	public void deleteAllRows() {

		SQLiteDatabase database = this.getWritableDatabase();

		String query = "DELETE FROM " + table_name;
		System.out.println(query);
		database.execSQL(query);
	}

	public void deleteThisUserData(String userID) {

		SQLiteDatabase database = this.getWritableDatabase();

		String query = "DELETE FROM " + table_name + " WHERE " + KEY_UserID
				+ " = " + userID;

		System.out.println(query);
		database.execSQL(query);

	}     

	// SCHEDULLED TWEET;

	public void addNewSchedulledTweet(SchPostModel schTweetModel) {

		SQLiteDatabase database = this.getWritableDatabase();

		ContentValues contentValues = new ContentValues();

		contentValues.put(KEY_SchTID, schTweetModel.getFeedId());

		contentValues.put(KEY_UserID, schTweetModel.getUserID());

		contentValues.put(KEY_FeedText, schTweetModel.getFeedText());

		contentValues.put(KEY_FeedImagePath, schTweetModel.getFeedImagePath());

		contentValues.put(KEY_FeedTime, schTweetModel.getFeedtime());

		database.insert(sch_table_name, null, contentValues);

		System.out.println("addNewSchedulledTweet " + contentValues);

	}

	//Create sharescheduler
	public void addNewPageShareagon(SchPostModel schTweetModel) {

		SQLiteDatabase database = this.getWritableDatabase();

		ContentValues contentValues = new ContentValues();

		contentValues.put(KEY_SchTID, schTweetModel.getFeedId());

		contentValues.put(KEY_UserID, schTweetModel.getUserID());

		contentValues.put(KEY_ShareLink, schTweetModel.getFeedText());

		contentValues.put(KEY_FeedTime, schTweetModel.getFeedtime());

		contentValues.put(KEY_Interval, schTweetModel.getInterval());

		database.insert(page_shareagon_table_name, null, contentValues);

		System.out.println("add new Share scheduler " + contentValues);

	}
	
	public void addNewShareScheduler(SchPostModel schTweetModel) {

		SQLiteDatabase database = this.getWritableDatabase();

		ContentValues contentValues = new ContentValues();

		contentValues.put(KEY_SchTID, schTweetModel.getFeedId());

		contentValues.put(KEY_UserID, schTweetModel.getUserID());

		contentValues.put(KEY_ShareLink, schTweetModel.getFeedText());

		contentValues.put(KEY_FeedTime, schTweetModel.getFeedtime());

		contentValues.put(KEY_Interval, schTweetModel.getInterval());

		database.insert(share_table_name, null, contentValues);

		System.out.println("add new Share scheduler " + contentValues);

	}

	public SchPostModel getPageShareagon(String schId) {

		SchPostModel schFeedModel = null;

		String query = "SELECT * FROM " + page_shareagon_table_name + " WHERE "
				+ KEY_SchTID + " = '" + schId + "'";

		SQLiteDatabase database = this.getReadableDatabase();

		Cursor cursor = database.rawQuery(query, null);

		if (cursor.moveToFirst()) {

			int feedID       = cursor.getInt(0);

			String userID    = cursor.getString(1);

			String sharelink = cursor.getString(2);

			Long feedtime    = new Long(cursor.getString(3));

			int interval     = new Integer(cursor.getString(4));

			schFeedModel     = new SchPostModel(feedID, userID, sharelink, feedtime.longValue(), interval);
		}

		return schFeedModel;
	}

	//Createe get Schedulled
	public SchPostModel getShareScheduler(String schId) {

		SchPostModel schFeedModel = null;

		String query = "SELECT * FROM " + share_table_name + " WHERE "
				+ KEY_SchTID + " = '" + schId + "'";

		SQLiteDatabase database = this.getReadableDatabase();

		Cursor cursor = database.rawQuery(query, null);

		if (cursor.moveToFirst()) {

			int feedID       = cursor.getInt(0);

			String userID    = cursor.getString(1);

			String sharelink = cursor.getString(2);

			Long feedtime    = new Long(cursor.getString(3));

			int interval     = new Integer(cursor.getString(4));

			schFeedModel     = new SchPostModel(feedID, userID, sharelink, feedtime.longValue(), interval);
		}

		return schFeedModel;
	}

	public SchPostModel getSchedulledTweet(String schId) {

		SchPostModel schFeedModel = null;

		String query = "SELECT * FROM " + sch_table_name + " WHERE "
				+ KEY_SchTID + " = '" + schId + "'";

		SQLiteDatabase database = this.getReadableDatabase();

		Cursor cursor = database.rawQuery(query, null);

		if (cursor.moveToFirst()) {

			int feedID = cursor.getInt(0);

			String userID = cursor.getString(1);

			String feedText = cursor.getString(2);

			String feedImagePath  = cursor.getString(3);

			Long feedtime  = new Long(cursor.getString(4));

			schFeedModel = new SchPostModel(feedID, userID, feedText, feedImagePath, feedtime.longValue());
		}

		return schFeedModel;
	}

	public ArrayList<SchPostModel> getAllSchedulledFeeds() {

		String query = "SELECT * FROM " + sch_table_name;

		ArrayList<SchPostModel> allschTweets = new ArrayList<SchPostModel>();

		System.out.println(query);

		SQLiteDatabase database = this.getReadableDatabase();

		Cursor cursor = database.rawQuery(query, null);

		SchPostModel schTweetModel;

		if (cursor.moveToFirst()) {

			do {

				int feedID = cursor.getInt(0);

				String userID = cursor.getString(1);

				String feedText = cursor.getString(2);

				String feedImagePath  = cursor.getString(3);

				Long feedtime  = new Long(cursor.getString(4));

				schTweetModel   = new SchPostModel(feedID, userID, feedText, feedImagePath, feedtime.longValue());

				allschTweets.add(schTweetModel);


			} while (cursor.moveToNext());
		}

		return allschTweets;

	}
	
	
	//Share getall
	public ArrayList<SchPostModel> getAllSchedulledShares() {

		String query = "SELECT * FROM " + share_table_name;

		ArrayList<SchPostModel> allschTweets = new ArrayList<SchPostModel>();

		System.out.println(query);

		SQLiteDatabase database = this.getReadableDatabase();

		Cursor cursor = database.rawQuery(query, null);

		SchPostModel schTweetModel;

		if (cursor.moveToFirst()) {

			do {

				int feedID = cursor.getInt(0);

				String userID = cursor.getString(1);

				String sharelinks = cursor.getString(2);

				Long feedtime    = new Long(cursor.getString(3));

				int interval     = new Integer(cursor.getString(4));

				schTweetModel   = new SchPostModel(feedID, userID, sharelinks, feedtime, interval);

				allschTweets.add(schTweetModel);


			} while (cursor.moveToNext());
		}

		return allschTweets;

	}

	//Share getall
		public ArrayList<SchPostModel> getAllSchedulledPageShareagon() {

			String query = "SELECT * FROM " + page_shareagon_table_name;

			ArrayList<SchPostModel> allschTweets = new ArrayList<SchPostModel>();

			System.out.println(query);

			SQLiteDatabase database = this.getReadableDatabase();

			Cursor cursor = database.rawQuery(query, null);

			SchPostModel schTweetModel;

			if (cursor.moveToFirst()) {

				do {

					int feedID = cursor.getInt(0);

					String userID = cursor.getString(1);

					String sharelinks = cursor.getString(2);

					Long feedtime    = new Long(cursor.getString(3));

					int interval     = new Integer(cursor.getString(4));

					schTweetModel   = new SchPostModel(feedID, userID, sharelinks, feedtime, interval);

					allschTweets.add(schTweetModel);


				} while (cursor.moveToNext());
			}

			return allschTweets;

		}

	
	public void deleteThisPost(int schid) 
	{
		SQLiteDatabase database = this.getWritableDatabase();

		String query = "DELETE FROM " + sch_table_name + " WHERE " + KEY_SchTID
				+ " = " + schid;

		System.out.println(query);

		database.execSQL(query);

	}
	
	
	public void deleteThisSharePost(int schid) 
	{
		SQLiteDatabase database = this.getWritableDatabase();

		String query = "DELETE FROM " + share_table_name + " WHERE " + KEY_SchTID
				+ " = " + schid;

		System.out.println(query);

		database.execSQL(query);

	}
	
	
	public void deleteThisSharePages(int schid) 
	{
		SQLiteDatabase database = this.getWritableDatabase();

		String query = "DELETE FROM " + page_shareagon_table_name + " WHERE " + KEY_SchTID
				+ " = " + schid;

		System.out.println(query);

		database.execSQL(query);

	}

}
