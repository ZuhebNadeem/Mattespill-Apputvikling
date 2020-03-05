package com.example.matte;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.example.matte.MainActivity;
import com.example.matte.R;


public class SplashActivity extends Activity {

    //DATAFELT
    private Handler ventStart = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        ventStart.postDelayed(new Runnable() {
            @Override
            public void run() {
                try {
                    //starter en ny aktivitet(side). I dette tilfelle MainActivity
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                    finish(); //vil ikke vise det videre, f.eks. hvis en bruker trykker tilbake-knappen
                } catch (Exception ignored) {
                    ignored.printStackTrace();
                }
            }// hvor mange sekunder den skal vises
        }, 3000);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ventStart.removeCallbacksAndMessages(null);
    }
    }
