package com.xboost.controller;

import com.google.common.collect.Maps;
import com.xboost.pojo.Route;
import com.xboost.pojo.SiteInfo;
import com.xboost.service.SolutionRouteService;
import com.xboost.util.ShiroUtil;
import com.xboost.util.Strings;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/11/5 0005.
 */

@Controller
@RequestMapping("/route")
public class SolutionRouteController {
    @Inject
    private SolutionRouteService solutionRouteService;

    @RequestMapping(method = RequestMethod.GET)
    public String list() {
        return "results/route";
    }

    /**
     * 添加route信息
     * @return
     */
    @RequestMapping(value = "/addRoute",method = RequestMethod.POST)
    @ResponseBody
    public String addRoute(Route route) {
        route.setScenariosId(ShiroUtil.getOpenScenariosId().toString());
        solutionRouteService.addRoute(route);
        return "success";
    }


    //查询路线信息
    @RequestMapping(value = "/route.json",method = RequestMethod.GET,produces = "application/json;charset=UTF-8")
    @ResponseBody
    public Map<String,Object> load(HttpServletRequest request) {

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
        String searchValue = request.getParameter("search[value]");
        String orderColumnIndex = request.getParameter("order[0][column]");
        String orderType = request.getParameter("order[0][dir]");
        String orderColumnName = request.getParameter("columns["+orderColumnIndex+"][name]");

        Map<String,Object> param = Maps.newHashMap();
        param.put("start",start);
        param.put("length",length);
        if(StringUtils.isNotEmpty(searchValue)) {
            param.put("keyword",Strings.toUTF8(searchValue));
        }
        param.put("orderColumn",orderColumnName);
        param.put("orderType",orderType);
        param.put("scenariosId",ShiroUtil.getOpenScenariosId());

        Map<String,Object> result = Maps.newHashMap();

        List<Route> allRoute = solutionRouteService.findAllRoute(ShiroUtil.getOpenScenariosId());
        List<Map<String,Object>> routeList = solutionRouteService.findByRoute(param); //.findAll();
        Integer count = solutionRouteService.findAllCountByRoute(ShiroUtil.getOpenScenariosId());
        Integer filteredCount = solutionRouteService.findCountByRoute(param);
  //      String totalDistance = solutionRouteService.findTotalDistance(ShiroUtil.getOpenScenariosId(),searchValue);

        Double sbVolSum;
        Double unloadVolSum;
        String sbVol;
        String unloadVol;

        if(""!=searchValue)
        {
            for(int i=0;i<routeList.size();i++)
            {
                Map<String,Object> route = routeList.get(i);
                for(int j=i+1;j<routeList.size();j++)
                {
                    if(route.get("sequence").equals(routeList.get(j).get("sequence"))){
//                        sbVolSum = Double.parseDouble(route.get("sbVolSum").equals("0")?"":route.get("sbVolSum").toString())
//                                +Double.parseDouble(routeList.get(j).get("sbVolSum").equals("0")?"":routeList.get(j).get("sbVolSum").toString());
//                        unloadVolSum = Double.parseDouble(route.get("unloadVolSum").equals("0")?"":route.get("unloadVolSum").toString())
//                                +Double.parseDouble(routeList.get(j).get("unloadVolSum").equals("0")?"":routeList.get(j).get("unloadVolSum").toString());
                        sbVol = (route.get("sbVol").equals("0")?"":route.get("sbVol").toString())
                                +(routeList.get(j).get("sbVol").equals("0")?"":routeList.get(j).get("sbVol").toString());
                        unloadVol = (route.get("unloadVol").equals("0")?"":route.get("unloadVol").toString())
                                +(routeList.get(j).get("unloadVol").equals("0")?"":(routeList.get(j).get("unloadVol").toString()));

//                        routeList.get(i).put("sbVolSum",sbVolSum);
//                        routeList.get(i).put("unloadVolSum",unloadVolSum);
                        routeList.get(i).put("sbVol",sbVol);
                        routeList.get(i).put("unloadVol",unloadVol);
                        routeList.remove(j);
                        count = count -1;
                        filteredCount = filteredCount-1;
                    }

                }
            }

        }


        List<String> usingCar = solutionRouteService.findUsingCar1(ShiroUtil.getOpenScenariosId());
        List<String> idleCar = solutionRouteService.findIdleCar1(ShiroUtil.getOpenScenariosId());


        result.put("draw",draw);
        result.put("recordsTotal",count); //总记录数
        result.put("recordsFiltered",filteredCount); //过滤出来的数量
        result.put("data",routeList);
    //    result.put("totalDistance",totalDistance);
        result.put("usingCar",usingCar);
        result.put("idleCar",idleCar);
        return result;
    }

