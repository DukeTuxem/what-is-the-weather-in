package com.example.weather.Model;

public class Coord {

    private double lat;
    private double lon;

    public Coord(int lat, int lon) {
        this.lat = lat;
        this.lon = lon;
    }

    public double getLat() {
        return lat;
    }

    public double getLon() {
        return lon;
    }

}
