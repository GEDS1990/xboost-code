package com.mckinsey.sf.data.config;

/**   
*    
* Author：Alivia Chen   
* Email : alivia_chen@mckinsey.com
* Date  ：May 2, 2017
* @version        
*/
public class JobPacker {
	private double interval;

	public JobPacker(double interval) {
		super();
		this.interval = interval;
	}

	public double getInterval() {
		return interval;
	}

	public void setInterval(double interval) {
		this.interval = interval;
	}

}
