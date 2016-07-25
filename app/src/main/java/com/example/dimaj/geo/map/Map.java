package com.example.dimaj.geo.map;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.location.Location;
import android.nfc.Tag;
import android.util.Log;

import com.example.dimaj.geo.images.LoadBitmapRunnable;
import com.example.dimaj.geo.json.StringRunabled;
import com.example.dimaj.geo.map.types.PointMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Scanner;

public class Map implements MapInterface {

    protected int width = 400;
    protected int height = 400;

    private ArrayList<PointMap> points;

    public Map(int width, int height) {
        this.width = width;
        this.height = height;
        points = new ArrayList<>();
    }

    public Map() {

    }

    public void addLabel(PointMap point) {
        points.add(point);
    }
    public void clearLabel() {
        points.clear();
    }

    public PointMap geGlobal(PointMap center, Point pixel) {
        float[] d = new float[]{
                0.0000057359385294830645f,
                0.000010728836073781167f
        };

        float p[] = new float[]{
                center.getLatitude() + width / 2 * d[0],
                center.getLongitude() - height / 2 * d[1]
        };

        return new PointMap(
                (p[0] - d[0] * (pixel.y - 15)),
                (p[1] + d[1] * (pixel.x + 15))
        );
    }



    @Override
    public Bitmap loadMapBitmap(PointMap pointMap) {
        String URL = "https://static-maps.yandex.ru/1.x/?ll=" + pointMap.getLongitude() + "," + pointMap.getLatitude()
                + "&z=17&size=" + width + "," + height + "&l=map";

        for (PointMap p: points) {
            URL = URL + "&pt="+p.getLongitude()+","+p.getLatitude()+",pmwtm1";
        }

        Log.d("location", URL);

        LoadBitmapRunnable load = new LoadBitmapRunnable(URL);
        Thread thread = new Thread(load);
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return load.getBitmap();
    }

    public String getAddress(Point point) {
        return getAddress(new PointMap(point.x, point.y));
    }

    public String getAddress(PointMap point) {

        final String URL = "https://geocode-maps.yandex.ru/1.x/?geocode="
                + point.getLongitude()
                + ","
                + point.getLatitude()
                + "&results=1&format=json";

        String json = StringRunabled.loadString(URL);

        try {
            JSONArray obj = new JSONObject(json)
                    .getJSONObject("response")
                    .getJSONObject("GeoObjectCollection")
                    .getJSONArray("featureMember");

            JSONObject geoObj = new JSONObject(obj.get(0).toString())
                    .getJSONObject("GeoObject")
                    .getJSONObject("metaDataProperty")
                    .getJSONObject("GeocoderMetaData");

            return geoObj.getString("text");
        } catch (JSONException e) {
            Log.d("Error YandexGeo: ", point.toString() );
            e.printStackTrace();
        }

        return "null";
//        try {
//            InputStream in = new java.net.URL(URL).openStream();
//            String json = new Scanner(in).useDelimiter("\\A").next();

//            return json;


//            return obj.getString("response").toString();

//        } catch (IOException e) {
//            e.printStackTrace();
//        }

//        return "Адресс";
    }


}
