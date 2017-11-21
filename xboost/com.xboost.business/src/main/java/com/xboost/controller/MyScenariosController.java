package com.xboost.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/MyScenarios")
public class MyScenariosController {
    /**
     * 跳转MyScenarios页面
     */
    @RequestMapping(method = RequestMethod.GET)
    public String MyScenarios() {
        return "MyScenarios/MyScenarios";
    }

}
