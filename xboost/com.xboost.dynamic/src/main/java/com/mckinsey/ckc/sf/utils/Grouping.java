package com.mckinsey.ckc.sf.utils;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import static java.util.Comparator.comparing;

import com.mckinsey.ckc.sf.data.*;

public class Grouping {

    /**
     * @param args the command line arguments
     */
//    public static void main(String[] args) {
//
//        List<OD_Demand> studlist = new ArrayList<OD_Demand>();
//        studlist.add(new OD_Demand(1,1,2.3,2.3,2,2.3,2.3));
//        studlist.add(new OD_Demand(2,1,2.3,2.3,2,2.3,2.3));
//        studlist.add(new OD_Demand(3,1,2.3,2.3,2,2.3,2.3));
//        studlist.add(new OD_Demand(2,1,2.3,2.3,2,2.3,2.3));
//        studlist.add(new OD_Demand(1,1,3.5,2.3,2,2.3,2.3));
//        studlist.add(new OD_Demand(1,2,2.3,2.3,2,2.3,2.3));
//        //Code to group students by location
//        /*  Output should be Like below
//            ID : 1726   Name : John Location : New York
//            ID : 5223   Name : Michael  Location : New York
//            ID : 4321   Name : Max  Location : California
//            ID : 7765   Name : Sam  Location : California
//
//         */
//        System.out.println(threeLevelGrouping(studlist).get(2).get(1).get(2).size());
//      //covert od_demand to group
//		Map<Integer, Map<Integer, Map<Integer, List<OD_Demand>>>> demandsGrouping = Grouping.threeLevelGrouping(studlist);
//        for(Entry<Integer, Map<Integer, Map<Integer, List<OD_Demand>>>> demandEntry : demandsGrouping.entrySet()){
//			int timeID = demandEntry.getKey();
//			for(Entry<Integer, Map<Integer, List<OD_Demand>>> secondDemand : demandEntry.getValue().entrySet()){
//				int inboundId= secondDemand.getKey();
//				for(Entry<Integer, List<OD_Demand>> thirdDemand : secondDemand.getValue().entrySet()){
//					int outboundID = thirdDemand.getKey();
//					int demand_count = demandsGrouping.get(timeID).get(inboundId).get(outboundID).size();
//					System.out.println(timeID+"-"+inboundId+"-"+outboundID+"-"+demand_count);
//					System.out.println(timeID+"-"+inboundId+"-"+outboundID+"-"+thirdDemand.getValue().size());
//				}
//			}
//        }
//
//
//    }
    
    public static Map<Integer, Map<Integer, Map<Integer, List<OD_Demand>>>> threeLevelGrouping(List<OD_Demand> demands) {
    	Map<Integer, Map<Integer, Map<Integer, List<OD_Demand>>>> demandsGrouping = demands.stream()
    			.sorted(comparing(OD_Demand::getOutboundDepotId)
    			.thenComparing(OD_Demand::getInboundDepotId)
    			.thenComparing(OD_Demand::getTimeId))
    			.collect(
        		Collectors.groupingBy(OD_Demand::getTimeId,
                		groupByInboundOutbound()
                )
        );
        return demandsGrouping; 
    }

    private static Collector<OD_Demand, ?, Map<Integer, Map<Integer, List<OD_Demand>>>> groupByInboundOutbound() {
        return Collectors.groupingBy(OD_Demand::getInboundDepotId, Collectors.groupingBy(OD_Demand::getOutboundDepotId));
    }

	public static Map<Integer, Map<Integer, Map<Integer, List<OD_Demand>>>> threeLevelGrouping2(
			List<OD_Demand> demandList) {
		demandList.sort(Comparator.comparing(demand -> demand.getOutboundDepotId()));
		demandList.sort(Comparator.comparing(demand -> demand.getInboundDepotId()));
		demandList.sort(Comparator.comparing(demand -> demand.getTimeId()));
		return null;
	}
	
//	 private static List<JavaBean> getListByGroup(List<JavaBean> list) {
//	        List<JavaBean> result = new ArrayList<JavaBean>();
//	        Map<String, Integer> map = new HashMap<String, Integer>();
//	 
//	        for (JavaBean bean : list) {
//	            if (map.containsKey(bean.getGroup())) {
//	                map.put(bean.getGroup(), map.get(bean.getGroup()) + bean.getMoney());
//	            } else {
//	                map.put(bean.getGroup(), bean.getMoney());
//	            }
//	        }
//	        for (Entry<String, Integer> entry : map.entrySet()) {
//	            result.add(new JavaBean(entry.getKey(), entry.getValue()));
//	        }
//	        return result;
//	    }
	
}


