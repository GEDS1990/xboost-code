package com.xboost.pojo;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/11/11 0011.
 */
public class JieliResult implements Serializable {
    private Integer id;
    private String scenariosId;
    private String timeBucket;         //起始集散点ID-终点集散点ID-时间段ID
    private String connection;         //起始集散点ID-终点集散点ID
    private String inboundId;         //起始集散点ID
    private String outboundId;         //终点集散点ID
    private String distance;         //距离
    private String crossRiver;         //是否跨江（上海模型特有）
    private String minutes;         //运输时间
    private String timeId;         //时间段ID
    private String jConnectionId;         //唯一ID标识符(件量id)
    private String kmhDidi;         //滴滴时速
    private String kmhTruck;         //货车时速
    private String kmhBike;         //百度时速
    private String minDidi;          //滴滴时间
    private String costBike;         //百度成本
    private String costDidi;         //滴滴成本
    private String costTruck;         //货车成本
    private String costData;         //达达成本
    private String volume;         //件量
    private String routeCnt;         //
    private String truckNum;         //货车数量
    private String bikeNum;         //百度数量
    private String didiNum;         //滴滴数量
    private String dadaNum;         //达达数量
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

    public String getTimeBucket() {
        return timeBucket;
    }

    public void setTimeBucket(String timeBucket) {
        this.timeBucket = timeBucket;
    }

    public String getConnection() {
        return connection;
    }

    public void setConnection(String connection) {
        this.connection = connection;
    }

    public String getInboundId() {
        return inboundId;
    }

    public void setInboundId(String inboundId) {
        this.inboundId = inboundId;
    }

    public String getOutboundId() {
        return outboundId;
    }

    public void setOutboundId(String outboundId) {
        this.outboundId = outboundId;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getCrossRiver() {
        return crossRiver;
    }

    public void setCrossRiver(String crossRiver) {
        this.crossRiver = crossRiver;
    }

    public String getMinutes() {
        return minutes;
    }

    public void setMinutes(String minutes) {
        this.minutes = minutes;
    }

    public String getTimeId() {
        return timeId;
    }

    public void setTimeId(String timeId) {
        this.timeId = timeId;
    }

    public String getjConnectionId() {
        return jConnectionId;
    }

    public void setjConnectionId(String jConnectionId) {
        this.jConnectionId = jConnectionId;
    }

    public String getKmhDidi() {
        return kmhDidi;
    }

    public void setKmhDidi(String kmhDidi) {
        this.kmhDidi = kmhDidi;
    }

    public String getKmhTruck() {
        return kmhTruck;
    }

    public void setKmhTruck(String kmhTruck) {
        this.kmhTruck = kmhTruck;
    }

    public String getKmhBike() {
        return kmhBike;
    }

    public void setKmhBike(String kmhBike) {
        this.kmhBike = kmhBike;
    }

    public String getMinDidi() {
        return minDidi;
    }

    public void setMinDidi(String minDidi) {
        this.minDidi = minDidi;
    }

    public String getCostBike() {
        return costBike;
    }

    public void setCostBike(String costBike) {
        this.costBike = costBike;
    }

    public String getCostDidi() {
        return costDidi;
    }

    public void setCostDidi(String costDidi) {
        this.costDidi = costDidi;
    }

    public String getCostTruck() {
        return costTruck;
    }

    public void setCostTruck(String costTruck) {
        this.costTruck = costTruck;
    }

    public String getCostData() {
        return costData;
    }

    public void setCostData(String costData) {
        this.costData = costData;
    }

    public String getVolume() {
        return volume;
    }

    public void setVolume(String volume) {
        this.volume = volume;
    }


    public String getTruckNum() {
        return truckNum;
    }

    public void setTruckNum(String truckNum) {
        this.truckNum = truckNum;
    }

    public String getRouteCnt() {
        return routeCnt;
    }

    public void setRouteCnt(String routeCnt) {
        this.routeCnt = routeCnt;
    }

    public String getBikeNum() {
        return bikeNum;
    }

    public void setBikeNum(String bikeNum) {
        this.bikeNum = bikeNum;
    }

    public String getDidiNum() {
        return didiNum;
    }

    public void setDidiNum(String didiNum) {
        this.didiNum = didiNum;
    }

    public String getDadaNum() {
        return dadaNum;
    }

    public void setDadaNum(String dadaNum) {
        this.dadaNum = dadaNum;
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
