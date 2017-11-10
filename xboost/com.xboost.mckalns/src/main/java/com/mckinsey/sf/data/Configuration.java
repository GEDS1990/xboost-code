package com.mckinsey.sf.data;
/**   
*    
* Author：Alivia Chen   
* Email : alivia_chen@mckinsey.com
* Date  ：Sep 19, 2017
* @version        
*/
import com.fasterxml.jackson.annotation.JsonProperty;

public class Configuration {

	@JsonProperty("optimize_iterations")
	private int optimizeIterations;

	@JsonProperty("distance_file")
	private String distanceFile;
	
	@JsonProperty("demand_file")
	private String demandFile;
	
	@JsonProperty("load_time")
	private double loadTime;
	
	@JsonProperty("car_templates")
	private Car[] carTemplates;
	
	@JsonProperty("dist_mode")
	private int distMode;
	
	@JsonProperty("car_cost_mode")
	private int carCostMode;

	public int getOptimizeIterations() {
		return optimizeIterations;
	}

	public void setOptimizeIterations(int optimizeIterations) {
		this.optimizeIterations = optimizeIterations;
	}

	public String getDistanceFile() {
		return distanceFile;
	}

	public void setDistanceFile(String distanceFile) {
		this.distanceFile = distanceFile;
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

	public String getDemandFile() {
		return demandFile;
	}

	public void setDemandFile(String demandFile) {
		this.demandFile = demandFile;
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
	
}

