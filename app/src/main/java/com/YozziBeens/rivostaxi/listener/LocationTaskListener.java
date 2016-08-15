package com.YozziBeens.rivostaxi.listener;

import android.location.Location;


public interface LocationTaskListener {
    public void onTaskStart();
    public void onTaskComplete(Location result);
    public void onTaskCancelled();
}
