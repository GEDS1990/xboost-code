package com.xboost.service;

import com.xboost.mapper.SolutionRouteMapper;
import com.xboost.mapper.SolutionVehiclesMapper;
import com.xboost.pojo.Route;
import com.xboost.util.ExportUtil;
import com.xboost.util.ShiroUtil;
import org.apache.poi.xssf.usermodel.*;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.ServletOutputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/11/5 0005.
 */

@Named
@Transactional
public class SolutionVehiclesService {
    private static Logger logger = LoggerFactory.getLogger(SolutionVehiclesService.class);

    @Inject
    private SolutionVehiclesMapper solutionVehiclesMapper;
    @Inject
    private MyScenariosService myScenariosService;

    /**
     * 根据carType获取线路信息
     * param param
     * @return
     */
    public List<Map<String, Object>> findByCar(Map<String, Object> param) {
        return solutionVehiclesMapper.findByCar(param);
    }

    /**
     * 根据carType获取线路信息
     * param param
     * @return
     */
    public List<Map<String, Object>> findByCarRealy(Map<String, Object> param) {
        return solutionVehiclesMapper.findByCarRealy(param);
    }

    /**
     * 根据carType获取线路数量
     * @param param
     * @return
     */
    public Integer findCountByCar(Map<String, Object> param) {
        return solutionVehiclesMapper.findCountByCar(param).intValue();
    }

    /**
     * 根据carType获取线路总数量
     * @param scenariosId
     * @return
     */
    public Integer findAllCountByCar(String scenariosId) {
        return solutionVehiclesMapper.findAllCountByCar(scenariosId).intValue();
    }

    /**
     *  查询已使用的车数
     * @param scenariosId
     * @return
     */
    public Integer findBusyCarCount(String scenariosId) {
        return solutionVehiclesMapper.findBusyCarCount(scenariosId).intValue();
    }

    public String timeTransfer(String time){
        Double result = Math.floor(Double.parseDouble(time));
        Integer h = (int)(result/60);
        Integer m = (int)(result%60);
        String t = "";
        if(m >= 0 && m <= 9) {
            t = h + ":0" + m;
        }else {
            t = h + ":" + m;
        }
        return t;
    }

    /**
     *  导出excel
     * @param scenariosId
     * @return
     */
    public void exportResult(String scenariosId,String[] titles,ServletOutputStream outputStream ) {
        List<Map<String, Object>> list = solutionVehiclesMapper.findAllByCar(scenariosId);

        String modelType = myScenariosService.findById(Integer.parseInt(ShiroUtil.getOpenScenariosId())).getScenariosModel();
        String sbVol;
        String unloadVol;
        if ("1".equals(modelType)) {
            for (int i = list.size()-1; i >= 0; i--) {
                String curLoc = (String) list.get(i).get("curLoc");
                String nextCurLoc = (String) list.get(i).get("nextCurLoc");

                if (curLoc.equals(nextCurLoc)) {
                    list.remove(i);
                }

/*                String curLoc2 = (String) list.get(i-1).get("curLoc");
                String nextCurLoc2 = (String) list.get(i-1).get("nextCurLoc");
                String calcDis2 = (String) list.get(i-1).get("calcDis");*/

                /*if (curLoc.equals(curLoc2)) {
                    if (curLoc.equals(nextCurLoc)) {
                        list.remove(i);
                    }
                    if (curLoc2.equals(nextCurLoc2)) {
                        list.remove(i-1);
                    }
                }*/
            }
        }

        // 创建一个workbook 对应一个excel应用文件
        XSSFWorkbook workBook = new XSSFWorkbook();
        // 在workbook中添加一个sheet,对应Excel文件中的sheet

        XSSFSheet sheet = workBook.createSheet("Vehicle");
        ExportUtil exportUtil = new ExportUtil(workBook, sheet);
        XSSFCellStyle headStyle = exportUtil.getHeadStyle();
        XSSFCellStyle bodyStyle = exportUtil.getBodyStyle();
        sheet.setColumnWidth(6, 50 * 250);
        sheet.setDefaultRowHeight((short) 36);
        // 构建表头
        XSSFRow headRow = sheet.createRow(0);
        XSSFCell cell = null;

        for (int i = 0; i < titles.length; i++) {
            cell = headRow.createCell(i);
            cell.setCellValue(titles[i]);
            cell.setCellStyle(headStyle);
        }
        // 构建表体数据
        if (list != null && list.size() > 0) {
            for (int j = 0; j < list.size(); j++) {
                XSSFRow bodyRow = sheet.createRow(j + 1);
                Map<String,Object> vehicle = list.get(j);

                int i = 0;
                cell = bodyRow.createCell(i++);
                cell.setCellValue(vehicle.get("carName")+"");
                cell.setCellStyle(bodyStyle);

                cell = bodyRow.createCell(i++);
                cell.setCellValue(vehicle.get("sequence")+"");
                cell.setCellStyle(bodyStyle);

                cell = bodyRow.createCell(i++);
                cell.setCellValue(vehicle.get("curLoc")+"");
                cell.setCellStyle(bodyStyle);

                cell = bodyRow.createCell(i++);
                cell.setCellValue(vehicle.get("siteName")+"");
                cell.setCellStyle(bodyStyle);

                cell = bodyRow.createCell(i++);
                cell.setCellValue(vehicle.get("siteAddress")+"");
                cell.setCellStyle(bodyStyle);

                String arrTime = (String)vehicle.get("arrTime");
                String arr = arrTime.substring(0, arrTime.indexOf('.'));
                cell = bodyRow.createCell(i++);
                cell.setCellValue(timeTransfer(arr));
                cell.setCellStyle(bodyStyle);

                cell = bodyRow.createCell(i++);
                if(vehicle.get("unloadVol").toString()==null || vehicle.get("unloadVol").toString()=="")
                {
                    cell.setCellValue("Unload 0,Load "+vehicle.get("sbVol").toString());
                }
                else if(vehicle.get("sbVol").toString()==null || vehicle.get("sbVol").toString()=="")
                {
                    cell.setCellValue("Unload "+vehicle.get("unloadVol")+",Load 0");
                }
                else if(vehicle.get("unloadVol").toString()==null || vehicle.get("unloadVol").toString()==""||vehicle.get("sbVol").toString()==null || vehicle.get("sbVol").toString()=="")
                {
                    cell.setCellValue("Unload 0,Load 0");
                }
                else
                {
                    cell.setCellValue("Unload "+vehicle.get("unloadVol").toString()+",Load "+vehicle.get("sbVol").toString());
                }
                cell.setCellStyle(bodyStyle);

                String endTime = (String)vehicle.get("endTime");
                String end = endTime.substring(0, endTime.indexOf('.'));
                cell = bodyRow.createCell(i++);
                cell.setCellValue(timeTransfer(end));
                cell.setCellStyle(bodyStyle);

                cell = bodyRow.createCell(i++);
                cell.setCellValue(vehicle.get("nextCurLoc")+"");
                cell.setCellStyle(bodyStyle);

                cell = bodyRow.createCell(i++);
                cell.setCellValue(vehicle.get("calcDis")+"km");
                cell.setCellStyle(bodyStyle);
            }
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
