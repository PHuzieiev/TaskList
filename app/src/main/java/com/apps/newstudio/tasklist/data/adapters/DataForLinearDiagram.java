package com.apps.newstudio.tasklist.data.adapters;

public class DataForLinearDiagram {
    private int mDay, mCountActive, mCountDone;
    private float mX = 0, mY = 0;


    /**
     * Constructor for DataForLinearDiagram object
     *
     * @param day         number of day in month
     * @param countActive count of active tasks
     * @param countDone   count of finished tasks
     */
    public DataForLinearDiagram(int day, int countActive, int countDone) {
        mDay = day;
        mCountActive = countActive;
        mCountDone = countDone;
    }

    /**
     * Getter for number of day in month
     *
     * @return number of day in month
     */
    public int getDay() {
        return mDay;
    }

    /**
     * Getter for count of active tasks
     *
     * @return count of active tasks
     */
    public int getCountActive() {
        return mCountActive;
    }

    /**
     * Getter for count of finished tasks
     *
     * @return count of finished tasks
     */
    public int getCountDone() {
        return mCountDone;
    }

    /**
     * Setter for coordinate X
     *
     * @param x coordinate X
     */
    public void setX(float x) {
        mX = x;
    }

    /**
     * Setter for coordinate Y
     *
     * @param y coordinate Y
     */
    public void setY(float y) {
        mY = y;
    }

    /**
     * Getter for coordinate X
     *
     * @return coordinate X
     */
    public float getX() {
        return mX;
    }

    /**
     * Getter for  coordinate Y
     *
     * @return coordinate Y
     */
    public float getY() {
        return mY;
    }
}
