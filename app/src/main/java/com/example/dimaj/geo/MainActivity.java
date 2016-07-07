package com.example.dimaj.geo;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.Rect;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.dimaj.geo.location.Location;

import java.io.InputStream;


public class MainActivity extends AppCompatActivity implements SensorEventListener  {

    private static final String TAG = MainActivity.class.getName();
    private Location loc;
    private TextView text;
    private ImageView map;

    private ImageView mPointer;

    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private Sensor mMagnetometer;
    private float[] mLastAccelerometer = new float[3];
    private float[] mLastMagnetometer = new float[3];
    private boolean mLastAccelerometerSet = false;
    private boolean mLastMagnetometerSet = false;
    private float[] mR = new float[9];
    private float[] mOrientation = new float[3];
    private float mCurrentDegree = 0f;
    private int dx = 5;
    private int currentAngle = 0;

    public void updateMap() {
        android.location.Location location = loc.get();
        if (location != null) {
            String textLocation = "Широта=" + location.getLatitude() + "; Долгота=" + location.getLongitude();
            Log.d("location", textLocation);
            text.setText(textLocation);
            String URL = "https://static-maps.yandex.ru/1.x/?ll=" + location.getLongitude() + "," + location.getLatitude()
                    + "&z=17&size=400,400&l=map";
            Log.d("location", URL);
            new DownloadImageTask(map).execute(URL);


        }
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        text = (TextView) findViewById(R.id.textView);
        map = (ImageView) findViewById(R.id.map);
        mPointer = (ImageView) findViewById(R.id.arrow);
        text.setText("start");

        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        loc = new Location();
        loc.init((LocationManager) getSystemService(Context.LOCATION_SERVICE));
        mPointer.setImageResource(R.mipmap.ic_arraw);
        mSensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mMagnetometer = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);

        LinearLayout layout = (LinearLayout) findViewById(R.id.layout);
        layout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
               updateMap();
            }
        });
    }

    @Override
    protected void onStart() {

        super.onStart();
    }

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
            int azimuthInDegress = (int) Math.round( (Math.toDegrees(azimuthInRadians)+360)%360 );
            if (currentAngle == 0) {
                currentAngle = azimuthInDegress;
            }

            if (azimuthInDegress - dx > currentAngle || azimuthInDegress + dx < currentAngle ) {
//                Log.d("location", Integer.toString(azimuthInDegress));

                currentAngle = azimuthInDegress;

                RotateAnimation ra = new RotateAnimation(
                        mCurrentDegree,
                        -azimuthInDegress,
                        Animation.RELATIVE_TO_SELF, 0.5f,
                        Animation.RELATIVE_TO_SELF,
                        0.5f);

                ra.setDuration(1000);


                ra.setFillAfter(true);
                mPointer.startAnimation(ra);
            }

            mCurrentDegree = -azimuthInDegress;
        }
    }

    public Bitmap getRoundedShape(Bitmap scaleBitmapImage) {
        int targetWidth = 400;
        int targetHeight = 400;
        Bitmap targetBitmap = Bitmap.createBitmap(targetWidth,
                targetHeight,Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(targetBitmap);
        Path path = new Path();
        path.addCircle(((float) targetWidth - 1) / 2,
                ((float) targetHeight - 1) / 2,
                (Math.min(((float) targetWidth),
                        ((float) targetHeight)) / 2),
                Path.Direction.CCW);

        canvas.clipPath(path);
        Bitmap sourceBitmap = scaleBitmapImage;
        canvas.drawBitmap(sourceBitmap,
                new Rect(0, 0, sourceBitmap.getWidth(),
                        sourceBitmap.getHeight()),
                new Rect(0, 0, targetWidth, targetHeight), null);
        return targetBitmap;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_GAME);
        mSensorManager.registerListener(this, mMagnetometer, SensorManager.SENSOR_DELAY_GAME);
    }

    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this, mAccelerometer);
        mSensorManager.unregisterListener(this, mMagnetometer);
    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            Log.d("location", "success load");
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap( getRoundedShape(result));
        }
    }
}

