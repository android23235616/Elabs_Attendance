package com.example.tanmay.elabs_attendance_geofencing;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Registration extends AppCompatActivity {

    EditText Roll, Number, Email;
    Button Register;
    SharedPreferences preferences;
    SharedPreferences.Editor sharedPreferencesEditor;
    DatabaseReference reference, subjectReference;
    ProgressDialog dialog;
    CheckValidity validity ;
    Handler handler;
    TextView t1, t2, t3, t4, t5;
   // TextView textSubject;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        Variables_Init();
        if (isRegistered()) {
           startActivity(new Intent(this, MainSubject.class));
            finish();
        } else {
            sharedPreferencesEditor.putBoolean(Constants.Registration_Shared_Preferences_key, true);
            sharedPreferencesEditor.apply();
        }

        Register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              String r= Roll.getText().toString();
                validity=  new CheckValidity(Registration.this,r,false);
                dialog = ProgressDialog.show(Registration.this, "Registering", "Please Wait...");
                dialog.setCancelable(true);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        while(true){
                            if(validity.has_visited){
                                Register();
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
        setTypeface();
    }

    private void setTypeface(){
        Typeface tf = Typeface.createFromAsset(getAssets(), "fonts/c.ttf");
        t1.setTypeface(tf);
        t2.setTypeface(tf);
        t3.setTypeface(tf);
        t4.setTypeface(tf);
        t5.setTypeface(tf);
    }

    private void Register(){
        CheckChilds();
       final String R = Roll.getText().toString();
       final String N = Number.getText().toString();
       final String E = Email.getText().toString();

        if (!validity.Length()) {
            Display("Roll Number is Invalid");

        } else if (!validity.isNotDuplicate && validity.has_visited) {
            Display("Roll Number is Duplicate");


        } else if (validity.has_visited) {

             startActivity(new Intent(Registration.this, MainSubject.class));
             finish();
            handler.post(new Runnable() {
                @Override
                public void run() {


                    Profile profile = new Profile(R, N, E);
                    sharedPreferencesEditor.putString("Roll", R);
                    sharedPreferencesEditor.apply();
                    WriteToDatabase(profile);
                    InstantiateProfile();
                }
            });
        }
        validity.has_visited=false;
    }


    private void CheckChilds(){
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                dialog.cancel();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void Variables_Init(){
        Roll = (EditText) findViewById(R.id.Roll);
        Number = (EditText)findViewById(R.id.Number);
        Email = (EditText)findViewById(R.id.Email);
        Register =(Button)findViewById(R.id.register);
        reference = FirebaseDatabase.getInstance().getReference("Profile");
        preferences = getSharedPreferences(Constants.Registration_Shared_Preferences, Context.MODE_PRIVATE);
        sharedPreferencesEditor = preferences.edit();
        handler = new Handler();
        t1 = (TextView)findViewById(R.id.textview1);
        t2 = (TextView)findViewById(R.id.textview2);
        t3 = (TextView)findViewById(R.id.textview3);
        t4 = (TextView)findViewById(R.id.textview4);
        t5 = (TextView)findViewById(R.id.textview5);
    }

    private void WriteToDatabase(Profile profile){

        reference.child(profile.Roll).setValue(profile);
        reference.child("1000").setValue(new Profile("1000","",""));

    }

    private void Display(final String msg){
        handler.post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(Registration.this,msg,Toast.LENGTH_SHORT).show();
            }
        });
        Log.i("info",msg);
    }

    private boolean isRegistered(){
        return preferences.getBoolean(Constants.Registration_Shared_Preferences_key,false);
    }

    private void InstantiateProfile(){
        List<String> sub = new ArrayList<>();
        sub.add("Choose Your class");
        sub.add("Android");
        sub.add("Communication");
        sub.add("Embedded");
        sub.add("MATLAB");
        sub.add("Networking");
        for(int i=1; i<sub.size(); i++){
            subjectReference = FirebaseDatabase.getInstance().getReference(sub.get(i));
            Attendance_Profile profile = new Attendance_Profile(sub.get(i),Roll.getText().toString(), 0, "");
            WriteToProfileDatabase(profile);
        }
    }

    private void WriteToProfileDatabase(Attendance_Profile profile) {
        subjectReference.child(profile.Roll).setValue(profile);
        subjectReference.child("1000").setValue(new Attendance_Profile("1000","",0,0+""));
    }

}
