package com.xboost.service;

import com.xboost.mapper.ConfigurationMapper;
import com.xboost.pojo.Configuration;
import jdk.nashorn.internal.ir.annotations.Ignore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Map;

@Service
public class ConfigurationService {
    /**
     * 根据查询条件获取模
     * @param param
     * @return
     */
    @Autowired
    ConfigurationMapper configurationMapper;
    public Configuration findConfigByParam(Map<String, Object> param) {
        return configurationMapper.findConfigByParam(param);
    }
}
