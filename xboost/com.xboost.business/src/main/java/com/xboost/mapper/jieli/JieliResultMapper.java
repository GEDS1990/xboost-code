package com.xboost.mapper.jieli;


import com.xboost.pojo.jieli.JieliResult;
import java.util.List;
import java.util.Map;

public interface JieliResultMapper {

    void save(JieliResult jieliResult);

    List<Map<String,Object>> findByParam(Map<String,Object> param);

    Integer findAllCount(String scenariosId);

    Integer findCountByParam(Map<String,Object> param);

    void update(JieliResult jieliResult);

    void delete(String scenariosId);
}
