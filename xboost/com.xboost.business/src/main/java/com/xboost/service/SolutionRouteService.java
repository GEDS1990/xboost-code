package com.xboost.service;

import com.xboost.mapper.SolutionRouteMapper;
import com.xboost.pojo.Activity;
import com.xboost.pojo.Route;
import com.xboost.util.ExcelUtil;
import com.xboost.util.ExportUtil;
import com.xboost.util.ShiroUtil;
import com.xboost.util.Strings;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Param;
import org.apache.poi.xssf.usermodel.*;
import org.apache.spark.sql.sources.In;
import org.joda.time.DateTime;
import org.junit.runners.Parameterized;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.ServletOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * Created by Administrator on 2017/11/5 0005.
 */

@Named
@Transactional
public class SolutionRouteService {
    private static Logger logger = LoggerFactory.getLogger(SolutionRouteService.class);

    @Inject
    private SolutionRouteMapper solutionRouteMapper;
    @Inject
    private MyScenariosService myScenariosService;

    /**
     * 新增route信息
     *
     * @param route
     */
    public void addRoute(Route route) {
        route.setCreateTime(DateTime.now().toString("yyyy-MM-dd HH:mm"));

        solutionRouteMapper.addRoute(route);

    }

    /**
     * update route信息
     *
     * @param route
     */
    public void updateRouteByTemp(Route route) {
        solutionRouteMapper.updateRouteByTemp(route);

    }

    /**
     * 根据场景id查询所有路线信息
     * param
     *
     * @return
     */
    public List<Route> findAllRoute(String scenariosId) {
        return solutionRouteMapper.findAll(scenariosId);
    }

    /**
     * 获取路线数量
     *
     * @return
     */
    public Integer findRouteNum(String scenariosId) {
        return solutionRouteMapper.findRouteNum(scenariosId).intValue();
    }

    /**
     * 获取路线信息总数量
     *
     * @return
     */
    public Integer findAllCount(String scenariosId) {
        return solutionRouteMapper.findAllCount(scenariosId).intValue();
    }

    /**
     * 根据查询条件获取路线信息
     * param param
     *
     * @return
     */
    public List<Route> findByParam(Map<String, Object> param) {
        return solutionRouteMapper.findByParam(param);
    }


    /**
     * 根据查询条件获取模型整体参数信息的路线数量
     *
     * @param param
     * @return
     */
    public Integer findCountByParam(Map<String, Object> param) {
        return solutionRouteMapper.findCountByParam(param).intValue();
    }

    //根据场景id查询routeId
    public Integer findRouteId(String scenariosId) {
        return solutionRouteMapper.findRouteId(scenariosId);
    }

    /**
     * 根据路线id获取线路信息
     * param param
     *
     * @return
     */
    public List<Map<String, Object>> findByRoute(Map<String, Object> param) {
        List<Map<String, Object>> mapList = solutionRouteMapper.findByRoute(param);
        String modelType = myScenariosService.findById(Integer.parseInt(ShiroUtil.getOpenScenariosId())).getScenariosModel();
        if ("1".equals(modelType))
            return mapList;
        for (Map map : mapList) {
            System.out.println(map.get("sbVol"));
            System.out.println(map.get("sbVolSum"));
            double sbVol = Math.floor(Double.parseDouble(map.get("sbVol") + ""));
            double sbVolSum = Math.floor(Double.parseDouble(map.get("sbVolSum") + ""));
            double unloadVol = Math.floor(Double.parseDouble(map.get("unloadVol") + ""));
            double unloadVolSum = Math.floor(Double.parseDouble(map.get("unloadVolSum") + ""));
            map.put("sbVol", sbVol);
            map.put("sbVolSum", sbVolSum);
            map.put("unloadVol", unloadVol);
            map.put("unloadVolSum", unloadVolSum);
        }
        return mapList;
    }

    /**
     * 根据路线id获取线路数量
     *
     * @param param
     * @return
     */
    public Integer findCountByRoute(Map<String, Object> param) {
        return solutionRouteMapper.findCountByRoute(param).intValue();
    }

    /**
     * 根据场景id查询全部线路信息
     * param param
     *
     * @return
     */
    public List<Map<String, Object>> findAllByRoute(String scenariosId) {
        return solutionRouteMapper.findAllByRoute(scenariosId);
    }

    /**
     * 根据场景id查询全部线路数量
     *
     * @param scenariosId
     * @return
     */
    public Integer findAllCountByRoute(String scenariosId) {
        return solutionRouteMapper.findAllCountByRoute(scenariosId).intValue();
    }


