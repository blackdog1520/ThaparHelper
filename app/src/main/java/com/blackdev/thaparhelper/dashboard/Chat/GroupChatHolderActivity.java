package com.blackdev.thaparhelper.dashboard.Chat;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.blackdev.thaparhelper.R;
import com.blackdev.thaparhelper.UserFacultyModelClass;
import com.blackdev.thaparhelper.allutils.Constants;
import com.blackdev.thaparhelper.allutils.Utils;
import com.blackdev.thaparhelper.dashboard.Chat.Models.ModelGroupChat;
import com.blackdev.thaparhelper.dashboard.Chat.adapter.OneToOneChatAdapter;
import com.blackdev.thaparhelper.database.AppDatabase;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

public class GroupChatHolderActivity extends AppCompatActivity implements View.OnClickListener {


    Toolbar toolbar;
    RecyclerView recyclerView;
    TextView recipientName;
    EditText messageET;
    ImageButton sendButton,bottomSheetView;
    CircularImageView profilePic;
    BottomSheetDialog bottomSheetDialog;
    String groupId,groupUrl,groupName;
    DatabaseReference mRef;
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
        init();
        setSupportActionBar(toolbar);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(linearLayoutManager);

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

    private void updateMessages() {
        //mRef

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
                    //sendAssignment(data);
                }

                break;
            case Constants.IMAGE_REQUEST_CODE:
        }
    }

    private void sendMessage(String message) {
        final String senderUid = FirebaseAuth.getInstance().getUid();
        ModelGroupChat data = new ModelGroupChat();
        data.setSenderUid(senderUid);
        data.setTimeStamp(""+System.currentTimeMillis());
        data.setMessage(message);
        data.setSeenCount(1);
        data.setMessageType(Constants.NORMAL_MESSAGE);
        switch (Utils.getCurrentUserType(GroupChatHolderActivity.this,senderUid)) {
            case Constants.USER_ADMINISTRATION:
            case Constants.USER_FACULTY:
                data.setSenderName(Utils.getCurrentUserDataF(GroupChatHolderActivity.this, senderUid).getName());
                break;
            case Constants.USER_STUDENT:
                data.setSenderName(Utils.getCurrentUserData(GroupChatHolderActivity.this, senderUid).getName());
                break;
        }
        HashMap<String, Object> hm = new HashMap<>();
        hm.put(getString(R.string.MessageType),Constants.NORMAL_MESSAGE);
        hm.put(getString(R.string.SenderUid),senderUid);
        hm.put(getString(R.string.Data),data);
        mRef.child(getString(R.string.new_messages)).setValue(hm);
        messageET.setText("");
        //updateMessages();


    }
}