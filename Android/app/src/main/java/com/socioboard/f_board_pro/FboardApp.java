package com.socioboard.f_board_pro;


import android.app.Application;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.util.Base64;
import android.util.Log;

import com.facebook.FacebookSdk;
import com.facebook.LoggingBehavior;
import com.facebook.appevents.AppEventsLogger;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class FboardApp extends Application{
    public void onCreate() {
        super.onCreate();
        printHashKey();
        AppEventsLogger.activateApp(this);
        AppEventsLogger logger = AppEventsLogger.newLogger(this);
        Log.e("Logger: ",logger.toString());
        FacebookSdk.addLoggingBehavior(LoggingBehavior.REQUESTS);
    }
    public void printHashKey()
    {
        try {
            PackageInfo packageInfo=getPackageManager().getPackageInfo("com.socioboard.f_board_pro", PackageManager.GET_SIGNATURES);
            for (Signature signature:packageInfo.signatures)
            {
                MessageDigest md=MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.e("KeyHash: "," "+ Base64.encodeToString(md.digest(),Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException|NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }
}
