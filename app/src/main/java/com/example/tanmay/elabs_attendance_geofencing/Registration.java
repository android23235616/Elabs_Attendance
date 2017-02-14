package com.example.tanmay.elabs_attendance_geofencing;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class Registration extends AppCompatActivity {

    EditText Roll, Number, Email;
    Button Register;
    SharedPreferences preferences;
    SharedPreferences.Editor sharedPreferencesEditor;
    DatabaseReference reference, subjectReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        Variables_Init();
        if(isRegistered()){
            startActivity(new Intent(this, MapsActivity.class));
            finish();
        }else{
            sharedPreferencesEditor.putBoolean(Constants.Registration_Shared_Preferences_key,true);
            sharedPreferencesEditor.apply();
        }

        Register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String R = Roll.getText().toString();
                String N = Number.getText().toString();
                String E = Email.getText().toString();
                Profile profile = new Profile(R,N,E);
                sharedPreferencesEditor.putString("Roll",R);
                sharedPreferencesEditor.apply();
                WriteToDatabase(profile);
                InstantiateProfile();
                startActivity(new Intent(Registration.this, MapsActivity.class));
                finish();
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
    }

    private void WriteToDatabase(Profile profile){
        reference.child(profile.Roll).setValue(profile);
    }

    private void Display(String msg){
        Toast.makeText(this,msg, Toast.LENGTH_SHORT).show();
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
