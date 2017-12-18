package com.xboost.pojo;

import java.io.Serializable;

public class Scenarios implements Serializable {
    private Integer id;
    private Integer userId;
    private String scenariosName;             //场景名称
    private String scenariosCategory;
    private String scenariosDesc;           //场景描述
    private String scenariosModel;            //场景所选模型
    private String scenariosOut;            //场景所需输出结果
    private String scenariosStatus;         //场景状态
    private String lastOpenTime;           //最近一次打开时间
    private String simulateFinishTime;    //算法运行结束时间
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

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getScenariosName() {
        return scenariosName;
    }

    public void setScenariosName(String scenariosName) {
        this.scenariosName = scenariosName;
    }

    public String getScenariosCategory() {
        return scenariosCategory;
    }

    public void setScenariosCategory(String scenariosCategory) {
        this.scenariosCategory = scenariosCategory;
    }

    public String getScenariosDesc() {
        return scenariosDesc;
    }

    public void setScenariosDesc(String scenariosDesc) {
        this.scenariosDesc = scenariosDesc;
    }

    public String getScenariosModel() {
        return scenariosModel;
    }

    public void setScenariosModel(String scenariosModel) {
        this.scenariosModel = scenariosModel;
    }

    public String getScenariosOut() {
        return scenariosOut;
    }

    public void setScenariosOut(String scenariosOut) {
        this.scenariosOut = scenariosOut;
    }

    public String getScenariosStatus() {
        return scenariosStatus;
    }

    public void setScenariosStatus(String scenariosStatus) {
        this.scenariosStatus = scenariosStatus;
    }

    public String getLastOpenTime() {
        return lastOpenTime;
    }

    public void setLastOpenTime(String lastOpenTime) {
        this.lastOpenTime = lastOpenTime;
    }

    public String getSimulateFinishTime() {
        return simulateFinishTime;
    }

    public void setSimulateFinishTime(String simulateFinishTime) {
        this.simulateFinishTime = simulateFinishTime;
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
