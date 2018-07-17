package com.example.linweiran.myeventplanner.view;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.linweiran.myeventplanner.R;
import com.example.linweiran.myeventplanner.controller.EventController;
import com.example.linweiran.myeventplanner.model.Event;
import com.example.linweiran.myeventplanner.view.model.CheckBoxAdapter;

import java.util.ArrayList;

public class ContactListActivity extends Activity {

    private ListView contactListView;
    private Button submit;

    private Cursor cursor;
    private ArrayList<String> contacts;
    public static ArrayList<String> selectedContacts;

    private Event event;

    private String callFrom;

    final private int REQUEST_CODE_ASK_PERMISSIONS = 123;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_list);

        contactListView = (ListView) findViewById(R.id.list);
        submit = (Button) findViewById(R.id.submit);

        //get selected contacts
        Intent getIntent = getIntent();
        Bundle bundle = getIntent.getExtras();
        selectedContacts = bundle.getStringArrayList("selectedContacts");
        callFrom = bundle.getString("callFrom");

        contacts = new ArrayList<String>();

        int hasWriteContactsPermission
                = checkSelfPermission(Manifest.permission.WRITE_CONTACTS);

        if(hasWriteContactsPermission
                != PackageManager.PERMISSION_GRANTED) {

            requestPermissions(new String[] {Manifest.permission.WRITE_CONTACTS}, REQUEST_CODE_ASK_PERMISSIONS);

            return;
        }

        cursor = getContentResolver().query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);

        while (cursor.moveToNext())
        {
            String id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
            String name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
            contacts.add(name);
        }

        contactListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

//        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,R.layout.contact_list_row, R.id.contactName, contacts);
        CheckBoxAdapter adapter = new CheckBoxAdapter(this,R.layout.contact_list_row, contacts);
        contactListView.setAdapter(adapter);

        contactListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String selectedItem = ((TextView) view).getText().toString();
                if (selectedContacts.contains(selectedItem))
                {
                    //unchecked item
                    selectedContacts.remove(selectedItem);
                }
                else {
                    //checked item
                    selectedContacts.add(selectedItem);
                }
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent();
                if (callFrom.equals("AddEventActivity")){
                    intent = new Intent(ContactListActivity.this,AddEventActivity.class);
                    AddEventActivity.attendeesList = selectedContacts;
                }
                else if (callFrom.equals("EditEventActivity")){
                    intent = new Intent(ContactListActivity.this,EditEventActivity.class);
                    EditEventActivity.attendeesList = selectedContacts;
                }
                intent.putExtra("callFrom", "ContactListActivity");
                startActivity(intent);
            }
        });
    }
}
