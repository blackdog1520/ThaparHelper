package com.blackdev.thaparhelper.allutils;

import android.content.Context;
import android.content.SharedPreferences;

import com.blackdev.thaparhelper.UserPersonalData;

public class MySharedPref {
    public SharedPreferences getMyPref() {
        return myPref;
    }

    public UserPersonalData getUser() {
        UserPersonalData user = new UserPersonalData();
        user.setName(myPref.getString("userName",""));
        user.setMobNumber(myPref.getString("userMobile",""));
        user.setEmail(myPref.getString("userEmail",""));
        user.setRollNumber(myPref.getString("userRoll",""));
        user.setProfileImageLink(myPref.getString("userImageLink",""));
        user.setUid(myPref.getString("userUid",""));
        return user;
    }

    public void saveUser(UserPersonalData user) {
        SharedPreferences.Editor editor = myPref.edit();
        editor.putString("userName",user.getName());
        editor.putString("userMobile",user.getMobNumber());
        editor.putString("userRoll",user.getRollNumber());
        editor.putString("userEmail",user.getEmail());
        editor.putString("userImageLink",user.getProfileImageLink());
        editor.putString("userUid",user.getUid());
        editor.apply();
    }

    UserPersonalData user;
    SharedPreferences myPref;
    public MySharedPref(Context context, String PREFERENCES){
        myPref = context.getSharedPreferences(PREFERENCES,Context.MODE_PRIVATE);
    }


}