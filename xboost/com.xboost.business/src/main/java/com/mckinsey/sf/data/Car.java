package com.mckinsey.sf.data;

import java.io.Serializable;
import java.util.HashMap;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 
 * Author：Alivia Chen 
 * Email : alivia_chen@mckinsey.com 
 * Date ：Apr 27, 2017
 * 
 * @version
 */
public class Car implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -3292204274815042073L;
	@JsonProperty("id")
	private String id;
	@JsonProperty("type")
	private String type;
	private String scenariosId;
	@JsonProperty("dimensions")
//	private double[] dimensions;
	private String dimensions;
	@JsonProperty("skills")
	private HashMap<String, Boolean> skills;
	@JsonProperty("time_window")
	private TimeWindow tw;
	@JsonProperty("start_location")
	private String startLocation;
	@JsonProperty("end_location")
	private String endLocation;
	@JsonProperty("max_distance")
	private double maxDistance;
	@JsonProperty("max_running_time")
	private double maxRunningTime;
	@JsonProperty("cost_per_distance")
	private double costPerDistance;
	@JsonProperty("cost_per_time")
	private double costPerTime;
		@JsonProperty("fixed_cost")
	private double fixedCost;
	private int maxStop;
	@JsonProperty("velocity")
	private double velocity;
	@JsonProperty("fixed_round")
//	private double[] fixedRound;
	private double fixedRound;


	@JsonProperty("fixed_round_fee")
//	private double[] fixedRoundFee;
	private double fixedRoundFee;

	private  String carSource;

	private  String maxLoad;

	private  String durationUnloadFull;

	private String a;            //分段距离0-a
	private String costa;            //0-a费用
	private String b;            //分段距离b
	private String costb;            //b费用
	private String c;            //分段距离c
	private String costc;            //c费用
	private String d;            //分段距离d
	private String costd;            //d费用
	private String e;            //分段距离e
	private String coste;            //e费用
	private String f;            //分段距离f
	private String costf;            //f费用

	private String createTime;

	private String updateTime;

	public Car() {
		super();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getScenariosId() {
		return scenariosId;
	}

	public void setScenariosId(String scenariosId) {
		this.scenariosId = scenariosId;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getDimensions() {
		return dimensions;
	}

	public void setDimensions(String dimensions) {
		this.dimensions = dimensions;
	}

	public HashMap<String, Boolean> getSkills() {
		return skills;
	}

	public void setSkills(HashMap<String, Boolean> skills) {
		this.skills = skills;
	}

	public TimeWindow getTw() {
		return tw;
	}

	public void setTw(TimeWindow tw) {
		this.tw = tw;
	}

	public String getStartLocation() {
		return startLocation;
	}

	public void setStartLocation(String startLocation) {
		this.startLocation = startLocation;
	}

	public String getEndLocation() {
		return endLocation;
	}

	public void setEndLocation(String endLocation) {
		this.endLocation = endLocation;
	}

	public double getMaxDistance() {
		return maxDistance;
	}

	public void setMaxDistance(double maxDistance) {
		this.maxDistance = maxDistance;
	}

	public double getMaxRunningTime() {
		return maxRunningTime;
	}

	public void setMaxRunningTime(double maxRunningTime) {
		this.maxRunningTime = maxRunningTime;
	}

	public double getCostPerDistance() {
		return costPerDistance;
	}

	public void setCostPerDistance(double costPerDistance) {
		this.costPerDistance = costPerDistance;
	}

	public double getCostPerTime() {
		return costPerTime;
	}

	public void setCostPerTime(double costPerTime) {
		this.costPerTime = costPerTime;
	}

	public double getFixedCost() {
		return fixedCost;
	}

	public void setFixedCost(double fixedCost) {
		this.fixedCost = fixedCost;
	}

	public int getMaxStop() {
		return maxStop;
	}

	public void setMaxStop(int maxStop) {
		this.maxStop = maxStop;
	}

	public double getVelocity() {
		return velocity;
	}

	public void setVelocity(double velocity) {
		this.velocity = velocity;
	}

	public double getFixedRound() {
		return fixedRound;
	}

	public void setFixedRound(double fixedRound) {
		this.fixedRound = fixedRound;
	}

	public double getFixedRoundFee() {
		return fixedRoundFee;
	}

	public void setFixedRoundFee(double fixedRoundFee) {
		this.fixedRoundFee = fixedRoundFee;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}

	public String getCarSource() {
		return carSource;
	}

	public void setCarSource(String carSource) {
		this.carSource = carSource;
	}

	public String getMaxLoad() {
		return maxLoad;
	}

	public void setMaxLoad(String maxLoad) {
		this.maxLoad = maxLoad;
	}

	public String getA() {
		return a;
	}

	public void setA(String a) {
		this.a = a;
	}

	public String getCosta() {
		return costa;
	}

	public void setCosta(String costa) {
		this.costa = costa;
	}

	public String getB() {
		return b;
	}

	public void setB(String b) {
		this.b = b;
	}

	public String getCostb() {
		return costb;
	}

	public void setCostb(String costb) {
		this.costb = costb;
	}

	public String getC() {
		return c;
	}

	public void setC(String c) {
		this.c = c;
	}

	public String getCostc() {
		return costc;
	}

	public void setCostc(String costc) {
		this.costc = costc;
	}

	public String getD() {
		return d;
	}

	public void setD(String d) {
		this.d = d;
	}

	public String getCostd() {
		return costd;
	}

	public void setCostd(String costd) {
		this.costd = costd;
	}

	public String getE() {
		return e;
	}

	public void setE(String e) {
		this.e = e;
	}

	public String getCoste() {
		return coste;
	}

	public void setCoste(String coste) {
		this.coste = coste;
	}

	public String getF() {
		return f;
	}

	public void setF(String f) {
		this.f = f;
	}

	public String getCostf() {
		return costf;
	}

	public void setCostf(String costf) {
		this.costf = costf;
	}

	public String getDurationUnloadFull() {
		return durationUnloadFull;
	}

	public void setDurationUnloadFull(String durationUnloadFull) {
		this.durationUnloadFull = durationUnloadFull;
	}
}
