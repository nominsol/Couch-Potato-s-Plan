package com.example.couchpotatosplan.month;

import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.couchpotatosplan.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.Date;

public class MemoDialog extends DialogFragment {
    private EditText memotitle;
    private EditText memocontent;
    private Button save_btn;
    private Button cancel_btn;
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;

    private long postNum;




    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        // Write a message to the database
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot dataSnapshot : snapshot.child(currentUser.getUid()).child("memo").getChildren()) {
                        MemoEvent post = dataSnapshot.getValue(MemoEvent.class);
                        if (post != null) {
                            postNum = post.getId();
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        View view = inflater.inflate(R.layout.month_memo_dialog, container, false);

        save_btn = (Button) view.findViewById(R.id.save_btn);
        cancel_btn = (Button) view.findViewById(R.id.cancel_btn);

        initWidgets(view);
        EventAction(view);

        return view;
    }

    private void initWidgets(View view)
    {
        memocontent = view.findViewById(R.id.memocontent);
        memotitle = view.findViewById(R.id.memotitle);
    }

    public void EventAction(View view)
    {

        save_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title = memotitle.getText().toString();
                String content = memocontent.getText().toString();
                Date currenttime = new Date();
                SimpleDateFormat formatter = new SimpleDateFormat("yy-MM-dd");
                String time = formatter.format(currenttime);
                if(title.equals("")) {
                    Toast.makeText(getContext(), "제목을 입력하세요", Toast.LENGTH_LONG).show();
                } else if(content.equals("")) {
                    Toast.makeText(getContext(), "내용을 입력하세요", Toast.LENGTH_LONG).show();
                } else {
                    writeNewEvent(title, content, time);
                }
                dismiss();
            }
        });
        cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    public void writeNewEvent(String title, String content, String time) {
        MemoEvent event = new MemoEvent(postNum+1, title, content, time);
        mDatabase.child(currentUser.getUid()).child("memo").child(String.valueOf(postNum+1)).setValue(event);
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.main_frame, new MonthFragment()).commit();
    }


}