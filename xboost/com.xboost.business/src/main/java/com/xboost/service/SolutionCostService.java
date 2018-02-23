package com.xboost.service;

import com.google.common.collect.Maps;
import com.mckinsey.sf.data.Car;
import com.xboost.mapper.SolutionCostMapper;
import com.xboost.mapper.SolutionRouteMapper;
import com.xboost.pojo.Cost;
import com.xboost.mapper.CarMapper;
import com.xboost.pojo.Route;
import com.xboost.util.ExcelUtil;
import com.xboost.util.ExportUtil;
import com.xboost.util.ShiroUtil;
import com.xboost.util.Strings;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.*;
import org.joda.time.DateTime;
import org.omg.CORBA.ObjectHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.ServletOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by Administrator on 2017/11/5 0005.
 */

@Named
@Transactional
public class SolutionCostService {

    private static Logger logger = LoggerFactory.getLogger(SolutionCostService.class);

    @Inject
    private SolutionCostMapper solutionCostMapper;
    @Inject
    private SolutionRouteMapper solutionRouteMapper;
    @Inject
    private CarMapper carMapper;
    @Inject
    private SolutionEfficiencyService solutionEfficiencyService;
    @Inject
    private DemandInfoService demandInfoService;

    /**
     * 新增成本基础信息
     *
     * @param cost
     */
    public void add(Cost cost) {
        cost.setCreateTime(DateTime.now().toString("yyyy-MM-dd HH:mm"));
        solutionCostMapper.add(cost);
    }

    public void updateTotalCostByScenariosId(String totalCost, Integer scenariosId) {
        solutionCostMapper.updateTotalCostByScenariosId(totalCost, scenariosId);
    }


    /**
     * 查询成本信息
     * param
     *
     * @return
     */
    public List<Cost> findAll(String scenariosId) {
        return solutionCostMapper.findAll(scenariosId);
    }

    public String findBranchCost(String scenariosId) {
        return solutionCostMapper.findBranchCost(scenariosId);
    }


    /**
     * 根据参数查询成本信息
     * param param
     *
     * @return
     */
    public List<Cost> findByParam(Map<String, Object> param) {
        return solutionCostMapper.findByParam(param);
    }

    /**
     * 根据场景id查询成本信息
     *
     * @param scenariosId
     * @return
     */
    public Cost findByScenariosId(String scenariosId) {
        return solutionCostMapper.findByScenariosId(scenariosId);
    }


    /**
     * 编辑成本信息
     *
     * @param cost
     */
    public void editCost(String scenariosId, Cost cost) {
        solutionCostMapper.editCost(scenariosId, cost);
    }

    /**
     * 根据scenariosId和siteCode编辑site info 里的cost相关信息
     *
     * @param scenariosId,siteCode
     */
    public void editSiteInfo(String scenariosId, String siteCode) {
        solutionCostMapper.editSiteInfo(scenariosId, siteCode);
    }

    //查询网点总票数
    public String findTotalVol(String scenariosId, String siteCode) {
        return solutionCostMapper.findTotalVol(scenariosId, siteCode);
    }

    //查询总件量
    public Integer findTotalPiece(String scenariosId) {
        return solutionCostMapper.findTotalPiece(scenariosId);
    }

    /**
     * 根据id删除模型整体参数信息
     *
     * @param scenariosId
     */
    public void delByScenariosId(Integer scenariosId) {

        solutionCostMapper.delByScenariosId(scenariosId);
    }

