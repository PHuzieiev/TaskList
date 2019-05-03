package com.apps.newstudio.tasklist.ui.fragments;

import android.content.DialogInterface;
import android.graphics.Typeface;
import android.os.Bundle;
import android.app.Fragment;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.apps.newstudio.tasklist.R;
import com.apps.newstudio.tasklist.data.adapters.AdapterOfDialogList;
import com.apps.newstudio.tasklist.data.adapters.DataForDialogListItem;
import com.apps.newstudio.tasklist.data.adapters.DataForLinearDiagram;
import com.apps.newstudio.tasklist.data.managers.DataManager;
import com.apps.newstudio.tasklist.data.managers.LanguageManager;
import com.apps.newstudio.tasklist.ui.activities.MainActivity;
import com.apps.newstudio.tasklist.ui.dialogs.DialogList;
import com.apps.newstudio.tasklist.ui.views.CustomCircleDiagram;
import com.apps.newstudio.tasklist.ui.views.CustomLinearDiagram;
import com.apps.newstudio.tasklist.utils.ConstantsManager;
import com.apps.newstudio.tasklist.utils.TaskListApplication;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;


public class StatisticFragment extends Fragment {

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

    private View mView;

    private Unbinder mUnbinder;

    private int mChosenMonth, mChosenYear, mStateFlag, mCountOfTasks, mCountOfActiveTasks, mCountOfDoneTasks;
    private String[] mTitlesOfMonths, mCircleDiagramTitles;
    private String mDaysTitle;
    private List<DataForLinearDiagram> mDataForLinearDiagram;

    private DialogList mDialogList;

    /**
     * Creates Fragment object
     *
     * @param savedInstanceState Bundle object
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null) {
            Calendar calendar = Calendar.getInstance();
            mChosenMonth = calendar.get(Calendar.MONTH) + 1;
            mChosenYear = calendar.get(Calendar.YEAR);
            mStateFlag = ConstantsManager.STATE_FLAG_ACTIVE;
        } else {
            mChosenMonth = savedInstanceState.getInt(ConstantsManager.CHOSEN_MONTH_KEY);
            mChosenYear = savedInstanceState.getInt(ConstantsManager.CHOSEN_YEAR_KEY);
            mStateFlag = savedInstanceState.getInt(ConstantsManager.TASK_STATE_FLAG);
        }
    }

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
        mView = inflater.inflate(R.layout.fragment_statistic, container, false);
        mUnbinder = ButterKnife.bind(this, mView);
        setLang();
        changeHeader();
        updateState(mStateFlag);
        updateData();
        navActiveText.setOnClickListener(mOnClickListenerActiveDone);
        navDoneText.setOnClickListener(mOnClickListenerActiveDone);
        return mView;
    }

    /**
     * Updates all data and sets diagrams
     */
    private void updateData() {
        int[] data = DataManager.getInstance().getDatabaseManager().getCountOfTasksUsingMonth(mChosenMonth, mChosenYear);
        mCountOfTasks = data[0];
        mCountOfActiveTasks = data[1];
        mCountOfDoneTasks = data[2];
        setCircleDiagram();
        mDataForLinearDiagram = DataManager.getInstance()
                .getDatabaseManager().getDataForLinearDiagram(mChosenMonth, mChosenYear);
        setLinearDiagram();
    }

    /**
     * Updates circle diagram and some TextView objects
     */
    private void setCircleDiagram() {
        if (mCountOfTasks != 0) {
            ((CustomCircleDiagram) mView.findViewById(R.id.fragment_circle_diagram))
                    .setValues(mCountOfTasks, mCountOfActiveTasks, mCountOfDoneTasks);
        } else {
            ((CustomCircleDiagram) mView.findViewById(R.id.fragment_circle_diagram))
                    .setValues(10, 0, 0);
        }
        ((TextView) mView.findViewById(R.id.circle_diagram_total_count_text))
                .setText(mCircleDiagramTitles[0].concat("" + mCountOfTasks));
        ((TextView) mView.findViewById(R.id.circle_diagram_active_count_text))
                .setText(mCircleDiagramTitles[1].concat("" + mCountOfActiveTasks));
        ((TextView) mView.findViewById(R.id.circle_diagram_done_count_text))
                .setText(mCircleDiagramTitles[2].concat("" + mCountOfDoneTasks));
    }

    /**
     * Updates linear diagram
     */
    private void setLinearDiagram() {
        ((CustomLinearDiagram) mView.findViewById(R.id.fragment_linear_diagram))
                .setData(mDataForLinearDiagram, mStateFlag, mDaysTitle);
    }

