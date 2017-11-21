package com.mckinsey.sf.data;

import org.apache.commons.lang3.builder.EqualsBuilder;

import com.mckinsey.sf.constants.IConstants;
import com.mckinsey.sf.removal.IRemoval;
import com.mckinsey.sf.removal.RemovalCtx;

/**   
*    
* Author：Alivia Chen   
* Email : alivia_chen@mckinsey.com
* Date  ：May 3, 2017
* @version        
*/
public class JobSimilarity implements IConstants {
	private IRemoval rev;
	private double ws, wt, wd, wc, maxD, maxT;
	private double[] maxC;

	public IRemoval getRev() {
		return rev;
	}

	public void setRev(IRemoval rev) {
		this.rev = rev;
	}

	public double getWs() {
		return ws;
	}

	public void setWs(double ws) {
		this.ws = ws;
	}

	public double getWt() {
		return wt;
	}

	public void setWt(double wt) {
		this.wt = wt;
	}

	public double getWd() {
		return wd;
	}

	public void setWd(double wd) {
		this.wd = wd;
	}

	public double getWc() {
		return wc;
	}

	public void setWc(double wc) {
		this.wc = wc;
	}

	public double getMaxD() {
		return maxD;
	}

	public void setMaxD(double maxD) {
		this.maxD = maxD;
	}

	public double getMaxT() {
		return maxT;
	}

	public void setMaxT(double maxT) {
		this.maxT = maxT;
	}

	public double[] getMaxC() {
		return maxC;
	}

	public void setMaxC(double[] maxC) {
		this.maxC = maxC;
	}

	public JobSimilarity(IRemoval rev, double ws, double wt, double wd, double wc, double maxD, double maxT,
			double[] maxC) {
		super();
		this.rev = rev;
		this.ws = ws;
		this.wt = wt;
		this.wd = wd;
		this.wc = wc;
		this.maxD = maxD;
		this.maxT = maxT;
		this.maxC = maxC;
	}

	public double similarity(RemovalCtx o, RemovalCtx c) {
		// check skills set
		double s = 0;

		Job oj = o.getJ();
		Job cj = c.getJ();

		if (EqualsBuilder.reflectionEquals(oj.getSkills(), cj.getSkills(), false)) {
			s = 1;
		}

		double d = 0;

		// location similarity

		if (oj.getPickup() == null || cj.getPickup() == null) {
			d += 1;
		} else {
			d += rev.calcDistance(oj.getPickup().getLocation(), cj.getPickup().getLocation());
		}

		if (oj.getDelivery() == null || cj.getDelivery() == null) {
			d += 1;
		} else {
			d += rev.calcDistance(oj.getDelivery().getLocation(), cj.getDelivery().getLocation());
		}

		d /= this.maxD;

		// dimensions similarity
		double cap = 0;
		for (int i = 0; i < oj.getDimensions().length; i++) {
			cap += Math.abs(oj.getDimensions()[i] - cj.getDimensions()[i]) / maxC[i];
		}

		cap /= maxC.length;

		// Service time similarity
		double t = 0;

		RouteState orstat = o.getS().getConstraintState(DEFAULT_CONSTRAINTS).getRouteState(o.getR());
		RouteState crstat = c.getS().getConstraintState(DEFAULT_CONSTRAINTS).getRouteState(c.getR());

		if (oj.getPickup() == null || cj.getPickup() == null) {
			t += 1;
		} else {
			t += Math.abs(orstat.getActStates().get(oj.getDelivery().getId()).getArrTime()
					- crstat.getActStates().get(cj.getDelivery().getId()).getArrTime());
		}

		if (oj.getDelivery() == null || cj.getDelivery() == null) {
			t += 1;
		} else {
			t += Math.abs(orstat.getActStates().get(oj.getPickup().getId()).getArrTime()
					- crstat.getActStates().get(cj.getPickup().getId()).getArrTime());
		}

		t /= maxT;

		return s * ws + d * wd + cap * wc + t * wt;

	}

}