    //支线运输成本
    public Double branchTransportCost() {
        //按KM计算成本
        Double branchCost = 0.00;
        //按包车计算成本
        Double branchCostRide = 0.00;
        String scenariosId = ShiroUtil.getOpenScenariosId();
        List<String> routeCountList = solutionRouteMapper.findRouteCount(scenariosId);
        String routeCount;
        for (int i = 0; i < routeCountList.size(); i++) {
            routeCount = routeCountList.get(i);
            Car car = carMapper.findCarCost(scenariosId, routeCount).get(0);

            Double velocity = car.getVelocity();
            Double totalDistance = Double.parseDouble(solutionRouteMapper.findTotalDistance(scenariosId, routeCount));


            int a1 = Integer.parseInt(Strings.isEmpty(car.getA1().trim()) ? "0" : car.getA1());
            int a2 = Integer.parseInt(Strings.isEmpty(car.getA2().trim()) ? "0" : car.getA2());
            int b1 = Integer.parseInt(Strings.isEmpty(car.getB1().trim()) ? "0" : car.getB1());
            int b2 = Integer.parseInt(Strings.isEmpty(car.getB2().trim()) ? "0" : car.getB2());

            int c1 = Integer.parseInt(Strings.isEmpty(car.getC1().trim()) ? "0" : car.getC1());

            int c2 = Integer.parseInt(Strings.isEmpty(car.getC2().trim()) ? "0" : car.getC2());
            int d1 = Integer.parseInt(Strings.isEmpty(car.getD1().trim()) ? "0" : car.getD1());
            int d2 = Integer.parseInt(Strings.isEmpty(car.getD2().trim()) ? "0" : car.getD2());
            int e1 = Integer.parseInt(Strings.isEmpty(car.getE1().trim()) ? "0" : car.getE1());
            int e2 = Integer.parseInt(Strings.isEmpty(car.getE2().trim()) ? "0" : car.getE2());
            int f1 = Integer.parseInt(Strings.isEmpty(car.getF1().trim()) ? "0" : car.getF1());
            int f2 = Integer.parseInt(Strings.isEmpty(car.getF2().trim()) ? "0" : car.getF2());

            //a1包车费用
            Double costa1 = Double.parseDouble(Strings.isEmpty(car.getCosta1().trim()) ? "0" : car.getCosta1());
            //每公里费用
            Double costa2 = Double.parseDouble(Strings.isEmpty(car.getCosta2().trim()) ? "0" : car.getCosta2());
            //每分钟费用
            Double costa3 = Double.parseDouble(Strings.isEmpty(car.getCosta3().trim()) ? "0" : car.getCosta3());

            Double costb1 = Double.parseDouble(Strings.isEmpty(car.getCostb1().trim()) ? "0" : car.getCostb1());
            Double costb2 = Double.parseDouble(Strings.isEmpty(car.getCostb2().trim()) ? "0" : car.getCostb2());
            Double costb3 = Double.parseDouble(Strings.isEmpty(car.getCostb3().trim()) ? "0" : car.getCostb3());

            Double costc1 = Double.parseDouble(Strings.isEmpty(car.getCostc1().trim()) ? "0" : car.getCostc1());
            Double costc2 = Double.parseDouble(Strings.isEmpty(car.getCostc2().trim()) ? "0" : car.getCostc2());
            Double costc3 = Double.parseDouble(Strings.isEmpty(car.getCostc3().trim()) ? "0" : car.getCostc3());

            Double costd1 = Double.parseDouble(Strings.isEmpty(car.getCostd1().trim()) ? "0" : car.getCostd1());
            Double costd2 = Double.parseDouble(Strings.isEmpty(car.getCostd2().trim()) ? "0" : car.getCostd2());
            Double costd3 = Double.parseDouble(Strings.isEmpty(car.getCostd3().trim()) ? "0" : car.getCostd3());

            Double coste1 = Double.parseDouble(Strings.isEmpty(car.getCoste1().trim()) ? "0" : car.getCoste1());
            Double coste2 = Double.parseDouble(Strings.isEmpty(car.getCoste2().trim()) ? "0" : car.getCoste2());
            Double coste3 = Double.parseDouble(Strings.isEmpty(car.getCoste3().trim()) ? "0" : car.getCoste3());

            Double costf1 = Double.parseDouble(Strings.isEmpty(car.getCostf1().trim()) ? "0" : car.getCostf1());
            Double costf2 = Double.parseDouble(Strings.isEmpty(car.getCostf2().trim()) ? "0" : car.getCostf2());
            Double costf3 = Double.parseDouble(Strings.isEmpty(car.getCostf3().trim()) ? "0" : car.getCostf3());

            if (totalDistance >= a1 && totalDistance <= a2) {
                branchCost = branchCost + costa1;
                branchCostRide = costa1;
            } else if (totalDistance > b1 && totalDistance <= b2) {
                branchCost = branchCost + costa1 + costb2 * (totalDistance - b1) + costb3 * ((totalDistance - b1) / velocity * 60);
                branchCostRide = costb1;
            } else if (totalDistance > c1 && totalDistance <= c2) {
                branchCost = branchCost + costa1 + costb2 * (b2 - b1) + costc2 * (totalDistance - c1) + costb3 * ((totalDistance - b1) / velocity * 60)
                        + costc3 * ((totalDistance - c1) / velocity * 60);
                branchCostRide = costc1;
            } else if (totalDistance > d1 && totalDistance <= d2) {
                branchCost = branchCost + costa1 + costb2 * (b2 - b1) + costc2 * (c2 - c1) + costd2 * (totalDistance - d1) + costb3 * ((totalDistance - b1) / velocity * 60)
                        + costc3 * ((totalDistance - c1) / velocity * 60) + costd3 * ((totalDistance - d1) / velocity * 60);
                branchCostRide = costd1;
            } else if (totalDistance > e1 && totalDistance <= e2) {
                branchCost = branchCost + costa1 + costb2 * (b2 - b1) + costc2 * (c2 - c1) + costd2 * (d2 - d1) + coste2 * (totalDistance - e1)
                        + costb3 * ((totalDistance - b1) / velocity * 60) + costc3 * ((totalDistance - c1) / velocity * 60)
                        + costd3 * ((totalDistance - d1) / velocity * 60) + coste3 * ((totalDistance - e1) / velocity * 60);
                branchCostRide = coste1;
            } else if (totalDistance > f1) {
                branchCost = branchCost + costa1 + costb2 * (b2 - b1) + costc2 * (c2 - c1) + costd2 * (d2 - d1) + coste2 * (e2 - e1)
                        + coste2 * (e2 - e1) + costf2 * (totalDistance - f1)
                        + costb3 * ((totalDistance - b1) / velocity * 60) + costc3 * ((totalDistance - c1) / velocity * 60)
                        + costd3 * ((totalDistance - d1) / velocity * 60) + coste3 * ((totalDistance - e1) / velocity * 60)
                        + costf3 * ((totalDistance - f1) / velocity * 60);
                branchCostRide = costf1;
            }

        }

        return branchCost;
    }

