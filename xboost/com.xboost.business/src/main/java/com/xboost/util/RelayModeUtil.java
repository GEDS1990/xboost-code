package com.xboost.util;

import com.mckinsey.sf.constants.IConstants;
import com.mckinsey.sf.data.Configuration;
import com.xboost.pojo.DemandInfo;
import com.xboost.pojo.SiteDist;
import com.xboost.pojo.SiteInfo;
import com.xboost.service.DemandInfoService;
import com.xboost.service.SiteDistService;
import com.xboost.service.SiteInfoService;
import com.xboost.service.jieli.TempService;
import org.joda.time.DateTimeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.socket.TextMessage;
import java.util.*;
import gurobi.*;
import org.ujmp.core.DenseMatrix;
import org.ujmp.core.Matrix;

public class RelayModeUtil extends Thread implements IConstants {

    public com.xboost.pojo.Configuration config;
    public DemandInfoService demandInfoService;
    public SiteDistService siteDistService;
    public TempService tempService;
    public SiteInfoService siteInfoService;
    private static Logger logger = LoggerFactory.getLogger(RelayModeUtil.class);

    public RelayModeUtil(TempService tempService, DemandInfoService demandInfoService, SiteDistService siteDistService, SiteInfoService siteInfoService){
        this.config = config;
        this.demandInfoService = demandInfoService;
        this.siteDistService = siteDistService;
        this.tempService = tempService;
    }
    public void run(){
        logger.info("RelayMode init");
        double[] dddd = null;
        logger.info("spark.Matrix");
        org.apache.spark.mllib.linalg.Matrix mmm = new org.apache.spark.mllib.linalg.DenseMatrix(16333, 16333,dddd);
        logger.info("spark.Matrix");

        Matrix rrrr = DenseMatrix.Factory.zeros(12333, 12333);
        logger.info("16333"+ DateTimeUtils.currentTimeMillis());
        Matrix ww = DenseMatrix.Factory.zeros(16333, 16333);
        logger.info("16333"+ DateTimeUtils.currentTimeMillis());
        //params
        systemWebSocketHandler.sendMessageToUser( new TextMessage("params:"));
        systemWebSocketHandler.sendMessageToUser( new TextMessage("1%"));
        int TimeLimit = 6000;
        double MIPgap = 0.05;
        Configuration configuration = new Configuration();
        configuration.setOptimizeIterations(TimeLimit);

        //mip
//        mip<-Rglpk_solve_LP(obj=obj,mat=cons,dir=sense,rhs=rhs,max=FALSE,types=types)
        //model
//        cons = rbind(cbind(M11,M12,M13,M14,M15),cbind(M21,M22,M23,M24,M25),cbind(M31,M32,M33,M34,M35),cbind(M41,M42,M43,M44,M45),cbind(M51,M52,M53,M54,M55));
//        obj<-c(rep(0,I), connection$cost_truck, connection$cost_bike,connection$cost_didi,connection$cost_data)
//        rhs = c(rep(1,M),rep(0,J),outflow_lim,inflow_lim,didi_outflow_lim,didi_inflow_lim,0);
//        types<-c(rep("B",I),rep("I",J*4))
//        sense = c(rep("=",M),rep("<=",J),rep("<=",N*4*max(timebucket_num)),"=");
        int i=0,I=0,J=0,M=0,N=0;
        Map map = new HashMap<String,Object>();
        map.put("scenariosId", ShiroUtil.getOpenScenariosId());
        //obj
        List<DemandInfo> demandInfoList = demandInfoService.findByScenairoIdParam(map);
        Map<String,Object> connection = new HashMap<String,Object>();
        Map<String,Object> OD_demand = new HashMap<String,Object>();
        Map<String,Object> distance_ref = new HashMap<String,Object>();
        Map<String,Object> two_points_route = new HashMap<String,Object>();
        Map<String,Object> three_points_route = new HashMap<String,Object>();
        Map<String,Object> four_points_route = new HashMap<String,Object>();

        logger.info("demandInfoList");
        Map<String,Object> temp = new HashMap<String,Object>();
        List<Map> OD_demand_list= new ArrayList<Map>();
        List<Map> distance_ref_list= new ArrayList<Map>();
        List<Map> two_points_route_list= new ArrayList<Map>();
        List<Map> three_points_route_list= new ArrayList<Map>();
        List<Map> four_points_route_list= new ArrayList<Map>();
        List<Map> temp_list= new ArrayList<Map>();
        List<Map> connection_temp_list= new ArrayList<Map>();
        List<Map> connection2_list= new ArrayList<Map>();


        logger.info("OD_demand");
        int[] scenario_lim1 = {1,1,1,1,1};
        int[] scenario_lim2 = {9,12,15,18,21};
        for(int j=0;j<demandInfoList.size();j++){
            OD_demand.put("OD_id",j+1);
            OD_demand.put("volume",Double.parseDouble(demandInfoList.get(j).getVotes())/0.8511);
            OD_demand.put("inbound_id",demandInfoList.get(j).getSiteCodeCollect());
            OD_demand.put("outbound_id",demandInfoList.get(j).getSiteCodeDelivery());
            OD_demand.put("scenario",1);
            OD_demand.put("scenario_lim1",1);
            OD_demand.put("scenario_lim2",9);
            OD_demand.put("kmh",speed2);
            OD_demand.put("minutes",Integer.parseInt(demandInfoList.get(j).getDurationEnd())-Integer.parseInt(demandInfoList.get(j).getDurationStart()));
            OD_demand_list.add(OD_demand);
        }
        //distance_ref
        systemWebSocketHandler.sendMessageToUser( new TextMessage("distance_ref:"));
        systemWebSocketHandler.sendMessageToUser( new TextMessage("10%"));
        List<SiteDist> siteDistList = siteDistService.findSiteDistByScenariosId(map);
        logger.info("siteDistList");
        for(int j=0;j<siteDistList.size();j++){
            distance_ref.put("inbound_id",siteDistList.get(j).getSiteCollect());
            distance_ref.put("outbound_id",siteDistList.get(j).getSiteDelivery());
            distance_ref.put("km",siteDistList.get(j).getCarDistance());
            distance_ref.put("minutes",siteDistList.get(j).getDurationNightDelivery());
            distance_ref.put("cross_river",0);
            for(int j2=0;j2<OD_demand_list.size();j2++){
                if(OD_demand_list.get(j2).get("inbound_id").equals(distance_ref.get("inbound_id"))&&OD_demand_list.get(j2).get("outbound_id").equals(distance_ref.get("outbound_id"))){
                    distance_ref.put("OD_id",OD_demand_list.get(j2).get("OD_id"));
                }else{
                    distance_ref.put("OD_id",0);
                }
            }
            distance_ref_list.add(distance_ref);
        }
        //two_points_route
        systemWebSocketHandler.sendMessageToUser( new TextMessage("two_points_route:"));
        systemWebSocketHandler.sendMessageToUser( new TextMessage("20%"));
        systemWebSocketHandler.sendMessageToUser( new TextMessage("please waiting...."));
//        for(int j=0;j<OD_demand_list.size();j++){
//            for(int j2=0;j2<distance_ref_list.size();j2++){
//                boolean b = OD_demand_list.get(j).get("inbound_id").equals(distance_ref_list.get(j2).get("inbound_id"));
//                boolean b2 = OD_demand_list.get(j).get("outbound_id").equals(distance_ref_list.get(j2).get("outbound_id"));
//                if(b&&b2){
//                    two_points_route.put("m_od_id",OD_demand_list.get(j).get("OD_id"));
//                    two_points_route.put("inbound_id",OD_demand_list.get(j).get("inbound_id"));
//                    two_points_route.put("i_route_id",OD_demand_list.get(j).get("inbound_id")+"-"+OD_demand_list.get(j).get("outbound_id"));
//                    two_points_route.put("distance",distance_ref_list.get(j2).get("km"));
//                    two_points_route.put("time",distance_ref_list.get(j2).get("minutes"));
//                    two_points_route.put("demand",OD_demand_list.get(j).get("volume"));
//                    two_points_route.put("point1",OD_demand_list.get(j).get("inbound_id"));
//                    two_points_route.put("point2",0);
//                    two_points_route.put("point3",0);
//                    two_points_route.put("point4",OD_demand_list.get(j).get("outbound_id"));
//                    two_points_route.put("connection1",OD_demand_list.get(j).get("inbound_id")+"-"+OD_demand_list.get(j).get("outbound_id"));
//                    two_points_route.put("connection2","");
//                    two_points_route.put("connection3","");
//                    two_points_route.put("time1",distance_ref_list.get(j2).get("minutes"));
//                    two_points_route.put("time2",0);
//                    two_points_route.put("time3",0);
//                    two_points_route.put("dist1",distance_ref_list.get(j2).get("km"));
//                    two_points_route.put("dist2",0);
//                    two_points_route.put("dist3",0);
//                    two_points_route.put("timecost",-Integer.parseInt(distance_ref_list.get(j2).get("minutes").toString()));
//                    two_points_route.put("scenario",OD_demand_list.get(j).get("scenario"));
//                    two_points_route.put("scenario_lim1",OD_demand_list.get(j).get("scenario_lim1"));
//                    two_points_route.put("scenario_lim2",OD_demand_list.get(j).get("scenario_lim2"));
//                    two_points_route.put("wait_time",0);
//                    two_points_route_list.add(two_points_route);
//                }else{
//                    two_points_route.put("m_od_id",OD_demand_list.get(j).get("OD_id"));
//                    two_points_route.put("inbound_id",OD_demand_list.get(j).get("inbound_id"));
//                    two_points_route.put("i_route_id",OD_demand_list.get(j).get("inbound_id")+"-"+OD_demand_list.get(j).get("outbound_id"));
//                    two_points_route.put("distance","");
//                    two_points_route.put("time","");
//                    two_points_route.put("demand",OD_demand_list.get(j).get("volume"));
//                    two_points_route.put("point1",OD_demand_list.get(j).get("inbound_id"));
//                    two_points_route.put("point2",0);
//                    two_points_route.put("point3",0);
//                    two_points_route.put("point4",OD_demand_list.get(j).get("outbound_id"));
//                    two_points_route.put("connection1",OD_demand_list.get(j).get("inbound_id")+"-"+OD_demand_list.get(j).get("outbound_id"));
//                    two_points_route.put("connection2","");
//                    two_points_route.put("connection3","");
//                    two_points_route.put("time1","0");
//                    two_points_route.put("time2",0);
//                    two_points_route.put("time3",0);
//                    two_points_route.put("dist1","");
//                    two_points_route.put("dist2",0);
//                    two_points_route.put("dist3",0);
//                    two_points_route.put("timecost","");
//                    two_points_route.put("scenario",OD_demand_list.get(j).get("scenario"));
//                    two_points_route.put("scenario_lim1",OD_demand_list.get(j).get("scenario_lim1"));
//                    two_points_route.put("scenario_lim2",OD_demand_list.get(j).get("scenario_lim2"));
//                    two_points_route.put("wait_time",0);
//                    two_points_route_list.add(two_points_route);
//                }
//            }
//        }
        logger.info("two_points_route_list");
        two_points_route_list = tempService.findAllTwoPointsRoute(ShiroUtil.getOpenScenariosId());
        //temp
        systemWebSocketHandler.sendMessageToUser( new TextMessage("temp:"));
        systemWebSocketHandler.sendMessageToUser( new TextMessage("30%"));
//        for(int j=0;j<OD_demand_list.size();j++){
//            for(int j2=0;j2<distance_ref_list.size();j2++){
//                boolean b = two_points_route_list.get(j).get("inbound_id").equals(distance_ref_list.get(j2).get("inbound_id"));
//                if(b){
//                    temp.put("m_od_id",OD_demand_list.get(j).get("OD_id"));
//                    temp.put("minutes",OD_demand_list.get(j).get("minutes"));
//                    temp.put("i_route_id",OD_demand_list.get(j).get("inbound_id")+"-"+OD_demand_list.get(j).get("outbound_id"));
//                    temp.put("distance",distance_ref_list.get(j2).get("km"));
//                    temp.put("km",distance_ref_list.get(j2).get("km"));
//                    temp.put("time",distance_ref_list.get(j2).get("minutes"));
//                    temp.put("demand",OD_demand_list.get(j).get("volume"));
//                    temp.put("point1",OD_demand_list.get(j).get("inbound_id"));
//                    temp.put("point2",0);
//                    temp.put("point3",0);
//                    temp.put("point4",OD_demand_list.get(j).get("outbound_id"));
//                    temp.put("outbound_id",OD_demand_list.get(j).get("outbound_id"));
//                    temp.put("inbound_id",OD_demand_list.get(j).get("inbound_id"));
//                    temp.put("connection1",OD_demand_list.get(j).get("inbound_id")+"-"+OD_demand_list.get(j).get("outbound_id"));
//                    temp.put("connection2","");
//                    temp.put("connection3","");
//                    temp.put("time1",distance_ref_list.get(j2).get("minutes"));
//                    temp.put("time2",0);
//                    temp.put("time3",0);
//                    temp.put("dist1",distance_ref_list.get(j2).get("km"));
//                    temp.put("dist2",0);
//                    temp.put("dist3",0);
//                    temp.put("timecost",-Integer.parseInt(distance_ref_list.get(j2).get("minutes").toString()));
//                    temp.put("scenario",OD_demand_list.get(j).get("scenario"));
//                    temp.put("scenario_lim1",OD_demand_list.get(j).get("scenario_lim1"));
//                    temp.put("scenario_lim2",OD_demand_list.get(j).get("scenario_lim2"));
//                    temp.put("wait_time",0);
//                    temp_list.add(temp);
//                }else{
//                    temp.put("m_od_id",OD_demand_list.get(j).get("OD_id"));
//                    temp.put("minutes",OD_demand_list.get(j).get("minutes"));
//                    temp.put("i_route_id",OD_demand_list.get(j).get("inbound_id")+"-"+OD_demand_list.get(j).get("outbound_id"));
//                    temp.put("distance","");
//                    temp.put("km","");
//                    temp.put("time","0");
//                    temp.put("demand",OD_demand_list.get(j).get("volume"));
//                    temp.put("point1",OD_demand_list.get(j).get("inbound_id"));
//                    temp.put("point2",0);
//                    temp.put("point3",0);
//                    temp.put("point4",OD_demand_list.get(j).get("outbound_id"));
//                    temp.put("outbound_id",OD_demand_list.get(j).get("outbound_id"));
//                    temp.put("inbound_id",OD_demand_list.get(j).get("inbound_id"));
//                    temp.put("connection1",OD_demand_list.get(j).get("inbound_id")+"-"+OD_demand_list.get(j).get("outbound_id"));
//                    temp.put("connection2","");
//                    temp.put("connection3","");
//                    temp.put("time1","0");
//                    temp.put("time2",0);
//                    temp.put("time3",0);
//                    temp.put("dist1","");
//                    temp.put("dist2",0);
//                    temp.put("dist3",0);
//                    temp.put("timecost","");
//                    temp.put("scenario",OD_demand_list.get(j).get("scenario"));
//                    temp.put("scenario_lim1",OD_demand_list.get(j).get("scenario_lim1"));
//                    temp.put("scenario_lim2",OD_demand_list.get(j).get("scenario_lim2"));
//                    temp.put("wait_time",0);
//                    temp_list.add(temp);
//                }
//            }
//        }
        temp_list = two_points_route_list;
        //three_points_route
        systemWebSocketHandler.sendMessageToUser( new TextMessage("three_points_route:"));
        systemWebSocketHandler.sendMessageToUser( new TextMessage("35%"));
        systemWebSocketHandler.sendMessageToUser( new TextMessage("It may take some minutes , please waiting...."));
        /*for(int j=0;j<temp_list.size();j++){
            for(int j2=0;j2<distance_ref_list.size();j2++){
                boolean b = temp_list.get(j).get("inbound_id").equals(distance_ref_list.get(j2).get("inbound_id"));
                boolean bwhere1 = !temp_list.get(j).get("point1").equals(temp_list.get(j).get("outbound_id"));
                boolean bwhere2 = !temp_list.get(j).get("point4").equals(temp_list.get(j).get("outbound_id"));
                boolean bwhere3 = (Integer.parseInt(temp_list.get(j).get("minutes").toString())+
                        Integer.parseInt(distance_ref_list.get(j2).get("minutes").toString())<=190);
                boolean bwhere4 = Integer.parseInt(temp_list.get(j).get("minutes").toString())+
                        Integer.parseInt(distance_ref_list.get(j2).get("minutes").toString())<=2*Integer.parseInt(temp_list.get(j2).get("time").toString());
                if(!bwhere1&&bwhere2&&bwhere3&&bwhere4)
                    continue;
                boolean bon1 = temp_list.get(j).get("outbound_id").toString().equals(distance_ref_list.get(j2).get("inbound_id").toString());
                boolean bon2 = temp_list.get(j).get("point4").toString().equals(distance_ref_list.get(j2).get("outbound_id").toString());
                boolean bon = bon1&&bon2;
                if(bon){
                    three_points_route.put("m_od_id",temp_list.get(j).get("OD_id"));
                    three_points_route.put("scenario",temp_list.get(j).get("scenario"));
                    three_points_route.put("scenario_lim1",temp_list.get(j).get("scenario_lim1"));
                    three_points_route.put("scenario_lim2",temp_list.get(j).get("scenario_lim2"));
                    three_points_route.put("i_route_id",temp_list.get(j).get("point1")+"-"+temp_list.get(j).get("outbound_id")+"-"+temp_list.get(j).get("point4"));
                    three_points_route.put("distance",Integer.parseInt(temp_list.get(j).get("km").toString())+Integer.parseInt(distance_ref_list.get(j).get("km").toString()));
                    three_points_route.put("time",Integer.parseInt(temp_list.get(j).get("minutes").toString())+Integer.parseInt(distance_ref_list.get(j).get("minutes").toString()));
                    three_points_route.put("demand",temp_list.get(j).get("demand"));
                    three_points_route.put("point1",temp_list.get(j).get("point1"));
                    three_points_route.put("point2",temp_list.get(j).get("outbound_id"));
                    three_points_route.put("point3",0);
                    three_points_route.put("point4",temp_list.get(j).get("point4"));
                    three_points_route.put("connection1",temp_list.get(j).get("point1")+"-"+temp_list.get(j).get("outbound_id"));
                    three_points_route.put("connection2",temp_list.get(j).get("outbound_id")+"-"+temp_list.get(j).get("point4"));
                    three_points_route.put("connection3","");
                    three_points_route.put("time1",temp_list.get(j).get("minutes"));
                    three_points_route.put("time2",distance_ref_list.get(j).get("minutes"));
                    three_points_route.put("time3",0);
                    three_points_route.put("dist1",temp_list.get(j).get("km"));
                    three_points_route.put("dist2",distance_ref_list.get(j).get("km"));
                    three_points_route.put("dist3",0);
                    three_points_route.put("timecost",-Integer.parseInt(temp_list.get(j).get("minutes").toString())-
                            Integer.parseInt(distance_ref_list.get(j).get("minutes").toString())-20);
                    three_points_route.put("wait_time",20);
                    three_points_route_list.add(three_points_route);
                }else{
                    three_points_route.put("m_od_id",temp_list.get(j).get("OD_id"));
                    three_points_route.put("scenario",temp_list.get(j).get("scenario"));
                    three_points_route.put("scenario_lim1",temp_list.get(j).get("scenario_lim1"));
                    three_points_route.put("scenario_lim2",temp_list.get(j).get("scenario_lim2"));
                    three_points_route.put("i_route_id",temp_list.get(j).get("point1")+"-"+temp_list.get(j).get("outbound_id")+"-"+temp_list.get(j).get("point4"));
                    three_points_route.put("distance",temp_list.get(j).get("km").toString());
                    three_points_route.put("time",Integer.parseInt(temp_list.get(j).get("minutes").toString()));
                    three_points_route.put("demand",temp_list.get(j).get("demand"));
                    three_points_route.put("point1",temp_list.get(j).get("point1"));
                    three_points_route.put("point2",temp_list.get(j).get("outbound_id"));
                    three_points_route.put("point3",0);
                    three_points_route.put("point4",temp_list.get(j).get("point4"));
                    three_points_route.put("connection1",temp_list.get(j).get("point1")+"-"+temp_list.get(j).get("outbound_id"));
                    three_points_route.put("connection2",temp_list.get(j).get("outbound_id")+"-"+temp_list.get(j).get("point4"));
                    three_points_route.put("connection3","");
                    three_points_route.put("time1",temp_list.get(j).get("minutes"));
                    three_points_route.put("time2",0);
                    three_points_route.put("time3",0);
                    three_points_route.put("dist1",temp_list.get(j).get("km"));
                    three_points_route.put("dist2",0);
                    three_points_route.put("dist3",0);
                    three_points_route.put("timecost",-Integer.parseInt(temp_list.get(j).get("minutes").toString())-20);
                    three_points_route.put("wait_time",20);
                    three_points_route_list.add(three_points_route);
                }
            }
        }*/
        three_points_route_list = temp_list;
        //temp2
        systemWebSocketHandler.sendMessageToUser( new TextMessage("temp2:"));
        systemWebSocketHandler.sendMessageToUser( new TextMessage("40%"));
        systemWebSocketHandler.sendMessageToUser( new TextMessage("please waiting...."));
        /*for(int j=0;j<three_points_route_list.size();j++){
            for(int j2=0;j2<distance_ref_list.size();j2++){
                boolean b = three_points_route_list.get(j).get("point2").equals(distance_ref_list.get(j2).get("point2"));
                boolean b2 = three_points_route_list.get(j).get("inbound_id").equals(distance_ref_list.get(j2).get("inbound_id"));
                if(b){
                    temp.put("m_od_id",three_points_route_list.get(j).get("OD_id"));
                    temp.put("i_route_id",three_points_route_list.get(j).get("inbound_id")+"-"+three_points_route_list.get(j).get("outbound_id"));
                    temp.put("distance",distance_ref_list.get(j2).get("km"));
                    temp.put("time",distance_ref_list.get(j2).get("minutes"));
                    temp.put("demand",three_points_route_list.get(j).get("volume"));
                    temp.put("point1",three_points_route_list.get(j).get("inbound_id"));
                    temp.put("point2",0);
                    temp.put("point3",0);
                    temp.put("point4",three_points_route_list.get(j).get("outbound_id"));
                    temp.put("connection1",three_points_route_list.get(j).get("inbound_id")+"-"+three_points_route_list.get(j).get("outbound_id"));
                    temp.put("connection2","");
                    temp.put("connection3","");
                    temp.put("time1",distance_ref_list.get(j2).get("minutes"));
                    temp.put("time2",0);
                    temp.put("time3",0);
                    temp.put("dist1",distance_ref_list.get(j2).get("km"));
                    temp.put("dist2",0);
                    temp.put("dist3",0);
                    temp.put("timecost",-Integer.parseInt(distance_ref_list.get(j2).get("minutes").toString()));
                    temp.put("scenario",three_points_route_list.get(j).get("scenario"));
                    temp.put("scenario_lim1",three_points_route_list.get(j).get("scenario_lim1"));
                    temp.put("scenario_lim2",three_points_route_list.get(j).get("scenario_lim2"));
                    temp.put("wait_time",0);
                    temp_list.add(temp);
                }else{
                    temp.put("m_od_id",three_points_route_list.get(j).get("OD_id"));
                    temp.put("i_route_id",three_points_route_list.get(j).get("inbound_id")+"-"+three_points_route_list.get(j).get("outbound_id"));
                    temp.put("distance","");
                    temp.put("time","");
                    temp.put("demand",three_points_route_list.get(j).get("volume"));
                    temp.put("point1",three_points_route_list.get(j).get("inbound_id"));
                    temp.put("point2",0);
                    temp.put("point3",0);
                    temp.put("point4",three_points_route_list.get(j).get("outbound_id"));
                    temp.put("connection1",three_points_route_list.get(j).get("inbound_id")+"-"+three_points_route_list.get(j).get("outbound_id"));
                    temp.put("connection2","");
                    temp.put("connection3","");
                    temp.put("time1","");
                    temp.put("time2",0);
                    temp.put("time3",0);
                    temp.put("dist1","");
                    temp.put("dist2",0);
                    temp.put("dist3",0);
                    temp.put("timecost","");
                    temp.put("scenario",three_points_route_list.get(j).get("scenario"));
                    temp.put("scenario_lim1",three_points_route_list.get(j).get("scenario_lim1"));
                    temp.put("scenario_lim2",three_points_route_list.get(j).get("scenario_lim2"));
                    temp.put("wait_time",0);
                    temp_list.add(temp);
                }
            }
        }*/
        //four_points_route
        systemWebSocketHandler.sendMessageToUser( new TextMessage("four_points_route:"));
        systemWebSocketHandler.sendMessageToUser( new TextMessage("50%"));
       /* for(int j=0;j<temp_list.size();j++){
            for(int j2=0;j2<distance_ref_list.size();j2++){
                boolean b = temp_list.get(j).get("inbound_id").equals(distance_ref_list.get(j2).get("inbound_id"));
                boolean bwhere1 = !temp_list.get(j).get("point1").equals(temp_list.get(j).get("outbound_id"));
                boolean bwhere11 = !temp_list.get(j).get("point2").equals(temp_list.get(j).get("outbound_id"));
                boolean bwhere2 = !temp_list.get(j).get("point4").equals(temp_list.get(j).get("outbound_id"));
                boolean bwhere3 = (Integer.parseInt(temp_list.get(j).get("time1").toString())+
                        Integer.parseInt(temp_list.get(j).get("minutes").toString())+
                        Integer.parseInt(distance_ref_list.get(j2).get("minutes").toString())<=170);
                boolean bwhere4 = Integer.parseInt(temp_list.get(j).get("time1").toString())+
                        Integer.parseInt(temp_list.get(j).get("minutes").toString())+
                        Integer.parseInt(distance_ref_list.get(j2).get("minutes").toString())<=2*Integer.parseInt(distance_ref_list.get(j2).get("time").toString());
                if(!bwhere1&&bwhere11&&bwhere2&&bwhere3&&bwhere4)
                    continue;
                boolean bon1 = Integer.parseInt(temp_list.get(j).get("outbound_id").toString())==
                        Integer.parseInt(distance_ref_list.get(j2).get("inbound_id").toString());
                boolean bon2 = Integer.parseInt(temp_list.get(j).get("point4").toString())==
                        Integer.parseInt(distance_ref_list.get(j2).get("outbound_id").toString());
                boolean bon = bon1&&bon2;
                if(bon){
                    four_points_route.put("m_od_id",temp_list.get(j).get("OD_id"));
                    four_points_route.put("scenario",temp_list.get(j).get("scenario"));
                    four_points_route.put("scenario_lim1",temp_list.get(j).get("scenario_lim1"));
                    four_points_route.put("scenario_lim2",temp_list.get(j).get("scenario_lim2"));
                    four_points_route.put("i_route_id",temp_list.get(j).get("point1")+"-"+temp_list.get(j).get("point2")+"-"
                            +temp_list.get(j).get("outbound_id")+"-"+temp_list.get(j).get("point4"));
                    four_points_route.put("distance",Integer.parseInt(temp_list.get(j).get("dist1").toString())+Integer.parseInt(temp_list.get(j).get("km").toString())+
                            Integer.parseInt(distance_ref_list.get(j).get("km").toString()));
                    four_points_route.put("time",Integer.parseInt(temp_list.get(j).get("time1").toString())+Integer.parseInt(temp_list.get(j).get("minutes").toString())+
                            Integer.parseInt(distance_ref_list.get(j).get("minutes").toString()));
                    four_points_route.put("demand",temp_list.get(j).get("demand"));
                    four_points_route.put("point1",temp_list.get(j).get("point1"));
                    four_points_route.put("point2",temp_list.get(j).get("point2"));
                    four_points_route.put("point3",temp_list.get(j).get("outbound_id"));
                    four_points_route.put("point4",temp_list.get(j).get("point4"));
                    four_points_route.put("connection1",temp_list.get(j).get("point1")+"-"+temp_list.get(j).get("point2"));
                    four_points_route.put("connection2",temp_list.get(j).get("point2")+"-"+temp_list.get(j).get("outbound_id"));
                    four_points_route.put("connection3",temp_list.get(j).get("outbound_id")+"-"+temp_list.get(j).get("point4"));
                    four_points_route.put("time1",temp_list.get(j).get("time1"));
                    four_points_route.put("time2",temp_list.get(j).get("minutes"));
                    four_points_route.put("time3",distance_ref_list.get(j).get("minutes"));
                    four_points_route.put("dist1",temp_list.get(j).get("dist1"));
                    four_points_route.put("dist2",temp_list.get(j).get("km"));
                    four_points_route.put("dist3",distance_ref_list.get(j).get("km"));
                    four_points_route.put("timecost",-Integer.parseInt(temp_list.get(j).get("time1").toString())-
                            Integer.parseInt(temp_list.get(j).get("minutes").toString())-
                            Integer.parseInt(distance_ref_list.get(j).get("minutes").toString())-40);
                    four_points_route.put("wait_time",40);
                    four_points_route_list.add(four_points_route);
                }else{
                    four_points_route.put("m_od_id",temp_list.get(j).get("OD_id"));
                    four_points_route.put("scenario",temp_list.get(j).get("scenario"));
                    four_points_route.put("scenario_lim1",temp_list.get(j).get("scenario_lim1"));
                    four_points_route.put("scenario_lim2",temp_list.get(j).get("scenario_lim2"));
                    four_points_route.put("i_route_id",temp_list.get(j).get("point1")+"-"+temp_list.get(j).get("outbound_id")+"-"+temp_list.get(j).get("point4"));
                    four_points_route.put("distance",Integer.parseInt(temp_list.get(j).get("km").toString()));
                    four_points_route.put("time",Integer.parseInt(temp_list.get(j).get("minutes").toString()));
                    four_points_route.put("demand",temp_list.get(j).get("demand"));
                    four_points_route.put("point1",temp_list.get(j).get("point1"));
                    four_points_route.put("point2",temp_list.get(j).get("outbound_id"));
                    four_points_route.put("point3",0);
                    four_points_route.put("point4",temp_list.get(j).get("point4"));
                    four_points_route.put("connection1",temp_list.get(j).get("point1")+"-"+temp_list.get(j).get("outbound_id"));
                    four_points_route.put("connection2",temp_list.get(j).get("outbound_id")+"-"+temp_list.get(j).get("point4"));
                    four_points_route.put("connection3","");
                    four_points_route.put("time1",temp_list.get(j).get("minutes"));
                    four_points_route.put("time2",0);
                    four_points_route.put("time3",0);
                    four_points_route.put("dist1",temp_list.get(j).get("km"));
                    four_points_route.put("dist2",0);
                    four_points_route.put("dist3",0);
                    four_points_route.put("timecost",-Integer.parseInt(temp_list.get(j).get("minutes").toString())-20);
                    four_points_route.put("wait_time",20);
                    four_points_route_list.add(four_points_route);
                }
            }
        }*/
       four_points_route_list = temp_list;
        //time buckets
        //real timebucket###
        systemWebSocketHandler.sendMessageToUser( new TextMessage("real timebucket###:"));
        systemWebSocketHandler.sendMessageToUser( new TextMessage("60%"));
        int full_time = 210;
        int route_time_unit = 10;
        int[][] timebucket_num = new int[1][full_time/route_time_unit];
        for(int j=0;j<full_time/route_time_unit;j++){
            timebucket_num[0][j] = j+1;
        }
        logger.info("timebucket_num");
        Object route_two_point = null;
        Object route_three_point = null;
        Object route_four_point = null;
//        ###two point route###
        int[][] site1 = new int[1][full_time/route_time_unit];
        int timebucket_site;
        for(int j=0;j<full_time/route_time_unit;j++){
            site1[0][j] = j+1;
            timebucket_site = j+1;
        }
        //route_temp
        systemWebSocketHandler.sendMessageToUser( new TextMessage("route_temp:"));
        systemWebSocketHandler.sendMessageToUser( new TextMessage("70%"));
        systemWebSocketHandler.sendMessageToUser( new TextMessage("please waiting...."));
        for(int j=0;j<two_points_route_list.size();j++){
            two_points_route.put("timebucket_1",two_points_route_list.get(j).get("connection1"));
            two_points_route.put("timebucket_"+j+2,"");
        }
        List<Map> code1 = two_points_route_list;
        List<Map> route_temp_list = code1;
        for(int j=0;j<route_temp_list.size();j++){
            route_temp_list.get(j).put("time_id1",site1[0][j%20]);
        }
        logger.info("two_points_route_list");
        for(int j=0;j<two_points_route_list.size();j++){
//            System.out.println(Double.parseDouble(two_points_route_list.get(j).get("time1").toString()));
            two_points_route_list.get(j).put("timebucket_1",route_temp_list.get(j).get("connection1"));
            two_points_route_list.get(j).put("timebucket_"+j+2,route_temp_list.get(j).get("timebucket_"+j+2));
//            two_points_route_list.get(j).put("ok",Integer.parseInt(two_points_route_list.get(j).get("scenario_lim2").toString())
//                    *route_time_unit-((Integer.parseInt(two_points_route_list.get(j).get("time_id1").toString())-1)*route_time_unit)
//                    +Double.parseDouble(two_points_route_list.get(j).get("time1").toString()));
            two_points_route_list.get(j).put("ok",Integer.parseInt(two_points_route_list.get(j).get("scenario_lim2").toString())
                    *route_time_unit-((Integer.parseInt(two_points_route_list.get(j).get("time_id1").toString())-1)*route_time_unit));
        }

        for(int j=0;j<two_points_route_list.size();j++){
//            Double ok = Integer.parseInt(two_points_route_list.get(j).get("scenario_lim2").toString())
//                    *route_time_unit-((Integer.parseInt(two_points_route_list.get(j).get("time_id1").toString())-1)*route_time_unit)
//                    +Double.parseDouble(two_points_route_list.get(j).get("time1").toString());
            int ok = Integer.parseInt(two_points_route_list.get(j).get("scenario_lim2").toString())
                    *route_time_unit-((Integer.parseInt(two_points_route_list.get(j).get("time_id1").toString())-1)*route_time_unit);
            if(ok<0){
                two_points_route_list.remove(j);
            }
        }

        for(int j=0;j<two_points_route_list.size();j++){
            int which1 = Integer.parseInt(two_points_route_list.get(j).get("time_id1").toString())-((Integer.parseInt(two_points_route_list.get(j).get("scenario_lim1").toString())));
            if(which1<0){
                two_points_route_list.remove(j);
            }
        }
//###three point route###
        systemWebSocketHandler.sendMessageToUser( new TextMessage("###three point route###:"));
        systemWebSocketHandler.sendMessageToUser( new TextMessage("80%"));
        site1 = new int[2][full_time/route_time_unit];
        int[] numt1=new int [full_time/route_time_unit];
        int[] numt2=new int [full_time/route_time_unit];
        for(int j=0;j<full_time/route_time_unit;j++){
            numt1[j] = full_time/route_time_unit+1;
        }
        numt2 = numt1;
        for(int j=0;j<numt1.length;j++){
            for(int j2=0;j2<numt2.length-j;j2++) {
                site1[0][j] = numt1[j];
                site1[1][j] = numt1[j2];
            }
        }
//20171226
        //String code1 = "";
        List<Map> route_three_point_list = tempService.findAll01(ShiroUtil.getOpenScenariosId());
        for(Map m:route_three_point_list){
            m.put("ok",Integer.parseInt(m.get("scenario_lim2").toString())*
                    Integer.parseInt(m.get("*route_time_unit").toString())-
                    (Integer.parseInt(m.get("*time_id2").toString())-1)*route_time_unit
                    +Integer.parseInt(m.get("*time2").toString())
            );
            if(Double.parseDouble(m.get("ok").toString())<0){
                route_three_point_list.remove(m);
            }else{
                m.put("ok",0);
            }
            if((Double.parseDouble(m.get("time_id1").toString())-Double.parseDouble(m.get("scenario_lim1").toString()))>=0){

            }else{
                route_three_point_list.remove(m);
            }
            m.put("ok",Double.parseDouble(m.get("time_id2").toString())-
                    Double.parseDouble(m.get("*time_id1").toString())*route_time_unit-
                    (Double.parseDouble(m.get("*time1").toString()))*20
            );
            if(Double.parseDouble(m.get("ok").toString())<0){
                route_three_point_list.remove(m);
            }else{
                m.put("ok",0);
            }
        }
//###four point route###
        systemWebSocketHandler.sendMessageToUser( new TextMessage("###four point route###:"));
        systemWebSocketHandler.sendMessageToUser( new TextMessage("90%"));
        site1 = new int[1][full_time/route_time_unit];
        for(int j=0;j<full_time/route_time_unit;j++){
            site1[0][j] = j+1;
            timebucket_site = j+1;
        }
        List<Map> route_four_point_list = tempService.findAll02(ShiroUtil.getOpenScenariosId());
        for(Map m:route_three_point_list){
            m.put("ok",Integer.parseInt(m.get("scenario_lim2").toString())*
                    Integer.parseInt(m.get("*route_time_unit").toString())-
                    (Integer.parseInt(m.get("*time_id2").toString())-1)*route_time_unit
                    +Integer.parseInt(m.get("*time2").toString())
            );
            if(Double.parseDouble(m.get("ok").toString())<0){
                route_three_point_list.remove(m);
            }else{
                m.put("ok",0);
            }
            if((Double.parseDouble(m.get("time_id1").toString())-Double.parseDouble(m.get("scenario_lim1").toString()))>=0){

            }else{
                route_three_point_list.remove(m);
            }
            m.put("ok",Double.parseDouble(m.get("time_id2").toString())-
                    Double.parseDouble(m.get("*time_id1").toString())*route_time_unit-
                    (Double.parseDouble(m.get("*time1").toString()))*20
            );
            if(Double.parseDouble(m.get("ok").toString())<0){
                route_three_point_list.remove(m);
            }else{
                m.put("ok",0);
            }
        }

        List<Map> route_list=two_points_route_list;
//        route_list.addAll(two_points_route_list);
        route_list.addAll(route_three_point_list);
        route_list.addAll(route_four_point_list);

        N = demandInfoList.size();
        M = two_points_route_list.size();
        int I1 = two_points_route_list.size();
        int I2 = route_three_point_list.size();
        int I3 = route_four_point_list.size();
        I = I1+I2+I3;
        J = distance_ref.size()*full_time/route_time_unit;
        temp_list = tempService.findAll03(ShiroUtil.getOpenScenariosId());
        connection_temp_list = tempService.findAll04(ShiroUtil.getOpenScenariosId());

        logger.info("M11");
        Matrix M1133333 = DenseMatrix.Factory.zeros(3, 3);
        System.out.println(route_list.size()+":"+I);
        int tagrelay = 0;
        logger.info("tag:"+tagrelay++);
        Matrix M11 = DenseMatrix.Factory.zeros(route_list.size(), I);
        logger.info("tag:"+tagrelay++);
        Matrix M12 = DenseMatrix.Factory.zeros(route_list.size(), I);
        logger.info("tag:"+tagrelay++);
        Matrix M13 = DenseMatrix.Factory.zeros(route_list.size(), I);
        logger.info("tag:"+tagrelay++);
        Matrix M14 = DenseMatrix.Factory.zeros(route_list.size(), I);
        logger.info("tag:"+tagrelay++);
        Matrix M15 = DenseMatrix.Factory.zeros(route_list.size(), I);
        logger.info("tag:"+tagrelay++);
        Vector v = new Vector();
        for(int mj=M;mj<J;mj++){
            v.add(mj,mj);
        }
        logger.info("tag:"+tagrelay++);
        for(int m=0;m<route_list.size();m++){
            for(int mi=0;mi<I;mi++){
                M11.setAsInt(v.indexOf(mi),m,mi);
                M12.setAsInt(v.indexOf(mi),m,mi);
                M13.setAsInt(v.indexOf(mi),m,mi);
                M14.setAsInt(v.indexOf(mi),m,mi);
                M15.setAsInt(v.indexOf(mi),m,mi);
            }
        }
        logger.info("tag:"+tagrelay++);

        logger.info("M21");
        Matrix M21 = DenseMatrix.Factory.zeros(route_list.size(), I);
        Matrix M22 = DenseMatrix.Factory.zeros(route_list.size(), I);
        Matrix M23 = DenseMatrix.Factory.zeros(route_list.size(), I);
        Matrix M24 = DenseMatrix.Factory.zeros(route_list.size(), I);
        Matrix M25 = DenseMatrix.Factory.zeros(route_list.size(), I);
        for(int m=0;m<J;m++){
            for(int mi=0;mi<J;mi++){
                M21.setAsInt(v.indexOf(mi),m,mi);
                M22.setAsInt(-truck_capacity,m,mi);
                M23.setAsInt(-truck_capacity2,m,mi);
                M24.setAsInt(-truck_capacity3,m,mi);
                M25.setAsInt(-truck_capacity4,m,mi);
            }
        }
        int f=0,g=0;
        Vector f1 = new Vector();
        for(int c2=0;c2<connection2_list.size();c2++){
            f1.add(Integer.parseInt(connection2_list.get(c2).get("dummy_in_id").toString())
                +(Integer.parseInt(connection2_list.get(c2).get("time_id").toString())-1)*demandInfoList.size());
        }
        Vector g1 = new Vector(1,J);

        connection2_list = tempService.findAll05(ShiroUtil.getOpenScenariosId());
        connection2_list = tempService.findAll06(ShiroUtil.getOpenScenariosId());
//## inflow:
        systemWebSocketHandler.sendMessageToUser( new TextMessage("#### inflow:"));
        systemWebSocketHandler.sendMessageToUser( new TextMessage("95%"));
        Vector f2 = new Vector();
        for(int c2=0;c2<connection2_list.size();c2++){
            double a = Integer.parseInt(connection_temp_list.get(c2).get("time_id").toString())-1;
            double b = Math.ceil((Integer.parseInt(connection_temp_list.get(c2).get("minutes").toString())-1)/route_time_unit);
            double c = 0;
            for(int ij=0;ij<timebucket_num[0].length-1;ij++){
                c = Math.min(timebucket_num[0][ij],timebucket_num[0][ij+1]);
            }
            double min;
            if (a < b && a < c) {
                min = a;
            } else if (c < a && c < b) {
                min = c;
            } else{
                min = b;
            }
            f2.add(Integer.parseInt(connection2_list.get(c2).get("dummy_out_id").toString())+min*demandInfoList.size()+N*c);
        }
        Vector g2 = new Vector(1,J);
//## all
        systemWebSocketHandler.sendMessageToUser( new TextMessage("#### all:"));
        systemWebSocketHandler.sendMessageToUser( new TextMessage("97%"));
        Vector ff = f1;
        for(int fi=0;fi<f2.size();fi++){
            ff.add(f2.indexOf(fi));
        }

        Vector gg = g1;
        for(int fi=0;fi<g2.size();fi++){
            ff.add(g2.indexOf(fi));
        }

        logger.info("M31");
        Matrix M31 = DenseMatrix.Factory.zeros(route_list.size(), I);
        Matrix M32 = DenseMatrix.Factory.zeros(route_list.size(), I);
        Matrix M33 = DenseMatrix.Factory.zeros(route_list.size(), I);
        Matrix M34 = DenseMatrix.Factory.zeros(route_list.size(), I);
        Matrix M35 = DenseMatrix.Factory.zeros(route_list.size(), I);
        int max = 0;
        for(int iii=0;iii<timebucket_num[i].length;iii++){
            max = Math.max(timebucket_num[i][iii]*2,I);
        }
        Vector t = new Vector(N*max);
        for(int m=0;m<J;m++){
            for(int mi=0;mi<J;mi++){
                M31.setAsInt(v.indexOf(mi),m,mi);
                M32.setAsInt(v.indexOf(mi),m,mi);
                M33.setAsInt(v.indexOf(mi),m,mi);
                M34.setAsInt(v.indexOf(mi),m,mi);
                M35.setAsInt(v.indexOf(mi),m,mi);
            }
        }

        Matrix M41 = M31;
        Matrix M42 = M31;
        Matrix M43 = M31;
        Matrix M44 = M32;
        Matrix M45 = M31;

        logger.info("M41");
        Matrix M51 = DenseMatrix.Factory.zeros(route_list.size(), I);
        Matrix M52 = DenseMatrix.Factory.zeros(route_list.size(), I);
        Matrix M53 = DenseMatrix.Factory.zeros(route_list.size(), I);
        Matrix M54 = DenseMatrix.Factory.zeros(route_list.size(), I);
        Matrix M55 = DenseMatrix.Factory.zeros(route_list.size(), I);
        v = new Vector(1,I);
        Vector vj = new Vector(1,I);
        for(int m=0;m<J;m++){
            for(int mi=0;mi<J;mi++){
                M51.setAsInt(v.indexOf(mi),m,mi);
                M52.setAsInt(vj.indexOf(mi),m,mi);
                M53.setAsInt(Integer.parseInt(connection_temp_list.get(mi).get("distance").toString())>dist_limit_bike
                        ?Integer.parseInt(connection_temp_list.get(mi).get("distance").toString())
                        :Integer.parseInt(connection_temp_list.get(mi).get("cross_river").toString()),m,mi);
                M54.setAsInt(v.indexOf(mi),m,mi);
                M55.setAsInt(Integer.parseInt(connection_temp_list.get(mi).get("distance").toString())>dist_limit_dada
                        ?Integer.parseInt(connection_temp_list.get(mi).get("distance").toString())
                        :Integer.parseInt(connection_temp_list.get(mi).get("cross_river").toString()),m,mi);
            }
        }
//########
        systemWebSocketHandler.sendMessageToUser( new TextMessage("//########"));
        systemWebSocketHandler.sendMessageToUser( new TextMessage("98%"));
        long consI = M11.getColumnCount()+M12.getColumnCount()+M13.getColumnCount()+M14.getColumnCount()+M15.getColumnCount();
        long consJ = M11.getRowCount()+M21.getRowCount()+M31.getRowCount()+M41.getRowCount()+M51.getRowCount();
        Matrix cons = DenseMatrix.Factory.zeros(consI, consJ);
        for(int m=0;m<M11.getColumnCount();m++){
            for(int mi=0;mi<M11.getRowCount();mi++) {
                cons.setAsInt(M11.getAsInt(m,mi),M11.getColumnCount()+m,M11.getRowCount()+mi);
            }
        }
        for(long m=M11.getColumnCount();m<M12.getColumnCount();m++){
            for(long mi=0;mi<M12.getRowCount();mi++) {
                cons.setAsInt(M12.getAsInt(m,mi),M12.getColumnCount()+m,M12.getRowCount()+mi);
            }
        }
        for(long m=M12.getColumnCount();m<M13.getColumnCount();m++){
            for(long mi=0;mi<M13.getRowCount();mi++) {
                cons.setAsInt(M13.getAsInt(m,mi),M13.getColumnCount()+m,M13.getRowCount()+mi);
            }
        }
        for(long m=M13.getColumnCount();m<M14.getColumnCount();m++){
            for(long mi=0;mi<M14.getRowCount();mi++) {
                cons.setAsInt(M14.getAsInt(m,mi),M14.getColumnCount()+m,M14.getRowCount()+mi);
            }
        }
        for(long m=M14.getColumnCount();m<M15.getColumnCount();m++){
            for(long mi=0;mi<M15.getRowCount();mi++) {
                cons.setAsInt(M15.getAsInt(m,mi),M15.getColumnCount()+m,M15.getRowCount()+mi);
            }
        }
        ///////////////
        for(int m=0;m<M11.getColumnCount();m++){
            for(int mi=0;mi<M11.getRowCount();mi++) {
                cons.setAsInt(M11.getAsInt(m,mi),M11.getColumnCount()+m,M11.getRowCount()+mi);
            }
        }
        for(long m=0;m<M12.getColumnCount();m++){
            for(long mi=M11.getRowCount();mi<M12.getRowCount();mi++) {
                cons.setAsInt(M12.getAsInt(m,mi),M12.getColumnCount()+m,M12.getRowCount()+mi);
            }
        }
        for(long m=0;m<M13.getColumnCount();m++){
            for(long mi=M12.getRowCount();mi<M13.getRowCount();mi++) {
                cons.setAsInt(M13.getAsInt(m,mi),M13.getColumnCount()+m,M13.getRowCount()+mi);
            }
        }
        for(long m=0;m<M14.getColumnCount();m++){
            for(long mi=M13.getRowCount();mi<M14.getRowCount();mi++) {
                cons.setAsInt(M14.getAsInt(m,mi),M14.getColumnCount()+m,M14.getRowCount()+mi);
            }
        }
        for(long m=0;m<M15.getColumnCount();m++){
            for(long mi=M14.getRowCount();mi<M15.getRowCount();mi++) {
                cons.setAsInt(M15.getAsInt(m,mi),M15.getColumnCount()+m,M15.getRowCount()+mi);
            }
        }
        logger.info("connection_temp_list");
        for(int ci=0;ci<connection_temp_list.size();ci++){
            connection_temp_list.get(ci).put("kmh_didi",Integer.parseInt(OD_demand_list.get(ci).get("km").toString())<=10?Integer.parseInt(OD_demand_list.get(ci).get("km").toString())*speed1:0+
                    Integer.parseInt(OD_demand_list.get(ci).get("km").toString())>10&&
                    Integer.parseInt(OD_demand_list.get(ci).get("km").toString())<=30?
                    Integer.parseInt(OD_demand_list.get(ci).get("km").toString())*speed3:0+
                    Integer.parseInt(OD_demand_list.get(ci).get("km").toString())>=30?
                    Integer.parseInt(OD_demand_list.get(ci).get("km").toString())*speed4:0);
            connection_temp_list.get(ci).put("kmh_truck",Integer.parseInt(OD_demand_list.get(ci).get("km").toString())<=10?Integer.parseInt(OD_demand_list.get(ci).get("km").toString())*speed1:0+
                    Integer.parseInt(OD_demand_list.get(ci).get("km").toString())>10&&
                    Integer.parseInt(OD_demand_list.get(ci).get("km").toString())<=30?
                    Integer.parseInt(OD_demand_list.get(ci).get("km").toString())*speed3:0+
                    Integer.parseInt(OD_demand_list.get(ci).get("km").toString())>=30?
                    Integer.parseInt(OD_demand_list.get(ci).get("km").toString())*speed4:0);
            connection_temp_list.get(ci).put("kmh_bike",Integer.parseInt(OD_demand_list.get(ci).get("km").toString())<=10?Integer.parseInt(OD_demand_list.get(ci).get("km").toString())*speed1:0);
            double min_didi_t = 0;
            min_didi_t = Double.parseDouble(connection_temp_list.get(ci).get("distance").toString())/
                    Double.parseDouble(connection_temp_list.get(ci).get("kmh_didi").toString())*60;
            connection_temp_list.get(ci).put("min_didi",min_didi_t);
            connection_temp_list.get(ci).put("cost_bike",11);
            double di = Double.parseDouble(connection_temp_list.get(ci).get("distance").toString());
            double didi = Double.parseDouble(connection_temp_list.get(ci).get("min_didi").toString());
            double d = Math.max((di>12?di:0)*(di-12)+di*2.4+didi*0.4,13);
            connection_temp_list.get(ci).put("cost_didi",d);
            connection_temp_list.get(ci).put("cost_truck", di);
            connection_temp_list.get(ci).put("cost_data",(Double.parseDouble(connection_temp_list.get(ci).get("distance").toString())-2)
                    *2*(Double.parseDouble(connection_temp_list.get(ci).get("distance").toString())>2?
                    Double.parseDouble(connection_temp_list.get(ci).get("distance").toString()):0)+10);
        }

        Vector obj = new Vector();

        //根据场景ID查询SiteDist
        systemWebSocketHandler.sendMessageToUser( new TextMessage("根据场景ID查询SiteDist"));
        systemWebSocketHandler.sendMessageToUser( new TextMessage("99%"));
        List<SiteInfo> siteInfoList = siteInfoService.findByParam(map);

        double[] outflow_lim = new double[siteInfoList.size()];
        for(int iii=0;iii<outflow_lim.length;iii++){
            outflow_lim[iii] = Integer.parseInt(siteInfoList.get(iii).getMaxOperateNum());
        }
//        double[] outflow_lim = res[1];;
        double[] inflow_lim = outflow_lim;
        double[] didi_outflow_lim = outflow_lim;
        double[] didi_inflow_lim = didi_outflow_lim;

        double[] rhs = new double[M+J+1+outflow_lim.length*4];
        for(int iii=0;iii<M;iii++){
            rhs[iii] = 1;
        }
        for(int iii=0;iii<J;iii++){
            rhs[M+iii] = 0;
        }
        for(int iii=0;iii<outflow_lim.length;iii++){
            rhs[M+J+iii] = outflow_lim[iii];
        }
        for(int iii=0;iii<inflow_lim.length;iii++){
            rhs[M+J+outflow_lim.length+iii] = inflow_lim[iii];
        }
        for(int iii=0;iii<didi_outflow_lim.length;iii++){
            rhs[M+J+outflow_lim.length+inflow_lim.length+iii] = didi_outflow_lim[iii];
        }
        for(int iii=0;iii<didi_inflow_lim.length;iii++){
            rhs[M+J+outflow_lim.length+inflow_lim.length+didi_outflow_lim.length+iii] = didi_inflow_lim[iii];
        }
        rhs[M+J+outflow_lim.length+inflow_lim.length+didi_outflow_lim.length+1] = 0;

        i = I+J;
        String[] types = new String[i];
        for(int n=0;n<I;n++){
            types[n] = "B";
        }
        for(int n=0;n<J;n++){
            types[I+n] = "I";
        }
//        int full_time = 210;
//        int route_time_unit = 10;
        int max_timebucket_num = 0;
//        int[] timebucket_num = new int[full_time/route_time_unit];
//        for(int ii=0;ii<full_time/route_time_unit;ii++){
//            timebucket_num[ii] = ii;
//            max_timebucket_num = timebucket_num[ii];
//        }
        i = M + J + N*4*max_timebucket_num;
        String[] sense = new String[i+1];
        for(int n=0;n<M;n++){
            sense[n] ="=";
        }
        for(int n=0;n<J;n++){
            sense[M+n] ="<=";
        }
        int W = i-M-J;
        for(int n=0;n<W;n++){
            sense[M+J+n] ="<=";
        }
        sense[i+1] ="=";

        logger.info("model");
//        Map<String,Object> model = new HashMap<String,Object>();
//        model.put("A",cons);
//        model.put("obj",obj);
//        model.put("sense",sense);
//        model.put("rhs",rhs);
//        model.put("vtypes",types);
//        model.put("modelsense","min");
        //call gurobi
//        mip<-gurobi(model,params)

//        logger.info("GRBEnv test();");
//        test();

        logger.info("GRBEnv");
        try{
            GRBEnv    env   = new GRBEnv("mip1.log");
            GRBModel m = new GRBModel(env);
//            m.set(GRB.StringParam.valueOf("A"), cons.toString());
//            m.set(GRB.StringParam.valueOf("obj"), obj.toString());
//            m.set(GRB.StringParam.valueOf("sense"), sense.toString());
//            m.set(GRB.StringParam.valueOf("rhs"), rhs.toString());
//            m.set(GRB.StringParam.valueOf("vtypes"), types.toString());
//            m.set(GRB.StringParam.valueOf("modelsense"), "min");

            // Budget
            int Budget = 12;
            GRBLinExpr vtypes2 = new GRBLinExpr();
            GRBVar[] Elem1 = m.addVars(types.length, GRB.BINARY);
            for (int e = 0; e < types.length; e++) {
                String vname = types[e];
                Elem1[e].set(GRB.StringAttr.VarName, vname);
                vtypes2.addTerm(1.0, Elem1[e]);
            }
            m.addConstr(vtypes2, GRB.LESS_EQUAL, Budget, "Budget");

            GRBLinExpr rhs2 = new GRBLinExpr();
            GRBVar[] Elem2 = m.addVars(types.length, GRB.BINARY);
//            for (int e = 0; e < rhs.length; e++) {
//                double vname = rhs[e];
//                Elem1[e].set(GRB.DoubleAttr.RHS, vname);
//                rhs2.addTerm(1.0, Elem1[e]);
//            }
//            m.addConstr(rhs2, GRB.LESS_EQUAL, Budget, "Budget");

            GRBLinExpr sense2 = new GRBLinExpr();
            GRBVar[] Elem = m.addVars(sense.length, GRB.BINARY);
            for (int e = 0; e < sense.length; e++) {
                String vname = sense[e];
                Elem[e].set(GRB.StringAttr.VarName, vname);
                sense2.addTerm(1.0, Elem[e]);
            }
            m.addConstr(sense2, GRB.LESS_EQUAL, Budget, "Budget");

            GRBLinExpr objn = new GRBLinExpr();
            int    SetObjPriority[] = new int[] {3, 2, 2, 1};
            double SetObjWeight[]   = new double[] {1.0, 0.25, 1.25, 1.0};
            for (int e = 0; e < obj.size(); e++){
                m.setObjectiveN(objn, i, SetObjPriority[i], SetObjWeight[i],
                        1.0 + i, 0.01, obj.get(e).toString());
            }
            m.set(GRB.IntAttr.ModelSense, GRB.MINIMIZE);

            // Populate A matrix
            double lb[] = new double[] {0, 0, 0};
            GRBVar[] vars = m.addVars(lb, null, null, null, null);
            char sense3[] = new char[] {'>', '>'};
            for (int ir = 0; ir < cons.numRows(); ir++) {
                GRBLinExpr expr = new GRBLinExpr();
                for (int j = 0; j < cons.numCols(); j++)
//                    double[][] A = cons.index(ir,j);
                    if (cons.index(ir,j) != 0)
                        expr.addTerm(cons.index(ir,j), vars[j]);
                m.addConstr(expr, sense3[i], rhs[i], "");
            }
//
//            GRBModel  bestModel = new GRBModel(m);
//
//            logger.info("RelayMode params");
//            bestModel.set(GRB.DoubleParam.TimeLimit, 6000);
//            bestModel.set(GRB.DoubleParam.MIPGap, 0.05);
//
//            systemWebSocketHandler.sendMessageToUser( new TextMessage("optimize"));
//            logger.info("optimize");
//            bestModel.optimize();
//            // Dispose of model and environment
//            bestModel.dispose();
//            env.dispose();
//            logger.info("dispose");
//            systemWebSocketHandler.sendMessageToUser( new TextMessage("100%"));
        }catch (Exception e){
            e.printStackTrace();
        }

//        try {
//            GRBEnv env2 = new GRBEnv();
//            double c[] = cons;
//            double Q[][] = cons;
//            double A[][] = obj;
//            char sense2[] = sense;
//            double rhs2[] = rhs;
//            double lb[] = types;
//            boolean success;
//            double sol[] = new double[3];
//
//
//            success = dense_optimize(env2, 2, 3, c, Q, A, sense2, rhs2,
//                    lb, null, null, sol);
//
//            if (success) {
//                System.out.println("x: " + sol[0] + ", y: " + sol[1] + ", z: " + sol[2]);
//            }
//
//            // Dispose of environment
//            env2.dispose();
//
//        } catch (GRBException e) {
//            System.out.println("Error code: " + e.getErrorCode() + ". " +
//                    e.getMessage());
//            e.printStackTrace();
//        }
    }

