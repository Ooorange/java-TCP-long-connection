package com.orange.blog.storage;

import com.orange.blog.database.bean.UserFriends;

import java.util.List;

/**
 * Created by orange on 16/6/22.
 */
public class UserFriendManager {

    private UserFriendManager(){}

    public static UserFriendManager instance;

    public static UserFriendManager getInstance(){
        if (instance==null){
            synchronized (UserFriendManager.class){
                if (instance==null){
                    instance=new UserFriendManager();
                    return instance;
                }
            }
        }
        return instance;
    }


    public void insertUserFriends(List<UserFriends> userFriendses){
        DBInit.getInstance().getAsyncSession().insertOrReplaceInTx(UserFriends.class,userFriendses);
    }

    public List<UserFriends> getUserFriends(String selfUUID){
       return DataControl.getUserFriendDao().loadAll();
    }
}
