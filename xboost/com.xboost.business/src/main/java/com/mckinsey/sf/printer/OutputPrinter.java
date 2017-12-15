package com.mckinsey.sf.printer;

import java.text.DecimalFormat;
import java.util.*;
import java.util.Map.Entry;

import com.xboost.service.*;
import com.xboost.util.CascadeModelUtil;
import com.xboost.util.ShiroUtil;
import com.xboost.util.SpringBeanFactoryUtil;
import org.apache.commons.lang3.StringUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mckinsey.sf.constants.IConstants;
import com.mckinsey.sf.data.ActState;
import com.mckinsey.sf.data.Activity;
import com.mckinsey.sf.data.Job;
import com.mckinsey.sf.data.Route;
import com.mckinsey.sf.data.RouteState;
import com.mckinsey.sf.data.RoutingTransportCosts;
import com.mckinsey.sf.data.constraint.ConstraintState;
import com.mckinsey.sf.data.constraint.IConstraint;
import com.mckinsey.sf.data.solution.ArrInfo;
import com.mckinsey.sf.data.solution.JobInfo;
import com.mckinsey.sf.data.solution.Solution;
import com.mckinsey.sf.data.solution.StatInfo;
import org.joda.time.DateTime;
import org.springframework.web.socket.TextMessage;

/**   
*    
* Author：Alivia Chen   
* Email : alivia_chen@mckinsey.com
* Date  ：Apr 27, 2017
* @version        
*/
public class OutputPrinter implements IConstants {

//	@Inject
//	static ArrInfoService arrInfoService;
//	@Inject
//	static SolutionRouteService solutionRouteService;
	static SolutionRouteService solutionRouteService = (SolutionRouteService)SpringBeanFactoryUtil.getBean("solutionRouteService");
	static SolutionActivityService solutionActivityService = (SolutionActivityService)SpringBeanFactoryUtil.getBean("solutionActivityService");
	static ArrInfoService arrInfoService = (ArrInfoService)SpringBeanFactoryUtil.getBean("arrInfoService");
	static JobInfoService jobInfoService = (JobInfoService)SpringBeanFactoryUtil.getBean("jobInfoService");
	static StatInfoService statInfoService = (StatInfoService)SpringBeanFactoryUtil.getBean("statInfoService");

	public static void printLine(String str){
		System.out.println(str);
//		systemWebSocketHandler.sendMessageToUser(new TextMessage(str));
	}
	
	public static void printError(String str){
//		System.err.println(str);
	}

	public static void PrintSolution(Solution s) {
		HashMap<String,Double> routeCost = new HashMap<String,Double>();
		printLine("+---------------------------------------------------------------------------------------------------------------------------------------------------------------+");
		printLine("| Solution                                                         ");
		printLine("| Solution                                                         ");
//		systemWebSocketHandler.sendMessageToUser(new TextMessage("+-----------------------------+"));
		printLine("| Cost:"+ s.cost());
//		systemWebSocketHandler.sendMessageToUser( new TextMessage("| Cost:"+ s.cost()));
		printLine("+---------------------------------------------------------------------------------------------------------------------------------------------------------------+");
//		systemWebSocketHandler.sendMessageToUser( new TextMessage("+-----------------------------+"));
		for (Map.Entry<String, Route> entry : s.getRoutes().entrySet()) {  
			Route r = entry.getValue();
			printRoute(r,routeCost,s);
		}

		printLine("+-----------------------------+");
		systemWebSocketHandler.sendMessageToUser(new TextMessage("+-----------------------------+"));
//		printLine("+-------------total cost : ----------------"+total);
		
	}

