package com.xboost.service;

import com.xboost.mapper.ModelArgMapper;
import com.xboost.pojo.ModelArg;
import com.xboost.pojo.SiteDist;
import com.xboost.pojo.SiteInfo;
import com.xboost.util.ExcelUtil;
import com.xboost.util.ExportUtil;
import com.xboost.util.ShiroUtil;
import org.apache.ibatis.annotations.Param;
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
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/11/5 0005.
 */

@Named
@Transactional
public class ModelArgService {
    private static Logger logger = LoggerFactory.getLogger(SiteInfoService.class);

    @Inject
    private ModelArgMapper modelArgMapper;

    /**
     * 新增模型整体参数信息
     * @param modelArg
     */
    public void addModelArg(ModelArg modelArg) {
        modelArg.setCreateTime(DateTime.now().toString("yyyy-MM-dd HH:mm"));

        modelArgMapper.add(modelArg);

    }

    //通过Excel新增网点信息
    public void addByExcel(ModelArg modelArg, MultipartFile[] file) {
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
                        for(int i=0;i<lineList.size();i++){
                            String[] row = lineList.get(i).split("#");
                            modelArg.setModelType(row[1]);
                            modelArg.setParameterCode(row[2]);
                            modelArg.setParameterName(row[3]);
                            modelArg.setData(row[4]);
                            modelArg.setNote(row[5]);
                            modelArg.setCreateTime(DateTime.now().toString("yyyy-MM-dd HH:mm"));

                            if(null==row[0] || ""==row[0] || " "==row[0] || "NA".equals(row[0]) ){
                                //insert
                                modelArgMapper.add(modelArg);
                                logger.info("update db:"+modelArg.getParameterName()+":"+modelArg.getData());
                            }else{
                                //update
                                modelArg.setId(row[0]);
                                modelArgMapper.update(modelArg);
                                logger.info("insert into db:"+modelArg.getParameterName()+":"+modelArg.getData());
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
     * 查询所有模型整体参数信息
     * param
     * @return
     */
    public List<ModelArg> findAllModelArg(String scenariosId) {
        return modelArgMapper.findAll(scenariosId);
    }

    /**
     * 获取模型整体参数信息总数量
     * @return
     */
    public Integer findAllCount(String scenariosId) {
        return modelArgMapper.findAllCount(scenariosId).intValue();
    }

    /**
     * 根据查询条件获取模型整体参数信息
     * param param
     * @return
     */
    public List<ModelArg> findByParam(Map<String, Object> param) {
        return modelArgMapper.findByParam(param);
    }

    /**
     * 根据查询条件获取模型整体参数信息的数量
     * @param param
     * @return
     */
    public Integer findCountByParam(Map<String, Object> param) {
        return modelArgMapper.findCountByParam(param).intValue();
    }


    /**
     * 根据用户的ID查询模型整体参数信息
     * @param id 用户ID
     * @return
     */
    public ModelArg findById(Integer id) {
        return modelArgMapper.findById(id);
    }


    /**
     * 编辑模型整体参数信息
     * @param modelArg
     */
    public void editModelArg(ModelArg modelArg) {

        modelArgMapper.editModelArg(modelArg);
    }

    /**
     * 根据id删除模型整体参数信息
     * @param id
     */
    public void delById(Integer id) {

        modelArgMapper.delById(id);
    }
    /**
     * 根据id删除模型整体参数信息
     * @param scenariosId
     */
    public void delByScenariosId(String scenariosId) {

        modelArgMapper.delByScenariosId(scenariosId);
    }

    //根据场景id,算法模型查询网点集散点人效
    public Integer findSitePeopleWork(String scenariosId,String modelType){

        return modelArgMapper.findSitePeopleWork(scenariosId,modelType);
    }

    //根据场景id,算法模型查询集配站集散点人效
    public Integer findDistribPeopleWork(String scenariosId,String modelType){

        return modelArgMapper.findDistribPeopleWork(scenariosId,modelType);
    }

    /**
     * 导出excel
     */
    public void exportExcel(String scenariosId,String[] titles,ServletOutputStream outputStream)
    {
        List<ModelArg> list = modelArgMapper.findAll(scenariosId);
        // 创建一个workbook 对应一个excel应用文件
        XSSFWorkbook workBook = new XSSFWorkbook();
        // 在workbook中添加一个sheet,对应Excel文件中的sheet

        XSSFSheet sheet = workBook.createSheet("Parameters");
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
                ModelArg modelArg = list.get(j);

                cell = bodyRow.createCell(0);
                cell.setCellValue("NA");
                cell.setCellStyle(bodyStyle);

                cell = bodyRow.createCell(1);
                cell.setCellValue(modelArg.getModelType());
                cell.setCellStyle(bodyStyle);

                cell = bodyRow.createCell(2);
                cell.setCellValue(modelArg.getParameterCode());
                cell.setCellStyle(bodyStyle);


                cell = bodyRow.createCell(3);
                cell.setCellValue(modelArg.getParameterName());
                cell.setCellStyle(bodyStyle);

                cell = bodyRow.createCell(4);
                cell.setCellValue(modelArg.getData());
                cell.setCellStyle(bodyStyle);

                cell = bodyRow.createCell(5);
                cell.setCellValue(modelArg.getNote());
                cell.setCellStyle(bodyStyle);
            }
        }
        try
        {
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
