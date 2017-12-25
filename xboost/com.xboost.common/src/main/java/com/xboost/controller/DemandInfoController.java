package com.xboost.controller;

import com.google.common.collect.Maps;
import com.xboost.pojo.DemandInfo;
import com.xboost.pojo.SiteInfo;
import com.xboost.service.DemandInfoService;;
import com.xboost.util.ShiroUtil;
import com.xboost.util.Strings;
import org.apache.commons.lang3.StringUtils;
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
        //设置场景Id
        demandInfo.setScenariosId(ShiroUtil.getOpenScenariosId());
        demandInfoService.addDemandInfo(demandInfo);
        return "success";
    }

    //通过Excel添加需求信息
    @RequestMapping(value = "/addByExcel",method = RequestMethod.POST)
    @ResponseBody
    public String AddDemandInfoByExcel(DemandInfo demandInfo, @RequestParam MultipartFile[] file) {
        //设置场景Id
        demandInfo.setScenariosId(ShiroUtil.getOpenScenariosId());
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
        //设置场景ID
        param.put("scenariosId",ShiroUtil.getOpenScenariosId());

        Map<String,Object> result = Maps.newHashMap();

        List<DemandInfo> demandInfoList = demandInfoService.findByParam(param); //.findAll();
        Integer count = demandInfoService.findAllCount(ShiroUtil.getOpenScenariosId());
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
        demandInfo.setScenariosId(ShiroUtil.getOpenScenariosId());
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

    @RequestMapping(value = "/exportExcel",method = RequestMethod.GET,produces = {"application/vnd.ms-excel;charset=UTF-8"})
    @ResponseBody
    public String exportExcel(HttpServletResponse response)
    {
        response.setContentType("application/vnd.ms-excel;charset=utf-8");
        try
        {
               ServletOutputStream outputStream = response.getOutputStream();
            String fileName = new String(("Demands").getBytes(), "utf-8");
            response.setCharacterEncoding("utf-8");
            response.setHeader("Content-disposition", "attachment; filename=" + fileName + ".xlsx");// 组装附件名称和格式
            //       response.setHeader("Content-disposition", "attachment; filename=distance.xlsx");
            String scenariosId = ShiroUtil.getOpenScenariosId();
            String[] titles = { "ID","date","start depot","start time","end depot","effective end time","piece (p)",
                    "weight (kg)","product type","effectiveness"};
            demandInfoService.exportExcel(scenariosId,titles,outputStream);
            //       System.out.println("outputStream:"+outputStream);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return null;
    }

}
