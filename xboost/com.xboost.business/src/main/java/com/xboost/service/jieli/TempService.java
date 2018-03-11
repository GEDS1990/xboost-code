package com.xboost.service.jieli;

import com.google.common.collect.Maps;
import com.mckinsey.sf.data.solution.ArrInfo;
import com.xboost.mapper.ArrInfoMapper;
import com.xboost.mapper.CarMapper;
import com.xboost.mapper.RideMapper;
import com.xboost.mapper.jieli.TempMapper;
import com.xboost.pojo.*;
import com.xboost.pojo.jieli.Temp;
import com.xboost.service.*;
import com.xboost.util.ShiroUtil;
import org.apache.spark.sql.sources.In;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import javax.inject.Named;
import java.text.ParseException;
import java.util.*;

@Named
@Transactional
public class TempService {
    /**
     * 根据查询条件获取模
     * @param param
     * @return
     */
    @Inject
    TempMapper tempMapper;
    @Inject
    RideMapper rideMapper;
    @Inject
    CarMapper carMapper;

    @Inject
    SolutionRouteService solutionRouteService;
    @Inject
    SolutionCostService solutionCostService;
    @Inject
    SiteDistService siteDistService;

    @Inject
    private  SolutionRideService solutionRideService;

    @Inject
    private JieliResultService jieliResultService;

    @Inject
    private SiteInfoService siteInfoService;

    public void saveTempInfo(Temp temp) {
        tempMapper.saveTemp(temp);
    }

    public void saveRouteOpt(Map ro) {
        tempMapper.saveRouteOpt(ro);
    }

    public void saveOD_demand(Map od) {
        tempMapper.saveOD_demand(od);
    }

    public void savedistance_ref(Map dr) {
        tempMapper.savedistance_ref(dr);
    }

    public Route setPickup(int i,JieliResult jieliResult,Route route,String openScenariosId){
        route.setScenariosId(openScenariosId);
        route.setRouteCount(String.valueOf(i+1));
        route.setLocation(siteInfoService.findSiteCodeById(Integer.parseInt(jieliResult.getInboundId()))+"-"
                +siteInfoService.findSiteCodeById(Integer.parseInt(jieliResult.getOutboundId())));
        route.setSequence(String.valueOf(1));
        route.setCurLoc(siteInfoService.findSiteCodeById(Integer.parseInt(jieliResult.getInboundId())));
        route.setType("PICKUP");
        route.setSbVol(jieliResult.getVolume());
        route.setSbVolSum(jieliResult.getVolume());
//        route.setArrTime((Integer.parseInt(jieliResult.getTimeId())-1)*10 + 780 +"");
//        route.setEndTime((Integer.parseInt(jieliResult.getTimeId()))*10 + 780 +"");
        route.setArrTime("--");
        route.setEndTime((Integer.parseInt(jieliResult.getTimeId())-1)*10 + 780 +"");
        route.setUnloadLoc("0");
        route.setUnloadVol("0");
        route.setUnloadVolSum("0");
        route.setNextCurLoc(siteInfoService.findSiteCodeById(Integer.parseInt(jieliResult.getOutboundId())));
        route.setCalcDis(jieliResult.getDistance());
        return route;
    }

