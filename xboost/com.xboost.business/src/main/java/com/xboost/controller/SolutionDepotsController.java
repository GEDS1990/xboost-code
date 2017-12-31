package com.xboost.controller;

import com.google.common.collect.Maps;
import com.xboost.pojo.Route;
import com.xboost.pojo.SiteInfo;
import com.xboost.service.SiteInfoService;
import com.xboost.service.SolutionRouteService;
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
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/11/5 0005.
 */

@Controller
@RequestMapping("/depots")
public class SolutionDepotsController {
    @Inject
    private SolutionRouteService solutionRouteService;
    @Inject
    private SiteInfoService siteInfoService;

    @RequestMapping(method = RequestMethod.GET)
    public String list() {
        return "results/depots";
    }


    //查询网点操作信息
    @RequestMapping(value = "/depots.json",method = RequestMethod.GET,produces = "application/json;charset=UTF-8")
    @ResponseBody
    public Map<String,Object> loadDepotsInfo(HttpServletRequest request) {

        String draw = request.getParameter("draw");
        Integer start;
        Integer length;
        if(request.getParameter("start")==null || request.getParameter("start")=="")
        {
            start = 0;
        }
        else{
            start =Integer.valueOf(request.getParameter("start"));
        }
        if(request.getParameter("length")==null || request.getParameter("length")=="")
        {
            length = 0;
        }
        else
        {
            length =Integer.valueOf(request.getParameter("length"));
        }
        String searchValue = request.getParameter("search[value]");
        String orderColumnIndex = request.getParameter("order[0][column]");
        String orderType = request.getParameter("order[0][dir]");
        String orderColumnName = request.getParameter("columns["+orderColumnIndex+"][name]");
        String scenariosId = ShiroUtil.getOpenScenariosId();
        String siteCode="";

        Map<String,Object> param = Maps.newHashMap();
        param.put("start",start);
        param.put("length",length);
        if(StringUtils.isNotEmpty(searchValue)) {
            param.put("keyword", Strings.toUTF8(searchValue));
            siteCode =Strings.toUTF8(searchValue);
        }
        param.put("orderColumn",orderColumnName);
        param.put("orderType",orderType);
        param.put("scenariosId",scenariosId);



        Map<String,Object> result = Maps.newHashMap();

        List<Map<String,Object>> siteList = siteInfoService.findBySiteCode(param); //.findAll();
        Integer count = siteInfoService.findAllCountBySiteCode(ShiroUtil.getOpenScenariosId());
        Integer filteredCount = siteInfoService.findCountBySiteCode(param);
    //    List<Map<String,Object>> nextSiteList = siteInfoService.findNextSite(scenariosId,siteCode);
        Double sbVolSum;
        Double unloadVolSum;
        String sbVol;
        String unloadVol;
        for(int i=0;i<siteList.size();i++)
        {
            Map<String,Object> site = siteList.get(i);
            for(int j=i+1;j<siteList.size();j++)
            {
                if(site.get("curLoc").equals(siteList.get(j).get("curLoc"))&&site.get("arrTime").equals(siteList.get(j).get("arrTime"))
                        &&site.get("carType").equals(siteList.get(j).get("carType"))){
                    sbVolSum = Double.parseDouble(site.get("sbVolSum").toString())
                            +Double.parseDouble(siteList.get(j).get("sbVolSum").toString());
                    unloadVolSum = Double.parseDouble(site.get("unloadVolSum").toString())
                            +Double.parseDouble(siteList.get(j).get("unloadVolSum").toString());
                    sbVol = (site.get("sbVol").equals("0")?"":site.get("sbVol").toString())
                            +(siteList.get(j).get("sbVol").equals("0")?"":siteList.get(j).get("sbVol").toString());
                    unloadVol = (site.get("unloadVol").equals("0")?"":site.get("unloadVol").toString())
                            +(siteList.get(j).get("unloadVol").equals("0")?"":(siteList.get(j).get("unloadVol").toString()));

                    siteList.get(i).put("sbVolSum",sbVolSum);
                    siteList.get(i).put("unloadVolSum",unloadVolSum);
                    siteList.get(i).put("sbVol",sbVol);
                    siteList.get(i).put("unloadVol",unloadVol);
                    siteList.remove(j);
                    count = count -1;
                    filteredCount = filteredCount-1;
                }

            }
        }

        result.put("draw",draw);
        result.put("recordsTotal",count); //总记录数
        result.put("recordsFiltered",filteredCount); //过滤出来的数量
        result.put("data",siteList);
 //       result.put("nextSite",nextSiteList);
        return result;
    }

    @RequestMapping(value = "/exportResult",method = RequestMethod.GET,produces = {"application/vnd.ms-excel;charset=UTF-8"})
    @ResponseBody
    public String exportResult(HttpServletResponse response)
    {
        response.setContentType("application/vnd.ms-excel;charset=utf-8");
        try
        {
            ServletOutputStream outputStream = response.getOutputStream();
            String fileName = new String(("Result_Depots").getBytes(), "utf-8");
            response.setCharacterEncoding("utf-8");
            response.setHeader("Content-disposition", "attachment; filename=" + fileName + ".xlsx");// 组装附件名称和格式
            String scenariosId = ShiroUtil.getOpenScenariosId();
            String[] titles = { "Depot ID","Incoming Vehicle","Arrival Time","Operation","Departure Time"};
            siteInfoService.exportResult(scenariosId,titles,outputStream);
            System.out.println("outputStream:"+outputStream);
        }
        catch (IOException e)
        {
            e.printStackTrace();
            System.out.println("网络连接故障!错误信息:"+e.getMessage());
        }
        return null;
    }

}
