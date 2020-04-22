package com.example.weather.Model;

public class Main {
    private double temp;
    private double pressure;
    private int humidity;
    private double temp_min;
    private double temp_max;

    public Main(int temp, int pressure, int humidity, int temp_min, int temp_max) {
        this.temp = temp;
        this.pressure = pressure;
        this.humidity = humidity;
        this.temp_min = temp_min;
        this.temp_max = temp_max;
    }

    public double getTemp() {
        return temp;
    }

    public double getPressure() {
        return pressure;
    }

    public int getHumidity() {
        return humidity;
    }

    public double getTemp_min() {
        return temp_min;
    }

    public double getTemp_max() {
        return temp_max;
    }
}
