package com.mckinsey.sf.data;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

/**
 * 
 * Author：Alivia Chen 
 * Email : alivia_chen@mckinsey.com 
 * Date ：Apr 28, 2017
 * 
 * @version
 */
public class Activity implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@JsonProperty("id")
	private String id ;

	@JsonProperty("type")
	private String type;

	@JsonProperty("time_window")
	private TimeWindow tw;

	@JsonProperty("service_time")
	private double serviceTime;

	@JsonProperty("location")
	private String location;

	@JsonProperty("job_id")
	private String jobId;

	public Activity() {

	}

	public Activity(String id, String type, TimeWindow tw, double serviceTime, String location) {
		super();
		this.id = id;
		this.type = type;
		this.tw = tw;
		this.serviceTime = serviceTime;
		this.location = location;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public TimeWindow getTw() {
		return tw;
	}

	public void setTw(TimeWindow tw) {
		this.tw = tw;
	}

	public double getServiceTime() {
		return serviceTime;
	}

	public void setServiceTime(double serviceTime) {
		this.serviceTime = serviceTime;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getJobId() {
		return jobId;
	}

	public void setJobId(String jobId) {
		this.jobId = jobId;
	}

}
