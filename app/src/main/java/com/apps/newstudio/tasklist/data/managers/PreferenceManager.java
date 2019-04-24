package com.apps.newstudio.tasklist.data.managers;

import android.content.SharedPreferences;

import com.apps.newstudio.tasklist.utils.ConstantsManager;
import com.apps.newstudio.tasklist.utils.TaskListApplication;

public class PreferenceManager {
    private SharedPreferences mSharedPreferences;

    public PreferenceManager() {
        mSharedPreferences = TaskListApplication.getSharedPreferences();
    }

    /**
     * Saves String value using mSharedPreferences object
     *
     * @param key   is String identificator which is used to save value
     * @param value is String object which will be saved
     */
    public void saveString(String key, String value) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(key, value);
        editor.apply();
    }

    /**
     * Loads String value from mSharedPreferences
     *
     * @param key          is String identificator which was used to save value
     * @param defaultValue is String value which will be returned if function do not find result with this key
     * @return String object
     */
    public String loadString(String key, String defaultValue) {
        return mSharedPreferences.getString(key, defaultValue);
    }


    /**
     * Loads String value which is used to define App Language
     *
     * @return String object
     */
    public String getLanguage() {
        return loadString(ConstantsManager.LANGUAGE_KEY, ConstantsManager.LANGUAGE_ENG);
    }

    /**
     * Saves String value which is used to define App Language
     *
     * @param parameter String object which defines App Language
     */
    public void setLanguage(String parameter){
        saveString(ConstantsManager.LANGUAGE_KEY,parameter);
    }

    /**
     * Loads String value which is used to define Calendar first weekday
     *
     * @return String object
     */
    public String getFirstDay() {
        return loadString(ConstantsManager.FIRST_DAY_KEY, ConstantsManager.FIRST_DAY_SUNDAY);
    }

    /**
     * Saves String value which is used to define Calendar first weekday
     *
     * @param parameter String object which defines Calendar first weekday
     */
    public void setFirstDay(String parameter){
        saveString(ConstantsManager.FIRST_DAY_KEY,parameter);
    }
}
