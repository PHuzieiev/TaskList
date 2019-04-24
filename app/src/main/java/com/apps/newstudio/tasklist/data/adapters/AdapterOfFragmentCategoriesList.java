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
import com.apps.newstudio.tasklist.data.managers.LanguageManager;
import com.apps.newstudio.tasklist.utils.ConstantsManager;
import com.apps.newstudio.tasklist.utils.TaskListApplication;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AdapterOfFragmentCategoriesList
        extends RecyclerView.Adapter<AdapterOfFragmentCategoriesList.ViewHolder> {

    private List<DataForCategoriesListItem> mData;
    private OnItemClickListener mOnClickOpen;
    private OnItemClickListener mOnClickDelete;
    private String mCountTitle;

    /**
     * Constructor for AdapterOfFragmentCategoriesList object
     *
     * @param data          DataForCategoriesListItem list object, main data for items
     * @param onClickOpen   realized AdapterOfFragmentCategoriesList.OnItemClickListener interface
     * @param onCLickDelete realized AdapterOfFragmentCategoriesList.OnItemClickListener interface
     */
    public AdapterOfFragmentCategoriesList(List<DataForCategoriesListItem> data, OnItemClickListener onClickOpen,
                                           OnItemClickListener onCLickDelete) {
        mData = data;
        mOnClickOpen = onClickOpen;
        mOnClickDelete = onCLickDelete;

        new LanguageManager() {
            @Override
            public void engLanguage() {
                mCountTitle = TaskListApplication.getContext().getResources()
                        .getString(R.string.count_of_active_tasks_eng);
            }

            @Override
            public void ukrLanguage() {
                mCountTitle = TaskListApplication.getContext().getResources()
                        .getString(R.string.count_of_active_tasks_ukr);
            }

            @Override
            public void rusLanguage() {
                mCountTitle = TaskListApplication.getContext().getResources()
                        .getString(R.string.count_of_active_tasks_rus);
            }
        };
    }

    /**
     * Setter for list of DataForCategoriesListItem objects
     *
     * @param data list of DataForCategoriesListItem objects
     */
    public void setData(List<DataForCategoriesListItem> data) {
        mData = data;
    }

    /**
     * Getter for list of DataForCategoriesListItem objects
     *
     * @return list of DataForCategoriesListItem objects
     */
    public List<DataForCategoriesListItem> getData() {
        return mData;
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
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_fragment_categories_list, parent, false);
        return new AdapterOfFragmentCategoriesList.ViewHolder(view, mOnClickOpen, mOnClickDelete);
    }

    /**
     * Updates item using data from DataForDialogListItem list object
     *
     * @param holder   ViewHolder object of some list item
     * @param position int value of item position
     */
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        updateHolder(holder, mData.get(position));
    }

    /**
     * Updates item using data from DataForCategoriesListItem object
     *
     * @param holder   ViewHolder object of some list item
     * @param itemData DataForCategoriesListItem  object
     */
    private void updateHolder(ViewHolder holder, DataForCategoriesListItem itemData) {
        holder.titleText.setText(itemData.getTitle());
        holder.countText.setText(mCountTitle.concat("" + itemData.getCount()));
        if (itemData.getCount() != 0) {
            holder.parentLayout.setBackgroundColor(TaskListApplication.getContext()
                    .getResources().getColor(R.color.colorPrimary));
        } else {
            holder.parentLayout.setBackgroundColor(TaskListApplication.getContext()
                    .getResources().getColor(R.color.grey_two));
        }

        switch (itemData.getId()) {
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
        return mData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.item_parent_layout)
        public LinearLayout parentLayout;

        @BindView(R.id.item_title_text)
        public TextView titleText;

        @BindView(R.id.item_title_ico)
        public ImageView titleIco;

        @BindView(R.id.item_count_text)
        public TextView countText;

        @BindView(R.id.item_delete_image)
        public ImageView deleteImage;

        private OnItemClickListener mOnItemClickOpen;
        private OnItemClickListener mOnItemClickDelete;

        /**
         * Constructor for AdapterOfDialogList.ViewHolder
         *
         * @param itemView          main view object of list item
         * @param onItemClickOpen   realized AdapterOfFragmentCategoriesList.OnItemClickListener interface
         * @param onItemClickDelete realized AdapterOfFragmentCategoriesList.OnItemClickListener interface
         */
        public ViewHolder(View itemView, OnItemClickListener onItemClickOpen, OnItemClickListener onItemClickDelete) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            mOnItemClickOpen = onItemClickOpen;
            mOnItemClickDelete = onItemClickDelete;
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
                    mOnItemClickOpen.onClick(getAdapterPosition());
                    break;
                case R.id.item_delete_image:
                    mOnItemClickDelete.onClick(getAdapterPosition());
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
