package com.xboost.mapper;

import com.xboost.pojo.Route;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/11/5 0005.
 */
public interface SolutionVehiclesMapper {

    // 根据路线id获取路线信息,根据carType和type关联查询t_solution_route,t_car_info   @param param @return
    List<Map<String,Object>> findByCar(Map<String, Object> param);

    // 根据路线id获取路线信息,根据carType和type关联查询t_solution_route,t_car_info
    Long findCountByCar(Map<String, Object> param);

    // 根据路线id获取路线信息总数,根据carType和type关联查询t_solution_route,t_car_info
    Long findAllCountByCar(String scenariosId);

    // 查询已使用的车数
    Long findBusyCarCount(String scenariosId);

}

