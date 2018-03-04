package com.mckinsey.sf.insertion;

import com.mckinsey.sf.data.Cache;
import com.mckinsey.sf.data.Job;
import com.mckinsey.sf.data.Route;
import com.mckinsey.sf.data.config.ITransportCosts;
import com.mckinsey.sf.data.constraint.IConstraint;
import com.mckinsey.sf.data.solution.ISolution;
import com.mckinsey.sf.data.solution.Solution;
import com.mckinsey.sf.printer.OutputPrinter;
import com.xboost.websocket.SystemWebSocketHandler;
import org.springframework.web.socket.TextMessage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/**   
*    
* Author：Alivia Chen   
* Email : alivia_chen@mckinsey.com
* Date  ：May 2, 2017
* @version        
*/
public class RegretInsertion implements IInsertion {

	//just the config for all regret insertion
	private int[] k;
	
	private int ks;
	private ITransportCosts transportCost;
	private static SystemWebSocketHandler systemWebSocketHandler = new SystemWebSocketHandler();

	public RegretInsertion(int[] k) {
		super();
		this.k = k;
	}

	public RegretInsertion(int k, ITransportCosts transportCost) {
		super();
		this.ks = k;
		this.transportCost = transportCost;
	}

	public int getKs() {
		return ks;
	}

	public void setKs(int ks) {
		this.ks = ks;
	}

	public ITransportCosts getTransportCost() {
		return transportCost;
	}

	public void setTransportCost(ITransportCosts transportCost) {
		this.transportCost = transportCost;
	}

	public int[] getK() {
		return k;
	}

	public void setK(int[] k) {
		this.k = k;
	}

	@Override
	public ISolution insert(ISolution solution) {
		Solution s = (Solution)solution;
		IConstraint[] constraints = s.getConstraints();
		
		Cache cache = new Cache();
		int n = s.getUnassigned().size();

//		systemWebSocketHandler.sendMessageToUser( new TextMessage("please waiting for minutes..."));
		for(int i=0 ;i<n;i++){
			InsertionCtx max = findMaxRegret(s,s.getUnassigned(),s.getRoutes(),cache);
//			systemWebSocketHandler.sendMessageToUser( new TextMessage("▉7"));
			//not enough routes
			if(max.isNullCtx()){
				HashMap<String, Route> routes = s.newRoutes();
				max = findMaxRegret(s,s.getUnassigned(),routes,cache);
			}
			
			if(max.isNullCtx()){
				OutputPrinter.printError("@Regret: can't insert any more jobs: " + s.getUnassigned());
				break;
			}
			
			s.applyInsetion(max);
			afterInsertion(max,constraints);
			cache.del(max.getR());
		}
		
		return solution;
	}

	private InsertionCtx findMaxRegret(Solution s, HashMap<String, Job> jobs, HashMap<String, Route> routes,
			Cache cache) {
		RegretCtx max = new RegretCtx();
		
		for(Job job : jobs.values()){
			List<InsertionCtx> ctxs = new ArrayList<InsertionCtx>();
			int jd3 = (routes.values().size()/10)>0?(routes.values().size()/10):1;
			int jd4 = 0;
			for(Route route : routes.values()){

//				jd4++;
//				if(jd4%jd3 == 0){
//					systemWebSocketHandler.sendMessageToUser( new TextMessage("▉8"));
//				}
				InsertionCtx ctx = null;
				if (cache.get(route, job) == null) {
					ctx = tryInsert(s, route, job);
					cache.put(route, job, ctx);
				} else {
					ctx = (InsertionCtx) cache.get(route, job);
				}
				
				ctxs.add(ctx);
			}
			
			RegretCtx regret = calcMaxRegret(ctxs);
			
			if(regret.getRegret() > max.getRegret()){
				max = regret;
			}
			
		}
		
		return max.getCtx();
	}

	private RegretCtx calcMaxRegret(List<InsertionCtx> ctxs) {
		int k = this.ks;
		if( k > ctxs.size()){
			k = ctxs.size();
		}
		
		if(k == 0){
			return new RegretCtx();
		}else if( k == 1 ){
			return new RegretCtx(ctxs.get(0),Double.MAX_VALUE-ctxs.get(0).getDelta());
		}else if( k > 1 ){
			Collections.sort(ctxs);
//			ctxs = ctxs[0:k]
			double regret = ctxs.get(k-1).getDelta() - ctxs.get(0).getDelta();
			return new RegretCtx(ctxs.get(0),regret);
		}else{
			OutputPrinter.printError("bad k! "+ k);
		}
		return null;
	}

	@Override
	public String name() {
		return "RegretInsertion-"+ks;
	}

}
