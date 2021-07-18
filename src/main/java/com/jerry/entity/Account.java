package com.jerry.entity;

public class Account {
    public static String DefaultPassword = "123456";
    private int user_id;
    private String password;
    private String role = "user";

    public int getUser_id() {
        return user_id;
    }

    public String getPassword() {
        return password;
    }

    public Account(int user_id, String password) {
        this.user_id = user_id;
        this.password = password;
    }
//    public String getRole() {
//        return role;
//    }
}
