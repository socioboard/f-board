package com.socioboard.f_board_pro.models;

/**
 * Created by GLB-122 on 5/23/2017.
 */

public class FirebaseModel
{


    boolean notificationStatus;
    String userId; //current user id  -----1
    String userName; //username           -----2
    String accessToken; //current user access token   -----3
    String deviceId;
    String getFirebaseToken;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public boolean isNotificationStatus() {
        return notificationStatus;
    }

    public void setNotificationStatus(boolean notificationStatus) {
        this.notificationStatus = notificationStatus;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getGetFirebaseToken() {
        return getFirebaseToken;
    }

    public void setGetFirebaseToken(String getFirebaseToken) {
        this.getFirebaseToken = getFirebaseToken;
    }



    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }




    public FirebaseModel(boolean notificationStatus, String userId, String userName, String accessToken, String deviceId, String getFirebase) {
        this.notificationStatus = notificationStatus;
        this.userId = userId;
        this.userName = userName;
        this.accessToken = accessToken;
        this.deviceId = deviceId;
        this.getFirebaseToken = getFirebase;
    }


}
