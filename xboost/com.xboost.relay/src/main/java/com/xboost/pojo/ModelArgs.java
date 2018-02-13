package com.xboost.pojo;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/11/5 0005.
 */
public class ModelArgs implements Serializable {
    private Integer id;
    private String scenariosId;            //场景id
    private String modelType;             //模型
    private Integer durationCollect;      //收件串连耗时(分钟)
    private Integer durationLoad;           //上车装货时间(分钟)
    private Integer durationSiteStartSort;            //始发地分拣耗时(分钟)
    private Integer durationRelay;                 //支线高速接力耗时(分钟)
    private Integer durationTransfer;             //高速网中转耗时(分钟)
    private Integer durationSiteEndSort;      //目的地分拣耗时(分钟)
    private Integer durationUnload;               //下车卸货时间(分钟)
    private Integer numOperater;            //集配站单人单小时处理量（件）
    private Integer sitePeopleWork;        //网点集散点人效（p）
    private Integer distriPeopleWork;        //集配站集散点人效（p）
    private String durationPeakStart;                //网点高峰时间段（开始）
    private String durationPeakEnd;          //网点高峰时间段（结束)
    private String durationSiteTransferStart;        //中转场高峰时间段（开始）
    private String durationSiteTransferEnd;          //中转场高峰时间段（结束）
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

    public Integer getDurationCollect() {
        return durationCollect;
    }

    public void setDurationCollect(Integer durationCollect) {
        this.durationCollect = durationCollect;
    }

    public Integer getDurationLoad() {
        return durationLoad;
    }

    public void setDurationLoad(Integer durationLoad) {
        this.durationLoad = durationLoad;
    }

    public Integer getDurationSiteStartSort() {
        return durationSiteStartSort;
    }

    public void setDurationSiteStartSort(Integer durationSiteStartSort) {
        this.durationSiteStartSort = durationSiteStartSort;
    }

    public Integer getDurationRelay() {
        return durationRelay;
    }

    public void setDurationRelay(Integer durationRelay) {
        this.durationRelay = durationRelay;
    }

    public Integer getDurationTransfer() {
        return durationTransfer;
    }

    public void setDurationTransfer(Integer durationTransfer) {
        this.durationTransfer = durationTransfer;
    }

    public Integer getDurationSiteEndSort() {
        return durationSiteEndSort;
    }

    public void setDurationSiteEndSort(Integer durationSiteEndSort) {
        this.durationSiteEndSort = durationSiteEndSort;
    }

    public Integer getDurationUnload() {
        return durationUnload;
    }

    public void setDurationUnload(Integer durationUnload) {
        this.durationUnload = durationUnload;
    }

    public Integer getNumOperater() {
        return numOperater;
    }

    public void setNumOperater(Integer numOperater) {
        this.numOperater = numOperater;
    }

    public Integer getSitePeopleWork() {
        return sitePeopleWork;
    }

    public void setSitePeopleWork(Integer sitePeopleWork) {
        this.sitePeopleWork = sitePeopleWork;
    }

    public Integer getDistriPeopleWork() {
        return distriPeopleWork;
    }

    public void setDistriPeopleWork(Integer distriPeopleWork) {
        this.distriPeopleWork = distriPeopleWork;
    }

    public String getDurationPeakStart() {
        return durationPeakStart;
    }

    public void setDurationPeakStart(String durationPeakStart) {
        this.durationPeakStart = durationPeakStart;
    }

    public String getDurationPeakEnd() {
        return durationPeakEnd;
    }

    public void setDurationPeakEnd(String durationPeakEnd) {
        this.durationPeakEnd = durationPeakEnd;
    }

    public String getDurationSiteTransferStart() {
        return durationSiteTransferStart;
    }

    public void setDurationSiteTransferStart(String durationSiteTransferStart) {
        this.durationSiteTransferStart = durationSiteTransferStart;
    }

    public String getDurationSiteTransferEnd() {
        return durationSiteTransferEnd;
    }

    public void setDurationSiteTransferEnd(String durationSiteTransferEnd) {
        this.durationSiteTransferEnd = durationSiteTransferEnd;
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
