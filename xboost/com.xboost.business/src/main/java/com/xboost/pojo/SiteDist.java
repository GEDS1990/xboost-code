package com.xboost.pojo;

import org.springframework.transaction.annotation.Transactional;

import javax.inject.Named;
import java.io.Serializable;

/**
 * Created by Administrator on 2017/11/11 0011.
 */
public class SiteDist implements Serializable {
    private Integer id;
    private String scenariosId;
    private String carType;         // 车型
    private String siteCollect;           //收件网点
    private String siteDelivery;            //派件网点
    private Float carDistance;                 //车辆运行距离(公里)
    private String durationNightDelivery;             //夜配运行时长（分钟）货车
    private String durationNightDelivery2;           //百度
    private String durationNightDelivery3;          //滴滴
    private String durationNightDelivery4;          //达达
    private String durationNightDelivery5;          //备用
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

    public String getCarType() {
        return carType;
    }

    public void setCarType(String carType) {
        this.carType = carType;
    }

    public String getSiteCollect() {
        return siteCollect;
    }

    public void setSiteCollect(String siteCollect) {
        this.siteCollect = siteCollect;
    }

    public String getSiteDelivery() {
        return siteDelivery;
    }

    public void setSiteDelivery(String siteDelivery) {
        this.siteDelivery = siteDelivery;
    }

    public Float getCarDistance() {
        return carDistance;
    }

    public void setCarDistance(Float carDistance) {
        this.carDistance = carDistance;
    }

    public String getDurationNightDelivery() {
        return durationNightDelivery;
    }

    public void setDurationNightDelivery(String durationNightDelivery) {
        this.durationNightDelivery = durationNightDelivery;
    }

    public String getDurationNightDelivery2() {
        return durationNightDelivery2;
    }

    public void setDurationNightDelivery2(String durationNightDelivery2) {
        this.durationNightDelivery2 = durationNightDelivery2;
    }

    public String getDurationNightDelivery3() {
        return durationNightDelivery3;
    }

    public void setDurationNightDelivery3(String durationNightDelivery3) {
        this.durationNightDelivery3 = durationNightDelivery3;
    }

    public String getDurationNightDelivery4() {
        return durationNightDelivery4;
    }

    public void setDurationNightDelivery4(String durationNightDelivery4) {
        this.durationNightDelivery4 = durationNightDelivery4;
    }

    public String getDurationNightDelivery5() {
        return durationNightDelivery5;
    }

    public void setDurationNightDelivery5(String durationNightDelivery5) {
        this.durationNightDelivery5 = durationNightDelivery5;
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
