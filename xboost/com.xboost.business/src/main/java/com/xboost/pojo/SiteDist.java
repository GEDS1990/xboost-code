package com.xboost.pojo;

import org.springframework.transaction.annotation.Transactional;

import javax.inject.Named;
import java.io.Serializable;

/**
 * Created by Administrator on 2017/11/11 0011.
 */
public class SiteDist implements Serializable {
    private Integer id;
    private Integer scenariosId;
    private String siteCollect;           //网点经度
    private String siteDelivery;            //网点纬度
    private Float carDistance;                 //网点名称
    private Double durationNightDelivery;             //网点地址
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

    public Integer getScenariosId() {
        return scenariosId;
    }

    public void setScenariosId(Integer scenariosId) {
        this.scenariosId = scenariosId;
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

    public Double getDurationNightDelivery() {
        return durationNightDelivery;
    }

    public void setDurationNightDelivery(Double durationNightDelivery) {
        this.durationNightDelivery = durationNightDelivery;
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
