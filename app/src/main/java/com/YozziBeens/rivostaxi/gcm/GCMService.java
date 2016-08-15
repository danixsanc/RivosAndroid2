package com.YozziBeens.rivostaxi.gcm;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;

import com.YozziBeens.rivostaxi.app.Main;

import com.YozziBeens.rivostaxi.R;

/**
 * Created by LuisAngel on 28/02/2016.
 */
public class GCMService extends IntentService
{
    private NotificationManager mNotificationManager;
    private int NOTIFICATION_ID = 1234;
    private static final String TAG = "Listener-Service";

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public GCMService(String name)
    {
        super("GCMService");
    }

    @Override
    protected void onHandleIntent(Intent intent)
    {
        Bundle extras = intent.getExtras();
        sendNotification("Recibida: " + extras.toString());
        GcmBroadcastReceiver.completeWakefulIntent(intent);
        mostrarNotification("");
    }

    private void sendNotification(String msg)
    {
        mNotificationManager = (NotificationManager)this.getSystemService(Context.NOTIFICATION_SERVICE);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,new Intent(this, Main.class), 0);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("Developer")
                .setStyle(new NotificationCompat.BigTextStyle()
                .bigText(msg))
                .setContentText(msg);
        mBuilder.setContentIntent(contentIntent);
        mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
    }

    private void mostrarNotification(String msg)
    {
        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(android.R.drawable.stat_sys_warning)
                .setContentTitle("Rivos GCM")
                .setContentText(msg);
        Intent notIntent =  new Intent(this, Main.class);
        PendingIntent contIntent = PendingIntent.getActivity(this, 0, notIntent, 0);
        mBuilder.setContentIntent(contIntent);
        mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
    }
}
