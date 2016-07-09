package com.example.dimaj.geo.json;

import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

public class StringRunabled implements Runnable {
    private String result;
    private String url;

    public StringRunabled(String URL) {
        this.url = URL;
    }
    @Override
    public void run() {
        InputStream in = null;
        try {
            in = new java.net.URL(url).openStream();
            result = new Scanner(in).useDelimiter("\\A").next();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public String getResult() {
        return result;
    }

    public static String loadString(String URL) {
        StringRunabled run = new StringRunabled(URL);
        Thread thread = new Thread(run);
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return run.getResult();
    }
}
