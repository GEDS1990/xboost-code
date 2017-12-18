package com.xboost.service;

import com.xboost.mapper.SolutionCostMapper;
import com.xboost.pojo.Cost;
import com.xboost.util.ExcelUtil;
import com.xboost.util.ExportUtil;
import org.apache.poi.xssf.usermodel.*;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.ServletOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/11/5 0005.
 */

@Named
@Transactional
public class SolutionCostService {

    private static Logger logger = LoggerFactory.getLogger(SolutionCostService.class);

    @Inject
    private SolutionCostMapper solutionCostMapper;

    /**
     * 新增成本基础信息
     * @param cost
     */
    public void add(Cost cost) {
        cost.setCreateTime(DateTime.now().toString("yyyy-MM-dd HH:mm"));
        solutionCostMapper.add(cost);
    }


    /**
     * 查询成本信息
     * param
     * @return
     */
    public List<Cost> findAll(String scenariosId) {
        return solutionCostMapper.findAll(scenariosId);
    }


    /**
     * 根据参数查询成本信息
     * param param
     * @return
     */
    public List<Cost> findByParam(Map<String, Object> param) {
        return solutionCostMapper.findByParam(param);
    }

    /**
     * 根据场景id查询成本信息
     * @param scenariosId
     * @return
     */
    public Cost findByScenariosId(String scenariosId) {
        return solutionCostMapper.findByScenariosId(scenariosId);
    }


    /**
     * 编辑成本信息
     * @param cost
     */
    public void editCost(Cost cost) {
        solutionCostMapper.editCost(cost);
    }

    /**
     * 根据scenariosId和siteCode编辑site info 里的cost相关信息
     * @param scenariosId,siteCode
     */
    public void editSiteInfo(String scenariosId,String siteCode) {
        solutionCostMapper.editSiteInfo(scenariosId,siteCode);
    }


}
