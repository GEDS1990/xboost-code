package com.mckinsey.sf.data.config;

import com.mckinsey.sf.data.Car;

/**   
*    
* Author：Alivia Chen   
* Email : alivia_chen@mckinsey.com
* Date  ：Apr 27, 2017
* @version        
*/
public interface ITransportCosts {
	double calcDistance(String from, String to);

	double calcTime(Car car, String from, String to);
}