    public Route setDelivery(int i,JieliResult jieliResult,Route route,String openScenariosId){
        route.setScenariosId(openScenariosId);
        route.setRouteCount(String.valueOf(i+1));
        route.setLocation(siteInfoService.findSiteCodeById(Integer.parseInt(jieliResult.getInboundId()))+"-"
                +siteInfoService.findSiteCodeById(Integer.parseInt(jieliResult.getOutboundId())));
        route.setSequence(String.valueOf(2));
        route.setCurLoc(siteInfoService.findSiteCodeById(Integer.parseInt(jieliResult.getOutboundId())));
        route.setType("DELIVER");
        route.setSbVol("0");
        route.setSbVolSum("0");
//        route.setArrTime((Integer.parseInt(jieliResult.getTimeId())-1)*10 + 780 +"");
//        route.setEndTime((Integer.parseInt(jieliResult.getTimeId()))*10 + 780 +"");
        route.setArrTime((Integer.parseInt(jieliResult.getTimeId()))*10 + 780 +"");
        route.setEndTime("--");
        route.setUnloadLoc(siteInfoService.findSiteCodeById(Integer.parseInt(jieliResult.getOutboundId())));
        route.setUnloadVol(jieliResult.getVolume());
        route.setUnloadVolSum(jieliResult.getVolume());
        route.setNextCurLoc("");
        route.setCalcDis("0.00");
        return route;
    }
    public void saveRoute(int i,JieliResult jieliResult,Route route,String openScenariosId){
        route = setPickup(i,jieliResult,route,openScenariosId);
        solutionRouteService.addRoute(route);
        route = setDelivery(i,jieliResult,route,openScenariosId);
        solutionRouteService.addRoute(route);
    }
    public void saveConnectionOpt(List<Map> jieliResults, double gurobyCost, String openScenariosId) {
        solutionRouteService.delByScenariosId(Integer.parseInt(openScenariosId));
        solutionRouteService.updateAllCarToIdle(ShiroUtil.getOpenScenariosId());//更新车辆为可用
        String siteCode="";
        for(int i=0;i<jieliResults.size();i++){

            JieliResult jieliResult = new JieliResult();
            jieliResult.setTimeId(jieliResults.get(i).get("time_id").toString());
            jieliResult.setDistance(jieliResults.get(i).get("distance").toString());
            jieliResult.setDidiNum(jieliResults.get(i).get("didi").toString());;
            jieliResult.setMinutes(jieliResults.get(i).get("minutes").toString());
            jieliResult.setTruckNum(jieliResults.get(i).get("truck").toString());
            jieliResult.setOutboundId(jieliResults.get(i).get("outbound_id").toString());
            jieliResult.setBikeNum(jieliResults.get(i).get("bike").toString());
            jieliResult.setInboundId(jieliResults.get(i).get("inbound_id").toString());
            jieliResult.setVolume(jieliResults.get(i).get("volume").toString());
            jieliResult.setRouteCnt(jieliResults.get(i).get("route_cnt").toString());
            jieliResult.setCrossRiver(jieliResults.get(i).get("cross_river").toString());
            jieliResult.setDadaNum(jieliResults.get(i).get("dada").toString());
            jieliResult.setjConnectionId(jieliResults.get(i).get("j_connection_id").toString());
            jieliResult.setConnection(jieliResults.get(i).get("connection").toString());
            jieliResult.setTimeBucket(jieliResults.get(i).get("timebucket").toString());

            Route route = new Route();

            if(Double.parseDouble(jieliResult.getDidiNum())>0){
                route.setCarType("didi");
                route.setStr1(jieliResult.getDidiNum());
                saveRoute(i,jieliResult,route,openScenariosId);
            }
            if(Double.parseDouble(jieliResult.getDadaNum())>0){
                route.setCarType("dada");
                route.setStr1(jieliResult.getDadaNum());
                saveRoute(i,jieliResult,route,openScenariosId);
            }
            if(Double.parseDouble(jieliResult.getBikeNum())>0){
                route.setCarType("baidu");
                route.setStr1(jieliResult.getBikeNum());
                saveRoute(i,jieliResult,route,openScenariosId);
            }
            if(Double.parseDouble(jieliResult.getTruckNum())>0){
                route.setCarType("truck");
                route.setStr1(jieliResult.getTruckNum());
                saveRoute(i,jieliResult,route,openScenariosId);
            }

        }
        //carType

        ///cost
        Cost cost = new Cost();
        solutionCostService.delByScenariosId(Integer.parseInt(openScenariosId));
        cost.setScenariosId(openScenariosId);
        double totalPiece = solutionCostService.findTotalPiece(openScenariosId);
        cost.setBranchTransportCost(String.valueOf(gurobyCost/totalPiece));
        solutionCostService.add(cost);

//        solutionRouteService.addRoute(route);
        solutionRideService.delByScenariosId(openScenariosId);
        List<Route> routeList = solutionRouteService.findAllRoute(openScenariosId);
        rideResult(routeList);
    }

