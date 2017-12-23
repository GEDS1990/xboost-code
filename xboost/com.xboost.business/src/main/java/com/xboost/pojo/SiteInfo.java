package com.xboost.pojo;

import org.quartz.utils.counter.sampled.TimeStampedCounterValue;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * Created by Administrator on 2017/11/5 0005.
 */
public class SiteInfo implements Serializable {
    private Integer id;
    private String scenariosId;       //场景Id
    private String siteCode;              //网点编码
    private String siteLongitude;           //网点经度
    private String siteLatitude;            //网点纬度
    private String siteName;                 //网点名称
    private String siteAddress;             //网点地址
    private String siteNightDelivery;      //是否可以做夜配集散点
    private String siteArea;               //场地运营面积(平方米)
    private String siteType;            //网点类型
    private String carNum;                //停货车数(辆)
    private String largeCarModel;          //进出最大车型（T）
    private String maxOperateNum;        //单一批量操作处理量上限(票)
    private String distribCenter;                   //预留字段(指定集散点)
    private String totalVol;
    private String fullTimeStaff1;
    private String partTimeStaff1;
    private String full_time_staff2;
    private String partTimeStaff2;
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

    public String getSiteCode() {
        return siteCode;
    }

    public void setSiteCode(String siteCode) {
        this.siteCode = siteCode;
    }

    public String getSiteLongitude() {
        return siteLongitude;
    }

    public void setSiteLongitude(String siteLongitude) {
        this.siteLongitude = siteLongitude;
    }

    public String getSiteLatitude() {
        return siteLatitude;
    }

    public void setSiteLatitude(String siteLatitude) {
        this.siteLatitude = siteLatitude;
    }

    public String getSiteName() {
        return siteName;
    }

    public void setSiteName(String siteName) {
        this.siteName = siteName;
    }

    public String getSiteAddress() {
        return siteAddress;
    }

    public void setSiteAddress(String siteAddress) {
        this.siteAddress = siteAddress;
    }

    public String getSiteNightDelivery() {
        return siteNightDelivery;
    }

    public void setSiteNightDelivery(String siteNightDelivery) {
        this.siteNightDelivery = siteNightDelivery;
    }

    public String getSiteArea() {
        return siteArea;
    }

    public void setSiteArea(String siteArea) {
        this.siteArea = siteArea;
    }

    public String getSiteType() {
        return siteType;
    }

    public void setSiteType(String siteType) {
        this.siteType = siteType;
    }

    public String getCarNum() {
        return carNum;
    }

    public void setCarNum(String carNum) {
        this.carNum = carNum;
    }

    public String getLargeCarModel() {
        return largeCarModel;
    }

    public void setLargeCarModel(String largeCarModel) {
        this.largeCarModel = largeCarModel;
    }

    public String getMaxOperateNum() {
        return maxOperateNum;
    }

    public void setMaxOperateNum(String maxOperateNum) {
        this.maxOperateNum = maxOperateNum;
    }

    public String getDistribCenter() {
        return distribCenter;
    }

    public void setDistribCenter(String distribCenter) {
        this.distribCenter = distribCenter;
    }

    public String getTotalVol() {
        return totalVol;
    }

    public void setTotalVol(String totalVol) {
        this.totalVol = totalVol;
    }

    public String getFullTimeStaff1() {
        return fullTimeStaff1;
    }

    public void setFullTimeStaff1(String fullTimeStaff1) {
        this.fullTimeStaff1 = fullTimeStaff1;
    }

    public String getPartTimeStaff1() {
        return partTimeStaff1;
    }

    public void setPartTimeStaff1(String partTimeStaff1) {
        this.partTimeStaff1 = partTimeStaff1;
    }

    public String getFull_time_staff2() {
        return full_time_staff2;
    }

    public void setFull_time_staff2(String full_time_staff2) {
        this.full_time_staff2 = full_time_staff2;
    }

    public String getPartTimeStaff2() {
        return partTimeStaff2;
    }

    public void setPartTimeStaff2(String partTimeStaff2) {
        this.partTimeStaff2 = partTimeStaff2;
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
