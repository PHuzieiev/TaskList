package com.apps.newstudio.tasklist.data.storage.models;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.DaoException;

@Entity(active = true, nameInDb = "TASKS")
public class TaskEntity {

    @Id
    private Long id;

    private String title;
    private int taskState;
    private int categoryId;
    private int day;
    private int month;
    private int year;
    private int hourBeginning;
    private int minuteBeginning;
    private int hourEnd;
    private int minuteEnd;
    private int alarmState;
    private int alarmHour;
    private int alarmMinute;

    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;

    /** Used for active entity operations. */
    @Generated(hash = 263689402)
    private transient TaskEntityDao myDao;
    @Generated(hash = 515350357)
    public TaskEntity(Long id, String title, int taskState, int categoryId, int day,
            int month, int year, int hourBeginning, int minuteBeginning,
            int hourEnd, int minuteEnd, int alarmState, int alarmHour,
            int alarmMinute) {
        this.id = id;
        this.title = title;
        this.taskState = taskState;
        this.categoryId = categoryId;
        this.day = day;
        this.month = month;
        this.year = year;
        this.hourBeginning = hourBeginning;
        this.minuteBeginning = minuteBeginning;
        this.hourEnd = hourEnd;
        this.minuteEnd = minuteEnd;
        this.alarmState = alarmState;
        this.alarmHour = alarmHour;
        this.alarmMinute = alarmMinute;
    }
    @Generated(hash = 397975341)
    public TaskEntity() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getTitle() {
        return this.title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public int getTaskState() {
        return this.taskState;
    }
    public void setTaskState(int taskState) {
        this.taskState = taskState;
    }
    public int getCategoryId() {
        return this.categoryId;
    }
    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }
    public int getDay() {
        return this.day;
    }
    public void setDay(int day) {
        this.day = day;
    }
    public int getMonth() {
        return this.month;
    }
    public void setMonth(int month) {
        this.month = month;
    }
    public int getYear() {
        return this.year;
    }
    public void setYear(int year) {
        this.year = year;
    }
    public int getHourBeginning() {
        return this.hourBeginning;
    }
    public void setHourBeginning(int hourBeginning) {
        this.hourBeginning = hourBeginning;
    }
    public int getMinuteBeginning() {
        return this.minuteBeginning;
    }
    public void setMinuteBeginning(int minuteBeginning) {
        this.minuteBeginning = minuteBeginning;
    }
    public int getHourEnd() {
        return this.hourEnd;
    }
    public void setHourEnd(int hourEnd) {
        this.hourEnd = hourEnd;
    }
    public int getMinuteEnd() {
        return this.minuteEnd;
    }
    public void setMinuteEnd(int minuteEnd) {
        this.minuteEnd = minuteEnd;
    }
    public int getAlarmState() {
        return this.alarmState;
    }
    public void setAlarmState(int alarmState) {
        this.alarmState = alarmState;
    }
    public int getAlarmHour() {
        return this.alarmHour;
    }
    public void setAlarmHour(int alarmHour) {
        this.alarmHour = alarmHour;
    }
    public int getAlarmMinute() {
        return this.alarmMinute;
    }
    public void setAlarmMinute(int alarmMinute) {
        this.alarmMinute = alarmMinute;
    }
    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#delete(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 128553479)
    public void delete() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.delete(this);
    }
    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#refresh(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 1942392019)
    public void refresh() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.refresh(this);
    }
    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#update(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 713229351)
    public void update() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.update(this);
    }
    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 424431507)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getTaskEntityDao() : null;
    }
}
