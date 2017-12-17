package com.xboost.controller;

import com.mckinsey.sf.data.Car;
import com.xboost.pojo.Configuration;
import com.xboost.service.*;
import com.xboost.util.CascadeModelUtil;
import com.xboost.util.RelayModeUtil;
import com.xboost.util.ShiroUtil;
import com.xboost.websocket.SystemWebSocketHandler;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.socket.TextMessage;

import javax.inject.Inject;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2017/11/10 0010.
 */
@Controller
@RequestMapping("/cascade")
public class CascadeController {

    @Inject
    CascadeService cascadeService;

    @Inject
    CarService carService;

    @Inject
    DemandInfoService demandInfoService;

    @Inject
	private SiteDistService siteDistService;

    @RequestMapping(value="/runSilumate",method = RequestMethod.POST)
    @ResponseBody
    public Map<String,Object> docascade(String distMode,String loadTime,String loopLimit){
        if("1".equals(distMode)){
            //查询Confiuration表数据
            Map<String, Object> param = new HashMap<String,Object>();
            Configuration config = new Configuration();
            Car[] carlist = null;
            //传入场景id
            param.put("scenariosId", ShiroUtil.getOpenScenariosId());
            config.setDistMode(Integer.parseInt(distMode));
            config.setLoadTime(Double.parseDouble(loadTime));
            config.setOptimizeIterations(Integer.parseInt(loopLimit));
            //查询car_info内容并set到config
            carlist =carService.findCarByParam(param);
            for(int i=0;i<carlist.length;i++){
                carlist[i].setStartLocation(carlist[i].getStartLocation().trim());
                carlist[i].setEndLocation(carlist[i].getEndLocation().trim());

                config.setCarTemplates(carlist);
            }


            CascadeModelUtil cmu = new CascadeModelUtil();
            try{
                cmu.excute(config,demandInfoService,siteDistService);
            }catch (NullPointerException e){
                SystemWebSocketHandler systemWebSocketHandler = new SystemWebSocketHandler();
                TextMessage message = new TextMessage("NullPointerException");
                systemWebSocketHandler.sendMessageToUser(message);
            }
//        LogFactory.getLog(AccountController.class).info("input:"+input);

        }else if("2".equals(distMode)){
//            RelayModeUtil rm = new RelayModeUtil();
//            try {
//                rm.excute();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
        }else{

        }
        return null;
    }
}
