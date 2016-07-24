package com.example.dimaj.geo;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.hardware.SensorManager;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.example.dimaj.geo.compas.Compass;
import com.example.dimaj.geo.geo.Location;
import com.example.dimaj.geo.houses.HouseFinder;
import com.example.dimaj.geo.houses.HousesListView;
import com.example.dimaj.geo.map.Map;
import com.example.dimaj.geo.map.types.PointMap;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getName();
    private ImageView map;
    private ImageView arrow;

    private Compass compass;
    private Location location;

    private HousesListView housesListView;

//    private LocationSensor myLocation;

    private static Map yandexMap = new Map(400, 450);

    public void updateMap() {

        if (location.getPoint() == null) {
            return;
        }

        Bitmap bitmap = yandexMap.loadMapBitmap(location.getPoint());

        HouseFinder finder = new HouseFinder(bitmap);
        finder.setAngle((int) compass.getAngle());

        ArrayList<Point> points = finder.getHousesPoints();

        for (Point p : points) {
            PointMap globalPoint = yandexMap.geGlobal(location.getPoint(), p);
            String tmpAddress = yandexMap.getAddress(globalPoint);
            housesListView.add(tmpAddress);

            Log.d("Location: ", tmpAddress);
            Log.d("pixel  Location: ", p.toString());
            Log.d("global Location: ", globalPoint.toString());
        }

        this.map.setImageBitmap(getCroppedBitmap(finder.getBitmap()));
    }

    public Bitmap getCroppedBitmap(Bitmap bitmap) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
                bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        // canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
        canvas.drawCircle(bitmap.getWidth() / 2, bitmap.getHeight() / 2,
                bitmap.getWidth() / 2, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        //Bitmap _bmp = Bitmap.createScaledBitmap(output, 60, 60, false);
        //return _bmp;
        return output;
    }


    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        ActivityCompat.requestPermissions(this, new String[]{
                Manifest.permission.ACCESS_FINE_LOCATION
        }, 1);


        housesListView = new HousesListView(this, (ListView) findViewById(R.id.listView));

        location = new Location(this);
        location.onLocationChanged(new Runnable() {
            @Override
            public void run() {
                //получил гео данные
                updateMap();

            }
        });


        map = (ImageView) findViewById(R.id.map);
        arrow = (ImageView) findViewById(R.id.arrow);

        compass = new Compass((SensorManager) getSystemService(SENSOR_SERVICE));
        compass.setImage(arrow);

        LinearLayout layout = (LinearLayout) findViewById(R.id.layout);
        layout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //смотрим что координаты получены
                if (location.getPoint() == null) {
                    return;
                }
                housesListView.clear();
                updateMap();
            }
        });


    }

    protected void onResume() {
        super.onResume();
        compass.onResume();
        location.onResume();
    }

    protected void onPause() {
        super.onPause();
        compass.onPause();
        location.onPause();
    }

}

