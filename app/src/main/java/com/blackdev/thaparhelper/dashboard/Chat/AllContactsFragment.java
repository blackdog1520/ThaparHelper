package com.blackdev.thaparhelper.dashboard.Chat;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;

import android.util.Log;
import android.widget.SearchView;
import androidx.core.view.MenuItemCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.blackdev.thaparhelper.R;
import com.blackdev.thaparhelper.UserPersonalData;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AllContactsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AllContactsFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private View view;
    private ArrayList<UserPersonalData> list = new ArrayList<>();
    private RecyclerView recyclerView;
    private AllUserAdapter adapter;
    private ProgressBar mProgressBar;
    private SearchView searchView;
    private boolean firstSearch = false;

    public AllContactsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AllContactsFragments.
     */
    // TODO: Rename and change types and number of parameters
    public static AllContactsFragment newInstance(String param1, String param2) {
        AllContactsFragment fragment = new AllContactsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    void init() {
        recyclerView = view.findViewById(R.id.allUserRecyclerView);
        adapter = new AllUserAdapter(getContext(),list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_all_contacts, container, false);
        init();

        getAllUsers();
        searchView =  view.findViewById(R.id.search_users);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if(query.trim().isEmpty() && firstSearch) {
                    searchUsers(query);
                } else if(!query.trim().isEmpty()) {
                    firstSearch = true;
                    searchUsers(query.trim());
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if(newText.trim().isEmpty() && firstSearch) {
                    searchUsers(newText);
                } else if(!newText.trim().isEmpty()) {
                    firstSearch = true;
                    searchUsers(newText.trim());
                }
                return false;
            }
        });
        return view;
    }



    private void getAllUsers() {
        DatabaseReference mRef = FirebaseDatabase.getInstance().getReference().child("Users").child("BasicData").child("Administration");
        mRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ds: snapshot.getChildren()) {
                    UserPersonalData data = ds.getValue(UserPersonalData.class);
                    if(data!=null && data.getUid()!=null && !data.getUid().equals(FirebaseAuth.getInstance().getUid())) {
                        list.add(data);
                    }
                }
                adapter.assignList(list);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void searchUsers(final String newText) {
        if (list.size()==0) {
            showProgressBar(getContext());
            Handler myHandler = new Handler();
            myHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    searchUsers(newText);
                }
            },1000);
        }else {
            hideProgressBar();
            if (newText.isEmpty()) {
                adapter.assignList(list);
                Log.i("LISTSIZE",""+adapter.list.size());
            } else {
                ArrayList<UserPersonalData> temp = new ArrayList<>();
                for (UserPersonalData data : list) {
                    if (data.getName().trim().toLowerCase().contains(newText.toLowerCase()) || data.getEmail().trim().contains(newText) || data.getBatch().trim().toLowerCase().contains(newText.toLowerCase())) {
                        temp.add(data);
                    }
                }
                adapter.assignList(temp);
            }
        }
    }

    public void showProgressBar(Context context) {

        if (mProgressBar != null) {
            mProgressBar.animate()
                    .setDuration(200)
                    .alpha(1)
                    .start();
        }
        else {
            RelativeLayout relativeLayout = (RelativeLayout) LayoutInflater.from(context).inflate(R.layout.invest_progress_loading, null);
            mProgressBar = (ProgressBar) relativeLayout.findViewById(R.id.progressBar);
            ((ViewGroup) searchView).addView(relativeLayout, 1);
        }
    }

    public void hideProgressBar() {

        if(mProgressBar == null)
            return;

        mProgressBar.animate()
                .setDuration(200)
                .alpha(0)
                .start();

    }
}