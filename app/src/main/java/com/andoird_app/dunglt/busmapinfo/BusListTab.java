package com.andoird_app.dunglt.busmapinfo;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.andoird_app.dunglt.busmapinfo.models.Bus;
import com.andoird_app.dunglt.busmapinfo.models.BusStationDetail;
import com.andoird_app.dunglt.busmapinfo.models.BusStationInfo;

import java.util.ArrayList;


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
    private static final String ARG_BUS_STATION_INFO = "mBusStationInfo";
    //private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private ArrayList<BusStationDetail> mBusStationInfo;
  //  private String mParam2;

    private OnFragmentInteractionListener mListener;

    //params for list bus view
    private final String authKey = "33044a4fc0d44b7ba4441a0f09c60381";
    final  static String  TAG = "BusListTab";
    ListView busListView;

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
    public static BusListTab newInstance(ArrayList<BusStationDetail> param1) {
        BusListTab fragment = new BusListTab();
        Bundle args = new Bundle();

        args.putSerializable(ARG_BUS_STATION_INFO, param1);
       // args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mBusStationInfo =(ArrayList<BusStationDetail>)getArguments().getSerializable(ARG_BUS_STATION_INFO);
            Log.d(TAG,"mBusStation info: "+ mBusStationInfo.toString());
          //  mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bus_list_tab, container, false);
        busListView = (ListView) view.findViewById(R.id.bus_list);

        prepareLayoutView();
        // Inflate the layout for this fragment
        return view;
    }
    public void prepareLayoutView(){
        if(mBusStationInfo.size() > 0) {
            ArrayList<Object> data = prepareDataForListView(mBusStationInfo);
            BusAdapter arrayAdapter = new BusAdapter(BusListTab.super.getContext(), data);
            busListView.setAdapter(arrayAdapter);

            busListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                }
            });
        }
    }

    public ArrayList<Object> prepareDataForListView(ArrayList<BusStationDetail> busStationDetail){
        ArrayList<Object> data = new ArrayList<Object>();

        if(busStationDetail.size() >0){
            for(int i =0; i < busStationDetail.size();i++){
                BusStationInfo bsi = new BusStationInfo(
                            busStationDetail.get(i).getR(),
                            busStationDetail.get(i).getS(),
                            busStationDetail.get(i).getV(),
                            busStationDetail.get(i).getRN(),
                            busStationDetail.get(i).getRNo(),
                            busStationDetail.get(i).getSN(),
                            busStationDetail.get(i).getVN());

                data.add(bsi);

                ArrayList<Bus> dataBusList = busStationDetail.get(i).getBusList();
                for (int j=0; j< dataBusList.size(); j++){
                    data.add(dataBusList.get(j));
                }
            }
        }
        Log.d(TAG, "prepare buslisttab: " + data.toString());
        return data;
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
