package com.blackdev.thaparhelper.dashboard.dashboardFrag;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.blackdev.thaparhelper.R;
import com.blackdev.thaparhelper.database.AppDatabase;
import com.blackdev.thaparhelper.database.TimeTableDao;
import com.blackdev.thaparhelper.database.TimeTableData;

import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

public class EditTimeTableActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    List<TimeTableData> list;
    TimeTableAdapter adapter;
    TimeTableDao timeTableDao;

    private void init() {
        recyclerView = findViewById(R.id.timeTableRecyclerView);
        timeTableDao = AppDatabase.getInstance(this).timeTableDao();
        list = new ArrayList<>();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_time_table);
        init();
        LinearLayoutManager manager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(manager);
        fetchData();
    }

    private void fetchData() {

        for(int i=0;i<6;i++) {
            TimeTableData data = new TimeTableData();
            data.setmDay(i);
            data.setClassType(-1);
            list.add(data);
            list.addAll(list.size(),timeTableDao.getDaysTimeTable(i));
        }
        adapter = new TimeTableAdapter(this,list);
        recyclerView.setAdapter(adapter);
        //adapter.notifyDataSetChanged();

    }


}