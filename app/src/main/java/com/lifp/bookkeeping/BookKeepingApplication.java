package com.lifp.bookkeeping;

import android.app.Application;
import android.content.Context;

import com.lifp.bookkeeping.database.DataBaseHelper;

public class BookKeepingApplication extends Application {

    private static Context sContext;
    private static DataBaseHelper sDataBaseHelper;

    @Override
    public void onCreate() {
        super.onCreate();
        sContext = this;
        sDataBaseHelper = new DataBaseHelper(sContext);
    }

    public static Context getContext() {
        return sContext;
    }

    public static DataBaseHelper getDataBaseHelper() {
        return sDataBaseHelper;
    }

}