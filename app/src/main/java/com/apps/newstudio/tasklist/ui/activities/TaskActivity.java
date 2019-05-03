package com.apps.newstudio.tasklist.ui.activities;

import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.apps.newstudio.tasklist.R;
import com.apps.newstudio.tasklist.data.adapters.AdapterOfDialogList;
import com.apps.newstudio.tasklist.data.adapters.DataForDialogListItem;
import com.apps.newstudio.tasklist.data.managers.DataManager;
import com.apps.newstudio.tasklist.data.managers.DatabaseManager;
import com.apps.newstudio.tasklist.data.managers.LanguageManager;
import com.apps.newstudio.tasklist.data.storage.models.TaskEntity;
import com.apps.newstudio.tasklist.ui.dialogs.DialogCalendar;
import com.apps.newstudio.tasklist.ui.dialogs.DialogList;
import com.apps.newstudio.tasklist.ui.dialogs.DialogTime;
import com.apps.newstudio.tasklist.ui.dialogs.DialogTwoButtons;
import com.apps.newstudio.tasklist.utils.AuxiliaryFunctions;
import com.apps.newstudio.tasklist.utils.ConstantsManager;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class TaskActivity extends BaseActivity {

    @BindView(R.id.toolbar)
    public Toolbar mToolbar;

    @BindView(R.id.task_header_text)
    public TextView stateTitleText;

    @BindView(R.id.task_nav_active_text)
    public TextView stateActiveText;

    @BindView(R.id.task_nav_active_line)
    public View stateActiveLine;

    @BindView(R.id.task_nav_done_text)
    public TextView stateDoneText;

    @BindView(R.id.task_nav_done_line)
    public View stateDoneLine;

    @BindViews({R.id.parameter_title_about_text, R.id.parameter_category_about_text, R.id.parameter_date_about_text,
            R.id.parameter_beginning_about_text, R.id.parameter_end_about_text, R.id.parameter_alarm_about_text})
    public TextView[] parametersAboutText;

    @BindView(R.id.parameter_title_main_text)
    public EditText parameterTitleEditText;

    @BindView(R.id.parameter_title_line)
    public View parameterTitleLine;

    @BindViews({R.id.parameter_category_main_text, R.id.parameter_date_main_text,
            R.id.parameter_beginning_main_text, R.id.parameter_end_main_text, R.id.parameter_alarm_main_text})
    public TextView[] parametersMainText;

    @BindViews({R.id.parameter_title_action_image,
            R.id.parameter_beginning_action_image, R.id.parameter_end_action_image, R.id.parameter_alarm_action_image})
    public ImageView[] parametersActionImage;

    @BindViews({R.id.parameter_category_image,
            R.id.parameter_beginning_image, R.id.parameter_end_image, R.id.parameter_alarm_image})
    public ImageView[] parametersImage;

    @BindViews({R.id.parameter_beginning_main_text, R.id.parameter_end_main_text, R.id.parameter_alarm_main_text})
    public TextView[] parametersMainTextTwo;

    @BindViews({R.id.parameter_beginning_main_layout, R.id.parameter_end_main_layout, R.id.parameter_alarm_main_layout})
    public LinearLayout[] parametersLayoutTwo;

    private Long mId;
    private int mState, mCategoryId, mDay, mMonth, mYear,
            mHourBeginning, mMinuteBeginning, mHourEnd, mMinuteEnd,
            mAlarmState, mAlarmHour, mAlarmMinute;
    private String mTitle;
    private String mDayOrMonthFormat = "%02d";
    private String mToolbarTitle[], mStateTitle[], mTaskParameters[], mCategories[], mTitlesOfDays[], mTitlesOfMonths[];
    private DialogList mDialogList;
    private DialogCalendar mDialogCalendar;
    private DialogTime mDialogTime;
    private String[] mEndTimeToast;
    private String[] mDialogDoneText;
    private String[] mDialogDoneButtonsTitle;

    /**
     * Perform initialization of all fragments.
     *
     * @param savedInstanceState Bundle object of saved values
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);
        ButterKnife.bind(this);
        setupToolbar();
        setLang();
        if (savedInstanceState == null) {
            getParameters();
        } else {
            getParametersUsingBundle(savedInstanceState);
        }

        if (mId.equals(ConstantsManager.TASK_ID_NONE)) {
            mToolbar.setTitle(mToolbarTitle[0]);
            mTitle = mToolbarTitle[2];
        } else {
            mToolbar.setTitle(mToolbarTitle[1]);
        }

        setState();
        setDataForParametersViews();
        parametersActionImage[1].setOnClickListener(switchClick);
        parametersActionImage[2].setOnClickListener(switchClick);
        parametersActionImage[3].setOnClickListener(switchClick);
    }

    /**
     * Sets all configuration parameters for ToolBar object
     */
    private void setupToolbar() {
        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_back);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    /**
     * Getter for all necessary parameters using Intent object
     */
    private void getParameters() {
        mId = getIntent().getLongExtra(ConstantsManager.TASK_ID_FLAG, ConstantsManager.TASK_ID_NONE);
        mTitle = getIntent().getStringExtra(ConstantsManager.TASK_TITLE_FLAG);
        mState = getIntent().getIntExtra(ConstantsManager.TASK_STATE_FLAG, ConstantsManager.STATE_FLAG_ACTIVE);
        mCategoryId = getIntent().getIntExtra(ConstantsManager.TASK_CATEGORY_FLAG, ConstantsManager.CATEGORIES_NONE);
        mDay = getIntent().getIntExtra(ConstantsManager.TASK_DAY_FLAG, 0);
        mMonth = getIntent().getIntExtra(ConstantsManager.TASK_MONTH_FLAG, 0);
        mYear = getIntent().getIntExtra(ConstantsManager.TASK_YEAR_FLAG, 0);
        mHourBeginning = getIntent().getIntExtra(ConstantsManager.TASK_HOUR_BEGINNING_FLAG, -1);
        mMinuteBeginning = getIntent().getIntExtra(ConstantsManager.TASK_MINUTE_BEGINNING_FLAG, -1);
        mHourEnd = getIntent().getIntExtra(ConstantsManager.TASK_HOUR_END_FLAG, -1);
        mMinuteEnd = getIntent().getIntExtra(ConstantsManager.TASK_MINUTE_END_FLAG, -1);
        mAlarmState = getIntent().getIntExtra(ConstantsManager.ALARM_FLAG, ConstantsManager.ALARM_FLAG_ON);
        mAlarmHour = getIntent().getIntExtra(ConstantsManager.ALARM_HOUR_FLAG, 8);
        mAlarmMinute = getIntent().getIntExtra(ConstantsManager.ALARM_MINUTE_FLAG, 0);
    }

    /**
     * Getter for all necessary parameters using Bundle object
     */
    private void getParametersUsingBundle(Bundle savedInstanceState) {
        mId = savedInstanceState.getLong(ConstantsManager.TASK_ID_FLAG, ConstantsManager.TASK_ID_NONE);
        mTitle = savedInstanceState.getString(ConstantsManager.TASK_TITLE_FLAG);
        mState = savedInstanceState.getInt(ConstantsManager.TASK_STATE_FLAG, ConstantsManager.STATE_FLAG_ACTIVE);
        mCategoryId = savedInstanceState.getInt(ConstantsManager.TASK_CATEGORY_FLAG, ConstantsManager.CATEGORIES_NONE);
        mDay = savedInstanceState.getInt(ConstantsManager.TASK_DAY_FLAG, 0);
        mMonth = savedInstanceState.getInt(ConstantsManager.TASK_MONTH_FLAG, 0);
        mYear = savedInstanceState.getInt(ConstantsManager.TASK_YEAR_FLAG, 0);
        mHourBeginning = savedInstanceState.getInt(ConstantsManager.TASK_HOUR_BEGINNING_FLAG, -1);
        mMinuteBeginning = savedInstanceState.getInt(ConstantsManager.TASK_MINUTE_BEGINNING_FLAG, -1);
        mHourEnd = savedInstanceState.getInt(ConstantsManager.TASK_HOUR_END_FLAG, -1);
        mMinuteEnd = savedInstanceState.getInt(ConstantsManager.TASK_MINUTE_END_FLAG, -1);
        mAlarmState = savedInstanceState.getInt(ConstantsManager.ALARM_FLAG, ConstantsManager.ALARM_FLAG_ON);
        mAlarmHour = savedInstanceState.getInt(ConstantsManager.ALARM_HOUR_FLAG, 8);
        mAlarmMinute = savedInstanceState.getInt(ConstantsManager.ALARM_MINUTE_FLAG, 0);
    }

    /**
     * Saves necessary values in Bundle object
     *
     * @param outState Bundle object
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putLong(ConstantsManager.TASK_ID_FLAG, mId);
        outState.putString(ConstantsManager.TASK_TITLE_FLAG, mTitle);
        outState.putInt(ConstantsManager.TASK_STATE_FLAG, mState);
        outState.putInt(ConstantsManager.TASK_CATEGORY_FLAG, mCategoryId);
        outState.putInt(ConstantsManager.TASK_DAY_FLAG, mDay);
        outState.putInt(ConstantsManager.TASK_MONTH_FLAG, mMonth);
        outState.putInt(ConstantsManager.TASK_YEAR_FLAG, mYear);
        outState.putInt(ConstantsManager.TASK_HOUR_BEGINNING_FLAG, mHourBeginning);
        outState.putInt(ConstantsManager.TASK_MINUTE_BEGINNING_FLAG, mMinuteBeginning);
        outState.putInt(ConstantsManager.TASK_HOUR_END_FLAG, mHourEnd);
        outState.putInt(ConstantsManager.TASK_MINUTE_END_FLAG, mMinuteEnd);
        outState.putInt(ConstantsManager.ALARM_FLAG, mAlarmState);
        outState.putInt(ConstantsManager.ALARM_HOUR_FLAG, mAlarmHour);
        outState.putInt(ConstantsManager.ALARM_MINUTE_FLAG, mAlarmMinute);
        super.onSaveInstanceState(outState);
    }

    /**
     * Changes value of task states
     *
     * @param v object which was clicked and uses the following function
     */
    @OnClick({R.id.task_nav_done_text, R.id.task_nav_active_text})
    public void onClick(View v) {
        if (v.getId() == R.id.task_nav_done_text) {
            mState = ConstantsManager.STATE_FLAG_DONE;
        } else if (v.getId() == R.id.task_nav_active_text) {
            mState = ConstantsManager.STATE_FLAG_ACTIVE;
        }
        setState();
    }

    /**
     * Change components of layout using state of task
     */
    private void setState() {
        if (mState == ConstantsManager.STATE_FLAG_ACTIVE) {
            stateActiveLine.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            stateDoneLine.setBackgroundColor(getResources().getColor(R.color.grey_two));
            stateActiveText.setTextColor(getResources().getColor(R.color.colorPrimary));
            stateDoneText.setTextColor(getResources().getColor(R.color.grey));
            stateTitleText.setText(mStateTitle[0]);
        } else if (mState == ConstantsManager.STATE_FLAG_DONE) {
            stateActiveLine.setBackgroundColor(getResources().getColor(R.color.grey_two));
            stateDoneLine.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            stateActiveText.setTextColor(getResources().getColor(R.color.grey));
            stateDoneText.setTextColor(getResources().getColor(R.color.colorPrimary));
            stateTitleText.setText(mStateTitle[1]);
        }
    }

    /**
     * Getter for all parameters of task
     */
    private void setDataForParametersViews() {
        for (int i = 0; i < parametersAboutText.length; i++) {
            parametersAboutText[i].setText(mTaskParameters[i]);
        }
        parameterTitleEditText.setText(mTitle);
        parameterTitleEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                mTitle = s.toString();
            }
        });
        setCategory();
        setDate();
        setTime(mHourBeginning, mMinuteBeginning, 0, null, R.drawable.ic_time, R.drawable.ic_time_grey);
        setTime(mHourEnd, mMinuteEnd, 1, null, R.drawable.ic_time, R.drawable.ic_time_grey);
        setTime(mAlarmHour, mAlarmMinute, 2, mAlarmState, R.drawable.ic_alarm_state, R.drawable.ic_alarm_state_grey);
    }

    /**
     * Changes state of EditText object
     */
    @OnClick(R.id.parameter_title_action_image)
    public void changeModeForEditText() {
        if (!parameterTitleEditText.isEnabled()) {
            setEditTextEnable();
        } else {
            parameterTitleEditText.setEnabled(false);
            parameterTitleEditText.setFocusable(false);
            parameterTitleEditText.setFocusableInTouchMode(false);
            parameterTitleLine.setBackgroundColor(getResources().getColor(R.color.white));
            parametersActionImage[0].setImageResource(R.drawable.ic_edit);
        }
    }

    /**
     * Changes state of EditText object on enabled
     */
    private void setEditTextEnable() {
        parameterTitleEditText.setEnabled(true);
        parameterTitleEditText.setFocusable(true);
        parameterTitleEditText.setFocusableInTouchMode(true);
        parameterTitleEditText.requestFocus();
        parameterTitleEditText.setSelection(parameterTitleEditText.getText().toString().length());
        parameterTitleLine.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        parametersActionImage[0].setImageResource(R.drawable.ic_done_circle);
    }

    /**
     * Sets category for task
     */
    private void setCategory() {
        parametersMainText[0].setText(mCategories[mCategoryId]);
        Integer image = R.drawable.ic_none_category;
        switch (mCategoryId) {
            case ConstantsManager.CATEGORIES_NONE:
                image = R.drawable.ic_none_category;
                break;
            case ConstantsManager.CATEGORIES_FAMILY:
                image = R.drawable.ic_family;
                break;
            case ConstantsManager.CATEGORIES_MEETINGS:
                image = R.drawable.ic_meetings;
                break;
            case ConstantsManager.CATEGORIES_HONE:
                image = R.drawable.ic_home;
                break;
            case ConstantsManager.CATEGORIES_SHOPPING:
                image = R.drawable.ic_shopping;
                break;
            case ConstantsManager.CATEGORIES_BIRTHDAY:
                image = R.drawable.ic_birthday;
                break;
            case ConstantsManager.CATEGORIES_BUSINESS:
                image = R.drawable.ic_business;
                break;
            case ConstantsManager.CATEGORIES_CALLS:
                image = R.drawable.ic_calls;
                break;
            case ConstantsManager.CATEGORIES_ENTERTAINMENTS:
                image = R.drawable.ic_entertainments;
                break;
            case ConstantsManager.CATEGORIES_EDUCATION:
                image = R.drawable.ic_education;
                break;
            case ConstantsManager.CATEGORIES_PETS:
                image = R.drawable.ic_pets;
                break;
            case ConstantsManager.CATEGORIES_PRIVATE_LIFE:
                image = R.drawable.ic_private_life;
                break;
            case ConstantsManager.CATEGORIES_REPAIR:
                image = R.drawable.ic_repair;
                break;
            case ConstantsManager.CATEGORIES_WORK:
                image = R.drawable.ic_work;
                break;
            case ConstantsManager.CATEGORIES_SPORT:
                image = R.drawable.ic_sport;
                break;
            case ConstantsManager.CATEGORIES_TRAVELLINGS:
                image = R.drawable.ic_travelling;
                break;
        }
        parametersImage[0].setImageResource(image);
    }

    /**
     * Sets date of task
     */
    private void setDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.setFirstDayOfWeek(Calendar.SUNDAY);
        calendar.set(Calendar.DAY_OF_MONTH, mDay);
        calendar.set(Calendar.YEAR, mYear);
        calendar.set(Calendar.MONTH, mMonth - 1);

        String date = mTitlesOfDays[calendar.get(Calendar.DAY_OF_WEEK) - 1] + ", " +
                String.format(Locale.ENGLISH, mDayOrMonthFormat + " ", mDay) +
                mTitlesOfMonths[mMonth - 1] + " " + mYear;
        parametersMainText[1].setText(date);
    }

    /**
     * Sets time of task
     *
     * @param hours      value of hour of day
     * @param minutes    value of minute of hour
     * @param position   position of layout components which will show time information
     * @param isActive   state of components which will show time information
     * @param image      image for enabled component
     * @param image_grey image for disabled component
     */
    private void setTime(int hours, int minutes, int position, Integer isActive, int image, int image_grey) {
        if (isActive == null) {
            if (hours != -1) {
                isActive = 1;
            } else {
                isActive = 0;
            }
        }
        if (isActive == 1) {
            parametersMainTextTwo[position].setTextColor(getResources().getColor(R.color.colorPrimary));
            parametersLayoutTwo[position].setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            parametersImage[position + 1].setImageResource(image);
            parametersActionImage[position + 1].setImageResource(R.drawable.ic_remove_circle);
        } else if (isActive == 0) {
            parametersMainTextTwo[position].setTextColor(getResources().getColor(R.color.grey));
            parametersLayoutTwo[position].setBackgroundColor(getResources().getColor(R.color.grey_two));
            parametersImage[position + 1].setImageResource(image_grey);
            parametersActionImage[position + 1].setImageResource(R.drawable.ic_add_circle);
        }
        parametersMainTextTwo[position].setText(AuxiliaryFunctions.convertToTimeFormat(hours, minutes));
    }

    /**
     * Change state of time components
     */
    private View.OnClickListener switchClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.parameter_beginning_action_image:
                    if (mHourBeginning != -1) {
                        mHourBeginning = mMinuteBeginning = mHourEnd = mMinuteEnd = -1;
                        setTime(mHourBeginning, mMinuteBeginning, 0, null, R.drawable.ic_time, R.drawable.ic_time_grey);
                        setTime(mHourEnd, mMinuteEnd, 1, null, R.drawable.ic_time, R.drawable.ic_time_grey);
                    } else {
                        changeTimeBeginning();
                    }
                    break;
                case R.id.parameter_end_action_image:
                    if (mHourEnd != -1) {
                        mHourEnd = mMinuteEnd = -1;
                        setTime(mHourEnd, mMinuteEnd, 1, null, R.drawable.ic_time, R.drawable.ic_time_grey);
                    } else {
                        changeTimeEnd();
                    }
                    break;
                case R.id.parameter_alarm_action_image:
                    if (mAlarmState == ConstantsManager.ALARM_FLAG_OFF) {
                        mAlarmState = ConstantsManager.ALARM_FLAG_ON;
                    } else {
                        mAlarmState = ConstantsManager.ALARM_FLAG_OFF;
                    }
                    setTime(mAlarmHour, mAlarmMinute, 2, mAlarmState, R.drawable.ic_alarm_state, R.drawable.ic_alarm_state_grey);
                    break;
            }

        }
    };

    /**
     * Changes category of task
     */
    @OnClick(R.id.parameter_category_main_layout)
    public void onClickCategory() {
        mDialogList = new DialogList(this, mTaskParameters[1], getDataForDialogList(),
                new AdapterOfDialogList.OnItemClickListener() {
                    @Override
                    public void onClick(int position) {
                        mCategoryId = position;
                        mDialogList.updateList(getDataForDialogList());
                        setCategory();
                        mDialogList.getDialog().dismiss();
                    }
                }, mCategoryId, null, ConstantsManager.DIALOG_LIST_TYPE_ONE);
    }

    /**
     * Performs data for DialogList object to change category id of task
     *
     * @return DataForDialogListItem list object
     */
    private List<DataForDialogListItem> getDataForDialogList() {
        List<DataForDialogListItem> result = new ArrayList<>();
        for (int i = 0; i < mCategories.length; i++) {
            if (i == mCategoryId) {
                result.add(new DataForDialogListItem(i, mCategories[i], true));
            } else {
                result.add(new DataForDialogListItem(i, mCategories[i], false));
            }
        }
        return result;
    }

    /**
     * Changes date of task
     */
    @OnClick(R.id.parameter_date_main_layout)
    public void onClickDate() {
        mDialogCalendar = new DialogCalendar(this, new DialogCalendar.OnChooseDay() {
            @Override
            public void function() {
                mDay = mDialogCalendar.getDayChosen();
                mMonth = mDialogCalendar.getMonthChosen();
                mYear = mDialogCalendar.getYearChosen();
                setDate();
                mDialogCalendar.getDialog().dismiss();
            }
        }, mYear, mMonth, mDay);

    }

    /**
     * Changes time of task beginning
     */
    @OnClick(R.id.parameter_beginning_main_layout)
    public void onClickBeginning() {
        changeTimeBeginning();
    }

    /**
     * Perform DialogTime object to change time of beginning
     */
    private void changeTimeBeginning() {
        mDialogTime = new DialogTime(this, mTaskParameters[3], mHourBeginning, mMinuteBeginning,
                new DialogTime.OnChooseTime() {
                    @Override
                    public void function() {
                        int hours = mDialogTime.getHours();
                        if (mDialogTime.getTimeStyle() != null) {
                            if (mDialogTime.getTimeStyle().equals(ConstantsManager.TIME_STYLE_PM)) {
                                hours = mDialogTime.getHours() + 12;
                            }
                        }
                        int minutes = mDialogTime.getMinutes();
                        if (AuxiliaryFunctions.isCorrectTime(hours, minutes,
                                mHourEnd, mMinuteEnd)) {
                            mDialogTime.getDialog().dismiss();
                            mHourBeginning = hours;
                            mMinuteBeginning = minutes;
                            setTime(mHourBeginning, mMinuteBeginning, 0, null, R.drawable.ic_time, R.drawable.ic_time_grey);
                        } else {
                            showToast(mEndTimeToast[1], R.drawable.ic_time_white,
                                    R.drawable.rectangle_for_toast_red, Toast.LENGTH_LONG);
                        }
                    }
                });
    }

    /**
     * Changes time of task end
     */
    @OnClick(R.id.parameter_end_main_layout)
    public void onClickEnd() {
        changeTimeEnd();
    }

    /**
     * Perform DialogTime object to change time of end
     */
    private void changeTimeEnd() {
        if (mHourBeginning != -1) {
            int hours = mHourEnd, minutes = mMinuteEnd;
            if (hours == -1) {
                hours = mHourBeginning;
                minutes = mMinuteBeginning;
            }
            mDialogTime = new DialogTime(this, mTaskParameters[4], hours, minutes,
                    new DialogTime.OnChooseTime() {
                        @Override
                        public void function() {
                            int hours = mDialogTime.getHours();
                            if (mDialogTime.getTimeStyle() != null) {
                                if (mDialogTime.getTimeStyle().equals(ConstantsManager.TIME_STYLE_PM)) {
                                    hours = mDialogTime.getHours() + 12;
                                }
                            }
                            int minutes = mDialogTime.getMinutes();
                            if (AuxiliaryFunctions.isCorrectTime(mHourBeginning, mMinuteBeginning,
                                    hours, minutes)) {
                                mDialogTime.getDialog().dismiss();
                                mHourEnd = hours;
                                mMinuteEnd = minutes;
                                setTime(mHourEnd, mMinuteEnd, 1, null, R.drawable.ic_time, R.drawable.ic_time_grey);
                            } else {
                                showToast(mEndTimeToast[1], R.drawable.ic_time_white,
                                        R.drawable.rectangle_for_toast_red, Toast.LENGTH_LONG);
                            }
                        }
                    });
        } else {
            showToast(mEndTimeToast[0], R.drawable.ic_time_white,
                    R.drawable.rectangle_for_toast_red, Toast.LENGTH_LONG);
        }
    }

    /**
     * Changes time of alarm clock
     */
    @OnClick(R.id.parameter_alarm_main_layout)
    public void onClickAlarm() {
        changeTimeAlarm();
    }

    /**
     * Perform DialogTime object to change time of alarm clock
     */
    private void changeTimeAlarm() {
        mDialogTime = new DialogTime(this, mTaskParameters[5], mAlarmHour, mAlarmMinute,
                new DialogTime.OnChooseTime() {
                    @Override
                    public void function() {
                        mAlarmHour = mDialogTime.getHours();
                        if (mDialogTime.getTimeStyle() != null) {
                            if (mDialogTime.getTimeStyle().equals(ConstantsManager.TIME_STYLE_PM)) {
                                mAlarmHour = mDialogTime.getHours() + 12;
                            }
                        }
                        mAlarmMinute = mDialogTime.getMinutes();
                        mDialogTime.getDialog().dismiss();
                        mAlarmState = ConstantsManager.ALARM_FLAG_ON;
                        setTime(mAlarmHour, mAlarmMinute, 2, mAlarmState, R.drawable.ic_alarm_state, R.drawable.ic_alarm_state_grey);
                    }
                });
    }

    /**
     * Closes Activity
     */
    @Override
    public void onBackPressed() {
        String text;
        if (mId.equals(ConstantsManager.TASK_ID_NONE)) {
            text = mDialogDoneText[0];
        } else {
            text = mDialogDoneText[1];
        }
        new DialogTwoButtons(this, mTitle, text, mDialogDoneButtonsTitle,
                new DialogTwoButtons.OnButtonClick() {
                    @Override
                    public void function() {
                        finish();
                    }
                }, new DialogTwoButtons.OnButtonClick() {
            @Override
            public void function() {
                clickFab();
            }
        });
    }

    /**
     * Creates new task or updates existed task using all parameters.
     * Adds new or changes existed item from db.
     */
    @OnClick(R.id.task_fab)
    public void clickFab() {
        DatabaseManager databaseManager = DataManager.getInstance().getDatabaseManager();
        databaseManager.updateOrAddTask(
                new TaskEntity(databaseManager.getTaskNewId(mId), mTitle, mState, mCategoryId, mDay, mMonth, mYear,
                        mHourBeginning, mMinuteBeginning, mHourEnd, mMinuteEnd,
                        mAlarmState, mAlarmHour, mAlarmMinute));
        finish();
    }

    /**
     * Performs text objects using language parameter
     */
    private void setLang() {
        new LanguageManager() {
            @Override
            public void engLanguage() {
                mDayOrMonthFormat = "%d";
                mToolbarTitle = getResources().getStringArray(R.array.edit_create_task_title_eng);
                mTaskParameters = getResources().getStringArray(R.array.task_parameters_eng);
                stateActiveText.setText(getResources()
                        .getStringArray(R.array.task_list_header_navigation_titles_eng)[0]);
                stateDoneText.setText(getResources()
                        .getStringArray(R.array.task_list_header_navigation_titles_eng)[1]);
                mStateTitle = getResources().getStringArray(R.array.state_task_titles_eng);
                mCategories = getResources().getStringArray(R.array.categories_eng);
                mTitlesOfDays = getResources().getStringArray(R.array.day_titles_eng);
                mTitlesOfMonths = getResources().getStringArray(R.array.month_titles_eng);
                mEndTimeToast = getResources().getStringArray(R.array.toast_add_beginning_eng);
                mDialogDoneText = getResources().getStringArray(R.array.save_question_eng);
                mDialogDoneButtonsTitle = getResources().getStringArray(R.array.no_yes_eng);
            }

            @Override
            public void ukrLanguage() {
                mToolbarTitle = getResources().getStringArray(R.array.edit_create_task_title_ukr);
                mTaskParameters = getResources().getStringArray(R.array.task_parameters_ukr);
                stateActiveText.setText(getResources()
                        .getStringArray(R.array.task_list_header_navigation_titles_ukr)[0]);
                stateDoneText.setText(getResources()
                        .getStringArray(R.array.task_list_header_navigation_titles_ukr)[1]);
                mStateTitle = getResources().getStringArray(R.array.state_task_titles_ukr);
                mCategories = getResources().getStringArray(R.array.categories_ukr);
                mTitlesOfDays = getResources().getStringArray(R.array.day_titles_ukr);
                mTitlesOfMonths = getResources().getStringArray(R.array.month_titles_ukr);
                mEndTimeToast = getResources().getStringArray(R.array.toast_add_beginning_ukr);
                mDialogDoneText = getResources().getStringArray(R.array.save_question_ukr);
                mDialogDoneButtonsTitle = getResources().getStringArray(R.array.no_yes_ukr);
            }

            @Override
            public void rusLanguage() {
                mToolbarTitle = getResources().getStringArray(R.array.edit_create_task_title_rus);
                stateActiveText.setText(getResources()
                        .getStringArray(R.array.task_list_header_navigation_titles_rus)[0]);
                stateDoneText.setText(getResources()
                        .getStringArray(R.array.task_list_header_navigation_titles_rus)[1]);
                mStateTitle = getResources().getStringArray(R.array.state_task_titles_rus);
                mTaskParameters = getResources().getStringArray(R.array.task_parameters_rus);
                mCategories = getResources().getStringArray(R.array.categories_rus);
                mTitlesOfDays = getResources().getStringArray(R.array.day_titles_rus);
                mTitlesOfMonths = getResources().getStringArray(R.array.month_titles_rus);
                mEndTimeToast = getResources().getStringArray(R.array.toast_add_beginning_rus);
                mDialogDoneText = getResources().getStringArray(R.array.save_question_rus);
                mDialogDoneButtonsTitle = getResources().getStringArray(R.array.no_yes_rus);
            }
        };
    }
}
