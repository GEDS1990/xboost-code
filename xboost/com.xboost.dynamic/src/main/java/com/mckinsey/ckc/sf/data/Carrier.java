package com.mckinsey.ckc.sf.data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

//import org.apache.logging.log4j.LogManager;
//import org.apache.logging.log4j.Logger;

import com.mckinsey.ckc.sf.constants.IConstants;
import com.mckinsey.ckc.sf.main.Main;
import com.mckinsey.ckc.sf.restful.data.MoveResponse;
import com.mckinsey.ckc.sf.utils.Tools;

public class Carrier implements IConstants {

	// private static Logger log = LogManager.getLogger(Carrier.class);

	private int carrierID; // carrier ID
	private int groupID; // carrier ID
	private Coordinate currentPos; // carrier current position
	private Coordinate destPos; // current moving direction
	private double speed;
	private double moveToNextTime; // time need to move to next point
	private double cumulativeDistance; // cumulative travel distance
	private HashMap<Integer, Parcel> parcelList;
	private int sleepTurns;
	private double avgUrgency;
	private int carrierType; // normal = 1, swot = 2
	private Coordinate initialPos;
	private PotentialOppotunities potentialOppo;
	private double carrierThetha;
	private double meanToDest;
	public int oppoNSize;
	public int pickUpSize;
	public int deliverSize;

	public Carrier() {
		this.carrierID = 0;
		this.groupID = 0;
		this.initialPos = new Coordinate(0, 0);
		this.currentPos = new Coordinate(0, 0);
		this.destPos = new Coordinate(0, 0);
		this.speed = 0;
		this.moveToNextTime = 0;
		this.cumulativeDistance = 0;
		this.parcelList = new HashMap<Integer, Parcel>();
		this.sleepTurns = 0;
		this.avgUrgency = 0;
		this.potentialOppo = new PotentialOppotunities(INTEREST_TOP_RANK);
		this.carrierThetha = 361;
	}

	
	public double getMeanToDest() {
		return meanToDest;
	}


	public void setMeanToDest(double meanToDest) {
		this.meanToDest = meanToDest;
	}


	public double getCarrierThetha() {
		return carrierThetha;
	}

	public void setCarrierThetha(double carrierThetha) {
		this.carrierThetha = carrierThetha;
	}

	public int getCarrierID() {
		return carrierID;
	}

	public void setCarrierID(int carrierID) {
		this.carrierID = carrierID;
	}

	public int getGroupID() {
		return groupID;
	}

	public void setGroupID(int groupID) {
		this.groupID = groupID;
	}

	public double getMoveToNextTime() {
		return moveToNextTime;
	}

	public void setMoveToNextTime(double moveToNextTime) {
		this.moveToNextTime = moveToNextTime;
	}

	public double getCumulativeDistance() {
		return cumulativeDistance;
	}

	public void setCumulativeDistance(double cumulativeDistance) {
		this.cumulativeDistance = cumulativeDistance;
	}

	public HashMap<Integer, Parcel> getParcelList() {
		return parcelList;
	}

	public void setParcelList(HashMap<Integer, Parcel> parcelList) {
		this.parcelList = parcelList;
	}

	public void addParcelList(Parcel newParcel) {
		if (parcelList == null) {
			parcelList = new HashMap<Integer, Parcel>();
		}
		this.parcelList.put(newParcel.getParcelID(), newParcel);
	}

	public int getSleepTurns() {
		return sleepTurns;
	}

	public void setSleepTurns(int sleepTurns) {
		this.sleepTurns = sleepTurns;
	}

	public Coordinate getCurrentPos() {
		return currentPos;
	}

	public void setCurrentPos(Coordinate currentPos) {
		this.currentPos = currentPos;
	}

	public Coordinate getDestPos() {
		return destPos;
	}

	public void setDestPos(Coordinate destPos) {
		this.destPos = destPos;
	}

