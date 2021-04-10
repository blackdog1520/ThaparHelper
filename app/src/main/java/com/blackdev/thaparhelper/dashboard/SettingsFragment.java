package com.blackdev.thaparhelper.dashboard;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.blackdev.thaparhelper.LoginActivity;
import com.blackdev.thaparhelper.MySharedPref;
import com.blackdev.thaparhelper.R;
import com.blackdev.thaparhelper.StaticVariables;
import com.blackdev.thaparhelper.UserPersonalData;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SettingsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SettingsFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private FirebaseAuth mAuth;
    private FirebaseDatabase mDB;
    private DatabaseReference mRef;
    private View view;

    private TextView userName,rollNumber,emailId;
    private ImageView profilePic;
    public SettingsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SettingsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SettingsFragment newInstance(String param1, String param2) {
        SettingsFragment fragment = new SettingsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    private void init() {
        mAuth = FirebaseAuth.getInstance();
        mDB = FirebaseDatabase.getInstance();
        mRef = mDB.getReference().child("Users").child("BasicData").child("Administration").child(mAuth.getUid());
        //set this ref based on the type of user signing in
        userName = getView().findViewById(R.id.userNameSettings);
        rollNumber = getView().findViewById(R.id.userRollNoSettings);
        emailId = getView().findViewById(R.id.userEmailIdSettings);
        profilePic = getView().findViewById(R.id.userProfileImageSettings);

    }

    public View getView(){
        return view;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        //init();
        // check Data offline


    }

    private void fetchAndSetData() {
        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot==null){
                    Log.e("Settings",mAuth.getCurrentUser().getEmail());
                    Snackbar.make(getView(),"Login again!",Snackbar.LENGTH_SHORT).show();
                    // logout user
                    mAuth.signOut();
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    startActivity(intent);
                    getActivity().finish();
                } else {
                    Log.e("SETTINGSVal",""+snapshot.getChildrenCount());
                    //for(DataSnapshot dp: snapshot.getChildren()) {
                        UserPersonalData data = snapshot.getValue(UserPersonalData.class);
                        Log.e("SETTINGSVal",snapshot.child("email").getValue().toString());
                        emailId.setText(data.getEmail());
                        userName.setText(data.getName());
                        rollNumber.setText(data.getRollNumber());
                    MySharedPref pref = new MySharedPref(getActivity(),"User-"+mAuth.getUid());
                    pref.saveUser(data);
                        if(data.getProfileImageLink()!=null && !data.getProfileImageLink().isEmpty()) {
                            Picasso.get().load(data.getProfileImageLink()).into(profilePic);
                        }
                        // set it to offline database;
                    //}
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Snackbar.make(getView(),"Error fetching info! Please try again.",Snackbar.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_settings, container, false);
        init();
        fetchAndSetData();

        return view;

    }
}