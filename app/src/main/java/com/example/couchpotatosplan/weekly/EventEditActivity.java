package com.example.couchpotatosplan.weekly;

import android.app.TimePickerDialog;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.couchpotatosplan.R;

import java.time.LocalTime;

public class EventEditActivity extends AppCompatActivity {
    private EditText eventNameET;
    private TimePickerDialog timePicker;
    private TextView time_set;

    private LocalTime time;
    private int hour;
    private int min;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        time_set = (TextView) findViewById(R.id.time_set);

        time_set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openTimePicker();
            }
        });

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_edit);
        initWidgets();

        time = LocalTime.now();
    }

    private void initWidgets()
    {
        eventNameET = findViewById(R.id.eventNameET);
    }

    public void saveEventAction(View view)
    {
        String eventName = eventNameET.getText().toString();
        Event newEvent = new Event(eventName, CalendarUtils.selectedDate, time);
        Event.eventsList.add(newEvent);
        finish();
    }

    public void openTimePicker() {
        int hourOfDay = 23;
        int minute = 55;
        boolean is24HourView = true;

//        timePicker = new TimePickerDialog(EventEditActivity.this, android.R.style.Theme_Holo_Light_Dialog_MinWidth,
//                new TimePickerDialog.OnTimeSetListener() {
//                    @Override
//                    public void onTimeSet(TimePicker timepicker, int m_hour, int m_min) {
//                        hour = m_hour;
//                        min = m_min;timePicker = new TimePickerDialog(EventEditActivity.this, android.R.style.Theme_Holo_Light_Dialog_MinWidth,
////                new TimePickerDialog.OnTimeSetListener() {
////                    @Override
////                    public void onTimeSet(TimePicker timepicker, int m_hour, int m_min) {
////                        hour = m_hour;
////                        min = m_min;
////
////                        SimpleDateFormat f24Hours = new SimpleDateFormat(
////                                "HH:mm"
////                        );
////
////                        try {
////                            Date date = f24Hours.parse(time);
////
////                            SimpleDateFormat f12Hours = new SimpleDateFormat(
////                                    "hh:mm aa"
////                            );
////                            time_set.setText(hour + " : " + min);
////                        } catch (ParseException e) {
////                            e.printStackTrace();
////                        }
////
////
////                    }
////                }, 12, 0, false
////        );
////        timePicker.updateTime(hour, minute);
////        timePicker.show();
//
//                        SimpleDateFormat f24Hours = new SimpleDateFormat(
//                                "HH:mm"
//                        );
//
//                        try {
//                            Date date = f24Hours.parse(time);
//
//                            SimpleDateFormat f12Hours = new SimpleDateFormat(
//                                    "hh:mm aa"
//                            );
//                            time_set.setText(hour + " : " + min);
//                        } catch (ParseException e) {
//                            e.printStackTrace();
//                        }
//
//
//                    }
//                }, 12, 0, false
//        );
//        timePicker.updateTime(hour, minute);
//        timePicker.show();
    }
}