	public void stand() {
		System.out.println("Carrier " + this.carrierID + " stands still.");
	}

	public double getSpeed() {
		return speed;
	}

	public void setSpeed(double speed) {
		this.speed = speed;
	}

	public double getAvgUrgency() {
		return avgUrgency;
	}

	public void setAvgUrgency(double avgUrgency) {
		this.avgUrgency = avgUrgency;
	}

	public int getCarrierType() {
		return carrierType;
	}

	public void setCarrierType(int carrierType) {
		this.carrierType = carrierType;
	}

	public Coordinate getInitialPos() {
		return initialPos;
	}

	public void setInitialPos(Coordinate initialPos) {
		this.initialPos = initialPos;
	}

	public PotentialOppotunities getPotentialOppo() {
		return potentialOppo;
	}

	public void setPotentialOppo(PotentialOppotunities potentialOppo) {
		this.potentialOppo = potentialOppo;
	}

	public void addPotentialOppo(Opportunity oppo) {
		this.potentialOppo.addOppotunities(oppo);
	}

	public void move(int timeId, double moveTime) {
		double timeToDest = Math.sqrt(Math.pow(destPos.getX() - currentPos.getX(), 2) + Math.pow(destPos.getY() - currentPos.getY(), 2)) * DISTANCE_INFLATOR / speed;
		Coordinate newPos = null;
		if (moveTime >= timeToDest) {
			newPos = this.destPos;
			cumulativeDistance += timeToDest * speed;
		} else {
			newPos = this.currentPos.addCoordinate((destPos.minusCoordinate(currentPos).multiply(TIME_UNIT / timeToDest)));
			cumulativeDistance += moveTime * speed;
		}
		currentPos = newPos;

		// System.out.println("carrier "+carrierID+" has moved for "+moveTime+"
		// minutes");
		// System.out.println("from
		// ("+oldPosition.getX()+","+oldPosition.getY()+") to
		// ("+newPos.getX()+","+newPos.getY()+")");
		// System.out.println("with "+parcelList.size()+" types of parcel");
		HashMap<Integer, MoveResponse> moveAction = new HashMap<Integer, MoveResponse>();
		MoveResponse mv = new MoveResponse();
		mv.setNextLong(newPos.getX());
		mv.setNextLat(newPos.getY());
		List<Parcel> parcels = new ArrayList<Parcel>();
		for (Map.Entry<Integer, Parcel> parcel : parcelList.entrySet()) {
			parcels.add(parcel.getValue());
		}
		mv.setPickupList(parcels);
		mv.setSpendTime(moveTime);
		moveAction.put(carrierID, mv);
		Main.responseList.put(timeId, moveAction);
	}

	public void pickUp(int pickupID, int timeid) {
		// System.out.println("carrier "+carrierID+" picked up parcel
		// "+pickupID+" at location
		// ("+currentPos.getX()+","+currentPos.getY()+")");
		// update parcel
		Parcel selectedParcel = Main.parcelMap.get(pickupID);
		selectedParcel.setCurrentCarrierID(carrierID);
		selectedParcel.setParcelDeliverStatus(PICKUP);
		selectedParcel.setPickupTime(timeid);
		if (Main.oppotunities.size() > 0) {
			for (Opportunity op : Main.oppotunities) {
				if (op.getParcel().getParcelID() == pickupID) {
					Parcel opParcel = op.getParcel();
					opParcel.setCurrentCarrierID(carrierID);
					opParcel.setParcelDeliverStatus(PICKUP);
					selectedParcel.setParcelVolumn(opParcel.getParcelVolumn());
					selectedParcel.setDestPos(opParcel.getDestPos());
					selectedParcel.setMinTimeToDest(opParcel.getMinTimeToDest());
				}
			}
		}
		// reorder current parcels on the carrier
		parcelList.put(pickupID, selectedParcel);
		
		double minMinToDest = Double.MAX_VALUE;
		Parcel minParcel = null;
		for (Map.Entry<Integer, Parcel> parcelentry : getParcelList().entrySet()) {
			Parcel p = parcelentry.getValue();
			if (p.getMinTimeToDest() < minMinToDest) {
				minMinToDest = p.getMinTimeToDest();
				minParcel = p;
			}
		}
		setDestPos(minParcel.getDestPos());
	}

