package com.xboost.controller;

import com.google.common.collect.Maps;
import com.xboost.pojo.ModelArg;
import com.xboost.pojo.SiteDist;
import com.xboost.pojo.SiteInfo;
import com.xboost.pojo.Transportation;
import com.xboost.pojo.DemandInfo;
import com.xboost.service.SiteInfoService;
import com.xboost.service.SiteDistService;
import com.xboost.service.DemandInfoService;
import com.xboost.service.TransportService;
import com.xboost.service.ModelArgService;
import com.xboost.util.ShiroUtil;
import com.xboost.util.Strings;
import com.xboost.websocket.SystemWebSocketHandler;
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
import org.springframework.web.socket.TextMessage;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/11/5 0005.
 */

@Controller
@RequestMapping("/simualte")
public class ValidateController {
    @Inject
    private SiteInfoService siteInfoService;
    @Inject
    private SiteDistService siteDistService;
    @Inject
    private DemandInfoService demandInfoService;
    @Inject
    private TransportService transportService;
    @Inject
    private ModelArgService modelArgService;

    @RequestMapping(method = RequestMethod.GET)
    public String list() {
        return "ScenariosName/SimualtValidate";
    }

    private static Logger logger = LoggerFactory.getLogger(SiteInfoService.class);

    private static SystemWebSocketHandler systemWebSocketHandler = new SystemWebSocketHandler();
    /**
     * 校验场景输入数据
     * @return
     */
    @RequestMapping(value = "/Validate",method = RequestMethod.POST)
    @ResponseBody
    public String Validate() {
        List<SiteInfo> siteInfoList = siteInfoService.findAllSiteInfo(ShiroUtil.getOpenScenariosId());
        List<SiteDist> siteDistList = siteDistService.findAllSiteDist(ShiroUtil.getOpenScenariosId());
        List<DemandInfo> demandInfoList = demandInfoService.findAll(ShiroUtil.getOpenScenariosId());
        List<Transportation> transportationList = transportService.findAll(ShiroUtil.getOpenScenariosId());
        List<ModelArg> modelArgList = modelArgService.findAllModelArg(ShiroUtil.getOpenScenariosId());
        String result="";
        int flag=0;
        //验证网点信息
        result = result + "Validating Depots Info......";
        for(int i=0;i<siteInfoList.size();i++){
            SiteInfo siteInfo = siteInfoList.get(i);
            if("".equals(siteInfo.getSiteCode())||" ".equals(siteInfo.getSiteCode())){
                flag = flag + 1;
                result = result + siteInfo.getSiteCode()+"ID is wrong. Because it's null.\n";
            }
            if("".equals(siteInfo.getSiteLongitude())||" ".equals(siteInfo.getSiteLongitude())){
                flag = flag + 1;
                result = result + siteInfo.getSiteCode()+"longitude is wrong. Because it's null.\n";
            }
            if("".equals(siteInfo.getSiteLatitude())||" ".equals(siteInfo.getSiteLatitude())){
                flag = flag + 1;
                result = result + siteInfo.getSiteCode()+"latitude is wrong. Because it's null.\n";
            }
            if("".equals(siteInfo.getSiteName())||" ".equals(siteInfo.getSiteName())){
                flag = flag + 1;
                result = result + siteInfo.getSiteCode()+"name is wrong. Because it's null.\n";
            }
            if("".equals(siteInfo.getSiteAddress())||" ".equals(siteInfo.getSiteAddress())){
                flag = flag + 1;
                result = result + siteInfo.getSiteCode()+"address is wrong. Because it's null.\n";
            }
            if("".equals(siteInfo.getSiteArea())||" ".equals(siteInfo.getSiteArea())){
                flag = flag + 1;
                result = result + siteInfo.getSiteCode()+"area is wrong. Because it's null.\n";
            }
            if("".equals(siteInfo.getSiteType())||" ".equals(siteInfo.getSiteType())){
                flag = flag + 1;
                result = result + siteInfo.getSiteCode()+"type is wrong. Because it's null.\n";
            }
            if("".equals(siteInfo.getDistribCenter())||" ".equals(siteInfo.getDistribCenter())){
                flag = flag + 1;
                result = result + siteInfo.getSiteCode()+"distrib.center is wrong. Because it's null.\n";
            }
            if("".equals(siteInfo.getCarNum())||" ".equals(siteInfo.getCarNum())){
                flag = flag + 1;
                result = result + siteInfo.getSiteCode()+"vehicle quantity limit is wrong. Because it's null.\n";
            }
            if("".equals(siteInfo.getLargeCarModel())||" ".equals(siteInfo.getLargeCarModel())){
                flag = flag + 1;
                result = result + siteInfo.getSiteCode()+"vehicle weight limit  is wrong. Because it's null.\n";
            }
            if("".equals(siteInfo.getMaxOperateNum())||" ".equals(siteInfo.getMaxOperateNum())){
                flag = flag + 1;
                result = result + siteInfo.getSiteCode()+"piece capacity is wrong. Because it's null.\n";
            }

        }

        /**
         * 验证网点距离信息
         */
        result = result + "Validating Depots Distance......";
        for(int i=0;i<siteDistList.size();i++){
            SiteDist siteDist = siteDistList.get(i);
            if("".equals(siteDist.getSiteCollect())||" ".equals(siteDist.getSiteCollect())){
                flag = flag + 1;
                result = siteDist.getSiteCollect()+"pickup depot is wrong. Because it's null.\n";
                systemWebSocketHandler.sendMessageToUser( new TextMessage(result));
            }
            if("".equals(siteDist.getSiteDelivery())||" ".equals(siteDist.getSiteDelivery())){
                flag = flag + 1;
                result = siteDist.getSiteCollect()+"delivery depot is wrong. Because it's null.\n";
                systemWebSocketHandler.sendMessageToUser( new TextMessage(result));
            }
            try {
                if (siteDist.getCarDistance() < 10) {
                    flag = flag + 1;
                    result = result + siteDist.getSiteCollect() + "transportation distance(km) is wrong. Because it's null.\n";
                }
            }catch (Exception e){
                flag = flag + 1;
                result = result + siteDist.getSiteCollect() + "transportation distance(km) is wrong. Because it's null exception.\n";
            }
            if("".equals(siteDist.getDurationNightDelivery())||" ".equals(siteDist.getDurationNightDelivery())){
                flag = flag + 1;
                result = result + siteDist.getSiteCollect()+"night transportation time(min) is wrong. Because it's null.\n";
            }
        }

        String success = "Validation is successful,and now you can run the simulation.";
        String fail = "Validation is completed,please check the error information.";
        String delimiter = "\n---------------------------------------------------------------------\n";

        if(flag == 0) {
            return result + delimiter + DateTime.now().toString("yyyy-MM-dd HH:mm:ss，")+ success;
        }
        else{
            return result + delimiter + DateTime.now().toString("yyyy-MM-dd HH:mm:ss，") + fail;
        }

    }



}
