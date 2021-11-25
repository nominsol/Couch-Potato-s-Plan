package com.example.couchpotatosplan.myday;

import static com.example.couchpotatosplan.weekly.CalendarUtils.formattedDate;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.couchpotatosplan.R;
import com.example.couchpotatosplan.month.ExcludeEvent;
import com.example.couchpotatosplan.month.ExcludeEventList;
import com.example.couchpotatosplan.month.FixEvent;
import com.example.couchpotatosplan.month.FixEventList;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

public class FragmentDialog extends DialogFragment {

    private EditText eventNameET;
    private EditText DateET;
    private Button save_btn;
    private DatabaseReference mDatabase;
    private long postNum;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        // Write a message to the database
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

        View view = inflater.inflate(R.layout.test_dialog, container, false);
        save_btn = (Button) view.findViewById(R.id.save_btn);

        initWidgets(view);
        saveEventAction(view);

        return view;
    }

    private void initWidgets(View view)
    {
        eventNameET = view.findViewById(R.id.eventNameET);
        DateET = view.findViewById(R.id.DateET);
    }

    public void saveEventAction(View view)
    {
        save_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String eventName = eventNameET.getText().toString();
                String Date = DateET.getText().toString();
                ArrayList<ExcludeEvent> exevents = new ArrayList<>();
                ArrayList<FixEvent> fxevents = new ArrayList<>();
                exevents = ExcludeEventList.eventsForDate(formattedDate(LocalDate.now()));
                fxevents = FixEventList.eventsForDate(formattedDate(LocalDate.now()));
                if(!eventName.equals("")) {
                    if(MyDayEventList.isTimeTableFooled()) {
                        dismiss();
                        return; // 시간표가 꽉차면 종료
                    }
                    int startTime = 0;
                    boolean ok = true;
                    if(!MyDayEventList.eventsList.isEmpty()) { //비어있지 않으면 검사후 적용
                        while (ok) {
                            ok = false;
                            startTime = RandomNum();
                            for (MyDayEvent e : MyDayEventList.eventsList) {
                                if(e != null) {
                                    if (e.isPiled(startTime)) {
                                        ok = true;
                                    }
                                }
                            }
                            if (!exevents.isEmpty()) {
                                for (ExcludeEvent e : exevents) {
                                    //Log.d("MyLog", "exclud : " + e.getStart_hour() + "~" + e.getEnd_hour());
                                    if (e.isPiled(startTime)) {
                                        ok = true;
                                    }
                                }
                            }
                            if (!fxevents.isEmpty()) {
                                for (FixEvent e : fxevents) {
                                    //Log.d("MyLog", "fixed : " + e.getStart_hour() + "~" + e.getEnd_hour());
                                    if (e.isPiled(startTime)) {
                                        ok = true;
                                    }
                                }
                            }
                            long mNow = System.currentTimeMillis();
                            java.util.Date mDate = new Date(mNow);
                            SimpleDateFormat mFormat = new SimpleDateFormat("H");
                            if(startTime <= Integer.parseInt(mFormat.format(mDate)))
                            {
                                Log.d("MyLog", mFormat.format(mDate) + "time , now");
                                ok = true;
                            }
                            Log.d("MyLog", mFormat.format(mDate) + "time , now");
                        }
                    }
                    else // 비어있으면 바로 적용
                    {
                        startTime = RandomNum();
                    }
                    writeNewEvent(Date, startTime, eventName, false);
                } else {
                    Toast.makeText(getContext(), "내용을 입력하세요", Toast.LENGTH_LONG).show();
                }
                dismiss();
            }
        });
    }

    public void writeNewEvent(String date, int time, String content, boolean checked) {
        MyDayEvent event = new MyDayEvent(postNum+1, date, time, content, checked);
        MyDayEventList.eventsList.add(event);
        mDatabase.child("event").child(String.valueOf(postNum+1)).setValue(event);
    }

    public static int RandomNum() {
        Random random = new Random();
        int time = random.nextInt(24) + 1;

        return time;
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        MyDayEventList.eventsForDate(formattedDate(LocalDate.now()));
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.main_frame, new MyDayFragment()).commit();
    }
}