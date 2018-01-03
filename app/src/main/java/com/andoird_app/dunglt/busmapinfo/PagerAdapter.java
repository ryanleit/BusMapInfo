package com.andoird_app.dunglt.busmapinfo;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by dunglt on 1/2/2018.
 */

public class PagerAdapter extends FragmentPagerAdapter {

    Integer mNoOfTabs, mStopId;
    double[] mCurrentLatlng, mBusStationLatlng;

    public PagerAdapter(FragmentManager fm, Integer NumberOfTabs, Integer stopId, double[] currentLatlng, double[] busStationLatlng) {
        super(fm);
        this.mNoOfTabs = NumberOfTabs;
        this.mStopId = stopId;
        this.mCurrentLatlng = currentLatlng;
        this.mBusStationLatlng = busStationLatlng;
    }

    @Override
    public Fragment getItem(int position) {
        Log.d("getItem", "position of item is: "+ Integer.toString(position));
        switch (position){
            case 0:
                BusListTab busListTab = BusListTab.newInstance(mStopId);
                return busListTab;

            case 1:
                DirectionTab directionTab = DirectionTab.newInstance(mCurrentLatlng, mBusStationLatlng);
                return directionTab;

            default:
                return null;
        }

    }

    @Override
    public int getCount() {
       return mNoOfTabs;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        String title = null;
        if (position == 0)
        {
            title = "Bus List";
        }
        else if (position == 1)
        {
            title = "Direction";
        }

        return title;
    }
}
