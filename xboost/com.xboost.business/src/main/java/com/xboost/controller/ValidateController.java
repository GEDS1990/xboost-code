package com.xboost.controller;

import com.google.common.collect.Maps;
import com.xboost.pojo.ModelArg;
import com.xboost.pojo.SiteDist;
import com.xboost.pojo.SiteInfo;
import com.xboost.pojo.Transportation;
import com.xboost.pojo.DemandInfo;
import com.xboost.service.SiteInfoService;
import com.xboost.util.ShiroUtil;
import com.xboost.util.Strings;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
@RequestMapping("/simualte")
public class ValidateController {
    @Inject
    private SiteInfoService siteInfoService;

    @RequestMapping(method = RequestMethod.GET)
    public String list() {
        return "ScenariosName/SimualtValidate";
    }

    private static Logger logger = LoggerFactory.getLogger(SiteInfoService.class);

    /**
     * 校验场景输入数据
     * @return
     */
    @RequestMapping(value = "/Validate",method = RequestMethod.POST)
    @ResponseBody
    public String Validate() {
        List<SiteInfo> siteInfoList = siteInfoService.findAllSiteInfo(ShiroUtil.getOpenScenariosId());
        String result="";
        for(int i=0;i<siteInfoList.size();i++){
            SiteInfo siteInfo = siteInfoList.get(i);
            if("".equals(siteInfo.getSiteCode())||" ".equals(siteInfo.getSiteCode())){
                result = result + siteInfo.getSiteCode()+"ID is wrong. Because it's null.\n";
            }
            if("".equals(siteInfo.getSiteLongitude())||" ".equals(siteInfo.getSiteLongitude())){
                result = result + siteInfo.getSiteCode()+"longitude is wrong. Because it's null.\n";
            }
            if("".equals(siteInfo.getSiteLatitude())||" ".equals(siteInfo.getSiteLatitude())){
                result = result + siteInfo.getSiteCode()+"latitude is wrong. Because it's null.\n";
            }
            if("".equals(siteInfo.getSiteName())||" ".equals(siteInfo.getSiteName())){
                result = result + siteInfo.getSiteCode()+"name is wrong. Because it's null.\n";
            }
            if("".equals(siteInfo.getSiteAddress())||" ".equals(siteInfo.getSiteAddress())){
                result = result + siteInfo.getSiteCode()+"address is wrong. Because it's null.\n";
            }
            if("".equals(siteInfo.getSiteArea())||" ".equals(siteInfo.getSiteArea())){
                result = result + siteInfo.getSiteCode()+"area is wrong. Because it's null.\n";
            }
            if("".equals(siteInfo.getSiteType())||" ".equals(siteInfo.getSiteType())){
                result = result + siteInfo.getSiteCode()+"type is wrong. Because it's null.\n";
            }
            if("".equals(siteInfo.getDistribCenter())||" ".equals(siteInfo.getDistribCenter())){
                result = result + siteInfo.getSiteCode()+"distrib.center is wrong. Because it's null.\n";
            }
            if("".equals(siteInfo.getCarNum())||" ".equals(siteInfo.getCarNum())){
                result = result + siteInfo.getSiteCode()+"vehicle quantity limit is wrong. Because it's null.\n";
            }
            if("".equals(siteInfo.getLargeCarModel())||" ".equals(siteInfo.getLargeCarModel())){
                result = result + siteInfo.getSiteCode()+"vehicle weight limit  is wrong. Because it's null.\n";
            }
            if("".equals(siteInfo.getMaxOperateNum())||" ".equals(siteInfo.getMaxOperateNum())){
                result = result + siteInfo.getSiteCode()+"piece capacity is wrong. Because it's null.\n";
            }

        }
        return  result;

    }



}
