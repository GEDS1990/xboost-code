package com.xboost.mapper;

import com.xboost.pojo.SiteInfo;
import com.xboost.pojo.User;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/11/5 0005.
 */
public interface SiteInfoMapper {

    //  添加网点基础信息  @param siteInfo
    void save(SiteInfo siteInfo);

    //  update网点基础信息  @param siteInfo
    void update(SiteInfo siteInfo);


     // 查询所有网点  @return
    List<SiteInfo> findAll(String scenariosId);

    Long findSiteInfoCount(String scenariosId);

    // 根据网点名称查询网点信息  @param siteName @return
    List<SiteInfo> findByParam(Map<String, Object> param);
    Long findSiteInfoCountByParam(Map<String, Object> param);

    /**
     * 根据场景id查询网点编码
     * @param scenariosId
     * @return
     */
    String findSiteCode(String scenariosId);

    /**
     * 根据场景id和网点编码查询网点信息
     * @param scenariosId
     * @return
     */
    SiteInfo findSiteInfoBySiteCode(@Param("scenariosId") String scenariosId, @Param("siteCode")String siteCode);

    /**
     * 根据用户的ID查询网点信息
     * @param id 用户ID
     * @return
     */
    SiteInfo findSiteInfoById(Integer id);


    // 根据id编辑网点信息  @param siteInfo
    void editSiteInfo(SiteInfo siteInfo);


    //根据网点编码删除网点信息   * @param siteCode
    void delById(Integer id);

    // 根据网点编码siteCode获网点信息,根据curLoc和siteCode关联查询t_solution_route,t_site_info   @param param @return
    List<Map<String,Object>> findBySiteCode(Map<String, Object> param);

    // 根据网点编码siteCode获取网点数量,根据curLoc和siteCode关联查询t_solution_route,t_site_info
    Long findCountBySiteCode(Map<String, Object> param);

    // 查询全部网点信息,根据curLoc和siteCode关联查询t_solution_route,t_site_info   @param param @return
    List<Map<String,Object>> findAllBySiteCode(String scenariosId);

    // 查询全部网点数量,根据curLoc和siteCode关联查询t_solution_route,t_site_info
    Long findAllCountBySiteCode(String scenariosId);


}

