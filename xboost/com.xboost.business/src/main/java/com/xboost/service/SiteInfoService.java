package com.xboost.service;

import com.xboost.exception.ForbiddenException;
import com.xboost.exception.NotFoundException;
import com.xboost.mapper.SiteInfoMapper;
import com.xboost.pojo.SiteDist;
import com.xboost.pojo.SiteInfo;

import com.xboost.util.ExportUtil;
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

/**
 * Created by Administrator on 2017/11/5 0005.
 */

@Named
@Transactional
public class SiteInfoService {

    private static Logger logger = LoggerFactory.getLogger(SiteInfoService.class);

    @Inject
    private SiteInfoMapper siteInfoMapper;

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
                        List<String> lineList = excelUtil.readExcel(fileTmp);
                        for(int i=0;i<lineList.size();i++){
                            String[] row = lineList.get(i).split("#");
                            //网点编码
                            siteInfo.setSiteCode(row[2]);
                            //网点经度
                            siteInfo.setSiteLongitude(row[3]);
                            //网点纬度
                            siteInfo.setSiteLatitude(row[4]);
                            //网点名称
                            siteInfo.setSiteName(row[5]);
                            //网点地址
                            siteInfo.setSiteAddress(row[6]);
                            //是否可以做夜配集散点
                            siteInfo.setSiteNightDelivery(Integer.parseInt(row[7]));
                            //网点面积
                            siteInfo.setSiteArea(row[8]);
                            //网点类型
                            siteInfo.setSiteType(row[9]);
                            //停货车数(辆)(10分钟停靠能力)
                            siteInfo.setCarNum(row[10]);
                            //进出最大车型(10分钟停靠能力)
                            siteInfo.setLargeCarModel(row[11]);
                            //单一批量操作处理量上限(以一个班次操作为单位)
                            siteInfo.setMaxOperateNum(row[12]);
                            siteInfo.setDistribCenter(row[13]);
                            siteInfo.setCreateTime(DateTime.now().toString("yyyy-MM-dd HH:mm"));

                            //ID
                            if(null==row[0] || ""==row[0] || " "==row[0] ){
                                //insert
                                siteInfoMapper.save(siteInfo);
                                logger.info("insert into db:"+siteInfo.getSiteCode());
                            }else{
                                siteInfo.setId(Integer.parseInt(row[1]));
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
     * 导出excel
     */
    public void exportExcel(String scenariosId,String[] titles, ServletOutputStream outputStream)
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
                SiteInfo siteInfo = list.get(j);

                cell = bodyRow.createCell(0);
                cell.setCellValue(siteInfo.getId());
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
                cell.setCellValue(siteInfo.getCarNum());
                cell.setCellStyle(bodyStyle);

                cell = bodyRow.createCell(11);
                cell.setCellValue(siteInfo.getLargeCarModel());
                cell.setCellStyle(bodyStyle);

                cell = bodyRow.createCell(12);
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
