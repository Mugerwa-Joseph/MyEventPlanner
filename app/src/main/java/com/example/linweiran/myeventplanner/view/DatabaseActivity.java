package com.example.linweiran.myeventplanner.view;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.linweiran.myeventplanner.R;
import com.example.linweiran.myeventplanner.model.Database;

import java.util.Arrays;
import java.util.Locale;


public class DatabaseActivity extends AppCompatActivity {

    private static final String DEBUG_TAG = DatabaseActivity.class.getName();

    // SQL CREATE AND DROP TABLE STATEMENTS
    private static final String CREATE_EVENT_TABLE = "CREATE TABLE tbl_events (id INTEGER PRIMARY KEY AUTOINCREMENT , name TEXT);";


    // Database instance
    private SQLiteDatabase mDatabase;

    public SQLiteDatabase getDatabase(){
        return mDatabase;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_database);

        runDatabase();
    }

    public void runDatabase() {
        Log.i(DEBUG_TAG, "Begin to run Database");

        if (Arrays.binarySearch(databaseList(), Database.TableInfo.DATABASE_NAME) >= 0)
        {
            // Delete the old database file, if it exists
            deleteDatabase(Database.TableInfo.DATABASE_NAME);
        }

        // create a new database
        mDatabase = openOrCreateDatabase(Database.TableInfo.DATABASE_NAME,
                SQLiteDatabase.CREATE_IF_NECESSARY, null);

        // CONFIGURATION INFO
        mDatabase.setLocale(Locale.getDefault()); // Set the locale
        mDatabase.setVersion(1); // Sets the database version.

        // Log some information about our database
        Log.i(DEBUG_TAG, "Created database: " + mDatabase.getPath());
        Log.i(DEBUG_TAG, "Database Version: " + mDatabase.getVersion());
        Log.i(DEBUG_TAG, "Database Page Size: " + mDatabase.getPageSize());
        Log.i(DEBUG_TAG, "Database Max Size: " + mDatabase.getMaximumSize());

        Log.i(DEBUG_TAG, "Database Open?  " + mDatabase.isOpen());
        Log.i(DEBUG_TAG, "Database readonly?  " + mDatabase.isReadOnly());
        Log.i(DEBUG_TAG,
                "Database Locked by current thread?  "
                        + mDatabase.isDbLockedByCurrentThread());

        // CREATE TABLES
        Log.i(DEBUG_TAG, "Create the tbl_events table using execSQL()");
        mDatabase.execSQL(CREATE_EVENT_TABLE);

    }
}
