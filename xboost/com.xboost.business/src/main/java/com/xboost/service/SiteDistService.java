package com.xboost.service;

import com.mysql.jdbc.log.LogFactory;
import com.xboost.mapper.SiteDistMapper;
import com.xboost.pojo.SiteDist;
import com.xboost.pojo.SiteInfo;
import com.xboost.util.ExcelUtil;
import com.xboost.util.ExportUtil;
import com.xboost.util.QiniuUtil;
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
import java.io.*;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/11/12 0012.
 */
@Named
@Transactional
public class SiteDistService {
    @Inject
    private QiniuUtil qiniuUtil;

    @Inject
    SiteDistMapper siteDistMapper;

    private Logger logger = LoggerFactory.getLogger(SiteDistService.class);

    /**
     * 保存新网点距离信息
     * @param siteDist
     */
    public void saveSiteDist(SiteDist siteDist) {
        siteDist.setCreateTime(DateTime.now().toString("yyyy-MM-dd HH:mm"));

        siteDistMapper.save(siteDist);

    }

    public List<SiteDist> findSiteDistByScenariosId(Map<String, Object> param) {
        return siteDistMapper.findSiteDistByScenariosId(param);
    }

    //通过Excel新增网点距离信息
    public void saveSiteDist(SiteDist siteDist,MultipartFile[] file) {
        //判断文件集合是否有文件
        if(file != null && file.length > 0) {
            for(MultipartFile multipartFile : file) {
                if(!multipartFile.isEmpty()) {
                    //解析文件并存储到数据库
                    File fileTmp = null;
                    long tempTime = System.currentTimeMillis();
                    try {
                        fileTmp=new File("src/main/resources/upload/temp/"+tempTime+ ".xlsx");
                        if (!fileTmp.exists()) fileTmp.mkdirs();
                        multipartFile.transferTo(fileTmp);
//                        File fileTemp = (File) multipartFile;
                        ExcelUtil excelUtil = new ExcelUtil();
                            List<String> lineList = excelUtil.readExcel(fileTmp);
                            for(int i=0;i<lineList.size();i++){
                                String[] row = lineList.get(i).split("#");
                                siteDist.setSiteCollect(row[0]);
                                siteDist.setSiteDelivery(row[1]);
                                siteDist.setCarDistance(Float.parseFloat(row[2]));
                                siteDist.setDurationNightDelivery(Double.parseDouble(row[3]));
                                siteDist.setCreateTime(DateTime.now().toString("yyyy-MM-dd HH:mm"));
                                //insert
                                siteDistMapper.save(siteDist);
                                logger.info("insert into db:"+siteDist.getSiteCollect());
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
     * 查询所有网点距离信息
     * param
     * @return
     */
    public List<SiteDist> findAllSiteDist(String scenariosId) {
        return siteDistMapper.findAll(scenariosId);
    }

    /**
     * 获取网点距离信息的总数量
     * @return
     */
    public Integer findAllCount(String scenariosId) {
        return siteDistMapper.findAllCount(scenariosId).intValue();
    }

    /**
     * 根据网点名称查询网点距离信息
     * param siteName
     * @return
     */
    public List<SiteDist> findByParam(Map<String, Object> param) {
        return siteDistMapper.findByParam(param);
    }

    /**
     * 根据查询条件获取网点距离信息的数量
     * @param param
     * @return
     */
    public Integer findCountByParam(Map<String, Object> param) {
        return siteDistMapper.findCountByParam(param).intValue();
    }


    /**
     * 根据用户的ID查询网点信息
     * @param id 用户ID
     * @return
     */
    public SiteDist findById(Integer id) {
        return siteDistMapper.findById(id);
    }


    /**
     * 编辑网点信息
     * @param siteDist
     */
    public void editSiteDist(SiteDist siteDist) {

        siteDistMapper.editSiteDist(siteDist);
    }

    /**
     * 删除网点信息
     * @param id
     */
    public void delById(Integer id) {

        siteDistMapper.delById(id);
    }

    /**
     * 导出excel
     */
    public void exportExcel(String scenariosId,String[] titles)
    {
        List<SiteDist> list = siteDistMapper.findAll(scenariosId);
         // 创建一个workbook 对应一个excel应用文件
         XSSFWorkbook workBook = new XSSFWorkbook();
         // 在workbook中添加一个sheet,对应Excel文件中的sheet

         XSSFSheet sheet = workBook.createSheet("DepotsDistance");
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
                 SiteDist siteDist = list.get(j);

                 cell = bodyRow.createCell(0);
                 cell.setCellValue(siteDist.getSiteCollect());
                 cell.setCellStyle(bodyStyle);


                 cell = bodyRow.createCell(1);
                 cell.setCellValue(siteDist.getSiteDelivery());
                 cell.setCellStyle(bodyStyle);

                 cell = bodyRow.createCell(2);
                 cell.setCellValue(siteDist.getCarDistance());
                 cell.setCellStyle(bodyStyle);

                 cell = bodyRow.createCell(3);
                 cell.setCellValue(siteDist.getDurationNightDelivery());
                 cell.setCellStyle(bodyStyle);

             }
         }
         try
         {
             FileOutputStream fout = new FileOutputStream("E:/distance.xlsx");
             workBook.write(fout);
             fout.flush();
             fout.close();
//             workBook.write(outputStream);
//             outputStream.flush();
//             outputStream.close();
         }
         catch (IOException e)
         {
             e.printStackTrace();
         }

    }


}
