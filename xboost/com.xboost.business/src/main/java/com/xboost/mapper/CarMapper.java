package com.xboost.mapper;

import com.mckinsey.sf.data.Car;
import com.mckinsey.sf.data.TimeWindow;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/11/5 0005.
 */
public interface CarMapper {

    //  添加运力信息  @param transport
    void save(Car car);

    //  update运力信息  @param transport
    void update(Car car);

    //  添加TimeWindow  @param tw
    void saveTimeWindow(TimeWindow tw);

    Car[] findCarByParam(Map<String, Object> param);

    // 查询所有运力信息  @return
    List<Car> findAll(String scenariosId);

    Long findAllCount(String scenariosId);

    // 根据参数查询运力信息  @param param @return
    List<Car> findByParam(Map<String, Object> param);

    Long findCountByParam(Map<String, Object> param);

    List<Car> findCarCost(@Param("scenariosId")String scenariosId,@Param("routeCount")String routeCount);

    /**
     * 根据ID查询运力信息
     * @param id
     * @return
     */
    Car findById(Integer id);
    /**
     * 根据ID查询运力信息
     * @param scenariosId
     * @return
     */
    void delByScenariosId(String scenariosId);

    // 根据id编辑运力信息  @param transport
    void editTransport(Car transport);


    //删除运力信息   * @param id
    void delById(Integer id);
    //根据运力信息id删除TimeWindow   * @param id
    void delTimeWindow(Integer id);


}

