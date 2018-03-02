package com.andoird_app.dunglt.busmapinfo;

import android.app.Application;

import com.andoird_app.dunglt.busmapinfo.models.DaoMaster;
import com.andoird_app.dunglt.busmapinfo.models.DaoSession;

import org.greenrobot.greendao.database.Database;

/**
 * Created by dunglt on 3/2/2018.
 */

public class Applications extends Application {
    private DaoSession mDaoSession;

    public static Applications instance = new Applications();

    @Override
    public void onCreate() {
        super.onCreate();
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, "bus-map-db");
        Database db = helper.getWritableDb();
        mDaoSession = new DaoMaster(db).newSession();
    }

    public DaoSession getDaoSession() {
        return mDaoSession;
    }
}