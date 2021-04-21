package com.blackdev.thaparhelper;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.blackdev.thaparhelper.allutils.Constants;
import com.blackdev.thaparhelper.allutils.MySharedPref;
import com.blackdev.thaparhelper.allutils.Utils;
import com.blackdev.thaparhelper.dashboard.Explore.Models.ModelPost;
import com.blackdev.thaparhelper.dashboard.Settings.ProfilePostAdapterClass;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ShowUserProfile extends AppCompatActivity {
    private DatabaseReference mRef;

    private TextView userName, userSpecificDetail, emailId;
    private LottieAnimationView coverPic;
    private CircularImageView profilePic;
    private String hisUid;
    ScrollView rootLayout;
    private int userType;
    private RecyclerView recyclerView;
    private ProfilePostAdapterClass adapterClass;
    private List<ModelPost> postList;



    private void init() {
        //set this ref based on the type of user signing in
        userName = findViewById(R.id.hisProfileuserName);
        userSpecificDetail = findViewById(R.id.hisProfileExtras);
        emailId = findViewById(R.id.hisProfileMail);
        profilePic = findViewById(R.id.hisProfileImage);
        coverPic = findViewById(R.id.showUserTypeLottie);
        recyclerView = findViewById(R.id.hisPostsRecyclerView);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new GridLayoutManager(this,3);
        recyclerView.setLayoutManager(linearLayoutManager);
        postList = new ArrayList<>();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_user_profile);
        init();
        hisUid = getIntent().getStringExtra("hisUid");
        userType = getIntent().getIntExtra("userType",1);
        mRef = Utils.getRefForBasicData(userType,hisUid);
        fetchAndSetData();
    }




    private void fetchAndSetData() {
        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot == null) {
                    Log.e("Settings",hisUid);
                    Snackbar.make(rootLayout, "Something went wrong!", Snackbar.LENGTH_SHORT).show();
                    // logout user
                } else {
                    Log.e("HisProfileVal", "" + snapshot.getChildrenCount());
                    //for(DataSnapshot dp: snapshot.getChildren()) {
                    switch (userType) {
                        case Constants.USER_ADMINISTRATION:
                            coverPic.setAnimation(R.raw.administration);
                            UserFacultyModelClass data = snapshot.getValue(UserFacultyModelClass.class);
                            Log.e("SETTINGSVal", snapshot.child("email").getValue().toString());
                            emailId.setText(data.getEmail());
                            userName.setText(data.getName());
                            userSpecificDetail.setText(data.getDepartment());
                            if (data.getProfileImageLink() != null && !data.getProfileImageLink().isEmpty()) {
                                Picasso.get().load(data.getProfileImageLink()).into(profilePic);
                            }
                            break;
                        case Constants.USER_STUDENT:
                            coverPic.setAnimation(R.raw.student);
                            UserPersonalData data1 = snapshot.getValue(UserPersonalData.class);
                            Log.e("SETTINGSVal", snapshot.child("email").getValue().toString());
                            emailId.setText(data1.getEmail());
                            userName.setText(data1.getName());
                            userSpecificDetail.setText(data1.getRollNumber());
                            if (data1.getProfileImageLink() != null && !data1.getProfileImageLink().isEmpty()) {
                                Picasso.get().load(data1.getProfileImageLink()).into(profilePic);
                            }
                            break;
                        case Constants.USER_FACULTY:
                            coverPic.setAnimation(R.raw.faculty);
                            UserFacultyModelClass data2 = snapshot.getValue(UserFacultyModelClass.class);
                            Log.e("SETTINGSVal", snapshot.child("email").getValue().toString());
                            emailId.setText(data2.getEmail());
                            userName.setText(data2.getName());
                            userSpecificDetail.setText(data2.getDepartment());
                            if (data2.getProfileImageLink() != null && !data2.getProfileImageLink().isEmpty()) {
                                Picasso.get().load(data2.getProfileImageLink()).into(profilePic);
                            }
                            break;
                    }

                    fetchPostData();
                    // set it to offline database;
                    //}
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Snackbar.make(rootLayout, "Error fetching info! Please try again.", Snackbar.LENGTH_SHORT).show();
            }
        });
    }


    private void fetchPostData() {
        mRef.child("UserPost").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                postList.clear();
                for (DataSnapshot snapshot1 : snapshot.getChildren()){
                    ModelPost post = snapshot1.getValue(ModelPost.class);
                    postList.add(post);
                }
                Collections.reverse(postList);
                adapterClass = new ProfilePostAdapterClass(ShowUserProfile.this,postList);
                recyclerView.setAdapter(adapterClass);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}