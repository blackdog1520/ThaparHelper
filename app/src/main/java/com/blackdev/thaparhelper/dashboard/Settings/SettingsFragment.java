package com.blackdev.thaparhelper.dashboard.Settings;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.blackdev.thaparhelper.LoginActivity;
import com.blackdev.thaparhelper.UserFacultyModelClass;
import com.blackdev.thaparhelper.allutils.Constants;
import com.blackdev.thaparhelper.allutils.MySharedPref;
import com.blackdev.thaparhelper.R;
import com.blackdev.thaparhelper.UserPersonalData;
import com.blackdev.thaparhelper.allutils.Utils;
import com.blackdev.thaparhelper.dashboard.Explore.Adapters.AdapterPosts;
import com.blackdev.thaparhelper.dashboard.Explore.Models.ModelPost;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;

import java.net.Inet4Address;
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
public class SettingsFragment extends Fragment implements ProfilePostAdapterClass.onPostClicked {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private FirebaseAuth mAuth;
    private DatabaseReference mRef;
    private View view;

    private RelativeLayout rootLayout;
    private DrawerLayout navigationDrawerLayout;
    private NavigationView navView;
    private TextView userName, userSpecificDetail, emailId, bioTv;
    private ImageView menuButton;
    private LottieAnimationView coverPic;
    private CircularImageView profilePic;
    private View editProfile;

    private MySharedPref sharedPref;
    private int userType;
    private List<ModelPost> list = new ArrayList<>();

    private RecyclerView recyclerView, userSpecificPostRecyclerView;
    private ScrollView scrollViewRoot;
    private ProfilePostAdapterClass adapterClass;
    private List<ModelPost> postList;
    private ArrayList<ModelPost> userOnlyPostList;
    private AdapterPosts adapterPosts;

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
        mRef = Utils.getRefForBasicData(Utils.getCurrentUserType(getContext(), mAuth.getUid()), mAuth.getUid());
        //set this ref based on the type of user signing in
        rootLayout = getView().findViewById(R.id.rootLayoutSettings);
        navigationDrawerLayout = getView().findViewById(R.id.navigation_drawer);
        navView = getView().findViewById(R.id.nav_view);
        scrollViewRoot = getView().findViewById(R.id.scrollViewSettings);
        userName = getView().findViewById(R.id.userNameSettings);
        userSpecificDetail = getView().findViewById(R.id.userSpecificDetailSettings);
        emailId = getView().findViewById(R.id.userEmailIdSettings);
        profilePic = getView().findViewById(R.id.userProfileImageSettings);
        coverPic = getView().findViewById(R.id.showUserTypeLottieSettings);
        menuButton = getView().findViewById(R.id.optionsButton);
        bioTv = getView().findViewById(R.id.userBioSettingsFragment);
        editProfile = getView().findViewById(R.id.editProfileTab);
        sharedPref = new MySharedPref(getContext(), Utils.getStringPref(mAuth.getUid()), Constants.TYPE_SHARED_PREF);
        userType = sharedPref.getUserType();
        pd = new ProgressDialog(getActivity());
        initUserData();
        coverPic.playAnimation();

        recyclerView = view.findViewById(R.id.postsRecyclerView);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new GridLayoutManager(getContext(),3);
        recyclerView.setLayoutManager(linearLayoutManager);
        postList = new ArrayList<>();

        userSpecificPostRecyclerView = view.findViewById(R.id.userSpecificPostRV);
        LinearLayoutManager linearLayoutManagerForRV = new LinearLayoutManager(getContext());
        userSpecificPostRecyclerView.setLayoutManager(linearLayoutManagerForRV);
    }

    private void initUserData() {
        MySharedPref pref = new MySharedPref(getContext(), Utils.getStringPref(mAuth.getUid()), Constants.DATA_SHARED_PREF);
        switch (userType) {
            case Constants.USER_ADMINISTRATION:
                setOthers(pref.getUserF());
                coverPic.setAnimation(R.raw.administration);
                break;
            case Constants.USER_STUDENT:
                setStudent(pref.getUser());
                coverPic.setAnimation(R.raw.student);
                break;
            case Constants.USER_FACULTY:
                setOthers(pref.getUserF());
                coverPic.setAnimation(R.raw.faculty);
                break;
        }
    }

    void setStudent(UserPersonalData data) {
        emailId.setText(data.getEmail());
        userName.setText(data.getName());
        bioTv.setText(data.getBio());

        userSpecificDetail.setText(data.getRollNumber());
        if (data.getProfileImageLink() != null && !data.getProfileImageLink().isEmpty()) {
            Picasso.get().load(data.getProfileImageLink()).into(profilePic);
        }
    }

    void setOthers(UserFacultyModelClass data) {
        emailId.setText(data.getEmail());
        userName.setText(data.getName());
        bioTv.setText(data.getBio());
        userSpecificDetail.setText(data.getDesignation());
        if (data.getProfileImageLink() != null && !data.getProfileImageLink().isEmpty()) {
            Picasso.get().load(data.getProfileImageLink()).into(profilePic);
        }
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
        // check Data offline


    }

    @Override
    public void onResume() {
        initUserData();
        super.onResume();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.nav_drawer, container, false);
        init();

        menuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showEditOptions();
            }
        });
        fetchPostData();

       navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
           @Override
           public boolean onNavigationItemSelected(@NonNull MenuItem item) {
               switch (item.getItemId()) {
                   case R.id.editProfileTab:
                       Intent intent = new Intent(getContext(), EditProfileSettings.class);
                       startActivity(intent);
                       showEditOptions();
                       return true;
               }
               return false;
           }
       });
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
                adapterClass = new ProfilePostAdapterClass(SettingsFragment.this::onPostClick, getContext(),postList);
                recyclerView.setAdapter(adapterClass);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void showEditOptions() {
        if(navigationDrawerLayout.isDrawerOpen(GravityCompat.END))
        {
            navigationDrawerLayout.closeDrawer(GravityCompat.END);
        }
        else
        {
            navigationDrawerLayout.openDrawer(GravityCompat.END);
        }
    }

    @Override
    public void onPostClick(int position) {
        scrollViewRoot.setVisibility(View.GONE);
        userSpecificPostRecyclerView.setVisibility(View.VISIBLE);
        userOnlyPostList = new ArrayList<>();

        mRef.child("UserPost").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userOnlyPostList.clear();
                for (DataSnapshot snapshot1 : snapshot.getChildren()){
                    ModelPost post = snapshot1.getValue(ModelPost.class);
                    userOnlyPostList.add(post);
                }
                Collections.reverse(userOnlyPostList);
                adapterPosts = new AdapterPosts(getContext(),userOnlyPostList);
                userSpecificPostRecyclerView.setAdapter(adapterPosts);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}