	public void dropOff(Parcel parcel, int t) {
		// update parcel
		// update time to destination for parcel list when carriers drop off
		// parcels
		parcelList.remove(parcel.getParcelID());

		// update opportunity
		if (Main.oppotunities.size() > 0) {
			for (Opportunity oppo : Main.oppotunities) {
				if (oppo.getParcel().getParcelID() == parcel.getParcelID()) {
					oppo.getParcel().setCurrPos(currentPos);
					oppo.getParcel().setParcelDeliverStatus(DELIVERED);
					oppo.getParcel().setMinTimeToDest(0);
					oppo.getParcel().setCurrentCarrierID(carrierID);
					oppo.getParcel().setDeliverTime(t);
				}
			}
		}

		// update parcelMap
		Main.parcelMap.get(parcel.getParcelID()).setCurrPos(currentPos);
		Main.parcelMap.get(parcel.getParcelID()).setParcelDeliverStatus(DELIVERED);
		Main.parcelMap.get(parcel.getParcelID()).setMinTimeToDest(0);
		Main.parcelMap.get(parcel.getParcelID()).setCurrentCarrierID(carrierID);
		Main.parcelMap.get(parcel.getParcelID()).setDeliverTime(t);
	}

	public void think(List<Opportunity> oppotunitiesN, int timeId) {

		int Nopt = oppotunitiesN.size();
		double maxUrgency = Double.MIN_VALUE;
		int maxUrgencyIndex = 0;
		for (Opportunity op : oppotunitiesN) {
			if (op.getOptParcelUrgency() > maxUrgency) {
				maxUrgency = op.getOptParcelUrgency();
				maxUrgencyIndex = op.getParcel().getParcelID();
			} else if (op.getOptParcelUrgency() == maxUrgency) {
				if (op.getParcel().getParcelID() < maxUrgencyIndex) {
					maxUrgencyIndex = op.getParcel().getParcelID();
				}
			}
		}

		// thinking logic:
		// case 1: there are some current parcel on the carrier
		// case 2: no parcel on the carrier, but ther are some opportunity
		// around
		// case 3: no parcel on the carrier, no opportunity around
		int scenario = 1;
		if ((parcelList.size() + Nopt) == 0) {
			scenario = 3;
		} else if (Nopt == 0) {
			scenario = 1;
		} else if (parcelList.size() == 0) {
			scenario = 2;
		} else if (avgUrgency < maxUrgency) {
			scenario = 2;
		} else {
			scenario = 1;
		}

		Parcel topParcel = null;
		double minTimeRemaining = Double.MAX_VALUE;
		for (Map.Entry<Integer, Parcel> parcelentry : parcelList.entrySet()) {
			Parcel p = parcelentry.getValue();
			double timeRemaining = Main.parcelMap.get(p.getParcelID()).getDeadline() - timeId;
			if ((p.getMinTimeToDest() - timeRemaining * 0.2) < minTimeRemaining) {
				minTimeRemaining = p.getMinTimeToDest() - timeRemaining * 0.2;
				topParcel = p;
			}

		}
		// #make choices based on scores
		// #if case 1, set the carrier's destination to the destination of its
		// closest item
		// #if case 2, set the carrier's destination to the origination of its
		// best opportunity
		// #if case 3, set the carrier's destination to carrier's current
		// position, i.e. stay & wait

		if (scenario == 1) {
			destPos = topParcel.getDestPos();
		} else if (scenario == 2) {
			// change carriers' destination
			destPos = Main.parcelMap.get(maxUrgencyIndex).getOrigPos();

			// if this carrier has reserved some other parcel, cancel that
			// reservation
			for (Entry<Integer, Parcel> pEntry : Main.parcelMap.entrySet()) {
				Parcel p = pEntry.getValue();
				if (p.getParcelDeliverStatus() == UNDEMAND && p.getCurrentCarrierID() == carrierID) {
					p.setParcelDeliverStatus(0);
					p.setCurrentCarrierID(0);
					for (Opportunity oppo : Main.oppotunities) {
						if (p.getParcelID() == oppo.getParcel().getParcelID()) {
							oppo.getParcel().setParcelDeliverStatus(0);
							oppo.getParcel().setCurrentCarrierID(0);
						}
					}
				}
			}

			// change the object parcel to reserved (status=-1), so that it is
			// no longer available to other carrier
			Main.parcelMap.get(maxUrgencyIndex).setParcelDeliverStatus(UNDEMAND);
			Main.parcelMap.get(maxUrgencyIndex).setCurrentCarrierID(carrierID);
			for (Opportunity oppo : Main.oppotunities) {
				if (maxUrgencyIndex == oppo.getParcel().getParcelID()) {
					oppo.getParcel().setParcelDeliverStatus(UNDEMAND);
					oppo.getParcel().setCurrentCarrierID(carrierID);
				}
			}
		} else {
			// stand();
		}
	}

