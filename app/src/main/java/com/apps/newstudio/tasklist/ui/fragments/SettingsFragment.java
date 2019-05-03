package com.apps.newstudio.tasklist.ui.fragments;

import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.apps.newstudio.tasklist.R;
import com.apps.newstudio.tasklist.data.adapters.AdapterOfDialogList;
import com.apps.newstudio.tasklist.data.adapters.DataForDialogListItem;
import com.apps.newstudio.tasklist.data.managers.DataManager;
import com.apps.newstudio.tasklist.data.managers.LanguageManager;
import com.apps.newstudio.tasklist.data.managers.PreferenceManager;
import com.apps.newstudio.tasklist.ui.activities.MainActivity;
import com.apps.newstudio.tasklist.ui.dialogs.DialogList;
import com.apps.newstudio.tasklist.ui.dialogs.DialogTime;
import com.apps.newstudio.tasklist.utils.AuxiliaryFunctions;
import com.apps.newstudio.tasklist.utils.ConstantsManager;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;


public class SettingsFragment extends Fragment {

    @BindViews({R.id.parameter_language_about_text, R.id.parameter_first_day_about_text, R.id.parameter_notification_about_text})
    public TextView[] parametersAboutText;

    @BindViews({R.id.parameter_language_main_text, R.id.parameter_first_day_main_text, R.id.parameter_notification_main_text})
    public TextView[] parametersMainText;

    @BindView(R.id.parameter_notification_action_image)
    public ImageView mOnOffNotificationImage;

    @BindView(R.id.parameter_notification_image)
    public ImageView mMainNotificationImage;

    @BindView(R.id.parameter_notification_main_layout)
    public LinearLayout mMainNotificationLayout;

    private Unbinder mUnbinder;
    private String[] mTitleAbout;
    private DialogList mDialogList;
    private DialogTime mDialogTime;
    private PreferenceManager mPreferenceManager;
    private int mChosenLangPosition = 0, mNotificationHour, mNotificationMinute;

