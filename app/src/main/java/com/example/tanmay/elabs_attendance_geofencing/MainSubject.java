package com.example.tanmay.elabs_attendance_geofencing;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class MainSubject extends AppCompatActivity {

    TextView android, matlab, communication, embedded;
    int mod=0;
    private List<cardView> albumList=new ArrayList<>();

    private RecyclerView recylerView;
    private CoordinatorLayout relative_layout;
    private RecyclerView.LayoutManager layoutManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_subject);
        initialise();
        prepareAlbum();

        layoutManager=new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        recylerView.setLayoutManager(layoutManager);
        adapter a=new adapter(this,albumList);
        recylerView.setAdapter(a);



 //       final Intent i = new Intent(this, MapsActivity.class);
//        android.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                i.putExtra("mainSubject","Android");
//                startActivity(i);
//            }
//        });

//        matlab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                i.putExtra("mainSubject","MATLAB");
//                startActivity(i);
//            }
//        });
//
//        embedded.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                i.putExtra("mainSubject","Embedded");
//                startActivity(i);
//            }
//        });
//
//        communication.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                i.putExtra("mainSubject","Communication");
//                startActivity(i);
//            }
//        });
    }



    private void initialise(){

        android = (TextView)findViewById(R.id.android);
        matlab = (TextView)findViewById(R.id.MATLAB);
        communication = (TextView)findViewById(R.id.COMMUNICATION);
        embedded = (TextView)findViewById(R.id.EMBEDDED);
//        setTypeface(this.android);
  //      setTypeface(this.communication);
    //    setTypeface(this.embedded);
      //  setTypeface(this.matlab);
        recylerView=(RecyclerView)findViewById(R.id.Recyler_view);
        relative_layout=(CoordinatorLayout)findViewById(R.id.activity_main);
    }

    private void setTypeface(TextView t){
        Typeface typeFace = Typeface.createFromAsset(getAssets(), "fonts/android.ttf");
        t.setTypeface(typeFace);
    }

    private void prepareAlbum()
    {
        int covers[]=new int[]{R.drawable.android,R.drawable.matlab,R.drawable.aduino,R.drawable.communication};

        cardView v=new cardView("A N D R O I D",covers[0]);
        albumList.add(v);
        cardView v1=new cardView("M A T L A B",covers[1]);
        albumList.add(v1);
        cardView v2=new cardView("A D U I N O",covers[2]);
        albumList.add(v2);
        cardView v3=new cardView("C O M M U N I C A T I O N",covers[3]);
        albumList.add(v3);

    }
}

