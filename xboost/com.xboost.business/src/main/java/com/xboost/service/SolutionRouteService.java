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
     * 新增activity信息
     * @param activity
     */
    public void addActivity(Activity activity) {
        activity.setCreateTime(DateTime.now().toString("yyyy-MM-dd HH:mm"));

        solutionRouteMapper.addActivity(activity);

    }

    /**
     * 查询所有模型整体参数信息
     * param
     * @return
     */
    public List<Route> findAllRoute(String scenariosId) {
        return solutionRouteMapper.findAll(scenariosId);
    }

    /**
     * 获取模型整体参数信息总数量
     * @return
     */
    public Integer findAllCount(String scenariosId) {
        return solutionRouteMapper.findAllCount(scenariosId).intValue();
    }

    /**
     * 根据查询条件获取模型整体参数信息
     * param param
     * @return
     */
    public List<Route> findByParam(Map<String, Object> param) {
        return solutionRouteMapper.findByParam(param);
    }

    /**
     * 根据查询条件获取模型整体参数信息的数量
     * @param param
     * @return
     */
    public Integer findCountByParam(Map<String, Object> param) {
        return solutionRouteMapper.findCountByParam(param).intValue();
    }


    /**
     * 根据用户的ID查询模型整体参数信息
     * @param id 用户ID
     * @return
     */
    public Route findById(Integer id) {
        return solutionRouteMapper.findById(id);
    }



}
