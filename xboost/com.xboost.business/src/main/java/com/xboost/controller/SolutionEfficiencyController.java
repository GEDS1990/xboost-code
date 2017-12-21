package com.xboost.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


@Controller
@RequestMapping("/efficiency")
public class SolutionEfficiencyController {
    @RequestMapping(method = RequestMethod.GET)
    public String list() {
        return "results/efficiency";
    }
}
