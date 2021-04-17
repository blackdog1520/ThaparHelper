package com.blackdev.thaparhelper.dashboard.dashboardFrag;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;

import com.blackdev.thaparhelper.R;
import com.blackdev.thaparhelper.allutils.Constants;
import com.blackdev.thaparhelper.allutils.Utils;
import com.blackdev.thaparhelper.database.AppDatabase;
import com.blackdev.thaparhelper.database.TimeTableDao;
import com.blackdev.thaparhelper.database.TimeTableData;

import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.TimeZone;

import androidx.core.app.NotificationCompat;

import static android.content.Context.ALARM_SERVICE;

public class NotifierAlarm extends BroadcastReceiver {

    private Context context;
    private static String TAG = "NotifierAlarm";

    @Override
    public void onReceive(Context context, Intent intent) {
        this.context = context;
        Log.i("ClassNotifier","Show notification of class now");
        TimeTableData reminder = new TimeTableData();
        reminder.setmSubjectName(intent.getStringExtra("Subject"));
        Date date = new Date(intent.getStringExtra("RemindDate"));
        reminder.setmMM(date.getMinutes());
        reminder.setmHH(date.getHours());
        reminder.setClassType(intent.getIntExtra("ClassType", Constants.LECTURE_TYPE));
        reminder.setmDay(date.getDay());




        Uri alarmsound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);

        Intent intent1 = new Intent(context,TimeTableOptionsActivity.class);
        intent1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        TaskStackBuilder taskStackBuilder = TaskStackBuilder.create(context);
        taskStackBuilder.addParentStack(TimeTableOptionsActivity.class);
        taskStackBuilder.addNextIntent(intent1);

        PendingIntent intent2 = taskStackBuilder.getPendingIntent(1,PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context,Constants.CHANNEL_ID);

        NotificationChannel channel = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            channel = new NotificationChannel("my_channel_01",Constants.APP_NAME, NotificationManager.IMPORTANCE_HIGH);
        }

        Notification notification = builder.setContentTitle(Constants.APP_NAME)
                .setContentText(Constants.TYPE_LIST[intent.getIntExtra("ClassType",0)]+" now of "+intent.getStringExtra("Subject")).setAutoCancel(true)
                .setSound(alarmsound).setSmallIcon(R.mipmap.ic_launcher_round)
                .setContentIntent(intent2)
                .setChannelId("my_channel_01")
                .build();

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationManager.createNotificationChannel(channel);
        }
        notificationManager.notify(new Random().nextInt(85-65),builder.build());

        if(intent.getIntExtra("AlarmNumber",Constants.MAX_ALARM) == Constants.MAX_ALARM || intent.getIntExtra("AlarmNumber",Constants.MAX_ALARM) == Constants.MAX_ALARM - 1 ) {
            int id = intent.getIntExtra("ChannelID",-1);
            if(id !=-1) {
                addFutureClass(reminder, id);
            }
        }
    }

    private void addFutureClass(TimeTableData data, int id) {
        Log.i("FutureAlarms","Adding Alarms");
        LocalDate prev = null;
        LocalDate nextDate;
        LocalDate ld;

        for(int i=1;i<=Constants.MAX_ALARM;i++) {

            if(i == 1) {
                Date cal = Calendar.getInstance(TimeZone.getTimeZone("GMT+5:30")).getTime();
                ld = LocalDate.of(2021, cal.getMonth() + 1, cal.getDate());
            } else {
                ld = prev;
            }
            nextDate = Utils.getNextDay(data.getmDay()-1, ld, Constants.NEXT_DAY_SEARCH);

            prev = nextDate;
            Log.i("INDEX", "SELECTED" + nextDate+" Prev "+ld);
            // check if its greater than current time or not
            Calendar newDate = Calendar.getInstance();

            int date = Integer.parseInt(nextDate.toString().split("-", 3)[2]);
            newDate.set(nextDate.getYear(), nextDate.getMonth().getValue() - 1, date, data.getmHH(), data.getmMM(), 0);
            Log.i("Today", " " + ld.toString() + "*NEXTDAY*:" + newDate.getTime().toString());


            Date finalDate = newDate.getTime();
            Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT+5:30"));
            calendar.setTime(newDate.getTime());
            //calendar.set(Calendar.SECOND,0);

            Intent intent = new Intent(context, NotifierAlarm.class);
            intent.putExtra("Subject", data.getmSubjectName());
            intent.putExtra("RemindDate", finalDate.toString());
            intent.putExtra("ClassType", data.getClassType());
            intent.putExtra("AlarmNumber", i);
            intent.putExtra("ChannelID", id);
//                intent.putExtra("id",reminders.getId());
            PendingIntent intent1 = PendingIntent.getBroadcast(context, id*Constants.MAX_ALARM+i, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            AlarmManager alarmManager = (AlarmManager) context.getSystemService(ALARM_SERVICE);
            Log.i("Date", "" + newDate.getTime().toString() + "CALENDAR: " + calendar.getTime().toString());
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis() + 1000, intent1);
        }




//        LocalDate ld = LocalDate.now();
//        LocalDate nextDate = Utils.getNextDay(data.getmDay(),ld,Constants.SAME_DAY_SEARCH);
//        data.setClassType(Constants.LECTURE_TYPE);
//        data.setmHH(data.getmHH());
//        data.setmMM(data.getmMM());
//        // check if its greater than current time or not
//        Calendar newDate = Calendar.getInstance();
//        int date = Integer.getInteger(nextDate.toString().split("-",3)[-1]);
//        Log.i("Date",""+date);
//        newDate.set(nextDate.getYear(),nextDate.getMonth().getValue(),date,data.getmHH(),data.getmMM(),0);
//        Date finalDate = new Date(newDate.toString());
//        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT+5:30"));
//        calendar.setTime(finalDate);
//        calendar.set(Calendar.SECOND,0);
//
//        Intent intent = new Intent(context,NotifierAlarm.class);
//        intent.putExtra("Subject",data.getmSubjectName());
//        intent.putExtra("RemindDate",finalDate.toString());
//        intent.putExtra("ClassType",Constants.LECTURE_TYPE);
////                intent.putExtra("id",reminders.getId());
//        PendingIntent intent1 = PendingIntent.getBroadcast(context,100,intent,PendingIntent.FLAG_UPDATE_CURRENT);
//        AlarmManager alarmManager = (AlarmManager)context.getSystemService(ALARM_SERVICE);
//        alarmManager.setExact(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),intent1);
//        Log.i(TAG,"Added Future Alarm");
    }
}
