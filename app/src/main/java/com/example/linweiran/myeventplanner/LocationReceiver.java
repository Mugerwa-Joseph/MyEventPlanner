package com.example.linweiran.myeventplanner;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.Address;

/**
 * Created by linweiran on 8/10/16.
 */

public class LocationReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        Address address = intent.getParcelableExtra("address");
        Intent i = new Intent("passToMapActivity");
        i.putExtra("destination", address);
        context.sendBroadcast(i);

    }
}
