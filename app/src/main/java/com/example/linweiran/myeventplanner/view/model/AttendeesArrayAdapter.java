package com.example.linweiran.myeventplanner.view.model;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.linweiran.myeventplanner.R;

import java.util.List;

/**
 * Created by linweiran on 20/08/2016.
 */
public class AttendeesArrayAdapter extends ArrayAdapter<String>
{

    private Activity activity;

    private List<String> attendees;


    public AttendeesArrayAdapter(Activity activity, int resource, List<String> objects) {
        super(activity, resource, objects);
        this.activity = activity;
        this.attendees = objects;
    }

    static class ContactHandler
    {
        TextView attendee_name;
    }

    @Override
    public String getItem(int position) {
        return attendees.get(position);
    }

    @Override
    public void remove(String object) {
        super.remove(object);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ContactHandler handler;
        if (convertView == null)
        {
            LayoutInflater inflater = activity.getLayoutInflater();
            convertView = inflater.inflate(R.layout.attendees_list_row, parent, false);
            handler = new ContactHandler();
            handler.attendee_name = (TextView) convertView.findViewById(R.id.attendee_name);
            convertView.setTag(handler);
        }
        else
        {
            handler = (ContactHandler) convertView.getTag();
        }

        final String attendee = getItem(position);

        handler.attendee_name.setText(attendee);

        return convertView;
    }
}
