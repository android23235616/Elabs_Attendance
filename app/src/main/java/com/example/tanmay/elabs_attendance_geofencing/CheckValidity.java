package com.example.tanmay.elabs_attendance_geofencing;

import android.content.Context;
import android.provider.Settings;
import android.util.Log;
import android.view.Display;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
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
    DatabaseReference databaseReference,subjectReferrence;
    public boolean isNotDuplicate, isAllowed, has_visited2;
    public static boolean has_visited;
    private Context context;
    String mainSubject;
    private boolean Parallel;

    public CheckValidity(Context context, String Roll, boolean Parallel) {
        this.Roll = Roll;
        mainSubject = new MapsActivity().mainSubject;
        databaseReference = FirebaseDatabase.getInstance().getReference("Profile");
        subjectReferrence = FirebaseDatabase.getInstance().getReference(mainSubject);
        Duplicate();
        this.context = context;
        databaseReference.child("1000").setValue(new Profile("1000", "7"+System.currentTimeMillis(), ""));
        subjectReferrence.child("1000").setValue(new Attendance_Profile(mainSubject,"1000",12,1212+""+ System.currentTimeMillis()));
        this.Parallel = Parallel;
        if(Parallel)
        isAttendanceOn();

    }

    public void isAttendanceOn(){
        subjectReferrence.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                has_visited2=true;
                Long isAll = (Long)dataSnapshot.child("key").getValue();
                if(isAll!=null)
                {
                    if(isAll==1)
                        isAllowed=true;
                    else
                        isAllowed=false;
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

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
