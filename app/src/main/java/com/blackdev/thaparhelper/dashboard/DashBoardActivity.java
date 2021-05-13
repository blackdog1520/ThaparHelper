package com.blackdev.thaparhelper.dashboard;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.blackdev.thaparhelper.MainActivity;
import com.blackdev.thaparhelper.LoginActivity;
import com.blackdev.thaparhelper.R;
import com.blackdev.thaparhelper.allutils.Utils;
import com.blackdev.thaparhelper.dashboard.Chat.ChatFragment;
import com.blackdev.thaparhelper.dashboard.Explore.CreatePostActivity;
import com.blackdev.thaparhelper.dashboard.Explore.ExploreFragment;
import com.blackdev.thaparhelper.dashboard.Settings.SettingsFragment;
import com.blackdev.thaparhelper.dashboard.dashboardFrag.DashboardFragment;
import com.blackdev.thaparhelper.database.AppDatabase;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;


public class DashBoardActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    private static final int action_dashboard = R.id.action_dashboard;
    private static final int action_explore = R.id.action_explore;
    private static final int action_map = R.id.action_map;
    private static final int action_chat = R.id.action_chat;
    private static final int action_settings = R.id.action_settings;

    final ExploreFragment exploreFragment = new ExploreFragment();
    final DashboardFragment dashboardFragment = new DashboardFragment();
    final SettingsFragment settingsFragment = new SettingsFragment();
    final MapsFragment mapsFragment = new MapsFragment();
    final ChatFragment chatFragment = new ChatFragment();
    BottomNavigationView bottomNavigationView;
    FrameLayout layout;
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
        layout = findViewById(R.id.dashboard_layout);
        mAuth = FirebaseAuth.getInstance();
        database = AppDatabase.getInstance(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board);
        init();
        bottomNavigationView.inflateMenu(R.menu.dashboard_menu);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        loadFragment(new DashboardFragment());
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

    private boolean loadFragment(Fragment fragment) {
        if( fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.dashboard_layout,fragment)
                    .commit();
            return true;
        }
        return false;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Fragment fragment = null;
        switch (item.getItemId()) {
            case action_dashboard:
                fragment = dashboardFragment;
                break;
            case action_explore:
                fragment = exploreFragment;
                break;
            case action_map:
                fragment = mapsFragment;
                break;
            case action_chat:
                fragment = chatFragment;
                break;
            case action_settings:
                fragment = settingsFragment;
                break;
        }
        return loadFragment(fragment);
    }

}