package com.xboost.controller;

import com.google.common.collect.Maps;
import com.xboost.pojo.SiteDist;
import com.xboost.service.SiteDistService;
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
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/siteDist")
public class SiteDistController {
    @RequestMapping(method = RequestMethod.GET)
    public String list() {
        return "ScenariosName/Distance";
    }
    private Logger logger = LoggerFactory.getLogger(SiteDistController.class);

    @Inject
    private SiteDistService siteDistService;

    @RequestMapping(value="/new",method = RequestMethod.POST)
    @ResponseBody
    public String addDist(SiteDist siteDist){

//        siteDistance.setCreateTime(DateTime.now().toString("yyyy-MM-dd HH:mm"));
        String s = siteDist.getSiteCollect();
        logger.info(s);
        logger.info("siteDistance：：：：：", siteDist.getSiteCollect().toString());
        return "success";
    }

    /**
     * 添加网点距离信息
     * @return
     */
    @RequestMapping(value = "/add",method = RequestMethod.POST)
    @ResponseBody
    public String AddDistInfo(SiteDist siteDist) {
        siteDist.setScenariosId(ShiroUtil.getOpenScenariosId());
        siteDistService.saveSiteDist(siteDist);
        return "success";
    }

    /**
     * 通过Excel添加网点距离基础信息
     * @return
     */
    @RequestMapping(value = "/addByExcel",method = RequestMethod.POST)
    @ResponseBody
    public String AddSiteDist(SiteDist siteDist,@RequestParam MultipartFile[] file) {
        siteDist.setScenariosId(ShiroUtil.getOpenScenariosId());
        siteDistService.saveSiteDist(siteDist,file);
        return "redirect:/siteDist";
    }

    //查询网点信息
    @RequestMapping(value = "/siteDist.json",method = RequestMethod.GET,produces = "application/json;charset=UTF-8")
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

        Map<String,Object> result = Maps.newHashMap();

        List<SiteDist> siteDistList = siteDistService.findByParam(param); //.findAllSiteInfo();
        Integer count = siteDistService.findAllCount(ShiroUtil.getOpenScenariosId());
        Integer filteredCount = siteDistService.findCountByParam(param);

        result.put("draw",draw);
        result.put("recordsTotal",count); //总记录数
        result.put("recordsFiltered",filteredCount); //过滤出来的数量
        result.put("data",siteDistList);
        return result;
    }

    /**
     * 根据ID获取网点信息
     */
    @RequestMapping(value = "/siteDistInfo.json",method = RequestMethod.GET)
    @ResponseBody
    public SiteDist getById(Integer id) {
        return siteDistService.findById(id);
    }

    /**
     * 编辑网点信息
     * @return
     */
    @RequestMapping(value = "/edit",method = RequestMethod.POST)
    @ResponseBody
    public String editSiteDist(SiteDist siteDist) {
        siteDist.setScenariosId(ShiroUtil.getOpenScenariosId());
        siteDistService.editSiteDist(siteDist);
        return "success";
    }


    /**
     * 删除网点距离
     */
    @RequestMapping(value = "/del",method = RequestMethod.POST)
    @ResponseBody
    public String delById(Integer id) {
        siteDistService.delById(id);
        return "success";
    }

    @RequestMapping(value = "/exportExcel",method = RequestMethod.POST)
    @ResponseBody
    public String exportExcel(HttpServletResponse response)
    {
         response.setContentType("application/vnd.ms-excel;charset=utf-8");
         try
         {
             ServletOutputStream outputStream = response.getOutputStream();
             String fileName = new String(("distance").getBytes(), "utf-8");
             response.setHeader("Content-disposition", "attachment; filename=" + fileName + ".xlsx");// 组装附件名称和格式
      //       response.setHeader("Content-disposition", "attachment; filename=distance.xlsx");
             String scenariosId = ShiroUtil.getOpenScenariosId();
             String[] titles = { "pickup depot", "delivery depot", "transportation distance(km)","night transportation time(min)" };
             siteDistService.exportExcel(scenariosId,titles, outputStream);
             }
         catch (IOException e)
         {
                 e.printStackTrace();
         }
         return null;
    }
}
