package com.apps.newstudio.tasklist.data.adapters;

public class DataForLinearDiagram {
    private int mDay, mCountActive, mCountDone;


    public DataForLinearDiagram(int day, int countActive, int countDone) {
        mDay = day;
        mCountActive = countActive;
        mCountDone = countDone;
    }

    public int getDay() {
        return mDay;
    }

    public int getCountActive() {
        return mCountActive;
    }

    public int getCountDone() {
        return mCountDone;
    }
}
