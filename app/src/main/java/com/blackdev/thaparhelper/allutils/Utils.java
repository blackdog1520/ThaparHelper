package com.blackdev.thaparhelper.allutils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.blackdev.thaparhelper.allutils.Constants;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
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

    public static boolean checkUserLoggedIn() {
        if(FirebaseAuth.getInstance() == null) {
            return false;
        } else {
            return true;
        }
    }

    public static String getPostPath(String timestamp, String uid) {
        String path = "Posts/posts_id_" + FirebaseAuth.getInstance().getUid() + "_time_" + timestamp;
        return path;
    }

    public static DatabaseReference getRefForPosts(String uid) {
        DatabaseReference mRef = FirebaseDatabase.getInstance().getReference("Posts");
        return mRef;
    }

    public static String getStringPref(String uid) {
        return "user-"+uid;
    }

    public static int getCurrentUserType(Context context, String uid) {
        MySharedPref mySharedPref = new MySharedPref(context,getStringPref(uid),Constants.TYPE_SHARED_PREF);
        return mySharedPref.getUserType();
    }

    public static LocalDate getNextDay(int day, LocalDate ld,int subType) {
        Log.i("GetNextDay","RECEIVED:"+day);
        switch (day+1) {
            case 1:
                Log.i("GetNextDayCase","1");
                switch (subType) {
                    case Constants.SAME_DAY_SEARCH:
                        LocalDate nextOrSame = ld.with( TemporalAdjusters.nextOrSame( DayOfWeek.MONDAY ) ) ;
                        return nextOrSame;
                     case Constants.NEXT_DAY_SEARCH:
                         LocalDate next = ld.with( TemporalAdjusters.next( DayOfWeek.MONDAY ) ) ;
                         return next;
                }

                break;
            case 2:
                Log.i("GetNextDayCase","2");
                switch (subType) {
                    case Constants.SAME_DAY_SEARCH:
                        LocalDate nextOrSame = ld.with( TemporalAdjusters.nextOrSame( DayOfWeek.TUESDAY ) ) ;
                        return nextOrSame;
                    case Constants.NEXT_DAY_SEARCH:
                        LocalDate next = ld.with( TemporalAdjusters.next( DayOfWeek.TUESDAY ) ) ;
                        return next;
                }
                break;
            case 3 :
                Log.i("GetNextDayCase","3");
                switch (subType) {
                    case Constants.SAME_DAY_SEARCH:
                        LocalDate nextOrSame = ld.with( TemporalAdjusters.nextOrSame( DayOfWeek.WEDNESDAY ) ) ;
                        return nextOrSame;
                    case Constants.NEXT_DAY_SEARCH:
                        LocalDate next = ld.with( TemporalAdjusters.next( DayOfWeek.WEDNESDAY ) ) ;
                        return next;
                }
                break;
            case 4 :
                Log.i("GetNextDayCase","4");
                switch (subType) {
                    case Constants.SAME_DAY_SEARCH:
                        LocalDate nextOrSame = ld.with( TemporalAdjusters.nextOrSame( DayOfWeek.THURSDAY) ) ;
                        return nextOrSame;
                    case Constants.NEXT_DAY_SEARCH:
                        LocalDate next = ld.with( TemporalAdjusters.next( DayOfWeek.THURSDAY ) ) ;
                        return next;
                }
                break;
            case 5 :
                Log.i("GetNextDayCase","5");
                switch (subType) {
                    case Constants.SAME_DAY_SEARCH:
                        LocalDate nextOrSame = ld.with( TemporalAdjusters.nextOrSame( DayOfWeek.FRIDAY ) ) ;
                        return nextOrSame;
                    case Constants.NEXT_DAY_SEARCH:
                        LocalDate next = ld.with( TemporalAdjusters.next( DayOfWeek.FRIDAY ) ) ;
                        return next;
                }
                break;
            case 6:
                Log.i("GetNextDayCase","6");
                switch (subType) {
                    case Constants.SAME_DAY_SEARCH:
                        LocalDate nextOrSame = ld.with( TemporalAdjusters.nextOrSame( DayOfWeek.SATURDAY ) ) ;
                        return nextOrSame;
                    case Constants.NEXT_DAY_SEARCH:
                        LocalDate next = ld.with( TemporalAdjusters.next( DayOfWeek.SATURDAY ) ) ;
                        return next;
                }
                break;

        }
        return null;
    }
}
