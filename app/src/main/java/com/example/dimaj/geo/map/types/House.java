package com.example.dimaj.geo.map.types;

public class House {
    protected PointMap point;
    private String address;

    public House(PointMap point, String address) {
        this.point = point;
        this.address = address;
    }

    public PointMap getPoint() {
        return point;
    }

    public String getAddress() {
        return address;
    }
}
