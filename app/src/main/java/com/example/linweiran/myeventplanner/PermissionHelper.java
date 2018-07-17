package com.example.linweiran.myeventplanner;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.os.ResultReceiver;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by linweiran on 9/10/16.
 */

public class PermissionHelper {

    private static final String TAG = PermissionHelper.class.getSimpleName();

    public static <T extends Context & ActivityCompat.OnRequestPermissionsResultCallback> void requestPermissions(final T context, String[] permissions, int requestCode, String notificationText) {
        ResultReceiver resultReceiver = new ResultReceiver(new Handler(Looper.getMainLooper())) {
            @Override
            protected void onReceiveResult (int resultCode, Bundle resultData) {
                String[] outPermissions = resultData.getStringArray("permissions");
                int[] grantResults = resultData.getIntArray("grantResults");
                context.onRequestPermissionsResult(resultCode, outPermissions, grantResults);
            }
        };

        Intent permIntent = new Intent(context, PermissionRequestActivity.class);
        permIntent.putExtra("resultReceiver", resultReceiver);
        permIntent.putExtra("permissions", permissions);
        permIntent.putExtra("requestCode", requestCode);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addNextIntent(permIntent);

        PendingIntent permPendingIntent =
                stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.alert_dialog_icon)
                .setContentText(notificationText)
                .setOngoing(true)
                //.setCategory(Notification.CATEGORY_STATUS)
                .setAutoCancel(true)
                .setWhen(0)
                .setContentIntent(permPendingIntent)
                .setStyle(null);

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(requestCode, builder.build());
    }

    public static class PermissionRequestActivity extends AppCompatActivity {
        ResultReceiver resultReceiver;
        String[] permissions;
        int requestCode;

        /**
         * Called when the user has made a choice in the permission dialog.
         *
         * This method wraps the responses in a {@link Bundle} and passes it to the {@link ResultReceiver}
         * specified in the {@link Intent} that started the activity, then closes the activity.
         */
        @Override
        public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
            Bundle resultData = new Bundle();
            resultData.putStringArray("permissions", permissions);
            resultData.putIntArray("grantResults", grantResults);
            resultReceiver.send(requestCode, resultData);
            finish();
        }


        /**
         * Called when the activity is started.
         *
         * This method obtains several extras from the {@link Intent} that started the activity: the request
         * code, the requested permissions and the {@link ResultReceiver} which will receive the results.
         * After that, it issues the permission request.
         */
        @Override
        protected void onStart() {
            super.onStart();

            resultReceiver = this.getIntent().getParcelableExtra("resultReceiver");
            permissions = this.getIntent().getStringArrayExtra("permissions");
            requestCode = this.getIntent().getIntExtra("requestCode", 0);

            ActivityCompat.requestPermissions(this, permissions, requestCode);
        }
    }



}
