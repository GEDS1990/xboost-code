package xboost.util;

import com.mckinsey.sf.constants.IConstants;
import com.mckinsey.sf.data.Configuration;
import com.xboost.pojo.DemandInfo;
import com.xboost.pojo.SiteDist;
import com.xboost.pojo.SiteInfo;
import com.xboost.service.DemandInfoService;
import com.xboost.service.SiteDistService;
import com.xboost.service.SiteInfoService;
import com.xboost.service.jieli.TempService;
import org.ujmp.core.DenseMatrix;
import org.ujmp.core.Matrix;
import com.xboost.util.ShiroUtil;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RelayModeUtil implements IConstants {
    public void excute(TempService tempService, DemandInfoService demandInfoService, SiteDistService siteDistService, SiteInfoService siteInfoService) throws IOException {

       /* String fileName = "D:/chinaxin/rdemo/input/集散点能力配置v11.xlsx";
        String fileNamedd = "D:/chinaxin/rdemo/input/集散点滴滴配置v11.xlsx";
        File f = new File(fileName);
        File fdd = new File(fileNamedd);
        ExcelUtil ex = new ExcelUtil();
        double res[][] = new double[1][];
        try {
            //货车
            List<String> lineList = ex.readExcel(f, 1);
            for(int j = 0 ; j<22 ; j++){
                for(int i=2;i<lineList.size();i++){
                    String[] row = lineList.get(i).split("#");
                    res[1][i] = Double.parseDouble(row[5+j]);
                }
            }
            //将数组转化为矩阵
            RealMatrix outflow_lim = new Array2DRowRealMatrix(res);
            System.out.println(outflow_lim);

            //滴滴
            List<String> lineListdd = ex.readExcel(fdd, 1);
            double resdd[][] = new double[1][];
            for(int j = 0 ; j<22 ; j++){
                for(int i=2;i<lineListdd.size();i++){
                    String[] row = lineListdd.get(i).split("#");
                    resdd[1][i] = Double.parseDouble(row[5+j]);
                }
            }
            //将数组转化为矩阵
            RealMatrix didi_flow_lim = new Array2DRowRealMatrix(res);
            System.out.println(didi_flow_lim);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //colclass
        String rescolclass[][] = new String[1][];
        String[] colclass = {"character","character","character","numeric","numeric","character","character","numeric","numeric","numeric","numeric"};
        String fileNamecolclass = "D:/chinaxin/rdemo/input/外环内资源点最新.xlsx";
        File fcolclass = new File(fileName);
        List<String> lineListcolclass = null;
        try {
            lineListcolclass = ex.readExcel(fcolclass, 1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        for(int i=2;i<lineListcolclass.size();i++){
            String[] row = lineListcolclass.get(i).split("#");
            rescolclass[1][i] = row[i];
        }
        //demands
        String resdemands[][] = new String[1][];
        String[] demands = {"character","character","character","numeric","numeric","character","character","numeric","numeric","numeric","numeric"};
        String fileNamedemands = "D:/chinaxin/rdemo/input/外环内资源点最新.xlsx";
        File fdemands = new File(fileNamedemands);
        List<String> lineListdemands = null;
        try {
            lineListdemands = ex.readExcel(fdemands, 1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        for(int i=2;i<lineListdemands.size();i++){
            String[] row = lineListdemands.get(i).split("#");
            resdemands[1][i] = row[i];
        }*/

        //params
        int TimeLimit = 6000;
        double MIPgap = 0.05;
        Configuration configuration = new Configuration();
        configuration.setOptimizeIterations(TimeLimit);
        Map<String,Object> params = new HashMap<String,Object>();
        params.put("TimeLimit",6000);
        params.put("MIPgap",0.05);
        //mip
//        mip<-Rglpk_solve_LP(obj=obj,mat=cons,dir=sense,rhs=rhs,max=FALSE,types=types)
        //model
//        cons = rbind(cbind(M11,M12,M13,M14,M15),cbind(M21,M22,M23,M24,M25),cbind(M31,M32,M33,M34,M35),cbind(M41,M42,M43,M44,M45),cbind(M51,M52,M53,M54,M55));
//        obj<-c(rep(0,I), connection$cost_truck, connection$cost_bike,connection$cost_didi,connection$cost_data)
//        rhs = c(rep(1,M),rep(0,J),outflow_lim,inflow_lim,didi_outflow_lim,didi_inflow_lim,0);
//        types<-c(rep("B",I),rep("I",J*4))
//        sense = c(rep("=",M),rep("<=",J),rep("<=",N*4*max(timebucket_num)),"=");
        int i=0,I=0,J=0,M=0;
        Map map = new HashMap<String,Object>();
        map.put("scenariosId", ShiroUtil.getOpenScenariosId());
        //obj
        List<DemandInfo> demandInfoList = demandInfoService.findByParam(map);
        Map<String,Object> connection = new HashMap<String,Object>();
        Map<String,Object> OD_demand = new HashMap<String,Object>();
        Map<String,Object> distance_ref = new HashMap<String,Object>();
        Map<String,Object> two_points_route = new HashMap<String,Object>();
        Map<String,Object> three_points_route = new HashMap<String,Object>();
        Map<String,Object> four_points_route = new HashMap<String,Object>();

        Map<String,Object> temp = new HashMap<String,Object>();
        List<Map> OD_demand_list= new ArrayList<Map>();
        List<Map> distance_ref_list= new ArrayList<Map>();
        List<Map> two_points_route_list= new ArrayList<Map>();
        List<Map> three_points_route_list= new ArrayList<Map>();
        List<Map> four_points_route_list= new ArrayList<Map>();
        List<Map> temp_list= new ArrayList<Map>();

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
        List<SiteDist> siteDistList = siteDistService.findByParam(map);
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
        for(int j=0;j<OD_demand_list.size();j++){
            for(int j2=0;j2<distance_ref_list.size();j2++){
                boolean b = OD_demand_list.get(j).get("inbound_id").equals(distance_ref_list.get(j2).get("inbound_id"));
                boolean b2 = OD_demand_list.get(j).get("outbound_id").equals(distance_ref_list.get(j2).get("outbound_id"));
                if(b&&b2){
                    two_points_route.put("m_od_id",OD_demand_list.get(j).get("OD_id"));
                    two_points_route.put("i_route_id",OD_demand_list.get(j).get("inbound_id")+"-"+OD_demand_list.get(j).get("outbound_id"));
                    two_points_route.put("distance",distance_ref_list.get(j2).get("km"));
                    two_points_route.put("time",distance_ref_list.get(j2).get("minutes"));
                    two_points_route.put("demand",OD_demand_list.get(j).get("volume"));
                    two_points_route.put("point1",OD_demand_list.get(j).get("inbound_id"));
                    two_points_route.put("point2",0);
                    two_points_route.put("point3",0);
                    two_points_route.put("point4",OD_demand_list.get(j).get("outbound_id"));
                    two_points_route.put("connection1",OD_demand_list.get(j).get("inbound_id")+"-"+OD_demand_list.get(j).get("outbound_id"));
                    two_points_route.put("connection2","");
                    two_points_route.put("connection3","");
                    two_points_route.put("time1",distance_ref_list.get(j2).get("minutes"));
                    two_points_route.put("time2",0);
                    two_points_route.put("time3",0);
                    two_points_route.put("dist1",distance_ref_list.get(j2).get("km"));
                    two_points_route.put("dist2",0);
                    two_points_route.put("dist3",0);
                    two_points_route.put("timecost",-Integer.parseInt(distance_ref_list.get(j2).get("minutes").toString()));
                    two_points_route.put("scenario",OD_demand_list.get(j).get("scenario"));
                    two_points_route.put("scenario_lim1",OD_demand_list.get(j).get("scenario_lim1"));
                    two_points_route.put("scenario_lim2",OD_demand_list.get(j).get("scenario_lim2"));
                    two_points_route.put("wait_time",0);
                    two_points_route_list.add(two_points_route);
                }else{
                    two_points_route.put("m_od_id",OD_demand_list.get(j).get("OD_id"));
                    two_points_route.put("i_route_id",OD_demand_list.get(j).get("inbound_id")+"-"+OD_demand_list.get(j).get("outbound_id"));
                    two_points_route.put("distance","");
                    two_points_route.put("time","");
                    two_points_route.put("demand",OD_demand_list.get(j).get("volume"));
                    two_points_route.put("point1",OD_demand_list.get(j).get("inbound_id"));
                    two_points_route.put("point2",0);
                    two_points_route.put("point3",0);
                    two_points_route.put("point4",OD_demand_list.get(j).get("outbound_id"));
                    two_points_route.put("connection1",OD_demand_list.get(j).get("inbound_id")+"-"+OD_demand_list.get(j).get("outbound_id"));
                    two_points_route.put("connection2","");
                    two_points_route.put("connection3","");
                    two_points_route.put("time1","");
                    two_points_route.put("time2",0);
                    two_points_route.put("time3",0);
                    two_points_route.put("dist1","");
                    two_points_route.put("dist2",0);
                    two_points_route.put("dist3",0);
                    two_points_route.put("timecost","");
                    two_points_route.put("scenario",OD_demand_list.get(j).get("scenario"));
                    two_points_route.put("scenario_lim1",OD_demand_list.get(j).get("scenario_lim1"));
                    two_points_route.put("scenario_lim2",OD_demand_list.get(j).get("scenario_lim2"));
                    two_points_route.put("wait_time",0);
                    two_points_route_list.add(two_points_route);
                }
            }
        }
        //temp
        for(int j=0;j<OD_demand_list.size();j++){
            for(int j2=0;j2<distance_ref_list.size();j2++){
                boolean b = two_points_route_list.get(j).get("inbound_id").equals(distance_ref_list.get(j2).get("inbound_id"));
                if(b){
                    temp.put("m_od_id",OD_demand_list.get(j).get("OD_id"));
                    temp.put("i_route_id",OD_demand_list.get(j).get("inbound_id")+"-"+OD_demand_list.get(j).get("outbound_id"));
                    temp.put("distance",distance_ref_list.get(j2).get("km"));
                    temp.put("time",distance_ref_list.get(j2).get("minutes"));
                    temp.put("demand",OD_demand_list.get(j).get("volume"));
                    temp.put("point1",OD_demand_list.get(j).get("inbound_id"));
                    temp.put("point2",0);
                    temp.put("point3",0);
                    temp.put("point4",OD_demand_list.get(j).get("outbound_id"));
                    temp.put("connection1",OD_demand_list.get(j).get("inbound_id")+"-"+OD_demand_list.get(j).get("outbound_id"));
                    temp.put("connection2","");
                    temp.put("connection3","");
                    temp.put("time1",distance_ref_list.get(j2).get("minutes"));
                    temp.put("time2",0);
                    temp.put("time3",0);
                    temp.put("dist1",distance_ref_list.get(j2).get("km"));
                    temp.put("dist2",0);
                    temp.put("dist3",0);
                    temp.put("timecost",-Integer.parseInt(distance_ref_list.get(j2).get("minutes").toString()));
                    temp.put("scenario",OD_demand_list.get(j).get("scenario"));
                    temp.put("scenario_lim1",OD_demand_list.get(j).get("scenario_lim1"));
                    temp.put("scenario_lim2",OD_demand_list.get(j).get("scenario_lim2"));
                    temp.put("wait_time",0);
                    temp_list.add(temp);
                }else{
                    temp.put("m_od_id",OD_demand_list.get(j).get("OD_id"));
                    temp.put("i_route_id",OD_demand_list.get(j).get("inbound_id")+"-"+OD_demand_list.get(j).get("outbound_id"));
                    temp.put("distance","");
                    temp.put("time","");
                    temp.put("demand",OD_demand_list.get(j).get("volume"));
                    temp.put("point1",OD_demand_list.get(j).get("inbound_id"));
                    temp.put("point2",0);
                    temp.put("point3",0);
                    temp.put("point4",OD_demand_list.get(j).get("outbound_id"));
                    temp.put("connection1",OD_demand_list.get(j).get("inbound_id")+"-"+OD_demand_list.get(j).get("outbound_id"));
                    temp.put("connection2","");
                    temp.put("connection3","");
                    temp.put("time1","");
                    temp.put("time2",0);
                    temp.put("time3",0);
                    temp.put("dist1","");
                    temp.put("dist2",0);
                    temp.put("dist3",0);
                    temp.put("timecost","");
                    temp.put("scenario",OD_demand_list.get(j).get("scenario"));
                    temp.put("scenario_lim1",OD_demand_list.get(j).get("scenario_lim1"));
                    temp.put("scenario_lim2",OD_demand_list.get(j).get("scenario_lim2"));
                    temp.put("wait_time",0);
                    temp_list.add(temp);
                }
            }
        }

        //three_points_route
        for(int j=0;j<temp_list.size();j++){
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
                boolean bon1 = Integer.parseInt(temp_list.get(j).get("outbound_id").toString())==
                        Integer.parseInt(distance_ref_list.get(j2).get("inbound_id").toString());
                boolean bon2 = Integer.parseInt(temp_list.get(j).get("point4").toString())==
                        Integer.parseInt(distance_ref_list.get(j2).get("outbound_id").toString());
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
                    three_points_route.put("distance",Integer.parseInt(temp_list.get(j).get("km").toString()));
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
        }

        //temp2
        for(int j=0;j<three_points_route_list.size();j++){
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
        }
        //four_points_route
        for(int j=0;j<temp_list.size();j++){
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
        }
        //time buckets
        //real timebucket###
        int full_time = 210;
        int route_time_unit = 10;
        int[][] timebucket_num = new int[1][full_time/route_time_unit];
        for(int j=0;j<full_time/route_time_unit;j++){
            timebucket_num[1][j] = j+1;
        }
        Object route_two_point = null;
        Object route_three_point = null;
        Object route_four_point = null;
//        ###two point route###
        int[][] site1 = new int[1][full_time/route_time_unit];
        int timebucket_site;
        for(int j=0;j<full_time/route_time_unit;j++){
            site1[1][j] = j+1;
            timebucket_site = j+1;
        }
        //route_temp
        for(int j=0;j<two_points_route_list.size();j++){
            two_points_route.put("timebucket_1",two_points_route_list.get(j).get("connection1"));
            two_points_route.put("timebucket_"+j+2,"");
        }
        List<Map> code1 = two_points_route_list;
        List<Map> route_temp_list = code1;
        for(int j=0;j<route_temp_list.size();j++){
            route_temp_list.get(j).put("time_id1",site1[1][j]);
        }
        for(int j=0;j<two_points_route_list.size();j++){
            two_points_route_list.get(j).put("timebucket_1",route_temp_list.get(j).get("connection1"));
            two_points_route_list.get(j).put("timebucket_"+j+2,route_temp_list.get(j).get("timebucket_"+j+2));
            two_points_route_list.get(j).put("ok",Integer.parseInt(two_points_route_list.get(j).get("scenario_lim2").toString())
                    *route_time_unit-((Integer.parseInt(two_points_route_list.get(j).get("time_id1").toString())-1)*route_time_unit)
                    +Integer.parseInt(two_points_route_list.get(j).get("time1").toString()));
        }

        for(int j=0;j<two_points_route_list.size();j++){
            int ok = Integer.parseInt(two_points_route_list.get(j).get("scenario_lim2").toString())
                    *route_time_unit-((Integer.parseInt(two_points_route_list.get(j).get("time_id1").toString())-1)*route_time_unit)
                    +Integer.parseInt(two_points_route_list.get(j).get("time1").toString());
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




        Matrix dense = DenseMatrix.Factory.zeros(4, 4);

//        connection.put("kmh_didi",);
//        connection.put("kmh_truck",);
//        connection.put("kmh_bike",);
//        connection.put("min_didi",);
//        connection.put("cost_bike",);
//        connection.put("cost_didi",);
//        connection.put("cost_truck",);
//        connection.put("cost_data",);
        int[] obj = new int[I];
        for(int j=0;j<I;j++){
            obj[j] = 0;
        }

        //根据场景ID查询SiteDist
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
        int N=0;
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

        Map<String,Object> model = new HashMap<String,Object>();
//        model.put("A",cons);
        model.put("obj",obj);
        model.put("sense",sense);
        model.put("rhs",rhs);
        model.put("vtypes",types);
//        model.put("start",mip$x);
        model.put("modelsense","min");

        //call gurobi

    }
}
