package com.example.linweiran.myeventplanner;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.support.v4.app.NotificationCompat;

/**
 * Created by linweiran on 10/10/16.
 */

public class MyEventNotification {

    private Context context;
    private Intent resultIntent;
    private PendingIntent pendingIntent;
    private NotificationCompat.Builder myBuilder;

    public MyEventNotification(Context context, Intent resultIntent, String title, String summary) {
        this.context = context;
        this.resultIntent = resultIntent;

        this.myBuilder = new NotificationCompat.Builder(this.context)
                .setContentText(summary)
                .setContentTitle(title)
                .setSmallIcon(R.drawable.alert_dialog_icon)
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.alert_dialog_icon))
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));
        getPendingIntent();
        myBuilder.setContentIntent(this.pendingIntent);
    }

    private void getPendingIntent() {
        this.pendingIntent = PendingIntent.getActivity(this.context, 0, this.resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    public NotificationCompat.Builder getNotificationBuilder() {
        return this.myBuilder;
    }
}
