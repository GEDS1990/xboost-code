package com.xboost.service;

import com.xboost.mapper.ConfigurationMapper;
import com.xboost.pojo.Configuration;
import jdk.nashorn.internal.ir.annotations.Ignore;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Named
@Transactional
public class ConfigurationService {
    /**
     * 根据查询条件获取模
     * @param param
     * @return
     */
    @Inject
    ConfigurationMapper configurationMapper;
    public Configuration findConfigByParam(Map<String, Object> param) {
        return configurationMapper.findConfigByParam(param);
    }
}
