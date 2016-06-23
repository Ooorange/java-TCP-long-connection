package com.orange.blog.storage;

import com.orange.blog.database.dao.UserFriendsDao;

/**
 * 这个作为同步的查询操作
 * Created by orange on 16/6/22.
 */
public class DataControl {

    public static UserFriendsDao getUserFriendDao(){
        return DBInit.getInstance().getDaoSession().getUserFriendsDao();
    }
}
