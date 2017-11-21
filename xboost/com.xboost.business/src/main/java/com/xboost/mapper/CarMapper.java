package com.xboost.mapper;

import com.mckinsey.sf.data.Car;

import java.util.Map;

public interface CarMapper {
    // 根据查询条件获取模型整体参数  @param param @return
    Car[] findCarByParam(Map<String, Object> param);

}
