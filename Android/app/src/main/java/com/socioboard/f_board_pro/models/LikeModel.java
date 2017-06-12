package com.socioboard.f_board_pro.models;

//model class for handling likes particullerly

public class LikeModel
{
	String userId=null;
	String feedId=null;
	String userName=null;
	public String getUserId()
	{
		return userId;
	}
	public void setUserId(String userId) 
	{
		this.userId = userId;
	}
	public String getFeedId()
	{
		return feedId;
	}
	public void setFeedId(String feedId)
	{
		this.feedId = feedId;
	}
	public String getUserName()
	{
		return userName;
	}
	public void setUserName(String userName) 
	{
		this.userName = userName;
	}
	

}
