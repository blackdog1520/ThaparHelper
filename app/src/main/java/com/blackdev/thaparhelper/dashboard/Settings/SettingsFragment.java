package com.blackdev.thaparhelper.dashboard.Settings;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.blackdev.thaparhelper.LoginActivity;
import com.blackdev.thaparhelper.allutils.Constants;
import com.blackdev.thaparhelper.allutils.MySharedPref;
import com.blackdev.thaparhelper.R;
import com.blackdev.thaparhelper.UserPersonalData;
import com.blackdev.thaparhelper.allutils.Utils;
import com.blackdev.thaparhelper.dashboard.Explore.Models.ModelPost;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

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

    private TextView userName, userSpecificDetail, emailId;
    private ImageView coverPic, menuButton;
    private CircularImageView profilePic;

    private MySharedPref sharedPref;
    private int userType;
    private List<ModelPost> list = new ArrayList<>();

    private RecyclerView recyclerView;
    private ProfilePostAdapterClass adapterClass;
    private List<ModelPost> postList;

    ProgressDialog pd;

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
        mRef = Utils.getRefForBasicData(Utils.getCurrentUserType(getContext(), mAuth.getUid()), mAuth.getUid());
        //set this ref based on the type of user signing in
        userName = getView().findViewById(R.id.userNameSettings);
        userSpecificDetail = getView().findViewById(R.id.userSpecificDetailSettings);
        emailId = getView().findViewById(R.id.userEmailIdSettings);
        profilePic = getView().findViewById(R.id.userProfileImageSettings);
        coverPic = getView().findViewById(R.id.coverPhoto);
        menuButton = getView().findViewById(R.id.optionsButton);
        sharedPref = new MySharedPref(getContext(), Utils.getStringPref(mAuth.getUid()), Constants.TYPE_SHARED_PREF);
        userType = sharedPref.getUserType();
        pd = new ProgressDialog(getActivity());

        recyclerView = view.findViewById(R.id.postsRecyclerView);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new GridLayoutManager(getContext(),3);
        recyclerView.setLayoutManager(linearLayoutManager);
        postList = new ArrayList<>();



    }

    public View getView() {
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
        setHasOptionsMenu(true);
        // check Data offline


    }

    private void fetchAndSetData() {
        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot == null) {
                    Log.e("Settings", mAuth.getCurrentUser().getEmail());
                    Snackbar.make(getView(), "Login again!", Snackbar.LENGTH_SHORT).show();
                    // logout user
                    mAuth.signOut();
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    startActivity(intent);
                    getActivity().finish();
                } else {
                    Log.e("SETTINGSVal", "" + snapshot.getChildrenCount());
                    //for(DataSnapshot dp: snapshot.getChildren()) {
                    UserPersonalData data = snapshot.getValue(UserPersonalData.class);
                    Log.e("SETTINGSVal", snapshot.child("email").getValue().toString());
                    emailId.setText(data.getEmail());
                    userName.setText(data.getName());
                    userSpecificDetail.setText(data.getRollNumber());

                    MySharedPref pref = new MySharedPref(getContext(), "User-" + mAuth.getUid(), Constants.DATA_SHARED_PREF);
                    pref.saveUser(data);
                    if (data.getProfileImageLink() != null && !data.getProfileImageLink().isEmpty()) {
                        Picasso.get().load(data.getProfileImageLink()).into(profilePic);
                    }
                    // set it to offline database;
                    //}
                }
                fetchPostData();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Snackbar.make(getView(), "Error fetching info! Please try again.", Snackbar.LENGTH_SHORT).show();
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

        menuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showEditOptions();
            }
        });
        fetchPostData();

        return view;

    }

    private void fetchPostData() {
        mRef.child("UserPost").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                postList.clear();
                for (DataSnapshot snapshot1 : snapshot.getChildren()){
                    ModelPost post = snapshot1.getValue(ModelPost.class);
                    postList.add(post);
                }
                Collections.reverse(postList);
                adapterClass = new ProfilePostAdapterClass(getContext(),postList);
                recyclerView.setAdapter(adapterClass);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void showEditOptions() {
        String options[] = {"Edit Name", "Edit Profile Photo", "Edit Cover Photo", "Edit Branch", "Edit Batch"};
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Edit Profile");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which)
                {
                    case 0:
                        break;
                    case 1:
                        break;
                    case 2:
                        break;
                    case 3:
                        break;
                    case 4:
                    break;
                }
            }
        });
        builder.create().show();
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        if (menu != null) {
            menu.findItem(R.id.action_add_post).setVisible(false);
        }
    }
}