package com.xboost.controller;

import com.xboost.pojo.Route;
import com.xboost.pojo.JieliResult;
import com.xboost.service.SolutionRouteService;
import com.xboost.service.JieliResultService;
import com.xboost.util.ShiroUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.inject.Inject;
import java.util.List;

/**
 * Created by Administrator on 2017/11/5 0005.
 */

@Controller
@RequestMapping("/result")
public class JieliResultController {
    @Inject
    private SolutionRouteService solutionRouteService;
    @Inject
    private JieliResultService jieliResultService;

    @RequestMapping(method = RequestMethod.GET)
    public String list() {
        return "results/result";
    }

    /**
     * 添加route信息
     * @return
     */
    @RequestMapping(value = "/add",method = RequestMethod.POST)
    @ResponseBody
    public String add(JieliResult jieliResult) {
        jieliResult.setScenariosId(ShiroUtil.getOpenScenariosId().toString());
        jieliResultService.save(jieliResult);
        return "success";
    }

    @RequestMapping(value = "/saveJieliToRoute",method = RequestMethod.GET)
    @ResponseBody
    public String save(Route route) {
        List<JieliResult> jieliResults=jieliResultService.findAll("90");
        JieliResult jieliResult;
        for(int i=0;i<jieliResults.size();i++){
            jieliResult=jieliResults.get(i);
            route.setScenariosId("90");
            route.setRouteCount(String.valueOf(i+1));
            route.setCarType(jieliResult.getStr1());
            route.setLocation(jieliResult.getConnection());
            route.setSequence(String.valueOf(1));
            route.setCurLoc(jieliResult.getInboundId());
            route.setType("PICKUP");
            route.setSbVol(jieliResult.getVolume());
            route.setSbVolSum(jieliResult.getVolume());
            route.setArrTime(jieliResult.getTimeId());
            route.setEndTime(jieliResult.getTimeId());
            route.setUnloadLoc("0");
            route.setUnloadVol("0");
            route.setUnloadVolSum("0");
            route.setNextCurLoc(jieliResult.getOutboundId());
            route.setCalcDis(jieliResult.getDistance());
            route.setStr1(jieliResult.getStr2());
            solutionRouteService.addRoute(route);

            route.setScenariosId("90");
            route.setRouteCount(String.valueOf(i+1));
            route.setCarType(jieliResult.getStr1());
            route.setLocation(jieliResult.getConnection());
            route.setSequence(String.valueOf(2));
            route.setCurLoc(jieliResult.getOutboundId());
            route.setType("DELIVER");
            route.setSbVol("0");
            route.setSbVolSum("0");
            route.setArrTime(jieliResult.getTimeId());
            route.setEndTime(jieliResult.getTimeId());
            route.setUnloadLoc("0");
            route.setUnloadVol(jieliResult.getVolume());
            route.setUnloadVolSum(jieliResult.getVolume());
            route.setNextCurLoc("");
            route.setCalcDis("0.00");
            route.setStr1(jieliResult.getStr2());
            solutionRouteService.addRoute(route);

        }

        solutionRouteService.addRoute(route);
        return "success";
    }

}
