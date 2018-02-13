package com.xboost.service;

import com.mckinsey.sf.data.solution.ArrInfo;
import com.xboost.mapper.ArrInfoMapper;
import com.xboost.mapper.ConfigurationMapper;
import com.xboost.pojo.Configuration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Service
public class ArrInfoService {
    /**
     * 根据查询条件获取模
     * @param param
     * @return
     */
    @Autowired
    ArrInfoMapper arrInfoMapper;
    public void saveArrInfo(ArrInfo arrInfo) {
       arrInfoMapper.saveArrInfo(arrInfo);
    }
}
