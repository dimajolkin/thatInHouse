package com.example.dimaj.geo.map;

import android.graphics.Bitmap;
import android.location.Location;

import com.example.dimaj.geo.map.types.PointMap;

public interface MapInterface {

    public Bitmap loadMapBitmap(PointMap pointMap);
}
