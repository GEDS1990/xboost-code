package com.mckinsey.sf.removal;

import com.mckinsey.sf.data.Route;

/**   
*    
* Author：Alivia Chen   
* Email : alivia_chen@mckinsey.com
* Date  ：May 10, 2017
* @version        
*/
public class RRCtx implements Comparable<RRCtx>{
	private Route r;
	private double avg;
	
	
	public Route getR() {
		return r;
	}
	public void setR(Route r) {
		this.r = r;
	}
	public double getAvg() {
		return avg;
	}
	public void setAvg(double avg) {
		this.avg = avg;
	}
	
	public int compareTo(RRCtx ctx) {
		if(this.getAvg() > ctx.getAvg()){
			return -1;
		}else if(this.getAvg() < ctx.getAvg()){
			return 1;
		}
		return 0;
	}
	
	

}
