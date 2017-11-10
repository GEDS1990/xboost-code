package com.mckinsey.sf.data.solution;

/**
 * 
 * Author：Alivia Chen 
 * Email : alivia_chen@mckinsey.com 
 * Date ：May 2, 2017
 * 
 * @version
 */
public interface ISolution {
	ISolution clone();

	double cost();

	void calcCost();
}
