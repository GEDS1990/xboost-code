package com.xboost.controller;

import com.google.common.collect.Maps;
import com.xboost.pojo.Cost;
import com.xboost.pojo.ModelArg;
import com.xboost.pojo.Route;
import com.xboost.pojo.SiteInfo;
import com.xboost.service.*;
import com.xboost.util.ShiroUtil;
import com.xboost.util.Strings;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.inject.Inject;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
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
    public String addCost(HttpServletRequest request,Cost cost,List<SiteInfo> siteInfoList) { ;
        cost.setScenariosId(ShiroUtil.getOpenScenariosId().toString());
    //    cost.setModelType(request.getParameter("modelType"));
   //     cost.setModelType(request.getParameter("plan"));
        solutionCostService.add(cost);
        for(int i=0;i<siteInfoList.size();i++){
            solutionCostService.editSiteInfo(ShiroUtil.getOpenScenariosId(),siteInfoList.get(i).getSiteCode());
        }
        return "success";
    }


    /**
     * 编辑成本信息
     * @return
     */
    @RequestMapping(value = "/edit",method = RequestMethod.POST)
    @ResponseBody
    public String edit(HttpServletRequest request,Cost cost) {
        String scenariosId = ShiroUtil.getOpenScenariosId();
        solutionCostService.editCost(scenariosId,cost);
        return "success";
    }

    /**
     * 编辑成本信息
     * @return
     */
    @RequestMapping(value = "/editRelay",method = RequestMethod.POST)
    @ResponseBody
    public String editRelay (HttpServletRequest request,Cost cost,List<SiteInfo> siteInfoList) {
        String scenariosId = ShiroUtil.getOpenScenariosId();
        solutionCostService.editCost(scenariosId,cost);
        for(int i=0;i<siteInfoList.size();i++) {
            solutionCostService.editSiteInfo(ShiroUtil.getOpenScenariosId(),siteInfoList.get(i).getSiteCode());
        }
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
        /*for(int i=0;i<siteCodeList.size();i++){
            SiteInfo siteInfo = siteInfoService.findSiteInfoBySiteCode(scenariosId, siteCodeList.get(i));
            String totalVol= solutionCostService.findTotalVol(scenariosId,siteCodeList.get(i));
            siteInfoList.put(siteCodeList.get(i),siteInfo);
            totalVolList.put(siteCodeList.get(i),totalVol);
        }*/
        //支线总运输成本
      //  Double branchTransportCost = solutionCostService.branchTransportCost();
         String branchTransportCost = solutionCostService.findBranchCost(scenariosId);

        int periodTime = modelType.equals("1") ? 10 : 20;
        int min = Integer.parseInt(demandInfoService.findMin(scenariosId));
        int max = Integer.parseInt(demandInfoService.findMax(scenariosId));
        List<String> siteList = solutionEfficiencyService.findAllSite(scenariosId);

        Map<String,Object> param2 = Maps.newHashMap();

        List<Route> carList;

        Integer sbVol;

        Integer unloadVol;

        for(int i=0;i<siteList.size();i++){
            String site = siteList.get(i);
            int deliverMaxNum = 0;
            int receivingMaxNum = 0;
            int maxNum = 0;

            for(int j=0;j<(max-min)/periodTime;j++) {
                param2.put("scenariosId",scenariosId);
                param2.put("curLoc",site);
                param2.put("min",min+(periodTime*j));
                param2.put("periodTime",periodTime);

                //发出票数
                sbVol = (null == solutionEfficiencyService.findSbVol(param2)?0:solutionEfficiencyService.findSbVol(param2));
                deliverMaxNum = deliverMaxNum > sbVol ? deliverMaxNum : sbVol;

                //到达票数
                unloadVol = (null == solutionEfficiencyService.findUnloadVol(param2)?0:solutionEfficiencyService.findUnloadVol(param2));
                receivingMaxNum = receivingMaxNum > unloadVol ? receivingMaxNum : unloadVol;
            }
            maxNum = deliverMaxNum > receivingMaxNum ? deliverMaxNum : receivingMaxNum;
            totalVolList.put(site, maxNum);
        }


        result.put("data",costList);
        result.put("modelType",modelType);
        result.put("totalPiece",totalPiece);
        result.put("siteInfoList",siteInfoList);
        result.put("totalVolList",totalVolList);
        result.put("branchTransportCost",branchTransportCost);

        return result;
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
        result.put("siteInfoList",siteInfoList);
        result.put("totalVolList",totalVolList);

        return result;
    }

    @RequestMapping(value = "/exportResult",method = RequestMethod.GET,produces = {"application/vnd.ms-excel;charset=UTF-8"})
    @ResponseBody
    public String exportResult(HttpServletResponse response,HttpServletRequest request) {
        Map<String, Object> planA = new HashMap<>();
        planA.put("plan", request.getParameter("plan"));
        planA.put("sitePeopleWork", request.getParameter("sitePeopleWork"));
        planA.put("distribPeopleWork", request.getParameter("distribPeopleWork"));
        planA.put("siteCount", request.getParameter("siteCount"));
        planA.put("peopleNumPerSite", request.getParameter("peopleNumPerSite"));
        planA.put("totalStaff", request.getParameter("siteCount"));
        planA.put("fullTimeStaff", request.getParameter("fullTimeStaff"));
        planA.put("partTimeStaff", request.getParameter("partTimeStaff"));
        planA.put("fullTimeSalary", request.getParameter("fullTimeSalary"));
        planA.put("fullTimeWorkDay", request.getParameter("fullTimeWorkDay"));
        planA.put("partTimeSalary", request.getParameter("partTimeSalary"));
        planA.put("partTimeWorkDay", request.getParameter("partTimeWorkDay"));
        planA.put("piece", request.getParameter("piece"));
        planA.put("sum1", request.getParameter("sum2"));
        planA.put("totalDailyLaborCost", request.getParameter("totalDailyLaborCost"));
        planA.put("branchTransportCost", request.getParameter("branchTransportCost"));
        planA.put("totalCost", request.getParameter("totalCost"));

        Map<String, Object> planB = new HashMap<>();
        planB.put("plan", request.getParameter("planB"));
        planB.put("sitePeopleWork", request.getParameter("sitePeopleWorkB"));
        planB.put("distribPeopleWork", request.getParameter("distribPeopleWorkB"));
        planB.put("siteCount", request.getParameter("siteCountB"));
        planB.put("peopleNumPerSite", request.getParameter("peopleNumPerSiteB"));
        planB.put("totalStaff", request.getParameter("siteCountB"));
        planB.put("fullTimeStaff", request.getParameter("fullTimeStaffB"));
        planB.put("partTimeStaff", request.getParameter("partTimeStaffB"));
        planB.put("fullTimeSalary", request.getParameter("fullTimeSalaryB"));
        planB.put("fullTimeWorkDay", request.getParameter("fullTimeWorkDayB"));
        planB.put("partTimeSalary", request.getParameter("partTimeSalaryB"));
        planB.put("partTimeWorkDay", request.getParameter("partTimeWorkDayB"));
        planB.put("piece", request.getParameter("pieceB"));
        planB.put("sum1", request.getParameter("sum1B"));
        planB.put("totalDailyLaborCost", request.getParameter("totalDailyLaborCostB"));
        planB.put("branchTransportCost", request.getParameter("branchTransportCostB"));
        planB.put("totalCost", request.getParameter("totalCostB"));

        response.setContentType("application/vnd.ms-excel;charset=utf-8");
        try {
            ServletOutputStream outputStream = response.getOutputStream();
            String fileName = new String(("Result_Costs").getBytes(), "utf-8");
            response.setCharacterEncoding("utf-8");
            response.setHeader("Content-disposition", "attachment; filename=" + fileName + ".xlsx");// 组装附件名称和格式
            String scenariosId = ShiroUtil.getOpenScenariosId();
            String modelType = myScenariosService.findById(Integer.parseInt(scenariosId)).getScenariosModel();
            String[] titles = { "串点模型", "接力模型", "综合模型" };
            solutionCostService.exportResult(scenariosId,titles,outputStream,modelType,planA,planB);
            System.out.println("outputStream:"+outputStream);
        }
        catch (IOException e) {
            e.printStackTrace();
            System.out.println("网络连接故障!错误信息:"+e.getMessage());
        }
        return null;
    }
}
