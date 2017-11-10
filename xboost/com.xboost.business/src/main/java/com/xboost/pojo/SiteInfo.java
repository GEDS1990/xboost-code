package com.xboost.pojo;

import org.quartz.utils.counter.sampled.TimeStampedCounterValue;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * Created by Administrator on 2017/11/5 0005.
 */
public class SiteInfo implements Serializable {
    private Integer id;
    private String siteCode;
    private Float siteLongitude;           //网点经度
    private Float siteLatitude;            //网点纬度
    private String siteName;                 //网点名称
    private String siteAddress;             //网点地址
    private Integer siteNightDelivery;      //是否可以做夜配集散点
    private Float siteArea;               //场地运营面积(平方米)
    private String siteType;            //网点类型
    private Integer carNum;                //停货车数(辆)
    private String largeCarModel;          //进出最大车型（T）
    private Integer maxOperateNum;        //单一批量操作处理量上限(票)
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

    public String getSiteCode() {
        return siteCode;
    }

    public void setSiteCode(String siteCode) {
        this.siteCode = siteCode;
    }

    public Float getSiteLongitude() {
        return siteLongitude;
    }

    public void setSiteLongitude(Float siteLongitude) {
        this.siteLongitude = siteLongitude;
    }

    public Float getSiteLatitude() {
        return siteLatitude;
    }

    public void setSiteLatitude(Float siteLatitude) {
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

    public Integer getSiteNightDelivery() {
        return siteNightDelivery;
    }

    public void setSiteNightDelivery(Integer siteNightDelivery) {
        this.siteNightDelivery = siteNightDelivery;
    }

    public Float getSiteArea() {
        return siteArea;
    }

    public void setSiteArea(Float siteArea) {
        this.siteArea = siteArea;
    }

    public String getSiteType() {
        return siteType;
    }

    public void setSiteType(String siteType) {
        this.siteType = siteType;
    }

    public Integer getCarNum() {
        return carNum;
    }

    public void setCarNum(Integer carNum) {
        this.carNum = carNum;
    }

    public String getLargeCarModel() {
        return largeCarModel;
    }

    public void setLargeCarModel(String largeCarModel) {
        this.largeCarModel = largeCarModel;
    }

    public Integer getMaxOperateNum() {
        return maxOperateNum;
    }

    public void setMaxOperateNum(Integer maxOperateNum) {
        this.maxOperateNum = maxOperateNum;
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
