package com.niikuc.googleloginaddword;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by Николче on 09.5.2018.
 */

public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {
    private static final String TAG="MyFirebaseInsIDService";

    @Override
    public void onTokenRefresh() {
        String refreshedToken=FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG,"New Token: "+ refreshedToken);


    }
}
