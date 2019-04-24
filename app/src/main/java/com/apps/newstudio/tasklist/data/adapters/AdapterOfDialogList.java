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
import com.apps.newstudio.tasklist.utils.TaskListApplication;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AdapterOfDialogList extends RecyclerView.Adapter<AdapterOfDialogList.ViewHolder> {

    private List<DataForDialogListItem> mData;
    private OnItemClickListener mClick;
    private Integer[] mDrawableId;

    /**
     * Main constructor of adapter for dialog list
     *
     * @param data       list object of DataForDialogListItem
     * @param click      realized AdapterOfDialogList.OnItemClickListener interface
     * @param drawableId Integer array
     */
    public AdapterOfDialogList(List<DataForDialogListItem> data, OnItemClickListener click, Integer[] drawableId) {
        mData = data;
        mClick = click;
        mDrawableId = drawableId;
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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_dialog_list, parent, false);
        return new AdapterOfDialogList.ViewHolder(view);
    }

    /**
     * Setter for list of DataForDialogListItem objects
     *
     * @param data list of DataForDialogListItem objects
     */
    public void setData(List<DataForDialogListItem> data) {
        mData = data;
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
     * Updates item using data from DataForDialogListItem  object
     *
     * @param holder ViewHolder object of some list item
     * @param data   DataForDialogListItem  object
     */
    private void updateHolder(ViewHolder holder, DataForDialogListItem data) {
        int[] images, images_grey;
        if (mDrawableId == null) {
            images = new int[]{R.drawable.ic_none_category, R.drawable.ic_family, R.drawable.ic_meetings,
                    R.drawable.ic_home, R.drawable.ic_shopping, R.drawable.ic_birthday, R.drawable.ic_business,
                    R.drawable.ic_calls, R.drawable.ic_entertainments, R.drawable.ic_education, R.drawable.ic_pets,
                    R.drawable.ic_private_life, R.drawable.ic_repair, R.drawable.ic_work, R.drawable.ic_sport,
                    R.drawable.ic_travelling};

            images_grey = new int[]{R.drawable.ic_none_category_grey, R.drawable.ic_family_grey, R.drawable.ic_meetings_grey,
                    R.drawable.ic_home_grey, R.drawable.ic_shopping_grey, R.drawable.ic_birthday_grey, R.drawable.ic_business_grey,
                    R.drawable.ic_calls_grey, R.drawable.ic_entertainments_grey, R.drawable.ic_education_grey, R.drawable.ic_pets_grey,
                    R.drawable.ic_private_life_grey, R.drawable.ic_repair_grey, R.drawable.ic_work_grey, R.drawable.ic_sport_grey,
                    R.drawable.ic_travelling_grey};
        } else {
            images = new int[mData.size()];
            for (int i = 0; i < images.length; i++) {
                images[i] = mDrawableId[0];
            }

            images_grey = new int[mData.size()];
            for (int i = 0; i < images_grey.length; i++) {
                images_grey[i] = mDrawableId[1];
            }
        }
        if (data.isChosen()) {
            holder.image.setImageResource(images[data.getId()]);
            holder.title.setTextColor(TaskListApplication.getContext()
                    .getResources().getColor(R.color.colorPrimary));
        } else {
            holder.image.setImageResource(images_grey[data.getId()]);
            holder.title.setTextColor(TaskListApplication.getContext()
                    .getResources().getColor(R.color.grey));
        }
        holder.title.setText(data.getTitle());
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

        @BindView(R.id.item_main_layout)
        public LinearLayout layout;

        @BindView(R.id.item_ico)
        public ImageView image;

        @BindView(R.id.item_title)
        public TextView title;


        /**
         * Constructor for AdapterOfDialogList.ViewHolder
         *
         * @param itemView main view object of list item
         */
        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            layout.setOnClickListener(this);

        }

        /**
         * Implementation of onClick function of OnClickListener object
         *
         * @param v view object which is clicked
         */
        @Override
        public void onClick(View v) {
            mClick.onClick(getAdapterPosition());
        }
    }

    /**
     * Interface which is used for processing onClick events
     */
    public interface OnItemClickListener {
        void onClick(int position);
    }
}
