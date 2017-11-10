package com.xboost.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/OutputData")
public class OutputDataController {
    /*
    * 跳转View页面
    * */
    @RequestMapping(value = "/View",method = RequestMethod.GET)
    public String ViewOut() {
        return "OutputData/View";
    }
    /*
    * 跳转Export页面
    * */
    @RequestMapping(value = "/Export",method = RequestMethod.GET)
    public String ExportT() {return "OutputData/Export"; }
}
