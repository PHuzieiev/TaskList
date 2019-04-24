package com.apps.newstudio.tasklist.utils;

public interface ConstantsManager {

    String EMPTY_STRING_VALUE = "";
    String TEG = "TEG FROM ";
    String LANGUAGE_KEY = "LANGUAGE_KEY", LANGUAGE_ENG = "ENG", LANGUAGE_RUS = "RUS", LANGUAGE_UKR = "UKR";
    String FIRST_DAY_KEY = "FIRST_DAY_KEY", FIRST_DAY_MONDAY = "FIRST_DAY_MONDAY", FIRST_DAY_SUNDAY = "FIRST_DAY_SUNDAY";
    String LANGUAGE_CHOSEN_KEY = "LANGUAGE_CHOSEN_KEY";
    String LANGUAGE_CHOSEN = "LANGUAGE_CHOSEN";

    String SAVED_FRAGMENT_ID = "SAVED_FRAGMENT_ID";
    String TIME_STYLE_AM = "AM", TIME_STYLE_PM = "PM";

    int CATEGORIES_NONE = 0;
    int CATEGORIES_FAMILY = 1;
    int CATEGORIES_MEETINGS = 2;
    int CATEGORIES_HONE = 3;
    int CATEGORIES_SHOPPING = 4;
    int CATEGORIES_BIRTHDAY = 5;
    int CATEGORIES_BUSINESS = 6;
    int CATEGORIES_CALLS = 7;
    int CATEGORIES_ENTERTAINMENTS = 8;
    int CATEGORIES_EDUCATION = 9;
    int CATEGORIES_PETS = 10;
    int CATEGORIES_PRIVATE_LIFE = 11;
    int CATEGORIES_REPAIR = 12;
    int CATEGORIES_WORK = 13;
    int CATEGORIES_SPORT = 14;
    int CATEGORIES_TRAVELLINGS = 15;

    String TASK_ID_FLAG = "TASK_ACTIVITY_ID_FLAG";
    Long TASK_ID_NONE = -1L;
    String TASK_STATE_FLAG = "TASK_STATE_FLAG";
    int STATE_FLAG_ACTIVE = 0, STATE_FLAG_DONE = 1;
    String TASK_TITLE_FLAG = "TASK_TITLE_FLAG", TASK_CATEGORY_FLAG = "TASK_CATEGORY_FLAG";
    String TASK_DAY_FLAG = "TASK_DAY_FLAG", TASK_MONTH_FLAG = "TASK_MONTH_FLAG", TASK_YEAR_FLAG = "TASK_YEAR_FLAG";
    String TASK_HOUR_BEGINNING_FLAG = "TASK_HOUR_BEGINNING_FLAG", TASK_MINUTE_BEGINNING_FLAG = "TASK_MINUTE_BEGINNING_FLAG";
    String TASK_HOUR_END_FLAG = "TASK_HOUR_END_FLAG", TASK_MINUTE_END_FLAG = "TASK_MINUTE_END_FLAG";
    String ALARM_FLAG = "ALARM_FLAG", ALARM_HOUR_FLAG = "ALARM_HOUR_FLAG", ALARM_MINUTE_FLAG = "ALARM_MINUTE_FLAG";
    int ALARM_FLAG_ON = 1, ALARM_FLAG_OFF = 0;

    int TASK_REQUEST_CODE = 1;

    String CHOSEN_DAY_KEY="CHOSEN_DAY_KEY";
    String CHOSEN_MONTH_KEY="CHOSEN_MONTH_KEY";
    String CHOSEN_YEAR_KEY="CHOSEN_YEAR_KEY";

}
