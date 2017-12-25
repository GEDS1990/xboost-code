package com.xboost.service;

import com.mckinsey.sf.data.solution.ArrInfo;
import com.xboost.mapper.ArrInfoMapper;
import com.xboost.mapper.ConfigurationMapper;
import com.xboost.pojo.Configuration;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.Map;

@Named
@Transactional
public class ArrInfoService {
    /**
     * 根据查询条件获取模
     * @param param
     * @return
     */
    @Inject
    ArrInfoMapper arrInfoMapper;
    public void saveArrInfo(ArrInfo arrInfo) {
       arrInfoMapper.saveArrInfo(arrInfo);
    }
}
