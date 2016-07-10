package com.example.dimaj.geo.map;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.location.Location;
import android.util.Log;

import com.example.dimaj.geo.images.LoadBitmapRunnable;
import com.example.dimaj.geo.json.StringRunabled;
import com.example.dimaj.geo.map.types.PointMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

public class Map implements MapInterface {

    protected int width = 400;
    protected int height = 400;

    public Map(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public Map() {

    }

    @Override
    public Bitmap loadMapBitmap(PointMap pointMap) {
        final String URL = "https://static-maps.yandex.ru/1.x/?ll=" + pointMap.getLongitude() + "," + pointMap.getLatitude()
                + "&z=17&size=" + width + "," + height + "&l=map";
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
