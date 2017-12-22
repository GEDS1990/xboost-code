package com.xboost.controller;

import com.google.common.collect.Maps;
import com.xboost.pojo.Route;
import com.xboost.service.SolutionCostService;
import com.xboost.service.SolutionEfficiencyService;
import com.xboost.service.SolutionRouteService;
import com.xboost.service.DemandInfoService;
import com.xboost.util.ShiroUtil;
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

    @RequestMapping(method = RequestMethod.GET)
    public String list() {
        return "results/efficiency";
    }

    //发出票数
    @RequestMapping(value = "/efficiency.json",method = RequestMethod.GET,produces = "application/json;charset=UTF-8")
    @ResponseBody
    public Map<String,Object> load(HttpServletRequest request) {
        String scenariosId = ShiroUtil.getOpenScenariosId();

//        String maxmix = demandInfoService.findMinMax(ShiroUtil.getOpenScenariosId());
//        int min = Integer.parseInt(maxmix.split("-")[0]);
//        int max = Integer.parseInt(maxmix.split("-

        int periodTime = 10;
        int min = Integer.parseInt(demandInfoService.findMin(scenariosId));
        int max = Integer.parseInt(demandInfoService.findMax(scenariosId));

        List<Route> routeList = solutionRouteService.findAllRoute(scenariosId);
        List<String> siteList = solutionEfficiencyService.findAllSite(scenariosId);

        Map<String,Object> result = Maps.newHashMap();
        Map<String,Object> param = Maps.newHashMap();
        Integer sbVol;
        Integer unloadVol;

        //发出票数
        for(int i=0;i<siteList.size();i++){
            String site = siteList.get(i);
            for(int j=0;j<(max-min)/periodTime;j++)
            {
                param.put("scenariosId",scenariosId);
                param.put("curLoc",site);
                param.put("min",min);
                param.put("periodTime",periodTime);
                sbVol = solutionEfficiencyService.findSbVol(param);
                result.put(String.valueOf(min+(periodTime*j))+"-"+String.valueOf(min+(periodTime*(j+1))),site+"-"+sbVol);
            }
        }

        //发出票数
        for(int i=0;i<siteList.size();i++){
            String site = siteList.get(i);
            for(int j=0;j<(max-min)/periodTime;j++)
            {
                param.put("scenariosId",scenariosId);
                param.put("curLoc",site);
                param.put("min",min);
                param.put("periodTime",periodTime);
                unloadVol = solutionEfficiencyService.findUnloadVol(param);
                result.put(String.valueOf(min+(periodTime*j))+"-"+String.valueOf(min+(periodTime*(j+1))),site+"-"+unloadVol);
            }
        }

		//到达车辆数
//        for(int i=0;i<siteList.size();i++){
//            String site = siteList.get(i);
//            for(int j=0;j<(max-min)/periodTime;j++)
//            {
//                param.put("scenariosId",scenariosId);
//                param.put("curLoc",site);
//                param.put("min",min);
//                param.put("periodTime",periodTime);
//                unloadVol = solutionEfficiencyService.findUnloadVol(param);
//                result.put(String.valueOf(min+(periodTime*j))+"-"+String.valueOf(min+(periodTime*(j+1))),site+"-"+unloadVol);
//            }
//        }


        return result;
    }
}
