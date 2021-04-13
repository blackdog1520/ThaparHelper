package com.blackdev.thaparhelper.dashboard.Explore;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.blackdev.thaparhelper.R;

public class AddPostDetailsActivity extends AppCompatActivity {

    public static Intent getIntent(Context context, String path, String fileName) {
        Intent intent = new Intent(context, EditPostActivity.class);
        intent.putExtra("filePath",path);
        intent.putExtra("fileName",fileName);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_post_details);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Edit");


    }
}