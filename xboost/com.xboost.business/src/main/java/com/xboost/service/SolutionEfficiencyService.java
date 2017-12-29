package com.xboost.service;

import com.xboost.mapper.SolutionRouteMapper;
import com.xboost.mapper.SolutionEfficiencyMapper;
import com.xboost.pojo.Route;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/11/5 0005.
 */

@Named
@Transactional
public class SolutionEfficiencyService {
    private static Logger logger = LoggerFactory.getLogger(SolutionEfficiencyService.class);

    @Inject
    private SolutionRouteMapper solutionRouteMapper;
    @Inject
    private SolutionEfficiencyMapper solutionEfficiencyMapper;


    /**
     * 根据场景id查询所有路线信息
     * param
     * @return
     */
    public List<Route> findAllRoute(String scenariosId) {
        return solutionRouteMapper.findAll(scenariosId);
    }

    /**
     * 查询所有网点
     * param
     * @return
     */
    public List<String> findAllSite(String scenariosId) {
        return solutionEfficiencyMapper.findAllSite(scenariosId);
    }

    //发出票数
    public Integer findSbVol(Map<String,Object> param){
        return solutionEfficiencyMapper.findSbVol(param);
    }

    //到达票数
    public Integer findUnloadVol(Map<String,Object> param){
        return solutionEfficiencyMapper.findUnloadVol(param);
    }

    //到达车辆
    public Integer findArrCar(Map<String,Object> param) {
        return solutionEfficiencyMapper.findArrCar(param);
    }

    //发出车辆
    public Integer findLeaveCar(Map<String,Object> param) {
        return solutionEfficiencyMapper.findLeaveCar(param);
    }


}
