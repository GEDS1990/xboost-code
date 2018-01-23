package com.xboost.controller;

import com.google.common.collect.Maps;
import com.mckinsey.sf.data.Car;
import com.xboost.pojo.ModelArg;
import com.xboost.pojo.SiteDist;
import com.xboost.pojo.SiteInfo;
import com.xboost.pojo.DemandInfo;
import com.xboost.service.*;
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
import java.util.ArrayList;
import java.util.HashMap;
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
    private CarService transportService;
    @Inject
    private ModelArgService modelArgService;

    @RequestMapping(method = RequestMethod.GET)
    public String list() {
        return "ScenariosName/SimualtValidate";
    }

    private static Logger logger = LoggerFactory.getLogger(SiteInfoService.class);

    private static SystemWebSocketHandler systemWebSocketHandler = new SystemWebSocketHandler();

    public String wrongLink(String address, String name) {
        String link = "<a href=\"/" + address + "\" style=\"color:red;\">" + name + "</a>";
        return link;
    }

    /**
     * 校验场景输入数据
     * @return
     */
    @RequestMapping(value = "/Validate",method = RequestMethod.POST)
    @ResponseBody
    public String Validate() {
        ShiroUtil.clearSimulateConsole();
        List<SiteInfo> siteInfoList = siteInfoService.findAllSiteInfo(ShiroUtil.getOpenScenariosId());
        List<SiteDist> siteDistList = siteDistService.findAllSiteDist(ShiroUtil.getOpenScenariosId());
        List<DemandInfo> demandInfoList = demandInfoService.findAll(ShiroUtil.getOpenScenariosId());
        List<Car> transportationList = transportService.findAll(ShiroUtil.getOpenScenariosId());
        List<ModelArg> modelArgList = modelArgService.findAllModelArg(ShiroUtil.getOpenScenariosId());
        String scenariosId = ShiroUtil.getOpenScenariosId();
        String result="";
        int flag=0;
        Logger logger = LoggerFactory.getLogger(ValidateController.class);


        /**
         * 校验车辆信息
         */
        result = "Validating vehicles...";
        systemWebSocketHandler.sendMessageToUser( new TextMessage(result));
        int vehicles_flag = flag;
        double longCarDistance = transportationList.get(0).getMaxDistance();
        for(int i=0;i<transportationList.size();i++){
            Car car = transportationList.get(i);
            String vehiclesWrongLink = wrongLink("car", car.getType());
            /*if(Strings.isEmpty(car.getName())) {
                flag = flag + 1;
                result = vehiclesWrongLink + ":vehicles name is empty.\n";
                logger.info(result);
                systemWebSocketHandler.sendMessageToUser( new TextMessage(result));
            }*/
            if(Strings.isEmpty(car.getType())) {
                flag = flag + 1;
                result = vehiclesWrongLink + ":vehicles weight limit is empty.\n";
                logger.info(result);
                systemWebSocketHandler.sendMessageToUser( new TextMessage(result));
            }
            if(Strings.isEmpty(car.getNum())) {
                flag = flag + 1;
                result = vehiclesWrongLink + ":vehicles quantity is empty.\n";
                logger.info(result);
                systemWebSocketHandler.sendMessageToUser( new TextMessage(result));
            }
            if(Strings.isEmpty(car.getCarSource())) {
                flag = flag + 1;
                result = vehiclesWrongLink + ":vehicles source is empty.\n";
                logger.info(result);
                systemWebSocketHandler.sendMessageToUser( new TextMessage(result));
            }
            if(Strings.isEmpty(car.getMaxLoad())) {
                flag = flag + 1;
                result = vehiclesWrongLink + ":vehicle piece capacity (p) is empty.\n";
                logger.info(result);
                systemWebSocketHandler.sendMessageToUser(new TextMessage(result));
            }
            if(Strings.isEmpty(car.getVelocity())) {
                flag = flag + 1;
                result = vehiclesWrongLink + ":vehicles speed is empty.\n";
                logger.info(result);
                systemWebSocketHandler.sendMessageToUser( new TextMessage(result));
            }
            if(Strings.isEmpty(car.getMaxStop())) {
                flag = flag + 1;
                result = vehiclesWrongLink + ":maximum top is empty.\n";
                logger.info(result);
                systemWebSocketHandler.sendMessageToUser( new TextMessage(result));
            }
            if(!Strings.isEmpty(car.getMaxLoad()) && Integer.parseInt(car.getMaxLoad())<1){
                flag = flag + 1;
                result = vehiclesWrongLink + ":maximum load is must >1.\n";
                logger.info(result);
                systemWebSocketHandler.sendMessageToUser( new TextMessage(result));
            }
            if(!Strings.isEmpty(car.getMaxLoad()) && Integer.parseInt(car.getMaxLoad())>2000){
                flag = flag + 1;
                result = vehiclesWrongLink + ":maximum load is must <2000.\n";
                logger.info(result);
                systemWebSocketHandler.sendMessageToUser( new TextMessage(result));
            }
            if(!Strings.isEmpty(car.getVelocity()) && car.getVelocity()<5){
                flag = flag + 1;
                result = vehiclesWrongLink + ":speed is must >5.\n";
                logger.info(result);
                systemWebSocketHandler.sendMessageToUser( new TextMessage(result));
            }
            if(!Strings.isEmpty(car.getVelocity()) && car.getVelocity()>80){
                flag = flag + 1;
                result = vehiclesWrongLink + ":speed is must <80.\n";
                logger.info(result);
                systemWebSocketHandler.sendMessageToUser( new TextMessage(result));
            }
            if(!Strings.isEmpty(car.getMaxStop()) && car.getMaxStop()<0){
                flag = flag + 1;
                result = vehiclesWrongLink + ":maximum stop is must >0.\n";
                logger.info(result);
                systemWebSocketHandler.sendMessageToUser( new TextMessage(result));
            }
            if(!Strings.isEmpty(car.getMaxStop()) && car.getMaxStop()>99){
                flag = flag + 1;
                result = vehiclesWrongLink + ":maximum stop is must <99.\n";
                logger.info(result);
                systemWebSocketHandler.sendMessageToUser( new TextMessage(result));
            }
            if(!Strings.isEmpty(car.getMaxDistance()) && car.getMaxDistance()<0){
                flag = flag + 1;
                result = vehiclesWrongLink + ":maximum distance is must >0.\n";
                logger.info(result);
                systemWebSocketHandler.sendMessageToUser( new TextMessage(result));
            }
            if(!Strings.isEmpty(car.getMaxDistance()) && car.getType().equals("baidu")&&car.getMaxDistance()>5){
                flag = flag + 1;
                result = vehiclesWrongLink + ":baidu maximum distance is must <5.\n";
                logger.info(result);
                systemWebSocketHandler.sendMessageToUser( new TextMessage(result));
            }
            if(!Strings.isEmpty(car.getMaxDistance()) && car.getMaxDistance()>999){
                flag = flag + 1;
                result = vehiclesWrongLink + ":maximum distance is must <999.\n";
                logger.info(result);
                systemWebSocketHandler.sendMessageToUser( new TextMessage(result));
            }
            if(!Strings.isEmpty(car.getMaxRunningTime()) && car.getMaxRunningTime()<0){
                flag = flag + 1;
                result = vehiclesWrongLink + ":maximum time is must >0.\n";
                logger.info(result);
                systemWebSocketHandler.sendMessageToUser( new TextMessage(result));
            }
            if(!Strings.isEmpty(car.getMaxRunningTime()) && car.getMaxRunningTime()>999){
                flag = flag + 1;
                result = vehiclesWrongLink + ":maximum time is must <999.\n";
                logger.info(result);
                systemWebSocketHandler.sendMessageToUser( new TextMessage(result));
            }
//            if(car.getType().equals("百度")&&car.getMaxRunningTime()>999){
//                flag = flag + 1;
//                result = vehiclesWrongLink + ":maximum time is must <999.\n";
//                logger.info(result);
//                systemWebSocketHandler.sendMessageToUser( new TextMessage(result));
//            }
            //获取车辆最远车程
            longCarDistance = longCarDistance > car.getMaxDistance() ? longCarDistance :car.getMaxDistance();
        }
        result = vehicles_flag == flag ? "vehicles passed" : wrongLink("car", "Vehicles") + " : is wrong";
        systemWebSocketHandler.sendMessageToUser( new TextMessage(result));

        //验证网点信息
        result = "Validating Depots Info......";
        systemWebSocketHandler.sendMessageToUser( new TextMessage(result));
        int depotsInfo_flag = flag;
        float minWeight = Float.parseFloat(siteInfoList.get(0).getMaxOperateNum());
        //获取所有siteInfo的code编码
        List<String> siteIdList = new ArrayList<>();
        for(int i = 0; i<siteInfoList.size(); i++) {
            siteIdList.add(siteInfoList.get(i).getSiteCode());
        }

        for(int i=0;i<siteInfoList.size();i++){
            SiteInfo siteInfo = siteInfoList.get(i);
            String siteInfoWrongLink = wrongLink("siteInfo", siteInfo.getSiteName());
            if(Strings.isEmpty(siteInfo.getSiteCode())){
                flag = flag + 1;
                result = siteInfoWrongLink+":ID is wrong. Because it's empty.\n";
                logger.info(result);
                systemWebSocketHandler.sendMessageToUser( new TextMessage(result));
            }
            if(Strings.isEmpty(siteInfo.getSiteLongitude())){
                flag = flag + 1;
                result = siteInfoWrongLink+":longitude is wrong. Because it's empty.\n";
                logger.info(result);
                systemWebSocketHandler.sendMessageToUser( new TextMessage(result));
            }
            if(Strings.isEmpty(siteInfo.getSiteLatitude())){
                flag = flag + 1;
                result = siteInfoWrongLink+":latitude is wrong. Because it's empty.\n";
                systemWebSocketHandler.sendMessageToUser( new TextMessage(result));
            }
            if(Strings.isEmpty(siteInfo.getSiteName())){
                flag = flag + 1;
                result = siteInfoWrongLink+":name is wrong. Because it's empty.\n";
                systemWebSocketHandler.sendMessageToUser( new TextMessage(result));
            }
            if(Strings.isEmpty(siteInfo.getSiteAddress())){
                flag = flag + 1;
                result = siteInfoWrongLink+":address is wrong. Because it's empty.\n";
                logger.info(result);
                systemWebSocketHandler.sendMessageToUser( new TextMessage(result));
            }
            if(Strings.isEmpty(siteInfo.getSiteArea())){
                flag = flag + 1;
                result = siteInfoWrongLink+":area is wrong. Because it's empty.\n";
                systemWebSocketHandler.sendMessageToUser( new TextMessage(result));
            }
            if(Strings.isEmpty(siteInfo.getSiteType())){
                flag = flag + 1;
                result = siteInfoWrongLink+":type is wrong. Because it's empty.\n";
                systemWebSocketHandler.sendMessageToUser( new TextMessage(result));
            }
           /* if(Strings.isEmpty(siteInfo.getDistribCenter())){
                flag = flag + 1;
                result = siteInfoWrongLink+":distrib.center is wrong. Because it's empty.\n";
                systemWebSocketHandler.sendMessageToUser( new TextMessage(result));
            }*/
            if(Strings.isEmpty(siteInfo.getSiteNightDelivery())){
                flag = flag + 1;
                result = siteInfoWrongLink+":night delivery is wrong. Because it's empty.\n";
                systemWebSocketHandler.sendMessageToUser( new TextMessage(result));
            }
            if(Strings.isEmpty(siteInfo.getCarNum())){
                flag = flag + 1;
                result = siteInfoWrongLink+":vehicle quantity limit is wrong. Because it's empty.\n";
                systemWebSocketHandler.sendMessageToUser( new TextMessage(result));
            }
            if(Strings.isEmpty(siteInfo.getLargeCarModel())){
                flag = flag + 1;
                result = siteInfoWrongLink+":vehicle weight limit  is wrong. Because it's empty.\n";
                systemWebSocketHandler.sendMessageToUser( new TextMessage(result));
            }
            if(Strings.isEmpty(siteInfo.getMaxOperateNum())){
                flag = flag + 1;
                result = siteInfoWrongLink+":piece capacity is wrong. Because it's empty.\n";
                systemWebSocketHandler.sendMessageToUser( new TextMessage(result));
            }
            if(!Strings.isEmpty(siteInfo.getSiteArea()) && Double.parseDouble(siteInfo.getSiteArea())<0){
                flag = flag + 1;
                result = siteInfoWrongLink+":depot area is must >0. \n";
                systemWebSocketHandler.sendMessageToUser( new TextMessage(result));
            }
            if(!Strings.isEmpty(siteInfo.getSiteArea()) && Double.parseDouble(siteInfo.getSiteArea())>3000){
                flag = flag + 1;
                result = siteInfoWrongLink+":depot area is must <3000. \n";
                systemWebSocketHandler.sendMessageToUser( new TextMessage(result));
            }
            if(!Strings.isEmpty(siteInfo.getCarNum()) && Integer.parseInt(siteInfo.getCarNum())<0){
                flag = flag + 1;
                result = siteInfoWrongLink+":vehicle quantity limit  is must >0. \n";
                systemWebSocketHandler.sendMessageToUser( new TextMessage(result));
            }
            if(!Strings.isEmpty(siteInfo.getCarNum()) && Integer.parseInt(siteInfo.getCarNum())>999){
                flag = flag + 1;
                result = siteInfoWrongLink+":vehicle quantity limit  is must <999. \n";
                systemWebSocketHandler.sendMessageToUser( new TextMessage(result));
            }
            if(!Strings.isEmpty(siteInfo.getLargeCarModel()) && Integer.parseInt(siteInfo.getLargeCarModel())<0){
                flag = flag + 1;
                result = siteInfoWrongLink+":vehicle weight limit  is must >0. \n";
                systemWebSocketHandler.sendMessageToUser( new TextMessage(result));
            }
            if(!Strings.isEmpty(siteInfo.getLargeCarModel()) && Integer.parseInt(siteInfo.getLargeCarModel())>999){
                flag = flag + 1;
                result = siteInfoWrongLink+":vehicle weight limit  is must <999. \n";
                systemWebSocketHandler.sendMessageToUser( new TextMessage(result));
            }
            if(!Strings.isEmpty(siteInfo.getMaxOperateNum()) && Integer.parseInt(siteInfo.getMaxOperateNum())<2000){
                flag = flag + 1;
                result = siteInfoWrongLink+":piece capacity (p) is must >2000. \n";
                systemWebSocketHandler.sendMessageToUser( new TextMessage(result));
            }
            if(!Strings.isEmpty(siteInfo.getMaxOperateNum()) && Integer.parseInt(siteInfo.getMaxOperateNum())>50000){
                flag = flag + 1;
                result = siteInfoWrongLink+":piece capacity (p)  is must <50000. \n";
                systemWebSocketHandler.sendMessageToUser( new TextMessage(result));
            }
            if(!Strings.isEmpty(siteInfo.getSiteLatitude()) && Double.parseDouble(siteInfo.getSiteLatitude())<-90){
                flag = flag + 1;
                result = siteInfoWrongLink+":latitude  is must >-90. \n";
                systemWebSocketHandler.sendMessageToUser( new TextMessage(result));
            }
            if(!Strings.isEmpty(siteInfo.getSiteLatitude()) && Double.parseDouble(siteInfo.getSiteLatitude())>90){
                flag = flag + 1;
                result = siteInfoWrongLink+":latitude  is must <90. \n";
                systemWebSocketHandler.sendMessageToUser( new TextMessage(result));
            }
            if(!Strings.isEmpty(siteInfo.getSiteLongitude()) && Double.parseDouble(siteInfo.getSiteLongitude())<-180){
                flag = flag + 1;
                result = siteInfoWrongLink+":longitude  is must >-180. \n";
                systemWebSocketHandler.sendMessageToUser( new TextMessage(result));
            }
            if(!Strings.isEmpty(siteInfo.getSiteLongitude()) && Double.parseDouble(siteInfo.getSiteLongitude())>180){
                flag = flag + 1;
                result = siteInfoWrongLink+":longitude  is must <180. \n";
                systemWebSocketHandler.sendMessageToUser( new TextMessage(result));
            }
            //验证集散点是否在depot info中
           /* if(!siteIdList.contains(siteInfo.getDistribCenter())) {
                flag = flag + 1;
                result = siteInfoWrongLink+":distrib.center is not in depot. \n";
                systemWebSocketHandler.sendMessageToUser( new TextMessage(result));
            }*/
            //获取所有网点最小承载量
            minWeight = minWeight < Float.parseFloat(siteInfo.getMaxOperateNum()) ? minWeight : Float.parseFloat(siteInfo.getMaxOperateNum());
        }
        result = depotsInfo_flag == flag ? "Validating Depot Info passed" : wrongLink("siteInfo", "Depot Info") + " : is wrong";
        systemWebSocketHandler.sendMessageToUser( new TextMessage(result));

        /**
         * 验证网点距离信息
         */
        result = "Validating Depots Distance......";
        systemWebSocketHandler.sendMessageToUser( new TextMessage(result));
        int depotsDistance_flag = flag;
        float shortSiteDistance = siteDistList.get(0).getCarDistance();
        for(int i=0;i<siteDistList.size();i++){
            SiteDist siteDist = siteDistList.get(i);
            String depotsDistanceWrongLink = wrongLink("siteDist", siteDist.getSiteCollect());
            if(Strings.isEmpty(siteDist.getSiteCollect())){
                flag = flag + 1;
                result = depotsDistanceWrongLink+":pickup depot is wrong. Because it's empty.\n";
                systemWebSocketHandler.sendMessageToUser( new TextMessage(result));
            }
            if(Strings.isEmpty(siteDist.getSiteDelivery())){
                flag = flag + 1;
                result = depotsDistanceWrongLink+":delivery depot is wrong. Because it's empty.\n";
                systemWebSocketHandler.sendMessageToUser( new TextMessage(result));
            }
            if (Strings.isEmpty(siteDist.getCarDistance())) {
                flag = flag + 1;
                result = depotsDistanceWrongLink + ":transportation distance(km) is wrong. Because it's empty.\n";
                systemWebSocketHandler.sendMessageToUser( new TextMessage(result));
            }
            if(Strings.isEmpty(siteDist.getDurationNightDelivery())){
                flag = flag + 1;
                result = depotsDistanceWrongLink+":night transportation time(min) is wrong. Because it's empty.\n";
                systemWebSocketHandler.sendMessageToUser( new TextMessage(result));
            }
            if(!Strings.isEmpty(siteDist.getCarDistance()) && siteDist.getCarDistance().floatValue()<0){
                flag = flag + 1;
                result = depotsDistanceWrongLink+":transportation distance is must >0. \n";
                systemWebSocketHandler.sendMessageToUser( new TextMessage(result));
            }
            if(!Strings.isEmpty(siteDist.getCarDistance()) && siteDist.getCarDistance().floatValue()>200){
                flag = flag + 1;
                result = depotsDistanceWrongLink+":transportation distance is must <200. \n";
                systemWebSocketHandler.sendMessageToUser( new TextMessage(result));
            }
            //判断收件网点是否存在
            if(!siteIdList.contains(siteDist.getSiteCollect())) {
                flag = flag + 1;
                result = depotsDistanceWrongLink+":start depot is not exist. \n";
                systemWebSocketHandler.sendMessageToUser( new TextMessage(result));
            }
            //判断派件网点是否存在
            if(!siteIdList.contains(siteDist.getSiteDelivery())) {
                flag = flag + 1;
                result = depotsDistanceWrongLink+":end depot is not exist. \n";
                systemWebSocketHandler.sendMessageToUser( new TextMessage(result));
            }
            //获取最短的网点距离
            shortSiteDistance = shortSiteDistance < siteDist.getCarDistance() ? shortSiteDistance : siteDist.getCarDistance();
        }
        if(shortSiteDistance > longCarDistance) {
            flag = flag + 1;
            result = "Validating Depots Distance is too long. \n";
            systemWebSocketHandler.sendMessageToUser( new TextMessage(result));
        }
        result = depotsDistance_flag == flag ? "Validating Depots Distance passed.\n" : wrongLink("siteDist", "Depots Distance") + " : is wrong";
        systemWebSocketHandler.sendMessageToUser( new TextMessage(result));

        /**
         * 验证需求信息
         */
        result = "Validating Demands......";
        systemWebSocketHandler.sendMessageToUser( new TextMessage(result));
        int demands_flag = flag;
        for(int i=0;i<demandInfoList.size();i++){
            DemandInfo demandInfo = demandInfoList.get(i);
            String demandInfoWrongLink = wrongLink("demandInfo", "ID为："+demandInfo.getId()+"的demand");
            if(Strings.isEmpty(demandInfo.getDate())){
                flag = flag + 1;
                result = demandInfoWrongLink+":Date is wrong. Because it's empty.\n";
                systemWebSocketHandler.sendMessageToUser( new TextMessage(result));
            }
            if(Strings.isEmpty(demandInfo.getSiteCodeCollect())){
                flag = flag + 1;
                result = demandInfoWrongLink+":pickup depot is wrong. Because it's empty.\n";
                systemWebSocketHandler.sendMessageToUser( new TextMessage(result));
            }
            if (Strings.isEmpty(demandInfo.getSiteCodeDelivery())) {
                flag = flag + 1;
                result = demandInfoWrongLink + ":delivery depot is wrong. Because it's empty.\n";
                systemWebSocketHandler.sendMessageToUser( new TextMessage(result));
            }
            if(Strings.isEmpty(demandInfo.getProductType())){
                flag = flag + 1;
                result = demandInfoWrongLink+":product type is wrong. Because it's empty.\n";
                systemWebSocketHandler.sendMessageToUser( new TextMessage(result));
            }
            if(Strings.isEmpty(demandInfo.getDurationStart())){
                flag = flag + 1;
                result = demandInfoWrongLink+":time start is wrong. Because it's empty.\n";
                systemWebSocketHandler.sendMessageToUser( new TextMessage(result));
            }
            if(Strings.isEmpty(demandInfo.getDurationEnd())){
                flag = flag + 1;
                result = demandInfoWrongLink+":time end is wrong. Because it's empty.\n";
                systemWebSocketHandler.sendMessageToUser( new TextMessage(result));
            }
            if(Strings.isEmpty(demandInfo.getWeight())){
                flag = flag + 1;
                result = demandInfoWrongLink+":weight is wrong. Because it's empty.\n";
                systemWebSocketHandler.sendMessageToUser( new TextMessage(result));
            }
            if(Strings.isEmpty(demandInfo.getVotes())){
                flag = flag + 1;
                result = demandInfoWrongLink+":piece is wrong. Because it's empty.\n";
                systemWebSocketHandler.sendMessageToUser( new TextMessage(result));
            }
            if(Integer.parseInt(demandInfo.getDurationStart())<0){
                flag = flag + 1;
                result = demandInfoWrongLink+":start time is must > 0.\n";
                systemWebSocketHandler.sendMessageToUser( new TextMessage(result));
            }
            if(Integer.parseInt(demandInfo.getDurationStart())>24*60){
                flag = flag + 1;
                result = demandInfoWrongLink+":start time is must < 24小时.\n";
                systemWebSocketHandler.sendMessageToUser( new TextMessage(result));
            }
            if(!Strings.isEmpty(demandInfo.getDurationEnd()) && Integer.parseInt(demandInfo.getDurationEnd())<0){
                flag = flag + 1;
                result = demandInfoWrongLink+":effective end time is must > 0.\n";
                systemWebSocketHandler.sendMessageToUser( new TextMessage(result));
            }
            if(!Strings.isEmpty(demandInfo.getDurationEnd()) && Integer.parseInt(demandInfo.getDurationEnd())>24*60){
                flag = flag + 1;
                result = demandInfoWrongLink+":effective end time is must < 24小时.\n";
                systemWebSocketHandler.sendMessageToUser( new TextMessage(result));
            }
            if(!Strings.isEmpty(demandInfo.getVotes()) && Integer.parseInt(demandInfo.getVotes())<0){
                flag = flag + 1;
                result = demandInfoWrongLink+":piece (p) is must > 0.\n";
                systemWebSocketHandler.sendMessageToUser( new TextMessage(result));
            }
            if(!Strings.isEmpty(demandInfo.getVotes()) && Integer.parseInt(demandInfo.getVotes())>2000){
                flag = flag + 1;
                result = demandInfoWrongLink+":piece (p) is must < 2000.\n";
                systemWebSocketHandler.sendMessageToUser( new TextMessage(result));
            }
            //判断收件网点是否存在
            if(!siteIdList.contains(demandInfo.getSiteCodeCollect())) {
                flag = flag + 1;
                result = demandInfoWrongLink+":start depot is not exist.\n";
                systemWebSocketHandler.sendMessageToUser( new TextMessage(result));
            }
            //判断派件网点是否存在
            if(!siteIdList.contains(demandInfo.getSiteCodeDelivery())) {
                flag = flag + 1;
                result = demandInfoWrongLink+":stop depot is not exist.\n";
                systemWebSocketHandler.sendMessageToUser( new TextMessage(result));
            }
            //判断网点的最大承载能力
            if(Float.parseFloat(demandInfo.getWeight()) > minWeight) {
                int maxCollectNum = Integer.parseInt(siteInfoService.findSiteInfoBySiteCode(scenariosId, demandInfo.getSiteCodeCollect()).getMaxOperateNum());
                int maxDeliveryNum = Integer.parseInt(siteInfoService.findSiteInfoBySiteCode(scenariosId, demandInfo.getSiteCodeDelivery()).getMaxOperateNum());
                if(Integer.parseInt(demandInfo.getWeight()) > maxCollectNum || Integer.parseInt(demandInfo.getWeight()) > maxDeliveryNum) {
                    flag = flag + 1;
                    result = demandInfoWrongLink+":weight is overload.\n";
                    systemWebSocketHandler.sendMessageToUser( new TextMessage(result));
                }
            }
        }

        result = demands_flag == flag ? "Validating Demands passed.\n" : wrongLink("demandInfo", "Demands") + " : is wrong";
        systemWebSocketHandler.sendMessageToUser( new TextMessage(result));

        /**
         * 验证整体模型参数
         */
        result = "Validating Parameters......";
        systemWebSocketHandler.sendMessageToUser( new TextMessage(result));
        int parameters_flag = flag;
        for(int i=0;i<modelArgList.size();i++){
            ModelArg modelArg = modelArgList.get(i);
            String modelArgoWrongLink = wrongLink("modelArg", modelArg.getParameterName());
            if(Strings.isEmpty(modelArg.getParameterName())){
                flag = flag + 1;
                result = modelArgoWrongLink+":parameter name is wrong. Because it's empty.\n";
                systemWebSocketHandler.sendMessageToUser( new TextMessage(result));
            }
            if(Strings.isEmpty(modelArg.getData())){
                flag = flag + 1;
                result = modelArgoWrongLink+":data is wrong. Because it's empty.\n";
                systemWebSocketHandler.sendMessageToUser( new TextMessage(result));
            }
        }
        result = parameters_flag == flag ? "Validating Parameters passed.\n" : wrongLink("modelArg", "Parameters") + " : is wrong";
        systemWebSocketHandler.sendMessageToUser( new TextMessage(result));


        String success = "Validation is successful,and now you can run the simulation.";
        String fail = "Validation is completed,please check the error information.";
        String delimiter = "\n---------------------------------------------------------------------\n";

        if(flag == 0) {

            result = DateTime.now().toString("yyyy-MM-dd HH:mm:ss，")+ success;
            systemWebSocketHandler.sendMessageToUser( new TextMessage(delimiter));
            systemWebSocketHandler.sendMessageToUser( new TextMessage(result));
            return "success";
        }
        else{
            result = DateTime.now().toString("yyyy-MM-dd HH:mm:ss，") + fail;
            systemWebSocketHandler.sendMessageToUser( new TextMessage(delimiter));
            systemWebSocketHandler.sendMessageToUser( new TextMessage(result));
            return "fail";
        }

    }



}