    public void rideResult(List<Route> jieliResults){
        int i=1;
        String scenariosId = ShiroUtil.getOpenScenariosId();
        Map<Integer,List> rideMap = Maps.newHashMap();
        while (null != jieliResults && jieliResults.size()>0){
            Ride ride = new Ride();
            Map<String,List<Route>> result = ride(jieliResults);
            List<Route> rideList= result.get("rideList");
            rideMap.put(i,rideList);
            for(int y=0;y<rideList.size();y++)
            {
                ride.setScenariosId(scenariosId);
                ride.setRideId(String.valueOf(i));
                ride.setRouteCount(rideList.get(y).getRouteCount());
                ride.setSequence(String.valueOf(y+1));
                ride.setCurLoc(rideList.get(y).getCurLoc());
                ride.setNextCurLoc(rideList.get(y).getNextCurLoc());
                ride.setCarType(rideList.get(y).getCarType());
                ride.setArrTime(rideList.get(y).getArrTime());
                ride.setEndTime(rideList.get(y).getEndTime());
                ride.setSbVol(rideList.get(y).getSbVol());
                ride.setUnloadVol(rideList.get(y).getUnloadVol());
                ride.setCarGoods(rideList.get(y).getNextCurLoc());
                ride.setCalcDis(rideList.get(y).getCalcDis());
                rideMapper.save(ride);
            }
            i++;

        }
    }

    public Map<String,List<Route>> ride(List<Route> jieliResults){
        Map<String,List<Route>> result1= firstRoute(jieliResults);
        List<Route> rideList= result1.get("rideList");
        List<Route> jieliResultList= result1.get("jieliResults");

        if (null != jieliResults && jieliResults.size()>0){
            for(int i=0;i<jieliResults.size();i++){
                Map<String,List<Route>> result = autoSplic(jieliResultList,rideList);
                rideList = result.get("rideList");
                jieliResults= result.get("jieliResults");
            }
        }


        Map<String,List<Route>> result = Maps.newHashMap();
        result.put("rideList",rideList);
        return result;
    }

