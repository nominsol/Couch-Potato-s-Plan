package com.example.couchpotatosplan.myday;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.couchpotatosplan.R;
import com.example.couchpotatosplan.weekly.CalendarUtils;
import com.example.couchpotatosplan.weekly.Event;
import com.example.couchpotatosplan.weekly.EventAdapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;


public class MyDayFragment extends Fragment {


    long mNow;
    Date mDate;
    SimpleDateFormat mFormat = new SimpleDateFormat("yyyy.MM.dd");
    private TextView time_tv;
    private TextView content_tv;
    private ListView eventListView;
    FragmentDialog dialog = new FragmentDialog();

    private ImageButton add_btn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.myday_fragment, container, false);

        TextView mTextView = view.findViewById(R.id.todayDate);
        add_btn = (ImageButton) view.findViewById(R.id.add_btn);

        initWidgets(view);
        mTextView.setText(getTime());
        addEventAction();

        setEventAdpater();

//        dialog.getDialog().setOnDismissListener(
//                new DialogInterface.OnDismissListener() {
//                    @Override
//                    public void onDismiss(DialogInterface dialog) {
//                        setEventAdpater();
//                    }
//                }
//        );

        return view;
    }

    private String getTime(){
        mNow = System.currentTimeMillis();
        mDate = new Date(mNow);
        return mFormat.format(mDate);
    }

    private void addEventAction() {
        add_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentDialog fragmentDialog = new FragmentDialog();
                fragmentDialog.show(getActivity().getSupportFragmentManager(), "dialog");
            }
        });
    }

//    @Override
//    public void onResume()
//    {
//        super.onResume();
//        setEventAdpater();
//    }

    private void setEventAdpater()
    {
        Toast.makeText(getContext(), "hi", Toast.LENGTH_SHORT).show();
        ArrayList<MyDayEvent> dailyEvents = MyDayEvent.eventsForDate();
        MyDayEventAdapter eventAdapter = new MyDayEventAdapter(getActivity().getApplicationContext(), dailyEvents);
        eventListView.setAdapter(eventAdapter);
    }

    private void initWidgets(View view)
    {
        time_tv = (TextView) view.findViewById(R.id.time_tv);
        content_tv = (TextView) view.findViewById(R.id.content_tv);
        eventListView = view.findViewById(R.id.eventListView);
    }
}