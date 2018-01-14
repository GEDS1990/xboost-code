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
import org.apache.spark.mllib.linalg.DenseMatrix;
import org.apache.spark.mllib.linalg.Matrix;
import org.joda.time.DateTimeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.socket.TextMessage;
import java.util.*;
import gurobi.*;

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
        this.siteInfoService = siteInfoService;
    }
    public void run() throws RuntimeException{
        logger.info("RelayMode init");
//        double[] dddd = {1.0,1.0,1.0};
//        logger.info("spark.Matrix");
//        org.apache.spark.mllib.linalg.Matrix mmm = new org.apache.spark.mllib.linalg.DenseMatrix(16333, 16333,dddd);
//        logger.info("spark.Matrix");

//        Matrix rrrr = DenseMatrix.Factory.zeros(12333, 12333);
//        logger.info("16333"+ DateTimeUtils.currentTimeMillis());
//        Matrix ww = DenseMatrix.Factory.zeros(16333, 16333);
//        logger.info("16333"+ DateTimeUtils.currentTimeMillis());
        //params
        systemWebSocketHandler.sendMessageToUser( new TextMessage("params:"));
        systemWebSocketHandler.sendMessageToUser( new TextMessage("1%"));
        logger.info("TimeLimit = 600");
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
            OD_demand.put("km",speed2);
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
//        Matrix M1133333 = DenseMatrix.Factory.zeros(3, 3);
//        System.out.println(route_list.size()+":"+I);
        int tagrelay = 0;
        logger.info("tag:"+tagrelay++);

        logger.info("spark.Matrix");
        double[] v222 = new double[route_list.size()*I];
        Vector v2 = new Vector();
        for(int mj=M;mj<J;mj++){
            v2.add(mj,mj);
        }
        for(int m=0;m<route_list.size();m++){
            for(int mi=0;mi<I;mi++){
                v222[m+mi]=v2.indexOf(mi);
            }
        }
        Matrix M11 = new DenseMatrix(route_list.size(), I,v222);
        Matrix M12 = new DenseMatrix(route_list.size(), I,v222);
        Matrix M13 = new DenseMatrix(route_list.size(), I,v222);
        Matrix M14 = new DenseMatrix(route_list.size(), I,v222);
        Matrix M15 = new DenseMatrix(route_list.size(), I,v222);
        logger.info("spark.Matrix");


//        Matrix M11 = DenseMatrix.Factory.zeros(route_list.size(), I);
//        logger.info("tag:"+tagrelay++);
//        Matrix M12 = DenseMatrix.Factory.zeros(route_list.size(), I);
//        logger.info("tag:"+tagrelay++);
//        Matrix M13 = DenseMatrix.Factory.zeros(route_list.size(), I);
//        logger.info("tag:"+tagrelay++);
//        Matrix M14 = DenseMatrix.Factory.zeros(route_list.size(), I);
//        logger.info("tag:"+tagrelay++);
//        Matrix M15 = DenseMatrix.Factory.zeros(route_list.size(), I);
//        logger.info("tag:"+tagrelay++);
//        Vector v = new Vector();
//        for(int mj=M;mj<J;mj++){
//            v.add(mj,mj);
//        }
//        logger.info("tag:"+tagrelay++);
//        for(int m=0;m<route_list.size();m++){
//            for(int mi=0;mi<I;mi++){
//                M11.setAsInt(v.indexOf(mi),m,mi);
//                M12.setAsInt(v.indexOf(mi),m,mi);
//                M13.setAsInt(v.indexOf(mi),m,mi);
//                M14.setAsInt(v.indexOf(mi),m,mi);
//                M15.setAsInt(v.indexOf(mi),m,mi);
//            }
//        }
        logger.info("tag:"+tagrelay++);

        logger.info("M21");
//        Matrix M21 = DenseMatrix.Factory.zeros(route_list.size(), I);
//        Matrix M22 = DenseMatrix.Factory.zeros(route_list.size(), I);
//        Matrix M23 = DenseMatrix.Factory.zeros(route_list.size(), I);
//        Matrix M24 = DenseMatrix.Factory.zeros(route_list.size(), I);
//        Matrix M25 = DenseMatrix.Factory.zeros(route_list.size(), I);
//        for(int m=0;m<J;m++){
//            for(int mi=0;mi<J;mi++){
//                M21.setAsInt(v.indexOf(mi),m,mi);
//                M22.setAsInt(-truck_capacity,m,mi);
//                M23.setAsInt(-truck_capacity2,m,mi);
//                M24.setAsInt(-truck_capacity3,m,mi);
//                M25.setAsInt(-truck_capacity4,m,mi);
//            }
//        }
        double[] v23 = new double[J*I];
        double[] v24 = new double[J*I];
        for(int m=0;m<J;m++){
            for(int mi=0;mi<I;mi++){
                v24[m+mi]=v2.indexOf(mi);
                v23[m+mi]=-truck_capacity;
            }
        }
        Matrix M21 = new DenseMatrix(J, I,v24);
        Matrix M22 = new DenseMatrix(J, I,v23);
        Matrix M23 = new DenseMatrix(J, I,v23);
        Matrix M24 = new DenseMatrix(J, I,v23);
        Matrix M25 = new DenseMatrix(J, I,v23);

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
//        Matrix M31 = DenseMatrix.Factory.zeros(route_list.size(), I);
//        Matrix M32 = DenseMatrix.Factory.zeros(route_list.size(), I);
//        Matrix M33 = DenseMatrix.Factory.zeros(route_list.size(), I);
//        Matrix M34 = DenseMatrix.Factory.zeros(route_list.size(), I);
//        Matrix M35 = DenseMatrix.Factory.zeros(route_list.size(), I);
        int max = 0;
        for(int iii=0;iii<timebucket_num[i].length;iii++){
            max = Math.max(timebucket_num[i][iii]*2,I);
        }
        Vector t = new Vector(N*max);
//        for(int m=0;m<J;m++){
//            for(int mi=0;mi<J;mi++){
//                M31.setAsInt(v.indexOf(mi),m,mi);
//                M32.setAsInt(v.indexOf(mi),m,mi);
//                M33.setAsInt(v.indexOf(mi),m,mi);
//                M34.setAsInt(v.indexOf(mi),m,mi);
//                M35.setAsInt(v.indexOf(mi),m,mi);
//            }
//        }
        double[] v33 = new double[J*J];
        for(int m=0;m<J;m++){
            for(int mi=0;mi<J;mi++){
                v33[m+mi]=t.indexOf(mi);
            }
        }
        Matrix M31 = new DenseMatrix(J, J,v33);
        Matrix M32 = new DenseMatrix(J, J,v33);
        Matrix M33 = new DenseMatrix(J, J,v33);
        Matrix M34 = new DenseMatrix(J, J,v33);
        Matrix M35 = new DenseMatrix(J, J,v33);

        Matrix M41 = M31;
        Matrix M42 = M31;
        Matrix M43 = M31;
        Matrix M44 = M32;
        Matrix M45 = M31;

        logger.info("M41");
//        Matrix M51 = DenseMatrix.Factory.zeros(route_list.size(), I);
//        Matrix M52 = DenseMatrix.Factory.zeros(route_list.size(), I);
//        Matrix M53 = DenseMatrix.Factory.zeros(route_list.size(), I);
//        Matrix M54 = DenseMatrix.Factory.zeros(route_list.size(), I);
//        Matrix M55 = DenseMatrix.Factory.zeros(route_list.size(), I);
        Vector v = new Vector(1,I);
        Vector vj = new Vector(1,I);
//        for(int m=0;m<J;m++){
//            for(int mi=0;mi<J;mi++){
//                M51.setAsInt(v.indexOf(mi),m,mi);
//                M52.setAsInt(vj.indexOf(mi),m,mi);
//                M53.setAsInt(Integer.parseInt(connection_temp_list.get(mi).get("distance").toString())>dist_limit_bike
//                        ?Integer.parseInt(connection_temp_list.get(mi).get("distance").toString())
//                        :Integer.parseInt(connection_temp_list.get(mi).get("cross_river").toString()),m,mi);
//                M54.setAsInt(v.indexOf(mi),m,mi);
//                M55.setAsInt(Integer.parseInt(connection_temp_list.get(mi).get("distance").toString())>dist_limit_dada
//                        ?Integer.parseInt(connection_temp_list.get(mi).get("distance").toString())
//                        :Integer.parseInt(connection_temp_list.get(mi).get("cross_river").toString()),m,mi);
//            }
//        }
        double[] v51 = new double[J*J];
        double[] v52 = new double[J*J];
        double[] v53 = new double[J*J];
        double[] v54 = new double[J*J];
        double[] v55 = new double[J*J];
        for(int m=0;m<J;m++){
            for(int mi=0;mi<J;mi++){
                v51[m+mi]=v.indexOf(mi);
                v52[m+mi]=vj.indexOf(mi);
                v53[m+mi]=vj.indexOf(mi);
                v54[m+mi]=vj.indexOf(mi);
                v55[m+mi]=vj.indexOf(mi);
//                v53[m+mi] = Integer.parseInt(connection_temp_list.get(mi).get("distance").toString())>dist_limit_bike
//                        ?Integer.parseInt(connection_temp_list.get(mi).get("distance").toString())
//                        :Integer.parseInt(connection_temp_list.get(mi).get("cross_river").toString());
//                v54[m+mi]= v.indexOf(mi);
//                v55[m+mi]=Integer.parseInt(connection_temp_list.get(mi).get("distance").toString())>dist_limit_dada
//                        ?Integer.parseInt(connection_temp_list.get(mi).get("distance").toString())
//                        :Integer.parseInt(connection_temp_list.get(mi).get("cross_river").toString());
            }
        }
        Matrix M51 = new DenseMatrix(J, J,v51);
        Matrix M52 = new DenseMatrix(J, J,v52);
        Matrix M53 = new DenseMatrix(J, J,v53);
        Matrix M54 = new DenseMatrix(J, J,v54);
        Matrix M55 = new DenseMatrix(J, J,v55);

//########
        systemWebSocketHandler.sendMessageToUser( new TextMessage("//########"));
        systemWebSocketHandler.sendMessageToUser( new TextMessage("98%"));
        long consI = M11.numCols()+M12.numCols()+M13.numCols()+M14.numCols()+M15.numCols();
        long consJ = M11.numRows()+M21.numRows()+M31.numRows()+M41.numRows()+M51.numRows();
//        Matrix cons = DenseMatrix.Factory.zeros(consI, consJ);
//        double[] vcons = new double[M11.numCols()*M11.numRows()];
//        for(int m=0;m<M11.numCols();m++){
//            for(int mi=0;mi<M11.numRows();mi++) {
////                cons.setAsInt(M11.index(m,mi),M11.numCols()+m,M11.numRows()+mi);
//                vcons[m+mi] = M11.index(m,mi);
//            }
//        }
        Matrix cons = new DenseMatrix(J, J,v51);
//        for(int m=M11.numCols();m<M12.numCols();m++){
//            for(int mi=0;mi<M12.numRows();mi++) {
////                cons.setAsInt(M12.index(m,mi),M12.numCols()+m,M12.numRows()+mi);
//                vcons[m+mi] = M12.index(m,mi);
//            }
//        }
//        cons.multiply(new DenseMatrix(M12.numCols()-M11.numCols(), M12.numRows(),vcons));
//        for(int m=M12.numCols();m<M13.numCols();m++){
//            for(int mi=0;mi<M13.numRows();mi++) {
////                cons.setAsInt(M13.index(m,mi),M13.numCols()+m,M13.numRows()+mi);
//                vcons[m+mi] = M13.index(m,mi);
//            }
//        }
//        cons.multiply(new DenseMatrix(M13.numCols()-M12.numCols(), M13.numRows(),vcons));
//        for(int m=M13.numCols();m<M14.numCols();m++){
//            for(int mi=0;mi<M14.numRows();mi++) {
////                cons.setAsInt(M14.index(m,mi),M14.numCols()+m,M14.numRows()+mi);
//                vcons[m+mi] = M14.index(m,mi);
//            }
//        }
//        cons.multiply(new DenseMatrix(M14.numCols()-M13.numCols(), M14.numRows(),vcons));
//        for(int m=M14.numCols();m<M15.numCols();m++){
//            for(int mi=0;mi<M15.numRows();mi++) {
////                cons.setAsInt(M15.index(m,mi),M15.numCols()+m,M15.numRows()+mi);
//                vcons[m+mi] = M15.index(m,mi);
//            }
//        }
//        cons.multiply(new DenseMatrix(M15.numCols()-M14.numCols(), M15.numRows(),vcons));
        ///////////////
//        for(int m=0;m<M11.numCols();m++){
//            for(int mi=0;mi<M11.numRows();mi++) {
////                cons.setAsInt(M11.index(m,mi),M11.numCols()+m,M11.numRows()+mi);
//                vcons[m+mi] = M11.index(m,mi);
//            }
//        }
//        for(int m=0;m<M12.numCols();m++){
//            for(int mi=M11.numRows();mi<M12.numRows();mi++) {
////                cons.setAsInt(M12.index(m,mi),M12.numCols()+m,M12.numRows()+mi);
//                vcons[m+mi] = M11.index(m,mi);
//            }
//        }
//        for(int m=0;m<M13.numCols();m++){
//            for(int mi=M12.numRows();mi<M13.numRows();mi++) {
////                cons.setAsInt(M13.index(m,mi),M13.numCols()+m,M13.numRows()+mi);
//                vcons[m+mi] = M11.index(m,mi);
//            }
//        }
//        for(int m=0;m<M14.numCols();m++){
//            for(int mi=M13.numRows();mi<M14.numRows();mi++) {
////                cons.setAsInt(M14.index(m,mi),M14.numCols()+m,M14.numRows()+mi);
//                vcons[m+mi] = M11.index(m,mi);
//            }
//        }
//        for(int m=0;m<M15.numCols();m++){
//            for(int mi=M14.numRows();mi<M15.numRows();mi++) {
////                cons.setAsInt(M15.index(m,mi),M15.numCols()+m,M15.numRows()+mi);
//                vcons[m+mi] = M11.index(m,mi);
//            }
//        }
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

        double[] obj = new double[I+connection_temp_list.size()*4];
        for(int e=0;e<I;e++){
            obj[e]=0;
        }
        for(int e=I;e<I+connection_temp_list.size();e++){
            obj[e]=Double.parseDouble(connection_temp_list.get(e-I).get("cost_truck").toString());
        }
        for(int e=I+connection_temp_list.size();e<I+connection_temp_list.size()*2;e++){
            obj[e]=Double.parseDouble(connection_temp_list.get(e-I-connection_temp_list.size()).get("cost_bike").toString());
        }
        for(int e=I+connection_temp_list.size()*2;e<I+connection_temp_list.size()*3;e++){
            obj[e]=Double.parseDouble(connection_temp_list.get(e-I-connection_temp_list.size()*2).get("cost_didi").toString());
        }
        for(int e=I+connection_temp_list.size()*3;e<I+connection_temp_list.size()*4;e++){
            obj[e]=Double.parseDouble(connection_temp_list.get(e-I-connection_temp_list.size()*3).get("cost_data").toString());
        }

        //根据场景ID查询SiteDist
        systemWebSocketHandler.sendMessageToUser( new TextMessage("SiteDist"));
        systemWebSocketHandler.sendMessageToUser( new TextMessage("99%"));
        List<SiteInfo> siteInfoList = siteInfoService.findAllSiteInfo(ShiroUtil.getOpenScenariosId());

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
        char[] types = new char[i];
        for(int n=0;n<I;n++){
            types[n] = "B".toCharArray()[0];
    }
        for(int n=0;n<J;n++){
            types[I+n] = "I".toCharArray()[0];
        }
        int max_timebucket_num = 0;
        i = M + J + N*4*max_timebucket_num;
//        sense
        char[] sense = new char[i+10];
        for(int n=0;n<M;n++){
//            sense[n] ="=".toCharArray()[0];
            sense[n] ="<".toCharArray()[0];
        }
        for(int n=0;n<J;n++){
            sense[M+n] ="<=".toCharArray()[0];
        }
        int W = i-M-J;
        for(int n=0;n<W;n++){
            sense[M+J+n] ="<=".toCharArray()[0];
        }
        sense[i+1] ="=".toCharArray()[0];

        logger.info("model");

        logger.info("GRBEnv");
//        try{
//            GRBEnv    env   = new GRBEnv("mip1.log");
//            GRBModel m = new GRBModel(env);
//            double lb[] = new double[i];
//            for(int q=0;q<lb.length;q++){
//                lb[q]=0;
//            }
//            GRBVar[] vars = m.addVars(lb, null, null, types, null);
////            cons
//            for (int iw = 0; iw < cons.numRows(); iw++) {
//                GRBLinExpr expr = new GRBLinExpr();
//                for (int jw = 0; jw < cons.numCols(); jw++)
//                    if (cons.index(iw,jw) != 0) {
//                        expr.addTerm(cons.index(iw,jw), vars[jw]);
//                    }
//                m.addConstr(expr, sense[iw], rhs[iw], "");
//            }
////            objn
//            GRBLinExpr objn = new GRBLinExpr();
//            for (int e = 0; e < obj.length; e++){
//                objn.addConstant(obj[e]);
//            }
//            m.setObjective(objn);
//            // Solve model
//            m.optimize();
//            // Extract solution
//            boolean success = false;
//            double[]   solution = new double[cons.numCols()];
//            logger.info("result:"+":"+GRB.Status.OPTIMAL);
//            if (m.get(GRB.IntAttr.Status) == GRB.Status.OPTIMAL) {
//                success = true;
//                for (int j = 0; j < cons.numCols(); j++){
//                    solution[j] = vars[j].get(GRB.DoubleAttr.X);
//                    logger.info("solution[j]"+j+":"+String.valueOf(solution[j]));
//                    systemWebSocketHandler.sendMessageToUser(new TextMessage(String.valueOf(solution[j])));
//                }
//            }
//            m.dispose();
//            // Dispose of environment
//            env.dispose();
//            systemWebSocketHandler.sendMessageToUser( new TextMessage("100%"));
//
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//////////////////////////////////////////////////////////
        logger.info("/n ////////////////////////////////////////////////////////////////");
        try {
            GRBEnv env2 = new GRBEnv();

//            double c2[] = new double[] {1, 1, 0};
            double c2[] = new double[cons.numCols()];
//                        objn
            for (int jw = 0; jw < cons.numCols(); jw++)
                c2[jw] = obj[jw];
//            double Q2[][] = new double[][] {{1, 1, 0}, {0, 1, 1}, {0, 0, 1}};
            double Q2[][] = null;
//            double Q2[][] = new double[cons.numRows()][cons.numCols()];
//            logger.info("iw:"+cons.numRows()+";jw:"+cons.numCols());
//            for (int iw = 0; iw < cons.numRows(); iw++) {
//                for (int jw = 0; jw < cons.numCols(); jw++)
//                    Q2[iw][jw] = cons.index(iw,jw);
//            }
            ///////
//            double A[][] = new double[][] {{1, 2, 3}, {1, 1, 0}};
            double A2[][] = new double[cons.numRows()][cons.numCols()];
            logger.info("iw:"+cons.numRows()+";jw:"+cons.numCols());
            for (int iw = 0; iw < cons.numRows(); iw++) {
                for (int jw = 0; jw < cons.numCols(); jw++)
                    A2[iw][jw] = cons.index(iw,jw);
            }
//            double A2[][] = null;
//            char sense2[] = new char[] {'>', '>'};
            logger.info("sense:"+sense.length);
            char sense2[] = new char[cons.numRows()];
            for(int is=0;is<cons.numRows();is++){
                sense2[is] = sense[is];
            }
//            double rhs2[] = new double[] {4, 1};
            logger.info("rhs:"+rhs.length);
            double rhs2[] = new double[cons.numRows()];
            for(int is=0;is<cons.numRows();is++){
                rhs2[is] = rhs[is];
            }
//            double lb2[] = new double[] {0, 0, 0};
            double lb2[] = new double[cons.numRows()];
            for(int q=0;q<lb2.length;q++){
                lb2[q]=0;
            }
//            double lb2[] = null;
//            double ub2[] = new double[cons.numRows()];
//            for(int q=0;q<ub2.length;q++){
//                ub2[q]=0;
//            }
            double ub2[] = null;
            boolean success;
            double sol2[] = new double[cons.numCols()];
            logger.info("sol2:"+sol2.length);

            Dense dense = new Dense();
            success = dense.dense_optimize(env2, cons.numRows(), cons.numCols(), c2, Q2, A2, sense2, rhs2,
                    lb2, ub2, types, sol2);

            if (success) {
                logger.info("success:");
                System.out.println("x: " + sol2[0] + ", y: " + sol2[1] + ", z: " + sol2[2]);
                double[] solution = sol2;
                makeResults(solution);
                double volume_to_ship = 0;
                for(int e=0;e<OD_demand_list.size();e++){
                    volume_to_ship += Double.parseDouble(OD_demand_list.get(e).get("volume").toString());
                }
                List<Map> route_opt = route_list;

            }else{
                logger.info("fail:");
            }

            // Dispose of environment
            env2.dispose();

        } catch (GRBException e) {
            System.out.println("Error code: " + e.getErrorCode() + ". " +
                    e.getMessage());
            e.printStackTrace();
        }
    }
    ////////////
    protected void makeResults(double[] solution){

    }
}
