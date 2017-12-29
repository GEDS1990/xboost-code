package com.xboost.service;

import com.mckinsey.sf.data.Car;
import com.xboost.mapper.SolutionCostMapper;
import com.xboost.mapper.SolutionRouteMapper;
import com.xboost.pojo.Cost;
import com.xboost.mapper.CarMapper;
import com.xboost.util.ExcelUtil;
import com.xboost.util.ExportUtil;
import com.xboost.util.ShiroUtil;
import org.apache.poi.xssf.usermodel.*;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.ServletOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/11/5 0005.
 */

@Named
@Transactional
public class SolutionCostService {

    private static Logger logger = LoggerFactory.getLogger(SolutionCostService.class);

    @Inject
    private SolutionCostMapper solutionCostMapper;
    @Inject
    private SolutionRouteMapper solutionRouteMapper;
    @Inject
    private CarMapper carMapper;

    /**
     * 新增成本基础信息
     * @param cost
     */
    public void add(Cost cost) {
        cost.setCreateTime(DateTime.now().toString("yyyy-MM-dd HH:mm"));
        solutionCostMapper.add(cost);
    }


    /**
     * 查询成本信息
     * param
     * @return
     */
    public List<Cost> findAll(String scenariosId) {
        return solutionCostMapper.findAll(scenariosId);
    }


    /**
     * 根据参数查询成本信息
     * param param
     * @return
     */
    public List<Cost> findByParam(Map<String, Object> param) {
        return solutionCostMapper.findByParam(param);
    }

    /**
     * 根据场景id查询成本信息
     * @param scenariosId
     * @return
     */
    public Cost findByScenariosId(String scenariosId) {
        return solutionCostMapper.findByScenariosId(scenariosId);
    }


    /**
     * 编辑成本信息
     * @param cost
     */
    public void editCost(String scenariosId,Cost cost) {
        solutionCostMapper.editCost(scenariosId,cost);
    }

    /**
     * 根据scenariosId和siteCode编辑site info 里的cost相关信息
     * @param scenariosId,siteCode
     */
    public void editSiteInfo(String scenariosId,String siteCode) {
        solutionCostMapper.editSiteInfo(scenariosId,siteCode);
    }

    //查询总件量
    public Integer findTotalPiece(String scenariosId){
        return solutionCostMapper.findTotalPiece(scenariosId);
    }

    /**
     * 根据id删除模型整体参数信息
     * @param scenariosId
     */
    public void delByScenariosId(Integer scenariosId) {

        solutionCostMapper.delByScenariosId(scenariosId);
    }

