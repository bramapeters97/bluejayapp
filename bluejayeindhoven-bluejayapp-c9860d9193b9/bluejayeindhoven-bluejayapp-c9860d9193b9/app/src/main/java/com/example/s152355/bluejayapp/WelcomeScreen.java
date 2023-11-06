package com.example.s152355.bluejayapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

public class WelcomeScreen extends AppCompatActivity {

    private static int SPLASH_TIME_OUT = 3000;
    Context thisContext = this;

    // Create all elements of the welcome screen including a timed, pending transition
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_screen);
        new Handler().postDelayed(new Runnable(){
            @Override
            public void run(){
                Intent homeIntent = new Intent(WelcomeScreen.this, LogIn.class);
                homeIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivityForResult(homeIntent, 0);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        }, SPLASH_TIME_OUT);
    }
}