package com.blackdev.thaparhelper.dashboard.Chat;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;

import com.blackdev.thaparhelper.Constants;
import com.blackdev.thaparhelper.R;
import com.blackdev.thaparhelper.Utils;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.core.content.res.ResourcesCompat;

public class FirebaseNotificationService extends FirebaseMessagingService {
    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {

        if (remoteMessage.getData().size()>0) {
            Map<String, String> map = remoteMessage.getData();
            if(Build.VERSION.SDK_INT > Build.VERSION_CODES.O) {
                createOreoNotification(map.get("title"),map.get("message"),map.get("hisUID"), map.get("hisProfile"),map.get("hisDept"));
            } else {
                createNormalNotification(map.get("title"),map.get("message"),map.get("hisUID"), map.get("hisProfile"),map.get("hisDept"));
            }
        }

        super.onMessageReceived(remoteMessage);
    }


    @Override
    public void onNewToken(@NonNull String s) {
        updateToke(s);
        super.onNewToken(s);
    }

    private void updateToke(String token) {
        DatabaseReference databaseReference = Utils.getRefForBasicData(Constants.USER_ADMINISTRATION,FirebaseAuth.getInstance().getUid());
        // choose path based on user type ;
        Map<String, Object> map= new HashMap<>();

        map.put("token",token);
        databaseReference.updateChildren(map);
    }

    private void createNormalNotification(String title, String message, String
            hisUID, String profileLink, String dept) {
        Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, Constants.CHANNEL_ID);
        builder.setContentTitle(title)
                .setContentText(message)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setAutoCancel(true)
                .setColor(ResourcesCompat.getColor(getResources(),R.color.ShadeMedium,null))
                .setSound(uri);

        Intent intent = new Intent(this, UserChatHolderActivity.class);
        intent.putExtra("HisUID",hisUID);
        intent.putExtra("HisName",title);
        intent.putExtra("HisDept",dept);
        intent.putExtra("HisProfile",profileLink);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,0,intent,PendingIntent.FLAG_ONE_SHOT);
        builder.setContentIntent(pendingIntent);

        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(new Random().nextInt(85-65),builder.build());

    }

    @RequiresApi(api =  Build.VERSION_CODES.O)
    private void createOreoNotification(String title, String message, String
            hisUID, String profileLink, String dept) {
        NotificationChannel channel = new NotificationChannel(Constants.CHANNEL_ID,"Message",NotificationManager.IMPORTANCE_HIGH);
        channel.setShowBadge(true);
        channel.enableLights(true);
        channel.enableVibration(true);
        channel.setDescription("Message Description");
        channel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager.createNotificationChannel(channel);

        Intent intent = new Intent(this, UserChatHolderActivity.class);
        intent.putExtra("HisUID",hisUID);
        intent.putExtra("HisName",title);
        intent.putExtra("HisDept",dept);
        intent.putExtra("HisProfile",profileLink);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,0,intent,PendingIntent.FLAG_ONE_SHOT);

        Notification notification = new Notification.Builder(this, Constants.CHANNEL_ID)
                .setContentTitle(title)
                .setContentText(message)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setColor(ResourcesCompat.getColor(getResources(),R.color.ShadeMedium,null))
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .build();
        manager.notify(new Random().nextInt(85-65),notification);

    }
}
