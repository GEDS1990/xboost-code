package com.mckinsey.sf.data;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mckinsey.sf.data.solution.SolutionJson;

/**
 * 
 * Author：Alivia Chen 
 * Email : alivia_chen@mckinsey.com 
 * Date ：Apr 27, 2017
 * 
 * @version
 */
public class Input {

	@JsonProperty("init_solution")
	private SolutionJson initSolution;

	@JsonProperty("car_templates")
	private Car[] carTemplates;

	public SolutionJson getInitSolution() {
		return initSolution;
	}

	public void setInitSolution(SolutionJson initSolution) {
		this.initSolution = initSolution;
	}

	public Car[] getCarTemplates() {
		return carTemplates;
	}

	public void setCarTemplates(Car[] carTemplates) {
		this.carTemplates = carTemplates;
	}

}
