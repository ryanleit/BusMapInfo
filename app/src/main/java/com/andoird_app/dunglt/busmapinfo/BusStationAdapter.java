package com.andoird_app.dunglt.busmapinfo;

/**
 * Created by WIN10 on 12/30/2017.
 */

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.andoird_app.dunglt.busmapinfo.models.BusStation;

import java.util.ArrayList;

public class BusStationAdapter extends ArrayAdapter<BusStation> {
    public BusStationAdapter(Context context, ArrayList<BusStation> busStations) {
        super(context, 0, busStations);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        BusStation busStation = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.activity_listview, parent, false);
        }
        // Lookup view for data population
        TextView tvName = (TextView) convertView.findViewById(R.id.bus_station_title);
        TextView tvAddress = (TextView) convertView.findViewById(R.id.bus_detail);
        // Populate the data into the template view using the data object
        tvName.setText(busStation.getName());
        tvAddress.setText(busStation.getAddressNo()+ ", " + busStation.getStreet());
        // Return the completed view to render on screen
        return convertView;
    }
}
