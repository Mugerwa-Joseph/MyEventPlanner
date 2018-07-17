package com.example.linweiran.myeventplanner.controller;

import com.example.linweiran.myeventplanner.model.Event;

import java.util.ArrayList;

/**
 * Created by linweiran on 20/08/2016.
 */
public class EventController
{
    private ArrayList<Event> events;

    private static EventController singletonInstance;

    public static EventController getSingletonInstance()
    {
        if(singletonInstance == null)
            singletonInstance = new EventController();
        return singletonInstance;
    }

    public EventController() {
        this.events = new ArrayList<Event>();
    }

    public boolean addEventObject(Event e)
    {
        return this.events.add(e);
    }


    public ArrayList<Event> getEventList() {
        return this.events;
    }

    public Event getEventById(String id)
    {
        for(Event event : events)
            if(event.getId().equals(id))
                return event;

        return null;
    }

//    public void loadAllEvents(Context context) {
//        // Load all events from database
//        this.events = (ArrayList<Event>) new LoadAllEvents(context).fetchAllEvent();
//    }

//    public void addEvent(Event event, Context context) {
//        if (event!=null) {
//            events.add(event);
//            (new InsertEvent(context)).execute(event);
//        }
//    }

}
