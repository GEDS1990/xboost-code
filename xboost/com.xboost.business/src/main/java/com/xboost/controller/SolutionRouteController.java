package com.xboost.controller;

import com.google.common.collect.Maps;
import com.mckinsey.sf.data.Car;
import com.xboost.pojo.CarLicence;
import com.xboost.pojo.DemandInfo;
import com.xboost.pojo.Route;
import com.xboost.pojo.SiteInfo;
import com.xboost.service.*;
import com.xboost.util.RedisUtil;
import com.xboost.util.ShiroUtil;
import com.xboost.util.Strings;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

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
@RequestMapping("/route")
public class SolutionRouteController {
    @Inject
    private SolutionRouteService solutionRouteService;
    @Inject
    private DemandInfoService demandInfoService;
    @Inject
    private CarService carService;
    @Inject
    private MyScenariosService myScenariosService;

    @Inject
    private RedisUtil redisUtil;

    private static Logger logger = LoggerFactory.getLogger(SolutionRouteController.class);

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

        // 判断是否有缓存
        Object value = null;
        String key = redisUtil.getKey(request);
        key = key+"-"+start+"-"+length+"-"+searchValue+"-"+orderColumnIndex+"-"+orderType
                +"-"+orderColumnName;//!!!!!!!!!!
        if (redisUtil.exists(key)) {
            value = redisUtil.get(key);
            logger.info("----获取缓存---key="+key);
            return (Map<String,Object>)value;
        }

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

                        String curLoc = (String) routeList.get(i).get("curLoc");
                        String nextCurLoc = (String) routeList.get(i).get("nextCurLoc");
                        String calcDis = (String) routeList.get(i).get("calcDis");
                        if(!curLoc.equals(nextCurLoc)) {
                            routeList.get(i).put("nextCurLoc",nextCurLoc);
                            routeList.get(i).put("calcDis",calcDis);
                        }

                        String curLoc2 = (String) routeList.get(j).get("curLoc");
                        String nextCurLoc2 = (String) routeList.get(j).get("nextCurLoc");
                        String calcDis2 = (String) routeList.get(j).get("calcDis");
                        if(!curLoc2.equals(nextCurLoc2)) {
                            routeList.get(i).put("nextCurLoc",nextCurLoc2);
                            routeList.get(i).put("calcDis",calcDis2);
                        }

