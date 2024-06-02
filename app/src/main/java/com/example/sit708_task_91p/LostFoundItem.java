package com.example.sit708_task_91p;

public class LostFoundItem {

    // Attributes of the lost or found item
    private String title;
    private String description;
    private String phone;
    private String date;
    private String location;
    private double latitude;
    private double longitude;

    // Constructor to initialize a LostFoundItem object with all attributes
    public LostFoundItem(String title, String description, String phone, String date, String location,
                         double latitude, double longitude) {
        this.title = title;
        this.description = description;
        this.phone = phone;
        this.date = date;
        this.location = location;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    // Get the title of the item
    public String getTitle() {
        return title;
    }

    // Set the title of the item
    public void setTitle(String title) {
        this.title = title;
    }

    // Get the description of the item
    public String getDescription() {
        return description;
    }

    // Set the description of the item
    public void setDescription(String description) {
        this.description = description;
    }

    // Get the contact phone number
    public String getPhone() {
        return phone;
    }

    // Set the contact phone number
    public void setPhone(String phone) {
        this.phone = phone;
    }

    // Get the date associated with the item
    public String getDate() {
        return date;
    }

    // Set the date associated with the item
    public void setDate(String date) {
        this.date = date;
    }

    // Get the location where the item was found or lost
    public String getLocation() {
        return location;
    }

    // Set the location where the item was found or lost
    public void setLocation(String location) {
        this.location = location;
    }

    // Get the latitude coordinate of the item's location
    public double getLatitude() {
        return latitude;
    }

    // Set the latitude coordinate of the item's location
    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    // Get the longitude coordinate of the item's location
    public double getLongitude() {
        return longitude;
    }

    // Set the longitude coordinate of the item's location
    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}
