package com.example.dimaj.geo.houses;


import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Point;
import android.util.Log;

import java.util.ArrayList;

public class HouseFinder {

    protected int size = 0;
    protected int latitude = 0;
    protected int longitude = 0;
    protected int maxlook = 230;
    protected int angle = 0;

//    protected final int HOUSE_COLOR = 10653984;
    protected final int HOUSE_COLOR = -1252154;

    protected MapBitmap map;

    public HouseFinder(Bitmap map) {
        this.map = new MapBitmap(map);
    }


    public void setLocation(int latitude, int longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }


    public void setSize(int size) {
        this.size = size;
    }

    public void setAngle(int angle) {
        this.angle = angle;
    }

    public void setMaxLook(int maxLook) {
        this.maxlook = maxLook;
    }

    public Bitmap getBitmap() {
        return map.getBitmap();
    }

    public ArrayList<Point> getHousesPoints() {
        ArrayList<Point> points = new ArrayList<>();

        Point center = map.getPointCenter();

        Log.d("LOG", center.toString());
        Log.d("LOG", String.valueOf(map.getRGB( new Point(2, 2))));

        int count = 0;
        for (int r = 0; r < 230; r++) {
            Point current = new Point(
                    (int) Math.round(center.x + r * Math.sin(angle * (Math.PI / 180))),
                    (int) Math.round(center.y + r * Math.cos(angle * (Math.PI / 180)))
            );

            if (map.getRGB(current) == HOUSE_COLOR) {
                map.flip(current, HOUSE_COLOR, count++);
                points.add(current);
            }

            map.drawPixel(current, Color.BLUE);
        }


        return points;
    }

}


