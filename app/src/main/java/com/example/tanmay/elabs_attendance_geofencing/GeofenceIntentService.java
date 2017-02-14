package com.example.tanmay.elabs_attendance_geofencing;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.os.PowerManager;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;
import android.util.Log;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingEvent;

import java.util.List;

/**
 * Created by Tanmay on 13-02-2017.
 */

public class GeofenceIntentService extends IntentService{

    private final String Tag=this.getClass().getCanonicalName();

    public GeofenceIntentService() {
        super("GeofenceIntentService");
    }

    public void onCreate()
    {
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        GeofencingEvent geofencingEvent= GeofencingEvent.fromIntent(intent);
        Log.v(Tag,"onHandleIntent");
        if(!geofencingEvent.hasError()  )
        {
            int transition=geofencingEvent.getGeofenceTransition();
            String notificationTitle;
            switch (transition)
            {
                case Geofence.GEOFENCE_TRANSITION_ENTER:
                    notificationTitle="Entered";
                    Constants.Has_Entered=Constants.Entered;
                    break;
                case  Geofence.GEOFENCE_TRANSITION_EXIT:
                    notificationTitle="Exited";
                    Constants.Has_Entered=Constants.Not_Entered;
                    break;
                case Geofence.GEOFENCE_TRANSITION_DWELL:
                    notificationTitle="Dwelling,ghum rha hai bhai";
                    Constants.Has_Entered=Constants.Entered;
                    break;

                default:
                    notificationTitle="Geofence default";

            }
            sendNotification(this,getTriggeringGeofence(intent),notificationTitle);
        }
    }
    private String getTriggeringGeofence(Intent intent)
    {
        GeofencingEvent geofenceEvent=GeofencingEvent.fromIntent(intent);
        List<Geofence> geofence=geofenceEvent.getTriggeringGeofences();

        String[] geofenceId=new String[geofence.size()];
        for (int i = 0; i < geofence.size(); i++) {
            geofenceId[i] = geofence.get(i).getRequestId();
        }

        return TextUtils.join(", ", geofenceId);
    }

    private void sendNotification(Context context,String notificationText,String notificationTitle)
    {
        PowerManager pm=(PowerManager) context.getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock wakelock=pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,"");
        wakelock.acquire();

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(
                context).setSmallIcon(R.drawable.cast_ic_notification_small_icon)
                .setContentTitle(notificationTitle)
                .setContentText(notificationText)
                .setDefaults(Notification.DEFAULT_ALL).setAutoCancel(false);

        NotificationManager notificationManager = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0, notificationBuilder.build());

        wakelock.release();

    }
}
