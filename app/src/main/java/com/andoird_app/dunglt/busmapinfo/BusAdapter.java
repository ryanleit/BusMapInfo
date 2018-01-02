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
import com.andoird_app.dunglt.busmapinfo.models.BusStation;

import java.util.ArrayList;

public class BusAdapter extends ArrayAdapter<Bus> {
    public BusAdapter(Context context, ArrayList<Bus> bus) {
        super(context, 0, bus);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Bus bus = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.bus_listview, parent, false);
        }
        // Lookup view for data population
        TextView tvName = (TextView) convertView.findViewById(R.id.bus_title);
        TextView tvBusInfor = (TextView) convertView.findViewById(R.id.bus_infor);
        TextView tvDistance = (TextView) convertView.findViewById(R.id.distance);
        // Populate the data into the template view using the data object
        tvName.setText(bus.getBusNumber());
        tvBusInfor.setText("Duration: " + Integer.toString(bus.getTime())+ ", " + bus.getDateTime());
        tvDistance.setText(Double.toString(bus.getDistance()) + "m");
        // Return the completed view to render on screen
        return convertView;
    }
}
