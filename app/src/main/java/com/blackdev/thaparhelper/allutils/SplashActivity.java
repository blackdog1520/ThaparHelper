package com.blackdev.thaparhelper.allutils;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.blackdev.thaparhelper.MainActivity;
import com.blackdev.thaparhelper.R;
import com.blackdev.thaparhelper.UserFacultyModelClass;
import com.blackdev.thaparhelper.UserPersonalData;
import com.blackdev.thaparhelper.dashboard.DashBoardActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SplashActivity extends AppCompatActivity {

    FirebaseUser user;
    DatabaseReference userRef;
    long start,end;

    @Override
    protected void onStart() {
        if(user!=null){
            fetchDetails();
        } else {
            sendToMainActivity();
        }
        super.onStart();
    }

    private void fetchDetails() {
        final int userType = Utils.getCurrentUserType(this,user.getUid());
        userRef = Utils.getRefForBasicData(userType,user.getUid());
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()) {
                    MySharedPref sharedPref = new MySharedPref(SplashActivity.this, Utils.getStringPref(user.getUid()), userType);
                    if(userType == Constants.USER_STUDENT) {
                        final UserPersonalData data = snapshot.getValue(UserPersonalData.class);
                        sharedPref.saveUser(data);
                    } else {
                        final UserFacultyModelClass data = snapshot.getValue(UserFacultyModelClass.class);
                        sharedPref.saveUser(data);
                    }
                    updateUI();
                } else {
                    FirebaseAuth.getInstance().signOut();
                    sendToMainActivity();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void sendToMainActivity() {
        end = System.currentTimeMillis() - start;
        end = end>4000?0:4000-end;
        Intent intent = new Intent(this, MainActivity.class);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(intent);
                finish();
            }
        },end);
    }

    private void updateUI() {
        end = System.currentTimeMillis() - start;
        end = end>4000?0:4000-end;
        Intent intent = new Intent(this, DashBoardActivity.class);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(intent);
                finish();
            }
        },end);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        start = System.currentTimeMillis();
        user = FirebaseAuth.getInstance().getCurrentUser();

    }
}