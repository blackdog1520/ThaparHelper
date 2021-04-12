package com.blackdev.thaparhelper.dashboard.Chat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

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
                    chatData.setSentByMe(false);
                    database.chatDataDao().insert(chatData);
                    if(chatData.isSeen()) {
                       // database.chatDataDao().updateMessage(true,hisUID,);
                    }
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
                    String message = messageET.getText().toString().trim();
                    sendMessage(message);
                    getToken(message);
                }
        }
    }

    private void sendMessage(String message) {
        Long ts = System.currentTimeMillis()/1000;
        String time = ts.toString();
        final DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference().child("Messages").child(hisUID).child("ReceivedMessages").child(firebaseUser.getUid());
        ChatData data = new ChatData(firebaseUser.getUid(),true,hisUID,message,false,"",false,time);
        database.chatDataDao().insert(data);
        dbRef.push().setValue(data);

        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Log.w("Updated","message updated now");
                for(DataSnapshot ds: snapshot.getChildren()) {
                    ChatData chatData = ds.getValue(ChatData.class);
                    if (chatData.isSeen()) {
                        Log.i("Seen","message seen now"+chatData.getMessage());
                        ds.getRef().removeValue();
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
        MySharedPref sharedPref = new MySharedPref(this, "User-"+FirebaseAuth.getInstance().getUid());
        final UserPersonalData myData =  sharedPref.getUser();
        DatabaseReference mRef = Utils.getRefForBasicData(Constants.USER_ADMINISTRATION,hisUID);
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