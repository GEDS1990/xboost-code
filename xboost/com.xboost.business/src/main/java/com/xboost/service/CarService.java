package com.xboost.service;

import com.mckinsey.sf.data.Car;
import com.mckinsey.sf.data.TimeWindow;
import com.xboost.mapper.CarMapper;
import com.xboost.pojo.CarLicence;
import com.xboost.pojo.DemandInfo;
import com.xboost.util.*;
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

    public void saveCarLincence(CarLicence carLicence) {
        carLicence.setCreateTime(DateTime.now().toString("yyyy-MM-dd HH:mm"));
        transportMapper.saveCarLincence(carLicence);
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
                        CarLicence carLicence = new CarLicence();
                        for(int i=2;i<lineList.size();i++){
                            String[] row = lineList.get(i).split("#");
                            transport.setName(row[1]);
                            transport.setCarType(row[1]);
                            transport.setType(row[1]);
                            String type = transport.getType();
                            transport.setNum(row[2]);
                            Integer num = Integer.parseInt(transport.getNum());
                            transport.setCarSource(row[4]);
                            transport.setVelocity(Double.parseDouble((row[5].trim().equals(""))?"0":row[5].trim()));
                            transport.setVelocity2(Double.parseDouble((row[6].trim().equals(""))?"0":row[6].trim()));
                            transport.setVelocity3(Double.parseDouble((row[7].trim().equals(""))?"0":row[7].trim()));
                            transport.setMaxDistance(Float.parseFloat((row[9].trim().equals(""))?"0":row[9].trim()));
//                            transport.setCostPerDistance(Double.parseDouble((row[6].trim().equals(""))?"0":row[6].trim()));
//                            transport.setCostPerTime(Double.parseDouble((row[7].trim().equals(""))?"0":row[7].trim()));
                            transport.setDurationUnloadFull(row[12]);
//                            transport.setFixedCost(Double.parseDouble((row[9].trim().equals(""))?"0":row[9].trim()));
//                            transport.setFixedRound(Double.parseDouble((row[10].trim().equals(""))?"0":row[10].trim()));
//                            transport.setFixedRoundFee(Double.parseDouble((row[11].trim().equals(""))?"0":row[11].trim()));
                            transport.setMaxLoad(row[3]);
                            transport.setDimensions(row[11]);
                            transport.setMaxRunningTime(Double.parseDouble((row[10].trim().equals(""))?"0":row[10].trim()));
                            transport.setMaxStop(Integer.parseInt((row[8].trim().equals(""))?"0":row[8].trim()));
//                            transport.setSkills(row[0]);
                            transport.setStartLocation(row[13]);
                            transport.setEndLocation(row[14]);

                            transport.setA1(row[15]);
                            transport.setA2(row[16]);
                            transport.setCosta1(row[17]);
                            transport.setCosta2(row[18]);
                            transport.setCosta3(row[19]);

                            transport.setB1(row[20]);
                            transport.setB2(row[21]);
                            transport.setCostb1(row[22]);
                            transport.setCostb2(row[23]);
                            transport.setCostb3(row[24]);

                            transport.setC1(row[25]);
                            transport.setC2(row[26]);
                            transport.setCostc1(row[27]);
                            transport.setCostc2(row[28]);
                            transport.setCostc3(row[29]);

                            transport.setD1(row[30]);
                            transport.setD2(row[31]);
                            transport.setCostd1(row[32]);
                            transport.setCostd2(row[33]);
                            transport.setCostd3(row[34]);

                            transport.setE1(row[35]);
                            transport.setE2(row[36]);
                            transport.setCoste1(row[37]);
                            transport.setCoste2(row[38]);
                            transport.setCoste3(row[39]);

                            transport.setF1(row[40]);
                            transport.setF2(row[41]);
                            transport.setCostf1(row[42]);
                            transport.setCostf2(row[43]);
                            transport.setCostf3(row[44]);
                            transport.setBusyIdle("0");
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

                            for(int k=1;k<num+1;k++){
                        //        String name = type + k;
                                String name = Strings.getCarLicence("Y");
                                carLicence.setScenariosId(ShiroUtil.getOpenScenariosId());
                                carLicence.setName(name);
                                carLicence.setType(type);
                                carLicence.setBusyIdle("0");
                                carLicence.setCreateTime(DateTime.now().toString("yyyy-MM-dd HH:mm"));
                                transportMapper.saveCarLincence(carLicence);
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
    /**
     * 删除运力信息
     * @param scenariosId
     */
    public void delByScenariosId(String scenariosId) {

        transportMapper.delByScenariosId(scenariosId);
    }

    /**
     * 删除运力信息
     * @param scenariosId
     */
    public void delCarLincenceByScenariosId(String scenariosId) {

        transportMapper.delCarLincenceByScenariosId(scenariosId);
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

        XSSFSheet sheet = workBook.createSheet("Vehicles");
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
            //System.out.println(titles[i]);
        }
        // 构建表体数据
        if (list != null && list.size() > 0)
        {
            try{

                for (int j = 0; j < list.size(); j++)
                {
                    XSSFRow bodyRow = sheet.createRow(j + 1);
                    Car car = list.get(j);
                    int i = 1;
                    cell = bodyRow.createCell(0);
                    cell.setCellValue(car.getId());
                    cell.setCellStyle(bodyStyle);

                    cell = bodyRow.createCell(0+i);
                    cell.setCellValue(car.getType());
                    cell.setCellStyle(bodyStyle);


                    cell = bodyRow.createCell(1+i);
                    cell.setCellValue(car.getDimensions());
                    cell.setCellStyle(bodyStyle);

                    cell = bodyRow.createCell(2+i);
                    cell.setCellValue(Strings.isEmpty(car.getSkills().toString())?" ":car.getSkills().toString());
                    cell.setCellStyle(bodyStyle);

                    cell = bodyRow.createCell(3+i);
                    cell.setCellValue(car.getStartLocation());
                    cell.setCellStyle(bodyStyle);

                    cell = bodyRow.createCell(4+i);
                    cell.setCellValue(car.getEndLocation());
                    cell.setCellStyle(bodyStyle);

                    cell = bodyRow.createCell(5+i);
                    cell.setCellValue(car.getMaxDistance());
                    cell.setCellStyle(bodyStyle);

                    cell = bodyRow.createCell(6+i);
                    cell.setCellValue(car.getMaxRunningTime());
                    cell.setCellStyle(bodyStyle);

                    cell = bodyRow.createCell(7+i);
                    cell.setCellValue(car.getCostPerDistance());
                    cell.setCellStyle(bodyStyle);

                    cell = bodyRow.createCell(8+i);
                    cell.setCellValue(car.getFixedCost());
                    cell.setCellStyle(bodyStyle);

                    cell = bodyRow.createCell(9+i);
                    cell.setCellValue(car.getMaxStop());
                    cell.setCellStyle(bodyStyle);

                    cell = bodyRow.createCell(10+i);
                    cell.setCellValue(car.getVelocity());
                    cell.setCellStyle(bodyStyle);

                    cell = bodyRow.createCell(11+i);
                    cell.setCellValue(car.getFixedRound());
                    cell.setCellStyle(bodyStyle);

                    cell = bodyRow.createCell(12+i);
                    cell.setCellValue(car.getFixedRoundFee());
                    cell.setCellStyle(bodyStyle);

                    cell = bodyRow.createCell(13+i);
                    cell.setCellValue(car.getCarSource());
                    cell.setCellStyle(bodyStyle);

                    cell = bodyRow.createCell(14+i);
                    cell.setCellValue(car.getMaxLoad());
                    cell.setCellStyle(bodyStyle);

                    cell = bodyRow.createCell(15+i);
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
