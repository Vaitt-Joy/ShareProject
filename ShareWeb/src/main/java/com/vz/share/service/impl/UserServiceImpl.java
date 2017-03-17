package com.vz.share.service.impl;

import com.vz.share.dao.UserDao;
import com.vz.share.entity.UserInfo;
import com.vz.share.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * Created by vz on 2017/3/15.
 */
@RequestMapping("userService")
public class UserServiceImpl implements UserService {
    @Autowired
    private UserDao userDao;

    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }

    public List<UserInfo> getUser() {
        return userDao.getAllUser();
    }

    public void addUser(UserInfo userInfo) {
        userDao.addUser(userInfo);
    }

    public void updateUser(UserInfo userInfo) {
        userDao.updateUser(userInfo);
    }

    public boolean deleteUser(UserInfo userInfo) {
        return userDao.delUser(userInfo.getId());
    }
}