	private static void printRoute(Route r, HashMap<String, Double> routeCost, Solution s) {
		ConstraintState cstat = s.getConstraintState(DEFAULT_CONSTRAINTS);
		RouteState rstat = cstat.getRouteState(r);

		printLine("|Vechile Type\t|"+r.getC().getType()+"\t|Start Loc.\t|"+r.getC().getStartLocation()+"\t|\n");
		printLine("|  Route     \t|"+r.getId()+"\t|Vehicle\t|"+r.getC().getId()+"\t|\n");
		printLine("| Total Dist.\t|"+rstat.getTotalDist()+"\t|Total Cost\t|"+rstat.getTotalCost()+"\t|\n");
		printLine("+-------------------------------------------------------------------------------------------------------------------------------------------------------------------------+");
		printLine("|type\t\t|location\t|job ID\t\t\t|arr_time\t|end_timev\t| activity ID\t|\r\n");
		printLine("+-------------------------------------------------------------------------------------------------------------------------------------------------------------------------+");
		systemWebSocketHandler.sendMessageToUser( new TextMessage("|Vechile Type\t|"+r.getC().getType()+"\t|Start Loc.\t|"+r.getC().getStartLocation()+"\t|\n"));
		systemWebSocketHandler.sendMessageToUser( new TextMessage("40%...."));
		systemWebSocketHandler.sendMessageToUser( new TextMessage("|  Route     \t|"+r.getId()+"\t|Vehicle\t|"+r.getC().getId()+"\t|\n"));
		systemWebSocketHandler.sendMessageToUser( new TextMessage("50%...."));
		systemWebSocketHandler.sendMessageToUser( new TextMessage("| Total Dist.\t|"+rstat.getTotalDist()+"\t|Total Cost\t|"+rstat.getTotalCost()+"\t|\n"));
		systemWebSocketHandler.sendMessageToUser( new TextMessage("55%...."));
		systemWebSocketHandler.sendMessageToUser( new TextMessage("+-------------------------------------------------------------------------------------------------------------------------------------------------------------------------+"));
		systemWebSocketHandler.sendMessageToUser( new TextMessage("60%...."));
		systemWebSocketHandler.sendMessageToUser( new TextMessage("|type\t\t|location\t|job ID\t\t\t|arr_time\t|end_timev\t| activity ID\t|\r\n"));
		systemWebSocketHandler.sendMessageToUser( new TextMessage("70%...."));
		systemWebSocketHandler.sendMessageToUser( new TextMessage("+-------------------------------------------------------------------------------------------------------------------------------------------------------------------------+"));
		systemWebSocketHandler.sendMessageToUser( new TextMessage("80%...."));
		if(r.getActs().size() != 0){
			Activity start = r.getActs().get(0);
			Activity end = r.getActs().get(r.getActs().size()-1);
			
			List<Activity> newActs = new ArrayList<Activity>();

			List<Activity> temp = new ArrayList<Activity>();
			String prevLocation = "";
			String prevType = "";
			//TODO
			for(Activity act : r.getActs()){
				if(!act.getLocation().equalsIgnoreCase(prevLocation)){
					
					if(temp.size()!=0){
						for(Activity e: temp){
							newActs.add(e);
						}
						temp.clear();
					}
					
					newActs.add(act);
					prevLocation = act.getLocation();
					prevType = act.getType();
				}else{
					if(act.getType().equalsIgnoreCase(prevType)){
						newActs.add(act);
						prevLocation = act.getLocation();
						prevType = act.getType();
					}else{
						temp.add(act);
					}
				}
			}
			
			for(Activity cur : newActs){
				boolean ok = eachAct(rstat,cur,getIndicator(cur,start,end));
				if(!ok){
					//TODO
//					return ok;
				}
			}
		}
		printLine("+-------------------------------------------------------------------------------------------------------------------------------------------------------------------------+");

	}
	
	private static int getIndicator(Activity cur, Activity start, Activity end){
		if(cur.getId().equals(start.getId())){
			return POS_START;
		}
		
		if(cur.getId().equals(end.getId())){
			return POS_END;
		}
		
		return POS_MIDDLE;
	}
	
	
	private static boolean eachAct(RouteState rstat, Activity act, int indicator){
		String type = act.getType();
		String loc = "";
		ActState actStat = rstat.getActStat(act.getId());
		double endTime = 0;
		double arrTime = 0;
		
		loc = act.getLocation();
		switch(indicator){
			case POS_START:
				break;
			case POS_MIDDLE :
			case POS_END :
				arrTime = actStat.getArrTime();
				endTime = actStat.getEndTime();
				break;
		
		}
		
		DecimalFormat df = new DecimalFormat("###0.00"); 
		String arrTimestr = df.format(arrTime);
		String endTimestr = df.format(endTime);
		
		if(type.equals(DELIVERY)){
			printLine("|"+type+"\t|"+loc+"\t\t|"+act.getJobId()+"\t|"+arrTimestr+"\t|"+endTimestr+"\t|"+act.getId()+"\t|\r\n");
			systemWebSocketHandler.sendMessageToUser( new TextMessage("|"+type+"\t|"+loc+"\t\t|"+act.getJobId()+"\t|"+arrTimestr+"\t|"+endTimestr+"\t|"+act.getId()+"\t|\r\n"));
		}else if(type.equals(START) || type.equals(END)){
			printLine("|"+type+"\t\t|"+loc+"\t\t|"+act.getJobId()+"\t\t\t|"+arrTimestr+"\t|"+endTimestr+"\t|"+act.getId()+"\t|\r\n");
			systemWebSocketHandler.sendMessageToUser( new TextMessage("|"+type+"\t\t|"+loc+"\t\t|"+act.getJobId()+"\t\t\t|"+arrTimestr+"\t|"+endTimestr+"\t|"+act.getId()+"\t|\r\n"));
		}else{
			printLine("|"+type+"\t\t|"+loc+"\t\t|"+act.getJobId()+"\t|"+arrTimestr+"\t|"+endTimestr+"\t|"+act.getId()+"\t|\r\n");
			systemWebSocketHandler.sendMessageToUser( new TextMessage("|"+type+"\t\t|"+loc+"\t\t|"+act.getJobId()+"\t|"+arrTimestr+"\t|"+endTimestr+"\t|"+act.getId()+"\t|\r\n"));
		}
		
		return true;
	}

