package com.example.linweiran.myeventplanner;

import android.app.Service;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.os.IBinder;
import android.widget.Toast;

import com.example.linweiran.myeventplanner.view.MapsActivity;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;

/**
 * Created by linweiran on 7/10/16.
 */

public class MapService extends Service {
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        String location = (String) intent.getExtras().get("location");
        List<Address> addressList = null;

        Geocoder geocoder = new Geocoder(this);

        try {
            addressList = geocoder.getFromLocationName(location, 1);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Intent intent_back = new Intent(MapService.this, MapsActivity.class);


        Bundle bundle = new Bundle();
        bundle.putSerializable("addressList", (Serializable) addressList);
        intent_back.putExtra("bundel", bundle);
        intent_back.setFlags(Intent.FLAG_ACTIVITY_LAUNCHED_FROM_HISTORY);

        startActivity(intent_back);


        Toast.makeText(this, "Service started...", Toast.LENGTH_SHORT).show();

        return START_STICKY;
    }

}

