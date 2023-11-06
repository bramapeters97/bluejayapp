package com.example.s152355.bluejayapp;


/* This class defines an object called 'emergency' with all its attributes. In the activity
'EmergenciesLog', data from the online mysql database is imported and stored in an array that contains
these 'emergency' objects. The purpose of this is that in the end, whenever the cameras of the drone
detect something, it will create a new entry in the mysql database. Then, finally the app will be
able to show all historical data of the emergencies that the drone has detected. */


public class Emergency {
    private int emg_id;
    private int drone_id;
    private String category;
    private String emg_time;
    private String location;
    private int max_resprate;
    private int min_resprate;
    private int avg_resprate;
    private int max_heartrate;
    private int min_heartrate;
    private int avg_heartrate;
    private int max_temp;
    private int min_temp;
    private int avg_temp;

    public Emergency(int emg_id, int drone_id, String category, String emg_time, String location, int max_resprate, int min_resprate, int avg_resprate, int max_heartrate, int min_heartrate, int avg_heartrate, int max_temp, int min_temp, int avg_temp) {
        this.emg_id = emg_id;
        this.drone_id = drone_id;
        this.category = category;
        this.emg_time = emg_time;
        this.location = location;
        this.max_resprate = max_resprate;
        this.min_resprate = min_resprate;
        this.avg_resprate = avg_resprate;
        this.max_heartrate = max_heartrate;
        this.min_heartrate = min_heartrate;
        this.avg_heartrate = avg_heartrate;
        this.max_temp = max_temp;
        this.min_temp = min_temp;
        this.avg_temp = avg_temp;
    }

    public int getEmg_id() {
        return emg_id;
    }

    public void setEmg_id(int emg_id) {
        this.emg_id = emg_id;
    }

    public int getDrone_id() {
        return drone_id;
    }

    public void setDrone_id(int drone_id) {
        this.drone_id = drone_id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getEmg_time() {
        return emg_time;
    }

    public void setEmg_time(String emg_time) {
        this.emg_time = emg_time;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getMax_resprate() {
        return max_resprate;
    }

    public void setMax_resprate(int max_resprate) {
        this.max_resprate = max_resprate;
    }

    public int getMin_resprate() {
        return min_resprate;
    }

    public void setMin_resprate(int min_resprate) {
        this.min_resprate = min_resprate;
    }

    public int getAvg_resprate() {
        return avg_resprate;
    }

    public void setAvg_resprate(int avg_resprate) {
        this.avg_resprate = avg_resprate;
    }

    public int getMax_heartrate() {
        return max_heartrate;
    }

    public void setMax_heartrate(int max_heartrate) {
        this.max_heartrate = max_heartrate;
    }

    public int getMin_heartrate() {
        return min_heartrate;
    }

    public void setMin_heartrate(int min_heartrate) {
        this.min_heartrate = min_heartrate;
    }

    public int getAvg_heartrate() {
        return avg_heartrate;
    }

    public void setAvg_heartrate(int avg_heartrate) {
        this.avg_heartrate = avg_heartrate;
    }

    public int getMax_temp() {
        return max_temp;
    }

    public void setMax_temp(int max_temp) {
        this.max_temp = max_temp;
    }

    public int getMin_temp() {
        return min_temp;
    }

    public void setMin_temp(int min_temp) {
        this.min_temp = min_temp;
    }

    public int getAvg_temp() {
        return avg_temp;
    }

    public void setAvg_temp(int avg_temp) {
        this.avg_temp = avg_temp;
    }
}
