package com.xboost.mapper;

import com.xboost.pojo.Cost;
import com.xboost.pojo.SiteInfo;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/11/5 0005.
 */
public interface SolutionCostMapper {

    //  添加Cost  @param cost
    void add(Cost cost);


    // 查询所有Cost信息  @return
    List<Cost> findAll(String scenariosId);


    /**
     * 根据ID查询Cost信息
     * @param scenariosId
     * @return
     */
    Cost findByScenariosId(String scenariosId);

    // 根据查询条件Cost信息  @param param @return
    List<Cost> findByParam(Map<String, Object> param);

    // 根据id编辑cost信息  @param siteInfo
    void edit(Cost cost);

}

