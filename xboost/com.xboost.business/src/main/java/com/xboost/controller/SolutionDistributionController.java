package com.xboost.controller;

import com.xboost.pojo.DemandInfo;
import com.xboost.pojo.Route;
import com.xboost.service.ArrInfoService;
import com.xboost.service.DemandInfoService;
import com.xboost.service.SolutionDistributionService;
import com.xboost.service.SolutionRouteService;
import com.xboost.util.ExportUtil;
import com.xboost.util.ShiroUtil;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.*;
import org.junit.runners.Parameterized;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.inject.Inject;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 计费过程
 */
@Controller
@RequestMapping("/distribution")
public class SolutionDistributionController {
    @Inject
    private SolutionDistributionService solutionDistributionService;

    @RequestMapping(method = RequestMethod.GET)
    public String list() {
        return "results/distribution";
    }

    /**
     * @param type 0: 各网点截单时间
     *             1：各网点送达时间分布
     *             2：收段达到集散点时间分布
     *             3：支线到达目的地集散点时间分布
     *             4：始发集散地OD件量分布
     *             5：始发网点OD件量分布
     *             6：始发集散点的发车分布
     * @return
     */
    @RequestMapping(value = "/getMaxMix.json", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public Map<String, Object> getMaxMix(String type) {
        return solutionDistributionService.getDataByType(type);
    }

    @RequestMapping(value = "/exportResult", method = RequestMethod.GET, produces = {"application/vnd.ms-excel;charset=UTF-8"})
    @ResponseBody
    public String exportResult(HttpServletResponse response) {
        response.setContentType("application/vnd.ms-excel;charset=utf-8");
        try {
            ServletOutputStream outputStream = response.getOutputStream();
            String fileName = new String(("Result_Distribution").getBytes(), "utf-8");
            response.setCharacterEncoding("utf-8");
            response.setHeader("Content-disposition", "attachment; filename=" + fileName + ".xlsx");// 组装附件名称和格式
            String scenariosId = ShiroUtil.getOpenScenariosId();
            String[] titles = {"各个网点的截单时间分布", "", "", "", "各个网点的实际送达时间分布", "", "", "",
                    "收端到达集散点时间分布", "", "", "支线到达目的地集散点时间分布", "", "", "始发集散地OD件量分布", "", "", "",
                    "始发网点OD件量分布", "", "", "", "始发集散点的发车分布", "", ""};

            solutionDistributionService.exportResult(scenariosId, titles, outputStream);
            System.out.println("outputStream:" + outputStream);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("网络连接故障!错误信息:" + e.getMessage());
        }
        return null;
    }
}
