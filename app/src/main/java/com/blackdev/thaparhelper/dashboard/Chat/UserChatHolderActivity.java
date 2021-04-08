package com.blackdev.thaparhelper.dashboard.Chat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.blackdev.thaparhelper.R;
import com.blackdev.thaparhelper.database.AppDatabase;
import com.blackdev.thaparhelper.database.ChatData;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class UserChatHolderActivity extends AppCompatActivity implements View.OnClickListener {

    AppDatabase database;
    Toolbar toolbar;
    RecyclerView recyclerView;
    TextView recipientName, recipientDept;
    EditText messageET;
    ImageButton sendButton;
    CircularImageView profilePic;
    String hisUID;

    ValueEventListener seenListener;
    DatabaseReference mRefForSeen;
    List<ChatData> chatList;
    OneToOneChatAdapter adapter;
    FirebaseUser firebaseUser;

    @Override
    protected void onPause() {
        super.onPause();
        if(mRefForSeen != null && seenListener!=null) {
            mRefForSeen.removeEventListener(seenListener);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        readMessages();
    }

    void init() {
        toolbar = findViewById(R.id.chatToolbar);
        recyclerView = findViewById(R.id.messagesRecyclerView);
        recipientName = findViewById(R.id.recipientName);
        recipientDept = findViewById(R.id.recipientDept);
        messageET = findViewById(R.id.messageEditText);
        sendButton = findViewById(R.id.sendMessageButton);
        profilePic = findViewById(R.id.profilePicRecipient);
        database = AppDatabase.getInstance(getApplicationContext());
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_chat_holder);
        init();
        setSupportActionBar(toolbar);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(linearLayoutManager);

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
        updateMessages();
    }

    private void seenMessages() {
        mRefForSeen = FirebaseDatabase.getInstance().getReference("Messages").child(firebaseUser.getUid()).child("ReceivedMessages").child(hisUID);
        seenListener = mRefForSeen.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ds: snapshot.getChildren()) {
                    ChatData data = ds.getValue(ChatData.class);
                    if (data != null) {
                        if (data.getToUID().equals(firebaseUser.getUid()) && data.getFromUID().equals(hisUID)) {
                            HashMap<String, Object> hashMap = new HashMap<>();
                            hashMap.put("isSeen", true);
                            ds.getRef().updateChildren(hashMap);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void readMessages() {
        chatList = new ArrayList<>();
        final DatabaseReference mRef = FirebaseDatabase.getInstance().getReference("Messages").child(firebaseUser.getUid()).child("ReceivedMessages").child(hisUID);
        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ds: snapshot.getChildren()) {
                    ChatData chatData = ds.getValue(ChatData.class);
                    chatData.setSentByMe(false);
                    Log.w("Updated","message updated now");
                    database.chatDataDao().insert(chatData);
                    updateMessages();
                    seenMessages();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    private void updateMessages() {
        chatList = database.chatDataDao().getMessagesOfUser(hisUID);
        adapter = new OneToOneChatAdapter(this,chatList);
        adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter);
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
        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference().child("Messages").child(hisUID).child("ReceivedMessages").child(firebaseUser.getUid());
        ChatData data = new ChatData(firebaseUser.getUid(),true,hisUID,message,false,"",false,time);
        database.chatDataDao().insert(data);
        dbRef.push().setValue(data);
        messageET.setText("");
        updateMessages();
    }
}