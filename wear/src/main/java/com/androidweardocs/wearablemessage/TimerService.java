package com.androidweardocs.wearablemessage;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

public class TimerService extends IntentService
{

    long[] pattern = { 0,2000 };
    private NotificationManager mNotificationManager;
    NotificationCompat.Builder builder;
    public TimerService() {
        super( "TimerService" );
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        String action = intent.getAction();
        if(action=="Fajr"){
            buildNotification("Fajr Time");
            }
        else if(action=="Dhuhr")
        {   buildNotification("Dhuhr Time");
        }
        else if(action=="Asr")
        {   buildNotification("Asr Time");
        }
        else if(action=="Maghrib")
        {   buildNotification("Maghrib Time");
        }
        else if(action=="Isha")
        {   buildNotification("Isha Time");
        }
        else
            buildNotification("Prayer Time");
    }




    private void buildNotification(String msg ) {

        mNotificationManager = (NotificationManager)
                this.getSystemService(Context.NOTIFICATION_SERVICE);

        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.ic_launcher)
                        .setContentTitle("MyPrayerCompanion")
                        .setVibrate(pattern)
                        .setContentText(msg)
                .setCategory(Notification.CATEGORY_ALARM)
                .setPriority(Notification.PRIORITY_HIGH);

        mBuilder.setContentIntent(contentIntent);
        mNotificationManager.notify(1, mBuilder.build());

    }


}
