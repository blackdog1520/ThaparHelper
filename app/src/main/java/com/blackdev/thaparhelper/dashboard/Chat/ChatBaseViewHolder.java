package com.blackdev.thaparhelper.dashboard.Chat;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public abstract class ChatBaseViewHolder extends RecyclerView.ViewHolder {
    private int mCurrentPosition;
    public ChatBaseViewHolder(@NonNull View itemView) {
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

