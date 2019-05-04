package com.apps.newstudio.tasklist.data.adapters;

import java.util.List;

public class DataForInfoListItem {

    private String mTitle;
    private int mParentId;
    private List<DataForInfoListItem> mChildList;
    private boolean isOpen = false;

    /**
     * Constructor for DataForInfoListItem object
     *
     * @param parentId  parent item id value
     * @param title     item title
     * @param childList child items list object
     */
    public DataForInfoListItem(int parentId, String title, List<DataForInfoListItem> childList) {
        mTitle = title;
        mParentId = parentId;
        mChildList = childList;
    }

    /**
     * Getter for title
     *
     * @return String value for title
     */
    public String getTitle() {
        return mTitle;
    }

    /**
     * Getter for parent item id value
     *
     * @return parent item id value
     */
    public int getParentId() {
        return mParentId;
    }

    /**
     * Getter for child items list object
     *
     * @return child items list object
     */
    public List<DataForInfoListItem> getChildList() {
        return mChildList;
    }

    /**
     * Setter child items list object
     *
     * @param childList child items list object
     */
    public void setChildList(List<DataForInfoListItem> childList) {
        mChildList = childList;
    }

    /**
     * Getter for status of item
     *
     * @return item status
     */
    public boolean isOpen() {
        return isOpen;
    }

    /**
     * Setter for item status
     *
     * @param open item status
     */
    public void setOpen(boolean open) {
        isOpen = open;
    }
}
