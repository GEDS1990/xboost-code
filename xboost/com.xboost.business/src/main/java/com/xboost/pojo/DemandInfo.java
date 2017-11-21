package com.xboost.pojo;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/11/5 0005.
 */
public class DemandInfo implements Serializable {
    private Integer id;
    private String date;      //日期
    private String siteCodeCollect;    //收件网点编码
    private String siteCodeDelivery;            //派件网点编码
    private String productType;                 //产品类型
    private String durationStart;             //时段(开始)
    private String durationEnd;         //时段(结束）
    private Float weight;               //重量（公斤）
    private Integer votes;            //票数（票）
    private Integer ageing;                //时效要求(小时)
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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getSiteCodeCollect() {
        return siteCodeCollect;
    }

    public void setSiteCodeCollect(String siteCodeCollect) {
        this.siteCodeCollect = siteCodeCollect;
    }

    public String getSiteCodeDelivery() {
        return siteCodeDelivery;
    }

    public void setSiteCodeDelivery(String siteCodeDelivery) {
        this.siteCodeDelivery = siteCodeDelivery;
    }

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }

    public String getDurationStart() {
        return durationStart;
    }

    public void setDurationStart(String durationStart) {
        this.durationStart = durationStart;
    }

    public String getDurationEnd() {
        return durationEnd;
    }

    public void setDurationEnd(String durationEnd) {
        this.durationEnd = durationEnd;
    }

    public Float getWeight() {
        return weight;
    }

    public void setWeight(Float weight) {
        this.weight = weight;
    }

    public Integer getVotes() {
        return votes;
    }

    public void setVotes(Integer votes) {
        this.votes = votes;
    }

    public Integer getAgeing() {
        return ageing;
    }

    public void setAgeing(Integer ageing) {
        this.ageing = ageing;
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
