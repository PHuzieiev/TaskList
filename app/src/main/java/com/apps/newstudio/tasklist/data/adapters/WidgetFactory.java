package com.apps.newstudio.tasklist.data.adapters;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.apps.newstudio.tasklist.R;
import com.apps.newstudio.tasklist.data.managers.DataManager;
import com.apps.newstudio.tasklist.data.managers.LanguageManager;
import com.apps.newstudio.tasklist.data.managers.PreferenceManager;
import com.apps.newstudio.tasklist.utils.AuxiliaryFunctions;
import com.apps.newstudio.tasklist.utils.ConstantsManager;
import com.apps.newstudio.tasklist.utils.TaskListApplication;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class WidgetFactory implements RemoteViewsService.RemoteViewsFactory {

    private List<DataForTasksListItem> mData;
    private Context mContext;
    private int mWidgetId, mState;
    private String mDayOrMonthFormat = "%02d";
    private String[] yesterdayTodayTomorrowTitles, yesterdayTodayTomorrowMask;

    public WidgetFactory(Context context, Intent intent) {
        mContext = context;
        mWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                AppWidgetManager.INVALID_APPWIDGET_ID);
    }

    @Override
    public void onCreate() {
        mData = new ArrayList<>();
    }


    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public RemoteViews getViewAt(int position) {
        if (position < getCount()) {
            RemoteViews view = new RemoteViews(mContext.getPackageName(), R.layout.item_widget_list);
            if (mData.get(position).getId() != null) {
                Resources resources = TaskListApplication.getContext().getResources();
                view.setTextViewText(R.id.item_title_text, mData.get(position).getTitle());
                switch (mData.get(position).getCategory()) {
                    case ConstantsManager.CATEGORIES_NONE:
                        view.setImageViewResource(R.id.item_title_ico, R.drawable.ic_none_category);
                        break;
                    case ConstantsManager.CATEGORIES_FAMILY:
                        view.setImageViewResource(R.id.item_title_ico, R.drawable.ic_family);
                        break;
                    case ConstantsManager.CATEGORIES_MEETINGS:
                        view.setImageViewResource(R.id.item_title_ico, R.drawable.ic_meetings);
                        break;
                    case ConstantsManager.CATEGORIES_HONE:
                        view.setImageViewResource(R.id.item_title_ico, R.drawable.ic_home);
                        break;
                    case ConstantsManager.CATEGORIES_SHOPPING:
                        view.setImageViewResource(R.id.item_title_ico, R.drawable.ic_shopping);
                        break;
                    case ConstantsManager.CATEGORIES_BIRTHDAY:
                        view.setImageViewResource(R.id.item_title_ico, R.drawable.ic_birthday);
                        break;
                    case ConstantsManager.CATEGORIES_BUSINESS:
                        view.setImageViewResource(R.id.item_title_ico, R.drawable.ic_business);
                        break;
                    case ConstantsManager.CATEGORIES_CALLS:
                        view.setImageViewResource(R.id.item_title_ico, R.drawable.ic_calls);
                        break;
                    case ConstantsManager.CATEGORIES_ENTERTAINMENTS:
                        view.setImageViewResource(R.id.item_title_ico, R.drawable.ic_entertainments);
                        break;
                    case ConstantsManager.CATEGORIES_EDUCATION:
                        view.setImageViewResource(R.id.item_title_ico, R.drawable.ic_education);
                        break;
                    case ConstantsManager.CATEGORIES_PETS:
                        view.setImageViewResource(R.id.item_title_ico, R.drawable.ic_pets);
                        break;
                    case ConstantsManager.CATEGORIES_PRIVATE_LIFE:
                        view.setImageViewResource(R.id.item_title_ico, R.drawable.ic_private_life);
                        break;
                    case ConstantsManager.CATEGORIES_REPAIR:
                        view.setImageViewResource(R.id.item_title_ico, R.drawable.ic_repair);
                        break;
                    case ConstantsManager.CATEGORIES_WORK:
                        view.setImageViewResource(R.id.item_title_ico, R.drawable.ic_work);
                        break;
                    case ConstantsManager.CATEGORIES_SPORT:
                        view.setImageViewResource(R.id.item_title_ico, R.drawable.ic_sport);
                        break;
                    case ConstantsManager.CATEGORIES_TRAVELLINGS:
                        view.setImageViewResource(R.id.item_title_ico, R.drawable.ic_travelling);
                        break;
                }


                int imageAlarmOn, imageAlarmOff;
                if (mData.get(position).isNow()) {
                    imageAlarmOn = R.drawable.ic_alarm_on;
                    imageAlarmOff = R.drawable.ic_alarm_off;
                    view.setInt(R.id.item_time_period_text, "setTextColor",
                            resources.getColor(R.color.colorPrimary));
                } else {
                    imageAlarmOn = R.drawable.ic_alarm_on_white;
                    imageAlarmOff = R.drawable.ic_alarm_off_white;
                    view.setInt(R.id.item_time_period_text, "setTextColor",
                            resources.getColor(R.color.white));
                }
                if (mData.get(position).isAlarmOn() == ConstantsManager.ALARM_FLAG_ON) {
                    view.setImageViewResource(R.id.item_alarm_image, imageAlarmOn);
                } else {
                    view.setImageViewResource(R.id.item_alarm_image, imageAlarmOff);
                }

                view.setTextViewText(R.id.item_time_period_text,
                        AuxiliaryFunctions.convertToTimePeriod(mData.get(position),
                                yesterdayTodayTomorrowTitles,
                                yesterdayTodayTomorrowMask, true));
                switch (mState) {
                    case ConstantsManager.STATE_FLAG_ACTIVE:
                        view.setImageViewResource(R.id.item_change_state_image, R.drawable.ic_done);
                        break;
                    case ConstantsManager.STATE_FLAG_DONE:
                        view.setImageViewResource(R.id.item_change_state_image, R.drawable.ic_arrow_back);
                        break;
                }
                view.setInt(R.id.item_change_state_image, "setBackgroundResource", R.drawable.ic_circle_widget);


                Intent intent = new Intent();
                intent.putExtra(ConstantsManager.WIDGET_ON_CLICK_LIST_ITEM_ID, mData.get(position).getId());
                intent.putExtra(ConstantsManager.WIDGET_ON_CLICK_LIST_ACTION_TAG,
                        ConstantsManager.WIDGET_ON_CLICK_LIST_ACTION_CHANGE);
                view.setOnClickFillInIntent(R.id.item_change_state_image, intent);

                intent = new Intent();
                intent.putExtra(ConstantsManager.WIDGET_ON_CLICK_LIST_ITEM_ID, mData.get(position).getId());
                intent.putExtra(ConstantsManager.WIDGET_ON_CLICK_LIST_ACTION_TAG,
                        ConstantsManager.WIDGET_ON_CLICK_LIST_ACTION_OPEN);
                view.setOnClickFillInIntent(R.id.item_parent_layout, intent);
            } else {
                view.setTextViewText(R.id.item_title_text, "");
                view.setImageViewResource(R.id.item_title_ico, R.drawable.ic_tr);
                view.setImageViewResource(R.id.item_alarm_image, R.drawable.ic_tr);
                view.setTextViewText(R.id.item_time_period_text, "");
                view.setImageViewResource(R.id.item_change_state_image, R.drawable.ic_tr);
                view.setInt(R.id.item_change_state_image, "setBackgroundResource", R.drawable.ic_tr);
                Intent intent = new Intent();
                intent.putExtra(ConstantsManager.WIDGET_ON_CLICK_LIST_ITEM_ID, 0L);
                view.setOnClickFillInIntent(R.id.item_change_state_image, intent);

                intent = new Intent();
                intent.putExtra(ConstantsManager.WIDGET_ON_CLICK_LIST_ITEM_ID, 0L);
                view.setOnClickFillInIntent(R.id.item_parent_layout, intent);
            }
            return view;
        } else {
            return new RemoteViews(mContext.getPackageName(), R.layout.item_widget_list);
        }
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }


    @Override
    public void onDataSetChanged() {
        setLang();
        PreferenceManager preferenceManager = DataManager.getInstance().getPreferenceManager();
        Calendar calendar = Calendar.getInstance();

        int widgetChosenDay = preferenceManager.loadInt(ConstantsManager.WIDGET_CHOSEN_DAY_FLAG + "_" + mWidgetId,
                calendar.get(Calendar.DAY_OF_MONTH));
        int widgetChosenMonth = preferenceManager.loadInt(ConstantsManager.WIDGET_CHOSEN_MONTH_FLAG + "_" + mWidgetId,
                calendar.get(Calendar.MONTH) + 1);
        int widgetChosenYear = preferenceManager.loadInt(ConstantsManager.WIDGET_CHOSEN_YEAR_FLAG + "_" + mWidgetId,
                calendar.get(Calendar.YEAR));
        mData.clear();
        mState = preferenceManager.loadInt(ConstantsManager.WIDGET_CHOSEN_STATE_FLAG + "_" + mWidgetId,
                ConstantsManager.STATE_FLAG_ACTIVE);

        switch (mState) {
            case ConstantsManager.STATE_FLAG_ACTIVE:
                mData = DataManager.getInstance().getDatabaseManager()
                        .getTasksUsingDate(widgetChosenDay, widgetChosenMonth, widgetChosenYear, ConstantsManager.STATE_FLAG_ACTIVE);
                for (DataForTasksListItem data : mData) {
                    data.setNow(AuxiliaryFunctions.isActualTask(data,
                            Long.parseLong(String.format(Locale.ENGLISH,
                                    "%04d%02d%02d%02d%02d", calendar.get(Calendar.YEAR),
                                    calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.DAY_OF_MONTH)
                                    , calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE)))));
                }
                break;
            case ConstantsManager.STATE_FLAG_DONE:
                mData = DataManager.getInstance().getDatabaseManager()
                        .getTasksUsingDate(widgetChosenDay, widgetChosenMonth, widgetChosenYear, ConstantsManager.STATE_FLAG_DONE);
                break;
        }
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH) + 1;
        int year = calendar.get(Calendar.YEAR);
        int todayDay = calendar.get(Calendar.DAY_OF_MONTH);

        yesterdayTodayTomorrowMask = new String[3];
        yesterdayTodayTomorrowMask[1] = String.format(Locale.ENGLISH, mDayOrMonthFormat + "." + mDayOrMonthFormat + ".%04d",
                day, month, year);
        calendar.set(Calendar.DAY_OF_MONTH, todayDay - 1);
        yesterdayTodayTomorrowMask[0] = String.format(Locale.ENGLISH, mDayOrMonthFormat + "." + mDayOrMonthFormat + ".%04d",
                calendar.get(Calendar.DAY_OF_MONTH), calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.YEAR));
        calendar.set(Calendar.DAY_OF_MONTH, todayDay + 1);
        yesterdayTodayTomorrowMask[2] = String.format(Locale.ENGLISH, mDayOrMonthFormat + "." + mDayOrMonthFormat + ".%04d",
                calendar.get(Calendar.DAY_OF_MONTH), calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.YEAR));

        if (mData.size() != 0) {
            mData.add(new DataForTasksListItem(null, "", -1, -1, -1,
                    -1, -1, -1,
                    -1, -1, false, -1));
        }
    }

    private void setLang() {
        new LanguageManager() {
            @Override
            public void engLanguage() {
                mDayOrMonthFormat = "%d";
                yesterdayTodayTomorrowTitles = TaskListApplication.getContext()
                        .getResources().getStringArray(R.array.yesterday_today_tomorrow_eng);
            }

            @Override
            public void ukrLanguage() {
                yesterdayTodayTomorrowTitles = TaskListApplication.getContext()
                        .getResources().getStringArray(R.array.yesterday_today_tomorrow_ukr);
            }

            @Override
            public void rusLanguage() {
                yesterdayTodayTomorrowTitles = TaskListApplication.getContext()
                        .getResources().getStringArray(R.array.yesterday_today_tomorrow_rus);
            }
        };
    }

    @Override
    public void onDestroy() {

    }


}
