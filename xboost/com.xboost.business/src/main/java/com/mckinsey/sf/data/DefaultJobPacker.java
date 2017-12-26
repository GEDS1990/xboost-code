package com.mckinsey.sf.data;

import com.mckinsey.sf.data.solution.RouteJson;
import com.mckinsey.sf.data.solution.SolutionJson;
import com.mckinsey.sf.printer.OutputPrinter;
import org.apache.commons.lang3.StringUtils;

import java.util.*;

/**
 * 
 * Author：Alivia Chen 
 * Email : alivia_chen@mckinsey.com 
 * Date ：May 2, 2017
 * 
 * @version
 */
public class DefaultJobPacker {
	private HashMap<String, Group> jobGroups;
	private HashMap<String, List<Job>> jobGroupIndex;
	private HashMap<String, List<String>> actGroups;
	private HashMap<String, String> actIndex;
	private double interval;
	private int maxJobs;

	public DefaultJobPacker(double interval, int maxJobs) {
		super();
		this.jobGroups = new HashMap<String, Group>();
		this.jobGroupIndex = new HashMap<String, List<Job>>();
		this.actGroups = new HashMap<String, List<String>>();
		this.actIndex = new HashMap<String, String>();
		this.interval = interval;
		this.maxJobs = maxJobs;
	}

	public HashMap<String, Group> getJobGroups() {
		return jobGroups;
	}

	public void setJobGroups(HashMap<String, Group> jobGroups) {
		this.jobGroups = jobGroups;
	}

	public HashMap<String, List<Job>> getJobGroupIndex() {
		return jobGroupIndex;
	}

	public void setJobGroupIndex(HashMap<String, List<Job>> jobGroupIndex) {
		this.jobGroupIndex = jobGroupIndex;
	}

	public HashMap<String, List<String>> getActGroups() {
		return actGroups;
	}

	public void setActGroups(HashMap<String, List<String>> actGroups) {
		this.actGroups = actGroups;
	}

	public HashMap<String, String> getActIndex() {
		return actIndex;
	}

	public void setActIndex(HashMap<String, String> actIndex) {
		this.actIndex = actIndex;
	}

	public double getInterval() {
		return interval;
	}

	public void setInterval(double interval) {
		this.interval = interval;
	}

	public int getMaxJobs() {
		return maxJobs;
	}

	public void setMaxJobs(int maxJobs) {
		this.maxJobs = maxJobs;
	}

	public SolutionJson pack(SolutionJson initSolution) {
		OutputPrinter.printLine("packing jobs...");
		RouteJson[] routes = new RouteJson[initSolution.getRoutes().length];

		Job[] unassigned = packJobs("unassigned", initSolution.getUnassignedJobs());

		for (int i = 0; i < initSolution.getRoutes().length; i++) {
			RouteJson r = packRouteJobs(initSolution.getRoutes()[i]);
			routes[i] = r;
		}

		return new SolutionJson(routes, unassigned);
	}

	private RouteJson packRouteJobs(RouteJson r) {
		Job[] vjobs = packJobs(r.getId(), r.getJobs());
		List<String> vids = new ArrayList<String>();
		String prev = "";
		for (int i = 0; i < r.getActs().size(); i++) {
			String id = r.getActs().get(i);
			if (actIndex.containsKey(id)) {
				String vid = actIndex.get(id);
				if (!prev.equals(vid)) {
					vids.add(vid);
					prev = vid;
				}
			}
		}
		return new RouteJson(r.getId(), r.getC(), vjobs, vids);
	}

	private Job[] packJobs(String scope, Job[] jobs) {
		for (Job j : jobs) {
			GroupCtx ctx = calcGroupCtx(scope, j);
			String key = ctx.key();
			Group group = null;
			if (!jobGroups.containsKey(key)) {
				group = new Group(ctx, new ArrayList<Job>(10));
			} else {
				group = jobGroups.get(key);
			}
			group.getJobs().add(j);
			jobGroups.put(key, group);
		}

		List<Job> virJobs = new ArrayList<Job>(jobGroups.size());

		for (Map.Entry<String, Group> entry : jobGroups.entrySet()) {
			Group gs = entry.getValue();
			Job[] vjs = makeVitrualJob(gs);
			for (int i = 0; i < vjs.length; i++) {
				virJobs.add(vjs[i]);
			}
		}
		return virJobs.toArray(new Job[0]);
	}

