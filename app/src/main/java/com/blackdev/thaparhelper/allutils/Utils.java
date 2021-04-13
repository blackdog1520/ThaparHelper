package com.blackdev.thaparhelper.allutils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.blackdev.thaparhelper.allutils.Constants;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class Utils {

    private static final int REQUESTCODE = 101;
    private static float density = 1f;
    ///////////////////////////////////////////////
    public static boolean isNetworkAvailable(Activity activity){
        if(activity == null){
            return false;
        }
        ConnectivityManager connectivityManager;
        NetworkInfo networkInfo = null;
        try{
            connectivityManager = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
            networkInfo = Objects.requireNonNull(connectivityManager).getActiveNetworkInfo();
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return networkInfo !=null;
    }

    //////////////////////////////////////////////

    public static int dp(float value, Context context) {
        if(density == 1f) {
            checkDisplaySize(context);
        }
        if(value == 0f) {
            return 0;
        } else {
            return (int) Math.ceil((density * value));
        }
    }
////////////////////////////////////////////////////
    private static void checkDisplaySize(Context context) {
     try {
         density = context.getResources().getDisplayMetrics().density;
     } catch (Exception e) {
         e.printStackTrace();
     }
    }

    public static DatabaseReference getRefForBasicData(int userType, String uid) {
        DatabaseReference mRef = null;
        switch (userType) {
            case Constants.USER_ADMINISTRATION:
                mRef = FirebaseDatabase.getInstance().getReference("Users").child("BasicData").child("Administration").child(uid);
                return mRef;
            case Constants.USER_FACULTY:
                mRef = FirebaseDatabase.getInstance().getReference("Users").child("BasicData").child("Faculty").child(uid);
                return mRef;
            case Constants.USER_STUDENT:
                mRef = FirebaseDatabase.getInstance().getReference("Users").child("BasicData").child("Student").child(uid);
                return mRef;
        }
        return mRef;
    }

    public static boolean verifyStoragePermission(Context context) {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE
            }, REQUESTCODE);
        }

        if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }
}