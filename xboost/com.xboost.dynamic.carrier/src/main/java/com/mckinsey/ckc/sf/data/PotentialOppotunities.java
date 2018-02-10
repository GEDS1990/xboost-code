package com.mckinsey.ckc.sf.data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class PotentialOppotunities {
	private int sizeN;
	private List<Opportunity> oppotunities;
	private double maxNDistance;

	public PotentialOppotunities(int topN) {
		this.sizeN = topN;
		oppotunities = new ArrayList<Opportunity>();
		this.maxNDistance = Double.MAX_VALUE;
	}

	public int getSize() {
		return oppotunities.size();
	}

	public List<Opportunity> getOppotunities() {
		return oppotunities;
	}

	public boolean canInsert(Opportunity oppo) {
		if (oppotunities.size() < sizeN || oppo.getCurrentDistance() <= maxNDistance) {
			return true;
		}
		return false;
	}

	public void addOppotunities(Opportunity oppo) {
		// insert new oppotunity
		oppotunities.add(oppo);
		
		// sort list
		Collections.sort(oppotunities, new Comparator<Opportunity>() {
			@Override
			public int compare(Opportunity oppo1, Opportunity oppo2) {
				if (oppo1.getCurrentDistance() < oppo2.getCurrentDistance()) {
					return 1;
				} else if (oppo1.getCurrentDistance() == oppo2.getCurrentDistance()) {
					return 0;
				}
				return -1;
			}
		});
		
		if (oppotunities.size() > sizeN) {
				// popupMax oppotunities
					int length = oppotunities.size();
					int maxTotalCount = 0;
					for(int i =0;i <length ;i++){
						if(oppotunities.get(i).getCurrentDistance() == maxNDistance){
							maxTotalCount ++;
						}
					}
					if(length-maxTotalCount >= sizeN){
						//remove max dist
						for(int i =0;i <length ;i++){
							if(oppotunities.get(i).getCurrentDistance() == maxNDistance){
								oppotunities.remove(i);
								i--;
								length--;
							}
						}
					}
			
		}
		
		// update max distance
		maxNDistance = oppotunities.get(0).getCurrentDistance();
	}

	public void setOppotunities(List<Opportunity> oppotunities) {
		this.oppotunities = oppotunities;
	}

	public double getMaxNDistance() {
		return maxNDistance;
	}

	public void setMaxNDistance(double maxNDistance) {
		this.maxNDistance = maxNDistance;
	}

	public static void main(String[] args) {
		PotentialOppotunities potentialOppo = new PotentialOppotunities(3);
		Opportunity oppo1 = new Opportunity();
		oppo1.setCurrentDistance(10.2);
		if (potentialOppo.canInsert(oppo1))
			potentialOppo.addOppotunities(oppo1);
		Opportunity oppo3 = new Opportunity();
		oppo3.setCurrentDistance(12.222);
		if (potentialOppo.canInsert(oppo3))
			potentialOppo.addOppotunities(oppo3);
		Opportunity oppo2 = new Opportunity();
		oppo2.setCurrentDistance(9.2222);
		if (potentialOppo.canInsert(oppo2))
			potentialOppo.addOppotunities(oppo2);
		
		Opportunity oppo4 = new Opportunity();
		oppo4.setCurrentDistance(5.2222);
		if (potentialOppo.canInsert(oppo4))
			potentialOppo.addOppotunities(oppo4);
		if (potentialOppo.canInsert(oppo2))
			potentialOppo.addOppotunities(oppo2);
		Opportunity oppo6 = new Opportunity();
		oppo6.setCurrentDistance(3.2222);
		if (potentialOppo.canInsert(oppo6))
			potentialOppo.addOppotunities(oppo6);
		for (Opportunity oppo : potentialOppo.getOppotunities()) {
			System.out.println(oppo.getCurrentDistance());

		}
		System.out.println(potentialOppo.getMaxNDistance());
	}

}
