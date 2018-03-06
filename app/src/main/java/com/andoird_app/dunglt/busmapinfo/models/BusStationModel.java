package com.andoird_app.dunglt.busmapinfo.models;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by dunglt on 12/29/2017.
 */

@Entity
public class BusStationModel {
    @Id(autoincrement = true)
    Long id;
    String name;
    String stopType;
    String addressNo;
    String street;
    String routes;
    Double lat;
    Double lng;
    @Generated(hash = 953416704)
    public BusStationModel(Long id, String name, String stopType, String addressNo,
            String street, String routes, Double lat, Double lng) {
        this.id = id;
        this.name = name;
        this.stopType = stopType;
        this.addressNo = addressNo;
        this.street = street;
        this.routes = routes;
        this.lat = lat;
        this.lng = lng;
    }
    @Generated(hash = 1903136693)
    public BusStationModel() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getStopType() {
        return this.stopType;
    }
    public void setStopType(String stopType) {
        this.stopType = stopType;
    }
    public String getAddressNo() {
        return this.addressNo;
    }
    public void setAddressNo(String addressNo) {
        this.addressNo = addressNo;
    }
    public String getStreet() {
        return this.street;
    }
    public void setStreet(String street) {
        this.street = street;
    }
    public String getRoutes() {
        return this.routes;
    }
    public void setRoutes(String routes) {
        this.routes = routes;
    }
    public Double getLat() {
        return this.lat;
    }
    public void setLat(Double lat) {
        this.lat = lat;
    }
    public Double getLng() {
        return this.lng;
    }
    public void setLng(Double lng) {
        this.lng = lng;
    }
}
