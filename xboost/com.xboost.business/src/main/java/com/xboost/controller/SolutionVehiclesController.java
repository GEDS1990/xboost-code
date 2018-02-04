package com.xboost.controller;

import com.google.common.collect.Maps;
import com.xboost.mapper.SolutionVehiclesMapper;
import com.xboost.pojo.Route;
import com.xboost.pojo.SiteInfo;
import com.xboost.service.MyScenariosService;
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
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

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
    @Inject
    private MyScenariosService myScenariosService;

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
            param.put("keyword", searchValue);
        }
        param.put("orderColumn",orderColumnName);
        param.put("orderType",orderType);
        param.put("scenariosId",ShiroUtil.getOpenScenariosId());



        Map<String,Object> result = Maps.newHashMap();
        Integer usingCarCount = solutionVehiclesService.findBusyCarCount(ShiroUtil.getOpenScenariosId());

        String modelType = myScenariosService.findById(Integer.parseInt(ShiroUtil.getOpenScenariosId())).getScenariosModel();
        List<Map<String,Object>> vehiclesList = new ArrayList<>();
        if ("1".equals(modelType)) {
            vehiclesList = solutionVehiclesService.findByCar(param); //.findAll();
        }
        if ("2".equals(modelType)) {
            vehiclesList = solutionVehiclesService.findByCarRealy(param); //.findAll();
        }

        Integer count = solutionVehiclesService.findAllCountByCar(ShiroUtil.getOpenScenariosId());
        Integer filteredCount = solutionVehiclesService.findCountByCar(param);

        Double sbVolSum;
        Double unloadVolSum;
        String sbVol;
        String unloadVol;
        if(""!=searchValue)
        {
            for(int i=0;i<vehiclesList.size();i++)
            {
                Map<String,Object> vehicle = vehiclesList.get(i);
                for(int j=i+1;j<vehiclesList.size();j++)
                {
                    if(vehicle.get("sequence").equals(vehiclesList.get(j).get("sequence"))){
//                        sbVolSum = Double.parseDouble(vehicle.get("sbVolSum").equals("0")?"":vehicle.get("sbVolSum").toString())
//                                +Double.parseDouble(vehiclesList.get(j).get("sbVolSum").equals("0")?"":vehiclesList.get(j).get("sbVolSum").toString());
//                        unloadVolSum = Double.parseDouble(vehicle.get("unloadVolSum").equals("0")?"":vehicle.get("unloadVolSum").toString())
//                                +Double.parseDouble(vehiclesList.get(j).get("unloadVolSum").equals("0")?"":vehiclesList.get(j).get("unloadVolSum").toString());
                        sbVol = (vehicle.get("sbVol").equals("0")?"":vehicle.get("sbVol").toString())
                                +(vehiclesList.get(j).get("sbVol").equals("0")?"":vehiclesList.get(j).get("sbVol").toString());
                        unloadVol = (vehicle.get("unloadVol").equals("0")?"":vehicle.get("unloadVol").toString())
                                +(vehiclesList.get(j).get("unloadVol").equals("0")?"":(vehiclesList.get(j).get("unloadVol").toString()));

//                        vehiclesList.get(i).put("sbVolSum",sbVolSum);
//                        vehiclesList.get(i).put("unloadVolSum",unloadVolSum);
                        vehiclesList.get(i).put("sbVol",sbVol);
                        vehiclesList.get(i).put("unloadVol",unloadVol);

                        String curLoc = (String) vehiclesList.get(i).get("curLoc");
                        String nextCurLoc = (String) vehiclesList.get(i).get("nextCurLoc");
                        String calcDis = (String) vehiclesList.get(i).get("calcDis");
                        if(!curLoc.equals(nextCurLoc)) {
                            vehiclesList.get(i).put("nextCurLoc",nextCurLoc);
                            vehiclesList.get(i).put("calcDis",calcDis);
                        }

                        String curLoc2 = (String) vehiclesList.get(j).get("curLoc");
                        String nextCurLoc2 = (String) vehiclesList.get(j).get("nextCurLoc");
                        String calcDis2 = (String) vehiclesList.get(j).get("calcDis");
                        if(!curLoc2.equals(nextCurLoc2)) {
                            vehiclesList.get(i).put("nextCurLoc",nextCurLoc2);
                            vehiclesList.get(i).put("calcDis",calcDis2);
                        }

                        vehiclesList.remove(j);
                        count = count -1;
                        filteredCount = filteredCount-1;
                    }
                }
            }
        }

        Collections.sort(vehiclesList, new Comparator<Map<String, Object>>() {
            @Override
            public int compare(Map<String, Object> o1, Map<String, Object> o2) {
                Integer s1 = Integer.parseInt(o1.get("sequence")+"");
                Integer s2 = Integer.parseInt(o2.get("sequence")+"");
                return s1.compareTo(s2);
            }
        });

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

    @RequestMapping(value = "/exportResult",method = RequestMethod.GET,produces = {"application/vnd.ms-excel;charset=UTF-8"})
    @ResponseBody
    public String exportResult(HttpServletResponse response) {
        response.setContentType("application/vnd.ms-excel;charset=utf-8");
        try {
            ServletOutputStream outputStream = response.getOutputStream();
            String fileName = new String(("Result-Vehicle").getBytes(), "utf-8");
            response.setCharacterEncoding("utf-8");
            response.setHeader("Content-disposition", "attachment; filename=" + fileName + ".xlsx");// 组装附件名称和格式
            //       response.setHeader("Content-disposition", "attachment; filename=distance.xlsx");
            String scenariosId = ShiroUtil.getOpenScenariosId();
            String[] titles = { "Vehicle ID","Depot Order","Depot ID","Depot Name","Depot Address",
                    "Arrival Time","Operation","Departure Time","Next Depot","Next Depot Distance" };
            solutionVehiclesService.exportResult(scenariosId,titles,outputStream);
            //       System.out.println("outputStream:"+outputStream);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
