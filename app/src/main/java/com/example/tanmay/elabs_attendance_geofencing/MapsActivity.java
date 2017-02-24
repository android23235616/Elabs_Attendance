package com.example.tanmay.elabs_attendance_geofencing;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.security.auth.Subject;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnCameraChangeListener {

    private GoogleMap mMap;
    ArrayList<Geofence> mGeofence;
    ArrayList<LatLng> mGeofenceCoordinates;
    ArrayList<Integer> mGeofenceRadius;
    private GeofenceStore mGeofenceStore;
    Button Present;
    public static String mainSubject="";
    Intent i;
    DatabaseReference reference;
    SharedPreferences sharedPreferences;
    ProgressDialog dialog;
    //TextView textSubject;
    Handler handler;
    int patt=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);


        mapFragment.getMapAsync(this);

        initialize();

        mGeofenceCoordinates.add(new LatLng(Constants.Lat, Constants.Lng));//My room

        mGeofence.add(new Geofence.Builder()
                .setRequestId("Hello man Welcome to Elabs")
                // The coordinates of the center of the geofence and the radius in meters.
                .setCircularRegion(mGeofenceCoordinates.get(0).latitude, mGeofenceCoordinates.get(0).longitude, Constants.radius)
                .setExpirationDuration(Geofence.NEVER_EXPIRE)
                // Required when we use the transition type of GEOFENCE_TRANSITION_DWELL
                .setLoiteringDelay(30000)
                .setTransitionTypes(
                        Geofence.GEOFENCE_TRANSITION_ENTER
                                | Geofence.GEOFENCE_TRANSITION_DWELL
                                | Geofence.GEOFENCE_TRANSITION_EXIT).build());

        mGeofenceStore = new GeofenceStore(this, mGeofence);
        final CheckValidity validity = new CheckValidity(MapsActivity.this,"1000", true);
        Present.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog = ProgressDialog.show(MapsActivity.this,"Wait","1. Checking Permission");
                dialog.setCancelable(true);
                if (Constants.Has_Entered.equals(Constants.Entered)) {
                    preFunction();

                } else {
                    Both("Not present in the class.");
                }


    }

    private void preFunction(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                while(true){
                   // Both("Inside While Loop Validity is "+validity.isAllowed+" , "+validity.has_visited2);
                    if(validity.has_visited2){
                        if(validity.isAllowed){
                            try {
                                mainFunction();
                            } catch (Exception e) {
                                e.printStackTrace();
                                Log.i("errors", e.toString());
                            }
                        }else{
                            Both("You are not allowed to give attendance write now!");
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    dialog.cancel();
                                }
                            });
                        }
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                dialog.setMessage("2. Checking Timing Validation...");
                            }
                        });
                        break;
                    }else{
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }).start();
    }
        });
    }

    private void Both(final String s){
       handler.post(new Runnable() {
           @Override
           public void run() {
                Toast.makeText(MapsActivity.this,s,Toast.LENGTH_LONG).show();
           }
       });
        Log.d("info",s);
    }

    private void initialize() {
       // textSubject = (TextView)findViewById(R.id.subjectText);
        mGeofence = new ArrayList<Geofence>();
        mGeofenceCoordinates = new ArrayList<LatLng>();
        mGeofenceRadius = new ArrayList<Integer>();
        i = getIntent();
        mainSubject = i.getStringExtra("mainSubject");
        Present = (Button)findViewById(R.id.Present);
        handler = new Handler();
        sharedPreferences = getSharedPreferences(Constants.Registration_Shared_Preferences, Context.MODE_PRIVATE);
        reference = FirebaseDatabase.getInstance().getReference("Attendance");
        setTypeFace();
    }


    private void mainFunction() throws Exception {
        if(mainSubject.equals(Constants.subject_Changed_Condition)){
            Both("Please choose a subject");
        }else {
            reference = FirebaseDatabase.getInstance().getReference(mainSubject);
            checker();
            if(patt==0) {
                Attendance_Profile profile = new Attendance_Profile(mainSubject, "1000", 0, getDate());
                WriteToDatabase(profile);
            }
        }
    }

    private void checker() throws Exception{
        reference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if (!(dataSnapshot.getValue() instanceof Long)) {
                    Attendance_Profile profile = dataSnapshot.getValue(Attendance_Profile.class);
                    if (profile.Roll.equals(getRoll()) && !(profile.equals(null))) {
                        String presentDate = profile.time;
                        if (presentDate.equals(getDate())) {
                            Both("You have already given your attendance");
                        } else {
                            double per = profile.Attendance;
                            profile = new Attendance_Profile(mainSubject, getRoll(), per + 1, getDate());
                            WriteToDatabase(profile);
                            //patt=1;
                        }
                    }
                }
            }
            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        dialog.cancel();
                    }
                });
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private String getDate(){
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());

        int mYear = calendar.get(Calendar.YEAR);
        int mMonth = calendar.get(Calendar.MONTH);
        int mDay = calendar.get(Calendar.DAY_OF_MONTH);

        return mDay+" "+ Constants.Months[mMonth]+" "+mYear;
    }

    private void WriteToDatabase(Attendance_Profile profile){
        reference.child(profile.Roll).setValue(profile);
    }

    private String getRoll(){
        return sharedPreferences.getString("Roll","null");
    }

    private void setTypeFace(){
        Typeface t = Typeface.createFromAsset(getAssets(),"fonts/a.ttf");
        Present.setTypeface(t);



    }



    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(20.35138524475558, 85.82143073306530), 14));
        mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
        mMap.setIndoorEnabled(false);
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mMap.setMyLocationEnabled(true);

        mMap.setOnCameraChangeListener(this);

    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }




    @Override
    public void onCameraChange(CameraPosition Position) {

//        mMap.addCircle(new CircleOptions().center(mGeofenceCoordinates.get(0))
//                .radius(Constants.radius)
//                .fillColor(0x40ff0000)
//                .strokeColor(Color.TRANSPARENT).strokeWidth(1));

    }
}