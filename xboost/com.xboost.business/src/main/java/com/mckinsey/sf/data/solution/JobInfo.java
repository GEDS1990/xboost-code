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
public class JobInfo implements Serializable{
	
	@JsonProperty("job_id")
	private String jobId;
	
	@JsonProperty("from")
	private String from;
	
	@JsonProperty("to")
	private String to;
	
	@JsonProperty("duration")
	private double duration;

	private String scenariosId;

	private String userId;

	private String createTime;
	
	public JobInfo(){
		super();
	}

	public String getJobId() {
		return jobId;
	}

	public void setJobId(String jobId) {
		this.jobId = jobId;
	}

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public String getTo() {
		return to;
	}

	public void setTo(String to) {
		this.to = to;
	}

	public double getDuration() {
		return duration;
	}

	public void setDuration(double duration) {
		this.duration = duration;
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
