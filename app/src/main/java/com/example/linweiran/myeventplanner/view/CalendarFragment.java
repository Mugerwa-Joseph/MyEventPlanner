package com.example.linweiran.myeventplanner.view;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.linweiran.myeventplanner.R;
import com.example.linweiran.myeventplanner.controller.EventController;
import com.example.linweiran.myeventplanner.model.Event;
import com.example.linweiran.myeventplanner.model.SimpleEvent;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 */
public class CalendarFragment extends Fragment implements View.OnClickListener{

    private static final String tag = "CalendarFragment";

    private Button seletedDayMonthYearBtn;
    private Button currentMonth;
    private ImageView prevMonth;
    private ImageView nextMonth;
    private GridView calendarView;
    private GridCellAdapter adapter;
    private Calendar calendar;
    private int month, year;
    private final DateFormat dateFormat = new DateFormat();
    private static final String dateTemplate = "MMMM yyyy";


    public CalendarFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_calendar,container,false);
        calendar = Calendar.getInstance(Locale.getDefault());
        month = calendar.get(Calendar.MONTH) + 1;
        year = calendar.get(Calendar.YEAR);
        Log.d(tag, "Calendar Instance: = "+"Month: "+month+" "+"Year: "+year);

        seletedDayMonthYearBtn = (Button) view.findViewById(R.id.selectedDayMonthYear);
        seletedDayMonthYearBtn.setText("Selected:");

        prevMonth = (ImageView) view.findViewById(R.id.prevMonth);
        prevMonth.setOnClickListener(this);

        currentMonth = (Button) view.findViewById(R.id.currentMonth);
        currentMonth.setText(dateFormat.format(dateTemplate,calendar.getTime()));

        nextMonth = (ImageView) view.findViewById(R.id.nextMonth);
        nextMonth.setOnClickListener(this);

        calendarView = (GridView) view.findViewById(R.id.calendar);

        //initialised
        adapter = new GridCellAdapter(getActivity(), R.id.calendar_day_gridcell,month,year);
        adapter.notifyDataSetChanged();
        calendarView.setAdapter(adapter);

        return view;
    }

    private void setGridCellAdapterToDate(int month, int year) {
        adapter = new GridCellAdapter(getActivity(), R.id.calendar_day_gridcell, month, year);
        calendar.set(year,month - 1, calendar.get(Calendar.DAY_OF_MONTH));
        currentMonth.setText(dateFormat.format(dateTemplate,calendar.getTime()));
        adapter.notifyDataSetChanged();
        calendarView.setAdapter(adapter);
    }

    @Override
    public void onClick(View view) {
        if (view == prevMonth)
        {
            if (month <= 1)
            {
                month = 12;
                year --;
            }
            else
            {
                month --;
            }
            Log.d(tag, "Setting Prev Month in GridCellAdapter: "+"Month: "+month+" Year: "+year);
            setGridCellAdapterToDate(month,year);
        }

        if (view == nextMonth)
        {
            if (month > 11)
            {
                month = 1;
                year ++;
            }
            else
            {
                month ++;
            }
            Log.d(tag, "Setting Next Month in GridCellAdapter: "+"Month: "+month+" Year: "+year);
            setGridCellAdapterToDate(month,year);
        }
    }

    @Override
    public void onDestroy() {
        Log.d(tag, "Destroying view...");
        super.onDestroy();
    }

    //inner class
    private class GridCellAdapter extends BaseAdapter implements View.OnClickListener{

        private static final String tag = "GridCellAdapter";
        private final Context context;

        private final List<String> list;
        private static final int DAY_OFFSET = 1;
        private final String[] weekdays = new String[]{"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};
        private final String[] months = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
        private final int[] daysOfMonth = {31,28,31,30,31,30,31,31,30,31,30,31};
        private final int month, year;
        private int daysInMonth, preMonthDays;
        private int currentDayOfMonth;
        private int currentWeekDay;
        private Button gridcell;
        private TextView num_events_per_day;
        private final HashMap eventsPerMonthMap;
        private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy");

        public SimpleEvent event;

        //Days in current Month
        public GridCellAdapter(Context context, int textviewResourceId, int month, int year)
        {
            super();
            this.context = context;
            this.list = new ArrayList<String>();
            this.month = month;
            this.year = year;

            Log.d(tag, "==> Passed in Date For Month: "+month+" "+"Year: "+year);
            Calendar calendar = Calendar.getInstance();
            setCurrentDayOfMonth(calendar.get(Calendar.DAY_OF_MONTH));
            setCurrentWeekDay(calendar.get(Calendar.DAY_OF_WEEK));
            Log.d(tag, "New Calendar:= "+calendar.getTime().toString());
            Log.d(tag, "CurrentDayofWeek: "+getCurrentWeekDay());
            Log.d(tag, "CurrentDayofMonth: "+getCurrentDayOfMonth());

            //print Month
            printMonth(month,year);

            //find number of Events
            eventsPerMonthMap = findNumberOfEventsPerMonth(year,month);

        }

        private String getMonthAsString(int i) {
            return months[i];
        }

        private String getWeekDayAsString(int i) {
            return weekdays[i];
        }

        private int getNumberOfDaysOfMonth(int i) {
            return daysOfMonth[i];
        }

        public String getItem(int position) {
            return list.get(position);
        }

        @Override
        public int getCount() {
            return list.size();
        }

        private void printMonth(int mm, int yy)
        {
            Log.d(tag, "==> printMonth: mm: "+mm+" "+"yy: "+yy);
            //The number of days to leave blank at the start of this month
            int trailingSpaces = 0;
            int leadingSpaces = 0;
            int daysInPrevMonth = 0;
            int prevMonth = 0;
            int prevYear = 0;
            int nextMonth = 0;
            int nextYear = 0;

            int currentMonth = mm -1;
            String currentMonthName = getMonthAsString(currentMonth);
            daysInMonth = getNumberOfDaysOfMonth(currentMonth);

            Log.d(tag, "Current Month: "+" "+currentMonthName+" having "+daysInMonth+" days.");

            //Gregorian Calendar: Minus 1, set to first of month
            GregorianCalendar cal = new GregorianCalendar(yy, currentMonth, 1);
            Log.d(tag, "Gregorian Calendar:= "+cal.getTime().toString());

            if (currentMonth == 11)
            {
                prevMonth = currentMonth - 1;
                daysInPrevMonth = getNumberOfDaysOfMonth(prevMonth);
                nextMonth = 0;
                prevYear = yy;
                nextYear = yy + 1;
                Log.d(tag, "*-> PrevYear: "+prevYear+" PrevMonth: "+prevMonth+" NextMonth: "+nextMonth+" NextYear: "+nextYear);
            }
            else if (currentMonth == 0)
            {
                prevMonth = 11;
                prevYear = yy - 1;
                nextYear = yy;
                daysInPrevMonth = getNumberOfDaysOfMonth(prevMonth);
                nextMonth = 1;
                Log.d(tag,"**--> PrevYear: "+prevYear+" PrevMonth: "+prevMonth+" NextMonth: "+nextMonth+" NextYear: "+nextYear);
            }
            else
            {
                prevMonth = currentMonth - 1;
                nextMonth = currentMonth + 1;
                nextYear = yy;
                prevYear = yy;
                daysInPrevMonth = getNumberOfDaysOfMonth(prevMonth);
                Log.d(tag,"***---> PrevYear: "+prevYear+" PrevMonth: "+prevMonth+" NextMonth: "+nextMonth+" NextYear: "+nextYear);
            }

            //compute how much to leave before the first day of the month
            //getDay() returns 0 for Sunday
            int currentWeekDay = cal.get(Calendar.DAY_OF_WEEK) - 1;
            trailingSpaces = currentWeekDay;

            Log.d(tag, "Week Day: "+currentWeekDay+" is "+getWeekDayAsString(currentWeekDay));
            Log.d(tag, "No. of Trailing space to add: "+trailingSpaces);
            Log.d(tag, "No. of days in previous month: "+daysInPrevMonth);

            if (cal.isLeapYear(cal.get(Calendar.YEAR)) && mm == 2)
            {
                ++ daysInMonth;
            }

            //Trailing Month days
            for (int i = 0; i < trailingSpaces; i++)
            {
                Log.d(tag, "Prev Month:= "+prevMonth+" => "+getMonthAsString(prevMonth)+" "+String.valueOf((daysInPrevMonth - trailingSpaces + DAY_OFFSET) + i));
                list.add(String.valueOf((daysInPrevMonth - trailingSpaces + DAY_OFFSET) + i) + "-GREY" + "-" + prevMonth + "-" + prevYear);
            }

            //current Month days


            int m = calendar.getTime().getMonth();
            for (int i = 1; i <= daysInMonth; i++)
            {
                Log.d(currentMonthName, String.valueOf(i)+" "+getMonthAsString(currentMonth)+" "+yy);
                if (i == getCurrentDayOfMonth() && m == currentMonth)
                {
                    list.add(String.valueOf(i) + "-RED" + "-" + currentMonth + "-" + yy);
                }
                else
                {
                    list.add(String.valueOf(i) + "-WHITE" + "-" + currentMonth + "-" + yy);
                }
            }

            //Leading Month days
            for (int i = 0; i < list.size() % 7; i++)
            {
                Log.d(tag, "Next Month:= " + getMonthAsString(nextMonth));
                list.add(String.valueOf(i+1) + "-GREY" + "-" + nextMonth+"-"+nextYear);
            }
        }

        private HashMap findNumberOfEventsPerMonth(int year, int month)
        {
            HashMap map = new HashMap<String, Integer>();
            List<Event> events = EventController.getSingletonInstance().getEventList();

            for (Event e : events) {
                if (year == e.getStart().get(Calendar.YEAR) && month == e.getStart().get(Calendar.MONTH))
                {

                    map.put(e.getStart(), e);
                }

            }




            return map;
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int position, View view, ViewGroup parent) {

            View row = view;
            if (row == null)
            {
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                row = inflater.inflate(R.layout.calendar_day_gridcell, parent, false);
            }

            //Get a reference to the Day grid cell
            gridcell = (Button) row.findViewById(R.id.calendar_day_gridcell);

            //Account for spacing
            Log.d(tag, "Current Day: " + getCurrentDayOfMonth());
            String[] day_color = list.get(position).split("-");
            String theDay = day_color[0];
            String theMonth = day_color[2];
            String theYear = day_color[3];

            String theYear_Month_Year = theYear+"."+((Integer.parseInt(theMonth))+1)+"."+theDay;

            if ((!eventsPerMonthMap.isEmpty()) && (eventsPerMonthMap != null))
            {
                if (eventsPerMonthMap.containsKey(theYear_Month_Year))
                {
                    num_events_per_day = (TextView) row.findViewById(R.id.num_events_per_day);
                    event = (SimpleEvent) eventsPerMonthMap.get(theYear_Month_Year);
                    gridcell.setBackground(getResources().getDrawable(R.drawable.calendar_bg_orange));
                    gridcell.setOnClickListener(this);
                }
            }

            //Set the Day grid cell
            gridcell.setText(theDay);
            gridcell.setTag(theDay + "-" + theMonth + "-" + theYear);

            Log.d(tag, "Setting GridCell "+theDay+"-"+theMonth+"-"+theYear);

            if (day_color[1].equals("GREY"))
            {
                gridcell.setTextColor(Color.LTGRAY);
            }
            if (day_color[1].equals("WHITE"))
            {
                gridcell.setTextColor(Color.WHITE);
            }
            if (day_color[1].equals("RED"))
            {
                gridcell.setTextColor(getResources().getColor(R.color.colorAccent));
            }

            return row;
        }

        @Override
        public void onClick(View view) {
            String date_month_year = (String) view.getTag();

            Intent intent = new Intent(getActivity(),EditEventActivity.class);
            intent.putExtra("selectedEvent", event.getId());
            intent.putExtra("calledFrom","CalendarFragment");
            startActivity(intent);

            try {
                Date parseDate = dateFormat.parse(date_month_year);
                Log.d(tag, "Parse Date: "+parseDate.toString());
            }
            catch (ParseException e) {
                e.printStackTrace();
            }

        }

        public int getCurrentDayOfMonth()
        {
            return currentDayOfMonth;
        }

        private void setCurrentDayOfMonth(int currentDayOfMonth)
        {
            this.currentDayOfMonth = currentDayOfMonth;
        }

        public void setCurrentWeekDay(int currentWeekDay)
        {
            this.currentWeekDay = currentWeekDay;
        }

        public int getCurrentWeekDay()
        {
            return currentWeekDay;
        }
    }
}