                        routeList.remove(j);
                        count = count -1;
                        filteredCount = filteredCount-1;
                    }

                }
            }
        }
        String modelType = myScenariosService.findById(Integer.parseInt(ShiroUtil.getOpenScenariosId())).getScenariosModel();
        
        if ("2".equals(modelType)) {
            for (Map map : routeList) {
                if (map.get("sequence").equals("1")) {
                    map.put("arrTime", "--");
                }
                if (map.get("sequence").equals("2")) {
                    map.put("endTime", "--");
                }
            }
        }


        Collections.sort(routeList, new Comparator<Map<String, Object>>() {
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
        result.put("data",routeList);
        result.put("modelType", modelType);

        redisUtil.set(key,result);
        logger.info("----加入缓存---key="+key);

        return result;
    }

    //查询路线id
    @RequestMapping(value = "/planCar.json",method = RequestMethod.GET,produces = "application/json;charset=UTF-8")
    @ResponseBody
    public Map<String,Object> findIdleCar(String routeCount) {
        Map<String,Object> result = Maps.newHashMap();
        List<String> idleCar = solutionRouteService.findIdleCar(ShiroUtil.getOpenScenariosId(),routeCount);
        List<String> usingCar = solutionRouteService.findUsingCar1(ShiroUtil.getOpenScenariosId());

        String modelType = myScenariosService.findById(Integer.parseInt(ShiroUtil.getOpenScenariosId())).getScenariosModel();
        result.put("modelType", modelType);
        result.put("usingCar",usingCar);
        result.put("idleCar",idleCar);
        return result;
    }

    //根据所有可选择的车
    @RequestMapping(value = "/planCarRelay.json",method = RequestMethod.GET,produces = "application/json;charset=UTF-8")
    @ResponseBody
    public Map<String,Object> findIdleCarRelay(String routeCount) {
        Map<String,Object> result = Maps.newHashMap();

        List<Route> routes = solutionRouteService.findRouteByRouteCount(ShiroUtil.getOpenScenariosId(), routeCount);
        String arrTime = routes.get(0).getArrTime();

        int min = Integer.parseInt(demandInfoService.findMin(ShiroUtil.getOpenScenariosId()));
        int max = Integer.parseInt(demandInfoService.findMax(ShiroUtil.getOpenScenariosId()));
        String openScenariosId = ShiroUtil.getOpenScenariosId();
        int timeNodeNum = (max - min) / 20 == 0 ? (max - min) / 20 : ((max - min) / 20) + 1;

        // 从0开始，起始为min
        int timeId = (Integer.parseInt(arrTime) - 840)/20;
        Set<String> idleCar = new HashSet<>();
        Set<String> usingCar = new HashSet<>();

        List<CarLicence> carList = carService.findAllCar(ShiroUtil.getOpenScenariosId());
        for (CarLicence car : carList) {
            String busyIdle = car.getBusyIdle();
            String name = car.getName();
            System.out.println(busyIdle+""+name);
            char[] chars = new char[timeNodeNum];
            if (busyIdle != null && !"0".equals(busyIdle+"")) {
                chars = busyIdle.toCharArray();
            }

            if(busyIdle == null || "0".equals(busyIdle+"")) {
                idleCar.add(car.getName());
            }else if("0".equals(""+chars[timeId])){
                idleCar.add(car.getName());
                usingCar.add(car.getName());
            }
        }
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
    public Map<String,Object> totalDistance(String routeCount) {
        String totalDistance =solutionRouteService.findTotalDistance(ShiroUtil.getOpenScenariosId(),routeCount);
        String carName = solutionRouteService.findRouteCar(ShiroUtil.getOpenScenariosId(),routeCount);
        Map<String,Object> result = Maps.newHashMap();

        result.put("totalDistance",totalDistance);
        result.put("carName",carName);

        return result;
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

    /**Relay
     * 排车更新route表carName
     * @return
     */
    @RequestMapping(value = "/updateCarNameRelay",method = RequestMethod.POST)
    @ResponseBody
    public String updateCarNameRelay(HttpServletRequest request) {
        String routeCount = request.getParameter("routeCount");
        String carName = request.getParameter("carName");
        String scenariosId = ShiroUtil.getOpenScenariosId();
        String oldCarName = solutionRouteService.findRouteCar(scenariosId,routeCount);

        int min = Integer.parseInt(demandInfoService.findMin(ShiroUtil.getOpenScenariosId()));
        int max = Integer.parseInt(demandInfoService.findMax(ShiroUtil.getOpenScenariosId()));
        int timeNodeNum = (max - min) / 20 == 0 ? (max - min) / 20 : ((max - min) / 20) + 1;

        List<Route> routes = solutionRouteService.findRouteByRouteCount(ShiroUtil.getOpenScenariosId(), routeCount);
        String arrTime = routes.get(0).getArrTime();
        // 从0开始，起始为min
        int timeId = (Integer.parseInt(arrTime) - 840)/20;

        Map<String,Object> param = Maps.newHashMap();
        param.put("routeCount",routeCount);
        param.put("name",carName);
        param.put("carName",carName);
        param.put("scenariosId",scenariosId);

        CarLicence carLincence = carService.findCarLincence(param);
        String busyIdle = carLincence.getBusyIdle();
        if (busyIdle == null || "0".equals(busyIdle)) {
            StringBuilder idle = new StringBuilder();
            for (int i = 0; i < timeNodeNum; i++) {
                if (i == timeId) {
                    idle.append("1");
                    continue;
                }
                idle.append("0");
            }
            busyIdle = idle.toString();
        }else {
            char[] idles = busyIdle.toCharArray();
            for (int i = 0; i < idles.length; i++) {
                if (i == timeId) {
                    idles[i] = '1';
                }
            }
            busyIdle = String.valueOf(idles);
        }

        param.put("busyIdle", busyIdle);
        carService.updateCarLincence(param);

        solutionRouteService.updateCarName(param);

//        if(!Strings.isEmpty(oldCarName)){
//            solutionRouteService.updateCarToIdle(scenariosId,oldCarName);
//            solutionRouteService.updateCarName(param);
//            //把车的状态更新为busy
//            solutionRouteService.updateCarToBusy(scenariosId,carName);
//        }
//        else{
//
//            solutionRouteService.updateCarName(param);
//            //把车的状态更新为busy
//            solutionRouteService.updateCarToBusy(scenariosId,carName);
//        }

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

    @RequestMapping(value = "/exportResult",method = RequestMethod.GET,produces = {"application/vnd.ms-excel;charset=UTF-8"})
    @ResponseBody
    public String exportResult(HttpServletResponse response)
    {
        response.setContentType("application/vnd.ms-excel;charset=utf-8");
        try
        {
            ServletOutputStream outputStream = response.getOutputStream();
            String fileName = new String(("Result_Route").getBytes(), "utf-8");
            response.setCharacterEncoding("utf-8");
            response.setHeader("Content-disposition", "attachment; filename=" + fileName + ".xlsx");// 组装附件名称和格式
            String scenariosId = ShiroUtil.getOpenScenariosId();
            String[] titles = { "Route ID","Depot Order","Depot ID","Depot Name","Depot Address","Arrival Time",
                                "Operation","Departure Time","Next Depot","Next Depot Distance","Car Type"};
            String modelType = myScenariosService.findById(Integer.parseInt(ShiroUtil.getOpenScenariosId())).getScenariosModel();
            solutionRouteService.exportResult(scenariosId,titles,outputStream,modelType);
            System.out.println("outputStream:"+outputStream);
        }
        catch (IOException e)
        {
            e.printStackTrace();
            System.out.println("网络连接故障!错误信息:"+e.getMessage());
        }
        return null;
    }

    @RequestMapping(value="/inputRoutesExcel", method = RequestMethod.POST)
    @ResponseBody
    public String inputRoutesExcel(Route route, @RequestParam MultipartFile[] file) {
        String scenariosId = ShiroUtil.getOpenScenariosId();
        //设置场景Id
        route.setScenariosId(scenariosId);
        String modelType = myScenariosService.findById(Integer.parseInt(ShiroUtil.getOpenScenariosId())).getScenariosModel();
        solutionRouteService.updateRouteByExcel(scenariosId, route, file, modelType);
        return "redirect:/siteInfo";
    }
}
