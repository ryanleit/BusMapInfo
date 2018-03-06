package com.andoird_app.dunglt.busmapinfo.dummy;

import com.andoird_app.dunglt.busmapinfo.Applications;
import com.andoird_app.dunglt.busmapinfo.models.BusStationModel;
import com.andoird_app.dunglt.busmapinfo.models.BusStationTable;
import com.andoird_app.dunglt.busmapinfo.models.DaoSession;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.lang.System.in;

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 * <p>
 * TODO: Replace all uses of this class before publishing your app.
 */
public class HistoryContent {

    /**
     * An array of sample (dummy) items.
     */
    public static final List<BusStationModel> ITEMS = new ArrayList<BusStationModel>();

    public HistoryContent(DaoSession daoSession){
        getBusStationListDb(daoSession);
    }
    /**
     * A map of sample (dummy) items, by ID.
     */
    public static final Map<Long, BusStationModel> ITEM_MAP = new HashMap<Long, BusStationModel>();

    private static final int COUNT = 25;

    private static void addItem(BusStationModel item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.getId(), item);
    }

    public void getBusStationListDb(DaoSession daoSession){
        //DaoSession daoSession = Applications.instance.getDaoSession();

        List<BusStationModel> nineteens = daoSession.getBusStationModelDao().queryBuilder().list();


        for(int i = 0; i < nineteens.size(); i++){
            addItem(nineteens.get(i));
        }
    }
}
