package com.akkaratanapat.altear.vrp_onboard.Utility;

/**
 * Created by altear on 8/16/2017.
 */

import android.app.Application;
import android.database.sqlite.SQLiteDatabase;

import com.akkaratanapat.altear.daogenerator.DaoMaster;
import com.akkaratanapat.altear.daogenerator.DaoSession;

public class GreenDaoApplication extends Application {

    public static final boolean ENCRYPTED = true;
    private DaoSession daoSession;


    @Override
    public void onCreate() {
        super.onCreate();
        setupDatabase();
    }

    private void setupDatabase() {
        DaoMaster.DevOpenHelper helper =
                new DaoMaster.DevOpenHelper(this, "APIFailure.db", null);
        SQLiteDatabase db = helper.getWritableDatabase();
        DaoMaster daoMaster = new DaoMaster(db);
        daoSession = daoMaster.newSession();
    }

    public DaoSession getDaoSession() {
        return daoSession;
    }
}