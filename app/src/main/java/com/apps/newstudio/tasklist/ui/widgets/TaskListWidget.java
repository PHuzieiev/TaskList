package com.apps.newstudio.tasklist.ui.widgets;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RemoteViews;
import android.widget.TextView;
import android.widget.Toast;

import com.apps.newstudio.tasklist.R;
import com.apps.newstudio.tasklist.data.adapters.DateStateObject;
import com.apps.newstudio.tasklist.data.managers.DataManager;
import com.apps.newstudio.tasklist.data.managers.LanguageManager;
import com.apps.newstudio.tasklist.data.managers.PreferenceManager;
import com.apps.newstudio.tasklist.data.storage.models.TaskEntity;
import com.apps.newstudio.tasklist.ui.activities.TaskActivity;
import com.apps.newstudio.tasklist.utils.ConstantsManager;
import com.apps.newstudio.tasklist.utils.TaskListApplication;
import com.apps.newstudio.tasklist.utils.WidgetService;

import java.util.Calendar;
import java.util.Locale;

/**
 * Implementation of App Widget functionality.
 */
public class TaskListWidget extends AppWidgetProvider {


    private String mDayOrMonthFormat = "%02d";
    private String[] mYesterdayTodayTomorrowMask = new String[3];
    private String[] mYesterdayTodayTomorrowTitles, mTitlesOfDays, mTitlesOfMonths, mActiveDoneText, mDeleteDoneBackToastTitle;
    private String[] mEmptyListTitle;

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId, 1);
        }
    }

    public void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId, int createListFlag) {
        RemoteViews views = new RemoteViews(context.getPackageName(),
                R.layout.task_list_widget);
        setLang();
        if (createListFlag == 1) {
            setList(views, context, appWidgetId);
        }
        setOnClickState(context, views, appWidgetId, ConstantsManager.WIDGET_ON_CLICK_STATE_ACTIVE);
        setOnClickState(context, views, appWidgetId, ConstantsManager.WIDGET_ON_CLICK_STATE_DONE);
        setOnClickLeftRight(context, views, appWidgetId, ConstantsManager.WIDGET_ON_CLICK_LEFT);
        setOnClickLeftRight(context, views, appWidgetId, ConstantsManager.WIDGET_ON_CLICK_RIGHT);
        setOnClickList(context, views, appWidgetId);
        setHeader(views, getDate(appWidgetId), appWidgetId);
        setOnClickAddNewTask(context, views, appWidgetId);
        setOnClickUpdate(context, views, appWidgetId);
        appWidgetManager.updateAppWidget(appWidgetId, views);
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetId,
                R.id.widget_list);
    }

    private void setOnClickAddNewTask(Context context, RemoteViews views, int appWidgetId) {
        Calendar calendar = Calendar.getInstance();
        int todayDay = calendar.get(Calendar.DAY_OF_MONTH);
        int todayMonth = calendar.get(Calendar.MONTH) + 1;
        int todayYear = calendar.get(Calendar.YEAR);

        PreferenceManager preferenceManager = DataManager.getInstance().getPreferenceManager();
        int widgetChosenDay = preferenceManager.loadInt(ConstantsManager.WIDGET_CHOSEN_DAY_FLAG + "_" + appWidgetId, todayDay);
        int widgetChosenMonth = preferenceManager.loadInt(ConstantsManager.WIDGET_CHOSEN_MONTH_FLAG + "_" + appWidgetId, todayMonth);
        int widgetChosenYear = preferenceManager.loadInt(ConstantsManager.WIDGET_CHOSEN_YEAR_FLAG + "_" + appWidgetId, todayYear);
        int state = preferenceManager.loadInt(ConstantsManager.WIDGET_CHOSEN_STATE_FLAG + "_" + appWidgetId,
                ConstantsManager.STATE_FLAG_ACTIVE);

        Intent intent = new Intent(context, TaskActivity.class);
        intent.putExtra(ConstantsManager.TASK_ID_FLAG, ConstantsManager.TASK_ID_NONE);
        intent.putExtra(ConstantsManager.TASK_STATE_FLAG, state);
        intent.putExtra(ConstantsManager.TASK_CATEGORY_FLAG, ConstantsManager.CATEGORIES_NONE);
        intent.putExtra(ConstantsManager.TASK_DAY_FLAG, widgetChosenDay);
        intent.putExtra(ConstantsManager.TASK_MONTH_FLAG, widgetChosenMonth);
        intent.putExtra(ConstantsManager.TASK_YEAR_FLAG, widgetChosenYear);
        intent.putExtra(ConstantsManager.ALARM_FLAG, ConstantsManager.ALARM_FLAG_ON);
        intent.putExtra(ConstantsManager.ALARM_HOUR_FLAG,
                DataManager.getInstance().getPreferenceManager()
                        .loadInt(ConstantsManager.TOTAL_ALARM_HOUR_FLAG, ConstantsManager.TOTAL_ALARM_HOUR_DEFAULT));
        intent.putExtra(ConstantsManager.ALARM_MINUTE_FLAG,
                DataManager.getInstance().getPreferenceManager()
                        .loadInt(ConstantsManager.TOTAL_ALARM_MINUTE_FLAG, ConstantsManager.TOTAL_ALARM_MINUTE_DEFAULT));
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        Uri data = Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME));
        intent.setData(data);

        PendingIntent pendingIntent = PendingIntent.getActivity(context, appWidgetId, intent, 0);
        views.setOnClickPendingIntent(R.id.widget_add_task, pendingIntent);
    }

    private void setOnClickList(Context context, RemoteViews views, int appWidgetId) {
        Intent intent = new Intent(context, TaskListWidget.class);
        intent.setAction(ConstantsManager.WIDGET_ON_CLICK_LIST);
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, appWidgetId, intent, 0);
        views.setPendingIntentTemplate(R.id.widget_list, pendingIntent);
    }

    private void setOnClickState(Context context, RemoteViews views, int appWidgetId, String action) {
        Intent stateIntent = new Intent(context, TaskListWidget.class);
        stateIntent.setAction(action);
        stateIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, appWidgetId, stateIntent, 0);
        if (action.equals(ConstantsManager.WIDGET_ON_CLICK_STATE_ACTIVE)) {
            views.setOnClickPendingIntent(R.id.widget_nav_active_text, pendingIntent);
        } else {
            views.setOnClickPendingIntent(R.id.widget_nav_done_text, pendingIntent);
        }
    }

    private void setOnClickLeftRight(Context context, RemoteViews views, int appWidgetId, String flag) {
        Intent stateIntent = new Intent(context, TaskListWidget.class);
        if (flag.equals(ConstantsManager.WIDGET_ON_CLICK_LEFT)) {
            stateIntent.setAction(ConstantsManager.WIDGET_ON_CLICK_LEFT);
        } else {
            stateIntent.setAction(ConstantsManager.WIDGET_ON_CLICK_RIGHT);
        }
        stateIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, appWidgetId, stateIntent, 0);
        if (flag.equals(ConstantsManager.WIDGET_ON_CLICK_LEFT)) {
            views.setOnClickPendingIntent(R.id.widget_header_left, pendingIntent);
        } else {
            views.setOnClickPendingIntent(R.id.widget_header_right, pendingIntent);
        }
    }

    private void setOnClickUpdate(Context context, RemoteViews views, int appWidgetId) {
        Intent intent = new Intent(context, TaskListWidget.class);
        intent.setAction(ConstantsManager.UPDATE_ALL_WIDGETS_TWO);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, appWidgetId, intent, 0);
        views.setOnClickPendingIntent(R.id.widget_update_widget, pendingIntent);
    }

    private DateStateObject getDate(int appWidgetId) {
        Calendar calendar = Calendar.getInstance();
        int todayDay = calendar.get(Calendar.DAY_OF_MONTH);
        int todayMonth = calendar.get(Calendar.MONTH) + 1;
        int todayYear = calendar.get(Calendar.YEAR);

        mYesterdayTodayTomorrowMask[1] = String.format(Locale.ENGLISH, mDayOrMonthFormat + "." + mDayOrMonthFormat + ".%04d",
                todayDay, todayMonth, todayYear);

        calendar.set(Calendar.DAY_OF_MONTH, todayDay - 1);
        mYesterdayTodayTomorrowMask[0] = String.format(Locale.ENGLISH, mDayOrMonthFormat + "." + mDayOrMonthFormat + ".%04d",
                calendar.get(Calendar.DAY_OF_MONTH), calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.YEAR));

        calendar.set(Calendar.DAY_OF_MONTH, todayDay + 1);
        mYesterdayTodayTomorrowMask[2] = String.format(Locale.ENGLISH, mDayOrMonthFormat + "." + mDayOrMonthFormat + ".%04d",
                calendar.get(Calendar.DAY_OF_MONTH), calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.YEAR));

        PreferenceManager preferenceManager = DataManager.getInstance().getPreferenceManager();

        int widgetTodayDay = preferenceManager.loadInt(ConstantsManager.WIDGET_TODAY_DAY_FLAG + "_" + appWidgetId, todayDay);
        int widgetTodayMonth = preferenceManager.loadInt(ConstantsManager.WIDGET_TODAY_MONTH_FLAG + "_" + appWidgetId, todayMonth);
        int widgetTodayYear = preferenceManager.loadInt(ConstantsManager.WIDGET_TODAY_YEAR_FLAG + "_" + appWidgetId, todayYear);

        String today = String.format(Locale.ENGLISH, "%04d%02d%02d", todayYear, todayMonth, todayDay);
        String widgetToday = String.format(Locale.ENGLISH, "%04d%02d%02d", widgetTodayYear, widgetTodayMonth, widgetTodayDay);

        int state = preferenceManager.loadInt(ConstantsManager.WIDGET_CHOSEN_STATE_FLAG + "_" + appWidgetId,
                ConstantsManager.STATE_FLAG_ACTIVE);

        if (today.equals(widgetToday)) {
            int widgetChosenDay = preferenceManager.loadInt(ConstantsManager.WIDGET_CHOSEN_DAY_FLAG + "_" + appWidgetId, todayDay);
            int widgetChosenMonth = preferenceManager.loadInt(ConstantsManager.WIDGET_CHOSEN_MONTH_FLAG + "_" + appWidgetId, todayMonth);
            int widgetChosenYear = preferenceManager.loadInt(ConstantsManager.WIDGET_CHOSEN_YEAR_FLAG + "_" + appWidgetId, todayYear);
            return new DateStateObject(widgetChosenDay, widgetChosenMonth, widgetChosenYear, state);
        } else {
            preferenceManager.saveInt(ConstantsManager.WIDGET_TODAY_DAY_FLAG + "_" + appWidgetId, todayDay);
            preferenceManager.saveInt(ConstantsManager.WIDGET_TODAY_MONTH_FLAG + "_" + appWidgetId, todayMonth);
            preferenceManager.saveInt(ConstantsManager.WIDGET_TODAY_YEAR_FLAG + "_" + appWidgetId, todayYear);
            preferenceManager.saveInt(ConstantsManager.WIDGET_CHOSEN_DAY_FLAG + "_" + appWidgetId, todayDay);
            preferenceManager.saveInt(ConstantsManager.WIDGET_CHOSEN_MONTH_FLAG + "_" + appWidgetId, todayMonth);
            preferenceManager.saveInt(ConstantsManager.WIDGET_CHOSEN_YEAR_FLAG + "_" + appWidgetId, todayYear);
            return new DateStateObject(todayDay, todayMonth, todayYear, state);
        }
    }

    private void setHeader(RemoteViews rv, DateStateObject data, int appWidgetId) {
        Resources resources = TaskListApplication.getContext().getResources();

        int mChosenDay = data.getDay(), mChosenMonth = data.getMonth(), mChosenYear = data.getYear();
        int lengthOfMainWord = -1;
        String result = "";
        String chosenDay = String.format(Locale.ENGLISH, mDayOrMonthFormat + "." + mDayOrMonthFormat + ".%04d",
                mChosenDay, mChosenMonth, mChosenYear);
        if (chosenDay.equals(mYesterdayTodayTomorrowMask[0])) {
            result = mYesterdayTodayTomorrowTitles[0] + " - ";
        }
        if (chosenDay.equals(mYesterdayTodayTomorrowMask[1])) {
            result = mYesterdayTodayTomorrowTitles[1] + " - ";
        }
        if (chosenDay.equals(mYesterdayTodayTomorrowMask[2])) {
            result = mYesterdayTodayTomorrowTitles[2] + " - ";
        }

        if (!result.equals("")) {
            lengthOfMainWord = result.length();
        }

        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.setFirstDayOfWeek(Calendar.SUNDAY);
        calendar.set(Calendar.DAY_OF_MONTH, mChosenDay);
        calendar.set(Calendar.YEAR, mChosenYear);
        calendar.set(Calendar.MONTH, mChosenMonth - 1);
        int weekDay = calendar.get(Calendar.DAY_OF_WEEK);
        result = result + mTitlesOfDays[weekDay - 1] + ", ";
        if (lengthOfMainWord == -1) {
            lengthOfMainWord = result.length();
        }
        result = result + String.format(Locale.ENGLISH, mDayOrMonthFormat + " ", mChosenDay) + mTitlesOfMonths[mChosenMonth - 1] + " " + mChosenYear;

        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(result);
        spannableStringBuilder.setSpan(
                new ForegroundColorSpan(resources.getColor(R.color.colorPrimary)),
                0, lengthOfMainWord, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableStringBuilder.setSpan(new StyleSpan(Typeface.BOLD), 0, lengthOfMainWord, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        rv.setTextViewText(R.id.widget_header_text, spannableStringBuilder);
        rv.setImageViewResource(R.id.widget_header_ico, R.drawable.ic_events);
        rv.setTextViewText(R.id.widget_header_text, spannableStringBuilder);
        rv.setTextViewText(R.id.widget_nav_active_text, mActiveDoneText[0]);
        rv.setTextViewText(R.id.widget_nav_done_text, mActiveDoneText[1]);
        int listDataSize = 0;
        switch (data.getState()) {
            case ConstantsManager.STATE_FLAG_ACTIVE:
                rv.setTextColor(R.id.widget_nav_active_text, resources.getColor(R.color.colorPrimary));
                rv.setTextColor(R.id.widget_nav_done_text, resources.getColor(R.color.white));
                rv.setInt(R.id.widget_nav_active_line, "setBackgroundColor", resources.getColor(R.color.colorPrimary));
                rv.setInt(R.id.widget_nav_done_line, "setBackgroundColor", resources.getColor(R.color.white));
                listDataSize = DataManager.getInstance().getDatabaseManager()
                        .getTasksUsingDate(data.getDay(), data.getMonth(), data.getYear(),
                                ConstantsManager.STATE_FLAG_ACTIVE).size();
                break;
            case ConstantsManager.STATE_FLAG_DONE:
                rv.setTextColor(R.id.widget_nav_active_text, resources.getColor(R.color.white));
                rv.setTextColor(R.id.widget_nav_done_text, resources.getColor(R.color.colorPrimary));
                rv.setInt(R.id.widget_nav_active_line, "setBackgroundColor", resources.getColor(R.color.white));
                rv.setInt(R.id.widget_nav_done_line, "setBackgroundColor", resources.getColor(R.color.colorPrimary));
                listDataSize = DataManager.getInstance().getDatabaseManager()
                        .getTasksUsingDate(data.getDay(), data.getMonth(), data.getYear(),
                                ConstantsManager.STATE_FLAG_DONE).size();
                break;
        }

        switch (listDataSize) {
            case 0:
                int state = DataManager.getInstance().getPreferenceManager()
                        .loadInt(ConstantsManager.WIDGET_CHOSEN_STATE_FLAG + "_" + appWidgetId,
                                ConstantsManager.STATE_FLAG_ACTIVE);
                if (state == ConstantsManager.STATE_FLAG_ACTIVE) {
                    rv.setTextViewText(R.id.widget_empty_tv, mEmptyListTitle[0]);
                } else {
                    rv.setTextViewText(R.id.widget_empty_tv, mEmptyListTitle[1]);
                }
                break;
            default:
                rv.setTextViewText(R.id.widget_empty_tv, "");
                break;
        }
    }

    private void setList(RemoteViews rv, Context context, int appWidgetId) {
        Intent intent = new Intent(context, WidgetService.class);
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        Uri data = Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME));
        intent.setData(data);
        rv.setRemoteAdapter(R.id.widget_list, intent);
    }

    /**
     * Performs text objects using language parameter
     */
    private void setLang() {
        new LanguageManager() {
            @Override
            public void engLanguage() {
                mDayOrMonthFormat = "%d";
                mYesterdayTodayTomorrowTitles = TaskListApplication.getContext().getResources()
                        .getStringArray(R.array.yesterday_today_tomorrow_eng);
                mTitlesOfDays = TaskListApplication.getContext().getResources().getStringArray(R.array.day_titles_eng);
                mTitlesOfMonths = TaskListApplication.getContext().getResources().getStringArray(R.array.month_titles_eng);
                mActiveDoneText = TaskListApplication.getContext().getResources()
                        .getStringArray(R.array.task_list_header_navigation_titles_eng);
                mEmptyListTitle = TaskListApplication.getContext().getResources().getStringArray(R.array.list_empty_eng);
                mDeleteDoneBackToastTitle = TaskListApplication.getContext()
                        .getResources().getStringArray(R.array.delete_done_back_eng);
            }

            @Override
            public void ukrLanguage() {
                mDayOrMonthFormat = "%d";
                mYesterdayTodayTomorrowTitles = TaskListApplication.getContext().getResources()
                        .getStringArray(R.array.yesterday_today_tomorrow_ukr);
                mTitlesOfDays = TaskListApplication.getContext().getResources().getStringArray(R.array.day_titles_ukr);
                mTitlesOfMonths = TaskListApplication.getContext().getResources().getStringArray(R.array.month_titles_ukr);
                mActiveDoneText = TaskListApplication.getContext().getResources()
                        .getStringArray(R.array.task_list_header_navigation_titles_ukr);
                mEmptyListTitle = TaskListApplication.getContext().getResources().getStringArray(R.array.list_empty_ukr);
                mDeleteDoneBackToastTitle = TaskListApplication.getContext()
                        .getResources().getStringArray(R.array.delete_done_back_eng);
            }

            @Override
            public void rusLanguage() {
                mDayOrMonthFormat = "%d";
                mYesterdayTodayTomorrowTitles = TaskListApplication.getContext().getResources()
                        .getStringArray(R.array.yesterday_today_tomorrow_rus);
                mTitlesOfDays = TaskListApplication.getContext().getResources().getStringArray(R.array.day_titles_rus);
                mTitlesOfMonths = TaskListApplication.getContext().getResources().getStringArray(R.array.month_titles_rus);
                mActiveDoneText = TaskListApplication.getContext().getResources()
                        .getStringArray(R.array.task_list_header_navigation_titles_rus);
                mEmptyListTitle = TaskListApplication.getContext().getResources().getStringArray(R.array.list_empty_rus);
                mDeleteDoneBackToastTitle = TaskListApplication.getContext()
                        .getResources().getStringArray(R.array.delete_done_back_eng);
            }
        };
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        super.onDeleted(context, appWidgetIds);
        for (int i = 0; i < appWidgetIds.length; i++) {
            DataManager.getInstance().getPreferenceManager().removeAllWidgetParameters(appWidgetIds[i]);
        }
    }

    @Override
    public void onEnabled(Context context) {
        super.onEnabled(context);
        Intent intent = new Intent(context, TaskListWidget.class);
        intent.setAction(ConstantsManager.UPDATE_ALL_WIDGETS);
        PendingIntent pIntent = PendingIntent.getBroadcast(context, 0, intent, 0);
        AlarmManager alarmManager = (AlarmManager) context
                .getSystemService(Context.ALARM_SERVICE);
        alarmManager.setRepeating(AlarmManager.RTC, System.currentTimeMillis(),
                60000, pIntent);
    }

    @Override
    public void onDisabled(Context context) {
        super.onDisabled(context);
        Intent intent = new Intent(context, TaskListWidget.class);
        intent.setAction(ConstantsManager.UPDATE_ALL_WIDGETS);
        PendingIntent pIntent = PendingIntent.getBroadcast(context, 0, intent, 0);
        AlarmManager alarmManager = (AlarmManager) context
                .getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(pIntent);
    }

    private void updateAllWidgets(Context context, Intent intent) {
        ComponentName thisAppWidget = new ComponentName(
                context.getPackageName(), getClass().getName());
        AppWidgetManager appWidgetManager = AppWidgetManager
                .getInstance(context);
        int ids[] = appWidgetManager.getAppWidgetIds(thisAppWidget);
        for (int appWidgetID : ids) {
            updateAppWidget(context, appWidgetManager, appWidgetID, 0);
        }

    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        if (intent.getAction().equalsIgnoreCase(ConstantsManager.UPDATE_ALL_WIDGETS) ||
                intent.getAction().equalsIgnoreCase(ConstantsManager.UPDATE_ALL_WIDGETS_TWO)) {
            updateAllWidgets(context, intent);
        }

        if (intent.getAction().equalsIgnoreCase(ConstantsManager.WIDGET_ON_CLICK_STATE_ACTIVE)) {
            onClickState(context, intent, ConstantsManager.STATE_FLAG_ACTIVE);
        }

        if (intent.getAction().equalsIgnoreCase(ConstantsManager.WIDGET_ON_CLICK_STATE_DONE)) {
            onClickState(context, intent, ConstantsManager.STATE_FLAG_DONE);
        }

        if (intent.getAction().equalsIgnoreCase(ConstantsManager.WIDGET_ON_CLICK_LEFT)) {
            onClickLeftRight(context, intent, -1);
        }

        if (intent.getAction().equalsIgnoreCase(ConstantsManager.WIDGET_ON_CLICK_RIGHT)) {
            onClickLeftRight(context, intent, 1);
        }

        if (intent.getAction().equalsIgnoreCase(ConstantsManager.WIDGET_ON_CLICK_LIST)) {
            onClickListItem(context, intent);
        }
    }

    private int getWidgetId(Intent intent) {
        Bundle extras = intent.getExtras();
        if (extras != null) {
            return extras.getInt(
                    AppWidgetManager.EXTRA_APPWIDGET_ID,
                    AppWidgetManager.INVALID_APPWIDGET_ID);
        } else {
            return AppWidgetManager.INVALID_APPWIDGET_ID;
        }
    }

    private void onClickListItem(Context context, Intent intent) {
        int widgetId = getWidgetId(intent);
        if (widgetId != AppWidgetManager.INVALID_APPWIDGET_ID) {
            Long id = intent.getLongExtra(ConstantsManager.WIDGET_ON_CLICK_LIST_ITEM_ID, 0L);
            if (!id.equals(0L)) {
                if (intent.getStringExtra(ConstantsManager.WIDGET_ON_CLICK_LIST_ACTION_TAG)
                        .equals(ConstantsManager.WIDGET_ON_CLICK_LIST_ACTION_CHANGE)) {
                    DataManager.getInstance().getDatabaseManager().changeStateInBD(id);
                    updateAllWidgets(context, intent);
                    String toastString = mDeleteDoneBackToastTitle[1];
                    int toastImage = R.drawable.ic_done;
                    if (DataManager.getInstance().getPreferenceManager()
                            .loadInt(ConstantsManager.WIDGET_CHOSEN_STATE_FLAG + "_" + widgetId,
                                    ConstantsManager.STATE_FLAG_ACTIVE) == ConstantsManager.STATE_FLAG_DONE) {
                        toastImage = R.drawable.ic_back;
                        toastString = mDeleteDoneBackToastTitle[2];
                    }
                    showToast(toastString, toastImage);
                } else if (intent.getStringExtra(ConstantsManager.WIDGET_ON_CLICK_LIST_ACTION_TAG)
                        .equals(ConstantsManager.WIDGET_ON_CLICK_LIST_ACTION_OPEN)) {
                    TaskEntity task = DataManager.getInstance().getDatabaseManager()
                            .getTaskEntityById(id);
                    Intent activityIntent = new Intent(context, TaskActivity.class);
                    activityIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    activityIntent.putExtra(ConstantsManager.TASK_ID_FLAG, task.getId());
                    activityIntent.putExtra(ConstantsManager.TASK_TITLE_FLAG, task.getTitle());
                    activityIntent.putExtra(ConstantsManager.TASK_STATE_FLAG, task.getTaskState());
                    activityIntent.putExtra(ConstantsManager.TASK_CATEGORY_FLAG, task.getCategoryId());
                    activityIntent.putExtra(ConstantsManager.TASK_DAY_FLAG, task.getDay());
                    activityIntent.putExtra(ConstantsManager.TASK_MONTH_FLAG, task.getMonth());
                    activityIntent.putExtra(ConstantsManager.TASK_YEAR_FLAG, task.getYear());
                    activityIntent.putExtra(ConstantsManager.TASK_HOUR_BEGINNING_FLAG, task.getHourBeginning());
                    activityIntent.putExtra(ConstantsManager.TASK_MINUTE_BEGINNING_FLAG, task.getMinuteBeginning());
                    activityIntent.putExtra(ConstantsManager.TASK_HOUR_END_FLAG, task.getHourEnd());
                    activityIntent.putExtra(ConstantsManager.TASK_MINUTE_END_FLAG, task.getMinuteEnd());
                    activityIntent.putExtra(ConstantsManager.ALARM_FLAG, task.getAlarmState());
                    activityIntent.putExtra(ConstantsManager.ALARM_HOUR_FLAG, task.getAlarmHour());
                    activityIntent.putExtra(ConstantsManager.ALARM_MINUTE_FLAG, task.getAlarmMinute());
                    activityIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetId);
                    TaskListApplication.getContext().startActivity(activityIntent);
                }
            }
        }
    }

    private void onClickState(Context context, Intent intent, int state) {
        int widgetId = getWidgetId(intent);
        if (widgetId != AppWidgetManager.INVALID_APPWIDGET_ID) {
            PreferenceManager preferenceManager = DataManager.getInstance().getPreferenceManager();
            if (preferenceManager.loadInt(ConstantsManager.WIDGET_CHOSEN_STATE_FLAG + "_" + widgetId,
                    ConstantsManager.STATE_FLAG_ACTIVE) != state) {
                preferenceManager.saveInt(ConstantsManager.WIDGET_CHOSEN_STATE_FLAG + "_" + widgetId, state);
                updateAllWidgets(context, intent);
            }
        }
    }

    private void onClickLeftRight(Context context, Intent intent, int direction) {
        int widgetId = getWidgetId(intent);
        if (widgetId != AppWidgetManager.INVALID_APPWIDGET_ID) {
            PreferenceManager preferenceManager = DataManager.getInstance().getPreferenceManager();
            Calendar calendar = Calendar.getInstance();
            int widgetChosenDay = preferenceManager.
                    loadInt(ConstantsManager.WIDGET_CHOSEN_DAY_FLAG + "_" + widgetId, calendar.get(Calendar.DAY_OF_MONTH));
            int widgetChosenMonth = preferenceManager.
                    loadInt(ConstantsManager.WIDGET_CHOSEN_MONTH_FLAG + "_" + widgetId, calendar.get(Calendar.MONTH) + 1);
            int widgetChosenYear = preferenceManager.
                    loadInt(ConstantsManager.WIDGET_CHOSEN_YEAR_FLAG + "_" + widgetId, calendar.get(Calendar.YEAR));

            calendar.clear();
            calendar.set(Calendar.YEAR, widgetChosenYear);
            calendar.set(Calendar.MONTH, widgetChosenMonth - 1);
            calendar.set(Calendar.DAY_OF_MONTH, widgetChosenDay + direction);
            preferenceManager.saveInt(ConstantsManager.WIDGET_CHOSEN_DAY_FLAG + "_" + widgetId, calendar.get(Calendar.DAY_OF_MONTH));
            preferenceManager.saveInt(ConstantsManager.WIDGET_CHOSEN_MONTH_FLAG + "_" + widgetId, calendar.get(Calendar.MONTH) + 1);
            preferenceManager.saveInt(ConstantsManager.WIDGET_CHOSEN_YEAR_FLAG + "_" + widgetId, calendar.get(Calendar.YEAR));
        }
        updateAllWidgets(context, intent);
    }

    /**
     * Shows toast object message
     *
     * @param message String object of message
     * @param ico     image for message
     */
    private void showToast(String message, int ico) {
        int toastPadding = TaskListApplication.getContext().getResources().getDimensionPixelSize(R.dimen.spacing_smaller_8dp);
        Toast toast = Toast.makeText(TaskListApplication.getContext(), message, Toast.LENGTH_SHORT);
        View viewToast = toast.getView();
        TextView textView = (TextView) ((LinearLayout) viewToast).getChildAt(0);
        textView.setTextColor(TaskListApplication.getContext().getResources().getColor(R.color.white));
        textView.setGravity(Gravity.CENTER);
        viewToast.setPadding(toastPadding, toastPadding, toastPadding, toastPadding);
        viewToast.setBackground(TaskListApplication.getContext().getResources().getDrawable(R.drawable.rectangle_for_toast_red));

        if (ico != 0) {
            ((LinearLayout) viewToast).removeAllViews();
            ImageView imageView = new ImageView(TaskListApplication.getContext());
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    TaskListApplication.getContext().getResources().getDimensionPixelSize(R.dimen.small_size_24dp),
                    TaskListApplication.getContext().getResources().getDimensionPixelSize(R.dimen.small_size_24dp));
            imageView.setImageResource(ico);
            layoutParams.rightMargin = TaskListApplication.getContext().getResources().getDimensionPixelOffset(R.dimen.spacing_smaller_4dp);
            imageView.setLayoutParams(layoutParams);
            ((LinearLayout) viewToast).setOrientation(LinearLayout.HORIZONTAL);
            ((LinearLayout) viewToast).setGravity(Gravity.CENTER_VERTICAL);
            ((LinearLayout) viewToast).addView(imageView);
            ((LinearLayout) viewToast).addView(textView);
        }
        toast.show();
    }
}

