package com.mckinsey.sf.data;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.HashMap;

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
	private String name;   //车辆名称；
	private String busyIdle; //车辆是否空闲；
	private String carType;

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

	private String a1;            //分段距离0-a
	private String costa1;            //0-a费用
	private String b1;            //分段距离b
	private String costb1;            //b费用
	private String c1;            //分段距离c
	private String costc1;            //c费用
	private String d1;            //分段距离d
	private String costd1;            //d费用
	private String e1;            //分段距离e
	private String coste1;            //e费用
	private String f1;            //分段距离f
	private String costf1;            //f费用

	private String a2;            //分段距离0-a
	private String costa2;            //0-a费用
	private String b2;            //分段距离b
	private String costb2;            //b费用
	private String c2;            //分段距离c
	private String costc2;            //c费用
	private String d2;            //分段距离d
	private String costd2;            //d费用
	private String e2;            //分段距离e
	private String coste2;            //e费用
	private String f2;            //分段距离f
	private String costf2;            //f费用
	///min
	private String costa3;            //0-a费用
	private String costb3;            //b费用
	private String costc3;            //c费用
	private String costd3;            //d费用
	private String coste3;            //e费用
	private String costf3;            //f费用

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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getBusyIdle() {
		return busyIdle;
	}

	public void setBusyIdle(String busyIdle) {
		this.busyIdle = busyIdle;
	}

	public String getCarType() {
		return carType;
	}

	public void setCarType(String carType) {
		this.carType = carType;
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
//		this.costPerDistance = Integer.parseInt(this.costa2+this.costb2+this.costc2+this.costd2+this.coste2+this.costf2)/6;
		this.costPerDistance = costPerDistance;
	}

	public double getCostPerTime() {
		return costPerTime;
	}

	public void setCostPerTime(double costPerTime) {
//		this.costPerTime = Integer.parseInt(this.costa3+this.costb3+this.costc3+this.costd3+this.coste3+this.costf3)/6;
		this.costPerTime = costPerTime;
	}

	public double getFixedCost() {
		return fixedCost;
	}

	public void setFixedCost(double fixedCost) {
//		this.fixedCost = Double.parseDouble(costa1);
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
//		this.fixedRound = Double.parseDouble(this.a1+this.a2+this.b1+this.b2+this.c1+this.c2+this.d1+this.d2+this.e1+this.e2+this.f1+this.f2);
		this.fixedRound = fixedRound;
	}

	public double getFixedRoundFee() {
		return fixedRoundFee;
	}

	public void setFixedRoundFee(double fixedRoundFee) {
//		this.fixedRoundFee =  Double.parseDouble(costa1+costa2+costb1+costb2+costc1+costc2+costd1+costd2+coste1+coste2+costf1+costf2);
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

	public String getA1() {
		return a1;
	}

	public void setA1(String a1) {
		this.a1 = a1;
	}

	public String getCosta1() {
		return costa1;
	}

	public void setCosta1(String costa1) {
		this.costa1 = costa1;
	}

	public String getB1() {
		return b1;
	}

	public void setB1(String b1) {
		this.b1 = b1;
	}

	public String getCostb1() {
		return costb1;
	}

	public void setCostb1(String costb1) {
		this.costb1 = costb1;
	}

	public String getC1() {
		return c1;
	}

	public void setC1(String c1) {
		this.c1 = c1;
	}

	public String getCostc1() {
		return costc1;
	}

	public void setCostc1(String costc1) {
		this.costc1 = costc1;
	}

	public String getD1() {
		return d1;
	}

	public void setD1(String d1) {
		this.d1 = d1;
	}

	public String getCostd1() {
		return costd1;
	}

	public void setCostd1(String costd1) {
		this.costd1 = costd1;
	}

	public String getE1() {
		return e1;
	}

	public void setE1(String e1) {
		this.e1 = e1;
	}

	public String getCoste1() {
		return coste1;
	}

	public void setCoste1(String coste1) {
		this.coste1 = coste1;
	}

	public String getF1() {
		return f1;
	}

	public void setF1(String f1) {
		this.f1 = f1;
	}

	public String getCostf1() {
		return costf1;
	}

	public void setCostf1(String costf1) {
		this.costf1 = costf1;
	}

	public String getA2() {
		return a2;
	}

	public void setA2(String a2) {
		this.a2 = a2;
	}

	public String getCosta2() {
		return costa2;
	}

	public void setCosta2(String costa2) {
		this.costa2 = costa2;
	}

	public String getB2() {
		return b2;
	}

	public void setB2(String b2) {
		this.b2 = b2;
	}

	public String getCostb2() {
		return costb2;
	}

	public void setCostb2(String costb2) {
		this.costb2 = costb2;
	}

	public String getC2() {
		return c2;
	}

	public void setC2(String c2) {
		this.c2 = c2;
	}

	public String getCostc2() {
		return costc2;
	}

	public void setCostc2(String costc2) {
		this.costc2 = costc2;
	}

	public String getD2() {
		return d2;
	}

	public void setD2(String d2) {
		this.d2 = d2;
	}

	public String getCostd2() {
		return costd2;
	}

	public void setCostd2(String costd2) {
		this.costd2 = costd2;
	}

	public String getE2() {
		return e2;
	}

	public void setE2(String e2) {
		this.e2 = e2;
	}

	public String getCoste2() {
		return coste2;
	}

	public void setCoste2(String coste2) {
		this.coste2 = coste2;
	}

	public String getF2() {
		return f2;
	}

	public void setF2(String f2) {
		this.f2 = f2;
	}

	public String getCostf2() {
		return costf2;
	}

	public void setCostf2(String costf2) {
		this.costf2 = costf2;
	}

	public String getCosta3() {
		return costa3;
	}

	public void setCosta3(String costa3) {
		this.costa3 = costa3;
	}

	public String getCostb3() {
		return costb3;
	}

	public void setCostb3(String costb3) {
		this.costb3 = costb3;
	}

	public String getCostc3() {
		return costc3;
	}

	public void setCostc3(String costc3) {
		this.costc3 = costc3;
	}

	public String getCostd3() {
		return costd3;
	}

	public void setCostd3(String costd3) {
		this.costd3 = costd3;
	}

	public String getCoste3() {
		return coste3;
	}

	public void setCoste3(String coste3) {
		this.coste3 = coste3;
	}

	public String getCostf3() {
		return costf3;
	}

	public void setCostf3(String costf3) {
		this.costf3 = costf3;
	}

	public String getDurationUnloadFull() {
		return durationUnloadFull;
	}

	public void setDurationUnloadFull(String durationUnloadFull) {
		this.durationUnloadFull = durationUnloadFull;
	}
}
