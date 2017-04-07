package com.example.tanmay.elabs_attendance_geofencing;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tanmay on 14-02-2017.
 */

public class Attendance_Profile {

    public String Class;
    public String Roll;
    public double Attendance;
    public String time;

    public Attendance_Profile(){}

    public Attendance_Profile(String C, String R, double A, String time){
        Class=C;

        Roll = R;
        Attendance=A;
        this.time=time;

    }

}
