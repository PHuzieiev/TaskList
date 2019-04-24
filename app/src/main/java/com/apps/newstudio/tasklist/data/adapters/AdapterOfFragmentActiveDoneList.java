package com.apps.newstudio.tasklist.data.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.apps.newstudio.tasklist.R;
import com.apps.newstudio.tasklist.utils.TaskListApplication;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AdapterOfFragmentActiveDoneList extends RecyclerView.Adapter<AdapterOfFragmentActiveDoneList.ViewHolder> {


    private List<DataForActiveDoneListItem> mData;
    private ItemOfFragmentListTouchHelperAdapter mAdapter;

    /**
     * Main constructor of list adapter
     * @param mData data for list items
     * @param mAdapter ItemOfFragmentListTouchHelperAdapter object
     */
    public AdapterOfFragmentActiveDoneList(List<DataForActiveDoneListItem> mData,
                                           ItemOfFragmentListTouchHelperAdapter mAdapter) {
        this.mData = mData;
        this.mAdapter=mAdapter;
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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_fragment_active_done_list, parent, false);
        return new AdapterOfFragmentActiveDoneList.ViewHolder(view);
    }

    /**
     * Updates item using data from DataForDialogListItem list object
     *
     * @param holder   ViewHolder object of some list item
     * @param position int value of item position
     */
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(TaskListApplication.getContext());
        holder.list.setLayoutManager(linearLayoutManager);
        holder.list.setAdapter(mData.get(position).getAdapter());
        ItemTouchHelper.Callback callback = new ItemOfFragmentListTouchHelperCallback(mAdapter);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(callback);
        itemTouchHelper.attachToRecyclerView(holder.list);
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

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.item_list)
        public RecyclerView list;

        /**
         * Constructor for AdapterOfDialogList.ViewHolder
         *
         * @param itemView main view object of list item
         */
        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
