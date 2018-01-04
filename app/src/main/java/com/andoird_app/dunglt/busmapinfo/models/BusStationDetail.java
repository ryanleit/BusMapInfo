package com.andoird_app.dunglt.busmapinfo.models;

import java.util.ArrayList;

/**
 * Created by dunglt on 12/29/2017.
 */

public class BusStationDetail {

    Integer r, s, v;
    String rN, rNo, sN, vN;

    ArrayList<Bus> busList;

    public BusStationDetail(Integer r, Integer s, Integer v, String rN, String rNo, String sN, String vN, ArrayList<Bus> busList) {
        this.r = r;
        this.s = s;
        this.v = v;
        this.rN = rN;
        this.rNo = rNo;
        this.sN = sN;
        this.vN = vN;
        this.busList = busList;
    }

    public Integer getR() {
        return r;
    }

    public Integer getS() {
        return s;
    }

    public Integer getV() {
        return v;
    }

    public String getRN() {
        return rN;
    }
    public String getRNo() {
        return rNo;
    }
    public String getSN() {
        return sN;
    }
    public String getVN() {
        return vN;
    }

    public ArrayList<Bus> getBusList() {
        return busList;
    }
}
