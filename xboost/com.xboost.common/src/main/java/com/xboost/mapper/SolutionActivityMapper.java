package com.xboost.mapper;

import com.xboost.pojo.Activity;
import com.xboost.pojo.Route;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/11/5 0005.
 */
public interface SolutionActivityMapper {

    //  添加Activity信息  @param route
    void addActivity(Activity activity);

    //scenariosId 删除   * @param scenariosId
    void delByScenariosId(Integer scenariosId);

    // 查询所有模型整体参数信息  @return
    List<Activity> findAll(String scenariosId);

    //查询所有模型整体参数信息总数
    Long findAllCount(@Param("scenariosId") String scenariosId,@Param("userId") String userId);

    /**
     * 根据用户的ID查询模型整体参数
     * @param id 模型整体参数id
     * @return
     */
    Activity findById(Integer id);

    // 根据查询条件获取模型整体参数  @param param @return
    List<Activity> findByParam(Map<String, Object> param);

    //根据查询条件获取模型整体参数信息的数量
    Long findCountByParam(Map<String, Object> param);

}

