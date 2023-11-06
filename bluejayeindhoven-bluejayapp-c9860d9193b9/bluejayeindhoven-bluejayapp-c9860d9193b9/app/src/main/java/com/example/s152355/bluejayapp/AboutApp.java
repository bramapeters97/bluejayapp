package com.example.s152355.bluejayapp;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;

public class AboutApp extends NavigationDrawer
        implements NavigationView.OnNavigationItemSelectedListener {

    // Create all interactive objects contained inside the activity
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_app);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // Cool bird-sound when image is clicked because why not
        ImageView img = (ImageView) findViewById(R.id.blueJayLogo);
        img.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                final MediaPlayer birdSound = MediaPlayer.create(thisContext, R.raw.bird_sound);
                birdSound.start();
            }
        });
    }
}
