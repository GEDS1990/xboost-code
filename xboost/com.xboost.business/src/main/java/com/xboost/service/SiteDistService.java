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
import java.util.Random;

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
    public void addSiteDistByExcel(SiteDist siteDist,MultipartFile[] file) {
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
//                        File fileTemp = (File) multipartFile;
                        ExcelUtil excelUtil = new ExcelUtil();
                            List<String> lineList = excelUtil.readExcel(fileTmp,2);
//                        NumberFormat numberFormat = NumberFormat.getInstance();
//                        numberFormat.setMaximumFractionDigits(2);
                        for(int i=2;i<lineList.size();i++){
                                String[] row = lineList.get(i).split("#");
                                siteDist.setCarType(row[1]);
                                siteDist.setSiteCollect(row[2]);
                                siteDist.setSiteDelivery(row[3]);
                                siteDist.setCarDistance(Float.parseFloat(row[4]));
                                siteDist.setDurationNightDelivery(row[5]);
                                siteDist.setDurationNightDelivery2(row[6]);
                                siteDist.setDurationNightDelivery3(row[7]);
                                siteDist.setCreateTime(DateTime.now().toString("yyyy-MM-dd HH:mm"));
                                if(null==row[0] || ""==row[0] || " "==row[0] || "NA".equals(row[0]) ){
                                    //insert
                                    siteDistMapper.save(siteDist);
                                    logger.info("insert into db:"+siteDist.getSiteCollect());
                                }else{
                                    siteDist.setId(Integer.parseInt(row[0]));
                                    //insert
                                    siteDistMapper.update(siteDist);
                                    logger.info("insert into db:"+siteDist.getSiteCollect());
                                }
//                                session.setAttribute("progress",numberFormat.format((float) i / (float) lineList.size() * 100)+"%");
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
     * 查询网点最大距离
     * @param scenariosId
     * @return
     */
    public Float findFarthestDist(String scenariosId) {
        return siteDistMapper.findFarthestDist(scenariosId);
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
     * 删除网点信息
     * @param scenariosId
     */
    public void delByScenariosIdd(String scenariosId) {

        siteDistMapper.delByScenariosIdd(scenariosId);
    }

    /**
     * 导出excel
     */
    public void exportExcel(String scenariosId,String[] titles,ServletOutputStream outputStream)
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
                 cell.setCellValue(siteDist.getId());
                 cell.setCellStyle(bodyStyle);

                 cell = bodyRow.createCell(1);
                 cell.setCellValue(siteDist.getCarType());
                 cell.setCellStyle(bodyStyle);

                 cell = bodyRow.createCell(2);
                 cell.setCellValue(siteDist.getSiteCollect());
                 cell.setCellStyle(bodyStyle);


                 cell = bodyRow.createCell(3);
                 cell.setCellValue(siteDist.getSiteDelivery());
                 cell.setCellStyle(bodyStyle);

                 cell = bodyRow.createCell(4);
                 cell.setCellValue(siteDist.getCarDistance());
                 cell.setCellStyle(bodyStyle);

                 cell = bodyRow.createCell(5);
                 cell.setCellValue(siteDist.getDurationNightDelivery());
                 cell.setCellStyle(bodyStyle);

                 cell = bodyRow.createCell(6);
                 cell.setCellValue(siteDist.getDurationNightDelivery2());
                 cell.setCellStyle(bodyStyle);

                 cell = bodyRow.createCell(7);
                 cell.setCellValue(siteDist.getDurationNightDelivery3());
                 cell.setCellStyle(bodyStyle);


             }
         }
         try
         {
            // String path = request.getServletPath(excelDownload);
            // String path = getServletContext().getRealPath("/");
//             String path = this.getClass().getClassLoader().getResource("").getPath();
//             String fileName = new String(("DepotsDistance_"+DateTime.now().toString("yyyyMMddHHmm")+new Random().nextInt()).getBytes(), "utf-8");
//             FileOutputStream fout = new FileOutputStream(path + fileName +".xlsx");
//             workBook.write(fout);
//             fout.flush();
//             fout.close();
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
