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

public class ActionsLog extends NavigationDrawer
        implements NavigationView.OnNavigationItemSelectedListener {

    Context thisContext;
    ListView actionsListView;
    ProgressDialog pd;
    ArrayList<Action> actionArrayList = new ArrayList<>();
    String sql = "SELECT * FROM action ORDER BY act_time DESC";


    // Create all interactive objects contained inside the activity
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actions_log);
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
        final Toast toastField = Toast.makeText(ActionsLog.this,"Press REFRESH to re-order", Toast.LENGTH_SHORT);
        toastField.setGravity(Gravity.BOTTOM, 0, 200);

        Resources res = getResources();
        actionsListView = (ListView) findViewById(R.id.actionsListView);
        thisContext = this;

        // This is the spinner object with which the database can be ordered
        final Spinner sortSpinnerAction = (Spinner) findViewById(R.id.sortSpinnerAction);
        ArrayAdapter<CharSequence> sortAdapter = ArrayAdapter.createFromResource(this,
                R.array.sortAction, android.R.layout.simple_spinner_item);
        sortAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sortSpinnerAction.setAdapter(sortAdapter);
        sortSpinnerAction.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch(position){
                    case 0:
                        sql = "SELECT * FROM action ORDER BY act_time DESC";
                        toastField.show();
                        break;
                    case 1:
                        sql = "SELECT * FROM action ORDER BY act_time ASC";
                        toastField.show();
                        break;
                    case 2:
                        sql = "SELECT * FROM action ORDER BY drone_id";
                        toastField.show();
                        break;
                    case 3:
                        sql = "SELECT * FROM action ORDER BY coordinates";
                        toastField.show();
                        break;
                    case 4:
                        sql = "SELECT * FROM action ORDER BY category";
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
                ActionsLog.GetData retrieveData = new ActionsLog.GetData();
                retrieveData.execute("");
            }
        });

        Button btnRefresh = (Button) findViewById(R.id.getDataButtonRefresh);
        btnRefresh.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                ActionsLog.GetData retrieveData = new ActionsLog.GetData();
                retrieveData.execute("");
            }
        });
    }

    /* Calls the ActionListAdapter to fill in the action_layout.xml file
    repeatedly for every object inside that array using this adapter. */
    public void showArrayList(){
        if(actionArrayList.size()>0){
            ActionListAdapter adapter = new ActionListAdapter(this, R.layout.action_layout, actionArrayList);
            actionsListView.setAdapter(adapter);
        }
    }

    // These methods are called when the Load Logbook button is pressed
    private class GetData extends AsyncTask<String, String, String> {

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
                actionArrayList.clear();
                Class.forName(JDBC_DRIVER);
                conn = DriverManager.getConnection(DB_URL, DbStrings.USERNAME, DbStrings.PASSWORD);
                stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql);
                /* For every row in the database, it gets the variables, stores them as an
                Action-object and adds them to the array list */
                while (rs.next()){
                    int act_id = rs.getInt("act_id");
                    String drone_id = rs.getString("drone_id");
                    String category = rs.getString("category");
                    String act_time = rs.getString("act_time");
                    String battery_life = rs.getString("battery_life").replaceAll("%", "")+"%";
                    String coordinates = rs.getString("coordinates");
                    String flightstatus = rs.getString("flightstatus");
                    String ipAddress = rs.getString("ip_address");
                    String phone = rs.getString("phone");
                    Action action = new Action(act_id, drone_id, category, act_time, battery_life, coordinates, flightstatus, ipAddress, phone);
                    actionArrayList.add(action);
                }
                msg = "Process complete.";
                rs.close();
                stmt.close();
                conn.close();
                // These toast messages will pop-up if the connection was unsuccesfull
            } catch (SQLException connError){
                Toast toastError = Toast.makeText(ActionsLog.this,"Can't connect to database. Please check your internet connection...", Toast.LENGTH_SHORT);
                toastError.setGravity(Gravity.BOTTOM, 0, 200);
            } catch (ClassNotFoundException e){
                Toast toastError = Toast.makeText(ActionsLog.this,"Can't connect to database. Please check your internet connection...", Toast.LENGTH_SHORT);
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