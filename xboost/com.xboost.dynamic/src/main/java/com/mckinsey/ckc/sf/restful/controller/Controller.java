package com.mckinsey.ckc.sf.restful.controller;

import java.util.concurrent.atomic.AtomicLong;

import org.springframework.web.bind.annotation.RequestMapping;  
import org.springframework.web.bind.annotation.RequestParam;  
import org.springframework.web.bind.annotation.RestController;

import com.mckinsey.ckc.sf.restful.data.Greeting;  
  
@RestController  
public class Controller {  
  
    private static final String template = "Hello, %s!";  
    private final AtomicLong counter = new AtomicLong();  
  
    @RequestMapping("/getNextPosition")  
    public Greeting greeting(@RequestParam(value="name", required=false, defaultValue="World") String name) {  
        return new Greeting(counter.incrementAndGet(),  
                            String.format(template, name));  
    }  	
    
    @RequestMapping("/hello")
    public String index() {
        return "Hello World";
    }

}  

