package com.xboost.controller;

import com.google.common.collect.Maps;
import com.xboost.pojo.DemandInfo;
import com.xboost.pojo.SiteInfo;
import com.xboost.service.DemandInfoService;;
import com.xboost.util.Strings;
import org.apache.commons.lang3.StringUtils;
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

/**
 * Created by Administrator on 2017/11/12 0005.
 */

@Controller
@RequestMapping("/demandInfo")
public class DemandInfoController {
    @Inject
    private DemandInfoService demandInfoService;

    @RequestMapping(method = RequestMethod.GET)
    public String list() {
        return "ScenariosName/Demands";
    }

    /**
     * 新增需求信息
     * @return
     */
    @RequestMapping(value = "/add",method = RequestMethod.POST)
    @ResponseBody
    public String addDemandInfo(DemandInfo demandInfo) {
        demandInfoService.addDemandInfo(demandInfo);
        return "success";
    }

    //通过Excel添加需求信息
    @RequestMapping(value = "/addByExcel",method = RequestMethod.POST)
    @ResponseBody
    public String AddDemandInfoByExcel(DemandInfo demandInfo, @RequestParam MultipartFile[] file) {
        demandInfoService.addDemandInfoByExcel(demandInfo,file);
        return "redirect:/siteInfo";
    }

    //查询所有需求信息
    @RequestMapping(value = "/demandInfo.json",method = RequestMethod.GET,produces = "application/json;charset=UTF-8")
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

        List<DemandInfo> demandInfoList = demandInfoService.findByParam(param); //.findAll();
        Integer count = demandInfoService.findAllCount();
        Integer filteredCount = demandInfoService.findCountByParam(param);

        result.put("draw",draw);
        result.put("recordsTotal",count); //总记录数
        result.put("recordsFiltered",filteredCount); //过滤出来的数量
        result.put("data",demandInfoList);
        return result;
    }

    /**
     * 根据ID获取需求信息
     */
    @RequestMapping(value = "/demandInfoById.json",method = RequestMethod.GET)
    @ResponseBody
    public DemandInfo getDemandInfo(Integer id) {
        return demandInfoService.findById(id);
    }

    /**
     * 编辑需求信息
     * @return
     */
    @RequestMapping(value = "/edit",method = RequestMethod.POST)
    @ResponseBody
    public String editDemandInfo(DemandInfo demandInfo) {
        demandInfoService.editDemandInfo(demandInfo);
        return "success";
    }


    /**
     * 删除需求信息
     */
    @RequestMapping(value = "/del",method = RequestMethod.POST)
    @ResponseBody
    public String delById(Integer id) {
        demandInfoService.delById(id);
        return "success";
    }

}
