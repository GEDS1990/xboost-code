package com.xboost.service;

import com.mckinsey.sf.data.Car;
import com.xboost.mapper.SolutionCostMapper;
import com.xboost.mapper.SolutionRouteMapper;
import com.xboost.pojo.Cost;
import com.xboost.mapper.CarMapper;
import com.xboost.util.ExcelUtil;
import com.xboost.util.ExportUtil;
import com.xboost.util.ShiroUtil;
import com.xboost.util.Strings;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.*;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.ServletOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

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

    /**
     * 新增成本基础信息
     * @param cost
     */
    public void add(Cost cost) {
        cost.setCreateTime(DateTime.now().toString("yyyy-MM-dd HH:mm"));
        solutionCostMapper.add(cost);
    }
    public void updateTotalCostByScenariosId(String totalCost,Integer scenariosId) {
        solutionCostMapper.updateTotalCostByScenariosId(totalCost,scenariosId);
    }


    /**
     * 查询成本信息
     * param
     * @return
     */
    public List<Cost> findAll(String scenariosId) {
        return solutionCostMapper.findAll(scenariosId);
    }

    public String findBranchCost(String scenariosId){
        return solutionCostMapper.findBranchCost(scenariosId);
    }


    /**
     * 根据参数查询成本信息
     * param param
     * @return
     */
    public List<Cost> findByParam(Map<String, Object> param) {
        return solutionCostMapper.findByParam(param);
    }

    /**
     * 根据场景id查询成本信息
     * @param scenariosId
     * @return
     */
    public Cost findByScenariosId(String scenariosId) {
        return solutionCostMapper.findByScenariosId(scenariosId);
    }


    /**
     * 编辑成本信息
     * @param cost
     */
    public void editCost(String scenariosId,Cost cost) {
        solutionCostMapper.editCost(scenariosId,cost);
    }

    /**
     * 根据scenariosId和siteCode编辑site info 里的cost相关信息
     * @param scenariosId,siteCode
     */
    public void editSiteInfo(String scenariosId,String siteCode) {
        solutionCostMapper.editSiteInfo(scenariosId,siteCode);
    }

    //查询总件量
    public Integer findTotalPiece(String scenariosId){
        return solutionCostMapper.findTotalPiece(scenariosId);
    }

    /**
     * 根据id删除模型整体参数信息
     * @param scenariosId
     */
    public void delByScenariosId(Integer scenariosId) {

        solutionCostMapper.delByScenariosId(scenariosId);
    }

    //支线运输成本
    public Double branchTransportCost()
    {
        //按KM计算成本
        Double branchCost=0.00;
        //按包车计算成本
        Double branchCostRide=0.00;
        String scenariosId = ShiroUtil.getOpenScenariosId();
        List<String> routeCountList = solutionRouteMapper.findRouteCount(scenariosId);
        String routeCount;
        for(int i=0;i<routeCountList.size();i++){
            routeCount = routeCountList.get(i);
            Car car = carMapper.findCarCost(scenariosId,routeCount).get(0);

            Double velocity = car.getVelocity();
            Double totalDistance = Double.parseDouble(solutionRouteMapper.findTotalDistance(scenariosId,routeCount));


            int a1=Integer.parseInt(Strings.isEmpty(car.getA1().trim())?"0":car.getA1());
            int a2=Integer.parseInt(Strings.isEmpty(car.getA2().trim())?"0":car.getA2());
            int b1=Integer.parseInt(Strings.isEmpty(car.getB1().trim())?"0":car.getB1());
            int b2=Integer.parseInt(Strings.isEmpty(car.getB2().trim())?"0":car.getB2());

            int c1=Integer.parseInt(Strings.isEmpty(car.getC1().trim())?"0":car.getC1());

            int c2=Integer.parseInt(Strings.isEmpty(car.getC2().trim())?"0":car.getC2());
            int d1=Integer.parseInt(Strings.isEmpty(car.getD1().trim())?"0":car.getD1());
            int d2=Integer.parseInt(Strings.isEmpty(car.getD2().trim())?"0":car.getD2());
            int e1=Integer.parseInt(Strings.isEmpty(car.getE1().trim())?"0":car.getE1());
            int e2=Integer.parseInt(Strings.isEmpty(car.getE2().trim())?"0":car.getE2());
            int f1=Integer.parseInt(Strings.isEmpty(car.getF1().trim())?"0":car.getF1());
            int f2=Integer.parseInt(Strings.isEmpty(car.getF2().trim())?"0":car.getF2());

            //a1包车费用
            Double costa1=Double.parseDouble(Strings.isEmpty(car.getCosta1().trim())?"0":car.getCosta1());
            //每公里费用
            Double costa2=Double.parseDouble(Strings.isEmpty(car.getCosta2().trim())?"0":car.getCosta2());
            //每分钟费用
            Double costa3=Double.parseDouble(Strings.isEmpty(car.getCosta3().trim())?"0":car.getCosta3());

            Double costb1=Double.parseDouble(Strings.isEmpty(car.getCostb1().trim())?"0":car.getCostb1());
            Double costb2=Double.parseDouble(Strings.isEmpty(car.getCostb2().trim())?"0":car.getCostb2());
            Double costb3=Double.parseDouble(Strings.isEmpty(car.getCostb3().trim())?"0":car.getCostb3());

            Double costc1=Double.parseDouble(Strings.isEmpty(car.getCostc1().trim())?"0":car.getCostc1());
            Double costc2=Double.parseDouble(Strings.isEmpty(car.getCostc2().trim())?"0":car.getCostc2());
            Double costc3=Double.parseDouble(Strings.isEmpty(car.getCostc3().trim())?"0":car.getCostc3());

            Double costd1=Double.parseDouble(Strings.isEmpty(car.getCostd1().trim())?"0":car.getCostd1());
            Double costd2=Double.parseDouble(Strings.isEmpty(car.getCostd2().trim())?"0":car.getCostd2());
            Double costd3=Double.parseDouble(Strings.isEmpty(car.getCostd3().trim())?"0":car.getCostd3());

            Double coste1=Double.parseDouble(Strings.isEmpty(car.getCoste1().trim())?"0":car.getCoste1());
            Double coste2=Double.parseDouble(Strings.isEmpty(car.getCoste2().trim())?"0":car.getCoste2());
            Double coste3=Double.parseDouble(Strings.isEmpty(car.getCoste3().trim())?"0":car.getCoste3());

            Double costf1=Double.parseDouble(Strings.isEmpty(car.getCostf1().trim())?"0":car.getCostf1());
            Double costf2=Double.parseDouble(Strings.isEmpty(car.getCostf2().trim())?"0":car.getCostf2());
            Double costf3=Double.parseDouble(Strings.isEmpty(car.getCostf3().trim())?"0":car.getCostf3());

            if(totalDistance>=a1 && totalDistance<=a2){
                branchCost = branchCost + costa1;
                branchCostRide = costa1;
            }
            else if(totalDistance>b1 && totalDistance<=b2){
                branchCost = branchCost + costa1 + costb2*(totalDistance-b1) + costb3*((totalDistance-b1)/velocity*60);
                branchCostRide = costb1;
            }
            else if(totalDistance>c1 && totalDistance<=c2){
                branchCost = branchCost + costa1 + costb2*(b2-b1) + costc2*(totalDistance-c1) + costb3*((totalDistance-b1)/velocity*60)
                        + costc3*((totalDistance-c1)/velocity*60);
                branchCostRide = costc1;
            }
            else if(totalDistance>d1 && totalDistance<=d2){
                branchCost = branchCost + costa1 + costb2*(b2-b1) + costc2*(c2-c1) + costd2*(totalDistance-d1)+costb3*((totalDistance-b1)/velocity*60)
                        + costc3*((totalDistance-c1)/velocity*60)+ costd3*((totalDistance-d1)/velocity*60);
                branchCostRide = costd1;
            }
            else if(totalDistance>e1 && totalDistance<=e2){
                branchCost = branchCost + costa1 + costb2*(b2-b1) + costc2*(c2-c1) + costd2*(d2-d1) + coste2*(totalDistance-e1)
                        + costb3*((totalDistance-b1)/velocity*60) + costc3*((totalDistance-c1)/velocity*60)
                        + costd3*((totalDistance-d1)/velocity*60) + coste3*((totalDistance-e1)/velocity*60);
                branchCostRide = coste1;
            }
            else if(totalDistance>f1){
                branchCost = branchCost + costa1 + costb2*(b2-b1) + costc2*(c2-c1) + costd2*(d2-d1) + coste2*(e2-e1)
                        + coste2*(e2-e1) + costf2*(totalDistance-f1)
                        + costb3*((totalDistance-b1)/velocity*60) + costc3*((totalDistance-c1)/velocity*60)
                        + costd3*((totalDistance-d1)/velocity*60) + coste3*((totalDistance-e1)/velocity*60)
                        + costf3*((totalDistance-f1)/velocity*60);
                branchCostRide = costf1;
            }

        }

        return branchCost;
    }

    /**
     *  导出excel
     * @param scenariosId
     * @return
     */
    public void exportResult(String scenariosId,String[] titles,ServletOutputStream outputStream ) {
        List<Cost> list = solutionCostMapper.findAll(scenariosId);
        // 创建一个workbook 对应一个excel应用文件
        XSSFWorkbook workBook = new XSSFWorkbook();
        // 在workbook中添加一个sheet,对应Excel文件中的sheet

        XSSFSheet sheet = workBook.createSheet("Costs");
        ExportUtil exportUtil = new ExportUtil(workBook, sheet);
        XSSFCellStyle headStyle = exportUtil.getHeadStyle();
        XSSFCellStyle bodyStyle = exportUtil.getBodyStyle();
        sheet.setColumnWidth(0,47*250);
        sheet.setColumnWidth(2,47*250);
        sheet.setColumnWidth(5,47*250);
        sheet.setColumnWidth(7,47*250);
        sheet.setColumnWidth(10,47*250);
        sheet.setColumnWidth(12,47*250);
        sheet.setDefaultRowHeight((short)15);
        bodyStyle.setAlignment(HorizontalAlignment.LEFT);

        for (int i = 0; i < 35; i++) {
            XSSFRow row = sheet.createRow(i);
            XSSFCell cell = null;
            for (int j = 0; j < 14; j++) {
                cell = row.createCell(j);
                cell.setCellValue("");
                cell.setCellStyle(bodyStyle);
            }
        }

        //对应excel中的行和列，下表从0开始{"开始行,结束行,开始列,结束列"}
        String[] headNum = { "0,0,0,3", "0,0,5,8", "0,0,10,13",
                            "1,1,0,1", "1,1,2,3", "1,1,5,6", "1,1,7,8", "1,1,10,11", "1,1,12,13",
                            "2,2,0,1", "2,2,2,3", "2,2,5,6", "2,2,7,8", "2,2,10,11", "2,2,12,13",
                            "3,3,0,1", "3,3,2,3", "3,3,5,6", "3,3,7,8", "3,3,10,11", "3,3,12,13",
                            "6,6,0,1", "6,6,2,3", "6,6,5,6", "6,6,7,8", "6,6,10,11", "6,6,12,13",
                            "7,7,0,1", "7,7,2,3", "7,7,5,6", "7,7,7,8", "7,7,10,11", "7,7,12,13",
                            "13,13,0,1", "13,13,2,3", "14,14,0,1", "14,14,2,3",
                            "19,19,0,1", "19,19,2,3", "20,20,0,1", "20,20,2,3",
                            "12,12,5,6", "12,12,7,8", "14,14,5,6", "14,14,7,8",
                            "15,15,5,6", "15,15,7,8", "20,20,5,6", "20,20,7,8", "21,21,5,6", "21,21,7,8",
                            "13,13,10,11", "13,13,12,13", "18,18,10,11", "18,18,12,13",
                            "20,20,10,11", "20,20,12,13", "26,26,10,11", "26,26,12,13", "27,27,10,11", "27,27,12,13"};

        //动态合并单元格
        for (int i = 0; i < headNum.length; i++) {
            String[] temp = headNum[i].split(",");
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
        for (int i = 0; i < 14; i++) {
            cell = headRow.createCell(i);
            if(i == 4 || i == 9) {
                cell.setCellStyle(headStyle);
                continue;
            }
            if(i < 4) {
                cell.setCellValue(titles[0]);
            } else if(i < 9) {
                cell.setCellValue(titles[1]);
            } else {
                cell.setCellValue(titles[2]);
            }
            cell.setCellStyle(headStyle);
        }

        String[] nextTitles = { "Plan A", "Plan B" };
        XSSFRow nextHeadRow = sheet.createRow(1);
        for (int i = 0; i < 14; i++) {
            cell = nextHeadRow.createCell(i);
            switch (i) {
                case 0: cell.setCellValue(nextTitles[0]);
                    break;
                case 1: cell.setCellValue(nextTitles[0]);
                    break;
                case 2: cell.setCellValue(nextTitles[1]);
                    break;
                case 3: cell.setCellValue(nextTitles[1]);
                    break;
                case 4:
                    break;
                case 5: cell.setCellValue(nextTitles[0]);
                    break;
                case 6: cell.setCellValue(nextTitles[0]);
                    break;
                case 7: cell.setCellValue(nextTitles[1]);
                    break;
                case 8: cell.setCellValue(nextTitles[1]);
                    break;
                case 9:
                    break;
                case 10: cell.setCellValue(nextTitles[0]);
                    break;
                case 11: cell.setCellValue(nextTitles[0]);
                    break;
                case 12: cell.setCellValue(nextTitles[1]);
                    break;
                case 13: cell.setCellValue(nextTitles[1]);
                    break;
            }
            cell.setCellStyle(headStyle);
        }

        // 串点模型
        String[] column1 = { "人效", "depot人效 (p)", "distrib. center人效 (p)", "",
                "人员配备", "收端派端depot&distrib. center数量", "每个收端派端depot/distrib. center的人数", "收端派端depot&distrib. center总人数",
                " · Full-time Staff", " · Part-time Staff", "",
                "工资设定", " · Full-time salary (/month)", " · Full-time working days (/month)", " · Part-time wage (/hour)", " · Part-time working hours (/day)", "",
                "成本", "收端派端depot&distrib. center单日人工成本", "单日总体人工成本 (per piece)", "支线运输成本 (per piece)", "总成本 (per piece)",
                "", "", "", "", "", "", "", "", "","", ""};

        String[] column2 = { "人效", "depot人效 (p)", "distrib. center人效 (p)", "", "",
                "支线  总票数", "支线  所需人数", " · Full-time Staff", " · Part-time Staff", "", "...", "",
                "工资设定", " · Full-time salary (/month)", " · Full-time working days (/month)", " · Part-time wage (/hour)", " · Part-time working hours (/day)", "",
                "成本", "支线depot单日人工成本", "支线distrib. center单日人工成本", "单日总体人工成本 (per piece)", "支线运输成本 (per piece)", "总成本 (per piece)",
                "", "", "", "", "", "", "", "", "" };

        String[] column3 = { "人效", "depot人效 (p)", "distrib. center人效 (p)", "",
                "人员配备", "收端派端depot&distrib. center数量", "每个收端派端depot/distrib. center的人数", "收端派端depot&distrib. center总人数",
                " · Full-time Staff", " · Part-time Staff", "",
                "支线  总票数", "支线  所需人数", " · Full-time Staff", " · Part-time Staff", "", "...", "","工资设定", " · Full-time salary (/month)", " · Full-time working days (/month)", " · Part-time wage (/hour)", " · Part-time working hours (/day)", "",
                "成本", "收端派端depot&distrib. center单日人工成本", "支线depot单日人工成本", "支线distrib. center单日人工成本",
                "单日总体人工成本 (per piece)", "收端运输成本 (per piece)", "支线运输成本 (per piece)", "派端运输成本 (per piece)", "总成本 (per piece)" };

        Cost CDA = new Cost();
        Cost CDB = new Cost();
        Cost JLA = new Cost();
        Cost JLB = new Cost();
        Cost ZHA = new Cost();
        Cost ZHB = new Cost();
        //cost.getModelType().equals("串点模型")...等需要根据实际的值进行判断，后期改
        for ( Cost cost : list ) {
            if("串点模型".equals(cost.getModelType()) && cost.getPlan().equals("A")) {
                CDA = cost;
            }
            if("串点模型".equals(cost.getModelType()) && cost.getPlan().equals("B")) {
                CDB = cost;
            }
            if("接力模型".equals(cost.getModelType()) && cost.getPlan().equals("A")) {
                JLA = cost;
            }
            if("接力模型".equals(cost.getModelType()) && cost.getPlan().equals("B")) {
                JLB = cost;
            }
            if("综合模型".equals(cost.getModelType()) && cost.getPlan().equals("A")) {
                ZHA = cost;
            }
            if("综合模型".equals(cost.getModelType()) && cost.getPlan().equals("B")) {
                ZHB = cost;
            }
        }
        // 构建表体结构
        for (int i = 3; i < 36; i++) {
            XSSFRow row = sheet.createRow(i);
            for(int j = 0; j < 14; j++) {
                cell = row.createCell(j);
                cell.setCellValue("");
                cell.setCellStyle(bodyStyle);
            }
            if(i == 4) {
                cell = row.createCell(1);
                cell.setCellValue(list.get(0).getSitePeopleWork());
                cell.setCellStyle(bodyStyle);

                cell = row.createCell(3);
                cell.setCellValue(list.get(0).getSitePeopleWork());
                cell.setCellStyle(bodyStyle);
            } else if (i == 5) {
                cell = row.createCell(1);
                cell.setCellValue(list.get(0).getDistribPeopleWork());
                cell.setCellStyle(bodyStyle);

                cell = row.createCell(3);
                cell.setCellValue(list.get(0).getDistribPeopleWork());
                cell.setCellStyle(bodyStyle);
            } else if (i == 8) {
                cell = row.createCell(1);
                cell.setCellValue(list.get(0).getSiteCount());
                cell.setCellStyle(bodyStyle);

                cell = row.createCell(3);
                cell.setCellValue(list.get(0).getSiteCount());
                cell.setCellStyle(bodyStyle);
            } else if(i == 9) {
                cell = row.createCell(1);
                cell.setCellValue(list.get(0).getPeopleNumPerSite());
                cell.setCellStyle(bodyStyle);

                cell = row.createCell(3);
                cell.setCellValue(list.get(0).getPeopleNumPerSite());
                cell.setCellStyle(bodyStyle);
            } else if (i == 10) {
                cell = row.createCell(1);
                cell.setCellValue(list.get(0).getSiteCount());
                cell.setCellStyle(bodyStyle);

                cell = row.createCell(3);
                cell.setCellValue(list.get(0).getSiteCount());
                cell.setCellStyle(bodyStyle);
            } else if (i == 11) {
                cell = row.createCell(1);
                cell.setCellValue(list.get(0).getFullTimeStaff());
                cell.setCellStyle(bodyStyle);

                cell = row.createCell(3);
                cell.setCellValue(list.get(0).getFullTimeStaff());
                cell.setCellStyle(bodyStyle);
            } else if (i == 12) {
                cell = row.createCell(1);
                cell.setCellValue(list.get(0).getPartTimeStaff());
                cell.setCellStyle(bodyStyle);

                cell = row.createCell(3);
                cell.setCellValue(list.get(0).getPartTimeStaff());
                cell.setCellStyle(bodyStyle);
            } else if (i == 15) {
                cell = row.createCell(1);
                cell.setCellValue(list.get(0).getFullTimeSalary());
                cell.setCellStyle(bodyStyle);

                cell = row.createCell(3);
                cell.setCellValue(list.get(0).getFullTimeSalary());
                cell.setCellStyle(bodyStyle);
            } else if (i == 16) {
                cell = row.createCell(1);
                cell.setCellValue(list.get(0).getFullTimeWorkDay());
                cell.setCellStyle(bodyStyle);

                cell = row.createCell(3);
                cell.setCellValue(list.get(0).getFullTimeWorkDay());
                cell.setCellStyle(bodyStyle);
            } else if (i == 17) {
                cell = row.createCell(1);
                cell.setCellValue(list.get(0).getPartTimeSalary());
                cell.setCellStyle(bodyStyle);

                cell = row.createCell(3);
                cell.setCellValue(list.get(0).getPartTimeSalary());
                cell.setCellStyle(bodyStyle);
            } else if (i == 18) {
                cell = row.createCell(1);
                cell.setCellValue(list.get(0).getPartTimeWorkDay());
                cell.setCellStyle(bodyStyle);

                cell = row.createCell(3);
                cell.setCellValue(list.get(0).getPartTimeWorkDay());
                cell.setCellStyle(bodyStyle);
            } else if (i == 21) {
                cell = row.createCell(1);
                cell.setCellValue("");
                cell.setCellStyle(bodyStyle);

                cell = row.createCell(3);
                cell.setCellValue("");
                cell.setCellStyle(bodyStyle);
            } else if (i == 22) {
                cell = row.createCell(1);
                cell.setCellValue(list.get(0).getSum1());
                cell.setCellStyle(bodyStyle);

                cell = row.createCell(3);
                cell.setCellValue(list.get(0).getSum1());
                cell.setCellStyle(bodyStyle);
            } else if (i == 23) {
                cell = row.createCell(1);
                cell.setCellValue(list.get(0).getTotalDailyLaborCost());
                cell.setCellStyle(bodyStyle);

                cell = row.createCell(3);
                cell.setCellValue(list.get(0).getTotalDailyLaborCost());
                cell.setCellStyle(bodyStyle);
            } else if (i == 24) {
                cell = row.createCell(1);
                cell.setCellValue(list.get(0).getBranchTransportCost());
                cell.setCellStyle(bodyStyle);

                cell = row.createCell(3);
                cell.setCellValue(list.get(0).getBranchTransportCost());
                cell.setCellStyle(bodyStyle);
            }

            cell = row.createCell(0);
            cell.setCellValue(column1[i-3]);
            cell.setCellStyle(bodyStyle);

            cell = row.createCell(2);
            cell.setCellValue(column1[i-3]);
            cell.setCellStyle(bodyStyle);

            cell = row.createCell(5);
            cell.setCellValue(column2[i-3]);
            cell.setCellStyle(bodyStyle);

            cell = row.createCell(7);
            cell.setCellValue(column2[i-3]);
            cell.setCellStyle(bodyStyle);

            cell = row.createCell(10);
            cell.setCellValue(column3[i-3]);
            cell.setCellStyle(bodyStyle);

            cell = row.createCell(12);
            cell.setCellValue(column3[i-3]);
            cell.setCellStyle(bodyStyle);
        }



        try
        {
//            FileOutputStream fout = new FileOutputStream("E:/Depots_info.xlsx");
//            workBook.write(fout);
//            fout.flush();
//            fout.close();
            workBook.write(outputStream);
            outputStream.flush();
            outputStream.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

}
