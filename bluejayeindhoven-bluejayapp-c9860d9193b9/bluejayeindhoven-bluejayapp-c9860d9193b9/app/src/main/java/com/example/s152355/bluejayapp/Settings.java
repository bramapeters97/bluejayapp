package com.example.s152355.bluejayapp;

import android.app.Notification;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import static com.example.s152355.bluejayapp.NotificationChannels.CHANNEL_1_ID;

public class Settings extends NavigationDrawer
        implements NavigationView.OnNavigationItemSelectedListener {

    private NotificationManagerCompat notificationManager;
    private Button saveBtn;

    // Create all elements contained inside the activity
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        saveBtn = findViewById(R.id.saveBtn);
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveSettings();
            }
        });

        TextView droneid = (TextView) findViewById(R.id.droneid);
        droneid.setText(IpAddress.DRONE_NAME);

        TextView ipaddress = (TextView) findViewById(R.id.ipAddress);
        ipaddress.setText(IpAddress.IP_ADDRESS);

        Button changeButton = (Button) findViewById(R.id.changeBtn);
        changeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeDrone();
            }
        });

        Switch pushSwitch = (Switch) findViewById(R.id.pushSwitch);

        if (IpAddress.NotificationsEnabled == true){
            pushSwitch.setChecked(true);
        } else {
            pushSwitch.setChecked(false);
        }

        pushSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked==true){
                    IpAddress.NotificationsEnabled = true;
                } else {
                    IpAddress.NotificationsEnabled = false;
                }
            }
        });

        notificationManager = NotificationManagerCompat.from(this);
    }

    public void saveSettings(){
        Toast.makeText(this, "Settings successfully saved", Toast.LENGTH_LONG).show();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    // Two test notifications, this is what the notifications that the drone sends in an emergency will also look like.
    public void sendOnChannel1(View v){
        if (IpAddress.NotificationsEnabled == true) {
            Notification notification = new NotificationCompat.Builder(this, CHANNEL_1_ID)
                    .setDefaults(NotificationCompat.DEFAULT_ALL)
                    .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.logo_icon_withspacing))
                    .setSmallIcon(R.drawable.logo_full_white_icon)
                    .setContentTitle("Emergency!")
                    .setContentText("Notification from Blue Jay. Press to go to map.")
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setCategory(NotificationCompat.CATEGORY_ALARM)
                    .build();
            notificationManager.notify(1, notification);
        } else {
            Toast.makeText(this, "Enable push notifications", Toast.LENGTH_LONG).show();
        }
    }

    public void changeDrone(){
        Intent intent = new Intent(this, LogIn.class);
        startActivity(intent);
    }

}