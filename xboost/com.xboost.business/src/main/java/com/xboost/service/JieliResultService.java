package com.xboost.service;

import com.xboost.mapper.JieliResultMapper;
import com.xboost.pojo.JieliResult;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.List;
import java.util.Map;

@Named
@Transactional
public class JieliResultService {
    /**
     * 根据查询条件获取模
     * @param param
     * @return
     */
    @Inject
    JieliResultMapper jieliResultMapper;

    public void save(JieliResult jieliResult) {
        jieliResultMapper.save(jieliResult);
    }

    public List<JieliResult> findAll(String scenariosId) {
        return jieliResultMapper.findAll(scenariosId);
    }

    public List<JieliResult> findByParam(Map<String,Object> param) {
        return jieliResultMapper.findByParam(param);
    }

    public Integer findCountByParam(Map<String,Object> param) {
        return jieliResultMapper.findCountByParam(param);
    }

    public Integer findAllCount(String scenariosId) {
        return jieliResultMapper.findAllCount(scenariosId);
    }

    public void update(JieliResult jieliResult){
        jieliResultMapper.update(jieliResult);
    }

    public void delete(String scenariosId){
        jieliResultMapper.delete(scenariosId);
    }


}
