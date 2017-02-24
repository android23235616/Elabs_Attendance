package com.example.tanmay.elabs_attendance_geofencing;

import android.animation.Animator;
import android.content.Intent;
import android.media.audiofx.BassBoost;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.ImageHeaderParser;

import pl.droidsonroids.gif.GifImageView;

public class Splash extends AppCompatActivity {
    ImageView logo;
    boolean MockLocation;
    @Override
    protected void onCreate(Bundle savedInstancecState){
        super.onCreate(savedInstancecState);
        setContentView(R.layout.activity_splash);
        logo = (ImageView)findViewById(R.id.logo);
        MockLocation=isMockLocationOn();
        if(MockLocation){

            Toast.makeText(this, "Please switch off Mock Location", Toast.LENGTH_SHORT).show();
            finish();
        }
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

    private boolean isMockLocationOn(){
        if(Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ALLOW_MOCK_LOCATION).equals(0)){
            return false;
        }else{
            return true;
        }
    }
}
