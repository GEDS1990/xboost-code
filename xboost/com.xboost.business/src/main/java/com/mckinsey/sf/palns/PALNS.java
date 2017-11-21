package com.mckinsey.sf.palns;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicIntegerArray;

import com.mckinsey.sf.data.config.PalnsConfig;
import com.mckinsey.sf.data.solution.ISolution;
import com.mckinsey.sf.insertion.IInsertion;
import com.mckinsey.sf.printer.OutputPrinter;
import com.mckinsey.sf.removal.IRemoval;

/**   
*    
* Author：Alivia Chen   
* Email : alivia_chen@mckinsey.com
* Date  ：May 2, 2017
* @version        
*/
public class PALNS{
	private PalnsConfig config;
	private ISolution best;
	private ISolution current;

	private IRemoval[] removalOps;
	private IInsertion[] insertionOps;
	private double[] weights;
	private AtomicIntegerArray opsScore;
	private AtomicIntegerArray opsCounts;
	private boolean quit;
	
	private int worker;

	public int getWorker() {
		return worker;
	}

	public void setWorker(int worker) {
		this.worker = worker;
	}

	public PalnsConfig getConfig() {
		return config;
	}

	public void setConfig(PalnsConfig config) {
		this.config = config;
	}

	public ISolution getBest() {
		return best;
	}

	public void setBest(ISolution best) {
		this.best = best;
	}

	public ISolution getCurrent() {
		return current;
	}

	public void setCurrent(ISolution current) {
		this.current = current;
	}

	public IRemoval[] getRemovalOps() {
		return removalOps;
	}

	public void setRemovalOps(IRemoval[] removalOps) {
		this.removalOps = removalOps;
	}

	public IInsertion[] getInsertionOps() {
		return insertionOps;
	}

	public void setInsertionOps(IInsertion[] insertionOps) {
		this.insertionOps = insertionOps;
	}

	public double[] getWeights() {
		return weights;
	}

	public void setWeights(double[] weights) {
		this.weights = weights;
	}

	public AtomicIntegerArray getOpsScore() {
		return opsScore;
	}

	public void setOpsScore(AtomicIntegerArray opsScore) {
		this.opsScore = opsScore;
	}

	public AtomicIntegerArray getOpsCounts() {
		return opsCounts;
	}

	public void setOpsCounts(AtomicIntegerArray opsCounts) {
		this.opsCounts = opsCounts;
	}

	public boolean isQuit() {
		return quit;
	}

	public void setQuit(boolean quit) {
		this.quit = quit;
	}

	public PALNS(ISolution s, List<IRemoval> rops, List<IInsertion> iops, PalnsConfig c) {
		this.config = c;
		this.best = s;
		this.current = s;
		this.removalOps = rops.toArray(new IRemoval[0]);
		this.insertionOps = iops.toArray(new IInsertion[0]);
		weights = new double[rops.size() * iops.size()];
		for (int i = 0; i < weights.length; i++) {
			weights[i] = 1;
		}
		opsScore = new AtomicIntegerArray(weights.length);
		opsCounts = new AtomicIntegerArray(weights.length);
		this.quit = false;

	}
	
	public PALNS() {
	}

	public ISolution runAlgo() {
		
		int cores = config.getCores();
		CountDownLatch count = new CountDownLatch(cores);
		for(int i = 0; i< cores; i++){
			OutputPrinter.printLine("start worker i: "+ i);
			PalnsThread t = new PalnsThread(i,this,count);
			t.start();
		}
			
		 try{
			 count.await();
		 }catch (InterruptedException e){  
			 e.printStackTrace();  
		}  
		
		return best;
	}

}
