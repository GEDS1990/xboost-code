package com.mckinsey.sf.data.solution;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mckinsey.sf.data.Job;

/**
 * 
 * Author：Alivia Chen 
 * Email : alivia_chen@mckinsey.com 
 * Date ：May 2, 2017
 * 
 * @version
 */
public class SolutionJson {

	@JsonProperty("routes")
	private RouteJson[] routes;

	@JsonProperty("unassigned_jobs")
	private Job[] unassignedJobs;

	public SolutionJson() {
		super();
	}

	public SolutionJson(RouteJson[] routes, Job[] unassignedJobs) {
		super();
		this.routes = routes;
		this.unassignedJobs = unassignedJobs;
	}

	public RouteJson[] getRoutes() {
		return routes;
	}

	public void setRoutes(RouteJson[] routes) {
		this.routes = routes;
	}

	public Job[] getUnassignedJobs() {
		return unassignedJobs;
	}

	public void setUnassignedJobs(Job[] unassignedJobs) {
		this.unassignedJobs = unassignedJobs;
	}

}
