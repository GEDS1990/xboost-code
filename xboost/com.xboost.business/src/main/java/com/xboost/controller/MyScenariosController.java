package com.xboost.controller;

import com.google.common.collect.Maps;
import com.xboost.pojo.Scenarios;
import com.xboost.pojo.User;
import com.xboost.service.ScenariosService;
import com.xboost.util.ShiroUtil;
import com.xboost.util.Strings;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/MyScenarios")
public class MyScenariosController {
    /**
     * 跳转MyScenarios页面
     */
    @RequestMapping(method = RequestMethod.GET)
    public String MyScenarios() {
        return "MyScenarios/MyScenarios";
    }

    @Inject
    private ScenariosService scenariosService;
    private static Logger logger = LoggerFactory.getLogger(ScenariosService.class);

    /**
     * 添加场景信息
     * @return
     */
    @RequestMapping(value = "/add",method = RequestMethod.POST)
    @ResponseBody
    public String add(Scenarios scenario) {
        scenariosService.save(scenario);
        return "success";
    }


    //通过Excel添加场景信息
    @RequestMapping(value = "/addByExcel",method = RequestMethod.POST)
    @ResponseBody
    public String AddByExcel(Scenarios scenario,@RequestParam MultipartFile[] file) {
        scenariosService.addByExcel(scenario,file);
        return "/ScenariosName/MyScenarios";
    }


    //查询场景信息
    @RequestMapping(value = "/scenarios.json",method = RequestMethod.GET,produces = "application/json;charset=UTF-8")
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



        Map<String,Object> result = Maps.newHashMap();

        List<Scenarios> scenariosList = scenariosService.findByParam(param); //.findAllSiteInfo();
        Integer count = scenariosService.findAllCount();
        Integer filteredCount = scenariosService.findCountByParam(param);

        result.put("draw",draw);
        result.put("recordsTotal",count); //总记录数
        result.put("recordsFiltered",filteredCount); //过滤出来的数量
        result.put("data",scenariosList);
        return result;
    }

    /**
     * 根据ID获取场景信息
     */
    @RequestMapping(value = "/scen.json",method = RequestMethod.GET)
    @ResponseBody
    public Scenarios getScenarios(Integer id) {
        return scenariosService.findById(id);
    }

    /**
     * 编辑场景信息
     * @return
     */
    @RequestMapping(value = "/edit",method = RequestMethod.POST)
    @ResponseBody
    public String edit(Scenarios scenario) {
        scenariosService.edit(scenario);

        return "success";
    }


    /**
     * 删除场景信息
     */
    @RequestMapping(value = "/del",method = RequestMethod.POST)
    @ResponseBody
    public String delById(Integer id) {
        scenariosService.delById(id);
        return "success";
    }

    /**
     * 打开场景
     * 将场景id设置到session中
     */
    @RequestMapping(value = "/open",method = RequestMethod.POST)
    @ResponseBody
    public String openScenariosById(String id) {
        String result = ShiroUtil.setOpenScenariosId(id);
        if(result.equals("success")){
            return "success";
        }else{
            return "fail";
        }
    }

}

