package com.xboost.controller;

import com.google.common.collect.Maps;
import com.xboost.pojo.Cost;
import com.xboost.pojo.ModelArg;
import com.xboost.service.ModelArgService;
import com.xboost.service.SolutionCostService;
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
@RequestMapping("/cost")
public class SolutionCostController {
    @Inject
    private SolutionCostService solutionCostService;
    @Inject
    private ModelArgService modelArgService;

    @RequestMapping(method = RequestMethod.GET)
    public String list() {
        return "results/cost";
    }


    public static void main(String[] args)
    {
        Cost cost = new Cost();
        System.out.println("Hello World!");

    }

    /**
     * 添加Cost信息
     * @return
     */
    @RequestMapping(value = "/addCost",method = RequestMethod.POST)
    @ResponseBody
    public String addCost(HttpServletRequest request,Cost cost) { ;
        cost.setScenariosId(ShiroUtil.getOpenScenariosId().toString());
        cost.setModelType(request.getParameter("modelType"));
        cost.setModelType(request.getParameter("plan"));
        solutionCostService.add(cost);
        return "success";
    }


    //查询成本信息
    @RequestMapping(value = "/cost.json",method = RequestMethod.GET,produces = "application/json;charset=UTF-8")
    @ResponseBody
    public Map<String,Object> load(HttpServletRequest request) {
        String scenariosId = ShiroUtil.getOpenScenariosId();
        String modelType = request.getParameter("modelType");
        String plan = request.getParameter("plan");

        Map<String,Object> param = Maps.newHashMap();
        param.put("modelType",modelType);
        param.put("scenariosId",scenariosId);
        param.put("plan",plan);

        Map<String,Object> result = Maps.newHashMap();
        List<Cost> costList = solutionCostService.findByParam(param);

        Integer sitePeopleWork = modelArgService.findSitePeopleWork(scenariosId,modelType);
        Integer distribPeopleWork = modelArgService.findDistribPeopleWork(scenariosId,modelType);

        result.put("data",costList);
        result.put("sitePeopleWork",sitePeopleWork);
        result.put("distribPeopleWork",distribPeopleWork);
        return result;
    }


    /**
     * 编辑成本信息
     * @return
     */
    @RequestMapping(value = "/edit",method = RequestMethod.POST)
    @ResponseBody
    public String edit(HttpServletRequest request,Cost cost) {
        cost.setScenariosId(ShiroUtil.getOpenScenariosId());
        cost.setModelType(request.getParameter("modelType"));
        cost.setModelType(request.getParameter("plan"));
        solutionCostService.edit(cost);
        return "success";
    }

}
