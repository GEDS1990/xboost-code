package com.xboost.controller;

import com.google.common.collect.Maps;
import com.xboost.pojo.SiteInfo;
import com.xboost.service.SiteInfoService;
import com.xboost.util.Strings;
import jxl.Cell;
import jxl.Sheet;
import org.apache.commons.lang3.StringUtils;
import jxl.Workbook;
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
import java.io.*;
import java.util.ArrayList;
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
        return "ScenariosName/Conditions";
    }

    private static Logger logger = LoggerFactory.getLogger(SiteInfoService.class);

    /**
     * 添加网点基础信息
     * @return
     */
    @RequestMapping(value = "/add",method = RequestMethod.POST)
    @ResponseBody
    public String AddSiteInfo(SiteInfo siteInfo) {
        siteInfoService.saveSiteInfo(siteInfo);
        return "success";
    }


    //通过Excel添加网点信息
    @RequestMapping(value = "/addByExcel",method = RequestMethod.POST)
    @ResponseBody
    public String AddSiteInfoByExcel(SiteInfo siteInfo,@RequestParam MultipartFile[] file) {
        siteInfoService.addSiteInfoByExcel(siteInfo,file);
        return "/ScenariosName/Conditions";
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
     * 根据ID获取网点信息
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
    public String delById(Integer id) {
        siteInfoService.delById(id);
        return "success";
    }

}
