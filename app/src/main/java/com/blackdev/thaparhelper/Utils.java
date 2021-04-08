package com.blackdev.thaparhelper;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.blackdev.thaparhelper.dashboard.Chat.ModelChatOneToOne;
import com.blackdev.thaparhelper.database.ChatData;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

public class Utils {

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





}
