package com.example.couchpotatosplan.myday;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.couchpotatosplan.R;
import com.example.couchpotatosplan.weekly.CalendarUtils;
import com.example.couchpotatosplan.weekly.Event;

import java.time.LocalTime;
import java.util.Random;

public class FragmentDialog extends DialogFragment {

    private EditText eventNameET;
    private LocalTime time;
    private Button save_btn;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View view = inflater.inflate(R.layout.test_dialog, container, false);
        save_btn = (Button) view.findViewById(R.id.save_btn);
        time = LocalTime.now();

        initWidgets(view);
        saveEventAction(view);

        return view;
    }

    private void initWidgets(View view)
    {
        eventNameET = view.findViewById(R.id.eventNameET);
    }

    public void saveEventAction(View view)
    {
        save_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String eventName = eventNameET.getText().toString();
                MyDayEvent newEvent = new MyDayEvent(RandomNum(), eventName);
                MyDayEvent.eventsList.add(newEvent);
                dismiss();
            }
        });
    }

    public int RandomNum() {
        Random random = new Random();
        int time = random.nextInt(20) + 1;

        return time;
    }
}
