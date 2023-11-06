package com.example.s152355.bluejayapp;

/* This class defines an object called 'action' with all its attributes. In the activity
'ActionsLog', data from the online mysql database is imported and stored in an array that contains
these 'action' objects. (Not clear whether this will be needed in the final app, but it would
be cool if the drone could add a database entry whenever a new drone action is performed so that
the user can see what Blue Jay has done in the past couple of hours). */

public class Action {
    private int act_id;
    private String drone_id;
    private String category;
    private String act_time;
    private String battery_life;
    private String coordinates;
    private String flightstatus;
    private String ipAddress;
    private String phone;

    public Action(int act_id, String drone_id, String category, String act_time, String battery_life, String coordinates, String flightstatus, String ipAddress, String phone) {
        this.act_id = act_id;
        this.drone_id = drone_id;
        this.category = category;
        this.act_time = act_time;
        this.battery_life = battery_life;
        this.coordinates = coordinates;
        this.flightstatus = flightstatus;
        this.ipAddress = ipAddress;
        this.phone = phone;
    }

    public int getAct_id() {
        return act_id;
    }

    public void setAct_id(int act_id) {
        this.act_id = act_id;
    }

    public String getDrone_id() {
        return drone_id;
    }

    public void setDrone_id(String drone_id) {
        this.drone_id = drone_id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getAct_time() {
        return act_time;
    }

    public void setAct_time(String act_time) {
        this.act_time = act_time;
    }

    public String getBattery_life() {
        return battery_life;
    }

    public void setBattery_life(String battery_life) {
        this.battery_life = battery_life;
    }

    public String getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(String destination) {
        this.coordinates = coordinates;
    }

    public String getFlightstatus(){
        return flightstatus;
    }

    public void setFlightstatus(String flightstatus){
        this.flightstatus = flightstatus;
    }

    public String getIpAddress(){
        return ipAddress;
    }

    public void setIpAddress(String ipAddress){
        this.ipAddress = ipAddress;
    }

    public String getPhone(){
        return phone;
    }

    public void setPhone(String phone){
        this.phone = phone;
    }

}
