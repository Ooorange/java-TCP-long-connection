package com.orange.blog.storage;

import android.database.sqlite.SQLiteDatabase;

import com.orange.blog.common.ProjectApplication;
import com.orange.blog.database.dao.DaoMaster;
import com.orange.blog.database.dao.DaoSession;

import de.greenrobot.dao.async.AsyncSession;

/**
 * DaoSession这个作为同步的查询操作,AsyncSession作为插入更新的异步操作
 * Created by orange on 16/6/22.
 */
public class DBInit {
    private static DBInit instance;
    private static String ORANGEDATABASE="user.db";
    private DaoSession daoSession;
    private AsyncSession asyncSession;
    private SQLiteDatabase db;

    private DBInit(){
        DaoMaster.DevOpenHelper devOpenHelper=
                new DaoMaster.DevOpenHelper(ProjectApplication.getContext(),ORANGEDATABASE,null);
        db=devOpenHelper.getWritableDatabase();

        daoSession=new DaoMaster(db).newSession();
        asyncSession=daoSession.startAsyncSession();
    }

    public static DBInit getInstance(){
        if (instance==null){
            synchronized (DBInit.class){
                if (instance==null){
                    instance=new DBInit();
                    return instance;
                }
            }
        }
        return instance;
    }

    public AsyncSession getAsyncSession(){
        return asyncSession;
    }
    public DaoSession getDaoSession(){
        return daoSession;
    }
}
