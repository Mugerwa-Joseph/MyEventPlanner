package com.example.linweiran.myeventplanner.model;

import android.location.Address;

import java.util.ArrayList;
import java.util.GregorianCalendar;

/**
 * Created by linweiran on 20/08/2016.
 */
public interface Event {
    String getId();

    String getName();

    void setName(String name);

    Address getVenue();

    void setVenue(Address venue);

    Double getLatitude();

    void setLatitude(Double latitude);

    Double getLongitude();

    void setLongitude(Double longitude);

    String getNote();

    void setNote(String note);


    ArrayList<String> getAttendees();

    void setAttendees(ArrayList<String> attendees);

    GregorianCalendar getStart();
    void setStart(GregorianCalendar start);

    GregorianCalendar getEnd();
    void setEnd(GregorianCalendar end);

}
