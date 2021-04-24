package com.blackdev.thaparhelper.dashboard.Chat;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.ItemTouchHelper;
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

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.blackdev.thaparhelper.allutils.Constants;
import com.blackdev.thaparhelper.allutils.MySharedPref;
import com.blackdev.thaparhelper.R;
import com.blackdev.thaparhelper.UserPersonalData;
import com.blackdev.thaparhelper.allutils.Utils;
import com.blackdev.thaparhelper.dashboard.Chat.adapter.OneToOneChatAdapter;
import com.blackdev.thaparhelper.database.AppDatabase;
import com.blackdev.thaparhelper.database.ChatData;
import com.blackdev.thaparhelper.database.RecentChatData;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserChatHolderActivity extends AppCompatActivity implements View.OnClickListener {

    AppDatabase database;
    Toolbar toolbar;
    RecyclerView recyclerView;
    TextView recipientName, recipientDept;
    EditText messageET;
    ImageButton sendButton,bottomSheetView;
    CircularImageView profilePic;
    BottomSheetDialog bottomSheetDialog;
    String hisUID,hisUrl,hisName;
    int hisType;
    private static final int sendButtonID = R.id.sendMessageButton;
    private static final int showBottomSheetID = R.id.showBottomSheet;

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
        messageET = findViewById(R.id.messageEditText);
        sendButton = findViewById(R.id.sendMessageButton);
        bottomSheetView = findViewById(R.id.showBottomSheet);
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
        hisUrl = getIntent().getStringExtra("HisProfile");
        hisName = getIntent().getStringExtra("HisName");
        recipientName.setText(hisName);
        hisType = getIntent().getIntExtra("HisType",1);
        try {
            Picasso.get().load(hisUrl)
                    .into(profilePic);
        } catch (Exception e) {
            e.printStackTrace();
        }
        sendButton.setOnClickListener(this);
        bottomSheetView.setOnClickListener(this);
        updateMessages();


        MessageSwipeController messageSwipeController = new MessageSwipeController(this, new SwipeControllerActions() {
            @Override
            public void showReplyUI(int position) {
                showInLog(chatList.get(position));
            }
        });
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(messageSwipeController);
        itemTouchHelper.attachToRecyclerView(recyclerView);

    }

    private void showInLog(ChatData chatData) {
        Log.i("SWIPED MESSAGE","TEST- "+chatData.getMessage());
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
                            hashMap.put("seen", true);
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
                    if(chatData !=null && chatData.getToUID() != null && chatData.getFromUID()!=null) {
                        chatData.setSentByMe(false);
                        RecentChatData data = new RecentChatData(Constants.ONE_TO_ONE_TYPE,chatData.getToUName(),chatData.getDpUrl(),"","",chatData.getFromUID(),chatData.getToUType());
                        AppDatabase.getInstance(UserChatHolderActivity.this).recentChatDao().insert(data);
                        database.chatDataDao().insert(chatData);
                        if (chatData.isSeen()) {
                            database.chatDataDao().updateMessage(true, hisUID, chatData.getTimeStamp());
                        }
                        updateMessages();
                        seenMessages();
                    }
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
            case sendButtonID:
                if(!messageET.getText().toString().trim().isEmpty()) {
                    String message = messageET.getText().toString().trim();
                    sendMessage(message);
                    getToken(message);
                }
                break;
            case showBottomSheetID:
                bottomSheetDialog = new BottomSheetDialog(UserChatHolderActivity.this,R.style.BottomSheetTheme);
                View sheetView = LayoutInflater.from(UserChatHolderActivity.this).inflate(R.layout.chat_bottom_sheet,null);
                sheetView.findViewById(R.id.send_image_button).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(getApplicationContext(),"Image",Toast.LENGTH_SHORT).show();
                    }
                });

                sheetView.findViewById(R.id.send_assignment_button).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(UserChatHolderActivity.this, CreateAssignmentActivity.class);
                        startActivityForResult(intent,Constants.ASSIGNMENT_REQUEST_CODE);
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
    }

    private void sendMessage(String message) {
        Long ts = System.currentTimeMillis()/1000;
        String time = ts.toString();
        final DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference().child("Messages").child(hisUID).child("ReceivedMessages").child(firebaseUser.getUid());
        ChatData chatData = new ChatData(firebaseUser.getUid(),true,hisUID,message,false,"",hisUrl,hisName,hisType,false,time);
        database.chatDataDao().insert(chatData);
        dbRef.push().setValue(chatData);
        RecentChatData data = new RecentChatData(Constants.ONE_TO_ONE_TYPE,chatData.getToUName(),chatData.getDpUrl(),"","",chatData.getFromUID(),chatData.getToUType());
        AppDatabase.getInstance(UserChatHolderActivity.this).recentChatDao().insert(data);
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Log.w("Updated","message updated now");
                for(DataSnapshot ds: snapshot.getChildren()) {
                    ChatData chatData = ds.getValue(ChatData.class);
                    if (chatData.isSeen()) {
                        Log.i("Seen","message seen now"+chatData.getMessage());
                        ds.getRef().removeValue();
                        database.chatDataDao().insert(chatData);
                        database.chatDataDao().updateMessage(true, hisUID, chatData.getTimeStamp());
                        adapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        messageET.setText("");
        updateMessages();
    }

    private void getToken(final String message) {
        MySharedPref sharedPref = new MySharedPref(this, Utils.getStringPref(FirebaseAuth.getInstance().getUid()),Constants.DATA_SHARED_PREF);
        final UserPersonalData myData =  sharedPref.getUser();
        DatabaseReference mRef = Utils.getRefForBasicData(hisType,hisUID);
        mRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String token = snapshot.child("token").getValue().toString();
                String profileLink = snapshot.child("profileImageLink").getValue().toString();

                JSONObject to =  new JSONObject();
                JSONObject data = new JSONObject();

                try {
                    data.put("title",myData.getName());
                    data.put("message",message);
                    data.put("hisUID",firebaseUser.getUid());
                    data.put("hisDept",myData.getBatch());
                    data.put("hisProfile",myData.getProfileImageLink());
                    data.put("hisType",Utils.getCurrentUserType(getApplicationContext(),myData.getUid()));

                    to.put("to",token);
                    to.put("data", data);
                    sendNotification(to);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void sendNotification(JSONObject to) {

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST,Constants.NOTIFICATION_URL,to, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("notification","sendNotification: "+response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("notification","sendNotification: "+error);
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("Authorization","Key="+Constants.CLOUD_SERVER_KEY);
                map.put("Content-Type","application/json");
                return map;
            }

            @Override
            public String getBodyContentType() {
                return "application/json";
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        request.setRetryPolicy(new DefaultRetryPolicy(30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(request);
    }


}