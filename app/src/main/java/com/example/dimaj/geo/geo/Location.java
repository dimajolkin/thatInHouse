package com.example.dimaj.geo.geo;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;

import com.example.dimaj.geo.map.types.PointMap;

import java.security.Permission;

public class Location {
    private LocationManager locationManager;
    private PointMap point;
    private Runnable onLocationChanged;

    public Location(Activity activity) {
        locationManager = (LocationManager) activity.getSystemService(Activity.LOCATION_SERVICE);
    }

    public void onResume() throws SecurityException {

        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                0, 10, locationListener);

        locationManager.requestLocationUpdates(
                LocationManager.NETWORK_PROVIDER,0, 10,
                locationListener);

//        checkEnabled();
    }

    public void onLocationChanged(Runnable runnable)
    {
        this.onLocationChanged = runnable;
    }

    public void onPause()  throws SecurityException {
        locationManager.removeUpdates(locationListener);
    }

    public PointMap getPoint()
    {
        return point;
    }

    private LocationListener locationListener = new LocationListener() {

        @Override
        public void onProviderDisabled(String provider) {
        }

        @Override
        public void onProviderEnabled(String provider) {
//            checkEnabled();
//            showLocation(locationManager.getLastKnownLocation(provider));
        }

        @Override
        public void onLocationChanged(android.location.Location location) {
            point = new PointMap(location);

            if (onLocationChanged != null) {
                onLocationChanged.run();
            }

        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
//            if (provider.equals(LocationManager.GPS_PROVIDER)) {
//                tvStatusGPS.setText("Status: " + String.valueOf(status));
//            } else if (provider.equals(LocationManager.NETWORK_PROVIDER)) {
//                tvStatusNet.setText("Status: " + String.valueOf(status));
//            }
        }
    };
}
