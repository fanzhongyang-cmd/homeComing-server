package com.jerry.entity;

import com.jerry.mapper.MessageMapper;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

public class Message {
//    @Autowired
//    public MessageMapper messageMapper;

    public static int Pattern_Group=1;
    public static int Pattern_private=0;

    public static int Type_Text=0;
    public static int Type_Image=1;
    public static int Type_Voice=2;

    private int message_id;
    private int pattern;
    private int from_id;
    private int to_id;
    private long time;
    private int type=0;
    private String message;

    public int getMessage_id() {
        return message_id;
    }

    public int getPattern() {
        return pattern;
    }

    public int getFrom_id() {
        return from_id;
    }

    public int getTo_id() {
        return to_id;
    }

    public long getTime() {
        return time;
    }

    public int getType() {
        return type;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage_id(int message_id) {
        this.message_id = message_id;
    }

    public void setPattern(int pattern) {
        this.pattern = pattern;
    }

    public void setFrom_id(int from_id) {
        this.from_id = from_id;
    }

    public void setTo_id(int to_id) {
        this.to_id = to_id;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public void setType(int type) {
        this.type = type;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Message(String json) {
        if (json != null && json.length() > 0) {
            JSONObject jsonObject = new JSONObject(json);
//            this.message_id=Integer.parseInt(jsonObject.optString("message_id"));
            this.from_id = Integer.parseInt(jsonObject.optString("from_id"));
            this.to_id = Integer.parseInt(jsonObject.optString("to_id"));
            this.pattern = Integer.parseInt(jsonObject.optString("pattern"));
            this.type = Integer.parseInt(jsonObject.optString("type"));
            this.time=System.currentTimeMillis();
            this.message=jsonObject.optString("message");
        }
    }

    public Message(int pattern, int from_id, int to_id, long time, int type, String message) {

        this.pattern = pattern;
        this.from_id = from_id;
        this.to_id = to_id;
        this.time = time;
        this.type = type;
        this.message = message;
    }

    public Message(int message_id, int pattern, int from_id, int to_id, long time, int type, String message) {
        this.message_id = message_id;
        this.pattern = pattern;
        this.from_id = from_id;
        this.to_id = to_id;
        this.time = time;
        this.type = type;
        this.message = message;
    }

    @Override
    public String toString() {
        return "Message{" +
                "message_id=" + message_id +
                ", pattern=" + pattern +
                ", from_id=" + from_id +
                ", to_id=" + to_id +
                ", time=" + time +
                ", type=" + type +
                ", message='" + message + '\'' +
                '}';
    }
}
