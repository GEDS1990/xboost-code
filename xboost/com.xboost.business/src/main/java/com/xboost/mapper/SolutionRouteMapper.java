package com.xboost.mapper;

import com.xboost.pojo.Activity;
import com.xboost.pojo.Route;
import com.xboost.pojo.SiteInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/11/5 0005.
 */
public interface SolutionRouteMapper {

    //  添加Route信息  @param route
    void addRoute(Route route);

    //scenariosId 删除   * @param scenariosId
    void delByScenariosId(Integer scenariosId);

    // 查询所有模型整体参数信息  @return
    List<Route> findAll(String scenariosId);

    //查询所有模型整体参数信息总数
    Long findAllCount(String scenariosId);

    /**
     * 根据用户的ID查询模型整体参数
     * @param id 模型整体参数id
     * @return
     */
    Route findById(Integer id);

    // 根据查询条件获路线信息  @param param @return
    List<Route> findByParam(Map<String, Object> param);

    //根据查询条件获取路线数量
    Long findCountByParam(Map<String, Object> param);

    //根据场景id查询routeId
    Integer findRouteId(String scenariosId);

    // 根据路线id获取路线信息,根据curLoc和siteCode关联查询t_solution_route,t_site_info   @param param @return
    List<Map<String,Object>> findByRoute(Map<String, Object> param);

    // 根据路线id获取路线信息,根据curLoc和siteCode关联查询t_solution_route,t_site_info
    Long findCountByRoute(Map<String, Object> param);

    // 查询全部路线信息,根据curLoc和siteCode关联查询t_solution_route,t_site_info   @param param @return
    List<Map<String,Object>> findAllByRoute(String scenariosId);

    // 查询全部路线信息,根据curLoc和siteCode关联查询t_solution_route,t_site_info
    Long findAllCountByRoute(String scenariosId);

    String findTotalDistance(@Param("scenariosId")String scenariosId,@Param("routeCount") String routeCount);

    //排车更新carName到route表
    void updateCarName(Map<String, Object> param);

    void updateCarToBusy(@Param("scenariosId")String scenariosId,@Param("carName") String carName);

    void updateCarToIdle(@Param("scenariosId")String scenariosId,@Param("carName") String carName);

    void updateAllCarToIdle(String scenariosId);

    void updateScenariosModel(@Param("scenariosModel")String scenariosModel,@Param("openScenariosId") String scenariosId);

    List<String> findUsingCar(@Param("scenariosId")String scenariosId,@Param("routeCount") String routeCount);
    List<String> findIdleCar1(String scenariosId);

    List<String> findIdleCar(@Param("scenariosId")String scenariosId,@Param("routeCount") String routeCount);

}

