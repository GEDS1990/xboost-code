package com.xboost.mapper;


import com.xboost.pojo.Route;
import org.apache.ibatis.annotations.Param;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/11/5 0005.
 */
public interface SolutionEfficiencyMapper {


    List<Map<String,Object>> findByCar(Map<String, Object> param);

    List<String> findAllSite(String scenariosId);

    Integer findSbVol(Map<String, Object> param);

    Integer findUnloadVol(Map<String, Object> param);

    List<Route> findArrCar(Map<String, Object> param);

    List<Route> findLeaveCar(Map<String, Object> param);

    Integer findArrCarCount(Map<String, Object> param);

    Integer findLeaveCarCount(Map<String, Object> param);

    List<Map<String,Object>> findSiteInfo(String scenariosId);

}

