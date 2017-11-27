package com.xboost.mapper;

import com.mckinsey.sf.data.Car;
import com.xboost.pojo.TimeWindow;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/11/5 0005.
 */
public interface CarMapper {

    //  添加运力信息  @param transport
    void save(Car car);

    //  添加TimeWindow  @param tw
    void saveTimeWindow(TimeWindow tw);

    Car[] findCarByParam(Map<String, Object> param);

    // 查询所有运力信息  @return
    List<Car> findAll(String scenariosId);

    Long findAllCount(String scenariosId);

    // 根据参数查询运力信息  @param param @return
    List<Car> findByParam(Map<String, Object> param);

    Long findCountByParam(Map<String, Object> param);

    /**
     * 根据ID查询运力信息
     * @param id
     * @return
     */
    Car findById(Integer id);


    // 根据id编辑运力信息  @param transport
    void editTransport(Car transport);


    //根据网点编码删除运力信息   * @param id
    void delById(Integer id);

}

