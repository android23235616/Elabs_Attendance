package com.example.tanmay.elabs_attendance_geofencing;

/**
 * Created by PRANSHOO VERMA on 31-03-2017.
 */

public class cardView {
    private String name;
    private int thumbnail;

    public cardView(String name, int thumbnail) {
        this.name = name;
        this.thumbnail = thumbnail;
    }

    public int getThumbnail() {
        return thumbnail;
    }

    public String getName()
    {
        return name;
    }
}
