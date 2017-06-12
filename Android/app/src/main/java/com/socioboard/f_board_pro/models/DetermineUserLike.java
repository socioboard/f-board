package com.socioboard.f_board_pro.models;
//model class for handling likes
public class DetermineUserLike
{
	String feedId=null;
	boolean like=false;
	
	public String getFeedId() 
	{
		return feedId;
	}
	public void setFeedId(String feedId)
	{
		this.feedId = feedId;
	}
	public boolean isLike()
	{
		return like;
	}
	public void setLike(boolean like)
	{
		this.like = like;
	}
	
}
