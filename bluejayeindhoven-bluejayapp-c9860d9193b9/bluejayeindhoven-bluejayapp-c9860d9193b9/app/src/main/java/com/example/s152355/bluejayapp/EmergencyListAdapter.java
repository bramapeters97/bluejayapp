package com.example.s152355.bluejayapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/* This class is used in the EmergenciesLog activity to create a new block for every single action-
 object that is stored inside the array in EmergenciesLog. It fills in the emergency_layout.xml file
  repeatedly for every object inside that array using this adapter. */

public class EmergencyListAdapter extends ArrayAdapter<Emergency> {

    private Context thisContext;
    int thisResource;

    public EmergencyListAdapter(Context context, int resource, ArrayList<Emergency> objects) {
        super(context, resource, objects);
        thisContext = context;
        thisResource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        int emg_id = getItem(position).getEmg_id();
        int drone_id = getItem(position).getDrone_id();
        String category = getItem(position).getCategory();
        String emg_time = getItem(position).getEmg_time();
        String location = getItem(position).getLocation();
        int avg_resprate = getItem(position).getAvg_resprate();
        int min_resprate = getItem(position).getMin_resprate();
        int max_resprate = getItem(position).getMax_resprate();
        int avg_heartrate = getItem(position).getAvg_heartrate();
        int min_heartrate = getItem(position).getMin_heartrate();
        int max_heartrate = getItem(position).getMax_heartrate();
        int avg_temp = getItem(position).getAvg_temp();
        int min_temp = getItem(position).getMin_temp();
        int max_temp = getItem(position).getMax_temp();

        Emergency emergency = new Emergency(emg_id, drone_id, category, emg_time, location, avg_resprate, min_resprate, max_resprate, avg_heartrate, min_heartrate, max_heartrate, avg_temp, min_temp, max_temp);

        LayoutInflater inflater = LayoutInflater.from(thisContext);
        convertView = inflater.inflate(thisResource, parent, false);

        TextView idTextView = (TextView) convertView.findViewById(R.id.idTextView);
        TextView emgCategoryTextView = (TextView) convertView.findViewById(R.id.emgCategoryTextView);
        TextView droneTextView = (TextView) convertView.findViewById(R.id.droneTextView);
        TextView dateTextView = (TextView) convertView.findViewById(R.id.dateTextView);
        TextView locationTextView = (TextView) convertView.findViewById(R.id.locationTextView);
        TextView heartAvg = (TextView) convertView.findViewById(R.id.heartAvg);
        TextView heartMin = (TextView) convertView.findViewById(R.id.heartMin);
        TextView heartMax = (TextView) convertView.findViewById(R.id.heartMax);
        TextView tempAvg = (TextView) convertView.findViewById(R.id.tempAvg);
        TextView tempMin = (TextView) convertView.findViewById(R.id.tempMin);
        TextView tempMax = (TextView) convertView.findViewById(R.id.tempMax);
        TextView respAvg = (TextView) convertView.findViewById(R.id.respAvg);
        TextView respMin = (TextView) convertView.findViewById(R.id.respMin);
        TextView respMax = (TextView) convertView.findViewById(R.id.respMax);

        idTextView.setText(Integer.toString(emg_id));
        emgCategoryTextView.setText(category);
        dateTextView.setText(emg_time);
        locationTextView.setText(location);

        if (drone_id == 0){
            droneTextView.setText("NULL");
        } else {
            droneTextView.setText(Integer.toString(drone_id));
        }

        if (avg_heartrate==0){
            heartAvg.setText("NULL");
        } else {
            heartAvg.setText(Integer.toString(avg_heartrate));
        }

        if (min_heartrate==0){
            heartMin.setText("NULL");
        } else {
            heartMin.setText(Integer.toString(min_heartrate));
        }

        if (max_heartrate==0){
            heartMax.setText("NULL");
        } else {
            heartMax.setText(Integer.toString(max_heartrate));
        }

        if (avg_resprate==0){
            respAvg.setText("NULL");
        } else {
            respAvg.setText(Integer.toString(avg_resprate));
        }

        if (max_resprate==0){
            respMax.setText("NULL");
        } else {
            respMax.setText(Integer.toString(max_resprate));
        }

        if (min_resprate==0){
            respMin.setText("NULL");
        } else {
            respMin.setText(Integer.toString(min_resprate));
        }

        if (avg_temp==0){
            tempAvg.setText("NULL");
        } else {
            tempAvg.setText(Integer.toString(avg_temp));
        }

        if (max_temp==0){
            tempMax.setText("NULL");
        } else {
            tempMax.setText(Integer.toString(max_temp));
        }

        if (min_temp==0){
            tempMin.setText("NULL");
        } else {
            tempMin.setText(Integer.toString(min_temp));
        }

        return convertView;
    }
}