    /**
     * 导出excel
     *
     * @param scenariosId
     * @return
     */
    public void exportResult(String scenariosId, String[] titles, ServletOutputStream outputStream, String modelType, Map<String, Object> planA, Map<String, Object> planB) {
        // 创建一个workbook 对应一个excel应用文件
        XSSFWorkbook workBook = new XSSFWorkbook();
        // 在workbook中添加一个sheet,对应Excel文件中的sheet

        XSSFSheet sheet = workBook.createSheet("Costs");
        ExportUtil exportUtil = new ExportUtil(workBook, sheet);
        XSSFCellStyle headStyle = exportUtil.getHeadStyle();
        XSSFCellStyle bodyStyle = exportUtil.getBodyStyle();
        sheet.setColumnWidth(0, 47 * 250);
        sheet.setColumnWidth(2, 47 * 250);
        sheet.setDefaultRowHeight((short) 15);
        bodyStyle.setAlignment(HorizontalAlignment.LEFT);

        // 串点模型
        String[] column1 = {"人效", "depot人效 (p)", "distrib. center人效 (p)", "",
                "人员配备", "收端派端depot&distrib. center数量", "每个收端派端depot/distrib. center的人数", "收端派端depot&distrib. center总人数",
                " · Full-time Staff", " · Part-time Staff", "",
                "工资设定", " · Full-time salary (/month)", " · Full-time working days (/month)", " · Part-time wage (/hour)", " · Part-time working hours (/day)", "",
                "成本", "收端派端depot&distrib. center单日人工成本", "单日总体人工成本 (per piece)", "支线运输成本 (per piece)", "总成本 (per piece)"};

        String[] column2 = {"人效", "depot人效 (p)", "distrib. center人效 (p)", "", "",
                "支线  总票数", "支线  所需人数", " · Full-time Staff", " · Part-time Staff", "", "...", "",
                "工资设定", " · Full-time salary (/month)", " · Full-time working days (/month)", " · Part-time wage (/hour)", " · Part-time working hours (/day)", "",
                "成本", "支线depot单日人工成本", "支线distrib. center单日人工成本", "单日总体人工成本 (per piece)", "支线运输成本 (per piece)", "总成本 (per piece)"};

        String[] column3 = {"人效", "depot人效 (p)", "distrib. center人效 (p)", "",
                "人员配备", "收端派端depot&distrib. center数量", "每个收端派端depot/distrib. center的人数", "收端派端depot&distrib. center总人数",
                " · Full-time Staff", " · Part-time Staff", "",
                "支线  总票数", "支线  所需人数", " · Full-time Staff", " · Part-time Staff", "", "...", "", "工资设定", " · Full-time salary (/month)", " · Full-time working days (/month)", " · Part-time wage (/hour)", " · Part-time working hours (/day)", "",
                "成本", "收端派端depot&distrib. center单日人工成本", "支线depot单日人工成本", "支线distrib. center单日人工成本",
                "单日总体人工成本 (per piece)", "收端运输成本 (per piece)", "支线运输成本 (per piece)", "派端运输成本 (per piece)", "总成本 (per piece)"};

        // 串点模型 null需要根据数据库的值重新设置
        if ("1".equals(modelType)) {
            String[] grid = {"0,0,0,3", "1,1,0,1", "1,1,2,3", "2,2,0,1", "2,2,2,3", "3,3,0,1", "3,3,2,3", "6,6,0,1", "6,6,2,3", "7,7,0,1", "7,7,2,3",
                    "13,13,0,1", "13,13,2,3", "14,14,0,1", "14,14,2,3", "19,19,0,1", "19,19,2,3", "20,20,0,1", "20,20,2,3",};

            //动态合并单元格
            for (int i = 0; i < grid.length; i++) {
                String[] temp = grid[i].split(",");
                Integer startrow = Integer.parseInt(temp[0]);
                Integer overrow = Integer.parseInt(temp[1]);
                Integer startcol = Integer.parseInt(temp[2]);
                Integer overcol = Integer.parseInt(temp[3]);
                sheet.addMergedRegion(new CellRangeAddress(startrow, overrow,
                        startcol, overcol));
            }

            // 构建表头
            XSSFRow headRow = sheet.createRow(0);
            XSSFCell cell = null;
            for (int i = 0; i < 4; i++) {
                cell = headRow.createCell(i);
                cell.setCellValue("串点模型");
                cell.setCellStyle(headStyle);
            }
            XSSFRow nextHeadRow = sheet.createRow(1);
            for (int i = 0; i < 4; i++) {
                cell = nextHeadRow.createCell(i);
                if (i < 2) {
                    cell.setCellValue("Plan A");
                } else {
                    cell.setCellValue("Plan B");
                }
                cell.setCellStyle(headStyle);
            }
            XSSFRow nullRow = sheet.createRow(2);
            for (int i = 0; i < 4; i++) {
                cell = nullRow.createCell(i);
                cell.setCellValue("");
                cell.setCellStyle(bodyStyle);
            }

            // 构建表体数据与结构
            for (int i = 3; i < column1.length + 3; i++) {
                XSSFRow row = sheet.createRow(i);
                // 初始化所有的列
                for (int j = 0; j < 4; j++) {
                    cell = row.createCell(j);
                    cell.setCellValue("");
                    cell.setCellStyle(bodyStyle);
                }

                cell = row.createCell(0);
                cell.setCellValue(column1[i-3]);
                cell.setCellStyle(bodyStyle);

                cell = row.createCell(2);
                cell.setCellValue(column1[i-3]);
                cell.setCellStyle(bodyStyle);

                if(i == 4) {
                    cell = row.createCell(1);
                    cell.setCellValue(planA.get("sitePeopleWork")+"");
                    cell.setCellStyle(bodyStyle);

                    cell = row.createCell(3);
                    cell.setCellValue(planB.get("sitePeopleWork")+"");
                    cell.setCellStyle(bodyStyle);
                } else if (i == 5) {
                    cell = row.createCell(1);
                    cell.setCellValue(planA.get("distribPeopleWork")+"");
                    cell.setCellStyle(bodyStyle);

                    cell = row.createCell(3);
                    cell.setCellValue(planB.get("distribPeopleWork")+"");
                    cell.setCellStyle(bodyStyle);
                } else if (i == 8) {
                    cell = row.createCell(1);
                    cell.setCellValue(planA.get("siteCount")+"");
                    cell.setCellStyle(bodyStyle);

                    cell = row.createCell(3);
                    cell.setCellValue(planB.get("siteCount")+"");
                    cell.setCellStyle(bodyStyle);
                } else if(i == 9) {
                    cell = row.createCell(1);
                    cell.setCellValue(planA.get("peopleNumPerSite")+"");
                    cell.setCellStyle(bodyStyle);

                    cell = row.createCell(3);
                    cell.setCellValue(planB.get("peopleNumPerSite")+"");
                    cell.setCellStyle(bodyStyle);
                } else if (i == 10) {
                    cell = row.createCell(1);
                    cell.setCellValue(planA.get("totalStaff")+"");
                    cell.setCellStyle(bodyStyle);

                    cell = row.createCell(3);
                    cell.setCellValue(planB.get("totalStaff")+"");
                    cell.setCellStyle(bodyStyle);
                } else if (i == 11) {
                    cell = row.createCell(1);
                    cell.setCellValue(planA.get("fullTimeStaff")+"");
                    cell.setCellStyle(bodyStyle);

                    cell = row.createCell(3);
                    cell.setCellValue(planB.get("fullTimeStaff")+"");
                    cell.setCellStyle(bodyStyle);
                } else if (i == 12) {
                    cell = row.createCell(1);
                    cell.setCellValue(planA.get("partTimeStaff")+"");
                    cell.setCellStyle(bodyStyle);

                    cell = row.createCell(3);
                    cell.setCellValue(planB.get("partTimeStaff")+"");
                    cell.setCellStyle(bodyStyle);
                } else if (i == 15) {
                    cell = row.createCell(1);
                    cell.setCellValue(planA.get("fullTimeSalary")+"");
                    cell.setCellStyle(bodyStyle);

                    cell = row.createCell(3);
                    cell.setCellValue(planB.get("fullTimeSalary")+"");
                    cell.setCellStyle(bodyStyle);
                } else if (i == 16) {
                    cell = row.createCell(1);
                    cell.setCellValue(planA.get("fullTimeWorkDay")+"");
                    cell.setCellStyle(bodyStyle);

                    cell = row.createCell(3);
                    cell.setCellValue(planB.get("fullTimeWorkDay")+"");
                    cell.setCellStyle(bodyStyle);
                } else if (i == 17) {
                    cell = row.createCell(1);
                    cell.setCellValue(planA.get("partTimeSalary")+"");
                    cell.setCellStyle(bodyStyle);

                    cell = row.createCell(3);
                    cell.setCellValue(planB.get("partTimeSalary")+"");
                    cell.setCellStyle(bodyStyle);
                } else if (i == 18) {
                    cell = row.createCell(1);
                    cell.setCellValue(planA.get("partTimeWorkDay")+"");
                    cell.setCellStyle(bodyStyle);

                    cell = row.createCell(3);
                    cell.setCellValue(planB.get("partTimeWorkDay")+"");
                    cell.setCellStyle(bodyStyle);
                } else if (i == 21) {
                    cell = row.createCell(1);
                    cell.setCellValue(planA.get("piece")+"");
                    cell.setCellStyle(bodyStyle);

                    cell = row.createCell(3);
                    cell.setCellValue(planB.get("piece")+"");
                    cell.setCellStyle(bodyStyle);
                } else if (i == 22) {
                    cell = row.createCell(1);
                    cell.setCellValue(planA.get("sum1")+"");
                    cell.setCellStyle(bodyStyle);

                    cell = row.createCell(3);
                    cell.setCellValue(planB.get("sum1")+"");
                    cell.setCellStyle(bodyStyle);
                } else if (i == 23) {
                    cell = row.createCell(1);
                    cell.setCellValue(planA.get("totalDailyLaborCost")+"");
                    cell.setCellStyle(bodyStyle);

                    cell = row.createCell(3);
                    cell.setCellValue(planB.get("totalDailyLaborCost")+"");
                    cell.setCellStyle(bodyStyle);
                } else if (i == 24) {
                    cell = row.createCell(1);
                    cell.setCellValue(planA.get("branchTransportCost")+"");
                    cell.setCellStyle(bodyStyle);

                    cell = row.createCell(3);
                    cell.setCellValue(planB.get("branchTransportCost")+"");
                    cell.setCellStyle(bodyStyle);
                }
            }
        }

        String[] relayColumn1 = {"人效", "depot人效 (p)", "distrib. center人效 (p)", "", "",
                "支线  总票数", "支线  所需人数", " · Full-time Staff", " · Part-time Staff", "", "...", "",
                "工资设定", " · Full-time salary (/month)", " · Full-time working days (/month)", " · Part-time wage (/hour)", " · Part-time working hours (/day)", "",
                "成本", "支线depot单日人工成本", "支线distrib. center单日人工成本", "单日总体人工成本 (per piece)", "支线运输成本 (per piece)", "总成本 (per piece)"};

        String[] relayColumn2 = {"支线  总票数", "支线  所需人数", " · Full-time Staff", " · Part-time Staff"};

        String[] relayColumn3 = {"工资设定", " · Full-time salary (/month)", " · Full-time working days (/month)", " · Part-time wage (/hour)", " · Part-time working hours (/day)", "",
                "成本", "支线depot单日人工成本", "支线distrib. center单日人工成本", "单日总体人工成本 (per piece)", "支线运输成本 (per piece)", "总成本 (per piece)"};


        // 接力模型
        if ("2".equals(modelType)) {
            Map<String,Object> param = Maps.newHashMap();
            param.put("modelType",modelType);
            param.put("scenariosId",scenariosId);
            param.put("plan","A");
            Map<String,Object> resultA = Maps.newHashMap();
            List<Cost> costListA = findByParam(param);

            param.put("plan","B");
            Map<String,Object> resultB = Maps.newHashMap();
            List<Cost> costListB = findByParam(param);

            //总件量
            Integer totalPiece = findTotalPiece(scenariosId);
            //网点
            List<String> siteCodeList=solutionEfficiencyService.findAllSite(scenariosId);
            Map<String,Object> totalVolList=Maps.newHashMap();
            Map<String,Object> siteInfoList=Maps.newHashMap();

            String branchTransportCost = findBranchCost(scenariosId);

            int periodTime = 20;
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

            resultA.put("data",costListA);
            resultA.put("modelType",modelType);
            resultA.put("totalPiece",totalPiece);
            resultA.put("siteInfoList",siteInfoList);
            resultA.put("totalVolList",totalVolList);
            resultA.put("branchTransportCost",branchTransportCost);

            resultB.put("data",costListB);
            resultB.put("modelType",modelType);
            resultB.put("totalPiece",totalPiece);
            resultB.put("siteInfoList",siteInfoList);
            resultB.put("totalVolList",totalVolList);
            resultB.put("branchTransportCost",branchTransportCost);

            String[] grid = {"0,0,0,3", "1,1,0,1", "1,1,2,3", "2,2,0,1", "2,2,2,3", "3,3,0,1", "3,3,2,3", "6,6,0,1", "6,6,2,3"};

            //动态合并单元格
            for (int i = 0; i < grid.length; i++) {
                String[] temp = grid[i].split(",");
                Integer startrow = Integer.parseInt(temp[0]);
                Integer overrow = Integer.parseInt(temp[1]);
                Integer startcol = Integer.parseInt(temp[2]);
                Integer overcol = Integer.parseInt(temp[3]);
                sheet.addMergedRegion(new CellRangeAddress(startrow, overrow,
                        startcol, overcol));
            }

            // 构建表头
            XSSFRow headRow = sheet.createRow(0);
            XSSFCell cell = null;
            for (int i = 0; i < 4; i++) {
                cell = headRow.createCell(i);
                cell.setCellValue("接力模型");
                cell.setCellStyle(headStyle);
            }
            XSSFRow nextHeadRow = sheet.createRow(1);
            for (int i = 0; i < 4; i++) {
                cell = nextHeadRow.createCell(i);
                if (i < 2) {
                    cell.setCellValue("Plan A");
                } else {
                    cell.setCellValue("Plan B");
                }
                cell.setCellStyle(headStyle);
            }
            XSSFRow nullRow = sheet.createRow(2);
            for (int i = 0; i < 4; i++) {
                cell = nullRow.createCell(i);
                cell.setCellValue("");
                cell.setCellStyle(bodyStyle);
            }

            // 构建表体数据与结构
            for (int i = 3; i <  6; i++) {
                String[] efficiency = {"300", "500"};
                XSSFRow row = sheet.createRow(i);
                for (int j = 0; j < 4; j++) {
                    cell = row.createCell(j);
                    cell.setCellValue("");
                    cell.setCellStyle(bodyStyle);
                }

                cell = row.createCell(0);
                cell.setCellValue(relayColumn1[i-3]);
                cell.setCellStyle(bodyStyle);

                cell = row.createCell(2);
                cell.setCellValue(relayColumn1[i-3]);
                cell.setCellStyle(bodyStyle);

                if (i > 3) {
                    cell = row.createCell(1);
                    cell.setCellValue(efficiency[i-4]);
                    cell.setCellStyle(bodyStyle);

                    cell = row.createCell(3);
                    cell.setCellValue(efficiency[i-4]);
                    cell.setCellStyle(bodyStyle);
                }
            }

            int n = 7;
            Set<String> keySet = totalVolList.keySet();
            Iterator<String> keyIterator = keySet.iterator();
            for (int i = 0; i < totalVolList.size(); i++) {
                String key = keyIterator.next();
                Integer value = (Integer) totalVolList.get(key);
                String[] depotData = {value+"",(value/500+1)+"", "1","0"};
                for (int k = n; k < relayColumn2.length + n; k++) {
                    XSSFRow row = sheet.createRow(k);
                    for (int j = 0; j < 4; j++) {
                        cell = row.createCell(j);
                        cell.setCellValue("");
                        cell.setCellStyle(bodyStyle);
                    }
                    cell = row.createCell(0);
                    cell.setCellValue(relayColumn2[k-n]);
                    cell.setCellStyle(bodyStyle);

                    cell = row.createCell(2);
                    cell.setCellValue(relayColumn2[k-n]);
                    cell.setCellStyle(bodyStyle);

                    cell = row.createCell(1);
                    cell.setCellValue(depotData[k-n]);
                    cell.setCellStyle(bodyStyle);

                    cell = row.createCell(3);
                    cell.setCellValue(depotData[k-n]);
                    cell.setCellStyle(bodyStyle);
                }
                n += relayColumn2.length;
                XSSFRow row = sheet.createRow(n);
                for (int j = 0; j < 4; j++) {
                    cell = row.createCell(j);
                    cell.setCellValue("");
                    cell.setCellStyle(bodyStyle);
                }
                n++;
            }

            for (int i = n; i < n + relayColumn3.length; i++) {
                XSSFRow row = sheet.createRow(i);
                for (int j = 0; j < 4; j++) {
                    cell = row.createCell(j);
                    cell.setCellValue("");
                    cell.setCellStyle(bodyStyle);
                }

                cell = row.createCell(0);
                cell.setCellValue(relayColumn3[i - n]);
                cell.setCellStyle(bodyStyle);

                cell = row.createCell(2);
                cell.setCellValue(relayColumn3[i - n]);
                cell.setCellStyle(bodyStyle);
            }
        }

        // 综合模型
        /*if ("3".equals(modelType)) {
            String[] grid = {"0,0,0,3", "1,1,0,1", "1,1,2,3", "2,2,0,1", "2,2,2,3", "3,3,0,1", "3,3,2,3", "6,6,0,1", "6,6,2,3", "7,7,0,1", "7,7,2,3",
                    "13,13,0,1", "13,13,2,3", "18,18,0,1", "18,18,2,3", "20,20,0,1", "20,20,2,3", "21,21,0,1", "21,21,2,3", "26,26,0,1", "26,26,2,3",
                    "27,27,0,1", "27,27,2,3"};

            //动态合并单元格
            for (int i = 0; i < grid.length; i++) {
                String[] temp = grid[i].split(",");
                Integer startrow = Integer.parseInt(temp[0]);
                Integer overrow = Integer.parseInt(temp[1]);
                Integer startcol = Integer.parseInt(temp[2]);
                Integer overcol = Integer.parseInt(temp[3]);
                sheet.addMergedRegion(new CellRangeAddress(startrow, overrow,
                        startcol, overcol));
            }

            // 构建表头
            XSSFRow headRow = sheet.createRow(0);
            XSSFCell cell = null;
            for (int i = 0; i < 4; i++) {
                cell = headRow.createCell(i);
                cell.setCellValue("接力模型");
                cell.setCellStyle(headStyle);
            }
            XSSFRow nextHeadRow = sheet.createRow(1);
            for (int i = 0; i < 4; i++) {
                cell = nextHeadRow.createCell(i);
                if (i < 2) {
                    cell.setCellValue("Plan A");
                } else {
                    cell.setCellValue("Plan B");
                }
                cell.setCellStyle(headStyle);
            }
            XSSFRow nullRow = sheet.createRow(2);
            for (int i = 0; i < 4; i++) {
                cell = nullRow.createCell(i);
                cell.setCellValue("");
                cell.setCellStyle(bodyStyle);
            }

            // 构建表体数据与结构
            for (int i = 3; i < column3.length + 3; i++) {
                XSSFRow row = sheet.createRow(i);
                // 初始化所有的列
                for (int j = 0; j < 4; j++) {
                    cell = row.createCell(j);
                    cell.setCellValue("");
                    cell.setCellStyle(bodyStyle);
                }

                cell = row.createCell(0);
                cell.setCellValue(column3[i-3]);
                cell.setCellStyle(bodyStyle);

                cell = row.createCell(2);
                cell.setCellValue(column3[i-3]);
                cell.setCellStyle(bodyStyle);
            }
        }*/

        try {
//            FileOutputStream fout = new FileOutputStream("E:/Depots_info.xlsx");
//            workBook.write(fout);
//            fout.flush();
//            fout.close();
            workBook.write(outputStream);
            outputStream.flush();
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
