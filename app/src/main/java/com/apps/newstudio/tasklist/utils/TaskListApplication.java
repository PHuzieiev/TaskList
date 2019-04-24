package com.apps.newstudio.tasklist.utils;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import com.apps.newstudio.tasklist.R;
import com.apps.newstudio.tasklist.data.storage.models.DaoMaster;
import com.apps.newstudio.tasklist.data.storage.models.DaoSession;

import org.greenrobot.greendao.database.Database;

public class TaskListApplication extends Application {

    private static Context sContext;
    private static SharedPreferences sSharedPreferences;
    private static DaoSession sDaoSession;

    @Override
    public void onCreate() {
        super.onCreate();
        sContext = this;
        sSharedPreferences = getSharedPreferences(getString(R.string.app_name), MODE_PRIVATE);
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, "task-db");
        Database database = helper.getWritableDb();
        sDaoSession = new DaoMaster(database).newSession();
    }

    public static Context getContext() {
        return sContext;
    }

    public static SharedPreferences getSharedPreferences() {
        return sSharedPreferences;
    }

    public static DaoSession getDaoSession() {
        return sDaoSession;
    }
}