	public void thinkDropOff(List<GroupInfo> groupList, List<Opportunity> oppotunitiesN, int timeId) {
		int Nopt = oppotunitiesN.size();
		double maxUrgency = Double.MIN_VALUE;
		int maxUrgencyIndex = 0;
		for (Opportunity op : oppotunitiesN) {
			if (op.getOptParcelUrgency() > maxUrgency) {
				maxUrgency = op.getOptParcelUrgency();
				maxUrgencyIndex = op.getParcel().getParcelID();
			} else if (op.getOptParcelUrgency() == maxUrgency) {
				if (op.getParcel().getParcelID() < maxUrgencyIndex) {
					maxUrgencyIndex = op.getParcel().getParcelID();
				}
			}
		}

		int endTime = 0;
		for (int i = 0; i < groupList.size(); i++) {
			if (groupList.get(i).getPreGroupId() == carrierID) {
				endTime = groupList.get(i).getDestDeactivateTime() - 3;
				System.out.println("drop off end time is " + endTime);
			}
		}

		// #thinking logic:
		// #case 1: there are some current parcel on the carrier
		// #case 2: no parcel on the carrier, but ther are some opportunity
		// around
		// #case 3: no parcel on the carrier, no opportunity around
		int option = 1;
		if ((parcelList.size() + Nopt) == 0) {
			option = 3;
		} else if (Nopt == 0) {
			option = 1;
		} else if (parcelList.size() == 0) {
			option = 2;
		}

		Parcel topParcel = null;
		double minTimeRemaining = Double.MAX_VALUE;
		for (Map.Entry<Integer, Parcel> parcelentry : parcelList.entrySet()) {
			Parcel p = parcelentry.getValue();
			double timeRemaining = Main.parcelMap.get(p.getParcelID()).getDeadline() - timeId;
			if ((p.getMinTimeToDest() - timeRemaining) < minTimeRemaining) {
				minTimeRemaining = p.getMinTimeToDest() - timeRemaining;
				topParcel = p;
			}
		}

		// #make choices based on scores
		// #if case 1, set the carrier's destination to the destination of its
		// closest item
		// #if case 2, set the carrier's destination to the origination of its
		// best opportunity
		// #if case 3, set the carrier's destination to carrier's current
		// position, i.e. stay & wait

		if (option == 1) {
			setDestPos(topParcel.getDestPos());
		} else if (option == 2) {
			// change carriers' destination
			setDestPos(Main.parcelMap.get(maxUrgencyIndex).getOrigPos());

			// if this carrier has reserved some other parcel, cancel that
			// reservation
			for (Entry<Integer, Parcel> pEntry : Main.parcelMap.entrySet()) {
				Parcel p = pEntry.getValue();
				if (p.getParcelDeliverStatus() == UNDEMAND && p.getCurrentCarrierID() == carrierID) {
					p.setParcelDeliverStatus(0);
					p.setCurrentCarrierID(0);
					for (Opportunity oppo : Main.oppotunities) {
						if (p.getParcelID() == oppo.getParcel().getParcelID()) {
							oppo.getParcel().setParcelDeliverStatus(0);
							oppo.getParcel().setCurrentCarrierID(0);
						}
					}
				}
			}

			// change the object parcel to reserved (status=-1), so that it is
			// no longer available to other carrier
			Main.parcelMap.get(maxUrgencyIndex).setParcelDeliverStatus(UNDEMAND);
			Main.parcelMap.get(maxUrgencyIndex).setCurrentCarrierID(carrierID);
			for (Opportunity oppo : Main.oppotunities) {
				if (maxUrgencyIndex == oppo.getParcel().getParcelID()) {
					oppo.getParcel().setParcelDeliverStatus(UNDEMAND);
					oppo.getParcel().setCurrentCarrierID(carrierID);
				}
			}
		} else {

		}
	}

