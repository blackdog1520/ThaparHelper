package com.blackdev.thaparhelper.dashboard.Explore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.blackdev.thaparhelper.MainActivity;
import com.blackdev.thaparhelper.R;
import com.blackdev.thaparhelper.UserPersonalData;
import com.blackdev.thaparhelper.allutils.Constants;
import com.blackdev.thaparhelper.allutils.MySharedPref;
import com.blackdev.thaparhelper.allutils.Utils;
import com.blackdev.thaparhelper.dashboard.DashBoardActivity;
import com.blackdev.thaparhelper.dashboard.Explore.Models.ModelPost;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;

public class AddPostDetailsActivity extends AppCompatActivity implements View.OnClickListener {

    public static Intent getIntent(Context context, String path, String fileName) {
        Intent intent = new Intent(context, AddPostDetailsActivity.class);
        intent.putExtra("filePath",path);
        intent.putExtra("fileName",fileName);
        return intent;
    }

    LinearLayout rootLayout;
    CircularImageView imageView;
    EditText descET, locationET;
    Button upload;
    String filePath = "";
    String fileName = "";
    String description = "";
    String location = "";

    void init() {
        imageView = findViewById(R.id.finalPostImageView);
        descET = findViewById(R.id.postDescriptionET);
        locationET = findViewById(R.id.locationET);
        upload = findViewById(R.id.uploadPostButton);
        filePath = getIntent().getStringExtra("filePath");
        fileName = getIntent().getStringExtra("fileName");
        rootLayout = findViewById(R.id.rootLayoutAddPostDetails);
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
        setContentView(R.layout.activity_add_post_details);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Post");
        init();

        try {
            Glide.with(this)
                    .load(filePath)
                    .apply(RequestOptions.centerCropTransform())
                    .into(imageView);
            upload.setOnClickListener(this);
        } catch (Exception e) {


                Snackbar.make(rootLayout,"Something went wrong. Try again!", BaseTransientBottomBar.LENGTH_SHORT).show();
            Log.e("ErrorPostDetails","ErrorLoadingGLide"+e.getMessage());
        }

    }

    @Override
    public void onClick(View view) {
        if( filePath.isEmpty() ) {
            Snackbar.make(rootLayout,"Something went wrong. Try again!", BaseTransientBottomBar.LENGTH_SHORT).show();
            Log.e("ErrorPostDetails","filePath is empty");
            return;
        }


        Uri imageUri;
            Log.e("ADDPOST","IMAGEUR2");
            imageUri = Uri.fromFile(new File(filePath));
        Log.e("ADDPOST","IMAGEURI: "+imageUri.toString());
        if( imageUri.equals("noImage") ) {
            Snackbar.make(rootLayout,"Something went wrong. Try again!", BaseTransientBottomBar.LENGTH_SHORT).show();
            Log.e("ErrorPostDetails","Image Uri Empty");
            return;
        }

        location = locationET.getText().toString();
        description = descET.getText().toString();

        // show loading symbol now
        final String timestamp = String.valueOf(System.currentTimeMillis());
        final String filePathAndName = Utils.getPostPath(timestamp,FirebaseAuth.getInstance().getUid());

        StorageReference storageReference = FirebaseStorage.getInstance().getReference().child(filePathAndName);
        int scaleDivider = 4;
        try {
            // 1. Convert uri to bitmap
            Bitmap fullBitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
            // 2. Get the downsized image content as a byte[]
            int scaleWidth = fullBitmap.getWidth() / scaleDivider;
            int scaleHeight = fullBitmap.getHeight() / scaleDivider;
            byte[] downsizedImageBytes =
                    getDownsizedImageBytes(fullBitmap, scaleWidth, scaleHeight);

        storageReference.putBytes(downsizedImageBytes)
                .addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        if(task.isSuccessful()) {
                            Task<Uri> uriTask = task.getResult().getStorage().getDownloadUrl();
                            while (!uriTask.isComplete()) {
                                if(uriTask.isComplete()) {
                                    postDataInDB(uriTask.getResult().toString(), timestamp, filePathAndName);
                                    break;
                                }
                            }

                        }
                        }
                    }
                )
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                       Log.i("Post","Success");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Snackbar.make(rootLayout,"Failed to upload post. Try again!", BaseTransientBottomBar.LENGTH_SHORT).show();
                    }
                });

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void postDataInDB(final String downloadUrl, final String timestamp, String postID) {
        UserPersonalData data = new MySharedPref(this, "User-"+FirebaseAuth.getInstance().getUid(),Constants.DATA_SHARED_PREF).getUser();

//        HashMap<Object, String> hashMap = new HashMap<>();
        final ModelPost modelPost = new ModelPost(postID,downloadUrl,description,location,timestamp,data.getUid(),data.getEmail(),data.getProfileImageLink(),0,data.getName());
//        hashMap.put("uid", data.getUid());
//        hashMap.put("uName", data.getName());
//        hashMap.put("uEmail",data.getEmail());
//        hashMap.put("uDp",data.getProfileImageLink());
//        hashMap.put("postId",postID);
//        hashMap.put("postDesc",description);
//        hashMap.put("postImage",downloadUrl);
//        hashMap.put("postLocation",location);
//        hashMap.put("postTime",timestamp);

        DatabaseReference mRef = Utils.getRefForPosts(data.getUid());
        mRef.child(timestamp+FirebaseAuth.getInstance().getUid()).setValue(modelPost)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.i("PostStatus:","Success");

                        addUserBasicData(timestamp, modelPost);

                        Intent intent = new Intent(AddPostDetailsActivity.this, DashBoardActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.i("PostStatus:","Failure: "+e.getMessage());
                    }
                });

    }

    private void addUserBasicData(String s, ModelPost modelPost) {
        DatabaseReference dbRef = Utils.getRefForBasicData(Utils.getCurrentUserType(AddPostDetailsActivity.this,FirebaseAuth.getInstance().getUid()),FirebaseAuth.getInstance().getUid());
        dbRef.child("UserPost").child(s).setValue(modelPost);
    }


    public byte[] getDownsizedImageBytes(Bitmap fullBitmap, int scaleWidth, int scaleHeight) throws IOException {

        Bitmap scaledBitmap = Bitmap.createScaledBitmap(fullBitmap, scaleWidth, scaleHeight, true);

        // 2. Instantiate the downsized image content as a byte[]
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] downsizedImageBytes = baos.toByteArray();

        return downsizedImageBytes;
    }
}