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
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.apps.newstudio.tasklist.R;
import com.apps.newstudio.tasklist.data.adapters.AdapterOfDialogList;
import com.apps.newstudio.tasklist.data.adapters.AdapterOfFragmentActiveDoneList;
import com.apps.newstudio.tasklist.data.adapters.AdapterOfFragmentTasksList;
import com.apps.newstudio.tasklist.data.adapters.DataForActiveDoneListItem;
import com.apps.newstudio.tasklist.data.adapters.DataForDialogListItem;
import com.apps.newstudio.tasklist.data.adapters.DataForTasksListItem;
import com.apps.newstudio.tasklist.data.adapters.ItemOfFragmentListTouchHelperAdapter;
import com.apps.newstudio.tasklist.data.managers.DataManager;
import com.apps.newstudio.tasklist.data.managers.DatabaseManager;
import com.apps.newstudio.tasklist.data.managers.LanguageManager;
import com.apps.newstudio.tasklist.data.storage.models.TaskEntity;
import com.apps.newstudio.tasklist.ui.dialogs.DialogList;
import com.apps.newstudio.tasklist.utils.AuxiliaryFunctions;
import com.apps.newstudio.tasklist.utils.ConstantsManager;
import com.apps.newstudio.tasklist.utils.TaskListApplication;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CategoryActivity extends BaseActivity {

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

    private int mCategoryId;

    private List<DataForTasksListItem> mMainDataForListActive;
    private List<DataForTasksListItem> mMainDataForListDone;
    private List<DataForActiveDoneListItem> mMainData;

    private int mChosenDay, mChosenMonth, mChosenYear, mStateFlag;

    private String mDayOrMonthFormat = "%02d", mParameter;
    private String[] mYesterdayTodayTomorrowMask = new String[3];
    private String[] mYesterdayTodayTomorrowTitles, mTitlesOfCategories, mDeleteDoneBackToastTitle;
    private String[] mEmptyListTitle;

    private DialogList mDialogList;
    private DatabaseManager mDatabaseManager;
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
        setContentView(R.layout.activity_category);
        ButterKnife.bind(this);
        initParameters(savedInstanceState);
        mDatabaseManager = DataManager.getInstance().getDatabaseManager();
        setLang();
        setupToolbar();
        getToday();
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
     * Perform all needed objects
     *
     * @param savedInstanceState Bundle object
     */
    private void initParameters(Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            mCategoryId = getIntent().getIntExtra(ConstantsManager.TASK_CATEGORY_FLAG, 0);
            mStateFlag = ConstantsManager.STATE_FLAG_ACTIVE;
        } else {
            mCategoryId = savedInstanceState.getInt(ConstantsManager.TASK_CATEGORY_FLAG);
            mStateFlag = savedInstanceState.getInt(ConstantsManager.TASK_STATE_FLAG);
        }
        Calendar calendar = Calendar.getInstance();
        mChosenDay = calendar.get(Calendar.DAY_OF_MONTH);
        mChosenMonth = calendar.get(Calendar.MONTH) + 1;
        mChosenYear = calendar.get(Calendar.YEAR);
    }

    /**
     * Initialize date values and masks
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
     * Changes text parameter of header TextView Object
     */
    private void changeHeader() {
        mHeaderText.setTextColor(getResources().getColor(firstColor));
        mHeaderText.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
        ((ImageView) findViewById(R.id.fragment_header_ico)).setImageResource(headerIco);
        mHeaderText.setText(mTitlesOfCategories[mCategoryId]);


    }

    /**
     * Opens activity to add new task
     */
    @OnClick(R.id.fragment_fab_layout)
    public void onClickFab() {
        Intent intent = new Intent(this, TaskActivity.class);
        intent.putExtra(ConstantsManager.TASK_ID_FLAG, ConstantsManager.TASK_ID_NONE);
        intent.putExtra(ConstantsManager.TASK_STATE_FLAG, mStateFlag);
        intent.putExtra(ConstantsManager.TASK_CATEGORY_FLAG, mCategoryId);
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
     * Changes value of task states
     */
    private View.OnClickListener mOnClickListenerActiveDone = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (mStateFlag == ConstantsManager.STATE_FLAG_ACTIVE && v.getId() == R.id.fragment_nav_done_text) {
                mStateFlag = ConstantsManager.STATE_FLAG_DONE;
                mRecyclerView.setLayoutFrozen(false);
                mRecyclerView.smoothScrollToPosition(1);
                checkList();
            } else if (mStateFlag == ConstantsManager.STATE_FLAG_DONE && v.getId() == R.id.fragment_nav_active_text) {
                mStateFlag = ConstantsManager.STATE_FLAG_ACTIVE;
                mRecyclerView.setLayoutFrozen(false);
                mRecyclerView.smoothScrollToPosition(0);
                checkList();
            }
        }
    };

    /**
     * Updates all components using state value
     *
     * @param state state of shown tasks
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
     * Prepares data for list of active tasks
     */
    private void prepareDataForListActive() {
        mMainDataForListActive = mDatabaseManager
                .getTasksUsingCategory(mCategoryId, ConstantsManager.STATE_FLAG_ACTIVE);
        for (DataForTasksListItem data : mMainDataForListActive) {
            data.setNow(AuxiliaryFunctions.isActualTask(data, getTime()));
        }
    }

    /**
     * Prepares data for list of finished tasks
     */
    private void prepareDataForListDone() {
        mMainDataForListDone = mDatabaseManager
                .getTasksUsingCategory(mCategoryId, ConstantsManager.STATE_FLAG_DONE);
    }

    /**
     * Creates String object from day, month, year, hour and minute values using special mask
     *
     * @return String object of date
     */
    private Long getTime() {
        Calendar calendar = GregorianCalendar.getInstance();
        return Long.parseLong(String.format(Locale.ENGLISH, "%04d%02d%02d%02d%02d",
                mChosenYear, mChosenMonth, mChosenDay, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE)));
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
                false, mYesterdayTodayTomorrowTitles, mYesterdayTodayTomorrowMask,
                mOnItemClickListenerOpen, mOnItemClickListenerDelete)));
        mMainData.add(new DataForActiveDoneListItem(new AdapterOfFragmentTasksList(mMainDataForListDone,
                false, mYesterdayTodayTomorrowTitles, mYesterdayTodayTomorrowMask,
                mOnItemClickListenerOpen, mOnItemClickListenerDelete)));

        AdapterOfFragmentActiveDoneList adapterOfFragmentActiveDoneList = new AdapterOfFragmentActiveDoneList(mMainData, adapter);


        mRecyclerView.setAdapter(adapterOfFragmentActiveDoneList);

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
            Intent intent = new Intent(CategoryActivity.this, TaskActivity.class);
            intent.putExtra(ConstantsManager.TASK_ID_FLAG, task.getId());
            intent.putExtra(ConstantsManager.TASK_TITLE_FLAG, task.getTitle());
            intent.putExtra(ConstantsManager.TASK_STATE_FLAG, mStateFlag);
            intent.putExtra(ConstantsManager.TASK_CATEGORY_FLAG, task.getCategoryId());
            intent.putExtra(ConstantsManager.TASK_DAY_FLAG, task.getDay());
            intent.putExtra(ConstantsManager.TASK_MONTH_FLAG, task.getMonth());
            intent.putExtra(ConstantsManager.TASK_YEAR_FLAG, task.getYear());
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
    public void changeCategory() {
        mDialogList = new DialogList(this, mParameter, getDataForDialogList(),
                new AdapterOfDialogList.OnItemClickListener() {
                    @Override
                    public void onClick(int position) {
                        mCategoryId = position;
                        mDialogList.updateList(getDataForDialogList());
                        mRecyclerView.setLayoutFrozen(false);
                        updateListActive();
                        updateListDone();
                        mRecyclerView.setLayoutFrozen(true);
                        mDialogList.getDialog().dismiss();
                        checkList();
                    }
                }, mCategoryId, null, ConstantsManager.DIALOG_LIST_TYPE_ONE);
    }

    /**
     * Performs data for DialogList object to show all categories
     *
     * @return DataForDialogListItem object
     */
    private List<DataForDialogListItem> getDataForDialogList() {
        List<DataForDialogListItem> result = new ArrayList<>();
        for (int i = 0; i < mTitlesOfCategories.length; i++) {
            if (i == mCategoryId) {
                result.add(new DataForDialogListItem(i, mTitlesOfCategories[i], true));
            } else {
                result.add(new DataForDialogListItem(i, mTitlesOfCategories[i], false));
            }
        }
        return result;
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
                headerIco = R.drawable.ic_categories_white;
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
                headerIco = R.drawable.ic_categories;
                findViewById(R.id.fragment_main_layout)
                        .setBackgroundColor(getResources().getColor(R.color.white));
                ((ImageView) findViewById(R.id.fragment_empty_iv)).setImageResource(R.drawable.ic_tr);
                break;
        }
        ((LinearLayout) findViewById(R.id.fragment_fab_layout)).removeAllViews();
        LayoutInflater.from(this).inflate(fab_layout_id,
                ((LinearLayout) findViewById(R.id.fragment_fab_layout)), true);
        ((TextView) findViewById(R.id.fragment_empty_tv)).setText(text);

        updateState(mStateFlag);
        changeHeader();
    }

    /**
     * Saves category id value in Bundle object
     *
     * @param outState Bundle object
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt(ConstantsManager.TASK_CATEGORY_FLAG, mCategoryId);
        outState.putInt(ConstantsManager.TASK_STATE_FLAG, mStateFlag);
        super.onSaveInstanceState(outState);
    }

    /**
     * Closes Activity
     */
    @Override
    public void onBackPressed() {
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
                mYesterdayTodayTomorrowTitles = getResources().getStringArray(R.array.yesterday_today_tomorrow_eng);
                mTitlesOfCategories = getResources().getStringArray(R.array.categories_eng);
                navActiveText.setText(getResources().getStringArray(R.array.task_list_header_navigation_titles_eng)[0]);
                navDoneText.setText(getResources().getStringArray(R.array.task_list_header_navigation_titles_eng)[1]);
                mDeleteDoneBackToastTitle = getResources().getStringArray(R.array.delete_done_back_eng);
                mEmptyListTitle = getResources().getStringArray(R.array.list_empty_two_eng);
                mParameter = getResources().getStringArray(R.array.task_parameters_eng)[1];
                mToolbar.setTitle(getResources().getStringArray(R.array.drawer_menu_items_eng)[0]);
            }

            @Override
            public void ukrLanguage() {
                mYesterdayTodayTomorrowTitles = getResources().getStringArray(R.array.yesterday_today_tomorrow_ukr);
                mTitlesOfCategories = getResources().getStringArray(R.array.categories_ukr);
                navActiveText.setText(getResources().getStringArray(R.array.task_list_header_navigation_titles_ukr)[0]);
                navDoneText.setText(getResources().getStringArray(R.array.task_list_header_navigation_titles_ukr)[1]);
                mDeleteDoneBackToastTitle = getResources().getStringArray(R.array.delete_done_back_ukr);
                mEmptyListTitle = getResources().getStringArray(R.array.list_empty_two_ukr);
                mParameter = getResources().getStringArray(R.array.task_parameters_ukr)[1];
                mToolbar.setTitle(getResources().getStringArray(R.array.drawer_menu_items_ukr)[0]);
            }

            @Override
            public void rusLanguage() {
                mYesterdayTodayTomorrowTitles = getResources().getStringArray(R.array.yesterday_today_tomorrow_rus);
                mTitlesOfCategories = getResources().getStringArray(R.array.categories_rus);
                navActiveText.setText(getResources().getStringArray(R.array.task_list_header_navigation_titles_rus)[0]);
                navDoneText.setText(getResources().getStringArray(R.array.task_list_header_navigation_titles_rus)[1]);
                mDeleteDoneBackToastTitle = getResources().getStringArray(R.array.delete_done_back_rus);
                mEmptyListTitle = getResources().getStringArray(R.array.list_empty_two_rus);
                mParameter = getResources().getStringArray(R.array.task_parameters_rus)[1];
                mToolbar.setTitle(getResources().getStringArray(R.array.drawer_menu_items_rus)[0]);
            }
        };
    }
}
