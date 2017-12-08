package com.xboost.controller;

import com.google.common.collect.Maps;
import com.xboost.pojo.Activity;
import com.xboost.service.SolutionActivityService;
import com.xboost.util.ShiroUtil;
import com.xboost.util.Strings;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/11/5 0005.
 */

@Controller
@RequestMapping("/activity")
public class SolutionActivityController {
    @Inject
    private SolutionActivityService solutionActivityService;

    @RequestMapping(method = RequestMethod.GET)
    public String list() {
        return "results/vehicles";
    }

    /**
     * 添加route信息
     * @return
     */
    @RequestMapping(value = "/addActivity",method = RequestMethod.POST)
    @ResponseBody
    public String addActivity(Activity activity) {
        activity.setScenariosId(ShiroUtil.getOpenScenariosId().toString());
        solutionActivityService.addActivity(activity);
        return "success";
    }


    //查询所有模型整体参数信息
    @RequestMapping(value = "/activity.json",method = RequestMethod.GET,produces = "application/json;charset=UTF-8")
    @ResponseBody
    public Map<String,Object> load(HttpServletRequest request) {

        String draw = request.getParameter("draw");
        Integer start = Integer.valueOf(request.getParameter("start"));
        Integer length = Integer.valueOf(request.getParameter("length"));
        String searchValue = request.getParameter("search[value]");
        String orderColumnIndex = request.getParameter("order[0][column]");
        String orderType = request.getParameter("order[0][dir]");
        String orderColumnName = request.getParameter("columns["+orderColumnIndex+"][name]");

        Map<String,Object> param = Maps.newHashMap();
        param.put("start",start);
        param.put("length",length);
        if(StringUtils.isNotEmpty(searchValue)) {
            param.put("keyword", "%" + Strings.toUTF8(searchValue) + "%");
        }
        param.put("orderColumn",orderColumnName);
        param.put("orderType",orderType);
        param.put("scenariosId",ShiroUtil.getOpenScenariosId());
        param.put("userId",ShiroUtil.getCurrentUserId());



        Map<String,Object> result = Maps.newHashMap();

        List<Activity> modelArgList = solutionActivityService.findByParam(param); //.findAll();
        Integer count = solutionActivityService.findAllCount(ShiroUtil.getOpenScenariosId(),String.valueOf(ShiroUtil.getCurrentUserId()));
        Integer filteredCount = solutionActivityService.findCountByParam(param);

        result.put("draw",draw);
        result.put("recordsTotal",count); //总记录数
        result.put("recordsFiltered",filteredCount); //过滤出来的数量
        result.put("data",modelArgList);
        return result;
    }

}
