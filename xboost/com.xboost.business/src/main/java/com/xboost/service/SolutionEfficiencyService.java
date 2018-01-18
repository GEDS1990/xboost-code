package com.xboost.service;

import com.google.common.collect.Maps;
import com.xboost.mapper.SolutionRouteMapper;
import com.xboost.mapper.SolutionEfficiencyMapper;
import com.xboost.pojo.Route;
import com.xboost.util.ExportUtil;
import com.xboost.util.ShiroUtil;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.*;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.ServletOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by Administrator on 2017/11/5 0005.
 */

@Named
@Transactional
public class SolutionEfficiencyService {
    private static Logger logger = LoggerFactory.getLogger(SolutionEfficiencyService.class);

    @Inject
    private SolutionRouteMapper solutionRouteMapper;
    @Inject
    private SolutionEfficiencyMapper solutionEfficiencyMapper;

    @Inject
    private DemandInfoService demandInfoService;

    /**
     * 根据场景id查询所有路线信息
     * param
     * @return
     */
    public List<Route> findAllRoute(String scenariosId) {
        return solutionRouteMapper.findAll(scenariosId);
    }

    /**
     * 查询所有网点
     * param
     * @return
     */
    public List<String> findAllSite(String scenariosId) {
        return solutionEfficiencyMapper.findAllSite(scenariosId);
    }

    //发出票数
    public Integer findSbVol(Map<String,Object> param){
        return solutionEfficiencyMapper.findSbVol(param);
    }

    //到达票数
    public Integer findUnloadVol(Map<String,Object> param){
        return solutionEfficiencyMapper.findUnloadVol(param);
    }

    //到达车辆
    public List<Route> findArrCar(Map<String,Object> param) {
        return solutionEfficiencyMapper.findArrCar(param);
    }

    //发出车辆
    public List<Route> findLeaveCar(Map<String,Object> param) {
        return solutionEfficiencyMapper.findLeaveCar(param);
    }

    //到达车辆数
    public Integer findArrCarCount(Map<String,Object> param) {
        return solutionEfficiencyMapper.findArrCarCount(param);
    }

    //发出车辆数
    public Integer findLeaveCarCount(Map<String,Object> param) {
        return solutionEfficiencyMapper.findLeaveCarCount(param);
    }

    //网点信息
    public List<Map<String,Object>> findSiteInfo(String scenariosId) {
        return solutionEfficiencyMapper.findSiteInfo(scenariosId);
    }

