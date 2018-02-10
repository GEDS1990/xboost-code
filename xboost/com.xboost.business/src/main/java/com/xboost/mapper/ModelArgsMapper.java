package com.xboost.mapper;

import com.xboost.pojo.ModelArgs;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/11/5 0005.
 */
public interface ModelArgsMapper {

    //  添加模型整体参数信息  @param modelArgs
    void add(ModelArgs modelArgs);

    // 查询所有模型整体参数信息  @return
    List<ModelArgs> findAll(@Param("scenariosId") String scenariosId,@Param("modelType") String modelType);

    //查询所有模型整体参数信息总数
    Long findAllCount(@Param("scenariosId") String scenariosId,@Param("modelType") String modelType);

    //根据用户的ID查询模型整体参数
    ModelArgs findById(Integer id);

    // 编辑模型整体参数  @param modelArg
    void edit(ModelArgs modelArgs);

}

