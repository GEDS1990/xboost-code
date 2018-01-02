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
import java.text.DecimalFormat;
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
        double totalAll = 0;
        double totalAllRoute = 0;
        for(DemandInfo demandInfo : demandInfoList){
            totalAll = totalAll + Integer.parseInt((demandInfo.getVotes()!=null)?demandInfo.getVotes():"0");
        }
        List<Map> arrT = demandInfoService.findarrTime();
        for(Route route : routeList){
            totalAllRoute = totalAllRoute + Double.parseDouble((route.getSbVolSum()!=null)?route.getSbVolSum():"0");
        }
        type = (type!=null)?type:"0";
        DecimalFormat df = new DecimalFormat("######0.00");
        switch (type){
            case "0":
                for(int i=0;i<(max-min)/jiange;i++){
                    double total = 0;
                    for(DemandInfo demandInfo : demandInfoList){
                        double res = Integer.parseInt(demandInfo.getDurationEnd()!=null?demandInfo.getDurationEnd():"0");
                        if(res>(min+jiange*i) && res<=(min+jiange*(i+1))){
                            total = total + Integer.parseInt(demandInfo.getVotes()!=null?demandInfo.getVotes():"0");
                        }
                    }
                    map.put(String.valueOf(min+(jiange*i))+"-"+String.valueOf(min+(jiange*(i+1))),String.valueOf(df.format(total/totalAll*100)));
                }
                break;
            case "1":
                for(int i=0;i<(max-min)/jiange;i++){
                    double total = 0;
                    for(Route route : routeList){
                        double res = Double.parseDouble(route.getArrTime()!=null?route.getArrTime():"0");
                        if(res>(min+jiange*i) && res<=(min+jiange*(i+1))){
                            total = total + Double.parseDouble(route.getSbVolSum()!=null?route.getSbVolSum():"0");
                        }
                    }
                    map.put(String.valueOf(min+(jiange*i))+"-"+String.valueOf(min+(jiange*(i+1))),String.valueOf(df.format(total/totalAllRoute*100)));
                }
                break;
            case "3":
                double total1 = 0,total2 = 0,total3 = 0,total4 = 0,total5 = 0,total6 = 0,total7 = 0,totalD=0;
                for(Map arr : arrT){
                    double arrTime = 0;
                    double vol = 0;
                    try {
                        arrTime = Double.parseDouble(arr.get("arr_time").toString());
                        vol = Double.parseDouble(arr.get("vol").toString());
                    }catch (Exception e){
                        continue;
                    }
                    double res = arrTime;
                    if(0<=arrTime){
                        //提前到
                        if(res>50&&res<=60){
                            total1 = total1 + vol;
                        }else if(res>40&&res<=50){
                            total2 = total2 + vol;
                        }else if(res>30&&res<=40){
                            total3 = total3 + vol;
                        }else if(res>20&&res<=30){
                            total4 = total4 + vol;
                        }else if(res>10&&res<=20){
                            total5 = total5 + vol;
                        }else if(res>0&&res<=10){
                            total6 = total6 + vol;
                        }else if(res == 0){
                            total7 = total7 + vol;
                        }
                    }else{
                        //晚到
                    }
                    totalD = total1+total2+total3+total4+total5+total6+total7;
                }
                map.put("tiqian60",df.format((total1/totalD)*100));
                map.put("tiqian50",df.format((total2/totalD)*100));
                map.put("tiqian40",df.format((total3/totalD)*100));
                map.put("tiqian30",df.format((total4/totalD)*100));
                map.put("tiqian20",df.format((total5/totalD)*100));
                map.put("tiqian10",df.format((total6/totalD)*100));
                map.put("zunshi",df.format((total7/totalD)*100));
            break;
            default:
                break;
        }
        return map;
    }
}
