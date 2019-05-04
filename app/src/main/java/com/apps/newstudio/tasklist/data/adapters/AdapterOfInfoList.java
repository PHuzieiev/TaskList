package com.apps.newstudio.tasklist.data.adapters;

import android.graphics.Typeface;
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

public class AdapterOfInfoList extends RecyclerView.Adapter<AdapterOfInfoList.ViewHolder> {

    private List<DataForInfoListItem> mData;
    private OnItemClickListener mClick;

    /**
     * Constructor for AdapterOfInfoList object
     *
     * @param data  DataForInfoListItem list object
     * @param click AdapterOfInfoList.OnItemClickListener object
     */
    public AdapterOfInfoList(List<DataForInfoListItem> data, OnItemClickListener click) {
        mData = data;
        mClick = click;
    }

    /**
     * Getter for data
     *
     * @return DataForInfoListItem list object
     */
    public List<DataForInfoListItem> getData() {
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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_fragment_info_list, parent, false);
        return new AdapterOfInfoList.ViewHolder(view, mClick);
    }

    /**
     * Updates item using data from DataForTasksListItem list object
     *
     * @param holder   ViewHolder object of some list item
     * @param position int value of item position
     */
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        DataForInfoListItem data = mData.get(position);
        if (data.getParentId() != 0) {
            holder.title.setText(data.getTitle());
            holder.title.setTextColor(TaskListApplication.getContext()
                    .getResources().getColor(R.color.colorPrimary));
            holder.number.setBackgroundResource(R.drawable.ic_circle_stroke);
            holder.number.setText(String.valueOf(data.getParentId()));
            holder.mainLayout.setBackgroundColor(TaskListApplication.getContext()
                    .getResources().getColor(R.color.white));
            if (data.isOpen()) {
                holder.parentLayout.setBackgroundColor(TaskListApplication.getContext()
                        .getResources().getColor(R.color.colorPrimary));
                holder.ico.setImageResource(R.drawable.ic_up);
            } else {
                holder.parentLayout.setBackgroundColor(TaskListApplication.getContext()
                        .getResources().getColor(R.color.grey_two));
                holder.ico.setImageResource(R.drawable.ic_down_two);
            }
            holder.title.setTypeface(Typeface.DEFAULT_BOLD);
        } else {
            holder.title.setText(data.getTitle());
            holder.title.setTextColor(TaskListApplication.getContext()
                    .getResources().getColor(R.color.white));
            holder.number.setBackgroundColor(TaskListApplication.getContext()
                    .getResources().getColor(R.color.colorPrimary));
            holder.number.setText("");
            holder.mainLayout.setBackgroundColor(TaskListApplication.getContext()
                    .getResources().getColor(R.color.colorPrimary));
            holder.parentLayout.setBackgroundColor(TaskListApplication.getContext()
                    .getResources().getColor(R.color.colorPrimary));
            holder.ico.setImageResource(R.drawable.ic_tr);
            holder.title.setTypeface(Typeface.DEFAULT);
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

        @BindView(R.id.item_main_layout)
        public LinearLayout mainLayout;

        @BindView(R.id.item_title_number)
        public TextView number;

        @BindView(R.id.item_title_text)
        public TextView title;

        @BindView(R.id.item_drop_down_image)
        public ImageView ico;

        private OnItemClickListener mOnClick;

        /**
         * Constructor for AdapterOfInfoList.ViewHolder
         *
         * @param itemView main view object of list item
         * @param onClick  realized AdapterOfInfoList.OnItemClickListener interface
         */
        public ViewHolder(View itemView, OnItemClickListener onClick) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            mOnClick = onClick;
            mainLayout.setOnClickListener(this);
        }

        /**
         * Implementation of onClick function of OnClickListener object
         *
         * @param v view object which is clicked
         */
        @Override
        public void onClick(View v) {
            mOnClick.onClick(getAdapterPosition());
        }
    }

    /**
     * Interface which is used for processing onClick events
     */
    public interface OnItemClickListener {
        void onClick(int position);
    }
}
