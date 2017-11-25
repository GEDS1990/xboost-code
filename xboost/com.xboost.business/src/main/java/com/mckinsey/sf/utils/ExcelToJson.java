package com.mckinsey.sf.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.*;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mckinsey.sf.constants.IConstants;
import com.mckinsey.sf.data.Activity;
import com.mckinsey.sf.data.Car;
import com.mckinsey.sf.data.Input;
import com.mckinsey.sf.data.Job;
import com.mckinsey.sf.data.TimeWindow;
import com.mckinsey.sf.data.solution.RouteJson;
import com.mckinsey.sf.data.solution.SolutionJson;
import com.xboost.pojo.Configuration;
import com.xboost.pojo.DemandInfo;
import com.xboost.service.ConfigurationService;
import com.xboost.service.DemandInfoService;
import com.xboost.util.ShiroUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.inject.Inject;

/**   
*    
* Author：Alivia Chen   
* Email : alivia_chen@mckinsey.com
* Date  ：May 24, 2017
* @version        
*/
@Component
public class ExcelToJson implements IConstants{

	public Input transferInput(Configuration config, DemandInfoService demandInfoService){
		double maxVol = 100;
		Input inputJson = new Input();

		ObjectMapper mapper = new ObjectMapper();
//		Configuration input = null;
//		try {
//			input = mapper.readValue(new File("src/main/resources/input.json"), Configuration.class);
//		} catch (JsonParseException e) {
//			e.printStackTrace();
//		} catch (JsonMappingException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		Configuration input = config;
		com.mckinsey.sf.data.Configuration input = new com.mckinsey.sf.data.Configuration();
		input.setDistMode(config.getDistMode());
		input.setCarCostMode(config.getCarCostMode());
		input.setOptimizeIterations(config.getOptimizeIterations());
		input.setLoadTime(config.getLoadTime());
		input.setCarTemplates(config.getCarTemplates());
		for(Car c : input.getCarTemplates()){
			double dimensionDouble = Double.parseDouble(c.getDimensions().split(",")[0]);
			if(dimensionDouble > maxVol){
				maxVol = dimensionDouble;
			}
		}
		
		inputJson.setCarTemplates(input.getCarTemplates());	
		
		//init demand info
		SolutionJson solution = new SolutionJson();
		List<Job> jobList = new ArrayList<Job>();
		RouteJson[] routes = new RouteJson[]{};
		solution.setRoutes(routes);
		//read distance from file
//		String splitBy = ",";
//        String line;
//		try {
//			BufferedReader br = new BufferedReader(new FileReader("src/main/resources/demand/"+input.getDemandFile()));
//			line = br.readLine();
			//从数据库读取数据
			Map<String, Object> param = new HashMap<String,Object>();
			//根据场景ID查询所有
			param.put("scenairosId", ShiroUtil.getOpenScenariosId());

			List<DemandInfo> demandInfoList = demandInfoService.findByScenairoIdParam(param);
			int jobID = 0;
			for(int i=0;i<demandInfoList.size();i++){
				String pickupLoc = demandInfoList.get(i).getSiteCodeCollect();
				String deliverLoc = demandInfoList.get(i).getSiteCodeDelivery();
				String jobSize = demandInfoList.get(i).getVotes().toString();
				String pickupStart = demandInfoList.get(i).getDurationStart();
				String deliverStart = demandInfoList.get(i).getDurationStart();
				String end = demandInfoList.get(i).getDurationEnd();

				TimeWindow pickupTime = new TimeWindow(Integer.parseInt(pickupStart),Integer.parseInt(end)+input.getLoadTime());
				TimeWindow deliverTime = new TimeWindow(Integer.parseInt(deliverStart),Integer.parseInt(end)+input.getLoadTime());
				double size = Math.ceil(Double.parseDouble(jobSize)*VOL_PERCENT);
				while(size > maxVol){
					jobID++;
					Activity delivery = new Activity(String.valueOf(UUID.randomUUID()),"DELIVER", pickupTime, 0, deliverLoc);
					delivery.setJobId(String.valueOf(jobID));
					Activity pickup = new Activity(String.valueOf(UUID.randomUUID()),"PICKUP", pickupTime, 0, pickupLoc);
					pickup.setJobId(String.valueOf(jobID));
					Job j = new Job(String.valueOf(jobID),1, new String[]{},new double[]{maxVol}, pickup, delivery,false);
					jobList.add(j);
					size -= maxVol;
				}

				jobID++;
				Activity delivery = new Activity(String.valueOf(UUID.randomUUID()),"DELIVER", deliverTime, 0, deliverLoc);
				delivery.setJobId(String.valueOf(jobID));
				Activity pickup = new Activity(String.valueOf(UUID.randomUUID()),"PICKUP", pickupTime, 0, pickupLoc);
				pickup.setJobId(String.valueOf(jobID));
				Job j = new Job(String.valueOf(jobID),1, new String[]{},new double[]{size}, pickup, delivery,false);
				jobList.add(j);
			}
			Job[] jobs = new Job[jobList.size()];
			for(int i =0; i< jobList.size();i++){
				jobs[i] = jobList.get(i);
			}
			solution.setUnassignedJobs(jobs);
			inputJson.setInitSolution(solution);

//			int jobID = 0;
//			while((line = br.readLine()) !=null){
//				String[] row = line.split(splitBy);
//				String jobSize = row[3];
//				String pickupLoc = row[1];
//				String deliverLoc = row[2];
//
//				String pickupStart = row[4];
//				String deliverStart = row[4];
//				String end = row[5];
//
//				TimeWindow pickupTime = new TimeWindow(Integer.parseInt(pickupStart),Integer.parseInt(end)+input.getLoadTime());
//				TimeWindow deliverTime = new TimeWindow(Integer.parseInt(deliverStart),Integer.parseInt(end)+input.getLoadTime());
//
//				double size = Math.ceil(Double.parseDouble(jobSize)*VOL_PERCENT);
//
//				//TODO
////				if("571WW".equalsIgnoreCase(pickupLoc)){
////					size = size *3;
////				}
//
//				while(size > maxVol){
//					jobID++;
//					Activity delivery = new Activity(String.valueOf(UUID.randomUUID()),"DELIVER", pickupTime, 0, deliverLoc);
//					delivery.setJobId(String.valueOf(jobID));
//					Activity pickup = new Activity(String.valueOf(UUID.randomUUID()),"PICKUP", pickupTime, 0, pickupLoc);
//					pickup.setJobId(String.valueOf(jobID));
//					Job j = new Job(String.valueOf(jobID),1, new String[]{},new double[]{maxVol}, pickup, delivery,false);
//					jobList.add(j);
//					size -= maxVol;
//				}
//
//				jobID++;
//				Activity delivery = new Activity(String.valueOf(UUID.randomUUID()),"DELIVER", deliverTime, 0, deliverLoc);
//				delivery.setJobId(String.valueOf(jobID));
//				Activity pickup = new Activity(String.valueOf(UUID.randomUUID()),"PICKUP", pickupTime, 0, pickupLoc);
//				pickup.setJobId(String.valueOf(jobID));
//				Job j = new Job(String.valueOf(jobID),1, new String[]{},new double[]{size}, pickup, delivery,false);
//				jobList.add(j);
//
//			}
//			br.close();

//			Job[] jobs = new Job[jobList.size()];
//			for(int i =0; i< jobList.size();i++){
//				jobs[i] = jobList.get(i);
//			}
//			solution.setUnassignedJobs(jobs);
//			inputJson.setInitSolution(solution);
//		} catch (IOException e) {
//			e.printStackTrace();
//		}


		
		
//		try {
//			mapper.writeValue(new File("src/main/resources/progInput.json"), inputJson);
//			return inputJson;
//		} catch (JsonGenerationException e) {
//			e.printStackTrace();
//		} catch (JsonMappingException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
		return inputJson;
	}

}
