package com.xboost.mapper;

import com.xboost.pojo.SiteInfo;
import com.xboost.pojo.User;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/11/5 0005.
 */
public interface SiteInfoMapper {

    //  添加网点基础信息  @param siteInfo
    void save(SiteInfo siteInfo);


     // 查询所有网点  @return
    List<SiteInfo> findAll();

    Long findSiteInfoCount();

    // 根据网点名称查询网点信息  @param siteName @return
    List<SiteInfo> findByParam(Map<String, Object> param);
    Long findSiteInfoCountByParam(Map<String, Object> param);

    /**
     * 根据用户的ID查询网点信息
     * @param id 用户ID
     * @return
     */
    SiteInfo findSiteInfoById(Integer id);


    // 编辑网点信息  @param siteInfo
    void editSiteInfo(SiteInfo siteInfo);


    //根据网点编码删除网点信息   * @param siteCode
    void delBySiteCode(Integer id);

}

