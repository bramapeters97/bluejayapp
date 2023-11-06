package com.example.s152355.bluejayapp;

import android.app.Notification;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
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

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.text.DecimalFormat;

import static com.example.s152355.bluejayapp.NotificationChannels.CHANNEL_1_ID;

public class FlyToLocation extends NavigationDrawer
        implements NavigationView.OnNavigationItemSelectedListener {

    Context thisContext;
    private NotificationManagerCompat notificationManager;
    private static DecimalFormat df2 = new DecimalFormat("#.##");

    private static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    private static final String DB_URL = "jdbc:mysql://" +
            DbStrings.DATABASE_URL + "/" +
            DbStrings.DATABASE_NAME;
    private static final String USER = DbStrings.USERNAME;
    private static final String PASS = DbStrings.PASSWORD;

    String localisation_current = "";
    String battery_current = "";
    String flight_status_current = "";

    public String coordinates;
    public String text;
    public int takeoff_height = 0;

    // Values for period on periodic GET request
    private int mInterval = 1000; // 1 second by default, can be changed later
    private Handler mHandler;

    // Create all elements contained inside the activity
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fly_to_location);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Instantiate the RequestQueue.
        final RequestQueue queue = Volley.newRequestQueue(this);

        // Initiate channel to send GET requests periodically
        mHandler = new Handler();
        startRepeatingTask();

        thisContext = this;
        notificationManager = NotificationManagerCompat.from(this);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        TextView droneid = (TextView) findViewById(R.id.droneid2);
        droneid.setText(IpAddress.DRONE_NAME);

        TextView ipaddress = (TextView) findViewById(R.id.ipAddress2);
        ipaddress.setText(IpAddress.IP_ADDRESS);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        Button changeButton = (Button) findViewById(R.id.changeBtn2);
        changeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeDrone();
            }
        });

        ImageButton rebootButton = findViewById(R.id.rebootButton2);
        rebootButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(FlyToLocation.this);
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
                        sendGETrequest("http://"+IpAddress.IP_ADDRESS+":5000/reboot", "Blue Jay will be rebooted", queue);
                        emergencyDialog.dismiss();
                    }
                });

            }
        });

        ImageButton poweroffButton = findViewById(R.id.powerOffButton2);
        poweroffButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(FlyToLocation.this);
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

        ImageView offlineButton = findViewById(R.id.offlineButton3);
        offlineButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayToast("Could not connect with the drone. Please make sure you are connected to the Blue Jay WiFi network!");
            }
        });

        Button landButton = findViewById(R.id.landButton);
        landButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(FlyToLocation.this);
                View mView = getLayoutInflater().inflate(R.layout.land_button, null);
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
                        sendGETrequest("http://"+IpAddress.IP_ADDRESS+":5000/flight/land", "Blue Jay will continue to land", queue);
                        emergencyDialog.dismiss();
                        //Send objSend = new Send();
                        //objSend.execute("");
                    }
                });
            }
        });

        Button stopButton = findViewById(R.id.stopButton);
        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(FlyToLocation.this);
                View mView = getLayoutInflater().inflate(R.layout.stop_button, null);
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
                        sendGETrequest("http://"+IpAddress.IP_ADDRESS+":5000/flight/stop", "Blue Jay will stop and hover at its current position", queue);
                        emergencyDialog.dismiss();
                    }
                });
            }
        });

        ImageButton takeOffButton = findViewById(R.id.takeOffButton2);
        takeOffButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(FlyToLocation.this);
                View mView = getLayoutInflater().inflate(R.layout.take_off_button, null);
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
                        EditText heightInput = findViewById(R.id.heightInput);
                        String height = heightInput.getText().toString();
                        sendGETrequest("http://"+IpAddress.IP_ADDRESS+":5000/flight/takeoff/" + height, "Blue Jay will take off to height: " + height + " mm", queue);
                        emergencyDialog.dismiss();
                        heightInput.getText().clear();
                    }
                });
            }
        });

        ImageView questionMark = findViewById(R.id.questionMark);
        questionMark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(FlyToLocation.this);
                View mView = getLayoutInflater().inflate(R.layout.fly_to_detail, null);
                Button goBackBtn = mView.findViewById(R.id.goBackBtn);
                mBuilder.setView(mView);
                final AlertDialog emergencyDialog = mBuilder.create();
                emergencyDialog.show();
                goBackBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        emergencyDialog.dismiss();
                    }
                });
            }
        });

        ImageView questionMark3 = findViewById(R.id.questionMark3);
        questionMark3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(FlyToLocation.this);
                View mView = getLayoutInflater().inflate(R.layout.offboard_detail, null);
                Button goBackBtn = mView.findViewById(R.id.goBackBtn);
                mBuilder.setView(mView);
                final AlertDialog emergencyDialog = mBuilder.create();
                emergencyDialog.show();
                goBackBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        emergencyDialog.dismiss();
                    }
                });
            }
        });

        ImageButton flyToButton = findViewById(R.id.flyToButton);
        flyToButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(FlyToLocation.this);
                View mView = getLayoutInflater().inflate(R.layout.fly_to_button, null);
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
                        EditText x_coordinate = findViewById(R.id.x_coordinate);
                        EditText y_coordinate = findViewById(R.id.y_coordinate);
                        EditText z_coordinate = findViewById(R.id.z_coordinate);
                        String x = x_coordinate.getText().toString();
                        String y = y_coordinate.getText().toString();
                        String z = z_coordinate.getText().toString();
                        coordinates = "("+x+","+y+","+z+")";
                        sendGETrequest("http://"+IpAddress.IP_ADDRESS+":5000/flight/flyto/"+x+"/"+y+"/"+z, "Blue Jay will fly to coordinates: " + coordinates, queue);
                        emergencyDialog.dismiss();
                        x_coordinate.getText().clear();
                        y_coordinate.getText().clear();
                        z_coordinate.getText().clear();
                    }
                });
            }
        });

        ImageView navImage = (ImageView) findViewById(R.id.mapImageFlyTo);
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
    }

    void startRepeatingTask() {
        mStatusChecker.run();
    }

    void stopRepeatingTask() {
        mHandler.removeCallbacks(mStatusChecker);
    }

    public void changeDrone(){
        Intent intent = new Intent(this, LogIn.class);
        startActivity(intent);
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
                            sendOnChannel1(command);
                        }
                        Send objSend = new Send();
                        objSend.execute("");
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                displayToast("Something went wrong.. Please make sure you are connected to the Blue Jay WiFi network and that the drone is in OffBoard-mode!");
            }
        });
        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

    public void displayToast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_LONG).show();
    }

    // Two test notifications, this is what the notifications that the drone sends in an emergency will also look like.
    public void sendOnChannel1(String command){
        Notification notification = new NotificationCompat.Builder(this, CHANNEL_1_ID)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.logo_icon_withspacing))
                .setSmallIcon(R.drawable.logo_full_white_icon)
                .setContentTitle("New command!")
                .setContentText(command)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_ALARM)
                .build();
        notificationManager.notify(1, notification);
    }

    public void batteryLife(String url, final String command, RequestQueue queue){
        final TextView batteryTextView = (TextView) findViewById(R.id.batteryLife);
        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        double battery = Math.round(((Double.parseDouble(response)*100))*10)/10;
                        String battery_string = (Double.toString(battery) + "%");
                        battery_current = battery_string;
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
        final TextView flightStatusTextView = (TextView) findViewById(R.id.flightStatus);
        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        String flight_status = response.replaceAll("^\"|\"$", "");
                        flightStatusTextView.setText(flight_status);
                        flight_status_current = response;
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

    public void coordinates(String url, final String command, RequestQueue queue){
        final TextView localisation_x = (TextView) findViewById(R.id.localisation_x);
        final TextView localisation_y = (TextView) findViewById(R.id.localisation_y);
        final TextView localisation_z = (TextView) findViewById(R.id.localisation_z);
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
                        localisation_current = "("+localisation_x+","+localisation_y+","+localisation_z+")";
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

    public void checkOnline(String url, final String command, RequestQueue queue){
        final ImageView onlineIcon = findViewById(R.id.onlineButton3);
        final ImageView offlineIcon = findViewById(R.id.offlineButton3);
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

    private class Send extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
        }
        @Override
        protected String doInBackground(String... strings) {
            try {
                Class.forName(JDBC_DRIVER);
                Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
                if (conn == null) {
                } else {
                    String query = "INSERT INTO sql7274832.action (drone_id, ip_address, category, coordinates, battery_life, flightstatus, takeoff_height, phone) VALUES ('" + IpAddress.DRONE_NAME + "', '" + IpAddress.IP_ADDRESS + "', 'Fly To Location', '" + localisation_current + "', '" + battery_current + "', '" + flight_status_current + "', '" + takeoff_height + "', '"+android.os.Build.MODEL+"')";
                    Statement stmt = conn.createStatement();
                    stmt.executeUpdate(query);
                }
                conn.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return "";
        }
        @Override
        protected void onPostExecute(String msg) {
        }
    }
}