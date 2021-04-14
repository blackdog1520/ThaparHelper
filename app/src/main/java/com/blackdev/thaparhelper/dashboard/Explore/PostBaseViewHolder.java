package com.blackdev.thaparhelper.dashboard.Explore;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public abstract class PostBaseViewHolder extends RecyclerView.ViewHolder {
    private int mCurrentPosition;
    public PostBaseViewHolder(@NonNull View itemView) {
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
