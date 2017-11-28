package com.xboost.controller;

import com.google.common.collect.Maps;
import com.xboost.pojo.Scenarios;
import com.xboost.pojo.ScenariosCategory;
import com.xboost.service.MyScenariosService;
import com.xboost.util.ShiroUtil;
import com.xboost.util.Strings;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
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
    private MyScenariosService myScenariosService;
    private static Logger logger = LoggerFactory.getLogger(MyScenariosService.class);

    /**
     * 添加场景信息
     * @return
     */
    @RequestMapping(value = "/add",method = RequestMethod.POST)
    @ResponseBody
    public String add(Scenarios scenario) {
        myScenariosService.save(scenario);
        return "success";
    }


    //通过Excel添加场景信息
    @RequestMapping(value = "/addByExcel",method = RequestMethod.POST)
    @ResponseBody
    public String AddByExcel(Scenarios scenario,@RequestParam MultipartFile[] file) {
        myScenariosService.addByExcel(scenario,file);
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
        param.put("userId",ShiroUtil.getCurrentUserId());



        Map<String,Object> result = Maps.newHashMap();

        List<Scenarios> scenariosList = myScenariosService.findByParam(param); //.findAllSiteInfo();
        Integer count = myScenariosService.findAllCount(ShiroUtil.getCurrentUserId());
        Integer filteredCount = myScenariosService.findCountByParam(param);

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
        return myScenariosService.findById(id);
    }

    /**
     * 编辑场景信息
     * @return
     */
    @RequestMapping(value = "/edit",method = RequestMethod.POST)
    @ResponseBody
    public String edit(Scenarios scenario) {
        myScenariosService.edit(scenario);

        return "success";
    }


    /**
     * 删除场景信息
     */
    @RequestMapping(value = "/del",method = RequestMethod.POST)
    @ResponseBody
    public String delById(Integer id) {
        myScenariosService.delById(id);
        return "success";
    }

    /**
     * 打开场景
     * 将场景id设置到session中
     */
    @RequestMapping(value = "/open",method = RequestMethod.POST)
    @ResponseBody
    public String openScenariosById(String openScenariosId,String openScenariosName) {
        String result = ShiroUtil.setOpenScenariosId(openScenariosId);
        String result1 = ShiroUtil.setOpenScenariosName(openScenariosName);

        if(result.equals("success") && result1.equals("success")){
            myScenariosService.updateOpenTime(openScenariosId);
            return "success";
        }else{
            return "fail";
        }
    }

    /**
     * 添加场景信息类别
     * @return
     */
    @RequestMapping(value = "/addCategory",method = RequestMethod.POST)
    @ResponseBody
    public String addCategory(ScenariosCategory category) {
        myScenariosService.addCategory(category);
        return "success";
    }

    /**
     * 查询场景信息类别
     * @return
     */
    @RequestMapping(value = "/category.json",method = RequestMethod.GET)
    @ResponseBody
    public List<ScenariosCategory> findCategory(Integer userId) {
        return myScenariosService.findCategory(userId);
    }


}

