package com.xboost.mapper;

import com.xboost.pojo.Configuration;

import java.util.List;
import java.util.Map;

public interface ConfigurationMapper {
    // 根据查询条件获取模型整体参数  @param param @return
    Configuration findConfigByParam(Map<String, Object> param);

}
