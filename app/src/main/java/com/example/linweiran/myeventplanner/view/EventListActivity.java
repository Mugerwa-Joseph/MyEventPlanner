package com.example.linweiran.myeventplanner.view;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.linweiran.myeventplanner.R;
import com.example.linweiran.myeventplanner.model.DatabaseOperations;
import com.example.linweiran.myeventplanner.view.model.ViewPagerAdapter;

public class EventListActivity extends AppCompatActivity {

    private static final String DEBUG_TAG = EventListActivity.class.getName();

    Toolbar tabBar;
    TabLayout tabLayout;
    ViewPager viewPager;
    ViewPagerAdapter viewPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_list);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(EventListActivity.this, AddEventActivity.class);
                i.putExtra("callFrom",EventListActivity.class.getName());
               startActivity(i);

            }
        });

        tabBar = (Toolbar) findViewById(R.id.tabBar);
        setSupportActionBar(tabBar);
        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPagerAdapter.addFragments(new ListFragment(), "List View");
        viewPagerAdapter.addFragments(new CalendarFragment(), "Calendar View");
        viewPager.setAdapter(viewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);


        Intent intent = new Intent(this, CheckTimeService.class);
        startService(intent);


        //create database
        DatabaseOperations db = new DatabaseOperations(this);
        SQLiteDatabase sqLiteDatabase = db.getWritableDatabase();
        db.onUpgrade(sqLiteDatabase, 1, 2);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_event_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent i = new Intent(this, MySettingsActivity.class);
            startActivity(i);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
