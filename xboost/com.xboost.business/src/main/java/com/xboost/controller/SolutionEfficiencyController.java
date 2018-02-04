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
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
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
                result.put(site+"-"+String.valueOf(min+(periodTime*j))+"-"+String.valueOf(min+(periodTime*(j+1))),sbVol);
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
        List<Route> carList;

        //发出车辆数
        for(int i=0;i<siteList.size();i++){
            String site = siteList.get(i);
            for(int j=0;j<(max-min)/periodTime;j++)
            {
                param.put("scenariosId",scenariosId);
                param.put("curLoc",site);
                param.put("min",min+(periodTime*j));
                param.put("periodTime",periodTime);
                carList = solutionEfficiencyService.findLeaveCar(param);
                leaveCarNum = solutionEfficiencyService.findLeaveCarCount(param);
                for(int x=0;x<carList.size();x++)
                {
                    Route car = carList.get(x);
                    for(int y=x+1;y<carList.size();y++)
                    {
                        if(car.getRouteCount().equals(carList.get(y).getRouteCount())&&car.getCarType().equals(carList.get(y).getCarType())
                                &&car.getSequence().equals(carList.get(y).getSequence())){
                            leaveCarNum = leaveCarNum -1;
                        }

                    }
                }
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
        Integer arrCarNum;
        List<Route> carList;

        //到达车辆数
        for(int i=0;i<siteList.size();i++){
            String site = siteList.get(i);
            for(int j=0;j<(max-min)/periodTime;j++)
            {
                param.put("scenariosId",scenariosId);
                param.put("curLoc",site);
                param.put("min",min+(periodTime*j));
                param.put("periodTime",periodTime);

                carList = solutionEfficiencyService.findArrCar(param);
                arrCarNum = solutionEfficiencyService.findArrCarCount(param);
                for(int x=0;x<carList.size();x++)
                {
                    Route car = carList.get(x);
                    for(int y=x+1;y<carList.size();y++)
                    {
                        if(car.getRouteCount().equals(carList.get(y).getRouteCount())&&car.getSequence().equals(carList.get(y).getSequence())
                                &&car.getCarType().equals(carList.get(y).getCarType())){
                            arrCarNum = arrCarNum -1;
                        }

                    }
                }
                result.put(site+"-"+String.valueOf(min+(periodTime*j))+"-"+String.valueOf(min+(periodTime*(j+1))),arrCarNum);
            }
        }
        return result;
    }

    //网点信息
    @RequestMapping(value = "/siteInfo.json",method = RequestMethod.GET,produces = "application/json;charset=UTF-8")
    @ResponseBody
    public List<Map<String,Object>> loadSiteInfo(HttpServletRequest request) {
        String scenariosId = ShiroUtil.getOpenScenariosId();

        List<Map<String,Object>> siteList = solutionEfficiencyService.findSiteInfo(scenariosId);

        return siteList;
    }

    @RequestMapping(value = "/exportResult",method = RequestMethod.GET,produces = {"application/vnd.ms-excel;charset=UTF-8"})
    @ResponseBody
    public String exportResult(HttpServletResponse response)
    {
        response.setContentType("application/vnd.ms-excel;charset=utf-8");
        try {
            ServletOutputStream outputStream = response.getOutputStream();
            String fileName = new String(("Result_Effciency").getBytes(), "utf-8");
            response.setCharacterEncoding("utf-8");
            response.setHeader("Content-disposition", "attachment; filename=" + fileName + ".xlsx");// 组装附件名称和格式
            String scenariosId = ShiroUtil.getOpenScenariosId();
            String[] titles = { "发出车辆数", "", "", "", "总车次", "", "发出票数", "", "", "", "总票数", "",
                    "到达车辆数", "", "", "", "总车次", "", "到达票数", "", "", "", "总票数" };
            solutionEfficiencyService.exportResult(scenariosId,titles,outputStream);
            System.out.println("outputStream:"+outputStream);
        }
        catch (IOException e) {
            e.printStackTrace();
            System.out.println("网络连接故障!错误信息:"+e.getMessage());
        }
        return null;
    }
}
