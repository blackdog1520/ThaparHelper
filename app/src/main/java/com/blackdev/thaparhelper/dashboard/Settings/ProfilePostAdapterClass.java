package com.blackdev.thaparhelper.dashboard.Settings;

import android.content.Context;
import android.media.Image;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.blackdev.thaparhelper.R;
import com.blackdev.thaparhelper.dashboard.Explore.Models.ModelPost;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

public class ProfilePostAdapterClass extends RecyclerView.Adapter<ProfilePostAdapterClass.ViewHolder> {

    private Context mContext;
    private List<ModelPost> postList;

    public ProfilePostAdapterClass(Context mContext, List<ModelPost> postList){
        this.mContext = mContext;
        this.postList = postList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.profile_posts_display_layout,parent,false);
        return new ProfilePostAdapterClass.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ModelPost post = postList.get(position);
        Glide.with(mContext).load(post.getPostImage()).apply(RequestOptions.centerCropTransform()).into(holder.postImageView);
    }

    @Override
    public int getItemCount() {
        return postList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        ImageView postImageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            postImageView = itemView.findViewById(R.id.row_user_post);

        }
    }


}