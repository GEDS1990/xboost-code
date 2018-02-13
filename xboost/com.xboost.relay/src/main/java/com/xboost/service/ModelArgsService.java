package com.xboost.service;

import com.xboost.mapper.ModelArgsMapper;
import com.xboost.pojo.ModelArgs;
import com.xboost.util.ExcelUtil;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/11/5 0005.
 */

@Service
public class ModelArgsService {
    private static Logger logger = LoggerFactory.getLogger(SiteInfoService.class);

    @Autowired
    private ModelArgsMapper modelArgsMapper;

    /**
     * 新增模型整体参数信息
     * @param modelArgs
     */
    public void add(ModelArgs modelArgs) {
        modelArgs.setCreateTime(DateTime.now().toString("yyyy-MM-dd HH:mm"));
        modelArgsMapper.add(modelArgs);
    }

//    //通过Excel新增整体模型参数
//    public void addByExcel(ModelArgs modelArgs, MultipartFile[] file) {
//        //判断文件集合是否有文件
//        if(file != null && file.length > 0) {
//            for(MultipartFile multipartFile : file) {
//                if(!multipartFile.isEmpty()) {
//                    //解析文件并存储到数据库
//                    File fileTmp = null;
//                    long tempTime = System.currentTimeMillis();
//                    try {
//                        fileTmp=new File(System.getProperty("user.dir")+"/temp/"+tempTime+ ".xlsx");
//                        if (!fileTmp.exists()) fileTmp.mkdirs();
//                        multipartFile.transferTo(fileTmp);
////                      File fileTemp = (File) multipartFile;
//                        ExcelUtil excelUtil = new ExcelUtil();
//                        List<String> lineList = excelUtil.readExcel(fileTmp);
//                        for(int i=0;i<lineList.size();i++){
//                            String[] row = lineList.get(i).split("#");
//                            modelArgs.setModelType(row[1]);
//                            modelArgs.setDurationCollect(Integer.parseInt(row[2]));
//                            modelArgs.setCreateTime(DateTime.now().toString("yyyy-MM-dd HH:mm"));
//                            if(null==row[0] || ""==row[0] || " "==row[0] ){
//                                modelArgs.setId(Integer.parseInt(row[0]));
//                                //update
//                                modelArgsMapper.edit(modelArgs);
//                                logger.info("update db:"+modelArgs.getModelType());
//                            }else{
//                                //insert
//                                modelArgsMapper.add(modelArgs);
//                                logger.info("insert into db:"+modelArgs.getModelType());
//                            }
//                        }
//                        logger.info("insert into db complete");
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//      }
//
//    }


    /**
     * 查询所有模型整体参数信息
     * param
     * @return
     */
    public List<ModelArgs> findAllModelArg(String scenariosId,String modelType) {
        return modelArgsMapper.findAll(scenariosId,modelType);
    }

    /**
     * 获取模型整体参数信息总数量
     * @return
     */
    public Integer findAllCount(String scenariosId,String modelType) {
        return modelArgsMapper.findAllCount(scenariosId,modelType).intValue();
    }

    /**
     * 根据用户的ID查询模型整体参数信息
     * @param id 用户ID
     * @return
     */
    public ModelArgs findById(Integer id) {
        return modelArgsMapper.findById(id);
    }

    public ModelArgs findByScenariosId(String scenariosId) {
        return modelArgsMapper.findByScenariosId(scenariosId);
    }

    /**
     * 编辑模型整体参数信息
     * @param modelArgs
     */
    public void edit(ModelArgs modelArgs) {

        modelArgsMapper.edit(modelArgs);
    }


}
