package com.example.couchpotatosplan.month;

import java.util.ArrayList;

public class ExcludeEventList {
    public static ArrayList<ExcludeEvent> eventsList = new ArrayList<>();

    public static ArrayList<ExcludeEvent> eventsForDate(String date)
    {
        ArrayList<ExcludeEvent> events = new ArrayList<>();


        for(ExcludeEvent event : eventsList)
        {
            events.add(event);
        }

        return events;
    }

}