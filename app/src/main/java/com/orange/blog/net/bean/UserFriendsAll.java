package com.orange.blog.net.bean;

import com.orange.blog.database.bean.UserFriends;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import java.util.List;

/**
 * Created by orange on 16/6/22.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserFriendsAll {
    private List<UserFriends> userFriends;

    public List<UserFriends> getUserFriendses() {
        return userFriends;
    }

    public void setUserFriendses(List<UserFriends> userFriendses) {
        this.userFriends = userFriendses;
    }
}
