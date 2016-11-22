package com.edu.jereh.maptest.application;

import android.app.Application;
import android.util.Log;

import com.edu.jereh.maptest.entity.Info;

import java.util.List;

/**
 * Created by lenovo on 2016/9/27.
 */
public class MyApplication extends Application {
    private static MyApplication app;
//    private static MainActivity main;
    private List<Info> infoList;
    private float latitude;
    private float longitude;

//    public static MainActivity getMain() {
//        return main;
//    }
//
//    public static void setMain(MainActivity main) {
//        MyApplication.main = main;
//    }

    public static MyApplication getApp() {
        return app;
    }

    public static void setApp(MyApplication app) {
        MyApplication.app = app;
    }

    public List<Info> getInfoList() {
        return infoList;
    }

    public void setInfoList(List<Info> infoList) {
        this.infoList = infoList;
    }

    public float getLatitude() {
        return latitude;
    }

    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }

    public float getLongitude() {
        return longitude;
    }

    public void setLongitude(float longitude) {
        this.longitude = longitude;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        infoList=getInfoList();
        Log.d("====",infoList.size()+"");
    }
}
