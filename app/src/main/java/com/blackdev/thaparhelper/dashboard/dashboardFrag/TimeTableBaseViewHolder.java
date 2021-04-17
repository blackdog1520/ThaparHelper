package com.blackdev.thaparhelper.dashboard.dashboardFrag;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public abstract class TimeTableBaseViewHolder extends RecyclerView.ViewHolder {
    private int mCurrentPosition;
    public TimeTableBaseViewHolder(@NonNull View itemView) {
        super(itemView);
    }
    protected abstract void clear();
    public void onBind(int position) {
        mCurrentPosition = position;
        clear();
    }
    public int getCurrentPosition() {
        return mCurrentPosition;
    }
}