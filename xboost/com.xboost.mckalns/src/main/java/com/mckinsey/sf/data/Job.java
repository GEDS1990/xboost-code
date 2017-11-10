package com.mckinsey.sf.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 
 * Author：Alivia Chen 
 * Email : alivia_chen@mckinsey.com 
 * Date ：Apr 28, 2017
 * 
 * @version
 */
public class Job implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -1070370243115091617L;

	@JsonProperty("id")
	private String id;

	@JsonProperty("priority")
	private int priority;

	@JsonProperty("skills")
	private String[] skills;

	@JsonProperty("dimensions")
	private double[] dimensions;

	@JsonProperty("pickup")
	private Activity pickup;

	@JsonProperty("delivery")
	private Activity delivery;

	@JsonProperty("fixed")
	private boolean fixed;

	public Job() {
		super();
	}

	public Job(String id, int priority, String[] skills, double[] dimensions, Activity pickup, Activity delivery,
			boolean fixed) {
		super();
		this.id = id;
		this.priority = priority;
		this.skills = skills;
		this.dimensions = dimensions;
		this.pickup = pickup;
		this.delivery = delivery;
		this.fixed = fixed;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}

	public String[] getSkills() {
		return skills;
	}

	public void setSkills(String[] skills) {
		this.skills = skills;
	}

	public double[] getDimensions() {
		return dimensions;
	}

	public void setDimensions(double[] dimensions) {
		this.dimensions = dimensions;
	}

	public Activity getPickup() {
		return pickup;
	}

	public void setPickup(Activity pickup) {
		this.pickup = pickup;
	}

	public Activity getDelivery() {
		return delivery;
	}

	public void setDelivery(Activity delivery) {
		this.delivery = delivery;
	}

	public boolean isFixed() {
		return fixed;
	}

	public void setFixed(boolean fixed) {
		this.fixed = fixed;
	}

	public int actsLen() {
		int ret = 0;
		if (pickup != null) {
			ret++;
		}
		if (delivery != null) {
			ret++;
		}
		return ret;
	}

//	@JsonIgnore
	public List<Activity> getActs() {
		List<Activity> ret = new ArrayList<Activity>();
		if(pickup != null){
			ret.add(pickup);
		}
		
		if(delivery != null){
			ret.add(delivery);
		}
		
		return ret;
	}

}
