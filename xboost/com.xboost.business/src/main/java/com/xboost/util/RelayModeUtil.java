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
import java.sql.Statement;
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

    //global variables
    List<Map> flow_lim;
    List<SiteInfo> didi_flow_lim;
    List<Map> OD_demand_list = new ArrayList<Map>();
    List<Map> distance_ref_list = new ArrayList<Map>();
    List<Map> jisan_candidates = new ArrayList<Map>();
    //double[] outflow_lim;
    //double[] didi_outflow_lim;


    List<Map> two_points_route_list = new ArrayList<Map>();
    List<Map> three_points_route_list = new ArrayList<Map>();
    List<Map> four_points_route_list = new ArrayList<Map>();
    List<Map> route_four_point_list = new ArrayList<Map>();

    List<Map> route_two_point_list = new ArrayList<Map>();
    List<Map> route_three_point_list = new ArrayList<Map>();
    int full_time = 210;
    int route_time_unit = 10;
    int[] timebucket_num;

    List<Map> route_list = new ArrayList<>();
    List<Map> connection_list = new ArrayList<>();
    List<Map> connection_opt_list = new ArrayList<>();

    Matrix M11;
    Matrix M12;
    Matrix M13;
    Matrix M14;
    Matrix M15;

    Matrix M21;
    Matrix M22;
    Matrix M23;
    Matrix M24;
    Matrix M25;

    Matrix M31;
    Matrix M32;
    Matrix M33;
    Matrix M34;
    Matrix M35;

    Matrix M41;
    Matrix M42;
    Matrix M43;
    Matrix M44;
    Matrix M45;

    Matrix M51;
    Matrix M52;
    Matrix M53;
    Matrix M54;
    Matrix M55;

    double truck_capacity = 400;
    double dist_limit_bike = 5;
    double dist_limit_dada = 10;

    double speed1 = 15;
    double speed2 = 12;
    double speed3 = 20;
    double speed4 = 30;


    GRBEnv env;
    GRBModel model;

    List<Integer> outflow_lim;

    double gurobyCost;
    double[] gurobyResult;
    //init data
    private void InitData(){
        List<SiteInfo> SiteInfoAll = siteInfoService.findAllSiteInfo(OpenScenariosId);
        flow_lim = tempService.findAllFlowLim(OpenScenariosId);
        for(int e=0;e<flow_lim.size();e++){
            String siteCode1 = flow_lim.get(e).get("网点ID").toString();
            for(int s=0;s<SiteInfoAll.size();s++){
                int idT = SiteInfoAll.get(s).getId();
                String siteCode2 = SiteInfoAll.get(s).getSiteCode();
                if(siteCode1.equals(siteCode2)){
//                    flow_lim.get(e).put("集散点ID",s+1);
                    flow_lim.get(e).put("集散点ID",idT);
                }
            }
        }
        outflow_lim = new ArrayList<Integer>();
        for(int n=2;n<=22;n++){
            for(int e=0;e<flow_lim.size();e++){
                outflow_lim.add(Integer.parseInt(flow_lim.get(e).get("c"+n).toString()));
            }
        }
        //didi_flow_lim = flow_lim;
        //didi_outflow_lim = outflow_lim;

/////////////// begin : jisan_candidates    SiteInfoAll

        int siz = 1;
        for(SiteInfo siteInfo:SiteInfoAll){
            if(siz<79){
                Map siteMap = new HashMap();
                siteMap.put("类型",siteInfo.getSiteType());
                siteMap.put("网点",siteInfo.getSiteCode());
                siteMap.put("行政区","");
                siteMap.put("经度",siteInfo.getSiteLongitude());
                siteMap.put("纬度",siteInfo.getSiteLatitude());
                siteMap.put("线环","");
                siteMap.put("网点名称",siteInfo.getSiteName());
                siteMap.put("四轮车流入上限",siteInfo.getNoOfTruckLimitation());
                siteMap.put("两轮车流出上限",siteInfo.getNoOfBaiduLimitation());
                siteMap.put("浦西","0");
                siteMap.put("depot_id",siz);
                jisan_candidates.add(siteMap);
            }
            siz++;

        }

        /////////////// end : jisan_candidates
        ///
        /////////////// begin : od_demand_list
        Map dbmap = new HashMap<String, Object>();
        dbmap.put("scenariosId", OpenScenariosId);

        List<DemandInfo> OD_demand_list_temp = new ArrayList<DemandInfo>();
        OD_demand_list_temp = demandInfoService.findByScenairoIdParam(dbmap);

        List<SiteDist> siteDistList = siteDistService.findSiteDistByScenariosId(dbmap);

        int idemand = 0;
        int[] scenario_lim1 = {1, 1, 1, 1, 1};
        int[] scenario_lim2 = {9, 12, 15, 18, 21};
        for (DemandInfo dinfo:OD_demand_list_temp)
        {
            Map<String, Object> OD_demand = new HashMap<String, Object>();
            OD_demand.put("scenariosId", OpenScenariosId);
            OD_demand.put("OD_id", idemand);
            OD_demand.put("volume", Double.parseDouble(dinfo.getVotes()) / 0.8511);

            for (int e = 0; e < SiteInfoAll.size(); e++) {
                int idT = SiteInfoAll.get(e).getId();
                if (SiteInfoAll.get(e).getSiteCode().toString().equals(dinfo.getSiteCodeCollect().toString())) {
//                    OD_demand.put("inbound_id", e + 1);
                    OD_demand.put("inbound_id", idT);
                    continue;
                }
                if (SiteInfoAll.get(e).getSiteCode().toString().equals(dinfo.getSiteCodeDelivery().toString())) {
//                    OD_demand.put("outbound_id", e + 1);
                    OD_demand.put("outbound_id", idT);
                    continue;
                }
            }

            for (int e = 0; e < siteDistList.size(); e++) {
                if (siteDistList.get(e).getSiteCollect().toString().equals(dinfo.getSiteCodeCollect().toString()) &&
                        siteDistList.get(e).getSiteDelivery().toString().equals(dinfo.getSiteCodeDelivery().toString())) {
                    OD_demand.put("km", siteDistList.get(e).getCarDistance());
                    int sce = 0;
                    double dis = siteDistList.get(e).getCarDistance();
                    if (dis < 10) {
                        sce = 1;
                    } else if (dis >= 10 && dis < 15) {
                        sce = 2;
                    } else if (dis >= 15 && dis < 20) {
                        sce = 3;
                    } else if (dis >= 20 && dis < 30) {
                        sce = 4;
                    } else if (dis >= 30) {
                        sce = 5;
                    }
                    OD_demand.put("scenario", sce);
                    OD_demand.put("scenario_lim1", scenario_lim1[sce - 1]);
                    OD_demand.put("scenario_lim2", scenario_lim2[sce - 1]);
                    if (dis <= 10) {
                        OD_demand.put("kmh", speed2);
                    } else if (dis > 10 && dis <= 30) {
                        OD_demand.put("kmh", speed3);
                    } else if (dis > 30) {
                        OD_demand.put("kmh", speed4);
                    }
                    OD_demand.put("minutes", (Double.parseDouble(OD_demand.get("km").toString()) / Double.parseDouble(OD_demand.get("kmh").toString())) * 60);
                }
            }
            if (!(Double.parseDouble(OD_demand.get("km").toString()) == 0.0) && !OD_demand.get("inbound_id").equals(OD_demand.get("outbound_id")))
            {
                OD_demand_list.add(OD_demand);
                idemand++;
            }


        }

        //////////////////end : od_demand_list
        for (int j = 0; j < OD_demand_list.size(); j++) {
            Map<String, Object> distance_ref = new HashMap<String, Object>();
            distance_ref.put("scenariosId", OpenScenariosId);
            distance_ref.put("inbound_id", OD_demand_list.get(j).get("inbound_id"));
            distance_ref.put("outbound_id", OD_demand_list.get(j).get("outbound_id"));
            distance_ref.put("km", OD_demand_list.get(j).get("km"));
            distance_ref.put("minutes", OD_demand_list.get(j).get("minutes"));
            distance_ref.put("cross_river", 0);
            distance_ref.put("OD_id", OD_demand_list.get(j).get("OD_id"));
            distance_ref_list.add(distance_ref);
        }

    }

    private void getTwoPointsRoute(){
        for (int iDemand=0;iDemand<OD_demand_list.size();iDemand++)
        {
            Map OD_demand = OD_demand_list.get(iDemand);
            for (int iDis=0;iDis<distance_ref_list.size();iDis++)
            {

                Map distance_ref = distance_ref_list.get(iDis);
                if (OD_demand.get("inbound_id").toString().equals(distance_ref.get("inbound_id").toString()) &&
                        OD_demand.get("outbound_id").toString().equals(distance_ref.get("outbound_id").toString()))
                {
                    Map<String,Object> newTPR = new HashMap<>();
                    newTPR.put("m_od_id",OD_demand.get("OD_id"));
                    newTPR.put("i_route_id",OD_demand.get("inbound_id").toString()+"-"+OD_demand.get("outbound_id").toString());
                    newTPR.put("distance",distance_ref.get("km"));
                    newTPR.put("time",distance_ref.get("minutes"));
                    newTPR.put("demand",OD_demand.get("volume"));
                    newTPR.put("point1",OD_demand.get("inbound_id"));
                    newTPR.put("point2","0");
                    newTPR.put("point3","0");
                    newTPR.put("point4",OD_demand.get("outbound_id"));
                    newTPR.put("connection1",OD_demand.get("inbound_id").toString()+"-"+OD_demand.get("outbound_id").toString());
                    newTPR.put("connection2","");
                    newTPR.put("connection3","");
                    newTPR.put("time1",distance_ref.get("minutes"));
                    newTPR.put("time2","0");
                    newTPR.put("time3","0");
                    newTPR.put("dist1",distance_ref.get("km"));
                    newTPR.put("dist2","0");
                    newTPR.put("dist2","0");
                    newTPR.put("timecost","-"+distance_ref.get("minutes").toString());
                    newTPR.put("scenario",OD_demand.get("scenario"));
                    newTPR.put("scenario_lim1",OD_demand.get("scenario_lim1"));
                    newTPR.put("scenario_lim2",OD_demand.get("scenario_lim2"));
                    newTPR.put("wait_time","0");
                    two_points_route_list.add(newTPR);
                }
            }

        }
    }

    private void getThreePointsRoute(){
        List<Map> temp_list = new ArrayList<Map>();
        //merge temp_list
        for (int j = 0; j < two_points_route_list.size(); j++) {
            boolean exist = false;
            for (int j2 = 0; j2 < distance_ref_list.size(); j2++) {

                boolean b = two_points_route_list.get(j).get("point1").toString().equals(distance_ref_list.get(j2).get("inbound_id").toString());
                if (b) {
                    exist = true;
                    Map<String, Object> temp = new HashMap<String, Object>(two_points_route_list.get(j));
                    temp.put("inbound_id", distance_ref_list.get(j2).get("inbound_id"));
                    temp.put("outbound_id", distance_ref_list.get(j2).get("outbound_id"));
                    temp.put("km", distance_ref_list.get(j2).get("km"));
                    temp.put("minutes", distance_ref_list.get(j2).get("minutes"));
                    temp.put("OD_id", distance_ref_list.get(j2).get("OD_id"));
                    temp.put("cross_river", distance_ref_list.get(j2).get("cross_river"));

                    temp_list.add(temp);
                }
            }
            if (!exist) {
                Map<String, Object> temp = new HashMap<String, Object>(two_points_route_list.get(j));
                temp.put("inbound_id", "");
                temp.put("outbound_id", "");
                temp.put("km", "");
                temp.put("minutes", "");
                temp.put("OD_id", "");
                temp.put("cross_river", "");
                temp_list.add(temp);
            }
        }


        for (int iDemand=0;iDemand<temp_list.size();iDemand++) {
            Map OD_demand = temp_list.get(iDemand);
            for (int iDis = 0; iDis < distance_ref_list.size(); iDis++) {

                Map distance_ref = distance_ref_list.get(iDis);

                if (OD_demand.get("outbound_id").toString().equals(distance_ref.get("inbound_id").toString()) &&
                        OD_demand.get("point4").toString().equals(distance_ref.get("outbound_id").toString()) ) {
                    double min1 = Double.parseDouble(OD_demand.get("minutes").toString());
                    double min2 = Double.parseDouble(distance_ref.get("minutes").toString());
                    double time1 = Double.parseDouble(OD_demand.get("time").toString());
                    if ( (!OD_demand.get("point1").toString().equals(OD_demand.get("outbound_id").toString())) &&
                            (!OD_demand.get("point4").toString().equals(OD_demand.get("outbound_id").toString())) &&
                            min1+min2<=190 && min1+min2<=2*time1)
                    {
                        Map<String,Object> newTPR = new HashMap<>();
                        newTPR.put("m_od_id",OD_demand.get("m_od_id"));
                        newTPR.put("scenario",OD_demand.get("scenario"));
                        newTPR.put("scenario_lim1",OD_demand.get("scenario_lim1"));
                        newTPR.put("scenario_lim2",OD_demand.get("scenario_lim2"));
                        newTPR.put("i_route_id",OD_demand.get("point1").toString()+"-"+OD_demand.get("outbound_id").toString()
                                +"-"+OD_demand.get("point4").toString());

                        double km1 = Double.parseDouble(OD_demand.get("km").toString());
                        double km2 = Double.parseDouble(distance_ref.get("km").toString());

                        newTPR.put("distance",km1+km2);

                        newTPR.put("time",min1+min2);

                        newTPR.put("demand",OD_demand.get("demand"));

                        newTPR.put("point1",OD_demand.get("point1"));
                        newTPR.put("point2",OD_demand.get("outbound_id"));
                        newTPR.put("point3","0");
                        newTPR.put("point4",OD_demand.get("point4"));

                        newTPR.put("connection1",newTPR.get("point1").toString()+"-"+newTPR.get("point2").toString());
                        newTPR.put("connection2",newTPR.get("point2").toString()+"-"+newTPR.get("point4").toString());
                        newTPR.put("connection3","");

                        newTPR.put("time1",OD_demand.get("minutes"));
                        newTPR.put("time2",(int)Double.parseDouble(distance_ref.get("minutes").toString()));
                        newTPR.put("time3",0);

                        newTPR.put("dist1",OD_demand.get("km"));
                        newTPR.put("dist2",(int)Double.parseDouble(distance_ref.get("km").toString()));
                        newTPR.put("dist3",0);

                        newTPR.put("timecost",-min1-min2-20);

                        newTPR.put("wait_time","20");

                        three_points_route_list.add(newTPR);

                    }

                }
            }
        }
        three_points_route_list = getTopFiveByParamter(three_points_route_list,"distance");

    }

    private void getFourPointsRoute() {
        List<Map> temp_list = new ArrayList<Map>();
        //merge temp_list
        for (int j = 0; j < three_points_route_list.size(); j++) {
            boolean exist = false;
            for (int j2 = 0; j2 < distance_ref_list.size(); j2++) {

                boolean b = three_points_route_list.get(j).get("point2").toString().equals(distance_ref_list.get(j2).get("inbound_id").toString());
                if (b) {
                    exist = true;
                    Map<String, Object> temp = new HashMap<String, Object>(three_points_route_list.get(j));
                    temp.put("inbound_id",distance_ref_list.get(j2).get("inbound_id"));
                    temp.put("outbound_id",distance_ref_list.get(j2).get("outbound_id"));
                    temp.put("km",distance_ref_list.get(j2).get("km"));
                    temp.put("minutes",distance_ref_list.get(j2).get("minutes"));
                    temp.put("OD_id",distance_ref_list.get(j2).get("OD_id"));
                    temp.put("cross_river",distance_ref_list.get(j2).get("cross_river"));

                    temp_list.add(temp);
                }
            }
            if (!exist) {
                Map<String, Object> temp = new HashMap<String, Object>(three_points_route_list.get(j));
                temp.put("inbound_id","");
                temp.put("outbound_id","");
                temp.put("km","");
                temp.put("minutes","");
                temp.put("OD_id","");
                temp.put("cross_river","");
                temp_list.add(temp);
            }
        }


        for (int iDemand=0;iDemand<temp_list.size();iDemand++) {
            Map OD_demand = temp_list.get(iDemand);
            for (int iDis = 0; iDis < distance_ref_list.size(); iDis++) {

                Map distance_ref = distance_ref_list.get(iDis);

                if (OD_demand.get("outbound_id").toString().equals(distance_ref.get("inbound_id").toString()) &&
                        OD_demand.get("point4").toString().equals(distance_ref.get("outbound_id").toString()) ) {
                    double min1 = Double.parseDouble(OD_demand.get("minutes").toString());
                    double min2 = Double.parseDouble(distance_ref.get("minutes").toString());
                    double time1 = Double.parseDouble(OD_demand.get("time1").toString());
                    double time = Double.parseDouble(OD_demand.get("time").toString());
                    if ( (!OD_demand.get("point1").toString().equals(OD_demand.get("outbound_id").toString())) &&
                            (!OD_demand.get("point2").toString().equals(OD_demand.get("outbound_id").toString()))&&
                            (!OD_demand.get("point4").toString().equals(OD_demand.get("outbound_id").toString())) &&
                            time1+min1+min2<=170 && time1+min1+min2<=2*time)
                    {
                        Map<String,Object> newTPR = new HashMap<>();
                        newTPR.put("m_od_id",OD_demand.get("m_od_id"));
                        newTPR.put("scenario",OD_demand.get("scenario"));
                        newTPR.put("scenario_lim1",OD_demand.get("scenario_lim1"));
                        newTPR.put("scenario_lim2",OD_demand.get("scenario_lim2"));
                        newTPR.put("i_route_id",OD_demand.get("point1").toString()+"-"+OD_demand.get("point2").toString()+"-"+
                                OD_demand.get("outbound_id").toString()
                                +"-"+OD_demand.get("point4").toString());

                        double km1 = Double.parseDouble(OD_demand.get("km").toString());
                        double km2 = Double.parseDouble(distance_ref.get("km").toString());
                        double dist1 = Double.parseDouble(OD_demand.get("dist1").toString());

                        newTPR.put("distance",km1+km2+dist1);

                        newTPR.put("time",min1+min2+time1);

                        newTPR.put("demand",OD_demand.get("demand"));

                        newTPR.put("point1",OD_demand.get("point1"));
                        newTPR.put("point2",OD_demand.get("point2"));
                        newTPR.put("point3",OD_demand.get("outbound_id"));
                        newTPR.put("point4",OD_demand.get("point4"));

                        newTPR.put("connection1",newTPR.get("point1").toString()+"-"+newTPR.get("point2").toString());
                        newTPR.put("connection2",newTPR.get("point2").toString()+"-"+newTPR.get("point3").toString());
                        newTPR.put("connection3",newTPR.get("point3").toString()+"-"+newTPR.get("point4").toString());

                        newTPR.put("time1",OD_demand.get("time1"));
                        newTPR.put("time2",(int)Double.parseDouble(OD_demand.get("minutes").toString()));
                        newTPR.put("time3",(int)Double.parseDouble(distance_ref.get("minutes").toString()));

                        newTPR.put("dist1",OD_demand.get("dist1"));
                        newTPR.put("dist2",(int)Double.parseDouble(OD_demand.get("km").toString()));
                        newTPR.put("dist3",(int)Double.parseDouble(distance_ref.get("km").toString()));

                        newTPR.put("timecost",-time1-min1-min2-40);

                        newTPR.put("wait_time","40");

                        four_points_route_list.add(newTPR);

                    }

                }
            }
        }
        four_points_route_list = getTopFiveByParamter(four_points_route_list,"distance");
    }

    private void getRouteTwoPoint()
    {
        int[] site1 = new int[full_time/route_time_unit];
        timebucket_num = new int[full_time/route_time_unit];

        for(int j=0;j<full_time/route_time_unit;j++){
            site1[j] = j+1;
            timebucket_num[j] = j+1;
        }

        for(int isite = 0;isite<site1.length;isite++)
        {
            for(int itpr=0;itpr<two_points_route_list.size();itpr++)
            {
                Map newRTP = new HashMap(two_points_route_list.get(itpr));
                newRTP.put("time_id1",site1[isite]);
                newRTP.put("timebucket_"+site1[isite],newRTP.get("connection1").toString()+"-t"+site1[isite]);
                for(int ibucket = 0;ibucket<timebucket_num.length;ibucket++){
                    if (timebucket_num[ibucket]!=site1[isite])
                    {
                        newRTP.put("timebucket_"+timebucket_num[ibucket],"");
                    }
                }
                route_two_point_list.add(newRTP);
            }
        }
//        route_two_point$ok<-route_two_point$scenario_lim2*route_time_unit-((route_two_point$time_id1-1)*route_time_unit+route_two_point$time1)
//        route_two_point<-route_two_point[route_two_point$ok>=0,]
        for(int e=0;e<route_two_point_list.size();e++){
            Map crtp = route_two_point_list.get(e);
            double okTemp = Double.parseDouble(crtp.get("scenario_lim2").toString())*route_time_unit
                    -((Double.parseDouble(crtp.get("time_id1").toString())-1)*route_time_unit
                    +Double.parseDouble(crtp.get("time1").toString()));

            if (okTemp<0)
            {
                route_two_point_list.remove(e);
                e--;
            }
            else
            {
                double val = Double.parseDouble(crtp.get("time_id1").toString()) - Double.parseDouble(crtp.get("scenario_lim1").toString());
                if (val<0)
                {
                    route_two_point_list.remove(e);
                    e--;
                }
                else
                {
                    crtp.remove("time_id1");
                }
            }

        }
    }

    private void getRouteThreePoint()
    {

        int timeslots = full_time/route_time_unit;
        int [][] site2 = new int[2][timeslots*(timeslots-1)/2];

        int timeidx = 0;
        for (int itime1=0;itime1<timeslots;itime1++)
        {
            for(int itime2 = itime1+1;itime2<timeslots;itime2++)
            {
                site2[0][timeidx] = itime1+1;
                site2[1][timeidx] = itime2+1;
                timeidx++;
            }
        }
        //
        for (int isite=0;isite<site2[0].length;isite++)
        {
            for (int itpr=0;itpr<three_points_route_list.size();itpr++)
            {
                Map newRTP = new HashMap(three_points_route_list.get(itpr));
                newRTP.put("time_id1",site2[0][isite]);
                newRTP.put("time_id2",site2[1][isite]);
                for (int itime=0;itime<timeslots;itime++)
                {
                    newRTP.put("timebucket_"+(itime+1),"");
                }
                newRTP.put("timebucket_"+site2[0][isite],newRTP.get("connection1").toString()+"-t"+site2[0][isite]);
                newRTP.put("timebucket_"+site2[1][isite],newRTP.get("connection2").toString()+"-t"+site2[1][isite]);
                route_three_point_list.add(newRTP);
            }
        }
        for (int irtp=0;irtp<route_three_point_list.size();irtp++)
        {
            Map rtp = route_three_point_list.get(irtp);
            double value = Double.parseDouble(rtp.get("scenario_lim2").toString())*route_time_unit-
                    ((Double.parseDouble(rtp.get("time_id2").toString())-1)*route_time_unit+Double.parseDouble(rtp.get("time2").toString()));

            if (value<0)
            {

                route_three_point_list.remove(irtp);
                irtp--;
            }
            else
            {
                value =Double.parseDouble(rtp.get("time_id1").toString())-Double.parseDouble(rtp.get("scenario_lim1").toString());
                if (value<0)
                {

                    route_three_point_list.remove(irtp);
                    irtp--;
                }
                else
                {
                    value = (Double.parseDouble(rtp.get("time_id2").toString()) - Double.parseDouble(rtp.get("time_id1").toString()))*
                            route_time_unit - (Double.parseDouble(rtp.get("time1").toString())+20);
                    if(value<0)
                    {
                        route_three_point_list.remove(irtp);
                        irtp--;
                    }
                    else
                    {
                        rtp.remove("time_id1");
                        rtp.remove("time_id2");
                    }
                }
            }
        }
    }

    private void getRouteFourPoint()
    {
        int timeslots = full_time/route_time_unit;
        int [][] site3 = new int[3][timeslots*(timeslots-1)*(timeslots-2)/(2*3)];
        int timeidx = 0;
        for (int itime1=0;itime1<timeslots;itime1++)
        {
            for(int itime2 = itime1+1;itime2<timeslots;itime2++)
            {
                for(int itime3=itime2+1;itime3<timeslots;itime3++)
                {
                    site3[0][timeidx] = itime1+1;
                    site3[1][timeidx] = itime2+1;
                    site3[2][timeidx] = itime3+1;
                    timeidx++;
                }
            }
        }
        int count1=0,count2=0,count3=0,count4=0;
        for (int isite=0;isite<site3[0].length;isite++)
        {
            for (int ifpr=0;ifpr<four_points_route_list.size();ifpr++)
            {
                Map newRFP = new HashMap(four_points_route_list.get(ifpr));

                newRFP.put("time_id1",site3[0][isite]);
                newRFP.put("time_id2",site3[1][isite]);
                newRFP.put("time_id3",site3[2][isite]);

                for (int itime=0;itime<timeslots;itime++)
                {
                    newRFP.put("timebucket_"+(itime+1),"");
                }
                newRFP.put("timebucket_"+site3[0][isite],newRFP.get("connection1").toString()+"-t"+site3[0][isite]);
                newRFP.put("timebucket_"+site3[1][isite],newRFP.get("connection2").toString()+"-t"+site3[1][isite]);
                newRFP.put("timebucket_"+site3[2][isite],newRFP.get("connection3").toString()+"-t"+site3[2][isite]);

                Map rfp = newRFP;
                double value = Double.parseDouble(rfp.get("scenario_lim2").toString())*route_time_unit-
                        ((Double.parseDouble(rfp.get("time_id3").toString())-1)*route_time_unit+Double.parseDouble(rfp.get("time3").toString()));
                if(value>=0)
                {
                    value = Double.parseDouble(rfp.get("time_id1").toString())-Double.parseDouble(rfp.get("scenario_lim1").toString());
                    count1++;
                    if(value>=0)
                    {
                        value = (Double.parseDouble(rfp.get("time_id2").toString())-Double.parseDouble(rfp.get("time_id1").toString()))*
                                route_time_unit-(Double.parseDouble(rfp.get("time1").toString())+20);
                        count2++;
                        if(value>=0)
                        {
                            value = (Double.parseDouble(rfp.get("time_id3").toString())-Double.parseDouble(rfp.get("time_id2").toString()))*
                                    route_time_unit-(Double.parseDouble(rfp.get("time2").toString())+20);
                            count3++;
                            if(value>=0)
                            {
                                count4++;
                                newRFP.remove("time_id1");
                                newRFP.remove("time_id2");
                                newRFP.remove("time_id3");
                                route_four_point_list.add(newRFP);
                            }
                        }

                    }
                }


            }
        }

    }

    private void getConneciton()
    {
        for (int itime=0;itime<timebucket_num.length;itime++)
        {
            for (int idis=0;idis<distance_ref_list.size();idis++)
            {
                HashMap conn = new HashMap();
                conn.put("time_id",timebucket_num[itime]);
                conn.put("inbound_id",distance_ref_list.get(idis).get("inbound_id"));
                conn.put("outbound_id",distance_ref_list.get(idis).get("outbound_id"));
                conn.put("distance",distance_ref_list.get(idis).get("km"));
                conn.put("cross_river",distance_ref_list.get(idis).get("cross_river"));
                conn.put("minutes",distance_ref_list.get(idis).get("minutes"));
                conn.put("connection",distance_ref_list.get(idis).get("inbound_id").toString()+"-"+
                        distance_ref_list.get(idis).get("outbound_id").toString());

                conn.put("timebucket",conn.get("connection").toString()+"-t"+
                        timebucket_num[itime]);
                conn.put("j_connection_id",connection_list.size());
                connection_list.add(conn);
            }
        }
    }
    private void getM1X()
    {
        int M = two_points_route_list.size();
        int J = distance_ref_list.size()*full_time/route_time_unit;


        M11 = SparseMatrix.Factory.zeros(M, route_list.size());
        M12 = SparseMatrix.Factory.zeros(M, J);
        M13 = SparseMatrix.Factory.zeros(M, J);
        M14 = SparseMatrix.Factory.zeros(M, J);
        M15 = SparseMatrix.Factory.zeros(M, J);

        for (int ir = 0;ir<route_list.size();ir++)
        {
            int ix = Integer.parseInt(route_list.get(ir).get("m_od_id").toString());
            int iy = ir;
            M11.setAsInt(1, ix, iy);
        }
    }

    private void getM2X()
    {
        List<Integer> p = new ArrayList<Integer>();
        List<Integer> q = new ArrayList<Integer>();
        List<Double> v = new ArrayList<Double>();

        for (int iroute=0;iroute<route_list.size();iroute++)
        {
            Map route = route_list.get(iroute);
            List<Integer> ind = new ArrayList<>();

            for (int icon=0;icon<connection_list.size();icon++)
            {
                String connection_timebucket = connection_list.get(icon).get("timebucket").toString();
                for (int it=0;it<timebucket_num.length;it++)
                {
                    if (connection_timebucket.equals(route.get("timebucket_"+timebucket_num[it])))
                    {
                        //findidx.add(icon);
                        ind.add(Integer.parseInt(connection_list.get(icon).get("j_connection_id").toString()));
                        break;
                    }
                }
            }

            p.addAll(ind);
            for (int iq=0;iq<ind.size();iq++)
            {
                q.add(iroute);
                v.add(Double.parseDouble(route.get("demand").toString()));
            }

        }

        int J = distance_ref_list.size()*full_time/route_time_unit;
        int I = route_list.size();
        M21 = SparseMatrix.Factory.zeros(J, I);

        for (int ip = 0;ip<p.size();ip++)
        {
            int ix = p.get(ip);
            int iy = q.get(ip);
            M21.setAsDouble(v.get(ip), ix, iy);
        }

        M22 = SparseMatrix.Factory.zeros(J, J);
        M23 = SparseMatrix.Factory.zeros(J, J);
        M24 = SparseMatrix.Factory.zeros(J, J);
        M25 = SparseMatrix.Factory.zeros(J, J);
        for (int ij=0;ij<J;ij++)
        {
            M22.setAsDouble(-truck_capacity,ij,ij);
            M23.setAsDouble(-truck_capacity,ij,ij);
            M24.setAsDouble(-truck_capacity,ij,ij);
            M25.setAsDouble(-truck_capacity,ij,ij);
        }

        logger.info("m2x");
    }

    private void getM3X()
    {
        List<Map> connection_list2 = new ArrayList<>();
        for (int i=0;i<connection_list.size();i++)
        {
            Map nCon = new HashMap<>(connection_list.get(i));
            for (int j=0;j<flow_lim.size();j++)
            {
                String in_id = nCon.get("inbound_id").toString();
                String out_id = nCon.get("outbound_id").toString();

                if (in_id.equals(flow_lim.get(j).get("集散点ID").toString()))
                {
                    nCon.put("dummy_in_id",j);
                }
                if (out_id.equals(flow_lim.get(j).get("集散点ID").toString()))
                {
                    nCon.put("dummy_out_id",j);
                }


            }
            connection_list2.add(nCon);
        }

        //
        List<Integer> f1 = new ArrayList<>();
        List<Integer> f2 = new ArrayList<>();
        List<Integer> f = new ArrayList<>();
        for (int i=0;i<connection_list2.size();i++)
        {
            int val = Integer.parseInt(connection_list2.get(i).get("dummy_in_id").toString()) +
                    (Integer.parseInt(connection_list.get(i).get("time_id").toString())-1)*flow_lim.size();
            f1.add(val);


            //(connection$time_id-1+ceiling(connection$minutes/route_time_unit))
            int vv1 = Integer.parseInt(connection_list.get(i).get("time_id").toString())-1+
                    (int)Math.ceil(Double.parseDouble(connection_list.get(i).get("minutes").toString())/route_time_unit);
            int vv2 = (timebucket_num[timebucket_num.length-1]-1);

            int val2 = Integer.parseInt(connection_list2.get(i).get("dummy_out_id").toString())+Math.min(vv1,vv2)*
                    flow_lim.size()+ flow_lim.size() * timebucket_num[timebucket_num.length-1];

            f2.add(val2);
        }
        f.addAll(f1);
        f.addAll(f2);
        int J = distance_ref_list.size()*full_time/route_time_unit;
        List<Integer> g = new ArrayList<>();
        for (int i=0;i<J;i++)
        {
            g.add(i);
        }
        for (int i=0;i<J;i++)
        {
            g.add(i);
        }

        int I = route_list.size();

        int dimx = flow_lim.size()*timebucket_num[timebucket_num.length-1]*2;
        M31 = SparseMatrix.Factory.zeros(dimx, I);
        M33 = SparseMatrix.Factory.zeros(dimx, J);
        M34 = SparseMatrix.Factory.zeros(dimx, J);
        M35 = SparseMatrix.Factory.zeros(dimx, J);

        M32 = SparseMatrix.Factory.zeros(dimx, J);
        for (int i=0;i<f.size();i++)
        {
            M32.setAsInt(1,f.get(i),g.get(i));
        }


        logger.info("m3x");
    }

    private void getM4X()
    {
        int dimx = flow_lim.size()*timebucket_num[timebucket_num.length-1]*2;
        int I = route_list.size();
        int J = distance_ref_list.size()*full_time/route_time_unit;

        M41 = SparseMatrix.Factory.zeros(dimx, I);
        M42 = SparseMatrix.Factory.zeros(dimx, J);
        M43 = SparseMatrix.Factory.zeros(dimx, J);
        M45 = SparseMatrix.Factory.zeros(dimx, J);

        M44 = M32.clone();

        logger.info("m4x");
    }

    private void getM5X()
    {
        int I = route_list.size();
        int J = distance_ref_list.size()*full_time/route_time_unit;
        M51 = SparseMatrix.Factory.zeros(1, I);
        M52 = SparseMatrix.Factory.zeros(1, J);
        M53 = SparseMatrix.Factory.zeros(1, J);
        for (int i=0;i<J;i++)
        {
            double val = 0;
            boolean b1 = Double.parseDouble(connection_list.get(i).get("distance").toString())>dist_limit_bike;
            boolean b2 = connection_list.get(i).get("cross_river").toString().equals("1");
            if (b1|b2){
                val = 1;
            }
            M53.setAsDouble(val, 0, i);
        }
        M54 = SparseMatrix.Factory.zeros(1, J);
        M55 = SparseMatrix.Factory.zeros(1, J);
        for (int i=0;i<J;i++)
        {
            double val = 0;
            boolean b1 = Double.parseDouble(connection_list.get(i).get("distance").toString())>dist_limit_dada;
            boolean b2 = connection_list.get(i).get("cross_river").toString().equals("1");
            if (b1|b2){
                val = 1;
            }
            M55.setAsDouble(val, 0, i);
        }

        logger.info("m5x");
    }
    private void InvokeGurobi()
    {
        int I =  route_list.size();
        int J =  distance_ref_list.size()*full_time/route_time_unit;

        try
        {
            env = new GRBEnv();
            model = new GRBModel(env);

            GRBVar[] x_var = model.addVars(I, GRB.BINARY);
            GRBVar[] i_var = model.addVars(J*4, GRB.INTEGER);

            //object
            GRBLinExpr exprObj = new GRBLinExpr();
            for (int iVar=0;iVar<J;iVar++)
            {
                double cost_truck=0.0,cost_bike=11.0,cost_didi=0.0,cost_dada=0.0;
                double distance =  Double.parseDouble(connection_list.get(iVar).get("distance").toString());
                cost_truck =distance*10;
                if (distance<2)
                {
                    cost_dada = 10;
                }
                else
                {
                    cost_dada = (distance-2)*2+10;
                }
                double didispeed = 0;

                if(distance<=10)
                {
                    didispeed = speed1;
                }
                else if(distance<=30)
                {
                    didispeed = speed3;
                }
                else
                {
                    didispeed = speed4;
                }
                cost_didi = distance/didispeed*60*0.4+distance*2.4;
                if (distance>12)
                {
                    cost_didi += distance-12;
                }
                if(cost_didi<13)
                {
                    cost_didi = 13;
                }


                exprObj.addTerm(cost_truck, i_var[iVar]);
                exprObj.addTerm(cost_bike, i_var[iVar+J]);
                exprObj.addTerm(cost_didi, i_var[iVar+J*2]);
                exprObj.addTerm(cost_dada, i_var[iVar+J*3]);
            }
            model.setObjective(exprObj, GRB.MINIMIZE);

            //M11
            //int nonzero1 = 0;
            for (int iRow=0;iRow<M11.getRowCount();iRow++)
            {
                GRBLinExpr expr = new GRBLinExpr();
                for (int iCol=0;iCol<M11.getColumnCount();iCol++)
                {
                    if(M11.getAsDouble(iRow,iCol)> 0.00001)
                    {
                        //logger.info("p");
                        //nonzero1++;
                        expr.addTerm(M11.getAsDouble(iRow,iCol), x_var[iCol]);
                    }

                }
                model.addConstr(expr,GRB.EQUAL,1.0,"m1_"+iRow);
            }

            //M22-M25
            //int nonzero2 = 0;
            for (int iRow=0;iRow<M21.getRowCount();iRow++)
            {
                GRBLinExpr expr = new GRBLinExpr();
                for (int iCol=0;iCol<M21.getColumnCount();iCol++) {
                    if (M21.getAsDouble(iRow, iCol) > 0.00001) {
                        //logger.info("p");
                        //nonzero2++;
                        expr.addTerm(M21.getAsDouble(iRow, iCol), x_var[iCol]);
                    }
                }
                for (int iCol=0;iCol<M22.getColumnCount();iCol++) {
                    if (M22.getAsDouble(iRow, iCol) <-0.00001) {
                        //nonzero2++;
                        expr.addTerm(M22.getAsDouble(iRow, iCol), i_var[iCol]);
                    }
                }
                for (int iCol=0;iCol<M23.getColumnCount();iCol++) {
                    if (M23.getAsDouble(iRow, iCol) <- 0.00001) {
                        //nonzero2++;
                        expr.addTerm(M23.getAsDouble(iRow, iCol), i_var[iCol + J]);
                    }
                }
                for (int iCol=0;iCol<M24.getColumnCount();iCol++) {
                    if (M24.getAsDouble(iRow, iCol) <- 0.00001) {
                        //nonzero2++;
                        expr.addTerm(M24.getAsDouble(iRow, iCol), i_var[iCol + J * 2]);
                    }
                }
                for (int iCol=0;iCol<M25.getColumnCount();iCol++)
                {
                    if(M25.getAsDouble(iRow,iCol)<-0.00001)
                    {
                        //nonzero2++;
                        expr.addTerm(M25.getAsDouble(iRow,iCol), i_var[iCol+J*3]);
                    }
                }

                model.addConstr(expr,GRB.LESS_EQUAL,0.0,"m2_"+iRow);
            }

            //M32
            //int nonzero3=0;
            for (int iRow=0;iRow<M32.getRowCount();iRow++)
            {
                GRBLinExpr expr = new GRBLinExpr();
                for (int iCol=0;iCol<M32.getColumnCount();iCol++)
                {
                    if(M32.getAsDouble(iRow,iCol)> 0.00001)
                    {
                        //logger.info("p");
                        //nonzero3++;
                        expr.addTerm(M32.getAsDouble(iRow,iCol), i_var[iCol]);
                    }
                }
                model.addConstr(expr,GRB.LESS_EQUAL,2,"m3_"+iRow); //todo : set 5 as outflow limit
            }
            //M44
            //int nonzero4=0;
            for (int iRow=0;iRow<M44.getRowCount();iRow++)
            {
                GRBLinExpr expr = new GRBLinExpr();
                for (int iCol=0;iCol<M44.getColumnCount();iCol++)
                {
                    if(M44.getAsDouble(iRow,iCol)> 0.00001)
                    {
                        //nonzero4++;
                        expr.addTerm(M44.getAsDouble(iRow,iCol), i_var[iCol+J*2]);
                    }
                }
                model.addConstr(expr,GRB.LESS_EQUAL,3,"m4_"+iRow); //todo : set 5 as outflow limit
            }

            //M53,M55
            //int nonzero5=0;
            for (int iRow=0;iRow<M53.getRowCount();iRow++)
            {
                GRBLinExpr expr = new GRBLinExpr();
                for (int iCol=0;iCol<M53.getColumnCount();iCol++)
                {
                    if(M53.getAsDouble(iRow,iCol)> 0.00001)
                    {
                        //nonzero5++;
                        expr.addTerm(M53.getAsDouble(iRow,iCol), i_var[iCol+J]);
                    }
                }
                for (int iCol=0;iCol<M55.getColumnCount();iCol++)
                {
                    if(M55.getAsDouble(iRow,iCol)> 0.00001)
                    {
                        //nonzero5++;
                        expr.addTerm(M55.getAsDouble(iRow,iCol), i_var[iCol+J*3]);
                    }
                }
                model.addConstr(expr,GRB.EQUAL,0.0,"m5_"+iRow); //
            }
            model.set("MIPgap","0.05");
            model.set("TimeLimit","3600");

            //invoke
            model.optimize();

            gurobyCost =  model.get(GRB.DoubleAttr.ObjVal);
            gurobyResult = new double[I+J*4];
            for (int i=0;i<I;i++)
            {
                gurobyResult[i] =x_var[i].get(GRB.DoubleAttr.X);
            }
            for (int i=0;i<J*4;i++)
            {
                gurobyResult[i+I] =i_var[i].get(GRB.DoubleAttr.X);

            }

        }
        catch (Exception e) {

        }
        //M11.
    }
    public void run() throws RuntimeException {




        systemWebSocketHandler.sendMessageToUser(new TextMessage("params:"));
        systemWebSocketHandler.sendMessageToUser(new TextMessage("1%"));

        InitData();

        systemWebSocketHandler.sendMessageToUser(new TextMessage("Get Route:"));
        systemWebSocketHandler.sendMessageToUser(new TextMessage("10%"));

        getTwoPointsRoute();
        getThreePointsRoute();
        getFourPointsRoute();

        getRouteTwoPoint();
        getRouteThreePoint();
        getRouteFourPoint();

        systemWebSocketHandler.sendMessageToUser(new TextMessage("Calculate Constraint matrix"));
        systemWebSocketHandler.sendMessageToUser(new TextMessage("20%"));


        route_list.addAll(route_two_point_list);
        route_list.addAll(route_three_point_list);
        route_list.addAll(route_four_point_list);

        getConneciton();

        getM1X();
        getM2X();
        getM3X();
        getM4X();
        getM5X();

        systemWebSocketHandler.sendMessageToUser(new TextMessage("Start Invoke Gurobi:"));
        systemWebSocketHandler.sendMessageToUser(new TextMessage("50%"));

        InvokeGurobi();

        systemWebSocketHandler.sendMessageToUser(new TextMessage("Gurobi Optim Finished:"));
        systemWebSocketHandler.sendMessageToUser(new TextMessage("Start Insert Result Data:"));
        systemWebSocketHandler.sendMessageToUser(new TextMessage("90%"));

        makeResults(gurobyResult,gurobyCost);

        systemWebSocketHandler.sendMessageToUser(new TextMessage("Finished."));
        systemWebSocketHandler.sendMessageToUser(new TextMessage("100%"));


    }
    ////////////
    protected void makeResults(double[] solution, double gurobyCost){
//                route_opt start
        int I = route_list.size();
        int J = distance_ref_list.size()*full_time/route_time_unit;
        int I1 = route_two_point_list.size();
        int I2 = route_three_point_list.size();
        int I3 = route_four_point_list.size();
        List<Map> route_opt = route_list;

        //make route_opt
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


        //
        List<Map> connection_volume_list = new ArrayList<Map>();
        Map<String,Map> timebucketMap = new HashMap<>();

        for (Map ropt:route_opt)
        {
            for(int n=0;n<timebucket_num.length;n++) {
                String openstr = ropt.get("route_open").toString();
                if (((int)Double.parseDouble(openstr))==1)
                {
                    String timebucket = ropt.get("timebucket_"+timebucket_num[n]).toString();
                    if (timebucket.length()>1) //not empty
                    {
                        if(timebucketMap.containsKey(timebucket))
                        {

                            Map c_volume = timebucketMap.get(timebucket);
                            double vol = Double.parseDouble(c_volume.get("volume").toString());
                            int r_count = Integer.parseInt(c_volume.get("route_cnt").toString());
                            c_volume.put("volume",vol+Double.parseDouble(ropt.get("demand").toString()));
                            c_volume.put("route_cnt",r_count+1);
                        }
                        else {
                            HashMap n_volume = new HashMap();
                            n_volume.put("timebucket",timebucket);
                            n_volume.put("volume",ropt.get("demand").toString());
                            n_volume.put("route_cnt",1);
                            connection_volume_list.add(n_volume);
                            timebucketMap.put(timebucket,n_volume);
                        }
                    }

                }

            }
        }

        //connection_opt_list

        for (int ic = 0;ic< connection_list.size();ic++)
        {
            Map conn = connection_list.get(ic);
            Map con_opt = new HashMap<>(conn);

            for (int icv=0;icv<connection_volume_list.size();icv++)
            {
                Map conv = connection_volume_list.get(icv);
                if (conn.get("timebucket").toString().equals(conv.get("timebucket").toString()))
                {
                    con_opt.put("volume",conv.get("volume"));
                    con_opt.put("route_cnt",conv.get("route_cnt"));
                }
            }
            connection_opt_list.add(con_opt);
        }

        //order connection_opt_list accroding j_connection_id
        connection_opt_list = orderByParameter(connection_opt_list,"j_connection_id");

        for(int e=0;e<J;e++){
            connection_opt_list.get(e).put("truck",solution[I+e]);
            connection_opt_list.get(e).put("bike",solution[I+J+e]);
            connection_opt_list.get(e).put("didi",solution[I+J*2+e]);
            connection_opt_list.get(e).put("dada",solution[I+J*3+e]);
        }

        for (int ic =0;ic<connection_opt_list.size();ic++)
        {
            double double1 = Double.parseDouble(connection_opt_list.get(ic).get("truck").toString());
            double double2 = Double.parseDouble(connection_opt_list.get(ic).get("bike").toString());
            double double3 = Double.parseDouble(connection_opt_list.get(ic).get("didi").toString());
            double double4 = Double.parseDouble(connection_opt_list.get(ic).get("dada").toString());

            if (double1<0.1&& double2<0.1&& double3<0.1 && double4<0.1)
            {
                connection_opt_list.remove(ic);
                ic--;
            }
        }

        logger.info("insert");
        tempService.saveConnectionOpt(connection_opt_list,gurobyCost,OpenScenariosId);

    }

    protected List<Map> getTopFiveByParamter(List<Map> listMap, String parameter){
        List<Map> list_result = new ArrayList<Map>();

        List<Integer> odid = new ArrayList<Integer>();

        for(int e=0;e< listMap.size();e++) {
            Integer currentodid = Integer.parseInt(listMap.get(e).get("m_od_id").toString());
            if (!odid.contains(currentodid)) {
                List<Map> list_temp = new ArrayList<Map>();
                for (int k = 0; k < listMap.size(); k++) {
                    Integer iterodid = Integer.parseInt(listMap.get(k).get("m_od_id").toString());
                    if (iterodid.equals(currentodid)) {
                        list_temp.add(listMap.get(k));
                    }
                }
                odid.add(currentodid);

                //same odid is added to temp
                //get top 5
                int loop = 0;
                while(list_temp.size()>0 && loop < 5)
                {
                    double mindistance = 100000000;
                    int minidx = -1;
                    for(int ll=0;ll< list_temp.size();ll++)
                    {
                        double dist = Double.parseDouble(list_temp.get(ll).get("distance").toString());
                        if(dist < mindistance) {
                            mindistance = dist;
                            minidx = ll;
                        }
                    }
                    list_result.add(list_temp.get(minidx));
                    list_temp.remove(minidx);
                    loop++;
                }

            }
        }

        return list_result;
    }

    public List<Map> orderByParameter(List<Map> list_temp,String orderParameter){
        List<Map> resultList = new ArrayList<>();

        while (!list_temp.isEmpty())
        {
            double minValue = 1000000000;
            int minIdx= -1;
            for (int il = 0;il<list_temp.size();il++)
            {
                Double dvalue = Double.parseDouble(list_temp.get(il).get(orderParameter).toString());
                if (dvalue<minValue)
                {
                    minValue = dvalue;
                    minIdx = il;
                }
            }
            resultList.add(list_temp.get(minIdx));
            list_temp.remove(minIdx);
        }

        return resultList;
    }

}
