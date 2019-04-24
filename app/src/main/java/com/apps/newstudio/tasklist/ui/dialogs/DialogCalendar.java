package com.apps.newstudio.tasklist.ui.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.apps.newstudio.tasklist.R;
import com.apps.newstudio.tasklist.data.managers.DataManager;
import com.apps.newstudio.tasklist.data.managers.LanguageManager;
import com.apps.newstudio.tasklist.utils.ConstantsManager;
import com.apps.newstudio.tasklist.utils.OnClickAdapter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

public class DialogCalendar {

    private Context mContext;
    private OnChooseDay mOnChooseDay;
    private Dialog mDialog;
    private List<TextView> mWeekdaysViews, mDaysViews;
    private String[] mMonthsTitles, mWeekdaysTitles, mDaysTitles;
    private String calendarFirstDayParameter;
    private int mYear, mMonth, mYearNow, mMonthNow, mDayNow, mYearChosen, mMonthChosen, mDayChosen;

    public DialogCalendar(Context context, OnChooseDay onChooseDay, int year, int month, int day) {
        mContext = context;
        mOnChooseDay = onChooseDay;
        mYear = year;
        mMonth = month;
        mYearChosen = year;
        mMonthChosen = month;
        mDayChosen = day;
        createDialog();
    }

    private void createDialog() {
        mDialog = new Dialog(mContext);
        mDialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        Drawable drawable = new ColorDrawable(Color.TRANSPARENT);
        mDialog.getWindow().setBackgroundDrawable(drawable);
        mDialog.setContentView(R.layout.dialog_calendar);
        initMainViews();
        setLang();
        calculateOrder();
        updateViewsData();

        mDialog.getWindow().findViewById(R.id.dialog_date_left).setOnClickListener(new OnClickAdapter(1,1) {
            @Override
            public void myFunc(View view) {
                showOtherMonth(-1);

            }
        });

        mDialog.getWindow().findViewById(R.id.dialog_date_right).setOnClickListener(new OnClickAdapter(1,1) {
            @Override
            public void myFunc(View view) {
                showOtherMonth(1);

            }
        });

        mDialog.show();
    }

    private void showOtherMonth(int direction){
        mMonth=mMonth+direction;
        if(mMonth==0){
            mYear=mYear-1;
            mMonth=12;
        }
        if(mMonth==13){
            mYear=mYear+1;
            mMonth=1;
        }
        calculateOrder();
        updateViewsData();
    }

    private void calculateOrder() {
        Calendar calendar = GregorianCalendar.getInstance();
        calendar.clear();
        calendar.setFirstDayOfWeek(Calendar.MONDAY);
        calendar.set(Calendar.YEAR, mYear);
        calendar.set(Calendar.MONTH, mMonth - 1);
        calendar.set(Calendar.DATE, 1);
        int maxDayInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        int first_day_week = 0;
        if (calendarFirstDayParameter.equals(ConstantsManager.FIRST_DAY_SUNDAY)) {
            first_day_week = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        } else if (calendarFirstDayParameter.equals(ConstantsManager.FIRST_DAY_MONDAY)) {
            first_day_week = calendar.get(Calendar.DAY_OF_WEEK) - 2;
        }

        int startNumber = 1;
        mDaysTitles = new String[42];
        for (int i = 0; i < 42; i++) {
            if (i < first_day_week) {
                mDaysTitles[i] = "";
            } else {
                if (startNumber <= maxDayInMonth) {
                    mDaysTitles[i] = startNumber + "";
                } else {
                    mDaysTitles[i] = "";
                }
                startNumber = startNumber + 1;
            }
        }

        calendar = GregorianCalendar.getInstance();
        mYearNow = calendar.get(Calendar.YEAR);
        mMonthNow = calendar.get(Calendar.MONTH);
        mDayNow = calendar.get(Calendar.DAY_OF_MONTH);
    }

