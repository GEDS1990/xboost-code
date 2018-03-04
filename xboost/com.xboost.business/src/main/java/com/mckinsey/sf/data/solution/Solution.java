package com.mckinsey.sf.data.solution;

import com.mckinsey.sf.data.Activity;
import com.mckinsey.sf.data.Car;
import com.mckinsey.sf.data.Job;
import com.mckinsey.sf.data.Route;
import com.mckinsey.sf.data.car.ICarManager;
import com.mckinsey.sf.data.config.ICostCalculator;
import com.mckinsey.sf.data.config.NoiseMaker;
import com.mckinsey.sf.data.constraint.ConstraintState;
import com.mckinsey.sf.data.constraint.IConstraint;
import com.mckinsey.sf.insertion.IInsertion;
import com.mckinsey.sf.insertion.InsertionCtx;
import com.mckinsey.sf.removal.RemovalCtx;
import com.mckinsey.sf.utils.DeepCopy;
import com.xboost.websocket.SystemWebSocketHandler;
import org.springframework.web.socket.TextMessage;

import java.io.Serializable;
import java.util.*;
import java.util.Map.Entry;

/**
 * 
 * Author：Alivia Chen 
 * Email : alivia_chen@mckinsey.com 
 * Date ：Apr 28, 2017
 * 
 * @version
 */
public class Solution implements ISolution,Serializable {
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6443864592238059662L;
	private HashMap<String, ConstraintState> states = new HashMap<String, ConstraintState>();
	private HashMap<String, Route> routes = new HashMap<String, Route>();
	private HashMap<String, Job> unassigned = new HashMap<String, Job>();
	private ICarManager cm;
	private double cost;
	private IConstraint[] constraints;
	private ICostCalculator objective;
	private NoiseMaker noiser;
	private static SystemWebSocketHandler systemWebSocketHandler = new SystemWebSocketHandler();

	public Solution() {
		super();
	}

	public Solution(HashMap<String, ConstraintState> states, HashMap<String, Route> routes,
			HashMap<String, Job> unassigned, ICarManager cm, double cost, IConstraint[] constraints,
			ICostCalculator objective, NoiseMaker noiser) {
		super();
		this.states = states;
		this.routes = routes;
		this.unassigned = unassigned;
		this.cm = cm;
		this.cost = cost;
		this.constraints = constraints;
		this.objective = objective;
		this.noiser = noiser;
	}

	public HashMap<String, ConstraintState> getStates() {
		return states;
	}

	public void setStates(HashMap<String, ConstraintState> states) {
		this.states = states;
	}

	public HashMap<String, Route> getRoutes() {
		return routes;
	}

	public void setRoutes(HashMap<String, Route> routes) {
		this.routes = routes;
	}

	public HashMap<String, Job> getUnassigned() {
		return unassigned;
	}

	public void setUnassigned(HashMap<String, Job> unassigned) {
		this.unassigned = unassigned;
	}

	public ICarManager getCm() {
		return cm;
	}

	public void setCm(ICarManager cm) {
		this.cm = cm;
	}

//	public double getCost() {
//		return cost;
//	}
//
	public void setCost(double cost) {
		this.cost = cost;
	}

	public IConstraint[] getConstraints() {
		return constraints;
	}

	public void setConstraints(IConstraint[] constraints) {
		this.constraints = constraints;
	}

	public ICostCalculator getObjective() {
		return objective;
	}

	public void setObjective(ICostCalculator objective) {
		this.objective = objective;
	}

	public NoiseMaker getNoiser() {
		return noiser;
	}

	public void setNoiser(NoiseMaker noiser) {
		this.noiser = noiser;
	}
	
	
	public static Solution newSolution(Job[] jobs, RouteJson[] routes, IConstraint[] cons, ICarManager cm, ICostCalculator obj,
			NoiseMaker noiser, IInsertion constructive) {
		HashMap<String, Route> initRoutes = new HashMap<String, Route>();

//		int jd11 = (routes.length/10)>0?(routes.length/10):1;
//		int jd22 = 0;
		for(RouteJson r : routes){
//			jd22++;
//			if(jd22%jd11 == 0){
//				systemWebSocketHandler.sendMessageToUser( new TextMessage("▉"));
//			}
			Route initR = routeFromJson(r);
			initRoutes.put(initR.getId(),initR);
		}
		
		HashMap<String, Job> initJobs = new HashMap<String, Job>();
		int jd1 = jobs.length/10;
		int jd2 = 0;
		for(Job j : jobs){
			jd2++;
			if(jd2 == 0){
				systemWebSocketHandler.sendMessageToUser( new TextMessage("▉2"));
			}
			initJobs.put(j.getId(), j);
		}
		
		Solution ret = new Solution(new HashMap<String,ConstraintState>(), initRoutes,
				initJobs, cm, Double.MAX_VALUE, cons,obj,noiser);
		int jd3 = ret.routes.entrySet().size()/10;
		int jd4 = 0;
		for(Entry<String, Route> entryRoute : ret.routes.entrySet()){
//			jd4++;
//			if(jd4%jd3 == 0){
//				systemWebSocketHandler.sendMessageToUser( new TextMessage("▉3"));
//			}
			Route r = entryRoute.getValue();
			for(IConstraint c : ret.constraints){
				c.updateStates(ret, r);
			}

		}

		ret = (Solution)constructive.insert(ret);ret.calcCost();
		return ret;

	}



