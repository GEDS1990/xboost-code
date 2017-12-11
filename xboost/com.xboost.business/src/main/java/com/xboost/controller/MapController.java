package com.xboost.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/map")
public class MapController {
    /*
    * 跳转map页面
    * */
    @RequestMapping(value = "/mapDepots",method = RequestMethod.GET)
    public String Overview() {
        return "map/mapDepots";
    }
}
