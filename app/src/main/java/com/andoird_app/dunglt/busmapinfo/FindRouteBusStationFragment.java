package com.andoird_app.dunglt.busmapinfo;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.andoird_app.dunglt.busmapinfo.models.BusStation;
import com.andoird_app.dunglt.busmapinfo.models.RouteSearchGuide;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.location.places.AutocompletePrediction;
import com.google.android.gms.location.places.GeoDataClient;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBufferResponse;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.RuntimeRemoteException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.maps.android.SphericalUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FindRouteBusStationFragment extends Fragment {

    private static final String TAG = "MapActivity";
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    ArrayList<BusStation> busStationList;
    ArrayList<BusStation> busStationListStart;
    ArrayList<BusStation> busStationListEnd;
    ArrayList<RouteSearchGuide> routeSearchGuides;

    private AutoCompleteTextView mSearchStart;
    private AutoCompleteTextView mSearchEnd;
    private Button mBtnFindRoute;

    private static final Double radisusForBound = 500.0;
    private static final LatLngBounds LAT_LNG_BOUNDS = new LatLngBounds(
            new LatLng(-40, -168), new LatLng(71, 136)
    );
    //Auto complete
    protected GeoDataClient mGeoDataClient;
    protected GeoDataClient mGeoDataClientDestination;
    private PlaceAutocompleteAdapter mPlaceAutocompleteAdapter;
    private PlaceAutocompleteAdapter mPlaceAutocompleteAdapterDestination;
    private RecyclerView recyclerFindRouteView;
    private LatLng position_a;
    private LatLng position_b;

    private final String authKey = "33044a4fc0d44b7ba4441a0f09c60381";

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AboutFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FindRouteBusStationFragment newInstance(String param1, String param2) {
        FindRouteBusStationFragment fragment = new FindRouteBusStationFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_find_route_bus_station, viewGroup, false);

        mSearchStart = (AutoCompleteTextView) view.findViewById(R.id.input_start);
        mSearchEnd = (AutoCompleteTextView) view.findViewById(R.id.input_destination);

       /* request get bustation */
        BusInfoApi busApi = new BusInfoApi();
        position_a = ((HomeBusStationActivity)getActivity()).getCurrentLocation();
        LatLngBounds latLngBounds = toBounds(position_a, radisusForBound);
        requestBusStationListApi(busApi.getUrlRequestBusStationInfoByBounds(latLngBounds),"start");

        initSearch();

        recyclerFindRouteView = (RecyclerView) view.findViewById(R.id.find_route_info_list);
        mBtnFindRoute = (Button) view.findViewById(R.id.btnFindRoute);
        mBtnFindRoute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if((busStationListStart != null && busStationListStart.size() > 0)
                        && (busStationListEnd != null && busStationListEnd.size() > 0)){
                    routeSearchGuides =  new ArrayList<RouteSearchGuide>();

                    for (int i=0; i < busStationListStart.size(); i++){
                        RouteSearchGuide rsg = null;
                        String[] routes = busStationListStart.get(i).getRoutes().split(",");
                        for (int j = 0; j < busStationListEnd.size(); j++) {
                            String routesNo = "";
                            String s = ", "+busStationListStart.get(i).getRoutes()+",";
                            for(int r=0; r < routes.length; r++) {
                                String subS = ", "+routes[r].trim()+",";
                                if (s.indexOf(subS) != -1) {
                                    routesNo += routes[r].trim()+",";
                                }
                            }

                            if(!routesNo.equals("")){
                                Double distance1 = distance(position_a.latitude, position_a.longitude,busStationListStart.get(i).getLatLng().latitude,busStationListStart.get(i).getLatLng().longitude,'K');
                                Double distance2 = distance(busStationListStart.get(i).getLatLng().latitude,busStationListStart.get(i).getLatLng().longitude,busStationListEnd.get(j).getLatLng().latitude,busStationListEnd.get(j).getLatLng().longitude,'K');
                                Double distance3 = distance(busStationListEnd.get(j).getLatLng().latitude,busStationListEnd.get(j).getLatLng().longitude,position_b.latitude, position_b.longitude,'K');

                                Double distance = distance1 + distance2 + distance3;
                                routesNo = routesNo.substring(0, routesNo.length() - 1);
                                rsg = new RouteSearchGuide(routesNo,busStationListStart.get(i).getName(), busStationListEnd.get(j).getName(), distance);

                                routeSearchGuides.add(rsg);
                            }
                        }

                    }

                    if(routeSearchGuides.size() > 0) {
                        Recycler_View_Find_Route_Adapter adapter = new Recycler_View_Find_Route_Adapter(routeSearchGuides, FindRouteBusStationFragment.super.getActivity());
                        recyclerFindRouteView.setAdapter(adapter);
                        recyclerFindRouteView.setLayoutManager(new LinearLayoutManager(FindRouteBusStationFragment.super.getActivity()));

                        //******
                        RecyclerView.ItemAnimator itemAnimator = new DefaultItemAnimator();
                        itemAnimator.setAddDuration(1000);
                        itemAnimator.setRemoveDuration(1000);
                        recyclerFindRouteView.setItemAnimator(itemAnimator);

                        recyclerFindRouteView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
                            @Override
                            public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {

                                rv.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {

                                    }
                                });
                                return false;
                            }

                            @Override
                            public void onTouchEvent(RecyclerView rv, MotionEvent e) {

                            }

                            @Override
                            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

                            }
                        });
                    }
                }else{
                    Toast.makeText(FindRouteBusStationFragment.super.getActivity(), "Have not data!", Toast.LENGTH_SHORT).show();
                }
            }
        });
        return view;
    }
    private double distance(double lat1, double lon1, double lat2, double lon2, char unit) {
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
        if (unit == 'K') {
            dist = dist * 1.609344;
        } else if (unit == 'N') {
            dist = dist * 0.8684;
        }
        return (dist);
    }

    /*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
    /*::  This function converts decimal degrees to radians             :*/
    /*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
    private double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    /*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
    /*::  This function converts radians to decimal degrees             :*/
    /*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
    private double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
    }

    private void initSearch() {
        // Register a listener that receives callbacks when a suggestion has been selected
        mSearchStart.setOnItemClickListener(mAutocompleteClickListener);
        // Construct a GeoDataClient for the Google Places API for Android.
        mGeoDataClient = Places.getGeoDataClient(super.getActivity(), null);

        mPlaceAutocompleteAdapter = new PlaceAutocompleteAdapter(super.getActivity(), mGeoDataClient, LAT_LNG_BOUNDS, null);

        mSearchStart.setAdapter(mPlaceAutocompleteAdapter);


        // Register a listener that receives callbacks when a suggestion has been selected
        mSearchEnd.setOnItemClickListener(mAutocompleteClickListenerDestination);
        mGeoDataClientDestination = Places.getGeoDataClient(super.getActivity(), null);
        mPlaceAutocompleteAdapterDestination = new PlaceAutocompleteAdapter(super.getActivity(), mGeoDataClientDestination, LAT_LNG_BOUNDS, null);
        mSearchEnd.setAdapter(mPlaceAutocompleteAdapterDestination);

        hideSoftKeyboard();
    }

    private void geoLocate(String searchString) {
        Geocoder geocoder = new Geocoder(super.getActivity());
        List<Address> list = new ArrayList<>();

        try {
            list = geocoder.getFromLocationName(searchString, 1);
        } catch (IOException e) {
            Log.d(TAG, "geoLocate: IOexception: " + e.getMessage());
        }

        if (list.size() > 0) {
            Address address = list.get(0);
        }
    }


    public LatLngBounds toBounds(LatLng center, double radiusInMeters) {
        double distanceFromCenterToCorner = radiusInMeters * Math.sqrt(2.0);
        LatLng southwestCorner =
                SphericalUtil.computeOffset(center, distanceFromCenterToCorner, 225.0);
        LatLng northeastCorner =
                SphericalUtil.computeOffset(center, distanceFromCenterToCorner, 45.0);
        return new LatLngBounds(southwestCorner, northeastCorner);
    }
    /*
    * ***********************************************************************************
    * AUTO COMPLETE SEARCH LOCATION
    * ***********************************************************************************
    * */

    /**
     * Listener that handles selections from suggestions from the AutoCompleteTextView that
     * displays Place suggestions.
     * Gets the place id of the selected item and issues a request to the Places Geo Data Client
     * to retrieve more details about the place.
     *
     * @see GeoDataClient#getPlaceById(String...)
     */
    private AdapterView.OnItemClickListener mAutocompleteClickListener
            = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            /*
             Retrieve the place ID of the selected item from the Adapter.
             The adapter stores each Place suggestion in a AutocompletePrediction from which we
             read the place ID and title.
              */
            final AutocompletePrediction item = mPlaceAutocompleteAdapter.getItem(position);

            final String placeId = item.getPlaceId();
            final CharSequence primaryText = item.getPrimaryText(null);

            Log.i(TAG, "Autocomplete item selected: " + primaryText);

            /*
             Issue a request to the Places Geo Data Client to retrieve a Place object with
             additional details about the place.
              */
            Task<PlaceBufferResponse> placeResult = mGeoDataClient.getPlaceById(placeId);
            placeResult.addOnCompleteListener(mUpdatePlaceDetailsCallback);
        }
    };

    /**
     * Callback for results from a Places Geo Data Client query that shows the first place result in
     * the details view on screen.
     */
    private OnCompleteListener<PlaceBufferResponse> mUpdatePlaceDetailsCallback
            = new OnCompleteListener<PlaceBufferResponse>() {
        @Override
        public void onComplete(Task<PlaceBufferResponse> task) {
            try {
                hideSoftKeyboard();

                PlaceBufferResponse places = task.getResult();

                // Get the Place object from the buffer.
                final Place place = places.get(0);

                 /* request get bustation */
                BusInfoApi busApi = new BusInfoApi();

                position_a = new LatLng(place.getLatLng().latitude, place.getLatLng().longitude);

                LatLngBounds latLngBounds = toBounds(position_a, radisusForBound);
                requestBusStationListApi(busApi.getUrlRequestBusStationInfoByBounds(latLngBounds),"start");

                Log.i(TAG, "Place details start: Lat: " + Double.toString(place.getLatLng().latitude) + ", Lng: "+ Double.toString(place.getLatLng().longitude));

                places.release();
            } catch (RuntimeRemoteException e) {
                // Request did not complete successfully
                Log.e(TAG, "Place query did not complete.", e);
                return;
            }
        }
    };

    /* for search place destination */
    /**
     * Listener that handles selections from suggestions from the AutoCompleteTextView that
     * displays Place suggestions.
     * Gets the place id of the selected item and issues a request to the Places Geo Data Client
     * to retrieve more details about the place.
     *
     * @see GeoDataClient#getPlaceById(String...)
     */
    private AdapterView.OnItemClickListener mAutocompleteClickListenerDestination
            = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            /*
             Retrieve the place ID of the selected item from the Adapter.
             The adapter stores each Place suggestion in a AutocompletePrediction from which we
             read the place ID and title.
              */
            final AutocompletePrediction item = mPlaceAutocompleteAdapterDestination.getItem(position);

            final String placeId = item.getPlaceId();
            final CharSequence primaryText = item.getPrimaryText(null);

            Log.i(TAG, "Autocomplete item selected: " + primaryText);

            /*
             Issue a request to the Places Geo Data Client to retrieve a Place object with
             additional details about the place.
              */
            Task<PlaceBufferResponse> placeResult = mGeoDataClient.getPlaceById(placeId);
            placeResult.addOnCompleteListener(mUpdatePlaceDetailsCallbackDestination);
        }
    };

    /**
     * Callback for results from a Places Geo Data Client query that shows the first place result in
     * the details view on screen.
     */
    private OnCompleteListener<PlaceBufferResponse> mUpdatePlaceDetailsCallbackDestination
            = new OnCompleteListener<PlaceBufferResponse>() {
        @Override
        public void onComplete(Task<PlaceBufferResponse> task) {
            try {
                hideSoftKeyboard();

                PlaceBufferResponse places = task.getResult();

                // Get the Place object from the buffer.
                final Place place = places.get(0);

                 /* request get bustation */
                BusInfoApi busApi = new BusInfoApi();

                position_b = new LatLng(place.getLatLng().latitude, place.getLatLng().longitude);

                LatLngBounds latLngBounds = toBounds(position_b, radisusForBound);
                requestBusStationListApi(busApi.getUrlRequestBusStationInfoByBounds(latLngBounds),"destination");

                places.release();
            } catch (RuntimeRemoteException e) {
                // Request did not complete successfully
                Log.e(TAG, "Place query did not complete.", e);
                return;
            }
        }
    };

    public void requestBusStationListApi(String uri, final String type) {

        RequestQueue queue = Volley.newRequestQueue(super.getActivity());

        // prepare the Request
        JsonArrayRequest getRequest = new JsonArrayRequest(Request.Method.GET, uri, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        if (response.length() > 0) {

                            busStationList = new ArrayList<BusStation>();
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject obj = null;
                                try {
                                    obj = response.getJSONObject(i);
                                    if (obj.getString("Status").equals("Đang khai thác")) {
                                        BusStation busStation = new BusStation(obj.getInt("StopId"),
                                                obj.getString("Name"),
                                                obj.getString("StopType"),
                                                obj.getString("AddressNo"),
                                                obj.getString("Street"),
                                                new LatLng(obj.getDouble("Lat"), obj.getDouble("Lng")),
                                                obj.getString("Routes")
                                        );
                                        busStationList.add(busStation);

                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                            if (busStationList.size() > 0) {
                                if(type.equals("start")){
                                    busStationListStart = busStationList;
                                }else{
                                    busStationListEnd = busStationList;
                                }

                                //busStationList.clear();
                            }
                        } else {
                            Log.d(TAG, "Data bus station return empty");
                        }
                    }
                },
                new Response.ErrorListener() {
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
    private void hideSoftKeyboard(){

        InputMethodManager imm = (InputMethodManager)super.getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(mSearchStart.getWindowToken(), 0);
        imm.hideSoftInputFromWindow(mSearchEnd.getWindowToken(), 0);
    }
}
