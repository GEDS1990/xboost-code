package com.xboost.pojo;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/11/11 0011.
 */
public class CarLicence implements Serializable {
    private Integer id;
    private String scenariosId;
    private String name;         // 车名
    private String busyIdle;
    private String type;           //车
    private String str1;                   //预留字段
    private String str2;                    //预留字段
    private String str3;                  //预留字段
    private String createTime;
    private String updateTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getScenariosId() {
        return scenariosId;
    }

    public void setScenariosId(String scenariosId) {
        this.scenariosId = scenariosId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getBusyIdle() {
        return busyIdle;
    }

    public void setBusyIdle(String busyIdle) {
        this.busyIdle = busyIdle;
    }

    public String getStr1() {
        return str1;
    }

    public void setStr1(String str1) {
        this.str1 = str1;
    }

    public String getStr2() {
        return str2;
    }

    public void setStr2(String str2) {
        this.str2 = str2;
    }

    public String getStr3() {
        return str3;
    }

    public void setStr3(String str3) {
        this.str3 = str3;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }
}
