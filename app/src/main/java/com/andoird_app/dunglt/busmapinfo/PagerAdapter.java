package com.andoird_app.dunglt.busmapinfo;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.andoird_app.dunglt.busmapinfo.models.BusStationDetail;

import java.util.ArrayList;

/**
 * Created by dunglt on 1/2/2018.
 */

public class PagerAdapter extends FragmentPagerAdapter {

    Integer mNoOfTabs;
    double[] mCurrentLatlng, mBusStationLatlng;
    ArrayList<BusStationDetail> mBusStationInfo;

    public PagerAdapter(FragmentManager fm, Integer NumberOfTabs, ArrayList<BusStationDetail> busStationInfo, double[] currentLatlng, double[] busStationLatlng) {
        super(fm);
        this.mNoOfTabs = NumberOfTabs;
        this.mBusStationInfo = busStationInfo;
        this.mCurrentLatlng = currentLatlng;
        this.mBusStationLatlng = busStationLatlng;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position){
            case 0:
                BusListTab busListTab = BusListTab.newInstance(mBusStationInfo);
                return busListTab;

            case 1:
                DirectionTab directionTab = DirectionTab.newInstance(mBusStationInfo, mCurrentLatlng, mBusStationLatlng);
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
