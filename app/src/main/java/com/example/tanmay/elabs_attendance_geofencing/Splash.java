package com.example.tanmay.elabs_attendance_geofencing;

import android.*;
import android.Manifest;
import android.animation.Animator;
import android.app.AppOpsManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.audiofx.BassBoost;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.ImageHeaderParser;


public class Splash extends AppCompatActivity {
    ImageView logo;
    boolean MockLocation;
    boolean flag=false;
    @Override
    protected void onCreate(Bundle savedInstancecState) {
        super.onCreate(savedInstancecState);
        setContentView(R.layout.activity_splash);
        logo = (ImageView) findViewById(R.id.logo);
        get_permissions();
        MockLocation = isMockLocationOn();
        if (!MockLocation) {
            if(flag)
            Toast.makeText(this, "Enable Mock Location On this Device!", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            if(!isNetWorkOn()){
                if(flag)
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

    private void get_permissions() {
        if(!checkPermissionLocation(this)){
            flag=false;
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.ACCESS_FINE_LOCATION},1);
        }else{
            flag=true;
        }
        if(!checkInternetPermission(this)){
            flag=false;
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.INTERNET},2);
        }else{
            flag=true;
        }
        if(!checkStoragePermission(this)){
            flag=false;
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE},3);
        }else{
            flag=true;
        }


    }

    public static boolean checkPermissionLocation(final Context context) {
        return ActivityCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    public static boolean checkInternetPermission(final Context context){
        return ActivityCompat.checkSelfPermission(context, Manifest.permission.INTERNET)==PackageManager.PERMISSION_GRANTED;
    }

    public static boolean checkStoragePermission(final Context context){
        return (ActivityCompat.checkSelfPermission(context,Manifest.permission.WRITE_EXTERNAL_STORAGE)==PackageManager.PERMISSION_GRANTED
                &&ActivityCompat.checkSelfPermission(context,Manifest.permission.READ_EXTERNAL_STORAGE)==PackageManager.PERMISSION_GRANTED);
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
