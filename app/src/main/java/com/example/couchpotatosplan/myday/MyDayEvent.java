package com.example.couchpotatosplan.myday;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

public class MyDayEvent {
    public static ArrayList<MyDayEvent> eventsList = new ArrayList<>();

    public static ArrayList<MyDayEvent> eventsForDate()
    {
        ArrayList<MyDayEvent> events = new ArrayList<>();

        for(MyDayEvent event : eventsList)
        {
            events.add(event);
        }

        return events;
    }


    private String content;
    private int time;

    public MyDayEvent(int time, String content)
    {
        this.time = time;
        this.content = content;
    }

    public String getContent()
    {
        return content;
    }

    public void setContent(String content)
    {
        this.content = content;
    }

    public int getTime()
    {
        return time;
    }

    public void setTime(int time)
    {
        this.time = time;
    }
}
