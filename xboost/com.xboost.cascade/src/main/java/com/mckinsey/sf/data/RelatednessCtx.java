package com.mckinsey.sf.data;

import com.mckinsey.sf.removal.RemovalCtx;

/**   
*    
* Author：Alivia Chen   
* Email : alivia_chen@mckinsey.com
* Date  ：May 3, 2017
* @version        
*/
public class RelatednessCtx implements Comparable<RelatednessCtx> {
	private RemovalCtx ctx;
	private double relatedness;
	private int i;

	public RemovalCtx getCtx() {
		return ctx;
	}

	public void setCtx(RemovalCtx ctx) {
		this.ctx = ctx;
	}

	public double getRelatedness() {
		return relatedness;
	}

	public void setRelatedness(double relatedness) {
		this.relatedness = relatedness;
	}

	public int getI() {
		return i;
	}

	public void setI(int i) {
		this.i = i;
	}

	public RelatednessCtx(RemovalCtx ctx, double relatedness, int i) {
		super();
		this.ctx = ctx;
		this.relatedness = relatedness;
		this.i = i;
	}

	@Override
	public int compareTo(RelatednessCtx ctx) {
		if(this.getRelatedness() < ctx.getRelatedness()){
			return -1;
		}else if(this.getRelatedness() > ctx.getRelatedness()){
			return 1;
		}
		return 0;
	}

}