	public void thinkPickUp(List<GroupInfo> groupList, List<Opportunity> oppotunitiesN, int timeId) {
		int Nopt = oppotunitiesN.size();
		double maxUrgency = Double.MIN_VALUE;
		int maxUrgencyIndex = 0;
		for (Opportunity op : oppotunitiesN) {
			if (op.getOptParcelUrgency() > maxUrgency) {
				maxUrgency = op.getOptParcelUrgency();
				maxUrgencyIndex = op.getParcel().getParcelID();
			} else if (op.getOptParcelUrgency() == maxUrgency) {
				if (op.getParcel().getParcelID() < maxUrgencyIndex) {
					maxUrgencyIndex = op.getParcel().getParcelID();
				}
			}
		}

		int endTime = 0;
		for (int i = 0; i < groupList.size(); i++) {

			if (groupList.get(i).getPreGroupId() == Math.abs(groupID)) {
				endTime = groupList.get(i).getDestDeactivateTime() - 3;
				// System.out.println("pick up end time is " + endTime);
			}
		}

		// #thinking logic:
		// #case 1: the capacity is full or time is up
		// #case 2: the capacity is not full or time is not up, and ther are
		// some opportunity around
		// #case 3: the capacity is not full or time is not up, no opportunity
		// around
		double totalVolume = 0;
		for (Map.Entry<Integer, Parcel> parcelentry : parcelList.entrySet()) {
			Parcel p = parcelentry.getValue();
			totalVolume += p.getParcelVolumn();
		}
		int option = 2;
		if (parcelList.size() == 0) {
			if (Nopt == 0) {
				option = 3;
			} else {
				option = 2;
			}
		} else if (totalVolume >= CARRIER_CAPACITY_PICKUP || timeId >= endTime || Nopt == 0) {
			option = 1;
		}

		Parcel topParcel = null;
		double minTimeRemaining = Double.MAX_VALUE;
		for (Map.Entry<Integer, Parcel> parcelentry : parcelList.entrySet()) {
			Parcel p = parcelentry.getValue();
			double timeRemaining = Main.parcelMap.get(p.getParcelID()).getDeadline() - timeId;
			if ((p.getMinTimeToDest() - timeRemaining) < minTimeRemaining) {
				minTimeRemaining = p.getMinTimeToDest() - timeRemaining;
				topParcel = p;
			}
		}

		// #make choices based on scores
		// #if case 1, set the carrier's destination to the destination of its
		// closest item
		// #if case 2, set the carrier's destination to the origination of its
		// best opportunity
		// #if case 3, set the carrier's destination to carrier's current
		// position, i.e. stay & wait

		if (option == 1) {
			setDestPos(topParcel.getDestPos());
		} else if (option == 2) {
			// change carriers' destination
			setDestPos(Main.parcelMap.get(maxUrgencyIndex).getOrigPos());

			// if this carrier has reserved some other parcel, cancel that
			// reservation
			for (Entry<Integer, Parcel> pEntry : Main.parcelMap.entrySet()) {
				Parcel p = pEntry.getValue();
				if (p.getParcelDeliverStatus() == UNDEMAND && p.getCurrentCarrierID() == carrierID) {
					p.setParcelDeliverStatus(0);
					p.setCurrentCarrierID(0);
					for (Opportunity oppo : Main.oppotunities) {
						if (p.getParcelID() == oppo.getParcel().getParcelID()) {
							oppo.getParcel().setParcelDeliverStatus(0);
							oppo.getParcel().setCurrentCarrierID(0);
						}
					}
				}
			}

			// change the object parcel to reserved (status=-1), so that it is
			// no longer available to other carrier
			Main.parcelMap.get(maxUrgencyIndex).setParcelDeliverStatus(UNDEMAND);
			Main.parcelMap.get(maxUrgencyIndex).setCurrentCarrierID(carrierID);
			for (Opportunity oppo : Main.oppotunities) {
				if (maxUrgencyIndex == oppo.getParcel().getParcelID()) {
					oppo.getParcel().setParcelDeliverStatus(UNDEMAND);
					oppo.getParcel().setCurrentCarrierID(carrierID);
				}
			}
		} else {

		}
	}
	
	
	//Synchronize think
	//TODO
	public static void synchronize_think(int timeId){
		
		List<UrgencyScoreCalculator> urgencyList = buildUrgency(Main.oppotunities, timeId);
		Set<Integer> exsitedCarriers = new HashSet<Integer>();
		Set<Integer> exsitedParcels = new HashSet<Integer>();
		
		//initialize position
		for(Map.Entry<Integer, Carrier> carrier : Main.carrierMap.entrySet()){
			Carrier c = carrier.getValue();
			if((c.oppoNSize+c.parcelList.size()) == 0){
				//stand still
			}else if(c.getParcelList().size() != 0){
				Parcel topParcel = null;
				double minTimeRemaining = Double.MAX_VALUE;
				for (Map.Entry<Integer, Parcel> parcelentry : c.parcelList.entrySet()) {
					Parcel p = parcelentry.getValue();
					double timeRemaining = Main.parcelMap.get(p.getParcelID()).getDeadline() - timeId;
					if ((p.getMinTimeToDest() - timeRemaining * 0.2) < minTimeRemaining) {
						minTimeRemaining = p.getMinTimeToDest() - timeRemaining * 0.2;
						topParcel = p;
					}

				}
				c.setDestPos(topParcel.getDestPos());
			}
		}
		
		for(UrgencyScoreCalculator calculator : urgencyList){
			// when urgency score below 0, loop end
			if(calculator.getUrgencyScore() < 0){
				break;
			}
			
			Opportunity opportunity = calculator.opportunity;
			Parcel parcel = opportunity.getParcel();
			Carrier carrier = opportunity.getCarrier();
			
			//if parcel or carrier has already been selected, skip current opportunity
			if(exsitedCarriers.contains(carrier.getCarrierID()) || exsitedParcels.contains(parcel.getParcelID())){
				continue;
			}
			
			Main.carrierMap.get(carrier.getCarrierID()).setDestPos(parcel.getOrigPos());
			// if this carrier has reserved some other parcel, cancel that
			// reservation
			for (Entry<Integer, Parcel> pEntry : Main.parcelMap.entrySet()) {
				Parcel p = pEntry.getValue();
				if (p.getParcelDeliverStatus() == UNDEMAND && p.getCurrentCarrierID() == carrier.getCarrierID()) {
					p.setParcelDeliverStatus(0);
					p.setCurrentCarrierID(0);
					for (Opportunity oppo : Main.oppotunities) {
						if (p.getParcelID() == oppo.getParcel().getParcelID()) {
							oppo.getParcel().setParcelDeliverStatus(0);
							oppo.getParcel().setCurrentCarrierID(0);
						}
					}
				}
			}

			// change the object parcel to reserved (status=-1), so that it is
			// no longer available to other carrier
			Main.parcelMap.get(parcel.getParcelID()).setParcelDeliverStatus(UNDEMAND);
			Main.parcelMap.get(parcel.getParcelID()).setCurrentCarrierID(carrier.getCarrierID());
			for (Opportunity oppo : Main.oppotunities) {
				if (parcel.getParcelID() == oppo.getParcel().getParcelID()) {
					oppo.getParcel().setParcelDeliverStatus(UNDEMAND);
					oppo.getParcel().setCurrentCarrierID(carrier.getCarrierID());
				}
			}
			
			//add parcel id and carrier id into set
			exsitedCarriers.add(carrier.getCarrierID());
			exsitedParcels.add(parcel.getParcelID());
		}
	}
	
