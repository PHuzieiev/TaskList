package com.apps.newstudio.tasklist.utils;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;

import com.apps.newstudio.tasklist.data.adapters.DataForTasksListItem;
import com.apps.newstudio.tasklist.data.managers.DataManager;
import com.apps.newstudio.tasklist.ui.widgets.TaskListWidget;

import java.util.Locale;

public class AuxiliaryFunctions {

    /**
     * Converts time to needed time format
     *
     * @param itemData                     DataForTasksListItem object
     * @param yesterdayTodayTomorrowTitles titles of previous today and next day
     * @param yesterdayTodayTomorrowMask   masks of previous today and next day
     * @param isShortFormOfTime            form of time
     * @return converted String object of time
     */
    public static String convertToTimePeriod(DataForTasksListItem itemData,
                                             String[] yesterdayTodayTomorrowTitles, String[] yesterdayTodayTomorrowMask,
                                             boolean isShortFormOfTime) {

        int beginningHours = itemData.getBeginningHours(), beginningMinutes = itemData.getBeginningMinutes(),
                endHours = itemData.getEndHours(), endMinutes = itemData.getEndMinutes(),
                day = itemData.getDay(), month = itemData.getMonth(), year = itemData.getYear();

        String dayOrMonthFormat = "%02d";
        String beginningTimeSuffix = "";
        String endTimeSuffix = "";
        if (DataManager.getInstance().getPreferenceManager().getLanguage().equals(ConstantsManager.LANGUAGE_ENG)) {
            if (beginningHours != -1) {
                if (beginningHours < 12) {
                    beginningTimeSuffix = " " + ConstantsManager.TIME_STYLE_AM;
                } else {
                    beginningTimeSuffix = " " + ConstantsManager.TIME_STYLE_PM;
                    beginningHours = beginningHours - 12;
                }
            }

            if (endHours != -1) {
                if (endHours < 12) {
                    endTimeSuffix = " " + ConstantsManager.TIME_STYLE_AM;
                } else {
                    endTimeSuffix = " " + ConstantsManager.TIME_STYLE_PM;
                    endHours = endHours - 12;
                }
            }
            dayOrMonthFormat = "%d";
        }

        String date = String.format(Locale.ENGLISH, dayOrMonthFormat + "." + dayOrMonthFormat + ".%04d", day, month, year);

        if (date.equals(yesterdayTodayTomorrowMask[0])) {
            date = yesterdayTodayTomorrowTitles[0];
        }
        if (date.equals(yesterdayTodayTomorrowMask[1])) {
            date = yesterdayTodayTomorrowTitles[1];
        }
        if (date.equals(yesterdayTodayTomorrowMask[2])) {
            date = yesterdayTodayTomorrowTitles[2];
        }
        String beginningTime = null;
        if (beginningHours != -1) {
            beginningTime = String.format(Locale.ENGLISH, "%02d:%02d" + beginningTimeSuffix, beginningHours, beginningMinutes);

        }

        String endTime = null;
        if (endHours != -1) {
            endTime = String.format(Locale.ENGLISH, "%02d:%02d" + endTimeSuffix, endHours, endMinutes);
        }

        String result = "";
        if (date != null) {
            result = result + date;
        }

        if (beginningTime != null) {
            result = result + ", " + beginningTime;
        }

        if (endTime != null) {
            result = result + " - " + endTime;
        }

        if (isShortFormOfTime && beginningTime != null) {
            result = result.replace(date + ", ", "");
        }

        return result;
    }

    /**
     * Checks if time period of task is actual now
     *
     * @param data DataForTasksListItem object
     * @param time Long object which contains date and time of this moment
     * @return boolean result of checking
     */
    public static boolean isActualTask(DataForTasksListItem data, Long time) {
        int beginningYear = data.getYear(), beginningMonth = data.getMonth(), beginningDay = data.getDay();
        int beginningHours = data.getBeginningHours(), beginningMinutes = data.getBeginningMinutes();
        int endHours = data.getEndHours(), endMinutes = data.getEndMinutes();
        String tmp = String.format(Locale.ENGLISH, "%04d%02d%02d", beginningYear, beginningMonth, beginningDay);
        if (beginningHours != -1) {
            tmp = tmp + String.format(Locale.ENGLISH, "%02d%02d", beginningHours, beginningMinutes);
        } else {
            tmp = tmp + "0000";
        }
        Long beginning = Long.parseLong(tmp);
        Long end = null;

        if (endHours != -1) {
            tmp = String.format(Locale.ENGLISH, "%04d%02d%02d", beginningYear, beginningMonth, beginningDay) +
                    String.format(Locale.ENGLISH, "%02d%02d", endHours, endMinutes);
            end = Long.parseLong(tmp);
        }

        if (time >= beginning) {
            if (end != null) {
                return time <= end;
            } else {
                return ("" + beginning).substring(0, 8).equals(("" + time).substring(0, 8));
            }
        }
        return false;
    }

    /**
     * Converts input time using specific time format
     *
     * @param hours   number of hour in day
     * @param minutes number of minute in hour
     * @return converted String object of time
     */
    public static String convertToTimeFormat(int hours, int minutes) {
        String suffix = "";

        if (hours != -1) {
            if (DataManager.getInstance().getPreferenceManager().getLanguage().equals(ConstantsManager.LANGUAGE_ENG)) {
                if (hours < 12) {
                    suffix = " " + ConstantsManager.TIME_STYLE_AM;
                } else {
                    suffix = " " + ConstantsManager.TIME_STYLE_PM;
                    hours = hours - 12;
                }
            }
            return String.format(Locale.ENGLISH, "%02d:%02d" + suffix, hours, minutes);
        } else {
            if (DataManager.getInstance().getPreferenceManager().getLanguage().equals(ConstantsManager.LANGUAGE_ENG)) {
                return String.format(Locale.ENGLISH, "%02d:%02d " + ConstantsManager.TIME_STYLE_AM, 0, 0);
            } else {
                return String.format(Locale.ENGLISH, "%02d:%02d", 0, 0);
            }
        }
    }

    /**
     * Checking if time of beginning and end is correct
     *
     * @param beginningHours   number of beginning hour
     * @param beginningMinutes number of beginning minute
     * @param endHours         number of end hour
     * @param endMinutes       number of end minute
     * @return boolean result of checking
     */
    public static boolean isCorrectTime(int beginningHours, int beginningMinutes, int endHours, int endMinutes) {
        if (beginningHours != -1 && endHours != -1) {
            Long beginning = Long.parseLong(String.format(Locale.ENGLISH, "%02d%02d", beginningHours, beginningMinutes));
            Long end = Long.parseLong(String.format(Locale.ENGLISH, "%02d%02d", endHours, endMinutes));
            return end > beginning;
        } else {
            return true;
        }
    }


    /**
     * Updates all existed widgets
     */
    public static void updateAllWidgets() {
        Intent updateWidgetIntent = new Intent(TaskListApplication.getContext(), TaskListWidget.class);
        updateWidgetIntent.setAction(ConstantsManager.UPDATE_ALL_WIDGETS);
        TaskListApplication.getContext().sendBroadcast(updateWidgetIntent);
    }


}
