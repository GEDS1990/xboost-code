package com.xboost.service;

import com.google.common.collect.Maps;
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
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.*;

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
    @Inject
    private CarService carService;
    @Inject
    private SolutionRideService solutionRideService;

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


    public List<Map> vehicleScheduling() {
        String scenariosId = ShiroUtil.getOpenScenariosId();
        String modelType = myScenariosService.findById(Integer.parseInt(ShiroUtil.getOpenScenariosId())).getScenariosModel();
        List<Map> rideList = new ArrayList<>();

        if(modelType.equals("2")){

            Integer maxRideId = solutionRideService.maxRideId(scenariosId);
            if(null!=maxRideId){

                for(int x=1;x<=maxRideId;x++)
                {
                    String depotOrder="";
                    List<Map> tempList = solutionRideService.findByRide2(scenariosId,String.valueOf(x));
                    String maxSequence= tempList.get(tempList.size()-1).get("sequence").toString();
                    Map<String,Object> rideRoute = Maps.newHashMap();
                    String carType="";
                    String carName="";
                    String curLoc ="";
                    List<String> carList= new ArrayList<>();

                    for(int y=0;y<tempList.size();y++){
                        Map<String,Object> temp = tempList.get(y);
                        curLoc = temp.get("curLoc").toString();
                        String sequence = temp.get("sequence").toString();
                        if(sequence.equals(maxSequence)){
                            depotOrder += curLoc;
                        }else {
                            depotOrder += curLoc + ">>";
                        }
                    }
                    if(null!=tempList.get(0).get("carName")){
                        carName = tempList.get(0).get("carName").toString();
                    }else
                    {
                        carName="--";
                    }
                    carType = tempList.get(0).get("carType").toString();
                    carList= carService.findIdleCar(ShiroUtil.getOpenScenariosId(),carType);

                    rideRoute.put("RideId",x);
                    rideRoute.put("depotOrder",depotOrder);
                    rideRoute.put("carType",carType);
                    rideRoute.put("carName",carName);
                    rideRoute.put("carList",carList);
                    rideList.add(rideRoute);
                }
            }

        }else {
            Integer maxRideId = solutionRideService.maxRouteId(scenariosId);
            if(null!=maxRideId){

                for(int x=1;x<=maxRideId;x++)
                {
                    List<Map> tempList = solutionRideService.findByRide1(scenariosId,String.valueOf(x));
                    String maxSequence= tempList.get(tempList.size()-1).get("sequence").toString();
                    String depotOrder="";
                    Map<String,Object> rideRoute = Maps.newHashMap();
                    String carType="";
                    String carName="";
                    String curLoc ="";
                    List<String> carList= new ArrayList<>();

                    for(int y=0;y<tempList.size();y++){
                        Map<String,Object> temp = tempList.get(y);
                        curLoc = temp.get("curLoc").toString();
                        String curNextLoc= temp.get("nextCurLoc").toString();
                        String separator=">>";
                        String sequence = temp.get("sequence").toString();


                        if(curLoc.equals(curNextLoc))
                        {
                            curLoc = "";
                            separator="";
                        }
                        if(sequence.equals(maxSequence)){
                            depotOrder += curLoc;
                        }else {
                            depotOrder += curLoc + separator;
                        }
                    }
                    if(null!=tempList.get(0).get("carName")){
                        carName = tempList.get(0).get("carName").toString();
                    }else
                    {
                        carName="--";
                    }
                    carType = tempList.get(0).get("carType").toString();
                    carList= carService.findIdleCar(ShiroUtil.getOpenScenariosId(),carType);

                    rideRoute.put("RideId",x);
                    rideRoute.put("depotOrder",depotOrder);
                    rideRoute.put("carType",carType);
                    rideRoute.put("carName",carName);
                    rideRoute.put("carList",carList);
                    rideList.add(rideRoute);
                }
            }

        }

        return rideList;

    }



    /**
     *  导出excel
     * @param
     * @return
     */
    public void exportResult(String[] titles,ServletOutputStream outputStream ) {
        // 创建一个workbook 对应一个excel应用文件
        XSSFWorkbook workBook = new XSSFWorkbook();
        // 在workbook中添加一个sheet,对应Excel文件中的sheet

        XSSFSheet sheet = workBook.createSheet("VehicleScheduling");
        ExportUtil exportUtil = new ExportUtil(workBook, sheet);
        XSSFCellStyle headStyle = exportUtil.getHeadStyle();
        XSSFCellStyle bodyStyle = exportUtil.getBodyStyle();
        sheet.setColumnWidth(0, 15 * 250);
        sheet.setColumnWidth(1, 80 * 250);
        sheet.setColumnWidth(2, 15 * 250);
        sheet.setColumnWidth(3, 15 * 250);
        sheet.setDefaultRowHeight((short) 100);
        // 构建表头
        XSSFRow headRow = sheet.createRow(0);
        XSSFCell cell = null;

        for (int i = 0; i < titles.length; i++) {
            cell = headRow.createCell(i);
            cell.setCellValue(titles[i]);
            cell.setCellStyle(headStyle);
        }
        List<Map> rideList = vehicleScheduling();
        // 构建表体数据
        if (rideList != null && rideList.size() > 0) {
            for (int j = 0; j < rideList.size(); j++) {
                XSSFRow bodyRow = sheet.createRow(j + 1);
                Map<String, Object> ride = rideList.get(j);

                int i = 0;
                cell = bodyRow.createCell(i++);
                cell.setCellValue("Ride " + String.format("%03d", Integer.parseInt(ride.get("RideId").toString())));
                cell.setCellStyle(bodyStyle);

                cell = bodyRow.createCell(i++);
                cell.setCellValue(ride.get("depotOrder") + "");
                cell.setCellStyle(bodyStyle);

                cell = bodyRow.createCell(i++);
                cell.setCellValue(ride.get("carType") + "");
                cell.setCellStyle(bodyStyle);

                cell = bodyRow.createCell(i++);
                cell.setCellValue(ride.get("carName") + "");
                cell.setCellStyle(bodyStyle);

            }

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
}