    //查询路线id
    @RequestMapping(value = "/planCar.json",method = RequestMethod.GET,produces = "application/json;charset=UTF-8")
    @ResponseBody
    public Map<String,Object> findIdleCar(String routeCount) {
        Map<String,Object> result = Maps.newHashMap();
        List<String> idleCar = solutionRouteService.findIdleCar(ShiroUtil.getOpenScenariosId(),routeCount);
        List<String> usingCar = solutionRouteService.findUsingCar1(ShiroUtil.getOpenScenariosId());

        result.put("usingCar",usingCar);
        result.put("idleCar",idleCar);
        return result;
    }


    //查询路线id
    @RequestMapping(value = "/routeId.json",method = RequestMethod.GET,produces = "application/json;charset=UTF-8")
    @ResponseBody
    public Map<String,Object> loadRouteId(HttpServletRequest request) {
        Map<String,Object> result = Maps.newHashMap();
        Integer routeIdList = solutionRouteService.findRouteId(ShiroUtil.getOpenScenariosId());
        result.put("data",routeIdList);
        return result;
    }


    //查询路线距离
    @RequestMapping(value = "/totalDistance.json",method = RequestMethod.GET,produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String totalDistance(String routeCount) {
        String totalDistance =solutionRouteService.findTotalDistance(ShiroUtil.getOpenScenariosId(),routeCount);

        return totalDistance;
    }

    /**排车更新route表carName
     * @return
     */
    @RequestMapping(value = "/updateCarName",method = RequestMethod.POST)
    @ResponseBody
    public String updateCarName(HttpServletRequest request) {
        String routeCount = request.getParameter("routeCount");
        String carName = request.getParameter("carName");
        String scenariosId = ShiroUtil.getOpenScenariosId();
        String oldCarName = solutionRouteService.findRouteCar(scenariosId,routeCount);

        Map<String,Object> param = Maps.newHashMap();
        param.put("routeCount",routeCount);
        param.put("carName",carName);
        param.put("scenariosId",scenariosId);

        if(!Strings.isEmpty(oldCarName)){
            solutionRouteService.updateCarToIdle(scenariosId,oldCarName);
            solutionRouteService.updateCarName(param);
            //把车的状态更新为busy
            solutionRouteService.updateCarToBusy(scenariosId,carName);
        }
        else{

            solutionRouteService.updateCarName(param);
            //把车的状态更新为busy
            solutionRouteService.updateCarToBusy(scenariosId,carName);
        }

        return "success";
    }

    /**把车的状态更新为busy
     * @return
     */
    @RequestMapping(value = "/updateCarToBusy",method = RequestMethod.POST)
    @ResponseBody
    public void updateCarToBusy(String scenariosId,String carName) {
        solutionRouteService.updateCarToBusy(scenariosId,carName);
    }

    /**把车的状态更新为idle
     * @return
     */
    @RequestMapping(value = "/updateCarToIdle",method = RequestMethod.POST)
    @ResponseBody
    public void updateCarToIdle(String scenariosId,String carName) {
        solutionRouteService.updateCarToIdle(scenariosId,carName);
    }



}
