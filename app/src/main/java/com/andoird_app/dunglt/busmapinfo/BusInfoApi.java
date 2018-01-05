package com.andoird_app.dunglt.busmapinfo;

import android.content.Context;
import android.util.Log;

import com.android.volley.RequestQueue;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;

import org.json.JSONArray;

import java.util.ArrayList;


/**
 * Created by dunglt on 12/29/2017.
 */

public class BusInfoApi extends VolleyApi{

    private final String urlRequestApi = "http://api.openfpt.vn/fbusinfo";
    private final String authKey = "33044a4fc0d44b7ba4441a0f09c60381";

    RequestQueue queue;

    private final String TAG = "BusInfoApi";

    public String getUrlRequestBusStationInfo(ArrayList latlngArr){
        Marker position1 = (Marker) latlngArr.get(0);
        Marker position2 = (Marker) latlngArr.get(1);
        String url = urlRequestApi + "/businfo/getstopsinbounds/"+position1.getPosition().longitude + "/"+position1.getPosition().latitude + "/"+position2.getPosition().longitude + "/"+position2.getPosition().latitude ;
        Log.d(TAG, url);
        return url;
    }

    public String getUrlRequestBusStationInfoByBounds(LatLngBounds bounds){
        String url = urlRequestApi + "/businfo/getstopsinbounds/"+bounds.northeast.longitude + "/"+bounds.northeast.latitude + "/"+bounds.southwest.longitude + "/"+bounds.southwest.latitude ;
        Log.d(TAG, url);
        return url;
    }
    public String getUrlRequestBusList(Integer stopId){

        String url = urlRequestApi + "/prediction/predictbystopid/"+ Integer.toString(stopId);
        Log.d(TAG, url);
        return url;
    }
    public JSONArray getBusStationInfo(Context context, ArrayList latlngArr){

        this.getRequestJsonArrayApi(context, getUrlRequestBusStationInfo(latlngArr));

        return this.dataApiReturn;
    }

}
