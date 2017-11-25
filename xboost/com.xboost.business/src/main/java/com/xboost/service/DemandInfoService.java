package com.xboost.service;

import com.xboost.mapper.DemandInfoMapper;
import com.xboost.pojo.DemandInfo;
import com.xboost.util.ExcelUtil;
import com.xboost.util.ShiroUtil;
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
import java.io.File;
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

    /**
     * 新增需求信息 
     * @param demandInfo
     */
    public void addDemandInfo(DemandInfo demandInfo) {
        demandInfo.setCreateTime(DateTime.now().toString("yyyy-MM-dd HH:mm"));

        demandInfoMapper.add(demandInfo);

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
                        fileTmp=new File("src/main/resources/upload/temp/"+tempTime+ ".xlsx");
                        if (!fileTmp.exists()) fileTmp.mkdirs();
                        multipartFile.transferTo(fileTmp);
//                      File fileTemp = (File) multipartFile;
                        ExcelUtil excelUtil = new ExcelUtil();
                        List<String> lineList = excelUtil.readExcel(fileTmp);
                        for(int i=0;i<lineList.size();i++){
                            String[] row = lineList.get(i).split("#");
                            //日期
                            demandInfo.setDate(row[0]);
                            //收件网点编码
                            demandInfo.setSiteCodeCollect(row[1]);
                            //派件网点编码
                            demandInfo.setSiteCodeDelivery(row[2]);
                            //产品类型
                            demandInfo.setProductType(row[3]);
                            //时段（开始）
                            demandInfo.setDurationStart(row[4]);
                            //时段(结束）
                            demandInfo.setDurationEnd(row[5]);
                            //重量（公斤）
                            demandInfo.setWeight(Float.parseFloat(row[6]));
                            //票数（票）
                            demandInfo.setVotes(Integer.parseInt(row[7]));
                            //时效要求(小时)
                            demandInfo.setAgeing(Integer.parseInt(row[8]));
                            demandInfo.setCreateTime(DateTime.now().toString("yyyy-MM-dd HH:mm"));

                            //insert
                            demandInfoMapper.add(demandInfo);
                            logger.info("insert into db:"+demandInfo.getDate());
                            logger.info("insert into db:"+demandInfo.getSiteCodeCollect());
                            logger.info("insert into db:"+demandInfo.getSiteCodeDelivery());
                            logger.info("insert into db:"+demandInfo.getProductType());
                            logger.info("insert into db:"+demandInfo.getDurationStart());
                            logger.info("insert into db:"+demandInfo.getDurationEnd());
                            logger.info("insert into db:"+demandInfo.getWeight());
                            logger.info("insert into db:"+demandInfo.getAgeing());

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

}
