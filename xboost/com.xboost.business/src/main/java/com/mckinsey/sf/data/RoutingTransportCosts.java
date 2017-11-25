package com.mckinsey.sf.data;
import java.io.Serializable;
/**   
*    
* Author：Alivia Chen   
* Email : alivia_chen@mckinsey.com
* Date  ：Apr 27, 2017 
* @version        
*/
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.xboost.pojo.SiteDist;
import com.xboost.service.SiteDistService;
import com.xboost.util.CascadeModelUtil;
import com.xboost.util.ShiroUtil;
import org.apache.commons.lang3.StringUtils;

import com.mckinsey.sf.constants.IConstants;
import com.mckinsey.sf.data.config.ITransportCosts;
import com.mckinsey.sf.printer.OutputPrinter;
import com.mckinsey.sf.utils.LocationUtils;

import javax.inject.Inject;

public class RoutingTransportCosts implements ITransportCosts,IConstants,Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2142357934793416214L;
	private Map<String,Double> distances = new HashMap<String,Double>();
	private Map<String,Double> time  = new HashMap<String,Double>();
	private Map<String,String> nearestNeighbors = new HashMap<String,String>();
	private double fixedStopTime;
	private double maxDistance;
	private double maxTime;
	private boolean isSymmetric;
	int distMode;
//	@Inject
//	private SiteDistService siteDistService;
	
	public RoutingTransportCosts(SiteDistService siteDistService, String distance, String nearest, double fixedStopTime, boolean isSymmetric,int distMode) {
		this.distances = new HashMap<String,Double>();
		this.maxDistance = 0;
		this.maxTime = 0;
		this.isSymmetric = isSymmetric;
		//read distance from file
		String splitBy = ",";
        String line;
//		this.fixedStopTime = fixedStopTime;
        this.distMode = distMode;
		if(distMode == 1){
			/*try {
				BufferedReader br = new BufferedReader(new FileReader(distance));
				line = br.readLine();
				while((line = br.readLine()) !=null){
		        	String[] row = line.split(splitBy);
		        	double dist = Double.parseDouble(row[2]);
		        	addTransportDistance(row[0], row[1], dist);
		        	
		        	double time = Double.parseDouble(row[3]);
		        	addTransportTime(row[0], row[1], time);
				}
				br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}*/
			//mod by geds
			//根据场景ID查询SiteDist
			Map map = new HashMap<String,Object>();
			map.put("scenariosId", ShiroUtil.getOpenScenariosId());
			List<SiteDist> siteDistsList = siteDistService.findSiteDistByScenariosId(map);
			for(int i=0;i<siteDistsList.size();i++){
				double dist = siteDistsList.get(i).getCarDistance();
				addTransportDistance(siteDistsList.get(i).getSiteCollect(), siteDistsList.get(i).getSiteDelivery(), dist);
				double time = siteDistsList.get(i).getDurationNightDelivery();
				addTransportTime(siteDistsList.get(i).getSiteCollect(), siteDistsList.get(i).getSiteDelivery(), time);
			}
		}
		
		if(distMode == 2){
			//TODO read distance2
//			To mod by geds
			OutputPrinter.printError("To mod by geds----- ");
//			try {
//				BufferedReader br = new BufferedReader(new FileReader(distance));
//				line = br.readLine();
//				while((line = br.readLine()) !=null){
//		        	String[] row = line.split(splitBy);
//					String loc = row[0];
//					String lonStr = row[1];
//					String latStr = row[2];
//
//					double lon = Double.parseDouble(lonStr);
//					double lat = Double.parseDouble(latStr);
//
//					Location location = new Location(loc,lon,lat);
//					Main.locs.put(loc, location);
//				}
//				br.close();
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
		}
		
	}
	
	public Map<String, Double> getDistances() {
		return distances;
	}
	public void setDistances(Map<String, Double> distances) {
		this.distances = distances;
	}
	public Map<String, String> getNearestNeighbors() {
		return nearestNeighbors;
	}
	public void setNearestNeighbors(Map<String, String> nearestNeighbors) {
		this.nearestNeighbors = nearestNeighbors;
	}
	public double getFixedStopTime() {
		return fixedStopTime;
	}
	public void setFixedStopTime(double fixedStopTime) {
		this.fixedStopTime = fixedStopTime;
	}
	public double getMaxDistance() {
		return maxDistance;
	}
	public void setMaxDistance(double maxDistance) {
		this.maxDistance = maxDistance;
	}
	public double getMaxTime() {
		return maxTime;
	}
	public void setMaxTime(double maxTime) {
		this.maxTime = maxTime;
	}
	public boolean isSymmetric() {
		return isSymmetric;
	}
	public void setSymmetric(boolean isSymmetric) {
		this.isSymmetric = isSymmetric;
	}
	
	public double calcDistance(String from, String to) {
		int mode = this.distMode;
		double dist = 0;
		
		if(StringUtils.isEmpty(from) || StringUtils.isEmpty(to)){
			return dist;
		}
		if(from.equals(to)){
			return dist;
		}
		
		if(mode == 1){
			String routeId = from+"->"+to;
			if(distances.containsKey(routeId)){
				dist = distances.get(routeId);
			}else if(isSymmetric){
				routeId = to+"->"+from;
				if(distances.containsKey(routeId)){
					dist =  distances.get(routeId);
				}
			}
			else{
				OutputPrinter.printError("Could not find distance between: "+from+" & "+to);
			}
		}
		
		if(mode == 2){
			Location loc1 = CascadeModelUtil.locs.get(from);
			Location loc2 = CascadeModelUtil.locs.get(to);
			dist = LocationUtils.getDistance(loc1.getLat(), loc1.getLon(), loc2.getLat(), loc2.getLon())/1000;
		}

		return dist;
	}
	
	public double calcTime(Car car, String from, String to) {
		if(StringUtils.isEmpty(from) || StringUtils.isEmpty(to)){
			return 0;
		}
		if(from.equals(to)){
			return 0;
		}
		
		int mode = this.distMode;
		double curTime  = 0;
		if(mode == 1 ){
			String routeId = from+"->"+to;
			if(time.containsKey(routeId)){
				curTime =  time.get(routeId);
			}else if(isSymmetric){
				routeId = to+"->"+from;
				if(time.containsKey(routeId)){
					curTime =  time.get(routeId);
				}
			}else{
				System.out.println("Could not find time between: "+from+" & "+to);
			}
		}else if(mode ==2){
			double dist = calcDistance(from,to);
			curTime = dist/car.getVelocity()*60;
		}
		
		curTime += fixedStopTime;
		
		return curTime;
	}
	
	private void addTransportDistance(String from,String to,double dist){
		String routeId = from+"->"+to;
		this.distances.put(routeId,dist);
		if(isSymmetric){
			routeId = to+"->"+from;
			this.distances.put(routeId,dist);
		}
		maxDistance = Math.max(dist, maxDistance);
	}
	
	private void addTransportTime(String from, String to, double time) {
		String routeId = from+"->"+to;
		this.time.put(routeId,time);
		if(isSymmetric){
			routeId = to+"->"+from;
			this.distances.put(routeId,time);
		}
	}
	
	public boolean isNearestNeighbors(String from,String to){
		if(StringUtils.isEmpty(from) || StringUtils.isEmpty(to)){
			return true;
		}
		
		String nearesr = this.nearestNeighbors.get(from);
		if(to.equals(nearesr)){
			return true;
		}else{
			return false;
		}
	}

}
