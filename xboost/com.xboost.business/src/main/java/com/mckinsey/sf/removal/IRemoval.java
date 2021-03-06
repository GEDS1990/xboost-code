package com.mckinsey.sf.removal;

import com.mckinsey.sf.data.constraint.ConstraintsResult;
import com.mckinsey.sf.data.constraint.IConstraint;
import com.mckinsey.sf.data.solution.ISolution;
import com.mckinsey.sf.data.solution.Solution;
import com.xboost.websocket.SystemWebSocketHandler;
import org.apache.log4j.Logger;
import org.springframework.web.socket.TextMessage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**   
*    
* Author：Alivia Chen   
* Email : alivia_chen@mckinsey.com
* Date  ：May 2, 2017
* @version        
*/
public interface IRemoval {

	ISolution remove(ISolution s);

	String name();

	double calcDistance(String from, String to);
	
	default void updateStates(Solution s, HashMap<String, Boolean> changed) {
		s.removeEmptyRoutes(changed);
		for (Map.Entry<String, Boolean> entry : changed.entrySet()) {
			String rid = entry.getKey();
			for (IConstraint c : s.getConstraints()) {
				c.updateStates(s, s.getRoutes().get(rid));
			}
		}

	}

	default void afterRemoval(RemovalCtx ctx, IConstraint[] constraints) {
		for (int i = 0; i < constraints.length; i++) {
			IConstraint c = constraints[i];
			c.afterRemoval(ctx, ctx.getContraintDeltas().get(i), ctx.getContraintCtxs().get(i));
		}

	}

	default int randomK(int max, int n, double p) {
		int k = (int) Math.ceil(p * n * 1.0);
		//TODO
		try{
			k = new Random().nextInt(k) + 1;
		}catch(IllegalArgumentException e){
			//add by geds
			Logger logger = Logger.getLogger(IRemoval.class);
			logger.info("IllegalArgumentException at randomK k=" + k + "and max=" + max);
			SystemWebSocketHandler systemWebSocketHandler = new SystemWebSocketHandler();
			TextMessage textMessage = new TextMessage("IllegalArgumentException at randomK k=" + k + "and max=" + max);
			systemWebSocketHandler.sendMessageToUser(textMessage);
			k = 1;
			Thread.interrupted();
		}

		if (k > max) {
			k = max;
		}
		return k;
	}
	
	
	
	default boolean beforeRemoval(RemovalCtx ctx, IConstraint[] constraints) {
		if(ctx.getJ().isFixed()){
			return false;
		}
		
		double total = 0;
		boolean can = true;
		
		ctx.setContraintCtxs(new ArrayList<Object>());
		ctx.setContraintDeltas(new ArrayList<Double>());
		
		for(IConstraint c : constraints){
			ConstraintsResult res = c.beforeRemoval(ctx);
			if(!res.isFlag()){
				can = false;
				break;
			}
			
			total += res.getDelta()*c.getWeight(ctx.getR().getC());
			ctx.getContraintCtxs().add(res.getObj());
			ctx.getContraintDeltas().add(res.getDelta());
		}
		ctx.setDelta(total);
		return can;
	}
}
