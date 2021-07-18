package com.jerry.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.socket.WebSocketSession;

import java.util.Date;
import java.util.List;

public class User {
    public static int ONLINE = 1;
    public static int OFFLINE = 0;

    private int user_id;
    private String user_name = "游客";
    private String user_gender = "男";


    private int user_age = 20;

    private long user_telephone;
    private String user_signature = "这个人什么都没留下";
    private String user_face = "/static/image/face.jpg";
    private int user_state = OFFLINE;
    private double user_latitude;
    private double user_longitude;

    private String user_region="重庆";
    private WebSocketSession session;
    public User(int user_id) {
        this.user_id = user_id;
    }

    public User(int user_id, long telephone) {
        this.user_telephone = telephone;
        this.user_id = user_id;
    }

    public User(int user_id, String user_name, String user_gender, int user_age, long user_telephone, String user_signature, int user_state) {
        this.user_id = user_id;
        this.user_name = user_name;
        this.user_gender = user_gender;
        this.user_age = user_age;
        this.user_telephone = user_telephone;
        this.user_signature = user_signature;
        this.user_state = user_state;
    }

    public User() {

    }

    public String getUser_region() {
        return user_region;
    }

    public void setUser_region(String user_region) {
        this.user_region = user_region;
    }

    public WebSocketSession getSession() {
        return session;
    }

    public void setSession(WebSocketSession session) {
        this.session = session;
    }

    public String getUser_face() {
        return user_face;
    }

    public double getUser_latitude() {
        return user_latitude;
    }

    public double getUser_longitude() {
        return user_longitude;
    }

    public int getUser_id() {
        return user_id;
    }

    public String getUser_name() {
        return user_name;
    }

    public String getUser_gender() {
        return user_gender;
    }

    public int getUser_age() {
        return user_age;
    }

    public long getUser_telephone() {
        return user_telephone;
    }

    public String getUser_signature() {
        return user_signature;
    }

    public int getUser_state() {
        return user_state;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public void setUser_gender(String user_gender) {
        this.user_gender = user_gender;
    }

    public void setUser_age(int user_age) {
        this.user_age = user_age;
    }

    public void setUser_telephone(long user_telephone) {
        this.user_telephone = user_telephone;
    }

    public void setUser_signature(String user_signature) {
        this.user_signature = user_signature;
    }

    public void setUser_state(int user_state) {
        this.user_state = user_state;
    }

    public void setUser_latitude(double user_latitude) {
        this.user_latitude = user_latitude;
    }

    public void setUser_longitude(double user_longitude) {
        this.user_longitude = user_longitude;
    }

    public void setUser_face(String user_face) {
        this.user_face = user_face;
    }

    @Override
    public String toString() {
        return "User{" +
                "user_id=" + user_id +
                ", user_name='" + user_name + '\'' +
                ", user_gender='" + user_gender + '\'' +
                ", user_age=" + user_age +
                ", user_telephone=" + user_telephone +
                ", user_signature='" + user_signature + '\'' +
                ", user_state=" + user_state +
                '}';
    }
}
