package com.xboost.controller;

import com.xboost.pojo.DemandInfo;
import com.xboost.service.DemandInfoService;
import com.xboost.util.ShiroUtil;
import org.junit.runners.Parameterized;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 计费过程
 */
@Controller
@RequestMapping("/distribution")
public class SolutionDistributionController {
    @Inject
    private DemandInfoService demandInfoService;

    @RequestMapping(method = RequestMethod.GET)
    public String list() {
        return "results/distribution";
    }
    /**
     *
     * @param type
     * 0:个网点截单时间
     * 1：各网点送达时间分布
     * 2：收段达到集散点时间分布
     * 3：支线到达目的地集散点时间分布
     * 4：始发集散地OD件量分布
     * 5：始发网点OD件量分布
     * 6：始发集散点的发车分布
     * @return
     */
    @RequestMapping(method = RequestMethod.GET)
    public Object list(String type) {
//        Map<String,Object> param = new HashMap<String,Object>();
//        param.put("scenariosId", ShiroUtil.getOpenScenariosId());
        String maxmix = demandInfoService.findMinMax(ShiroUtil.getOpenScenariosId());
        int min = Integer.parseInt(maxmix.split("-")[0]);
        int max = Integer.parseInt(maxmix.split("-")[1]);
        //间隔
        int jiange = 10;
        Map<String,Object> map = new HashMap<String,Object>();
        List<DemandInfo> demandInfoList = demandInfoService.findAll(ShiroUtil.getOpenScenariosId());
        for(int i=0;i<(max-min)/jiange;i++){
            int total = 0;
            for(DemandInfo demandInfo : demandInfoList){
                int res = Integer.parseInt(demandInfo.getDurationEnd());
                if(res>=(min+jiange*i) && res<(min+jiange*(i+1))){
                    total = total + Integer.parseInt(demandInfo.getVotes());
                }
            }
            map.put(String.valueOf(min+(jiange*i))+"-"+String.valueOf(min+(jiange*(i+1))),String.valueOf(total));
        }
        return demandInfoList;
    }
}
