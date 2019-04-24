package com.apps.newstudio.tasklist.data.managers;

import com.apps.newstudio.tasklist.data.adapters.DataForTasksListItem;
import com.apps.newstudio.tasklist.data.storage.models.DaoSession;
import com.apps.newstudio.tasklist.data.storage.models.TaskEntity;
import com.apps.newstudio.tasklist.data.storage.models.TaskEntityDao;
import com.apps.newstudio.tasklist.utils.ConstantsManager;
import com.apps.newstudio.tasklist.utils.TaskListApplication;

import org.greenrobot.greendao.query.WhereCondition;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class DatabaseManager {


    private DaoSession mDaoSession;
    private TaskEntityDao mTaskEntityDao;

    public DatabaseManager() {
        mDaoSession = TaskListApplication.getDaoSession();
        mTaskEntityDao = mDaoSession.getTaskEntityDao();
    }

    public Long getTaskNewId(Long id) {
        if (id.equals(ConstantsManager.TASK_ID_NONE)) {
            List<TaskEntity> list = mDaoSession.queryBuilder(TaskEntity.class)
                    .orderAsc(TaskEntityDao.Properties.Id)
                    .list();
            Long result = 1L;
            if (list.size() != 0) {
                result = list.get(list.size() - 1).getId() + 1;
            }
            return result;
        } else {
            return id;
        }
    }

    public void updateOrAddTask(TaskEntity task) {
        mTaskEntityDao.insertOrReplace(task);
    }

    public List<DataForTasksListItem> getTasksUsingDate(int day, int month, int year, int taskState) {
        List<DataForTasksListItem> result = new ArrayList<>();
        List<TaskEntity> list = mDaoSession.queryBuilder(TaskEntity.class)
                .where(new WhereCondition
                        .StringCondition("day = " + day + " and month = " + month + " and year = " + year + " and TASK_STATE = " + taskState))
                .orderAsc(TaskEntityDao.Properties.HourBeginning, TaskEntityDao.Properties.MinuteBeginning)
                .list();

        for (TaskEntity task : list) {
            result.add(new DataForTasksListItem(task.getId(), task.getTitle(), task.getCategoryId(),
                    day, month, year, task.getHourBeginning(), task.getMinuteBeginning(),
                    task.getHourEnd(), task.getMinuteEnd(), false, task.getAlarmState()));
        }
        return result;
    }

    public List<DataForTasksListItem> getTasksUsingCategory(int category, int taskState) {
        List<DataForTasksListItem> result = new ArrayList<>();
        List<TaskEntity> list = mDaoSession.queryBuilder(TaskEntity.class)
                .where(new WhereCondition
                        .StringCondition("CATEGORY_ID = " + category + " and TASK_STATE = " + taskState))
                .orderAsc(TaskEntityDao.Properties.Year, TaskEntityDao.Properties.Month, TaskEntityDao.Properties.Day,
                        TaskEntityDao.Properties.HourBeginning, TaskEntityDao.Properties.MinuteBeginning)
                .list();

        for (TaskEntity task : list) {
            result.add(new DataForTasksListItem(task.getId(), task.getTitle(), task.getCategoryId(),
                    task.getDay(), task.getMonth(), task.getYear(), task.getHourBeginning(), task.getMinuteBeginning(),
                    task.getHourEnd(), task.getMinuteEnd(), false, task.getAlarmState()));
        }
        return result;
    }

    public void removeFromDB(Long id) {
        mTaskEntityDao.deleteByKey(id);
    }

    public TaskEntity getTaskEntityById(Long id) {
        return mTaskEntityDao.load(id);
    }

    public void changeStateInBD(Long id) {
        TaskEntity task = getTaskEntityById(id);
        if (task.getTaskState() == ConstantsManager.STATE_FLAG_ACTIVE) {
            task.setTaskState(ConstantsManager.STATE_FLAG_DONE);
        } else {
            task.setTaskState(ConstantsManager.STATE_FLAG_ACTIVE);
        }
        updateOrAddTask(task);
    }

    public List<TaskEntity> getCountByCategoryId(int id) {
        return mDaoSession.queryBuilder(TaskEntity.class)
                .where(new WhereCondition
                        .StringCondition("CATEGORY_ID = " + id))
                .list();
    }

    public void deleteTasksByCategory(int id) {
        mTaskEntityDao.deleteInTx(getCountByCategoryId(id));
    }
}
