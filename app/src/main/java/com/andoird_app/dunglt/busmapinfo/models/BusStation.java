package com.andoird_app.dunglt.busmapinfo.models;

import com.google.android.gms.maps.model.LatLng;

import java.util.Locale;

/**
 * Created by dunglt on 12/29/2017.
 */

public class BusStation {

    Integer stopId;
    String code, name, stopType, zone, ward, addressNo, street, supportDisability, status, search, routes;

    LatLng latLng;

    public BusStation(Integer stopId, String name, String stopType, String addressNo, String street, LatLng latLng, String routes) {
        this.stopId = stopId;
        this.name = name;
        this.stopType = stopType;
        this.addressNo = addressNo;
        this.street = street;
        this.latLng = latLng;
        this.routes = routes;

    }

    public Integer getStopId() {
        return stopId;
    }

    public String getName() {
        return name;
    }

    public String getStopType() {
        return stopType;
    }

    public String getAddressNo() {
        return addressNo;
    }

    public String getStreet() {
        return street;
    }

    public LatLng getLatLng() {
        return latLng;
    }

    public String getRoutes(){
        return routes;
    }
}
