package com.socioboard.f_board_pro;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by GLB-122 on 5/23/2017.
 */

public class MyFirebaseInstanceIdService extends FirebaseInstanceIdService
{
    private static final String TAG = "MyFirebaseIIDService";
    public String tokenid;

    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();

        //Getting Registration Token
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();

        //Displaying token on logcat
        Log.d(TAG, "Refreshed token=======: " + refreshedToken);

        System.out.println("Firebase Token===="+refreshedToken);
        sendRegistrationToServer(refreshedToken);
    }

    public void sendRegistrationToServer(String token)
    {
        tokenid = token;
    }
}
