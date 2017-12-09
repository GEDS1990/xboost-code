package com.xboost.controller;

import com.google.common.collect.Maps;
import com.xboost.service.*;
import com.xboost.util.ShiroUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.inject.Inject;
import java.util.Map;

@Controller
@RequestMapping("/ScenariosName")
public class ScenariosNameController {
    @Inject
    private MyScenariosService myScenariosService;
    @Inject
    SiteInfoService siteInfoService;
    @Inject
    SiteDistService siteDistService;
    @Inject
    CarService transportService;
    @Inject
    DemandInfoService demandInfoService;

    /**
     * 跳转ScenariosName页面
     */
    @RequestMapping(method = RequestMethod.GET)
    public String ScenariosName() {
        return "ScenariosName/ScenariosName";
    }

    /*
    * 跳转Conditions.jsp页面
    * */
    @RequestMapping(value = "/Conditions",method = RequestMethod.GET)
    public String Create() {
        return "ScenariosName/Conditions";
    }

    /*
    * 跳转Conditions.jsp页面
    * */
    @RequestMapping(value = "/settingsOverview.json",method = RequestMethod.GET)
    @ResponseBody
    public Map<String,Object> settingsOverview(Integer id) {

        Integer siteCounter1 = siteInfoService.findSiteInfoCount(ShiroUtil.getOpenScenariosId());
        Integer tranCounter1 = transportService.findAllCount(ShiroUtil.getOpenScenariosId());
        Integer demandCounter1 = demandInfoService.findAllCount(ShiroUtil.getOpenScenariosId());
        Integer siteDistCounter1 = siteDistService.findAllCount(ShiroUtil.getOpenScenariosId());
        Float farthestDist1 = siteDistService.findFarthestDist(ShiroUtil.getOpenScenariosId());
        String siteCounter;
        String tranCounter;
        String demandCounter;
        String farthestDist;
        Map<String,Object> result = Maps.newHashMap();

        //判断是否有场景数据
        //判断网点信息是否有数据
        if(siteCounter1 > 0){
            siteCounter = siteCounter1.toString();
        }
        else{
            siteCounter = "--";
        }
        //判断车辆信息是否有数据
        if(tranCounter1 > 0){
            tranCounter = tranCounter1.toString();
        }
        else{
            tranCounter = "--";
        }
       //判断需求信息是否有数据
        if(demandCounter1 > 0){
            demandCounter = demandCounter1.toString();
        }
        else{
            demandCounter = "--";
        }
        //判断网点距离是否有数据
        if(siteDistCounter1 > 0){
            farthestDist = farthestDist1.toString();
        }
        else{
            farthestDist = "--";
        }

        result.put("siteCounter",siteCounter);
        result.put("tranCounter",tranCounter); //总记录数
        result.put("demandsCounter",demandCounter); //过滤出来的数量
        result.put("farthestDist",farthestDist);
        return result;
    }
}
