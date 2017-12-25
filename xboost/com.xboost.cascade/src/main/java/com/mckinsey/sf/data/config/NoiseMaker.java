package com.mckinsey.sf.data.config;

import java.io.Serializable;
import java.util.Random;

/**   
*    
* Author：Alivia Chen   
* Email : alivia_chen@mckinsey.com
* Date  ：Apr 28, 2017
* @version        
*/
public class NoiseMaker implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -2911812292080319084L;
	private double noiseLevel;
	private double noiseProb;
	private double maxNoise;

	public NoiseMaker(double noiseLevel, double noiseProb) {
		super();
		this.noiseLevel = noiseLevel;
		this.noiseProb = noiseProb;
	}

	public double getNoiseLevel() {
		return noiseLevel;
	}

	public void setNoiseLevel(double noiseLevel) {
		this.noiseLevel = noiseLevel;
	}

	public double getNoiseProb() {
		return noiseProb;
	}

	public void setNoiseProb(double noiseProb) {
		this.noiseProb = noiseProb;
	}

	public double getMaxNoise() {
		return maxNoise;
	}

	public void setMaxNoise(double maxNoise) {
		this.maxNoise = maxNoise;
	}

	public double makeNoise(double costPerDist) {
		double rand  = new Random().nextDouble();
		if( rand < noiseProb){
			return noiseLevel * (rand - 0.5) *2 * maxNoise * costPerDist;
		}
		return 0;
	}

}
