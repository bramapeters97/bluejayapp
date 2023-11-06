package com.example.s152355.bluejayapp;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

// NavigationDrawer class contains all the content of the side menu
public class NavigationDrawer extends AppCompatActivity {

    Context thisContext = this;

    // The action performed when you press the back-button
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);

        ImageView navImage = (ImageView) findViewById(R.id.navHeader);
        navImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(thisContext, MainActivity.class);
                startActivity(intent);
            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id == R.id.stream_camera) {
            Intent intent = new Intent(this, StreamCameraFootage.class);
            startActivity(intent);
        } else if (id == R.id.fly_to_location) {
            Intent intent = new Intent(this, FlyToLocation.class);
            startActivity(intent);
        } else if (id == R.id.emergency_log) {
            Intent intent = new Intent(this, EmergenciesLog.class);
            startActivity(intent);
        } else if (id == R.id.action_log) {
            Intent intent = new Intent(this, ActionsLog.class);
            startActivity(intent);
        } else if (id == R.id.settings) {
            Intent intent = new Intent(this, Settings.class);
            startActivity(intent);
         } else if (id == R.id.aboutapp) {
            Intent intent = new Intent(this, AboutApp.class);
            startActivity(intent);
        } else if (id == R.id.website) {
            Intent intent = new Intent(this, Website.class);
            startActivity(intent);
        } else if (id == R.id.control_panel){
            Intent intent = new Intent(this, ControlScreen.class);
            startActivity(intent);
        } else if (id == R.id.home){
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}