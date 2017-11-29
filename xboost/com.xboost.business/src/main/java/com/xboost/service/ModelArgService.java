package com.xboost.service;

import com.xboost.mapper.ModelArgMapper;
import com.xboost.pojo.ModelArg;
import com.xboost.pojo.SiteInfo;
import com.xboost.util.ExcelUtil;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.inject.Inject;
import javax.inject.Named;
import java.io.File;
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
                        fileTmp=new File("src/main/resources/upload/temp/"+tempTime+ ".xlsx");
                        if (!fileTmp.exists()) fileTmp.mkdirs();
                        multipartFile.transferTo(fileTmp);
//                      File fileTemp = (File) multipartFile;
                        ExcelUtil excelUtil = new ExcelUtil();
                        List<String> lineList = excelUtil.readExcel(fileTmp);
                        for(int i=0;i<lineList.size();i++){
                            String[] row = lineList.get(i).split("#");
                            modelArg.setParameterName(row[0]);
                            modelArg.setData(row[1]);
                            modelArg.setNote(row[2]);
                            modelArg.setCreateTime(DateTime.now().toString("yyyy-MM-dd HH:mm"));


                            //insert
                            modelArgMapper.add(modelArg);
                            logger.info("insert into db:"+modelArg.getParameterName()+":"+modelArg.getData());
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
     * 整体模型参数导出
     */

//    public void exportExcel{
//        String title = Message.getString("manifestIExportTitle");
//        String[] rowsName = new String[]{"Parameters","货物运输批次号","提运单号","状态","录入人","录入时间"};
//        List<Object[]>  dataList = new ArrayList<Object[]>();
//        Object[] objs = null;
//        for (int i = 0; i < manifestIMainList.size(); i++) {
//            ManifestIMain man = manifestIMainList.get(i);
//            objs = new Object[rowsName.length];
//            objs[0] = i;
//            objs[1] = man.getTranNo();
//            objs[2] = man.getBillNo();
//            objs[3] = man.getStatusFlagCnName();
//            objs[4] = man.getLoginName();
//            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//            String date = df.format(man.getModiDate());
//            objs[5] = date;
//            dataList.add(objs);
//        }
//        ExportExcel ex = new ExportExcel(title, rowsName, dataList);
//        ex.export();
//    }

}
