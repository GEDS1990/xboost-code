package com.mckinsey.sf.data.config;

import com.mckinsey.sf.constants.IConstants;

/**   
*    
* Author：Alivia Chen   
* Email : alivia_chen@mckinsey.com
* Date  ：Apr 27, 2017 
* @version        
*/

public class PalnsConfig implements IConstants {
	private int cores;
	private int numIters;
	private int maxTime;
	private double w; // control percentage of initial temprature
	private double decay;
	private double alpha;
	private Score[] scores; // REJECTED,ACCEPTED,BETTER_THAN_CURRENT,NEWBESTs
	private int segment;

	public PalnsConfig() {
		init();
	}

	public PalnsConfig(int cores, int numIters, int maxTime, double w, double decay, double alpha, Score[] scores,
			int segment) {
		super();
		this.cores = cores;
		this.numIters = numIters;
		this.maxTime = maxTime;
		this.w = w;
		this.decay = decay;
		this.alpha = alpha;
		this.scores = scores;
		this.segment = segment;
		init();
	}

	public void init() {
		if (cores <= 0) {
			cores = Runtime.getRuntime().availableProcessors();
		}
		System.out.println("Cores:" + cores);
	}

	public int getCores() {
		return cores;
	}

	public void setCores(int cores) {
		this.cores = cores;
	}

	public int getNumIters() {
		return numIters;
	}

	public void setNumIters(int numIters) {
		this.numIters = numIters;
	}

	public int getMaxTime() {
		return maxTime;
	}

	public void setMaxTime(int maxTime) {
		this.maxTime = maxTime;
	}

	public double getW() {
		return w;
	}

	public void setW(double w) {
		this.w = w;
	}

	public double getDecay() {
		return decay;
	}

	public void setDecay(double decay) {
		this.decay = decay;
	}

	public double getAlpha() {
		return alpha;
	}

	public void setAlpha(double alpha) {
		this.alpha = alpha;
	}

	public Score[] getScores() {
		return scores;
	}

	public void setScores(Score[] scores) {
		this.scores = scores;
	}

	public int getSegment() {
		return segment;
	}

	public void setSegment(int segment) {
		this.segment = segment;
	}

}
