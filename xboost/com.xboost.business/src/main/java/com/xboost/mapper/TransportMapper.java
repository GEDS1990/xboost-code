package com.xboost.mapper;

import com.xboost.pojo.Transportation;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/11/5 0005.
 */
public interface TransportMapper {

    //  添加运力信息  @param transport
    void save(Transportation transport);


     // 查询所有运力信息  @return
    List<Transportation> findAll(String scenariosId);

    Long findAllCount(String scenariosId);

    // 根据参数查询运力信息  @param param @return
    List<Transportation> findByParam(Map<String, Object> param);
    Long findCountByParam(Map<String, Object> param);

    /**
     * 根据ID查询运力信息
     * @param id
     * @return
     */
    Transportation findById(Integer id);


    // 根据id编辑运力信息  @param transport
    void editTransport(Transportation transport);


    //根据网点编码删除运力信息   * @param id
    void delById(Integer id);

}

