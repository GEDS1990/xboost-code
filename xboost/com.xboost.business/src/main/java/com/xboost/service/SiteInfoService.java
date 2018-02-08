package com.xboost.service;

import com.xboost.exception.ForbiddenException;
import com.xboost.exception.NotFoundException;
import com.xboost.mapper.SiteInfoMapper;
import com.xboost.pojo.SiteInfo;

import com.xboost.util.ExportUtil;
import com.xboost.util.ShiroUtil;
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
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import com.xboost.util.ExcelUtil;

import static java.lang.Math.floor;

/**
 * Created by Administrator on 2017/11/5 0005.
 */

@Named
@Transactional
public class SiteInfoService {

    private static Logger logger = LoggerFactory.getLogger(SiteInfoService.class);

    @Inject
    private SiteInfoMapper siteInfoMapper;
    @Inject
    private MyScenariosService myScenariosService;

    /**
     * 保存新网点基础信息
     * @param siteInfo
     */
    public void saveSiteInfo(SiteInfo siteInfo) {
        siteInfo.setCreateTime(DateTime.now().toString("yyyy-MM-dd HH:mm"));

        siteInfoMapper.save(siteInfo);

    }

    //通过Excel新增网点信息
    public void addSiteInfoByExcel(SiteInfo siteInfo,MultipartFile[] file) {
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
                        List<String> lineList = excelUtil.readExcel(fileTmp,2);
                        for(int i=2;i<lineList.size();i++){
                            String[] row = lineList.get(i).split("#");
                            //网点编码
                            siteInfo.setSiteCode(row[1]);
                            //网点经度
                            siteInfo.setSiteLongitude(row[2]);
                            //网点纬度
                            siteInfo.setSiteLatitude(row[3]);
                            //网点名称
                            siteInfo.setSiteName(row[4]);
                            //网点地址
                            siteInfo.setSiteAddress(row[5]);
                            //是否可以做夜配集散点
                            siteInfo.setSiteNightDelivery(row[9]);
                            //网点面积
                            siteInfo.setSiteArea(row[6]);
                            //网点类型
                            siteInfo.setSiteType(row[7]);
                            //停货车数(辆)(10分钟停靠能力)
                            siteInfo.setNoOfTruckLimitation(row[10]);
                            siteInfo.setNoOfBaiduLimitation(row[11]);
                            siteInfo.setNoOfDidiLimitation(row[12]);
                            siteInfo.setNoOfDadaLimitation(row[13]);
                            siteInfo.setReserve(row[14]);
                            //进出最大车型(10分钟停靠能力)
                            siteInfo.setLargeCarModel(row[15]);
                            //单一批量操作处理量上限(以一个班次操作为单位)
                            siteInfo.setMaxOperateNum(row[16]);
                            siteInfo.setDistribCenter(row[8]);
                            siteInfo.setCreateTime(DateTime.now().toString("yyyy-MM-dd HH:mm"));

                            //ID
                            if(null==row[0] || ""==row[0] || " "==row[0] || "NA".equals(row[0]) ){
                                //insert
                                siteInfoMapper.save(siteInfo);
                                logger.info("insert into db:"+siteInfo.getSiteCode());
                            }else{
//                                siteInfo.setId(Integer.parseInt(row[0]));
                                //update
                                siteInfoMapper.update(siteInfo);
                                logger.info("update:"+siteInfo.getSiteCode());
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


    /**
     * 查询所有网点信息
     * param
     * @return
     */
    public List<SiteInfo> findAllSiteInfo(String scenariosId) {
        return siteInfoMapper.findAll(scenariosId);
    }

       /**
     * 获取网点的总数量
     * @return
     */
    public Integer findSiteInfoCount(String scenariosId) {
        return siteInfoMapper.findSiteInfoCount(scenariosId).intValue();
    }

    /**
     * 根据网点查询网点信息
     * param siteName
     * @return
     */
    public List<SiteInfo> findByParam(Map<String, Object> param) {
        return siteInfoMapper.findByParam(param);
    }

    /**
     * 根据查询条件获取网点的数量
     * @param param
     * @return
     */
    public Integer findSiteInfoCountByParam(Map<String, Object> param) {
        return siteInfoMapper.findSiteInfoCountByParam(param).intValue();
    }

    /**
     * 根据场景id查询网点编码
     * @param scenariosId
     * @return
     */
    public String findSiteCode(String scenariosId) {
        return siteInfoMapper.findSiteCode(scenariosId);
    }


    /**
     * 根据场景id和网点编码查询网点信息
     * @param scenariosId
     * @return
     */
    public SiteInfo findSiteInfoBySiteCode(String scenariosId,String siteCode) {
        return siteInfoMapper.findSiteInfoBySiteCode(scenariosId,siteCode);
    }


    /**
     * 根据用户的ID查询网点信息
     * @param id 用户ID
     * @return
     */
    public SiteInfo findSiteInfoById(Integer id) {
        return siteInfoMapper.findSiteInfoById(id);
    }

    public String findSiteCodeById(Integer id) {
        return siteInfoMapper.findSiteCodeById(id);
    }


    /**
     * 编辑网点信息
     * @param siteInfo
     */
    public void editSiteInfo(SiteInfo siteInfo) {

        siteInfoMapper.editSiteInfo(siteInfo);
    }

    /**
     * 删除网点信息
     * @param id
     */
    public void delById(Integer id) {

        siteInfoMapper.delById(id);
    }
    /**
     * 删除网点信息
     * @param scenariosId
     */
    public void delbyScenariodId(String scenariosId) {

        siteInfoMapper.delbyScenariodId(scenariosId);
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
     * 导出result_depots excel
     */
    public void exportResult(String scenariosId,String[] titles, ServletOutputStream outputStream)
    {
        List<Map<String,Object>> list = siteInfoMapper.findAllBySiteCode(scenariosId);
        // 创建一个workbook 对应一个excel应用文件
        XSSFWorkbook workBook = new XSSFWorkbook();
        // 在workbook中添加一个sheet,对应Excel文件中的sheet

        XSSFSheet sheet = workBook.createSheet("Depots");
        ExportUtil exportUtil = new ExportUtil(workBook, sheet);
        XSSFCellStyle headStyle = exportUtil.getHeadStyle();
        XSSFCellStyle bodyStyle = exportUtil.getBodyStyle();
        sheet.setColumnWidth(3, 80 * 250);
        // 构建表头
        XSSFRow headRow = sheet.createRow(0);
        XSSFCell cell = null;

        for (int i = 0; i < titles.length; i++)
        {
            cell = headRow.createCell(i);
            cell.setCellValue(titles[i]);
            cell.setCellStyle(headStyle);
            System.out.println(titles[i]);
        }
        // 构建表体数据
        if (list != null && list.size() > 0)
        {
            for (int j = 0; j < list.size(); j++)
            {
                XSSFRow bodyRow = sheet.createRow(j + 1);
                Map<String,Object> depots = list.get(j);

                cell = bodyRow.createCell(0);
                if(depots.get("siteCode").toString()==null || depots.get("siteCode").toString()=="")
                {
                    cell.setCellValue("");
                }
                else
                {
                    cell.setCellValue(depots.get("siteCode").toString());
                }
                cell.setCellStyle(bodyStyle);

                cell = bodyRow.createCell(1);
                if(depots.get("carType").toString()==null || depots.get("carType").toString()=="")
                {
                    cell.setCellValue("");
                }
                else
                {
                    cell.setCellValue(depots.get("carType").toString());
                }
                cell.setCellStyle(bodyStyle);


                cell = bodyRow.createCell(2);
                if(depots.get("arrTime").toString()==null || depots.get("arrTime").toString()=="")
                {
                    cell.setCellValue("");
                }
                else
                {
                    cell.setCellValue(timeTransfer(depots.get("arrTime").toString()));

                }
                cell.setCellStyle(bodyStyle);

                cell = bodyRow.createCell(3);
                if(depots.get("unloadVol").toString()==null || depots.get("unloadVol").toString()=="")
                {
                    cell.setCellValue("Unload 0,Load "+depots.get("sbVol").toString());
                }
                else if(depots.get("sbVol").toString()==null || depots.get("sbVol").toString()=="")
                {
                    cell.setCellValue("Unload "+depots.get("unloadVol")+",Load 0");
                }
                else if(depots.get("unloadVol").toString()==null || depots.get("unloadVol").toString()==""||depots.get("sbVol").toString()==null || depots.get("sbVol").toString()=="")
                {
                    cell.setCellValue("Unload 0,Load 0");
                }
                else
                {
                    cell.setCellValue("Unload "+depots.get("unloadVol").toString()+",Load "+depots.get("sbVol").toString());
                }
                cell.setCellStyle(bodyStyle);

                /*if(depots.get("unloadVol").toString()==null || depots.get("unloadVol").toString()=="") {
                    cell = bodyRow.createCell(3);
                    cell.setCellValue("Unload");
                    cell.setCellStyle(bodyStyle);
                    cell = bodyRow.createCell(4);
                    cell.setCellValue("0");
                    cell.setCellStyle(bodyStyle);
                    cell = bodyRow.createCell(5);
                    cell.setCellValue("Load");
                    cell.setCellStyle(bodyStyle);
                    cell = bodyRow.createCell(6);
                    cell.setCellValue(depots.get("sbVol").toString());
                    cell.setCellStyle(bodyStyle);
                }else if(depots.get("sbVol").toString()==null || depots.get("sbVol").toString()=="") {
                    cell = bodyRow.createCell(3);
                    cell.setCellValue("Unload");
                    cell.setCellStyle(bodyStyle);
                    cell = bodyRow.createCell(4);
                    cell.setCellValue(depots.get("unloadVol").toString());
                    cell.setCellStyle(bodyStyle);
                    cell = bodyRow.createCell(5);
                    cell.setCellValue("Load");
                    cell.setCellStyle(bodyStyle);
                    cell = bodyRow.createCell(6);
                    cell.setCellValue("0");
                    cell.setCellStyle(bodyStyle);
                }else if(depots.get("unloadVol").toString()==null || depots.get("unloadVol").toString()==""||depots.get("sbVol").toString()==null || depots.get("sbVol").toString()=="") {
                    cell.setCellValue("Unload 0,Load 0");
                    cell = bodyRow.createCell(3);
                    cell.setCellValue("Unload");
                    cell.setCellStyle(bodyStyle);
                    cell = bodyRow.createCell(4);
                    cell.setCellValue("0");
                    cell.setCellStyle(bodyStyle);
                    cell = bodyRow.createCell(5);
                    cell.setCellValue("Load");
                    cell.setCellStyle(bodyStyle);
                    cell = bodyRow.createCell(6);
                    cell.setCellValue("0");
                    cell.setCellStyle(bodyStyle);
                }else {
                    cell.setCellValue("Unload "+depots.get("unloadVol").toString()+",Load "+depots.get("sbVol").toString());
                    cell = bodyRow.createCell(3);
                    cell.setCellValue("Unload");
                    cell.setCellStyle(bodyStyle);
                    cell = bodyRow.createCell(4);
                    cell.setCellValue(depots.get("unloadVol").toString());
                    cell.setCellStyle(bodyStyle);
                    cell = bodyRow.createCell(5);
                    cell.setCellValue("Load");
                    cell.setCellStyle(bodyStyle);
                    cell = bodyRow.createCell(6);
                    cell.setCellValue(depots.get("sbVol").toString());
                    cell.setCellStyle(bodyStyle);
                }*/

                cell = bodyRow.createCell(4);
                if(depots.get("endTime").toString()==null || depots.get("endTime").toString()=="")
                {
                    cell.setCellValue("");
                }
                else
                {
                    cell.setCellValue(timeTransfer(depots.get("endTime").toString()));
                }
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

    /**
     * 根据网点编码siteCode获取网点信息
     * param param
     * @return
     */
    public List<Map<String, Object>> findBySiteCode(Map<String, Object> param) {
        List<Map<String, Object>> mapList = siteInfoMapper.findBySiteCode(param);
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
     * 根据网点编码siteCode获网点数量
     * @param param
     * @return
     */
    public Integer findCountBySiteCode(Map<String, Object> param) {
        return siteInfoMapper.findCountBySiteCode(param).intValue();
    }

    /**
     * 根据网点编码siteCode获网点信息
     * param param
     * @return
     */
    public List<Map<String, Object>> findAllBySiteCode(String scenariosId) {
        return siteInfoMapper.findAllBySiteCode(scenariosId);
    }

    /**
     * 根据网点编码siteCode获网点数量
     * @param scenariosId
     * @return
     */
    public Integer findAllCountBySiteCode(String scenariosId) {
        return siteInfoMapper.findAllCountBySiteCode(scenariosId).intValue();
    }

    /**
     * 根据网点编码siteCode查询下一个网点信息
     * param param
     * @return
     */
    public List<Map<String, Object>> findNextSite(String scenariosId,String siteCode) {
        return siteInfoMapper.findNextSite(scenariosId,siteCode);
    }


    /**
     * 导出settings_depots info excel
     */
    public void exportExcel(String scenariosId,String[] titles,String[] nextTitles,ServletOutputStream outputStream)
    {
        List<SiteInfo> list = siteInfoMapper.findAll(scenariosId);
        // 创建一个workbook 对应一个excel应用文件
        XSSFWorkbook workBook = new XSSFWorkbook();
        // 在workbook中添加一个sheet,对应Excel文件中的sheet

        XSSFSheet sheet = workBook.createSheet("DepotsInfo");
        ExportUtil exportUtil = new ExportUtil(workBook, sheet);
        XSSFCellStyle headStyle = exportUtil.getHeadStyle();
        XSSFCellStyle bodyStyle = exportUtil.getBodyStyle();
        // 构建表头
        XSSFRow headRow = sheet.createRow(0);
        XSSFRow nextHeadRow = sheet.createRow(1);
        XSSFCell cell = null;
        for (int i = 0; i < titles.length; i++) {
            cell = headRow.createCell(i);
            cell.setCellValue(titles[i]);
            cell.setCellStyle(headStyle);
            System.out.println(titles[i]);
            cell = nextHeadRow.createCell(i);
            cell.setCellValue(nextTitles[i]);
            cell.setCellStyle(headStyle);
            System.out.println(nextTitles[i]);
        }

        // 构建表体数据
        if (list != null && list.size() > 0)
        {
            for (int j = 0; j < list.size(); j++)
            {
                XSSFRow bodyRow = sheet.createRow(j + 2);
                SiteInfo siteInfo = list.get(j);

                cell = bodyRow.createCell(0);
                cell.setCellValue("NA");
                cell.setCellStyle(bodyStyle);

                cell = bodyRow.createCell(1);
                cell.setCellValue(siteInfo.getSiteCode());
                cell.setCellStyle(bodyStyle);


                cell = bodyRow.createCell(2);
                cell.setCellValue(siteInfo.getSiteLongitude());
                cell.setCellStyle(bodyStyle);

                cell = bodyRow.createCell(3);
                cell.setCellValue(siteInfo.getSiteLatitude());
                cell.setCellStyle(bodyStyle);

                cell = bodyRow.createCell(4);
                cell.setCellValue(siteInfo.getSiteName());
                cell.setCellStyle(bodyStyle);

                cell = bodyRow.createCell(5);
                cell.setCellValue(siteInfo.getSiteAddress());
                cell.setCellStyle(bodyStyle);

                cell = bodyRow.createCell(6);
                cell.setCellValue(siteInfo.getSiteArea());
                cell.setCellStyle(bodyStyle);

                cell = bodyRow.createCell(7);
                cell.setCellValue(siteInfo.getSiteType());
                cell.setCellStyle(bodyStyle);

                cell = bodyRow.createCell(8);
                cell.setCellValue(siteInfo.getDistribCenter());
                cell.setCellStyle(bodyStyle);

                cell = bodyRow.createCell(9);
                cell.setCellValue(siteInfo.getSiteNightDelivery());
                cell.setCellStyle(bodyStyle);

                cell = bodyRow.createCell(10);
                cell.setCellValue(siteInfo.getNoOfTruckLimitation());
                cell.setCellStyle(bodyStyle);

                cell = bodyRow.createCell(11);
                cell.setCellValue(siteInfo.getNoOfBaiduLimitation());
                cell.setCellStyle(bodyStyle);

                cell = bodyRow.createCell(12);
                cell.setCellValue(siteInfo.getNoOfDidiLimitation());
                cell.setCellStyle(bodyStyle);

                cell = bodyRow.createCell(13);
                cell.setCellValue(siteInfo.getNoOfDadaLimitation());
                cell.setCellStyle(bodyStyle);

                cell = bodyRow.createCell(14);
                cell.setCellValue(siteInfo.getReserve());
                cell.setCellStyle(bodyStyle);

                cell = bodyRow.createCell(15);
                cell.setCellValue(siteInfo.getLargeCarModel());
                cell.setCellStyle(bodyStyle);

                cell = bodyRow.createCell(16);
                cell.setCellValue(siteInfo.getMaxOperateNum());
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
