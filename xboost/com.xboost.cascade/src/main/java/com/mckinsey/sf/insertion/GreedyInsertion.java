package com.mckinsey.sf.insertion;

import com.mckinsey.sf.data.Cache;
import com.mckinsey.sf.data.Job;
import com.mckinsey.sf.data.Route;
import com.mckinsey.sf.data.config.ITransportCosts;
import com.mckinsey.sf.data.constraint.IConstraint;
import com.mckinsey.sf.data.solution.ISolution;
import com.mckinsey.sf.data.solution.Solution;
import com.mckinsey.sf.printer.OutputPrinter;

import java.util.HashMap;
import java.util.Map;

/**   
*    
* Author：Alivia Chen   
* Email : alivia_chen@mckinsey.com
* Date  ：Apr 28, 2017
* @version        
*/
public class GreedyInsertion implements IInsertion {

	private boolean concurrent;
	private ITransportCosts transportCost;

	public GreedyInsertion() {
		concurrent = false;
	}

	public boolean isConcurrent() {
		return concurrent;
	}

	public void setConcurrent(boolean concurrent) {
		this.concurrent = concurrent;
	}

	public ITransportCosts getTransportCost() {
		return transportCost;
	}

	public void setTransportCost(ITransportCosts transportCost) {
		this.transportCost = transportCost;
	}

	@Override
	public ISolution insert(ISolution solution) {
		Solution s = (Solution)solution;
		IConstraint[] constraints = s.getConstraints();

		Cache cache = new Cache();
		int n = s.getUnassigned().size();
		
//		OutputPrinter.printLine("routes 1: "+s.getRoutes().size());
		for (int i = 0; i < n; i++) {
//			OutputPrinter.printLine("i: "+i);
			InsertionCtx min = findMin(s, s.getUnassigned(), s.getRoutes(), cache);
//			OutputPrinter.printLine("routes 2: "+s.getRoutes().size());
			// not enough routes
			if (min.isNullCtx()) {
				HashMap<String, Route> routes = s.newRoutes();
//				OutputPrinter.printLine("routes 3: "+s.getRoutes().size());
				min = findMin(s, s.getUnassigned(), routes, cache);
//				OutputPrinter.printLine("routes 4: "+s.getRoutes().size());
			}

			if (min.isNullCtx()) {
				OutputPrinter.printError("@Greedy: can't insert any more jobs: " + s.getUnassigned());
				break;
			}

			s.applyInsetion(min);
//			OutputPrinter.printLine("routes 5: "+s.getRoutes().size());
			afterInsertion(min, constraints);
//			OutputPrinter.printLine("routes 6: "+s.getRoutes().size());
			
//			for(Route r : s.getRoutes().values()){
//				OutputPrinter.printLine("inside insertion  r:"+s.getConstraintState("DEFAULT_CONSTRAINTS").getRouteState(r).getMaxArr());
//			}
//			
			cache.del(min.getR());

		}

		return solution;
	}

	private InsertionCtx findMin(Solution s, HashMap<String, Job> jobs, HashMap<String, Route> routes, Cache cache) {
		InsertionCtx min = new InsertionCtx();
		for (Map.Entry<String, Job> entryJ : jobs.entrySet()) {
			Job job = entryJ.getValue();

			for (Map.Entry<String, Route> entryR : routes.entrySet()) {
				Route route = entryR.getValue();

//				OutputPrinter.printLine("job : "+ job.getPickup().getLocation()+"-"+job.getDelivery().getLocation());
//				OutputPrinter.printLine("route : "+ route.getC().getType()+" jobs:"+route.getJobs().size());
				
				InsertionCtx ctx = null;
				InsertionCtx initCtx = (InsertionCtx) cache.get(route, job);
				if ( initCtx == null) {
					ctx = tryInsert(s, route, job);
//					OutputPrinter.printLine("After try insert, delta is : "+ ctx.getDelta());
					cache.put(route, job, ctx);
				} else {
					ctx = initCtx;
//					OutputPrinter.printLine("ctx exist, delta is : "+ ctx.getDelta());
				}

				if (ctx.getDelta() < min.getDelta()) {
					min = ctx;
					min.setNullCtx(false);
				}
//				OutputPrinter.printLine("min delta is : "+ min.getDelta());
			}
		}
		return min;
	}

	@Override
	public String name() {
		return "GreedyInsertion";
	}


}
