package com.blackdev.thaparhelper.dashboard;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import android.animation.ValueAnimator;
import android.graphics.Path;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.blackdev.thaparhelper.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.sdsmdg.harjot.vectormaster.VectorMasterView;
import com.sdsmdg.harjot.vectormaster.models.PathModel;

public class DashBoardActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener, ViewPager.OnPageChangeListener {

    private static final int action_dashboard = R.id.action_dashboard;
    private static final int action_explore = R.id.action_explore;
    private static final int action_map = R.id.action_map;
    private static final int action_chat = R.id.action_chat;
    private static final int action_settings = R.id.action_settings;
    BottomNavigationView bottomNavigationView;
    ViewPager viewPager;

    void init() {
        bottomNavigationView = findViewById(R.id.bottom_navigation_dash);
        viewPager = findViewById(R.id.dashboard_view_pager_view);
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
}