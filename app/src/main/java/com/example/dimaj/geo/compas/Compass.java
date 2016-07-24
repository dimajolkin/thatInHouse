package com.example.dimaj.geo.compas;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

public class Compass {
    private final SensorManager sensorManager;
    private float currentDegree = 0f;
    private ImageView image;
    private TextView text;

    private Runnable onSensorChanged;

    private float angle;

    public Compass(SensorManager sensorManager)
    {
        this.sensorManager = sensorManager;
    }

    public void setImage(ImageView img)
    {
        image = img;
    }

    public float getAngle()
    {
        return angle;
    }

    public void setText(TextView text)
    {
        this.text = text;
    }

    public void onSensorChanged(Runnable run)
    {
        onSensorChanged = run;
    }

    private SensorEventListener getListener()
    {
        return  new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {

                // get the angle around the z-axis rotated
                float degree = Math.round(event.values[0]);
                angle = degree;
                if (onSensorChanged != null) {
                    onSensorChanged.run();
                }

                if (text != null) {
                    text.setText("Heading: " + Float.toString(degree) + " degrees");
                }

                // create a rotation animation (reverse turn degree degrees)
                RotateAnimation ra = new RotateAnimation(
                        currentDegree,
                        -degree,
                        Animation.RELATIVE_TO_SELF, 0.5f,
                        Animation.RELATIVE_TO_SELF,
                        0.5f);

                // how long the animation will take place
                ra.setDuration(210);

                // set the animation after the end of the reservation status
                ra.setFillAfter(true);

                // Start the animation
                if (image != null) {
                    image.startAnimation(ra);
                }

                currentDegree = -degree;

            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int i) {

            }
        };
    }

    public void onResume()
    {
        sensorManager.registerListener(
                getListener(),
                sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION),
                SensorManager.SENSOR_DELAY_GAME
        );
    }

    public void onPause()
    {
        sensorManager.unregisterListener(getListener());
    }
}
