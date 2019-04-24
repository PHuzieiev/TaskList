package com.apps.newstudio.tasklist.data.managers;


public class DataManager {

    private static DataManager sInstance = null;
    private PreferenceManager mPreferenceManager;
    private DatabaseManager mDatabaseManager;


    private DataManager() {
        mPreferenceManager = new PreferenceManager();
        mDatabaseManager = new DatabaseManager();
    }

    public static DataManager getInstance() {
        if (sInstance == null)
            sInstance = new DataManager();
        return sInstance;
    }

    public PreferenceManager getPreferenceManager() {
        return mPreferenceManager;
    }


    public DatabaseManager getDatabaseManager() {
        return mDatabaseManager;
    }
}
