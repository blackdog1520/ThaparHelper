package com.blackdev.thaparhelper.dashboard;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.blackdev.thaparhelper.MainActivity;
import com.blackdev.thaparhelper.allutils.Constants;
import com.blackdev.thaparhelper.LoginActivity;
import com.blackdev.thaparhelper.R;
import com.blackdev.thaparhelper.allutils.Utils;
import com.blackdev.thaparhelper.dashboard.Explore.CreatePostActivity;
import com.blackdev.thaparhelper.database.AppDatabase;
import com.blackdev.thaparhelper.database.ChatData;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.HashMap;
import java.util.Map;

public class DashBoardActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener, ViewPager.OnPageChangeListener {

    private static final int action_dashboard = R.id.action_dashboard;
    private static final int action_explore = R.id.action_explore;
    private static final int action_map = R.id.action_map;
    private static final int action_chat = R.id.action_chat;
    private static final int action_settings = R.id.action_settings;
    BottomNavigationView bottomNavigationView;
    ViewPager viewPager;
    FirebaseAuth mAuth;
    AppDatabase database;

    @Override
    protected void onResume() {
        super.onResume();
        if(!Utils.checkUserLoggedIn()) {
            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        }
    }

    void init() {
        bottomNavigationView = findViewById(R.id.bottom_navigation_dash);
        viewPager = findViewById(R.id.dashboard_view_pager_view);
        mAuth = FirebaseAuth.getInstance();
        database = AppDatabase.getInstance(this);
        //readMessages();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board);
        init();
        bottomNavigationView.inflateMenu(R.menu.dashboard_menu);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        viewPager.setAdapter(new ViewPagerDashBoardAdapter(getSupportFragmentManager()));
        viewPager.addOnPageChangeListener(this);

        updateFirebaseToken();
    }

    private void updateFirebaseToken() {
        DatabaseReference databaseReference = Utils.getRefForBasicData(Utils.getCurrentUserType(this,mAuth.getUid()),mAuth.getUid());
        // choose path based on user type ;
        Map<String, Object> map= new HashMap<>();
        map.put("token", FirebaseMessaging.getInstance().getToken().toString());
        databaseReference.updateChildren(map);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.dashboard_toolbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_logout:
                mAuth.signOut();
                startActivity(new Intent(this,LoginActivity.class));
                finish();
                return true;
            case R.id.action_add_post:
                Intent intent = new Intent(this, CreatePostActivity.class);
                startActivity(intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case action_dashboard:
                viewPager.setCurrentItem(0,true);
                break;

            case action_explore:
                viewPager.setCurrentItem(1,true);
                break;
            case action_map:
                viewPager.setCurrentItem(2,true);
                break;
            case action_chat:
                viewPager.setCurrentItem(3,true);
                break;
            case action_settings:
                viewPager.setCurrentItem(4,true);
                break;
        }
        return true;
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
       // Log.e("POSITION: ",""+position);
    }

    @Override
    public void onPageSelected(int position) {
        switch (position) {
            case 0:
                bottomNavigationView.setSelectedItemId(action_dashboard);
                break;
            case 1:
                bottomNavigationView.setSelectedItemId(action_explore);
                break;
            case 2:
                bottomNavigationView.setSelectedItemId(action_map);
                break;
            case 3:
                bottomNavigationView.setSelectedItemId(action_chat);
                break;
            case 4:
                bottomNavigationView.setSelectedItemId(action_settings);
                break;
            default:
                bottomNavigationView.setSelectedItemId(action_dashboard);
                break;
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    private void readMessages() {

        final DatabaseReference mRef = FirebaseDatabase.getInstance().getReference("Messages").child(FirebaseAuth.getInstance().getUid()).child("ReceivedMessages");
        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ds: snapshot.getChildren()) {
                    for(DataSnapshot dd: ds.getChildren()) {
                        Log.e(dd.getRef().getKey()+" :t", (String) dd.child("fromUID").getValue());
                    }
                    Log.e("DASHBOARD",ds.getValue().toString());

//                    String fromUID = (String) ds.child("Sender").getValue();
//                    String ToUID = FirebaseAuth.getInstance().getUid();
//                    String Message = (String) ds.child("Message").getValue();
//                    boolean isMediaFile = (boolean) ds.child("ediaFile").getValue();
//                    String MediaUrl  ="";
//                    if(isMediaFile) {
//                        MediaUrl = (String) ds.child("MediaUrl").getValue();
//                    }
//                    boolean isSeen = (boolean) ds.child("IsSeen").getValue();
//                    String timeStamp = (String) ds.child("TimeStamp").getValue();
//                    ChatData chatData = new ChatData(fromUID,false,ToUID,Message,isMediaFile,MediaUrl,isSeen,timeStamp);
                    ChatData chatData = ds.getValue(ChatData.class);

                    database.chatDataDao().insert(chatData);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}