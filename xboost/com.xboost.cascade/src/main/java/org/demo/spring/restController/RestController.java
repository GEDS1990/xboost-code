package org.demo.spring.restController;

import org.demo.spring.model.Lend;
import org.demo.spring.service.LendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;


/**
 * Created by Administrator on 2017/5/13 0013.
 */
@org.springframework.web.bind.annotation.RestController
public class RestController {
    @Autowired
    private LendService lendService;
    /*@20170629*/
    @RequestMapping(value = "/restControl/queryLends", method = RequestMethod.GET)
    public List<Lend> queryLends(String lendName) {
        return lendService.queryLends(lendName);
    }
}
