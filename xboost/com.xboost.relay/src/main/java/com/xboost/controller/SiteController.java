package com.xboost.controller;

import com.google.common.collect.Maps;
import com.xboost.pojo.SiteInfo;
import com.xboost.service.SiteInfoService;
import com.xboost.util.ShiroUtil;
import com.xboost.util.Strings;
import jxl.Cell;
import jxl.Sheet;
import org.apache.commons.lang3.StringUtils;
import jxl.Workbook;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.inject.Inject;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
    @Autowired
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
        siteInfo.setScenariosId(ShiroUtil.getOpenScenariosId());
        siteInfoService.saveSiteInfo(siteInfo);
        return "success";
    }


    //通过Excel添加网点信息
    @RequestMapping(value = "/addByExcel",method = RequestMethod.POST)
    @ResponseBody
    public String AddSiteInfoByExcel(SiteInfo siteInfo,@RequestParam MultipartFile[] file) {
        siteInfo.setScenariosId(ShiroUtil.getOpenScenariosId());
        siteInfoService.delbyScenariodId(ShiroUtil.getOpenScenariosId());
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
            param.put("keyword", "%" + searchValue + "%");
        }
        param.put("orderColumn",orderColumnName);
        param.put("orderType",orderType);
        param.put("scenariosId",ShiroUtil.getOpenScenariosId());



        Map<String,Object> result = Maps.newHashMap();

        List<SiteInfo> siteInfoList = siteInfoService.findByParam(param); //.findAllSiteInfo();
        Integer count = siteInfoService.findSiteInfoCount(ShiroUtil.getOpenScenariosId());
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
        siteInfo.setScenariosId(ShiroUtil.getOpenScenariosId());
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

    @RequestMapping(value = "/exportExcel",method = RequestMethod.GET,produces = {"application/vnd.ms-excel;charset=UTF-8"})
    @ResponseBody
    public String exportExcel(HttpServletResponse response)
    {
        response.setContentType("application/vnd.ms-excel;charset=utf-8");
        try
        {
               ServletOutputStream outputStream = response.getOutputStream();
            String fileName = new String(("Depot_Info").getBytes(), "utf-8");
            response.setCharacterEncoding("utf-8");
            response.setHeader("Content-disposition", "attachment; filename=" + fileName + ".xlsx");// 组装附件名称和格式
            String scenariosId = ShiroUtil.getOpenScenariosId();
            String[] titles = { "ID","depot ID","longitude","latitude","depot name","depot address","depot area",
                    "depot type","to Dummy Hub","night distrib","one period of time : 10 (min)",
                    "one period of time : 10 (min)","one period of time : 10 (min)",
                    "one period of time : 10 (min)","one period of time : 10 (min)",
                    "one period of time : 10 (min)","one period of time : 10 (min)"};
            String[] nextTitles = {"","","","","","","", "","","","no of truck limitation","no of truck limitation","no of didi limitation",
                    "no of didi limitation","reserve", "vehicle weight limit *","piece capacity (p) *"};
            siteInfoService.exportExcel(scenariosId,titles,nextTitles,outputStream);
                   System.out.println("outputStream:"+outputStream);
        }
        catch (IOException e)
        {
            e.printStackTrace();
            System.out.println("网络连接故障!错误信息:"+e.getMessage());
        }
        return null;
    }

}
