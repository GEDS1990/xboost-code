package com.xboost.controller;

import com.google.common.collect.Maps;
import com.xboost.pojo.Customer;
import com.xboost.pojo.SiteInfo;
import com.xboost.pojo.User;
import com.xboost.service.CustomerService;
import com.xboost.service.SiteInfoService;
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
@RequestMapping("/siteInfo")
public class SiteController {
    @Inject
    private SiteInfoService siteInfoService;

    @RequestMapping(method = RequestMethod.GET)
    public String list() {
        return "siteInfo/siteList";
    }

    /**
     * 添加网点基础信息
     * @return
     */
    @RequestMapping(value = "/add",method = RequestMethod.POST)
    @ResponseBody
    public String AddSiteInfo(SiteInfo siteInfo) {
        siteInfoService.saveAddSiteInfo(siteInfo);
        return "success";
    }

    //查询网点信息
    @RequestMapping(value = "/siteInfo.json",method = RequestMethod.GET,produces = "application/json;charset=UTF-8")
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

        List<SiteInfo> siteInfoList = siteInfoService.findByParam(param); //.findAllSiteInfo();
        Integer count = siteInfoService.findSiteInfoCount();
        Integer filteredCount = siteInfoService.findSiteInfoCountByParam(param);

        result.put("draw",draw);
        result.put("recordsTotal",count); //总记录数
        result.put("recordsFiltered",filteredCount); //过滤出来的数量
        result.put("data",siteInfoList);
        return result;
    }

    /**
     * 根据ID获取用户信息
     */
    @RequestMapping(value = "/site.json",method = RequestMethod.GET)
    @ResponseBody
    public SiteInfo getSiteInfo(Integer id) {
        return siteInfoService.findSiteInfoById(id);
    }

    /**
     * 编辑网点信息
     * @return
     */
    @RequestMapping(value = "/edit",method = RequestMethod.POST)
    @ResponseBody
    public String editSiteInfo(SiteInfo siteInfo) {
        siteInfoService.editSiteInfo(siteInfo);
        return "success";
    }


    /**
     * 删除用户
     */
    @RequestMapping(value = "/del",method = RequestMethod.POST)
    @ResponseBody
    public String delBySiteCode(Integer id) {
        siteInfoService.delBySiteCode(id);
        return "success";
    }

}
