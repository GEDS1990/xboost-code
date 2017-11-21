package com.mckinsey.sf.removal;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import com.mckinsey.sf.constants.IConstants;
import com.mckinsey.sf.data.Dimension;
import com.mckinsey.sf.data.JobSimilarity;
import com.mckinsey.sf.data.RelatednessCtx;
import com.mckinsey.sf.data.Route;
import com.mckinsey.sf.data.RouteState;
import com.mckinsey.sf.data.RoutingTransportCosts;
import com.mckinsey.sf.data.constraint.ConstraintState;
import com.mckinsey.sf.data.constraint.IConstraint;
import com.mckinsey.sf.data.solution.ISolution;
import com.mckinsey.sf.data.solution.Solution;
import com.mckinsey.sf.printer.OutputPrinter;

/**   
*    
* Author：Alivia Chen   
* Email : alivia_chen@mckinsey.com
* Date  ：May 2, 2017
* @version        
*/
public class ShawRemoval implements IRemoval, IConstants {
	private double ws;
	private double wt;
	private double wd;
	private double wc;
	private double p;
	private double k;
	private int maxK;
	private double maxD;

	public double getMaxD() {
		return maxD;
	}

	public void setMaxD(double maxD) {
		this.maxD = maxD;
	}

	private RoutingTransportCosts transportCost;

	public ShawRemoval(double ws, double wt, double wd, double wc, double p, double k, int maxK, double maxD) {
		super();
		this.ws = ws;
		this.wt = wt;
		this.wd = wd;
		this.wc = wc;
		this.p = p;
		this.k = k;
		this.maxK = maxK;
		this.maxD = maxD;
	}

	public double getWs() {
		return ws;
	}

	public void setWs(double ws) {
		this.ws = ws;
	}

	public double getWt() {
		return wt;
	}

	public void setWt(double wt) {
		this.wt = wt;
	}

	public double getWd() {
		return wd;
	}

	public void setWd(double wd) {
		this.wd = wd;
	}

	public double getWc() {
		return wc;
	}

	public void setWc(double wc) {
		this.wc = wc;
	}

	public double getP() {
		return p;
	}

	public void setP(double p) {
		this.p = p;
	}

	public double getK() {
		return k;
	}

	public void setK(double k) {
		this.k = k;
	}

	public int getMaxK() {
		return maxK;
	}

	public void setMaxK(int maxK) {
		this.maxK = maxK;
	}

	public RoutingTransportCosts getTransportCost() {
		return transportCost;
	}

	public void setTransportCost(RoutingTransportCosts transportCost) {
		this.transportCost = transportCost;
		this.maxD = transportCost.getMaxDistance();
	}

	@Override
	public ISolution remove(ISolution solution) {
		Solution s = (Solution) solution;
		int n = s.numAssignedJobs();
		IConstraint[] constraints = s.getConstraints();
		List<RemovalCtx> ctxs = new ArrayList<RemovalCtx>();

		for (Route r : s.getRoutes().values()) {
			r.eachJob(this,ctxs, constraints, s, r);
		}

		if (ctxs.size() == 0) {
			OutputPrinter.printLine("shaw_removal: empty ctxs");
			return s;
		}

		Dimension dim = calcMax(ctxs);
		JobSimilarity simi = new JobSimilarity(this, ws, wt, wd, wc, dim.getD(), dim.getT(), dim.getC());
		// k is the number of requests should be removed;
		int k = randomK(this.maxK, n, this.k);

		HashMap<Integer, Boolean> d = new HashMap<Integer, Boolean>();
		List<Integer> initD = new ArrayList<Integer>();

		int index = new Random().nextInt(ctxs.size());
		d.put(index, true);
		initD.add(index);

		while (initD.size() < k) {
			int i = new Random().nextInt(initD.size());
			RemovalCtx r = ctxs.get(i);
			List<RelatednessCtx> l = new ArrayList<RelatednessCtx>();

			for (int j = 0; j < ctxs.size(); j++) {
				RemovalCtx ctx = ctxs.get(j);
				if (d.containsKey(j)) {
					continue;
				}

				double relatedness = simi.similarity(r, ctx);
				l.add(new RelatednessCtx(ctx, relatedness, j));
			}

			Collections.sort(l);
			double y = new Random().nextDouble();
			index = (int) Math.pow(y, this.p) * l.size();

			d.put(l.get(index).getI(), true);
			initD.add(l.get(index).getI());

		}

		HashMap<String, Boolean> changed = new HashMap<String, Boolean>();
		for (int i = 0; i < initD.size(); i++) {
			RemovalCtx ctx = ctxs.get(initD.get(i));
			s.applyRemoval(ctx);
			afterRemoval(ctx, constraints);
			changed.put(ctx.getR().getId(), true);
		}

		updateStates(s, changed);
		return s;
	}

	private Dimension calcMax(List<RemovalCtx> ctxs) {
		int l = ctxs.get(0).getJ().getDimensions().length;
		double[] maxC = new double[l];
		double maxT = 0;
		for (RemovalCtx ctx : ctxs) {
			ConstraintState cstat = ctx.getS().getConstraintState(DEFAULT_CONSTRAINTS);
			RouteState rstat = cstat.getRouteState(ctx.getR());

			for (int i = 0; i < rstat.getMaxDim().length; i++) {
				maxC[i] = Math.max(rstat.getMaxDim()[i], maxC[i]);
			}

			maxT = Math.max(rstat.getMaxArr(), maxT);
		}

		return new Dimension(this.maxD, maxT, maxC);
	}

	@Override
	public String name() {
		return "Shaw Removal";
	}

	@Override
	public double calcDistance(String from, String to) {
		return transportCost.calcDistance(from, to);
	}

}
