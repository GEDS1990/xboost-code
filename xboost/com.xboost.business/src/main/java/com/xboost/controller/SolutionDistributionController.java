package com.xboost.controller;

import com.xboost.pojo.DemandInfo;
import com.xboost.pojo.Route;
import com.xboost.service.ArrInfoService;
import com.xboost.service.DemandInfoService;
import com.xboost.service.SolutionRouteService;
import com.xboost.util.ShiroUtil;
import org.junit.runners.Parameterized;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 计费过程
 */
@Controller
@RequestMapping("/distribution")
public class SolutionDistributionController {
    @Inject
    private DemandInfoService demandInfoService;
    @Inject
    private SolutionRouteService solutionRouteService;

    @RequestMapping(method = RequestMethod.GET)
    public String list() {
        return "results/distribution";
    }
    /**
     *
     * @param type
     * 0:个网点截单时间
     * 1：各网点送达时间分布
     * 2：收段达到集散点时间分布
     * 3：支线到达目的地集散点时间分布
     * 4：始发集散地OD件量分布
     * 5：始发网点OD件量分布
     * 6：始发集散点的发车分布
     * @return
     */
    @RequestMapping(value = "/getMaxMix.json",method = RequestMethod.GET,produces = "application/json;charset=UTF-8")
    @ResponseBody
    public Map<String,Object> getMaxMix(String type) {
        String maxmix = demandInfoService.findMinMax(ShiroUtil.getOpenScenariosId());
        int min = Integer.parseInt(maxmix.split("-")[0]);
        int max = Integer.parseInt(maxmix.split("-")[1]);
        //间隔
        int jiange = 10;
        Map<String,Object> map = new HashMap<String,Object>();
        List<DemandInfo> demandInfoList = demandInfoService.findAll(ShiroUtil.getOpenScenariosId());
        List<Route> routeList = solutionRouteService.findAllRoute(ShiroUtil.getOpenScenariosId());
        int totalAll = 0;
//        int totalAllRoute = 0;
        for(DemandInfo demandInfo : demandInfoList){
            totalAll = totalAll + Integer.parseInt((demandInfo.getVotes()!=null)?demandInfo.getVotes():"0");
        }
        List<Object> arrT = demandInfoService.findarrTime();
//        for(Route route : routeList){
//            totalAllRoute = totalAllRoute + Integer.parseInt((route.getSbVolSum()!=null)?route.getSbVolSum():"0");
//        }
        type = (type!=null)?type:"0";
        switch (type){
            case "0":
                for(int i=0;i<(max-min)/jiange;i++){
                    int total = 0;
                    for(DemandInfo demandInfo : demandInfoList){
                        int res = Integer.parseInt(demandInfo.getDurationEnd()!=null?demandInfo.getDurationEnd():"0");
                        if(res>(min+jiange*i) && res<=(min+jiange*(i+1))){
                            total = total + Integer.parseInt(demandInfo.getVotes()!=null?demandInfo.getVotes():"0");
                        }
                    }
                    map.put(String.valueOf(min+(jiange*i))+"-"+String.valueOf(min+(jiange*(i+1))),String.valueOf(total/totalAll*100).concat("%"));
                }
                break;
            case "1":
                for(int i=0;i<(max-min)/jiange;i++){
                    int total = 0;
                    for(Route route : routeList){
                        int res = Integer.parseInt(route.getArrTime()!=null?route.getArrTime():"0");
                        if(res>(min+jiange*i) && res<=(min+jiange*(i+1))){
                            total = total + Integer.parseInt(route.getSbVolSum()!=null?route.getSbVolSum():"0");
                        }
                    }
                    map.put(String.valueOf(min+(jiange*i))+"-"+String.valueOf(min+(jiange*(i+1))),String.valueOf(total/totalAll*100).concat("%"));
                }
                break;
            case "3":
                double total1 = 0,total2 = 0,total3 = 0,total4 = 0,total5 = 0,total6 = 0,total7 = 0;
                for(Route route : routeList){
                    Double res = Double.parseDouble(route.getArrTime()!=null?route.getArrTime():"0")-min;
                    if(res>50&&res<=60){
                        total1 = total1 + Integer.parseInt(route.getSbVolSum()!=null?route.getSbVolSum():"0");
                    }else if(res>40&&res<=50){
                        total2 = total2 + Integer.parseInt(route.getSbVolSum()!=null?route.getSbVolSum():"0");
                    }else if(res>30&&res<=40){
                        total3 = total3 + Integer.parseInt(route.getSbVolSum()!=null?route.getSbVolSum():"0");
                    }else if(res>20&&res<=30){
                        total4 = total4 + Integer.parseInt(route.getSbVolSum()!=null?route.getSbVolSum():"0");
                    }else if(res>10&&res<=20){
                        total5 = total5 + Integer.parseInt(route.getSbVolSum()!=null?route.getSbVolSum():"0");
                    }else if(res>0&&res<=10){
                        total6 = total6 + Integer.parseInt(route.getSbVolSum()!=null?route.getSbVolSum():"0");
                    }else if(res == 0){
                        total7 = total7 + Integer.parseInt(route.getSbVolSum()!=null?route.getSbVolSum():"0");
                    }
                }
                map.put("tiqian60",total1);
                map.put("tiqian50",total2);
                map.put("tiqian40",total3);
                map.put("tiqian30",total4);
                map.put("tiqian20",total5);
                map.put("tiqian10",total6);
                map.put("zunshi",total7);
            break;
            default:
                break;
        }
        return map;
    }
}
