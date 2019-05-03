package com.apps.newstudio.tasklist.data.managers;


public class DataManager {

    private static DataManager sInstance = null;
    private PreferenceManager mPreferenceManager;
    private DatabaseManager mDatabaseManager;


    /**
     * Constructor of DataManager object
     */
    private DataManager() {
        mPreferenceManager = new PreferenceManager();
        mDatabaseManager = new DatabaseManager();
    }

    /**
     * Getter for DataManager object which returns existed object of creates new object
     *
     * @return DataManager object
     */
    public static DataManager getInstance() {
        if (sInstance == null)
            sInstance = new DataManager();
        return sInstance;
    }

    /**
     * Getter for PreferenceManager object
     *
     * @return PreferenceManager object
     */
    public PreferenceManager getPreferenceManager() {
        return mPreferenceManager;
    }


    /**
     * Getter for DatabaseManager object
     *
     * @return DatabaseManager object
     */
    public DatabaseManager getDatabaseManager() {
        return mDatabaseManager;
    }
}
