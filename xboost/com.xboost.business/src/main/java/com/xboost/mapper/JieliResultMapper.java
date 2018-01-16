package com.xboost.mapper;


import com.xboost.pojo.JieliResult;
import java.util.List;
import java.util.Map;

public interface JieliResultMapper {

    void save(JieliResult jieliResult);

    List<JieliResult> findAll(String scenariosId);

    List<JieliResult> findByParam(Map<String,Object> param);

    Integer findAllCount(String scenariosId);

    Integer findCountByParam(Map<String,Object> param);

    void update(JieliResult jieliResult);

    void delete(String scenariosId);
}
