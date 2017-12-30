package com.andoird_app.dunglt.busmapinfo;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.widget.Toast;

/**
 * Created by WIN10 on 12/30/2017.
 */

public class BusStationDetailActivity extends FragmentActivity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bus_station_detail_activity);
        Intent i = getIntent();
        Integer position = i.getIntExtra ("position",1);
        Toast.makeText(BusStationDetailActivity.this, "Item "+ Integer.toString(position)+ " is clicked!", Toast.LENGTH_SHORT).show();
    }
}
