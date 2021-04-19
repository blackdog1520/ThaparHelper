package com.blackdev.thaparhelper.dashboard.dashboardFrag;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.LinearLayout;

import com.blackdev.thaparhelper.R;
import com.blackdev.thaparhelper.allutils.Constants;
import com.blackdev.thaparhelper.database.AppDatabase;
import com.blackdev.thaparhelper.database.TimeTableDao;
import com.blackdev.thaparhelper.database.TimeTableData;
import com.google.android.material.snackbar.Snackbar;

import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

public class EditTimeTableActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    List<TimeTableData> list;
    TimeTableAdapter adapter;
    TimeTableDao timeTableDao;
    LinearLayout rootLayout;
    boolean delete = true;

    private void init() {
        recyclerView = findViewById(R.id.timeTableRecyclerView);
        timeTableDao = AppDatabase.getInstance(this).timeTableDao();
        list = new ArrayList<>();
        rootLayout = findViewById(R.id.rootLayoutEditTT);
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
        enableSwipeToDeleteAndUndo();
        //adapter.notifyDataSetChanged();

    }

    private void enableSwipeToDeleteAndUndo() {

        SwipeToDeleteCallBack swipeToDeleteCallback = new SwipeToDeleteCallBack(this) {
            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {


                final int position = viewHolder.getAdapterPosition();
                final TimeTableData item = adapter.getData().get(position);

                adapter.removeItem(position);


                Snackbar snackbar = Snackbar
                        .make(rootLayout, "Item was removed from the list.", Snackbar.LENGTH_LONG);
                snackbar.setAction("UNDO", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        delete = false;
                        adapter.restoreItem(item, position);
                        recyclerView.scrollToPosition(position);
                    }
                });
                snackbar.setActionTextColor(Color.YELLOW);
                snackbar.show();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if(delete) {
                            timeTableDao.deleteEntry(item);

                            AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                            Intent myIntent = new Intent(getApplicationContext(), NotifierAlarm.class);
                                PendingIntent pendingIntent = PendingIntent.getBroadcast(
                                        getApplicationContext(), item.getId() , myIntent,
                                        PendingIntent.FLAG_UPDATE_CURRENT);

                                alarmManager.cancel(pendingIntent);

                        } else {
                            delete = true;
                        }
                    }
                },snackbar.getDuration()+1500);



            }
        };

        ItemTouchHelper itemTouchhelper = new ItemTouchHelper(swipeToDeleteCallback);
        itemTouchhelper.attachToRecyclerView(recyclerView);
    }
}