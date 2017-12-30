package com.andoird_app.dunglt.busmapinfo;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

/**
 * Created by WIN10 on 12/30/2017.
 */

public class BusStationDetailActivity extends AppCompatActivity {
    //TabHost tabHost;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bus_station_detail_activity);
        Intent i = getIntent();
        Integer position = i.getIntExtra ("position",1);
        Toast.makeText(BusStationDetailActivity.this, "Item "+ Integer.toString(position)+ " is clicked!", Toast.LENGTH_SHORT).show();

        android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Bus Station Detail");
        toolbar.setNavigationIcon(getDrawable(R.drawable.ic_navbar));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(BusStationDetailActivity.this, "Icon navigation click", Toast.LENGTH_SHORT).show();
            }
        });
        setSupportActionBar(toolbar);

        final ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);


    }



}
