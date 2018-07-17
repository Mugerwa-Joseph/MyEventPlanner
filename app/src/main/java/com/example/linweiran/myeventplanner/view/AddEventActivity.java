package com.example.linweiran.myeventplanner.view;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.location.Address;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.linweiran.myeventplanner.R;
import com.example.linweiran.myeventplanner.controller.EventController;
import com.example.linweiran.myeventplanner.model.DatabaseOperations;
import com.example.linweiran.myeventplanner.model.Event;
import com.example.linweiran.myeventplanner.model.SimpleEvent;
import com.example.linweiran.myeventplanner.view.model.AttendeesArrayAdapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.UUID;

public class AddEventActivity extends AppCompatActivity {

    private static final String DEBUG_TAG = AddEventActivity.class.getName();

    private Button startDate_pick;
    private Button startTime_pick;
    private Button endDate_pick;
    private Button endTime_pick;
    private Button addAttendee;
    private Button chooseVenue;
    private Button delete_event;

    private EditText eventName;
    private TextView venue;
    private TextView latitude;
    private TextView longitude;
    private EditText note;

    private ExpandListView attendees;

    static final int START_DATE_DIALOG_ID = 0;
    static final int END_DATE_DIALOG_ID = 1;
    static final int START_TIME_DIALOG_ID = 2;
    static final int END_TIME_DIALOG_ID = 3;
    public static boolean DELETE_FLAG = false;

    private static GregorianCalendar start = new GregorianCalendar();
    private static GregorianCalendar end = new GregorianCalendar();

    private static Address location;

    static final int START_BEFORE_END = -1;
    static final int START_AFTER_END = -3;

    Event event;

    private static String nameTxt;
    private static String noteTxt;

    public static ArrayList<String> attendeesList = new ArrayList<String>();

