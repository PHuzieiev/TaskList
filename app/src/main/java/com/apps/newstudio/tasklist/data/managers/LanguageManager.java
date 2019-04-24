package com.apps.newstudio.tasklist.data.managers;

import com.apps.newstudio.tasklist.utils.ConstantsManager;

public abstract class LanguageManager {
    /**
     * Abstract method where you must initialize your English String objects
     */
    public abstract void engLanguage();
    /**
     * Abstract method where you must initialize your Ukrainian String objects
     */
    public abstract void ukrLanguage();
    /**
     * Abstract method where you must initialize your Russian String objects
     */
    public abstract void rusLanguage();


    /**
     * Constructor for abstract class LanguageManager, makes main methods
     */
    public LanguageManager() {
        switch (DataManager.getInstance().getPreferenceManager().getLanguage()){
            case ConstantsManager.LANGUAGE_ENG:
                engLanguage();
                break;
            case ConstantsManager.LANGUAGE_RUS:
                rusLanguage();
                break;
            case ConstantsManager.LANGUAGE_UKR:
                ukrLanguage();
                break;
        }
    }
}
