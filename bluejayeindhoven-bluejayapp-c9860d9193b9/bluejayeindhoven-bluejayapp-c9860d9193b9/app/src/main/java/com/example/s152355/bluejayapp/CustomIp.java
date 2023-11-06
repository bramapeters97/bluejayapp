package com.example.s152355.bluejayapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

public class CustomIp extends AppCompatActivity {

    EditText editText;
    ImageButton imageButton;
    private static int SPLASH_TIME_OUT = 1000;
    public String text;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_ip);

        // Instantiate the RequestQueue.
        final RequestQueue queue = Volley.newRequestQueue(this);
        ImageView backButton = (ImageView) findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent homeIntent = new Intent(CustomIp.this, LogIn.class);
                        showProgress();
                        homeIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        startActivityForResult(homeIntent, 0);
                    }
                }, SPLASH_TIME_OUT);
            }
        });

        editText = findViewById(R.id.editText);
        imageButton = findViewById(R.id.imageButton);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editText.getText().toString().equals("192.168.1.126")){
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Intent homeIntent = new Intent(CustomIp.this, MainActivity.class);
                            showProgress();
                            IpAddress.IP_ADDRESS = "192.168.1.126";
                            IpAddress.DRONE_NAME = "Blue Jay 2";
                            startActivityForResult(homeIntent, 0);
                            displayToast("Selected IP-Address: " + "192.168.1.126");
                        }
                    }, SPLASH_TIME_OUT);
                } else if (editText.getText().toString().equals("192.168.1.125")) {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Intent homeIntent = new Intent(CustomIp.this, MainActivity.class);
                            showProgress();
                            IpAddress.IP_ADDRESS = "192.168.1.125";
                            IpAddress.DRONE_NAME = "Blue Jay 1";
                            startActivityForResult(homeIntent, 0);
                            displayToast("Selected IP-Address: " + "192.168.1.125");
                        }
                    }, SPLASH_TIME_OUT);
                } else if (editText.getText().toString().equals("192.168.1.127")) {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Intent homeIntent = new Intent(CustomIp.this, MainActivity.class);
                            showProgress();
                            IpAddress.IP_ADDRESS = "192.168.1.127";
                            IpAddress.DRONE_NAME = "Blue Jay 3";
                            startActivityForResult(homeIntent, 0);
                            displayToast("Selected IP-Address: " + "192.168.1.127");
                        }
                    }, SPLASH_TIME_OUT);
                } else {
                    sendTestRequest("http://"+editText.getText().toString()+":5000/test", "", queue);
                }
            }
        });
    }

    public void displayToast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_LONG).show();
    }

    public void showProgress(){
        TextView textView1 = (TextView) findViewById(R.id.textView5);
        TextView textView2 = (TextView) findViewById(R.id.textView6);
        EditText editText = (EditText) findViewById(R.id.editText);
        ImageButton imageButton = (ImageButton) findViewById(R.id.imageButton);
        ProgressBar progressBar3 = (ProgressBar) findViewById(R.id.progressBar3);
        ImageView backButton = (ImageView) findViewById(R.id.backButton);

        textView1.setVisibility(View.INVISIBLE);
        textView2.setVisibility(View.INVISIBLE);
        editText.setVisibility(View.INVISIBLE);
        imageButton.setVisibility(View.INVISIBLE);
        progressBar3.setVisibility(View.VISIBLE);
        backButton.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, LogIn.class);
        startActivity(intent);
    }

    public void sendTestRequest(String url, final String command, RequestQueue queue){
        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Intent homeIntent = new Intent(CustomIp.this, MainActivity.class);
                                showProgress();
                                IpAddress.IP_ADDRESS = editText.getText().toString();
                                IpAddress.DRONE_NAME = editText.getText().toString();
                                startActivityForResult(homeIntent, 0);
                                displayToast("Selected IP-Address: " + editText.getText().toString());
                            }
                        }, SPLASH_TIME_OUT);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                displayToast("Could not connect to this IP-address. Please check whether you are connected to Blue Jay WiFi, the drone is switched on and you entered the IP-address correctly");
            }
        });
        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }
}

