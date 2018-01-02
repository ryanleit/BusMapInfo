package com.andoird_app.dunglt.busmapinfo.models;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by dunglt on 12/29/2017.
 */

public class Bus {

    Double distance, kmh;
    Integer time, status;

    String dateTime, busNumber;


    public Bus(Double distance, Double kmh, Integer time, Integer status, String dateTime, String busNumber) {
        this.distance = distance;
        this.kmh = kmh;
        this.time = time;
        this.status = status;
        this.dateTime = dateTime;
        this.busNumber = busNumber;

    }

    public Double getDistance() {
        return distance;
    }

    public Double getKmh() {
        return kmh;
    }

    public Integer getTime() {
        return time;
    }
    public String getDateTime() {
        return dateTime;
    }

    public Integer getStatus() {
        return status;
    }

    public String getBusNumber() {
        return busNumber;
    }

}