    //支线运输成本
    public Double branchTransportCost()
    {
        Double branchCost=0.00;
        String scenariosId = ShiroUtil.getOpenScenariosId();
        List<String> routeCountList = solutionRouteMapper.findRouteCount(scenariosId);
        String routeCount;
        for(int i=0;i<routeCountList.size();i++){
            routeCount = routeCountList.get(i);
            Car car = carMapper.findCarCost(scenariosId,routeCount).get(0);

            Double velocity = car.getVelocity();
            Double totalDistance = Double.parseDouble(solutionRouteMapper.findTotalDistance(scenariosId,routeCount));


            if((car.getC1()).trim()==""||(car.getC1()).trim()==" "||(car.getC1()).trim()==null){
                String c="0";
            }

            String A1= car.getA1();
            int a1=Integer.parseInt(car.getA1().isEmpty()?"0":car.getA1());
            int a2=Integer.parseInt(car.getA1().isEmpty()?"0":car.getA2());
            int b1=Integer.parseInt(car.getA1().isEmpty()?"0":car.getB1());
            int b2=Integer.parseInt(car.getB2().isEmpty()?"0":car.getB2());
            int c1=Integer.parseInt((car.getC1()).trim()==""||(car.getC1()).trim()==" "?"0":car.getC1());
            int c2=Integer.parseInt((car.getC2()).trim()==""||(car.getC2()).trim()==" "?"0":car.getC2());
            int d1=Integer.parseInt(car.getD1().isEmpty()?"0":car.getD1());
            int d2=Integer.parseInt(car.getD2().isEmpty()?"0":car.getD2());
            int e1=Integer.parseInt(car.getE1().isEmpty()?"0":car.getE1());
            int e2=Integer.parseInt(car.getE2().isEmpty()?"0":car.getE2());
            int f1=Integer.parseInt(car.getF1().isEmpty()?"0":car.getF1());
            int f2=Integer.parseInt(car.getF2().isEmpty()?"0":car.getF2());

            //a1包车费用
            Double costa1=Double.parseDouble(car.getCosta1().isEmpty()?"0":car.getCosta1());
            //每公里费用
            Double costa2=Double.parseDouble(car.getCosta2().isEmpty()?"0":car.getCosta2());
            //每分钟费用
            Double costa3=Double.parseDouble(car.getCosta3().isEmpty()?"0":car.getCosta3());

            Double costb1=Double.parseDouble(car.getCostb1().isEmpty()?"0":car.getCostb1());
            Double costb2=Double.parseDouble(car.getCosta2().isEmpty()?"0":car.getCostb2());
            Double costb3=Double.parseDouble(car.getCostb3().isEmpty()?"0":car.getCostb3());

            Double costc1=Double.parseDouble(car.getCostc1().isEmpty()?"0":car.getCostc1());
            Double costc2=Double.parseDouble(car.getCostc2().isEmpty()?"0":car.getCostc2());
            Double costc3=Double.parseDouble(car.getCostc3().isEmpty()?"0":car.getCostc3());

            Double costd1=Double.parseDouble(car.getCostd1().isEmpty()?"0":car.getCostd1());
            Double costd2=Double.parseDouble(car.getCostd2().isEmpty()?"0":car.getCostd2());
            Double costd3=Double.parseDouble(car.getCostd3().isEmpty()?"0":car.getCostd3());

            Double coste1=Double.parseDouble(car.getCoste1().isEmpty()?"0":car.getCoste1());
            Double coste2=Double.parseDouble(car.getCoste2().isEmpty()?"0":car.getCoste2());
            Double coste3=Double.parseDouble(car.getCoste3().isEmpty()?"0":car.getCoste3());

            Double costf1=Double.parseDouble(car.getCostf1().isEmpty()?"0":car.getCostf1());
            Double costf2=Double.parseDouble(car.getCostf2().isEmpty()?"0":car.getCostf2());
            Double costf3=Double.parseDouble(car.getCostf3().isEmpty()?"0":car.getCostf3());

            if(totalDistance>=a1 && totalDistance<=a2){
                branchCost = branchCost + costa1;
            }
            else if(totalDistance>b1 && totalDistance<=b2){
                branchCost = branchCost + costa1 + costb2*(totalDistance-b1) + costb3*((totalDistance-b1)/velocity*60);
            }
            else if(totalDistance>c1 && totalDistance<=c2){
                branchCost = branchCost + costa1 + costb2*(b2-b1) + costc2*(totalDistance-c1) + costb3*((totalDistance-b1)/velocity*60)
                        + costc3*((totalDistance-c1)/velocity*60);
            }
            else if(totalDistance>d1 && totalDistance<=d2){
                branchCost = branchCost + costa1 + costb2*(b2-b1) + costc2*(c2-c1) + costd2*(totalDistance-d1)+costb3*((totalDistance-b1)/velocity*60)
                        + costc3*((totalDistance-c1)/velocity*60)+ costd3*((totalDistance-d1)/velocity*60);
            }
            else if(totalDistance>e1 && totalDistance<=e2){
                branchCost = branchCost + costa1 + costb2*(b2-b1) + costc2*(c2-c1) + costd2*(d2-d1) + coste2*(totalDistance-e1)
                        + costb3*((totalDistance-b1)/velocity*60) + costc3*((totalDistance-c1)/velocity*60)
                        + costd3*((totalDistance-d1)/velocity*60) + coste3*((totalDistance-e1)/velocity*60);
            }
            else if(totalDistance>f1){
                branchCost = branchCost + costa1 + costb2*(b2-b1) + costc2*(c2-c1) + costd2*(d2-d1) + coste2*(e2-e1)
                        + coste2*(e2-e1) + costf2*(totalDistance-f1)
                        + costb3*((totalDistance-b1)/velocity*60) + costc3*((totalDistance-c1)/velocity*60)
                        + costd3*((totalDistance-d1)/velocity*60) + coste3*((totalDistance-e1)/velocity*60)
                        + costf3*((totalDistance-f1)/velocity*60);
            }
        }

        return branchCost;
    }

}
