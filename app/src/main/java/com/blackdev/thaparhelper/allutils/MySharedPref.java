package com.blackdev.thaparhelper.allutils;

import android.content.Context;
import android.content.SharedPreferences;

import com.blackdev.thaparhelper.UserFacultyModelClass;
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

    public UserFacultyModelClass getUserF() {
        UserFacultyModelClass user = new UserFacultyModelClass();
        user.setName(myPref.getString("userName",""));
        user.setMobNumber(myPref.getString("userMobile",""));
        user.setEmail(myPref.getString("userEmail",""));
        user.setDepartment(myPref.getString("userDept",""));
        user.setDesignation(myPref.getString("userDesignation",""));
        user.setUserType(myPref.getInt("userSubType",2));
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

    public void saveUser(UserFacultyModelClass user) {
        SharedPreferences.Editor editor = myPref.edit();
        editor.putString("userName",user.getName());
        editor.putString("userMobile",user.getMobNumber());
        editor.putString("userEmail",user.getEmail());
        editor.putString("userImageLink",user.getProfileImageLink());
        editor.putString("userUid",user.getUid());
        editor.putString("userDept",user.getDepartment());
        editor.putString("userDesignation",user.getDesignation());
        editor.putInt("userSubType",user.getUserType());
        editor.apply();
    }

    UserPersonalData user;

    public void saveUserType(int type) {
        SharedPreferences.Editor editor = myPref.edit();
        editor.putInt("userType",type);
        editor.apply();
    }

    public int getUserType() {
        return myPref.getInt("userType",1);
    }

    SharedPreferences myPref;
    public MySharedPref(Context context, String PREFERENCES,int type){
        switch (type) {
            case Constants.DATA_SHARED_PREF:
                myPref = context.getSharedPreferences(PREFERENCES,Context.MODE_PRIVATE);
                break;
            case Constants.TYPE_SHARED_PREF:
                myPref = context.getSharedPreferences(PREFERENCES+"_type",Context.MODE_PRIVATE);
                break;
            case Constants.TOKEN_SHARED_PREF:
                myPref = context.getSharedPreferences(PREFERENCES+"_token",Context.MODE_PRIVATE);
                break;
        }
    }

    public void saveToken(String token) {
        SharedPreferences.Editor editor = myPref.edit();
        editor.putString("Token",token);
        editor.apply();
    }

    public String getToken() {
       return myPref.getString("Token","");
    }

}
