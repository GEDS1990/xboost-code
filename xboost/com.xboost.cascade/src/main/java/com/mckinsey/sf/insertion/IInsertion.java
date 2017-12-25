package com.mckinsey.sf.insertion;

import com.mckinsey.sf.data.ActState;
import com.mckinsey.sf.data.Activity;
import com.mckinsey.sf.data.Job;
import com.mckinsey.sf.data.Route;
import com.mckinsey.sf.data.constraint.ConstraintsResult;
import com.mckinsey.sf.data.constraint.IConstraint;
import com.mckinsey.sf.data.solution.ISolution;
import com.mckinsey.sf.data.solution.Solution;
import com.mckinsey.sf.printer.OutputPrinter;

import java.util.ArrayList;
import java.util.List;

/**   
*    
* Author：Alivia Chen   
* Email : alivia_chen@mckinsey.com
* Date  ：Apr 28, 2017
* @version        
*/
public interface IInsertion {
	ISolution insert(ISolution solution);

	String name();

	default InsertionCtx tryInsert(Solution s, Route r, Job job) {
		
		for (String skill : job.getSkills()) {
			if (!r.getC().getSkills().containsKey(skill)) {
				OutputPrinter.printError("Skills set not qualified!");
				return new InsertionCtx();
			}
		}

		IConstraint[] constraints = s.getConstraints();
//		InsertionCtx[] ctxs = new InsertionCtx[r.getActs().size() * r.getActs().size()];
//		OutputPrinter.printLine("job actlen:"+job.actsLen());
		switch (job.actsLen()) {
		case 2:
			List<InsertionCtx> ctxs = new ArrayList<InsertionCtx>();
			
			for(int i=0; i<r.getActs().size()-1; i++){
				Activity cur = r.getActs().get(i);
				List<Activity> actIters = new ArrayList<Activity>();
				actIters.add(cur);
				InsertionCtx ctx = new InsertionCtx(s, r, job,actIters , false);
				ConstraintsResult cr = beforeInsertion(ctx,constraints);
				
				if(cr.isFlag()){
//					OutputPrinter.printLine("after beforeInsertion:"+cr.getDelta());
					ctx.setDelta(cr.getDelta());
					ctxs.add(ctx);
				}
			}
			
			InsertionCtx initCtx = selectMin(ctxs);
			if(initCtx.isNullCtx()){
				return initCtx;
			}
			
			Activity a = initCtx.getActIters().get(0);
			ctxs = new ArrayList<InsertionCtx>();
			
			int index = r.getActs().indexOf(a);
			for(;index < r.getActs().size()-1; index++){
				Activity cur = r.getActs().get(index);
				
				if(cur == a){
					cur = null;
				}
				List<Activity> actIters = new ArrayList<Activity>();
				actIters.add(a);
				actIters.add(cur);
				InsertionCtx ctx = new InsertionCtx(s, r, job,actIters , false);
				ConstraintsResult cr = beforeInsertion(ctx,constraints);
				if(cr.isFlag()){
					ctx.setDelta(cr.getDelta());
					ctxs.add(ctx);
				}
			}
			return selectMin(ctxs);

		case 1:
			ctxs = new ArrayList<InsertionCtx>();
			
			for(Activity cur : r.getActs()){
				List<Activity> actIters = new ArrayList<Activity>();
				actIters.add(cur);
				InsertionCtx ctx = new InsertionCtx(s, r, job,actIters , false);
				ConstraintsResult cr = beforeInsertion(ctx,constraints);
				if(cr.isFlag()){
					ctx.setDelta(cr.getDelta());
					ctxs.add(ctx);
				}
			}
			return selectMin(ctxs);
		default:
			System.err.println("bad job, bad acts length: %s" + job.getId());
		}
		return null;
	}

	default InsertionCtx selectMin(List<InsertionCtx> ctxs){
		InsertionCtx min = new InsertionCtx();
		for(InsertionCtx ctx : ctxs){
			if(ctx.getDelta() < min.getDelta()){
				min = ctx;
			}
		}
		return min;
	}


	default ConstraintsResult beforeInsertion(InsertionCtx ctx, IConstraint[] constraints){
		double total = 0;
		boolean can = true;
		
		ctx.setContraintCtxs(new ArrayList<Object>());
		ctx.setContraintDeltas(new ArrayList<Double>());
		
		for(IConstraint c : constraints){
			ConstraintsResult cr = c.beforeInsertion(ctx);
			if(!cr.isFlag()){
				can = false;
				break;
			}
			
			ctx.getContraintCtxs().add(cr.getObj());
			ctx.getContraintDeltas().add(cr.getDelta());
			
			total += cr.getDelta()*c.getWeight(ctx.getR().getC());
		}
		
	    total += ctx.getS().getNoiser().makeNoise(ctx.getR().getC().getCostPerDistance());
	    ctx.setDelta(total);
		return new ConstraintsResult(can,total,null,new ActState(),"");
	}

	default void afterInsertion(InsertionCtx ctx, IConstraint[] constraints) {
		for (int i = 0; i < constraints.length; i++) {
			IConstraint c = constraints[i];
			c.afterInsertion(ctx, ctx.getContraintDeltas().get(i), ctx.getContraintCtxs().get(i));
		}
	}

	default InsertionCtx selectMin(InsertionCtx[] ctxs) {
		InsertionCtx min = new InsertionCtx();
		min.setDelta(Double.MAX_VALUE);
		for (InsertionCtx ctx : ctxs) {
			if( ctx != null ){
				if (ctx.getDelta() < min.getDelta()) {
					min = ctx;
				}
			}
		}
		return min;
	}

}
