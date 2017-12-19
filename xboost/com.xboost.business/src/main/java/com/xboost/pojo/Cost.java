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
    private String siteCode;            //网点编码
    private String siteType;            //网点类型
    private Integer totalVol;                 //总票数
    private Integer fullTimeStaff;                 //全职人数
    private Integer partTimeStaff;                 //兼职人数
    private Integer fullTimeSalary;                 //全职薪资
    private Integer partTimeSalary;                 //兼职薪资
    private Integer fullTimeWorkDay;                 //全职工作天数
    private Integer partTimeWorkDay;               //兼职工作天数
    private Double sum1;                         //收端派端depot&distrib.center单日人工成本
    private Double sum2;                         //支线depot单日人工成本
    private Double sum3;                         //支线distrib.center单日人工成本
    private Double totalDailyLaborCost;            //单日总体人工成本
    private Double pickupTransportCost;          //收端运输成本
    private Double branchTransportCost;         //支线运输成本
    private Double deliveryTransportCost;      //派端运输成本
    private Double totalCost;                       //总成本
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

    public Double getSum1() {
        return sum1;
    }

    public void setSum1(Double sum1) {
        this.sum1 = sum1;
    }

    public Double getSum2() {
        return sum2;
    }

    public void setSum2(Double sum2) {
        this.sum2 = sum2;
    }

    public Double getSum3() {
        return sum3;
    }

    public void setSum3(Double sum3) {
        this.sum3 = sum3;
    }

    public Double getTotalDailyLaborCost() {
        return totalDailyLaborCost;
    }

    public void setTotalDailyLaborCost(Double totalDailyLaborCost) {
        this.totalDailyLaborCost = totalDailyLaborCost;
    }

    public Double getPickupTransportCost() {
        return pickupTransportCost;
    }

    public void setPickupTransportCost(Double pickupTransportCost) {
        this.pickupTransportCost = pickupTransportCost;
    }

    public Double getBranchTransportCost() {
        return branchTransportCost;
    }

    public void setBranchTransportCost(Double branchTransportCost) {
        this.branchTransportCost = branchTransportCost;
    }

    public Double getDeliveryTransportCost() {
        return deliveryTransportCost;
    }

    public void setDeliveryTransportCost(Double deliveryTransportCost) {
        this.deliveryTransportCost = deliveryTransportCost;
    }

    public Double getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(Double totalCost) {
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
