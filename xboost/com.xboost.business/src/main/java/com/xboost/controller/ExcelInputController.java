package com.xboost.controller;

import com.xboost.pojo.SiteDist;
import com.xboost.service.SiteDistService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.inject.Inject;

@Controller
@RequestMapping("/excelInput")
public class ExcelInputController {
    @Inject
    private SiteDistService siteDistService;

    @RequestMapping(method = RequestMethod.GET)
    public String list() {
        return "ExcelInput/list";
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
