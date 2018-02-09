package com.xboost.service;

import com.xboost.pojo.DemandInfo;
import com.xboost.pojo.Route;
import com.xboost.util.ExportUtil;
import com.xboost.util.ShiroUtil;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.*;
import org.apache.spark.sql.sources.In;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.ServletOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.*;

@Named
@Transactional
public class SolutionDistributionService {
    @Inject
    private DemandInfoService demandInfoService;
    @Inject
    private SolutionRouteService solutionRouteService;

    public Map<String, Object> getDataByType(String type) {
        String maxmix = demandInfoService.findMinMax(ShiroUtil.getOpenScenariosId());
        int min = Integer.parseInt(maxmix.split("-")[0]);
        int max = Integer.parseInt(maxmix.split("-")[1]);
        //间隔
        int jiange = 10;
        Map<String, Object> map = new HashMap<String, Object>();
        List<DemandInfo> demandInfoList = demandInfoService.findAll(ShiroUtil.getOpenScenariosId());
        List<Route> routeList = solutionRouteService.findAllRoute(ShiroUtil.getOpenScenariosId());
        double totalAll = 0;
        double totalAllRoute = 0;
        for (DemandInfo demandInfo : demandInfoList) {
            totalAll = totalAll + Integer.parseInt((demandInfo.getVotes() != null) ? demandInfo.getVotes() : "0");
        }
        List<Map> arrT = demandInfoService.findarrTime();
        for (Route route : routeList) {
            totalAllRoute = totalAllRoute + Double.parseDouble((route.getSbVolSum() != null) ? route.getSbVolSum() : "0");
        }
        type = (type != null) ? type : "0";
        DecimalFormat df = new DecimalFormat("######0.00");
        switch (type) {
            case "0":
                for (int i = 0; i < (max - min) / jiange; i++) {
                    double total = 0;
                    for (DemandInfo demandInfo : demandInfoList) {
                        double res = Integer.parseInt(demandInfo.getDurationEnd() != null ? demandInfo.getDurationEnd() : "0");
                        if (res > (min + jiange * i) && res <= (min + jiange * (i + 1))) {
                            total = total + Integer.parseInt(demandInfo.getVotes() != null ? demandInfo.getVotes() : "0");
                        }
                    }
                    double d = 0;
                    d = Double.parseDouble(df.format(total / totalAll * 100))>100?100:Double.parseDouble(df.format(total / totalAll * 100));
                    d = Double.parseDouble(df.format(total / totalAll * 100))<0?0:Double.parseDouble(df.format(total / totalAll * 100));
                    map.put(String.valueOf(min + (jiange * i)) + "-" + String.valueOf(min + (jiange * (i + 1))), String.valueOf(d));
                }
                break;
            case "1":
                for (int i = 0; i < (max - min) / jiange; i++) {
                    double total = 0;
                    for (Route route : routeList) {
                        double res = Double.parseDouble(route.getArrTime() != null ? route.getArrTime() : "0");
                        if (res > (min + jiange * i) && res <= (min + jiange * (i + 1))) {
                            total = total + Double.parseDouble(route.getSbVolSum() != null ? route.getSbVolSum() : "0");
                        }
                    }
                    double d = 0;
                    d = Double.parseDouble(df.format(total / totalAllRoute * 100))>100?100:Double.parseDouble(df.format(total / totalAllRoute * 100));
                    d = Double.parseDouble(df.format(total / totalAllRoute * 100))<0?0:Double.parseDouble(df.format(total / totalAllRoute * 100));
                    map.put(String.valueOf(min + (jiange * i)) + "-" + String.valueOf(min + (jiange * (i + 1))), String.valueOf(d));
                }
                break;
            case "3":
                double total1 = 0, total2 = 0, total3 = 0, total4 = 0, total5 = 0, total6 = 0, total7 = 0, totalD = 0;
                for (Map arr : arrT) {
                    double arrTime = 0;
                    double vol = 0;
                    try {
                        arrTime = Double.parseDouble(arr.get("arr_time").toString());
                        vol = Double.parseDouble(arr.get("vol").toString());
                    } catch (Exception e) {
                        continue;
                    }
                    double res = arrTime;
                    if (0 <= arrTime) {
                        //提前到
                        if (res > 50 && res <= 60) {
                            total1 = total1 + vol;
                        } else if (res > 40 && res <= 50) {
                            total2 = total2 + vol;
                        } else if (res > 30 && res <= 40) {
                            total3 = total3 + vol;
                        } else if (res > 20 && res <= 30) {
                            total4 = total4 + vol;
                        } else if (res > 10 && res <= 20) {
                            total5 = total5 + vol;
                        } else if (res > 0 && res <= 10) {
                            total6 = total6 + vol;
                        } else if (res == 0) {
                            total7 = total7 + vol;
                        }
                    } else {
                        //晚到
                    }
                    totalD = total1 + total2 + total3 + total4 + total5 + total6 + total7;
                }
                int d1 = 0;
                int d2 = 0;
                int d3 = 0;
                int d4 = 0;
                int d5 = 0;
                int d6 = 0;
                int d7 = 0;

                d1 = (int)(total1 / totalD);
                d2 = (int)(total2 / totalD);
                d3 = (int)(total3 / totalD);
                d4 = (int)(total4 / totalD);
                d5 = (int)(total5 / totalD);
                d6 = (int)(total6 / totalD);
                d7 = (int)(total7 / totalD);
//                d1 = (total1 / totalD)>1?1:(total1 / totalD);
//                d1 = (total1 / totalD)<0?0:(total1 / totalD);
//
//                d2 = (total2 / totalD)>1?1:(total2 / totalD);
//                d2 = (total2 / totalD)<0?0:(total2 / totalD);
//
//                d3 = (total3 / totalD)>1?1:(total3 / totalD);
//                d3 = (total3 / totalD)<0?0:(total3 / totalD);
//
//                d4 = (total4 / totalD)>1?1:(total4 / totalD);
//                d4 = (total4 / totalD)<0?0:(total4 / totalD);
//
//                d5 = (total5 / totalD)>1?1:(total5 / totalD);
//                d5 = (total5 / totalD)<0?0:(total5 / totalD);
//
//                d6 = (total6 / totalD)>1?1:(total6 / totalD);
//                d6 = (total6 / totalD)<0?0:(total6 / totalD);
//
//                d7 = (total7 / totalD)>1?1:(total7 / totalD);
//                d7 = (total7 / totalD)<0?0:(total7 / totalD);

                map.put("tiqian60", df.format(d1 * 100));
                map.put("tiqian50", df.format(d2 * 100));
                map.put("tiqian40", df.format(d3 * 100));
                map.put("tiqian30", df.format(d4 * 100));
                map.put("tiqian20", df.format(d5 * 100));
                map.put("tiqian10", df.format(d6 * 100));
                map.put("zunshi", df.format(d7 * 100));
                break;
            default:
                break;
        }
        return map;
    }

