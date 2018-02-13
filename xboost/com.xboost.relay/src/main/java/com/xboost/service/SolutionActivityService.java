package com.xboost.service;

import com.xboost.mapper.SolutionActivityMapper;
import com.xboost.mapper.SolutionRouteMapper;
import com.xboost.pojo.Activity;
import com.xboost.pojo.Route;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/11/5 0005.
 */

@Service
public class SolutionActivityService {
    private static Logger logger = LoggerFactory.getLogger(SolutionActivityService.class);

    @Autowired
    private SolutionActivityMapper solutionActivityMapper;

    /**
     * 新增activity信息
     * @param activity
     */
    public void addActivity(Activity activity) {
        activity.setCreateTime(DateTime.now().toString("yyyy-MM-dd HH:mm"));

        solutionActivityMapper.addActivity(activity);

    }

    /**
     * 查询所有模型整体参数信息
     * param
     * @return
     */
    public List<Activity> findAllActivity(String scenariosId) {
        return solutionActivityMapper.findAll(scenariosId);
    }

    /**
     * 获取模型整体参数信息总数量
     * @return
     */
    public Integer findAllCount(String scenariosId,String userId) {
        return solutionActivityMapper.findAllCount(scenariosId,userId).intValue();
    }

    /**
     * 根据查询条件获取模型整体参数信息
     * param param
     * @return
     */
    public List<Activity> findByParam(Map<String, Object> param) {
        return solutionActivityMapper.findByParam(param);
    }

    /**
     * 根据查询条件获取模型整体参数信息的数量
     * @param param
     * @return
     */
    public Integer findCountByParam(Map<String, Object> param) {
        return solutionActivityMapper.findCountByParam(param).intValue();
    }


    /**
     * 根据用户的ID查询模型整体参数信息
     * @param id 用户ID
     * @return
     */
    public Activity findById(Integer id) {
        return solutionActivityMapper.findById(id);
    }


    /**
     * 根据id删除模型整体参数信息
     * @param scenariosId
     */
    public void delByScenariosId(Integer scenariosId) {

        solutionActivityMapper.delByScenariosId(scenariosId);
    }
}