    //按照sequeue正序排列;
    public void ascBySequence(List<Route> rideList) {
        if (null != rideList && rideList.size() > 0) {
            //   final SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
            Collections.sort(rideList, new Comparator<Route>() {
                @Override
                public int compare(Route o1, Route o2) {
                    int ret = 0;
                    try {
                        //比较两个对象的顺序，如果前者小于、等于或者大于后者，则分别返回-1/0/1
                        ret = o1.getSequence().compareTo(o2.getSequence());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return ret;
                }
            });
        }
    }

    public Map<String,List<Route>> firstRoute(List<Route> jieliResults){
        if (null != jieliResults && jieliResults.size()>0) {
            //   final SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
            Collections.sort(jieliResults, new Comparator<Route>() {
                @Override
                public int compare(Route o1, Route o2) {
                    int ret = 0;
                    try {
                        //比较两个对象的顺序，如果前者小于、等于或者大于后者，则分别返回-1/0/1
                        ret = o1.getEndTime().compareTo(o2.getEndTime());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return ret;
                }
            });
        }

        List<Route> rideList = new ArrayList<>();
        String routeCount = jieliResults.get(0).getRouteCount();
        Map<String,List<Route>> tempResult = oneRoute(jieliResults,routeCount);
        List<Route> tempList = tempResult.get("tempList");
        ascBySequence(tempList);
        rideList.add(tempList.get(0));
        rideList.add(tempList.get(1));

        System.out.println("first route: route count ="+rideList.get(0).getRouteCount()
                +"  sequence="+rideList.get(0).getSequence());
        System.out.println("first route: route count ="+rideList.get(1).getRouteCount()
                +"  sequence="+rideList.get(1).getSequence());

        for(int j=0;j<jieliResults.size();j++)
        {
            if(routeCount.equals(jieliResults.get(j).getRouteCount())){

                jieliResults.remove(jieliResults.get(j));
            }
        }
        if(1==jieliResults.size()){

            jieliResults.clear();
        }

        Map<String,List<Route>> result = Maps.newHashMap();
        if(null ==jieliResults || jieliResults.size()==0)
        {
            System.out.println("jieliResults is null");
//            result.put("jieliResults",null);
        }else{
            result.put("jieliResults",jieliResults);
        }
        result.put("rideList",rideList);
        return result;

    }

    public Map<String,List<Route>> autoSplic(List<Route> jieliResults,List<Route> rideList) {
  //      solutionRouteService.delByScenariosId(Integer.parseInt(openScenariosId));

        for(int i=0;i<jieliResults.size();i++){
            Route ride = jieliResults.get(i);
            String routeCount = ride.getRouteCount();
            System.out.println("out of bound: "+routeCount);
            System.out.println("jieliResult size :" +jieliResults.size());

            List<Route> tempList = new ArrayList<>();
            Map<String,List<Route>> result=oneRoute(jieliResults,routeCount);
            tempList = result.get("tempList");
            rideList.add(tempList.get(0));
            rideList.add(tempList.get(1));
            if(!isQualifiedCondition(rideList)){
                rideList.remove(rideList.size()-1);
                rideList.remove(rideList.size()-1);
            }else {
                for(int j=0;j<jieliResults.size();j++)
                {
                    if(routeCount.equals(jieliResults.get(j).getRouteCount())){

                        jieliResults.remove(jieliResults.get(j));
                    }
                }
                break;
            }
        }

        if(rideList.size()>2){

            Route ride1 = rideList.get(rideList.size()-2);
            Route ride2 = rideList.get(rideList.size()-3);
            if(ride1.getCurLoc().equals(ride2.getCurLoc())){
                rideList.get(rideList.size()-2).setUnloadVol(ride2.getUnloadVol());
                rideList.remove(rideList.size()-3);
            }else{
                rideList.get(rideList.size()-3).setNextCurLoc(ride1.getCurLoc());
                Map<String,Object> param = Maps.newHashMap();
                param.put("scenariosId",ShiroUtil.getOpenScenariosId());
                param.put("siteCollect",ride2.getCurLoc());
                param.put("siteDelivery",ride1.getCurLoc());
                String distance = siteDistService.findDistance(param);
                rideList.get(rideList.size()-3).setCalcDis(distance);
            }
        }
        Map<String,List<Route>> result = Maps.newHashMap();
        result.put("jieliResults",jieliResults);
        result.put("rideList",rideList);
        return result;
    }

    public Map<String,List<Route>> oneRoute(List<Route> jieliResults,String routeCount){
        List<Route> tempList = new ArrayList<>();
        for(int j=0;j<jieliResults.size();j++)
        {
            Route ride = jieliResults.get(j);
            if(routeCount.equals(ride.getRouteCount())){

                tempList.add(jieliResults.get(j));
            }
        }
        ascBySequence(tempList);
        Map<String,List<Route>> result = Maps.newHashMap();
        result.put("jieliResults",jieliResults);
        result.put("tempList",tempList);
        return result;
    }


    public boolean isQualifiedCondition(List<Route> rideList){
        boolean result=true;
        if(isSameCarType(rideList) && isOnTime(rideList) && isMaxDistance(rideList) && isMaxRunTime(rideList) && isMaxStop(rideList)){
            result = true;
        }else{
            result = false;
        }



        return result;
    }

    public boolean isOnTime(List<Route> rideList){
        String scenariosId = ShiroUtil.getOpenScenariosId();
        boolean result=true;

        String oriSite = rideList.get(rideList.size()-3).getCurLoc();
        String desSite = rideList.get(rideList.size()-2).getCurLoc();
        String oriArrTime = rideList.get(rideList.size()-3).getArrTime();
        String desEndTime = rideList.get(rideList.size()-2).getEndTime();
        Map<String,Object> param = Maps.newHashMap();
        param.put("scenariosId",scenariosId);
        param.put("siteCollect",oriSite);
        param.put("siteDelivery",desSite);
        SiteDist siteDist = siteDistService.findTime(param);
        double time=0;
        String carType = rideList.get(0).getCarType();
        if(carType.equals("truck")){
            time = Double.parseDouble(siteDist.getDurationNightDelivery());
        }else if(carType.equals("baidu")){
            time = Double.parseDouble(siteDist.getDurationNightDelivery2());
        }
        else if(carType.equals("didi")){
            time = Double.parseDouble(siteDist.getDurationNightDelivery3());
        }else if(carType.equals("dada")){
            time = Double.parseDouble(siteDist.getDurationNightDelivery4());
        }else {
            time = Double.parseDouble(siteDist.getDurationNightDelivery5());
        }

        if(Double.parseDouble(desEndTime)-Double.parseDouble(oriArrTime)>=time){
            result = true;
        }else {
            result = false;
        }


        return result;
    }
    public boolean isMaxDistance(List<Route> rideList){
        String scenariosId = ShiroUtil.getOpenScenariosId();
        double sumDisTance=0.0;
        boolean result=true;
        for(int i=0;i<rideList.size();i++)
        {
            sumDisTance += Double.parseDouble(rideList.get(i).getCalcDis());
        }
        String carType=rideList.get(0).getCarType();
        Double maxDistance = carMapper.findByCarType(scenariosId,carType).getMaxDistance();

        if(sumDisTance<=maxDistance)
        {
            result = true;
        }else {
            result = false;
        }

        return result;
    }
    public boolean isMaxStop(List<Route> rideList){
        String scenariosId = ShiroUtil.getOpenScenariosId();
        boolean result=true;
        String carType=rideList.get(0).getCarType();
        int sumStop = rideList.size();
        int maxStop = carMapper.findByCarType(scenariosId,carType).getMaxStop();

        if(sumStop<=maxStop)
        {
            result = true;
        }else {
            result = false;
        }

        return result;
    }
    public boolean isMaxRunTime(List<Route> rideList){
        String scenariosId = ShiroUtil.getOpenScenariosId();
        boolean result=true;
        String carType=rideList.get(0).getCarType();
        String endTime = rideList.get(0).getEndTime();
        String arrTime = rideList.get(rideList.size()-1).getArrTime();
        int sumRunTime = Integer.parseInt(arrTime)-Integer.parseInt(endTime);
        Double maxRunTime = carMapper.findByCarType(scenariosId,carType).getMaxRunningTime();


        if(sumRunTime<=maxRunTime)
        {
            result = true;
        }else {
            result = false;
        }

        return result;
    }
    public boolean isSameCarType(List<Route> rideList){
        String scenariosId = ShiroUtil.getOpenScenariosId();
        boolean result=true;
        String carType0 = rideList.get(0).getCarType();
        String carType = rideList.get(rideList.size()-1).getCarType();

        if(carType0.equals(carType))
        {
            result = true;
        }else {
            result = false;
        }

        return result;
    }





    public void savetemp_list(Map tp) {
        tempMapper.savetemp_list(tp);
    }


    public void delAllOD_demandByScenariosId(String OpenScenariosId) {
        tempMapper.delAllOD_demandByScenariosId(OpenScenariosId);
    }

    public void delAlltemp_listByScenariosId(String OpenScenariosId) {
        tempMapper.delAlltemp_listByScenariosId(OpenScenariosId);
    }

    public void delAlldistance_refByScenariosId(String OpenScenariosId) {
        tempMapper.delAlldistance_refByScenariosId(OpenScenariosId);
    }




    public List<Map> findthree_points_route(String scenariosId) {
        return tempMapper.findthree_points_route(scenariosId);
    }

    public List<Map> findfour_points_route(String scenariosId) {
        return tempMapper.findfour_points_route(scenariosId);
    }


    public List<Map> findAllTwoPointsRoute(String scenariosId) {
        return tempMapper.findAllTwoPointsRoute(scenariosId);
    }


    public List<Map> findRouteTempBycode1(String route_temp_sql) {
        return tempMapper.findRouteTempBycode1(route_temp_sql);
    }

    /**
     * 查询所有运力信息
     * param
     * @return
     */

    public List<Map> findAll01(String scenariosId) {
        return tempMapper.findAll01(scenariosId);
    }


    /**
     * 查询所有运力信息
     * param
     * @return
     */

    public List<Map> findAllFlowLim(String scenariosId) {
        return tempMapper.findAllFlowLim(scenariosId);
    }

    /**
     * 查询所有运力信息
     * param
     * @return
     */

    public List<Map> findAll02(String scenariosId) {
        return tempMapper.findAll02(scenariosId);
    }
    /**
     * 查询所有运力信息
     * param
     * @return
     */

    public List<Map> findAll03(String scenariosId) {
        return tempMapper.findAll03(scenariosId);
    }
    /**
     * 查询所有运力信息
     * param
     * @return
     */

    public List<Map> findAll04(String scenariosId) {
        return tempMapper.findAll04(scenariosId);
    }
    /**
     * 查询所有运力信息
     * param
     * @return
     */

    public List<Map> findAll05(String scenariosId) {
        return tempMapper.findAll05(scenariosId);
    }
    /**
     * 查询所有运力信息
     * param
     * @return
     */

    public List<Map> findAll06(String scenariosId) {
        return tempMapper.findAll06(scenariosId);
    }
    /**
     * 查询所有运力信息
     * param
     * @return
     */

    public List<Map> findAll07(String scenariosId) {
        return tempMapper.findAll07(scenariosId);
    }
    /**
     * 查询所有运力信息
     * param
     * @return
     */

    public List<Map> findAll08(String scenariosId) {
        return tempMapper.findAll08(scenariosId);
    }

}
