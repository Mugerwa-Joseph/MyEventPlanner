package com.example.linweiran.myeventplanner.view.model;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;

import com.example.linweiran.myeventplanner.R;
import com.example.linweiran.myeventplanner.view.ContactListActivity;

import java.util.ArrayList;

/**
 * Created by linweiran on 20/08/2016.
 */
public class CheckBoxAdapter extends ArrayAdapter{

    private Activity activity;
    private ArrayList<String> contacts;

    public CheckBoxAdapter(Activity activity, int resource, ArrayList<String> contacts) {
        super(activity, resource, contacts);
        this.activity = activity;
        this.contacts = contacts;
    }

    static class Handler {
        CheckedTextView contactName;
    }

    @Override
    public int getCount() {
        return contacts.size();
    }

    @Override
    public Object getItem(int position) {
        return contacts.get(position);
    }

    @Override
    public void add(Object object) {
        super.add(object);
        contacts.add(object.toString());
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Handler handler;
        if (convertView == null) {
            LayoutInflater inflater = activity.getLayoutInflater();
            convertView = inflater.inflate(R.layout.contact_list_row, parent, false);
            handler = new Handler();
            handler.contactName = (CheckedTextView) convertView.findViewById(R.id.contactName);
            convertView.setTag(handler);
        }
        else {
            handler = (Handler) convertView.getTag();
        }

        String name = getItem(position).toString();

        handler.contactName.setText(name);
        if (ContactListActivity.selectedContacts.contains(name))
        {
            handler.contactName.setChecked(true);
        }

        return convertView;
    }
}
