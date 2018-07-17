package com.example.linweiran.myeventplanner;

import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.List;

/**
 * Created by linweiran on 4/10/16.
 */

public class MyTask extends AsyncTask<Void, Void, Void> {

    Context context;
    LatLng location;

    public MyTask(Context context, LatLng location) {
        this.context = context;
        this.location = location;
    }

    @Override
    protected void onPreExecute() {

        List<Address> addressList = null;

        Geocoder geocoder = new Geocoder(context);

        try {
            addressList = geocoder.getFromLocation(location.latitude, location.longitude, 1);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (addressList == null) {
            Toast.makeText(context, "No result searched...", Toast.LENGTH_SHORT).show();
            return;
        }
        if (addressList.isEmpty()) {
            Toast.makeText(context, "No result searched...", Toast.LENGTH_SHORT).show();
            return;
        }

        Address address = addressList.get(0);

        System.out.println("\ngot address"+address.toString());

        Intent intent = new Intent();
        intent.setAction("com.example.linweiran.myeventplanner.LocationReceiver");
        intent.putExtra("address",address);
        context.sendBroadcast(intent);
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected Void doInBackground(Void... params) {
        return null;
    }

}
