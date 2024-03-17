package com.example.couchpotatosplan.month;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
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

import com.example.couchpotatosplan.R;
import com.example.couchpotatosplan.myday.MyDayEvent;
import com.example.couchpotatosplan.myday.MyDayEventList;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MonthFragment extends Fragment {
    public TextView exclude_btn;
    public TextView fix_btn;
    public ImageButton add_btn;
    public MemoDialog dialog;
    public long postNumOfMemo;
    private DatabaseReference mDatabase;
    private MemoEventAdapter adapter;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private String theme_num = "0";
    private View bar;
    private View bar2;
    private ListView eventListView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.month_fragment, container, false);

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        add_btn = view.findViewById((R.id.add_btn));
        exclude_btn = view.findViewById(R.id.exclude);
        fix_btn = view.findViewById(R.id.fix);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        bar = view.findViewById(R.id.view3);
        bar2 = view.findViewById(R.id.view5);
        eventListView = view.findViewById(R.id.memo);

        add_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog = new MemoDialog();
                dialog.show(getActivity().getSupportFragmentManager(), "dialog");
            }
        });

        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                MemoEventList.memoeventList.clear();

                for (DataSnapshot dataSnapshot : snapshot.child(currentUser.getUid()).child("memo").getChildren()) {
                    MemoEvent post = dataSnapshot.getValue(MemoEvent.class);
                    MemoEventList.memoeventList.add(post);
                    if (post != null) {
                        postNumOfMemo = post.getId();
                    }
                }

                if (snapshot.exists()) {
                    if(snapshot.child(currentUser.getUid()).child("theme").getValue() != null) {
                        theme_num = snapshot.child(currentUser.getUid()).child("theme").getValue().toString();
                    }
                    int theme = Integer.parseInt(theme_num);

                    switch (theme) {
                        case 0:
                            bar.setBackgroundColor(Color.rgb(143, 186, 216));
                            bar2.setBackgroundColor(Color.rgb(143, 186, 216));
                            break;
                        case 1:
                            bar.setBackgroundColor(Color.rgb(255, 196, 0));
                            bar2.setBackgroundColor(Color.rgb(255, 196, 0));
                            break;
                        case 2:
                            bar.setBackgroundColor(Color.rgb(214, 92, 107));
                            bar2.setBackgroundColor(Color.rgb(214, 92, 107));
                            break;
                        case 3:
                            bar.setBackgroundColor(Color.rgb(201, 117, 127));
                            bar2.setBackgroundColor(Color.rgb(201, 117, 127));
                            break;
                        case 4:
                            bar.setBackgroundColor(Color.rgb(97, 95, 133));
                            bar2.setBackgroundColor(Color.rgb(97, 95, 133));
                            break;
                        case 5:
                            bar.setBackgroundColor(Color.rgb(48, 52, 63));
                            bar2.setBackgroundColor(Color.rgb(48, 52, 63));
                            break;
                    }
                    setEventAdpater();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        setEventAdpater();


        exclude_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.main_frame, new MonthExcludeFragment()).commit();
            }
        });

        fix_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.main_frame, new MonthFixFragment()).commit();
            }
        });

        eventListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), R.style.MyDatePickerStyle);
                builder.setTitle("일정 삭제").setMessage("정말로 삭제하시겠습니까?");

                builder.setPositiveButton("예", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        MemoEvent item = (MemoEvent) adapter.getItem(position);
                        mDatabase.child(currentUser.getUid()).child("memo").child(String.valueOf(item.getId())).removeValue();

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

                return true;
            }
        });


        return view;
    }


    public void setEventAdpater()
    {
        try {
            ArrayList<MemoEvent> dailyEvents = MemoEventList.memoeventList;
            adapter = new MemoEventAdapter(getActivity().getApplicationContext(), dailyEvents);
            eventListView.setAdapter(adapter);
        } catch (Exception e) {
            //TODO
        }
    }
}