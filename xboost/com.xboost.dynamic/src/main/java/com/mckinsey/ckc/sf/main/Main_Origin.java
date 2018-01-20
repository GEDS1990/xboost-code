package com.mckinsey.ckc.sf.main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import com.mckinsey.ckc.sf.constants.IConstants;
import com.mckinsey.ckc.sf.data.Carrier;
import com.mckinsey.ckc.sf.data.CarrierRecords;
import com.mckinsey.ckc.sf.data.Coordinate;
import com.mckinsey.ckc.sf.data.Opportunity;
import com.mckinsey.ckc.sf.data.Parcel;
import com.mckinsey.ckc.sf.data.Points;
import com.mckinsey.ckc.sf.data.Result;
import com.mckinsey.ckc.sf.preparation.DataPreparation;
import com.mckinsey.ckc.sf.restful.data.MoveResponse;
import com.mckinsey.ckc.sf.utils.Tools;

public class Main_Origin implements IConstants{
	
//	public static HashMap<Integer,Parcel> parcelMap = new HashMap<Integer,Parcel>();;
	public static List<Parcel> deliverParcelMap;
	public static List<Opportunity> oppotunities = new ArrayList<Opportunity>();;
	public static HashMap<Integer,Carrier> carrierMap = new HashMap<Integer,Carrier>();
	public static HashMap<Integer,HashMap<Integer,MoveResponse>> responseList = new HashMap<Integer,HashMap<Integer,MoveResponse>>();
	public static double totalVolume = 0;
	public static int MAX_ANGLE0 = 60;
	
