package com.example.couchpotatosplan.myday;

import static com.example.couchpotatosplan.weekly.CalendarUtils.formattedDate;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.couchpotatosplan.R;
import com.example.couchpotatosplan.month.ExcludeEvent;
import com.example.couchpotatosplan.month.ExcludeEventList;
import com.example.couchpotatosplan.month.FixEvent;
import com.example.couchpotatosplan.month.FixEventList;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.Random;


public class MyDayFragment extends Fragment {


    private long mNow;
    private Date mDate;
    private SimpleDateFormat mFormat;
    private ListView mydayEventListView;
    private MyDayEventAdapter myDayEventAdapter;
    private ImageButton add_btn;
    private ImageButton reroll_btn;
    private DatabaseReference mDatabase;
    private FirebaseUser currentUser;
    private FirebaseAuth mAuth;
    private View view;

    private FragmentDialog dialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.myday_fragment, container, false);

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        TextView mTextView = view.findViewById(R.id.todayDate);
        add_btn = (ImageButton) view.findViewById(R.id.add_btn);
        reroll_btn = (ImageButton) view.findViewById(R.id.reRoll_btn);
        mydayEventListView = view.findViewById(R.id.mydayeventListView);

        mFormat = new SimpleDateFormat("yyyy.MM.dd");

        mDatabase = FirebaseDatabase.getInstance().getReference();

        mTextView.setText(getTime());
        addEventAction();

        setEventAdpater();

        mydayEventListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MyDayEvent item = (MyDayEvent) myDayEventAdapter.getItem(position);
                if(item.getId() != null) {
                    mDatabase.child(currentUser.getUid()).child("event").child(String.valueOf(item.getId())).child("checked").setValue(!item.isChecked());
                }
            }
        });

        mydayEventListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                if (myDayEventAdapter.getItem(position).getId() != null) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), R.style.MyDatePickerStyle);
                    builder.setTitle("일정 삭제").setMessage("정말로 삭제하시겠습니까?");

                    builder.setPositiveButton("예", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            MyDayEvent item = (MyDayEvent) myDayEventAdapter.getItem(position);
                            mDatabase.child(currentUser.getUid()).child("event").child(String.valueOf(item.getId())).removeValue();
                            myDayEventAdapter.notifyDataSetChanged();
                            Toast.makeText(getContext(), "삭제되었습니다", Toast.LENGTH_LONG).show();
                        }
                    });

                    builder.setNegativeButton("아니요", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //TODO
                        }
                    });
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                }
                return true;
            }
        });

        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                setEventAdpater();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

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
                dialog = new FragmentDialog();
                dialog.show(getActivity().getSupportFragmentManager(), "dialog");
            }
        });
        reroll_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reroll();
                setEventAdpater();
            }
        });
        FragmentTransaction ft = getFragmentManager().beginTransaction(); ft.detach(this). attach(this).commit();
    }

    public void reroll() {
        int randomNum = ReRollNum();
        boolean flag;
        ArrayList<MyDayEvent> myDayEvents = MyDayEventList.eventsForReroll(formattedDate(LocalDate.now()));
        ArrayList<Integer> timeSet = new ArrayList<>();

        while(timeSet.size() <= myDayEvents.size()) {
            flag = false;
            // event list 확인
            if(!timeSet.isEmpty()) {
                for (int item : timeSet) {
                    if (randomNum == item) {
                        flag = true;
                        break;
                    }
                }
            }
            // Exclude Time 범위 안에 랜덤 숫자가 들어있는지 확인
            for (ExcludeEvent item : ExcludeEventList.eventsList) {
                if (item.getStart_hour() <= randomNum && item.getEnd_hour() >= randomNum) {
                    flag = true;
                    break;
                }
            }
            // Fix Time 범위 안에 랜덤 숫자가 들어있는지 확인
            for (FixEvent item : FixEventList.fixeventsList) {
                if (item.getStart_hour() <= randomNum && item.getEnd_hour() >= randomNum) {
                    flag = true;
                    break;
                }
            }

            // 현재 시간 이전인 확인
//            if(randomNum <= LocalTime.now().getMinute()) {
//                flag = true;
//            }

            if(!flag) {
                timeSet.add(randomNum);
                randomNum = ReRollNum();
            } else {
                randomNum = ReRollNum();
            }
        }

        // 변경된 시간으로 저장
        int i = 0;
        for(MyDayEvent events : myDayEvents) {
            mDatabase.child(currentUser.getUid()).child("event").child(String.valueOf(events.getId())).child("time").setValue(timeSet.get(i));
            i++;
        }
    }

    @Override
    public void onResume()
    {
        super.onResume();
        setEventAdpater();
    }

    public void setEventAdpater()
    {
        try {

            ArrayList<FixEvent> fixEvents = FixEventList.fixeventsList;
            ArrayList<MyDayEvent> fixToDaily  = new ArrayList<>();
            for(FixEvent e : fixEvents)
            {
                int h = e.getStart_hour();
                int m = e.getStart_min();
                MyDayEvent ee = new MyDayEvent();
                ee.setId(null);
                ee.setContent(e.getContent());
                ee.setTime(100*h + m);
                ee.setChecked(false);
                fixToDaily.add(ee);
            }
            ArrayList<MyDayEvent> dailyEvents = MyDayEventList.eventsForDate(formattedDate(LocalDate.now()));
            dailyEvents.addAll(fixToDaily);

            dailyEvents.sort(new comparator());
            myDayEventAdapter = new MyDayEventAdapter(getActivity().getApplicationContext(), dailyEvents);
            mydayEventListView.setAdapter(myDayEventAdapter);
        }
        catch (Exception e) {
            //TODO
        }
    }

    class comparator implements Comparator<MyDayEvent> {
        @Override
        public int compare(MyDayEvent item1, MyDayEvent item2) {
            if (item1.getTime() > item2.getTime()) {
                return 1;
            } else if (item1.getTime() < item2.getTime()) {
                return -1;
            }
            return 0;
        }
    }
    public int ReRollNum() {
        int n = 0;
        boolean flag = true;
        Random random = new Random();
        long mNow = System.currentTimeMillis();
        Date mDate = new Date(mNow);
        SimpleDateFormat mFormat = new SimpleDateFormat("H");
        while(flag) {
            n = random.nextInt(24) + 1;
            if (n > Integer.parseInt(mFormat.format(mDate)) ) {
                Log.d("MyLog", mFormat.format(mDate) + "time , now");
                flag = false;
            }
        }
        return n;
    }
}