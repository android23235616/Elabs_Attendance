package com.example.tanmay.elabs_attendance_geofencing;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class MainSubject extends AppCompatActivity {

    TextView android, matlab, communication, embedded;
    int mod=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_subject);
        initialise();
        final Intent i = new Intent(this, MapsActivity.class);
        android.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                i.putExtra("mainSubject","Android");
                startActivity(i);
            }
        });

        matlab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                i.putExtra("mainSubject","MATLAB");
                startActivity(i);
            }
        });

        embedded.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                i.putExtra("mainSubject","Embedded");
                startActivity(i);
            }
        });

        communication.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                i.putExtra("mainSubject","Communication");
                startActivity(i);
            }
        });
    }



    private void initialise(){

        android = (TextView)findViewById(R.id.android);
        matlab = (TextView)findViewById(R.id.MATLAB);
        communication = (TextView)findViewById(R.id.COMMUNICATION);
        embedded = (TextView)findViewById(R.id.EMBEDDED);
        setTypeface(this.android);
        setTypeface(this.communication);
        setTypeface(this.embedded);
        setTypeface(this.matlab);
    }

    private void setTypeface(TextView t){
        Typeface typeFace = Typeface.createFromAsset(getAssets(), "fonts/android.ttf");
        t.setTypeface(typeFace);
    }
}

