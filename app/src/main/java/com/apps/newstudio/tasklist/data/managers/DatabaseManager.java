package com.apps.newstudio.tasklist.data.managers;

import com.apps.newstudio.tasklist.data.adapters.DataForLinearDiagram;
import com.apps.newstudio.tasklist.data.adapters.DataForTasksListItem;
import com.apps.newstudio.tasklist.data.storage.models.DaoSession;
import com.apps.newstudio.tasklist.data.storage.models.TaskEntity;
import com.apps.newstudio.tasklist.data.storage.models.TaskEntityDao;
import com.apps.newstudio.tasklist.utils.ConstantsManager;
import com.apps.newstudio.tasklist.utils.TaskListApplication;

import org.greenrobot.greendao.query.WhereCondition;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class DatabaseManager {


    private DaoSession mDaoSession;
    private TaskEntityDao mTaskEntityDao;

    /**
     * Constructor of DatabaseManager object
     */
    public DatabaseManager() {
        mDaoSession = TaskListApplication.getDaoSession();
        mTaskEntityDao = mDaoSession.getTaskEntityDao();
    }

    /**
     * Calculates the following id for new task
     *
     * @param id value for id task which will be checked
     * @return new id value
     */
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

    /**
     * Inserts new item in db
     *
     * @param task data for new item of new task
     */
    public void updateOrAddTask(TaskEntity task) {
        mTaskEntityDao.insertOrReplace(task);
    }

    /**
     * Gets list of DataForTasksListItem objects using day, month, year, state of task values.
     * Data from db will be converted in DataForTasksListItem objects.
     *
     * @param day       number of day in month
     * @param month     number of month in year
     * @param year      number of year
     * @param taskState state of task - active of finished task
     * @return list of DataForTasksListItem objects which contains information about task
     */
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

    /**
     * Gets list of TaskEntity objects using day, month, year, hour and minute values.
     *
     * @param day    number of day in month
     * @param month  number of month in year
     * @param year   number of year
     * @param hour   number of hour in day
     * @param minute number of minute in hour
     * @return list of TaskEntity objects which contains information about task
     */
    public List<TaskEntity> getTasksUsingDateAndTime(int day, int month, int year, int hour, int minute) {
        return mDaoSession.queryBuilder(TaskEntity.class)
                .where(new WhereCondition
                        .StringCondition("day = " + day + " and month = " + month + " and year = " + year
                        + " and ALARM_HOUR = " + hour
                        + " and ALARM_MINUTE = " + minute
                        + " and TASK_STATE = " + ConstantsManager.STATE_FLAG_ACTIVE
                        + " and ALARM_STATE = " + ConstantsManager.ALARM_FLAG_ON))
                .orderAsc(TaskEntityDao.Properties.HourBeginning, TaskEntityDao.Properties.MinuteBeginning)
                .list();
    }

    /**
     * Gets int array which contains total count of tasks, active tasks, finished tasks in some month.
     *
     * @param month number of month in year
     * @param year  number of year
     * @return int array which contains total count of tasks, active tasks, finished tasks
     */
    public int[] getCountOfTasksUsingMonth(int month, int year) {
        int[] result = new int[3];
        List<TaskEntity> list = mDaoSession.queryBuilder(TaskEntity.class)
                .where(new WhereCondition
                        .StringCondition("month = " + month + " and year = " + year +
                        " and TASK_STATE = " + ConstantsManager.STATE_FLAG_ACTIVE))
                .orderAsc(TaskEntityDao.Properties.HourBeginning, TaskEntityDao.Properties.MinuteBeginning)
                .list();
        result[1] = list.size();

        list = mDaoSession.queryBuilder(TaskEntity.class)
                .where(new WhereCondition
                        .StringCondition("month = " + month + " and year = " + year +
                        " and TASK_STATE = " + ConstantsManager.STATE_FLAG_DONE))
                .orderAsc(TaskEntityDao.Properties.HourBeginning, TaskEntityDao.Properties.MinuteBeginning)
                .list();
        result[2] = list.size();
        result[0] = result[1] + result[2];
        return result;
    }

    /**
     * Gets list of DataForLinearDiagram objects which are used for creation of linear diagram
     *
     * @param month number of month in year
     * @param year  number of year
     * @return list of DataForLinearDiagram objects
     */
    public List<DataForLinearDiagram> getDataForLinearDiagram(int month, int year) {
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.set(Calendar.MONTH, month - 1);
        calendar.set(Calendar.YEAR, year);
        int countOfDays = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);

        List<DataForLinearDiagram> result = new ArrayList<>();
        for (int i = 1; i <= countOfDays; i++) {
            int countActive = mDaoSession.queryBuilder(TaskEntity.class)
                    .where(new WhereCondition
                            .StringCondition("day = " + i + " and month = " + month + " and year = " + year +
                            " and TASK_STATE = " + ConstantsManager.STATE_FLAG_ACTIVE))
                    .orderAsc(TaskEntityDao.Properties.HourBeginning, TaskEntityDao.Properties.MinuteBeginning)
                    .list().size();

            int countDone = mDaoSession.queryBuilder(TaskEntity.class)
                    .where(new WhereCondition
                            .StringCondition("day = " + i + " and month = " + month + " and year = " + year +
                            " and TASK_STATE = " + ConstantsManager.STATE_FLAG_DONE))
                    .orderAsc(TaskEntityDao.Properties.HourBeginning, TaskEntityDao.Properties.MinuteBeginning)
                    .list().size();

            result.add(new DataForLinearDiagram(i, countActive, countDone));
        }
        return result;
    }

    /**
     * Gets list of DataForTasksListItem object using category of task and state of task.
     * Result contains main information of chosen tasks
     * @param category number of task category
     * @param taskState state of task - active or finished
     * @return list of DataForTasksListItem object
     */
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

    /**
     * Deletes some tasks from db
     * @param id id value of task which will be deleted
     */
    public void removeFromDB(Long id) {
        mTaskEntityDao.deleteByKey(id);
    }

    /**
     * Gets TaskEntity object using id value of task
     * @param id id value of task
     * @return TaskEntity object which contains main information of task
     */
    public TaskEntity getTaskEntityById(Long id) {
        return mTaskEntityDao.load(id);
    }

    /**
     * Changes state of task using id value of task
     * @param id id value of task
     */
    public void changeStateInBD(Long id) {
        TaskEntity task = getTaskEntityById(id);
        if (task.getTaskState() == ConstantsManager.STATE_FLAG_ACTIVE) {
            task.setTaskState(ConstantsManager.STATE_FLAG_DONE);
        } else {
            task.setTaskState(ConstantsManager.STATE_FLAG_ACTIVE);
        }
        updateOrAddTask(task);
    }

    /**
     * Gets list of TaskEntity object using category of task
     * @param category value of category id
     * @return TaskEntity object list which contains information of tasks
     */
    public List<TaskEntity> getCountByCategoryId(int category) {
        return mDaoSession.queryBuilder(TaskEntity.class)
                .where(new WhereCondition
                        .StringCondition("CATEGORY_ID = " + category))
                .list();
    }

    /**
     * Deletes some tasks from db using category id of task
     * @param category value of category id
     */
    public void deleteTasksByCategory(int category) {
        mTaskEntityDao.deleteInTx(getCountByCategoryId(category));
    }
}
