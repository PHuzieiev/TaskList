package com.apps.newstudio.tasklist.data.adapters;

public interface ItemOfFragmentListTouchHelperAdapter {

    /**
     * Function witch realizes deletion
     *
     * @param position position of item in list
     */
    void onItemDismiss(int position);

    /**
     * Function witch changes status of task
     *
     * @param position position of item in list
     */
    void onItemDoneOrActive(int position);
}
