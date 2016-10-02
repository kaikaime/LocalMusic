package com.example.administrator.good.utils;

import android.app.Application;
import android.content.Context;
import android.os.Handler;

/**
 * Created by lvkaixue on 2016/9/24.
 */
public class BaseApplication extends Application {
    private static Context context;
    private static Handler handler;
    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        handler = new Handler();
    }
    public static Context getContext(){
        return context;
    }
    public static Handler getHandler(){
        return handler;
    }
}
