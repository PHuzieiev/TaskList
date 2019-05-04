package com.apps.newstudio.tasklist.data.adapters;

import java.util.List;

public class DataForInfoListItem {

    private String mTitle;
    private int mParentId;
    private List<DataForInfoListItem> mChildList;
    private boolean isOpen = false;

    public DataForInfoListItem(int parentId, String title, List<DataForInfoListItem> childList) {
        mTitle = title;
        mParentId = parentId;
        mChildList = childList;
    }

    public String getTitle() {
        return mTitle;
    }

    public int getParentId() {
        return mParentId;
    }

    public List<DataForInfoListItem> getChildList() {
        return mChildList;
    }

    public void setChildList(List<DataForInfoListItem> childList) {
        mChildList = childList;
    }

    public boolean isOpen() {
        return isOpen;
    }

    public void setOpen(boolean open) {
        isOpen = open;
    }
}
