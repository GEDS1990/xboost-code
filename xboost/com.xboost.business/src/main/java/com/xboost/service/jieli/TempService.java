package com.xboost.service.jieli;

import com.mckinsey.sf.data.solution.ArrInfo;
import com.xboost.mapper.ArrInfoMapper;
import com.xboost.mapper.jieli.TempMapper;
import com.xboost.pojo.jieli.Temp;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.List;
import java.util.Map;

@Named
@Transactional
public class TempService {
    /**
     * 根据查询条件获取模
     * @param param
     * @return
     */
    @Inject
    TempMapper tempMapper;
    public void saveTempInfo(Temp temp) {
        tempMapper.saveTemp(temp);
    }

    public void saveRouteOpt(Map ro) {
        tempMapper.saveRouteOpt(ro);
    }

    public void saveOD_demand(Map od) {
        tempMapper.saveOD_demand(od);
    }

    public void savedistance_ref(Map dr) {
        tempMapper.savedistance_ref(dr);
    }

    public void savetemp_list(Map tp) {
        tempMapper.savetemp_list(tp);
    }


    public void delAllOD_demandByScenariosId(String OpenScenariosId) {
        tempMapper.delAllOD_demandByScenariosId(OpenScenariosId);
    }

    public void delAlltemp_listByScenariosId(String OpenScenariosId) {
        tempMapper.delAlltemp_listByScenariosId(OpenScenariosId);
    }

    public void delAlldistance_refByScenariosId(String OpenScenariosId) {
        tempMapper.delAlldistance_refByScenariosId(OpenScenariosId);
    }



    @Cacheable
    public List<Map> findthree_points_route(String scenariosId) {
        return tempMapper.findthree_points_route(scenariosId);
    }
    @Cacheable
    public List<Map> findfour_points_route(String scenariosId) {
        return tempMapper.findfour_points_route(scenariosId);
    }

    @Cacheable
    public List<Map> findAllTwoPointsRoute(String scenariosId) {
        return tempMapper.findAllTwoPointsRoute(scenariosId);
    }

    @Cacheable
    public List<Map> findRouteTempBycode1(String route_temp_sql) {
        return tempMapper.findRouteTempBycode1(route_temp_sql);
    }

    /**
     * 查询所有运力信息
     * param
     * @return
     */
    @Cacheable
    public List<Map> findAll01(String scenariosId) {
        return tempMapper.findAll01(scenariosId);
    }


    /**
     * 查询所有运力信息
     * param
     * @return
     */
    @Cacheable
    public List<Map> findAllFlowLim(String scenariosId) {
        return tempMapper.findAllFlowLim(scenariosId);
    }

    /**
     * 查询所有运力信息
     * param
     * @return
     */
    @Cacheable
    public List<Map> findAll02(String scenariosId) {
        return tempMapper.findAll02(scenariosId);
    }
    /**
     * 查询所有运力信息
     * param
     * @return
     */
    @Cacheable
    public List<Map> findAll03(String scenariosId) {
        return tempMapper.findAll03(scenariosId);
    }
    /**
     * 查询所有运力信息
     * param
     * @return
     */
    @Cacheable
    public List<Map> findAll04(String scenariosId) {
        return tempMapper.findAll04(scenariosId);
    }
    /**
     * 查询所有运力信息
     * param
     * @return
     */
    @Cacheable
    public List<Map> findAll05(String scenariosId) {
        return tempMapper.findAll05(scenariosId);
    }
    /**
     * 查询所有运力信息
     * param
     * @return
     */
    @Cacheable
    public List<Map> findAll06(String scenariosId) {
        return tempMapper.findAll06(scenariosId);
    }
    /**
     * 查询所有运力信息
     * param
     * @return
     */
    @Cacheable
    public List<Map> findAll07(String scenariosId) {
        return tempMapper.findAll07(scenariosId);
    }
    /**
     * 查询所有运力信息
     * param
     * @return
     */
    @Cacheable
    public List<Map> findAll08(String scenariosId) {
        return tempMapper.findAll08(scenariosId);
    }

}
