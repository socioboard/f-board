package com.socioboard.f_board_pro.models;

 
//model class for handling home feeds

public class HomeFeedModel
{
	String FeedId=null;
	String profilePic=null;
	String fromID=null;
	String from=null;
	String dateTime=null;
	String message=null;
	String picture=null;
	String description=null;
	int likes=0;
	int comments=0;
	
	
	
	
	public String getFeedId() {
		return FeedId;
	}
	public void setFeedId(String feedId) {
		FeedId = feedId;
	}
	public String getProfilePic() 
	{
		return profilePic;
	}
	public void setProfilePic(String profilePic)
	{
		this.profilePic = profilePic;
	}
	public String getFromID()
	{
		return fromID;
	}
	public void setFromID(String fromID)
	{
		this.fromID = fromID;
	}
	public String getFrom()
	{
		return from;
	}
	public void setFrom(String from)
	{
		this.from = from;
	}
	public String getDateTime()
	{
		return dateTime;
	}
	public void setDateTime(String dateTime)
	{
		this.dateTime = dateTime;
	}
	public String getMessage()
	{
		return message;
	}
	public void setMessage(String message)
	{
		this.message = message;
	}
	public String getPicture()
	{
		return picture;
	}
	public void setPicture(String picture)
	{
		this.picture = picture;
	}
	public String getDescription()
	{
		return description;
	}
	public void setDescription(String description)
	{
		this.description = description;
	}
	public int getLikes() 
	{
		return likes;
	}
	public void setLikes(int likes)
	{
		this.likes = likes;
	}
	public int getComments()
	{
		return comments;
	}
	public void setComments(int comments)
	{
		this.comments = comments;
	}
	
	
	
}
