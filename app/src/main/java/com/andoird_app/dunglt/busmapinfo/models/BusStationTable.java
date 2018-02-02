package com.andoird_app.dunglt.busmapinfo.models;

import com.orm.SugarRecord;

/**
 * Created by dunglt on 12/29/2017.
 */

public class BusStationTable extends SugarRecord<BusStationTable> {

    Integer stopId;
    String code, name, stopType, zone, ward, addressNo, street, supportDisability, status, search, routes;

    Double lat, lng;

    public BusStationTable(Integer stopId, String name, String stopType, String addressNo, String street,Double lat, Double lng, String routes) {
        this.stopId = stopId;
        this.name = name;
        this.stopType = stopType;
        this.addressNo = addressNo;
        this.street = street;
        this.lat = lat;
        this.lng = lng;
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

    public Double getLat() {
        return lat;
    }

    public Double getLng() {
        return lng;
    }

    public String getRoutes(){
        return routes;
    }
}
