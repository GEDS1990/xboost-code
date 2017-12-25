package com.mckinsey.sf.removal;

import com.mckinsey.sf.data.Job;
import com.mckinsey.sf.data.Route;

/**   
*    
* Author：Alivia Chen   
* Email : alivia_chen@mckinsey.com
* Date  ：May 9, 2017
* @version        
*/
public class RandomCtx {
	
	private Job job;
	private Route route;
	
	public RandomCtx(Job job, Route route) {
		super();
		this.job = job;
		this.route = route;
	}
	public Job getJob() {
		return job;
	}
	public void setJob(Job job) {
		this.job = job;
	}
	public Route getRoute() {
		return route;
	}
	public void setRoute(Route route) {
		this.route = route;
	}
	

}
