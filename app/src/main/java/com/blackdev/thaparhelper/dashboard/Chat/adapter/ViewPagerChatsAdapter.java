package com.blackdev.thaparhelper.dashboard.Chat.adapter;

import com.blackdev.thaparhelper.dashboard.Chat.AllChatsFragment;
import com.blackdev.thaparhelper.dashboard.Chat.AllContactsFragment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class ViewPagerChatsAdapter extends FragmentPagerAdapter {
    public ViewPagerChatsAdapter(@NonNull FragmentManager fm) {
        super(fm,FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch(position) {
            case 0:
                return new AllChatsFragment();
            case 1:
                return new AllContactsFragment();
            default:
                return null;
        }

    }

    @Override
    public int getCount() {
        return 2;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        String title = null;
        if (position == 0)
        {
            title = "Chats";
        }
        else if (position == 1)
        {
            title = "Users";
        }

        return title;
    }
}
