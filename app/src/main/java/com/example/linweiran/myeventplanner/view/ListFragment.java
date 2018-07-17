package com.example.linweiran.myeventplanner.view;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.ToggleButton;

import com.example.linweiran.myeventplanner.R;
import com.example.linweiran.myeventplanner.controller.EventController;
import com.example.linweiran.myeventplanner.model.Event;
import com.example.linweiran.myeventplanner.model.SimpleEvent;
import com.example.linweiran.myeventplanner.view.model.EventArrayAdapter;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class ListFragment extends Fragment {

    ListView eventListView;
    ToggleButton sort;
    private static EventArrayAdapter adapter;

    private static List<Event> events;

    public ListFragment() {
        // Required empty public constructor
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_list,container,false);
        eventListView = (ListView) view.findViewById(R.id.eventListView);
        events = EventController.getSingletonInstance().getEventList();


        sort = (ToggleButton) view.findViewById(R.id.toggleButton);
        sort.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    // the toggle is enabled
                    sortList(-1);
                } else {
                    // the toggle is disabled
                    sortList(1);
                }
            }
        });

        this.adapter = new EventArrayAdapter(getActivity(),R.layout.event_list_row,events);
        eventListView.setAdapter(adapter);

        eventListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Event e = (SimpleEvent) eventListView.getItemAtPosition(i);
                Intent intent = new Intent(getActivity(),EditEventActivity.class);
                intent.putExtra("selectedEvent", e.getId());
                intent.putExtra("calledFrom", "ListFragment");
                startActivity(intent);

            }
        });
        return view;

    }


    public static void sortList(int order) {
        Collections.sort(events, new Sorter(order));
        adapter.notifyDataSetChanged();
    }

    static class Sorter implements Comparator<Event> {
        int order = -1;

        Sorter(int order) {
            this.order = order;
        }

        @Override
        public int compare(Event e1, Event e2) {

            if (e1.getStart().compareTo(e2.getStart()) == 0) return 0;
            else if (e1.getStart().compareTo(e2.getStart()) > 0) return order;
            else return (-1*order);

        }
    }
}
