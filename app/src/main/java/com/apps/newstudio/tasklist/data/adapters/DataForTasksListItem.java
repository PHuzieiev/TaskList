package com.apps.newstudio.tasklist.data.adapters;

public class DataForTasksListItem {

    private Long id;
    private String title;
    private int category;
    private int day;
    private int month;
    private int year;
    private int beginningHours;
    private int beginningMinutes;
    private int endHours;
    private int endMinutes;
    private boolean isNow;
    private int isAlarmOn;

    /**
     * Constructor for DataForTasksListItem object
     *
     * @param id               id value of task
     * @param title            task title
     * @param category         value of category id
     * @param day              value of day
     * @param month            value of month
     * @param year             value of year
     * @param beginningHours   value of beginning hour
     * @param beginningMinutes value of beginning minute
     * @param endHours         value of end hour
     * @param endMinutes       value of end minute
     * @param isNow            value witch defines actualization of task
     * @param isAlarmOn        value of alarm clock status
     */
    public DataForTasksListItem(Long id, String title, int category, int day, int month,
                                int year, int beginningHours, int beginningMinutes,
                                int endHours, int endMinutes,
                                boolean isNow, int isAlarmOn) {
        this.id = id;
        this.title = title;
        this.category = category;
        this.day = day;
        this.month = month;
        this.year = year;
        this.beginningHours = beginningHours;
        this.beginningMinutes = beginningMinutes;
        this.endHours = endHours;
        this.endMinutes = endMinutes;
        this.isNow = isNow;
        this.isAlarmOn = isAlarmOn;
    }

    /**
     * Getter for id value of task
     *
     * @return id value of task
     */
    public Long getId() {
        return id;
    }

    /**
     * Getter for task title
     *
     * @return task title
     */
    public String getTitle() {
        return title;
    }

    /**
     * Getter for value of beginning hour
     *
     * @return value of beginning hour
     */
    public int getBeginningHours() {
        return beginningHours;
    }

    /**
     * Getter for value of beginning minute
     *
     * @return value of beginning minute
     */
    public int getBeginningMinutes() {
        return beginningMinutes;
    }

    /**
     * Getter for value of end hour
     *
     * @return value of end hour
     */
    public int getEndHours() {
        return endHours;
    }

    /**
     * Getter for value of end minute
     *
     * @return value of end minute
     */
    public int getEndMinutes() {
        return endMinutes;
    }

    /**
     * Getter for  value witch defines actualization of task
     *
     * @return value witch defines actualization of task
     */
    public boolean isNow() {
        return isNow;
    }

    /**
     * Getter for value of alarm clock status
     *
     * @return value of alarm clock status
     */
    public int isAlarmOn() {
        return isAlarmOn;
    }

    /**
     * Setter for value witch defines actualization of task
     *
     * @param now value witch defines actualization of task
     */
    public void setNow(boolean now) {
        isNow = now;
    }

    /**
     * Getter for int value of day
     *
     * @return value of day
     */
    public int getDay() {
        return day;
    }

    /**
     * Getter for int value of month
     *
     * @return value of month
     */
    public int getMonth() {
        return month;
    }

    /**
     * Getter for int value of year
     *
     * @return value of year
     */
    public int getYear() {
        return year;
    }

    /**
     * Getter for int value of category id
     *
     * @return int value of category id
     */
    public int getCategory() {
        return category;
    }
}
