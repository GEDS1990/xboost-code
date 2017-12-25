package com.mckinsey.sf.data.config;

import com.mckinsey.sf.insertion.RegretInsertion;
import com.mckinsey.sf.removal.RandomRemoval;
import com.mckinsey.sf.removal.RouteRemoval;
import com.mckinsey.sf.removal.ShawRemoval;
import com.mckinsey.sf.removal.WorstRemoval;

/**
 * 
 * Author：Alivia Chen 
 * Email : alivia_chen@mckinsey.com 
 * Date ：Apr 27, 2017
 * 
 * @version
 */
public class Config {

	private PalnsConfig pconf;
	private TransportCost transportCost;
	private ICostCalculator costCalculator;
	private TimeConstraint timeConstraint;
	private DistanceConstraint distanceConstraint;
	private RandomRemoval randomRemoval;
	private RouteRemoval routeRemoval;
	private WorstRemoval worstRemoval;
	private ShawRemoval shawRemoval;
	private RegretInsertion regretInsertion;
	private NoiseMaker noiseMaker;
	private JobPacker jobPacker;

	public Config(PalnsConfig pconf, TransportCost transportCost, ICostCalculator costCalculator,
			TimeConstraint timeConstraint, DistanceConstraint distanceConstraint, RandomRemoval randomRemoval,
			RouteRemoval routeRemoval, WorstRemoval worstRemoval, ShawRemoval shawRemoval,
			RegretInsertion regretInsertion, NoiseMaker noiseMaker, JobPacker jobPacker) {
		super();
		this.pconf = pconf;
		this.transportCost = transportCost;
		this.costCalculator = costCalculator;
		this.timeConstraint = timeConstraint;
		this.distanceConstraint = distanceConstraint;
		this.randomRemoval = randomRemoval;
		this.routeRemoval = routeRemoval;
		this.worstRemoval = worstRemoval;
		this.shawRemoval = shawRemoval;
		this.regretInsertion = regretInsertion;
		this.noiseMaker = noiseMaker;
		this.jobPacker = jobPacker;
	}

	public TimeConstraint getTimeConstraint() {
		return timeConstraint;
	}

	public void setTimeConstraint(TimeConstraint timeConstraint) {
		this.timeConstraint = timeConstraint;
	}

	public DistanceConstraint getDistanceConstraint() {
		return distanceConstraint;
	}

	public void setDistanceConstraint(DistanceConstraint distanceConstraint) {
		this.distanceConstraint = distanceConstraint;
	}

	public RandomRemoval getRandomRemoval() {
		return randomRemoval;
	}

	public void setRandomRemoval(RandomRemoval randomRemoval) {
		this.randomRemoval = randomRemoval;
	}

	public RouteRemoval getRouteRemoval() {
		return routeRemoval;
	}

	public void setRouteRemoval(RouteRemoval routeRemoval) {
		this.routeRemoval = routeRemoval;
	}

	public WorstRemoval getWorstRemoval() {
		return worstRemoval;
	}

	public void setWorstRemoval(WorstRemoval worstRemoval) {
		this.worstRemoval = worstRemoval;
	}

	public ShawRemoval getShawRemoval() {
		return shawRemoval;
	}

	public void setShawRemoval(ShawRemoval shawRemoval) {
		this.shawRemoval = shawRemoval;
	}

	public RegretInsertion getRegretInsertion() {
		return regretInsertion;
	}

	public void setRegretInsertion(RegretInsertion regretInsertion) {
		this.regretInsertion = regretInsertion;
	}

	public NoiseMaker getNoiseMaker() {
		return noiseMaker;
	}

	public void setNoiseMaker(NoiseMaker noiseMaker) {
		this.noiseMaker = noiseMaker;
	}

	public JobPacker getJobPacker() {
		return jobPacker;
	}

	public void setJobPacker(JobPacker jobPacker) {
		this.jobPacker = jobPacker;
	}

	public PalnsConfig getPconf() {
		return pconf;
	}

	public void setPconf(PalnsConfig pconf) {
		this.pconf = pconf;
	}

	public TransportCost getTransportCost() {
		return transportCost;
	}

	public void setTransportCost(TransportCost transportCost) {
		this.transportCost = transportCost;
	}

	public ICostCalculator getCostCalculator() {
		return costCalculator;
	}

	public void setCostCalculator(ICostCalculator costCalculator) {
		this.costCalculator = costCalculator;
	}

}
