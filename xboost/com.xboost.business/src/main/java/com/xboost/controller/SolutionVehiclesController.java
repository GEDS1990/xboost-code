package com.xboost.controller;

import com.google.common.collect.Maps;
import com.xboost.mapper.SolutionVehiclesMapper;
import com.xboost.pojo.Route;
import com.xboost.pojo.SiteInfo;
import com.xboost.service.SiteInfoService;
import com.xboost.service.SolutionRouteService;
import com.xboost.service.SolutionVehiclesService;
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
@RequestMapping("/vehicles")
public class SolutionVehiclesController {
    @Inject
    private SolutionVehiclesService solutionVehiclesService;
    @Inject
    private SiteInfoService siteInfoService;
    @Inject
    private SolutionRouteService solutionRouteService;

    @RequestMapping(method = RequestMethod.GET)
    public String list() {
        return "results/vehicles";
    }



    //查询网点操作信息
    @RequestMapping(value = "/vehicles.json",method = RequestMethod.GET,produces = "application/json;charset=UTF-8")
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
        String searchValue = request.getParameter("search[value]");
        String orderColumnIndex = request.getParameter("order[0][column]");
        String orderType = request.getParameter("order[0][dir]");
        String orderColumnName = request.getParameter("columns["+orderColumnIndex+"][name]");

        Map<String,Object> param = Maps.newHashMap();
        param.put("start",start);
        param.put("length",length);
        if(StringUtils.isNotEmpty(searchValue)) {
            param.put("keyword", Strings.toUTF8(searchValue));
        }
        param.put("orderColumn",orderColumnName);
        param.put("orderType",orderType);
        param.put("scenariosId",ShiroUtil.getOpenScenariosId());



        Map<String,Object> result = Maps.newHashMap();
        Integer usingCarCount = solutionVehiclesService.findBusyCarCount(ShiroUtil.getOpenScenariosId());

        List<Map<String,Object>> vehiclesList = solutionVehiclesService.findByCar(param); //.findAll();
        Integer count = solutionVehiclesService.findAllCountByCar(ShiroUtil.getOpenScenariosId());
        Integer filteredCount = solutionVehiclesService.findCountByCar(param);

//        List<Map<String,Object>> vehiclesList;
//        Integer count;
//        Integer filteredCount;
//
//        if(usingCarCount>0){
//            vehiclesList = solutionVehiclesService.findByCar(param); //.findAll();
//            count = solutionVehiclesService.findAllCountByCar(ShiroUtil.getOpenScenariosId());
//            filteredCount = solutionVehiclesService.findCountByCar(param);
//        }
//        else{
//            vehiclesList = null;
//            count = 0;
//            filteredCount = 0;
//        }


        result.put("draw",draw);
        result.put("recordsTotal",count); //总记录数
        result.put("recordsFiltered",filteredCount); //过滤出来的数量
        result.put("data",vehiclesList);
        return result;
    }

//    //查询网点信息
//    @RequestMapping(value = "/baseInfo.json",method = RequestMethod.GET,produces = "application/json;charset=UTF-8")
//    @ResponseBody
//    public SiteInfo loadBaseSiteInfo(String siteCode) {
//       String scenariosId = ShiroUtil.getOpenScenariosId();
//       SiteInfo siteInfo = siteInfoService.findSiteInfoBySiteCode(scenariosId,siteCode);
//       System.out.println("success");
//       return siteInfo;
//    }
//
//    //查询网点编码
//    @RequestMapping(value = "/siteCode.json",method = RequestMethod.GET,produces = "application/json;charset=UTF-8")
//    @ResponseBody
//    public Map<String,Object> findSiteCode(HttpServletRequest request) {
//        String scenariosId = ShiroUtil.getOpenScenariosId();
//        String siteCode = siteInfoService.findSiteCode(scenariosId);
//        Map<String,Object> result = Maps.newHashMap();;
//        result.put("data",result);
//        return result;
//    }

}
