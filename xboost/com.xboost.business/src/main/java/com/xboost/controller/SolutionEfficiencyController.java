package com.xboost.controller;

import com.google.common.collect.Maps;
import com.xboost.pojo.Route;
import com.xboost.service.*;
import com.xboost.util.ShiroUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;


@Controller
@RequestMapping("/efficiency")
public class SolutionEfficiencyController {
    @Inject
    SolutionRouteService solutionRouteService;
    @Inject
    DemandInfoService demandInfoService;
    @Inject
    SolutionEfficiencyService solutionEfficiencyService;

    private static Logger logger = LoggerFactory.getLogger(SiteInfoService.class);

    @RequestMapping(method = RequestMethod.GET)
    public String list() {
        return "results/efficiency";
    }

    //发出票数
    @RequestMapping(value = "/sbVol.json",method = RequestMethod.GET,produces = "application/json;charset=UTF-8")
    @ResponseBody
    public Map<String,Object> loadSbVol(HttpServletRequest request) {
        String scenariosId = ShiroUtil.getOpenScenariosId();
        int periodTime = 10;
        int min = Integer.parseInt(demandInfoService.findMin(scenariosId));
        int max = Integer.parseInt(demandInfoService.findMax(scenariosId));
        List<String> siteList = solutionEfficiencyService.findAllSite(scenariosId);

        Map<String,Object> result = Maps.newHashMap();
        Map<String,Object> res = Maps.newHashMap();
        Map<String,Object> param = Maps.newHashMap();
        Integer sbVol;

        //发出票数
        for(int i=0;i<siteList.size();i++){
            String site = siteList.get(i);
            for(int j=0;j<(max-min)/periodTime;j++)
            {
                param.put("scenariosId",scenariosId);
                param.put("curLoc",site);
                param.put("min",min+(periodTime*j));
                param.put("periodTime",periodTime);
                sbVol = (null == solutionEfficiencyService.findSbVol(param)?0:solutionEfficiencyService.findSbVol(param));
               // result.put(site+"-"+String.valueOf(min+(periodTime*j))+"-"+String.valueOf(min+(periodTime*(j+1))),sbVol);
                res.put("site",site);
                res.put("start",min+periodTime*j);
                res.put("end",min+periodTime*(j+1));
                res.put("sbVol",sbVol);
                result.put("sbVol",res);
            }
        }
        return result;
    }

    //到达票数
    @RequestMapping(value = "/unloadVol.json",method = RequestMethod.GET,produces = "application/json;charset=UTF-8")
    @ResponseBody
    public Map<String,Object> loadUnloadVol(HttpServletRequest request) {
        String scenariosId = ShiroUtil.getOpenScenariosId();
        int periodTime = 10;
        int min = Integer.parseInt(demandInfoService.findMin(scenariosId));
        int max = Integer.parseInt(demandInfoService.findMax(scenariosId));
        List<String> siteList = solutionEfficiencyService.findAllSite(scenariosId);

        Map<String,Object> result = Maps.newHashMap();
        Map<String,Object> param = Maps.newHashMap();
        Integer unloadVol;

        //到达票数
        for(int i=0;i<siteList.size();i++){
            String site = siteList.get(i);
            for(int j=0;j<(max-min)/periodTime;j++)
            {
                param.put("scenariosId",scenariosId);
                param.put("curLoc",site);
                param.put("min",min+(periodTime*j));
                param.put("periodTime",periodTime);
                unloadVol = (null == solutionEfficiencyService.findUnloadVol(param)?0:solutionEfficiencyService.findUnloadVol(param));
                result.put(site+"-"+String.valueOf(min+(periodTime*j))+"-"+String.valueOf(min+(periodTime*(j+1))),unloadVol);
            }
        }
        return result;
    }

    //发出车辆数
    @RequestMapping(value = "/leaveCarNum.json",method = RequestMethod.GET,produces = "application/json;charset=UTF-8")
    @ResponseBody
    public Map<String,Object> loadLeaveCarNum(HttpServletRequest request) {
        String scenariosId = ShiroUtil.getOpenScenariosId();
        int periodTime = 10;
        int min = Integer.parseInt(demandInfoService.findMin(scenariosId));
        int max = Integer.parseInt(demandInfoService.findMax(scenariosId));
        List<String> siteList = solutionEfficiencyService.findAllSite(scenariosId);

        Map<String,Object> result = Maps.newHashMap();
        Map<String,Object> param = Maps.newHashMap();
        Integer leaveCarNum;

        //发出车辆数
        for(int i=0;i<siteList.size();i++){
            String site = siteList.get(i);
            for(int j=0;j<(max-min)/periodTime;j++)
            {
                leaveCarNum=0;
                param.put("scenariosId",scenariosId);
                param.put("curLoc",site);
                param.put("min",min+(periodTime*j));
                param.put("periodTime",periodTime);
//                for(String s:solutionEfficiencyService.findLeaveCar(param)){
//                    if(null==s){
//                        leaveCarNum = leaveCarNum + 0;
//                    }
//                    else
//                    {
//                        leaveCarNum = leaveCarNum + 1;
//                    }
//                }
                leaveCarNum = solutionEfficiencyService.findLeaveCar(param).size();
                result.put(site+"-"+String.valueOf(min+(periodTime*j))+"-"+String.valueOf(min+(periodTime*(j+1))),leaveCarNum);
            }
        }
        return result;
    }

    //到达车辆数
    @RequestMapping(value = "/arrCarNum.json",method = RequestMethod.GET,produces = "application/json;charset=UTF-8")
    @ResponseBody
    public Map<String,Object> load(HttpServletRequest request) {
        String scenariosId = ShiroUtil.getOpenScenariosId();
        int periodTime = 10;
        int min = Integer.parseInt(demandInfoService.findMin(scenariosId));
        int max = Integer.parseInt(demandInfoService.findMax(scenariosId));
        List<String> siteList = solutionEfficiencyService.findAllSite(scenariosId);

        Map<String,Object> result = Maps.newHashMap();
        Map<String,Object> param = Maps.newHashMap();
        Integer arrCarNum=0;

        //到达车辆数
        for(int i=0;i<siteList.size();i++){
            String site = siteList.get(i);
            for(int j=0;j<(max-min)/periodTime;j++)
            {
                param.put("scenariosId",scenariosId);
                param.put("curLoc",site);
                param.put("min",min);
                param.put("periodTime",min+(periodTime*j));
//                for(String s:solutionEfficiencyService.findArrCar(param)){
//                    if(null==s){
//                        arrCarNum=arrCarNum+0;
//                    }
//                    else
//                    {
//                        arrCarNum = arrCarNum+1;
//                    }
//                }
                arrCarNum = solutionEfficiencyService.findArrCar(param).size();
                result.put(site+"-"+String.valueOf(min+(periodTime*j))+"-"+String.valueOf(min+(periodTime*(j+1))),arrCarNum);
            }
        }
        return result;
    }
}
