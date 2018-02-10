package com.xboost.controller;

import com.google.common.collect.Maps;
import com.xboost.pojo.ModelArgs;
import com.xboost.service.ModelArgsService;
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
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/11/5 0005.
 */

@Controller
@RequestMapping("/modelArgs")
public class ModelArgsController {
    @Inject
    private ModelArgsService modelArgsService;

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
    public String addModelArg(ModelArgs modelArgs) {
        modelArgs.setScenariosId(ShiroUtil.getOpenScenariosId());
        modelArgsService.add(modelArgs);
        return "success";
    }

//    //通过Excel添加网点信息
//    @RequestMapping(value = "/addByExcel",method = RequestMethod.POST)
//    @ResponseBody
//    public String AddByExcel(ModelArgs modelArgs, @RequestParam MultipartFile[] file) {
//        modelArgs.setScenariosId(ShiroUtil.getOpenScenariosId());
//        modelArgsService.addByExcel(modelArgs,file);
//        return "/ScenariosName/Parameters";
//    }


    //查询所有模型整体参数信息
    @RequestMapping(value = "/modelArgs.json",method = RequestMethod.GET,produces = "application/json;charset=UTF-8")
    @ResponseBody
    public Map<String,Object> load(HttpServletRequest request) {
        String modelType = request.getParameter("modelType");
        String scenariosId = ShiroUtil.getOpenScenariosId();
        Map<String,Object> result = Maps.newHashMap();

        List<ModelArgs> modelArgsList = modelArgsService.findAllModelArg(scenariosId,modelType); //.findAll();
        result.put("data",modelArgsList);
        return result;
    }

    /**
     * 根据ID获取模型整体参数信息
     */
    @RequestMapping(value = "/modelArgById.json",method = RequestMethod.GET)
    @ResponseBody
    public ModelArgs getModelArgs(Integer id) {
        return modelArgsService.findById(id);
    }

    /**
     * 编辑模型整体参数信息
     * @return
     */
    @RequestMapping(value = "/edit",method = RequestMethod.POST)
    @ResponseBody
    public String editModelArg(ModelArgs modelArgs) {
        modelArgs.setScenariosId(ShiroUtil.getOpenScenariosId());
        modelArgsService.edit(modelArgs);
        return "success";
    }

}