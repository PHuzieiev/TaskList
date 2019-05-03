package com.apps.newstudio.tasklist.ui.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.apps.newstudio.tasklist.R;
import com.apps.newstudio.tasklist.data.managers.LanguageManager;
import com.apps.newstudio.tasklist.utils.ConstantsManager;
import com.apps.newstudio.tasklist.utils.OnClickAdapter;

import java.util.Locale;

public class DialogTime {

    private Context mContext;
    private String mTitle;
    private int mHours = 0, mMinutes = 0, mHoursMax = 23;
    private OnChooseTime mOnChooseTime;

    private Dialog mDialog;
    private Button mButtonCancel, mButtonDone;
    private TextView mTextViewAm, mTextViewPm, mTextViewHours, mTextViewMinutes;

    private String mTimeStyle = null;

    /**
     * Constructor of DialogTime object
     *
     * @param context      Context object
     * @param title        title for Dialog object
     * @param hours        number of hour in day
     * @param minutes      number of minute in hour
     * @param onChooseTime OnChooseTime object
     */
    public DialogTime(Context context, String title, int hours, int minutes, OnChooseTime onChooseTime) {
        mContext = context;
        mTitle = title;
        if (hours != -1) {
            mHours = hours;
            mMinutes = minutes;
        }
        mOnChooseTime = onChooseTime;
        createDialog();
    }

