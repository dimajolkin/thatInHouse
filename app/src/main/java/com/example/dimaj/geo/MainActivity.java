package com.example.dimaj.geo;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.hardware.SensorManager;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.dimaj.geo.geo.LocationSensor;
import com.example.dimaj.geo.map.Map;
import com.example.dimaj.geo.map.types.PointMap;


public class MainActivity extends AppCompatActivity  {

    private static final String TAG = MainActivity.class.getName();
    private TextView text;
    private ImageView map;
    private ImageView mPointer;

    private LocationSensor myLocation;

    public void updateMap() {
        Map map = new Map();
        String address = map.getAddress(myLocation.getMyPointMap());
        text.setText(address);
        this.map.setImageBitmap(map.loadMapBitmap(myLocation.getMyPointMap()));
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        text = (TextView) findViewById(R.id.textView);
        map = (ImageView) findViewById(R.id.map);

        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);


        if (myLocation == null) {
            mPointer = (ImageView) findViewById(R.id.arrow);
            mPointer.setImageResource(R.mipmap.ic_arraw);

            myLocation = new LocationSensor(
                    (SensorManager) getSystemService(Context.SENSOR_SERVICE),
                    (LocationManager)getSystemService(Context.LOCATION_SERVICE)
            );

            myLocation.onSensor(new Runnable() {
                @Override
                public void run() {
                    mPointer.setAnimation(myLocation.getRotateAnimation());
                    Log.d(TAG, String.valueOf(myLocation.getAngle()));
                }
            });
        }


        LinearLayout layout = (LinearLayout) findViewById(R.id.layout);
        layout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                updateMap();
            }
        });
    }

    protected void onResume() {
        super.onResume();
        if (myLocation != null) {
            myLocation.onResume();
        }

    }

    protected void onPause() {
        super.onPause();
        if (myLocation != null) {
            myLocation.onPause();
        }
    }

}