	public static void PrintProblem(Solution s) {
		printLine("+---------------------------------------+");
		printLine("| Problem                               |");
		printLine("+---------------------------------------+");
		printLine("| Indicator     | Value                 |");
		printLine("+---------------------------------------+");
		
		
		DecimalFormat df = new DecimalFormat("######0.000");  
		double totalDis = 0;
		double totalAssigned = 0;
		double totalLoad = 0;
		double totalArrivalTime = 0;
		
		List<JobInfo> jobInfos = new ArrayList<JobInfo>();
		List<ArrInfo> arrInfos = new ArrayList<ArrInfo>();
		
		for(Route r : s.getRoutes().values()){
			ConstraintState cstat = s.getConstraintState(DEFAULT_CONSTRAINTS);
			RouteState rstat = cstat.getRouteState(r);
			
			totalDis += rstat.getTotalDist();
			//TODO change this!
			totalAssigned += r.getJobs().size();
//			totalLoad += r.getJobs().size()*1.0 / Integer.valueOf(r.getC().getDimensions().split(",")[0]);
			totalLoad += r.getJobs().size()*1.0 / Double.parseDouble(r.getC().getDimensions().split(",")[0]);

			String prevLoc = "";
			
			for(Activity cur : r.getActs()){
//				int indicator = Route.getIndicator(cur,  r.getActs().get(0), r.getActs().get(r.getActs().size()-1));
				ActState astat = rstat.getActStat(cur.getId());
				
				if(!prevLoc.equals(cur.getLocation())){
					ArrInfo arrInfo = new ArrInfo();
					arrInfo.setArrTime(astat.getArrTime());
					arrInfo.setEndTime(astat.getEndTime());
					arrInfo.setLocation(cur.getLocation());
					arrInfo.setRouteId(r.getId());
//					arrInfos.add(arrInfo);
					arrInfo.setScenariosId(ShiroUtil.getOpenScenariosId());
					arrInfo.setUserId(String.valueOf(ShiroUtil.getCurrentUserId()));
					arrInfo.setCreateTime(DateTime.now().toString("yyyy-MM-dd HH:mm"));
					arrInfoService.saveArrInfo(arrInfo);
					prevLoc = cur.getLocation();
				}
				
				if(cur.getType().equals(DELIVERY)){
					totalArrivalTime += astat.getArrTime();
				}
			}

			for( Job j : r.getJobs().values()){
				double duration = rstat.getActStates().get(j.getDelivery().getId()).getEndTime() - j.getPickup().getTw().getStart();
				JobInfo ji = new JobInfo();
				ji.setJobId(j.getId());
				ji.setFrom(j.getPickup().getLocation());
				ji.setTo(j.getDelivery().getLocation());
				ji.setDuration(duration);
//				jobInfos.add(ji);
				ji.setScenariosId(ShiroUtil.getOpenScenariosId());
				ji.setUserId(String.valueOf(ShiroUtil.getCurrentUserId()));
				ji.setCreateTime(DateTime.now().toString("yyyy-MM-dd HH:mm"));
				jobInfoService.saveJobInfo(ji);
			}

			if(CascadeModelUtil.carsMap.containsKey(r.getC().getType())){
				CascadeModelUtil.carsMap.put(r.getC().getType(), CascadeModelUtil.carsMap.get(r.getC().getType())+1);
			}else{
				CascadeModelUtil.carsMap.put(r.getC().getType(), 1);
			}

		}
		
		double avgDis = totalDis / s.getRoutes().size();
		double avgCost = s.cost()/ totalAssigned;
		double avgJobs =s.numAssignedJobs()*1.0 / s.getRoutes().size();
		double avgLoad = totalLoad*1.0 /s.getRoutes().size();
		double avgArrivalTime = totalArrivalTime*1.0 /s.numAssignedJobs();
		
		StatInfo stat = new StatInfo(s.cost(), s.numTotalJobs(), avgDis, avgCost, avgJobs, avgArrivalTime,
				avgLoad, s.getRoutes().size(),
				CascadeModelUtil.carsMap);
		
		ObjectMapper mapper = new ObjectMapper();
//		try {
//			mapper.writeValue(new File("src/main/resources/solution/arrInfos.json"), arrInfos);
//			mapper.writeValue(new File("src/main/resources/solution/jobInfos.json"), jobInfos);
//			mapper.writeValue(new File("src/main/resources/solution/stats.json"), stat);
//			转存到数据库
//			for(int i=0;i<arrInfos.size();i++){
//				arrInfos.get(i).setScenariosId(ShiroUtil.getOpenScenariosId());
//				arrInfos.get(i).setCreateTime(DateTime.now().toString("yyyy-MM-dd HH:mm"));
//				arrInfoService.saveArrInfo(arrInfos.get(i));
//			}
//			for(int i=0;i<jobInfos.size();i++){
//				jobInfos.get(i).setScenariosId(ShiroUtil.getOpenScenariosId());
//				jobInfos.get(i).setCreateTime(DateTime.now().toString("yyyy-MM-dd HH:mm"));
//				jobInfoService.saveJobInfo(jobInfos.get(i));
//			}
			stat.setScenariosId(ShiroUtil.getOpenScenariosId());
			stat.setUserId(String.valueOf(ShiroUtil.getCurrentUserId()));
			stat.setCreateTime(DateTime.now().toString("yyyy-MM-dd HH:mm"));
			statInfoService.saveStatInfo(stat);

//		} catch (JsonGenerationException e) {
//			e.printStackTrace();
//		} catch (JsonMappingException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}

		printLine( "|cost\t\t|"+ df.format(s.cost())+"\t\t|");
		systemWebSocketHandler.sendMessageToUser( new TextMessage("|cost\t\t|"+ df.format(s.cost())+"\t\t|"));
		printLine( "|noJobs\t\t|"+ df.format(s.numTotalJobs())+"\t\t\t|");
		systemWebSocketHandler.sendMessageToUser( new TextMessage("|noJobs\t\t|"+ df.format(s.numTotalJobs())+"\t\t\t|"));
		printLine( "|avgDis\t\t|"+ df.format(avgDis)+"\t\t|");
		systemWebSocketHandler.sendMessageToUser( new TextMessage("|avgDis\t\t|"+ df.format(avgDis)+"\t\t|"));
		printLine( "|avgCost\t|"+ df.format(avgCost)+"\t\t\t|");
		systemWebSocketHandler.sendMessageToUser( new TextMessage("|avgCost\t|"+ df.format(avgCost)+"\t\t\t|"));
		printLine( "|avgJobs\t|"+ df.format(avgJobs)+"\t\t\t|");
		systemWebSocketHandler.sendMessageToUser( new TextMessage("|avgJobs\t|"+ df.format(avgJobs)+"\t\t\t|"));
		printLine( "|avgTime\t|"+ df.format(avgArrivalTime)+"\t\t|");
		systemWebSocketHandler.sendMessageToUser( new TextMessage("|avgTime\t|"+ df.format(avgArrivalTime)+"\t\t|"));
		printLine( "|avgLoad\t|"+ df.format(avgLoad)+"\t\t\t|");
		systemWebSocketHandler.sendMessageToUser( new TextMessage("|avgLoad\t|"+ df.format(avgLoad)+"\t\t\t|"));
		printLine( "|numRoutes\t|"+ s.getRoutes().size()+"\t\t\t|");
		systemWebSocketHandler.sendMessageToUser( new TextMessage("|numRoutes\t|"+ s.getRoutes().size()+"\t\t\t|"));
		
		for(Entry<String, Integer> keyValue : CascadeModelUtil.carsMap.entrySet()){
			String key = keyValue.getKey();
			int value = keyValue.getValue();
			
			printLine( "|"+key+"\t|"+ (CascadeModelUtil.carsMap.containsKey(key)?value:0)+"\t\t\t|");
			systemWebSocketHandler.sendMessageToUser( new TextMessage("|"+key+"\t|"+ (CascadeModelUtil.carsMap.containsKey(key)?value:0)+"\t\t\t|"));


		}
		
		for(IConstraint c : s.getConstraints()){
			printLine("Constraint\t|"+c.getName()+"\t|");
			systemWebSocketHandler.sendMessageToUser( new TextMessage("Constraint\t|"+c.getName()+"\t|"));
		}
		
		printLine("+---------------------------------------+");
		systemWebSocketHandler.sendMessageToUser( new TextMessage("+---------------------------------------+"));
		
	}

