package com.example.linweiran.myeventplanner.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by linweiran on 21/09/2016.
 */
public class DatabaseOperations extends SQLiteOpenHelper {

    public static final int VERSION = 1;
    public static final String DEBUG_TAG = DatabaseOperations.class.getName();

    SQLiteDatabase mDatabase;

    // SQL create table statement
    public String CREATE_EVENT_TABLE = "CREATE TABLE "+ Database.TableInfo.TABLE_EVENT+"("
            + Database.TableInfo.EVENT_ID+" INTEGER PRIMARY KEY AUTOINCREMENT,"
            + Database.TableInfo.EVENT_NAME+" TEXT,"
            + Database.TableInfo.EVENT_LAT+" TEXT"
            + Database.TableInfo.EVENT_LNG+" TEXT"
            + Database.TableInfo.EVENT_START+" INTEGER"
            + Database.TableInfo.EVENT_END+" INTEGER"
            +");";

    public DatabaseOperations(Context context) {
        super(context, Database.TableInfo.DATABASE_NAME, null, VERSION);
        mDatabase = this.getWritableDatabase();
    }


    @Override
    public void onCreate(SQLiteDatabase mDatabase) {
        mDatabase.execSQL(CREATE_EVENT_TABLE);
        Log.d(DEBUG_TAG,"created table");
        printDbInfo(mDatabase);
    }

    @Override
    public void onUpgrade(SQLiteDatabase mDatabase, int i, int i1) {
//        mDatabase.execSQL("DROP TABLE IF EXISTS "+ Database.TableInfo.TABLE_EVENT);
//        mDatabase.execSQL("DROP TABLE IF EXISTS "+ Database.TableInfo.TABLE_ATTENDEES);
//        Log.d(DEBUG_TAG, "drop table");
//        onCreate(mDatabase);
    }

    public void printDbInfo(SQLiteDatabase mDatabase) {

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

    }

    public void addEvent(DatabaseOperations dop, Event event) {
        ContentValues cv = new ContentValues();
        cv.put(Database.TableInfo.EVENT_ID, event.getId());
        cv.put(Database.TableInfo.EVENT_NAME, event.getName());
        cv.put(Database.TableInfo.EVENT_START, event.getStart().getTimeInMillis());
        cv.put(Database.TableInfo.EVENT_END, event.getEnd().getTimeInMillis());
        cv.put(Database.TableInfo.EVENT_LAT, event.getLatitude());
        cv.put(Database.TableInfo.EVENT_LNG, event.getLongitude());
        mDatabase.insert(Database.TableInfo.TABLE_EVENT, null, cv);
        Cursor c = mDatabase.query(Database.TableInfo.TABLE_EVENT, null, null, null, null, null, null);
        LogCursorInfo(c);
        c.close();
    }

    public void LogCursorInfo(Cursor c) {
        Log.i(DEBUG_TAG, "*** Cursor Begin *** " + " Results:" + c.getCount()
                + " Columns: " + c.getColumnCount());
        // Print column names
        String rowHeaders = "|| ";
        for (int i = 0; i < c.getColumnCount(); i++)
        {

            rowHeaders = rowHeaders.concat(c.getColumnName(i) + " || ");
        }
        Log.i(DEBUG_TAG, "COLUMNS " + rowHeaders);

        // Print records
        c.moveToFirst();
        while (c.isAfterLast() == false)
        {
            String rowResults = "|| ";
            for (int i = 0; i < c.getColumnCount(); i++)
            {
                rowResults = rowResults.concat(c.getString(i) + " || ");
            }
            Log.i(DEBUG_TAG, "Row " + c.getPosition() + ": " + rowResults);

            c.moveToNext();
        }
        Log.i(DEBUG_TAG, "*** Cursor End ***");
    }
}
