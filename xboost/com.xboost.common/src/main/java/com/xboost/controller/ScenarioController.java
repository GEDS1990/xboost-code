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
@RequestMapping("/Scenario")
public class ScenarioController {
    /*
    * 跳转Create页面
    * */
    @RequestMapping(value = "/Create",method = RequestMethod.GET)
    public String Create() {
        return "Scenario/Create";
    }
    /*
    * 跳转Open页面
    * */
    @RequestMapping(value = "/Open",method = RequestMethod.GET)
    public String Open() {
        return "Scenario/Open";
    }
    /*
    * 跳转Save页面
    * */
    @RequestMapping(value = "/Save",method = RequestMethod.GET)
    public String Save() {
        return "Scenario/Save";
    }
}
