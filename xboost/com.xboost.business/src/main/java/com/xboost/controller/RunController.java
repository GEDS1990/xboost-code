package com.xboost.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/Run")
public class RunController {
    /*
    * 跳转Execute页面
    * */
    @RequestMapping(value = "/Execute",method = RequestMethod.GET)
    public String Execute() {
        return "Run/Execute";
    }
    /*
    * 跳转Progress页面
    * */
    @RequestMapping(value = "/Progress",method = RequestMethod.GET)
    public String Progress() {return "Run/Progress"; }

}
