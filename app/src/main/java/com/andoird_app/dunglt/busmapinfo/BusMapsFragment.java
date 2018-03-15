package com.andoird_app.dunglt.busmapinfo;


import android.Manifest;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.FloatingActionButton;

import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;

import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.andoird_app.dunglt.busmapinfo.models.BusStation;
import com.andoird_app.dunglt.busmapinfo.models.BusStationModel;
import com.andoird_app.dunglt.busmapinfo.models.BusStationModelDao;
import com.andoird_app.dunglt.busmapinfo.models.BusStationTable;
import com.andoird_app.dunglt.busmapinfo.models.DaoSession;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.AutocompletePrediction;
import com.google.android.gms.location.places.GeoDataClient;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBufferResponse;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.RuntimeRemoteException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.maps.android.SphericalUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BusMapsFragment extends Fragment implements
        OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    private GoogleMap mMap;
    private static final String TAG = "MapActivity";
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COARSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1234;
    private static final float DEFAULT_ZOOM = 15.0f;
    private static final LatLngBounds LAT_LNG_BOUNDS = new LatLngBounds(
            new LatLng(-40, -168), new LatLng(71, 136)
    );

    //vars
    private GoogleApiClient client;
    private LocationRequest locationRequest;
    private Location lastLocation;
    private Marker currentLocationMarker;
    private Boolean mLocationPermissionsGranted = false;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private PlaceAutocompleteAdapter mPlaceAutocompleteAdapter;

    //widgets
    private AutoCompleteTextView mSearchText;
    private FloatingActionButton mGps;
    //private ImageView mGps;
    private ImageView mIconSearch;
    private Button mClearText;
    public  DrawerLayout drawer;

    //Auto complete
    protected GeoDataClient mGeoDataClient;

    LinearLayout layoutBusList;
    BottomSheetBehavior sheetBehavior;
    FloatingActionButton mBtnBusFloat;
    TextView mCurrentAddress;
    TextView mNumberBusStations;

    //Maker for get Bus list around between two point
    private ArrayList<Marker> markerArray = new ArrayList<Marker>();

    // Array of strings...
    ListView busListView;
    ArrayList<BusStation> busStationList;
    ArrayList<Marker> busStationMarkers;
    /**
     * Callback for changes in location.
     */
    private LocationCallback mLocationCallback;

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
    public static AboutFragment newInstance(String param1, String param2) {
        AboutFragment fragment = new AboutFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_bus_maps);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_bus_maps, viewGroup, false);

        mSearchText = (AutoCompleteTextView) view.findViewById(R.id.input_search);
        mGps = (FloatingActionButton) view.findViewById(R.id.ic_gps);
        mIconSearch = (ImageView) view.findViewById(R.id.ic_magnify);
        /***Number on list view ***/
        mNumberBusStations = (TextView) view.findViewById(R.id.number_stations);
        busListView = (ListView) view.findViewById(R.id.bus_station_list_around);

        //View search place detail variable
        mCurrentAddress = (TextView) view.findViewById(R.id.current_address_text);

        //Initialize Google Play Services
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getLocationPermission();
        }

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        //SupportMapFragment mapFragment = (SupportMapFragment) (ActivityCompat)this.findFragmentById(R.id.map);
        MapFragment mapFragment = (MapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if(mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(super.getActivity());

        /* Handle menu navigation view show/hide */
        ImageView img_navibar = (ImageView)view.findViewById(R.id.ic_navibar);
        img_navibar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((HomeBusStationActivity)getActivity()).openNavigatioView();
            }
        });

        /* Event clear text button */
        mClearText = (Button) view.findViewById(R.id.btn_clear);
        mClearText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSearchText.setText("");
            }
        });
        /**
         * *************************************
         * BUS LIST INFO BOTTOM SHEET
         * *************************************
         */

        layoutBusList = (LinearLayout) view.findViewById(R.id.bus_list_info);
        sheetBehavior = BottomSheetBehavior.from(layoutBusList);
        /**
         * bottom sheet state change listener
         * we are changing button text when sheet changed state
         * */
        sheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                switch (newState) {
                    case BottomSheetBehavior.STATE_HIDDEN:
                        break;
                    case BottomSheetBehavior.STATE_EXPANDED: {
                        //Toast.makeText(BusMapsActivity.super.getActivity(), "Close Sheet", Toast.LENGTH_SHORT).show();
                    }
                    break;
                    case BottomSheetBehavior.STATE_COLLAPSED: {
                        //Toast.makeText(BusMapsActivity.super.getActivity(), "Close Sheet", Toast.LENGTH_SHORT).show();
                    }
                    break;
                    case BottomSheetBehavior.STATE_DRAGGING:
                        break;
                    case BottomSheetBehavior.STATE_SETTLING:
                        break;
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });
        /**
         * manually opening / closing bottom sheet on button click
         */
        layoutBusList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sheetBehavior.getState() != BottomSheetBehavior.STATE_EXPANDED) {
                    sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                } else {
                    sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                }
            }
        });
        mBtnBusFloat = (FloatingActionButton) view.findViewById(R.id.btn_bus_float);
        mBtnBusFloat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Float buttion click event!");
                if (sheetBehavior.getState() != BottomSheetBehavior.STATE_EXPANDED) {
                    sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                } else {
                    sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                }
            }
        });

        /* HIDE DIALOG LOADING*/
       // ((HomeBusStationActivity)getActivity()).hideDialog();

        return view;
    }

    private void initSearch() {
        // Register a listener that receives callbacks when a suggestion has been selected
        mSearchText.setOnItemClickListener(mAutocompleteClickListener);
        // Construct a GeoDataClient for the Google Places API for Android.
        mGeoDataClient = Places.getGeoDataClient(super.getActivity(), null);

        mPlaceAutocompleteAdapter = new PlaceAutocompleteAdapter(super.getActivity(), mGeoDataClient, LAT_LNG_BOUNDS, null);

        mSearchText.setAdapter(mPlaceAutocompleteAdapter);

        mIconSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                geoLocate();
            }
        });
        mSearchText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                if(actionId == EditorInfo.IME_ACTION_SEARCH
                    || actionId == EditorInfo.IME_ACTION_DONE
                    || event.getAction() == KeyEvent.ACTION_DOWN
                    || event.getAction() == KeyEvent.KEYCODE_ENTER){
                    //execute our method for searching
                    geoLocate();
                }
                return false;
            }
        });

        mGps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: click gps icon!");
                getDeviceLocation();
            }
        });

        hideSoftKeyboard();
    }

    private void geoLocate() {
        String searchString = mSearchText.getText().toString();
        Geocoder geocoder = new Geocoder(super.getActivity());
        List<Address> list = new ArrayList<>();

        try {
            list = geocoder.getFromLocationName(searchString, 1);
        } catch (IOException e) {
            Log.d(TAG, "geoLocate: IOexception: " + e.getMessage());
        }

        if (list.size() > 0) {
            Address address = list.get(0);
            Log.d(TAG, "geoLocate: found address" + address.toString());

            mCurrentAddress.setText(address.getLocality().toString());
            moveCamera(new LatLng(address.getLatitude(), address.getLongitude()), DEFAULT_ZOOM, address.getLocality());
        }
    }

    private void showCurrentAddress(LatLng latLng) {
        String searchString = mSearchText.getText().toString();
        Geocoder geocoder = new Geocoder(super.getActivity());
        List<Address> addresses = new ArrayList<>();

        try {
            addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
        } catch (IOException e) {
            Log.d(TAG, "showCurrentAddress: IOexception: " + e.getMessage());
        }
        // Handle case where no address was found.
        if (addresses == null || addresses.size() == 0) {
            Log.d(TAG, "showCurrentAddress: Get address string is fail");
        } else {
            Address address = addresses.get(0);
            ArrayList<String> addressFragments = new ArrayList<String>();

            // Fetch the address lines using getAddressLine,
            // join them, and send them to the thread.
            for (int i = 0; i <= address.getMaxAddressLineIndex(); i++) {
                addressFragments.add(address.getAddressLine(i));
            }

            mCurrentAddress.setText(TextUtils.join(", ",
                    addressFragments));
        }
    }

    private void getLocationPermission() {
        String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION};

        if (ContextCompat.checkSelfPermission(super.getActivity().getApplicationContext(),
                FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            if (ContextCompat.checkSelfPermission(super.getActivity().getApplicationContext(),
                    COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                mLocationPermissionsGranted = true;
            } else {
                ActivityCompat.requestPermissions(super.getActivity(),
                        permissions,
                        LOCATION_PERMISSION_REQUEST_CODE);
            }
        } else {
            ActivityCompat.requestPermissions(super.getActivity(),
                    permissions,
                    LOCATION_PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        Toast.makeText(super.getActivity(), "Map is already!", Toast.LENGTH_SHORT).show();
        mMap = googleMap;
        if (mLocationPermissionsGranted) {
            initSearch();
            buildGoogleApiClient();
            getDeviceLocation();
            if (ActivityCompat.checkSelfPermission(super.getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(super.getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            mMap.setMyLocationEnabled(true);
        }else{
            Log.d(TAG, "Location permission is denied!");
        }

        /* Add event touch or tap on map */
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {

            @Override
            public void onMapClick(LatLng point) {
                showMarkerForClickEvent(point);
            }
        });
    }

    private void showMarkerForClickEvent(LatLng point) {
        if (markerArray.size() >= 2) {
            for (int i = 0; i < markerArray.size(); i++) {
                markerArray.get(i).remove();
            }
            markerArray.clear();
            MarkerOptions marker = new MarkerOptions().position(
                    new LatLng(point.latitude, point.longitude)).title("First position").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));

            markerArray.add(0, mMap.addMarker(marker));

        } else if (markerArray.size() == 1) {
            MarkerOptions marker = new MarkerOptions().position(
                    new LatLng(point.latitude, point.longitude)).title("Second position").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
            markerArray.add(1, mMap.addMarker(marker));

            BusInfoApi busApi = new BusInfoApi();
            requestBusStationListApi(busApi.getUrlRequestBusStationInfo(markerArray));

        } else {
            MarkerOptions marker = new MarkerOptions().position(
                    new LatLng(point.latitude, point.longitude)).title("First position").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
            markerArray.add(0, mMap.addMarker(marker));
        }

        for (int i = 0; i < markerArray.size(); i++) {
            markerArray.get(i).showInfoWindow();
        }

    }

    public void requestBusStationListApi(String uri) {

        RequestQueue queue = Volley.newRequestQueue(super.getActivity());

        // prepare the Request
        JsonArrayRequest getRequest = new JsonArrayRequest(Request.Method.GET, uri, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                        //Reset bus Station list
                        resetBusStationList();
                        if (response.length() > 0) {

                            busStationList = new ArrayList<BusStation>();
                            busStationMarkers = new ArrayList<Marker>();
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

                                        busStationMarkers.add(mMap.addMarker(new MarkerOptions()
                                                .position(new LatLng(Double.parseDouble(obj.getString("Lat")), Double.parseDouble(obj.getString("Lng"))))
                                                .title(busStation.getName())
                                                .snippet("Routes: " + busStation.getRoutes())
                                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_bus_station_marker))));
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                            if (busStationList.size() > 0) {
                                CharSequence textNumberBusStation = "Have " + Integer.toString(busStationList.size()) + " bus stations around the position.";
                                mNumberBusStations.setText(textNumberBusStation);

                                BusStationAdapter arrayAdapter = new BusStationAdapter(BusMapsFragment.super.getActivity(), busStationList, currentLocationMarker.getPosition());
                                busListView.setAdapter(arrayAdapter);

                                busListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                        @SuppressWarnings("unchecked")
                                        BusStation objStation = (BusStation) parent.getItemAtPosition(position);
                                        /* Save Bus Station to DB*/
                                        //DaoSession daoSession = Applications.instance.getDaoSession();
                                        DaoSession daoSession = ((Applications)BusMapsFragment.super.getActivity().getApplication()).getDaoSession();
                                        BusStationModelDao busStationModelDao = daoSession.getBusStationModelDao();
                                        BusStationModel bst = daoSession.getBusStationModelDao().queryBuilder().where(BusStationModelDao.Properties.Id.eq(objStation.getStopId().longValue())).unique();
                                        if(bst == null) {
                                            bst = new BusStationModel(objStation.getStopId().longValue(), objStation.getName(), objStation.getStopType(), objStation.getAddressNo(), objStation.getStreet(), objStation.getRoutes(), objStation.getLatLng().latitude, objStation.getLatLng().longitude);
                                            busStationModelDao.insert(bst);
                                        }
                                        /* End save */
                                        Intent intent = new Intent(BusMapsFragment.super.getActivity(), BusStationDetailActivity.class);
                                        intent.putExtra("mStopId", objStation.getStopId());
                                        intent.putExtra("mCurrentLatlng", new double[]{currentLocationMarker.getPosition().latitude, currentLocationMarker.getPosition().longitude});
                                        intent.putExtra("mBusStationLatlng", new double[]{objStation.getLatLng().latitude, objStation.getLatLng().longitude});

                                        startActivity(intent);
                                    }
                                });

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

    // reset List View for bus station list
    private void resetBusStationList() {
        mNumberBusStations.setText("Can not find out bus station");

        //remove bus station marker
        if (busStationMarkers != null && busStationMarkers.size() > 0) {
            for (int i = 0; i < busStationMarkers.size(); i++) {
                busStationMarkers.get(i).remove();
            }
        }
        //reset list view
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(super.getActivity(), R.layout.bus_station_listview, R.id.bus_detail, new ArrayList<String>());
        busListView.setAdapter(arrayAdapter);
    }

    protected void buildGoogleApiClient() {
        client = new GoogleApiClient.Builder(super.getActivity())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
               // .enableAutoManage(this, this)
                .build();

        client.connect();
    }

    private void getDeviceLocation() {
        Log.d(TAG, "getDeviceLocation: getting the devices current location");
        try {

            Task location = mFusedLocationProviderClient.getLastLocation();
            location.addOnFailureListener(super.getActivity(), new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.d(TAG, "get device location fail: " + e.getMessage());
                }
            });
            location.addOnCompleteListener(super.getActivity(), new OnCompleteListener<Location>() {
                @Override
                public void onComplete(@NonNull Task<Location> task) {
                    if (task.isSuccessful() && task.getResult() != null) {
                        Location currentLocation = task.getResult();
                        moveCamera(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()), DEFAULT_ZOOM, "Your location");

                        ((HomeBusStationActivity)getActivity()).setCurrentLocation(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()));

                        showCurrentAddress(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()));
                    } else {
                        mCurrentAddress.setText("Unable define your location!");
                    }
                }
            });
        } catch (SecurityException e) {
            Log.d(TAG, "getDeviceLocation: Security Exception: " + e.getMessage());
        }
    }

    private void moveCamera(LatLng latlng, float zoom, String title) {
        Log.d(TAG, "Move camera to location: " + latlng.latitude + ", " + latlng.longitude);

        if (currentLocationMarker != null) {
            currentLocationMarker.remove();
        }
        currentLocationMarker = mMap.addMarker(new MarkerOptions().position(latlng).title(title).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latlng, DEFAULT_ZOOM));

        // Zoom in, animating the camera.
        mMap.animateCamera(CameraUpdateFactory.zoomIn());

        // Zoom out to zoom level 10, animating with a duration of 2 seconds.
        mMap.animateCamera(CameraUpdateFactory.zoomTo(10), 2000, null);

        // Construct a CameraPosition focusing on Mountain View and animate the camera to that position.
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(latlng)      // Sets the center of the map to Mountain View
                .zoom(17)                   // Sets the zoom
                .bearing(90)                // Sets the orientation of the camera to east
                .tilt(30)                   // Sets the tilt of the camera to 30 degrees
                .build();                   // Creates a CameraPosition from the builder
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

        // drawCircle(latlng);

        /* request get bustation */
        BusInfoApi busApi = new BusInfoApi();
        LatLngBounds latLngBounds = toBounds(new LatLng(currentLocationMarker.getPosition().latitude, currentLocationMarker.getPosition().longitude), 1000.0);
        requestBusStationListApi(busApi.getUrlRequestBusStationInfoByBounds(latLngBounds));

        //drawBounds(latLngBounds, Color.RED);
        hideSoftKeyboard();
    }

    private void drawBounds(LatLngBounds bounds, int color) {
        PolygonOptions polygonOptions = new PolygonOptions()
                .add(new LatLng(bounds.northeast.latitude, bounds.northeast.longitude))
                .add(new LatLng(bounds.southwest.latitude, bounds.northeast.longitude))
                .add(new LatLng(bounds.southwest.latitude, bounds.southwest.longitude))
                .add(new LatLng(bounds.northeast.latitude, bounds.southwest.longitude))
                .strokeColor(color);
        mMap.addMarker(new MarkerOptions().position(new LatLng(bounds.northeast.latitude, bounds.northeast.longitude)).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW)));
        mMap.addMarker(new MarkerOptions().position(new LatLng(bounds.southwest.latitude, bounds.southwest.longitude)).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW)));
        mMap.addPolygon(polygonOptions);
    }

    private Circle drawCircle(LatLng latLng) {

        CircleOptions options = new CircleOptions()
                .center(latLng)
                .radius(500)
                .fillColor(Color.parseColor("#500084d3"))
                .strokeColor(Color.parseColor("#50007fd3"))
                .strokeWidth(1);

        return mMap.addCircle(options);
    }

    public LatLngBounds toBounds(LatLng center, double radiusInMeters) {
        double distanceFromCenterToCorner = radiusInMeters * Math.sqrt(2.0);
        LatLng southwestCorner =
                SphericalUtil.computeOffset(center, distanceFromCenterToCorner, 225.0);
        LatLng northeastCorner =
                SphericalUtil.computeOffset(center, distanceFromCenterToCorner, 45.0);
        return new LatLngBounds(southwestCorner, northeastCorner);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Log.d(TAG, "onRequest PermissionsResult: called.");
        mLocationPermissionsGranted = false;

        switch (requestCode) {
            case LOCATION_PERMISSION_REQUEST_CODE: {
                if (grantResults.length > 0) {
                    for (int i = 0; i < grantResults.length; i++) {
                        if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                            mLocationPermissionsGranted = false;
                            Log.d(TAG, "onRequestPermissionsResult: permission failed");
                            return;
                        }
                    }
                    mLocationPermissionsGranted = true;
                    if (mMap == null) {
                        buildGoogleApiClient();
                    }
                    if (ActivityCompat.checkSelfPermission(super.getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(super.getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        return;
                    }
                    mMap.setMyLocationEnabled(true);
                }
            }
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        lastLocation = location;

        //Place current location marker
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());

        ((HomeBusStationActivity)getActivity()).setCurrentLocation(latLng);

        moveCamera(latLng, DEFAULT_ZOOM, "Current position");

        if(client != null)
        {
            mFusedLocationProviderClient.removeLocationUpdates(locationCallback);
        }
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle){}

    @Override
    public void onProviderEnabled(String provider) {}

    @Override
    public void onProviderDisabled(String provider) {}

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        locationRequest = new LocationRequest();

        locationRequest.setInterval(1000);
        locationRequest.setFastestInterval(1000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        if(ContextCompat.checkSelfPermission(super.getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            mFusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, null);
        }
    }
    private LocationCallback locationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            super.onLocationResult(locationResult);
            Location lastLocation = locationResult.getLastLocation();
            onLocationChanged(lastLocation);
        }
    };

    @Override
    public void onConnectionSuspended(int i) {}

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {}

    private void hideSoftKeyboard(){
        //this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        InputMethodManager imm = (InputMethodManager)super.getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(mSearchText.getWindowToken(), 0);
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

            Toast.makeText(BusMapsFragment.super.getActivity(), "Clicked: " + primaryText,
                    Toast.LENGTH_SHORT).show();
            Log.i(TAG, "Called getPlaceById to get Place details for " + placeId);
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

                // Format details of the place for display and show it in a TextView.
                mCurrentAddress.setText(formatPlaceDetails(getResources(), place.getName(),
                        place.getId(), place.getAddress(), place.getPhoneNumber(),
                        place.getWebsiteUri()));
                //geoLocate();
                moveCamera(new LatLng(place.getLatLng().latitude, place.getLatLng().longitude),DEFAULT_ZOOM, place.getName().toString());
                // Display the third party attributions if set.
                /*final CharSequence thirdPartyAttribution = places.getAttributions();
                if (thirdPartyAttribution == null) {
                    mPlaceDetailsAttribution.setVisibility(View.GONE);
                } else {
                    mPlaceDetailsAttribution.setVisibility(View.VISIBLE);
                    mPlaceDetailsAttribution.setText(
                            Html.fromHtml(thirdPartyAttribution.toString()));
                }*/

                Log.i(TAG, "Place details received: " + place.getName());

                places.release();
            } catch (RuntimeRemoteException e) {
                // Request did not complete successfully
                Log.e(TAG, "Place query did not complete.", e);
                return;
            }
        }
    };

    private static Spanned formatPlaceDetails(Resources res, CharSequence name, String id,
                                              CharSequence address, CharSequence phoneNumber, Uri websiteUri) {
        return Html.fromHtml(res.getString(R.string.place_details, address));

    }
}
