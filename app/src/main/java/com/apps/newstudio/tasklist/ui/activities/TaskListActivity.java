package com.apps.newstudio.tasklist.ui.activities;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.support.v7.widget.Toolbar;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.apps.newstudio.tasklist.R;
import com.apps.newstudio.tasklist.data.adapters.AdapterOfFragmentActiveDoneList;
import com.apps.newstudio.tasklist.data.adapters.AdapterOfFragmentTasksList;
import com.apps.newstudio.tasklist.data.adapters.DataForActiveDoneListItem;
import com.apps.newstudio.tasklist.data.adapters.DataForTasksListItem;
import com.apps.newstudio.tasklist.data.adapters.ItemOfFragmentListTouchHelperAdapter;
import com.apps.newstudio.tasklist.data.managers.DataManager;
import com.apps.newstudio.tasklist.data.managers.DatabaseManager;
import com.apps.newstudio.tasklist.data.managers.LanguageManager;
import com.apps.newstudio.tasklist.data.storage.models.TaskEntity;
import com.apps.newstudio.tasklist.ui.dialogs.DialogCalendar;
import com.apps.newstudio.tasklist.utils.AuxiliaryFunctions;
import com.apps.newstudio.tasklist.utils.ConstantsManager;
import com.apps.newstudio.tasklist.utils.OnClickAdapter;
import com.apps.newstudio.tasklist.utils.TaskListApplication;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class TaskListActivity extends BaseActivity {

    @BindView(R.id.toolbar)
    public Toolbar mToolbar;

    @BindView(R.id.fragment_list)
    public RecyclerView mRecyclerView;

    @BindView(R.id.fragment_header_text)
    public TextView mHeaderText;

    @BindView(R.id.fragment_nav_active_text)
    public TextView navActiveText;

    @BindView(R.id.fragment_nav_active_line)
    public View navActiveLine;

    @BindView(R.id.fragment_nav_done_text)
    public TextView navDoneText;

    @BindView(R.id.fragment_nav_done_line)
    public View navDoneLine;

    private List<DataForTasksListItem> mMainDataForListActive;
    private List<DataForTasksListItem> mMainDataForListDone;
    private List<DataForActiveDoneListItem> mMainData;

    private int mChosenDay, mChosenMonth, mChosenYear, mStateFlag;

    private String mDayOrMonthFormat = "%02d";
    private String[] mYesterdayTodayTomorrowMask = new String[3];
    private String[] mYesterdayTodayTomorrowTitles, mTitlesOfDays, mTitlesOfMonths, mDeleteDoneBackToastTitle;
    private String[] mEmptyListTitle;

    private DatabaseManager mDatabaseManager;

    private DialogCalendar mDialogCalendar;

    private int firstColor = R.color.colorPrimary, secondColor = R.color.grey_two, thirdColor = R.color.grey;
    private int headerIco = R.drawable.ic_events;

    /**
     * Perform initialization of all fragments.
     *
     * @param savedInstanceState Bundle object of saved values
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null) {
            Calendar calendar = Calendar.getInstance();
            mChosenDay = getIntent().getIntExtra(ConstantsManager.CHOSEN_DAY_KEY,
                    calendar.get(Calendar.DAY_OF_MONTH));
            mChosenMonth = getIntent().getIntExtra(ConstantsManager.CHOSEN_MONTH_KEY,
                    calendar.get(Calendar.MONTH) + 1);
            mChosenYear = getIntent().getIntExtra(ConstantsManager.CHOSEN_YEAR_KEY,
                    calendar.get(Calendar.YEAR));
            mStateFlag = getIntent().getIntExtra(ConstantsManager.TASK_STATE_FLAG,
                    ConstantsManager.STATE_FLAG_ACTIVE);
            Log.d("mStateFlag", "" + mStateFlag);
        } else {
            mChosenDay = savedInstanceState.getInt(ConstantsManager.CHOSEN_DAY_KEY);
            mChosenMonth = savedInstanceState.getInt(ConstantsManager.CHOSEN_MONTH_KEY);
            mChosenYear = savedInstanceState.getInt(ConstantsManager.CHOSEN_YEAR_KEY);
            mStateFlag = savedInstanceState.getInt(ConstantsManager.TASK_STATE_FLAG);
        }

        setContentView(R.layout.activity_task_list);
        ButterKnife.bind(this);
        setLang();
        setupToolbar();
        mDatabaseManager = DataManager.getInstance().getDatabaseManager();
        createList();
        navActiveText.setOnClickListener(mOnClickListenerActiveDone);
        navDoneText.setOnClickListener(mOnClickListenerActiveDone);
        checkList();
    }

    /**
     * Perform Toolbar object
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
     * Closes Activity
     */
    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra(ConstantsManager.CHOSEN_MONTH_KEY, mChosenMonth);
        intent.putExtra(ConstantsManager.CHOSEN_YEAR_KEY, mChosenYear);
        intent.putExtra(ConstantsManager.TASK_STATE_FLAG, mStateFlag);
        setResult(1, intent);
        finish();
    }

    /**
     * Saves data in Bundle object
     *
     * @param outState Bundle object
     */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putInt(ConstantsManager.CHOSEN_DAY_KEY, mChosenDay);
        outState.putInt(ConstantsManager.CHOSEN_MONTH_KEY, mChosenMonth);
        outState.putInt(ConstantsManager.CHOSEN_YEAR_KEY, mChosenYear);
        outState.putInt(ConstantsManager.TASK_STATE_FLAG, mStateFlag);
        super.onSaveInstanceState(outState);
    }

    /**
     * Action to open new Activity object for adding or editing main information of task
     */
    @OnClick(R.id.fragment_fab_layout)
    public void onClickFab() {
        Intent intent = new Intent(this, TaskActivity.class);
        intent.putExtra(ConstantsManager.TASK_ID_FLAG, ConstantsManager.TASK_ID_NONE);
        intent.putExtra(ConstantsManager.TASK_STATE_FLAG, mStateFlag);
        intent.putExtra(ConstantsManager.TASK_CATEGORY_FLAG, ConstantsManager.CATEGORIES_NONE);
        intent.putExtra(ConstantsManager.TASK_DAY_FLAG, mChosenDay);
        intent.putExtra(ConstantsManager.TASK_MONTH_FLAG, mChosenMonth);
        intent.putExtra(ConstantsManager.TASK_YEAR_FLAG, mChosenYear);
        intent.putExtra(ConstantsManager.ALARM_FLAG, ConstantsManager.ALARM_FLAG_ON);
        intent.putExtra(ConstantsManager.ALARM_HOUR_FLAG,
                DataManager.getInstance().getPreferenceManager()
                        .loadInt(ConstantsManager.TOTAL_ALARM_HOUR_FLAG, ConstantsManager.TOTAL_ALARM_HOUR_DEFAULT));
        intent.putExtra(ConstantsManager.ALARM_MINUTE_FLAG,
                DataManager.getInstance().getPreferenceManager()
                        .loadInt(ConstantsManager.TOTAL_ALARM_MINUTE_FLAG, ConstantsManager.TOTAL_ALARM_MINUTE_DEFAULT));
        startActivityForResult(intent, ConstantsManager.TASK_REQUEST_CODE);
    }

    /**
     * Action to show active or finished tasks
     */
    private View.OnClickListener mOnClickListenerActiveDone = new OnClickAdapter(1, 1) {
        @Override
        public void myFunc(View view) {
            if (mStateFlag == ConstantsManager.STATE_FLAG_ACTIVE && view.getId() == R.id.fragment_nav_done_text) {
                mStateFlag = ConstantsManager.STATE_FLAG_DONE;
                mRecyclerView.setLayoutFrozen(false);
                mRecyclerView.smoothScrollToPosition(1);
                checkList();
            } else if (mStateFlag == ConstantsManager.STATE_FLAG_DONE && view.getId() == R.id.fragment_nav_active_text) {
                mStateFlag = ConstantsManager.STATE_FLAG_ACTIVE;
                mRecyclerView.setLayoutFrozen(false);
                mRecyclerView.smoothScrollToPosition(0);
                checkList();
            }
        }
    };

    /**
     * Changes components of fragment if state of shown tasks is changed
     *
     * @param state state of tasks
     */
    private void updateState(int state) {
        switch (state) {
            case ConstantsManager.STATE_FLAG_ACTIVE:
                navActiveLine.setBackgroundColor(getResources().getColor(firstColor));
                navDoneLine.setBackgroundColor(getResources().getColor(secondColor));
                navActiveText.setTextColor(getResources().getColor(firstColor));
                navDoneText.setTextColor(getResources().getColor(thirdColor));
                break;
            case ConstantsManager.STATE_FLAG_DONE:
                navActiveLine.setBackgroundColor(getResources().getColor(secondColor));
                navDoneLine.setBackgroundColor(getResources().getColor(firstColor));
                navActiveText.setTextColor(getResources().getColor(thirdColor));
                navDoneText.setTextColor(getResources().getColor(firstColor));
                break;
        }
        findViewById(R.id.fragment_nav_other_line).setBackgroundColor(getResources().getColor(secondColor));
    }

    /**
     * Sets masks of time
     */
    private void getToday() {
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH) + 1;
        int year = calendar.get(Calendar.YEAR);
        int todayDay = calendar.get(Calendar.DAY_OF_MONTH);
        mYesterdayTodayTomorrowMask[1] = String.format(Locale.ENGLISH, mDayOrMonthFormat + "." + mDayOrMonthFormat + ".%04d",
                day, month, year);

        calendar.set(Calendar.DAY_OF_MONTH, todayDay - 1);
        mYesterdayTodayTomorrowMask[0] = String.format(Locale.ENGLISH, mDayOrMonthFormat + "." + mDayOrMonthFormat + ".%04d",
                calendar.get(Calendar.DAY_OF_MONTH), calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.YEAR));

        calendar.set(Calendar.DAY_OF_MONTH, todayDay + 1);
        mYesterdayTodayTomorrowMask[2] = String.format(Locale.ENGLISH, mDayOrMonthFormat + "." + mDayOrMonthFormat + ".%04d",
                calendar.get(Calendar.DAY_OF_MONTH), calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.YEAR));

        changeHeader();
    }

    /**
     * Sets information and visual style of header components
     */
    private void changeHeader() {
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

        mHeaderText.setTextColor(getResources().getColor(secondColor));
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(result);
        spannableStringBuilder.setSpan(
                new ForegroundColorSpan(TaskListApplication.getContext().getResources().getColor(firstColor)),
                0, lengthOfMainWord, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableStringBuilder.setSpan(new StyleSpan(Typeface.BOLD), 0, lengthOfMainWord, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        mHeaderText.setText(spannableStringBuilder);
        ((ImageView) findViewById(R.id.fragment_header_ico)).setImageResource(headerIco);
    }

    /**
     * Prepares data for list of active tasks
     */
    private void prepareDataForListActive() {
        mMainDataForListActive = mDatabaseManager
                .getTasksUsingDate(mChosenDay, mChosenMonth, mChosenYear, ConstantsManager.STATE_FLAG_ACTIVE);
        for (DataForTasksListItem data : mMainDataForListActive) {
            data.setNow(AuxiliaryFunctions.isActualTask(data, getTime()));
        }
    }

    /**
     * Prepares data for list of finished tasks
     */
    private void prepareDataForListDone() {
        mMainDataForListDone = mDatabaseManager
                .getTasksUsingDate(mChosenDay, mChosenMonth, mChosenYear, ConstantsManager.STATE_FLAG_DONE);
    }

    /**
     * Creates String object from day, month, year, hour and minute values using special mask
     *
     * @return String object of date
     */
    private Long getTime() {
        Calendar calendar = GregorianCalendar.getInstance();
        return Long.parseLong(String.format(Locale.ENGLISH, "%04d%02d%02d%02d%02d", calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.DAY_OF_MONTH)
                , calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE)));
    }


    /**
     * Creates main list using
     */
    private void createList() {
        prepareDataForListActive();
        prepareDataForListDone();
        ItemOfFragmentListTouchHelperAdapter adapter = new ItemOfFragmentListTouchHelperAdapter() {
            @Override
            public void onItemDismiss(int position) {
                removeFromListAndDb(position);
            }

            @Override
            public void onItemDoneOrActive(int position) {
                mDatabaseManager.changeStateInBD(
                        mMainData.get(mStateFlag).getAdapter().getData().get(position).getId());
                removeFromList(position);
                changeStateOfTask();
            }
        };
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(TaskListApplication.getContext(),
                LinearLayoutManager.HORIZONTAL, false);
        SnapHelper snapHelper = new PagerSnapHelper();
        mRecyclerView.setLayoutManager(linearLayoutManager);
        snapHelper.attachToRecyclerView(mRecyclerView);

        mMainData = new ArrayList<>();

        mMainData.add(new DataForActiveDoneListItem(new AdapterOfFragmentTasksList(mMainDataForListActive,
                true, mYesterdayTodayTomorrowTitles, mYesterdayTodayTomorrowMask,
                mOnItemClickListenerOpen, mOnItemClickListenerDelete)));
        mMainData.add(new DataForActiveDoneListItem(new AdapterOfFragmentTasksList(mMainDataForListDone,
                true, mYesterdayTodayTomorrowTitles, mYesterdayTodayTomorrowMask,
                mOnItemClickListenerOpen, mOnItemClickListenerDelete)));

        AdapterOfFragmentActiveDoneList adapterOfFragmentActiveDoneList = new AdapterOfFragmentActiveDoneList(mMainData, adapter);
        mRecyclerView.setAdapter(adapterOfFragmentActiveDoneList);

        mRecyclerView.scrollToPosition(mStateFlag);

        mRecyclerView.setLayoutFrozen(true);
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    mRecyclerView.setLayoutFrozen(true);
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });
    }

    /**
     * Removes item from main list
     *
     * @param position position of item which will be removed
     */
    private void removeFromList(int position) {
        mRecyclerView.setLayoutFrozen(false);
        mMainData.get(mStateFlag).getAdapter().getData().remove(position);
        mMainData.get(mStateFlag).getAdapter().notifyItemRemoved(position);
        mMainData.get(mStateFlag).getAdapter().notifyItemChanged(position - 1);
        mRecyclerView.setLayoutFrozen(true);
        checkList();
    }

    /**
     * Remove some item from list and db
     *
     * @param position position of item which will be deleted
     */
    private void removeFromListAndDb(int position) {
        mDatabaseManager.removeFromDB(mMainData.get(mStateFlag).getAdapter().getData().get(position).getId());

        if (mMainData.get(mStateFlag).getAdapter().getData().size() != 1) {
            removeFromList(position);
        } else {
            mRecyclerView.setLayoutFrozen(false);
            updateListActive();
            updateListDone();
            mRecyclerView.setLayoutFrozen(true);
            checkList();
        }
        showToast(mDeleteDoneBackToastTitle[0], R.drawable.ic_delete_uncircle_white,
                R.drawable.rectangle_for_toast_red, Toast.LENGTH_SHORT);
    }

    /**
     * Performs AdapterOfFragmentTasksList.OnItemClickListener object for deleting from db and list
     */
    private AdapterOfFragmentTasksList.OnItemClickListener mOnItemClickListenerDelete
            = new AdapterOfFragmentTasksList.OnItemClickListener() {
        @Override
        public void onClick(int position) {
            removeFromListAndDb(position);
        }
    };

    /**
     * Performs AdapterOfFragmentTasksList.OnItemClickListener object for opening detail information of task
     */
    private AdapterOfFragmentTasksList.OnItemClickListener mOnItemClickListenerOpen
            = new AdapterOfFragmentTasksList.OnItemClickListener() {
        @Override
        public void onClick(int position) {
            TaskEntity task = mDatabaseManager
                    .getTaskEntityById(mMainData.get(mStateFlag).getAdapter().getData().get(position).getId());
            Intent intent = new Intent(TaskListActivity.this, TaskActivity.class);
            intent.putExtra(ConstantsManager.TASK_ID_FLAG, task.getId());
            intent.putExtra(ConstantsManager.TASK_TITLE_FLAG, task.getTitle());
            intent.putExtra(ConstantsManager.TASK_STATE_FLAG, mStateFlag);
            intent.putExtra(ConstantsManager.TASK_CATEGORY_FLAG, task.getCategoryId());
            intent.putExtra(ConstantsManager.TASK_DAY_FLAG, mChosenDay);
            intent.putExtra(ConstantsManager.TASK_MONTH_FLAG, mChosenMonth);
            intent.putExtra(ConstantsManager.TASK_YEAR_FLAG, mChosenYear);
            intent.putExtra(ConstantsManager.TASK_HOUR_BEGINNING_FLAG, task.getHourBeginning());
            intent.putExtra(ConstantsManager.TASK_MINUTE_BEGINNING_FLAG, task.getMinuteBeginning());
            intent.putExtra(ConstantsManager.TASK_HOUR_END_FLAG, task.getHourEnd());
            intent.putExtra(ConstantsManager.TASK_MINUTE_END_FLAG, task.getMinuteEnd());
            intent.putExtra(ConstantsManager.ALARM_FLAG, task.getAlarmState());
            intent.putExtra(ConstantsManager.ALARM_HOUR_FLAG, task.getAlarmHour());
            intent.putExtra(ConstantsManager.ALARM_MINUTE_FLAG, task.getAlarmMinute());
            startActivityForResult(intent, ConstantsManager.TASK_REQUEST_CODE);
        }
    };

    /**
     * Function for changing state of task from list
     */
    public void changeStateOfTask() {
        mRecyclerView.setLayoutFrozen(false);
        String toastString = mDeleteDoneBackToastTitle[1];
        int toastImage = R.drawable.ic_done;
        if (mStateFlag == ConstantsManager.STATE_FLAG_DONE) {
            toastImage = R.drawable.ic_back;
            toastString = mDeleteDoneBackToastTitle[2];
            updateListActive();
        } else {
            updateListDone();
        }
        showToast(toastString, toastImage,
                R.drawable.rectangle_for_toast_red, Toast.LENGTH_SHORT);
        mRecyclerView.setLayoutFrozen(true);
    }

    /**
     * Updates data from list of active tasks
     */
    public void updateListActive() {
        prepareDataForListActive();
        mMainData.get(0).getAdapter().setData(mMainDataForListActive);
        mMainData.get(0).getAdapter().notifyDataSetChanged();
    }

    /**
     * Updates data from list of finished tasks
     */
    public void updateListDone() {
        prepareDataForListDone();
        mMainData.get(1).getAdapter().setData(mMainDataForListDone);
        mMainData.get(1).getAdapter().notifyDataSetChanged();
    }


    /**
     * Updates list after adding or changing information of tasks
     *
     * @param requestCode int value for request code
     * @param resultCode  int value for result code
     * @param data        Intent object which contains information from closed activity object
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == ConstantsManager.TASK_REQUEST_CODE) {
            mRecyclerView.setLayoutFrozen(false);
            updateListActive();
            updateListDone();
            mRecyclerView.setLayoutFrozen(true);
            checkList();
        }
    }

    /**
     * Changes category of shown tasks from list
     */
    @OnClick(R.id.fragment_header_layout)
    public void changeDate() {
        mDialogCalendar = new DialogCalendar(this, new DialogCalendar.OnChooseDay() {
            @Override
            public void function() {
                mChosenDay = mDialogCalendar.getDayChosen();
                mChosenMonth = mDialogCalendar.getMonthChosen();
                mChosenYear = mDialogCalendar.getYearChosen();
                mRecyclerView.setLayoutFrozen(false);
                updateListActive();
                updateListDone();
                mRecyclerView.setLayoutFrozen(true);
                mDialogCalendar.getDialog().dismiss();
                checkList();
            }
        }, mChosenYear, mChosenMonth, mChosenDay);
    }

    /**
     * Changes style of main layout if main list is empty
     */
    private void checkList() {
        int fab_layout_id = R.layout.fab_one;
        String text = "";
        switch (mMainData.get(mStateFlag).getAdapter().getData().size()) {
            case 0:
                firstColor = R.color.white;
                secondColor = R.color.white_two;
                thirdColor = R.color.white_three;
                headerIco = R.drawable.ic_events_white;
                findViewById(R.id.fragment_main_layout)
                        .setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                fab_layout_id = R.layout.fab_two;
                ((ImageView) findViewById(R.id.fragment_empty_iv)).setImageResource(R.drawable.ico_empty_main);
                if (mStateFlag == ConstantsManager.STATE_FLAG_ACTIVE) {
                    text = mEmptyListTitle[0];
                } else {
                    text = mEmptyListTitle[1];
                }
                break;
            default:
                firstColor = R.color.colorPrimary;
                secondColor = R.color.grey_two;
                thirdColor = R.color.grey;
                headerIco = R.drawable.ic_events;
                findViewById(R.id.fragment_main_layout)
                        .setBackgroundColor(getResources().getColor(R.color.white));
                ((ImageView) findViewById(R.id.fragment_empty_iv)).setImageResource(R.drawable.ic_tr);
                break;
        }
        ((LinearLayout) findViewById(R.id.fragment_fab_layout)).removeAllViews();
        LayoutInflater.from(TaskListActivity.this).inflate(fab_layout_id,
                ((LinearLayout) findViewById(R.id.fragment_fab_layout)), true);
        ((TextView) findViewById(R.id.fragment_empty_tv)).setText(text);

        updateState(mStateFlag);
        getToday();
    }

    /**
     * Performs text objects using language parameter
     */
    private void setLang() {
        new LanguageManager() {
            @Override
            public void engLanguage() {
                mDayOrMonthFormat = "%d";
                mYesterdayTodayTomorrowTitles = getResources().getStringArray(R.array.yesterday_today_tomorrow_eng);
                mTitlesOfDays = getResources().getStringArray(R.array.day_titles_eng);
                mTitlesOfMonths = getResources().getStringArray(R.array.month_titles_eng);
                navActiveText.setText(getResources().getStringArray(R.array.task_list_header_navigation_titles_eng)[0]);
                navDoneText.setText(getResources().getStringArray(R.array.task_list_header_navigation_titles_eng)[1]);
                mDeleteDoneBackToastTitle = getResources().getStringArray(R.array.delete_done_back_eng);
                mEmptyListTitle = getResources().getStringArray(R.array.list_empty_eng);
                mToolbar.setTitle(getResources().getStringArray(R.array.drawer_menu_items_eng)[0]);
            }

            @Override
            public void ukrLanguage() {
                mYesterdayTodayTomorrowTitles = getResources().getStringArray(R.array.yesterday_today_tomorrow_ukr);
                mTitlesOfDays = getResources().getStringArray(R.array.day_titles_ukr);
                mTitlesOfMonths = getResources().getStringArray(R.array.month_titles_ukr);
                navActiveText.setText(getResources().getStringArray(R.array.task_list_header_navigation_titles_ukr)[0]);
                navDoneText.setText(getResources().getStringArray(R.array.task_list_header_navigation_titles_ukr)[1]);
                mDeleteDoneBackToastTitle = getResources().getStringArray(R.array.delete_done_back_ukr);
                mEmptyListTitle = getResources().getStringArray(R.array.list_empty_ukr);
                mToolbar.setTitle(getResources().getStringArray(R.array.drawer_menu_items_ukr)[0]);
            }

            @Override
            public void rusLanguage() {
                mYesterdayTodayTomorrowTitles = getResources().getStringArray(R.array.yesterday_today_tomorrow_rus);
                mTitlesOfDays = getResources().getStringArray(R.array.day_titles_rus);
                mTitlesOfMonths = getResources().getStringArray(R.array.month_titles_rus);
                navActiveText.setText(getResources().getStringArray(R.array.task_list_header_navigation_titles_rus)[0]);
                navDoneText.setText(getResources().getStringArray(R.array.task_list_header_navigation_titles_rus)[1]);
                mDeleteDoneBackToastTitle = getResources().getStringArray(R.array.delete_done_back_rus);
                mEmptyListTitle = getResources().getStringArray(R.array.list_empty_rus);
                mToolbar.setTitle(getResources().getStringArray(R.array.drawer_menu_items_rus)[0]);

            }
        };
    }


}
