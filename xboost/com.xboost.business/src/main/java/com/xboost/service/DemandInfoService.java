package com.xboost.service;

import com.mckinsey.sf.data.Car;
import com.xboost.mapper.CarMapper;
import com.xboost.mapper.DemandInfoMapper;
import com.xboost.pojo.DemandInfo;
import com.xboost.pojo.SiteDist;
import com.xboost.util.ExcelUtil;
import com.xboost.util.ExportUtil;
import com.xboost.util.ShiroUtil;
import org.apache.poi.xssf.usermodel.*;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.inject.Inject;
import javax.inject.Named;
import javax.security.auth.Subject;
import javax.servlet.ServletOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/11/12 0005.
 */

@Named
@Transactional
public class DemandInfoService {

    private static Logger logger = LoggerFactory.getLogger(SiteInfoService.class);

    @Inject
    private DemandInfoMapper demandInfoMapper;

    @Inject
    CarMapper carMapper;
    /**
     * 新增需求信息 
     * @param demandInfo
     */
    public void addDemandInfo(DemandInfo demandInfo) {
        demandInfo.setCreateTime(DateTime.now().toString("yyyy-MM-dd HH:mm"));

        demandInfoMapper.add(demandInfo);

    }


    public Car[] findCarByParam(Map<String, Object> param) {
        return carMapper.findCarByParam(param);
    }

