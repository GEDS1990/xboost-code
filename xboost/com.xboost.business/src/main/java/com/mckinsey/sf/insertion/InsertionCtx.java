package com.mckinsey.sf.insertion;

import com.mckinsey.sf.constants.IConstants;
import com.mckinsey.sf.data.Activity;
import com.mckinsey.sf.data.IContext;
import com.mckinsey.sf.data.Job;
import com.mckinsey.sf.data.Route;
import com.mckinsey.sf.data.solution.Solution;
import com.mckinsey.sf.printer.OutputPrinter;

import java.util.List;

/**
 * 
 * Author：Alivia Chen 
 * Email : alivia_chen@mckinsey.com 
 * Date ：Apr 28, 2017
 * 
 * @version
 */
public class InsertionCtx implements IConstants,IContext,Comparable<InsertionCtx> {
	private Solution s;
	private Route r;
	private Job j;
	private List<Activity> actIters;
	private List<Object> contraintCtxs;
	private List<Double> contraintDeltas;
	private double delta;
	private boolean nullCtx;

	public boolean isNullCtx() {
		return nullCtx;
	}

	public void setNullCtx(boolean nullCtx) {
		this.nullCtx = nullCtx;
	}

	public InsertionCtx() {
		this.nullCtx = true;
		delta = Double.MAX_VALUE;
	}

	public InsertionCtx(Solution s, Route r, Job j, List<Activity> actIters,boolean nullCtx) {
		super();
		this.s = s;
		this.r = r;
		this.j = j;
		this.actIters = actIters;
		this.nullCtx = nullCtx;
		this.delta = Double.MAX_VALUE;
	}

	public Solution getS() {
		return s;
	}

	public void setS(Solution s) {
		this.s = s;
	}

	public Route getR() {
		return r;
	}

	public void setR(Route r) {
		this.r = r;
	}

	public Job getJ() {
		return j;
	}

	public void setJ(Job j) {
		this.j = j;
	}

	public List<Activity> getActIters() {
		return actIters;
	}

	public void setActIters(List<Activity> actIters) {
		this.actIters = actIters;
	}

	public List<Object> getContraintCtxs() {
		return contraintCtxs;
	}

	public void setContraintCtxs(List<Object> contraintCtxs) {
		this.contraintCtxs = contraintCtxs;
	}

	public List<Double> getContraintDeltas() {
		return contraintDeltas;
	}

	public void setContraintDeltas(List<Double> contraintDeltas) {
		this.contraintDeltas = contraintDeltas;
	}

	public double getDelta() {
		return delta;
	}

	public void setDelta(double delta) {
		this.delta = delta;
	}

	public Job getJob(String jobId) {
		if(r.getJobs().containsKey(jobId)){
			return r.getJobs().get(jobId);
		}
		
		if(s.getUnassigned().containsKey(jobId)){
			return s.getUnassigned().get(jobId);
		}
		
		OutputPrinter.printError("job not found:"+jobId);
		return null;
	}

	@Override
	public int compareTo(InsertionCtx ctx) {
		if(this.getDelta() < ctx.getDelta()){
			return -1;
		}else if(this.getDelta()> ctx.getDelta()){
			return 1;
		}
		return 0;
	}


}
