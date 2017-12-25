package com.mckinsey.sf.data.constraint;

import com.mckinsey.sf.constants.IConstants;
import com.mckinsey.sf.data.*;
import com.mckinsey.sf.data.config.ITransportCosts;
import com.mckinsey.sf.data.solution.Solution;
import com.mckinsey.sf.insertion.InsertionCtx;
import com.mckinsey.sf.printer.OutputPrinter;
import com.mckinsey.sf.removal.RemovalCtx;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**   
*    
* Author：Alivia Chen   
* Email : alivia_chen@mckinsey.com
* Date  ：May 2, 2017
* @version        
*/
public class DefaultConstraints implements IConstraint,IConstants,Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3285402786639678627L;
	private double wDist;
	private double wTime;
	private double wCap;
	private ITransportCosts costMatrix;

	public DefaultConstraints(double wDist, double wTime, double wCap, ITransportCosts costMatrix) {
		super();
		this.wDist = wDist;
		this.wTime = wTime;
		this.wCap = wCap;
		this.costMatrix = costMatrix;
	}

	public double getwDist() {
		return wDist;
	}

	public void setwDist(double wDist) {
		this.wDist = wDist;
	}

	public double getwTime() {
		return wTime;
	}

	public void setwTime(double wTime) {
		this.wTime = wTime;
	}

	public double getwCap() {
		return wCap;
	}

	public void setwCap(double wCap) {
		this.wCap = wCap;
	}

	public ITransportCosts getCostMatrix() {
		return costMatrix;
	}

	public void setCostMatrix(ITransportCosts costMatrix) {
		this.costMatrix = costMatrix;
	}

	@Override
	public IConstraint clone() {
		return this;
	}

	@Override
	public void clearStates(Solution s, Route r) {
		ConstraintState cstat = s.getConstraintState(DEFAULT_CONSTRAINTS);
		RouteState rstat = cstat.getRouteState(r);
		cstat.setTotalCost(cstat.getTotalCost()-rstat.getTotalCost());
		cstat.getRouteStates().remove(r.getId());
	}

	@Override
	public void updateStates(Solution s, Route r) {
		ConstraintState cstat = s.getConstraintState(DEFAULT_CONSTRAINTS);
		RouteState rstat = cstat.getRouteState(r);
		
		int lenDim = r.getC().getDimensions().split(",").length;
		RouteState newStates = new RouteState(lenDim);
		
		String prevLoc = "";
		ActState prevStat = new ActState(lenDim);
		double[] maxDim = new double[lenDim];
		
		Activity start = r.getActs().get(0);

//		double totalVolume = 0;
		for (int ind =0; ind<r.getActs().size(); ind++) {
			Activity act = r.getActs().get(ind);
			int indicator = Route.getIndicator(act, start, r.getActs().get(r.getActs().size()-1));
			Job j = r.getJobs().get(act.getJobId());
			
			double arr = 0;
			double totalTime = 0;
			switch(indicator){
			case POS_START:
				newStates.calcStops(act.getLocation(), "");
				prevStat = new ActState(lenDim);
				prevStat.setArrTime(0);
				prevStat.setEndTime(r.getC().getTw().getStart());
				newStates.getActStates().put(act.getId(), prevStat);
				prevLoc = act.getLocation();
				break;
			case POS_MIDDLE:
				newStates.calcStops(act.getLocation(), prevLoc);
				newStates.setTotalDist(newStates.getTotalDist() + costMatrix.calcDistance(prevLoc, act.getLocation()));
				ActState actStat = new ActState(lenDim);
				actStat.calcCapacity(prevStat, act, j, maxDim);
				arr = costMatrix.calcTime(r.getC(), prevLoc, act.getLocation());
				actStat.calcArrTime(prevStat,arr);
				totalTime += arr;
				actStat.calcEndTime(act.getServiceTime(), act.getTw().getStart());
				newStates.getActStates().put(act.getId(), actStat);
				prevStat = actStat;
				prevLoc = act.getLocation();
				break;
			case POS_END:
				newStates.calcStops(act.getLocation(), "");
				newStates.setTotalDist(newStates.getTotalDist() + costMatrix.calcDistance(prevLoc, act.getLocation()));
				ActState last = new ActState(lenDim);
				arr = costMatrix.calcTime(r.getC(), prevLoc, act.getLocation());
				totalTime += arr;
				last.calcArrTime(prevStat, arr);
				last.calcEndTime(act.getServiceTime(), act.getTw().getStart());
				newStates.getActStates().put(act.getId(), last);
				newStates.setTotalTime(totalTime);
				newStates.setMaxDim(maxDim);
				newStates.setMaxArr(last.getArrTime());
				//TODO
				double total = calcTotalCost(newStates.getTotalDist(),newStates.getTotalTime(),r);
				newStates.setTotalCost(total);
//				newStates.setTotalCost(newStates.getTotalDist()*wDist*r.getC().getCostPerDistance() +
//						newStates.getTotalTime()*wTime*r.getC().getCostPerTime());
				if(r.getJobs().size() > 0){
					newStates.setTotalCost(newStates.getTotalCost()+r.getC().getFixedCost());
				}
			}
			
		}


		cstat.setTotalCost(cstat.getTotalCost() - rstat.getTotalCost() + newStates.getTotalCost());
		cstat.getRouteStates().put(r.getId(), newStates);

	}

	@Override
	public String getName() {
		return DEFAULT_CONSTRAINTS;
	}

	@Override
	public double getWeight(Car car) {
		return 1;
	}

	@Override
	public double getCost(Solution s) {
		return s.getConstraintState(DEFAULT_CONSTRAINTS).getTotalCost();
	}

	@Override
	public double getCostByRoute(Solution s, Route r) {
		return 0;
	}

	@Override
	public void afterInsertion(InsertionCtx ctx, double delta, Object extra) {
		RouteState newStates = (RouteState)extra;
		ConstraintState cstat = ctx.getS().getConstraintState(DEFAULT_CONSTRAINTS);
		RouteState oldStates = cstat.getRouteState(ctx.getR());
		cstat.setTotalCost(cstat.getTotalCost()-oldStates.getTotalCost());
		oldStates.setTotalDist(newStates.getTotalDist());
		oldStates.setTotalStops(newStates.getTotalStops());
		oldStates.setTotalTime(newStates.getTotalTime());
		oldStates.setMaxArr(newStates.getMaxArr());
		oldStates.setMaxDim(newStates.getMaxDim());
//		oldStates.setTotalVolume(newStates.getTotalVolume());
		oldStates.setTotalCost(newStates.getTotalCost());
		cstat.setTotalCost(cstat.getTotalCost()+oldStates.getTotalCost());
		
		for (Map.Entry<String, ActState> entry : newStates.getActStates().entrySet()) {  
			oldStates.getActStates().put(entry.getKey(), entry.getValue());
		}
		
		cstat.getRouteStates().put(ctx.getR().getId(), oldStates);

	}

	@Override
	public ConstraintsResult beforeRemoval(RemovalCtx ctx) {
		ConstraintState cstat = ctx.getS().getConstraintState(DEFAULT_CONSTRAINTS);
		RouteState rstat = cstat.getRouteState(ctx.getR());
		double deltaDist = 0;
		double deltaTime = 0;
		
		List<Activity> newActs = ctx.getR().getActs();
		
		int l = ctx.getActIters().size();
		switch(l){
		case 2:
			Activity a = ctx.getActIters().get(0);
			Activity b = ctx.getActIters().get(1);
			
			int indexA = ctx.getR().getActs().indexOf(a);
			int indexB = ctx.getR().getActs().indexOf(b);
			if( indexA+1 == indexB){
				Activity prev = ctx.getR().getActs().get(indexA-1);
				Activity next = ctx.getR().getActs().get(indexB+1);
				
				double prevToNextDist = costMatrix.calcDistance(prev.getLocation(), next.getLocation());
				double prevToActADist = costMatrix.calcDistance(prev.getLocation(), a.getLocation());
				double actAToActBDist = costMatrix.calcDistance(a.getLocation(), b.getLocation());
				double actBToNextDist = costMatrix.calcDistance(b.getLocation(), next.getLocation());
				deltaDist = prevToNextDist - (prevToActADist + actAToActBDist + actBToNextDist);
				
				ActState prevActStat = rstat.getActStates().get(prev.getId());
				ActState nextActStat = rstat.getActStates().get(next.getId());

				double prevToNextTime = Math.max(costMatrix.calcTime(ctx.getR().getC(), prev.getLocation(), next.getLocation()), next.getTw().getStart()) + next.getServiceTime();
				deltaTime = prevToNextTime - (nextActStat.getEndTime() - prevActStat.getEndTime());
				
			}else{
				deltaTime = removalTimeDelta(newActs,a, rstat, ctx.getR().getC()) + removalTimeDelta(newActs,b, rstat, ctx.getR().getC());
				deltaDist = removalDistDelta(newActs,a) + removalDistDelta(newActs,b);
			}
			break;
		case 1:
			deltaTime = removalTimeDelta(null,ctx.getActIters().get(0), rstat, ctx.getR().getC());
			deltaDist = removalDistDelta(null,ctx.getActIters().get(0));
			break;
		default:
			OutputPrinter.printError("bad ctx:"+ctx.toString());
		}
		
		Route r = ctx.getR();
		//TODO
//		double delta = wDist*deltaDist*r.getC().getCostPerDistance()+wTime*deltaDist*r.getC().getCostPerTime();
//		double delta = 0;
//		if("货车".equalsIgnoreCase(r.getC().getType())){
//			delta = wDist*deltaDist*5;
//		}
		double delta = calcTotalCost(deltaDist,deltaTime,r);
		if(ctx.getR().getActs().size() -2 == ctx.getJ().actsLen()){
			delta -= ctx.getR().getC().getFixedCost();
		}
		
		return new ConstraintsResult(true,delta,null,new ActState(),"");
	}

	private double removalDistDelta(List<Activity> acts,Activity it) {
		int curIndex = acts.indexOf(it);
		Activity prev = acts.get(curIndex-1);
		Activity next = acts.get(curIndex+1);
		double delta = costMatrix.calcDistance(prev.getLocation(), next.getLocation()) - (costMatrix.calcDistance(prev.getLocation(), it.getLocation()) + costMatrix.calcDistance(it.getLocation(), next.getLocation()));
		return delta;
	}

	private double removalTimeDelta(List<Activity> acts, Activity it, RouteState rstat, Car c) {
		int curIndex = acts.indexOf(it);
		Activity prev = acts.get(curIndex-1);
		Activity next = acts.get(curIndex+1);

		ActState prevActStat = rstat.getActStates().get(prev.getId());
		ActState nextActStat = rstat.getActStates().get(next.getId());


		double prevToNext = Math.max(costMatrix.calcTime(c, prev.getLocation(), next.getLocation()), next.getTw().getStart()) + next.getServiceTime();
		return prevToNext - nextActStat.getEndTime() - prevActStat.getEndTime();
	}

	@Override
	public void afterRemoval(RemovalCtx ctx, double delta, Object consCtx) {
	}

	@Override
	public ConstraintsResult beforeInsertion(InsertionCtx ctx) {
		//check skill set
		if(!checkSkillsSet(ctx.getJ(),ctx.getR().getC())){
			return new ConstraintsResult(false,0,null,new ActState(),"");
		}
		
		ConstraintState cstat = ctx.getS().getConstraintState(DEFAULT_CONSTRAINTS);
		RouteState rstat = cstat.getRouteState(ctx.getR());
		int lenDim = ctx.getR().getC().getDimensions().split(",").length;
		RouteState newStates = new RouteState(lenDim);
		double deltaDist = 0;
		
		// check max distance constraints
		// check max stop constraints

		List<Activity> acts = ctx.getJ().getActs();
		int l = ctx.getActIters().size();
		switch(l){
		case 2:
			Activity a = ctx.getActIters().get(0);
			Activity b = ctx.getActIters().get(1);
			
			if(b == null){
				int aIndex = ctx.getR().getActs().indexOf(a);
				Activity prev = a;
				Activity next = ctx.getR().getActs().get(aIndex+1);
				double prevToActA = costMatrix.calcDistance(prev.getLocation(),acts.get(0).getLocation());
				double actAToActB = costMatrix.calcDistance(acts.get(0).getLocation(), acts.get(1).getLocation());
				double actBToNext = costMatrix.calcDistance(acts.get(1).getLocation(), next.getLocation());
				double prevToNext = costMatrix.calcDistance(prev.getLocation(), next.getLocation());
				deltaDist = (prevToActA + actAToActB + actBToNext) - prevToNext;
			}else{
				deltaDist = insertionDistDelta(ctx.getR().getActs(),a, acts.get(0)) + insertionDistDelta(ctx.getR().getActs(),b, acts.get(1));
			}
			break;
		case 1:
			deltaDist = insertionDistDelta(ctx.getR().getActs(),ctx.getActIters().get(0), acts.get(0));
			break;
		}

		newStates.setTotalDist(rstat.getTotalDist()+deltaDist);
		newStates.setTotalStops(rstat.getTotalStops() + ctx.getActIters().size());
		
		if(!checkDistAndStops(newStates.getTotalStops(),newStates.getTotalDist(),ctx.getR().getC())){
			OutputPrinter.printError("Dist. or Stops C break");
			return new ConstraintsResult(false,0,null,new ActState(),"");
		}
		
		// check time windows constraints
		// check max time constraints
		// check capacity constraints

		ConstraintsResult cr= eachAct(ctx,rstat,newStates,ctx.getActIters().get(0),lenDim);
		boolean ok = cr.isFlag();
		
		if(ok){
			Route r = ctx.getR();
			//TODO
			double delta = calcTotalCost(newStates.getTotalDist(),newStates.getTotalTime(),r);
			if(ctx.getR().getJobs().size() == 0){
				delta += ctx.getR().getC().getFixedCost();
			}
			newStates.setTotalCost(delta);
			newStates.setMaxDim(rstat.getMaxDim());
			
			
			for(int i=0; i< ctx.getJ().getDimensions().length; i++){
				double v = ctx.getJ().getDimensions()[i];
				newStates.getMaxDim()[i] = Math.max(v, newStates.getMaxDim()[i]);
			}
			return new ConstraintsResult(true,delta,newStates,new ActState(),"");
		}else{
			return new ConstraintsResult(false,0,null,new ActState(),"");
		}
	}


	private boolean checkSkillsSet(Job j, Car c) {
		for(String skill : j.getSkills()){
			if(!c.getSkills().equals(skill)){
				OutputPrinter.printError("skills set cant fulfilled");
				return false;
			}
		}
		return true;
	}

	private boolean checkDistAndStops(int stops, double dist, Car c) {
		
//		OutputPrinter.printError("Max Dist: "+c.getMaxDistance()+" Stop: "+c.getMaxStop());
		if(c.getMaxStop() >0 ){
			if (stops > c.getMaxStop()){
				OutputPrinter.printError("Dist: "+dist+" Stop: "+stops);
				return false;
			}
		}
		
		if(c.getMaxDistance() >0 ){
			if(dist > c.getMaxDistance()){
				OutputPrinter.printError("Dist: "+dist+" Stop: "+stops);
				return false;
			}
		}
		
		
//		OutputPrinter.printError("True Dist: "+dist+" Stop: "+stops);
		return true;
	}

	private double insertionDistDelta(List<Activity> acts, Activity it, Activity act) {
		Activity prev = it;
		int prevIndex = acts.indexOf(it);
		Activity next = acts.get(prevIndex+1);
		return costMatrix.calcDistance(prev.getLocation(), act.getLocation()) + costMatrix.calcDistance(act.getLocation(), next.getLocation()) - costMatrix.calcDistance(prev.getLocation(), next.getLocation());

	}
	
	public ConstraintsResult eachAct(InsertionCtx i, RouteState rstat,RouteState newStates, Activity start,int lenDim) {
		
		String prevLoc = "";
		ActState prevStat = new ActState(lenDim);
		ConstraintsResult cr = new ConstraintsResult();
		List<Activity> newActs = i.getJ().getActs();
		if(start == null){
			 start = i.getR().getActs().get(0);
		}
		
		int l = i.getActIters().size();
		List<Activity> routeActs =  i.getR().getActs();
		int startIndex = routeActs.indexOf(start);
		switch(l){
		case 1:
			for(int ind = startIndex; ind< routeActs.size();ind++){
				Activity prev = null;
				if(ind != 0)
					prev = routeActs.get(ind-1);
				Activity e = routeActs.get(ind);
				Activity next = null;
				if(ind != routeActs.size()-1){
					next = routeActs.get(ind+1);
				}
				cr = eachActFlag(i,rstat,newStates,prevStat,prevLoc,prev,e,next,Route.getIndicator(e,start,routeActs.get(routeActs.size()-1)),lenDim);
				
				if(!cr.isFlag()){
					return cr;
				}
				prevLoc = cr.getPrevLoc();
				prevStat = cr.getPrevStat();
				
				int zeroIndex = routeActs.indexOf(i.getActIters().get(0));
				if(ind == zeroIndex){
					cr = eachActFlag(i,rstat,newStates,prevStat,prevLoc,prev,newActs.get(0),next,POS_MIDDLE,lenDim);
					
					if(!cr.isFlag()){
						return cr;
					}
					
					prevLoc = cr.getPrevLoc();
					prevStat = cr.getPrevStat();
				}
				
			}
			break;
		case 2:
			if(i.getActIters().get(1) == null){
				for(int ind = startIndex; ind< routeActs.size();ind++){
					Activity e = routeActs.get(ind);
					
					Activity prev = null;
					if(ind != 0)
						prev = routeActs.get(ind-1);
					Activity next = null;
					if(ind != routeActs.size()-1){
						next = routeActs.get(ind+1);
					}
					
					cr = eachActFlag(i,rstat,newStates,prevStat,prevLoc,prev,e,next,Route.getIndicator(e,start,routeActs.get(routeActs.size()-1)),lenDim);
					
					if(!cr.isFlag()){
						return cr;
					}
					
					prevLoc = cr.getPrevLoc();
					prevStat = cr.getPrevStat();
					
					int zeroIndex = routeActs.indexOf(i.getActIters().get(0));
					if(ind == zeroIndex){
						cr = eachActFlag(i,rstat,newStates,prevStat,prevLoc,prev,newActs.get(0),next,POS_MIDDLE,lenDim);
						
						if(!cr.isFlag()){
							return cr;
						}
						
						prevLoc = cr.getPrevLoc();
						prevStat = cr.getPrevStat();
						
						cr = eachActFlag(i,rstat,newStates,prevStat,prevLoc,prev,newActs.get(1),next,POS_MIDDLE,lenDim);
						
						if(!cr.isFlag()){
							return cr;
						}
						
						prevLoc = cr.getPrevLoc();
						prevStat = cr.getPrevStat();
						
					}
				}
			}else{
				for(int ind = startIndex; ind< routeActs.size();ind++){
					Activity e = routeActs.get(ind);
					
					Activity prev = null;
					if(ind != 0)
						prev = routeActs.get(ind-1);
					Activity next = null;
					if(ind != routeActs.size()-1){
						next = routeActs.get(ind+1);
					}
					
					cr = eachActFlag(i,rstat,newStates,prevStat,prevLoc,prev,e,next,Route.getIndicator(e,start,routeActs.get(routeActs.size()-1)),lenDim);
					
					if(!cr.isFlag()){
						return cr;
					}
					
					prevLoc = cr.getPrevLoc();
					prevStat = cr.getPrevStat();
					
					int zeroIndex = routeActs.indexOf(i.getActIters().get(0));
					if(ind == zeroIndex){
						cr = eachActFlag(i,rstat,newStates,prevStat,prevLoc,prev,newActs.get(0),next,POS_MIDDLE,lenDim);
						
						if(!cr.isFlag()){
							return cr;
						}
						
						prevLoc = cr.getPrevLoc();
						prevStat = cr.getPrevStat();
					}
					
					
					int oneIndex = routeActs.indexOf(i.getActIters().get(1));
					if(ind == oneIndex){
						cr = eachActFlag(i,rstat,newStates,prevStat,prevLoc,prev,newActs.get(1),next,POS_MIDDLE,lenDim);
						
						if(!cr.isFlag()){
							return cr;
						}
						
						prevLoc = cr.getPrevLoc();
						prevStat = cr.getPrevStat();
					}
				}
			}

			break;
		default:
			OutputPrinter.printLine("bad insertion ctx:"+i);
		}
		
		cr.setFlag(true);
		return cr;
	}
	
	private ConstraintsResult eachActFlag(InsertionCtx ctx,RouteState rstat,RouteState newStates, ActState prevStat, 
			String prevLoc, Activity prevAct, Activity act, Activity nextAct,int indicator,int lenDim){

		switch(indicator){
		case POS_START:
			prevStat = rstat.getActStat(act.getId());
			prevLoc = act.getLocation();
			break;
		case POS_MIDDLE:
			Job j = ctx.getJob(act.getJobId());
			ActState initActStat = new ActState(lenDim);
			if(!initActStat.calcAndCheckCapacity(prevStat,act,j,ctx.getR().getC())){
				OutputPrinter.printError("Capacity C breaks");
				return new ConstraintsResult(false,0,null,prevStat,prevLoc);
			}

			initActStat.calcArrTime(prevStat, costMatrix.calcTime(ctx.getR().getC(),prevLoc,act.getLocation()));
			initActStat.calcEndTime(act.getServiceTime(), act.getTw().getStart());
			
			if(!checkTW(initActStat,act.getTw()) || !checkRunningTime(initActStat,ctx.getR().getC().getMaxRunningTime())){
				OutputPrinter.printError("TW C break check failed.");
				return new ConstraintsResult(false,0,null,prevStat,prevLoc);
			}
			
			newStates.getActStates().put(act.getId(), initActStat);
			prevStat = initActStat;	
			prevLoc = act.getLocation();
			break;
		case POS_END:
			ActState initLast = new ActState(lenDim);
			initLast.calcArrTime(prevStat, costMatrix.calcTime(ctx.getR().getC(), prevLoc, act.getLocation()));
			initLast.calcEndTime(act.getServiceTime(), act.getTw().getStart());
			if(!checkTW(initLast,act.getTw())){
				OutputPrinter.printError("TW C break check failed.");
				return new ConstraintsResult(false,0,null,prevStat,prevLoc);
			}
			
			newStates.getActStates().put(act.getId(), initLast);
			newStates.setMaxArr(initLast.getArrTime());
			double deltaTime = initLast.getEndTime() - rstat.getActStat(act.getId()).getEndTime();
			//TODO: sum total cost
			return new ConstraintsResult(true,deltaTime,null,prevStat,prevLoc);
		}
		return new ConstraintsResult(true,0,null,prevStat,prevLoc);
	}

	private boolean checkRunningTime(ActState actStat, double maxRunningTime) {
		if(maxRunningTime >0 && actStat.getEndTime() > maxRunningTime){
			OutputPrinter.printError("Running Time: "+actStat.getEndTime());
			return false;
		}
		
		return true;
	}

	private boolean checkTW(ActState actStat, TimeWindow tw) {
		if(actStat.getArrTime() > tw.getEnd()){
			OutputPrinter.printError("TW1: "+actStat.getArrTime()+","+tw.getEnd());
			return false;
		}
		
		if(actStat.getEndTime() < tw.getStart() || actStat.getEndTime() > tw.getEnd()){
			OutputPrinter.printError("TW2: "+actStat.getEndTime()+","+tw.getStart()+","+tw.getEnd());
			return false;
		}
		
		return true;
	}
	
	private double calcTotalCost(double dist,double time, Route r) {
		//TODO
		double total = 0;
		//hangzhou toal
//		if(dist >0 && dist <= 50 ){
//			if("1.5T".equalsIgnoreCase(r.getC().getType())){
//				total = 200;
//			}else if("3.5T".equalsIgnoreCase(r.getC().getType())){
//				total = 263;
//			}else if("7T".equalsIgnoreCase(r.getC().getType())){
//				total = 500;
//			}else{
//				System.err.println("no such car");
//			}
//		}else if(dist <= 100 ){
//			if("1.5T".equalsIgnoreCase(r.getC().getType())){
//				total = 220;
//			}else if("3.5T".equalsIgnoreCase(r.getC().getType())){
//				total = 263;
//			}else if("7T".equalsIgnoreCase(r.getC().getType())){
//				total = 500;
//			}else{
//				System.err.println("no such car");
//			}
//		}
//		else{
//			if("1.5T".equalsIgnoreCase(r.getC().getType())){
//				total = 220;
//			}else if("3.5T".equalsIgnoreCase(r.getC().getType())){
//				total = 263;
//			}
//		}
		if("百度".equalsIgnoreCase(r.getC().getType())){
			total = 11;
		}else if("滴滴".equalsIgnoreCase(r.getC().getType())){
			if(dist<=15){
				total = 8+2*dist+0.55*time;
			}else{
				total = 8+2*dist+0.55*time+(dist-15)*1.5;
			}
		}else if("货车".equalsIgnoreCase(r.getC().getType())){
			if(dist<=5){
				total = 30;
			}else{
				total = 30+(dist-5)*5;
			}
		}else{
			System.err.println("no such car");
		}
		
		//initial total
//		total =  dist*wDist*r.getC().getCostPerDistance()
//				+time*wTime*r.getC().getCostPerTime();
		
		return total;
		
		
	}

}
