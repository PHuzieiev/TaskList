package com.apps.newstudio.tasklist.data.storage.models;

import java.util.Map;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.AbstractDaoSession;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.identityscope.IdentityScopeType;
import org.greenrobot.greendao.internal.DaoConfig;

import com.apps.newstudio.tasklist.data.storage.models.TaskEntity;

import com.apps.newstudio.tasklist.data.storage.models.TaskEntityDao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.

/**
 * {@inheritDoc}
 * 
 * @see org.greenrobot.greendao.AbstractDaoSession
 */
public class DaoSession extends AbstractDaoSession {

    private final DaoConfig taskEntityDaoConfig;

    private final TaskEntityDao taskEntityDao;

    public DaoSession(Database db, IdentityScopeType type, Map<Class<? extends AbstractDao<?, ?>>, DaoConfig>
            daoConfigMap) {
        super(db);

        taskEntityDaoConfig = daoConfigMap.get(TaskEntityDao.class).clone();
        taskEntityDaoConfig.initIdentityScope(type);

        taskEntityDao = new TaskEntityDao(taskEntityDaoConfig, this);

        registerDao(TaskEntity.class, taskEntityDao);
    }
    
    public void clear() {
        taskEntityDaoConfig.clearIdentityScope();
    }

    public TaskEntityDao getTaskEntityDao() {
        return taskEntityDao;
    }

}
