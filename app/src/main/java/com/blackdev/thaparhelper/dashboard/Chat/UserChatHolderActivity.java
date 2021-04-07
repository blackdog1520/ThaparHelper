package com.blackdev.thaparhelper.dashboard.Chat;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.blackdev.thaparhelper.R;
import com.blackdev.thaparhelper.database.AppDatabase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;

import java.sql.Timestamp;
import java.util.HashMap;

public class UserChatHolderActivity extends AppCompatActivity implements View.OnClickListener {

    AppDatabase database;
    Toolbar toolbar;
    RecyclerView recyclerView;
    TextView recipientName, recipientDept;
    EditText messageET;
    ImageButton sendButton;
    CircularImageView profilePic;
    String hisUID;


    void init() {
        toolbar = findViewById(R.id.chatToolbar);
        recyclerView = findViewById(R.id.messagesRecyclerView);
        recipientName = findViewById(R.id.recipientName);
        recipientDept = findViewById(R.id.recipientDept);
        messageET = findViewById(R.id.messageEditText);
        sendButton = findViewById(R.id.sendMessageButton);
        profilePic = findViewById(R.id.profilePicRecipient);
        database = AppDatabase.getInstance(getApplicationContext());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_chat_holder);
        init();
        setSupportActionBar(toolbar);
        toolbar.setTitle("");
        hisUID = getIntent().getStringExtra("HisUID");
        recipientName.setText(getIntent().getStringExtra("HisName"));
        recipientDept.setText(getIntent().getStringExtra("HisDept"));
        try {
            Picasso.get().load(getIntent().getStringExtra("HisProfile"))
                    .into(profilePic);
        } catch (Exception e) {
            e.printStackTrace();
        }
        sendButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.sendMessageButton:
                if(!messageET.getText().toString().trim().isEmpty()) {
                    sendMessage(messageET.getText().toString().trim());
                }
        }
    }

    private void sendMessage(String message) {
        Long ts = System.currentTimeMillis()/1000;
        String time = ts.toString();
        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference().child("Messages").child(hisUID).child("RecievedMessages");
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("TimeStamp",time);
        hashMap.put("Sender",FirebaseAuth.getInstance().getUid());
        hashMap.put("Message", message);

        dbRef.push().setValue(hashMap);
        messageET.setText("");
    }
}