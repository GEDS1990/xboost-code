package com.xboost.mapper;

import com.xboost.pojo.Route;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/11/5 0005.
 */
public interface SolutionEfficiencyMapper {


    // 查询所有路线信息  @return
    List<Route> findAll(String scenariosId);

    //查询所有网点
    List<String> findAllSite(String scenariosId);

    Integer findSbVol(Map<String,Object> param);


}

