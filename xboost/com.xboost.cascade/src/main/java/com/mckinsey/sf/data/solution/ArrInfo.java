package com.mckinsey.sf.data.solution;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

/**   
*    
* Author：Alivia Chen   
* Email : alivia_chen@mckinsey.com
* Date  ：May 8, 2017
* @version        
*/
public class ArrInfo implements Serializable{
	
	@JsonProperty("route_id")
	private String routeId;
	
	@JsonProperty("arr_time")
	private double arrTime;
	
	@JsonProperty("end_time")
	private double endTime;
	
	@JsonProperty("location")
	private String location;

	private String scenariosId;

	private String userId;

	private String createTime;
	
	public ArrInfo(){
		super();
	}

	public String getRouteId() {
		return routeId;
	}

	public void setRouteId(String routeId) {
		this.routeId = routeId;
	}

	public double getArrTime() {
		return arrTime;
	}

	public void setArrTime(double arrTime) {
		this.arrTime = arrTime;
	}

	public double getEndTime() {
		return endTime;
	}

	public void setEndTime(double endTime) {
		this.endTime = endTime;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getScenariosId() {
		return scenariosId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
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
