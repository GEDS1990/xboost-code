package com.xboost.service;

import com.xboost.mapper.SolutionRouteMapper;
import com.xboost.mapper.SolutionVehiclesMapper;
import com.xboost.pojo.Route;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/11/5 0005.
 */

@Named
@Transactional
public class SolutionVehiclesService {
    private static Logger logger = LoggerFactory.getLogger(SolutionVehiclesService.class);

    @Inject
    private SolutionVehiclesMapper solutionVehiclesMapper;

    /**
     * 根据carType获取线路信息
     * param param
     * @return
     */
    public List<Map<String, Object>> findByCar(Map<String, Object> param) {
        return solutionVehiclesMapper.findByCar(param);
    }

    /**
     * 根据carType获取线路数量
     * @param param
     * @return
     */
    public Integer findCountByCar(Map<String, Object> param) {
        return solutionVehiclesMapper.findCountByCar(param).intValue();
    }

    /**
     * 根据carType获取线路总数量
     * @param scenariosId
     * @return
     */
    public Integer findAllCountByCar(String scenariosId) {
        return solutionVehiclesMapper.findAllCountByCar(scenariosId).intValue();
    }

}
