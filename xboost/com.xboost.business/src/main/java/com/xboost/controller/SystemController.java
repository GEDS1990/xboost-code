package com.xboost.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/System")
public class SystemController {
    /*
    * 跳转Users页面
    * */
    @RequestMapping(value = "/Users",method = RequestMethod.GET)
    public String Users() {
        return "System/Users";
    }
    /*
    * 跳转Scenarios页面
    * */
    @RequestMapping(value = "/Scenarios",method = RequestMethod.GET)
    public String Scenarios() {return "System/Scenarios"; }
}
