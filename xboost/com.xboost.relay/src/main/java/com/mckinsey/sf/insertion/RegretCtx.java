package com.mckinsey.sf.insertion;
/**   
*    
* Author：Alivia Chen   
* Email : alivia_chen@mckinsey.com
* Date  ：May 9, 2017
* @version        
*/
public class RegretCtx{
	
	private InsertionCtx ctx;
	private double regret;
	
	public RegretCtx(){
		this.ctx = new InsertionCtx();
		this.regret =0;
	}
	
	public RegretCtx(InsertionCtx ctx, double regret) {
		super();
		this.ctx = ctx;
		this.regret = regret;
	}

	public InsertionCtx getCtx() {
		return ctx;
	}
	public void setCtx(InsertionCtx ctx) {
		this.ctx = ctx;
	}
	public double getRegret() {
		return regret;
	}
	public void setRegret(double regret) {
		this.regret = regret;
	}
	
	

}
