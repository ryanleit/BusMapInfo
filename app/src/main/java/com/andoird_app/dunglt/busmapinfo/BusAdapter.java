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

import com.andoird_app.dunglt.busmapinfo.models.Bus;
import com.andoird_app.dunglt.busmapinfo.models.BusStationInfo;

import java.util.ArrayList;

public class BusAdapter extends ArrayAdapter<Object> {
    private ArrayList<Object> dataArray;
    private LayoutInflater inflater;
    private static final int TYPE_BUS = 0;
    private static final int TYPE_DIVIDER = 1;

    public BusAdapter(Context context, ArrayList<Object> dataArray) {
        super(context, 0, dataArray);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        int type = TYPE_DIVIDER;
        if (getItem(position) instanceof Bus) {
            type = TYPE_BUS;
        }

        switch (type) {
            case TYPE_BUS:
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.bus_listview, parent, false);
                break;
            case TYPE_DIVIDER:
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.bus_listview_header, parent, false);
                break;
        }

        switch (type) {
            case TYPE_BUS:
                // Get the data item for this position
                Bus bus = (Bus)getItem(position);

                // Check if an existing view is being reused, otherwise inflate the view
                // Lookup view for data population
                TextView tvName = (TextView) convertView.findViewById(R.id.bus_title);
                TextView tvBusInfor = (TextView) convertView.findViewById(R.id.bus_infor);
                TextView tvDistance = (TextView) convertView.findViewById(R.id.distance);
                // Populate the data into the template view using the data object
                tvName.setText(bus.getBusNumber());
                tvBusInfor.setText("Duration: " + Integer.toString(bus.getTime())+ ", " + bus.getDateTime());
                tvDistance.setText(Double.toString(bus.getDistance()) + "m");

                break;
            case TYPE_DIVIDER:
                BusStationInfo busStation = (BusStationInfo)getItem(position);

                TextView tvBusStationRoute = (TextView)convertView.findViewById(R.id.bus_station_route);
                TextView tvBusStationDesc = (TextView)convertView.findViewById(R.id.bus_station_desc);

                tvBusStationRoute.setText(busStation.getRN());
                tvBusStationDesc.setText(busStation.getRNo()+", "+busStation.getSN()+", "+ busStation.getVN());
                break;
        }

        // Return the completed view to render on screen
        return convertView;
    }
}
