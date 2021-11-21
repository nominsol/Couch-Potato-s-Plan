package com.example.couchpotatosplan.myday;

import static com.example.couchpotatosplan.weekly.CalendarUtils.formattedDate;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.couchpotatosplan.month.ExcludeEvent;
import com.example.couchpotatosplan.month.ExcludeEventList;
import com.example.couchpotatosplan.month.FixEvent;
import com.example.couchpotatosplan.month.FixEventList;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.IgnoreExtraProperties;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

@IgnoreExtraProperties
public class MyDayEventList {
    public static ArrayList<MyDayEvent> eventsList = new ArrayList<>();
    private static DatabaseReference mDatabase;
    private static long postNum;
    public static ArrayList<MyDayEvent> eventsForDate(String date)
    {
        ArrayList<MyDayEvent> events = new ArrayList<>();

        for(MyDayEvent event : eventsList)
        {
            if(event != null) {
                if (event.getDate().equals(date))
                    events.add(event);
            }
        }
        Collections.sort(events, new Comparator<MyDayEvent>() {
            @Override
            public int compare(MyDayEvent o1, MyDayEvent o2) {
                if( o1.getTime() > o2.getTime())
                {
                    return 1;
                }
                else {
                    return -1;
                }
            }
        });
        return events;
    }

    static boolean isTimeTableFooled()
    {
        boolean p = false;
        ArrayList<MyDayEvent> events = new ArrayList<>();
        ArrayList<ExcludeEvent> exevents = new ArrayList<>();
        ArrayList<FixEvent> fxevents = new ArrayList<>();
        exevents = ExcludeEventList.eventsForDate(formattedDate(LocalDate.now()));
        fxevents = FixEventList.eventsForDate(formattedDate(LocalDate.now()));
        events = eventsForDate(formattedDate(LocalDate.now()));
        for(int i = 0 ; i < 24 ; i ++)
        {
            p = false;
            for(MyDayEvent event : events)
            {
                if(event.isPiled(i))
                {
                    p = true;
                }
            }
            for (ExcludeEvent e : exevents) {
                if (e.isPiled(i)) {
                    p = true;
                }
            }
            for (FixEvent e : fxevents) {
                if (e.isPiled(i)) {
                    p = true;
                }
            }
            if(!p)
            {
                return false;
            }
        }
        return true;
    }
    public static void reroll(String date) {

        mDatabase = FirebaseDatabase.getInstance().getReference();
        ArrayList<MyDayEvent> events;
        ArrayList<ExcludeEvent> exevents = new ArrayList<>();
        ArrayList<FixEvent> fxevents = new ArrayList<>();
        exevents = ExcludeEventList.eventsForDate(formattedDate(LocalDate.now()));
        fxevents = FixEventList.eventsForDate(formattedDate(LocalDate.now()));
        events = eventsForDate(date);
        if(events.isEmpty())
        {
            return;
        }
        ArrayList<MyDayEvent> newevents = new ArrayList<>();
        for(MyDayEvent event : events)
        {
            if(!event.isChecked()) {
                //Log.d("MyLog", event.getContent());
                int startTime = 0;
                boolean ok = true;
                while (ok) {
                    ok = false;
                    startTime = FragmentDialog.RandomNum();
                    Log.d("MyLog", "New Time : " + startTime);
                    if (!newevents.isEmpty()) {
                        for (MyDayEvent e : newevents) {
                            Log.d("MyLog", "new : " + e.getTime());
                            if (e.isPiled(startTime)) {
                                ok = true;
                            }
                        }
                    }
                    if (!exevents.isEmpty()) {
                        for (ExcludeEvent e : exevents) {
                            Log.d("MyLog", "exclud : " + e.getStart_hour() + "~" + e.getEnd_hour());
                            if (e.isPiled(startTime)) {
                                ok = true;
                            }
                        }
                    }
                    if (!fxevents.isEmpty()) {
                        for (FixEvent e : fxevents) {
                            Log.d("MyLog", "fixed : " + e.getStart_hour() + "~" + e.getEnd_hour());
                            if (e.isPiled(startTime)) {
                                ok = true;
                            }
                        }
                    }
                    long mNow = System.currentTimeMillis();
                    Date mDate = new Date(mNow);
                    SimpleDateFormat mFormat = new SimpleDateFormat("H");
                    if(startTime <= Integer.parseInt(mFormat.format(mDate)))
                    {
                        Log.d("MyLog", mFormat.format(mDate) + "time , now");
                        ok = true;
                    }
                    if (!ok) {
                        event.setTime(startTime);
                        newevents.add(event);
                        break;
                    }
                }
            }
        }
        /*
        for(MyDayEvent event : newevents)
        {
            Log.d("MyLog", event.getContent());
        }
        */
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists())
                    postNum = (snapshot.child("event").getChildrenCount());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        Task<DataSnapshot> t;
        while (!(t = mDatabase.child("event").get()).isComplete()) ;
        for(MyDayEvent target : newevents)
        {
            for(int i = 0; i <= postNum ; i++) {
                MyDayEvent e = t.getResult().child(String.valueOf(i)).getValue(MyDayEvent.class);
                if(e != null && target != null) {
                    if (e.getContent().equals(target.getContent())&& e.getDate().equals(target.getDate()) && !e.isChecked()) {
                        mDatabase.child("event").child(String.valueOf(i)).child("time").setValue(target.getTime());
                    }
                }
            }
        }
    }
}