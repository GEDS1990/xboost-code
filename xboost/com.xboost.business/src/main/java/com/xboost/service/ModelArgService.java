package com.xboost.service;

import com.xboost.mapper.ModelArgMapper;
import com.xboost.pojo.ModelArg;
import com.xboost.util.ExcelUtil;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.inject.Inject;
import javax.inject.Named;
import java.io.File;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/11/5 0005.
 */

@Named
@Transactional
public class ModelArgService {
    private static Logger logger = LoggerFactory.getLogger(SiteInfoService.class);

    @Inject
    private ModelArgMapper modelArgMapper;

    /**
     * 新增模型整体参数信息
     * @param modelArg
     */
    public void addModelArg(ModelArg modelArg) {
        modelArg.setCreateTime(DateTime.now().toString("yyyy-MM-dd HH:mm"));

        modelArgMapper.add(modelArg);

    }


    /**
     * 查询所有模型整体参数信息
     * param
     * @return
     */
    public List<ModelArg> findAllModelArg() {
        return modelArgMapper.findAll();
    }

    /**
     * 获取模型整体参数信息总数量
     * @return
     */
    public Integer findAllCount() {
        return modelArgMapper.findAllCount().intValue();
    }

    /**
     * 根据查询条件获取模型整体参数信息
     * param param
     * @return
     */
    public List<ModelArg> findByParam(Map<String, Object> param) {
        return modelArgMapper.findByParam(param);
    }

    /**
     * 根据查询条件获取模型整体参数信息的数量
     * @param param
     * @return
     */
    public Integer findCountByParam(Map<String, Object> param) {
        return modelArgMapper.findCountByParam(param).intValue();
    }


    /**
     * 根据用户的ID查询模型整体参数信息
     * @param id 用户ID
     * @return
     */
    public ModelArg findById(Integer id) {
        return modelArgMapper.findById(id);
    }


    /**
     * 编辑模型整体参数信息
     * @param modelArg
     */
    public void editModelArg(ModelArg modelArg) {

        modelArgMapper.editModelArg(modelArg);
    }

    /**
     * 根据id删除模型整体参数信息
     * @param id
     */
    public void delById(Integer id) {

        modelArgMapper.delById(id);
    }

}
