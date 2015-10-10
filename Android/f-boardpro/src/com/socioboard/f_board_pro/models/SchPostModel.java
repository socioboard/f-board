package com.socioboard.f_board_pro.models;

import java.util.Random;

public class SchPostModel {

	String  userID;

	long feedtime;

	int feedId;

	String feedImagePath;

	String feedText;
	
	int interval;


	public int getInterval() {
		return interval;
	}


	public void setInterval(int interval) {
		this.interval = interval;
	}


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

}
