package com.blackdev.thaparhelper.dashboard.Explore;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.blackdev.thaparhelper.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class GalleryViewAdapter extends RecyclerView.Adapter<GalleryViewAdapter.ImageHolder> {
    Context context;
    ArrayList<ImageModel> list;
    ImageClickListener clickListener;
    View prevView;
    

    public GalleryViewAdapter(Context context, ArrayList<ImageModel> list, ImageClickListener onClickListener) {
        this.context = context;
        this.list = list;
        clickListener = onClickListener;
    }

    @NonNull
    @Override
    public ImageHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view  = LayoutInflater.from(context).inflate(R.layout.row_gallery_image,parent,false);

        return new ImageHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ImageHolder holder, final int position) {
        final ImageModel data = list.get(position);
        Glide.with(context)
                .load(data.getImagePath())
                .apply(RequestOptions.centerCropTransform())
                .into(holder.imageView);
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(prevView == null) {
                    view.setAlpha(1f);
                    view.animate()
                            .alpha(0.3f)
                            .setDuration(400)
                            .setListener(null);
                } else if(prevView == view) {


                } else {
                    view.setAlpha(1f);
                    view.animate()
                            .alpha(0.3f)
                            .setDuration(400)
                            .setListener(null);

//Out transition: (alpha from 0.5 to 0)
                    prevView.setAlpha(0.3f);
                    prevView.animate()
                            .alpha(1f)
                            .setDuration(300)
                            .setListener(null);
                }
                prevView = view;

                clickListener.setOnItemClick(position,data);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ImageHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        public ImageHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.row_image_view);
        }
    }

}
