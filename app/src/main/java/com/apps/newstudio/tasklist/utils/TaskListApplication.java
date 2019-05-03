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

    /**
     * Create TaskListApplication object
     */
    @Override
    public void onCreate() {
        super.onCreate();
        sContext = this;
        sSharedPreferences = getSharedPreferences(getString(R.string.app_name), MODE_PRIVATE);
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, "task-db");
        Database database = helper.getWritableDb();
        sDaoSession = new DaoMaster(database).newSession();
    }

    /**
     * Getter for Context object of application
     *
     * @return Context object
     */
    public static Context getContext() {
        return sContext;
    }

    /**
     * Getter for main SharedPreferences object
     *
     * @return SharedPreferences object
     */
    public static SharedPreferences getSharedPreferences() {
        return sSharedPreferences;
    }

    /**
     * Getter for main DaoSession object
     *
     * @return DaoSession object
     */
    public static DaoSession getDaoSession() {
        return sDaoSession;
    }
}
