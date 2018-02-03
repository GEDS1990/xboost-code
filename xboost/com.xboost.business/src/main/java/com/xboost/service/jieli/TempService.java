package com.xboost.service.jieli;

import com.mckinsey.sf.data.solution.ArrInfo;
import com.xboost.mapper.ArrInfoMapper;
import com.xboost.mapper.jieli.TempMapper;
import com.xboost.pojo.Cost;
import com.xboost.pojo.JieliResult;
import com.xboost.pojo.Route;
import com.xboost.pojo.jieli.Temp;
import com.xboost.service.*;
import com.xboost.util.ShiroUtil;
import org.apache.spark.sql.sources.In;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.List;
import java.util.Map;

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
    SolutionRouteService solutionRouteService;
    @Inject
    SolutionCostService solutionCostService;

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

    public void saveConnectionOpt(List<Map> jieliResults, double gurobyCost, String openScenariosId) {
        solutionRouteService.delByScenariosId(Integer.parseInt(openScenariosId));
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
            }
            else if(Double.parseDouble(jieliResult.getDadaNum())>0){
                route.setCarType("dada");
                route.setStr1(jieliResult.getDadaNum());
            }
            else if(Double.parseDouble(jieliResult.getBikeNum())>0){
                route.setCarType("baidu");
                route.setStr1(jieliResult.getBikeNum());
            }
            else if(Double.parseDouble(jieliResult.getTruckNum())>0){
                route.setCarType("truck");
                route.setStr1(jieliResult.getTruckNum());
            }

            route.setScenariosId(openScenariosId);
            route.setRouteCount(String.valueOf(i+1));
//            route.setCarType(jieliResult.getCarType());
            route.setLocation(siteInfoService.findSiteCodeById(Integer.parseInt(jieliResult.getInboundId()))+"-"
                    +siteInfoService.findSiteCodeById(Integer.parseInt(jieliResult.getOutboundId())));
            route.setSequence(String.valueOf(1));
            route.setCurLoc(siteInfoService.findSiteCodeById(Integer.parseInt(jieliResult.getInboundId())));
            route.setType("PICKUP");
            route.setSbVol(jieliResult.getVolume());
            route.setSbVolSum(jieliResult.getVolume());
            route.setArrTime((Integer.parseInt(jieliResult.getTimeId())-1)*10 + 780 +"");
            route.setEndTime((Integer.parseInt(jieliResult.getTimeId()))*10 + 780 +"");
            route.setUnloadLoc("0");
            route.setUnloadVol("0");
            route.setUnloadVolSum("0");
            route.setNextCurLoc(siteInfoService.findSiteCodeById(Integer.parseInt(jieliResult.getOutboundId())));
            route.setCalcDis(jieliResult.getDistance());
//            route.setStr1(jieliResult.getCarNum());
            solutionRouteService.addRoute(route);

            route.setScenariosId(openScenariosId);
            route.setRouteCount(String.valueOf(i+1));
//            route.setCarType(jieliResult.getCarType());
            route.setLocation(siteInfoService.findSiteCodeById(Integer.parseInt(jieliResult.getInboundId()))+"-"
                    +siteInfoService.findSiteCodeById(Integer.parseInt(jieliResult.getOutboundId())));
            route.setSequence(String.valueOf(2));
            route.setCurLoc(siteInfoService.findSiteCodeById(Integer.parseInt(jieliResult.getOutboundId())));
            route.setType("DELIVER");
            route.setSbVol("0");
            route.setSbVolSum("0");
            route.setArrTime((Integer.parseInt(jieliResult.getTimeId())-1)*10 + 780 +"");
            route.setEndTime((Integer.parseInt(jieliResult.getTimeId()))*10 + 780 +"");
            route.setUnloadLoc(siteInfoService.findSiteCodeById(Integer.parseInt(jieliResult.getOutboundId())));
            route.setUnloadVol(jieliResult.getVolume());
            route.setUnloadVolSum(jieliResult.getVolume());
            route.setNextCurLoc("");
            route.setCalcDis("0.00");
//            route.setStr1(jieliResult.getCarNum());
            solutionRouteService.addRoute(route);

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



    @Cacheable
    public List<Map> findthree_points_route(String scenariosId) {
        return tempMapper.findthree_points_route(scenariosId);
    }
    @Cacheable
    public List<Map> findfour_points_route(String scenariosId) {
        return tempMapper.findfour_points_route(scenariosId);
    }

    @Cacheable
    public List<Map> findAllTwoPointsRoute(String scenariosId) {
        return tempMapper.findAllTwoPointsRoute(scenariosId);
    }

    @Cacheable
    public List<Map> findRouteTempBycode1(String route_temp_sql) {
        return tempMapper.findRouteTempBycode1(route_temp_sql);
    }

    /**
     * 查询所有运力信息
     * param
     * @return
     */
    @Cacheable
    public List<Map> findAll01(String scenariosId) {
        return tempMapper.findAll01(scenariosId);
    }


    /**
     * 查询所有运力信息
     * param
     * @return
     */
    @Cacheable
    public List<Map> findAllFlowLim(String scenariosId) {
        return tempMapper.findAllFlowLim(scenariosId);
    }

    /**
     * 查询所有运力信息
     * param
     * @return
     */
    @Cacheable
    public List<Map> findAll02(String scenariosId) {
        return tempMapper.findAll02(scenariosId);
    }
    /**
     * 查询所有运力信息
     * param
     * @return
     */
    @Cacheable
    public List<Map> findAll03(String scenariosId) {
        return tempMapper.findAll03(scenariosId);
    }
    /**
     * 查询所有运力信息
     * param
     * @return
     */
    @Cacheable
    public List<Map> findAll04(String scenariosId) {
        return tempMapper.findAll04(scenariosId);
    }
    /**
     * 查询所有运力信息
     * param
     * @return
     */
    @Cacheable
    public List<Map> findAll05(String scenariosId) {
        return tempMapper.findAll05(scenariosId);
    }
    /**
     * 查询所有运力信息
     * param
     * @return
     */
    @Cacheable
    public List<Map> findAll06(String scenariosId) {
        return tempMapper.findAll06(scenariosId);
    }
    /**
     * 查询所有运力信息
     * param
     * @return
     */
    @Cacheable
    public List<Map> findAll07(String scenariosId) {
        return tempMapper.findAll07(scenariosId);
    }
    /**
     * 查询所有运力信息
     * param
     * @return
     */
    @Cacheable
    public List<Map> findAll08(String scenariosId) {
        return tempMapper.findAll08(scenariosId);
    }

}
