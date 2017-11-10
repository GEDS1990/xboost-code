package com.mckinsey.sf.data;

import java.io.Serializable;

import com.mckinsey.sf.data.config.ICostCalculator;
import com.mckinsey.sf.data.constraint.IConstraint;
import com.mckinsey.sf.data.solution.Solution;

/**
 * 
 * Author：Alivia Chen 
 * Email : alivia_chen@mckinsey.com 
 * Date ：May 2, 2017
 * 
 * @version
 */
public class DefaultCostCalculator implements ICostCalculator,Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3433947054428690543L;
	private double min;
	private double max;
	private int n;
	private double delta;
	private double cur;
	private int i;

	public DefaultCostCalculator() {

	}

	public DefaultCostCalculator(double min, double max, int n) {
		super();
		this.min = min;
		this.max = max;
		this.n = n;
		this.delta = (max - min) * 1.0 / n;
		this.cur = max;
		this.i = 0;
	}

	public double getMin() {
		return min;
	}

	public void setMin(double min) {
		this.min = min;
	}

	public double getMax() {
		return max;
	}

	public void setMax(double max) {
		this.max = max;
	}

	public int getN() {
		return n;
	}

	public void setN(int n) {
		this.n = n;
	}

	public double getDelta() {
		return delta;
	}

	public void setDelta(double delta) {
		this.delta = delta;
	}

	public double getCur() {
		return cur;
	}

	public void setCur(double cur) {
		this.cur = cur;
	}

	public int getI() {
		return i;
	}

	public void setI(int i) {
		this.i = i;
	}
	
	public double getSolutionCost(Solution s) {
		double cost = 0;
		for(IConstraint c :s.getConstraints()){
			cost += c.getCost(s);
		}
		
		int numOfComplete = s.numAssignedJobs();
		if(numOfComplete == 0){
			return Double.MAX_VALUE;
		}
		
		double jobAverageCost = cost*1.0/numOfComplete;
		
		for(Job j : s.getUnassigned().values()){
			cost += j.getPriority()*1.0*jobAverageCost*cur;
		}
		
		i += 1;
		if(i < n){
			cur -= delta;
		}

		return cost;
	}

}
