package com.mckinsey.sf.data;

/**
 * 
 * Author：Alivia Chen 
 * Email : alivia_chen@mckinsey.com 
 * Date ：May 2, 2017
 * 
 * @version
 */
public class NoiseMaker {
	private double noiseLevel;
	private double noiseProb;
	private double maxNoise;

	public NoiseMaker(double noiseLevel, double noiseProb, double maxNoise) {
		super();
		this.noiseLevel = noiseLevel;
		this.noiseProb = noiseProb;
		this.maxNoise = maxNoise;
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

}
