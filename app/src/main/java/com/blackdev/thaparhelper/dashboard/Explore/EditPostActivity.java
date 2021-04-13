package com.blackdev.thaparhelper.dashboard.Explore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import com.blackdev.thaparhelper.R;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

public class EditPostActivity extends AppCompatActivity {

    String finalPath;
    String finalName;
    ConstraintLayout rootLayout;
    public static Intent getIntent(Context context, String path, String fileName) {
        Intent intent = new Intent(context, EditPostActivity.class);
        intent.putExtra("filePath",path);
        intent.putExtra("fileName",fileName);
        return intent;
    }

    void init() {
        rootLayout = findViewById(R.id.rootLayoutEditPost);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_post);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Edit");
        init();
        finalPath = getIntent().getStringExtra("filePath");
        finalName = getIntent().getStringExtra("finalName");

        // add edit image options. For now moving directly to nextActivity;



    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_next:
                if(finalPath.isEmpty()) {
                    Snackbar.make(rootLayout,"Please select a photo", BaseTransientBottomBar.LENGTH_SHORT).show();
                } else {
                    Intent intent = AddPostDetailsActivity.getIntent(this, finalPath, finalName);
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
}