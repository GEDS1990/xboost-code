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
    private String selected;       //是否被选中0否1是
    private String sitePeopleWork;    //网点集散点人数
    private String distribPeopleWork;  //集配站集散点人数
    private String siteCount;            //网点数量
    private String peopleNumPerSite;    //每个网点的人数
    private String fullTimeStaff;                 //全职人数
    private String partTimeStaff;                 //兼职人数
    private String fullTimeSalary;                 //全职薪资
    private String partTimeSalary;                 //兼职薪资
    private String fullTimeWorkDay;                 //全职工作天数
    private String partTimeWorkDay;               //兼职工作天数
    private String sum1;                         //收端派端depot&distrib.center单日人工成本
    private String sum2;                         //支线depot单日人工成本
    private String sum3;                         //支线distrib.center单日人工成本
    private String totalDailyLaborCost;            //单日总体人工成本
    private String pickupTransportCost;          //收端运输成本
    private String branchTransportCost;         //支线运输成本
    private String deliveryTransportCost;      //派端运输成本
    private String totalCost;                       //总成本
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

    public String getSelected() {
        return selected;
    }

    public void setSelected(String selected) {
        this.selected = selected;
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

    public String getSiteCount() {
        return siteCount;
    }

    public void setSiteCount(String siteCount) {
        this.siteCount = siteCount;
    }

    public String getPeopleNumPerSite() {
        return peopleNumPerSite;
    }

    public void setPeopleNumPerSite(String peopleNumPerSite) {
        this.peopleNumPerSite = peopleNumPerSite;
    }

    public String getFullTimeStaff() {
        return fullTimeStaff;
    }

    public void setFullTimeStaff(String fullTimeStaff) {
        this.fullTimeStaff = fullTimeStaff;
    }

    public String getPartTimeStaff() {
        return partTimeStaff;
    }

    public void setPartTimeStaff(String partTimeStaff) {
        this.partTimeStaff = partTimeStaff;
    }

    public String getFullTimeSalary() {
        return fullTimeSalary;
    }

    public void setFullTimeSalary(String fullTimeSalary) {
        this.fullTimeSalary = fullTimeSalary;
    }

    public String getPartTimeSalary() {
        return partTimeSalary;
    }

    public void setPartTimeSalary(String partTimeSalary) {
        this.partTimeSalary = partTimeSalary;
    }

    public String getFullTimeWorkDay() {
        return fullTimeWorkDay;
    }

    public void setFullTimeWorkDay(String fullTimeWorkDay) {
        this.fullTimeWorkDay = fullTimeWorkDay;
    }

    public String getPartTimeWorkDay() {
        return partTimeWorkDay;
    }

    public void setPartTimeWorkDay(String partTimeWorkDay) {
        this.partTimeWorkDay = partTimeWorkDay;
    }

    public String getSum1() {
        return sum1;
    }

    public void setSum1(String sum1) {
        this.sum1 = sum1;
    }

    public String getSum2() {
        return sum2;
    }

    public void setSum2(String sum2) {
        this.sum2 = sum2;
    }

    public String getSum3() {
        return sum3;
    }

    public void setSum3(String sum3) {
        this.sum3 = sum3;
    }

    public String getTotalDailyLaborCost() {
        return totalDailyLaborCost;
    }

    public void setTotalDailyLaborCost(String totalDailyLaborCost) {
        this.totalDailyLaborCost = totalDailyLaborCost;
    }

    public String getPickupTransportCost() {
        return pickupTransportCost;
    }

    public void setPickupTransportCost(String pickupTransportCost) {
        this.pickupTransportCost = pickupTransportCost;
    }

    public String getBranchTransportCost() {
        return branchTransportCost;
    }

    public void setBranchTransportCost(String branchTransportCost) {
        this.branchTransportCost = branchTransportCost;
    }

    public String getDeliveryTransportCost() {
        return deliveryTransportCost;
    }

    public void setDeliveryTransportCost(String deliveryTransportCost) {
        this.deliveryTransportCost = deliveryTransportCost;
    }

    public String getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(String totalCost) {
        this.totalCost = totalCost;
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
