package com.andoird_app.dunglt.busmapinfo;

import com.andoird_app.dunglt.busmapinfo.models.BusStation;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dunglt on 12/29/2017.
 */

public class DataJsonParser {
        static List<BusStation> busStationList;

        public static List<BusStation> parseData(JSONArray content) {

            JSONArray bus_station_arr = null;
            BusStation busStations = null;
            try {
                busStationList = new ArrayList<>();
                BusStation busStation = null;
                for (int i = 0; i < content.length(); i++) {
                    JSONObject obj = content.getJSONObject(i);
                    busStation = new BusStation(obj.getInt("stopId"),
                            obj.getString("name"),
                            obj.getString("stopType"),
                            obj.getString("addressNo"),
                            obj.getString("street"),
                            new LatLng(obj.getDouble("lat"), obj.getDouble("lng"))
                    );

                    busStationList.add(busStation);
                }
                return busStationList;
            }
            catch (JSONException ex) {
                ex.printStackTrace();
                return null;
            }
        }

}