	public static void PrintUnassigned(Solution s) {
		printLine("+-----------------------------+");
		printLine("| Unassigned                  |");
		printLine("+-----------------------------+");
		systemWebSocketHandler.sendMessageToUser( new TextMessage("+-----------------------------+"));
		systemWebSocketHandler.sendMessageToUser( new TextMessage("| Unassigned                  |"));
		systemWebSocketHandler.sendMessageToUser( new TextMessage("+-----------------------------+"));
		for (Map.Entry<String, Job> entry : s.getUnassigned().entrySet()) {  
			Job j = entry.getValue();
			printLine("| "+j.getId()+"\t | "+j.getPickup().getLocation()+" - "+j.getDelivery().getLocation()+"\t |\n");
			systemWebSocketHandler.sendMessageToUser( new TextMessage("| "+j.getId()+"\t | "+j.getPickup().getLocation()+" - "+j.getDelivery().getLocation()+"\t |\n"));
		}
	}

	public static void PrintRoutes(Solution s) {
		int minStops = Integer.MAX_VALUE;
		int maxStops = Integer.MIN_VALUE;
		int totalStops = 0;
		
		for (Map.Entry<String, Route> entry : s.getRoutes().entrySet()) {  
			Route route = entry.getValue();
			
			String prev = "";
			int maxStopsCur = 0;
			StringBuilder routeTracer = new StringBuilder();
			
			for(Activity cur :route.getActs()){
				if(!cur.getLocation().equals(prev)  && !StringUtils.isEmpty(cur.getLocation()) ){
					prev = cur.getLocation();
					maxStopsCur ++ ;
					routeTracer.append(prev);
					routeTracer.append("-");
				}
			}
			
			printLine("-----------------------------");
			printLine("route id:"+route.getId());
			printLine("route start time:"+route.getActs().get(1).getTw().getStart());
			printLine("route trace:"+routeTracer.toString());
			printLine("route max stops:"+maxStopsCur);
			systemWebSocketHandler.sendMessageToUser( new TextMessage("+-----------------------------+"));
			systemWebSocketHandler.sendMessageToUser( new TextMessage("route id:"+route.getId()));
			systemWebSocketHandler.sendMessageToUser( new TextMessage("route start time:"+route.getActs().get(1).getTw().getStart()));
			systemWebSocketHandler.sendMessageToUser( new TextMessage("route trace:"+routeTracer.toString()));
			systemWebSocketHandler.sendMessageToUser( new TextMessage("route max stops:"+maxStopsCur));
			
			if(maxStopsCur < minStops){
				minStops = maxStopsCur;
			}
			
			if(maxStopsCur > maxStops){
				maxStops = maxStopsCur;
			}
			
			totalStops += maxStopsCur;
			
		}
		double meanStops = 0.0;
		if(s.getRoutes().size()>0){

			meanStops = totalStops/s.getRoutes().size();
		}
		printLine("total max stops:"+maxStops);
		printLine("total min stops:"+minStops);
		printLine("total mean stops:"+meanStops);
		systemWebSocketHandler.sendMessageToUser( new TextMessage("total max stops:"+maxStops));
		systemWebSocketHandler.sendMessageToUser( new TextMessage("total min stops:"+minStops));
		systemWebSocketHandler.sendMessageToUser( new TextMessage("total mean stops:"+meanStops));
	}
	
