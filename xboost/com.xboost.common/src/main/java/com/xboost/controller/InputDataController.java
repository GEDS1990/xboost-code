package com.xboost.controller;

import com.xboost.pojo.Customer;
import com.xboost.service.CustomerService;
import com.xboost.service.SiteInfoService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.inject.Inject;

@Controller
@RequestMapping("/InputData")
public class InputDataController {
    /*
    * 跳转View页面
    * */
    @RequestMapping(value = "/View_edit",method = RequestMethod.GET)
    public String View() {
        return "InputData/View_edit";
    }
    /*
    * 跳转Import页面
    * */
    @RequestMapping(value = "/Import",method = RequestMethod.GET)
    public String Import() {return "InputData/Import"; }
    /*
    * 跳转Export页面
    * */
    @RequestMapping(value = "/Export",method = RequestMethod.GET)
    public String Export() {
        return "InputData/Export";
    }
}
