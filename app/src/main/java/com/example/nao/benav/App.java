package com.example.nao.benav;

import android.app.Application;

import org.altbeacon.beacon.BeaconManager;

/**
 * Created by nao on 8/13/15.
 */
public class App extends Application {
    public void onCreate() {
        super.onCreate();
        BeaconManager beaconManager = org.altbeacon.beacon.BeaconManager.getInstanceForApplication(this);
    }
}
