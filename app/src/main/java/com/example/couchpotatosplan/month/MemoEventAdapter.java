package com.example.couchpotatosplan.month;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.couchpotatosplan.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class MemoEventAdapter extends ArrayAdapter<MemoEvent> {
    private TextView titleM;
    private TextView contentM;
    private TextView timeM;

    public MemoEventAdapter(@NonNull Context context, List<MemoEvent> events)
    {
        super(context, 0, events);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View view, @NonNull ViewGroup parent)
    {
        MemoEvent event = getItem(position);

        if (view == null)
            view = LayoutInflater.from(getContext()).inflate(R.layout.month_memo_cell, parent, false);

        titleM = view.findViewById(R.id.titleM);
        contentM = view.findViewById(R.id.contentM);
        timeM = view.findViewById((R.id.timeM));

        String content = event.getContent();
        String title = event.getTitle();
        String time = event.getTime();

        titleM.setText(title);
        contentM.setText(content);
        timeM.setText(time);

        return view;
    }

}