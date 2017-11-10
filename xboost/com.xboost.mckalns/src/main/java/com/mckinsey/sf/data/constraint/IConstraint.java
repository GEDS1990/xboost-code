package com.mckinsey.sf.data.constraint;

import com.mckinsey.sf.data.Car;
import com.mckinsey.sf.data.Route;
import com.mckinsey.sf.data.solution.Solution;
import com.mckinsey.sf.insertion.InsertionCtx;
import com.mckinsey.sf.removal.RemovalCtx;

/**   
*    
* Author：Alivia Chen   
* Email : alivia_chen@mckinsey.com
* Date  ：Apr 28, 2017
* @version        
*/
public interface IConstraint {
	IConstraint clone();

	void clearStates(Solution s, Route r);

	void updateStates(Solution s, Route r);

	String getName();

	double getWeight(Car car);

	double getCost(Solution s);

	double getCostByRoute(Solution s, Route r);

	ConstraintsResult beforeRemoval(RemovalCtx ctx);

	void afterRemoval(RemovalCtx ctx, double delta, Object consCtx);

	ConstraintsResult beforeInsertion(InsertionCtx ctx);

	void afterInsertion(InsertionCtx ctx, double delta, Object consCtx);

}
