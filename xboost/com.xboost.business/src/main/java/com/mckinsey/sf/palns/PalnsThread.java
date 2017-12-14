package com.mckinsey.sf.palns;

import com.mckinsey.sf.data.IntPair;
import com.mckinsey.sf.data.solution.ISolution;
import com.mckinsey.sf.data.solution.Solution;
import com.mckinsey.sf.insertion.IInsertion;
import com.mckinsey.sf.printer.OutputPrinter;
import com.mckinsey.sf.removal.IRemoval;

import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicIntegerArray;

import com.mckinsey.sf.constants.IConstants;
import com.xboost.util.ShiroUtil;
import org.springframework.web.socket.TextMessage;

/**   
*    
* Author：Alivia Chen   
* Email : alivia_chen@mckinsey.com
* Date  ：May 3, 2017
* @version        
*/
public class PalnsThread extends Thread implements IConstants  {
	
	 private CountDownLatch count;  

	private int workerid;
	private PALNS palns;

	public int getWorkerid() {
		return workerid;
	}

	public void setWorkerid(int workerid) {
		this.workerid = workerid;
	}

	public PALNS getPalns() {
		return palns;
	}

	public void setPalns(PALNS palns) {
		this.palns = palns;
	}

	public PalnsThread(int workerid, PALNS palns,CountDownLatch count) {
		super();
		this.workerid = workerid;
		this.palns = palns;
		this.setCount(count);
	}

//	@Override
	public void run(){
		double temperature = palns.getConfig().getW()*palns.getCurrent().cost()/Math.log(2);
		for(int i =0 ;i < palns.getConfig().getNumIters() ; i++){
			if(palns.isQuit())
				return;
			
			IntPair pair = runImpl(temperature, workerid);
			adapt(workerid, i, pair.getChoice(), pair.getScoreIndex());
			if(i % 10 == 0){
				OutputPrinter.printLine("id: "+ workerid+ "\tbest: "+ ((Solution)palns.getBest()).cost()+"\t#"+ i/10);
				systemWebSocketHandler.sendMessageToUser(new TextMessage("id: "+ workerid+ "\tbest: "+ ((Solution)palns.getBest()).cost()+"\t#"+ i/10));
			}
			temperature *= palns.getConfig().getAlpha();
		}
		
		this.count.countDown();
	}
	
	private synchronized void adapt(int id, int i, int choice, Score scoreIndex) {
		palns.getOpsCounts().addAndGet(choice, 1);
		palns.getOpsScore().addAndGet(choice, scoreIndex.ordinal());
		
		if( i % (palns.getConfig().getSegment()/palns.getConfig().getCores()) == 0){
			palns.getWeights()[choice] = palns.getWeights()[choice]*(1-palns.getConfig().getDecay())+
					palns.getConfig().getDecay()*1.0*palns.getOpsScore().get(choice)/palns.getOpsCounts().get(choice);

			if( id == 0 ){
				palns.setOpsScore(new AtomicIntegerArray(palns.getWeights().length));
//				palns.setOpsCounts(new AtomicIntegerArray(palns.getWeights().length));
				for(int index =0 ;index < palns.getOpsCounts().length(); index++){
					palns.getOpsCounts().set(index, 1);
				}
			}
		}
	}

	private synchronized IntPair runImpl(double temperature, int id){
		double[] initWs = new double[palns.getWeights().length];
		for(int i = 0 ; i< palns.getWeights().length; i++){
			initWs[i] = palns.getWeights()[i];
		}
		
		int index = roulette(initWs);
		IRemoval rop = palns.getRemovalOps()[index/palns.getInsertionOps().length];
		IInsertion iop = palns.getInsertionOps()[index%palns.getInsertionOps().length];

		Score scoreIndex = null;

		ISolution tmp = palns.getCurrent().clone();
		tmp = rop.remove(tmp);
		tmp = iop.insert(tmp);
		tmp.calcCost();

		scoreIndex = accept(tmp,palns.getCurrent(),temperature);
		if(palns.getCurrent().cost() < palns.getBest().cost()){
			palns.setBest(palns.getCurrent());
			scoreIndex = Score.NEWBEST;
		}

		return new IntPair(index,scoreIndex);
	}


	private synchronized int roulette(double[] weights) {
		double[] cumsum = cumsum(weights);
		double random =new Random().nextDouble();
		double n = random*cumsum[cumsum.length-1];
		
		return search(cumsum,n);
	}

	private synchronized boolean getFlag(double[] cumsum,int h,double nn) {
		return cumsum[h] > nn;
	}

	private synchronized int search(double[] cumsum,double nn) {
		int i = 0;
		int j = cumsum.length;
		
		while(i < j){
			int h = i + (j-i)/2;
			if(!getFlag(cumsum,h,nn)){
				i = h + 1;
			}else{
				j = h;
			}
		}
		
		return i;
	}

	private synchronized double[] cumsum(double[] weights) {
		double[] r = new double[weights.length];
		double sum = 0;
		for(int i =0; i < weights.length ; i++){
			sum += weights[i];
			r[i] = sum;
		}
		return r;
	}

	private synchronized Score accept(ISolution newsol, ISolution cur, double temperature) {
		if(newsol.cost() > cur.cost()){
			palns.setCurrent(newsol);
			return Score.BETTER_THAN_CURRENT;
		}
		
		double prob = Math.exp(-(newsol.cost()-cur.cost())/temperature);
		if(new Random().nextDouble() <= prob){
			palns.setCurrent(newsol);
			return Score.ACCEPTED;
		}
		
		return Score.REJECTED;
	}

	public CountDownLatch getCount() {
		return count;
	}

	public void setCount(CountDownLatch count) {
		this.count = count;
	}

}
