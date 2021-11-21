package com.example.couchpotatosplan.month;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.couchpotatosplan.R;
import com.example.couchpotatosplan.myday.MyDayEventAdapter;

import java.util.ArrayList;

public class MonthFixFragment extends Fragment {
    private ImageButton add_btn;
    private ImageButton back_btn;
    private ListView eventListView;
    private FixDialog dialog;
    private FixEventAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.month_fix_fragment, container, false);

        add_btn = view.findViewById((R.id.add_btn));
        eventListView = view.findViewById((R.id.fixEventListView));
        back_btn = view.findViewById((R.id.back_btn));

        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.main_frame, new MonthFragment()).commit();
            }
        });

        add_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog = new FixDialog();
                dialog.show(getActivity().getSupportFragmentManager(), "dialog");
            }
        });

        setEventAdpater();

        return view;
    }

    public void setEventAdpater() {
        ArrayList<FixEvent> dailyEvents = FixEventList.fixeventsList;
        if (getActivity() != null) {
            adapter = new FixEventAdapter(getActivity().getApplicationContext(), dailyEvents);
            eventListView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }
    }
}