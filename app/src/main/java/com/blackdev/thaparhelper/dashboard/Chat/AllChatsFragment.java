package com.blackdev.thaparhelper.dashboard.Chat;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.blackdev.thaparhelper.R;
import com.blackdev.thaparhelper.database.AppDatabase;
import com.blackdev.thaparhelper.database.ChatData;
import com.blackdev.thaparhelper.database.ChatDataDao;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AllChatsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AllChatsFragment extends Fragment implements View.OnClickListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private RecyclerView recyclerView;
    List<ChatData> chatData = new ArrayList<>();
    AllUserAdapter adapter;
    FloatingActionButton createGroup;




    public AllChatsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AllChatsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AllChatsFragment newInstance(String param1, String param2) {
        AllChatsFragment fragment = new AllChatsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view  = inflater.inflate(R.layout.fragment_all_chats, container, false);
        recyclerView = view.findViewById(R.id.userRecentChatsRV);
        createGroup = view.findViewById(R.id.createGroupFloatingButton);
        createGroup.setOnClickListener(this);
        chatData = AppDatabase.getInstance(getContext()).chatDataDao().getChatHistory();
        Log.i("RecentChats","SIZE: "+chatData.size());
        showUserChat(view);

        return view;
    }

    private void showUserChat(View view) {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setSmoothScrollbarEnabled(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new AllUserAdapter(getContext(),chatData);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.createGroupFloatingButton) {
            Intent intent = new Intent(getActivity(),CreateGroupActivity.class);
            startActivity(intent);
        }
    }
}