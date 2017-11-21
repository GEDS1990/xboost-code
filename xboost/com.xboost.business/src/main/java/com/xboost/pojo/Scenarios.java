package com.xboost.pojo;

public class Scenarios {
    private Integer id;
    private Integer userId;
    private String scenarios_name;
    private String scenarios_desc;           //网点经度
    private String scenarios_model;            //网点纬度
    private String scenarios_out;            //网点纬度
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

    public String getScenarios_name() {
        return scenarios_name;
    }

    public void setScenarios_name(String scenarios_name) {
        this.scenarios_name = scenarios_name;
    }

    public String getScenarios_desc() {
        return scenarios_desc;
    }

    public void setScenarios_desc(String scenarios_desc) {
        this.scenarios_desc = scenarios_desc;
    }

    public String getScenarios_model() {
        return scenarios_model;
    }

    public void setScenarios_model(String scenarios_model) {
        this.scenarios_model = scenarios_model;
    }

    public String getScenarios_out() {
        return scenarios_out;
    }

    public void setScenarios_out(String scenarios_out) {
        this.scenarios_out = scenarios_out;
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