	private static Route routeFromJson(RouteJson r) {
		Route route = new Route(r.getId(),r.getC(), new HashMap<String, Job>(), new ArrayList<Activity>(), new HashMap<String, List<Activity>>());
		HashMap<String,Activity> acts = new HashMap<String,Activity>();

		for(Job j : r.getJobs()){
			route.getJobs().put(j.getId(), j);
			route.getJtoA().put(j.getId(), new ArrayList<Activity>(2));
			for(Activity act : j.getActs()){
				acts.put(act.getId(), act);
			}
		}

		Car car =r.getC();
		route.getActs().add(new Activity(UUID.randomUUID().toString(), "START", car.getTw(), 0, car.getStartLocation()));
		for(String id:  r.getActs()){
			Activity act = acts.get(id);
			route.getActs().add(act);
			//TODO
			route.getJtoA().get(act.getJobId()).add(act);
			route.getJtoA().put(act.getJobId(), route.getJtoA().get(act.getJobId()));
		}

		route.getActs().add(new Activity(UUID.randomUUID().toString(), "END", car.getTw(), 0, car.getEndLocation()));
		return route;
	}

	public HashMap<String, Route> newRoutes() {
		HashMap<String, Route> ret = new HashMap<String, Route>();
		List<Car> cars = cm.newCars(unassigned);

		for (Entry<String, Job> entry : unassigned.entrySet()) {
			Job j = entry.getValue();

			boolean hasMatched = false;
			for (Car car : cars) {
				if (car.getStartLocation().equals(j.getPickup().getLocation())) {
					hasMatched = true;
				}
			}

			if (!hasMatched) {
//				OutputPrinter.printLine("missing route from loc.: " + j.getPickup().getLocation());
			}

		}

		if (cars.size() == 0) {
			return ret;
		}
		int jd3 = (cars.size()*constraints.length/10)>0?(cars.size()*constraints.length/10):1;
		int jd4 = 0;
		int jd5 = 0;
		for (Car car : cars) {
			jd4++;
			Route r = Route.newRoute(car, UUID.randomUUID().toString());
			for (IConstraint c : constraints) {
//				jd5++;
//				if((jd4*jd5)%jd3 == 0){
//					systemWebSocketHandler.sendMessageToUser( new TextMessage("▉3"));
//				}
				c.updateStates(this, r);
			}
			this.routes.put(r.getId(), r);
			ret.put(r.getId(), r);
		}

		return ret;

	}

	public void applyInsetion(InsertionCtx ctx) {
		ctx.getR().applyInsertion(ctx);
		unassigned.remove(ctx.getJ().getId());
	}

	@Override
	public ISolution clone() {
		Solution newSol  = (Solution)DeepCopy.copy(this);
		return newSol;
	}

	@Override
	public double cost() {
		return cost;
	}

	@Override
	public synchronized void calcCost() {

		Iterator<Entry<String, Route>> it = routes.entrySet().iterator();
		while (it.hasNext()){
		   Entry<String, Route> entry = it.next();
//		   String id = entry.getKey();
			Route r = entry.getValue();

			if(r.getJobs().size() == 0){
				it.remove();
				for(IConstraint c : constraints){
					c.clearStates(this, r);
				}
				cm.freeCar(r.getC());
			}
		}

		cost = objective.getSolutionCost(this);

	}

	public SolutionJson SolutionToJson(Solution s) {
		SolutionJson json = new SolutionJson(new RouteJson[s.routes.size()], new Job[s.unassigned.size()]);

		int i = 0;
		for(Job job : s.unassigned.values()){
			json.getUnassignedJobs()[i++] = job;
		}

		int j =0;
		for(Route r : s.routes.values()){
			json.getRoutes()[j++] = routeToJson(r);
		}

		return json;
	}

	private RouteJson routeToJson(Route r) {
		RouteJson json = new RouteJson(r.getId(),r.getC(), new Job[r.getJobs().size()], new ArrayList<String>());

		int jIndex = 0;
		for(Job j : r.getJobs().values()){
			json.getJobs()[jIndex++] = j;
		}

		r.eachAct(json,null);

		return json;
	}

	public ConstraintState getConstraintState(String defaultConstraints) {
		if (states.containsKey(defaultConstraints)) {
			return states.get(defaultConstraints);
		} else {
			ConstraintState constraintStates = new ConstraintState();
			states.put(defaultConstraints, constraintStates);
			return constraintStates;
		}
	}

	public int numAssignedJobs() {
		int ret = 0;
		for(Entry<String, Route> entry: routes.entrySet()){
			Route r = entry.getValue();
			ret += r.getJobs().size();
		}
		return ret;
	}

	public void applyRemoval(RemovalCtx ctx) {
		ctx.getR().applyRemoval(ctx);
		unassigned.put(ctx.getJ().getId(), ctx.getJ());
	}

	public void removeEmptyRoutes(HashMap<String, Boolean> changed) {
		
		Iterator<Entry<String, Boolean>> it = changed.entrySet().iterator();
		while(it.hasNext()){
			Entry<String, Boolean> item = it.next();
			String rid = item.getKey();
			
			Route r = routes.get(rid);
			
			if(r.getJobs().size() == 0){
				routes.remove(rid);
				it.remove();
				
				for(IConstraint c : constraints){
					c.clearStates(this, r);
				}
				
				cm.freeCar(r.getC());
			}
		}
	
	}

	public int numTotalJobs() {
		return numAssignedJobs()+this.unassigned.size();
	}

}
