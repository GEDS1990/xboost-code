package com.mckinsey.sf.data.constraint;
/**   
*    
* Author：Alivia Chen   
* Email : alivia_chen@mckinsey.com
* Date  ：May 2, 2017
* @version        
*/

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mckinsey.sf.data.Route;
import com.mckinsey.sf.data.RouteState;

import java.io.Serializable;
import java.util.HashMap;

public class ConstraintState implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -581312410139532001L;

	@JsonProperty("route_states")
	private HashMap<String, RouteState> routeStates = new HashMap<String, RouteState>();

	@JsonProperty("total_cost")
	private double totalCost;

	public HashMap<String, RouteState> getRouteStates() {
		return routeStates;
	}

	public void setRouteStates(HashMap<String, RouteState> routeStates) {
		this.routeStates = routeStates;
	}

	public double getTotalCost() {
		return totalCost;
	}

	public void setTotalCost(double totalCost) {
		this.totalCost = totalCost;
	}

	public RouteState getRouteState(Route r) {
		if (routeStates.containsKey(r.getId())) {
			return routeStates.get(r.getId());
		} else {
			RouteState initRouteState = new RouteState((r.getC().getDimensions().split(",")[0]).length());
			routeStates.put(r.getId(), initRouteState);
			return initRouteState;
		}
	}

}
