package com.example.couchpotatosplan;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.couchpotatosplan.month.MonthFragment;
import com.example.couchpotatosplan.myday.MyDayFragment;
import com.example.couchpotatosplan.setting.SettingFragment;
import com.example.couchpotatosplan.weekly.WeeklyFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;


public class MainActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.bottomNavi);

        getSupportFragmentManager().beginTransaction().add(R.id.main_frame, new WeeklyFragment()).commit();

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    //item을 클릭시 id값을 가져와 FrameLayout에 fragment.xml띄우기
                    case R.id.bottom_weekly:
                        getSupportFragmentManager().beginTransaction().replace(R.id.main_frame, new WeeklyFragment()).commit();
                        break;
                    case R.id.bottom_myday:
                        getSupportFragmentManager().beginTransaction().replace(R.id.main_frame, new MyDayFragment()).commit();
                        break;
                    case R.id.bottom_month:
                        getSupportFragmentManager().beginTransaction().replace(R.id.main_frame, new MonthFragment()).commit();
                        break;
                    case R.id.bottom_set:
                        getSupportFragmentManager().beginTransaction().replace(R.id.main_frame, new SettingFragment()).commit();
                        break;
                }
                return true;
            }
        });
    }

}



