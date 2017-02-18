package com.example.tanmay.elabs_attendance_geofencing;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.ImageHeaderParser;

import pl.droidsonroids.gif.GifImageView;

public class Splash extends AppCompatActivity {
    ImageView gif;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        gif = (ImageView)findViewById(R.id.gifImageView);
        Glide.with(this).load(R.drawable.unn).asGif().centerCrop().crossFade().into(gif);
    }

}
