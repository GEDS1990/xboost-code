package com.xboost.pojo;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/11/5 0005.
 */
public class Transportation implements Serializable {
    private Integer id;
    private String scenarioId;         //场景id
    private String carType;             //车型
    private Integer carNum;            //数量
    private String carSource;           //车辆来源
    private Float speed;             //时速
    private Integer maxLoad;        //最大装载（件）
    private Integer durationUnloadFull;               //满载卸车耗时(分钟)
    private Float maxDistance;            //最远距离（KM）
    private Float carCost1;               //车辆成本1
    private Float carCost2;               //车辆成本2
    private Float carCost3;               //车辆成本3
    private Float singleVoteCost1;          //单票支线成本1
    private Float singleVoteCost2;          //单票支线成本2
    private Float singleVoteCost3;          //单票支线成本3
    private String str1;                   //预留字段
    private String str2;                   //预留字段
    private String str3;                  //预留字段
    private String createTime;
    private String updateTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getScenarioId() {
        return scenarioId;
    }

    public void setScenarioId(String scenarioId) {
        this.scenarioId = scenarioId;
    }

    public String getCarType() {
        return carType;
    }

    public void setCarType(String carType) {
        this.carType = carType;
    }

    public Integer getCarNum() {
        return carNum;
    }

    public void setCarNum(Integer carNum) {
        this.carNum = carNum;
    }

    public String getCarSource() {
        return carSource;
    }

    public void setCarSource(String carSource) {
        this.carSource = carSource;
    }

    public Float getSpeed() {
        return speed;
    }

    public void setSpeed(Float speed) {
        this.speed = speed;
    }

    public Integer getMaxLoad() {
        return maxLoad;
    }

    public void setMaxLoad(Integer maxLoad) {
        this.maxLoad = maxLoad;
    }

    public Integer getDurationUnloadFull() {
        return durationUnloadFull;
    }

    public void setDurationUnloadFull(Integer durationUnloadFull) {
        this.durationUnloadFull = durationUnloadFull;
    }

    public Float getMaxDistance() {
        return maxDistance;
    }

    public void setMaxDistance(Float maxDistance) {
        this.maxDistance = maxDistance;
    }

    public Float getCarCost1() {
        return carCost1;
    }

    public void setCarCost1(Float carCost1) {
        this.carCost1 = carCost1;
    }

    public Float getCarCost2() {
        return carCost2;
    }

    public void setCarCost2(Float carCost2) {
        this.carCost2 = carCost2;
    }

    public Float getCarCost3() {
        return carCost3;
    }

    public void setCarCost3(Float carCost3) {
        this.carCost3 = carCost3;
    }

    public Float getSingleVoteCost1() {
        return singleVoteCost1;
    }

    public void setSingleVoteCost1(Float singleVoteCost1) {
        this.singleVoteCost1 = singleVoteCost1;
    }

    public Float getSingleVoteCost2() {
        return singleVoteCost2;
    }

    public void setSingleVoteCost2(Float singleVoteCost2) {
        this.singleVoteCost2 = singleVoteCost2;
    }

    public Float getSingleVoteCost3() {
        return singleVoteCost3;
    }

    public void setSingleVoteCost3(Float singleVoteCost3) {
        this.singleVoteCost3 = singleVoteCost3;
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
