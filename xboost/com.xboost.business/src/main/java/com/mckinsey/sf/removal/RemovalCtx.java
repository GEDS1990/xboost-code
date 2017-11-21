package com.mckinsey.sf.removal;

import java.util.List;

import com.mckinsey.sf.data.Activity;
import com.mckinsey.sf.data.IContext;
import com.mckinsey.sf.data.Job;
import com.mckinsey.sf.data.Route;
import com.mckinsey.sf.data.solution.Solution;

/**   
*    
* Author：Alivia Chen   
* Email : alivia_chen@mckinsey.com
* Date  ：May 2, 2017
* @version        
*/
public class RemovalCtx implements IContext,Comparable<RemovalCtx>{
	private Solution s;
	private Route r;
	private Job j;
	private List<Activity> actIters;
	private List<Object> contraintCtxs;
	private List<Double> contraintDeltas;
	private double delta;
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
	public RemovalCtx(Solution s, Route r, Job j, List<Activity> actIters) {
		super();
		this.s = s;
		this.r = r;
		this.j = j;
		this.actIters = actIters;
	}
	@Override
	public int compareTo(RemovalCtx ctx) {
		if(this.getDelta() < ctx.getDelta()){
			return -1;
		}else if(this.getDelta() > ctx.getDelta()){
			return 1;
		}
		return 0;
	}
	
}
