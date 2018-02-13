package com.xboost.mapper;

import com.mckinsey.sf.data.solution.StatInfo;

public interface StatInfoMapper {
    // 根据查询条件获取模型整体参数  @param param @return
    void saveStatInfo(StatInfo statInfo);

}