    protected static boolean
    dense_optimize(GRBEnv     env,
                   int        rows,
                   int        cols,
                   double[]   c,      // linear portion of objective function
                   double[][] Q,      // quadratic portion of objective function
                   double[][] A,      // constraint matrix
                   char[]     sense,  // constraint senses
                   double[]   rhs,    // RHS vector
                   double[]   lb,     // variable lower bounds
                   double[]   ub,     // variable upper bounds
                   char[]     vtype,  // variable types (continuous, binary, etc.)
                   double[]   solution) {

        boolean success = false;

        try {
            GRBModel model = new GRBModel(env);

            // Add variables to the model

            GRBVar[] vars = model.addVars(lb, ub, null, vtype, null);

            // Populate A matrix

            for (int i = 0; i < rows; i++) {
                GRBLinExpr expr = new GRBLinExpr();
                for (int j = 0; j < cols; j++)
                    if (A[i][j] != 0)
                        expr.addTerm(A[i][j], vars[j]);
                model.addConstr(expr, sense[i], rhs[i], "");
            }

            // Populate objective

            GRBQuadExpr obj = new GRBQuadExpr();
            if (Q != null) {
                for (int i = 0; i < cols; i++)
                    for (int j = 0; j < cols; j++)
                        if (Q[i][j] != 0)
                            obj.addTerm(Q[i][j], vars[i], vars[j]);
                for (int j = 0; j < cols; j++)
                    if (c[j] != 0)
                        obj.addTerm(c[j], vars[j]);
                model.setObjective(obj);
            }

            // Solve model

            model.optimize();

            // Extract solution

            if (model.get(GRB.IntAttr.Status) == GRB.Status.OPTIMAL) {
                success = true;

                for (int j = 0; j < cols; j++)
                    solution[j] = vars[j].get(GRB.DoubleAttr.X);
            }

            model.dispose();

        } catch (GRBException e) {
            System.out.println("Error code: " + e.getErrorCode() + ". " +
                    e.getMessage());
            e.printStackTrace();
        }

        return success;
    }
    public void test() {
        try {
            GRBEnv    env   = new GRBEnv("mip1.log");
            GRBModel  model = new GRBModel(env);

            // Create variables

            GRBVar x = model.addVar(0.0, 1.0, 0.0, GRB.BINARY, "x");
            GRBVar y = model.addVar(0.0, 1.0, 0.0, GRB.BINARY, "y");
            GRBVar z = model.addVar(0.0, 1.0, 0.0, GRB.BINARY, "z");

            // Set objective: maximize x + y + 2 z

            GRBLinExpr expr = new GRBLinExpr();
            expr.addTerm(1.0, x); expr.addTerm(1.0, y); expr.addTerm(2.0, z);
            model.setObjective(expr, GRB.MAXIMIZE);

            // Add constraint: x + 2 y + 3 z <= 4

            expr = new GRBLinExpr();
            expr.addTerm(1.0, x); expr.addTerm(2.0, y); expr.addTerm(3.0, z);
            model.addConstr(expr, GRB.LESS_EQUAL, 4.0, "c0");

            // Add constraint: x + y >= 1

            expr = new GRBLinExpr();
            expr.addTerm(1.0, x); expr.addTerm(1.0, y);
            model.addConstr(expr, GRB.GREATER_EQUAL, 1.0, "c1");

            // Optimize model

            model.optimize();

            System.out.println(x.get(GRB.StringAttr.VarName)
                    + " " +x.get(GRB.DoubleAttr.X));
            System.out.println(y.get(GRB.StringAttr.VarName)
                    + " " +y.get(GRB.DoubleAttr.X));
            System.out.println(z.get(GRB.StringAttr.VarName)
                    + " " +z.get(GRB.DoubleAttr.X));

            System.out.println("Obj: " + model.get(GRB.DoubleAttr.ObjVal));

            // Dispose of model and environment

            model.dispose();
            env.dispose();

        } catch (GRBException e) {
            System.out.println("Error code: " + e.getErrorCode() + ". " +
                    e.getMessage());
        }
    }
}
