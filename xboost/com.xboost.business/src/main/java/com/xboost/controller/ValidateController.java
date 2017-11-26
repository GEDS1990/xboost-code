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
import sun.rmi.runtime.Log;

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
    public void Validate() {
        List<SiteInfo> siteInfoList = siteInfoService.findAllSiteInfo(ShiroUtil.getOpenScenariosId());
        List<SiteDist> siteDistList = siteDistService.findAllSiteDist(ShiroUtil.getOpenScenariosId());
        List<DemandInfo> demandInfoList = demandInfoService.findAll(ShiroUtil.getOpenScenariosId());
        List<Transportation> transportationList = transportService.findAll(ShiroUtil.getOpenScenariosId());
        List<ModelArg> modelArgList = modelArgService.findAllModelArg(ShiroUtil.getOpenScenariosId());
        String result="";
        int flag=0;
        Logger logger = LoggerFactory.getLogger(ValidateController.class);
        //验证网点信息
        result = "Validating Depots Info......";
        systemWebSocketHandler.sendMessageToUser( new TextMessage(result));
        for(int i=0;i<siteInfoList.size();i++){
            SiteInfo siteInfo = siteInfoList.get(i);
            if(Strings.isEmpty(siteInfo.getSiteCode())){
                flag = flag + 1;
                result = siteInfo.getSiteCode()+":ID is wrong. Because it's empty.\n";
                logger.info(result);
                systemWebSocketHandler.sendMessageToUser( new TextMessage(result));
            }
            if(Strings.isEmpty(siteInfo.getSiteLongitude())){
                flag = flag + 1;
                result = siteInfo.getSiteCode()+":longitude is wrong. Because it's empty.\n";
                logger.info(result);
                systemWebSocketHandler.sendMessageToUser( new TextMessage(result));
            }
            if(Strings.isEmpty(siteInfo.getSiteLatitude())){
                flag = flag + 1;
                result = siteInfo.getSiteCode()+":latitude is wrong. Because it's empty.\n";
                systemWebSocketHandler.sendMessageToUser( new TextMessage(result));
            }
            if(Strings.isEmpty(siteInfo.getSiteName())){
                flag = flag + 1;
                result = siteInfo.getSiteCode()+":name is wrong. Because it's empty.\n";
                systemWebSocketHandler.sendMessageToUser( new TextMessage(result));
            }
            if(Strings.isEmpty(siteInfo.getSiteAddress())){
                flag = flag + 1;
                result = siteInfo.getSiteCode()+":address is wrong. Because it's empty.\n";
                logger.info(result);
                systemWebSocketHandler.sendMessageToUser( new TextMessage(result));
            }
            if(Strings.isEmpty(siteInfo.getSiteArea())){
                flag = flag + 1;
                result = siteInfo.getSiteCode()+":area is wrong. Because it's empty.\n";
                systemWebSocketHandler.sendMessageToUser( new TextMessage(result));
            }
            if(Strings.isEmpty(siteInfo.getSiteType())){
                flag = flag + 1;
                result = siteInfo.getSiteCode()+":type is wrong. Because it's empty.\n";
                systemWebSocketHandler.sendMessageToUser( new TextMessage(result));
            }
            if(Strings.isEmpty(siteInfo.getDistribCenter())){
                flag = flag + 1;
                result = siteInfo.getSiteCode()+":distrib.center is wrong. Because it's empty.\n";
                systemWebSocketHandler.sendMessageToUser( new TextMessage(result));
            }
            if(Strings.isEmpty(siteInfo.getCarNum())){
                flag = flag + 1;
                result = siteInfo.getSiteCode()+":vehicle quantity limit is wrong. Because it's empty.\n";
                systemWebSocketHandler.sendMessageToUser( new TextMessage(result));
            }
            if(Strings.isEmpty(siteInfo.getLargeCarModel())){
                flag = flag + 1;
                result = siteInfo.getSiteCode()+":vehicle weight limit  is wrong. Because it's empty.\n";
                systemWebSocketHandler.sendMessageToUser( new TextMessage(result));
            }
            if(Strings.isEmpty(siteInfo.getMaxOperateNum())){
                flag = flag + 1;
                result = siteInfo.getSiteCode()+":piece capacity is wrong. Because it's empty.\n";
                systemWebSocketHandler.sendMessageToUser( new TextMessage(result));
            }

        }

        /**
         * 验证网点距离信息
         */
        result = "Validating Depots Distance......";
        systemWebSocketHandler.sendMessageToUser( new TextMessage(result));
        for(int i=0;i<siteDistList.size();i++){
            SiteDist siteDist = siteDistList.get(i);
            if(Strings.isEmpty(siteDist.getSiteCollect())){
                flag = flag + 1;
                result = siteDist.getSiteCollect()+":pickup depot is wrong. Because it's empty.\n";
                systemWebSocketHandler.sendMessageToUser( new TextMessage(result));
            }
            if(Strings.isEmpty(siteDist.getSiteDelivery())){
                flag = flag + 1;
                result = siteDist.getSiteCollect()+":delivery depot is wrong. Because it's empty.\n";
                systemWebSocketHandler.sendMessageToUser( new TextMessage(result));
            }
            if (Strings.isEmpty(siteDist.getCarDistance())) {
                flag = flag + 1;
                result = siteDist.getSiteCollect() + ":transportation distance(km) is wrong. Because it's empty.\n";
                systemWebSocketHandler.sendMessageToUser( new TextMessage(result));
            }
            if(Strings.isEmpty(siteDist.getDurationNightDelivery())){
                flag = flag + 1;
                result = siteDist.getSiteCollect()+":night transportation time(min) is wrong. Because it's empty.\n";
            }
        }

        /**
         * 验证需求信息
         */
        result = "Validating Demands......";
        systemWebSocketHandler.sendMessageToUser( new TextMessage(result));
        for(int i=0;i<demandInfoList.size();i++){
            DemandInfo demandInfo = demandInfoList.get(i);
            if(Strings.isEmpty(demandInfo.getDate())){
                flag = flag + 1;
                result = demandInfo.getDate()+":Date is wrong. Because it's empty.\n";
                systemWebSocketHandler.sendMessageToUser( new TextMessage(result));
            }
            if(Strings.isEmpty(demandInfo.getSiteCodeCollect())){
                flag = flag + 1;
                result = demandInfo.getDate()+":pickup depot is wrong. Because it's empty.\n";
                systemWebSocketHandler.sendMessageToUser( new TextMessage(result));
            }
            if (Strings.isEmpty(demandInfo.getSiteCodeDelivery())) {
                flag = flag + 1;
                result = demandInfo.getDate() + ":delivery depot is wrong. Because it's empty.\n";
                systemWebSocketHandler.sendMessageToUser( new TextMessage(result));
            }
            if(Strings.isEmpty(demandInfo.getProductType())){
                flag = flag + 1;
                result = demandInfo.getDate()+":product type is wrong. Because it's empty.\n";
            }
            if(Strings.isEmpty(demandInfo.getDurationStart())){
                flag = flag + 1;
                result = demandInfo.getDate()+":time start is wrong. Because it's empty.\n";
            }
            if(Strings.isEmpty(demandInfo.getDurationEnd())){
                flag = flag + 1;
                result = demandInfo.getDate()+":time end is wrong. Because it's empty.\n";
            }
            if(Strings.isEmpty(demandInfo.getWeight())){
                flag = flag + 1;
                result = demandInfo.getDate()+":weight is wrong. Because it's empty.\n";
            }
            if(Strings.isEmpty(demandInfo.getVotes())){
                flag = flag + 1;
                result = demandInfo.getDate()+":piece is wrong. Because it's empty.\n";
            }
            if(Strings.isEmpty(demandInfo.getAgeing())){
                flag = flag + 1;
                result = demandInfo.getDate()+":effectiveness is wrong. Because it's empty.\n";
            }
        }

        /**
         * 验证整体模型参数
         */
        result = "Validating Parameters......";
        systemWebSocketHandler.sendMessageToUser( new TextMessage(result));
        for(int i=0;i<modelArgList.size();i++){
            ModelArg modelArg = modelArgList.get(i);
            if(Strings.isEmpty(modelArg.getParameterName())){
                flag = flag + 1;
                result = modelArg.getParameterName()+":parameter name is wrong. Because it's empty.\n";
                systemWebSocketHandler.sendMessageToUser( new TextMessage(result));
            }
            if(Strings.isEmpty(modelArg.getData())){
                flag = flag + 1;
                result = modelArg.getParameterName()+":data is wrong. Because it's empty.\n";
                systemWebSocketHandler.sendMessageToUser( new TextMessage(result));
            }
        }


        String success = "Validation is successful,and now you can run the simulation.";
        String fail = "Validation is completed,please check the error information.";
        String delimiter = "\n---------------------------------------------------------------------\n";

        if(flag == 0) {

            result = DateTime.now().toString("yyyy-MM-dd HH:mm:ss，")+ success;
            systemWebSocketHandler.sendMessageToUser( new TextMessage(delimiter));
            systemWebSocketHandler.sendMessageToUser( new TextMessage(result));
        }
        else{
            result = DateTime.now().toString("yyyy-MM-dd HH:mm:ss，") + fail;
            systemWebSocketHandler.sendMessageToUser( new TextMessage(delimiter));
            systemWebSocketHandler.sendMessageToUser( new TextMessage(result));
        }

    }



}
