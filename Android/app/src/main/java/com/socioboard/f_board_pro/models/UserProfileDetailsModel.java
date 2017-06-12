package com.socioboard.f_board_pro.models;

import java.util.ArrayList;

public class UserProfileDetailsModel
{
	String userMail=null;
	String UserName=null;
	String UserProfilePic=null;
	String UserCoverPic=null;
	String UserLocation=null;
	String UserHomeTown=null;
	String UserBirthDate=null;
	String UserWork=null;
	String UserGender=null;
	String UserLorgeProfilePic=null;
	String UserId=null;
    String first_name=null;
    String User_token=null;

    public String getUser_token() {
        return User_token;
    }

    public void setUser_token(String user_token) {
        User_token = user_token;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getUserId() {
		return UserId;
	}

	public void setUserId(String userId) {
		UserId = userId;
	}

	public String getUserLorgeProfilePic() {
		return UserLorgeProfilePic;
	}

	public void setUserLorgeProfilePic(String userLorgeProfilePic) {
		UserLorgeProfilePic = userLorgeProfilePic;
	}


	
	
	public String getUserGender() 
	{
		return UserGender;
	}
	public void setUserGender(String userGender)
	{
		UserGender = userGender;
	}
	public String getUserMail()
	{
		return userMail;
	}
	public void setUserMail(String userMail)
	{
		this.userMail = userMail;
	}
	public String getUserName()
	{
		return UserName;
	}
	public void setUserName(String userName)
	{
		UserName = userName;
	}
	public String getUserProfilePic()
	{
		return UserProfilePic;
	}
	public void setUserProfilePic(String userProfilePic)
	{
		UserProfilePic = userProfilePic;
	}
	public String getUserCoverPic()
	{
		return UserCoverPic;
	}
	public void setUserCoverPic(String userCoverPic)
	{
		UserCoverPic = userCoverPic;
	}
	public String getUserLocation()
	{
		return UserLocation;
	}
	public void setUserLocation(String userLocation)
	{
		UserLocation = userLocation;
	}
	public String getUserHomeTown()
	{
		return UserHomeTown;
	}
	public void setUserHomeTown(String userHomeTown)
	{
		UserHomeTown = userHomeTown;
	}
	public String getUserBirthDate()
	{
		return UserBirthDate;
	}
	public void setUserBirthDate(String userBirthDate)
	{
		UserBirthDate = userBirthDate;
	}
	public String getUserWork()
	{
		return UserWork;
	}
	public void setUserWork(String userWork)
	{
		UserWork = userWork;
	}
	public static ArrayList<UserProfileDetailsModel>userProfileDetailsModelsArrayList=new ArrayList<>();
	
}
