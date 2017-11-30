package com.xboost.service;

import com.xboost.mapper.ModelArgMapper;
import com.xboost.mapper.SolutionMapper;
import com.xboost.pojo.Activity;
import com.xboost.pojo.ModelArg;
import com.xboost.pojo.Route;
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
public class SolutionService {
    private static Logger logger = LoggerFactory.getLogger(SolutionService.class);

    @Inject
    private SolutionMapper solutionMapper;

    /**
     * 新增route信息
     * @param route
     */
    public void addRoute(Route route) {
        route.setCreateTime(DateTime.now().toString("yyyy-MM-dd HH:mm"));

        solutionMapper.addRoute(route);

    }

    /**
     * 新增activity信息
     * @param activity
     */
    public void addActivity(Activity activity) {
        activity.setCreateTime(DateTime.now().toString("yyyy-MM-dd HH:mm"));

        solutionMapper.addActivity(activity);

    }

}
