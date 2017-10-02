package com.therock.practicaactividades;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;

import java.util.Timer;
import java.util.TimerTask;


public class SplashActivity extends AppCompatActivity {

    private static final long SPLASH_DELAY = 3000;
    SharedPreferences prefs;
    SharedPreferences.Editor editor;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        this.supportRequestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.activity_splash);

        prefs = getSharedPreferences("SP" , Context.MODE_PRIVATE);
        editor = prefs.edit();
        final int optLog = prefs.getInt("optlog",0);




        TimerTask task = new TimerTask() {
            @Override
            public void run() {

                if(optLog == 0){
                    intent = new Intent(SplashActivity.this,LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
                else {Intent intent =new Intent(SplashActivity.this,MainActivity.class);

                    startActivity(intent);
                    finish();}





            }
        };

        Timer timer = new Timer();
        timer.schedule(task,SPLASH_DELAY);
    }
}
