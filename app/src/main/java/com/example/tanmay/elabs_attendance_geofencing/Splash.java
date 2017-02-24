package com.example.tanmay.elabs_attendance_geofencing;

import android.animation.Animator;
import android.content.Intent;
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
    ImageView logo;
    @Override
    protected void onCreate(Bundle savedInstancecState){
        super.onCreate(savedInstancecState);
        setContentView(R.layout.activity_splash);
        logo = (ImageView)findViewById(R.id.logo);
        logo.animate().alpha(1).setDuration(2400).setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                startActivity(new Intent(Splash.this, Registration.class));
                finish();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }
}
