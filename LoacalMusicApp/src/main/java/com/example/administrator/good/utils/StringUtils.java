package com.example.administrator.good.utils;

import java.text.SimpleDateFormat;

/**
 * Created by Administrator on 2016/9/23.
 */
public class StringUtils {
    public static String reFormatDate(long time){
        return new SimpleDateFormat("mm:ss").format(time);
    }

    public static String reFormatTime(long time){
        return new SimpleDateFormat("mm:ss").format(time);
    }
    public static String duration(int duration){
        String result = "";
        int i = duration/1000;
        int min = i/60;
        int sec = i%60;
        if(min>9) {
            if (sec>9) {
                result=min+ ":" +sec;
            }else{
                result = min +":0"+sec;
            }
        }else {
            if (sec>9) {
                result="0"+min+ ":" +sec;
            }else{
                result="0"+min+ ":0" +sec;
            }
        }
        return result;
    }

    public static String splitString(String str) {
        if (str.contains("<unknown>")) {
            return "未知歌手";
        }
        return str;
    }
}
