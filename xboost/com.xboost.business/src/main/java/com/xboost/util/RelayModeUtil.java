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
//import org.apache.spark.mllib.linalg.DenseMatrix;
//import org.apache.spark.mllib.linalg.Matrix;
//import org.apache.spark.mllib.linalg.SparseMatrix;
import org.joda.time.DateTimeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.socket.TextMessage;

import java.io.File;
import java.util.*;
import gurobi.*;
import org.ujmp.core.Matrix;
import org.ujmp.core.SparseMatrix;
import org.ujmp.core.calculation.Calculation;

public class RelayModeUtil extends Thread implements IConstants {

    public com.xboost.pojo.Configuration config;
    public DemandInfoService demandInfoService;
    public SiteDistService siteDistService;
    public TempService tempService;
    public SiteInfoService siteInfoService;
    public String OpenScenariosId;
    private static Logger logger = LoggerFactory.getLogger(RelayModeUtil.class);

    public RelayModeUtil(TempService tempService, DemandInfoService demandInfoService, SiteDistService siteDistService, SiteInfoService siteInfoService){
        this.config = config;
        this.demandInfoService = demandInfoService;
        this.siteDistService = siteDistService;
        this.tempService = tempService;
        this.siteInfoService = siteInfoService;
        this.OpenScenariosId = ShiroUtil.getOpenScenariosId();
    }
    public void run() throws RuntimeException{
        long starttime = DateTimeUtils.currentTimeMillis();
        logger.info("RelayMode init starttime:"+starttime);
//        double[][] A2sss = new double[27216][];
//        logger.info("RelayMode init"+A2sss);
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
//        Configuration configuration = new Configuration();
//        configuration.setOptimizeIterations(TimeLimit);

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
        map.put("scenariosId", OpenScenariosId);
        //obj
        List<DemandInfo> flow_lim = demandInfoService.findByScenairoIdParam(map);
        Map<String,Object> connection = new HashMap<String,Object>();

        logger.info("flow_lim");
        List<Map> distance_ref_list= new ArrayList<Map>();
        List<Map> two_points_route_list= new ArrayList<Map>();
        List<Map> OD_demand_list= new ArrayList<Map>();
        List<Map> three_points_route_list= new ArrayList<Map>();
        List<Map> four_points_route_list= new ArrayList<Map>();
        List<Map> temp_list= new ArrayList<Map>();
        List<Map> connection_temp_list= new ArrayList<Map>();
        List<Map> connection2_list= new ArrayList<Map>();


        logger.info("OD_demand");
        int[] scenario_lim1 = {1,1,1,1,1};
        int[] scenario_lim2 = {9,12,15,18,21};
        for(int j=0;j<flow_lim.size();j++){
            Map<String,Object> OD_demand = new HashMap<String,Object>();
            OD_demand.put("OD_id",j+1);
            OD_demand.put("volume",Double.parseDouble(flow_lim.get(j).getVotes())/0.8511);
            OD_demand.put("inbound_id",flow_lim.get(j).getSiteCodeCollect());
            OD_demand.put("outbound_id",flow_lim.get(j).getSiteCodeDelivery());
            OD_demand.put("scenario",1);
            OD_demand.put("scenario_lim1",1);
            OD_demand.put("scenario_lim2",9);
            OD_demand.put("kmh",speed2);
            OD_demand.put("km",speed2);
            OD_demand.put("minutes",Integer.parseInt(flow_lim.get(j).getDurationEnd())-Integer.parseInt(flow_lim.get(j).getDurationStart()));
            OD_demand_list.add(OD_demand);
        }
        //distance_ref
        systemWebSocketHandler.sendMessageToUser( new TextMessage("distance_ref:"));
        systemWebSocketHandler.sendMessageToUser( new TextMessage("10%"));
        List<SiteDist> siteDistList = siteDistService.findSiteDistByScenariosId(map);
        logger.info("siteDistList");
        for(int j=0;j<siteDistList.size();j++){
            Map<String,Object> distance_ref = new HashMap<String,Object>();
//            logger.info("inbound_id："+siteDistList.get(j).getSiteCollect());
            distance_ref.put("inbound_id",siteDistList.get(j).getSiteCollect());
            distance_ref.put("outbound_id",siteDistList.get(j).getSiteDelivery());
            distance_ref.put("km",siteDistList.get(j).getCarDistance());
            distance_ref.put("minutes",siteDistList.get(j).getDurationNightDelivery());
            distance_ref.put("time",siteDistList.get(j).getDurationNightDelivery());
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
//        for(int e=0;e<distance_ref_list.size();e++){
//            logger.info(distance_ref_list.get(e).get("inbound_id").toString());
//        }
        //two_points_route
        systemWebSocketHandler.sendMessageToUser( new TextMessage("two_points_route:"));
        systemWebSocketHandler.sendMessageToUser( new TextMessage("20%"));

        logger.info("two_points_route_list");
        two_points_route_list = tempService.findAllTwoPointsRoute(OpenScenariosId);//size =8085
        //temp
        systemWebSocketHandler.sendMessageToUser( new TextMessage("temp:"));
        systemWebSocketHandler.sendMessageToUser( new TextMessage("30%"));
        systemWebSocketHandler.sendMessageToUser( new TextMessage("please waiting...."));
        for(int j=0;j<two_points_route_list.size();j++){
            for(int j2=0;j2<distance_ref_list.size();j2++){
//                logger.info(j+":"+two_points_route_list.get(j).get("inbound_id")+":"+j2+":"+distance_ref_list.get(j2).get("inbound_id"));
                boolean b = two_points_route_list.get(j).get("inbound_id").equals(distance_ref_list.get(j2).get("inbound_id"));
                boolean b2 = two_points_route_list.get(j).get("inbound_id") == distance_ref_list.get(j2).get("inbound_id");
                if(b||b2){
                    Map<String,Object> temp = new HashMap<String,Object>();
                    temp.put("m_od_id",two_points_route_list.get(j).get("m_od_id"));
                    temp.put("minutes",two_points_route_list.get(j).get("time"));
                    temp.put("i_route_id",two_points_route_list.get(j).get("inbound_id")+"-"+two_points_route_list.get(j).get("outbound_id"));
                    temp.put("distance",distance_ref_list.get(j2).get("km"));
                    temp.put("km",distance_ref_list.get(j2).get("km"));
                    temp.put("time",distance_ref_list.get(j2).get("minutes"));
                    temp.put("demand",two_points_route_list.get(j).get("volume"));
                    temp.put("point1",two_points_route_list.get(j).get("inbound_id"));
                    temp.put("point2",0);
                    temp.put("point3",0);
                    temp.put("point4",two_points_route_list.get(j).get("outbound_id"));
                    temp.put("outbound_id",two_points_route_list.get(j).get("outbound_id"));
                    temp.put("inbound_id",two_points_route_list.get(j).get("inbound_id"));
                    temp.put("connection1",two_points_route_list.get(j).get("inbound_id")+"-"+two_points_route_list.get(j).get("outbound_id"));
                    temp.put("connection2","");
                    temp.put("connection3","");
                    temp.put("time1",distance_ref_list.get(j2).get("minutes"));
                    temp.put("time2",0);
                    temp.put("time3",0);
                    temp.put("dist1",distance_ref_list.get(j2).get("km"));
                    temp.put("dist2",0);
                    temp.put("dist3",0);
                    temp.put("timecost",-Integer.parseInt(distance_ref_list.get(j2).get("minutes").toString()));
                    temp.put("scenario",two_points_route_list.get(j).get("scenario"));
                    temp.put("scenario_lim1",two_points_route_list.get(j).get("scenario_lim1"));
                    temp.put("scenario_lim2",two_points_route_list.get(j).get("scenario_lim2"));
                    temp.put("wait_time",0);
                    temp_list.add(temp);
                }
            }
        }
        try {
            listSort(temp_list,"distance");
        } catch (Exception e) {
            e.printStackTrace();
        }
        for(int e=0;e<temp_list.size();e++){
            if(e>=5){
                temp_list.remove(e);
            }
        }
//        temp_list = two_points_route_list;
        //three_points_route
        systemWebSocketHandler.sendMessageToUser( new TextMessage("three_points_route:"));
        systemWebSocketHandler.sendMessageToUser( new TextMessage("35%"));
        systemWebSocketHandler.sendMessageToUser( new TextMessage("It may take some minutes , please waiting...."));
        for(int j=0;j<temp_list.size();j++){
            for(int j2=0;j2<distance_ref_list.size();j2++){
                boolean b = temp_list.get(j).get("inbound_id").equals(distance_ref_list.get(j2).get("inbound_id"));
                boolean bwhere1 = !temp_list.get(j).get("point1").equals(temp_list.get(j).get("outbound_id"));
                boolean bwhere2 = !temp_list.get(j).get("point4").equals(temp_list.get(j).get("outbound_id"));
                boolean bwhere3 = (Double.parseDouble(temp_list.get(j).get("minutes").toString())+
                        Double.parseDouble(distance_ref_list.get(j2).get("minutes").toString())<=190);
                boolean bwhere4 = Double.parseDouble(temp_list.get(j).get("minutes").toString())+
                        Double.parseDouble(distance_ref_list.get(j2).get("minutes").toString())<=2*Double.parseDouble(temp_list.get(j2).get("time").toString());
                if(!bwhere1&&bwhere2&&bwhere3&&bwhere4)
                    continue;
                boolean bon1 = temp_list.get(j).get("outbound_id").toString().equals(distance_ref_list.get(j2).get("inbound_id").toString());
                boolean bon2 = temp_list.get(j).get("point4").toString().equals(distance_ref_list.get(j2).get("outbound_id").toString());
                boolean bon = bon1&&bon2;
                if(bon){
                    Map<String,Object> three_points_route = new HashMap<String,Object>();
                    three_points_route.put("m_od_id",temp_list.get(j).get("OD_id"));
                    three_points_route.put("scenario",temp_list.get(j).get("scenario"));
                    three_points_route.put("scenario_lim1",temp_list.get(j).get("scenario_lim1"));
                    three_points_route.put("scenario_lim2",temp_list.get(j).get("scenario_lim2"));
                    three_points_route.put("i_route_id",temp_list.get(j).get("point1")+"-"+temp_list.get(j).get("outbound_id")+"-"+temp_list.get(j).get("point4"));
                    three_points_route.put("distance",Double.parseDouble(temp_list.get(j).get("km").toString())+Double.parseDouble(distance_ref_list.get(j2).get("km").toString()));
                    three_points_route.put("time",Double.parseDouble(temp_list.get(j).get("minutes").toString())+Double.parseDouble(distance_ref_list.get(j2).get("minutes").toString()));
                    three_points_route.put("demand",temp_list.get(j).get("demand"));
                    three_points_route.put("point1",temp_list.get(j).get("point1"));
                    three_points_route.put("point2",temp_list.get(j).get("outbound_id"));
                    three_points_route.put("point3",0);
                    three_points_route.put("point4",temp_list.get(j).get("point4"));
                    three_points_route.put("connection1",temp_list.get(j).get("point1")+"-"+temp_list.get(j).get("outbound_id"));
                    three_points_route.put("connection2",temp_list.get(j).get("outbound_id")+"-"+temp_list.get(j).get("point4"));
                    three_points_route.put("connection3","");
                    three_points_route.put("time1",temp_list.get(j).get("minutes"));
                    three_points_route.put("time2",distance_ref_list.get(j2).get("minutes"));
                    three_points_route.put("time3",0);
                    three_points_route.put("dist1",temp_list.get(j).get("km"));
                    three_points_route.put("dist2",distance_ref_list.get(j2).get("km"));
                    three_points_route.put("dist3",0);
                    three_points_route.put("inbound_id",temp_list.get(j).get("inbound_id"));
                    three_points_route.put("timecost",-Double.parseDouble(temp_list.get(j).get("minutes").toString())-
                            Double.parseDouble(distance_ref_list.get(j2).get("minutes").toString())-20);
                    three_points_route.put("wait_time",20);
                    three_points_route_list.add(three_points_route);
                }
            }
        }
//        three_points_route_list = temp_list;
        try {
            listSort(three_points_route_list,"distance");
        } catch (Exception e) {
            e.printStackTrace();
        }
        for(int e=0;e<three_points_route_list.size();e++){
            if(e>=5){
                three_points_route_list.remove(e);
            }
        }
        //temp2
        systemWebSocketHandler.sendMessageToUser( new TextMessage("temp2:"));
        systemWebSocketHandler.sendMessageToUser( new TextMessage("40%"));
        systemWebSocketHandler.sendMessageToUser( new TextMessage("please waiting...."));
        for(int j=0;j<three_points_route_list.size();j++){
            for(int j2=0;j2<distance_ref_list.size();j2++){
                boolean b = three_points_route_list.get(j).get("point2").equals(distance_ref_list.get(j2).get("point2"));
                boolean b2 = three_points_route_list.get(j).get("inbound_id").equals(distance_ref_list.get(j2).get("inbound_id"));
                if(b){
                    Map<String,Object> temp = new HashMap<String,Object>();
                    temp.put("m_od_id",three_points_route_list.get(j).get("OD_id"));
                    temp.put("i_route_id",three_points_route_list.get(j).get("inbound_id")+"-"+three_points_route_list.get(j).get("outbound_id"));
                    temp.put("distance",distance_ref_list.get(j2).get("km"));
                    temp.put("time",distance_ref_list.get(j2).get("minutes"));
                    temp.put("minutes",distance_ref_list.get(j2).get("minutes"));
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
                    temp.put("timecost",-Double.parseDouble(distance_ref_list.get(j2).get("minutes").toString()));
                    temp.put("scenario",three_points_route_list.get(j).get("scenario"));
                    temp.put("scenario_lim1",three_points_route_list.get(j).get("scenario_lim1"));
                    temp.put("scenario_lim2",three_points_route_list.get(j).get("scenario_lim2"));
                    temp.put("wait_time",0);
                    temp_list.add(temp);
                }
            }
        }
        //four_points_route
        systemWebSocketHandler.sendMessageToUser( new TextMessage("four_points_route:"));
        systemWebSocketHandler.sendMessageToUser( new TextMessage("50%"));
        for(int j=0;j<temp_list.size();j++){
            for(int j2=0;j2<distance_ref_list.size();j2++){
                boolean b = temp_list.get(j).get("inbound_id").equals(distance_ref_list.get(j2).get("inbound_id"));
                boolean bwhere1 = !temp_list.get(j).get("point1").equals(temp_list.get(j).get("outbound_id"));
                boolean bwhere11 = !temp_list.get(j).get("point2").equals(temp_list.get(j).get("outbound_id"));
                boolean bwhere2 = !temp_list.get(j).get("point4").equals(temp_list.get(j).get("outbound_id"));
                boolean bwhere3 = (Double.parseDouble(temp_list.get(j).get("time1").toString())+
                        Double.parseDouble(temp_list.get(j).get("minutes").toString())+
                        Double.parseDouble(distance_ref_list.get(j2).get("minutes").toString())<=170);
                boolean bwhere4 = Double.parseDouble(temp_list.get(j).get("time1").toString())+
                        Double.parseDouble(temp_list.get(j).get("minutes").toString())+
                        Double.parseDouble(distance_ref_list.get(j2).get("minutes").toString())<=2*Double.parseDouble(distance_ref_list.get(j2).get("time").toString());
                if(!bwhere1&&bwhere11&&bwhere2&&bwhere3&&bwhere4)
                    continue;
                boolean bon1 = temp_list.get(j).get("outbound_id").toString()==
                        distance_ref_list.get(j2).get("inbound_id").toString();
                boolean bon2 = temp_list.get(j).get("point4").toString()==
                        distance_ref_list.get(j2).get("outbound_id").toString();
                boolean bon = bon1&&bon2;
                if(bon){
                    Map<String,Object> four_points_route = new HashMap<String,Object>();
                    four_points_route.put("m_od_id",temp_list.get(j).get("OD_id"));
                    four_points_route.put("scenario",temp_list.get(j).get("scenario"));
                    four_points_route.put("scenario_lim1",temp_list.get(j).get("scenario_lim1"));
                    four_points_route.put("scenario_lim2",temp_list.get(j).get("scenario_lim2"));
                    four_points_route.put("i_route_id",temp_list.get(j).get("point1")+"-"+temp_list.get(j).get("point2")+"-"
                            +temp_list.get(j).get("outbound_id")+"-"+temp_list.get(j).get("point4"));
                    four_points_route.put("distance",Double.parseDouble(temp_list.get(j).get("dist1").toString())+Double.parseDouble(temp_list.get(j).get("km").toString())+
                            Double.parseDouble(distance_ref_list.get(j).get("km").toString()));
                    four_points_route.put("time",Double.parseDouble(temp_list.get(j).get("time1").toString())+Double.parseDouble(temp_list.get(j).get("minutes").toString())+
                            Double.parseDouble(distance_ref_list.get(j).get("minutes").toString()));
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
                    four_points_route.put("timecost",-Double.parseDouble(temp_list.get(j).get("time1").toString())-
                            Double.parseDouble(temp_list.get(j).get("minutes").toString())-
                            Double.parseDouble(distance_ref_list.get(j).get("minutes").toString())-40);
                    four_points_route.put("wait_time",40);
                    four_points_route_list.add(four_points_route);
                }
            }
        }
//        four_points_route_list = temp_list;
        try {
            listSort(four_points_route_list,"distance");
        } catch (Exception e) {
            e.printStackTrace();
        }
        for(int e=0;e<four_points_route_list.size();e++){
            if(e>=5){
                four_points_route_list.remove(e);
            }
        }
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
            Map<String,Object> two_points_route = new HashMap<String,Object>();
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
//            two_points_route_list.get(j).put("ok",Double.parseDouble(two_points_route_list.get(j).get("scenario_lim2").toString())
//                    *route_time_unit-((Double.parseDouble(two_points_route_list.get(j).get("time_id1").toString())-1)*route_time_unit)
//                    +Double.parseDouble(two_points_route_list.get(j).get("time1").toString()));
            two_points_route_list.get(j).put("ok",Double.parseDouble(two_points_route_list.get(j).get("scenario_lim2").toString())
                    *route_time_unit-((Double.parseDouble(two_points_route_list.get(j).get("time_id1").toString())-1)*route_time_unit));
        }

        for(int j=0;j<two_points_route_list.size();j++){
//            Double ok = Double.parseDouble(two_points_route_list.get(j).get("scenario_lim2").toString())
//                    *route_time_unit-((Double.parseDouble(two_points_route_list.get(j).get("time_id1").toString())-1)*route_time_unit)
//                    +Double.parseDouble(two_points_route_list.get(j).get("time1").toString());
            int ok = (int)(Double.parseDouble(two_points_route_list.get(j).get("scenario_lim2").toString())
                    *route_time_unit-((Double.parseDouble(two_points_route_list.get(j).get("time_id1").toString())-1)*route_time_unit));
            if(ok<0){
                two_points_route_list.remove(j);
            }
        }

        for(int j=0;j<two_points_route_list.size();j++){
            int which1 = (int)(Double.parseDouble(two_points_route_list.get(j).get("time_id1").toString())-((Double.parseDouble(two_points_route_list.get(j).get("scenario_lim1").toString()))));
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
        List<Map> route_three_point_list = tempService.findAll01(OpenScenariosId);
        for(Map m:route_three_point_list){
            m.put("ok",Double.parseDouble(m.get("scenario_lim2").toString())*
                    Double.parseDouble(m.get("*route_time_unit").toString())-
                    (Double.parseDouble(m.get("*time_id2").toString())-1)*route_time_unit
                    +Double.parseDouble(m.get("*time2").toString())
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
        List<Map> route_four_point_list = tempService.findAll02(OpenScenariosId);
        for(Map m:route_three_point_list){
            m.put("ok",Double.parseDouble(m.get("scenario_lim2").toString())*
                    Double.parseDouble(m.get("*route_time_unit").toString())-
                    (Double.parseDouble(m.get("*time_id2").toString())-1)*route_time_unit
                    +Double.parseDouble(m.get("*time2").toString())
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

        N = flow_lim.size();
        M = two_points_route_list.size();
        int I1 = two_points_route_list.size();
        int I2 = route_three_point_list.size();
        int I3 = route_four_point_list.size();
        I = I1+I2+I3;
        J = distance_ref_list.size()*full_time/route_time_unit;
//        geds TODO
//        temp_list = tempService.findAll03(OpenScenariosId);//size = 6065
//        connection_temp_list = tempService.findAll04(OpenScenariosId); //size = 1
        connection_temp_list = temp_list;

        logger.info("M11");
//        Matrix M1133333 = DenseMatrix.Factory.zeros(3, 3);
//        System.out.println(route_list.size()+":"+I);
        int tagrelay = 0;
        logger.info("tag:"+tagrelay++);

        logger.info("spark.Matrix");
        double[] v222 = new double[route_list.size()];
        double[] v223 = new double[route_list.size()];
        int[] a1 = new int[route_list.size()+1];
        int[] a2 = new int[route_list.size()];
        int[] a3 = new int[route_list.size()];
        int[] a4 = new int[route_list.size()];
        a1[0]=0;
        for(int e = 0; e<route_list.size() ;e++){
            a1[e]=e+1;
        }
        for(int e = 0; e<route_list.size() ;e++){
            a2[e]=1;
        }
        Vector v2 = new Vector();
        for(int mj=0;mj<I;mj++){
            v2.add(mj,mj);
        }
        for(int m=0;m<route_list.size();m++){
            v222[m]=1;
        }
//        Matrix M11 = new DenseMatrix(route_list.size(), I,v222);
//        Matrix M11 = new SparseMatrix(route_list.size(), I,a1,a2,v222);
//        Matrix M12 = new SparseMatrix(route_list.size(), I,a3,a4,v223);
//        Matrix M13 = new SparseMatrix(route_list.size(), I,a3,a4,v223);
//        Matrix M14 = new SparseMatrix(route_list.size(), I,a3,a4,v223);
//        Matrix M15 = new SparseMatrix(route_list.size(), I,a3,a4,v223);
        logger.info("spark.Matrix");
        Matrix M11 = SparseMatrix.Factory.zeros(route_list.size(), I);
        Matrix M12 = SparseMatrix.Factory.zeros(route_list.size(), I);
        Matrix M13 = SparseMatrix.Factory.zeros(route_list.size(), I);
        Matrix M14 = SparseMatrix.Factory.zeros(route_list.size(), I);
        Matrix M15 = SparseMatrix.Factory.zeros(route_list.size(), I);

        for(int m=0;m<route_list.size();m++){
                M11.setAsDouble(v222[m],m,m);
        }


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
//        double[] v23 = new double[J*I];
//        double[] v24 = new double[J*I];
//        for(int m=0;m<J;m++){
//            for(int mi=0;mi<I;mi++){
//                v24[m+mi]=v2.indexOf(mi);
//                v23[m+mi]=-truck_capacity;
//            }
//        }
        double[] v23 = new double[I];
        double[] v24 = new double[I];
        for(int mi=0;mi<I;mi++){
            v24[mi]=v2.indexOf(mi);
            v23[mi]=-truck_capacity;
        }
//        Matrix M21 = new DenseMatrix(J, I,v24);
//        Matrix M22 = new DenseMatrix(J, I,v23);
//        Matrix M23 = new DenseMatrix(J, I,v23);
//        Matrix M24 = new DenseMatrix(J, I,v23);
//        Matrix M25 = new DenseMatrix(J, I,v23);
        Matrix M21 = SparseMatrix.Factory.zeros(I, I);
        Matrix M22 = SparseMatrix.Factory.zeros(I, I);
        Matrix M23 = SparseMatrix.Factory.zeros(I, I);
        Matrix M24 = SparseMatrix.Factory.zeros(I, I);
        Matrix M25 = SparseMatrix.Factory.zeros(I, I);

        for(int m=0;m<route_list.size();m++){
            M21.setAsDouble(v24[m],m,m);
            M22.setAsDouble(v23[m],m,m);
            M23.setAsDouble(v23[m],m,m);
            M24.setAsDouble(v23[m],m,m);
            M25.setAsDouble(v23[m],m,m);
        }

        int f=0,g=0;
        Vector f1 = new Vector();
        for(int c2=0;c2<connection2_list.size();c2++){
            f1.add(Double.parseDouble(connection2_list.get(c2).get("dummy_in_id").toString())
                    +(Double.parseDouble(connection2_list.get(c2).get("time_id").toString())-1)*flow_lim.size());
        }
        Vector g1 = new Vector(1,J);

        connection2_list = tempService.findAll05(OpenScenariosId);
        connection2_list = tempService.findAll06(OpenScenariosId);
//## inflow:
        systemWebSocketHandler.sendMessageToUser( new TextMessage("#### inflow:"));
        systemWebSocketHandler.sendMessageToUser( new TextMessage("95%"));
        Vector f2 = new Vector();
        for(int c2=0;c2<connection2_list.size();c2++){
            double a = Double.parseDouble(connection_temp_list.get(c2).get("time_id").toString())-1;
            double b = Math.ceil((Double.parseDouble(connection_temp_list.get(c2).get("minutes").toString())-1)/route_time_unit);
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
            f2.add(Double.parseDouble(connection2_list.get(c2).get("dummy_out_id").toString())+min*flow_lim.size()+N*c);
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
//        double[] v33 = new double[J*J];
//        for(int m=0;m<J;m++){
//            for(int mi=0;mi<J;mi++){
//                v33[m+mi]=t.indexOf(mi);
//            }
//        }
        double[] v33 = new double[I];
        for(int mi=0;mi<I;mi++){
            v33[mi]=t.indexOf(mi);
        }
//        Matrix M31 = new DenseMatrix(J, J,v33);
//        Matrix M32 = new DenseMatrix(J, J,v33);
//        Matrix M33 = new DenseMatrix(J, J,v33);
//        Matrix M34 = new DenseMatrix(J, J,v33);
//        Matrix M35 = new DenseMatrix(J, J,v33);
//        Matrix M31 = SparseMatrix.Factory.zeros(J, J);
//        Matrix M32 = SparseMatrix.Factory.zeros(J, J);
//        Matrix M33 = SparseMatrix.Factory.zeros(J, J);
//        Matrix M34 = SparseMatrix.Factory.zeros(J, J);
//        Matrix M35 = SparseMatrix.Factory.zeros(J, J);

        Matrix M31 = SparseMatrix.Factory.zeros(I, I);
        Matrix M32 = SparseMatrix.Factory.zeros(I, I);
        Matrix M33 = SparseMatrix.Factory.zeros(I, I);
        Matrix M34 = SparseMatrix.Factory.zeros(I, I);
        Matrix M35 = SparseMatrix.Factory.zeros(I, I);
        for(int m=0;m<route_list.size();m++){
//            logger.info(String.valueOf(m));
//            logger.info(String.valueOf(v33[m]));
            M31.setAsDouble(v33[m],m,m);
            M32.setAsDouble(v33[m],m,m);
            M33.setAsDouble(v33[m],m,m);
            M34.setAsDouble(v33[m],m,m);
            M35.setAsDouble(v33[m],m,m);
        }
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
//        Vector v = new Vector(1,I);
//        Vector vj = new Vector(1,I);
        double[] v = new double[I];
        double[] vj = new double[I];
        for(int e=0;e<I;e++){
            v[e] = 1;
            vj[e] = 1;
        }
//        for(int m=0;m<J;m++){
//            for(int mi=0;mi<J;mi++){
//                M51.setAsInt(v.indexOf(mi),m,mi);
//                M52.setAsInt(vj.indexOf(mi),m,mi);
//                M53.setAsInt(Double.parseDouble(connection_temp_list.get(mi).get("distance").toString())>dist_limit_bike
//                        ?Double.parseDouble(connection_temp_list.get(mi).get("distance").toString())
//                        :Double.parseDouble(connection_temp_list.get(mi).get("cross_river").toString()),m,mi);
//                M54.setAsInt(v.indexOf(mi),m,mi);
//                M55.setAsInt(Double.parseDouble(connection_temp_list.get(mi).get("distance").toString())>dist_limit_dada
//                        ?Double.parseDouble(connection_temp_list.get(mi).get("distance").toString())
//                        :Double.parseDouble(connection_temp_list.get(mi).get("cross_river").toString()),m,mi);
//            }
//        }
//        double[] v51 = new double[J*J];
//        double[] v52 = new double[J*J];
//        double[] v53 = new double[J*J];
//        double[] v54 = new double[J*J];
//        double[] v55 = new double[I*I];

        double[] v51 = new double[I];
        double[] v52 = new double[I];
        double[] v53 = new double[I];
        double[] v54 = new double[I];
        double[] v55 = new double[I];
        for(int mi=0;mi<I;mi++){
//            v51[mi]=v.indexOf(mi);
//            v52[mi]=vj.indexOf(mi);
//            v53[mi]=vj.indexOf(mi);
//            v54[mi]=vj.indexOf(mi);
//            v55[mi]=vj.indexOf(mi);

            v51[mi]=v[mi];
            v52[mi]=vj[mi];
            v53[mi]=vj[mi];
            v54[mi]=vj[mi];
            v55[mi]=vj[mi];
//                v53[m+mi] = Double.parseDouble(connection_temp_list.get(mi).get("distance").toString())>dist_limit_bike
//                        ?Double.parseDouble(connection_temp_list.get(mi).get("distance").toString())
//                        :Double.parseDouble(connection_temp_list.get(mi).get("cross_river").toString());
//                v54[m+mi]= v.indexOf(mi);
//                v55[m+mi]=Double.parseDouble(connection_temp_list.get(mi).get("distance").toString())>dist_limit_dada
//                        ?Double.parseDouble(connection_temp_list.get(mi).get("distance").toString())
//                        :Double.parseDouble(connection_temp_list.get(mi).get("cross_river").toString());
        }
//        Matrix M51 = new DenseMatrix(J, J,v51);
//        Matrix M52 = new DenseMatrix(J, J,v52);
//        Matrix M53 = new DenseMatrix(J, J,v53);
//        Matrix M54 = new DenseMatrix(J, J,v54);
//        Matrix M55 = new DenseMatrix(J, J,v55);
//        Matrix M51 = SparseMatrix.Factory.zeros(J, J);
//        Matrix M52 = SparseMatrix.Factory.zeros(J, J);
//        Matrix M53 = SparseMatrix.Factory.zeros(J, J);
//        Matrix M54 = SparseMatrix.Factory.zeros(J, J);
//        Matrix M55 = SparseMatrix.Factory.zeros(J, J);
        Matrix M51 = SparseMatrix.Factory.zeros(I, I);
        Matrix M52 = SparseMatrix.Factory.zeros(I, I);
        Matrix M53 = SparseMatrix.Factory.zeros(I, I);
        Matrix M54 = SparseMatrix.Factory.zeros(I, I);
        Matrix M55 = SparseMatrix.Factory.zeros(I, I);
        for(int m=0;m<route_list.size();m++){
            M31.setAsDouble(v51[m],m,m);
            M32.setAsDouble(v52[m],m,m);
            M33.setAsDouble(v53[m],m,m);
            M34.setAsDouble(v54[m],m,m);
            M35.setAsDouble(v55[m],m,m);
        }

//########
        systemWebSocketHandler.sendMessageToUser( new TextMessage("//########"));
        systemWebSocketHandler.sendMessageToUser( new TextMessage("98%"));
        long consI = M11.getColumnCount()+M12.getColumnCount()+M13.getColumnCount()+M14.getColumnCount()+M15.getColumnCount();
        long consJ = M11.getRowCount()+M21.getRowCount()+M31.getRowCount()+M41.getRowCount()+M51.getRowCount();
//        Matrix cons = DenseMatrix.Factory.zeros(consI, consJ);
//        double[] vcons = new double[M11.getColumnCount()*M11.getRowCount()];
//        for(int m=0;m<M11.getColumnCount();m++){
//            for(int mi=0;mi<M11.getRowCount();mi++) {
////                cons.setAsInt(M11.index(m,mi),M11.getColumnCount()+m,M11.getRowCount()+mi);
//                vcons[m+mi] = M11.index(m,mi);
//            }
//        }
//        Matrix cons = new DenseMatrix(J, J,v51);
        Matrix cons1 = SparseMatrix.Factory.zeros(M11.getRowCount()*5, M11.getColumnCount());
        Matrix cons2 = SparseMatrix.Factory.zeros(M11.getRowCount()*5, M11.getColumnCount());
        Matrix cons3 = SparseMatrix.Factory.zeros(M11.getRowCount()*5, M11.getColumnCount());
        Matrix cons4 = SparseMatrix.Factory.zeros(M11.getRowCount()*5, M11.getColumnCount());
        Matrix cons5 = SparseMatrix.Factory.zeros(M11.getRowCount()*5, M11.getColumnCount());
        Matrix cons = SparseMatrix.Factory.zeros(M11.getRowCount()*5, M11.getColumnCount()*5);
        for(int m=0;m<route_list.size();m++){
            M31.setAsDouble(v51[m],m,m);
        }//        }
        Calculation.Ret ret = Calculation.Ret.LINK;
        cons1 = M11.appendVertically(ret,M12 ,M13 ,M14 ,M15);
        cons2 = M21.appendVertically(ret,M22 ,M23 ,M24 ,M25);
        cons3 = M31.appendVertically(ret,M32 ,M33 ,M34 ,M35);
        cons4 = M41.appendVertically(ret,M42 ,M43 ,M44 ,M45);
        cons5 = M51.appendVertically(ret,M52 ,M53 ,M54 ,M55);
        cons = cons1.appendHorizontally(ret,cons2,cons3,cons4,cons5);
        cons1 = M11.appendVertically(ret,M12 ,M13 ,M14);
        cons2 = M21.appendVertically(ret,M22 ,M23 ,M24);
        cons3 = M31.appendVertically(ret,M32 ,M33 ,M34);
        cons4 = M41.appendVertically(ret,M42 ,M43 ,M44);
        cons = cons1.appendHorizontally(ret,cons2,cons3,cons4);

//        cons = M11;
        logger.info("connection_temp_list");
        for(int ci=0;ci<(connection_temp_list.size()<OD_demand_list.size()?connection_temp_list.size():OD_demand_list.size());ci++){
            connection_temp_list.get(ci).put("kmh_didi",Double.parseDouble(OD_demand_list.get(ci).get("km").toString())<=10?Double.parseDouble(OD_demand_list.get(ci).get("km").toString())*speed1:0+
                    Double.parseDouble(OD_demand_list.get(ci).get("km").toString())>10&&
                    Double.parseDouble(OD_demand_list.get(ci).get("km").toString())<=30?
                    Double.parseDouble(OD_demand_list.get(ci).get("km").toString())*speed3:0+
                    Double.parseDouble(OD_demand_list.get(ci).get("km").toString())>=30?
                    Double.parseDouble(OD_demand_list.get(ci).get("km").toString())*speed4:0);
            connection_temp_list.get(ci).put("kmh_truck",Double.parseDouble(OD_demand_list.get(ci).get("km").toString())<=10?Double.parseDouble(OD_demand_list.get(ci).get("km").toString())*speed1:0+
                    Double.parseDouble(OD_demand_list.get(ci).get("km").toString())>10&&
                    Double.parseDouble(OD_demand_list.get(ci).get("km").toString())<=30?
                    Double.parseDouble(OD_demand_list.get(ci).get("km").toString())*speed3:0+
                    Double.parseDouble(OD_demand_list.get(ci).get("km").toString())>=30?
                    Double.parseDouble(OD_demand_list.get(ci).get("km").toString())*speed4:0);
            connection_temp_list.get(ci).put("kmh_bike",Double.parseDouble(OD_demand_list.get(ci).get("km").toString())<=10?Double.parseDouble(OD_demand_list.get(ci).get("km").toString())*speed1:0);
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
//                    logger.info(e-I+":"+connection_temp_list.get(e-I).get("cost_truck").toString());
            try{
                    obj[e]=Double.parseDouble(connection_temp_list.get(e-I).get("cost_truck").toString());
                }catch (NullPointerException ex){
                    continue;
                }
        }
        for(int e=I+connection_temp_list.size();e<I+connection_temp_list.size()*2;e++){
            try{
                obj[e]=Double.parseDouble(connection_temp_list.get(e-I-connection_temp_list.size()).get("cost_bike").toString());
            }catch (NullPointerException ex){
                continue;
            }
        }
        for(int e=I+connection_temp_list.size()*2;e<I+connection_temp_list.size()*3;e++){
            try{
                obj[e]=Double.parseDouble(connection_temp_list.get(e-I-connection_temp_list.size()*2).get("cost_didi").toString());
            }catch (NullPointerException ex){
                continue;
            }
        }
        for(int e=I+connection_temp_list.size()*3;e<I+connection_temp_list.size()*4;e++){
            try{
                obj[e]=Double.parseDouble(connection_temp_list.get(e-I-connection_temp_list.size()*3).get("cost_data").toString());
            }catch (NullPointerException ex){
                continue;
            }
        }

        //根据场景ID查询SiteDist
        systemWebSocketHandler.sendMessageToUser( new TextMessage("SiteDist"));
        systemWebSocketHandler.sendMessageToUser( new TextMessage("99%"));
        List<SiteInfo> siteInfoList = siteInfoService.findAllSiteInfo(OpenScenariosId);

        double[] outflow_lim = new double[siteInfoList.size()];
        for(int iii=0;iii<outflow_lim.length;iii++){
            outflow_lim[iii] = Double.parseDouble(siteInfoList.get(iii).getMaxOperateNum());
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
            sense[n] =">".toCharArray()[0];
        }
        for(int n=0;n<J;n++){
            sense[M+n] ="<=".toCharArray()[0];
        }
        int W = i-M-J;
        for(int n=0;n<W;n++){
            sense[M+J+n] ="<=".toCharArray()[0];
        }
        sense[i+1] ="=".toCharArray()[0];
int tag = 0;
        logger.info("trag:"+tag++);
        logger.info("model");
        logger.info("trag:"+tag++);

        logger.info("GRBEnv");
        logger.info("trag:"+tag++);
//        try{
//            GRBEnv    env   = new GRBEnv("mip1.log");
//            GRBModel m = new GRBModel(env);
//            double lb[] = new double[i];
//            for(int q=0;q<lb.length;q++){
//                lb[q]=0;
//            }
//            GRBVar[] vars = m.addVars(lb, null, null, types, null);
////            cons
//            for (int iw = 0; iw < cons.getRowCount(); iw++) {
//                GRBLinExpr expr = new GRBLinExpr();
//                for (int jw = 0; jw < cons.getColumnCount(); jw++)
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
//            double[]   solution = new double[cons.getColumnCount()];
//            logger.info("result:"+":"+GRB.Status.OPTIMAL);
//            if (m.get(GRB.IntAttr.Status) == GRB.Status.OPTIMAL) {
//                success = true;
//                for (int j = 0; j < cons.getColumnCount(); j++){
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
        logger.info("////////////////////////////////////////////////////////////////");
        logger.info("/////////////user time :"+(DateTimeUtils.currentTimeMillis()-starttime)+"////////////////////////////////////////////");

//        double A222[][] = new double[(int)cons.getRowCount()][(int)cons.getRowCount()];
//        for (int iw = 0; iw < cons.getRowCount(); iw++) {
//            for (int jw = 0; jw < cons.getColumnCount(); jw++){
//                A222[iw][jw] = cons.getAsDouble(iw,jw);
//            }
//        }

        try {
            logger.info("trag:"+tag++);

            logger.info("trag:"+tag++);
//            double c2[] = new double[] {1, 1, 0};
            double c2[] = new double[(int)cons.getColumnCount()];
//                        objn
            logger.info("trag:"+tag++);
            for (int jw = 0; jw < cons.getColumnCount(); jw++)
                c2[jw] = obj[jw];
//            double Q2[][] = new double[][] {{1, 1, 0}, {0, 1, 1}, {0, 0, 1}};
            double Q2[][] = null;
            logger.info("trag:"+tag++);
//            double Q2[][] = new double[cons.getRowCount()][cons.getColumnCount()];
//            logger.info("iw:"+cons.getRowCount()+";jw:"+cons.numCols());
//            for (int iw = 0; iw < cons.getRowCount(); iw++) {
//                for (int jw = 0; jw < cons.getColumnCount(); jw++)
//                    Q2[iw][jw] = cons.index(iw,jw);
//            }
            ///////
//            double A[][] = new double[][] {{1, 2, 3}, {1, 1, 0}};
            logger.info("cons.getRowCount():"+cons.getRowCount());
            logger.info("cons.getColumnCount():"+cons.getColumnCount());
            double A2[][] = new double[(int)cons.getRowCount()][(int)cons.getRowCount()];
//            double A2[][] = new double[1065][1065];
            logger.info("iw:"+cons.getRowCount()+";jw:"+cons.getColumnCount());
//            for (int iw = 0; iw < cons.getRowCount(); iw++) {
//                for (int jw = 0; jw < cons.getColumnCount(); jw++){
//                    A2[iw][jw] = cons.getAsDouble(iw,jw);
//                }
//            }
            for (int iw = 0; iw < cons.getRowCount(); iw++) {
                for (int jw = 0; jw < cons.getRowCount(); jw++){
                    A2[iw][jw] = cons.getAsDouble(iw,jw);
                    logger.info("trag:"+tag+++"-------A2[iw][jw]:"+A2[iw][jw]);
                }
            }
            logger.info("-------trag:"+tag++);
//            double A2[][] = null;
//            char sense2[] = new char[] {'>', '>'};
            logger.info("sense:"+sense.length);
            char sense2[] = new char[(int)cons.getRowCount()];
            for(int is=0;is<cons.getRowCount();is++){
                sense2[is] = sense[is];
            }
            logger.info("trag:"+tag++);
//            double rhs2[] = new double[] {4, 1};
            logger.info("rhs:"+rhs.length);
            logger.info("trag:"+tag++);
            double rhs2[] = new double[(int)cons.getRowCount()];
            for(int is=0;is<cons.getRowCount();is++){
                rhs2[is] = rhs[is];
            }
            logger.info("trag:"+tag++);
//            double lb2[] = new double[] {0, 0, 0};
            double lb2[] = new double[(int)cons.getRowCount()];
            for(int q=0;q<lb2.length;q++){
                lb2[q]=0;
            }
            logger.info("trag:"+tag++);
//            double lb2[] = null;
//            double ub2[] = new double[cons.getRowCount()];
//            for(int q=0;q<ub2.length;q++){
//                ub2[q]=0;
//            }
            double ub2[] = null;
            boolean success;
            double sol2[] = new double[(int)cons.getColumnCount()];
            logger.info("sol2:"+sol2.length);

            logger.info("trag:"+tag++);
            Dense dense = new Dense();
            GRBEnv env2 = new GRBEnv();
            success = dense.dense_optimize(env2, (int)cons.getRowCount(), (int)cons.getColumnCount(), c2, Q2, A2, sense2, rhs2,
                    lb2, ub2, types, sol2);

            logger.info("trag:"+tag++);
            if (success) {
                logger.info("success:");
                System.out.println("x: " + sol2[0] + ", y: " + sol2[1] + ", z: " + sol2[2]);
                double[] solution = sol2;
                logger.info("trag:"+tag++);
                makeResults(solution);
//                double volume_to_ship = 0;
//                for(int e=0;e<OD_demand_list.size();e++){
//                    volume_to_ship += Double.parseDouble(OD_demand_list.get(e).get("volume").toString());
//                }
                logger.info("trag:"+tag++);
                List<Map> route_opt = route_list;
                for(int e=0;e<I;e++){
                    route_opt.get(e).put("route_open",solution[e]);
                }
                for(int e=0;e<I1;e++){
                    route_opt.get(e).put("route_type",1);
                }
                for(int e=0;e<I2;e++){
                    route_opt.get(e).put("route_type",2);
                }
                for(int e=0;e<I3;e++){
                    route_opt.get(e).put("route_type",3);
                }
                logger.info("trag:"+tag++);
                route_opt = tempService.findAll07(OpenScenariosId);
                List<Map> connection_volume = tempService.findAll08(OpenScenariosId);
                List<Map> connection_opt = connection_temp_list;
                connection_opt.addAll(connection_volume);
                for(int e=0;e<J-1;e++){
                    if(solution[1+I+e]>0)
                    connection_opt.get(e).put("truck",solution[1+I+e]);
                }
                logger.info("trag:"+tag++);
                for(int e=0;e<J-1;e++){
                    if(solution[1+I+J+e]>0)
                    connection_opt.get(e).put("bike",solution[1+I+J+e]);
                }
                for(int e=0;e<J-1;e++){
                    if(solution[1+I+J*2+e]>0)
                    connection_opt.get(e).put("didi",solution[1+I+J*2+e]);
                }
                for(int e=0;e<J-1;e++){
                    if(solution[1+I+J*3+e]>0)
                    connection_opt.get(e).put("dada",solution[1+I+J*3+e]);
                }
//                summary(connection_opt)
                List<Map> active_connection = connection_opt;

                logger.info("trag:"+tag++);
            }else{
                logger.info("fail:");
            }

            // Dispose of environment
            env2.dispose();
            logger.info("/////////////total user time :"+(DateTimeUtils.currentTimeMillis()-starttime)+"////////////////////////////////////////////");

        } catch (GRBException e) {
            System.out.println("Error code: " + e.getErrorCode() + ". " +
                    e.getMessage());
            e.printStackTrace();
        }
    }
    ////////////
    protected void makeResults(double[] solution){

    }
    public void listSort(List<Map> resultList,String sequence) throws Exception{
        // resultList是需要排序的list，其内放的是Map
        // 返回的结果集
        Collections.sort(resultList,new Comparator<Map>() {

            public int compare(Map o1,Map o2) {
                //o1，o2是list中的Map，可以在其内取得值，按其排序，此例为升序，s1和s2是排序字段值
                double d1 = Double.valueOf(o1.get(sequence).toString());
                double d2 = Double.valueOf(o2.get(sequence).toString());
                Integer s1 = (int) d1;
                Integer s2 = (int) d2;
                return s1.compareTo(s2);
//                if(s1<s2) {
//                    return -1;
//                }else {
//                    return 1;
//                }
            }
        });

    }
}
