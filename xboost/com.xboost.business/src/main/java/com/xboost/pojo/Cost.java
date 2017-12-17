package com.xboost.pojo;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/11/11 0011.
 */
public class Cost implements Serializable {
    private Integer id;
    private String scenariosId;
    private String modelType;         // 算法模型
    private String plan;           //计划
    private String sitePeopleWork;    //网点集散点人数
    private String distribPeopleWork;  //集配站集散点人数
    private String siteCode;            //网点编码
    private String siteType;            //网点类型
    private Integer totalVol;                 //总票数
    private Integer fullTimeStaff;                 //全职人数
    private Integer partTimeStaff;                 //兼职人数
    private Integer fullTimeSalary;                 //全职薪资
    private Integer partTimeSalary;                 //兼职薪资
    private Integer fullTimeWorkDay;                 //全职工作天数
    private Integer partTimeWorkDay;               //兼职工作天数
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

    public String getModelType() {
        return modelType;
    }

    public void setModelType(String modelType) {
        this.modelType = modelType;
    }

    public String getPlan() {
        return plan;
    }

    public void setPlan(String plan) {
        this.plan = plan;
    }

    public String getSitePeopleWork() {
        return sitePeopleWork;
    }

    public void setSitePeopleWork(String sitePeopleWork) {
        this.sitePeopleWork = sitePeopleWork;
    }

    public String getDistribPeopleWork() {
        return distribPeopleWork;
    }

    public void setDistribPeopleWork(String distribPeopleWork) {
        this.distribPeopleWork = distribPeopleWork;
    }

    public String getSiteCode() {
        return siteCode;
    }

    public void setSiteCode(String siteCode) {
        this.siteCode = siteCode;
    }

    public String getSiteType() {
        return siteType;
    }

    public void setSiteType(String siteType) {
        this.siteType = siteType;
    }

    public Integer getTotalVol() {
        return totalVol;
    }

    public void setTotalVol(Integer totalVol) {
        this.totalVol = totalVol;
    }

    public Integer getFullTimeStaff() {
        return fullTimeStaff;
    }

    public void setFullTimeStaff(Integer fullTimeStaff) {
        this.fullTimeStaff = fullTimeStaff;
    }

    public Integer getPartTimeStaff() {
        return partTimeStaff;
    }

    public void setPartTimeStaff(Integer partTimeStaff) {
        this.partTimeStaff = partTimeStaff;
    }

    public Integer getFullTimeSalary() {
        return fullTimeSalary;
    }

    public void setFullTimeSalary(Integer fullTimeSalary) {
        this.fullTimeSalary = fullTimeSalary;
    }

    public Integer getPartTimeSalary() {
        return partTimeSalary;
    }

    public void setPartTimeSalary(Integer partTimeSalary) {
        this.partTimeSalary = partTimeSalary;
    }

    public Integer getFullTimeWorkDay() {
        return fullTimeWorkDay;
    }

    public void setFullTimeWorkDay(Integer fullTimeWorkDay) {
        this.fullTimeWorkDay = fullTimeWorkDay;
    }

    public Integer getPartTimeWorkDay() {
        return partTimeWorkDay;
    }

    public void setPartTimeWorkDay(Integer partTimeWorkDay) {
        this.partTimeWorkDay = partTimeWorkDay;
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
