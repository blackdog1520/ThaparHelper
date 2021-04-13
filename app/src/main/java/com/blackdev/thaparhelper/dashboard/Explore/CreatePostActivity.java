package com.blackdev.thaparhelper.dashboard.Explore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toolbar;

import com.blackdev.thaparhelper.LoginActivity;
import com.blackdev.thaparhelper.MainActivity;
import com.blackdev.thaparhelper.R;
import com.blackdev.thaparhelper.allutils.Utils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

public class CreatePostActivity extends AppCompatActivity{

    RecyclerView recyclerView;
    ImageView currImage;
    String currImageName = "";String currImagePath = "";
    ArrayList<ImageModel> imageList;
    GridLayoutManager manager;
    LinearLayout rootLayout;

    Toolbar toolbar;
    void init() {
        recyclerView = findViewById(R.id.galleryViewRecyclerView);
        currImage = findViewById(R.id.currImageView);
        imageList = new ArrayList<>();
        rootLayout = findViewById(R.id.create_post_root_layout);
        manager = new GridLayoutManager(this, 3);
    }

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_post);
        init();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Create");
        if(!Utils.verifyStoragePermission(this)) {
            finish();
        }
        recyclerView.setLayoutManager(manager);
        if(imageList.isEmpty()) {
            addAllImages();
            currImagePath = imageList.get(0).getImagePath();
            currImageName = imageList.get(0).getImageName();
            Glide.with(getApplicationContext())
                    .load(imageList.get(0).imagePath)
                    .apply(RequestOptions.centerCropTransform())
                    .apply(RequestOptions.encodeQualityOf(80))
                    .into(currImage);
            GalleryViewAdapter adapter = new GalleryViewAdapter(this, imageList, new ImageClickListener() {
                @Override
                public void setOnItemClick(int position, ImageModel data) {
                    currImagePath = data.getImagePath();
                    currImageName = data.getImageName();
                    Glide.with(getApplicationContext())
                            .load(data.getImagePath())
                            .apply(RequestOptions.centerCropTransform())
                            .apply(RequestOptions.encodeQualityOf(60))
                            .into(currImage);
                }
            });
            recyclerView.setAdapter(adapter);
        }
        //recyclerView.setHasFixedSize(true);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_next:
                if(currImagePath.isEmpty()) {
                    Snackbar.make(rootLayout,"Please select a photo", BaseTransientBottomBar.LENGTH_SHORT).show();
                } else {
                    Intent intent = EditPostActivity.getIntent(this,currImagePath,currImageName);
                    startActivity(intent);
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_post_menu,menu);
        return true;
    }

    private void addAllImages() {

        Uri allImageUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        String[] projection = {MediaStore.Images.ImageColumns.DATA, MediaStore.Images.Media.DISPLAY_NAME};
        Cursor cursor = this.getContentResolver().query(allImageUri, projection, null, null, null);

        try {
            cursor.moveToFirst();
            while(cursor.moveToNext()) {
                ImageModel data = new ImageModel();
                data.imagePath = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA));
                data.imageName = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME));
                imageList.add(data);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}