package com.jerry.config;

import java.text.SimpleDateFormat;

public class Configuration {
    public static int port = 8088;
    public static String BASEDIR="D:/home/";
    public static String faceRepository = BASEDIR+"face/faceRepository/";
    public static String faceTaken = BASEDIR+"face/tempFace/";
    public static String iconRepository = BASEDIR+"face/icon/";
    public static String messageFileRepository=BASEDIR+"message/";
    //    public static SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//    public static String SuccessLoginCode="20000";
//    public static String FailLoginCode="20002";
    public static int globalSuccessCode = 200;
    public static int globalFailureCode = 500;
    public static int globalNullExceptionCode = 100;
}
