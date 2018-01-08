package com.xboost.mapper.jieli;


import com.xboost.pojo.jieli.Temp;

import java.util.List;
import java.util.Map;

public interface TempMapper {
    // 根据查询条件获取模型整体参数  @param param @return
    void saveTemp(Temp tmep);

    List<Map> findAllTwoPointsRoute(String scenariosId);
    //findAll
    List<Map> findAll01(String scenariosId);

    //findAll
    List<Map> findAll02(String scenariosId);
    //findAll
    List<Map> findAll03(String scenariosId);
    //findAll
    List<Map> findAll04(String scenariosId);
    //findAll
    List<Map> findAll05(String scenariosId);
    //findAll
    List<Map> findAll06(String scenariosId);

}
