package com.xboost.service;

import com.mckinsey.sf.data.solution.StatInfo;
import com.xboost.mapper.StatInfoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StatInfoService {
    /**
     * 根据查询条件获取模
     * @param param
     * @return
     */
    @Autowired
    StatInfoMapper statInfoMapper;
    public void saveStatInfo(StatInfo statInfo) {
        statInfoMapper.saveStatInfo(statInfo);
    }
}
