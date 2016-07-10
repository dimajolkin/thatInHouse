package com.example.dimaj.geo;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.JsonReader;

import com.example.dimaj.geo.map.Map;
import com.example.dimaj.geo.map.types.PointMap;

import junit.framework.Assert;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;

import java.net.URL;
import java.util.Scanner;


public class MapTest {


    @Test
    public void testGetAddress() throws Exception {
        String address = new Map().getAddress(new PointMap(57.767223f, 40.928392f));
        Assert.assertEquals(address, "Россия, Кострома, Советская улица, 1");
    }

    @Test
    public void testMoveIn() {



    }


}