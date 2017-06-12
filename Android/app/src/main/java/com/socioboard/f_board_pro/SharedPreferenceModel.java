package com.socioboard.f_board_pro;


import java.util.ArrayList;

public class SharedPreferenceModel {
   // public  String FacebookSharedPrefrence="FacebookBoard";
    public  String FacebookUserId;
    public  String FacebookUserName;
    public  String FacebookFirstName;
    public  String FacebookShortImagePath;
    public  String FacebookLargeImagePath;
    public  String FacebookEmailId;
    public  String FacebookGender;
    public  String FacebookDOB;
    public  String FacebookAccessToken;
    public  String FacebookCoverImagePath;
    public  String FacebookUserCurrentLocation;
    public  String FacebookUserHomeTown;

    public  String getFacebookUserWorkLocation() {
        return FacebookUserWorkLocation;
    }

    public  void setFacebookUserWorkLocation(String facebookUserWorkLocation) {
        FacebookUserWorkLocation = facebookUserWorkLocation;
    }

    public  String getFacebookUserHomeTown() {
        return FacebookUserHomeTown;
    }

    public  void setFacebookUserHomeTown(String facebookUserHomeTown) {
        FacebookUserHomeTown = facebookUserHomeTown;
    }

    public  String getFacebookUserCurrentLocation() {
        return FacebookUserCurrentLocation;
    }

    public  void setFacebookUserCurrentLocation(String facebookUserCurrentLocation) {
        FacebookUserCurrentLocation = facebookUserCurrentLocation;
    }

    public  String FacebookUserWorkLocation;

    public  String getFacebookUserId() {
        return FacebookUserId;
    }

    public  void setFacebookUserId(String facebookUserId) {
        FacebookUserId = facebookUserId;
    }

    public  String getFacebookUserName() {
        return FacebookUserName;
    }

    public  void setFacebookUserName(String facebookUserName) {
        FacebookUserName = facebookUserName;
    }

    public  String getFacebookFirstName() {
        return FacebookFirstName;
    }

    public  void setFacebookFirstName(String facebookFirstName) {
        FacebookFirstName = facebookFirstName;
    }

    public  String getFacebookShortImagePath() {
        return FacebookShortImagePath;
    }

    public  void setFacebookShortImagePath(String facebookShortImagePath) {
        FacebookShortImagePath = facebookShortImagePath;
    }

    public  String getFacebookLargeImagePath() {
        return FacebookLargeImagePath;
    }

    public  void setFacebookLargeImagePath(String facebookLargeImagePath) {
        FacebookLargeImagePath = facebookLargeImagePath;
    }

    public  String getFacebookEmailId() {
        return FacebookEmailId;
    }

    public  void setFacebookEmailId(String facebookEmailId) {
        FacebookEmailId = facebookEmailId;
    }

    public  String getFacebookGender() {
        return FacebookGender;
    }

    public  void setFacebookGender(String facebookGender) {
        FacebookGender = facebookGender;
    }

    public  String getFacebookDOB() {
        return FacebookDOB;
    }

    public  void setFacebookDOB(String facebookDOB) {
        FacebookDOB = facebookDOB;
    }

    public  String getFacebookAccessToken() {
        return FacebookAccessToken;
    }

    public  void setFacebookAccessToken(String facebookAccessToken) {
        FacebookAccessToken = facebookAccessToken;
    }

    public  String getFacebookCoverImagePath() {
        return FacebookCoverImagePath;
    }

    public  void setFacebookCoverImagePath(String facebookCoverImagePath) {
        FacebookCoverImagePath = facebookCoverImagePath;
    }
    public static ArrayList<SharedPreferenceModel> sharedPreferenceModelsArrayList=new ArrayList<>();
}
