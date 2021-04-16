package com.blackdev.thaparhelper.dashboard;

import com.blackdev.thaparhelper.dashboard.Chat.ChatFragment;
import com.blackdev.thaparhelper.dashboard.Explore.ExploreFragment;
import com.blackdev.thaparhelper.dashboard.dashboardFrag.DashboardFragment;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

public class ViewPagerDashBoardAdapter extends FragmentStatePagerAdapter {

    public ViewPagerDashBoardAdapter(@NonNull FragmentManager fm) {
        super(fm,FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch(position) {
            case 0:
                return new DashboardFragment();
            case 1:
                return new ExploreFragment();
            case 2:
                return new MapsFragment();
            case 3:
                return new ChatFragment();
            case 4:
                return new SettingsFragment();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 5;
    }
}
