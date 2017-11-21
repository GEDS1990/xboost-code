package com.xboost.service;

import com.xboost.exception.ForbiddenException;
import com.xboost.exception.NotFoundException;
import com.xboost.mapper.SiteInfoMapper;
import com.xboost.pojo.SiteInfo;

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
                        fileTmp=new File("src/main/resources/upload/temp/"+tempTime+ ".xlsx");
                        if (!fileTmp.exists()) fileTmp.mkdirs();
                        multipartFile.transferTo(fileTmp);
//                      File fileTemp = (File) multipartFile;
                        ExcelUtil excelUtil = new ExcelUtil();
                        List<String> lineList = excelUtil.readExcel(fileTmp);
                        for(int i=0;i<lineList.size();i++){
                            String[] row = lineList.get(i).split("#");
                            //网点编码
                            siteInfo.setSiteCode(row[1]);
                            //网点经度
                            siteInfo.setSiteLongitude(row[2]);
                            //网点纬度
                            siteInfo.setSiteLatitude(row[3]);
                            //网点名称
                            siteInfo.setSiteName(row[4]);
                            //网点地址
                            siteInfo.setSiteAddress(row[5]);
                            //是否可以做夜配集散点
                            siteInfo.setSiteNightDelivery(null);
                            //网点面积
                            siteInfo.setSiteArea(row[6]);
                            //网点类型
                            siteInfo.setSiteType(row[7]);
                            //停货车数(辆)(10分钟停靠能力)
                            siteInfo.setCarNum(row[8]);
                            //进出最大车型(10分钟停靠能力)
                            siteInfo.setLargeCarModel(row[9]);
                            //单一批量操作处理量上限(以一个班次操作为单位)
                            siteInfo.setMaxOperateNum(row[10]);
                            siteInfo.setCreateTime(DateTime.now().toString("yyyy-MM-dd HH:mm"));


                            //insert
                            siteInfoMapper.save(siteInfo);
                            logger.info("insert into db:"+siteInfo.getSiteCode());
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
    public List<SiteInfo> findAllSiteInfo() {
        return siteInfoMapper.findAll();
    }

       /**
     * 获取网点的总数量
     * @return
     */
    public Integer findSiteInfoCount() {
        return siteInfoMapper.findSiteInfoCount().intValue();
    }

    /**
     * 根据网点名称查询网点信息
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

}
