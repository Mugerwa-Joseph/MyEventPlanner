package com.example.linweiran.myeventplanner.view.model;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.linweiran.myeventplanner.R;
import com.example.linweiran.myeventplanner.model.Event;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by linweiran on 20/08/2016.
 */
public class EventArrayAdapter extends ArrayAdapter<Event>
{
    private Activity activity;

    private List<Event> events;

    public EventArrayAdapter(Activity activity, int resource, List<Event> objects) {
        super(activity, resource, objects);
        this.events = objects;
        this.activity = activity;
    }

    static class EventHandler
    {
        TextView eventName;
        TextView startDate;
    }

    @Override
    public int getCount() {
        return events.size();
    }

    @Override
    public void add(Event object) {
        super.add(object);
        events.add(object);
    }

    @Override
    public Event getItem(int position) {
        return events.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        EventHandler handler;
        if(convertView == null)
        {
            LayoutInflater inflater = activity.getLayoutInflater();
            convertView = inflater.inflate(R.layout.event_list_row, parent, false);
            handler = new EventHandler();
            handler.eventName = (TextView) convertView.findViewById(R.id.eventText);
            handler.startDate = (TextView) convertView.findViewById(R.id.dateText);
            convertView.setTag(handler);
        }
        else
        {
            handler = (EventHandler) convertView.getTag();
        }

        Event event = getItem(position);

        handler.eventName.setText(event.getName());
        SimpleDateFormat sdf = new SimpleDateFormat("dd-M-yyyy hh:mm");
        handler.startDate.setText(sdf.format(event.getStart().getTime())+" - "+sdf.format(event.getEnd().getTime()));

        return convertView;
    }
}