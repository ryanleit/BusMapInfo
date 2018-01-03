package com.andoird_app.dunglt.busmapinfo;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by WIN10 on 12/30/2017.
 */

public class BusStationDetailActivity extends AppCompatActivity implements BusListTab.OnFragmentInteractionListener, DirectionTab.OnFragmentInteractionListener {

    TabLayout tabLayout;

    /* Bus station params */
    Integer mStopId;
    double[] mCurrentLatlng, mBustationLatlng;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bus_station_detail_activity);
        Intent i = getIntent();
        mStopId = i.getIntExtra ("mStopId",199);
        mCurrentLatlng = i.getDoubleArrayExtra("mCurrentLatlng");
        mBustationLatlng = i.getDoubleArrayExtra("mBusStationLatlng");

        //Toast.makeText(BusStationDetailActivity.this, "Item "+ Integer.toString(mStopId)+ " is clicked!", Toast.LENGTH_SHORT).show();

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

        //final ActionBar ab = getSupportActionBar();
        //ab.setDisplayHomeAsUpEnabled(true);

        TabLayout tabLayout =  (TabLayout) findViewById(R.id.tabLayout);
        //Adding the tabs using addTab() method
        tabLayout.addTab(tabLayout.newTab());
        tabLayout.addTab(tabLayout.newTab());
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        ViewPager viewPager = (ViewPager) findViewById(R.id.pager);

        PagerAdapter pagerAdapter;
        pagerAdapter = new PagerAdapter(getSupportFragmentManager(),2, mStopId, mCurrentLatlng, mBustationLatlng);

        viewPager.setAdapter(pagerAdapter);

        tabLayout.setupWithViewPager(viewPager, true);
    }


    @Override
    public void onFragmentInteraction(Uri uri) {
        Log.d("BusStation Detail: " , uri.toString());
    }
}
