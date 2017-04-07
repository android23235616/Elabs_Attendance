package com.example.tanmay.elabs_attendance_geofencing;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.nfc.Tag;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.util.ArrayList;


public class GeofenceStore  implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, ResultCallback<Status>, LocationListener {
    private final String Tag = this.getClass().getSimpleName();
    private Context mContext;
    private GoogleApiClient mGoogleApiClient;
    private PendingIntent mPendingIntent;
    private ArrayList<Geofence> mGeofence;
    private GeofencingRequest mGeofencingRequest;
    private LocationRequest mLocationRequest;

    public GeofenceStore(Context context, ArrayList<Geofence> geofence) {
        mContext = context;
        mGeofence = new ArrayList<Geofence>(geofence);
        mPendingIntent = null;
        mGoogleApiClient = new GoogleApiClient.Builder(context).addApi(LocationServices.API).addConnectionCallbacks(this).addOnConnectionFailedListener(this).build();

        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(4000).setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mGoogleApiClient.connect();
    }


    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        mGeofencingRequest = new GeofencingRequest.Builder().addGeofences(mGeofence).build();
        mPendingIntent = createRequestPendingIntent();

        if (ActivityCompat.checkSelfPermission(mContext, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        PendingResult<Status> pendingResult = LocationServices.GeofencingApi.addGeofences(mGoogleApiClient, mGeofencingRequest, mPendingIntent);

        pendingResult.setResultCallback(this);
    }

    private PendingIntent createRequestPendingIntent() {
        if(mPendingIntent==null)
        {
            Intent intent=new Intent(mContext,GeofenceIntentService.class);
            mPendingIntent=PendingIntent.getService(mContext,0,intent,PendingIntent.FLAG_UPDATE_CURRENT);

        }
        return mPendingIntent;
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onResult(@NonNull Status status) {

    }
}
