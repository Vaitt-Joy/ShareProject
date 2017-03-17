package com.vz.share.service;

import com.vz.share.entity.UserInfo;

import java.util.List;

/**
 * Created by vz on 2017/3/15.
 */
public interface UserService {
    List<UserInfo> getUser();
    void addUser(UserInfo userInfo);
    void updateUser(UserInfo userInfo);
    boolean deleteUser(UserInfo userInfo);
}
