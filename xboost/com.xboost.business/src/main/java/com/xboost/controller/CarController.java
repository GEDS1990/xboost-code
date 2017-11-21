package com.xboost.controller;

import com.xboost.service.CarService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.inject.Inject;

@Controller
@RequestMapping("/car")
public class CarController {
    @Inject
    private CarService carService;

    @RequestMapping(method = RequestMethod.GET)
    public String list() {
        return "car/list";
    }
}
