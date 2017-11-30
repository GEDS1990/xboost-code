package com.xboost.pojo;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/11/5 0005.
 */
public class Route implements Serializable {
    private Integer id;
    private String scenariosId;            //场景id
    private String routeCount;      //车辆编号
    private String carType;           //车型
    private String location;            //出车网点-收车网点
    private String sequence;            //停靠点顺序
    private String curLoc;            //当前网点
    private String type;            //操作
    private String sbLoc;            //装货目的地代码
    private String sbVol;            //装货票数
    private String arrTime;            //到达本网点时间
    private String endTime;            //离开本网点时间
    private String unloadLoc;            //卸货目的地代码
    private String unloadVol;            //卸货票数
    private String nextCurLoc;            //下一个停靠网点代码
    private String calcDis;            //到下一个停靠点运行里程
    private String carGoods;            //车上货物
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

    public String getRouteCount() {
        return routeCount;
    }

    public void setRouteCount(String routeCount) {
        this.routeCount = routeCount;
    }

    public String getCarType() {
        return carType;
    }

    public void setCarType(String carType) {
        this.carType = carType;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSbLoc() {
        return sbLoc;
    }

    public void setSbLoc(String sbLoc) {
        this.sbLoc = sbLoc;
    }

    public String getSbVol() {
        return sbVol;
    }

    public void setSbVol(String sbVol) {
        this.sbVol = sbVol;
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

    public String getUnloadLoc() {
        return unloadLoc;
    }

    public void setUnloadLoc(String unloadLoc) {
        this.unloadLoc = unloadLoc;
    }

    public String getUnloadVol() {
        return unloadVol;
    }

    public void setUnloadVol(String unloadVol) {
        this.unloadVol = unloadVol;
    }

    public String getNextCurLoc() {
        return nextCurLoc;
    }

    public void setNextCurLoc(String nextCurLoc) {
        this.nextCurLoc = nextCurLoc;
    }

    public String getCalcDis() {
        return calcDis;
    }

    public void setCalcDis(String calcDis) {
        this.calcDis = calcDis;
    }

    public String getCarGoods() {
        return carGoods;
    }

    public void setCarGoods(String carGoods) {
        this.carGoods = carGoods;
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
