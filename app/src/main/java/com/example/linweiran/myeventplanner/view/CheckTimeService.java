package com.example.linweiran.myeventplanner.view;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Intent;
import android.preference.PreferenceManager;
import android.util.Log;

import com.example.linweiran.myeventplanner.NotificationReceiver;

import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Created by linweiran on 4/10/16.
 */

public class CheckTimeService extends IntentService {

    private static final String DEBUG_TAG = CheckTimeService.class.getName();

    public CheckTimeService() {
        super("");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            Intent i = new Intent(this, NotificationReceiver.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, i, 0);

            AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

            Calendar calendar = new GregorianCalendar();
            int alarmTime = Integer.valueOf(PreferenceManager.getDefaultSharedPreferences(this).getString("default_alarm_time","5")) * 60;
            alarmManager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, 1000, alarmTime * 1000, pendingIntent);
            Log.d(DEBUG_TAG, "alarm setted");
        }
        else {
            Log.e(DEBUG_TAG, "Alarm manager service intent is null");
        }
    }

}
