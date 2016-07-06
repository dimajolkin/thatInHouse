package com.example.dimaj.geo.location;

import android.content.Context;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;


public class Location  implements LocationListener {
    private static final String TAG="location";

    private android.location.Location current;
    private LocationManager lm;

    public void init(LocationManager lm) {
        this.lm = lm;
        try {
            this.lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
            this.lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);
            current = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        } catch (SecurityException ex) {
            Log.d(TAG, ex.getMessage());
        }
        Log.d(TAG, "init");
    }

    @Override
    public void onLocationChanged(android.location.Location location) {
        if (location != null)
        {
            current = location;
//            Log.d(TAG, "Широта=" + location.getLatitude());
//            Log.d(TAG, "Долгота="+location.getLongitude());
        }
//        Log.d(TAG, "Локация");
    }

    public android.location.Location get() {
        return current;
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}
