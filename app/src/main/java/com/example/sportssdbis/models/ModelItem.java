package com.example.sportssdbis.models;

public class ModelItem {
    String uid, id, title, location, description, category;
    long timestamp;

    public ModelItem() {
    }

    public ModelItem(String uid, String id, String title, String location, String description, String category, long timestamp) {
        this.uid = uid;
        this.id = id;
        this.title = title;
        this.location = location;
        this.description = description;
        this.category = category;
        this.timestamp = timestamp;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