    /**
     * 根据用户的ID查询路线信息
     *
     * @param id 用户ID
     * @return
     */
    public Route findById(Integer id) {
        return solutionRouteMapper.findById(id);
    }

    /**
     * 根据id删除模型整体参数信息
     *
     * @param scenariosId
     */
    public void delByScenariosId(Integer scenariosId) {

        solutionRouteMapper.delByScenariosId(scenariosId);
    }

    //查询路线总路程
    public String findTotalDistance(String scenariosId, String routeCount) {
        return solutionRouteMapper.findTotalDistance(scenariosId, routeCount);
    }

    //把排车的车名更新到route表
    public void updateCarName(Map<String, Object> param) {
        solutionRouteMapper.updateCarName(param);
    }

    public void updateCarToBusy(String scenariosId, String carName) {
        solutionRouteMapper.updateCarToBusy(scenariosId, carName);
    }

    public void updateCarToIdle(String scenariosId, String carName) {
        solutionRouteMapper.updateCarToIdle(scenariosId, carName);
    }

    public void updateAllCarToIdle(String scenariosId) {
        solutionRouteMapper.updateAllCarToIdle(scenariosId);
    }

    public void updateScenariosModel(String scenariosModel) {
        solutionRouteMapper.updateScenariosModel(scenariosModel, ShiroUtil.getOpenScenariosId());
    }

    public List<String> findUsingCar(String scenariosId, String routeCount) {
        return solutionRouteMapper.findUsingCar(scenariosId, routeCount);
    }

    public List<String> findUsingCar1(String scenariosId) {
        return solutionRouteMapper.findUsingCar1(scenariosId);
    }

    public List<String> findIdleCar1(String scenariosId) {
        return solutionRouteMapper.findIdleCar1(scenariosId);
    }

    public List<String> findIdleCar(String scenariosId, String routeCount) {
        return solutionRouteMapper.findIdleCar(scenariosId, routeCount);
    }

    public String findRouteCar(String scenariosId, String routeCount) {
        return solutionRouteMapper.findRouteCar(scenariosId, routeCount);
    }

    //校验这段路线车上件量是否超出车的最大载件量
    public String findCarLoad(String scenariosId, String routeCount) {
        List<Map<String, Object>> carList = solutionRouteMapper.findCarLoad(scenariosId, routeCount);


        return "success";
    }

    //校验这一段路线是否超过车的最远距离

    //车辆能否在预定时间到达该段路程的起点

    //车辆能否在预定时间内跑完该段路程

