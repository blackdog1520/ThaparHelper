package com.blackdev.thaparhelper.dashboard.dashboardFrag;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.blackdev.thaparhelper.LoginActivity;
import com.blackdev.thaparhelper.R;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.auth.FirebaseAuth;

// here we will add button for TimeTable


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DashboardFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DashboardFragment extends Fragment implements View.OnClickListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final int timetableId = R.id.timetableButtonDashBoard;
    private static final int complaintId = R.id.complaintButtonDashboard;
    private static final int timetableViewId = R.id.timetableViewButtonDashBoard;
    private static final int logoutId = R.id.action_logout;
    private static final int closeId = R.id.close;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    MaterialCardView timetable,complaint,editTimeTable;
    ImageView logout, close;

    public DashboardFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DashboardFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DashboardFragment newInstance(String param1, String param2) {
        DashboardFragment fragment = new DashboardFragment();
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
        View view =  inflater.inflate(R.layout.fragment_dashboard, container, false);
        timetable = view.findViewById(R.id.timetableButtonDashBoard);
        complaint = view.findViewById(R.id.complaintButtonDashboard);
        editTimeTable = view.findViewById(R.id.timetableViewButtonDashBoard);
        logout = view.findViewById(R.id.action_logout);
        close = view.findViewById(R.id.close);

        logout.setOnClickListener(this);
        close.setOnClickListener(this);
        timetable.setOnClickListener(this);
        complaint.setOnClickListener(this);
        editTimeTable.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case timetableId:
                Intent intent = new Intent(getActivity(),TimeTableOptionsActivity.class);
                startActivity(intent);
                break;
            case complaintId:
                Toast.makeText(getContext(),"Currently in development phase",Toast.LENGTH_SHORT).show();
                break;
            case timetableViewId:
                Intent intent1 = new Intent(getActivity(),EditTimeTableActivity.class);
                startActivity(intent1);
                break;
            case logoutId:
                FirebaseAuth.getInstance().signOut();
                Intent intent2 = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent2);
                break;
            case closeId:
                getActivity().onBackPressed();
                break;

        }

    }
}