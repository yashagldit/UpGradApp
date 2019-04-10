package com.upgrad.internshipapp;

public class Tags {
    public String name, count, selected;

    public Tags(String name, String count, String selected) {
        this.name = name;
        this.count = count;
        this.selected = selected;
    }

    public String getSelected() {
        return selected;
    }

    public String getName() {
        return name;
    }

    public String getCount() {
        return count;
    }
}
