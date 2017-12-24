package com.xboost.mapper;

import com.xboost.pojo.DemandInfo;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/11/12 0005.
 */
public interface DemandInfoMapper {

    //新增需求信息  @param siteInfo
    void add(DemandInfo demandInfo);
	//max-min
    String findMinMax(String scenariosId);

    String findMax(String scenariosId);

    String findMin(String scenariosId);

     // 查询所有需求信息  @return
    List<DemandInfo> findAll(String scenariosId);

    //查询所有需求信息总数
    Long findAllCount(String scenariosId);

    /**
     * 根据用户的ID查询需求信息
     * @param
     * @return
     */
    DemandInfo findById(Integer id);

    // 根据查询条件获取需求信息  @param param @return
    List<DemandInfo> findByParam(Map<String, Object> param);

    // 根据查询条件获取需求信息  @param param @return
    List<DemandInfo> findByScenairoIdParam(Map<String, Object> param);

    //根据查询条件获取需求信息的数量
    Long findCountByParam(Map<String, Object> param);


    // 编辑需求信息  @param modelArg
    void editDemandInfo(DemandInfo demandInfo);


    //根据id 删除需求信息   * @param siteCode
    void delById(Integer id);

    List<Map> findarrTime(String scenariosId);

    List<String> findSiteCollect(String scenariosId);

    List<String> findSiteDelivery(String scenariosId);
}

