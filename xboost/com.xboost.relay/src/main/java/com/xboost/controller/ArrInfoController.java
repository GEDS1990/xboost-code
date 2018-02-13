package com.xboost.controller;

import com.mckinsey.sf.data.solution.ArrInfo;
import com.xboost.service.ArrInfoService;
import com.xboost.util.ShiroUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

;

/**
 * Created by Administrator on 2017/11/12 0005.
 */

@Controller
@RequestMapping("/arrInfo")
public class ArrInfoController {
    @Autowired
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
