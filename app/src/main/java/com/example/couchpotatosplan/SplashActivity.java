package com.example.couchpotatosplan;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.example.couchpotatosplan.login.UserLoginActivity;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceStare){
        super.onCreate(savedInstanceStare);
        setContentView((R.layout.splash_activity));

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(getApplicationContext(), UserLoginActivity.class);
                startActivity(intent);
                finish();
            }
        },2000);
    }

    @Override
    protected void onPause(){
        super.onPause();
        finish();
    }
}