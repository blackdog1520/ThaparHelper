package com.blackdev.thaparhelper.allutils;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.blackdev.thaparhelper.dashboard.dashboardFrag.NotifierAlarm;
import com.blackdev.thaparhelper.database.AppDatabase;
import com.blackdev.thaparhelper.database.TimeTableDao;
import com.blackdev.thaparhelper.database.TimeTableData;

import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import static android.content.Context.ALARM_SERVICE;

public class BootCompleteReciever extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if( intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) {
            setAlarmsAgain(context);
        }
    }

    private void setAlarmsAgain(Context context) {
        TimeTableDao timeTableDao = AppDatabase.getInstance(context).timeTableDao();
        List<TimeTableData> dataList = timeTableDao.getAll();
        for(TimeTableData data : dataList) {
            int hr = data.getmHH();
            int min = data.getmMM();
            int id = data.getId();
            LocalDate prev = null;
            LocalDate nextDate = null;
            Calendar calendar = null;

            Date cal = Calendar.getInstance(TimeZone.getTimeZone("GMT+5:30")).getTime();
            LocalDate ld = LocalDate.of(2021, cal.getMonth() + 1, cal.getDate());
            nextDate = Utils.getNextDay(data.getmDay(), ld, Constants.SAME_DAY_SEARCH);
            Log.i("INDEX", "SELECTED" + nextDate);
            // check if its greater than current time or not
            Calendar newDate = Calendar.getInstance();
            int date = Integer.parseInt(nextDate.toString().split("-", 3)[2]);
            newDate.set(nextDate.getYear(), nextDate.getMonth().getValue() - 1, date, hr, min, 0);
            Log.i("Today", " " + ld.toString() + "*NEXTDAY*:" + newDate.getTime().toString());
            Date finalDate = newDate.getTime();
            calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT+5:30"));
            calendar.setTime(newDate.getTime());
            calendar.set(Calendar.SECOND, 0);

            Intent intent = new Intent(context, NotifierAlarm.class);
            intent.putExtra("Subject", data.getmSubjectName());
            intent.putExtra("RemindDate", finalDate.toString());
            intent.putExtra("ClassType", data.getClassType());
            intent.putExtra("ChannelID", id);
            PendingIntent intent1 = PendingIntent.getBroadcast(context
                    , id * (Constants.MAX_ALARM), intent, PendingIntent.FLAG_UPDATE_CURRENT);
            AlarmManager alarmManager = (AlarmManager) context.getSystemService(ALARM_SERVICE);
            Log.i("Date", "" + newDate.getTime().toString() + "CALENDAR: " + calendar.getTime().toString());
            alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), ((long) 7) * (AlarmManager.INTERVAL_DAY), intent1);
            //alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis() + i*10*1000, intent1);
        }
        Log.i("Setting Time Table","Inserted Successfully");




    }
}
