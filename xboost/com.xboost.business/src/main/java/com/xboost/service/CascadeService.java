package com.xboost.service;

import com.xboost.pojo.Customer;
import com.xboost.pojo.ProgressFile;
import com.xboost.pojo.SiteDist;
import com.xboost.util.QiniuUtil;
import com.xboost.util.ShiroUtil;
import org.joda.time.DateTime;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.inject.Inject;
import javax.inject.Named;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.UUID;

/**
 * Created by Administrator on 2017/11/11 0011.
 */
@Named
@Transactional
public class CascadeService {
    @Inject
    private QiniuUtil qiniuUtil;

//    @Inject
//    private SiteDistance siteDistance;

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
                    try {
                        key = qiniuUtil.uploadFile(multipartFile.getInputStream());
                    } catch (IOException e) {
                        throw new RuntimeException("获取inputstream异常");
                    }

                    //progressFile.setPath(key);
                }
            }
        }
    }
}
