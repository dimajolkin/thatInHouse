package com.example.dimaj.geo.map.types;


import android.location.Location;

public class PointMap {
    //широта
    private float latitude;
    //долгота
    private float longitude;

    public PointMap(float latitude, float longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public PointMap(Location location) {
        if (location != null) {
            this.longitude = (float) location.getLongitude();
            this.latitude = (float) location.getLatitude();
        }
    }

    public float getLatitude() {
        return latitude;
    }

    public float getLongitude() {
        return longitude;
    }


    public String toString() {
        return latitude + "," + longitude;
    }
}
