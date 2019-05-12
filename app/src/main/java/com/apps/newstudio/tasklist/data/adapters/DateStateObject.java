package com.apps.newstudio.tasklist.data.adapters;

public class DateStateObject {
    private int day, month, year, state;

    public DateStateObject(int day, int month, int year, int state) {
        this.day = day;
        this.month = month;
        this.year = year;
        this.state = state;
    }

    public int getDay() {
        return day;
    }

    public int getMonth() {
        return month;
    }

    public int getYear() {
        return year;
    }

    public int getState() {
        return state;
    }
}
