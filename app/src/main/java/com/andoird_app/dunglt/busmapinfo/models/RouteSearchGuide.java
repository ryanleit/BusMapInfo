package com.andoird_app.dunglt.busmapinfo.models;

/**
 * Created by dunglt on 12/29/2017.
 */

public class RouteSearchGuide {

    Double distance;
    String routeNumber,busStationStart, busStationEnd;

    public RouteSearchGuide(String routeNumber, String busStationStart, String busStationEnd, Double distance) {
        this.routeNumber = routeNumber;
        this.busStationStart = busStationStart;
        this.busStationEnd = busStationEnd;
        this.distance = distance;
    }

    public String getRouteNumber() {
        return routeNumber;
    }

    public String getBusStationEnd() {
        return busStationEnd;
    }

    public String getBusStationStart() {
        return busStationStart;
    }

    public Double getDistance(){
        return Math.round(distance*100)/100.0;
    }
}
