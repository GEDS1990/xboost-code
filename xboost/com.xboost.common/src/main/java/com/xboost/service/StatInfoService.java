package com.xboost.service;

import com.mckinsey.sf.data.solution.ArrInfo;
import com.mckinsey.sf.data.solution.StatInfo;
import com.xboost.mapper.ArrInfoMapper;
import com.xboost.mapper.StatInfoMapper;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import javax.inject.Named;

@Named
@Transactional
public class StatInfoService {
    /**
     * 根据查询条件获取模
     * @param param
     * @return
     */
    @Inject
    StatInfoMapper statInfoMapper;
    public void saveStatInfo(StatInfo statInfo) {
        statInfoMapper.saveStatInfo(statInfo);
    }
}
