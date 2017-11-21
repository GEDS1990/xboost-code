package com.xboost.pojo;

import com.mckinsey.sf.data.Car;
import java.io.Serializable;
import java.util.List;

public class Configuration implements Serializable{
    private int id;
    private int optimizeIterations;
    private int scenariosId;
//    private String distanceFile;
//    private String demandFile;
    private double loadTime;
    private Car[] carTemplates;
//    private List<Car> carTemplates;
    private int distMode;
    private int carCostMode;
    private List<SiteDist> siteDistList;
    private List<DemandInfo> demandList;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getScenariosId() {
        return scenariosId;
    }

    public void setScenariosId(int scenariosId) {
        this.scenariosId = scenariosId;
    }

    public int getOptimizeIterations() {
        return optimizeIterations;
    }

    public void setOptimizeIterations(int optimizeIterations) {
        this.optimizeIterations = optimizeIterations;
    }

    public double getLoadTime() {
        return loadTime;
    }

    public void setLoadTime(double loadTime) {
        this.loadTime = loadTime;
    }

    public Car[] getCarTemplates() {
        return carTemplates;
    }

    public void setCarTemplates(Car[] carTemplates) {
        this.carTemplates = carTemplates;
    }

    public int getDistMode() {
        return distMode;
    }

    public void setDistMode(int distMode) {
        this.distMode = distMode;
    }

    public int getCarCostMode() {
        return carCostMode;
    }

    public void setCarCostMode(int carCostMode) {
        this.carCostMode = carCostMode;
    }

    public List<SiteDist> getSiteDistList() {
        return siteDistList;
    }

    public void setSiteDistList(List<SiteDist> siteDistList) {
        this.siteDistList = siteDistList;
    }

    public List<DemandInfo> getDemandList() {
        return demandList;
    }

    public void setDemandList(List<DemandInfo> demandList) {
        this.demandList = demandList;
    }
}
