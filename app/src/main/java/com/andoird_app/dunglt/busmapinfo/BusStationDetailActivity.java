package com.andoird_app.dunglt.busmapinfo;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.andoird_app.dunglt.busmapinfo.models.Bus;
import com.andoird_app.dunglt.busmapinfo.models.BusStationDetail;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by WIN10 on 12/30/2017.
 */

public class BusStationDetailActivity extends AppCompatActivity implements
        NavigationView.OnNavigationItemSelectedListener,
        BusListTab.OnFragmentInteractionListener,
        DirectionTab.OnFragmentInteractionListener {

    TabLayout tabLayout;

    /* Bus station params */
    Integer mStopId;
    double[] mCurrentLatlng, mBustationLatlng;

    //params for list bus view
    private final String authKey = "33044a4fc0d44b7ba4441a0f09c60381";
    final  static String  TAG = "BusStationDetail";

    ArrayList<BusStationDetail> mBusStationInfo;

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
       // toolbar.setNavigationIcon(getDrawable(R.drawable.ic_navbar));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(BusStationDetailActivity.this, "Icon navigation click", Toast.LENGTH_SHORT).show();
            }
        });

        setSupportActionBar(toolbar);
        final ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        tabLayout =  (TabLayout) findViewById(R.id.tabLayout);
        //Adding the tabs using addTab() method
        tabLayout.addTab(tabLayout.newTab());
        tabLayout.addTab(tabLayout.newTab());
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        tabLayout.setSelectedTabIndicatorColor(Color.parseColor("#3a07e9"));

        BusInfoApi busApi = new BusInfoApi();
        requestBusListApi(busApi.getUrlRequestBusList(mStopId));
    }

    public void setUpTabLayout(){

        ViewPager viewPager = (ViewPager) findViewById(R.id.pager);

        PagerAdapter pagerAdapter;
        pagerAdapter = new PagerAdapter(getSupportFragmentManager(),2, mStopId, mBusStationInfo, mCurrentLatlng, mBustationLatlng);

        viewPager.setAdapter(pagerAdapter);

        tabLayout.setupWithViewPager(viewPager);
    }
    // Request bus list API
    public void requestBusListApi(String uri){
        RequestQueue queue = Volley.newRequestQueue(this);
        Log.d(TAG, "Call API :" + uri);
        // prepare the Request
        JsonArrayRequest getRequest = new JsonArrayRequest(Request.Method.GET, uri, null,
                new Response.Listener<JSONArray>()
                {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d(TAG,"Api response: " + response.toString());
                        mBusStationInfo = new ArrayList<BusStationDetail>();
                        // display response
                        if(response.length() > 0) {
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject obj = null;
                                try {
                                    obj = response.getJSONObject(i);

                                    ArrayList<Bus> busList = new ArrayList<Bus>();
                                    JSONArray data = obj.getJSONArray("arrs");
                                    for (int j = 0; j < data.length(); j++) {
                                        JSONObject objBus = data.getJSONObject(j);

                                        Bus bus = new Bus(objBus.getDouble("d"),
                                                objBus.getDouble("s"),
                                                objBus.getInt("t"),
                                                objBus.getInt("sts"),
                                                objBus.getString("dts"),
                                                objBus.getString("v")
                                        );
                                        busList.add(bus);
                                    }

                                    BusStationDetail busStationDetail = new BusStationDetail(
                                            obj.getInt("r"),
                                            obj.getInt("s"),
                                            obj.getInt("v"),
                                            obj.getString("rN"),
                                            obj.getString("rNo"),
                                            obj.getString("sN"),
                                            obj.getString("vN"),
                                            busList
                                    );
                                    mBusStationInfo.add(busStationDetail);

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                        setUpTabLayout();
                        return;
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d(TAG, "Error: " + error.getMessage());
                    }
                }
        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<String, String>();
                //  params.put("Content-Type", "application/json");
                params.put("api_key", authKey);
                return params;
            }

        };
        // add it to the RequestQueue
        queue.add(getRequest);

    }

    @Override
    public void onFragmentInteraction(Uri uri) {
        Log.d("BusStation Detail: " , uri.toString());
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.find_route_bus_station, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
