package com.example.quang.gallerytrain.models;

import java.util.ArrayList;

public class Groups {
    private ArrayList<Images> images;
    private String days;
    private String month;
    private String location;
    public Groups(){}

    public Groups(ArrayList<Images> images, String days, String month, String location) {
        this.images = images;
        this.days = days;
        this.month = month;
        this.location = location;
    }

    public ArrayList<Images> getImages() {
        return images;
    }

    public void setImages(ArrayList<Images> images) {
        this.images = images;
    }

    public String getDays() {
        return days;
    }

    public void setDays(String days) {
        this.days = days;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
