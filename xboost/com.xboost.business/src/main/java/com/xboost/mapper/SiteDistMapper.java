package com.xboost.mapper;

import com.xboost.pojo.SiteDist;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/11/12 0012.
 */
public interface SiteDistMapper {


    //  添加网点距离信息  @param siteInfo
    void save(SiteDist siteDist);

    // 查询所有网点距离  @return
    List<SiteDist> findAll();

    Long findAllCount();

    // 根据网点名称查询网点距离信息  @param siteName @return
    List<SiteDist> findByParam(Map<String, Object> param);

    List<SiteDist> findSiteDistByScenariosId(Map<String, Object> param);

    Long findCountByParam(Map<String, Object> param);

    /**
     * 根据用户的ID查询网点距离信息
     * @param id 用户ID
     * @return
     */
    SiteDist findById(Integer id);


    // 根据id编辑网点距离信息  @param siteInfo
    void editSiteDist(SiteDist siteDist);


    //根据网点编码删除网点距离信息   * @param siteCode
    void delById(Integer id);

}
