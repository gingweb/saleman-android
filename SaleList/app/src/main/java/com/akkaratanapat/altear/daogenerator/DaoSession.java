package com.akkaratanapat.altear.daogenerator;

import java.util.Map;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.AbstractDaoSession;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.identityscope.IdentityScopeType;
import org.greenrobot.greendao.internal.DaoConfig;

import com.akkaratanapat.altear.daogenerator.APIFailure;

import com.akkaratanapat.altear.daogenerator.APIFailureDao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.

/**
 * {@inheritDoc}
 * 
 * @see org.greenrobot.greendao.AbstractDaoSession
 */
public class DaoSession extends AbstractDaoSession {

    private final DaoConfig aPIFailureDaoConfig;

    private final APIFailureDao aPIFailureDao;

    public DaoSession(Database db, IdentityScopeType type, Map<Class<? extends AbstractDao<?, ?>>, DaoConfig>
            daoConfigMap) {
        super(db);

        aPIFailureDaoConfig = daoConfigMap.get(APIFailureDao.class).clone();
        aPIFailureDaoConfig.initIdentityScope(type);

        aPIFailureDao = new APIFailureDao(aPIFailureDaoConfig, this);

        registerDao(APIFailure.class, aPIFailureDao);
    }
    
    public void clear() {
        aPIFailureDaoConfig.clearIdentityScope();
    }

    public APIFailureDao getAPIFailureDao() {
        return aPIFailureDao;
    }

}
