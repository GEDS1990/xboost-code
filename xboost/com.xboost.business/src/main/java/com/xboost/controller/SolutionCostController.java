package com.xboost.controller;

import com.google.common.collect.Maps;
import com.xboost.pojo.Cost;
import com.xboost.pojo.ModelArg;
import com.xboost.pojo.SiteInfo;
import com.xboost.service.*;
import com.xboost.util.ShiroUtil;
import com.xboost.util.Strings;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.inject.Inject;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/11/5 0005.
 */

@Controller
@RequestMapping("/costs")
public class SolutionCostController {
    @Inject
    private SolutionCostService solutionCostService;
    @Inject
    private ModelArgService modelArgService;
    @Inject
    private SiteInfoService siteInfoService;
    @Inject
    private MyScenariosService myScenariosService;
    @Inject
    private SolutionRouteService solutionRouteService;
    @Inject
    private DemandInfoService demandInfoService;
    @Inject
    private SolutionEfficiencyService solutionEfficiencyService;

    @RequestMapping(method = RequestMethod.GET)
    public String list() {
        return "results/costs";
    }


    public static void main(String[] args)
    {
        Cost cost = new Cost();
        System.out.println("Hello World!");

    }

    /**
     * 添加Cost信息
     * @return
     */
    @RequestMapping(value = "/addCost",method = RequestMethod.POST)
    @ResponseBody
    public String addCost(HttpServletRequest request,Cost cost) { ;
        cost.setScenariosId(ShiroUtil.getOpenScenariosId().toString());
    //    cost.setModelType(request.getParameter("modelType"));
   //     cost.setModelType(request.getParameter("plan"));
        solutionCostService.add(cost);
        return "success";
    }


    //查询成本信息
    @RequestMapping(value = "/cost.json",method = RequestMethod.GET,produces = "application/json;charset=UTF-8")
    @ResponseBody
    public Map<String,Object> load(HttpServletRequest request) {
        String scenariosId = ShiroUtil.getOpenScenariosId();
        String modelType = myScenariosService.findById(Integer.parseInt(scenariosId)).getScenariosModel();
        String plan = request.getParameter("plan");

        Map<String,Object> param = Maps.newHashMap();
        param.put("modelType",modelType);
        param.put("scenariosId",scenariosId);
        param.put("plan",plan);

        Map<String,Object> result = Maps.newHashMap();
        List<Cost> costList = solutionCostService.findByParam(param);

        //总件量
        Integer totalPiece = solutionCostService.findTotalPiece(scenariosId);
        //网点
        List<String> siteCodeList=solutionEfficiencyService.findAllSite(scenariosId);
        Map<String,Object> totalVolList=Maps.newHashMap();
        Map<String,Object> siteInfoList=Maps.newHashMap();
        for(int i=0;i<siteCodeList.size();i++){
            SiteInfo siteInfo = siteInfoService.findSiteInfoBySiteCode(scenariosId, siteCodeList.get(i));
            String totalVol= solutionCostService.findTotalVol(scenariosId,siteCodeList.get(i));
            siteInfoList.put(siteCodeList.get(i),siteInfo);
            totalVolList.put(siteCodeList.get(i),totalVol);
        }
        //支线总运输成本
      //  Double branchTransportCost = solutionCostService.branchTransportCost();
      //   String branchTransportCost = solutionCostService.findBranchCost(scenariosId);

        //总票数
      //  List<String> totalVolList = solutionCostService.findTotalVol(scenariosId,siteCode);

        result.put("data",costList);
        result.put("modelType",modelType);
        result.put("totalPiece",totalPiece);
        result.put("siteInfoList",siteInfoList);
        result.put("totalVolList",totalVolList);
 //       result.put("branchTransportCost",branchTransportCost);

        return result;
    }


    /**
     * 编辑成本信息
     * @return
     */
    @RequestMapping(value = "/edit",method = RequestMethod.POST)
    @ResponseBody
    public String edit(HttpServletRequest request,Cost cost) {
        String scenariosId = ShiroUtil.getOpenScenariosId();
        String siteCode = "";
        solutionCostService.editCost(scenariosId,cost);
   //     solutionCostService.editSiteInfo(scenariosId,siteCode);
        return "success";
    }

    //查询初始化成本信息
    @RequestMapping(value = "/costInitData.json",method = RequestMethod.GET,produces = "application/json;charset=UTF-8")
    @ResponseBody
    public Map<String,Object> loadInitData(HttpServletRequest request) {
        String scenariosId = ShiroUtil.getOpenScenariosId();
        String modelType = myScenariosService.findById(Integer.parseInt(scenariosId)).getScenariosModel();

        //网点集散点人效
        Integer sitePeopleWork = modelArgService.findSitePeopleWork(scenariosId,modelType);
        //集配站集散点人效
        Integer distribPeopleWork = modelArgService.findDistribPeopleWork(scenariosId,modelType);
        //当前场景下网点总数
        Integer siteCount = demandInfoService.siteCount();
        //总件量
        Integer totalPiece = solutionCostService.findTotalPiece(scenariosId);

        //支线总运输成本
      //  Double branchTransportCost = solutionCostService.branchTransportCost();
          String branchTransportCost = solutionCostService.findBranchCost(scenariosId);
          //单件运输成本
      //  Double branchTransportCost = Double.parseDouble(branchTransportCostSum)/totalPiece;

        Map<String,Object> result = Maps.newHashMap();
        result.put("modelType",modelType);
        result.put("sitePeopleWork",sitePeopleWork);
        result.put("distribPeopleWork",distribPeopleWork);
        result.put("siteCount",siteCount);
        result.put("totalPiece",totalPiece);
        result.put("branchTransportCost",branchTransportCost);

        return result;
    }

    @RequestMapping(value = "/exportResult",method = RequestMethod.GET,produces = {"application/vnd.ms-excel;charset=UTF-8"})
    @ResponseBody
    public String exportResult(HttpServletResponse response)
    {
        response.setContentType("application/vnd.ms-excel;charset=utf-8");
        try {
            ServletOutputStream outputStream = response.getOutputStream();
            String fileName = new String(("Result_Costs").getBytes(), "utf-8");
            response.setCharacterEncoding("utf-8");
            response.setHeader("Content-disposition", "attachment; filename=" + fileName + ".xlsx");// 组装附件名称和格式
            String scenariosId = ShiroUtil.getOpenScenariosId();
            String[] titles = { "串点模型", "接力模型", "综合模型" };
            solutionCostService.exportResult(scenariosId,titles,outputStream);
            System.out.println("outputStream:"+outputStream);
        }
        catch (IOException e) {
            e.printStackTrace();
            System.out.println("网络连接故障!错误信息:"+e.getMessage());
        }
        return null;
    }
}