	private static List<UrgencyScoreCalculator> buildUrgency(List<Opportunity> opportunityList, int timeId) {
		//1.
		List<UrgencyScoreCalculator> res = new ArrayList<UrgencyScoreCalculator>();
		for (Opportunity opportunity :  opportunityList) {
			Parcel parcel = opportunity.getParcel();
			if(parcel.getParcelDeliverStatus() != PICKUP && parcel.getParcelDeliverStatus() != DELIVERED){
				UrgencyScoreCalculator usc = new UrgencyScoreCalculator(opportunity);
				usc.calculateScore(timeId);
//				if (usc.getUrgencyScore() < 0){
//					continue;
//				}
				res.add(usc);
			}
		}
		
		//2. 排序
		Collections.sort(res, new Comparator<UrgencyScoreCalculator>(){

			@Override
			public int compare(UrgencyScoreCalculator arg0, UrgencyScoreCalculator arg1) {
				if (arg0.getUrgencyScore() > arg1.getUrgencyScore()) {
					return -1;
				} else if (arg0.getUrgencyScore() == arg1.getUrgencyScore()) {
					return 0;
				} else {
					return 1;
				}
			}
			
		});
		
		printUrgencyMatrix(res);
		return res;
		
	}
	
	
	private static void printUrgencyMatrix(List<UrgencyScoreCalculator> res) {
		for(UrgencyScoreCalculator cal : res){
			System.out.println("carrierID:"+cal.opportunity.getCarrier().getCarrierID()
					+"\tparcelID:"+cal.opportunity.getParcel().getParcelID()
					+"\tScore:"+formatDouble(cal.getUrgencyScore())
					+"\tdistTime:"+formatDouble(cal.distTime)
					+"\tincTime:"+formatDouble(cal.incremenTime)
					+"\tpUrgency:"+formatDouble(cal.pUrgencyTime)
					+"\tcUrgency:"+formatDouble(cal.cUrgencyTime));
		}
	
	}
	
