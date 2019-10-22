package com.mple.seriestracker.util;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import com.mple.seriestracker.Countdown;
import com.mple.seriestracker.R;
import com.mple.seriestracker.activity.HomeScreenActivity;

public class NotificationGenerator extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void CheckIfAiring(long seconds) {
       if (seconds < 13000){
            sendNotification();
        }
    }

    public void sendNotification(View view)
    {
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, "M_CH_ID");

        //Create the intent that’ll fire when the user taps the notification//

        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.androidauthority.com/"));
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

        notificationBuilder.setContentIntent(pendingIntent);

        NotificationManager nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        NotificationChannel notificationChannel =
            new NotificationChannel("M_CH_ID", "M_CH_ID", NotificationManager.IMPORTANCE_DEFAULT);
        notificationChannel.setDescription("Test");
        nm.createNotificationChannel(notificationChannel);


        notificationBuilder.setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_ALL)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.drawable.ic_launcher)
                .setTicker("Hearty365")
                .setContentTitle("Default notification")
                .setContentText("Random words")
                .setContentInfo("Info");

        nm.notify(1, notificationBuilder.build());
    }
}
