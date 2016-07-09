package com.example.dimaj.geo.geo;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;

import com.example.dimaj.geo.map.types.PointMap;

public class LocationSensor {

    private MySensorEventListener manager;
    private MyLocationListener locationListener;

    private PointMap pointMap;

    public LocationSensor(SensorManager manager, LocationManager locationManager) {
        this.manager = new MySensorEventListener(manager);
        this.locationListener =  new MyLocationListener(locationManager);
    }

    public int getAngle() {
        return this.manager.getAngle();
    }

    public PointMap getMyPointMap() {
        return new PointMap(this.locationListener.getLocation());
    }

    public RotateAnimation getRotateAnimation() {
        return this.manager.getRotateAnimation();
    }

    public void onSensor(Runnable onSensor) {
        this.manager.setOnSensor(onSensor);
    }

    public void onResume() {
       this.manager.onResume();
    }

    public void onPause() {
        this.manager.onPause();
    }


}

class MyLocationListener implements LocationListener {
    private static final String TAG="location";

    private android.location.Location current;
    private LocationManager lm;

    public MyLocationListener(LocationManager lm) {
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
    public void onLocationChanged(Location location) {
        if (location != null) {
            current = location;
        }
    }

    public android.location.Location getLocation() {

        return current;
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }
}

class MySensorEventListener implements SensorEventListener {
    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private Sensor mMagnetometer;
    private Runnable onSensor;


    public MySensorEventListener(SensorManager manager) {
        mSensorManager = manager;
        mAccelerometer = manager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mMagnetometer = manager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
    }

    public void setOnSensor(Runnable onSensor) {
        this.onSensor = onSensor;
    }

    private float[] mLastAccelerometer = new float[3];
    private float[] mLastMagnetometer = new float[3];
    private boolean mLastAccelerometerSet = false;
    private boolean mLastMagnetometerSet = false;

    private float[] mR = new float[9];
    private float[] mOrientation = new float[3];
    private float mCurrentDegree = 0f;
    private int dx = 5;
    private int currentAngle = 0;

    private RotateAnimation ra;

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor == mAccelerometer) {
            System.arraycopy(event.values, 0, mLastAccelerometer, 0, event.values.length);
            mLastAccelerometerSet = true;
        } else if (event.sensor == mMagnetometer) {
            System.arraycopy(event.values, 0, mLastMagnetometer, 0, event.values.length);
            mLastMagnetometerSet = true;
        }
        if (mLastAccelerometerSet && mLastMagnetometerSet) {
            SensorManager.getRotationMatrix(mR, null, mLastAccelerometer, mLastMagnetometer);
            SensorManager.getOrientation(mR, mOrientation);
            float azimuthInRadians = mOrientation[0];
            int azimuthInDegress = (int) Math.round((Math.toDegrees(azimuthInRadians) + 360) % 360);
            if (currentAngle == 0) {
                currentAngle = azimuthInDegress;
            }

            if (azimuthInDegress - dx > currentAngle || azimuthInDegress + dx < currentAngle) {
                currentAngle = azimuthInDegress;

                ra = new RotateAnimation(
                        mCurrentDegree,
                        -azimuthInDegress,
                        Animation.RELATIVE_TO_SELF, 0.5f,
                        Animation.RELATIVE_TO_SELF,
                        0.5f);

                if (onSensor != null) {
                    onSensor.run();
                }

                ra.setDuration(1000);
                ra.setFillAfter(true);

            }

            mCurrentDegree = -azimuthInDegress;
        }
    }

    public int getAngle() {
        return this.currentAngle;
    }


    public void onResume() {
        mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_GAME);
        mSensorManager.registerListener(this, mMagnetometer, SensorManager.SENSOR_DELAY_GAME);
    }

    public void onPause() {
        mSensorManager.unregisterListener(this, mAccelerometer);
        mSensorManager.unregisterListener(this, mMagnetometer);
    }

    public RotateAnimation getRotateAnimation() {
        return ra;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}