    /**
     * Saves data in Bundle object
     *
     * @param outState Bundle object
     */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putInt(ConstantsManager.CHOSEN_MONTH_KEY, mChosenMonth);
        outState.putInt(ConstantsManager.CHOSEN_YEAR_KEY, mChosenYear);
        outState.putInt(ConstantsManager.TASK_STATE_FLAG, mStateFlag);
        super.onSaveInstanceState(outState);
    }

    /**
     * Action which changes chosen state of tasks
     */
    private View.OnClickListener mOnClickListenerActiveDone = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (mStateFlag == ConstantsManager.STATE_FLAG_ACTIVE && v.getId() == R.id.fragment_nav_done_text) {
                mStateFlag = ConstantsManager.STATE_FLAG_DONE;
            } else if (mStateFlag == ConstantsManager.STATE_FLAG_DONE && v.getId() == R.id.fragment_nav_active_text) {
                mStateFlag = ConstantsManager.STATE_FLAG_ACTIVE;
            }
            updateState(mStateFlag);
            setLinearDiagram();
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
                navActiveLine.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                navDoneLine.setBackgroundColor(getResources().getColor(R.color.grey_two));
                navActiveText.setTextColor(getResources().getColor(R.color.colorPrimary));
                navDoneText.setTextColor(getResources().getColor(R.color.grey));
                break;
            case ConstantsManager.STATE_FLAG_DONE:
                navActiveLine.setBackgroundColor(getResources().getColor(R.color.grey_two));
                navDoneLine.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                navActiveText.setTextColor(getResources().getColor(R.color.grey));
                navDoneText.setTextColor(getResources().getColor(R.color.colorPrimary));
                break;
        }
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
     * Sets information and visual style of header components
     */
    private void changeHeader() {
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(
                mTitlesOfMonths[mChosenMonth - 1].concat(", " + mChosenYear));
        spannableStringBuilder.setSpan(
                new ForegroundColorSpan(TaskListApplication.getContext().getResources().getColor(R.color.colorPrimary)),
                0, mTitlesOfMonths[mChosenMonth - 1].length() + 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableStringBuilder.setSpan(new StyleSpan(Typeface.BOLD), 0, mTitlesOfMonths[mChosenMonth - 1].length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        mHeaderText.setText(spannableStringBuilder);
    }

    /**
     * Action which give user opportunity to change month and year
     */
    @OnClick(R.id.fragment_header_layout)
    public void changeMonth() {
        mDialogList = new DialogList(((MainActivity) getActivity()).getContext(), "" + mChosenYear, getDataForDialogList(),
                new AdapterOfDialogList.OnItemClickListener() {
                    @Override
                    public void onClick(int position) {
                        mChosenMonth = position + 1;
                        mDialogList.updateList(getDataForDialogList());
                        mDialogList.getDialog().dismiss();
                    }
                }, mChosenMonth - 1,
                new Integer[]{R.drawable.ic_radio_button_checked, R.drawable.ic_radio_button_unchecked},
                ConstantsManager.DIALOG_LIST_TYPE_TWO);
        mDialogList.getDialog().setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                mChosenYear = Integer.parseInt(mDialogList.getTitle());
                changeHeader();
                updateData();
            }
        });
    }

    /**
     * Getter for DataForDialogListItem list object
     * @return DataForDialogListItem list object
     */
    private List<DataForDialogListItem> getDataForDialogList() {
        List<DataForDialogListItem> result = new ArrayList<>();
        for (int i = 0; i < mTitlesOfMonths.length; i++) {
            if (i == mChosenMonth - 1) {
                result.add(new DataForDialogListItem(i, mTitlesOfMonths[i], true));
            } else {
                result.add(new DataForDialogListItem(i, mTitlesOfMonths[i], false));
            }
        }
        return result;
    }

    /**
     * Performs text objects using language parameter
     */
    private void setLang() {
        new LanguageManager() {
            @Override
            public void engLanguage() {
                mTitlesOfMonths = getResources().getStringArray(R.array.month_titles_eng);
                navActiveText.setText(getResources().getStringArray(R.array.task_list_header_navigation_titles_eng)[0]);
                navDoneText.setText(getResources().getStringArray(R.array.task_list_header_navigation_titles_eng)[1]);
                mCircleDiagramTitles = getResources().getStringArray(R.array.total_active_done_tasks_eng);
                mDaysTitle = getResources().getString(R.string.days_eng);
            }

            @Override
            public void ukrLanguage() {
                mTitlesOfMonths = getResources().getStringArray(R.array.month_titles_two_ukr);
                navActiveText.setText(getResources().getStringArray(R.array.task_list_header_navigation_titles_ukr)[0]);
                navDoneText.setText(getResources().getStringArray(R.array.task_list_header_navigation_titles_ukr)[1]);
                mCircleDiagramTitles = getResources().getStringArray(R.array.total_active_done_tasks_ukr);
                mDaysTitle = getResources().getString(R.string.days_ukr);
            }

            @Override
            public void rusLanguage() {
                mTitlesOfMonths = getResources().getStringArray(R.array.month_titles_two_rus);
                navActiveText.setText(getResources().getStringArray(R.array.task_list_header_navigation_titles_rus)[0]);
                navDoneText.setText(getResources().getStringArray(R.array.task_list_header_navigation_titles_rus)[1]);
                mCircleDiagramTitles = getResources().getStringArray(R.array.total_active_done_tasks_rus);
                mDaysTitle = getResources().getString(R.string.days_rus);
            }
        };
    }


}
