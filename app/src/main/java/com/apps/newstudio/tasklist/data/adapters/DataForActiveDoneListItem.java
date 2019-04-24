package com.apps.newstudio.tasklist.data.adapters;

public class DataForActiveDoneListItem {
    private AdapterOfFragmentTasksList mAdapter;

    public DataForActiveDoneListItem(AdapterOfFragmentTasksList mAdapter) {
        this.mAdapter = mAdapter;
    }

    public AdapterOfFragmentTasksList getAdapter() {
        return mAdapter;
    }
}
