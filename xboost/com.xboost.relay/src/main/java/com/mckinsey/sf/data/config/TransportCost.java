package com.mckinsey.sf.data.config;

/**   
*    
* Author：Alivia Chen   
* Email : alivia_chen@mckinsey.com
* Date  ：Apr 27, 2017
* @version        
*/
public class TransportCost {
	private String distance;
	private String nearest;
	private double fixed_stop_time;

	public TransportCost(String distance, String nearest, double fixed_stop_time) {
		super();
		this.distance = distance;
		this.nearest = nearest;
		this.fixed_stop_time = fixed_stop_time;
	}

	public String getDistance() {
		return distance;
	}

	public void setDistance(String distance) {
		this.distance = distance;
	}

	public String getNearest() {
		return nearest;
	}

	public void setNearest(String nearest) {
		this.nearest = nearest;
	}

	public double getFixed_stop_time() {
		return fixed_stop_time;
	}

	public void setFixed_stop_time(double fixed_stop_time) {
		this.fixed_stop_time = fixed_stop_time;
	}

}