	public static void writeStandardOutputToExcel(Solution s, RoutingTransportCosts transportCost) {
//		SolutionRouteService solutionRouteService = new SolutionRouteService();
		com.xboost.pojo.Route routePojo = new com.xboost.pojo.Route();
		com.xboost.pojo.Activity activityPojo = new com.xboost.pojo.Activity();
//		String fileName = "src/main/resources/标准串点输出.xls";

		systemWebSocketHandler.sendMessageToUser(new TextMessage("删除该场景的旧数据...."));
		solutionRouteService.delByScenariosId(Integer.parseInt(ShiroUtil.getOpenScenariosId()));//删除该场景的旧数据
		solutionActivityService.delByScenariosId(Integer.parseInt(ShiroUtil.getOpenScenariosId()));
		systemWebSocketHandler.sendMessageToUser(new TextMessage("删除该场景的旧数据成功"));

//		Workbook wb = null;
//		OutputStream out = null;
		try {
//			wb = new HSSFWorkbook();
//			Sheet sheet = wb.createSheet("车辆");
//			Sheet sheet2 = wb.createSheet("货物");
			int count = 0;
			int count2 = 0;
			int routeCount = 1;

//			Row row = sheet.createRow(count++);
//			row.createCell(0).setCellValue("total cost:");
//			row.createCell(1).setCellValue(s.cost());


//			Row rr3 = sheet.createRow(count++);
//			rr3.createCell(0).setCellValue("车辆编号");
//			rr3.createCell(1).setCellValue("车型");
//			rr3.createCell(2).setCellValue("出车网点-收车网点");
//			rr3.createCell(3).setCellValue("停靠点顺序");
//			rr3.createCell(4).setCellValue("当前网点");
//			rr3.createCell(5).setCellValue("操作");
//			rr3.createCell(6).setCellValue("装货目的地代码");
//			rr3.createCell(7).setCellValue("装货票数");
//			rr3.createCell(8).setCellValue("到达本网点时间");
//			rr3.createCell(9).setCellValue("离开本网点时间");
//			rr3.createCell(10).setCellValue("卸货目的地代码");
//			rr3.createCell(11).setCellValue("卸货票数");
//			rr3.createCell(12).setCellValue("下一个停靠网点代码");
//			rr3.createCell(13).setCellValue("到下一个停靠点运行里程");
//			rr3.createCell(14).setCellValue("车上货物");

			//sheet2

//			Row rr4 = sheet2.createRow(count2++);
//			rr4.createCell(0).setCellValue("寄件网点");
//			rr4.createCell(1).setCellValue("派件网点");
//			rr4.createCell(2).setCellValue("车辆编号");
//			rr4.createCell(3).setCellValue("发车时间");
//			rr4.createCell(4).setCellValue("到车时间");
//			rr4.createCell(5).setCellValue("票数");


			//write route
//			double totalLoadRate = 0;

			systemWebSocketHandler.sendMessageToUser(new TextMessage("新增数据...."));
			for (Map.Entry<String, Route> entry : s.getRoutes().entrySet()) {
				Route r = entry.getValue();
				ConstraintState cstat = s.getConstraintState(DEFAULT_CONSTRAINTS);
				RouteState rstat = cstat.getRouteState(r);

				HashSet<String> currentLoc = new HashSet<String>();

				String prevLoc = "";
				if(r.getActs().size() != 0){
					Activity start = r.getActs().get(0);
					Activity end = r.getActs().get(r.getActs().size()-1);
					List<Activity> newActs = new ArrayList<Activity>();

					List<Activity> temp = new ArrayList<Activity>();
					String prevLocation = "";
					String prevType = "";

					for(Activity act : r.getActs()){
						if(!act.getLocation().equalsIgnoreCase(prevLocation)){

							if(temp.size()!=0){
								for(Activity e: temp){
									newActs.add(e);
								}
								temp.clear();
							}

							newActs.add(act);
							prevLocation = act.getLocation();
							prevType = act.getType();
						}else{
							if(act.getType().equalsIgnoreCase(prevType)){
								newActs.add(act);
								prevLocation = act.getLocation();
								prevType = act.getType();
							}else{
								temp.add(act);
							}
						}
					}
					int sequence = 0;
					for(int index = 0;index <newActs.size()-1;index++){
						
						Activity cur = newActs.get(index);
						int indicator = getIndicator(cur,start,end);
						if(index ==newActs.size()-1){
							break;
						}
						if(index ==0 ){
							continue;
						}
						
						String type = cur.getType();
//						String loc = "";
						ActState actStat = rstat.getActStat(cur.getId());
						double endTime = 0;
						double arrTime = 0;
						
						String jobId = cur.getJobId();
						
						switch(indicator){
							case POS_START:
								break;
							case POS_MIDDLE :
							case POS_END :
								arrTime = actStat.getArrTime();
								endTime = actStat.getEndTime();
								break;
						
						}
						
						DecimalFormat df = new DecimalFormat("###0.00"); 
						
//						Row rr = sheet.createRow(count++);
						String carType = r.getC().getType();
						
//						rr.createCell(0).setCellValue(routeCount);
						routePojo.setRouteCount(String.valueOf(routeCount));
//						rr.createCell(1).setCellValue(carType);
						routePojo.setCarType(carType);
						routePojo.setCarName(carType +"-"+ String.valueOf(UUID.randomUUID()));
//						rr.createCell(2).setCellValue(newActs.get(1).getLocation()+"-"+newActs.get(newActs.size()-2).getLocation());
						routePojo.setLocation(newActs.get(1).getLocation()+"-"+newActs.get(newActs.size()-2).getLocation());
//						rr.createCell(4).setCellValue(cur.getLocation());
						routePojo.setCurLoc(cur.getLocation());

						if(cur.getLocation().equalsIgnoreCase(prevLoc)){
//							rr.createCell(3).setCellValue(sequence);
							routePojo.setSequence(String.valueOf(sequence));
						}else{
//							rr.createCell(3).setCellValue(++sequence);
							routePojo.setSequence(String.valueOf(++sequence));
						}
						prevLoc = cur.getLocation();
						
						//TODO 发车时间
//						if(index == 1){
//						rr.createCell(9).setCellValue(arrTime);
//						}else{
//						rr.createCell(8).setCellValue(Math.max(arrTime, CascadeModelUtil.totalJobs.get(jobId).getPickup().getTw().getStart()));
						routePojo.setArrTime(String.valueOf(Math.max(arrTime, CascadeModelUtil.totalJobs.get(jobId).getPickup().getTw().getStart())));
//						rr.createCell(9).setCellValue(endTime);
						routePojo.setEndTime(String.valueOf(endTime));
//						}
						if("PICKUP".equals(type)){
//							rr.createCell(5).setCellValue(type);
							routePojo.setType(type);
							StringBuilder sbLoc = new StringBuilder();
							StringBuilder sbVol = new StringBuilder();
							int j = index;
							int count1 = 0;
							for(;j<newActs.size();j++){
								Activity curJ = newActs.get(j);
								if(curJ.getLocation().equalsIgnoreCase(cur.getLocation())){
									if(type.equals(curJ.getType())){
										count1 ++;
										sbLoc.append(CascadeModelUtil.totalJobs.get(curJ.getJobId()).getDelivery().getLocation()+"/");
										sbVol.append(CascadeModelUtil.totalJobs.get(curJ.getJobId()).getDimensions()[0]+"/");
										currentLoc.add(CascadeModelUtil.totalJobs.get(curJ.getJobId()).getDelivery().getLocation());
										
										//TODO: update sheet2
//										Row rrr4 = sheet2.createRow(count2++);
//										rrr4.createCell(0).setCellValue(CascadeModelUtil.totalJobs.get(curJ.getJobId()).getPickup().getLocation());
										activityPojo.setPickupLoc(CascadeModelUtil.totalJobs.get(curJ.getJobId()).getPickup().getLocation());
//										rrr4.createCell(1).setCellValue(CascadeModelUtil.totalJobs.get(curJ.getJobId()).getDelivery().getLocation());
										activityPojo.setDeliveryLoc(CascadeModelUtil.totalJobs.get(curJ.getJobId()).getDelivery().getLocation());
//										rrr4.createCell(2).setCellValue(routeCount);
										activityPojo.setRouteCount(String.valueOf(routeCount));
										//TODO
//										rrr4.createCell(3).setCellValue(endTime);
										activityPojo.setEndTime(String.valueOf(endTime));
										for(Activity act : newActs){
											if("DELIVER".equalsIgnoreCase(act.getType()) && 
													act.getJobId().equalsIgnoreCase(curJ.getJobId())){
//												rrr4.createCell(4).setCellValue(rstat.getActStat(act.getId()).getArrTime());
												activityPojo.setArrTime(String.valueOf(rstat.getActStat(act.getId()).getArrTime()));
											}
											
										}
										
//										rrr4.createCell(5).setCellValue(CascadeModelUtil.totalJobs.get(curJ.getJobId()).getDimensions()[0]);
										activityPojo.setVol(String.valueOf(CascadeModelUtil.totalJobs.get(curJ.getJobId()).getDimensions()[0]));
									}
									//update sheet2 end
									activityPojo.setScenariosId(ShiroUtil.getOpenScenariosId());
									activityPojo.setUserId(String.valueOf(ShiroUtil.getCurrentUserId()));
									systemWebSocketHandler.sendMessageToUser(new TextMessage("增加新数据...."));
									solutionActivityService.addActivity(activityPojo);
									systemWebSocketHandler.sendMessageToUser(new TextMessage("增加新数据成功"));
								}else{
									break;
								}
							}
							index += count1-1;
//							rr.createCell(6).setCellValue(sbLoc.toString().substring(0,sbLoc.toString().length()-1));
							routePojo.setSbLoc(sbLoc.toString().substring(0,sbLoc.toString().length()-1));
//							rr.createCell(7).setCellValue(sbVol.toString().substring(0,sbVol.toString().length()-1));
							routePojo.setSbVol(sbVol.toString().substring(0,sbVol.toString().length()-1));
//							rr.createCell(6).setCellValue(Main.totalJobs.get(cur.getJobId()).getDelivery().getLocation());
//							rr.createCell(7).setCellValue(Main.totalJobs.get(cur.getJobId()).getDimensions()[0]);
							
						}
						
						
						if("DELIVER".equals(type)){
//							rr.createCell(5).setCellValue(type);
							routePojo.setType(type);
							StringBuilder sbLoc = new StringBuilder();
							StringBuilder sbVol = new StringBuilder();
							int j = index;
							int count1 = 0;
							for(;j<newActs.size();j++){
								Activity curJ = newActs.get(j);
								if(curJ.getLocation().equalsIgnoreCase(cur.getLocation())){
									if(type.equals(curJ.getType())){
										count1 ++;
										sbLoc.append(CascadeModelUtil.totalJobs.get(curJ.getJobId()).getDelivery().getLocation()+"/");
										sbVol.append(CascadeModelUtil.totalJobs.get(curJ.getJobId()).getDimensions()[0]+"/");
									}
								}else{
									break;
								}
							}
							index += count1-1;
//							rr.createCell(10).setCellValue(cur.getLocation());
							routePojo.setUnloadLoc(cur.getLocation());
							try{
								currentLoc.remove(cur.getLocation());
							}catch (Exception e){
								//TODO
							}
//							rr.createCell(11).setCellValue(sbVol.toString().substring(0,sbVol.toString().length()-1));
							routePojo.setUnloadVol(sbVol.toString().substring(0,sbVol.toString().length()-1));
//							rr.createCell(10).setCellValue(Main.totalJobs.get(cur.getJobId()).getDelivery().getLocation());
//							rr.createCell(11).setCellValue(Main.totalJobs.get(cur.getJobId()).getDimensions()[0]);
						}
						
						if(index == newActs.size()-1){
							index = newActs.size() -2;
						}
						Activity nextCur = newActs.get(index+1);
//						rr.createCell(12).setCellValue(nextCur.getLocation());
						routePojo.setNextCurLoc(nextCur.getLocation());
//						rr.createCell(13).setCellValue(df.format(transportCost.calcDistance(cur.getLocation(), nextCur.getLocation())));
						routePojo.setCalcDis(df.format(transportCost.calcDistance(cur.getLocation(), nextCur.getLocation())));
						StringBuilder str14 = new StringBuilder();
						for(String ss:currentLoc){
							str14.append(ss+"/");
						}
						if(str14.toString().length() == 0){
//							rr.createCell(14).setCellValue("");
							routePojo.setCarGoods("");
						}else{
//							rr.createCell(14).setCellValue(str14.toString().substring(0, str14.toString().length()-1));
							routePojo.setCarGoods(str14.toString().substring(0, str14.toString().length()-1));
						}
						routePojo.setScenariosId(ShiroUtil.getOpenScenariosId());
						routePojo.setUserId(String.valueOf(ShiroUtil.getCurrentUserId()));
						solutionRouteService.addRoute(routePojo);//将route插入数据库
					}
				}
				routeCount ++ ;
			}
			systemWebSocketHandler.sendMessageToUser(new TextMessage("增加数据成功"));

			
//			out = new FileOutputStream(fileName);
//			wb.write(out);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
//			try {
//				out.close();
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
		}
		
	}
}
