package com.example.linweiran.myeventplanner.view;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.linweiran.myeventplanner.LocationReceiver;
import com.example.linweiran.myeventplanner.MapService;
import com.example.linweiran.myeventplanner.MyTask;
import com.example.linweiran.myeventplanner.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import static com.google.android.gms.location.LocationServices.FusedLocationApi;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    final private int REQUEST_CODE_ASK_PERMISSIONS = 123;
    LatLng latLng = new LatLng(-37.865390, 144.976750);

    EditText location_tf;
    String callFrom;

    Address destination;

    MyTask myTask = null;

    private GoogleMap mMap;
    MarkerOptions markerOption;
    LocationReceiver locationReceiver = new LocationReceiver();

    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            destination = intent.getParcelableExtra("destination");

            setDestination(destination);

            mMap.clear();

            markerOption
                    .position(new LatLng(destination.getLatitude(), destination.getLongitude()))
                    .title(destination.getFeatureName());
            mMap.addMarker(markerOption);
            mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
        }
    };

    public void setDestination(Address address){
        this.destination = address;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps2);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        callFrom = getIntent().getStringExtra("callFrom");

        Button search_bt = (Button) findViewById(R.id.search_button);

        IntentFilter intentFilter = new IntentFilter("com.example.linweiran.myeventplanner.LocationReceiver");
        registerReceiver(locationReceiver,intentFilter);

        IntentFilter filter = new IntentFilter("passToMapActivity");
        registerReceiver(broadcastReceiver,filter);

        location_tf = (EditText) findViewById(R.id.location_tf);

        search_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String user_input;
                user_input = location_tf.getText().toString();

                if (user_input != null || !user_input.equals("")) {

                    String[] parseLocation = user_input.split(",");
                    latLng = new LatLng(Double.parseDouble(parseLocation[0]), Double.parseDouble(parseLocation[1]));
                    onSearchLocation(latLng);
                }
            }
        });


    }

    public void onSearchLocation(LatLng location) {
        myTask = new MyTask(MapsActivity.this, location);
        myTask.execute();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(locationReceiver);
        unregisterReceiver(broadcastReceiver);
        super.onDestroy();
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near RMIT, Melbourne, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Rmit and move the camera

        System.out.println("set map");
        markerOption = new MarkerOptions().position(latLng).title("Acland Street");
        mMap.addMarker(markerOption);

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16));
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            this.askForPermission();

            return;
        }
        mMap.setMyLocationEnabled(true);
        mMap.setOnMarkerClickListener(this);

    }

    private void askForPermission() {
        int hasWriteMyLocationPermission = ActivityCompat.checkSelfPermission(MapsActivity.this, Manifest.permission.ACCESS_FINE_LOCATION);
        if (hasWriteMyLocationPermission != PackageManager.PERMISSION_GRANTED) {
            if(!shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)) {
                showMessageOKCancel("You need to allow access to current location",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION},REQUEST_CODE_ASK_PERMISSIONS);
                            }
                        });
                return;
            }
            requestPermissions(new String[] {Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_CODE_ASK_PERMISSIONS);
            return;
        }

    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(MapsActivity.this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

    @Override
    public boolean onMarkerClick(Marker marker) {

        Intent intent = new Intent();

        if (callFrom.equals("AddEventActivity"))
             intent = new Intent(this,AddEventActivity.class);
        else if (callFrom.equals("EditEventActivity"))
            intent = new Intent(this, EditEventActivity.class);
        intent.putExtra("callFrom", "MapsActivity");
        intent.putExtra("destination", destination);

        if (myTask != null)
            myTask.cancel(true);

        startActivity(intent);

        return true;

    }
}
