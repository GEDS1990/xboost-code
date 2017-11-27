package com.xboost.service;

import com.mckinsey.sf.data.Car;
import com.xboost.mapper.CarMapper;
import com.xboost.mapper.TransportMapper;
import com.xboost.pojo.Transportation;
import com.xboost.util.ExcelUtil;
import com.xboost.util.QiniuUtil;
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
 * Created by Administrator on 2017/11/12 0012.
 */
@Named
@Transactional
public class CarService {
    @Inject
    private QiniuUtil qiniuUtil;

    @Inject
    CarMapper transportMapper;

    private Logger logger = LoggerFactory.getLogger(CarService.class);

    /**
     * 保存新运力信息
     * @param transport
     */
    public void saveCar(Car transport) {
        transport.setCreateTime(DateTime.now().toString("yyyy-MM-dd HH:mm"));

        transportMapper.save(transport);

    }

    //通过Excel新增运力信息
    public void addCarByExcel(Car transport,MultipartFile[] file) {
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
//                            transport.setCarType(row[0]);
//                            transport.setCarWeightLimit(row[1]);
//                            transport.setCarNum(Integer.parseInt(row[2]));
//                            transport.setCarSource(row[3]);
//                            transport.setSpeed(Float.parseFloat(row[4]));
//                            transport.setMaxLoad(Integer.parseInt(row[5]));
//                            transport.setDurationUnloadFull(Integer.parseInt(row[6]));
                            transport.setMaxDistance(Float.parseFloat(row[7]));
//                            transport.setCarCost1(Float.parseFloat(row[8]));
//                            transport.setCarCost2(Float.parseFloat(row[9]));
//                            transport.setCarCost3(Float.parseFloat(row[10]));
//                            transport.setSingleVoteCost1(Float.parseFloat(row[11]));
//                            transport.setSingleVoteCost2(Float.parseFloat(row[12]));
//                            transport.setSingleVoteCost3(Float.parseFloat(row[13]));
                            transport.setCreateTime(DateTime.now().toString("yyyy-MM-dd HH:mm"));
                            //insert
                            transportMapper.save(transport);
//                            logger.info("insert into db:"+transport.getCarType());
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
     * 查询所有运力信息
     * param
     * @return
     */
    public List<Car> findAll(String scenariosId) {
        return transportMapper.findAll(scenariosId);
    }

    /**
     * 获取运力信息的总数量
     * @return
     */
    public Integer findAllCount(String scenariosId) {
        return transportMapper.findAllCount(scenariosId).intValue();
    }

    /**
     * 根据参数查询运力信息
     * param param
     * @return
     */
    public List<Car> findByParam(Map<String, Object> param) {
        return transportMapper.findByParam(param);
    }

    /**
     * 根据查询条件获取运力信息的数量
     * @param param
     * @return
     */
    public Integer findCountByParam(Map<String, Object> param) {
        return transportMapper.findCountByParam(param).intValue();
    }


    /**
     * 根据用户的ID查询运力信息
     * @param id
     * @return
     */
    public Car findById(Integer id) {
        return transportMapper.findById(id);
    }


    /**
     * 编辑运力信息
     * @param transport
     */
    public void editTransport(Car transport) {

        transportMapper.editTransport(transport);
    }

    /**
     * 删除运力信息
     * @param id
     */
    public void delById(Integer id) {

        transportMapper.delById(id);
    }

    public Car[] findCarByParam(Map<String, Object> param) {
        return transportMapper.findCarByParam(param);
    }
}
