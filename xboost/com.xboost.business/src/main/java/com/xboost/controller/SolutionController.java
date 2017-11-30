package com.xboost.controller;

import com.xboost.pojo.Route;
import com.xboost.service.SolutionService;
import com.xboost.util.ShiroUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.inject.Inject;

/**
 * Created by Administrator on 2017/11/5 0005.
 */

@Controller
@RequestMapping("/solution")
public class SolutionController {
    @Inject
    private SolutionService solutionService;

    @RequestMapping(method = RequestMethod.GET)
    public String list() {
        return "results/solution";
    }

    /**
     * 添加route信息
     * @return
     */
    @RequestMapping(value = "/addRoute",method = RequestMethod.POST)
    @ResponseBody
    public String addRoute(Route route) {
        route.setScenariosId(ShiroUtil.getOpenScenariosId().toString());
        solutionService.addRoute(route);
        return "success";
    }
}