    /**
     * Creates View object of fragment
     *
     * @param inflater           LayoutInflater object
     * @param container          ViewGroup object
     * @param savedInstanceState Bundle object
     * @return inflated View object
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);
        mUnbinder = ButterKnife.bind(this, view);
        mPreferenceManager = DataManager.getInstance().getPreferenceManager();
        setLang();
        setAllParameters();
        return view;
    }

    /**
     * Does unbind process when Fragment object is on destroy stage
     */
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUnbinder.unbind();
    }

    /**
     * Setter for all parameters of settings
     */
    private void setAllParameters() {
        setLanguageParameter();
        setFirstDayParameter();
        setNotificationParameter();
    }

    /**
     * Setter for language parameter
     */
    private void setLanguageParameter() {
        parametersMainText[0].setText(getResources().getStringArray(R.array.language_options)[mChosenLangPosition]);
    }

    /**
     * Setter for first day in week parameter
     */
    private void setFirstDayParameter() {
        switch (mPreferenceManager.getFirstDay()) {
            case ConstantsManager
                    .FIRST_DAY_SUNDAY:
                parametersMainText[1].setText(mTitleAbout[4]);
                break;
            case ConstantsManager
                    .FIRST_DAY_MONDAY:
                parametersMainText[1].setText(mTitleAbout[5]);
                break;
        }
    }

    /**
     * Setter for notifications parameter
     */
    private void setNotificationParameter() {
        switch (mPreferenceManager.loadInt(ConstantsManager.TOTAL_ALARM_STATE_FLAG, ConstantsManager.TOTAL_ALARM_STATE_ON)) {
            case ConstantsManager.TOTAL_ALARM_STATE_ON:
                mMainNotificationLayout.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                mOnOffNotificationImage.setImageResource(R.drawable.ic_remove_circle);
                mMainNotificationImage.setImageResource(R.drawable.ic_notifications);
                parametersMainText[2].setTextColor(getResources().getColor(R.color.colorPrimary));
                break;
            case ConstantsManager.TOTAL_ALARM_STATE_OFF:
                mMainNotificationLayout.setBackgroundColor(getResources().getColor(R.color.grey_two));
                mOnOffNotificationImage.setImageResource(R.drawable.ic_add_circle);
                mMainNotificationImage.setImageResource(R.drawable.ic_notifications_grey);
                parametersMainText[2].setTextColor(getResources().getColor(R.color.grey));
                break;
        }
        mNotificationHour = mPreferenceManager.loadInt(ConstantsManager.TOTAL_ALARM_HOUR_FLAG, ConstantsManager.TOTAL_ALARM_HOUR_DEFAULT);
        mNotificationMinute = mPreferenceManager.loadInt(ConstantsManager.TOTAL_ALARM_MINUTE_FLAG, ConstantsManager.TOTAL_ALARM_MINUTE_DEFAULT);
        parametersMainText[2].setText(mTitleAbout[3].concat(AuxiliaryFunctions
                .convertToTimeFormat(mNotificationHour, mNotificationMinute)));
    }

    /**
     * Action which gives user opportunity to choose language of app
     */
    @OnClick(R.id.parameter_language_main_layout)
    public void chooseLang() {
        List<DataForDialogListItem> mDataForDialogList = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            mDataForDialogList.add(new DataForDialogListItem(i,
                    getResources().getStringArray(R.array.language_options)[i], false));
            if (i == mChosenLangPosition)
                mDataForDialogList.get(i).setChosen(true);
        }
        mDialogList = new DialogList(((MainActivity) getActivity()).getContext(),
                mTitleAbout[0], mDataForDialogList, new AdapterOfDialogList.OnItemClickListener() {
            @Override
            public void onClick(int position) {
                switch (position) {
                    case 0:
                        mPreferenceManager.setLanguage(ConstantsManager.LANGUAGE_ENG);
                        break;
                    case 1:
                        mPreferenceManager.setLanguage(ConstantsManager.LANGUAGE_UKR);
                        break;
                    case 2:
                        mPreferenceManager.setLanguage(ConstantsManager.LANGUAGE_RUS);
                        break;
                }
                setLang();
                setAllParameters();
                ((MainActivity) getActivity()).setLang();
                ((MainActivity) getActivity()).updateNavigationView();
                mDialogList.getDialog().dismiss();
            }
        }, 0, new Integer[]{R.drawable.ic_radio_button_checked, R.drawable.ic_radio_button_unchecked}
                , ConstantsManager.DIALOG_LIST_TYPE_ONE);
        mDialogList.getDialog().findViewById(R.id.dialog_list_main_layout)
                .setLayoutParams(new FrameLayout.LayoutParams(getResources().getDimensionPixelSize(R.dimen.huge_size_300dp),
                        ViewGroup.LayoutParams.WRAP_CONTENT));
    }

    /**
     * Action which gives user opportunity to choose first day of week in calendar of app
     */
    @OnClick(R.id.parameter_first_day_main_layout)
    public void chooseFirstDay() {
        List<DataForDialogListItem> mDataForDialogList = new ArrayList<>();
        switch (mPreferenceManager.getFirstDay()) {
            case ConstantsManager
                    .FIRST_DAY_SUNDAY:
                mDataForDialogList.add(new DataForDialogListItem(0, mTitleAbout[4], true));
                mDataForDialogList.add(new DataForDialogListItem(1, mTitleAbout[5], false));
                break;
            case ConstantsManager
                    .FIRST_DAY_MONDAY:
                mDataForDialogList.add(new DataForDialogListItem(0, mTitleAbout[4], false));
                mDataForDialogList.add(new DataForDialogListItem(1, mTitleAbout[5], true));
                break;
        }
        mDialogList = new DialogList(((MainActivity) getActivity()).getContext(),
                mTitleAbout[1], mDataForDialogList, new AdapterOfDialogList.OnItemClickListener() {
            @Override
            public void onClick(int position) {
                switch (position) {
                    case 0:
                        mPreferenceManager.setFirstDay(ConstantsManager.FIRST_DAY_SUNDAY);
                        break;
                    case 1:
                        mPreferenceManager.setFirstDay(ConstantsManager.FIRST_DAY_MONDAY);
                        break;
                }
                setFirstDayParameter();
                mDialogList.getDialog().dismiss();
            }
        }, 0, new Integer[]{R.drawable.ic_radio_button_checked, R.drawable.ic_radio_button_unchecked}
                , ConstantsManager.DIALOG_LIST_TYPE_ONE);
        mDialogList.getDialog().findViewById(R.id.dialog_list_main_layout)
                .setLayoutParams(new FrameLayout.LayoutParams(getResources().getDimensionPixelSize(R.dimen.huge_size_300dp),
                        ViewGroup.LayoutParams.WRAP_CONTENT));
    }

    /**
     * Action which gives user opportunity to change default notifications settings
     */
    @OnClick(R.id.parameter_notification_main_layout)
    public void chooseNotificationDefaultTime() {
        mDialogTime = new DialogTime(((MainActivity) getActivity()).getContext(),
                mTitleAbout[2], mNotificationHour, mNotificationMinute,
                new DialogTime.OnChooseTime() {
                    @Override
                    public void function() {
                        int mAlarmHour = mDialogTime.getHours();
                        if (mDialogTime.getTimeStyle() != null) {
                            if (mDialogTime.getTimeStyle().equals(ConstantsManager.TIME_STYLE_PM)) {
                                mAlarmHour = mDialogTime.getHours() + 12;
                            }
                        }
                        mPreferenceManager.saveInt(ConstantsManager.TOTAL_ALARM_HOUR_FLAG, mAlarmHour);
                        mPreferenceManager.saveInt(ConstantsManager.TOTAL_ALARM_MINUTE_FLAG, mDialogTime.getMinutes());

                        mDialogTime.getDialog().dismiss();
                        mPreferenceManager.saveInt(ConstantsManager.TOTAL_ALARM_STATE_FLAG, ConstantsManager.TOTAL_ALARM_STATE_ON);
                        setNotificationParameter();
                    }
                });
    }

    /**
     * Action which gives user opportunity to turn on or turn off all notifications
     */
    @OnClick(R.id.parameter_notification_action_image)
    public void activateNotifications() {
        switch (mPreferenceManager.loadInt(ConstantsManager.TOTAL_ALARM_STATE_FLAG, ConstantsManager.TOTAL_ALARM_STATE_ON)) {
            case ConstantsManager.TOTAL_ALARM_STATE_ON:
                mPreferenceManager.saveInt(ConstantsManager.TOTAL_ALARM_STATE_FLAG, ConstantsManager.TOTAL_ALARM_STATE_OFF);
                setNotificationParameter();
                break;
            case ConstantsManager.TOTAL_ALARM_STATE_OFF:
                chooseNotificationDefaultTime();
                break;
        }
    }

    /**
     * Performs text objects using language parameter
     */
    private void setLang() {
        new LanguageManager() {
            @Override
            public void engLanguage() {
                mTitleAbout = getResources().getStringArray(R.array.settings_titles_eng);
                mChosenLangPosition = 0;
            }

            @Override
            public void ukrLanguage() {
                mTitleAbout = getResources().getStringArray(R.array.settings_titles_ukr);
                mChosenLangPosition = 1;
            }

            @Override
            public void rusLanguage() {
                mTitleAbout = getResources().getStringArray(R.array.settings_titles_rus);
                mChosenLangPosition = 2;
            }
        };

        parametersAboutText[0].setText(mTitleAbout[0]);
        parametersAboutText[1].setText(mTitleAbout[1]);
        parametersAboutText[2].setText(mTitleAbout[2]);
    }


}
