package com.xboost.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * Created by Administrator on 2017/11/11 0011.
 */
@Service
public class CascadeService {
    @Autowired

    public void saveNewfile(MultipartFile[] file) {
        //判断文件集合是否有文件
        if(file != null && file.length > 0) {
            for(MultipartFile multipartFile : file) {
                if(!multipartFile.isEmpty()) {
                    //解析文件并存储到数据库
                    /*String splitBy = ",";
                    String line;
                    try {
                        BufferedReader br = new BufferedReader(new FileReader(""));
                        while((line = br.readLine()) !=null){
                            String[] row = line.split(splitBy);
                            siteDistance.setSiteCollect(row[1]);
                            siteDistance.setSiteDelivery(row[2]);
                            siteDistance.setCarDistance(Float.parseFloat(row[3]));
                            siteDistance.setDurationNightDelivery(Integer.parseInt(row[4]));
                            siteDistance.setCreateTime(DateTime.now().toString("yyyy-MM-dd HH:mm"));
                        }
                        br.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }*/
                    //上传到文件服务器
                    String key = null;
//                    try {
//                        key = qiniuUtil.uploadFile(multipartFile.getInputStream());
//                    } catch (IOException e) {
//                        throw new RuntimeException("获取inputstream异常");
//                    }

                    //progressFile.setPath(key);
                }
            }
        }
    }
}