    //通过Excel新增网点信息
    public void addDemandInfoByExcel(DemandInfo demandInfo, MultipartFile[] file) {
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
                        for(int i=1;i<lineList.size();i++){
                            String[] row = lineList.get(i).split("#");
                            //日期
                            demandInfo.setDate(row[1]);
                            //收件网点编码
                            demandInfo.setSiteCodeCollect(row[2]);
                            //时段（开始）
                            try{
                                d = Integer.parseInt(row[3].split(":")[0])*60+Integer.parseInt(row[3].split(":")[1]);
                            }catch (java.lang.ArrayIndexOutOfBoundsException e){
                                d =Integer.parseInt(row[3]);
                            }
                            demandInfo.setDurationStart(String.valueOf(d));
                            //派件网点编码
                            demandInfo.setSiteCodeDelivery(row[4]);
                            //时段(结束）
                            try{
                                d = Integer.parseInt(row[5].split(":")[0])*60+Integer.parseInt(row[5].split(":")[1]);
                            }catch (java.lang.ArrayIndexOutOfBoundsException e){
                                d =Integer.parseInt(row[5]);
                            }
                            demandInfo.setDurationEnd(String.valueOf(d));
                            //票数（票）
                            demandInfo.setVotes(row[6]);
                            //重量（公斤）
                            demandInfo.setWeight(row[7]);
                            //产品类型
                            demandInfo.setProductType(row[8]);
                            //时效要求(小时)
                            demandInfo.setAgeing("NA");
                            demandInfo.setCreateTime(DateTime.now().toString("yyyy-MM-dd HH:mm"));
                            if(null==row[0] || ""==row[0] || " "==row[0] || "NA".equals(row[0]) ){
                                demandInfo.setId(row[0]);
                                //update
                                demandInfoMapper.add(demandInfo);
                                logger.info("update into db:"+demandInfo.getId());
                            }else{
                                //insert
                                demandInfoMapper.add(demandInfo);
                                logger.info("insert into db:"+demandInfo.getId());
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
     * 查询所有需求信息 
     * param
     * @return
     */
    public List<DemandInfo> findAll(String scenariosId) {
        return demandInfoMapper.findAll(scenariosId);
    }

    /**
     * 查询所有需求信息 包含起点和终点的负重
     * param
     * @return
     */
    public List<Map<String, Object>> findAllAndWeight(String scenariosId) {
        return demandInfoMapper.findAllAndWeight(scenariosId);
    }

    //max-min
    public String findMinMax(String scenariosId){
        return demandInfoMapper.findMinMax(scenariosId);
    }

    //max
    public String findMax(String scenariosId){
        return demandInfoMapper.findMax(scenariosId);
    }

    //min
    public String findMin(String scenariosId){
        return demandInfoMapper.findMin(scenariosId);
    }

    /**
     * 获取需求信息总数量
     * @return
     */
    public Integer findAllCount(String scenariosId) {
        return demandInfoMapper.findAllCount(scenariosId).intValue();
    }

    /**
     * 根据查询条件获取需求信息 
     * param param
     * @return
     */
    public List<DemandInfo> findByParam(Map<String, Object> param) {
        return demandInfoMapper.findByParam(param);
    }

    /**
     * 根据查询条件获取需求信息
     * param param
     * @return
     */
    public List<DemandInfo> findByScenairoIdParam(Map<String, Object> param) {
        return demandInfoMapper.findByScenairoIdParam(param);
    }

    /**
     * 根据查询条件获取需求信息 
     * @param param
     * @return
     */
    public Integer findCountByParam(Map<String, Object> param) {
        return demandInfoMapper.findCountByParam(param).intValue();
    }


    /**
     * 根据用户的ID查询需求信息 
     * @param id 用户ID
     * @return
     */
    public DemandInfo findById(Integer id) {
        return demandInfoMapper.findById(id);
    }


    /**
     * 编辑需求信息 
     * @param demandInfo
     */
    public void editDemandInfo(DemandInfo demandInfo) {

        demandInfoMapper.editDemandInfo(demandInfo);
    }

    /**
     * 根据id删除需求信息 
     * @param id
     */
    public void delById(Integer id) {

        demandInfoMapper.delById(id);
    }
    /**
     * 根据id删除需求信息
     * @param scenariosId
     */
    public void delByScenariosId(String scenariosId) {

        demandInfoMapper.delByScenariosId(scenariosId);
    }


    /**
     * 导出excel
     */
    public void exportExcel(String scenariosId,String[] titles,ServletOutputStream outputStream )
    {
        List<DemandInfo> list = demandInfoMapper.findAll(scenariosId);
        // 创建一个workbook 对应一个excel应用文件
        XSSFWorkbook workBook = new XSSFWorkbook();
        // 在workbook中添加一个sheet,对应Excel文件中的sheet

        XSSFSheet sheet = workBook.createSheet("Demands");
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
            try{

                for (int j = 0; j < list.size(); j++)
                {
                    XSSFRow bodyRow = sheet.createRow(j + 1);
                    DemandInfo demandInfo = list.get(j);
                    int i = 0;

                    cell = bodyRow.createCell(0+i);
                    cell.setCellValue("NA");
                    cell.setCellStyle(bodyStyle);

                    cell = bodyRow.createCell(1+i);
                    cell.setCellValue(demandInfo.getDate());
                    cell.setCellStyle(bodyStyle);


                    cell = bodyRow.createCell(2+i);
                    cell.setCellValue(demandInfo.getSiteCodeCollect());
                    cell.setCellStyle(bodyStyle);

                    cell = bodyRow.createCell(3+i);
                    cell.setCellValue(demandInfo.getDurationStart());
                    cell.setCellStyle(bodyStyle);

                    cell = bodyRow.createCell(4+i);
                    cell.setCellValue(demandInfo.getSiteCodeDelivery());
                    cell.setCellStyle(bodyStyle);

                    cell = bodyRow.createCell(5+i);
                    cell.setCellValue(demandInfo.getDurationEnd());
                    cell.setCellStyle(bodyStyle);

                    cell = bodyRow.createCell(6+i);
                    cell.setCellValue(demandInfo.getVotes());
                    cell.setCellStyle(bodyStyle);

                    cell = bodyRow.createCell(7+i);
                    cell.setCellValue(demandInfo.getWeight());
                    cell.setCellStyle(bodyStyle);

                    cell = bodyRow.createCell(8+i);
                    cell.setCellValue(demandInfo.getProductType());
                    cell.setCellStyle(bodyStyle);

                    /*cell = bodyRow.createCell(9+i);
                    cell.setCellValue(demandInfo.getAgeing());
                    cell.setCellStyle(bodyStyle);*/
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
        try
        {
//            FileOutputStream fout = new FileOutputStream("E:/Demands.xlsx");
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
    //
    public List<Map> findarrTime(){
        return demandInfoMapper.findarrTime(ShiroUtil.getOpenScenariosId());
    }

    //网点数
    public Integer siteCount()
    {
        List<String> siteCollect = demandInfoMapper.findSiteCollect(ShiroUtil.getOpenScenariosId());
        List<String> siteDelivery = demandInfoMapper.findSiteDelivery(ShiroUtil.getOpenScenariosId());
        int siteCount = 0;
        for(int i=0;i<siteCollect.size();i++){
            siteCount = siteDelivery.size();
            if(siteDelivery.contains(siteCollect.get(i))){
                siteCount = siteCount + 0;
            }
            else
            {
                siteCount = siteCount + 1;
            }
        }
        return siteCount;
    }

}
