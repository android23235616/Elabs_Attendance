package com.example.tanmay.elabs_attendance_geofencing;

import android.content.Context;
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

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Registration extends AppCompatActivity {

    EditText Roll, Number, Email;
    Button Register;
    SharedPreferences preferences;
    SharedPreferences.Editor sharedPreferencesEditor;
    DatabaseReference reference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        Variables_Init();
        if(isRegistered()){
            Display("Registered");
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
                WriteToDatabase(profile);
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

}
