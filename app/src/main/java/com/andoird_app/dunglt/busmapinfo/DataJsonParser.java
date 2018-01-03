package com.andoird_app.dunglt.busmapinfo;

import com.andoird_app.dunglt.busmapinfo.models.BusStation;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by dunglt on 12/29/2017.
 */

public class DataJsonParser {
        public static ArrayList<ArrayList> parseBusStationData(JSONArray datas, GoogleMap mMap) {
            ArrayList<ArrayList> dataReturn = new ArrayList<ArrayList>();
            ArrayList<BusStation> busStationList = new ArrayList<BusStation>();
            ArrayList<Marker> busStationMarkers = new ArrayList<Marker>();

            for (int i = 0; i < datas.length(); i++) {
                JSONObject obj = null;
                try {
                    obj = datas.getJSONObject(i);
                    BusStation busStation = new BusStation(obj.getInt("StopId"),
                            obj.getString("Name"),
                            obj.getString("StopType"),
                            obj.getString("AddressNo"),
                            obj.getString("Street"),
                            new LatLng(obj.getDouble("Lat"),obj.getDouble("Lng"))
                    );
                    busStationList.add(busStation);

                    busStationMarkers.add(mMap.addMarker(new MarkerOptions()
                            .position(new LatLng(Double.parseDouble(obj.getString("Lat")),Double.parseDouble(obj.getString("Lng"))))
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_bus_station_marker))));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            dataReturn.add(busStationList);
            dataReturn.add(busStationMarkers);

            return dataReturn;
        }

}

