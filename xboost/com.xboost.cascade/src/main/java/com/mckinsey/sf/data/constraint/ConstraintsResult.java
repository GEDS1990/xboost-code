package com.mckinsey.sf.data.constraint;

import com.mckinsey.sf.data.ActState;

/**   
*    
* Author：Alivia Chen   
* Email : alivia_chen@mckinsey.com
* Date  ：May 2, 2017
* @version        
*/
public class ConstraintsResult {
	
	private boolean flag;
	private double delta;
	private Object obj;
	private ActState prevStat;
	private String prevLoc;
	
	
	public ActState getPrevStat() {
		return prevStat;
	}

	public void setPrevStat(ActState prevStat) {
		this.prevStat = prevStat;
	}

	public String getPrevLoc() {
		return prevLoc;
	}

	public void setPrevLoc(String prevLoc) {
		this.prevLoc = prevLoc;
	}

	public ConstraintsResult(){
		
	}
	
	public ConstraintsResult(boolean flag, double delta, Object obj,ActState r,String prevLoc) {
		super();
		this.flag = flag;
		this.delta = delta;
		this.obj = obj;
		this.prevLoc = prevLoc;
		this.prevStat = r;
	}
	public boolean isFlag() {
		return flag;
	}
	public void setFlag(boolean flag) {
		this.flag = flag;
	}
	public double getDelta() {
		return delta;
	}
	public void setDelta(double delta) {
		this.delta = delta;
	}
	public Object getObj() {
		return obj;
	}
	public void setObj(Object obj) {
		this.obj = obj;
	}
	
}
