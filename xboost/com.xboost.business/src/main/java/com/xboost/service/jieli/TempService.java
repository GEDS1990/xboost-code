package com.xboost.service.jieli;

import com.mckinsey.sf.data.solution.ArrInfo;
import com.xboost.mapper.ArrInfoMapper;
import com.xboost.mapper.jieli.TempMapper;
import com.xboost.pojo.jieli.Temp;
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

    public List<Map> findAllTwoPointsRoute(String scenariosId) {
        return tempMapper.findAllTwoPointsRoute(scenariosId);
    }
    /**
     * 查询所有运力信息
     * param
     * @return
     */
    public List<Map> findAll01(String scenariosId) {
        return tempMapper.findAll01(scenariosId);
    }
    /**
     * 查询所有运力信息
     * param
     * @return
     */
    public List<Map> findAll02(String scenariosId) {
        return tempMapper.findAll02(scenariosId);
    }
    /**
     * 查询所有运力信息
     * param
     * @return
     */
    public List<Map> findAll03(String scenariosId) {
        return tempMapper.findAll03(scenariosId);
    }
    /**
     * 查询所有运力信息
     * param
     * @return
     */
    public List<Map> findAll04(String scenariosId) {
        return tempMapper.findAll04(scenariosId);
    }
    /**
     * 查询所有运力信息
     * param
     * @return
     */
    public List<Map> findAll05(String scenariosId) {
        return tempMapper.findAll05(scenariosId);
    }
    /**
     * 查询所有运力信息
     * param
     * @return
     */
    public List<Map> findAll06(String scenariosId) {
        return tempMapper.findAll05(scenariosId);
    }

}
