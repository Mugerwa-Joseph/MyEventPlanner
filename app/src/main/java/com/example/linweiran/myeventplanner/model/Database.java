package com.example.linweiran.myeventplanner.model;

import android.provider.BaseColumns;

/**
 * Created by linweiran on 18/09/2016.
 */
public class Database {

    public Database()
    {

    }

    // inner class
    public static abstract class TableInfo implements BaseColumns
    {
        // column names
        public static final String EVENT_ID = "event_id";
        public static final String EVENT_NAME = "event_name";
        public static final String EVENT_START = "event_start";
        public static final String EVENT_END = "event_end";
        public static final String EVENT_LAT = "event_lat";
        public static final String EVENT_LNG = "event_lng";
        public static final String EVENT_NOTE = "event_note";
        public static final String EVENT_ATTENDEES = "attendees";

        // TODO: 18/09/2016 other event's attributes 


        // database & table name
        public static final String DATABASE_NAME = "my_event_planner";
        public static final String TABLE_EVENT = "event";
        public static final String TABLE_ATTENDEES = "attendees";

    }
}
