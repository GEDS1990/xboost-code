package com.xboost.pojo;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/11/5 0005.
 */
public class Ride implements Serializable {
    private Integer id;
    private String scenariosId;            //场景id
    private String userId;            //
    private String rideId;           //排班编号
    private String timeId;
    private String routeCount;      //车辆编号
    private String sequence;            //停靠点顺序
    private String curLoc;            //当前网点
    private String nextCurLoc;            //下一个停靠网点代码
    private String carType;           //车型
    private String carName;          //车辆名称
    private String arrTime;
    private String endTime;
    private String sbVol;
    private String unloadVol;
    private String carGoods;
    private String calcDis;
    private String str1;                    //预留字段
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

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getRideId() {
        return rideId;
    }

    public void setRideId(String rideId) {
        this.rideId = rideId;
    }

    public String getTimeId() {
        return timeId;
    }

    public void setTimeId(String timeId) {
        this.timeId = timeId;
    }

    public String getRouteCount() {
        return routeCount;
    }

    public void setRouteCount(String routeCount) {
        this.routeCount = routeCount;
    }

    public String getSequence() {
        return sequence;
    }

    public void setSequence(String sequence) {
        this.sequence = sequence;
    }

    public String getCurLoc() {
        return curLoc;
    }

    public void setCurLoc(String curLoc) {
        this.curLoc = curLoc;
    }

    public String getNextCurLoc() {
        return nextCurLoc;
    }

    public void setNextCurLoc(String nextCurLoc) {
        this.nextCurLoc = nextCurLoc;
    }

    public String getCarType() {
        return carType;
    }

    public void setCarType(String carType) {
        this.carType = carType;
    }

    public String getCarName() {
        return carName;
    }

    public void setCarName(String carName) {
        this.carName = carName;
    }

    public String getArrTime() {
        return arrTime;
    }

    public void setArrTime(String arrTime) {
        this.arrTime = arrTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getSbVol() {
        return sbVol;
    }

    public void setSbVol(String sbVol) {
        this.sbVol = sbVol;
    }

    public String getUnloadVol() {
        return unloadVol;
    }

    public void setUnloadVol(String unloadVol) {
        this.unloadVol = unloadVol;
    }

    public String getCarGoods() {
        return carGoods;
    }

    public void setCarGoods(String carGoods) {
        this.carGoods = carGoods;
    }

    public String getCalcDis() {
        return calcDis;
    }

    public void setCalcDis(String calcDis) {
        this.calcDis = calcDis;
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
