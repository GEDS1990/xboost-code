package com.xboost.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/ScenariosName")
public class ScenariosNameController {
    /**
     * 跳转ScenariosName页面
     */
    @RequestMapping(method = RequestMethod.GET)
    public String ScenariosName() {
        return "ScenariosName/ScenariosName";
    }

    /*
    * 跳转Conditions.jsp页面
    * */
    @RequestMapping(value = "/Conditions",method = RequestMethod.GET)
    public String Create() {
        return "ScenariosName/Conditions";
    }
}
