package com.mckinsey.sf.data.solution;

import java.io.Serializable;
import java.util.HashMap;

import com.fasterxml.jackson.annotation.JsonProperty;

/**   
*    
* Author：Alivia Chen   
* Email : alivia_chen@mckinsey.com
* Date  ：May 8, 2017
* @version        
*/
public class StatInfo implements Serializable{
	
	@JsonProperty("cost")
	private double cost;
	
	@JsonProperty("no_jobs")
	private int noJobs;
	
	@JsonProperty("avg_dis")
	private double avgDis;
	
	@JsonProperty("avg_cost")
	private double avgCost;
	
	@JsonProperty("avg_jobs")
	private double avgJobs;
	
	@JsonProperty("avg_time")
	private double avgTime;
	
	@JsonProperty("avg_load")
	private double avgLoad;
	
	@JsonProperty("num_routes")
	private int numroutes;
	
	@JsonProperty("num_4wheels")
	private int num4wheels;
	
	@JsonProperty("num_2wheels")
	private int num2wheels;
	
	@JsonProperty("num_3wheels")
	private int num3wheels;
	
	@JsonProperty("dada")
	private int dada;

	private String scenariosId;

	private String createTime;

	public StatInfo(double cost, int noJobs, double avgDis, double avgCost, double avgJobs, double avgTime,
			double avgLoad, int numroutes, HashMap<String, Integer> carsMap) {
		super();
		this.cost = cost;
		this.noJobs = noJobs;
		this.avgDis = avgDis;
		this.avgCost = avgCost;
		this.avgJobs = avgJobs;
		this.avgTime = avgTime;
		this.avgLoad = avgLoad;
		this.numroutes = numroutes;
	}



	public double getCost() {
		return cost;
	}

	public void setCost(double cost) {
		this.cost = cost;
	}

	public int getNoJobs() {
		return noJobs;
	}

	public void setNoJobs(int noJobs) {
		this.noJobs = noJobs;
	}

	public double getAvgDis() {
		return avgDis;
	}

	public void setAvgDis(double avgDis) {
		this.avgDis = avgDis;
	}

	public double getAvgCost() {
		return avgCost;
	}

	public void setAvgCost(double avgCost) {
		this.avgCost = avgCost;
	}

	public double getAvgJobs() {
		return avgJobs;
	}

	public void setAvgJobs(double avgJobs) {
		this.avgJobs = avgJobs;
	}

	public double getAvgTime() {
		return avgTime;
	}

	public void setAvgTime(double avgTime) {
		this.avgTime = avgTime;
	}

	public double getAvgLoad() {
		return avgLoad;
	}

	public void setAvgLoad(double avgLoad) {
		this.avgLoad = avgLoad;
	}

	public int getNumroutes() {
		return numroutes;
	}

	public void setNumroutes(int numroutes) {
		this.numroutes = numroutes;
	}

	public int getNum4wheels() {
		return num4wheels;
	}

	public void setNum4wheels(int num4wheels) {
		this.num4wheels = num4wheels;
	}

	public int getNum2wheels() {
		return num2wheels;
	}

	public void setNum2wheels(int num2wheels) {
		this.num2wheels = num2wheels;
	}

	public int getNum3wheels() {
		return num3wheels;
	}

	public void setNum3wheels(int num3wheels) {
		this.num3wheels = num3wheels;
	}

	public String getScenariosId() {
		return scenariosId;
	}

	public void setScenariosId(String scenariosId) {
		this.scenariosId = scenariosId;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
}
