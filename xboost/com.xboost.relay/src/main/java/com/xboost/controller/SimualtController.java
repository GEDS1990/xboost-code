package com.xboost.controller;

import com.xboost.service.SiteDistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/Simualte")
public class SimualtController {
    @Autowired
    private SiteDistService siteDistService;

    @RequestMapping(method = RequestMethod.GET)
    public String list() {
        return "ScenariosName/Simualte";
    }

    /**
     * 添加网点距离基础信息
     * @return
     */
    /*
    移到业务相关controller中
    @RequestMapping(value = "/addByExcel",method = RequestMethod.POST)
    @ResponseBody
    public void AddSiteDist(SiteDist siteDist,@RequestParam MultipartFile[] file) {
        siteDistService.saveSiteDist(siteDist,file);
    }*/


}
