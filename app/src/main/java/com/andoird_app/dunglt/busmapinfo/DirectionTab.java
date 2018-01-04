package com.andoird_app.dunglt.busmapinfo;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.andoird_app.dunglt.busmapinfo.models.BusStationDetail;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link DirectionTab.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link DirectionTab#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DirectionTab extends Fragment implements OnMapReadyCallback {

    final static String TAG = "DirectionTab";
    private GoogleMap mMap;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_BUS_STATION_INFO = "mBusStationInfo";
    private static final String ARG_CURRENT_LATLNG = "mCurrentLatlng";
    private static final String ARG_BUS_STATION_LATLNG = "mBusStationLatlng";

    // TODO: Rename and change types of parameters
    private ArrayList<BusStationDetail> mBusStationInfo;
    private double[] mCurrentLatlng;
    private double[] mBusStationLatlng;

    private OnFragmentInteractionListener mListener;

    public DirectionTab() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DirectionTab.
     */
    // TODO: Rename and change types and number of parameters
    public static DirectionTab newInstance(ArrayList<BusStationDetail> param0, double[] param1, double[] param2) {
        DirectionTab fragment = new DirectionTab();
        Bundle args = new Bundle();
        args.putSerializable(ARG_BUS_STATION_INFO, param0);
        args.putDoubleArray(ARG_CURRENT_LATLNG, param1);
        args.putDoubleArray(ARG_BUS_STATION_LATLNG, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mBusStationInfo =(ArrayList<BusStationDetail>)getArguments().getSerializable(ARG_BUS_STATION_INFO);
            mCurrentLatlng = getArguments().getDoubleArray(ARG_CURRENT_LATLNG);
            mBusStationLatlng = getArguments().getDoubleArray(ARG_BUS_STATION_LATLNG);

            Log.d(TAG, "Latlng Data: " + Double.toString(mCurrentLatlng[0])+ ", "+Double.toString(mCurrentLatlng[1]));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_direction_tab, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        final SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.bus_detail_map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        } else {
            Log.d("DirectionTab", "mapFragment is null");
            Toast.makeText(super.getContext(), "mapFragment is null", Toast.LENGTH_SHORT).show();
        }

    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        // Add a marker in Sydney and move the camera
        LatLng currentLatlng = new LatLng(mCurrentLatlng[0], mCurrentLatlng[1]);
        mMap.addMarker(new MarkerOptions().position(currentLatlng).title("Current Location").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ROSE)));

        LatLng busStationLatlng = new LatLng(mBusStationLatlng[0], mBusStationLatlng[1]);
        mMap.addMarker(new MarkerOptions().position(busStationLatlng).title("Bus Station Location").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET)));

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatlng, 10));

        mMap.animateCamera(CameraUpdateFactory.zoomTo(15.0f));
        mMap.getUiSettings().setCompassEnabled(true);
        mMap.getUiSettings().setZoomControlsEnabled(true);

        if (ActivityCompat.checkSelfPermission(super.getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(super.getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
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

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
