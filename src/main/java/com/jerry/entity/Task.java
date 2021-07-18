package com.jerry.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Arrays;
import java.util.Date;


public class Task {

    public static String DefaultEmptyFace="empty";
    public static String RunningState="进行";
    public static String FinishState="完成";
    public static String CancelState="取消";
    private int task_id;
    private int user_id;//发布者
    private long publish_time = System.currentTimeMillis();

    private String task_state = RunningState;
    private String name;
    private String gender;
    private int age;
    private int height;
    private String description;
    private String id_card;
    private String lost_face=DefaultEmptyFace;
    private String lost_address="重庆";

    private long lost_time=0;

    private double lost_longitude=106;
    private double lost_latitude=29;
    private long complete_time = 0;
    private long cancel_time = 0;

    private int distance;
    private long connect_phone=0;

    public long getConnect_phone() {
        return connect_phone;
    }

    public void setConnect_phone(long connect_phone) {
        this.connect_phone = connect_phone;
    }

    public Task(int task_id, int user_id, long publish_time, String task_state, String name, String gender, int age, int height, String description, String id_card, String lost_address, long lost_time) {
        this.task_id = task_id;
        this.user_id = user_id;
        this.publish_time = publish_time;
        this.task_state = task_state;
        this.name = name;
        this.gender = gender;
        this.age = age;
        this.height = height;
        this.description = description;
        this.id_card = id_card;
        this.lost_address = lost_address;
        this.lost_time = lost_time;


    }

    public Task() {
    }

    public String getLost_face() {
        return lost_face;
    }

    public void setLost_face(String lost_face) {
        this.lost_face = lost_face;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public double getLost_longitude() {
        return lost_longitude;
    }

    public double getLost_latitude() {
        return lost_latitude;
    }

    public long getComplete_time() {
        return complete_time;
    }

    public long getCancel_time() {
        return cancel_time;
    }

    public int getTask_id() {
        return task_id;
    }

    public int getUser_id() {
        return user_id;
    }

    public long getPublish_time() {
        return publish_time;
    }

    public String getTask_state() {
        return task_state;
    }

    public String getName() {
        return name;
    }

    public String getGender() {
        return gender;
    }

    public int getAge() {
        return age;
    }

    public int getHeight() {
        return height;
    }

    public String getDescription() {
        return description;
    }

    public String getId_card() {
        return id_card;
    }

    public String getLost_address() {
        return lost_address;
    }

    public long getLost_time() {
        return lost_time;
    }


    public void setTask_state(String task_state) {
        this.task_state = task_state;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setId_card(String id_card) {
        this.id_card = id_card;
    }

    public void setLost_address(String lost_address) {
        this.lost_address = lost_address;
    }

    public void setLost_time(long lost_time) {
        this.lost_time = lost_time;
    }

    public void setTask_id(int task_id) {
        this.task_id = task_id;
    }

    public void setPublish_time(long publish_time) {
        this.publish_time = publish_time;
    }

    public void setLost_longitude(double lost_longitude) {
        this.lost_longitude = lost_longitude;
    }

    public void setLost_latitude(double lost_latitude) {
        this.lost_latitude = lost_latitude;
    }

    public void setComplete_time(long complete_time) {
        this.complete_time = complete_time;
    }

    public void setCancel_time(long cancel_time) {
        this.cancel_time = cancel_time;
    }

    @Override
    public String toString() {
        return "Task{" +
                "task_id=" + task_id +
                ", user_id=" + user_id +
                ", publish_time=" + publish_time +
                ", task_state='" + task_state + '\'' +
                ", name='" + name + '\'' +
                ", gender='" + gender + '\'' +
                ", age=" + age +
                ", height=" + height +
                ", description='" + description + '\'' +
                ", id_card='" + id_card + '\'' +
                ", lost_face='" + lost_face + '\'' +
                ", lost_address='" + lost_address + '\'' +
                ", lost_time=" + lost_time +
                ", lost_longitude=" + lost_longitude +
                ", lost_latitude=" + lost_latitude +
                ", complete_time=" + complete_time +
                ", cancel_time=" + cancel_time +
                ", distance=" + distance +
                ", connect_phone=" + connect_phone +
                '}';
    }
}
