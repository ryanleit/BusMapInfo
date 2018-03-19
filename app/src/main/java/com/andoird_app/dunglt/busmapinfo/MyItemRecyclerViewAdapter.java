package com.andoird_app.dunglt.busmapinfo;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.andoird_app.dunglt.busmapinfo.HistoryFragment.OnListFragmentInteractionListener;
import com.andoird_app.dunglt.busmapinfo.models.BusStationModel;
import com.andoird_app.dunglt.busmapinfo.models.BusStationModelDao;
import com.andoird_app.dunglt.busmapinfo.models.BusStationTable;
import com.andoird_app.dunglt.busmapinfo.models.DaoSession;
import com.google.android.gms.maps.model.LatLng;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link BusStationTable} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class MyItemRecyclerViewAdapter extends RecyclerView.Adapter<MyItemRecyclerViewAdapter.ViewHolder> {

    public Context context;
    private final List<BusStationModel> mValues;
    private final OnListFragmentInteractionListener mListener;
    public DaoSession daoSession;
    public LatLng currentLatlng;

    public MyItemRecyclerViewAdapter(Context cont, List<BusStationModel> items, OnListFragmentInteractionListener listener, DaoSession daoSess, LatLng current) {
        context = cont;
        mValues = items;
        mListener = listener;
        daoSession =  daoSess;
        currentLatlng = current;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.mItem = mValues.get(position);
        holder.mIdView.setText(mValues.get(position).getName());

        holder.mDelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);

                builder.setMessage("Are you sure?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Long stopId = mValues.get(position).getId();
                        daoSession.getBusStationModelDao().queryBuilder().where(BusStationModelDao.Properties.Id.eq(stopId)).buildDelete().executeDeleteWithoutDetachingEntities();

                        removeAt(position);
                    }
                })
                .setNegativeButton("No", null)
                .show();
            }
        });

        holder.mDetailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Long id = mValues.get(position).getId();
                 /* End save */
                Intent intent = new Intent(context, BusStationDetailActivity.class);
                intent.putExtra("mStopId", mValues.get(position).getId());
                intent.putExtra("mCurrentLatlng", new double[]{currentLatlng.latitude, currentLatlng.longitude});
                intent.putExtra("mBusStationLatlng", new double[]{mValues.get(position).getLat(), mValues.get(position).getLng()});

                context.startActivity(intent);
            }
        });

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(holder.mItem);
                }
            }
        });
    }
    public void removeAt(int position){
        mValues.remove(position);
        notifyItemChanged(position);
        notifyDataSetChanged();

    }
    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mIdView;
        public final Button mDetailButton;
        public final Button mDelButton;
        public BusStationModel mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mIdView = (TextView) view.findViewById(R.id.title);
            mDetailButton = (Button) view.findViewById(R.id.btn_station_detail);
            mDelButton = (Button) view.findViewById(R.id.btn_station_delete);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mIdView.getText() + "'";
        }
    }
}
