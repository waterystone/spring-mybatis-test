package com.adu.spring_test.mybatis.model;

import java.io.Serializable;
import java.util.Date;

import com.adu.spring_test.mybatis.enums.SexEnum;
import com.adu.spring_test.mybatis.util.Stringfy;

public class UserInfo extends Stringfy implements Serializable {
    private static final long serialVersionUID = 6323573554785370660L;

    private int id;
    private String userName;
    private SexEnum sex;
    private int age;
    private ProfInfo profInfo;
    private Date createTime;
    private Date updateTime;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public SexEnum getSex() {
        return sex;
    }

    public void setSex(SexEnum sex) {
        this.sex = sex;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public ProfInfo getProfInfo() {
        return profInfo;
    }

    public void setProfInfo(ProfInfo profInfo) {
        this.profInfo = profInfo;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}
