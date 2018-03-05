package com.xboost.controller;

import com.google.common.collect.Maps;
import com.xboost.pojo.Ride;
import com.xboost.pojo.Route;
import com.xboost.service.*;
import com.xboost.service.jieli.TempService;
import com.xboost.util.ShiroUtil;
import com.xboost.util.Strings;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.inject.Inject;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

/**
 * Created by Administrator on 2017/11/5 0005.
 */

@Controller
@RequestMapping("/vehiclesPlan")
public class SolutionVehiclesPlanController {
    @Inject
    private SolutionVehiclesService solutionVehiclesService;
    @Inject
    private SiteInfoService siteInfoService;
    @Inject
    private SolutionRideService solutionRideService;
    @Inject
    private SolutionRouteService solutionRouteService;
    @Inject
    private MyScenariosService myScenariosService;
    @Inject
    private TempService tempService;
    @Inject
    private CarService carService;


    @RequestMapping(method = RequestMethod.GET)
    public String list() {
        return "results/vehiclesplan";
    }



    //查询网点操作信息
    @RequestMapping(value = "/vehiclesPlan.json",method = RequestMethod.GET,produces = "application/json;charset=UTF-8")
    @ResponseBody
    public Map<String,Object> loadVehiclesInfo(HttpServletRequest request) {

        String draw = request.getParameter("draw");
        Integer start;
        Integer length;
        if(request.getParameter("start")==null || request.getParameter("start")=="")
        {
            start = 0;
        }
        else{
            start =Integer.valueOf(request.getParameter("start"));
        }
        if(request.getParameter("length")==null || request.getParameter("length")=="")
        {
            length = 0;
        }
        else
        {
            length =Integer.valueOf(request.getParameter("length"));
        }
        //Linux
        String searchValue = request.getParameter("search[value]");
        //Windows
     //   String searchValue = Strings.toUTF8(request.getParameter("search[value]"));
        String orderColumnIndex = request.getParameter("order[0][column]");
        String orderType = request.getParameter("order[0][dir]");
        String orderColumnName = request.getParameter("columns["+orderColumnIndex+"][name]");

        Map<String,Object> param = Maps.newHashMap();
        param.put("start",start);
        param.put("length",length);
        if(StringUtils.isNotEmpty(searchValue)) {
            param.put("keyword", searchValue);
        }
        param.put("orderColumn",orderColumnName);
        param.put("orderType",orderType);
        param.put("scenariosId",ShiroUtil.getOpenScenariosId());



        Map<String,Object> result = Maps.newHashMap();

        String modelType = myScenariosService.findById(Integer.parseInt(ShiroUtil.getOpenScenariosId())).getScenariosModel();
        List<Map> rideList = null;

        if(modelType.equals("2")){
            rideList = solutionRideService.findAllRidesRelay(ShiroUtil.getOpenScenariosId());

        }else{
            rideList = solutionRideService.findAllRidesSeries(ShiroUtil.getOpenScenariosId());
        }
        for(int i=0;i<rideList.size();i++)
        {
            String carType=rideList.get(i).get("carType").toString();
            List<String> carList= carService.findIdleCar(ShiroUtil.getOpenScenariosId(),carType);
            rideList.get(i).put("carList",carList);
        }
        Integer count = solutionVehiclesService.findAllCountByCar(ShiroUtil.getOpenScenariosId());
        Integer filteredCount = solutionVehiclesService.findCountByCar(param);

//        tempService.rideResult(routeList);

        result.put("draw",draw);
        result.put("recordsTotal",count); //总记录数
        result.put("recordsFiltered",filteredCount); //过滤出来的数量
        result.put("data",rideList);
        return result;

    }

    //排车
    @RequestMapping(value = "/planCar",method = RequestMethod.GET,produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String planCar(HttpServletRequest request) {
        Map<String,Object> result = Maps.newHashMap();
        String rideId = request.getParameter("rideId");
        String carName = request.getParameter("carName");
        String scenariosId = ShiroUtil.getOpenScenariosId();
        String modelType = myScenariosService.findById(Integer.parseInt(scenariosId)).getScenariosModel();
        String oldCarName="";
        if(modelType.equals("2"))
        {
            oldCarName = solutionRouteService.findRouteCarRelay(scenariosId,rideId);
        }else {

            oldCarName = solutionRouteService.findRouteCar(scenariosId,rideId);
        }

        Map<String,Object> param = Maps.newHashMap();
        param.put("rideId",rideId);
        param.put("carName",carName);
        param.put("scenariosId",scenariosId);

        if(!Strings.isEmpty(oldCarName)){
            solutionRouteService.updateCarToIdle(scenariosId,oldCarName);
        }
        if(modelType.equals("2"))
        {
            solutionRouteService.updateCarNameRelay(param);
        }else {
            solutionRouteService.updateCarName(param);
        }
        //把车的状态更新为busy
        solutionRouteService.updateCarToBusy(scenariosId,carName);

        return "success";

    }

    //自动拼接
    @RequestMapping(value = "/saveSplic",method = RequestMethod.GET,produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String saveSplic(HttpServletRequest request) {

        String scenariosId = ShiroUtil.getOpenScenariosId();

        Map<String,Object> result = Maps.newHashMap();
        List<Route> routeList = solutionRouteService.findAllRoute(ShiroUtil.getOpenScenariosId());

        tempService.rideResult(routeList);

        return "success";
    }




}
