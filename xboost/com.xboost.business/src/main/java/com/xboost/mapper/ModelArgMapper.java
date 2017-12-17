package com.xboost.mapper;

import com.xboost.pojo.ModelArg;
import com.xboost.pojo.SiteInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/11/5 0005.
 */
public interface ModelArgMapper {

    //  添加模型整体参数信息  @param siteInfo
    void add(ModelArg modelArg);


    //  update模型整体参数信息  @param siteInfo
    void update(ModelArg modelArg);


    // 查询所有模型整体参数信息  @return
    List<ModelArg> findAll(String scenariosId);

    //查询所有模型整体参数信息总数
    Long findAllCount(String scenariosId);

    /**
     * 根据用户的ID查询模型整体参数
     * @param id 模型整体参数id
     * @return
     */
    ModelArg findById(Integer id);

    // 根据查询条件获取模型整体参数  @param param @return
    List<ModelArg> findByParam(Map<String, Object> param);

    //根据查询条件获取模型整体参数信息的数量
    Long findCountByParam(Map<String, Object> param);


    // 编辑模型整体参数  @param modelArg
    void editModelArg(ModelArg modelArg);


    //根据id 删除模型整体参数   * @param siteCode
    void delById(Integer id);

    //根据场景id,算法模型查询网点集散点人效
    Integer findSitePeopleWork(@Param("scenariosId") String scenariosId,@Param("modelType") String modelType);

    //根据场景id,算法模型查询集配站集散点人效
    Integer findDistribPeopleWork(@Param("scenariosId") String scenariosId,@Param("modelType") String modelType);
}

