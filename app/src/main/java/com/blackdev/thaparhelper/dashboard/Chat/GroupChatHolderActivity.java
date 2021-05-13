package com.blackdev.thaparhelper.dashboard.Chat;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.blackdev.thaparhelper.LoginActivity;
import com.blackdev.thaparhelper.R;
import com.blackdev.thaparhelper.UserFacultyModelClass;
import com.blackdev.thaparhelper.allutils.Constants;
import com.blackdev.thaparhelper.allutils.Utils;
import com.blackdev.thaparhelper.dashboard.Chat.CreateAssignmentActivity;
import com.blackdev.thaparhelper.dashboard.Chat.Models.ModelGroupChat;
import com.blackdev.thaparhelper.dashboard.Chat.adapter.GroupChatAdapter;
import com.blackdev.thaparhelper.dashboard.Chat.adapter.OneToOneChatAdapter;
import com.blackdev.thaparhelper.database.AppDatabase;
import com.blackdev.thaparhelper.database.ChatData;
import com.blackdev.thaparhelper.database.ChatDataDao;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class GroupChatHolderActivity extends AppCompatActivity implements View.OnClickListener {


    Toolbar toolbar;
    RecyclerView recyclerView;
    TextView recipientName;
    EditText messageET;
    ImageButton sendButton,bottomSheetView;
    CircularImageView profilePic;
    BottomSheetDialog bottomSheetDialog;
    String groupId,groupUrl,groupName;
    ChatDataDao chatDataDao;
    DatabaseReference mRef;
    final String senderUid = FirebaseAuth.getInstance().getUid();
    String senderName;

    GroupChatAdapter adapter;
    ArrayList<HashMap<String, Object>> list = new ArrayList<>();
    private static final int sendButtonID = R.id.sendGroupMessageButton;
    private static final int showBottomSheetID = R.id.showGroupBottomSheet;

    void init() {
        toolbar = findViewById(R.id.groupChatToolbar);
        recyclerView = findViewById(R.id.groupMessagesRecyclerView);
        recipientName = findViewById(R.id.groupName);
        messageET = findViewById(R.id.groupMessageEditText);
        sendButton = findViewById(R.id.sendGroupMessageButton);
        bottomSheetView = findViewById(R.id.showGroupBottomSheet);
        profilePic = findViewById(R.id.groupProfileImageView);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_chat_holder);
        chatDataDao = AppDatabase.getInstance(this).chatDataDao();
        init();
        setSupportActionBar(toolbar);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new GroupChatAdapter(list,this, groupId);
        recyclerView.setAdapter(adapter);
        int userType;
        switch (Utils.getCurrentUserType(GroupChatHolderActivity.this,senderUid)) {
            case Constants.USER_ADMINISTRATION:
                senderName = Utils.getCurrentUserDataF(GroupChatHolderActivity.this, senderUid).getName();
                if(senderName.equals("")) {
                    fetchNameFromDatabase(Constants.USER_ADMINISTRATION);
                }
                break;
            case Constants.USER_FACULTY:
                senderName = Utils.getCurrentUserDataF(GroupChatHolderActivity.this, senderUid).getName();
                if(senderName.equals("")) {
                    fetchNameFromDatabase(Constants.USER_FACULTY);
                }
                break;
            case Constants.USER_STUDENT:
                senderName = Utils.getCurrentUserData(GroupChatHolderActivity.this, senderUid).getName();
                if(senderName.equals("")) {
                    fetchNameFromDatabase(Constants.USER_STUDENT);
                }
                break;
        }

        toolbar.setTitle("");
        groupId = getIntent().getStringExtra("GroupId");
        groupUrl = getIntent().getStringExtra("GroupProfile");
        groupName = getIntent().getStringExtra("GroupName");
        mRef = Utils.getRefForGroup(groupId);
        recipientName.setText(groupName);
        try {
            Picasso.get().load(groupUrl)
                    .into(profilePic);
        } catch (Exception e) {
            e.printStackTrace();
        }
        sendButton.setOnClickListener(this);
        bottomSheetView.setOnClickListener(this);
        updateMessages();
    }

    private void fetchNameFromDatabase(int type) {
        final DatabaseReference ref = Utils.getRefForBasicData(type,senderUid);
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()) {
                    senderName = snapshot.child("name").getValue(String.class);
                } else {
                    FirebaseAuth.getInstance().signOut();
                    startActivity(new Intent(GroupChatHolderActivity.this, LoginActivity.class));
                    finish();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void updateMessages() {
        //mRef
        mRef.child(getString(R.string.new_messages)).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()) {
                    list.clear();
                    for(DataSnapshot dataSnap: snapshot.getChildren()) {
                        Log.i("Chat",dataSnap.child(getString(R.string.MessageType)).getValue()+"");
                        switch(dataSnap.child(getString(R.string.MessageType)).getValue(Integer.class)) {
                            case Constants.NORMAL_MESSAGE:
                                final HashMap<String, Object> hm = new HashMap<>();
                                hm.put(getString(R.string.SenderUid),dataSnap.child(getString(R.string.SenderUid)).getValue(String.class));
                                hm.put(getString(R.string.MessageType),Constants.NORMAL_MESSAGE);
                                hm.put(getString(R.string.Data),dataSnap.child(getString(R.string.Data)).getValue(ModelGroupChat.class));
                                list.add(hm);
                                break;
                            case Constants.ASSIGNMENT_MESSAGE:
                                //ModelGroupChat modelGroupChat = dataSnap.child(getString(R.string.Data)).getValue(ModelGroupChat.class);
                                final HashMap<String, Object> hm2 = new HashMap<>();
                                hm2.put(getString(R.string.SenderUid),dataSnap.child(getString(R.string.SenderUid)).getValue(String.class));
                                hm2.put(getString(R.string.MessageType),Constants.ASSIGNMENT_MESSAGE);
                                hm2.put(getString(R.string.Data),dataSnap.child(getString(R.string.Data)).getValue());
                                list.add(hm2);
                                break;
//                            case Constants.IMAGE_MESSAGE:
//                                ModelGroupChat modelGroupChat = dataSnap.child(getString(R.string.Data)).getValue(ModelGroupChat.class);
//                                final HashMap<String, Object> hm = new HashMap<>();
//                                hm.put(getString(R.string.SenderUid),dataSnap.child(getString(R.string.SenderUid)));
//                                hm.put(getString(R.string.MessageType),Constants.NORMAL_MESSAGE);
//                                hm.put(getString(R.string.Data),modelGroupChat);
//                                list.add(hm);
//                                break;
//                            case Constants.DOC_MESSAGE:
//                                ModelGroupChat modelGroupChat = dataSnap.child(getString(R.string.Data)).getValue(ModelGroupChat.class);
//                                final HashMap<String, Object> hm = new HashMap<>();
//                                hm.put(getString(R.string.SenderUid),dataSnap.child(getString(R.string.SenderUid)));
//                                hm.put(getString(R.string.MessageType),Constants.NORMAL_MESSAGE);
//                                hm.put(getString(R.string.Data),modelGroupChat);
//                                list.add(hm);
//                                break;
                        }


//                      Currently we can't store the data in offline database. Need better mechanism to do so.   So group chat will be stored on server
                    }
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case sendButtonID:
                if(!messageET.getText().toString().trim().isEmpty()) {
                    String message = messageET.getText().toString().trim();
                    sendMessage(message);
                    //getToken(message);
                }
                break;
            case showBottomSheetID:
                bottomSheetDialog = new BottomSheetDialog(GroupChatHolderActivity.this,R.style.BottomSheetTheme);
                View sheetView = LayoutInflater.from(GroupChatHolderActivity.this).inflate(R.layout.chat_bottom_sheet,null);
                sheetView.findViewById(R.id.send_image_button).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(getApplicationContext(),"Image",Toast.LENGTH_SHORT).show();
                    }
                });

                sheetView.findViewById(R.id.send_assignment_button).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(GroupChatHolderActivity.this, CreateAssignmentActivity.class);
                        startActivityForResult(intent, Constants.ASSIGNMENT_REQUEST_CODE);
                        Toast.makeText(getApplicationContext(),"Assignment",Toast.LENGTH_SHORT).show();
                    }
                });

                sheetView.findViewById(R.id.send_document_button).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(getApplicationContext(),"Doc",Toast.LENGTH_SHORT).show();
                    }
                });

                bottomSheetDialog.setContentView(sheetView);
                bottomSheetDialog.show();

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case Constants.ASSIGNMENT_REQUEST_CODE:
                if(data!=null) {
                    sendAssignment(data);
                }

                break;
            case Constants.IMAGE_REQUEST_CODE:
        }
    }

    private void sendAssignment(Intent data) {
        HashMap<String, Object> data1 = new HashMap<>();
        data1.put(getString(R.string.subjectName),data.getStringExtra(getString(R.string.subjectName)));
        data1.put(getString(R.string.assignmentTopic),data.getStringExtra(getString(R.string.assignmentTopic)));
        data1.put(getString(R.string.assignmentDesc),data.getStringExtra(getString(R.string.assignmentDesc)));
        data1.put(getString(R.string.assignmentDeadline),data.getStringExtra(getString(R.string.assignmentDeadline)));
        data1.put(getString(R.string.submissionLink),data.getStringExtra(getString(R.string.submissionLink)));
        data1.put(getString(R.string.fileLink),data.getStringExtra(getString(R.string.fileLink)));
        data1.put(getString(R.string.senderName),senderName);
        HashMap<String, Object> hm = new HashMap<>();
        hm.put(getString(R.string.MessageType),Constants.ASSIGNMENT_MESSAGE);
        hm.put(getString(R.string.SenderUid),senderUid);
        hm.put(getString(R.string.Data),data1);
        mRef.child(getString(R.string.new_messages)).child(System.currentTimeMillis() + senderUid).setValue(hm);
        messageET.setText("");
    }

    private void sendMessage(String message) {
        ModelGroupChat data = new ModelGroupChat();
        data.setSenderUid(senderUid);
        data.setTimeStamp(""+System.currentTimeMillis());
        data.setMessage(message);
        data.setSeenCount(1);
        data.setMessageType(Constants.NORMAL_MESSAGE);
        data.setSenderName(senderName);
        HashMap<String, Object> hm = new HashMap<>();
        hm.put(getString(R.string.MessageType),Constants.NORMAL_MESSAGE);
        hm.put(getString(R.string.SenderUid),senderUid);
        hm.put(getString(R.string.Data),data);
        mRef.child(getString(R.string.new_messages)).child(System.currentTimeMillis() + senderUid).setValue(hm);
        messageET.setText("");
        //updateMessages();


    }
}