package com.mckinsey.sf.removal;

import com.mckinsey.sf.data.*;
import com.mckinsey.sf.data.constraint.IConstraint;
import com.mckinsey.sf.data.solution.ISolution;
import com.mckinsey.sf.data.solution.Solution;
import com.xboost.websocket.SystemWebSocketHandler;
import org.apache.log4j.Logger;
import org.springframework.web.socket.TextMessage;

import java.util.*;

/**   
*    
* Author：Alivia Chen   
* Email : alivia_chen@mckinsey.com
* Date  ：May 2, 2017
* @version        
*/
public class WorstRemoval implements IRemoval {

	private double k;
	private double p;
	private int maxK;
	
	private RoutingTransportCosts transportCost;

	public RoutingTransportCosts getTransportCost() {
		return transportCost;
	}

	public void setTransportCost(RoutingTransportCosts transportCost) {
		this.transportCost = transportCost;
	}

	public WorstRemoval(double k, double p, int maxK) {
		super();
		this.k = k;
		this.p = p;
		this.maxK = maxK;
	}

	public double getK() {
		return k;
	}

	public void setK(double k) {
		this.k = k;
	}

	public double getP() {
		return p;
	}

	public void setP(double p) {
		this.p = p;
	}

	public int getMaxK() {
		return maxK;
	}

	public void setMaxK(int maxK) {
		this.maxK = maxK;
	}

	@Override
	public ISolution remove(ISolution solution) {
		Solution s = (Solution) solution;
		int n = s.numAssignedJobs();
		IConstraint[] constraints = s.getConstraints();
		
		int k = randomK(this.maxK,n,this.k);
		Cache cache = new Cache();
		
		for(int i = 0; i < k ; i++){
			List<RemovalCtx> ctxs = findWorst(s,n,cache);
			double y = new Random().nextDouble();
			int index = (int)Math.pow(y, this.p) * ctxs.size();
			
			try{
				RemovalCtx ctx = ctxs.get(index);
				if(ctx.getDelta() >= 0){
					continue;
				}
				s.applyRemoval(ctx);
				afterRemoval(ctx,constraints);
				HashMap<String,Boolean> map = new HashMap<String,Boolean>();
				map.put(ctx.getR().getId(), true);
				updateStates(s,map);
				cache.del(ctx.getR());
			}catch (IndexOutOfBoundsException e){
				//add by geds
				Logger logger = Logger.getLogger(WorstRemoval.class);
				logger.info("IndexOutOfBoundsException at WorstRemoval...");
				SystemWebSocketHandler systemWebSocketHandler = new SystemWebSocketHandler();
				TextMessage textMessage = new TextMessage("fail due to IndexOutOfBoundsException at WorstRemoval...");
				systemWebSocketHandler.sendMessageToUser(textMessage);
				e.printStackTrace();
				break;
			}


		}
		
		return s;
	}

	private List<RemovalCtx> findWorst(Solution s, int n, Cache cache) {
		IConstraint[] constraints = s.getConstraints();
		List<RemovalCtx> ctxs = new ArrayList<RemovalCtx>();

		for(Route r : s.getRoutes().values()){
			for(Map.Entry<String, Job> entry : r.getJobs().entrySet()){
				String id = entry.getKey();
				Job job = entry.getValue();
				List<Activity> iters = r.getJtoA().get(id);
				
				RemovalCtx ctx = null;
				RemovalCtx initCtx = (RemovalCtx) cache.get(r, job);
				
				if(initCtx == null ){
					ctx = new RemovalCtx(s,r,job,iters);
					boolean can = beforeRemoval(ctx,constraints);
					
					if(can){
						ctx.setDelta(ctx.getDelta() + s.getNoiser().makeNoise(ctx.getR().getC().getCostPerDistance()));
					}else{
						ctx.setDelta(Double.MAX_VALUE);
					}
					
					cache.put(r, job, ctx);
				}else{
					ctx = initCtx;
				}
				ctxs.add(ctx);
				
			}
		}
		Collections.sort(ctxs);
		return ctxs;
	}

	@Override
	public String name() {
		return "WorstRemoval";
	}

	@Override
	public double calcDistance(String from, String to) {
		return this.transportCost.calcDistance(from, to);
	}

}
