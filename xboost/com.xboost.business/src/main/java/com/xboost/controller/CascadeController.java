package com.xboost.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

/**
 * Created by Administrator on 2017/11/10 0010.
 */
@Controller
@RequestMapping("/cascade")
public class CascadeController {
    @RequestMapping(value="/query",method = RequestMethod.POST)
    @ResponseBody
    public Map<String,Object> cascade(){

        return null;
    }
}
