package com.xboost.service;

import com.xboost.mapper.MyScenariosMapper;
import com.xboost.pojo.Scenarios;
import com.xboost.pojo.ScenariosCategory;
import com.xboost.util.ExcelUtil;
import com.xboost.util.ShiroUtil;
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

/**
 * Created by Administrator on 2017/11/5 0005.
 */

@Named
@Transactional
public class MyScenariosService {

    private static Logger logger = LoggerFactory.getLogger(MyScenariosService.class);

    @Inject
    private MyScenariosMapper myScenariosMapper;

    /**
     * 保存新场景信息
     * @param scenario
     */
    public void save(Scenarios scenario) {
        scenario.setCreateTime(DateTime.now().toString("yyyy-MM-dd HH:mm"));
        scenario.setUserId(ShiroUtil.getCurrentUserId());
        myScenariosMapper.save(scenario);
        myScenariosMapper.saveScenariosIduserId(scenario.getId(),ShiroUtil.getCurrentUserId());

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
                            scenario.setUserId(ShiroUtil.getCurrentUserId());

                            //insert
                            myScenariosMapper.save(scenario);
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
    public List<Scenarios> findAll(Integer userId) {
        return myScenariosMapper.findAll(userId);
    }

       /**
     * 获取场景的总数量
     * @return
     */
    public Integer findAllCount(Integer userId) {
        return myScenariosMapper.findAllCount(userId).intValue();
    }

    /**
     * 根据查询条件查询场景信息
     * param siteName
     * @return
     */
    public List<Scenarios> findByParam(Map<String, Object> param) {
        return myScenariosMapper.findByParam(param);
    }

    /**
     * 根据查询条件获取场景信息的数量
     * @param param
     * @return
     */
    public Integer findCountByParam(Map<String, Object> param) {
        return myScenariosMapper.findCountByParam(param).intValue();
    }


    /**
     * 根据ID查询场景信息
     * @param id 用户ID
     * @return
     */
    public Scenarios findById(Integer id) {
        return myScenariosMapper.findById(id);
    }


    /**
     * 编辑场景信息
     * @param scenario
     */
    public void edit(Scenarios scenario) {

        myScenariosMapper.edit(scenario);
    }

    /**
     * 删除场景信息
     * @param id
     */
    public void delById(Integer id) {

        myScenariosMapper.delById(id);
    }
    /**
     * send场景信息to user
     * @param scenariosId
     * @param userId
     */
    public void sendToUserByScenariosId(String scenariosId,int userId) {
        int userIdTemp = userId;
//        scenario.getUserId();
        if( null != String.valueOf(userIdTemp) && "" != String.valueOf(userIdTemp)){
//            userIdTemp = userIdTemp + "," + userId;
//            scenario.setUserId(userIdTemp);
        }
        myScenariosMapper.sendToUserByScenariosId(scenariosId,userIdTemp);
    }

    /**
     *
     * @param openScenariosId
     * @return
     */
    public void updateOpenTime(String openScenariosId) {
        String s = DateTime.now().toString("yyyy-MM-dd HH:mm");
        myScenariosMapper.updateOpenTime(openScenariosId,s);

    }

    /**
     * 保存新场景类别
     * @param category
     */
    public void addCategory(ScenariosCategory category) {
        category.setCreateTime(DateTime.now().toString("yyyy-MM-dd HH:mm"));
        category.setUserId(ShiroUtil.getCurrentUserId());
        myScenariosMapper.addCategory(category);

    }

    /**
     * // 根据用户id查询场景类别  @return
     * param userId
     * @return
     */
    public List<ScenariosCategory> findCategory() {
        int userId = ShiroUtil.getCurrentUserId();
        return myScenariosMapper.findCategory(userId);
    }



}
