package com.edu.jereh.maptest.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by lenovo on 2016/9/27.
 */
public class Location implements Parcelable{
    private Double latitude;
    private Double longitude;
//    private Double nlatitude;
//    private Double nlongitude;

    public Location() {
    }

    public Location(Double latitude, Double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
//        this.nlatitude = nlatitude;
//        this.nlongitude = nlongitude;
    }

    protected Location(Parcel in) {
    }

    public static final Creator<Location> CREATOR = new Creator<Location>() {
        @Override
        public Location createFromParcel(Parcel in) {
            return new Location(in);
        }

        @Override
        public Location[] newArray(int size) {
            return new Location[size];
        }
    };

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

//    public Double getNlatitude() {
//        return nlatitude;
//    }
//
//    public void setNlatitude(Double nlatitude) {
//        this.nlatitude = nlatitude;
//    }
//
//    public Double getNlongitude() {
//        return nlongitude;
//    }
//
//    public void setNlongitude(Double nlongitude) {
//        this.nlongitude = nlongitude;
//    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
    }
}
