package com.andoird_app.dunglt.busmapinfo;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.andoird_app.dunglt.busmapinfo.models.Bus;
import com.andoird_app.dunglt.busmapinfo.models.BusStation;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link BusListTab.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link BusListTab#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BusListTab extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_STOPID = "mStopId";
    //private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private Integer mStopId;
  //  private String mParam2;

    private OnFragmentInteractionListener mListener;

    //params for list bus view
    private final String authKey = "33044a4fc0d44b7ba4441a0f09c60381";
    final  static String  TAG = "BusListTab";
    ListView busListView;
    ArrayList<Bus> busList;

    public BusListTab() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @return A new instance of fragment BusListTab.
     */
    // TODO: Rename and change types and number of parameters
    public static BusListTab newInstance(Integer param1) {
        BusListTab fragment = new BusListTab();
        Bundle args = new Bundle();
        args.putInt(ARG_STOPID, param1);
       // args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        Log.d(TAG,"Tab 1 params: " + Integer.toString(param1));
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mStopId = getArguments().getInt(ARG_STOPID);
          //  mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bus_list_tab, container, false);
        busListView = (ListView) view.findViewById(R.id.bus_list);
        BusInfoApi busApi = new BusInfoApi();
        Log.d(TAG, "stopId: " + Integer.toString(mStopId));
        requestBusListApi(busApi.getUrlRequestBusList(mStopId));
        // Inflate the layout for this fragment
        return view;
    }

    // Request bus list API
    public void requestBusListApi(String uri){
        RequestQueue queue = Volley.newRequestQueue(super.getContext());
        Log.d(TAG, "Call API");
        // prepare the Request
        JsonArrayRequest getRequest = new JsonArrayRequest(Request.Method.GET, uri, null,
                new Response.Listener<JSONArray>()
                {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d(TAG, "API RESPONSE");
                        // display response
                        if(response.length() > 0) {
                            busList = new ArrayList<Bus>();
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject obj = null;
                                try {
                                    obj = response.getJSONObject(i);

                                    Log.d(TAG, "DATA API: "+ obj.getJSONArray("arrs").toString());
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
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                            if(busList.size() > 0) {
                                BusAdapter arrayAdapter = new BusAdapter(BusListTab.super.getContext(), busList);
                                busListView.setAdapter(arrayAdapter);

                                busListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                                    }
                                });
                            }
                        }
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
