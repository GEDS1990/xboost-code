package com.mckinsey.sf.data;
/**   
*    
* Author：Alivia Chen   
* Email : alivia_chen@mckinsey.com
* Date  ：May 3, 2017
* @version        
*/
public class Dimension {
	private double d;
	private double t;
	private double[] c;
	public double getD() {
		return d;
	}
	public void setD(double d) {
		this.d = d;
	}
	public double getT() {
		return t;
	}
	public void setT(double t) {
		this.t = t;
	}
	public double[] getC() {
		return c;
	}
	public void setC(double[] c) {
		this.c = c;
	}
	public Dimension(double d, double t, double[] c) {
		super();
		this.d = d;
		this.t = t;
		this.c = c;
	}

}
