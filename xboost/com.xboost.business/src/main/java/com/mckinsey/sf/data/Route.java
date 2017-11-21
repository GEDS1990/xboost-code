package com.mckinsey.sf.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.mckinsey.sf.constants.IConstants;
import com.mckinsey.sf.data.constraint.IConstraint;
import com.mckinsey.sf.data.solution.RouteJson;
import com.mckinsey.sf.data.solution.Solution;
import com.mckinsey.sf.insertion.InsertionCtx;
import com.mckinsey.sf.printer.OutputPrinter;
import com.mckinsey.sf.removal.RemovalCtx;
import com.mckinsey.sf.removal.ShawRemoval;

/**
 * 
 * Author：Alivia Chen 
 * Email : alivia_chen@mckinsey.com 
 * Date ：Apr 28, 2017
 * 
 * @version
 */
public class Route implements IConstants, Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7134449492130752200L;
	private String id;
	private Car c;
	private Map<String, Job> jobs = new HashMap<String, Job>();
	private List<Activity> acts = new ArrayList<Activity>();
	private Map<String, List<Activity>> jtoA = new HashMap<String, List<Activity>>();
	private double totalVolume;

	public Route() {

	}

	public Route(String id, Car c, Map<String, Job> jobs, List<Activity> acts, Map<String, List<Activity>> jtoA) {
		super();
		this.id = id;
		this.c = c;
		this.jobs = jobs;
		this.acts = acts;
		this.jtoA = jtoA;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Car getC() {
		return c;
	}

	public void setC(Car c) {
		this.c = c;
	}

	public Map<String, Job> getJobs() {
		return jobs;
	}

	public void setJobs(Map<String, Job> jobs) {
		this.jobs = jobs;
	}

	public List<Activity> getActs() {
		return acts;
	}

	public void setActs(List<Activity> acts) {
		this.acts = acts;
	}

	public Map<String, List<Activity>> getJtoA() {
		return jtoA;
	}

	public void setJtoA(Map<String, List<Activity>> jtoA) {
		this.jtoA = jtoA;
	}
	
	public double getTotalVolume() {
		return totalVolume;
	}

	public void setTotalVolume(double totalVolume) {
		this.totalVolume = totalVolume;
	}

	public void applyInsertion(InsertionCtx ctx) {
		if (!this.getId().equals(ctx.getR().getId())) {
			OutputPrinter.printError("bad insertion ctx, bad route");
		}

		List<Activity> actsCtx = ctx.getJ().getActs();

		// for (int i = 0; i < ctx.getJ().getActs().size(); i++) {
		// acts.add(ctx.getJ().getActs().get(i));
		// }

		if (actsCtx.size() != ctx.getActIters().size()) {
			OutputPrinter.printError("bad insertion ctx, different length");
		}

		List<Activity> actIters = new ArrayList<Activity>();
		Activity prev = null;

		for (int i = 0; i < actsCtx.size(); i++) {
			Activity e = ctx.getActIters().get(i);
			if (e == null) {
				e = prev;
			}
			int index = ctx.getR().getActs().indexOf(e);

			Activity a = actsCtx.get(i);
			// a.setJobId(ctx.getJ().getId());;
			this.acts.add(index +1, a);
			prev = a;
			actIters.add(a);
		}

		jobs.put(ctx.getJ().getId(), ctx.getJ());
		jtoA.put(ctx.getJ().getId(), actIters);
	}

	public static Route newRoute(Car car, String id) {
		Route route = new Route();
		route.setC(car);
		route.setId(id);

		List<Activity> acts = new ArrayList<Activity>();
		Activity act1 = new Activity(UUID.randomUUID().toString(), "START", car.getTw(), 0, car.getStartLocation());
		acts.add(act1);
		Activity act2 = new Activity(UUID.randomUUID().toString(), "END", car.getTw(), 0, car.getEndLocation());
		acts.add(act2);
		route.setActs(acts);

		return route;
	}

	public void eachJob(ShawRemoval shawRemoval, List<RemovalCtx> ctxs, IConstraint[] constraints, Solution s, Route r) {
		for (Map.Entry<String, Job> entry : jobs.entrySet()) {
			String id = entry.getKey();
			Job j = entry.getValue();

			List<Activity> iters = jtoA.get(id);
			f(shawRemoval,ctxs, constraints, j, iters, s, r);
		}
	}

	public boolean eachAct(RouteJson json, Activity start) {
		if (start == null) {
			start = acts.get(0);
		}

		for (Activity e : acts) {
			boolean ok = func(json, e, getIndicator(e, start, acts.get(acts.size() - 1)));
			if (!ok) {
				return ok;
			}
		}

		return true;
	}

	public static int getIndicator(Activity cur, Activity start, Activity end) {
		if (cur.getId().equals(start.getId())) {
			return POS_START;
		}

		if (cur.getId().equals(end.getId())) {
			return POS_END;
		}

		return POS_MIDDLE;
	}

	private boolean func(RouteJson json, Activity act, int indicator) {
		switch (indicator) {
		case POS_START:
		case POS_END:
		case POS_MIDDLE:
			json.getActs().add(act.getId());
		}
		return true;
	}

	private void f(ShawRemoval shawRemoval, List<RemovalCtx> ctxs, IConstraint[] constraints, Job job, List<Activity> iters, Solution s,
			Route r) {
		RemovalCtx ctx = new RemovalCtx(s, r, job, iters);
		boolean can = shawRemoval.beforeRemoval(ctx,constraints);
		if (can) {
			ctxs.add(ctx);
		}
	}

	public void applyRemoval(RemovalCtx ctx) {
		if (!id.equals(ctx.getR().getId())) {
			OutputPrinter.printError("bad removal ctx");
		}

		List<Activity> es = jtoA.get(ctx.getJ().getId());
		for (Activity e : es) {
			acts.remove(e);
		}

		jtoA.remove(ctx.getJ().getId());
		jobs.remove(ctx.getJ().getId());

	}

}