    /**
     *  导出excel
     * @param scenariosId
     * @return
     */
    public void exportResult(String scenariosId,String[] titles,ServletOutputStream outputStream ) {
        //发出车辆数
        int periodTime = 10;
        int min = Integer.parseInt(demandInfoService.findMin(scenariosId));
        int max = Integer.parseInt(demandInfoService.findMax(scenariosId));
        List<String> siteList = findAllSite(scenariosId);

        Map<String,Object> result = Maps.newHashMap();
        Map<String,Object> param = Maps.newHashMap();
        Integer leaveCarNum;
        List<Route> carList;

        Integer sbVol;

        Integer arrCarNum;

        Integer unloadVol;

        for(int i=0;i<siteList.size();i++){
            String site = siteList.get(i);
            int leaveALLNum = 0;
            int leaveMaxNum = 0;

            int deliveryALLNum = 0;
            int deliverMaxNum = 0;

            int arravingALLNum = 0;
            int arravingMaxNum = 0;

            int receivingALLNum = 0;
            int receivingMaxNum = 0;

            for(int j=0;j<(max-min)/periodTime;j++) {

                param.put("scenariosId",scenariosId);
                param.put("curLoc",site);
                param.put("min",min+(periodTime*j));
                param.put("periodTime",periodTime);
                //发出车辆数
                carList = findLeaveCar(param);
                leaveCarNum = findLeaveCarCount(param);
                for(int x=0;x<carList.size();x++) {
                    Route car = carList.get(x);
                    for(int y=x+1;y<carList.size();y++)
                    {
                        if(car.getRouteCount().equals(carList.get(y).getRouteCount())&&car.getCarType().equals(carList.get(y).getCarType())
                                &&car.getSequence().equals(carList.get(y).getSequence())){
                            leaveCarNum = leaveCarNum -1;
                        }

                    }
                }
                leaveALLNum += leaveCarNum;
                leaveMaxNum = leaveMaxNum > leaveCarNum ? leaveMaxNum : leaveCarNum;
                result.put(site+"-leaveALLNum", leaveALLNum);
                result.put(site+"-leaveMaxNum", leaveMaxNum);

                //发出票数
                sbVol = (null == findSbVol(param)?0:findSbVol(param));
                deliveryALLNum += sbVol;
                deliverMaxNum = deliverMaxNum > sbVol ? deliverMaxNum : sbVol;
                result.put(site+"-deliveryALLNum", deliveryALLNum);
                result.put(site+"-deliveryMaxNum", deliverMaxNum);

                //到达车辆数
                carList = findArrCar(param);
                arrCarNum = findArrCarCount(param);
                for(int x=0;x<carList.size();x++) {
                    Route car = carList.get(x);
                    for(int y=x+1;y<carList.size();y++) {
                        if(car.getRouteCount().equals(carList.get(y).getRouteCount())&&car.getSequence().equals(carList.get(y).getSequence())
                                &&car.getCarType().equals(carList.get(y).getCarType())){
                            arrCarNum = arrCarNum -1;
                        }

                    }
                }
                arravingALLNum += arrCarNum;
                arravingMaxNum = arravingMaxNum > arrCarNum ? arravingMaxNum : arrCarNum;
                result.put(site+"-arravingALLNum", arravingALLNum);
                result.put(site+"-arravingMaxNum", arravingMaxNum);

                //到达票数
                unloadVol = (null == findUnloadVol(param)?0:findUnloadVol(param));
                receivingALLNum += unloadVol;
                receivingMaxNum = receivingMaxNum > unloadVol ? receivingMaxNum : unloadVol;
                result.put(site+"-receivingALLNum", receivingALLNum);
                result.put(site+"-receivingMaxNum", receivingMaxNum);
            }
        }



        // 创建一个workbook 对应一个excel应用文件
        XSSFWorkbook workBook = new XSSFWorkbook();
        // 在workbook中添加一个sheet,对应Excel文件中的sheet

        XSSFSheet sheet = workBook.createSheet("Efficiency");
        ExportUtil exportUtil = new ExportUtil(workBook, sheet);
        XSSFCellStyle headStyle = exportUtil.getHeadStyle();
        XSSFCellStyle bodyStyle = exportUtil.getBodyStyle();
        sheet.setDefaultColumnWidth(13*250);
        sheet.setDefaultRowHeight((short)15);
        bodyStyle.setAlignment(HorizontalAlignment.LEFT);

        //对应excel中的行和列，下表从0开始{"开始行,结束行,开始列,结束列"}
        String[] headNum = { "0,0,0,3", "0,0,6,9", "0,0,12,15", "0,0,18,21", "0,0,24,27" };

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
        String[] nextTitles = { "装车网点", "峰值发出车次", "", "总发出量", "", "", "装车网点", "峰值发出票数", "", "总发出量", "", "",
                "卸车网点", "峰值到达车次", "", "总到达量", "", "", "卸车网点", "峰值到达票数", "", "总到达量", "" };
        XSSFRow headRow = sheet.createRow(0);
        XSSFCell cell = null;

        for (int i = 0; i < titles.length; i++) {
            cell = headRow.createCell(i);
            cell.setCellValue(titles[i]);
            cell.setCellStyle(headStyle);
        }

        XSSFRow nextHeadRow = sheet.createRow(1);
        for (int i = 0; i < nextTitles.length; i++) {
            cell = nextHeadRow.createCell(i);
            cell.setCellValue(nextTitles[i]);
            cell.setCellStyle(headStyle);
        }

        int departingTotal = 0;
        int deliveryTotal = 0;
        int arravingTotal = 0;
        int receivingTotal = 0;
        // 构建表体数据
        for (int i = 0; i < siteList.size(); i++) {
            XSSFRow row = sheet.createRow(i+2);
            cell = row.createCell(0);
            cell.setCellValue(siteList.get(i));
            cell.setCellStyle(bodyStyle);

            cell = row.createCell(1);
            cell.setCellValue((int) result.get(siteList.get(i)+"-leaveMaxNum"));
            cell.setCellStyle(bodyStyle);

            cell = row.createCell(3);
            cell.setCellValue((int) result.get(siteList.get(i)+"-leaveALLNum"));
            cell.setCellStyle(bodyStyle);

            departingTotal += (int) result.get(siteList.get(i)+"-leaveALLNum");

            cell = row.createCell(6);
            cell.setCellValue(siteList.get(i));
            cell.setCellStyle(bodyStyle);

            cell = row.createCell(7);
            cell.setCellValue((int) result.get(siteList.get(i)+"-deliveryMaxNum"));
            cell.setCellStyle(bodyStyle);

            cell = row.createCell(9);
            cell.setCellValue((int) result.get(siteList.get(i)+"-deliveryALLNum"));
            cell.setCellStyle(bodyStyle);

            deliveryTotal += (int) result.get(siteList.get(i)+"-deliveryALLNum");

            cell = row.createCell(12);
            cell.setCellValue(siteList.get(i));
            cell.setCellStyle(bodyStyle);

            cell = row.createCell(13);
            cell.setCellValue((int) result.get(siteList.get(i)+"-arravingMaxNum"));
            cell.setCellStyle(bodyStyle);

            cell = row.createCell(15);
            cell.setCellValue((int) result.get(siteList.get(i)+"-arravingALLNum"));
            cell.setCellStyle(bodyStyle);

            arravingTotal += (int) result.get(siteList.get(i)+"-arravingALLNum");

            cell = row.createCell(18);
            cell.setCellValue(siteList.get(i));
            cell.setCellStyle(bodyStyle);

            cell = row.createCell(19);
            cell.setCellValue((int) result.get(siteList.get(i)+"-receivingMaxNum"));
            cell.setCellStyle(bodyStyle);

            cell = row.createCell(21);
            cell.setCellValue((int) result.get(siteList.get(i)+"-receivingALLNum"));
            cell.setCellStyle(bodyStyle);

            receivingTotal += (int) result.get(siteList.get(i)+"-receivingALLNum");
        }

        cell = nextHeadRow.createCell(4);
        cell.setCellValue(departingTotal);
        cell.setCellStyle(headStyle);

        cell = nextHeadRow.createCell(10);
        cell.setCellValue(deliveryTotal);
        cell.setCellStyle(headStyle);

        cell = nextHeadRow.createCell(16);
        cell.setCellValue(arravingTotal);
        cell.setCellStyle(headStyle);

        cell = nextHeadRow.createCell(22);
        cell.setCellValue(receivingTotal);
        cell.setCellStyle(headStyle);

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
