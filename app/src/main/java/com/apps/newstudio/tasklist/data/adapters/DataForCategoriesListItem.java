package com.apps.newstudio.tasklist.data.adapters;

public class DataForCategoriesListItem {

    private int mId;
    private String mTitle;
    private Integer mCount;

    /**
     * Constructor for DataForCategoriesListItem object
     *
     * @param id    category id value
     * @param title category title value
     * @param count count of category tasks
     */
    public DataForCategoriesListItem(int id, String title, Integer count) {
        mId = id;
        mTitle = title;
        mCount = count;
    }

    /**
     * Getter for category id value
     *
     * @return category id value
     */
    public int getId() {
        return mId;
    }

    /**
     * Getter for category title value
     *
     * @return category title value
     */
    public String getTitle() {
        return mTitle;
    }

    /**
     * Getter for count of category tasks
     *
     * @return count of category tasks
     */
    public Integer getCount() {
        return mCount;
    }
}
