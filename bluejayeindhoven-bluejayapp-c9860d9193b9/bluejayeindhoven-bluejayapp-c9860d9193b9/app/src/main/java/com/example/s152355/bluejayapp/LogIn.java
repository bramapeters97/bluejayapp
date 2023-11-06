package com.example.s152355.bluejayapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public class LogIn extends AppCompatActivity {

    Context thisContext = this;
    private static int SPLASH_TIME_OUT = 800;

    // Create all elements of the welcome screen including a timed, pending transition
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        RadioButton blueJay1Select = findViewById(R.id.blueJay1Select);
        RadioButton blueJay2Select = findViewById(R.id.blueJay2Select);
        RadioButton blueJay3Select = findViewById(R.id.blueJay3Select);
        Button customIpSelect = findViewById(R.id.customIP);

        blueJay1Select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Handler().postDelayed(new Runnable(){
                    @Override
                    public void run(){
                        Intent homeIntent = new Intent(LogIn.this, MainActivity.class);
                        displayToast("Selected drone: Blue Jay 1");
                        showProgress();
                        IpAddress.IP_ADDRESS = "192.168.1.125";
                        IpAddress.DRONE_NAME = "Blue Jay 1";
                        startActivityForResult(homeIntent, 0);
                    }
                }, SPLASH_TIME_OUT);
            }
        });

        blueJay2Select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Handler().postDelayed(new Runnable(){
                    @Override
                    public void run(){
                        Intent homeIntent = new Intent(LogIn.this, MainActivity.class);
                        displayToast("Selected drone: Blue Jay 2");
                        showProgress();
                        IpAddress.IP_ADDRESS = "192.168.1.126";
                        IpAddress.DRONE_NAME = "Blue Jay 2";
                        startActivityForResult(homeIntent, 0);
                    }
                }, SPLASH_TIME_OUT);
            }
        });

        blueJay3Select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Handler().postDelayed(new Runnable(){
                    @Override
                    public void run(){
                        Intent homeIntent = new Intent(LogIn.this, MainActivity.class);
                        displayToast("Selected drone: Blue Jay 3");
                        showProgress();
                        IpAddress.IP_ADDRESS = "192.168.1.127";
                        IpAddress.DRONE_NAME = "Blue Jay 3";
                        startActivityForResult(homeIntent, 0);
                    }
                }, SPLASH_TIME_OUT);
            }
        });

        customIpSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Handler().postDelayed(new Runnable(){
                    @Override
                    public void run(){
                        Intent homeIntent = new Intent(LogIn.this, CustomIp.class);
                        showProgress();
                        homeIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        startActivityForResult(homeIntent, 0);
                    }
                }, SPLASH_TIME_OUT);
            }
        });

    }

    public void showProgress(){
        TextView textView1 = (TextView) findViewById(R.id.textView5);
        TextView textView2 = (TextView) findViewById(R.id.textView6);
        RadioGroup radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        Button customIpSelect = findViewById(R.id.customIP);
        ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBar);

        radioGroup.setVisibility(View.INVISIBLE);
        textView1.setVisibility(View.INVISIBLE);
        textView2.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.VISIBLE);
        customIpSelect.setVisibility(View.INVISIBLE);
        progressBar.bringToFront();
    }

    public void displayToast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_LONG).show();
    }
}