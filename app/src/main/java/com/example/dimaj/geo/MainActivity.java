package com.example.dimaj.geo;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.Point;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dimaj.geo.geo.LocationSensor;
import com.example.dimaj.geo.houses.HouseFinder;
import com.example.dimaj.geo.map.Map;
import com.example.dimaj.geo.map.types.PointMap;

import java.util.ArrayList;

import ru.yandex.yandexmapkit.MapController;
import ru.yandex.yandexmapkit.MapView;
import ru.yandex.yandexmapkit.overlay.OverlayItem;
import ru.yandex.yandexmapkit.overlay.location.MyLocationItem;
import ru.yandex.yandexmapkit.overlay.location.OnMyLocationListener;
import ru.yandex.yandexmapkit.utils.GeoPoint;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getName();
    private TextView text;
    private MapView map;
    private MapController mMapController;
    private ImageView arrow;

    private LocationSensor myLocation;

    public void updateMap() {

//        Map map = new Map(400, 450);
//        PointMap point = myLocation.getMyPointMap();
//        Log.d("LOG", point.toString());
////        String address = map.getAddress(point);
////        text.setText(address);
//
//        Bitmap bitmap = map.loadMapBitmap(point);
//
//        HouseFinder finder = new HouseFinder(bitmap);
//        finder.setAngle(myLocation.getAngle() - 180);
//        ArrayList<Point> points = finder.getHousesPoints();
//
//        for (Point p: points ) {
////            String tmpAddress = map.getAddress(p);
//            Log.d("Location: ", p.toString());
//
////            Toast toast = Toast.makeText(getApplicationContext(), tmpAddress, Toast.LENGTH_SHORT);
////            toast.show();
//        }
//
//        this.map.setImageBitmap(finder.getBitmap());


    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        arrow = (ImageView) findViewById(R.id.arrow);

        ActivityCompat.requestPermissions(this, new String[]{
                Manifest.permission.ACCESS_FINE_LOCATION
        }, 1);

        map = (MapView) findViewById(R.id.map);
        mMapController = map.getMapController();

        GeoPoint geoPoint = mMapController.getMapCenter(); //получает географические координаты центра карты
        OverlayItem overlayItem = new OverlayItem(geoPoint, null); // необходимо для получения недоступного экземпляра класса cp,  представляет внутренние координаты центра карты библиотеки
        mMapController.getMapRotator().a(overlayItem.getPoint()); //задаем точку относительно которой производится вращение

        mMapController.setEnabled(false);
        mMapController.setZoomCurrent(17);
        mMapController.getOverlayManager().getMyLocation().addMyLocationListener(new OnMyLocationListener() {
            @Override
            public void onMyLocationChange(MyLocationItem myLocationItem) {
                myLocation = new LocationSensor(
                        (SensorManager) getSystemService(Context.SENSOR_SERVICE),
                        (LocationManager) getSystemService(Context.LOCATION_SERVICE)
                );
                myLocation.onResume();

            }
        });


        RelativeLayout layout = (RelativeLayout) findViewById(R.id.layout);
        layout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
//                updateMap();
//                PointMap location = myLocation.getMyPointMap();
//                mMapController.setPositionAnimationTo(new GeoPoint(location.getLatitude(), location.getLongitude()));


            }
        });

        myLocation.onSensor(new Runnable() {
            int old = -1;
            @Override
            public synchronized void run() {
                int f = myLocation.getAngle();
                if (old != f) {
                    old = f;
                    arrow.setAnimation(myLocation.getRotateAnimation());
                }
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

