package com.apps.newstudio.tasklist.data.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.apps.newstudio.tasklist.R;
import com.apps.newstudio.tasklist.utils.AuxiliaryFunctions;
import com.apps.newstudio.tasklist.utils.ConstantsManager;
import com.apps.newstudio.tasklist.utils.TaskListApplication;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AdapterOfFragmentTasksList
        extends RecyclerView.Adapter<AdapterOfFragmentTasksList.ViewHolder> {

    private List<DataForTasksListItem> data;
    private OnItemClickListener onItemClickOpen;
    private OnItemClickListener onItemClickDelete;
    private boolean isShortFormOfTime;
    private String[] yesterdayTodayTomorrowTitles, yesterdayTodayTomorrowMask;


    /**
     * Constructor of AdapterOfFragmentTasksList object
     *
     * @param data                         object of data for list items
     * @param isShortFormOfTime            value witch initializes date format
     * @param yesterdayTodayTomorrowTitles array of string objects
     * @param yesterdayTodayTomorrowMask   array of string objects
     * @param onItemClickOpen              realized AdapterOfFragmentTasksList.OnItemClickListener interface
     * @param onItemClickDelete            realized AdapterOfFragmentTasksList.OnItemClickListener interface
     */
    public AdapterOfFragmentTasksList(List<DataForTasksListItem> data, boolean isShortFormOfTime,
                                      String[] yesterdayTodayTomorrowTitles, String[] yesterdayTodayTomorrowMask, OnItemClickListener onItemClickOpen,
                                      OnItemClickListener onItemClickDelete) {
        this.data = data;
        this.isShortFormOfTime = isShortFormOfTime;
        this.yesterdayTodayTomorrowTitles = yesterdayTodayTomorrowTitles;
        this.yesterdayTodayTomorrowMask = yesterdayTodayTomorrowMask;
        this.onItemClickOpen = onItemClickOpen;
        this.onItemClickDelete = onItemClickDelete;
    }

    /**
     * Setter for list of DataForTasksListItem objects
     *
     * @param data list of DataForTasksListItem objects
     */
    public void setData(List<DataForTasksListItem> data) {
        this.data = data;
    }

    /**
     * Getter for list of DataForTasksListItem objects
     *
     * @return list of DataForTasksListItem objects
     */
    public List<DataForTasksListItem> getData() {
        return data;
    }

    /**
     * Creates item of list from xml layout file
     *
     * @param parent   ViewGroup object
     * @param viewType int value
     * @return inflated view object of list item
     */
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_fragment_days_list, parent, false);
        return new AdapterOfFragmentTasksList.ViewHolder(view, onItemClickOpen, onItemClickDelete);
    }

    /**
     * Updates item using data from DataForTasksListItem list object
     *
     * @param holder   ViewHolder object of some list item
     * @param position int value of item position
     */
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        updateHolder(holder, data.get(position));
        RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) holder.parentLayout.getLayoutParams();
        if (position == getItemCount() - 1) {
            layoutParams.bottomMargin = TaskListApplication.getContext().getResources()
                    .getDimensionPixelSize(R.dimen.spacing_big_88dp);
        } else {
            layoutParams.bottomMargin = 0;
        }
        holder.parentLayout.setLayoutParams(layoutParams);


    }

    /**
     * Updates item using data from DataForTasksListItem object
     *
     * @param holder   ViewHolder object of some list item
     * @param itemData DataForTasksListItem  object
     */
    private void updateHolder(ViewHolder holder, DataForTasksListItem itemData) {
        holder.titleText.setText(itemData.getTitle());
        int imageAlarmOn, imageAlarmOff;
        if (itemData.isNow()) {
            imageAlarmOn = R.drawable.ic_alarm_on;
            imageAlarmOff = R.drawable.ic_alarm_off;
            holder.parentLayout.setBackgroundColor(TaskListApplication.getContext()
                    .getResources().getColor(R.color.colorPrimary));
        } else {
            imageAlarmOn = R.drawable.ic_alarm_on_grey;
            imageAlarmOff = R.drawable.ic_alarm_off_grey;
            holder.parentLayout.setBackgroundColor(TaskListApplication.getContext()
                    .getResources().getColor(R.color.grey_two));
        }
        if (itemData.isAlarmOn() == ConstantsManager.ALARM_FLAG_ON) {
            holder.alarmImage.setImageResource(imageAlarmOn);
        } else {
            holder.alarmImage.setImageResource(imageAlarmOff);
        }

        holder.timePeriodText.setText(AuxiliaryFunctions.convertToTimePeriod(itemData, yesterdayTodayTomorrowTitles,
                yesterdayTodayTomorrowMask, isShortFormOfTime));

        switch (itemData.getCategory()) {
            case ConstantsManager.CATEGORIES_NONE:
                holder.titleIco.setImageResource(R.drawable.ic_none_category);
                break;
            case ConstantsManager.CATEGORIES_FAMILY:
                holder.titleIco.setImageResource(R.drawable.ic_family);
                break;
            case ConstantsManager.CATEGORIES_MEETINGS:
                holder.titleIco.setImageResource(R.drawable.ic_meetings);
                break;
            case ConstantsManager.CATEGORIES_HONE:
                holder.titleIco.setImageResource(R.drawable.ic_home);
                break;
            case ConstantsManager.CATEGORIES_SHOPPING:
                holder.titleIco.setImageResource(R.drawable.ic_shopping);
                break;
            case ConstantsManager.CATEGORIES_BIRTHDAY:
                holder.titleIco.setImageResource(R.drawable.ic_birthday);
                break;
            case ConstantsManager.CATEGORIES_BUSINESS:
                holder.titleIco.setImageResource(R.drawable.ic_business);
                break;
            case ConstantsManager.CATEGORIES_CALLS:
                holder.titleIco.setImageResource(R.drawable.ic_calls);
                break;
            case ConstantsManager.CATEGORIES_ENTERTAINMENTS:
                holder.titleIco.setImageResource(R.drawable.ic_entertainments);
                break;
            case ConstantsManager.CATEGORIES_EDUCATION:
                holder.titleIco.setImageResource(R.drawable.ic_education);
                break;
            case ConstantsManager.CATEGORIES_PETS:
                holder.titleIco.setImageResource(R.drawable.ic_pets);
                break;
            case ConstantsManager.CATEGORIES_PRIVATE_LIFE:
                holder.titleIco.setImageResource(R.drawable.ic_private_life);
                break;
            case ConstantsManager.CATEGORIES_REPAIR:
                holder.titleIco.setImageResource(R.drawable.ic_repair);
                break;
            case ConstantsManager.CATEGORIES_WORK:
                holder.titleIco.setImageResource(R.drawable.ic_work);
                break;
            case ConstantsManager.CATEGORIES_SPORT:
                holder.titleIco.setImageResource(R.drawable.ic_sport);
                break;
            case ConstantsManager.CATEGORIES_TRAVELLINGS:
                holder.titleIco.setImageResource(R.drawable.ic_travelling);
                break;
        }
    }

    /**
     * Getter for count of list items
     *
     * @return int value of item count
     */
    @Override
    public int getItemCount() {
        return data.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private OnItemClickListener mOnItemClickListenerOpen;
        private OnItemClickListener mOnItemClickListenerDelete;

        @BindView(R.id.item_parent_layout)
        public LinearLayout parentLayout;

        @BindView(R.id.item_title_text)
        public TextView titleText;

        @BindView(R.id.item_title_ico)
        public ImageView titleIco;

        @BindView(R.id.item_alarm_image)
        public ImageView alarmImage;

        @BindView(R.id.item_time_period_text)
        public TextView timePeriodText;

        @BindView(R.id.item_delete_image)
        public ImageView deleteImage;

        /**
         * Constructor for AdapterOfDialogList.ViewHolder
         *
         * @param itemView                   main view object of list item
         * @param mOnItemClickListenerOpen   realized AdapterOfFragmentTasksList.OnItemClickListener interface
         * @param mOnItemClickListenerDelete realized AdapterOfFragmentTasksList.OnItemClickListener interface
         */
        public ViewHolder(View itemView, OnItemClickListener mOnItemClickListenerOpen,
                          OnItemClickListener mOnItemClickListenerDelete) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            this.mOnItemClickListenerDelete = mOnItemClickListenerDelete;
            this.mOnItemClickListenerOpen = mOnItemClickListenerOpen;
            parentLayout.setOnClickListener(this);
            deleteImage.setOnClickListener(this);
        }

        /**
         * Implementation of onClick function of OnClickListener object
         *
         * @param v view object which is clicked
         */
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.item_parent_layout:
                    mOnItemClickListenerOpen.onClick(getAdapterPosition());
                    break;
                case R.id.item_delete_image:
                    mOnItemClickListenerDelete.onClick(getAdapterPosition());
                    break;
            }
        }
    }

    /**
     * Interface which is used for processing onClick events
     */
    public interface OnItemClickListener {
        void onClick(int position);
    }
}
