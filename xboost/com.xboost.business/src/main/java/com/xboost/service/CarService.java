package com.xboost.service;

import com.mckinsey.sf.data.Car;
import com.xboost.mapper.CarMapper;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.Map;

@Named
@Transactional
public class CarService {
    /**
     * 根据查询条件获取模
     * @param param
     * @return
     */
    @Inject
    CarMapper carMapper;
    public Car[] findCarByParam(Map<String, Object> param) {
        return carMapper.findCarByParam(param);
    }
}
