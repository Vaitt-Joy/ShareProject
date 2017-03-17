package com.vz.share.entity;

import com.vz.share.entity.base.BaseEntity;

import javax.persistence.Column;
import java.util.Date;

/**
 * Created by vz on 2017/3/15
 * 用户的实体类
 */
public class UserInfo extends BaseEntity {

    @Column(name = "LOGINNAME")
    private String loginName;//登录号
    @Column(name = "PHONE")
    private String phone;// 手机号
    @Column(name = "PHONE")
    private String password;// 用户密码
    @Column(name = "USERNAME")
    private String userName;// 用户名
    @Column(name = "EMAIL")
    private String email; // 邮箱
    @Column(name = "AGE")
    private int age;// 年龄
    @Column(name = "LASTLOGINDATE")
    private Date lastLoginDate;// 最后登录时间

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public Date getLastLoginDate() {
        return lastLoginDate;
    }

    public void setLastLoginDate(Date lastLoginDate) {
        this.lastLoginDate = lastLoginDate;
    }
}