	private Job[] makeVitrualJob(Group group) {
		List<Job> jobs = group.getJobs();
		GroupCtx ctx = group.getCtx();
		int id = 0;
		List<Job> ret = new ArrayList<Job>(30);
		
//		double totalVolume = 0;
//		for(Job j : jobs){
//			totalVolume += j.getDimensions()[0];
//		}

		int jobSize = jobs.size();
		int startIndex = 0;
		while (jobSize > maxJobs) {
			List<Job> initJobs = new ArrayList<Job>(maxJobs);
			for (int i = startIndex; i < maxJobs + startIndex; i++) {
				initJobs.add(jobs.get(i));
			}
			startIndex += maxJobs;
			jobSize -= maxJobs;

			Job j = initMakeVitrualJob(ctx.key() + "|" + id, ctx, initJobs);
			jobGroupIndex.put(j.getId(), initJobs);
			ret.add(j);
			id++;
		}

		if (jobSize > 0) {
			List<Job> initJobs = new ArrayList<Job>(maxJobs);
			for (int i = startIndex; i < jobs.size(); i++) {
				initJobs.add(jobs.get(i));
			}
			Job j = initMakeVitrualJob(ctx.key() + "|" + id, ctx, initJobs);
			jobGroupIndex.put(j.getId(), initJobs);
			ret.add(j);
		}

		return ret.toArray(new Job[0]);
	}

	private Job initMakeVitrualJob(String key, GroupCtx ctx, List<Job> jobs) {
		List<String> pickupIds = new ArrayList<String>(jobs.size());
		List<String> deliveryIds = new ArrayList<String>(jobs.size());

		double[] dims = new double[jobs.get(0).getDimensions().length];
		double pickupSerivceTime = 0;
		double deliverySerivceTime = 0;

		for (Job j : jobs) {
			if (j.getPickup() != null) {
				pickupIds.add(j.getPickup().getId());
				pickupSerivceTime += j.getDelivery().getServiceTime();
			}

			if (j.getDelivery() != null) {
				deliveryIds.add(j.getDelivery().getId());
				deliverySerivceTime += j.getDelivery().getServiceTime();
			}

			for (int i = 0; i < dims.length; i++) {
				dims[i] += j.getDimensions()[i];
			}
		}

		Activity pickup = null;
		Activity delivery = null;

		if (!StringUtils.isEmpty(ctx.getFrom())) {
			pickup = new Activity(String.valueOf(UUID.randomUUID()), "PICKUP", ctx.getFromTW(), pickupSerivceTime,
					ctx.getFrom());
			pickup.setJobId(key);
			actGroups.put(pickup.getId(), pickupIds);
		}

		if (!StringUtils.isEmpty(ctx.getTo())) {
			delivery = new Activity(String.valueOf(UUID.randomUUID()), "DELIVERY", ctx.getToTW(), deliverySerivceTime,
					ctx.getTo());
			delivery.setJobId(key);
			actGroups.put(delivery.getId(), deliveryIds);
		}

		Job vjob = new Job(key, ctx.getPriority(), ctx.getSkills(), dims, pickup, delivery, ctx.isFixed());
		System.out.println("job "+key+",volume:"+dims[0]);
		return vjob;
	}

	private GroupCtx calcGroupCtx(String scope, Job j) {
		GroupCtx ctx = new GroupCtx();
		ctx.setScope(scope);
		ctx.setFixed(j.isFixed());
		ctx.setSkills(j.getSkills());
		ctx.setPriority(j.getPriority());

		if (j.getPickup() != null) {
			ctx.setFrom(j.getPickup().getLocation());
			ctx.setFromTW(j.getPickup().getTw());
			ctx.getFromTW().setStart(floor(ctx.getFromTW().getStart(), interval));
			ctx.getFromTW().setEnd(floor(ctx.getFromTW().getEnd(), interval));
		}

		if (j.getDelivery() != null) {
			ctx.setTo(j.getDelivery().getLocation());
			ctx.setToTW(j.getDelivery().getTw());
			ctx.getToTW().setStart(floor(ctx.getToTW().getStart(), interval));
			ctx.getToTW().setEnd(floor(ctx.getToTW().getEnd(), interval));
		}

		return ctx;
	}

	private double floor(double t, double interval) {
		return (t / interval) * interval * 1.0;
	}

	public SolutionJson unpack(SolutionJson s) {
		OutputPrinter.printLine("unpacking jobs...");
		List<RouteJson> routes = new ArrayList<RouteJson>(s.getRoutes().length);
		Job[] unassigned = unpackJobs(s.getUnassignedJobs());

		for (int i = 0; i < s.getRoutes().length; i++) {
			RouteJson r = unpackRouteJobs(s.getRoutes()[i]);
			routes.add(r);
		}

		return new SolutionJson(routes.toArray(new RouteJson[0]), unassigned);
	}

	private RouteJson unpackRouteJobs(RouteJson r) {
		Job[] jobs = unpackJobs(r.getJobs());
		List<String> acts = new ArrayList<String>(10 * r.getActs().size());
		
		for (String id : r.getActs()) {
			System.out.println("id:"+id);
			if(actGroups.containsKey(id)){
				for (String act : actGroups.get(id)) {
					acts.add(act);
				}
			}
		}

		return new RouteJson(r.getId(), r.getC(), jobs, acts);
	}

	private Job[] unpackJobs(Job[] jobs) {
		List<Job> ret = new ArrayList<Job>(10 * jobs.length);

		for (Job j : jobs) {
			for (Job job : jobGroupIndex.get(j.getId())) {
				ret.add(job);
			}
		}
		return ret.toArray(new Job[0]);
	}

}