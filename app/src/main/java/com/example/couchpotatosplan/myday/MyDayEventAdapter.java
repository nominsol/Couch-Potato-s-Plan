package com.example.couchpotatosplan.myday;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.example.couchpotatosplan.R;
import com.example.couchpotatosplan.weekly.CalendarUtils;
import com.example.couchpotatosplan.weekly.Event;

import java.util.List;
import java.util.Random;

public class MyDayEventAdapter extends ArrayAdapter<MyDayEvent> {
    public MyDayEventAdapter(@NonNull Context context, List<MyDayEvent> events)
    {
        super(context, 0, events);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View view, @NonNull ViewGroup parent)
    {
        MyDayEvent event = getItem(position);

        if (view == null)
            view = LayoutInflater.from(getContext()).inflate(R.layout.myday_event_cell, parent, false);

        TextView time_tv = view.findViewById(R.id.time_tv);
        TextView content_tv = view.findViewById(R.id.content_tv);

        String time = Integer.toString(event.getTime());
        String content = event.getContent();

        time_tv.setText(time);
        content_tv.setText(content);

        return view;
    }


}
