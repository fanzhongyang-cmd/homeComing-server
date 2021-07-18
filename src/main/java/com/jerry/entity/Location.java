package com.jerry.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

public class Location {
    private int task_id;
    private int user_id;
    private double longitude;
    private double latitude;
    private long collect_time;

    public void setTask_id(int task_id) {
        this.task_id = task_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setCollect_time(long collect_time) {
        this.collect_time = collect_time;
    }

    public int getTask_id() {
        return task_id;
    }

    public int getUser_id() {
        return user_id;
    }

    public static double getRAD() {
        return RAD;
    }

    public static double getEarthRadius() {
        return EARTH_RADIUS;
    }

    private static double RAD = Math.PI / 180.0;
    private static double EARTH_RADIUS = 6378137;

    public double getLongitude() {
        return longitude;
    }

    public double getLatitude() {
        return latitude;
    }


    public long getCollect_time() {
        return collect_time;
    }

    public double getDistance(Location location) {
        double radLat1 = this.getLatitude() * RAD;
        double radLat2 = location.getLatitude() * RAD;
        double a = radLat1 - radLat2;
        double b = (this.getLongitude() - location.getLongitude()) * RAD;
        double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2) +
                Math.cos(radLat1) * Math.cos(radLat2) * Math.pow(Math.sin(b / 2), 2)));
        s = s * EARTH_RADIUS;
        s = (double) Math.round(s * 10000) / 10000;
        return s;

    }

    public double getDistance(double longitude, double latitude) {
        double radLat1 = this.getLatitude() * RAD;
        double radLat2 = latitude * RAD;
        double a = radLat1 - radLat2;
        double b = (this.getLongitude() - longitude) * RAD;
        double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2) +
                Math.cos(radLat1) * Math.cos(radLat2) * Math.pow(Math.sin(b / 2), 2)));
        s = s * EARTH_RADIUS;
        s = (double) Math.round(s * 10000) / 10000;
        return s;

    }

    public Location(double longitude, double latitude) {
        this.longitude = longitude;
        this.latitude = latitude;
    }

    @Override
    public String toString() {
        return "Location{" +
                "task_id=" + task_id +
                ", user_id=" + user_id +
                ", longitude=" + longitude +
                ", latitude=" + latitude +
                ", collect_time=" + collect_time +
                '}';
    }
}
