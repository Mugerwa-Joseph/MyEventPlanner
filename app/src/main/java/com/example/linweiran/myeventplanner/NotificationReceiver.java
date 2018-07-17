package com.example.linweiran.myeventplanner;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;

import static com.google.android.gms.location.LocationServices.API;
import static com.google.android.gms.location.LocationServices.FusedLocationApi;

/**
 * Created by linweiran on 9/10/16.
 */

public class NotificationReceiver extends BroadcastReceiver implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener
{

    GoogleApiClient googleApiClient;
    Context context;

    final private int REQUEST_CODE_ASK_PERMISSIONS = 123;


    @Override
    public void onReceive(Context context, Intent intent) {

        // check connection
        this.context = context;
        if (isConnect(context)) {
            // create & connect GoogleApiClient
            googleApiClient = new GoogleApiClient.Builder(context)
                    .addApi(API)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .build();

            googleApiClient.connect();
        } else {
            // show error message
            Toast.makeText(context, "No available network connection.", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean isConnect(Context context) {

        ConnectivityManager cManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cManager.getActiveNetworkInfo();

        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        } else {
            return false;
        }

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

        if (context.checkSelfPermission(android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && context.checkSelfPermission(android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.

            int hasWriteMyLocationPermission = context.checkSelfPermission(android.Manifest.permission.ACCESS_FINE_LOCATION);
            if (hasWriteMyLocationPermission != PackageManager.PERMISSION_GRANTED) {
                showMessageOKCancel("You need to allow access to current location",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ActivityCompat.requestPermissions((Activity)context, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},REQUEST_CODE_ASK_PERMISSIONS);
                            }
                        });

            }

            return;
        }
        Location cur_location = FusedLocationApi.getLastLocation(googleApiClient);



        if (cur_location != null) {
            //execute calculateTime task
            new CalculateTimeTask(context).execute(cur_location);
        }
    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(context)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
