package com.xboost.service;

import com.xboost.mapper.SolutionRouteMapper;
import com.xboost.pojo.Activity;
import com.xboost.pojo.Route;
import com.xboost.util.ExportUtil;
import com.xboost.util.ShiroUtil;
import com.xboost.util.Strings;
import org.apache.ibatis.annotations.Param;
import org.apache.poi.xssf.usermodel.*;
import org.joda.time.DateTime;
import org.junit.runners.Parameterized;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.ServletOutputStream;
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
    public void exportResult(String scenariosId, String[] titles, ServletOutputStream outputStream) {
        List<Map<String, Object>> list = solutionRouteMapper.findAllByRoute(scenariosId);
        Collections.sort(list, new Comparator<Map<String, Object>>() {
            public int compare(Map<String, Object> o1, Map<String, Object> o2) {
                Integer name1 = Integer.valueOf(o1.get("routeCount").toString()) ;//name1是从你list里面拿出来的一个
                Integer name2 = Integer.valueOf(o2.get("routeCount").toString()) ; //name1是从你list里面拿出来的第二个name
                return name1.compareTo(name2);
            }
        });
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

        for (int i = 0; i < titles.length; i++) {
            cell = headRow.createCell(i);
            cell.setCellValue(titles[i]);
            cell.setCellStyle(headStyle);
        }
        // 构建表体数据
        if (list != null && list.size() > 0) {
            for (int j = 0; j < list.size(); j++) {
                XSSFRow bodyRow = sheet.createRow(j + 1);
                Map<String, Object> route = list.get(j);

                int i = 0;
                cell = bodyRow.createCell(i++);
                cell.setCellValue("Route" + route.get("routeCount"));
                cell.setCellStyle(bodyStyle);

                cell = bodyRow.createCell(i++);
                cell.setCellValue(route.get("sequence").toString());
                cell.setCellStyle(bodyStyle);

                cell = bodyRow.createCell(i++);
                cell.setCellValue(route.get("curLoc").toString());
                cell.setCellStyle(bodyStyle);

                cell = bodyRow.createCell(i++);
                cell.setCellValue(route.get("siteName").toString());
                cell.setCellStyle(bodyStyle);

                cell = bodyRow.createCell(i++);
                cell.setCellValue(route.get("siteAddress").toString());
                cell.setCellStyle(bodyStyle);

                String arrTime = (String) route.get("arrTime");
                String arr = arrTime.substring(0, arrTime.indexOf('.'));
                cell = bodyRow.createCell(i++);
                cell.setCellValue(Integer.parseInt(arr) / 60 + ":" + Integer.parseInt(arr) % 60);
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
                String end = endTime.substring(0, endTime.indexOf('.'));
                cell = bodyRow.createCell(i++);
                cell.setCellValue(Integer.parseInt(end) / 60 + ":" + Integer.parseInt(end) % 60);
                cell.setCellStyle(bodyStyle);

                cell = bodyRow.createCell(i++);
                cell.setCellValue(route.get("nextCurLoc").toString());
                cell.setCellStyle(bodyStyle);

                cell = bodyRow.createCell(i++);
                cell.setCellValue(route.get("calcDis").toString() + "km");
                cell.setCellStyle(bodyStyle);
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
}
