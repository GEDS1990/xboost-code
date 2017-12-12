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
import javax.swing.table.TableRowSorter;
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
                        List<String> lineList = excelUtil.readExcel(fileTmp,2);
                        for(int i=2;i<lineList.size();i++){
                            String[] row = lineList.get(i).split("#");
                            transport.setType(row[1]);
                            transport.setDimensions(row[3]);
                            transport.setCarSource(row[4]);
                            transport.setVelocity(Double.parseDouble((row[5].trim().equals(""))?"0":row[5].trim()));
                            transport.setMaxDistance(Float.parseFloat((row[7].trim().equals(""))?"0":row[7].trim()));
//                            transport.setCostPerDistance(Double.parseDouble((row[6].trim().equals(""))?"0":row[6].trim()));
//                            transport.setCostPerTime(Double.parseDouble((row[7].trim().equals(""))?"0":row[7].trim()));
                            transport.setDurationUnloadFull(row[10]);
//                            transport.setFixedCost(Double.parseDouble((row[9].trim().equals(""))?"0":row[9].trim()));
//                            transport.setFixedRound(Double.parseDouble((row[10].trim().equals(""))?"0":row[10].trim()));
//                            transport.setFixedRoundFee(Double.parseDouble((row[11].trim().equals(""))?"0":row[11].trim()));
                            transport.setMaxLoad(row[3]);
                            transport.setMaxRunningTime(Double.parseDouble((row[8].trim().equals(""))?"0":row[8].trim()));
                            transport.setMaxStop(Integer.parseInt((row[7].trim().equals(""))?"0":row[7].trim()));
//                            transport.setSkills(row[0]);
                            transport.setStartLocation(row[12]);
                            transport.setEndLocation(row[13]);
                            transport.setA1(row[14]);
                            transport.setA2(row[15]);
                            transport.setCosta2(row[16]);
                            transport.setCosta1(row[17]);
                            transport.setB1(row[18]);
                            transport.setCostb1(row[19]);
                            transport.setB2(row[20]);
                            transport.setCostb2(row[21]);
                            transport.setC1(row[22]);
                            transport.setCostc1(row[23]);
                            transport.setC2(row[24]);
                            transport.setCostc2(row[25]);
                            transport.setD1(row[26]);
                            transport.setCostd1(row[27]);
                            transport.setD2(row[28]);
                            transport.setCostd2(row[29]);
                            transport.setE1(row[30]);
                            transport.setCoste1(row[31]);
                            transport.setE2(row[32]);
                            transport.setCoste2(row[33]);
                            transport.setF1(row[34]);
                            transport.setCostf1(row[35]);
                            transport.setF2(row[36]);
                            transport.setCostf2(row[37]);
//                            transport.setTw(row[0]);

                            if(null==row[0] || ""==row[0] || " "==row[0] || "NA".equals(row[0]) ){
                                //insert
                                transport.setCreateTime(DateTime.now().toString("yyyy-MM-dd HH:mm"));
                                transportMapper.save(transport);
                                transport.getTw().setCarId(Integer.parseInt(transport.getId()));//save时间窗口
                                transportMapper.saveTimeWindow(transport.getTw());
//                            logger.info("insert into db:"+transport.getCarType());
                            }else{
                                transport.setId(row[0]);
                                //update
                                transportMapper.update(transport);
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
