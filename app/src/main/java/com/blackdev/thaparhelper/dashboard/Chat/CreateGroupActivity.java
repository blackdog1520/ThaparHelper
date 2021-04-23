package com.blackdev.thaparhelper.dashboard.Chat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.blackdev.thaparhelper.R;
import com.blackdev.thaparhelper.allutils.Constants;
import com.blackdev.thaparhelper.allutils.CustomButtonWithPD;
import com.blackdev.thaparhelper.allutils.Utils;
import com.blackdev.thaparhelper.dashboard.Chat.Models.ModelGroupDetails;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class CreateGroupActivity extends AppCompatActivity implements View.OnClickListener {
    CustomButtonWithPD createButton;
    ImageView groupIcon;
    TextInputLayout nameL,courseL,batchL;
    String groupName,courseName,batchName,groupId;
    String key = null;

    void init() {
        createButton = findViewById(R.id.createGroupButton);
        groupIcon = findViewById(R.id.groupIconCreate);

        nameL = findViewById(R.id.groupNameCreateTextLayout);
        courseL = findViewById(R.id.courseIdCreateTextLayout);
        batchL = findViewById(R.id.batchCreateTextLayout);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_group);
        init();
        createButton.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        if(R.id.createGroupButton == view.getId()) {
            createButton.showLoading();
            if (validateDetails()) {
                groupName = nameL.getEditText().getText().toString().trim();
                courseName = courseL.getEditText().getText().toString().trim();
                batchName = batchL.getEditText().getText().toString().trim();
                String timestamp = Long.toString(System.currentTimeMillis());
                groupId = FirebaseAuth.getInstance().getUid() + "zG" + timestamp + "Id";
                getNotificationKey();

            }
        }
    }

    private void getNotificationKey() {
        JSONObject to =  new JSONObject();
        try {
            to.put("operation", "create");
            to.put("notification_key_name",groupId);
            to.put("registration_ids", new String[]{FirebaseMessaging.getInstance().getToken().toString()});
        } catch (Exception e) {
            e.printStackTrace();
        }

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, Constants.GROUP_NOTIFICATION_URL,to, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    key = response.get("notification_key").toString();
                    createGroup();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.i("notification","sendNotification: "+response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("notification","sendNotification: "+error);
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("Authorization","Key="+Constants.CLOUD_SERVER_KEY);
                map.put("Content-Type","application/json");
                map.put("project_id",Constants.SENDER_ID);
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

    private void createGroup() {

        DatabaseReference mRef = Utils.getRefForGroup(groupId);
        ModelGroupDetails details = new ModelGroupDetails(groupId,groupName,courseName,"",key,new String[]{FirebaseAuth.getInstance().getUid()});
        // save groupKeys in room;
        mRef.setValue(details).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                    updateUI();
            }
        });
    }

    private void updateUI() {
        finish();
    }

    private boolean validateDetails() {
        // TODO:add checking details here
        return true;
    }
}