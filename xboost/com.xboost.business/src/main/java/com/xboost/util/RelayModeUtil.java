package com.xboost.util;

import com.mckinsey.sf.constants.IConstants;
import com.mckinsey.sf.data.Configuration;
import com.xboost.websocket.SystemWebSocketHandler;
import jxl.Workbook;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;

import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RelayModeUtil implements IConstants {
    public void excute() throws IOException {

        String fileName = "D:/chinaxin/rdemo/input/集散点能力配置v11.xlsx";
        String fileNamedd = "D:/chinaxin/rdemo/input/集散点滴滴配置v11.xlsx";
        File f = new File(fileName);
        File fdd = new File(fileNamedd);
        ExcelUtil ex = new ExcelUtil();
        try {
            //货车
            List<String> lineList = ex.readExcel(f, 1);
            double res[][] = new double[1][];
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
        }
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
//        rhs = c(rep(1,M),rep(0,J),outflow_lim,inflow_lim,didi_outflow_lim,didi_inflow_lim,0);
//        obj<-c(rep(0,I), connection$cost_truck, connection$cost_bike,connection$cost_didi,connection$cost_data)
////        types<-c(rep("B",I),rep("I",J*4))
//        sense = c(rep("=",M),rep("<=",J),rep("<=",N*4*max(timebucket_num)),"=");
        int i=0,I=0,J=0,M=0;
        i = I+J;
        String[] types = new String[i];
        for(int n=0;n<I;n++){
            types[n] = "B";
        }
        for(int n=0;n<J;n++){
            types[I+n] = "I";
        }
        int N=0;
//        i = M + J + N*4*max(timebucket_num);
        String[] sense = new String[i];
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

        Map<String,Object> model = new HashMap<String,Object>();
//        model.put("A",cons);
//        model.put("obj",obj);
        model.put("sense",sense);
//        model.put("rhs",rhs);
        model.put("vtypes",types);
//        model.put("start",mip$x);
        model.put("modelsense","min");

        //call gurobi

    }
}
