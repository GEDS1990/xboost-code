package com.xboost.mapper;

import com.xboost.pojo.Scenarios;
import org.apache.ibatis.annotations.Param;
import org.joda.time.DateTime;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/11/5 0005.
 */
public interface MyScenariosMapper {

    //  添加场景信息  @param scenario
    void save(Scenarios scenario);


     // 查询所有场景信息  @return
    List<Scenarios> findAll(Integer userId);

    Long findAllCount(Integer userId);

    // 根据参数查询场景信息  @param param @return
    List<Scenarios> findByParam(Map<String, Object> param);
    Long findCountByParam(Map<String, Object> param);

    /**
     * 根据ID查询场景信息
     * @param id
     * @return
     */
    Scenarios findById(Integer id);


    // 根据id编辑场景信息  @param scenario
    void edit(Scenarios scenario);


    //根据id删除场景信息   * @param id
    void delById(Integer id);

    /**
     *
     * @param openScenariosId
     * @return
     */
    void updateOpenTime(@Param("openScenariosId") String openScenariosId,@Param("openTime") String openTime);


}
