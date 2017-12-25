package com.xboost.mapper;

import com.xboost.pojo.Dimensions;

import java.util.List;

public interface DimensionsMapper {
    /**
     * 根据车辆ID查找
     * @param scenariosId
     * @return
     */
    List<Dimensions> findByScenariosId(Integer scenariosId);


}
