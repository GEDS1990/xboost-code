package com.xboost.controller;

import com.google.common.collect.Maps;
import com.mckinsey.sf.data.Car;
import com.xboost.service.CarService;
import com.xboost.util.ShiroUtil;
import com.xboost.util.Strings;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.inject.Inject;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/11/5 0005.
 */

@Controller
@RequestMapping("/car")
public class CarController {
    @Inject
    private CarService transportService;

    @RequestMapping(method = RequestMethod.GET)
    public String list() {
        return "ScenariosName/Tran";
    }

    private static Logger logger = LoggerFactory.getLogger(CarService.class);

    /**
     * 添加运力信息
     * @return
     */
    @RequestMapping(value = "/add",method = RequestMethod.POST)
    @ResponseBody
    public String AddCar(Car car) {
        car.setScenariosId(ShiroUtil.getOpenScenariosId().toString());
        car.setBusyIdle("0");
//        timeWindow.setStart(0);
//        timeWindow.setEnd(1440);
        com.mckinsey.sf.data.TimeWindow tw = new com.mckinsey.sf.data.TimeWindow();
        tw.setStart(0);
        tw.setEnd(1440);
        car.setTw(tw);
        transportService.saveCar(car);
        return "success";
    }


    //通过Excel添加运力信息
    @RequestMapping(value = "/addByExcel",method = RequestMethod.POST)
    @ResponseBody
    public String AddCarByExcel(Car transport,@RequestParam MultipartFile[] file) {
        transport.setScenariosId(ShiroUtil.getOpenScenariosId());
        transport.setBusyIdle("0");
      //  System.out.println("车辆是否空闲"+transport.getBusyIdle());
        com.mckinsey.sf.data.TimeWindow tw = new com.mckinsey.sf.data.TimeWindow();
        tw.setStart(0);
        tw.setEnd(1440);
        transport.setTw(tw);
        transportService.delByScenariosId(ShiroUtil.getOpenScenariosId());
        transportService.delCarLincenceByScenariosId(ShiroUtil.getOpenScenariosId());
        transportService.addCarByExcel(transport,file);
        return "/ScenariosName/Conditions";
    }


    //查询运力信息
    @RequestMapping(value = "/transport.json",method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
    @ResponseBody
    public Map<String,Object> load(HttpServletRequest request) {

        String draw = request.getParameter("draw");
        Integer start = Integer.valueOf(request.getParameter("start"));
        Integer length = Integer.valueOf(request.getParameter("length"));
        String searchValue = request.getParameter("search[value]");
        String orderColumnIndex = request.getParameter("order[0][column]");
        String orderType = request.getParameter("order[0][dir]");
        String orderColumnName = request.getParameter("columns["+orderColumnIndex+"][name]");

        Map<String,Object> param = Maps.newHashMap();
        param.put("start",start);
        param.put("length",length);
        if(StringUtils.isNotEmpty(searchValue)) {
            param.put("keyword", "%" + Strings.toUTF8(searchValue) + "%");
        }
        param.put("orderColumn",orderColumnName);
        param.put("orderType",orderType);
        param.put("scenariosId",ShiroUtil.getOpenScenariosId());



        Map<String,Object> result = Maps.newHashMap();

        List<Car> transportList = transportService.findByParam(param); //.findAllSiteInfo();
        Integer count = transportService.findAllCount(ShiroUtil.getOpenScenariosId());
        Integer filteredCount = transportService.findCountByParam(param);

        result.put("draw",draw);
        result.put("recordsTotal",count); //总记录数
        result.put("recordsFiltered",filteredCount); //过滤出来的数量
        result.put("data",transportList);
        return result;
    }

    /**
     * 根据ID获取运力信息
     */
    @RequestMapping(value = "/transpt.json",method = RequestMethod.GET)
    @ResponseBody
    public Car getTransport(Integer id) {
        return transportService.findById(id);
    }

    /**
     * 编辑运力信息
     * @return
     */
    @RequestMapping(value = "/edit",method = RequestMethod.POST)
    @ResponseBody
    public String editTransport(Car transport) {
        transport.setScenariosId(ShiroUtil.getOpenScenariosId());
        transportService.editTransport(transport);

        return "success";
    }


    /**
     * 删除用户
     */
    @RequestMapping(value = "/del",method = RequestMethod.POST)
    @ResponseBody
    public String delById(Integer id) {
        transportService.delById(id);
        return "success";
    }


    @RequestMapping(value = "/exportExcel",method = RequestMethod.GET,produces = {"application/vnd.ms-excel;charset=UTF-8"})
    @ResponseBody
    public String exportExcel(HttpServletResponse response)
    {
        response.setContentType("application/vnd.ms-excel;charset=utf-8");
        try
        {
            ServletOutputStream outputStream = response.getOutputStream();
            String fileName = new String(("Vehicles").getBytes(), "utf-8");
            response.setCharacterEncoding("utf-8");
            response.setHeader("Content-disposition", "attachment; filename=" + fileName + ".xlsx");// 组装附件名称和格式
            //       response.setHeader("Content-disposition", "attachment; filename=distance.xlsx");
            String scenariosId = ShiroUtil.getOpenScenariosId();
            String[] titles = { "type","dimensions","skills","start_location","end_location",
                    "max_distance","max_running_time","cost_per_distance","cost_per_time",
                    "fixed_cost","max_stop","velocity","fixed_round","fixed_round_fee","car_source"
                    ,"max_load","duration_unload_full"};
            transportService.exportExcel(scenariosId,titles,outputStream);
            //       System.out.println("outputStream:"+outputStream);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return null;
    }

}
