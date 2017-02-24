package com.example.tanmay.elabs_attendance_geofencing;

import android.animation.Animator;
import android.app.AppOpsManager;
import android.content.Context;
import android.content.Intent;
import android.media.audiofx.BassBoost;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.os.Build;
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
    protected void onCreate(Bundle savedInstancecState) {
        super.onCreate(savedInstancecState);
        setContentView(R.layout.activity_splash);
        logo = (ImageView) findViewById(R.id.logo);
        MockLocation = isMockLocationOn();
        if (!MockLocation&&false) {

            Toast.makeText(this, "Enable Mock Location On this Device!", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            if(!isNetWorkOn()){
                Toast.makeText(this,"No Network is Available", Toast.LENGTH_LONG).show();
            }else
            {
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
    }
    private boolean isMockLocationOn(){
        boolean isMockLocation = false;
        try
        {
            //if marshmallow
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            {
                AppOpsManager opsManager = (AppOpsManager) this.getSystemService(Context.APP_OPS_SERVICE);
                isMockLocation = (opsManager.checkOp(AppOpsManager.OPSTR_MOCK_LOCATION, android.os.Process.myUid(), BuildConfig.APPLICATION_ID)== AppOpsManager.MODE_ALLOWED);
            }
            else
            {
                // in marshmallow this will always return true
                isMockLocation = !android.provider.Settings.Secure.getString(this.getContentResolver(), "mock_location").equals("0");
            }
        }
        catch (Exception e)
        {
            return isMockLocation;
        }

        return isMockLocation;

    }

    private boolean isNetWorkOn(){
        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        boolean enabled = false;
        if(networkInfo!=null&&networkInfo.isConnected()){
            enabled=true;
        }
        return enabled;
    }
}