    private AttendeesArrayAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_event);

        //get selected event object from detail view

        eventName = (EditText) findViewById(R.id.title_edit);
        venue = (TextView) findViewById(R.id.venue_edit);
        latitude = (TextView) findViewById(R.id.latitude_edit);
        longitude = (TextView) findViewById(R.id.longitude_edit);
        note = (EditText) findViewById(R.id.note_edit);
        attendees = (ExpandListView) findViewById(R.id.attendeesList_edit);
        addAttendee = (Button) findViewById(R.id.add_attendee);
        chooseVenue = (Button) findViewById(R.id.venue_btn);
        delete_event = (Button) findViewById(R.id.delete_event);
        delete_event.setVisibility(View.INVISIBLE);


        eventName.setText(nameTxt);

        Intent i = getIntent();
        Intent getIntent = getIntent();
        Bundle bundle = getIntent.getExtras();
        String callFrom = bundle.getString("callFrom");
        if (callFrom.equals("MapsActivity"))
            location = i.getParcelableExtra("destination");

        if (location != null)
        {
            venue.setText(location.getAddressLine(0));
            latitude.setText(String.valueOf(location.getLatitude()));
            longitude.setText(String.valueOf(location.getLongitude()));
        }


        note.setText(noteTxt);

        chooseVenue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nameTxt = eventName.getText().toString();
                noteTxt = note.getText().toString();

                Intent intent = new Intent(AddEventActivity.this,MapsActivity.class);
                intent.putExtra("callFrom", "AddEventActivity");
                startActivity(intent);
            }
        });

        addAttendee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nameTxt = eventName.getText().toString();

                noteTxt = note.getText().toString();

                Intent intent = new Intent(AddEventActivity.this,ContactListActivity.class);
                intent.putExtra("selectedContacts", attendeesList);
                intent.putExtra("callFrom","AddEventActivity");
                startActivity(intent);
            }
        });

        adapter = new AttendeesArrayAdapter(this,R.layout.attendees_list_row,attendeesList);
        attendees.setAdapter(adapter);


        //set page title
        TextView activity_title = (TextView) findViewById(R.id.titleView);
        activity_title.setText("Add Event");

        this.showDatePickerDialog();
        this.showTimePickerDialog();

        ImageButton confirm = (ImageButton) findViewById(R.id.ok_button);
        confirm.setOnClickListener(confirmOnClickListener);

    }

    private View.OnClickListener confirmOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            if (compareDate() == START_AFTER_END) {
                Toast.makeText(AddEventActivity.this,"The end time need to be after the start time!", Toast.LENGTH_LONG).show();
                return;
            }


            String eventId = UUID.randomUUID().toString();

            event = new SimpleEvent(null);

            event.setName(eventName.getText().toString());

            event.setVenue(location);

            if (location!=null) {
                event.setLatitude(location.getLatitude());
                event.setLongitude(location.getLongitude());
            }
            event.setNote(note.getText().toString());

            event.setStart(start);
            event.setEnd(end);

            event.setAttendees(attendeesList);

            EventController.getSingletonInstance().addEventObject(event);

            DatabaseOperations dop = new DatabaseOperations(AddEventActivity.this);
            dop.addEvent(dop,event);


            cleanData();


            Intent intent = new Intent(AddEventActivity.this,EventListActivity.class);
            startActivity(intent);

        }
    };

    private void showDatePickerDialog() {
        //set org start date on the button
        SimpleDateFormat sdf = new SimpleDateFormat("dd-M-yyyy");

        startDate_pick = (Button) findViewById(R.id.startDate_edit);

        startDate_pick.setText(sdf.format(start.getTime()));

        //set org end date on the button
        endDate_pick = (Button) findViewById(R.id.endDate_edit);
        endDate_pick.setText(sdf.format(end.getTime()));

        //datePicker dialog --> start date
        startDate_pick.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        showDialog(START_DATE_DIALOG_ID);
                    }
                }
        );
        //datePicker dialog --> end date
        endDate_pick.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        showDialog(END_DATE_DIALOG_ID);
                    }
                }
        );
    }

    private void showTimePickerDialog() {
        SimpleDateFormat sdf = new SimpleDateFormat("H:mm");
        //set org start time on the button
        startTime_pick = (Button) findViewById(R.id.startTime_edit);
        startTime_pick.setText(sdf.format(start.getTime()));

        //set org end time on the button
        endTime_pick = (Button) findViewById(R.id.endTime_edit);

        endTime_pick.setText(sdf.format(end.getTime()));

        //timePicker dialog --> start time
        startTime_pick.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        showDialog(START_TIME_DIALOG_ID);
                    }
                }
        );
        //timePicker dialog --> end time
        endTime_pick.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        showDialog(END_TIME_DIALOG_ID);
                    }
                }
        );
    }


    @Override
    protected Dialog onCreateDialog(int id) {
        if (id == START_DATE_DIALOG_ID) {
            Log.d(DEBUG_TAG, "start date picker: " + start.getTime().toString());
            return new DatePickerDialog(this, startDatePickerListener, start.get(Calendar.YEAR), start.get(Calendar.MONTH), start.get(Calendar.DAY_OF_MONTH));
        }else if(id == END_DATE_DIALOG_ID)
            return new DatePickerDialog(this, endDatePickerListener, end.get(Calendar.YEAR), end.get(Calendar.MONTH), end.get(Calendar.DAY_OF_MONTH));
        else if(id == START_TIME_DIALOG_ID)
            return new TimePickerDialog(this, startTimePickerListener, start.get(Calendar.HOUR_OF_DAY), start.get(Calendar.MINUTE), true);
        else if(id == END_TIME_DIALOG_ID)
            return new TimePickerDialog(this, endTimePickerListener, end.get(Calendar.HOUR_OF_DAY), end.get(Calendar.MINUTE), true);
        return null;
    }

    private DatePickerDialog.OnDateSetListener startDatePickerListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
            startDate_pick.setText(i2+"-"+(i1+1)+"-"+i);
            start.set(i, i1+1, i2);
        }
    };

    private DatePickerDialog.OnDateSetListener endDatePickerListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
            end.set(i, i1+1, i2);
            endDate_pick.setText(i2 +"-"+(i1+1)+"-"+i);
        }
    };

    private TimePickerDialog.OnTimeSetListener startTimePickerListener = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker timePicker, int i, int i1) {
            start.set(Calendar.HOUR, i);
            start.set(Calendar.MINUTE, i1);
            startTime_pick.setText(i+":"+i1);
        }
    };

    private TimePickerDialog.OnTimeSetListener endTimePickerListener = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker timePicker, int i, int i1) {
            end.set(Calendar.HOUR, i);
            end.set(Calendar.MINUTE, i1);
            endTime_pick.setText(i+":"+i1);
        }
    };

    public void cleanData(){
        nameTxt = null;
        location = null;
        noteTxt = null;
        attendeesList = new ArrayList<String>();
        start = new GregorianCalendar();
        end = new GregorianCalendar();
    }

    public int compareDate() {

        if (start.compareTo(end) > 0)
        {
            return START_AFTER_END;
        }
        return START_BEFORE_END;


    }

}
