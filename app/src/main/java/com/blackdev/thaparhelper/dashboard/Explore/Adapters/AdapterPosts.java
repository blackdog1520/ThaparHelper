package com.blackdev.thaparhelper.dashboard.Explore.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.blackdev.thaparhelper.R;
import com.blackdev.thaparhelper.ShowUserProfile;
import com.blackdev.thaparhelper.dashboard.Explore.Models.ModelPost;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class AdapterPosts  extends RecyclerView.Adapter<PostBaseViewHolder> {

    Context context;
    ArrayList<ModelPost> list;


    public AdapterPosts(Context context, ArrayList<ModelPost> list) {
        this.context = context;
        this.list = list;
    }

    public class NormalPostHolder extends PostBaseViewHolder implements View.OnClickListener {

        CircularImageView dp;
        ImageView postImageView;
        TextView userNameTV,locationTV,descriptionTV,likesTV,timeTV;
        ImageButton likeButton,commentButton,additionalSettings;


        public NormalPostHolder(@NonNull View itemView) {
            super(itemView);
            dp = itemView.findViewById(R.id.row_post_user_profile);
            postImageView = itemView.findViewById(R.id.row_post_image_view);
            userNameTV = itemView.findViewById(R.id.row_post_user_name);
            locationTV = itemView.findViewById(R.id.row_post_location);
            descriptionTV = itemView.findViewById(R.id.row_post_description);
            likesTV = itemView.findViewById(R.id.row_post_likes);
            timeTV = itemView.findViewById(R.id.row_post_time);
            likeButton = itemView.findViewById(R.id.row_post_like_button);
            commentButton = itemView.findViewById(R.id.row_post_comment_button);
            additionalSettings = itemView.findViewById(R.id.row_post_additional_settings);
        }

        @Override
        protected void clear() {

        }

        @Override
        public void onBind(final int position) {
            super.onBind(position);
            final ModelPost post = list.get(position);
            try {
                Glide.with(context)
                        .load(post.getuDp())
                        .into(dp);
            } catch (Exception e) {
                e.printStackTrace();
            }
            userNameTV.setText(post.getuName());
            locationTV.setText(post.getPostLocation());
            descriptionTV.setText(post.getPostDesc());
            String likesS = "Liked by "+post.getLikes();
            likesTV.setText(likesS);
            SimpleDateFormat formatter = new SimpleDateFormat("EEE, MMM d, ''yy", Locale.ENGLISH);
            String dateString = formatter.format(new Date(Long.parseLong(post.getPostTime())));
            formatter = new SimpleDateFormat("h:mm a"  , Locale.ENGLISH);
            dateString = dateString + " at " + formatter.format(new Date(Long.parseLong(post.getPostTime())));
            timeTV.setText(dateString);
            try {
                Glide.with(context)
                        .load(post.getPostImage())
                        .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                        .into(postImageView);
            } catch (Exception e) {
                e.printStackTrace();
            }
            likeButton.setOnClickListener(this);
            commentButton.setOnClickListener(this);
            additionalSettings.setOnClickListener(this);
            userNameTV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, ShowUserProfile.class);
                    intent.putExtra("userType",post.getuType());
                    intent.putExtra("hisUid",post.getUid());
                    context.startActivity(intent);
                }
            });

        }

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.row_post_like_button:
                    if( likeButton.getDrawable() == context.getDrawable(R.drawable.ic_outline_favorite_border_24) ) {
                        likeButton.setImageDrawable(context.getDrawable(R.drawable.ic_baseline_favorite_24));
                    } else {
                        likeButton.setImageDrawable(context.getDrawable(R.drawable.ic_outline_favorite_border_24));
                    }
                    break;

            }
        }
    }

    @NonNull
    @Override
    public PostBaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_post_simple,parent,false);
        return new NormalPostHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostBaseViewHolder holder, int position) {
        holder.onBind(position);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }



}
