package com.xboost.service;

import com.xboost.mapper.SolutionRouteMapper;
import com.xboost.mapper.SolutionVehiclesMapper;
import com.xboost.pojo.Route;
import com.xboost.util.ExportUtil;
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

/**
 * Created by Administrator on 2017/11/5 0005.
 */

@Named
@Transactional
public class SolutionVehiclesService {
    private static Logger logger = LoggerFactory.getLogger(SolutionVehiclesService.class);

    @Inject
    private SolutionVehiclesMapper solutionVehiclesMapper;

    /**
     * 根据carType获取线路信息
     * param param
     * @return
     */
    public List<Map<String, Object>> findByCar(Map<String, Object> param) {
        return solutionVehiclesMapper.findByCar(param);
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

    /**
     *  导出excel
     * @param scenariosId
     * @return
     */
    public void exportResult(String scenariosId,String[] titles,ServletOutputStream outputStream ) {
        List<Map<String, Object>> list = solutionVehiclesMapper.findAllByCar(scenariosId);
        // 创建一个workbook 对应一个excel应用文件
        XSSFWorkbook workBook = new XSSFWorkbook();
        // 在workbook中添加一个sheet,对应Excel文件中的sheet

        XSSFSheet sheet = workBook.createSheet("Vehicle");
        ExportUtil exportUtil = new ExportUtil(workBook, sheet);
        XSSFCellStyle headStyle = exportUtil.getHeadStyle();
        XSSFCellStyle bodyStyle = exportUtil.getBodyStyle();
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
                cell.setCellValue(vehicle.get("carType")+"");
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
                cell.setCellValue(Integer.parseInt(arr)/60+":"+Integer.parseInt(arr)%60);
                cell.setCellStyle(bodyStyle);

                cell = bodyRow.createCell(i++);
                cell.setCellValue("Upload");
                cell.setCellStyle(bodyStyle);

                if(vehicle.get("unloadVol")==null || vehicle.get("unloadVol")=="") {
                    cell = bodyRow.createCell(i++);
                    cell.setCellValue("0");
                    cell.setCellStyle(bodyStyle);
                }else {
                    cell = bodyRow.createCell(i++);
                    cell.setCellValue(vehicle.get("unloadVol")+"");
                    cell.setCellStyle(bodyStyle);
                }

                cell = bodyRow.createCell(i++);
                cell.setCellValue("Load");
                cell.setCellStyle(bodyStyle);

                if(vehicle.get("sbVol")==null || vehicle.get("sbVol")=="") {
                    cell = bodyRow.createCell(i++);
                    cell.setCellValue("0");
                    cell.setCellStyle(bodyStyle);
                }else {
                    cell = bodyRow.createCell(i++);
                    cell.setCellValue(vehicle.get("sbVol")+"");
                    cell.setCellStyle(bodyStyle);
                }

                String endTime = (String)vehicle.get("endTime");
                String end = endTime.substring(0, endTime.indexOf('.'));
                cell = bodyRow.createCell(i++);
                cell.setCellValue(Integer.parseInt(end)/60+":"+Integer.parseInt(end)%60);
                cell.setCellStyle(bodyStyle);

                cell = bodyRow.createCell(i++);
                cell.setCellValue(vehicle.get("nextCurLoc")+"");
                cell.setCellStyle(bodyStyle);

                cell = bodyRow.createCell(i++);
                cell.setCellValue(vehicle.get("calcDis")+"");
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
