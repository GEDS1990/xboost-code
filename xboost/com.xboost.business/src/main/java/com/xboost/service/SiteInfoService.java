package com.xboost.service;

import com.xboost.exception.ForbiddenException;
import com.xboost.exception.NotFoundException;
import com.xboost.mapper.CustomerMapper;
import com.xboost.mapper.SiteInfoMapper;
import com.xboost.mapper.UserMapper;
import com.xboost.pojo.Customer;
import com.xboost.pojo.SiteInfo;
import com.xboost.pojo.User;
import com.xboost.util.ShiroUtil;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/11/5 0005.
 */

@Named
@Transactional
public class SiteInfoService {

    @Inject
    private SiteInfoMapper siteInfoMapper;

    /**
     * 保存新网点基础信息
     * @param siteInfo
     */
    public void saveAddSiteInfo(SiteInfo siteInfo) {
        siteInfo.setCreateTime(DateTime.now().toString("yyyy-MM-dd HH:mm"));

        siteInfoMapper.save(siteInfo);

    }


    /**
     * 查询所有网点信息
     * param
     * @return
     */
    public List<SiteInfo> findAllSiteInfo() {
        return siteInfoMapper.findAll();
    }

    /**
     * 获取网点的总数量
     * @return
     */
    public Integer findSiteInfoCount() {
        return siteInfoMapper.findSiteInfoCount().intValue();
    }

    /**
     * 根据网点名称查询网点信息
     * param siteName
     * @return
     */
    public List<SiteInfo> findByParam(Map<String, Object> param) {
        return siteInfoMapper.findByParam(param);
    }

    /**
     * 根据查询条件获取网点的数量
     * @param param
     * @return
     */
    public Integer findSiteInfoCountByParam(Map<String, Object> param) {
        return siteInfoMapper.findSiteInfoCountByParam(param).intValue();
    }


    /**
     * 根据用户的ID查询用户以及管理的角色
     * @param id 用户ID
     * @return
     */
    public SiteInfo findSiteInfoById(Integer id) {
        return siteInfoMapper.findSiteInfoById(id);
    }


    /**
     * 编辑网点信息
     * @param siteInfo
     */
    public void editSiteInfo(SiteInfo siteInfo) {

        siteInfoMapper.editSiteInfo(siteInfo);
    }

    /**
     * 删除网点信息
     * @param id
     */
    public void delBySiteCode(Integer id) {

        siteInfoMapper.delBySiteCode(id);
    }

}
