package com.example.s152355.bluejayapp;

import android.app.Notification;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.text.DecimalFormat;

import static com.example.s152355.bluejayapp.NotificationChannels.CHANNEL_1_ID;
import static java.lang.Math.round;

public class MainActivity extends NavigationDrawer
        implements NavigationView.OnNavigationItemSelectedListener {

    public String text;
    private NotificationManagerCompat notificationManager;
    private static DecimalFormat df2 = new DecimalFormat("#.###");

    String response_old;

    // Values for period on periodic GET request
    private int mInterval = 1000; // 5 seconds by default, can be changed later
    private Handler mHandler;

    // Create all elements contained inside the activity
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Initiate channel to send GET requests periodically
        mHandler = new Handler();
        startRepeatingTask();

        notificationManager = NotificationManagerCompat.from(this);

        // Instantiate the RequestQueue.
        final RequestQueue queue = Volley.newRequestQueue(this);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ImageButton rebootButton = findViewById(R.id.rebootButton);
        rebootButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(MainActivity.this);
                View mView = getLayoutInflater().inflate(R.layout.reboot_button, null);
                Button goBackBtn = mView.findViewById(R.id.goBackBtn);
                Button proceedBtn = mView.findViewById(R.id.proceedBtn);
                mBuilder.setView(mView);
                final AlertDialog emergencyDialog = mBuilder.create();
                emergencyDialog.show();
                goBackBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        emergencyDialog.dismiss();
                    }
                });
                proceedBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        sendGETrequest("http://"+ IpAddress.IP_ADDRESS+":5000/reboot", "Blue Jay will be rebooted", queue);
                        emergencyDialog.dismiss();
                    }
                });

            }
        });

        ImageButton poweroffButton = findViewById(R.id.poweroffButton);
        poweroffButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(MainActivity.this);
                View mView = getLayoutInflater().inflate(R.layout.power_off, null);
                Button goBackBtn = mView.findViewById(R.id.goBackBtn);
                Button proceedBtn = mView.findViewById(R.id.proceedBtn);
                mBuilder.setView(mView);
                final AlertDialog emergencyDialog = mBuilder.create();
                emergencyDialog.show();
                goBackBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        emergencyDialog.dismiss();
                    }
                });
                proceedBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        sendGETrequest("http://"+IpAddress.IP_ADDRESS+":5000/poweroff", "Blue Jay will shut down", queue);
                        emergencyDialog.dismiss();
                    }
                });

            }
        });

        ImageView offlineButton = findViewById(R.id.offlineButton);
        offlineButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayToast("Could not connect with the drone. Please make sure you are connected to the Blue Jay WiFi network!");
            }
        });

        Button emergencyLogBtn = findViewById(R.id.emergencyLogBtn);
        emergencyLogBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openEmergencyLog();
            }
        });

        Button changeDrone = (Button) findViewById(R.id.actionsLogBtn);
        changeDrone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeDrone();
            }
        });

        Button droneLogbookButton = findViewById(R.id.droneLogbookBtn);
        droneLogbookButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActionsLog();
            }
        });

        TextView droneName = (TextView) findViewById(R.id.droneName);
        droneName.setText(IpAddress.DRONE_NAME);

        ImageView navImage = (ImageView) findViewById(R.id.mapImage);
        navImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.com/maps/place/Blue+Jay+Eindhoven/@51.447914,5.493877,17z/data=!3m1!4b1!4m5!3m4!1s0x47c6d8e7a36ca67b:0x4d3251ec7275b02a!8m2!3d51.447914!4d5.4960657"));
                startActivity(myIntent);
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopRepeatingTask();
    }

    Runnable mStatusChecker = new Runnable() {
        @Override
        public void run() {
            try {
                updateStatus(); //this function can change value of mInterval.
            } finally {
                // 100% guarantee that this always happens, even if
                // your update method throws an exception
                mHandler.postDelayed(mStatusChecker, mInterval);
            }
        }
    };

    public void updateStatus(){
        // Instantiate the RequestQueue.
        final RequestQueue queue = Volley.newRequestQueue(this);
        batteryLife("http://"+IpAddress.IP_ADDRESS+":5000/battery", "", queue);
        flightStatus("http://"+IpAddress.IP_ADDRESS+":5000/flyingState", "", queue);
        coordinates("http://"+IpAddress.IP_ADDRESS+":5000/localisation", "", queue);
        checkOnline("http://"+IpAddress.IP_ADDRESS+":5000/test", "", queue);
        checkEmergency("http://"+IpAddress.IP_ADDRESS+":5000/emergency", "", queue);
    }

    void startRepeatingTask() {
        mStatusChecker.run();
    }

    void stopRepeatingTask() {
        mHandler.removeCallbacks(mStatusChecker);
    }

    public void openEmergencyLog(){
        Intent intent = new Intent(this, EmergenciesLog.class);
        startActivity(intent);
    }

    public void openActionsLog(){
        Intent intent = new Intent(this, ActionsLog.class);
        startActivity(intent);
    }

    public void changeDrone(){
        Intent intent = new Intent(this, LogIn.class);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            Intent a = new Intent(Intent.ACTION_MAIN);
            a.addCategory(Intent.CATEGORY_HOME);
            a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(a);
        }
    }

    public void sendOnChannel1(String title, String text){
        Notification notification = new NotificationCompat.Builder(this, CHANNEL_1_ID)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.logo_icon_withspacing))
                .setSmallIcon(R.drawable.logo_full_white_icon)
                .setContentTitle(title)
                .setContentText(text)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_ALARM)
                .build();
        notificationManager.notify(1, notification);
    }

    public void sendGETrequest(String url, final String command, RequestQueue queue){
        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        text = response.toString();
                        if (IpAddress.NotificationsEnabled == true){
                            sendOnChannel1("New command!", command);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                displayToast("Something went wrong.. Please make sure you are connected to the Blue Jay WiFi network and to the right IP-Address!");
            }
        });
        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

    public void batteryLife(String url, final String command, RequestQueue queue){
        final TextView batteryTextView = (TextView) findViewById(R.id.batteryTextView);
        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        double battery = round(((Double.parseDouble(response)*100))*10)/10;
                        String battery_string = (Double.toString(battery) + "%");
                        batteryTextView.setText(battery_string);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                batteryTextView.setText("NULL");
            }
        });
        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

    public void flightStatus(String url, final String command, RequestQueue queue){
        final TextView flightStatusTextView = (TextView) findViewById(R.id.statusTextView);
        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        String flight_status = response.replaceAll("^\"|\"$", "");
                        flightStatusTextView.setText(flight_status);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                flightStatusTextView.setText("NULL");
            }
        });
        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

    public void checkOnline(String url, final String command, RequestQueue queue){
        final ImageView onlineIcon = findViewById(R.id.onlineButton);
        final ImageView offlineIcon = findViewById(R.id.offlineButton);
        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        onlineIcon.setVisibility(View.VISIBLE);
                        offlineIcon.setVisibility(View.INVISIBLE);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                onlineIcon.setVisibility(View.INVISIBLE);
                offlineIcon.setVisibility(View.VISIBLE);
            }
        });
        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

    public void checkEmergency(String url, final String command, RequestQueue queue){
        final ImageView onlineIcon = findViewById(R.id.onlineButton_emergency);
        final ImageView warningIcon = findViewById(R.id.warningIcon);
        final TextView emergencyTextViewTitle = findViewById(R.id.emergencyTextViewTitle);
        final TextView timeTextViewTitle = findViewById(R.id.timeTextViewTitle);
        final TextView emergencyTextView = findViewById(R.id.emergencyTextView);
        final TextView timeTextView = findViewById(R.id.timeTextView);
        final CardView CardViewCover = findViewById(R.id.CardViewCover);
        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        System.out.println(response);
                        System.out.println(response.trim().equals("false"));
                        if (IpAddress.NotificationsEnabled) {
                            if (response.trim().equals("false")) {
                                onlineIcon.setVisibility(View.INVISIBLE);
                            } else {
                                if (response.equals(response_old)) {
                                } else {
                                    response_old = response;
                                    sendOnChannel1("New emergency!", "At time: " + response);
                                    onlineIcon.setVisibility(View.VISIBLE);
                                    warningIcon.setVisibility(View.VISIBLE);
                                    emergencyTextViewTitle.setVisibility(View.VISIBLE);
                                    timeTextViewTitle.setVisibility(View.VISIBLE);
                                    emergencyTextView.setVisibility(View.VISIBLE);
                                    timeTextView.setVisibility(View.VISIBLE);
                                    timeTextView.setText(response);
                                    emergencyTextView.setText(IpAddress.DRONE_NAME);
                                    CardViewCover.setVisibility(View.INVISIBLE);
                                }
                            }
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

    public void coordinates(String url, final String command, RequestQueue queue){
        final TextView localisation_x = (TextView) findViewById(R.id.localisation_x_main);
        final TextView localisation_y = (TextView) findViewById(R.id.localisation_y_main);
        final TextView localisation_z = (TextView) findViewById(R.id.localisation_z_main);
        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        String localisation_raw = (response.replaceAll("^\"|\"$", ""));
                        String[] localisation_split = localisation_raw.split(",");
                        String x_coordinate = df2.format(Double.parseDouble(localisation_split[0].replaceAll(" ", ""))).replaceAll(",", ".");
                        String y_coordinate = df2.format(Double.parseDouble(localisation_split[1].replaceAll(" ", ""))).replaceAll(",", ".");
                        String z_coordinate = df2.format(Double.parseDouble(localisation_split[2].replaceAll(" ", ""))).replaceAll(",", ".");
                        localisation_x.setText(x_coordinate);
                        localisation_y.setText(y_coordinate);
                        localisation_z.setText(z_coordinate);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

    public void displayToast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_LONG).show();
    }
}