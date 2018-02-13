package com.mckinsey.sf.data;

import java.util.List;

/**
 * 
 * Author：Alivia Chen 
 * Email : alivia_chen@mckinsey.com 
 * Date ：May 2, 2017
 * 
 * @version
 */
public class Group {
	private GroupCtx ctx;
	private List<Job> jobs;
	private double totalVolume;

	public Group(GroupCtx ctx, List<Job> jobs) {
		super();
		this.ctx = ctx;
		this.jobs = jobs;
	}

	public GroupCtx getCtx() {
		return ctx;
	}

	public void setCtx(GroupCtx ctx) {
		this.ctx = ctx;
	}

	public List<Job> getJobs() {
		return jobs;
	}

	public void setJobs(List<Job> jobs) {
		this.jobs = jobs;
	}

	public void addJob(Job job) {
		this.jobs.add(job);
	}

	public double getTotalVolume() {
		return totalVolume;
	}

	public void setTotalVolume(double totalVolume) {
		this.totalVolume = totalVolume;
	}
	
}
