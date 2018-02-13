package com.xboost.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/Research")
public class ResearchController {
    /*
    * 跳转Overview页面
    * */
    @RequestMapping(method = RequestMethod.GET)
    public String Overview() {
        return "Research/Research";
    }

}
