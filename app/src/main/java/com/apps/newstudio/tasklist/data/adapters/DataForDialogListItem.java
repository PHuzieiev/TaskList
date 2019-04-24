package com.apps.newstudio.tasklist.data.adapters;

public class DataForDialogListItem {

    private int id;
    private String title;
    private boolean isChosen;

    /**
     * Constructor for DataForDialogListItem object
     * @param id id value of item
     * @param title item title
     * @param isChosen value which defines chosen item
     */
    public DataForDialogListItem(int id, String title, boolean isChosen) {
        this.id = id;
        this.title = title;
        this.isChosen = isChosen;
    }

    /**
     * Getter for id value of item
     * @return id value of item
     */
    public int getId() {
        return id;
    }

    /**
     * Getter for item title
     * @return item title
     */
    public String getTitle() {
        return title;
    }

    /**
     * Getter for value which defines chosen item
     * @return value which defines chosen item
     */
    public boolean isChosen() {
        return isChosen;
    }

    /**
     * Setter for value which defines chosen item
     * @param chosen value which defines chosen item
     */
    public void setChosen(boolean chosen) {
        isChosen = chosen;
    }
}
