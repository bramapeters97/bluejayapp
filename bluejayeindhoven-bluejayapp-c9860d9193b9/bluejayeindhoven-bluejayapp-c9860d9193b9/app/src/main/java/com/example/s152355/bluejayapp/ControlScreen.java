package com.example.s152355.bluejayapp;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

public class ControlScreen extends NavigationDrawer
        implements NavigationView.OnNavigationItemSelectedListener {

    // Create all elements contained inside the activity
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Instantiate the RequestQueue.
        final RequestQueue queue = Volley.newRequestQueue(this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_control_screen);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        this.setFinishOnTouchOutside(false);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        final Spinner ledSpinner = (Spinner) findViewById(R.id.ledSpinner);
        ArrayAdapter<CharSequence> ledAdapter = ArrayAdapter.createFromResource(this,
                R.array.ledstate, android.R.layout.simple_spinner_item);
        ledAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ledSpinner.setAdapter(ledAdapter);
        ledSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch(position){
                    case 0:
                        sendGETrequest("http://"+ IpAddress.IP_ADDRESS+":5000/lights/6", "Stand-By", queue);
                        break;
                    case 1:
                        sendGETrequest("http://"+IpAddress.IP_ADDRESS+":5000/lights/1", "Searching", queue);
                        break;
                    case 2:
                        sendGETrequest("http://"+IpAddress.IP_ADDRESS+":5000/lights/2", "White Pulse", queue);
                        break;
                    case 3:
                        sendGETrequest("http://"+IpAddress.IP_ADDRESS+":5000/lights/3", "Emergency", queue);
                        break;
                    case 4:
                        sendGETrequest("http://"+IpAddress.IP_ADDRESS+":5000/lights/4", "Low Battery", queue);
                        break;
                    case 5:
                        sendGETrequest("http://"+IpAddress.IP_ADDRESS+":5000/lights/5", "Green Pulse", queue);
                        break;
                    case 6:
                        sendGETrequest("http://"+IpAddress.IP_ADDRESS+":5000/lights/0", "Blue Jay Pulse", queue);
                        break;
                    case 7:
                        sendGETrequest("http://"+IpAddress.IP_ADDRESS+":5000/lights/7", "Acceleration", queue);
                        break;
                    case 8:
                        sendGETrequest("http://"+IpAddress.IP_ADDRESS+":5000/lights/8", "Take-Off", queue);
                        break;
                    case 9:
                        sendGETrequest("http://"+IpAddress.IP_ADDRESS+":5000/lights/9", "Static Green", queue);
                        break;
                    case 10:
                        sendGETrequest("http://"+IpAddress.IP_ADDRESS+":5000/lights/10", "Landing", queue);
                        break;
                    case 11:
                        sendGETrequest("http://"+IpAddress.IP_ADDRESS+":5000/lights/11", "Rainbow", queue);
                        break;
                    case 12:
                        sendGETrequest("http://"+IpAddress.IP_ADDRESS+":5000/lights/12", "Red Pulse", queue);
                        break;
                    case 13:
                        sendGETrequest("http://"+IpAddress.IP_ADDRESS+":5000/lights/13", "Blue Flash", queue);
                        break;
                    case 14:
                        sendGETrequest("http://"+IpAddress.IP_ADDRESS+":5000/lights/14", "Valentine", queue);
                        break;
                    case 15:
                        sendGETrequest("http://"+IpAddress.IP_ADDRESS+":5000/lights/15", "Acknowledgement", queue);
                        break;
                    case 16:
                        sendGETrequest("http://"+IpAddress.IP_ADDRESS+":5000/lights/16", "Positive", queue);
                        break;
                    case 17:
                        sendGETrequest("http://"+IpAddress.IP_ADDRESS+":5000/lights/17", "Light Blue", queue);
                        break;
                    case 18:
                        sendGETrequest("http://"+IpAddress.IP_ADDRESS+":5000/lights/18", "Lights Off", queue);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        ImageView questionMark = findViewById(R.id.questionMark2);
        questionMark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(ControlScreen.this);
                View mView = getLayoutInflater().inflate(R.layout.control_screen_manual, null);
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

        ImageView offlineButton4 = findViewById(R.id.offlineButton4);
        offlineButton4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayToast("Could not connect with the drone. Please make sure you are connected to the Blue Jay WiFi network!");
            }
        });

        Button happyEyesButton = findViewById(R.id.happyEyesBtn);
        happyEyesButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                sendGETrequest("http://"+IpAddress.IP_ADDRESS+":5000/eyes/Happy", "Happy", queue);
            }
        });

        Button neutralEyesButton = findViewById(R.id.neutralEyesBtn);
        neutralEyesButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                sendGETrequest("http://"+IpAddress.IP_ADDRESS+":5000/eyes/Neutral", "Neutral", queue);
            }
        });

        Button searchingEyesButton = findViewById(R.id.searchingEyesBtn);
        searchingEyesButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                sendGETrequest("http://"+IpAddress.IP_ADDRESS+":5000/eyes/Searching", "Searching", queue);
            }
        });

        Button lookUpEyesButton = findViewById(R.id.lookUpEyesBtn);
        lookUpEyesButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                sendGETrequest("http://"+IpAddress.IP_ADDRESS+":5000/eyes/LookUp", "LookUp", queue);
            }
        });

        Button lookDownEyesButton = findViewById(R.id.lookDownEyesBtn);
        lookDownEyesButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                sendGETrequest("http://"+IpAddress.IP_ADDRESS+":5000/eyes/LookDown", "LookDown", queue);
            }
        });

        Button acknowledgementEyesButton = findViewById(R.id.acknowledgementEyesBtn);
        acknowledgementEyesButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                sendGETrequest("http://"+IpAddress.IP_ADDRESS+":5000/eyes/Acknowledge", "Acknowledge", queue);
            }
        });

        Button tiredEyesButton = findViewById(R.id.tiredEyesBtn);
        tiredEyesButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                sendGETrequest("http://"+IpAddress.IP_ADDRESS+":5000/eyes/Tired", "Tired", queue);
            }
        });

        Button heartEyesButton = findViewById(R.id.heartEyesBtn);
        heartEyesButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                sendGETrequest("http://"+IpAddress.IP_ADDRESS+":5000/eyes/Heart", "Heart", queue);
            }
        });

        Button surprisedEyesButton = findViewById(R.id.surprisedEyesBtn);
        surprisedEyesButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                sendGETrequest("http://"+IpAddress.IP_ADDRESS+":5000/eyes/Surprised", "Surprised", queue);
            }
        });

        Button neutralStatusButton = findViewById(R.id.neutralStatusBtn);
        neutralStatusButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                sendGETrequest("http://"+IpAddress.IP_ADDRESS+":5000/eyes/Neutral", "Neutral Eyes", queue);
                sendGETrequest("http://"+IpAddress.IP_ADDRESS+":5000/lights/6", "Stand-By Lights", queue);
            }
        });

        Button searchingStatusButton = findViewById(R.id.searchingStatusBtn);
        searchingStatusButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                sendGETrequest("http://"+IpAddress.IP_ADDRESS+":5000/eyes/Searching", "Searching Eyes", queue);
                sendGETrequest("http://"+IpAddress.IP_ADDRESS+":5000/lights/1", "Searching Lights", queue);
            }
        });

        Button detectionStatusButton = findViewById(R.id.detectionStatusBtn);
        detectionStatusButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                sendGETrequest("http://"+IpAddress.IP_ADDRESS+":5000/eyes/LookDown", "LookDown Eyes", queue);
                sendGETrequest("http://"+IpAddress.IP_ADDRESS+":5000/lights/6", "Stand-By Lights", queue);
            }
        });

        Button emergencyStatusButton = findViewById(R.id.emergencyStatusBtn);
        emergencyStatusButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                sendGETrequest("http://"+IpAddress.IP_ADDRESS+":5000/eyes/Neutral", "Neutral Eyes", queue);
                sendGETrequest("http://"+IpAddress.IP_ADDRESS+":5000/lights/3", "Emergency Lights", queue);
            }
        });

        Button takeOffStatusButton = findViewById(R.id.takeOffStatusBtn);
        takeOffStatusButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                sendGETrequest("http://"+IpAddress.IP_ADDRESS+":5000/eyes/LookUp", "LookUp Eyes", queue);
                sendGETrequest("http://"+IpAddress.IP_ADDRESS+":5000/lights/8", "Take-Off Lights", queue);
            }
        });

        Button landingStatusButton = findViewById(R.id.landingStatusBtn);
        landingStatusButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                sendGETrequest("http://"+IpAddress.IP_ADDRESS+":5000/eyes/LookDown", "LookDown Eyes", queue);
                sendGETrequest("http://"+IpAddress.IP_ADDRESS+":5000/lights/10", "Landing Lights", queue);
            }
        });

        Button loveStatusButton = findViewById(R.id.loveStatusBtn);
        loveStatusButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                sendGETrequest("http://"+IpAddress.IP_ADDRESS+":5000/eyes/Heart", "Heart Eyes", queue);
                sendGETrequest("http://"+IpAddress.IP_ADDRESS+":5000/lights/14", "Valentine Lights", queue);
            }
        });

        Button happyStatusButton = findViewById(R.id.happyStatusBtn);
        happyStatusButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                sendGETrequest("http://"+IpAddress.IP_ADDRESS+":5000/eyes/Happy", "Happy Eyes", queue);
                sendGETrequest("http://"+IpAddress.IP_ADDRESS+":5000/lights/11", "Rainbow Lights", queue);
            }
        });

        Button lowBatteryStatusButton = findViewById(R.id.lowBatteryStatusBtn);
        lowBatteryStatusButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                sendGETrequest("http://"+IpAddress.IP_ADDRESS+":5000/eyes/Tired", "Tired Eyes", queue);
                sendGETrequest("http://"+IpAddress.IP_ADDRESS+":5000/lights/4", "Low Battery Lights", queue);
            }
        });
    }

    public void sendGETrequest(String url, final String command, RequestQueue queue){
        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
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

    public void displayToast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_LONG).show();
    }
}