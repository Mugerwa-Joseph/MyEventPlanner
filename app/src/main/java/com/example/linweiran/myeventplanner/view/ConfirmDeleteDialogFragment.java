package com.example.linweiran.myeventplanner.view;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import com.example.linweiran.myeventplanner.R;
import com.example.linweiran.myeventplanner.controller.EventController;
import com.example.linweiran.myeventplanner.model.SimpleEvent;

/**
 * Created by linweiran on 13/08/2016.
 */
public class ConfirmDeleteDialogFragment extends Activity
{
    @Override
    public Dialog onCreateDialog(int id)
    {
//        super.onCreate(savedInstanceState);

        Bundle bundle = getIntent().getExtras();
        String eventId = bundle.getString("event");
        final String name = bundle.getString("attendee");
        final SimpleEvent event = (SimpleEvent) EventController.getSingletonInstance().getEventById(eventId);

        Dialog dialog = new AlertDialog.Builder(ConfirmDeleteDialogFragment.this)
                .setIcon(R.drawable.alert_dialog_icon)
                .setTitle("Delete the attendee?")
                .setPositiveButton("Delete",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton)
                            {
                                event.deleteAttendee(name);
                        /* User clicked OK so do some stuff */
                            }
                        })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton)
                            {

                        /* User clicked Cancel so do some stuff */
                            }
                        }).create();

        return dialog;
    }
}
