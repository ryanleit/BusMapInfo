package com.andoird_app.dunglt.busmapinfo;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.andoird_app.dunglt.busmapinfo.models.BusStationDetail;
import com.andoird_app.dunglt.busmapinfo.models.RouteSearchGuide;

import java.util.Collections;
import java.util.List;

/**
 * Created by WIN10 on 1/4/2018.
 */

public class Recycler_View_Find_Route_Adapter extends RecyclerView.Adapter<View_Find_Route_Holder> {

    List<RouteSearchGuide> list = Collections.emptyList();
    Context context;

    public Recycler_View_Find_Route_Adapter(List<RouteSearchGuide> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public View_Find_Route_Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        //Inflate the layout, initialize the View Holder
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.find_route_recycler_row_layout, parent, false);
        View_Find_Route_Holder holder = new View_Find_Route_Holder(v);
        return holder;

    }

    @Override
    public void onBindViewHolder(View_Find_Route_Holder holder, int position) {

        //Use the provided View Holder on the onCreateViewHolder method to populate the current row on the RecyclerView
        holder.title.setText("Station start: "+list.get(position).getBusStationStart()+ "- Destination: "+list.get(position).getBusStationEnd());
        holder.distance.setText("Total distance: "+ Double.toString(list.get(position).getDistance())+ " km");
        holder.description.setText("Go on these routes: " +list.get(position).getRouteNumber());
        holder.imageView.setImageResource(R.drawable.ic_bus_float);

        //animate(holder);

    }

    @Override
    public int getItemCount() {
        //returns the number of elements the RecyclerView will display
        return list.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    // Insert a new item to the RecyclerView on a predefined position
    public void insert(int position, RouteSearchGuide data) {
        list.add(position, data);
        notifyItemInserted(position);
    }

    // Remove a RecyclerView item containing a specified Data object
    public void remove(RouteSearchGuide data) {
        int position = list.indexOf(data);
        list.remove(position);
        notifyItemRemoved(position);
    }

}
