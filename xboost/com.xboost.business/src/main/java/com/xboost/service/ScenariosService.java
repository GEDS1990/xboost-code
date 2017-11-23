package com.xboost.service;

import com.xboost.mapper.ScenariosMapper;
import com.xboost.pojo.Scenarios;
import com.xboost.util.ExcelUtil;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.inject.Inject;
import javax.inject.Named;
import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 * Created by Administrator on 2017/11/5 0005.
 */

@Named
@Transactional
public class ScenariosService {

    private static Logger logger = LoggerFactory.getLogger(ScenariosService.class);

    @Inject
    private ScenariosMapper scenariosMapper;

    /**
     * 保存新场景信息
     * @param scenario
     */
    public void save(Scenarios scenario) {
        scenario.setCreateTime(DateTime.now().toString("yyyy-MM-dd HH:mm"));

        scenariosMapper.save(scenario);

    }

    //通过Excel新增场景信息
    public void addByExcel(Scenarios scenario,MultipartFile[] file) {
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
                            scenario.setUserId(Integer.parseInt(row[1]));
                            scenario.setScenariosName(row[2]);
                            scenario.setScenariosCategory(row[3]);
                            scenario.setScenariosDesc(row[4]);
                            scenario.setScenariosModel(row[5]);
                            scenario.setScenariosOut(row[6]);
                            scenario.setScenariosStatus(row[7]);
                            scenario.setLastOpenTime(row[8]);
                            scenario.setCreateTime(DateTime.now().toString("yyyy-MM-dd HH:mm"));

                            //insert
                            scenariosMapper.save(scenario);
                            logger.info("insert into db:"+scenario.getUserId());
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
     * 查询所有场景信息
     * param
     * @return
     */
    public List<Scenarios> findAll() {
        return scenariosMapper.findAll();
    }

       /**
     * 获取场景的总数量
     * @return
     */
    public Integer findAllCount() {
        return scenariosMapper.findAllCount().intValue();
    }

    /**
     * 根据查询条件查询场景信息
     * param siteName
     * @return
     */
    public List<Scenarios> findByParam(Map<String, Object> param) {
        return scenariosMapper.findByParam(param);
    }

    /**
     * 根据查询条件获取场景信息的数量
     * @param param
     * @return
     */
    public Integer findCountByParam(Map<String, Object> param) {
        return scenariosMapper.findCountByParam(param).intValue();
    }


    /**
     * 根据ID查询场景信息
     * @param id 用户ID
     * @return
     */
    public Scenarios findById(Integer id) {
        return scenariosMapper.findById(id);
    }


    /**
     * 编辑场景信息
     * @param scenario
     */
    public void edit(Scenarios scenario) {

        scenariosMapper.edit(scenario);
    }

    /**
     * 删除场景信息
     * @param id
     */
    public void delById(Integer id) {

        scenariosMapper.delById(id);
    }

}
