package com.andoird_app.dunglt.busmapinfo;

import android.app.Dialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.andoird_app.dunglt.busmapinfo.models.BusStationModel;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.maps.model.LatLng;

public class HomeBusStationActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        HistoryFragment.OnListFragmentInteractionListener{


    private static final String TAG = "HomeScreen";
    private static final int ERROR_DIALOG_REQUEST = 9001;
    public DrawerLayout drawer;
    public NavigationView navigationView;
    public LatLng currentLocation;

    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_home_bus_station);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
       // toolbar.setLogo(R.drawable.ic_bus_station_title);
        setSupportActionBar(toolbar);

        if (isServicesOK()) {
            drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                    this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
            drawer.addDrawerListener(toggle);
            toggle.syncState();

            navigationView = (NavigationView) findViewById(R.id.nav_view);
            navigationView.setNavigationItemSelectedListener(this);


            //Get menuItem index 0
            getSupportActionBar().hide();
            MenuItem item = navigationView.getMenu().getItem(0);
            item.setChecked(true);

            android.app.FragmentTransaction ft = getFragmentManager().beginTransaction();
            Class busMapsClass = BusMapsFragment.class;
            try {
                ft.replace(R.id.content_frame, (Fragment) busMapsClass.newInstance());
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            ft.commit();

            /*onNavigationItemSelected(item);

            AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.appbar_layout);
            appBarLayout.setExpanded(false, true);*/

            overridePendingTransition(R.transition.fade_in,R.transition.fade_out);
            if(dialog != null){
                dialog.hide();
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
       // showDialog();
    }

    public void showDialog() {
        dialog = new ProgressDialog(this);
        dialog.setMessage("Loading...");
        dialog.setCancelable(false);
        dialog.setInverseBackgroundForced(false);
        dialog.show();
    }

    public void hideDialog() {
        dialog.hide();
    }

    public void init(){
        /*final android.os.Handler handler = new android.os.Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(BusStations.this, BusMapsActivity.class);
                startActivity(intent);
                overridePendingTransition(R.transition.fade_in,R.transition.fade_out);
            }
        }, 2000);*/
        return;
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
        Fragment fragment = null;
        Class fragmentClass;

        switch(item.getItemId()) {
            case R.id.nav_bus_map:
                fragmentClass = BusMapsFragment.class;
                getSupportActionBar().hide();
                break;
            case R.id.nav_find_route:
                fragmentClass = FindRouteBusStationFragment.class;
                getSupportActionBar().show();
                break;
            case R.id.nav_history:
                fragmentClass = HistoryFragment.class;
                getSupportActionBar().show();
                break;
            case R.id.nav_guide:
                fragmentClass = AboutFragment.class;
                getSupportActionBar().show();
                break;
            default:
                fragmentClass = BusMapsFragment.class;
                getSupportActionBar().hide();
        }

        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
            Log.d(TAG, "Error Navigation: "+ e.getMessage());
        }
        //replacing the fragment
        if (fragment != null) {
            // Insert the fragment by replacing any existing fragment
           // FragmentManager fragmentManager = getSupportFragmentManager();
           // fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
            android.app.FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, fragment);
            ft.commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public boolean isServicesOK()
    {
        //everything is find and user can make map request
        Log.d(TAG, "isServicesOk: check google services version");

        int available = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(HomeBusStationActivity.this);

        if(available == ConnectionResult.SUCCESS){
            Log.d(TAG, "isServicesOK: Google Play Services is working");
            return true;
        }else if(GoogleApiAvailability.getInstance().isUserResolvableError(available)) {
            //an error occured but we can reslove it
            Log.d(TAG, "isServicesOK: an error occured but we can fix it");
            Dialog dialog = GoogleApiAvailability.getInstance().getErrorDialog(HomeBusStationActivity.this, available, ERROR_DIALOG_REQUEST);

            dialog.show();
        }else{
            Toast.makeText(this, "You can't make map request", Toast.LENGTH_SHORT).show();
        }

        return false;
    }

    public void openNavigatioView(){
        drawer.openDrawer(GravityCompat.START);
    }

    public void setCurrentLocation(LatLng location){
        currentLocation = location;
    }

    public  LatLng getCurrentLocation(){
        return currentLocation;
    }

    @Override
    public void onListFragmentInteraction(BusStationModel item) {

    }
}