    /**
     * 导出result_depots excel
     */
    public void exportResult(String scenariosId, String[] titles, ServletOutputStream outputStream, String modelType) {
        List<Map<String, Object>> routeList = solutionRouteMapper.findAllByRoute(scenariosId);
        for (int i = 0; i < routeList.size(); i++) {
            String sbVol = (String) routeList.get(i).get("sbVol");
            String unloadVol = (String) routeList.get(i).get("unloadVol");
            if (sbVol != null && !"0".equals(sbVol)) {
                routeList.get(i).put("sbVol", sbVol.substring(0, sbVol.indexOf(".")));
            }
            if (unloadVol != null && !"0".equals(unloadVol)) {
                routeList.get(i).put("unloadVol", unloadVol.substring(0, unloadVol.indexOf(".")));
            }
        }
        String sbVol;
        String unloadVol;
        // 创建一个workbook 对应一个excel应用文件
        XSSFWorkbook workBook = new XSSFWorkbook();
        // 在workbook中添加一个sheet,对应Excel文件中的sheet

        XSSFSheet sheet = workBook.createSheet("Route");
        ExportUtil exportUtil = new ExportUtil(workBook, sheet);
        XSSFCellStyle headStyle = exportUtil.getHeadStyle();
        XSSFCellStyle bodyStyle = exportUtil.getBodyStyle();

        // 构建表头
        XSSFRow headRow = sheet.createRow(0);
        XSSFCell cell = null;



//        if ("1".equals(modelType)) {
        if (false) {
            String[] title = { "Route Id", "Route", "Arrang Car" };
            sheet.setColumnWidth(1, 80 * 250);
            sheet.setDefaultRowHeight((short) 36);
            for (int i = 0; i < title.length; i++) {
                cell = headRow.createCell(i);
                cell.setCellValue(title[i]);
                cell.setCellStyle(headStyle);
            }

            Set<String> routeId = new TreeSet<>(new Comparator<String>() {
                @Override
                public int compare(String o1, String o2) {
                    Integer i1 = Integer.parseInt(o1);
                    Integer i2 = Integer.parseInt(o2);
                    return i1-i2 ;
                }
            });
            for (Map map : routeList) {
                routeId.add(map.get("routeCount").toString());
            }
            Map<String, String> routeData = new TreeMap<>(new Comparator<String>() {
                @Override
                public int compare(String o1, String o2) {
                    Integer i1 = Integer.parseInt(o1);
                    Integer i2 = Integer.parseInt(o2);
                    return i1 - i2;
                }
            });
            for (String id : routeId) {
                List<String> list = new ArrayList<String>();
                for (Map map : routeList) {
                    if(map.get("routeCount").equals(id) && !list.contains(map.get("curLoc").toString())) {
                        list.add(map.get("curLoc").toString());
                    }
                }
                routeData.put(id, StringUtils.join(list, "-"));
            }

            // 构建表体数据
            if (routeList != null && routeList.size() > 0) {
                Set<Map.Entry<String, String>> entries = routeData.entrySet();
                int j = 1;
                for (Map.Entry<String, String> entry: entries) {
                    XSSFRow bodyRow = sheet.createRow(j++);
                    int i = 0;
                    cell = bodyRow.createCell(i++);
                    cell.setCellValue("Route" + entry.getKey());
                    cell.setCellStyle(bodyStyle);

                    cell = bodyRow.createCell(i++);
                    cell.setCellValue(entry.getValue());
                    cell.setCellStyle(bodyStyle);

                    cell = bodyRow.createCell(i++);
                    cell.setCellValue("");
                    cell.setCellStyle(bodyStyle);
                }
            }
        }

//        if ("2".equals(modelType)) {
        if (true) {
            sheet.setColumnWidth(6, 50 * 250);
            sheet.setDefaultRowHeight((short) 36);

            for (int i = 0; i < titles.length; i++) {
                cell = headRow.createCell(i);
                cell.setCellValue(titles[i]);
                cell.setCellStyle(headStyle);
            }

            cell = headRow.createCell(titles.length);
            cell.setCellValue("Car Num");
            cell.setCellStyle(headStyle);

            cell = headRow.createCell(titles.length+1);
            cell.setCellValue("Car Name");
            cell.setCellStyle(headStyle);
            // 构建表体数据
            if (routeList != null && routeList.size() > 0) {
                for (int j = 0; j < routeList.size(); j++) {
                    XSSFRow bodyRow = sheet.createRow(j + 1);
                    Map<String, Object> route = routeList.get(j);

                    int i = 0;
                    cell = bodyRow.createCell(i++);
                    cell.setCellValue("Route" + route.get("routeCount"));
                    cell.setCellStyle(bodyStyle);

                    cell = bodyRow.createCell(i++);
                    cell.setCellValue(route.get("sequence").toString());
                    cell.setCellStyle(bodyStyle);

                    cell = bodyRow.createCell(i++);
                    cell.setCellValue(route.get("curLoc")+"");
                    cell.setCellStyle(bodyStyle);

                    cell = bodyRow.createCell(i++);
                    cell.setCellValue(route.get("siteName")+"");
                    cell.setCellStyle(bodyStyle);

                    cell = bodyRow.createCell(i++);
                    cell.setCellValue(route.get("siteAddress")+"");
                    cell.setCellStyle(bodyStyle);

                    String arrTime = (String) route.get("arrTime");
                    arrTime = convertTime(arrTime);
                    cell = bodyRow.createCell(i++);
                    if("1".equals(route.get("sequence"))) {
                        cell.setCellValue("--");
                    }else {
                        cell.setCellValue(arrTime);
                    }
                    cell.setCellStyle(bodyStyle);

                    cell = bodyRow.createCell(i++);
                    if (route.get("unloadVol").toString() == null || route.get("unloadVol").toString() == "") {
                        cell.setCellValue("Unload 0,Load " + route.get("sbVol").toString());
                    } else if (route.get("sbVol").toString() == null || route.get("sbVol").toString() == "") {
                        cell.setCellValue("Unload " + route.get("unloadVol") + ",Load 0");
                    } else if (route.get("unloadVol").toString() == null || route.get("unloadVol").toString() == "" || route.get("sbVol").toString() == null || route.get("sbVol").toString() == "") {
                        cell.setCellValue("Unload 0,Load 0");
                    } else {
                        cell.setCellValue("Unload " + route.get("unloadVol").toString() + ",Load " + route.get("sbVol").toString());
                    }
                    cell.setCellStyle(bodyStyle);

                    String endTime = (String) route.get("endTime");
                    endTime = convertTime(endTime);
                    cell = bodyRow.createCell(i++);
                    if("2".equals(route.get("sequence"))) {
                        cell.setCellValue("--");
                    }else {
                        cell.setCellValue(endTime);
                    }
                    cell.setCellStyle(bodyStyle);

                    cell = bodyRow.createCell(i++);
                    if("2".equals(route.get("sequence"))) {
                        cell.setCellValue("--");
                    }else {
                        cell.setCellValue(route.get("nextCurLoc")+"");
                    }
                    cell.setCellStyle(bodyStyle);

                    cell = bodyRow.createCell(i++);
                    if("2".equals(route.get("sequence"))) {
                        cell.setCellValue("--");
                    }else {
                        cell.setCellValue(route.get("calcDis").toString() + "km");
                    }
                    cell.setCellStyle(bodyStyle);

                    cell = bodyRow.createCell(i++);
                    cell.setCellValue(route.get("carType")+"");
                    cell.setCellStyle(bodyStyle);

                    cell = bodyRow.createCell(i++);
                    cell.setCellValue(route.get("carNum")+"");
                    cell.setCellStyle(bodyStyle);

                    cell = bodyRow.createCell(i++);
                    cell.setCellValue("");
                    cell.setCellStyle(bodyStyle);
                }
            }
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

    //根据route_count查询route
    public List<Route> findRouteByRouteCount(String scenariosId, String routeCount) {
        return solutionRouteMapper.findRouteByRouteCount(scenariosId, routeCount);
    }

    public void updateRouteByExcel(String scenariosId, Route route, MultipartFile[] file, String modelType){
        //判断文件集合是否有文件
        if(file != null && file.length > 0) {
            for(MultipartFile multipartFile : file) {
                if(!multipartFile.isEmpty()) {
                    //解析文件并存储到数据库
                    File fileTmp = null;
                    long tempTime = System.currentTimeMillis();
                    try {
                        fileTmp=new File(System.getProperty("user.dir")+"/temp/"+tempTime+ ".xlsx");
                        if (!fileTmp.exists()) fileTmp.mkdirs();
                        multipartFile.transferTo(fileTmp);
//                      File fileTemp = (File) multipartFile;
                        ExcelUtil excelUtil = new ExcelUtil();
                        List<String> lineList = excelUtil.readExcel(fileTmp,1);
                        int d = 0;
                        Map<String, Object> param = new HashMap<String, Object>();
                        for(int i=1;i<lineList.size();i++){
                            String[] row = lineList.get(i).split("#");
                            if ("1".equals(modelType) && row.length == 3 && !StringUtils.isBlank(row[2])) {
                                String routeCount = row[0].substring(5);
                                String carName = row[2];
                                String oldCarName = findRouteCar(scenariosId,routeCount);

                                param.put("scenariosId",ShiroUtil.getOpenScenariosId());
                                param.put("routeCount", routeCount);
                                param.put("carName", carName);

                                if (!Strings.isEmpty(oldCarName)) {
                                    updateCarToIdle(scenariosId,oldCarName);
                                    updateCarName(param);
                                    //把车的状态更新为busy
                                    updateCarToBusy(scenariosId,carName);
                                } else {
                                    updateCarName(param);
                                    //把车的状态更新为busy
                                    updateCarToBusy(scenariosId,carName);
                                }
                            }

                            if ("2".equals(modelType) && row.length == 13 && "1".equals(row[1]) && !StringUtils.isBlank(row[12])) {
                                String routeCount = row[0].substring(5);
                                String carName = row[12];
                                param.put("scenariosId",ShiroUtil.getOpenScenariosId());
                                param.put("routeCount", routeCount);
                                param.put("carName", carName);
                                solutionRouteMapper.updateCarName(param);
                            }
                        }
                        logger.info("insert into db complete");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }

    }

    public String convertTime(String time) {
        if (time.equals("--")) {
            return time;
        }
        int formatTime = Math.round(Float.parseFloat(time));
        int formatHour = formatTime / 60;
        int formatMinute = formatTime % 60;
        if(formatMinute >= 0 && formatMinute <= 9) {
            time = formatHour + " : 0" + formatMinute;
        }else {
            time = formatHour + " : " + formatMinute;
        }
        return time;
    }
}
