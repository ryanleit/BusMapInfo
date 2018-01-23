package com.andoird_app.dunglt.busmapinfo.models;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by dunglt on 12/29/2017.
 */

public class RouteSearchGuide {

    Integer distance;
    String routeNumber,busStationStart, busStationEnd;

    public RouteSearchGuide(String routeNumber, String busStationStart, String busStationEnd) {
        this.routeNumber = routeNumber;
        this.busStationStart = busStationStart;
        this.busStationEnd = busStationEnd;
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
}
