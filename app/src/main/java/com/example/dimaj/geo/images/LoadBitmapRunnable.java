package com.example.dimaj.geo.images;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.InputStream;


public class LoadBitmapRunnable implements Runnable {

    private Bitmap bitmap;
    private String URL;

    public LoadBitmapRunnable(String URL) {
        this.URL = URL;
    }

    @Override
    public void run() {
        try {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inMutable = true;

            InputStream in = new java.net.URL(URL).openStream();
            bitmap = BitmapFactory.decodeStream(in, null,  options);
        } catch (Exception e) {
//            Log.e("Error", e.getMessage());
            e.printStackTrace();
        }

//        Log.d("loadImg", "success load");
    }

    public Bitmap getBitmap() {
        return bitmap;
    }
}