    private void updateViewsData() {
        ((TextView) mDialog.findViewById(R.id.dialog_date_title)).setText(mMonthsTitles[mMonth - 1].concat(", "+mYear));
        for (TextView weekdayView : mWeekdaysViews) {
            if (weekdayView.getId() != R.id.dialog_date_day_title_7) {
                weekdayView.setTextColor(mContext.getResources().getColor(R.color.colorPrimary));
            }
        }

        for (TextView dayView : mDaysViews) {
            dayView.setTextColor(mContext.getResources().getColor(R.color.grey));
            dayView.setBackgroundColor(mContext.getResources().getColor(R.color.tr));
            dayView.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
            dayView.setOnClickListener(null);
        }

        int startIndex = 5;
        if (calendarFirstDayParameter.equals(ConstantsManager.FIRST_DAY_SUNDAY)) {
            mWeekdaysViews.get(0).setTextColor(mContext.getResources().getColor(R.color.colorPrimaryTwo));
            startIndex = 6;
            mDaysViews.get(0).setTextColor(mContext.getResources().getColor(R.color.grey_two));
        } else if (calendarFirstDayParameter.equals(ConstantsManager.FIRST_DAY_MONDAY)) {
            mWeekdaysViews.get(5).setTextColor(mContext.getResources().getColor(R.color.colorPrimaryTwo));
        }

        for (int i = startIndex; i < mDaysViews.size(); i = i + 7) {
            mDaysViews.get(i).setTextColor(mContext.getResources().getColor(R.color.grey_two));
            if (i + 1 < mDaysViews.size()) {
                mDaysViews.get(i + 1).setTextColor(mContext.getResources().getColor(R.color.grey_two));
            }
        }

        for (int i = 0; i < mWeekdaysTitles.length; i++) {
            mWeekdaysViews.get(i).setText(mWeekdaysTitles[i]);
        }
        for (int i = 0; i < mDaysTitles.length; i++) {
            mDaysViews.get(i).setText(mDaysTitles[i]);
            if (mDaysTitles[i].equals(mDayNow + "") && mMonthNow+1 == mMonth && mYearNow == mYear) {
                mDaysViews.get(i).setTextColor(mContext.getResources().getColor(R.color.colorPrimary));
            }
            if (mDaysTitles[i].equals(mDayChosen + "") && mMonthChosen == mMonth && mYearChosen == mYear) {
                mDaysViews.get(i).setTextColor(mContext.getResources().getColor(R.color.white));
                mDaysViews.get(i).setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                mDaysViews.get(i).setBackgroundResource(R.drawable.ic_circle);
            }

            if (!mDaysTitles[i].equals("")) {
                mDaysViews.get(i).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mDayChosen = Integer.parseInt(((TextView) v).getText().toString());
                        mMonthChosen = mMonth;
                        mYearChosen = mYear;
                        calculateOrder();
                        updateViewsData();
                        mOnChooseDay.function();
                    }
                });
            }
        }
    }

    private void setLang() {
        calendarFirstDayParameter = DataManager.getInstance().getPreferenceManager().getFirstDay();
        new LanguageManager() {
            @Override
            public void engLanguage() {
                if (calendarFirstDayParameter.equals(ConstantsManager.FIRST_DAY_SUNDAY)) {
                    mWeekdaysTitles = mContext.getResources().getStringArray(R.array.sunday_weekdays_titles_eng);
                } else if (calendarFirstDayParameter.equals(ConstantsManager.FIRST_DAY_MONDAY)) {
                    mWeekdaysTitles = mContext.getResources().getStringArray(R.array.monday_weekdays_titles_eng);
                }
                mMonthsTitles = mContext.getResources().getStringArray(R.array.month_titles_eng);
            }

            @Override
            public void ukrLanguage() {
                if (calendarFirstDayParameter.equals(ConstantsManager.FIRST_DAY_SUNDAY)) {
                    mWeekdaysTitles = mContext.getResources().getStringArray(R.array.sunday_weekdays_titles_ukr);
                } else if (calendarFirstDayParameter.equals(ConstantsManager.FIRST_DAY_MONDAY)) {
                    mWeekdaysTitles = mContext.getResources().getStringArray(R.array.monday_weekdays_titles_ukr);
                }
                mMonthsTitles = mContext.getResources().getStringArray(R.array.month_titles_two_ukr);
            }

            @Override
            public void rusLanguage() {
                if (calendarFirstDayParameter.equals(ConstantsManager.FIRST_DAY_SUNDAY)) {
                    mWeekdaysTitles = mContext.getResources().getStringArray(R.array.sunday_weekdays_titles_rus);
                } else if (calendarFirstDayParameter.equals(ConstantsManager.FIRST_DAY_MONDAY)) {
                    mWeekdaysTitles = mContext.getResources().getStringArray(R.array.monday_weekdays_titles_rus);
                }
                mMonthsTitles = mContext.getResources().getStringArray(R.array.month_titles_two_rus);
            }
        };


    }

    private void initMainViews() {
        mWeekdaysViews = new ArrayList<>();
        mWeekdaysViews.add((TextView) mDialog.getWindow().findViewById(R.id.dialog_date_day_title_1));
        mWeekdaysViews.add((TextView) mDialog.getWindow().findViewById(R.id.dialog_date_day_title_2));
        mWeekdaysViews.add((TextView) mDialog.getWindow().findViewById(R.id.dialog_date_day_title_3));
        mWeekdaysViews.add((TextView) mDialog.getWindow().findViewById(R.id.dialog_date_day_title_4));
        mWeekdaysViews.add((TextView) mDialog.getWindow().findViewById(R.id.dialog_date_day_title_5));
        mWeekdaysViews.add((TextView) mDialog.getWindow().findViewById(R.id.dialog_date_day_title_6));
        mWeekdaysViews.add((TextView) mDialog.getWindow().findViewById(R.id.dialog_date_day_title_7));

        mDaysViews = new ArrayList<>();
        mDaysViews.add((TextView) mDialog.getWindow().findViewById(R.id.dialog_date_day_1));
        mDaysViews.add((TextView) mDialog.getWindow().findViewById(R.id.dialog_date_day_2));
        mDaysViews.add((TextView) mDialog.getWindow().findViewById(R.id.dialog_date_day_3));
        mDaysViews.add((TextView) mDialog.getWindow().findViewById(R.id.dialog_date_day_4));
        mDaysViews.add((TextView) mDialog.getWindow().findViewById(R.id.dialog_date_day_5));
        mDaysViews.add((TextView) mDialog.getWindow().findViewById(R.id.dialog_date_day_6));
        mDaysViews.add((TextView) mDialog.getWindow().findViewById(R.id.dialog_date_day_7));
        mDaysViews.add((TextView) mDialog.getWindow().findViewById(R.id.dialog_date_day_8));
        mDaysViews.add((TextView) mDialog.getWindow().findViewById(R.id.dialog_date_day_9));
        mDaysViews.add((TextView) mDialog.getWindow().findViewById(R.id.dialog_date_day_10));
        mDaysViews.add((TextView) mDialog.getWindow().findViewById(R.id.dialog_date_day_11));
        mDaysViews.add((TextView) mDialog.getWindow().findViewById(R.id.dialog_date_day_12));
        mDaysViews.add((TextView) mDialog.getWindow().findViewById(R.id.dialog_date_day_13));
        mDaysViews.add((TextView) mDialog.getWindow().findViewById(R.id.dialog_date_day_14));
        mDaysViews.add((TextView) mDialog.getWindow().findViewById(R.id.dialog_date_day_15));
        mDaysViews.add((TextView) mDialog.getWindow().findViewById(R.id.dialog_date_day_16));
        mDaysViews.add((TextView) mDialog.getWindow().findViewById(R.id.dialog_date_day_17));
        mDaysViews.add((TextView) mDialog.getWindow().findViewById(R.id.dialog_date_day_18));
        mDaysViews.add((TextView) mDialog.getWindow().findViewById(R.id.dialog_date_day_19));
        mDaysViews.add((TextView) mDialog.getWindow().findViewById(R.id.dialog_date_day_20));
        mDaysViews.add((TextView) mDialog.getWindow().findViewById(R.id.dialog_date_day_21));
        mDaysViews.add((TextView) mDialog.getWindow().findViewById(R.id.dialog_date_day_22));
        mDaysViews.add((TextView) mDialog.getWindow().findViewById(R.id.dialog_date_day_23));
        mDaysViews.add((TextView) mDialog.getWindow().findViewById(R.id.dialog_date_day_24));
        mDaysViews.add((TextView) mDialog.getWindow().findViewById(R.id.dialog_date_day_25));
        mDaysViews.add((TextView) mDialog.getWindow().findViewById(R.id.dialog_date_day_26));
        mDaysViews.add((TextView) mDialog.getWindow().findViewById(R.id.dialog_date_day_27));
        mDaysViews.add((TextView) mDialog.getWindow().findViewById(R.id.dialog_date_day_28));
        mDaysViews.add((TextView) mDialog.getWindow().findViewById(R.id.dialog_date_day_29));
        mDaysViews.add((TextView) mDialog.getWindow().findViewById(R.id.dialog_date_day_30));
        mDaysViews.add((TextView) mDialog.getWindow().findViewById(R.id.dialog_date_day_31));
        mDaysViews.add((TextView) mDialog.getWindow().findViewById(R.id.dialog_date_day_32));
        mDaysViews.add((TextView) mDialog.getWindow().findViewById(R.id.dialog_date_day_33));
        mDaysViews.add((TextView) mDialog.getWindow().findViewById(R.id.dialog_date_day_34));
        mDaysViews.add((TextView) mDialog.getWindow().findViewById(R.id.dialog_date_day_35));
        mDaysViews.add((TextView) mDialog.getWindow().findViewById(R.id.dialog_date_day_36));
        mDaysViews.add((TextView) mDialog.getWindow().findViewById(R.id.dialog_date_day_37));
        mDaysViews.add((TextView) mDialog.getWindow().findViewById(R.id.dialog_date_day_38));
        mDaysViews.add((TextView) mDialog.getWindow().findViewById(R.id.dialog_date_day_39));
        mDaysViews.add((TextView) mDialog.getWindow().findViewById(R.id.dialog_date_day_40));
        mDaysViews.add((TextView) mDialog.getWindow().findViewById(R.id.dialog_date_day_41));
        mDaysViews.add((TextView) mDialog.getWindow().findViewById(R.id.dialog_date_day_42));
    }

    public int getYearChosen() {
        return mYearChosen;
    }

    public int getMonthChosen() {
        return mMonthChosen;
    }

    public int getDayChosen() {
        return mDayChosen;
    }

    public interface OnChooseDay {
        void function();
    }

    public Dialog getDialog() {
        return mDialog;
    }
}
