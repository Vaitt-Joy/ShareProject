package com.vz.share.dao;

import com.vz.share.dao.base.BaseDao;
import com.vz.share.entity.UserInfo;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * Created by VVz on 2017/3/17.
 *
 * @des
 */
@RequestMapping("userDao")
public class UserDaoImpl extends BaseDao<UserInfo> implements UserDao {

    public UserInfo getUser(String id) {
        return findObject("from User u where u.id=?");
    }

    public List<UserInfo> getAllUser() {
        return find("from User");
    }

    public void addUser(UserInfo user) {
        add(user);
    }

    public boolean delUser(String id) {
        remove(getUser(id));
        return getUser(id) == null;
    }

    public void updateUser(UserInfo user) {
        update(user);
    }
}
