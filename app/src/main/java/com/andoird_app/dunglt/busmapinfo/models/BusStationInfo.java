package com.andoird_app.dunglt.busmapinfo.models;

/**
 * Created by dunglt on 12/29/2017.
 */

public class BusStationInfo {

    Integer r, s, v;
    String rN, rNo, sN, vN;


    public BusStationInfo(Integer r, Integer s, Integer v, String rN, String rNo, String sN, String vN) {
        this.r = r;
        this.s = s;
        this.v = v;
        this.rN = rN;
        this.rNo = rNo;
        this.sN = sN;
        this.vN = vN;
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
}
