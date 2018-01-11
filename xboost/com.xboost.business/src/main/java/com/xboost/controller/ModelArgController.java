package com.xboost.controller;

import com.google.common.collect.Maps;
import com.xboost.pojo.ModelArg;
import com.xboost.pojo.SiteInfo;
import com.xboost.service.ModelArgService;
import com.xboost.util.ExcelUtil;
import com.xboost.util.ShiroUtil;
import com.xboost.util.Strings;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
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
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/11/5 0005.
 */

@Controller
@RequestMapping("/modelArg")
public class ModelArgController {
    @Inject
    private ModelArgService modelArgService;

    @RequestMapping(method = RequestMethod.GET)
    public String list() {
        return "ScenariosName/Patameters";
    }

    /**
     * 添加模型整体参数信息
     * @return
     */
    @RequestMapping(value = "/add",method = RequestMethod.POST)
    @ResponseBody
    public String addModelArg(ModelArg modelArg) {
        modelArg.setScenariosId(ShiroUtil.getOpenScenariosId().toString());
        modelArgService.addModelArg(modelArg);
        return "success";
    }

    //通过Excel添加网点信息
    @RequestMapping(value = "/addByExcel",method = RequestMethod.POST)
    @ResponseBody
    public String AddByExcel(ModelArg modelArg, @RequestParam MultipartFile[] file) {
        modelArg.setScenariosId(ShiroUtil.getOpenScenariosId());
        modelArgService.delByScenariosId(ShiroUtil.getOpenScenariosId());
        modelArgService.addByExcel(modelArg,file);
        return "/ScenariosName/Parameters";
    }


    //查询所有模型整体参数信息
    @RequestMapping(value = "/modelArg.json",method = RequestMethod.GET,produces = "application/json;charset=UTF-8")
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
            param.put("keyword", searchValue);
        }
        param.put("orderColumn",orderColumnName);
        param.put("orderType",orderType);
        param.put("scenariosId",ShiroUtil.getOpenScenariosId());



        Map<String,Object> result = Maps.newHashMap();

        List<ModelArg> modelArgList = modelArgService.findByParam(param); //.findAll();
        Integer count = modelArgService.findAllCount(ShiroUtil.getOpenScenariosId());
        Integer filteredCount = modelArgService.findCountByParam(param);

        result.put("draw",draw);
        result.put("recordsTotal",count); //总记录数
        result.put("recordsFiltered",filteredCount); //过滤出来的数量
        result.put("data",modelArgList);
        return result;
    }

    /**
     * 根据ID获取模型整体参数信息
     */
    @RequestMapping(value = "/modelArgById.json",method = RequestMethod.GET)
    @ResponseBody
    public ModelArg getModelArg(Integer id) {
        return modelArgService.findById(id);
    }

    /**
     * 编辑模型整体参数信息
     * @return
     */
    @RequestMapping(value = "/edit",method = RequestMethod.POST)
    @ResponseBody
    public String editModelArg(ModelArg modelArg) {
        modelArg.setScenariosId(ShiroUtil.getOpenScenariosId());
        modelArgService.editModelArg(modelArg);
        return "success";
    }


    /**
     * 删除模型整体参数信息
     */
    @RequestMapping(value = "/del",method = RequestMethod.POST)
    @ResponseBody
    public String delById(Integer id) {
        modelArgService.delById(id);
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

            String fileName = new String(("Parameters").getBytes(), "utf-8");
            response.setCharacterEncoding("utf-8");
            response.setHeader("Content-disposition", "attachment; filename=" + fileName + ".xlsx");// 组装附件名称和格式
            String scenariosId = ShiroUtil.getOpenScenariosId();
            String[] titles = { "ID","模型(1串点2接力3综合)","编码","各环节时间参数(如图)","数据","备注" };
            modelArgService.exportExcel(scenariosId,titles,outputStream);
            //       System.out.println("outputStream:"+outputStream);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return null;
    }
}
