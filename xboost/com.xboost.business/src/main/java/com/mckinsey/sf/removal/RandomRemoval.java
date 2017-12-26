package com.mckinsey.sf.removal;

import com.mckinsey.sf.constants.IConstants;
import com.mckinsey.sf.data.Activity;
import com.mckinsey.sf.data.Job;
import com.mckinsey.sf.data.Route;
import com.mckinsey.sf.data.RoutingTransportCosts;
import com.mckinsey.sf.data.constraint.IConstraint;
import com.mckinsey.sf.data.solution.ISolution;
import com.mckinsey.sf.data.solution.Solution;
import com.mckinsey.sf.printer.OutputPrinter;

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
public class RandomRemoval implements IRemoval, IConstants{

	private double k;
	private int maxK;
	
	private RoutingTransportCosts transportCost;

	public RandomRemoval(double k, int maxK) {
		super();
		this.k = k;
		this.maxK = maxK;
	}

	public double getK() {
		return k;
	}

	public void setK(double k) {
		this.k = k;
	}

	public int getMaxK() {
		return maxK;
	}

	public void setMaxK(int maxK) {
		this.maxK = maxK;
	}
	
	public RoutingTransportCosts getTransportCost() {
		return transportCost;
	}

	public void setTransportCost(RoutingTransportCosts transportCost) {
		this.transportCost = transportCost;
	}

	@Override
	public ISolution remove(ISolution solution) {
		
		Solution s = (Solution) solution;
		IConstraint[] constraints = s.getConstraints();
		int n = s.numAssignedJobs();
		
		if( n == 0 ){
			OutputPrinter.printError("num assigned jobs == 0");
		}
		
		double k = randomK(this.maxK,n,this.k);
		List<RandomCtx> ctxs = new ArrayList<RandomCtx>();
		
		for(Route route : s.getRoutes().values()){
			
			for(Job job : route.getJobs().values()){
				
				ctxs.add(new RandomCtx(job,route));
				
			}
		}
		
		HashMap<Integer,Boolean> removed = new HashMap<Integer,Boolean>();
		HashMap<String,Boolean> changed = new HashMap<String,Boolean>();
		if(ctxs.size()>0){

			for(int i = 0; i < k;){
				int index = new Random().nextInt(ctxs.size());
				if(removed.containsKey(index)){
					continue;
				}else{
					removed.put(index, true);
					i++;
				}

				RandomCtx ctx = ctxs.get(index);
				Route route = ctx.getRoute();
				Job job = ctx.getJob();
				List<Activity> iters = route.getJtoA().get(job.getId());
				RemovalCtx rctx = new RemovalCtx(s,route,job,iters);

				boolean can = beforeRemoval(rctx, constraints);

				if(can){
					s.applyRemoval(rctx);
					afterRemoval(rctx, constraints);
					changed.put(rctx.getR().getId(), true);
				}

			}
		}

		updateStates(s,changed);
		return s;
	}

	@Override
	public String name() {
		return "RandomRemoval";
	}

	@Override
	public double calcDistance(String from, String to) {
		return transportCost.calcDistance(from, to);
	}

}
