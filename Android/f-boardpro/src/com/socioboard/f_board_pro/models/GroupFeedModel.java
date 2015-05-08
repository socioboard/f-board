package com.socioboard.f_board_pro.models;

//model class for group feeds
public class GroupFeedModel 
{
	String message=null;
	String from=null;
	String picture=null;
	String dateTime=null;
	String fromID=null;
	String profilePic=null;
	String FeedId=null;
	int likes=0;
	int comments=0;
	
	public String getFromID()
	{
		return fromID;
	}

	public void setFromID(String fromID)
	{
		this.fromID = fromID;
	}

	public String getProfilePic() 
	{
		return profilePic;
	}

	public void setProfilePic(String profilePic) {
		this.profilePic = profilePic;
	}

	
	public String getFeedID()
	{
		return FeedId;
	}
	public void setFeedID(String feedID)
	{
		this.FeedId = feedID;
	}
	public int getLikes()
	{
		return likes;
	}
	public void setLikes(int likes)
	{
		this.likes = likes;
	}
	public int getComments() {
		return comments;
	}
	public void setComments(int comments) {
		this.comments = comments;
	}
	public String getMessage()
	{
		return message;
	}
	public void setMessage(String message)
	{
		this.message = message;
	}
	public String getFrom() 
	{
		return from;
	}
	public void setFrom(String from) 
	{
		this.from = from;
	}
	
	public String getPicture() 
	{
		return picture;
	}
	public void setPicture(String picture)
	{
		this.picture = picture;
	}
	public String getDateTime()
	{
		return dateTime;
	}
	public void setDateTime(String dateTime)
	{
		this.dateTime = dateTime;
	}
	
	

}