    /**
     * Create Dialog object and sets all components for this object
     */
    private void createDialog() {
        mDialog = new Dialog(mContext);
        mDialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        Drawable drawable = new ColorDrawable(Color.TRANSPARENT);
        mDialog.getWindow().setBackgroundDrawable(drawable);
        mDialog.setContentView(R.layout.dialog_time);
        ((TextView) mDialog.getWindow().findViewById(R.id.dialog_time_title)).setText(mTitle);
        mButtonCancel = mDialog.getWindow().findViewById(R.id.dialog_time_cancel);
        mButtonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.dismiss();
            }
        });
        mButtonDone = mDialog.getWindow().findViewById(R.id.dialog_time_done);
        mButtonDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnChooseTime != null)
                    mOnChooseTime.function();
            }
        });
        mTextViewAm = mDialog.getWindow().findViewById(R.id.dialog_time_am);
        mTextViewPm = mDialog.getWindow().findViewById(R.id.dialog_time_pm);
        mTextViewHours = mDialog.getWindow().findViewById(R.id.dialog_time_hours);
        mTextViewMinutes = mDialog.getWindow().findViewById(R.id.dialog_time_minutes);

        new LanguageManager() {
            @Override
            public void engLanguage() {
                mButtonCancel.setText(mContext.getResources().getStringArray(R.array.cancel_done_eng)[0]);
                mButtonDone.setText(mContext.getResources().getStringArray(R.array.cancel_done_eng)[1]);
                calculateTimeStyle();
            }

            @Override
            public void ukrLanguage() {
                mButtonCancel.setText(mContext.getResources().getStringArray(R.array.cancel_done_ukr)[0]);
                mButtonDone.setText(mContext.getResources().getStringArray(R.array.cancel_done_ukr)[1]);
                mTextViewAm.setVisibility(View.INVISIBLE);
                mTextViewPm.setVisibility(View.INVISIBLE);
            }

            @Override
            public void rusLanguage() {
                mButtonCancel.setText(mContext.getResources().getStringArray(R.array.cancel_done_rus)[0]);
                mButtonDone.setText(mContext.getResources().getStringArray(R.array.cancel_done_rus)[1]);
                mTextViewAm.setVisibility(View.INVISIBLE);
                mTextViewPm.setVisibility(View.INVISIBLE);
            }
        };

        mTextViewHours.setText(String.format(Locale.ENGLISH, "%02d", mHours));
        mTextViewMinutes.setText(String.format(Locale.ENGLISH, "%02d", mMinutes));
        mTextViewAm.setOnClickListener(mOnClickTimeStyle);
        mTextViewPm.setOnClickListener(mOnClickTimeStyle);
        mDialog.getWindow().findViewById(R.id.dialog_time_hours_up).setOnClickListener(mOnClickAdapterUpDown);
        mDialog.getWindow().findViewById(R.id.dialog_time_hours_down).setOnClickListener(mOnClickAdapterUpDown);
        mDialog.getWindow().findViewById(R.id.dialog_time_minutes_up).setOnClickListener(mOnClickAdapterUpDown);
        mDialog.getWindow().findViewById(R.id.dialog_time_minutes_down).setOnClickListener(mOnClickAdapterUpDown);
        mDialog.show();
    }

    /**
     * Convert time to one of existed styles of time
     */
    private void calculateTimeStyle() {
        if (mHours < 12) {
            mTimeStyle = ConstantsManager.TIME_STYLE_AM;
            mTextViewAm.setTextColor(mContext.getResources().getColor(R.color.colorPrimary));
            mTextViewPm.setTextColor(mContext.getResources().getColor(R.color.grey));
        } else {
            mTimeStyle = ConstantsManager.TIME_STYLE_PM;
            mHours = mHours - 12;
            mTextViewAm.setTextColor(mContext.getResources().getColor(R.color.grey));
            mTextViewPm.setTextColor(mContext.getResources().getColor(R.color.colorPrimary));
        }
        mHoursMax = 11;
    }

    /**
     * Changes hour or minute value on previous or next value
     */
    private OnClickAdapter mOnClickAdapterUpDown = new OnClickAdapter(1, 1) {
        @Override
        public void myFunc(View v) {
            int index = 1;
            if (v.getId() == R.id.dialog_time_minutes_down || v.getId() == R.id.dialog_time_hours_down) {
                index = -1;
            }
            if (v.getId() == R.id.dialog_time_minutes_up || v.getId() == R.id.dialog_time_minutes_down) {
                mMinutes = mMinutes + index;
                if (mMinutes > 59) {
                    mMinutes = 0;
                }
                if (mMinutes < 0) {
                    mMinutes = 59;
                }
                mTextViewMinutes.setText(String.format(Locale.ENGLISH, "%02d", mMinutes));
            }
            if (v.getId() == R.id.dialog_time_hours_up || v.getId() == R.id.dialog_time_hours_down) {
                mHours = mHours + index;
                if (mHours > mHoursMax) {
                    mHours = 0;
                }
                if (mHours < 0) {
                    mHours = mHoursMax;
                }
                mTextViewHours.setText(String.format(Locale.ENGLISH, "%02d", mHours));
            }
        }
    };

    /**
     * Action for changing type of time on AM or PM
     */
    private View.OnClickListener mOnClickTimeStyle = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (mTimeStyle != null) {
                switch (v.getId()) {
                    case R.id.dialog_time_am:
                        mTimeStyle = ConstantsManager.TIME_STYLE_AM;
                        mTextViewAm.setTextColor(mContext.getResources().getColor(R.color.colorPrimary));
                        mTextViewPm.setTextColor(mContext.getResources().getColor(R.color.grey));
                        break;
                    case R.id.dialog_time_pm:
                        mTimeStyle = ConstantsManager.TIME_STYLE_PM;
                        mTextViewAm.setTextColor(mContext.getResources().getColor(R.color.grey));
                        mTextViewPm.setTextColor(mContext.getResources().getColor(R.color.colorPrimary));
                        break;
                }
            }
        }
    };

    /**
     * Getter for Dialog object
     *
     * @return Dialog object
     */
    public Dialog getDialog() {
        return mDialog;
    }

    /**
     * Getter for number of hour in day
     *
     * @return value of hour
     */
    public int getHours() {
        return mHours;
    }

    /**
     * Getter for number of minute in hour
     *
     * @return value of minute
     */
    public int getMinutes() {
        return mMinutes;
    }

    /**
     * Getter for time format
     *
     * @return time format
     */
    public String getTimeStyle() {
        return mTimeStyle;
    }

    /**
     * Interface to perform action for Button objects
     */
    public interface OnChooseTime {
        void function();
    }

}