    /**
     *  导出excel
     * @param scenariosId
     * @return
     */
    public void exportResult(String scenariosId,String[] titles,ServletOutputStream outputStream ) {
        // 创建一个workbook 对应一个excel应用文件
        XSSFWorkbook workBook = new XSSFWorkbook();
        // 在workbook中添加一个sheet,对应Excel文件中的sheet

        XSSFSheet sheet = workBook.createSheet("Distribution");
        ExportUtil exportUtil = new ExportUtil(workBook, sheet);
        XSSFCellStyle headStyle = exportUtil.getHeadStyle();
        XSSFCellStyle bodyStyle = exportUtil.getBodyStyle();

        // 对应excel中的行和列，下表从0开始{"开始行,结束行,开始列,结束列"}
        String[] headNum = {"0,0,0,2", "0,0,4,6", "0,0,8,9", "0,0,11,12", "0,0,14,16", "0,0,18,20", "0,0,22,24", "1,1,0,1", "1,1,4,5"};

        // 动态合并单元格
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
        for (int i = 0; i < 24; i++) {
            cell = headRow.createCell(i);
            cell.setCellValue(titles[i]);
            cell.setCellStyle(headStyle);
        }

        // 构建表结构
        String[] cutOffTime = {"时间段", "", "件量%"};
        String[] arrivalTime = {"提早60", "提早50", "提早40", "提早30", "提早20", "提早10", "准时到",""};
        String[] ODTitles = {"件量分布", "件量比例", "路线比例"};
        String[] receiveDepot = {"<5", "5~30", "30~300", ">300", "Grand Total", "", ""};
        String[] deliveryDepot = {"<5", "5~30", "30~80", "Grand Total", "", "", ""};
        String[] startDelivertDepot = {"timeid", "", "Grand Total"};
        String[] startDepotTime = {"准时", "+10分钟", "+20分钟", "+30分钟", "+40分钟", "+50分钟", "Grand Total"};

        // 获取表数据
        /**
         * @param type 0: 各网点截单时间
         *             1：各网点送达时间分布
         *             2：收段达到集散点时间分布
         *             3：支线到达目的地集散点时间分布
         *             4：始发集散地OD件量分布
         *             5：始发网点OD件量分布
         *             6：始发集散点的发车分布
         */

        Map<String, Object> dataByType0 = getDataByType("0");
        Set<String> sortKeySet0 = getSortKeySetByMap(dataByType0);
        Iterator<String> iterator0 = sortKeySet0.iterator();

        Map<String, Object> dataByType1 = getDataByType("1");
        Set<String> sortKeySet1 = getSortKeySetByMap(dataByType1);
        Iterator<String> iterator1 = sortKeySet0.iterator();

        Map<String, Object> dataByType2 = getDataByType("2");

        Map<String, Object> dataByType3 = getDataByType("3");
        Set<String> sortKeySet3 = getSortKeySetByMap(dataByType3);
        Iterator<String> iterator3 = sortKeySet3.iterator();
        Float arrPercentage = 0F;

        Map<String, Object> dataByType4 = getDataByType("4");
        Map<String, Object> dataByType5 = getDataByType("5");

        // 构建表结构和数据
        for (int j = 1; j < Math.max(9,dataByType0.size()+2); j++) {
            XSSFRow row = sheet.createRow(j);

            if(j < 9) {
                // 构建第二行表头
                if (j == 1) {
                    for (int i = 0; i < 3; i++) {
                        cell = row.createCell(i);
                        cell.setCellValue(cutOffTime[i]);
                        cell.setCellStyle(bodyStyle);

                        cell = row.createCell(i + 4);
                        cell.setCellValue(cutOffTime[i]);
                        cell.setCellStyle(bodyStyle);

                        cell = row.createCell(i + 14);
                        cell.setCellValue(ODTitles[i]);
                        cell.setCellStyle(bodyStyle);

                        cell = row.createCell(i + 18);
                        cell.setCellValue(ODTitles[i]);
                        cell.setCellStyle(bodyStyle);

                        cell = row.createCell(i + 22);
                        cell.setCellValue(startDelivertDepot[i]);
                        cell.setCellStyle(bodyStyle);
                    }
                }

                cell = row.createCell(8);
                cell.setCellValue(arrivalTime[j - 1]);
                cell.setCellStyle(bodyStyle);

                cell = row.createCell(11);
                cell.setCellValue(arrivalTime[j-1]);
                cell.setCellStyle(bodyStyle);

                if (j > 1) {
                    cell = row.createCell(14);
                    cell.setCellValue(receiveDepot[j-2]);
                    cell.setCellStyle(bodyStyle);

                    cell = row.createCell(18);
                    cell.setCellValue(deliveryDepot[j-2]);
                    cell.setCellStyle(bodyStyle);

                    cell = row.createCell(22);
                    cell.setCellValue(startDepotTime[j-2]);
                    cell.setCellStyle(bodyStyle);
                }
            }

            if(j >= 2 && j<=dataByType0.size()+2 && dataByType0.size()>0 ) {
                String next = iterator0.next();
                cell = row.createCell(0);
                cell.setCellValue(convertTime(next));
                cell.setCellStyle(bodyStyle);

                cell = row.createCell(2);
                cell.setCellValue(dataByType0.get(next)+"%");
                cell.setCellStyle(bodyStyle);

                String next1 = iterator1.next();
                cell = row.createCell(4);
                cell.setCellValue(convertTime(next1));
                cell.setCellStyle(bodyStyle);

                cell = row.createCell(6);
                cell.setCellValue(dataByType1.get(next1)+"%");
                cell.setCellStyle(bodyStyle);
            }


            if(j >= 1 && j<=dataByType3.size() && dataByType3.size()>0 ) {
                if (dataByType3.get("tiqian"+(7-j)+"0") != null) {
                    arrPercentage += Float.parseFloat(dataByType3.get("tiqian"+(7-j)+"0")+"");
                }
                cell = row.createCell(12);
                cell.setCellValue(dataByType3.get("tiqian"+(7-j)+"0") == null ? "0.00%" : arrPercentage+"%");
                cell.setCellStyle(bodyStyle);
            }

        }


        try {
            workBook.write(outputStream);
            outputStream.flush();
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Set<String> getSortKeySetByMap(Map<String, Object> map) {
        Set<String> keySet = map.keySet();
        Set<String> sortSet = new TreeSet<>(new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return o1.compareTo(o2);
            }
        });
        Map<String, Object> sortMap = new HashMap<>();
        for (String set:keySet) {
            sortSet.add(set);
        }
        /*for (String set : sortSet) {
            String time = Integer.parseInt(set.substring(0, set.indexOf("-")))/60 + ":" + Integer.parseInt(set.substring(0, set.indexOf("-")))%60
                    + "-" + Integer.parseInt(set.substring(set.indexOf("-")+1))/60 + ":" + Integer.parseInt(set.substring(set.indexOf("-")+1))%60;
            sortMap.put(time, map.get(set));
        }*/


        return sortSet;
    }

    public String convertTime(String key) {
        return  Integer.parseInt(key.substring(0, key.indexOf("-")))/60 + ":" + Integer.parseInt(key.substring(0, key.indexOf("-")))%60
                + "-" + Integer.parseInt(key.substring(key.indexOf("-")+1))/60 + ":" + Integer.parseInt(key.substring(key.indexOf("-")+1))%60;
    }
}
