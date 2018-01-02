package com.andoird_app.dunglt.busmapinfo;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by dunglt on 1/2/2018.
 */

public class PagerAdapter extends FragmentPagerAdapter {

    Integer mNoOfTabs;

    public PagerAdapter(FragmentManager fm, Integer NumberOfTabs) {
        super(fm);
        this.mNoOfTabs = NumberOfTabs;
    }

    @Override
    public Fragment getItem(int position) {
        Log.d("getItem", "position of item is: "+ Integer.toString(position));
        switch (position){
            case 0:
                BusListTab busListTab = new BusListTab();
                return busListTab;

            case 1:
                DirectionTab directionTab = new DirectionTab();
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
