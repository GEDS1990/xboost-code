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
      //  String searchValue = request.getParameter("search[value]");
        String searchValue = request.getParameter("rideId");
        //Windows
     //   String searchValue = Strings.toUTF8(request.getParameter("search[value]"));
        String orderColumnIndex = request.getParameter("order[0][column]");
        String orderType = request.getParameter("order[0][dir]");
        String orderColumnName = request.getParameter("columns["+orderColumnIndex+"][name]");
        String scenariosId = ShiroUtil.getOpenScenariosId();

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
        List<Map> rideList = new ArrayList<>();
        String carType="";
        String carName="";
        String maxLoad="";
        String totalDistance="";

        if(modelType.equals("2")){
            rideList = solutionRideService.findByRideRelay(scenariosId,searchValue);
            carType = rideList.get(0).get("carType").toString();
            carName = rideList.get(0).get("carName").toString();
            maxLoad = carService.findByCarType(scenariosId,carType).getMaxLoad();

        }else {
            rideList = solutionRideService.findByRideSeries(scenariosId,searchValue);
            carType = rideList.get(0).get("carType").toString();
            carName = rideList.get(0).get("carName").toString();
            maxLoad = carService.findByCarType(scenariosId,carType).getMaxLoad();
            totalDistance = solutionRouteService.findTotalDistance(scenariosId,searchValue);
            for (int i = 0; i < rideList.size(); i++) {
                Map<String, Object> ride = rideList.get(i);
                String sbVol;
                String unloadVol;
                for (int j = i + 1; j < rideList.size(); j++) {
                    if (ride.get("sequence").equals(rideList.get(j).get("sequence"))) {
                        sbVol = (ride.get("sbVol").equals("0") ? "" : ride.get("sbVol").toString())
                                + (rideList.get(j).get("sbVol").equals("0") ? "" : rideList.get(j).get("sbVol").toString());
                        unloadVol = (ride.get("unloadVol").equals("0") ? "" : ride.get("unloadVol").toString())
                                + (rideList.get(j).get("unloadVol").equals("0") ? "" : (rideList.get(j).get("unloadVol").toString()));

                        rideList.get(i).put("sbVol", sbVol);
                        rideList.get(i).put("unloadVol", unloadVol);

                        String curLoc = (String) rideList.get(i).get("curLoc");
                        String nextCurLoc = (String) rideList.get(i).get("nextCurLoc");
                        if (!curLoc.equals(nextCurLoc)) {
                            rideList.get(i).put("nextCurLoc", nextCurLoc);
                        }

                        String curLoc2 = (String) rideList.get(j).get("curLoc");
                        String nextCurLoc2 = (String) rideList.get(j).get("nextCurLoc");
                        if (!curLoc2.equals(nextCurLoc2)) {
                            rideList.get(i).put("nextCurLoc", nextCurLoc2);
                        }

                        rideList.remove(j);
//                        count = count -1;
//                        filteredCount = filteredCount-1;
                    }
                }
            }
        }


        result.put("draw",draw);
//        result.put("recordsTotal",count); //总记录数
//        result.put("recordsFiltered",filteredCount); //过滤出来的数量
        result.put("data",rideList);
        result.put("totalDistance",totalDistance);
        result.put("maxLoad",maxLoad);
        result.put("carType",carType);
        result.put("carName",carName);
        return result;

    }

    //查询网点操作信息
    @RequestMapping(value = "/vehicles.json",method = RequestMethod.GET,produces = "application/json;charset=UTF-8")
    @ResponseBody
    public Map<String,Object> loadRideMap(HttpServletRequest request) {
        Map<String,Object> result = Maps.newHashMap();
        String scenariosId = ShiroUtil.getOpenScenariosId();
        String modelType = myScenariosService.findById(Integer.parseInt(ShiroUtil.getOpenScenariosId())).getScenariosModel();
        List<Map> rideList = new ArrayList<>();



        if(modelType.equals("2")){

            Integer maxRideId = solutionRideService.maxRideId(scenariosId);
            for(int x=1;x<=maxRideId;x++)
            {
                String depotOrder="";
                List<Map> tempList = solutionRideService.findByRide2(scenariosId,String.valueOf(x));
                Integer maxSequence= Integer.parseInt(tempList.get(tempList.size()-1).get("sequence").toString());
                Map<String,Object> rideRoute = Maps.newHashMap();
                String carType="";
                String carName="";
                String curLoc ="";
                List<String> carList= new ArrayList<>();

                for(int y=0;y<maxSequence;y++){
                    Map<String,Object> temp = tempList.get(y);
                    curLoc = temp.get("curLoc").toString();
                    if(y == maxSequence-1){
                        depotOrder += curLoc;
                    }else {
                        depotOrder += curLoc + ">>";
                    }
                }
                if(null!=tempList.get(0).get("carName")){
                    carName = tempList.get(0).get("carName").toString();
                }else
                {
                    carName="--";
                }
                carType = tempList.get(0).get("carType").toString();
                carList= carService.findIdleCar(ShiroUtil.getOpenScenariosId(),carType);

                rideRoute.put("RideId",x);
                rideRoute.put("depotOrder",depotOrder);
                rideRoute.put("carType",carType);
                rideRoute.put("carName",carName);
                rideRoute.put("carList",carList);
                rideList.add(rideRoute);
            }

        }else {
            Integer maxRideId = solutionRideService.maxRouteId(scenariosId);
            for(int x=1;x<=maxRideId;x++)
            {
                List<Map> tempList = solutionRideService.findByRide1(scenariosId,String.valueOf(x));
                Integer maxSequence= Integer.parseInt(tempList.get(tempList.size()-1).get("sequence").toString());
                String depotOrder="";
                Map<String,Object> rideRoute = Maps.newHashMap();
                String carType="";
                String carName="";
                String curLoc ="";
                List<String> carList= new ArrayList<>();

                for(int y=0;y<maxSequence;y++){
                    Map<String,Object> temp = tempList.get(y);
                    curLoc = temp.get("curLoc").toString();
                    String curNextLoc= temp.get("nextCurLoc").toString();
                    String separator=">>";


                    if(curLoc.equals(curNextLoc))
                    {
                        curLoc = "";
                        separator="";
                    }
                    if(y == maxSequence-1){
                        depotOrder += curLoc;
                    }else {
                        depotOrder += curLoc + separator;
                    }
                }
                if(null!=tempList.get(0).get("carName")){
                    carName = tempList.get(0).get("carName").toString();
                }else
                {
                    carName="--";
                }
                carType = tempList.get(0).get("carType").toString();
                carList= carService.findIdleCar(ShiroUtil.getOpenScenariosId(),carType);

                rideRoute.put("RideId",x);
                rideRoute.put("depotOrder",depotOrder);
                rideRoute.put("carType",carType);
                rideRoute.put("carName",carName);
                rideRoute.put("carList",carList);
                rideList.add(rideRoute);
            }

        }


        result.put("data",rideList);
        return result;

    }


    //排车
    @RequestMapping(value = "/planCar",method = RequestMethod.GET,produces = "application/json;charset=UTF-8")
    @ResponseBody
    public Map planCar(HttpServletRequest request) {
        String rideId = request.getParameter("rideId");
        //Linux
      //  String carName = request.getParameter("carName");
        //Windows
        String carName = Strings.toUTF8(request.getParameter("carName"));
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

        Map<String,Object> result = Maps.newHashMap();
        result.put("data","success");

        return result;

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
