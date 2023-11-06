package com.example.s152355.bluejayapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/* This class is used in the ActionsLog activity to create a new block for every single action-
 object that is stored inside the array in ActionsLog. It fills in the action_layout.xml file
  repeatedly for every object inside that array using this adapter. */


public class ActionListAdapter extends ArrayAdapter<Action> {

    private Context thisContext;
    int thisResource;


    public ActionListAdapter(Context context, int resource, ArrayList<Action> objects) {
        super(context, resource, objects);
        thisContext = context;
        thisResource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        int act_id = getItem(position).getAct_id();
        String drone_id = getItem(position).getDrone_id();
        String category = getItem(position).getCategory();
        String act_time = getItem(position).getAct_time();
        String battery_life = getItem(position).getBattery_life();
        String coordinates = getItem(position).getCoordinates();
        String flightstatus = getItem(position).getFlightstatus();
        String ipAddress = getItem(position).getIpAddress();
        String phone = getItem(position).getPhone();

        Action action = new Action(act_id, drone_id, category, act_time, battery_life, coordinates, flightstatus, ipAddress, phone);

        LayoutInflater inflater = LayoutInflater.from(thisContext);
        convertView = inflater.inflate(thisResource, parent, false);

        TextView droneIdTextView = (TextView) convertView.findViewById(R.id.droneIdTextView);
        TextView actCategoryTextView = (TextView) convertView.findViewById(R.id.actCategoryTextView);
        TextView actDateTextView = (TextView) convertView.findViewById(R.id.actDateTextView);
        TextView batteryTextView = (TextView) convertView.findViewById(R.id.batteryTextView);
        TextView destinationTextView = (TextView) convertView.findViewById(R.id.destinationTextView);
        TextView flightStatusTextView = (TextView) convertView.findViewById(R.id.flightStatusTextView);
        TextView ipTextView = (TextView) convertView.findViewById(R.id.ipTextView);
        TextView phoneTextView = (TextView) convertView.findViewById(R.id.phoneTextView);

        droneIdTextView.setText(drone_id);
        actCategoryTextView.setText(category);
        actDateTextView.setText(act_time);
        destinationTextView.setText(coordinates);
        flightStatusTextView.setText(flightstatus);
        ipTextView.setText(ipAddress);
        batteryTextView.setText(battery_life);
        phoneTextView.setText(phone);
        return convertView;
    }
}