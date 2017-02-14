package com.example.tanmay.elabs_attendance_geofencing;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
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
    private Spinner subjects;
    Button Present;
    private String mainSubject="";
    DatabaseReference reference;
    SharedPreferences sharedPreferences;
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

        mGeofenceStore=new GeofenceStore(this,mGeofence);

        Present.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Constants.Has_Entered.equals(Constants.Entered)){
                    mainFunction();

                }else{
                    Both("Not present in the class.");
                }

            }
        });

        subjects.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mainSubject =parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void Both(String s){
        Toast.makeText(this,s,Toast.LENGTH_SHORT).show();
        Log.d("info",s);
    }

    private void initialize() {
        mGeofence = new ArrayList<Geofence>();
        mGeofenceCoordinates = new ArrayList<LatLng>();
        mGeofenceRadius = new ArrayList<Integer>();
        subjects = (Spinner)findViewById(R.id.subjects);
        Present = (Button)findViewById(R.id.Present);
        setUpSpinner();
        sharedPreferences = getSharedPreferences(Constants.Registration_Shared_Preferences, Context.MODE_PRIVATE);
        reference = FirebaseDatabase.getInstance().getReference("Attendance");
    }


    private void mainFunction(){
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

    private void checker(){
        reference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Attendance_Profile profile=dataSnapshot.getValue(Attendance_Profile.class);
                if(profile.Roll.equals(getRoll())&&!(profile.equals(null))){
                    double per = profile.Attendance;
                    profile = new Attendance_Profile(mainSubject, getRoll(), per+1, getDate());
                    WriteToDatabase(profile);
                    //patt=1;
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

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

    private void setUpSpinner(){
        List<String> sub = new ArrayList<>();
        sub.add("Choose Your class");
        sub.add("Android");
        sub.add("Communication");
        sub.add("Embedded");
        sub.add("MATLAB");
        sub.add("Networking");

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,sub);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        subjects.setAdapter(arrayAdapter);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(20.35138524475558, 85.82143073306530), 14));
        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
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

        mMap.addCircle(new CircleOptions().center(mGeofenceCoordinates.get(0))
                .radius(Constants.radius)
                .fillColor(0x40ff0000)
                .strokeColor(Color.TRANSPARENT).strokeWidth(1));

    }
}