package com.mckinsey.sf.removal;

import com.mckinsey.sf.data.Activity;
import com.mckinsey.sf.data.Job;
import com.mckinsey.sf.data.Route;
import com.mckinsey.sf.data.RoutingTransportCosts;
import com.mckinsey.sf.data.constraint.IConstraint;
import com.mckinsey.sf.data.solution.ISolution;
import com.mckinsey.sf.data.solution.Solution;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

/**   
*    
* Author：Alivia Chen   
* Email : alivia_chen@mckinsey.com
* Date  ：May 2, 2017
* @version        
*/
public class RouteRemoval implements IRemoval{

	private double k;
	
	private RoutingTransportCosts transportCost;

	public RoutingTransportCosts getTransportCost() {
		return transportCost;
	}

	public void setTransportCost(RoutingTransportCosts transportCost) {
		this.transportCost = transportCost;
	}

	public RouteRemoval(double k) {
		super();
		this.k = k;
	}

	public double getK() {
		return k;
	}

	public void setK(double k) {
		this.k = k;
	}

	@Override
	public ISolution remove(ISolution solution) {
		
		Solution s = (Solution) solution;
		IConstraint[] constraints = s.getConstraints();
		List<Route> routes = new ArrayList<Route>();
		
		for(Route r : s.getRoutes().values()){
			routes.add(r);
		}
		
		for(int i = 0; i < routes.size(); i++){
			int index = 0;
			
			if(routes.size() > 1){
				index = new Random().nextInt(routes.size()-1);
			}
			
			Route r = routes.get(index);
			List<RemovalCtx> ctxs = new ArrayList<RemovalCtx>();
			boolean canRm = true;
			
			for(Job job : r.getJobs().values()){
				List<Activity> iters = r.getJtoA().get(job.getId());
				RemovalCtx rctx = new RemovalCtx(s,r,job,iters);
				boolean can = beforeRemoval(rctx,constraints);
				
				if(!can){
					canRm = false;
					break;
				}
				
				ctxs.add(rctx);
			}
			
			if(!canRm){
				continue;
			}
			
			for(RemovalCtx rctx : ctxs){
				s.applyRemoval(rctx);
				afterRemoval(rctx,constraints);
			}
			
			
			HashMap<String, Boolean> changed = new HashMap<String, Boolean>();
			changed.put(r.getId(), true);
			s.removeEmptyRoutes(changed );
			return s;

		}

		return s;
	}

	@Override
	public String name() {
		return "RouteRemoval";
	}

	@Override
	public double calcDistance(String from, String to) {
		 return this.transportCost.calcDistance(from, to);
	}

}
