package com.blackdev.thaparhelper.dashboard.dashboardFrag;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import com.blackdev.thaparhelper.R;
import com.blackdev.thaparhelper.allutils.Constants;
import com.blackdev.thaparhelper.allutils.Utils;
import com.blackdev.thaparhelper.database.AppDatabase;
import com.blackdev.thaparhelper.database.TimeTableDao;
import com.blackdev.thaparhelper.database.TimeTableData;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.temporal.TemporalField;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class TimeTableOptionsActivity extends AppCompatActivity implements  View.OnClickListener, AdapterView.OnItemSelectedListener {

    Button addButton;
    Spinner subjectSpinner,daySpinner;
    String[] subjectList,dayList;
    TimePicker timePicker;
    TimeTableData data;
    AppDatabase appDatabase;
    TimeTableDao timeTableDao;

    private void init() {
        addButton = findViewById(R.id.addButtonTimeTableOptions);
        subjectSpinner = findViewById(R.id.subjectListDropDown);
        daySpinner = findViewById(R.id.dayListDropDown);
        timePicker = findViewById(R.id.timepickerTTOptions);
        subjectList = Constants.SUBJECTLIST;
        dayList = Constants.DAYLIST;
        appDatabase = AppDatabase.getInstance(this);
        timeTableDao = appDatabase.timeTableDao();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_table_options);
        init();
        ArrayAdapter<String> adapterSubject = new ArrayAdapter<String>(this,R.layout.support_simple_spinner_dropdown_item,subjectList);
        adapterSubject.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        subjectSpinner.setAdapter(adapterSubject);
        subjectSpinner.setOnItemSelectedListener(this);

        ArrayAdapter<String> adapterDays = new ArrayAdapter<String>(this,R.layout.support_simple_spinner_dropdown_item,dayList);
        adapterSubject.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        daySpinner.setAdapter(adapterDays);
        daySpinner.setOnItemSelectedListener(this);
        data = new TimeTableData();
        addButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.addButtonTimeTableOptions:
                int hr = timePicker.getHour();
                int min = timePicker.getMinute();
                data.setmHH(hr);
                data.setmMM(min);
                data.setClassType(Constants.LECTURE_TYPE);
                data.setmDay(daySpinner.getSelectedItemPosition());
                data.setmSubjectName(subjectList[subjectSpinner.getSelectedItemPosition()]);

                Date cal = Calendar.getInstance(TimeZone.getTimeZone("GMT+5:30")).getTime();
                LocalDate ld = LocalDate.of(2021,cal.getMonth()+1,cal.getDate());
                LocalDate nextDate = Utils.getNextDay(data.getmDay(),ld,Constants.SAME_DAY_SEARCH);

                Log.i("INDEX",""+cal.toString()+"SELECTED"+nextDate);
                // check if its greater than current time or not
                Calendar newDate = Calendar.getInstance();

                int date = Integer.parseInt(nextDate.toString().split("-",3)[2]);
                newDate.set(nextDate.getYear(),nextDate.getMonth().getValue()-1,date,hr,min,0);
                Log.i("Today"," "+ld.toString()+"*NEXTDAY*:"+newDate.getTime().toString());


                Date finalDate = newDate.getTime();
                Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT+5:30"));
                calendar.setTime(newDate.getTime());
                //calendar.set(Calendar.SECOND,0);

                Intent intent = new Intent(TimeTableOptionsActivity.this,NotifierAlarm.class);
                intent.putExtra("Subject",data.getmSubjectName());
                intent.putExtra("RemindDate",finalDate.toString());
                intent.putExtra("ClassType",Constants.LECTURE_TYPE);
//                intent.putExtra("id",reminders.getId());
                PendingIntent intent1 = PendingIntent.getBroadcast(TimeTableOptionsActivity.this,10001,intent,PendingIntent.FLAG_UPDATE_CURRENT);
                AlarmManager alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);
                Log.i("Date",""+newDate.getTime().toString()+"CALENDAR: "+calendar.getTime().toString());
                alarmManager.setExact(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis()+5*1000,intent1);

                Toast.makeText(TimeTableOptionsActivity.this,"Inserted Successfully",Toast.LENGTH_SHORT).show();



        }
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        switch (view.getId()) {
            case R.id.subjectListDropDown:
                data.setmSubjectName(subjectList[i]);
                break;
            case R.id.dayListDropDown:
                Log.i("DAY","SELECTED: "+i);


                data.setmDay(i);
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}