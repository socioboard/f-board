package com.socioboard.f_board_pro.models;

import java.util.Random;

public class SchPostModel {

	String  userID;

	long feedtime;

	int feedId;

	String feedImagePath;

	String feedText;
	
	int interval;

	int total_count;


	String group_name;


	public SchPostModel() {
	}


	//for post data scheduler
	public SchPostModel(String userID,  String feedText, String feedImagePath, long feedtime) {

		this.userID   = userID;
		this.feedtime = feedtime;

		this.feedId = new Random().nextInt();

		if (this.feedId < 0) {

			this.feedId = -this.feedId;
		}

		this.feedImagePath = feedImagePath;

		this.feedText =feedText;

	}
//for post data scheduler
	public SchPostModel(int feedId , String userID,  String feedText, String feedImagePath, long feedtime)
	{	
		this.feedId = feedId;
		this.userID = userID;
		this.feedText =feedText;
		this.feedImagePath = feedImagePath;
		this.feedtime = feedtime;		
	}

	
	//for share-link scheduler
	public SchPostModel(int feedId,  String userID, String feedText, long feedtime, int interval)
	{
		this.feedId = feedId;
		this.userID = userID;
		this.feedText =feedText;
		this.feedtime = feedtime;
		this.interval = interval;
	}
    //for share-link schedular
	public SchPostModel(String userID,  String feedText, long feedtime, int interval) {

		this.userID   = userID;
		this.feedtime = feedtime;

		this.feedId = new Random().nextInt();

		if (this.feedId < 0) {

			this.feedId = -this.feedId;
		}

		this.feedText =feedText;
		this.interval = interval;

	}



	//this is used for ShareagonPage Module to share link
	public SchPostModel(int feedId,  String userID, String feedText, long feedtime, int interval, int total_count)
	{
		this.feedId = feedId;
		this.userID = userID;
		this.feedText =feedText;
		this.feedtime = feedtime;
		this.interval = interval;
		this.total_count = total_count;
	}




	//this is using for Shareagon Page module.
	public SchPostModel(String userID,  String feedText, long feedtime, int interval, int total_count) {

		this.userID   = userID;
		this.feedtime = feedtime;

		this.feedId = new Random().nextInt();

		if (this.feedId < 0) {

			this.feedId = -this.feedId;
		}

		this.feedText =feedText;
		this.interval = interval;

		this.total_count = total_count;

	}

	//this is using for Shareagon Group module.
	public SchPostModel(String userID,  String feedText, long feedtime, int interval, int total_count, String group_name) {

		this.userID   = userID;
		this.feedtime = feedtime;

		this.feedId = new Random().nextInt();

		if (this.feedId < 0) {

			this.feedId = -this.feedId;
		}

		this.feedText =feedText;
		this.interval = interval;

		this.total_count = total_count;
		this.group_name = group_name;

	}


	//this is used for ShareagonGroup Module to share link
	public SchPostModel(int feedId,  String userID, String feedText, long feedtime, int interval, int total_count, String group_name)
	{
		this.feedId = feedId;
		this.userID = userID;
		this.feedText =feedText;
		this.feedtime = feedtime;
		this.interval = interval;
		this.total_count = total_count;
		this.group_name = group_name;
	}
	

	public String getUserID() {
		return userID;
	}


	public void setUserID(String userID) {
		this.userID = userID;
	}


	public long getFeedtime() {
		return feedtime;
	}


	public void setFeedtime(long feedtime) {
		this.feedtime = feedtime;
	}


	public int getFeedId() {
		return feedId;
	}


	public void setFeedId(int feedId) {
		this.feedId = feedId;
	}


	public String getFeedImagePath() {
		return feedImagePath;
	}


	public void setFeedImagePath(String feedImagePath) {
		this.feedImagePath = feedImagePath;
	}


	public String getFeedText() {
		return feedText;
	}


	public void setFeedText(String feedText) {
		this.feedText = feedText;
	}

	public int getTotal_count() {
		return total_count;
	}

	public void setTotal_count(int total_count) {
		this.total_count = total_count;
	}

	public String getGroup_name() {
		return group_name;
	}

	public void setGroup_name(String group_name) {
		this.group_name = group_name;
	}

	public int getInterval() {
		return interval;
	}


	public void setInterval(int interval) {
		this.interval = interval;
	}


}