	public void calculate(){
		long start_time = System.currentTimeMillis();
		int carrierSpeedPerKmh = SPEED_RANGE[0];
		double carrierSpeedPerMm = carrierSpeedPerKmh*1000/60.0;
		
		//initialize parcel data
		DataPreparation db = new DataPreparation();
		db.loadParcelsFromDB(carrierSpeedPerMm);
		deliverParcelMap = new ArrayList<Parcel>();
		
		//initialize carrier data
		List<Points> pointsList = db.loadPoints();
		
		
		
		
		
		
		
		
		Random rand = new Random();
		for(int i=1;i <=N_CARRIER_RANGE;i++){
			Points point = pointsList.get(rand.nextInt(pointsList.size()));
			Carrier carrier = new Carrier();
			carrier.setCarrierID(i);
			carrier.setCurrentPos(point.getPosition());
			carrier.setInitialPos(point.getPosition());
			carrier.setSpeed(carrierSpeedPerMm);
			//normal carrier
			carrier.setGroupID(0);
			carrier.setCarrierType(NORMAL_CARRIER);
			carrierMap.put(i, carrier);
		}

		//initialize carrier records;
		List<CarrierRecords> recordsList = new ArrayList<CarrierRecords>();
		
		//start simulation
		for(int t=PICKING_START_TIME*60/TIME_UNIT; t<= PICKING_END_TIME*60/TIME_UNIT;t ++){
			//calculate each urgency for each carrier-parcel pair
			
			//SWOT team show up
			if(t == PICKING_START_TIME_SWOT*60/TIME_UNIT){
				for(int i=N_CARRIER_RANGE+1;i <=N_CARRIER_RANGE+N_SWOT_CARRIER;i++){
					Points point = pointsList.get(rand.nextInt(pointsList.size()));
					Carrier carrier = new Carrier();
					carrier.setCarrierID(i);
					carrier.setCurrentPos(point.getPosition());
					carrier.setInitialPos(point.getPosition());
					carrier.setSpeed(carrierSpeedPerMm);
					//swot carrier
					carrier.setGroupID(1);
					carrier.setDestPos(point.getPosition());
					carrier.setCarrierType(SWOT_CARRIER);
					carrierMap.put(i, carrier);
				}
			}
			
			//SWOT team end
			if(t == SWOT_END_TIME*60/TIME_UNIT){
				for(Map.Entry<Integer, Parcel> parcelEntry : Main.parcelMap.entrySet()){
					parcelEntry.getValue().setGroupID(0);
				}
				for(Map.Entry<Integer, Carrier> carrier : carrierMap.entrySet()){
					carrier.getValue().setGroupID(0);
				}
			}
			
			//max angle changes over time
			if(t > 230){
				MAX_ANGLE0 = 180;
			}
			int maxAngle = MAX_ANGLE0;
			if(t >= (DEMAND_CUT_TIME-OPEN_TIMING)*60/TIME_UNIT){
				maxAngle = 180;
			}
			
			HashMap<Integer,Parcel> parcelMapT = new HashMap<Integer,Parcel>();
			//identify urgent parcels
			for(Map.Entry<Integer, Parcel> parcelEntry : Main.parcelMap.entrySet()){
				Parcel parcel = parcelEntry.getValue();
				if(parcel.getParcelDeliverStatus() == UNATTENDED && (t < SWOT_END_TIME*60/TIME_UNIT) 
						&& ((t-parcel.getTimeId())>=URGENCY_LEAD_TIME*60/TIME_UNIT)){
					parcel.setGroupID(1);
				}
				//filter parcels occur before time t
				if((parcel.getParcelDeliverStatus() == UNATTENDED || parcel.getParcelDeliverStatus() == UNDEMAND) && parcel.getTimeId() <= t){
					parcelMapT.put(parcel.getParcelID(), parcel);
				}
			}
			
			//only select opportunity with lowest distances
			//initialize opportunities
			oppotunities.clear();
			for(Map.Entry<Integer, Carrier> carrier : carrierMap.entrySet()){
				Carrier c = carrier.getValue();
				c.getPotentialOppo().getOppotunities().clear();
				for(Map.Entry<Integer, Parcel> parcel : parcelMapT.entrySet()){
					Opportunity oppo = new Opportunity();
					Parcel p = parcel.getValue();
					if(c.getGroupID() == p.getGroupID() && p.getParcelDeliverStatus()!=PICKUP && p.getParcelDeliverStatus()!=DELIVERED){
						oppo.setCarrier(c);
						oppo.setParcel(p);
 						double currentDistance = Math.sqrt(Math.pow(c.getCurrentPos().getX()-p.getCurrPos().getX(), 2)+Math.pow(c.getCurrentPos().getY()-p.getCurrPos().getY(), 2))*DISTANCE_INFLATOR;
						oppo.setCurrentDistance(currentDistance);
						if(c.getPotentialOppo().canInsert(oppo)){
							c.addPotentialOppo(oppo);
						}
					}
				}
				
				//add potential to oppotunities_all
				for(Opportunity oppo : c.getPotentialOppo().getOppotunities()){
					Parcel p = oppo.getParcel();
					double destDistance = Math.sqrt(Math.pow(c.getDestPos().getX()-p.getDestPos().getX(), 2)+Math.pow(c.getDestPos().getY()-p.getDestPos().getY(), 2))*DISTANCE_INFLATOR;
					oppo.setDestDistance(destDistance);
					oppo.setOptParcelTimeRemaining(p.getDeadline()-t);
					oppo.setOptParcelUrgency(Math.exp(REQUIRED_SHIPPING_TIME-2-OPT_IMPORTANCE_RANGE[0]-oppo.getOptParcelTimeRemaining()));
					double parcelODDistance = Tools.getVectorLength(p.getDestPos(), p.getOrigPos())*DISTANCE_INFLATOR;
					oppo.setParcelODDistance(parcelODDistance);
					oppotunities.add(oppo);
				}
			}

//			System.out.println("Opportunity size:"+oppotunities.size());
		    //loop over carriers
			//drop off parcel
			//find most potential parcel
			//pickup all capable parcels
			//think how where to go next
			//move
			//update status for Carrier, Parcel, Opportunity
			
			for(Map.Entry<Integer, Carrier> carrier : carrierMap.entrySet()){
				
				Carrier currentCarrier = carrier.getValue();
				//reinitialize oppolist
				List<Parcel> delivers = new ArrayList<Parcel>();
				//when current position equals package destination, dropoff the parcel
				for(Map.Entry<Integer, Parcel> parcelentry : Main.parcelMap.entrySet()){
					Parcel parcel = parcelentry.getValue();
					if(parcel.getDestPos().equals(currentCarrier.getCurrentPos()) && parcel.getCurrentCarrierID() == currentCarrier.getCarrierID()){
						if(parcel.getParcelDeliverStatus() == PICKUP && parcel.getTimeId() <= t){
							delivers.add(parcel);
							deliverParcelMap.add(parcel);
						}
					}
				}
				
				//dropoff parcel
				for(Parcel parcel : delivers){
					currentCarrier.dropOff(parcel,t);
					Parcel par = Main.parcelMap.get(parcel.getParcelID());
					par.setCurrentCarrierID(currentCarrier.getCarrierID());
					par.setCurrPos(currentCarrier.getCurrentPos());
					par.setParcelDeliverStatus(DELIVERED);
					par.setDeliverTime(t);
					par.setMinTimeToDest(0);
				}
				
				//recalculate minToDest
				for(Map.Entry<Integer, Parcel> parcelentry : currentCarrier.getParcelList().entrySet()){
					Parcel parcel = parcelentry.getValue();
//					double minTimeToDest = Tools.getVectorLength(currentCarrier.getCurrentPos(), parcel.getDestPos())*DISTANCE_INFLATOR/currentCarrier.getSpeed();
					//TODO change on 3-31 for onTimeRate
					double minTimeToDest = Math.sqrt(Math.pow(currentCarrier.getCurrentPos().getX()-parcel.getDestPos().getX(), 2)+
							Math.pow(currentCarrier.getCurrentPos().getY()-parcel.getDestPos().getY(), 2))
							*DISTANCE_INFLATOR/currentCarrier.getSpeed();
					parcel.setMinTimeToDest(minTimeToDest);
					Main.parcelMap.get(parcel.getParcelID()).setMinTimeToDest(minTimeToDest);
				}
				
				//calculate carriers' theta
				//future optimize merge to up
				double carrierNTheta = 361;
				//TODO change for ontimerate
				double meanParcelLong = 0;
				double meanParcelLat = 0;
				double meanToDest = 0;
				int nParcel = currentCarrier.getParcelList().size();
				if(currentCarrier.getParcelList().size() != 0){
					double totalLong = 0;
					double totalLat = 0;
					double totalTime = 0;
					for(Map.Entry<Integer, Parcel> parcelentry : currentCarrier.getParcelList().entrySet()){
						Parcel parcel = parcelentry.getValue();
						totalLong += parcel.getDestPos().getX();
						totalLat += parcel.getDestPos().getY();
						totalTime += parcel.getMinTimeToDest();
					}
					meanParcelLong = totalLong/nParcel;
					meanParcelLat = totalLat/nParcel;
					meanToDest = totalTime/nParcel;
					carrierNTheta = 180/Math.PI*Math.asin((meanParcelLat-currentCarrier.getCurrentPos().getY())/Math.sqrt(Math.pow(meanParcelLong-currentCarrier.getCurrentPos().getX(), 2)+Math.pow(meanParcelLat-currentCarrier.getCurrentPos().getY(), 2)));
//					System.out.println("meanLong is "+meanParcelLong+",meanLat is "+meanParcelLat+",theta is "+carrierNTheta);
					
				}
				currentCarrier.setCarrierThetha(carrierNTheta);
				
		        //opportunity of this carrier
				List<Opportunity> oppotunitiesN = new ArrayList<Opportunity>();
				for(Opportunity oppo : oppotunities){
					if(oppo.getCarrier().getCarrierID() == currentCarrier.getCarrierID()){
						if(oppo.getParcel().getParcelDeliverStatus() ==UNATTENDED ||
								(oppo.getParcel().getParcelDeliverStatus() == UNDEMAND && oppo.getParcel().getCurrentCarrierID() == currentCarrier.getCarrierID())){
							double thetaBetween = 0;
							if(currentCarrier.getCarrierThetha()<=360){
								thetaBetween = Math.abs(oppo.getParcel().getParcelTheta()-carrierNTheta);
							}
							thetaBetween = Math.min(360-thetaBetween, thetaBetween);
							//TODO change for onTimeRate 3-31
							oppo.setAngleBetween(thetaBetween);
							if(thetaBetween <= OPT_MAX_ANGLE){
									oppotunitiesN.add(oppo);
							}
						}
					}
				}
//				System.out.println("carrier "+currentCarrier.getCarrierID()+" oppotunity size:"+oppotunitiesN.size());
				
				//pick up
				//TODO optimize
				List<Opportunity> pickupOppo1 = new ArrayList<Opportunity>();
				Set<Integer> pickupID = new HashSet<Integer>();
				if( t == PICKING_START_TIME*60/TIME_UNIT){
					double minToDest = Double.MIN_VALUE;
					Opportunity oppoMaxMinToDest = null;
					//TODO change for onTimeRate
					for(Opportunity oppo : oppotunitiesN){
						if(oppo.getParcel().getMinTimeToDest() > minToDest){
							minToDest = oppo.getParcel().getMinTimeToDest();
							oppoMaxMinToDest = oppo;
						}
					}
					if(oppoMaxMinToDest != null){
						if(oppoMaxMinToDest.getCurrentDistance() <= 0.001){
							pickupOppo1.add(oppoMaxMinToDest);
						}
					}
				}else if( currentCarrier.getParcelList().size() == 0 || oppotunitiesN.size() ==0 ){
					for(Opportunity oppo : oppotunitiesN){
						if(oppo.getCurrentDistance() <= 0.001){
							pickupOppo1.add(oppo);
						}
					}
				}else{
					//calculate angle between carrier's direction and parcel's direction
					Coordinate vector1 = new Coordinate(meanParcelLong-currentCarrier.getCurrentPos().getX(),
							meanParcelLat-currentCarrier.getCurrentPos().getY());
					for(Opportunity oppo : oppotunitiesN){
						if(oppo.getCurrentDistance() <= 0.001){
							double incremenTime = Tools.calculateThirdSide(oppo.getParcel().getMinTimeToDest(), meanToDest, oppo.getAngleBetween() )+
									oppo.getParcel().getMinTimeToDest()-meanToDest;
							oppo.setIncremenTime(incremenTime);
							//TODO change for onTimeRate
							double judgeTime = meanToDest*Math.pow(10,(t - PICKING_START_TIME*60.0/TIME_UNIT)/((PICKING_END_TIME*60.0/TIME_UNIT)-(PICKING_START_TIME*60.0/TIME_UNIT))-1 );
							//TODO bug maxAngle should be maxAngle0
							if(oppo.getAngleBetween() <= MAX_ANGLE0 || incremenTime <= judgeTime){
								pickupOppo1.add(oppo);
							}
						}
					}
				}
				
				for(Opportunity op:pickupOppo1){
					pickupID.add(op.getParcel().getParcelID());
				}
				
				for(Map.Entry<Integer, Parcel> parcelEntry : Main.parcelMap.entrySet()){
					Parcel abParcel = parcelEntry.getValue();
					if(abParcel.getOrigPos().equals(currentCarrier.getCurrentPos()) && abParcel.getParcelDeliverStatus() == UNDEMAND){
						pickupID.add(abParcel.getParcelID());
					}
				}
				
				Iterator<Integer> pickupIndex =  pickupID.iterator();
				while(pickupIndex.hasNext()){
					int currentPickID = pickupIndex.next();
//					System.out.println("carrier "+currentCarrier.getCarrierID()+" pickup :"+currentPickID);
					if(currentCarrier.getGroupID() == 0){
						if(currentCarrier.getParcelList().size() > CARRIER_CAPACITY){
							break;
						}
					}else{
						if(currentCarrier.getParcelList().size() > SWOT_CAPACITY){
							break;
						}
					}
					Main.parcelMap.get(currentPickID).setCurrentCarrierID(currentCarrier.getCarrierID());
					Main.parcelMap.get(currentPickID).setParcelDeliverStatus(PICKUP);
					Main.parcelMap.get(currentPickID).setPickupTime(t);
					currentCarrier.pickUp(currentPickID,t);
				}
				
				//TODO current no use code
//				if(currentCarrier.getSleepTurns() == 1){
//					currentCarrier.setSleepTurns(0);
//				}
//				
//				if((pickupID.size() + delivers.size()) >= 1){
//					currentCarrier.setSleepTurns(0);
//				}
				
				// carrier thinks and make choice
				// calculate current parcel's urgency
				if(currentCarrier.getParcelList().size() == 0){
					currentCarrier.setAvgUrgency(-999999);
				}else if(currentCarrier.getParcelList().size() >= (currentCarrier.getGroupID()==0?CARRIER_CAPACITY:SWOT_CAPACITY)){
					currentCarrier.setAvgUrgency(Double.POSITIVE_INFINITY);
				}else{
					// mark:full loaded scenario
					double minRemainingTime = Double.MAX_VALUE;
					for(Map.Entry<Integer, Parcel> parcelentry : Main.parcelMap.entrySet()){
						Parcel p = parcelentry.getValue();
						if(p.getParcelDeliverStatus() == PICKUP && p.getCurrentCarrierID() == currentCarrier.getCarrierID()){
							if((p.getDeadline()) < minRemainingTime){
								minRemainingTime = p.getDeadline();
							}
						}
					}
					minRemainingTime -= t;
					currentCarrier.setAvgUrgency(Math.exp(REQUIRED_SHIPPING_TIME-2-minRemainingTime));
				}

				List<Opportunity> oppotunitiesNFilter = new ArrayList<Opportunity>();
				for(int index = 0 ; index<oppotunitiesN.size();index++){
					if(oppotunitiesN.get(index).getCurrentDistance() > Math.pow(10,-5)){
						oppotunitiesNFilter.add(oppotunitiesN.get(index));
					}
				}
//				System.out.println("carrier "+currentCarrier.getCarrierID()+" oppotunity filter:"+oppotunitiesNFilter.size());
				//TODO 
				currentCarrier.think(oppotunitiesNFilter,t);
//				currentCarrier.think(oppotunitiesN, t, meanToDest);
				//take action based on think result
				currentCarrier.move(t,TIME_UNIT);
//				System.out.println("carrier "+currentCarrier.getCarrierID()+" after move is :"+currentCarrier.getDestPos().getX()+","+currentCarrier.getDestPos().getY());
				
				double volume = 0;
				for(Map.Entry<Integer, Parcel> parcelEntry : currentCarrier.getParcelList().entrySet()){
					volume += parcelEntry.getValue().getParcelVolumn();
				}
	         //record carrier track
			CarrierRecords currentRecord = new CarrierRecords(t, currentCarrier.getCarrierID(), currentCarrier.getGroupID(), 
					currentCarrier.getCurrentPos(), currentCarrier.getDestPos(),
					currentCarrier.getCumulativeDistance(), currentCarrier.getParcelList().size(), 
					volume, pickupID.size(), delivers.size());
			recordsList.add(currentRecord);
			}
			double totalPickUp = 0;
			double totalDelivered = 0;
			double totalOnTimeDelivered = 0;
			double totalUnattended = 0;
			double totalDemandUnshown = 0;
			for(Map.Entry<Integer, Parcel> parcel : Main.parcelMap.entrySet()){
				//recalculate minToDest
				//TODO change for onTime
				double minTimeToDest = Math.sqrt(Math.pow(parcel.getValue().getCurrPos().getX()-parcel.getValue().getDestPos().getX(), 2)+Math.pow(parcel.getValue().getCurrPos().getY()-parcel.getValue().getDestPos().getY(), 2))*DISTANCE_INFLATOR*1.0/carrierSpeedPerMm;
				parcel.getValue().setMinTimeToDest(minTimeToDest);
				if(parcel.getValue().getParcelVolumn()<0){
					System.err.println("parcel volume:"+parcel.getValue().getParcelVolumn());
				}
				if(parcel.getValue().getParcelDeliverStatus() == PICKUP){
					totalPickUp += parcel.getValue().getParcelVolumn();
				}else if(parcel.getValue().getParcelDeliverStatus() == DELIVERED){
					totalDelivered += parcel.getValue().getParcelVolumn();
					if(parcel.getValue().getDeliverTime() <= parcel.getValue().getDeadline()){
						totalOnTimeDelivered += parcel.getValue().getParcelVolumn();
					}
				}else if(parcel.getValue().getParcelDeliverStatus() <= 0 && parcel.getValue().getTimeId() <= t){
						totalUnattended += parcel.getValue().getParcelVolumn();
				}else if(parcel.getValue().getParcelDeliverStatus() <= 0 &&  parcel.getValue().getTimeId() > t){
					totalDemandUnshown += parcel.getValue().getParcelVolumn();
				}else{
					System.err.println("deliver status error:"+parcel.getValue().getParcelDeliverStatus());
				}
			}
			
			System.out.println("carrier number="+carrierMap.size()+", time="+t+", picked up "+
					totalPickUp+
                    ", delivered "+
                    totalDelivered+
                     ", ontime delivered "+
                     totalOnTimeDelivered+
                    ", unattended "+
                    totalUnattended+
                    ", demand unshown "+
                    totalDemandUnshown);

			
		}
		
		
		//statistical data
		int[] workingHour = new int[carrierMap.size()];
		int[] actualWorkingHour = new int[carrierMap.size()];
		double totalOntimeVolume = 0;
		double totalDistance = 0;
		double totalShippingTime = 0;
		double totalParcel = 0;
		double totalDeliverVolume = 0;
		int totalParcelPieces = 0;
		for(CarrierRecords record : recordsList){
			int carrierId = record.getCarrierID();
			if(record.getParcelType() > 0){
				actualWorkingHour[carrierId-1]++;
				if(record.getTimeID() > workingHour[carrierId-1]){
					workingHour[carrierId-1] = record.getTimeID();
				}
			}
			
			if(record.getTimeID() <= workingHour[carrierId-1]){
				totalParcel += record.getParcelVolume();
				totalParcelPieces ++;
			}
			
			if(record.getTimeID() == PICKING_END_TIME*60/TIME_UNIT){
				totalDistance += record.getDistanceTraveled();
			}
		}
		
		int totalWorkingHour = 0;
		int totalActualWorkingHour = 0;
		for(int i =0 ; i<carrierMap.size(); i++){
			totalWorkingHour += workingHour[i];
			totalActualWorkingHour += actualWorkingHour[i];
		}
		
		for(Parcel deliverP : deliverParcelMap){
			totalDeliverVolume += deliverP.getParcelVolumn();
			if(deliverP.getParcelDeliverStatus() == DELIVERED){
				totalShippingTime += (deliverP.getDeliverTime()-deliverP.getTimeId())*deliverP.getParcelVolumn();
			}
			if(deliverP.getDeadline() >= deliverP.getDeliverTime()){
				totalOntimeVolume += deliverP.getParcelVolumn();
			}
		}
		
		Result result = new Result(SPEED_RANGE[0],OPT_IMPORTANCE_RANGE[0],carrierMap.size(),Main.totalVolume, totalDeliverVolume	,
				totalDeliverVolume/carrierMap.size(),totalWorkingHour*TIME_UNIT/carrierMap.size()/60.0-10, totalActualWorkingHour/carrierMap.size()*TIME_UNIT/60.0, 
    		  totalParcel/totalParcelPieces, totalShippingTime*5/60.0/totalVolume, totalOntimeVolume/Main.totalVolume, totalDistance/carrierMap.size(), totalDistance);
		
		System.out.println("speed:\t"+result.getSpeed());
		System.out.println("importance:\t"+result.getOptImportance());
		System.out.println("carrier count:\t"+result.getnCarrier());
		System.out.println("total demand:\t"+result.getnParcel());
		System.out.println("deliver count:\t"+result.getnParcelDelivered());
		System.out.println("deliver per carrier:\t"+result.getParcelPerCarrier());
		System.out.println("avgWorkingHour:\t"+result.getAvgWorkingHour());
		System.out.println("avgActualWorkingHour:\t"+result.getAvgActualWorkingHour());
		System.out.println("avgParcelOnCarrier:\t"+result.getAvgParcelOnCarrier());
		System.out.println("avgShippingTime:\t"+result.getAvgWorkingHour());
		System.out.println("onTimeRate:\t"+result.getOnTimeRate());
		System.out.println("totalDistance:\t"+result.getTotalDistance());
		System.out.println("distancePerCarrier:\t"+result.getDistancePerCarrier());
		System.out.println("time used:\t"+(System.currentTimeMillis()-start_time));
	}

//	public static void main(String[] args){
//		Main_Origin main = new Main_Origin();
//		main.calculate();
//
//		//open for services
////		for(Entry<Integer, HashMap<Integer, MoveResponse>> response :responseList.entrySet()){
////			System.out.println("timeId: "+response.getKey());
////			HashMap<Integer,MoveResponse> temp = response.getValue();
////			for(Entry<Integer, MoveResponse> item : temp.entrySet()){
////				System.out.println("carrierID: "+item.getKey());
////				MoveResponse mv = item.getValue();
////				System.out.println(mv.getNextLat()+"-"+mv.getNextLong());
////			}
////		}
//	}
}
