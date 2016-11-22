package com.edu.jereh.maptest.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by lenovo on 2016/9/26.
 */
public class Info implements Parcelable{
    private String cityName;
    private String title;
    private String tel;
    private String distance;
    private double latitude;
    private double longitude;
    private double nlatitude;
    private double nlongitude;

    public Info() {
    }

    public Info(String cityName, String title, String tel, String distance, double latitude, double longitude, double nlatitude, double nlongitude) {
        this.cityName = cityName;
        this.title = title;
        this.tel = tel;
        this.distance = distance;
        this.latitude = latitude;
        this.longitude = longitude;
        this.nlatitude = nlatitude;
        this.nlongitude = nlongitude;
    }

    protected Info(Parcel in) {
        cityName = in.readString();
        title = in.readString();
        tel = in.readString();
        distance = in.readString();
    }

    public static final Creator<Info> CREATOR = new Creator<Info>() {
        @Override
        public Info createFromParcel(Parcel in) {
            return new Info(in);
        }

        @Override
        public Info[] newArray(int size) {
            return new Info[size];
        }
    };

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public static Creator<Info> getCREATOR() {
        return CREATOR;
    }

    public double getNlatitude() {
        return nlatitude;
    }

    public void setNlatitude(double nlatitude) {
        this.nlatitude = nlatitude;
    }

    public double getNlongitude() {
        return nlongitude;
    }

    public void setNlongitude(double nlongitude) {
        this.nlongitude = nlongitude;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(cityName);
        dest.writeString(title);
        dest.writeString(tel);
        dest.writeString(distance);
        dest.writeDouble(nlatitude);
        dest.writeDouble(nlongitude);
    }
}