	public static String formatDouble(double d) {
		return String.format("%.2f", d);
	}
	
	
	//================================== static inner class ========================
	public static class UrgencyScoreCalculator {
		private Opportunity opportunity;
		private double urgencyScore;
		
		private static  double a = -1000000;
		private static  double b = -1000000;
		private static  double c = -1;
		private static  double d = 10000000;
		
		double distTime;
		double incremenTime;
		double cUrgencyTime;
		double pUrgencyTime;
		
		public UrgencyScoreCalculator(Opportunity opportunity) {
			this.opportunity = opportunity;
		}
		
		public void calculateScore( int timeId){
			Carrier carrier = this.opportunity.getCarrier();
			Parcel parcel =this.opportunity.getParcel();
			double speed = carrier.getSpeed();//currentCarrier.getSpeed()
			
			//calculate meanToDest and Angle
			double carrierNTheta = 361;
			double meanParcelLong = 0;
			double meanParcelLat = 0;
			double meanToDest = 0;
			int nParcel = carrier.getParcelList().size();
			if (carrier.getParcelList().size() != 0) {
				double totalLong = 0;
				double totalLat = 0;
				double totalTime = 0;
				for (Map.Entry<Integer, Parcel> parcelentry : carrier.getParcelList().entrySet()) {
					Parcel p = parcelentry.getValue();
					totalLong += p.getDestPos().getX();
					totalLat += p.getDestPos().getY();
					totalTime += p.getMinTimeToDest();
				}
				meanParcelLong = totalLong / nParcel;
				meanParcelLat = totalLat / nParcel;
				meanToDest = totalTime / nParcel;
				carrierNTheta = 180 / Math.PI * Math.asin((meanParcelLat - carrier.getCurrentPos().getY())
				        / Math.sqrt(Math.pow(meanParcelLong - carrier.getCurrentPos().getX(), 2) + Math.pow(meanParcelLat - carrier.getCurrentPos().getY(), 2)));
			}
			double angleBetween = 0;
			if (carrier.getCarrierThetha() <= 360) {
				angleBetween = Math.abs(parcel.getParcelTheta() - carrierNTheta);
			}
			angleBetween = Math.min(360 - angleBetween, angleBetween);

			//calculate increTime
			incremenTime = Tools.calculateThirdSide(parcel.getMinTimeToDest(), meanToDest, angleBetween)
			        + parcel.getMinTimeToDest() - meanToDest;
			distTime = Math.sqrt(Math.pow(carrier.getCurrentPos().getX() - parcel.getCurrPos().getX(), 2)
							+ Math.pow(carrier.getCurrentPos().getY() - parcel.getCurrPos().getY(), 2)) * DISTANCE_INFLATOR/speed;
//			double incremenTime = Tools.calculateThirdSide(parcel.getMinTimeToDest(), 
//					carrier.getMeanToDest(), opportunity.getAngleBetween() ) + parcel.getMinTimeToDest() - carrier.getMeanToDest();
//			if (carrier.getParcelList().size() == 0) {
//				cUrgencyTime = -999999;
//			} else if (carrier.getParcelList().size() >= (carrier.getGroupID() == 0 ? CARRIER_CAPACITY : SWOT_CAPACITY)) {
//				cUrgencyTime = Double.POSITIVE_INFINITY;
//			} else {
//				// mark:full loaded scenario
//				double minRemainingTime = Double.MAX_VALUE;
//				for (Map.Entry<Integer, Parcel> parcelentry : Main.parcelMap.entrySet()) {
//					Parcel p = parcelentry.getValue();
//					if (p.getParcelDeliverStatus() == PICKUP && p.getCurrentCarrierID() == carrier.getCarrierID()) {
//						if ((p.getDeadline()) < minRemainingTime) {
//							minRemainingTime = p.getDeadline();
//						}
//					}
//				}
//				minRemainingTime -= timeId;
//				cUrgencyTime = (REQUIRED_SHIPPING_TIME - 2 - minRemainingTime)*TIME_UNIT;
//			}
			cUrgencyTime = carrier.getAvgUrgency();
			if(cUrgencyTime != -999999 && cUrgencyTime != Double.POSITIVE_INFINITY){
				cUrgencyTime = Math.log(cUrgencyTime)*TIME_UNIT;
			}
			pUrgencyTime = Math.log(opportunity.getOptParcelUrgency())*TIME_UNIT;
			this.urgencyScore = a*distTime + b*incremenTime + c*cUrgencyTime + d*pUrgencyTime ;
		}
		
		public double getUrgencyScore(){
			return this.urgencyScore;
		}
	}

}
