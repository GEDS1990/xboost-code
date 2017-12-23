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

        List<Map<String,Object>> routeList = solutionRouteService.findByRoute(param); //.findAll();
        Integer count = solutionRouteService.findAllCountByRoute(ShiroUtil.getOpenScenariosId());
        Integer filteredCount = solutionRouteService.findCountByRoute(param);
  //      String totalDistance = solutionRouteService.findTotalDistance(ShiroUtil.getOpenScenariosId(),searchValue);

        List<String> usingCar = solutionRouteService.findUsingCar(ShiroUtil.getOpenScenariosId());
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
    @RequestMapping(value = "/idleCar.json",method = RequestMethod.GET,produces = "application/json;charset=UTF-8")
    @ResponseBody
    public List<String> findIdleCar(String routeCount) {
        List<String> idleCar = solutionRouteService.findIdleCar(ShiroUtil.getOpenScenariosId(),routeCount);
        return idleCar;
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


    //查询路线id
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

        Map<String,Object> param = Maps.newHashMap();
        param.put("routeCount",routeCount);
        param.put("carName",carName);
        param.put("scenariosId",scenariosId);


        solutionRouteService.updateCarName(param);
        //把车的状态更新为busy
        solutionRouteService.updateCarToBusy(scenariosId,carName);

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
