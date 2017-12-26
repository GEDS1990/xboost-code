package com.xboost.mapper.jieli;


import com.xboost.pojo.jieli.Temp;

import java.util.List;

public interface TempMapper {
    // 根据查询条件获取模型整体参数  @param param @return
    void saveTemp(Temp tmep);
    //findAll
    List<Temp> findAll01(String scenariosId);

}
