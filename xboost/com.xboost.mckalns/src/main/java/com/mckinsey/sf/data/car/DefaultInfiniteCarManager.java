package com.mckinsey.sf.data.car;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;

import com.mckinsey.sf.data.Car;
import com.mckinsey.sf.data.Job;
import com.mckinsey.sf.data.config.ITransportCosts;

/**
 * 
 * Author：Alivia Chen 
 * Email : alivia_chen@mckinsey.com 
 * Date ：May 2, 2017
 * 
 * @version
 */
public class DefaultInfiniteCarManager implements ICarManager,Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7749419670100943541L;
	private Car[] template;
	private ITransportCosts transportCost;

	public DefaultInfiniteCarManager(Car[] template, ITransportCosts transportCost) {
		super();
		this.template = template;
		this.transportCost = transportCost;
	}

	@Override
	public List<Car> newCars(HashMap<String, Job> jobs) {
		HashMap<Car, Boolean> selected = new HashMap<Car, Boolean>();
		List<Car> cars = new ArrayList<Car>(template.length);

		for (Map.Entry<String, Job> entry : jobs.entrySet()) {
			Job job = entry.getValue();
//			OutputPrinter.printLine("job:"+job.getId());
			List<Car> initCars = selectedCars(job);
			for (Car c : initCars) {
				if (selected.containsKey(c)) {
					continue;
				} else {
//					OutputPrinter.printLine("car: "+c.getType());
					selected.put(c, true);
//					Car initC = c;
					c.setId(UUID.randomUUID().toString());
					cars.add(c);
				}
			}

		}
		return cars;
	}

	private List<Car> selectedCars(Job job) {
		List<Car> cars = new ArrayList<Car>();
		for (Car c : template) {
			if (filterCars(c, job)) {
				cars.add(c);
			}
		}
		return cars;
	}

	public double calcDistance(String from, String to) {
		return transportCost.calcDistance(from, to);
	}

	private boolean filterCars(Car car, Job job) {
		if (!StringUtils.isEmpty(car.getStartLocation()) && job.getPickup() != null
				&& !car.getStartLocation().equals(job.getPickup().getLocation())) {
			return false;
		}

		for (String skill : job.getSkills()) {
			if (!car.getSkills().containsKey(skill)) {
				return false;
			}
		}

		if (job.getPickup() != null && job.getDelivery() != null) {
			double dist = calcDistance(job.getPickup().getLocation(), job.getDelivery().getLocation());
			if (car.getMaxDistance() > 0 && dist > car.getMaxDistance()) {
				return false;
			}
		}

		for (int i = 0; i < job.getDimensions().length; i++) {
			if (job.getDimensions()[i] > car.getDimensions()[i]) {
				return false;
			}
		}

		return true;
	}

	@Override
	public void freeCar(Car c) {
		
	}

}
