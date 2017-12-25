package com.mckinsey.sf.data.car;

import com.mckinsey.sf.data.Car;
import com.mckinsey.sf.data.Job;

import java.util.HashMap;
import java.util.List;

/**
 * 
 * Author：Alivia Chen Email : alivia_chen@mckinsey.com Date ：Apr 28, 2017
 * 
 * @version
 */
public interface ICarManager {

	List<Car> newCars(HashMap<String, Job> unassigned);

	void freeCar(Car c);

}
