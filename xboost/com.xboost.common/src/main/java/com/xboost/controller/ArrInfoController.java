package com.xboost.controller;

import com.google.common.collect.Maps;
import com.mckinsey.sf.data.solution.ArrInfo;
import com.xboost.pojo.DemandInfo;
import com.xboost.service.ArrInfoService;
import com.xboost.service.DemandInfoService;
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

;

/**
 * Created by Administrator on 2017/11/12 0005.
 */

@Controller
@RequestMapping("/arrInfo")
public class ArrInfoController {
    @Inject
    private ArrInfoService arrInfoService;

    @RequestMapping(method = RequestMethod.GET)
    public String list() {
        return "arrinfo/list";
    }

    /**
     * 保存arrinfo信息
     * @return
     */
    @RequestMapping(value = "/save",method = RequestMethod.POST)
    @ResponseBody
    public String saveArrInfo(ArrInfo arrInfo) {
        //设置场景Id
        arrInfo.setScenariosId(ShiroUtil.getOpenScenariosId());
        arrInfoService.saveArrInfo(arrInfo);
        return "success";
    }

}
