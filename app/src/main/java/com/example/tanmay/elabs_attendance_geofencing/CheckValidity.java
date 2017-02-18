package com.example.tanmay.elabs_attendance_geofencing;

import android.content.Context;
import android.util.Log;
import android.view.Display;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by Tanmay on 18-02-2017.
 */

public class CheckValidity {

    String Roll;
    DatabaseReference databaseReference;
    public boolean isNotDuplicate;
    public static boolean has_visited;
    private Context context;

    public CheckValidity(Context context, String Roll) {
        this.Roll = Roll;
        databaseReference = FirebaseDatabase.getInstance().getReference("Profile");
        Duplicate();
        this.context = context;
        databaseReference.child("1000").setValue(new Profile("1000", "7"+System.currentTimeMillis(), ""));

    }

    public boolean Length() {
        if (Roll.length() != 7)
            return false;
        else
            return true;
    }

    

    private void Duplicate() {
        Log.e("error","I am called");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.i("database", dataSnapshot.toString());
                 Log.e("error","I am inside f");
                has_visited=true;
                isNotDuplicate=true;
                for (DataSnapshot shot : dataSnapshot.getChildren()) {
                    if (shot.getValue(Profile.class).Roll.equals(Roll)) {
                        isNotDuplicate = false;

                        break;
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }



}
