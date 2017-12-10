package com.xboost.service;

import com.mckinsey.sf.data.Car;
import com.mckinsey.sf.data.TimeWindow;
import com.xboost.mapper.CarMapper;
import com.xboost.pojo.DemandInfo;
import com.xboost.util.ExcelUtil;
import com.xboost.util.ExportUtil;
import com.xboost.util.QiniuUtil;
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
        transport.getTw().setCarId(Integer.parseInt(transport.getId()));//save时间窗口
        transportMapper.saveTimeWindow(transport.getTw());
    }

    /**
     * 保存TimeWindow
     * @param tw
     */
    public void saveTimeWindow(TimeWindow tw) {
//        tw.setCreateTime(DateTime.now().toString("yyyy-MM-dd HH:mm"));
        transportMapper.saveTimeWindow(tw);
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
                        fileTmp=new File(System.getProperty("user.dir")+"/temp/"+tempTime+ ".xlsx");
                        if (!fileTmp.exists()) fileTmp.mkdirs();
                        multipartFile.transferTo(fileTmp);
//                        File fileTemp = (File) multipartFile;
                        ExcelUtil excelUtil = new ExcelUtil();
                        List<String> lineList = excelUtil.readExcel(fileTmp);
                        for(int i=0;i<lineList.size();i++){
                            String[] row = lineList.get(i).split("#");
                            transport.setType(row[1]);
                            transport.setDimensions(row[2]);
                            transport.setCarSource(row[3]);
                            transport.setVelocity(Double.parseDouble(row[4]));
                            transport.setMaxDistance(Float.parseFloat(row[5]));
                            transport.setCostPerDistance(Double.parseDouble(row[6]));
                            transport.setCostPerTime(Double.parseDouble(row[7]));
                            transport.setDurationUnloadFull(row[8]);
                            transport.setFixedCost(Double.parseDouble(row[9]));
                            transport.setFixedRound(Double.parseDouble(row[10]));
                            transport.setFixedRoundFee(Double.parseDouble(row[11]));
                            transport.setMaxLoad(row[12]);
                            transport.setMaxRunningTime(Double.parseDouble(row[13]));
                            transport.setMaxStop(Integer.parseInt(row[14]));
//                            transport.setSkills(row[0]);
                            transport.setStartLocation(row[15]);
                            transport.setEndLocation(row[16]);
//                            transport.setTw(row[0]);

                            transport.setCreateTime(DateTime.now().toString("yyyy-MM-dd HH:mm"));
                            if(null==row[0] || ""==row[0] || " "==row[0] ){
                                transport.setId(row[0]);
                                //update
                                transportMapper.update(transport);
                            }else{
                                //insert
                                transportMapper.save(transport);
                                transport.getTw().setCarId(Integer.parseInt(transport.getId()));//save时间窗口
                                transportMapper.saveTimeWindow(transport.getTw());
//                            logger.info("insert into db:"+transport.getCarType());
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
        transportMapper.delTimeWindow(id);
    }

    public Car[] findCarByParam(Map<String, Object> param) {
        return transportMapper.findCarByParam(param);
    }


    /**
     * 导出excel
     */
    public void exportExcel(String scenariosId,String[] titles,ServletOutputStream outputStream )
    {
        List<Car> list = transportMapper.findAll(scenariosId);
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
                    Car car = list.get(j);

                    cell = bodyRow.createCell(0);
                    cell.setCellValue(car.getType());
                    cell.setCellStyle(bodyStyle);


                    cell = bodyRow.createCell(1);
                    cell.setCellValue(car.getDimensions());
                    cell.setCellStyle(bodyStyle);

                    cell = bodyRow.createCell(2);
                    cell.setCellValue(car.getSkills().toString());
                    cell.setCellStyle(bodyStyle);

                    cell = bodyRow.createCell(3);
                    cell.setCellValue(car.getStartLocation());
                    cell.setCellStyle(bodyStyle);

                    cell = bodyRow.createCell(4);
                    cell.setCellValue(car.getEndLocation());
                    cell.setCellStyle(bodyStyle);

                    cell = bodyRow.createCell(5);
                    cell.setCellValue(car.getMaxDistance());
                    cell.setCellStyle(bodyStyle);

                    cell = bodyRow.createCell(6);
                    cell.setCellValue(car.getMaxRunningTime());
                    cell.setCellStyle(bodyStyle);

                    cell = bodyRow.createCell(7);
                    cell.setCellValue(car.getCostPerDistance());
                    cell.setCellStyle(bodyStyle);

                    cell = bodyRow.createCell(8);
                    cell.setCellValue(car.getFixedCost());
                    cell.setCellStyle(bodyStyle);

                    cell = bodyRow.createCell(9);
                    cell.setCellValue(car.getMaxStop());
                    cell.setCellStyle(bodyStyle);

                    cell = bodyRow.createCell(10);
                    cell.setCellValue(car.getVelocity());
                    cell.setCellStyle(bodyStyle);

                    cell = bodyRow.createCell(11);
                    cell.setCellValue(car.getFixedRound());
                    cell.setCellStyle(bodyStyle);

                    cell = bodyRow.createCell(12);
                    cell.setCellValue(car.getFixedRoundFee());
                    cell.setCellStyle(bodyStyle);

                    cell = bodyRow.createCell(13);
                    cell.setCellValue(car.getCarSource());
                    cell.setCellStyle(bodyStyle);

                    cell = bodyRow.createCell(14);
                    cell.setCellValue(car.getMaxLoad());
                    cell.setCellStyle(bodyStyle);

                    cell = bodyRow.createCell(15);
                    cell.setCellValue(car.getDurationUnloadFull());
                    cell.setCellStyle(bodyStyle);
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

}
