package com.xboost.controller;

import com.mckinsey.sf.data.Car;
import com.xboost.pojo.Configuration;
import com.xboost.service.*;
import com.xboost.util.CascadeModelUtil;
import com.xboost.util.ShiroUtil;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2017/11/10 0010.
 */
@Controller
@RequestMapping("/cascade")
public class CascadeController {

    @Inject
    CascadeService cascadeService;

    @Inject
    ConfigurationService configurationService;

    @Inject
    CarService carService;

    @Inject
    DemandInfoService demandInfoService;

    @Inject
	private SiteDistService siteDistService;

    @RequestMapping(value="/runSilumate",method = RequestMethod.POST)
    @ResponseBody
    public Map<String,Object> docascade(Map input){
//        cascadeService.saveNewfile(file);
        //查询Confiuration表数据
        Map<String, Object> param = new HashMap<String,Object>();
        Configuration config = new Configuration();
        Car[] carlist = null;
        //传入场景id
        param.put("scenariosId", ShiroUtil.getOpenScenariosId());
//        config =configurationService.findConfigByParam(param);
        config.setDistMode(1);
        config.setLoadTime(10);
        config.setOptimizeIterations(500);
        //查询car_info内容并set到config
        carlist =carService.findCarByParam(param);
        for(int i=0;i<carlist.length;i++){
            config.setCarTemplates(carlist);
        }


        CascadeModelUtil cmu = new CascadeModelUtil();
        cmu.excute(config,demandInfoService,siteDistService);
        LogFactory.getLog(AccountController.class).info("input:"+input);
        return null;
    }
}
