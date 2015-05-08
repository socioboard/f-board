package com.socioboard.f_board_pro.models;

// model class for handling comments
public class CommentModel
{
	String name=null;
	String comment=null;
	String dateTime=null;
	String fromID=null;
	String profilePic=null;
	
	public String getProfilePic()
	{
		return profilePic;
	}
	public void setProfilePic(String profilePic)
	{
		this.profilePic = profilePic;
	}
	public String getFromID() {
		return fromID;
	}
	public void setFromID(String fromID) 
	{
		this.fromID = fromID;
	}
	public String getDateTime()
	{
		return dateTime;
	}
	public void setDateTime(String dateTime) 
	{
		this.dateTime = dateTime;
	}
	public String getName()
	{
		return name;
	}
	public void setName(String name)
	{
		this.name = name;
	}
	public String getComment()
	{
		return comment;
	}
	public void setComment(String comment)
	{
		this.comment = comment;
	}
	

}
