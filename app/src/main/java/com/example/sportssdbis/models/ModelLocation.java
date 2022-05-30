package com.example.sportssdbis.models;

public class ModelLocation {

    String id, title, location, schedule, description, uid;
    long timestamp;

    public ModelLocation() {
    }

    public ModelLocation(String id, String title, String location, String schedule, String description, String uid, long timestamp) {
        this.id = id;
        this.title = title;
        this.location = location;
        this.schedule = schedule;
        this.description = description;
        this.uid = uid;
        this.timestamp = timestamp;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getLocation() {
        return location;
    }

    public String getSchedule() {
        return schedule;
    }

    public String getDescription() {
        return description;
    }

    public String getUid() {
        return uid;
    }

    public long getTimestamp() {
        return timestamp;
    }
}
