package com.apps.newstudio.tasklist.data.adapters;

public class DateStateObject {

    private int day, month, year, state;

    /**
     * Constructor for DateStateObject object
     *
     * @param day   number of day in month
     * @param month number of month in year
     * @param year  number of year
     * @param state state of tasks
     */
    public DateStateObject(int day, int month, int year, int state) {
        this.day = day;
        this.month = month;
        this.year = year;
        this.state = state;
    }

    /**
     * Getter for number of days in month
     *
     * @return number of days in month
     */
    public int getDay() {
        return day;
    }

    /**
     * Getter for number of month in year
     *
     * @return number of month in year
     */
    public int getMonth() {
        return month;
    }

    /**
     * Getter for number of year
     *
     * @return number of year
     */
    public int getYear() {
        return year;
    }

    /**
     * Getter for state of tasks
     *
     * @return state of tasks
     */
    public int getState() {
        return state;
    }
}
