package com.example.linweiran.myeventplanner.model;

import android.location.Address;

import java.util.ArrayList;
import java.util.GregorianCalendar;

/**
 * Created by linweiran on 20/08/2016.
 */
public class SimpleEvent implements Event
{
    private String id;
    private String name;

    private int startYear, startMonth, startDay;
    private int endYear, endMonth, endDay;
    private int startHour, startMinute;
    private int endHour, endMinute;

    private GregorianCalendar start;
    private GregorianCalendar end;

    private Address venue;
    private Double latitude;
    private Double longitude;
    private String note;
    private ArrayList<String> attendees;

    static int EVENT_ID = 0;

    public SimpleEvent(String name) {
        this.name = name;
        this.id = String.valueOf(EVENT_ID);
        this.attendees = new ArrayList<String>();
        EVENT_ID++;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public Address getVenue() {
        return venue;
    }

    public void setVenue(Address venue) {
        this.venue = venue;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    @Override
    public ArrayList<String> getAttendees() {
        return attendees;
    }

    @Override
    public void setAttendees(ArrayList<String> attendees) {
        this.attendees = attendees;
    }

    public void deleteAttendee(String attendee) {
        for (String s:this.attendees) {
            if (s.equals(attendee))
                this.attendees.remove(s);
        }
    }

    public GregorianCalendar getStart() {
        return start;
    }

    public void setStart(GregorianCalendar start) {
        this.start = start;
    }

    public GregorianCalendar getEnd() {
        return end;
    }

    public void setEnd(GregorianCalendar end) {
        this.end = end;
    }
}

