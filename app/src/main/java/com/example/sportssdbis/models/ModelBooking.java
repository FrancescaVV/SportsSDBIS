package com.example.sportssdbis.models;

public class ModelBooking {

    String location, time, date, id, uid;
    long  timestamp;

    public ModelBooking() {
    }

    public ModelBooking(String location, String time, String date, String id, String uid, long timestamp) {
        this.location = location;
        this.time = time;
        this.date = date;
        this.id = id;
        this.uid = uid;
        this.timestamp = timestamp;
    }

    public String getLocation() {
        return location;
    }

    public String getTime() {
        return time;
    }

    public String getDate() {
        return date;
    }

    public String getId() {
        return id;
    }

    public String getUid() {
        return uid;
    }

    public long getTimestamp() {
        return timestamp;
    }
}
