package com.example.s152355.bluejayapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;


// All of these methods are the same as in the ActionsLog class

public class EmergenciesLog extends NavigationDrawer
        implements NavigationView.OnNavigationItemSelectedListener {

    Context thisContext;
    ListView emergencyListView;
    ProgressDialog pd;
    ArrayList<Emergency> emergencyArrayList = new ArrayList<>();

    String sql = "SELECT * FROM emergency ORDER BY emg_time ASC";

    // Create all elements contained inside the activity
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emergencies_log);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // This toast will be used for ordering the logbook
        final Toast toastField = Toast.makeText(EmergenciesLog.this,"Press REFRESH to re-order", Toast.LENGTH_SHORT);
        toastField.setGravity(Gravity.BOTTOM, 0, 200);

        Resources res = getResources();
        emergencyListView = (ListView) findViewById(R.id.emergencyListView);
        thisContext = this;

        // This is the spinner object with which the database can be ordered
        final Spinner sortSpinner = (Spinner) findViewById(R.id.sortSpinner);
        ArrayAdapter<CharSequence> sortAdapter = ArrayAdapter.createFromResource(this,
                R.array.sortEmergency, android.R.layout.simple_spinner_item);
        sortAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sortSpinner.setAdapter(sortAdapter);
        sortSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch(position){
                    case 0:
                        sql = "SELECT * FROM emergency ORDER BY emg_time ASC";
                        break;
                    case 1:
                        sql = "SELECT * FROM emergency ORDER BY emg_time DESC";
                        toastField.show();
                        break;
                    case 2:
                        sql = "SELECT * FROM emergency ORDER BY drone_id";
                        toastField.show();
                        break;
                    case 3:
                        sql = "SELECT * FROM emergency ORDER BY location";
                        toastField.show();
                        break;
                    case 4:
                        sql = "SELECT * FROM emergency ORDER BY category";
                        toastField.show();
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        // Both buttons do the same thing but are different in design
        Button btn = (Button) findViewById(R.id.getDataButton);
        btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                GetData retrieveData = new GetData();
                retrieveData.execute("");
            }
        });

        Button btnRefresh = (Button) findViewById(R.id.getDataButtonRefresh);
        btnRefresh.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                GetData retrieveData = new GetData();
                retrieveData.execute("");
            }
        });
    }

    /* Calls the ActionListAdapter to fill in the action_layout.xml file
    repeatedly for every object inside that array using this adapter. */
    public void showArrayList(){
        if(emergencyArrayList.size()>0){
            EmergencyListAdapter adapter = new EmergencyListAdapter(this, R.layout.emergency_layout, emergencyArrayList);
            emergencyListView.setAdapter(adapter);
        }
    }

    // These methods are called when the Load Logbook button is pressed
    private class GetData extends AsyncTask<String, String, String>{

        String msg = "";
        static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
        static final String DB_URL = "jdbc:mysql://" +
                DbStrings.DATABASE_URL + "/" +
                DbStrings.DATABASE_NAME;

        // Progress dialog that shows Blue Jay is connecting while connection is being made
        @Override
        protected void onPreExecute(){
            pd= new ProgressDialog(thisContext);
            pd.setTitle("Connecting");
            pd.setMessage("Please wait... Connecting to the Blue Jay database");
            pd.show();
        }

        // When the connection was succesfully made and the query succesfully performed
        public void onRetrieve(){
            // final MediaPlayer sendSound = MediaPlayer.create(thisContext, R.raw.send_sound);
            Button btn = (Button) findViewById(R.id.getDataButton);
            Button btnRefresh = (Button) findViewById(R.id.getDataButtonRefresh);
            btn.setVisibility(View.INVISIBLE);
            btnRefresh.setVisibility(View.VISIBLE);
            // sendSound.start();
        }

        // App tries to make a connection with the mysql database
        @Override
        protected String doInBackground(String... params) {

            Connection conn = null;
            Statement stmt = null;

            try {
                emergencyArrayList.clear();

                Class.forName(JDBC_DRIVER);
                conn = DriverManager.getConnection(DB_URL, DbStrings.USERNAME, DbStrings.PASSWORD);

                stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql);

                /* For every row in the database, it gets the variables, stores them as an
                Emergency-object and adds them to the array list */
                while (rs.next()){
                    int emg_id = rs.getInt("emg_id");
                    int drone_id = rs.getInt("drone_id");
                    String category = rs.getString("category");
                    String emg_time = rs.getString("emg_time");
                    String location = rs.getString("location");
                    int avg_resprate = rs.getInt("avg_resprate");
                    int min_resprate = rs.getInt("min_resprate");
                    int max_resprate = rs.getInt("max_resprate");
                    int avg_heartrate = rs.getInt("avg_heartrate");
                    int min_heartrate = rs.getInt("min_heartrate");
                    int max_heartrate = rs.getInt("max_heartrate");
                    int avg_temp = rs.getInt("avg_temp");
                    int min_temp = rs.getInt("min_temp");
                    int max_temp = rs.getInt("max_temp");

                    Emergency emergency = new Emergency(emg_id, drone_id, category, emg_time, location, avg_resprate, min_resprate, max_resprate, avg_heartrate, min_heartrate, max_heartrate, avg_temp, min_temp, max_temp);
                    emergencyArrayList.add(emergency);

                }

                msg = "Process complete.";

                rs.close();
                stmt.close();
                conn.close();

                // These toast messages will pop-up if the connection was unsuccesfull
            } catch (SQLException connError){
                Toast toastError = Toast.makeText(EmergenciesLog.this,"Can't connect to database. Please check your internet connection...", Toast.LENGTH_SHORT);
                toastError.setGravity(Gravity.BOTTOM, 0, 200);
            } catch (ClassNotFoundException e){
                Toast toastError = Toast.makeText(EmergenciesLog.this,"Can't connect to database. Please check your internet connection...", Toast.LENGTH_SHORT);
                toastError.setGravity(Gravity.BOTTOM, 0, 200);
            } finally {

                try{
                   if (stmt != null){
                       stmt.close();
                   }
                } catch (SQLException e) {
                    e.printStackTrace();
                }

                try{
                    if (conn != null){
                        conn.close();
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        // When the query was executed, it will call the showArrayList method
        @Override
        protected void onPostExecute(String msg){
            Runnable progressRunnable = new Runnable() {
                @Override
                public void run() {
                    pd.cancel();
                }};
            Handler pdCanceller = new Handler();
            pdCanceller.postDelayed(progressRunnable, 1500);
            onRetrieve();
            showArrayList();
        }
    }
}
