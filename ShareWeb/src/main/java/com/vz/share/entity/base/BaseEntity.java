package com.vz.share.entity.base;

import com.vz.share.utils.JsonUtil;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by VVz on 2017/3/16.
 *
 * @des TODO
 */
public class BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name= "ID")
    private String id;
    @Column(name = "CREATEDATE")
    private Date createDate;
    @Column(name = "UPDATEDATE")
    private Date updateDate;
    @Column(name = "CREATEUSER")
    private String createUser;
    @Column(name = "UPDATEUSER")
    private String updateUser;

    /**
     * 拷贝自身
     *
     * @return
     */
    @SuppressWarnings("unchecked")
    public <T> T copy() {
        String json = JsonUtil.toJson(this);
        return (T) JsonUtil.fromJson(json, this.getClass());
    }

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }

    // property
    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    public String getUpdateUser() {
        return updateUser;
    }

    public void setUpdateUser(String updateUser) {
        this.updateUser = updateUser;
    }
}
