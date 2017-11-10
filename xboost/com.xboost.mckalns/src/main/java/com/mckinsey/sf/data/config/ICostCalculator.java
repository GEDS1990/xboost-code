package com.mckinsey.sf.data.config;

import com.mckinsey.sf.data.solution.Solution;

/**   
*    
* Author：Alivia Chen   
* Email : alivia_chen@mckinsey.com
* Date  ：Apr 27, 2017
* @version        
*/
public interface ICostCalculator {
	double getSolutionCost(Solution s);

}
