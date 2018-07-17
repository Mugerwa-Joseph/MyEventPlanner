package com.example.linweiran.myeventplanner;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.AsyncTask;
import android.util.Log;

import com.example.linweiran.myeventplanner.controller.EventController;
import com.example.linweiran.myeventplanner.model.Event;
import com.example.linweiran.myeventplanner.view.EditEventActivity;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * Created by linweiran on 9/10/16.
 */

public class CalculateTimeTask extends AsyncTask<Location, Void, Void> {

    Context context;
    List<Event> events;

    private static final String DEBUG_TAG = CalculateTimeTask.class.getName();

    int id;

    public CalculateTimeTask(Context context) {
        this.context = context;
        this.events = EventController.getSingletonInstance().getEventList();
    }

    @Override
    protected Void doInBackground(Location... locations) {
        //call calculateTime()
        Location cur_location = locations[0];
        this.calculateTime(cur_location);
        return null;
    }

    private void calculateTime(Location cur_location) {
        Date date = new Date();
        GregorianCalendar calendar = new GregorianCalendar();

        for (Event event : events) {
            Double dest_lat = event.getLatitude();
            Double dest_lng = event.getLongitude();

            //Default 15 minutes distance threshold (safety margin) for the alarm
            int duration = googleDistanceMatrix(cur_location, dest_lat, dest_lng) + (15 * 60);
            Log.d(DEBUG_TAG, "duration: "+duration);
            GregorianCalendar current = new GregorianCalendar();
            Log.d(DEBUG_TAG, "now: "+current.getTime());
//            String startInString = event.format_startDate();
//            Log.d(DEBUG_TAG, "event start: "+startInString);
//            try {
//                date = sdf.parse(startInString);
////                calendar.setTime(date);
//                Log.d(DEBUG_TAG,"event start: "+date.getTime());
//
//            } catch (ParseException e) {
//                e.printStackTrace();
//            }
            long remaining_time = (event.getStart().getTimeInMillis() - current.getTimeInMillis())/1000;
            Log.d(DEBUG_TAG, "start time in millis: "+calendar.getTimeInMillis());
            Log.d(DEBUG_TAG, "current time in millis: "+current.getTimeInMillis());
            Log.d(DEBUG_TAG, "remaining time: "+remaining_time);
            Log.d(DEBUG_TAG, "remaing - duration: "+String.valueOf(remaining_time/60 - duration));
//            Log.d(DEBUG_TAG,String.valueOf(cur_location.getLatitude()+cur_location.getLongitude()));
            if ((remaining_time >= 0) && (remaining_time/60 - duration) < 0) {
                Log.d(DEBUG_TAG, "push notification");
                onFireNotification(event);
            }
        }

    }

    private void onFireNotification(Event event) {

        SimpleDateFormat sdf = new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
        MyEventNotification myEventNotification = new MyEventNotification(context,
                new Intent(context, EditEventActivity.class).putExtra("selectedEvent", event.getId()),
                event.getName(), event.getVenue().getAddressLine(0)+"\n"+sdf.format(event.getStart().getTime())
                );
        NotificationManager notiMgr = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notiMgr.notify(id++, myEventNotification.getNotificationBuilder().build());
    }

    private int googleDistanceMatrix(Location cur_location, Double dest_lat, Double dest_lng) {
        int duration = 0;
        HttpURLConnection con = null;

        String url_str = "https://maps.googleapis.com/maps/api/distancematrix/json?origins="
                + cur_location.getLatitude() + ","
                + cur_location.getLongitude() + "&destinations="
                + dest_lat + ","
                + dest_lng
                + "&key=AIzaSyCClh5aXlDKDkexkSf7iS_DsFDBt-jZiGE";

        Log.d(DEBUG_TAG, url_str);

        try {
            URL url = new URL(url_str);
            con = (HttpURLConnection) url.openConnection();
            InputStream in = new BufferedInputStream(con.getInputStream(), 8192);
            ParseResponse response = new ParseResponse();
            response.loadJsonStream(in);

            Log.d(DEBUG_TAG, "response topStatus: "+response.getDistance().getTopStatus());
            Log.d(DEBUG_TAG, "response elementStatus: "+response.getDistance().getElementStatus());

            if (response.getDistance().getTopStatus().equals("OK") && response.getDistance().getElementStatus().equals("OK")) {
                duration = response.getDistance().getDuration();
                Log.d(DEBUG_TAG, "duration: "+duration);
            }
            else {
                //get distance matrix response failed
//                Toast.makeText(context, "No response from Google Distance Matrix", Toast.LENGTH_SHORT).show();
                Log.d(DEBUG_TAG, "No response from Google Distance Matrix");
            }


        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            con.disconnect();
        }

        return duration;
    }
}
