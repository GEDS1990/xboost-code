package com.xboost.service;

import com.xboost.mapper.SolutionRouteMapper;
import com.xboost.pojo.Activity;
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
public class SolutionRouteService {
    private static Logger logger = LoggerFactory.getLogger(SolutionRouteService.class);

    @Inject
    private SolutionRouteMapper solutionRouteMapper;

    /**
     * 新增route信息
     * @param route
     */
    public void addRoute(Route route) {
        route.setCreateTime(DateTime.now().toString("yyyy-MM-dd HH:mm"));

        solutionRouteMapper.addRoute(route);

    }

    /**
     * 根据场景id查询所有路线信息
     * param
     * @return
     */
    public List<Route> findAllRoute(String scenariosId) {
        return solutionRouteMapper.findAll(scenariosId);
    }

    /**
     * 获取路线总数量
     * @return
     */
    public Integer findAllCount(String scenariosId) {
        return solutionRouteMapper.findAllCount(scenariosId).intValue();
    }

       /**
     * 根据查询条件获取路线信息
     * param param
     * @return
     */
    public List<Route> findByParam(Map<String, Object> param) {
        return solutionRouteMapper.findByParam(param);
    }


    /**
     * 根据查询条件获取模型整体参数信息的路线数量
     * @param param
     * @return
     */
    public Integer findCountByParam(Map<String, Object> param) {
        return solutionRouteMapper.findCountByParam(param).intValue();
    }

    //根据场景id查询routeId
    public Integer findRouteId(String scenariosId) {
        return solutionRouteMapper.findRouteId(scenariosId);
    }

    /**
     * 根据路线id获取线路信息
     * param param
     * @return
     */
    public List<Map<String, Object>> findByRoute(Map<String, Object> param) {
        return solutionRouteMapper.findByRoute(param);
    }

    /**
     * 根据路线id获取线路数量
     * @param param
     * @return
     */
    public Integer findCountByRoute(Map<String, Object> param) {
        return solutionRouteMapper.findCountByRoute(param).intValue();
    }

    /**
     * 根据场景id查询全部线路信息
     * param param
     * @return
     */
    public List<Map<String, Object>> findAllByRoute(String scenariosId) {
        return solutionRouteMapper.findAllByRoute(scenariosId);
    }

    /**
     * 根据场景id查询全部线路数量
     * @param scenariosId
     * @return
     */
    public Integer findAllCountByRoute(String scenariosId) {
        return solutionRouteMapper.findAllCountByRoute(scenariosId).intValue();
    }


    /**
     * 根据用户的ID查询路线信息
     * @param id 用户ID
     * @return
     */
    public Route findById(Integer id) {
        return solutionRouteMapper.findById(id);
    }

    /**
     * 根据id删除模型整体参数信息
     * @param scenariosId
     */
    public void delByScenariosId(Integer scenariosId) {

        solutionRouteMapper.delByScenariosId(scenariosId);
    }




    //查询路线总路程
    public String findTotalDistance(String scenariosId,String routeCount) {
        return solutionRouteMapper.findTotalDistance(scenariosId,routeCount);
    }
}
